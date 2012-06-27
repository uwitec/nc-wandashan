package nc.ui.wdsnew.pub;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.sf.IFuncRegisterQueryService;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowAttribute;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.pf.BillSourceDLG;
import nc.ui.pub.pf.PfUtilBO_Client;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.bill.BillTempletVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.summarize.Hashlize;
import nc.vo.trade.summarize.VOHashKeyAdapter;

/**
 * mlr  
 * 支持表头表体多选的参照对话框
 * 1、支持按BillTempletID加载来源参照模板 
 * 2、支持来源参照表体多页签显示，也支持设为单页签显示 
 * 3、支持表体不显示 
 * 4、支持表头单选
 * 5、支持表体不可选中 
 * 6、支持表体单选（前提是表头单选、表体可选） 
 * 7、支持鼠标双击事件 
 * 8、支持表头、表体选中事件业务扩展
 * 9、支持子类指定BillVO（可不在pub_votable中注册） 
 * 10、支持表头、表体（多页签）设置ComboBox
 * 11、初始化调用来源单据ClientUI的初始化币种精度处理 
 * 12、支持按表头原币币种设置表头、表体（多页签）币种精度及汇率精度
 * 
 */
public abstract class MutiBillSourceDLG extends BillSourceDLG {
	/**
	 * 
	 */
	private static final long serialVersionUID = 66807432490855456L;




		// 按钮
	private JPanel ivjUIDialogContentPane1 = null;
	private UIButton ivjbtnAllSel = null;
	private UIButton ivjbtnAllCancelSel = null;
	private UIButton ivjbtnLink = null;

	// 来源参照对话框
	private MutiBillSourceDLG m_sourcedlg = null;
	// 当前（目的）单据UI
	protected BillManageUI m_currdlg = null;
	// 来源单据UI（用于设置下拉、币种精度等）
	private BillManageUI m_clientUI = null;

	private boolean m_isBodySingle = false;
	private boolean m_isHasBodyTemplet = true;

	// 来源参照模板表体TableCode
	private String m_bodyDefaultTableCode = null;
	private String[] m_bodyTableCodes = null;
	// 来源单据UI表体TableCode
	private String[] m_clientUIBodyTableCodes = null;

	/**
	 * 表体监听 作者：薛恩平 创建日期：2008-3-6 上午10:32:49
	 */
	private class BodyRowStateListener implements
			IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {

			int ibodyRow = e.getRow();
			int iheadSelRow = getbillListPanel().getHeadTable()
					.getSelectedRow();

			BillModel headModel = getbillListPanel().getHeadBillModel();
			IBillModelRowStateChangeEventListener headListener = headModel
					.getRowStateChangeEventListener();
			headModel.removeRowStateChangeEventListener();

			BillModel bodyModel = getbillListPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener bodyListener = bodyModel
					.getRowStateChangeEventListener();
			bodyModel.removeRowStateChangeEventListener();

			// 表体单选
			if (m_isBodySingle) {
				// 如果表头全未选中或表体选中的不是表头选中行的，则提示
				if (headModel.isHasSelectRow()
						&& headModel.getRowState(iheadSelRow) != BillModel.SELECTED) {
					bodyModel.setRowState(ibodyRow, BillModel.UNSTATE);
					MessageDialog.showHintDlg(m_sourcedlg, "提示",
							"表体单选，请选择表头选中行对应的表体行！");
					headModel.addRowStateChangeEventListener(headListener);
					bodyModel.addRowStateChangeEventListener(bodyListener);
					return;
				}
			}

			// 表体不可选
			if (!isBodyCanSelected()) {
				// 表头有选中行
				if (headModel.isHasSelectRow()) {
					if (iheadSelRow >= 0) {
						boolean isHeadSelected = headModel
								.getRowState(iheadSelRow) == BillModel.SELECTED ? true
								: false;
						if (isHeadSelected)
							bodyModel.setRowState(ibodyRow, BillModel.SELECTED);
						else
							bodyModel.setRowState(ibodyRow, BillModel.UNSTATE);
					}
				}
				// 没有则表体全为不可选
				else {
					bodyModel.setRowState(ibodyRow, BillModel.UNSTATE);
				}
			}
			// 表体可选
			else {
				if (e.isSelectState()) {

					// 表头单选，行切换前将表头、表体选中行（当前头及体各行除外）都清掉
					if (!isHeadCanMultiSelect())
						setAllRowSelStateExceptSelf(BillModel.UNSTATE);

					headModel.setRowState(iheadSelRow, BillModel.SELECTED);
					// 表体单选
					if (m_isBodySingle) {
						int bodyRowCount = bodyModel.getRowCount();
						for (int i = 0; i < bodyRowCount; i++) {
							if (i != ibodyRow) {
								bodyModel.setRowState(i, BillModel.UNSTATE);
							}
						}
					}
				} else {
					if (!bodyModel.isHasSelectRow())
						headModel.setRowState(iheadSelRow, BillModel.UNSTATE);
				}
			}

			// 子类根据需要扩展
			bodyRowStateChange(e);

			headModel.addRowStateChangeEventListener(headListener);
			bodyModel.addRowStateChangeEventListener(bodyListener);
			getbillListPanel().updateUI();
		}

	}

	/**
	 * 表头监听 作者：薛恩平 创建日期：2008-3-9 上午11:50:48
	 */
	private class HeadRowStateListener implements
			IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {

			int iheadRow = e.getRow();
			// 头部行切换时表体行自动选择第一个页签--------------------------
			getbillListPanel().getBodyTabbedPane().setSelectedIndex(0);
			// -------------------------------------------------
			BillModel headModel = getbillListPanel().getHeadBillModel();
			IBillModelRowStateChangeEventListener headListener = headModel
					.getRowStateChangeEventListener();
			headModel.removeRowStateChangeEventListener();

			BillModel bodyModel = getbillListPanel().getBodyBillModel();
			IBillModelRowStateChangeEventListener bodyListener = bodyModel
					.getRowStateChangeEventListener();
			bodyModel.removeRowStateChangeEventListener();

			// 表头单选，行切换前将表头、表体选中行都清掉
			if (!isHeadCanMultiSelect())
				setAllRowSelState(BillModel.UNSTATE);

			if (iheadRow != getbillListPanel().getHeadTable().getSelectedRow()) {
				headRowChange(iheadRow);
			}

			// 表头单选
			if (!isHeadCanMultiSelect()) {
				int rowCount = headModel.getRowCount();
				if (rowCount > 0) {
					if (e.isSelectState()) {
						for (int i = 0; i < rowCount; i++) {
							if (i == iheadRow) {
								headModel.setRowState(i, BillModel.SELECTED);
							}
						}
					}
				}
			}

			// 处理表体选中
			if (e.isSelectState()) {
				// 设置当前表体模式. ---------------------------
				getbillListPanel().getChildListPanel(m_bodyDefaultTableCode)
						.setTableModel(getbillListPanel().getBodyBillModel());
				// -----------------------------------------------------------------------
				// 不是表体单选的，表头选中全选表体
				if (!m_isBodySingle) {
					getbillListPanel().getBodyBillModel().setNeedCalculate(false); // 关闭合计
					getbillListPanel()
							.getChildListPanel(m_bodyDefaultTableCode)
							.selectAllTableRow();
					getbillListPanel().getBodyBillModel().setNeedCalculate(true); // 开启合计
				}
			} else {
				getbillListPanel().getChildListPanel(m_bodyDefaultTableCode)
						.cancelSelectAllTableRow();
			}

			// 子类根据需要扩展
			headRowStateChange(e);

			headModel.addRowStateChangeEventListener(headListener);
			bodyModel.addRowStateChangeEventListener(bodyListener);
			getbillListPanel().updateUI();
		}
	}

	/**
	 * HYUIBillSourceDLG 构造子注释
	 * 
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param parent
	 */
	public MutiBillSourceDLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);

		 m_currdlg = (BillManageUI)parent;

		initialize();
	}

	/**
	 * HYUIBillSourceDLG 构造子注释
	 * 
	 * @param pkField
	 * @param pkCorp
	 * @param operator
	 * @param funNode
	 * @param queryWhere
	 * @param billType
	 * @param businessType
	 * @param templateId
	 * @param currentBillType
	 * @param nodeKey
	 * @param userObj
	 * @param parent
	 */
	public MutiBillSourceDLG(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {

		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);

		 m_currdlg = (BillManageUI)parent;

		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see event.ActionListener#actionPerformed(event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		UIButton btn = (UIButton) e.getSource();
		if (btn.getName().equals(getbtnAllSel().getName())) {
			onAllSel();
		} else if (btn.getName().equals(getbtnAllCancelSel().getName())) {
			onAllCancelSel();
		} else if (btn.getName().equals(getbtnLink().getName())) {
			onLink();
		}
	}

	/**
	 * 子类千万不要复写或调用本方法，否则出现未知错误将不负任何责任
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#addListenerEvent() 作者：薛恩平 创建日期：2008-3-5
	 *      下午07:11:53
	 * @deprecated
	 */
	public void addListenerEvent() {

		super.addListenerEvent();

		// 表体监听
		getbillListPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());
		getbillListPanel().getBodyBillModel().addRowStateChangeEventListener(
				new BodyRowStateListener());

//		for add mlr 
		// 去掉表头/表体排序监听
//		getbillListPanel().getHeadTable().removeSortListener();
//		getbillListPanel().getBodyTable().removeSortListener();
	}

	/**
	 * 子类可复写 表体界面数据加载前业务扩展 作者：薛恩平 创建日期：2008-3-17 下午02:41:42
	 * 
	 * @param row
	 */
	protected CircularlyAccessibleValueObject[] beforeLoadBodyData(int headRow,
			CircularlyAccessibleValueObject[] bodyVos) {
		return bodyVos;
	}

	/**
	 * 子类可复写 表头界面数据加载前业务扩展 作者：薛恩平 创建日期：2008-3-17 下午02:44:34
	 */
	protected CircularlyAccessibleValueObject[] beforeLoadHeadData(
			CircularlyAccessibleValueObject[] headVos) {
		return headVos;
	}

	/**
	 * 子类可复写 作者：薛恩平 创建日期：2008-3-6 下午08:19:00
	 * 
	 * @param e
	 */
	protected void bodyRowStateChange(RowStateChangeEvent e) {
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-19 上午10:44:57
	 * 
	 * @param tmpAry
	 * @param tableCodes
	 * @param clientUITableCodes
	 * @return
	 */
	private String[][] filterBodyAryByClientUI(String[][] tmpAry,
			boolean isShowNum) {

		String[][] retAry = null;

		if (tmpAry == null || tmpAry.length == 0)
			return tmpAry;

		if (getTableCodes() == null || getTableCodes().length == 0
				|| getClientUITableCodes() == null
				|| getClientUITableCodes().length == 0)
			return tmpAry;

		// 页签数相等，直接返回（注：目前默认页签顺序相等）
		if (getTableCodes().length == getClientUITableCodes().length)
			return tmpAry;
		// 页签数不等，过滤（注：目前只支持来源参照模板表体页签数量少于来源单据UI的）
		else {
			ArrayList<String[]> ls = new ArrayList<String[]>();
			for (int i = 0; i < getTableCodes().length; i++) {
				for (int j = 0; j < getClientUITableCodes().length; j++) {
					if (getTableCodes()[i].equals(getClientUITableCodes()[j])) {
						if (!isShowNum) {
							ls.add(tmpAry[j]);
						} else {
							ls.add(tmpAry[j * 2]);
							ls.add(tmpAry[j * 2 + 1]);
						}
						break;
					}
				}
			}
			retAry = (String[][]) ls.toArray(new String[0][0]);
		}

		return retAry;
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-10 下午01:30:43
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private BillTempletVO filterNoShowBody(BillTempletVO vo) throws Exception {

		// 单据模板如不Clone会导致来源单据节点模板错误
		BillTempletVO retvo = (BillTempletVO) ObjectUtils.serializableClone(vo);

		BillTempletBodyVO[] bodyvos = (BillTempletBodyVO[]) retvo
				.getChildrenVO();
		if (bodyvos == null && bodyvos.length == 0)
			return retvo;

		// 分出表头表尾、表体
		ArrayList<BillTempletBodyVO> al = new ArrayList<BillTempletBodyVO>();
		ArrayList<BillTempletBodyVO> albody = new ArrayList<BillTempletBodyVO>();
		for (int i = 0; i < bodyvos.length; i++) {
			int pos = bodyvos[i].getPos().intValue();
			if (pos == 0 || pos == 2) {
				al.add((BillTempletBodyVO) bodyvos[i]);
			} else if (pos == 1) {
				albody.add((BillTempletBodyVO) bodyvos[i]);
			}
		}

		// 表体不显示或没有表体，返回
		if (!isBodyShow() || albody.size() == 0) {
			bodyvos = al.toArray(new BillTempletBodyVO[0]);
			retvo.setChildrenVO(bodyvos);
			m_isHasBodyTemplet = false;
			m_bodyTableCodes = null;
			return retvo;
		}

		// 按页签分组
		bodyvos = albody.toArray(new BillTempletBodyVO[0]);
		HashMap hs = Hashlize.hashlizeVOs(bodyvos, new VOHashKeyAdapter(
				new String[] { "table_code" }));

		// 单页签，直接返回
		if (hs.size() == 1) {
			m_bodyTableCodes = new String[] { bodyvos[0].getTableCode() };
			return retvo;
		}

		// 多页签，如果未复写getBodyOnlyShowTableCode()，组织TableCodes返回
		if (getBodyOnlyShowTableCode() == null
				|| getBodyOnlyShowTableCode().trim().length() == 0) {
			ArrayList<String> ls = new ArrayList<String>();
			// 页签VO
			BillTabVO[] tabvos = (BillTabVO[]) HYPubBO_Client
					.queryByCondition(
							BillTabVO.class,
							" pk_billtemplet = '"
									+ vo.getPKBillTemplet()
									+ "' and isnull(dr,0)=0 and pos = 1 order by tabindex");
			if (tabvos != null && tabvos.length > 0) {
				for (int i = 0; i < tabvos.length; i++) {
					if (!ls.contains(tabvos[i].getTabcode())) {
						ls.add(tabvos[i].getTabcode());
					}
				}
				m_bodyTableCodes = ls.toArray(new String[0]);
			}
			return retvo;
		}

		// //多页签
		// if (getBodyOnlyShowTableCode() == null ||
		// getBodyOnlyShowTableCode().trim().length() == 0)
		// throw new
		// BusinessException("来源单据表体多页签，必须复写getBodyShowTableCode()方法！");
		if (!hs.containsKey(getBodyOnlyShowTableCode()))
			throw new BusinessException(
					"来源单据表体多页签，子类复写getBodyShowTableCode()方法返回的页签无法找到！");

		albody = (ArrayList) hs.get(getBodyOnlyShowTableCode());
		al.addAll(albody);
		bodyvos = al.toArray(new BillTempletBodyVO[0]);
		retvo.setChildrenVO(bodyvos);
		m_bodyTableCodes = new String[] { getBodyOnlyShowTableCode() };

		return retvo;
	}

	/**
	 * 子类不要复写
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getbillListPanel() 作者：薛恩平 创建日期：2008-3-6
	 *      下午07:18:25
	 * @return
	 */
	protected final BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				// 获的显示位数值

				// 装载模板
				BillTempletVO vo = null;
				if (getBillTempletID() != null
						&& getBillTempletID().trim().length() > 0) {
					vo = ivjbillListPanel.getTempletData(getBillTempletID());
				} else {
					vo = ivjbillListPanel.getDefaultTemplet(getBillType(),
							null,
							/* getBusinessType(), */getOperator(),
							getPkCorp(), getNodeKey());
				}

				// 表体不显示或表体多页签，将不显示的页签去掉的VO过滤
				vo = filterNoShowBody(vo);

				BillListData billDataVo = new BillListData(vo);

				// 更改主表的显示位数--设置默认精度
				String[][] tmpAry = null;
				if (getHeadShowNum() != null && getHeadShowNum().length > 0)
					tmpAry = getHeadShowNum();
				// else if(getClientUI()!=null){
				// tmpAry = getClientUI().getHeadShowNum();}
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				// 更改子表的显示位数--设置默认精度
				if (getBodyShowNum() != null && getBodyShowNum().length > 0)
					tmpAry = getBodyShowNum();
				// else if(getClientUI()!=null)
				// tmpAry =
				// filterBodyAryByClientUI(getClientUI().getItemShowNum(),
				// true);
				if (tmpAry != null) {
					setVoDecimalDigitsBody(billDataVo, tmpAry);
				}

				ivjbillListPanel.setListData(billDataVo);

				// 进行主子隐藏列的判断
				if (getHeadHideCol() != null) {
					for (int i = 0; i < getHeadHideCol().length; i++) {
						ivjbillListPanel.hideHeadTableCol(getHeadHideCol()[i]);
					}
				}
				if (getBodyHideCol() != null) {
					for (int i = 0; i < getBodyHideCol().length; i++) {
						ivjbillListPanel.hideBodyTableCol(getBodyHideCol()[i]);
					}
				}

				// 多选
				// if (isHeadCanMultiSelect())
				ivjbillListPanel.setMultiSelect(true);
				// else
				// ivjbillListPanel.setMultiSelect(false);
				
				// 初始化表体右键菜单是否可用 edit by zhangws 2009/05/07
				if(!isBodyCanSelected()){
					if(ivjbillListPanel.getBillListData()!=null){
						String[] tblCodes = ivjbillListPanel.getBillListData().getBodyTableCodes();
						if(tblCodes!=null&&tblCodes.length>0){
							for(String code:tblCodes){
								BillScrollPane bsp = ivjbillListPanel.getBodyScrollPane(code);
								if(bsp!=null)
									bsp.setBBodyMenuShow(false);
							}
						}
					}
				}

				// 合计
				ivjbillListPanel.getParentListPanel().setTotalRowShow(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * 子类可复写 如果自定义了来源参照模板，子类必须复写 作者：薛恩平 创建日期：2008-3-10 上午10:44:03
	 * 
	 * @return
	 */
	protected String getBillTempletID() {
		return null;
	}

	/**
	 * 建议子类不复写
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getBillVO() 作者：薛恩平 创建日期：2008-3-14
	 *      上午10:17:00
	 */
	public void getBillVO() {
		if (getBillVoName() != null && getBillVoName().length > 0) {
			m_billVo = getBillVoName()[0];
			m_billHeadVo = getBillVoName()[1];
			m_billBodyVo = getBillVoName()[2];
		} else {
			super.getBillVO();
		}
	}

	/**
	 * 子类可复写 对于未在pub_votable中注册的，请复写
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getBillVO() 作者：薛恩平 创建日期：2008-3-14
	 *      上午10:17:00
	 */
	protected String[] getBillVoName() {
		return null;
	}

	/**
	 * 子类可复写 设置表体唯一显示的页签，其余页签将隐藏 来源单据表体为多页签的可以复写本方法，若不复写则全显示，但只有第一个页签可选中
	 * 来源单据表体为单页签的复写也没用 作者：薛恩平 创建日期：2008-3-7 下午12:50:59
	 * 
	 * @return
	 */
	protected String getBodyOnlyShowTableCode() {
		return null;
	}

	/**
	 * 子类可调用 取得指定表头行对应表体处于选中状态的行 作者：薛恩平 创建日期：2008-3-9 下午02:18:41
	 * 
	 * @param iHeadRow
	 * @return
	 */
	protected int[] getBodySelectedStateRows(int iHeadRow) {

		int[] ret = null;

		ArrayList<Integer> al = new ArrayList<Integer>();
		Hashtable h = (Hashtable) getbillListPanel().getHeadBillModel()
				.getRowAttributeObject(iHeadRow, "BILLLIST");
		if (h != null && !h.isEmpty()) {
			Vector v = (Vector) ((Vector) h.get(m_bodyDefaultTableCode)).get(0);
			for (int i = 0; i < v.size(); i++) {
				RowAttribute ra = (RowAttribute) v.get(i);
				if (ra.getRowState() == BillModel.SELECTED) {
					al.add(new Integer(i));
				}
			}
		}

		if (al.size() > 0) {
			ret = new int[al.size()];
			for (int i = 0; i < al.size(); i++) {
				ret[i] = al.get(i).intValue();
			}
		}

		return ret;
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-14 下午04:46:01
	 * 
	 * @return
	 */
	private UIButton getbtnAllCancelSel() {
		if (ivjbtnAllCancelSel == null) {
			ivjbtnAllCancelSel = new UIButton();
			ivjbtnAllCancelSel.setName("btnAllCancelSel");
			ivjbtnAllCancelSel.setText("全消");

			if (!isHeadCanMultiSelect()) {
				ivjbtnAllCancelSel.setEnabled(false);
			}
		}
		return ivjbtnAllCancelSel;
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-14 下午04:46:05
	 * 
	 * @return
	 */
	private UIButton getbtnAllSel() {
		if (ivjbtnAllSel == null) {
			ivjbtnAllSel = new UIButton();
			ivjbtnAllSel.setName("AllSel");
			ivjbtnAllSel.setText("全选");

			if (!isHeadCanMultiSelect()) {
				ivjbtnAllSel.setEnabled(false);
			}
		}
		return ivjbtnAllSel;
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-14 下午04:46:09
	 * 
	 * @return
	 */
	private UIButton getbtnLink() {
		if (ivjbtnLink == null) {
			ivjbtnLink = new UIButton();
			ivjbtnLink.setName("btnLink");
			ivjbtnLink.setText("联查");

			if (getClientUI() == null || !(getClientUI() instanceof ILinkQuery)) {
				ivjbtnLink.setEnabled(false);
			}
		}
		return ivjbtnLink;
	}

	/**
	 * 本方法与getbillListPanel()没有任何对象上的关系 作者：薛恩平 创建日期：2008-3-14 上午10:44:38
	 * 
	 * @return
	 */
	protected final BillManageUI getClientUI() {

		if (m_clientUI == null) {
			try {
				// 增加模版调用(UI)
				// BilltypeVO resultVO =
				// PfUIDataCache.getBillType(getBillType());
				// String nodecode = resultVO.getNodecode();

				IFuncRegisterQueryService iIFuncRegisterQueryService = (IFuncRegisterQueryService) NCLocator
						.getInstance().lookup(
								IFuncRegisterQueryService.class.getName());

				FuncRegisterVO[] vo = iIFuncRegisterQueryService
						.queryFuncWhere(" fun_code='" + getFunNode()
								+ "' and isnull(dr,0)=0 ");

				String uiClassName = vo[0].getClassName();
				// if (uiClassName != null && uiClassName.trim().length() > 3)
				// uiClassName = uiClassName.trim().substring(3);
				// else
				// throw new
				// Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000091")/*@res
				// "单据类型注册中未注册UI类！！！"*/);

				Class c = Class.forName(uiClassName);
				// Class[] ArgsClass = new Class[] { Boolean.class };
				// Object[] Arguments = new Object[] { new Boolean(true) };
				// Constructor ArgsConstructor = c.getConstructor();
				// Object retObj = (Object) ArgsConstructor.newInstance();
				Object retObj = (Object) c.newInstance();
				ToftPanel tp = (ToftPanel) retObj;
				if (tp instanceof BillManageUI) {
					m_clientUI = (BillManageUI) tp;

					if (tp instanceof MultiChildBillManageUI) {
						ArrayList<String> ls = new ArrayList<String>();
						for (int i = 0; i < m_clientUI.getBillListPanel()
								.getBodyTabbedPane().getComponentCount(); i++) {
							Component com = m_clientUI.getBillListPanel()
									.getBodyTabbedPane().getComponent(i);
							if (com instanceof BillScrollPane) {
								ls.add(((BillScrollPane) com).getTableCode());
							}
						}
						m_clientUIBodyTableCodes = ls.toArray(new String[0]);
					}
				}
			} catch (Throwable ivjExc) {
				System.out.println("初始化ui类的构造函数出错！");
			}
		}
		return m_clientUI;
	}

	/**
	 * 来源单据UI表体页签编码（单、多页签均有，单表头或表体不显示时为空） 作者：薛恩平 创建日期：2008-3-19 上午11:03:03
	 * 
	 * @return
	 */
	public final String[] getClientUITableCodes() {
		return m_clientUIBodyTableCodes;
	}

	/**
	 * 子类可调用 取得表头处于选中状态的行 作者：薛恩平 创建日期：2008-3-9 下午01:59:58
	 * 
	 * @return
	 */
	protected int[] getHeadSelectedStateRows() {

		int[] ret = null;

		ArrayList<Integer> al = new ArrayList<Integer>();
		BillModel headModel = getbillListPanel().getHeadBillModel();

		for (int i = 0; i < headModel.getRowCount(); i++) {
			if (headModel.getRowState(i) == BillModel.SELECTED) {
				al.add(new Integer(i));
			}
		}

		if (al.size() > 0) {
			ret = new int[al.size()];
			for (int i = 0; i < al.size(); i++) {
				ret[i] = al.get(i).intValue();
			}
		}

		return ret;
	}

	/**
	 * 来源参照模板表体页签编码（单、多页签均有，单表头或表体不显示时为空） 作者：薛恩平 创建日期：2008-3-18 上午08:54:58
	 * 
	 * @return
	 */
	public  String[] getTableCodes() {
		return m_bodyTableCodes;
	}
	
	public void setTableCodes(String[] bodyTableCodes){
		this.m_bodyTableCodes = bodyTableCodes;
	}

	/**
	 * @see nc.ui.pub.pf.BillSourceDLG#getUIDialogContentPane() 作者：薛恩平
	 *      创建日期：2008-3-14 下午04:46:29
	 * @return
	 */
	protected JPanel getUIDialogContentPane() {

		if (ivjUIDialogContentPane1 == null) {
			UIPanel panel = (UIPanel) super.getUIDialogContentPane()
					.getComponent(0);
			int comcount = panel.getComponentCount();
//			if (comcount < 6) {//zpm注销
			if (comcount < 5) {
				UIButton[] btns = new UIButton[comcount];
				for (int i = 0; i < comcount; i++) {
					btns[i] = (UIButton) panel.getComponent(i);
				}
				for (int i = comcount - 1; i >= 0; i--) {
					panel.remove(i);
				}

				panel.add(getbtnAllSel());
				panel.add(getbtnAllCancelSel());
				for (int i = 0; i < comcount; i++) {
					panel.add(btns[i]);
				}
				/*****************************************zpm注销掉联查按钮************************************************************/
//				panel.add(getbtnLink());

				// 加按钮监听
				getbtnAllSel().addActionListener(this);
				getbtnAllCancelSel().addActionListener(this);
//				getbtnLink().addActionListener(this);//zpm注销
			}
			ivjUIDialogContentPane1 = super.getUIDialogContentPane();
		}
		return ivjUIDialogContentPane1;
	}

	/**
	 * 只对表头进行处理
	 * <li>行切换 事件
	 * <li>双击 事件
	 * <li>WARN::行切换事件发生在双击事件之前
	 * 
	 * @param iNewRow
	 */
	protected synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,
				getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				// 1.初次载入表体数据
				loadBodyData(iNewRow);
				// 2.备份到模型中
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	/**
	 * 子类可复写 作者：薛恩平 创建日期：2008-3-6 下午08:20:09
	 * 
	 * @param e
	 */
	protected void headRowStateChange(RowStateChangeEvent e) {
	}

	/**
	 * 子类可调用 设置表体下拉列表 作者：薛恩平 创建日期：2008-3-13 下午08:05:30
	 * 
	 * @param itemkey
	 * @param values
	 * @param isWhithIndex
	 */
	protected void initBodyComboBox(String itemkey, Object[] values,
			boolean isWhithIndex) {
		BillItem item = getbillListPanel().getBodyItem(itemkey);
		if (item != null)
			setComboBox(item, values, isWhithIndex);
	}

	/**
	 * 子类可调用 设置多页签表体下拉列表 作者：薛恩平 创建日期：2008-3-13 下午08:05:30
	 * 
	 * @param itemkey
	 * @param values
	 * @param isWhithIndex
	 */
	protected void initBodyComboBox(String tablecode, String itemkey,
			Object[] values, boolean isWhithIndex) {
		BillItem item = getbillListPanel().getBodyItem(tablecode, itemkey);
		if (item != null)
			setComboBox(item, values, isWhithIndex);
	}

	/**
	 * 子类可复写 初始化下拉列表 作者：薛恩平 创建日期：2008-3-10 上午10:03:58
	 */
	protected void initComboBox() {

		initHeadComboBox("vbillstatus", IBillStatus.strStateRemark, true);
	}

	/**
	 * 子类可调用 设置表头下拉列表 作者：薛恩平 创建日期：2008-3-13 下午08:05:27
	 * 
	 * @param itemkey
	 * @param values
	 * @param isWhithIndex
	 */
	protected void initHeadComboBox(String itemkey, Object[] values,
			boolean isWhithIndex) {
		BillItem item = getbillListPanel().getHeadItem(itemkey);
		if (item != null)
			setComboBox(item, values, isWhithIndex);
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-9 下午01:42:29
	 */
	public void initialize() {

		m_sourcedlg = this;

		// 加载单据界面
		getClientUI();

		// 表体默认页签
		m_bodyDefaultTableCode = (getBodyOnlyShowTableCode() == null || getBodyOnlyShowTableCode()
				.trim().length() == 0) ? getbillListPanel().getChildListPanel()
				.getTableCode() : getBodyOnlyShowTableCode();

		// 表体单选，仅当表头单选、表体可选时该值有效
		m_isBodySingle = !isHeadCanMultiSelect() && isBodyCanSelected()
				&& !isBodyCanMultiSelect();

		// 初始化千分位
		initShowThMark(true);

		// 初始化下拉列表
		initComboBox();
	}

	/**
	 * 初始化设置显示千分位 作者：薛恩平 创建日期：2008-3-18 上午08:52:22
	 * 
	 * @param isShow
	 */
	protected void initShowThMark(boolean isShow) {

		try {
			// 列表表头
			getbillListPanel().getParentListPanel().setShowThMark(isShow);

			// 表体
			if (getTableCodes() != null && getTableCodes().length > 0) {
				for (int i = 0; i < getTableCodes().length; i++) {
					// 列表表体
					BillScrollPane bsp = getbillListPanel().getBodyScrollPane(
							getTableCodes()[i]);
					bsp.setShowThMark(isShow);
				}
			}
		} catch (Exception e) {
			// 列表表体为空时，会抛空指针异常
			System.out.println("千分位初始化时出现异常，但不是错误");
			e.printStackTrace();
		}
	}

	/**
	 * 子类可复写 表体是否支持多选
	 * 默认为true，仅当isHeadCanMultiSelect()为false且isBodyCanSelected()为true时起作用
	 * 作者：薛恩平 创建日期：2008-3-9 下午01:34:04
	 * 
	 * @return
	 */
	protected boolean isBodyCanMultiSelect() {
		return true;
	}

	/**
	 * 子类可复写 表体是否支持选中 默认为true
	 * 
	 * @see nc.ui.trade.billsource.HYUIBillSourceDLG#isBodyCanSelected() 作者：薛恩平
	 *      创建日期：2008-3-6 上午10:31:44
	 * @return
	 */
	protected boolean isBodyCanSelected() {
		return true;
	}

	/**
	 * 子类可复写 是否显示表体 默认为true 数据交换中只能配置表头数据 作者：薛恩平 创建日期：2008-3-13 下午01:05:43
	 * 
	 * @return
	 */
	protected boolean isBodyShow() {
		return true;
	}

	/**
	 * 子类可复写 表头是否支持多选 默认为true 作者：薛恩平 创建日期：2008-3-6 上午10:31:22
	 * 
	 * @return
	 */
	protected boolean isHeadCanMultiSelect() {
		return true;
	}

	/**
	 * 根据主表获取子表数据
	 * 
	 * @param row
	 *            选中的表头行
	 */
	public void loadBodyData(int row) {
		try {
			// 获得主表ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row,
					getpkField()).toString();
			// 查询子表VO数组
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client
					.queryBodyAllData(getBillType(), id, getBodyCondition());

			// 表头界面数据加载前业务扩展
			tmpBodyVo = beforeLoadBodyData(row, tmpBodyVo);

			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}
	public abstract String getHeadTableCode();
	/**
	 * @see nc.ui.pub.pf.BillSourceDLG#loadHeadData() 作者：薛恩平 创建日期：2008-3-17
	 *      下午04:13:43
	 */
	public void loadHeadData() {
		try {
			// 利用产品组传入的条件与当前查询条件获得条件组成主表查询条件
			String tmpWhere = null;
			if (getHeadCondition() != null) {
				if (m_whereStr == null) {
					tmpWhere = " (" + getHeadCondition() + ")";
				} else {
					tmpWhere = " (" + m_whereStr + ") and ("
							+ getHeadCondition() + ")";
				}
			} else {
				tmpWhere = m_whereStr;
			}
			String businessType = null;
			if (getIsBusinessType()) {
				businessType = getBusinessType();
			}
			SuperVO[] tmpHeadVo = HYPubBO_Client.queryByCondition(Class.forName(getHeadTableCode()), tmpWhere);
//			CircularlyAccessibleValueObject[] tmpHeadVo = PfUtilBO_Client.
//					queryHeadAllData(getBillType(), businessType, tmpWhere);

			// 表头界面数据加载前业务扩展
//			tmpHeadVo = beforeLoadHeadData(tmpHeadVo);

			// 根据主表原币币种，设置表头、表体本币汇率、原币金额精度
//			updateShowDigits(tmpHeadVo);////////////////////////////////////////////////////////////////////////////////////////////////

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			// lj+ 2005-4-5
			// selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000237")/* @res "错误" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000490")/* @res "数据加载失败！" */);
		}
	}

	/**
	 * 全消按钮事件 作者：薛恩平 创建日期：2008-3-14 下午04:49:00
	 */
	public void onAllCancelSel() {

		int rowcount = getbillListPanel().getHeadBillModel().getRowCount();
		if (rowcount == 0)
			return;

		BillModel headModel = getbillListPanel().getHeadBillModel();
		IBillModelRowStateChangeEventListener headListener = headModel
				.getRowStateChangeEventListener();
		headModel.removeRowStateChangeEventListener();

		BillModel bodyModel = getbillListPanel().getBodyBillModel();
		IBillModelRowStateChangeEventListener bodyListener = bodyModel
				.getRowStateChangeEventListener();
		bodyModel.removeRowStateChangeEventListener();

		rowcount = headModel.getRowCount();
		if (rowcount > 0) {
			for (int i = 0; i < rowcount; i++) {
				setAllRowSelState(BillModel.UNSTATE);
			}
		}

		headModel.addRowStateChangeEventListener(headListener);
		bodyModel.addRowStateChangeEventListener(bodyListener);
		getbillListPanel().updateUI();
	}

	/**
	 * 全选按钮事件 作者：薛恩平 创建日期：2008-3-14 下午04:48:58
	 */
	public void onAllSel() {

		int rowcount = getbillListPanel().getHeadBillModel().getRowCount();
		if (rowcount == 0)
			return;

		int selrow = getbillListPanel().getHeadTable().getSelectedRow();

		// 全选一定要将表头各行全选择一遍，最后保证第1行选择
		for (int i = rowcount - 1; i >= 0; i--) {
			getbillListPanel().getHeadTable().setRowSelectionInterval(0, i);
		}

		BillModel headModel = getbillListPanel().getHeadBillModel();
		IBillModelRowStateChangeEventListener headListener = headModel
				.getRowStateChangeEventListener();
		headModel.removeRowStateChangeEventListener();

		BillModel bodyModel = getbillListPanel().getBodyBillModel();
		IBillModelRowStateChangeEventListener bodyListener = bodyModel
				.getRowStateChangeEventListener();
		bodyModel.removeRowStateChangeEventListener();

		rowcount = headModel.getRowCount();
		if (rowcount > 0) {
			getbillListPanel().getHeadTable();
			for (int i = 0; i < rowcount; i++) {
				setAllRowSelState(BillModel.SELECTED);
			}
		}

		if (selrow > 0)
			getbillListPanel().getHeadTable().setRowSelectionInterval(selrow,
					selrow);

		headModel.addRowStateChangeEventListener(headListener);
		bodyModel.addRowStateChangeEventListener(bodyListener);
		getbillListPanel().updateUI();
	}

	/**
	 * 联查按钮事件 作者：薛恩平 创建日期：2008-3-14 下午04:49:03
	 */
	public void onLink() {

		int selrow = getbillListPanel().getHeadTable().getSelectedRow();
		if (selrow < 0) {
			MessageDialog.showHintDlg(this, "提示", "请选择需要联查的单据！");
			return;
		}

		// 打开对话框
		SFClientUtil.openLinkedQueryDialog(m_sourcedlg.getFunNode(), this,
				new ILinkQueryData() {

					public String getBillID() {
						SuperVO vo = null;
						try {
							vo = (SuperVO) Class.forName(m_billHeadVo)
									.newInstance();
						} catch (Exception e) {
							e.printStackTrace();
						}
						String pkField = vo.getPKFieldName();
						int selrow = getbillListPanel().getHeadTable()
								.getSelectedRow();
						return (String) getbillListPanel().getHeadBillModel()
								.getValueAt(selrow, pkField);
					}

					public String getBillType() {
						return m_sourcedlg.getBillType();
					}

					public String getPkOrg() {
						return m_sourcedlg.getPkCorp();
					}

					public Object getUserObject() {
						return null;
					}
				});
	}

	/**
	 * "确定"按钮的响应，从界面获取被选单据VO
	 */
	public void onOk() {
		int headRowCount = getbillListPanel().getHeadBillModel().getRowCount();
		if (headRowCount > 0) {
			boolean isHeadSelState = getbillListPanel().getHeadBillModel()
					.isHasSelectRow();
			if (!isHeadSelState) {
				MessageDialog.showErrorDlg(this, "错误", "未选中任何表头数据！");
				return;
			}

			getbillListPanel().getBodyBillModel();
			boolean isBodySelState = false;
			for (int i = 0; i < headRowCount; i++) {
				if (getbillListPanel().getHeadBillModel().getRowState(i) == BillModel.SELECTED
						&& getBodySelectedStateRows(i) != null) {
					isBodySelState = true;
					break;
				}
			}
			if (m_isHasBodyTemplet && !isBodySelState) {
				if (isBodyCanSelected()) {
					MessageDialog.showErrorDlg(this, "错误", "未选中任何表体数据！");
					return;
				}
			}
		}else{
			MessageDialog.showErrorDlg(this, "错误", "表头无数据！");
			return;
		}
		super.onOk();
	}
	
	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-9 下午02:31:54
	 * 
	 * @param rowState
	 */
	protected void setAllRowSelState(int selState) {
		int rowCount = getbillListPanel().getHeadTable().getRowCount();
		if (rowCount > 0) {
			for (int i = 0; i < rowCount; i++) {
				getbillListPanel().getHeadBillModel().setRowState(i, selState);
				Hashtable h = (Hashtable) getbillListPanel().getHeadBillModel()
						.getRowAttributeObject(i, "BILLLIST");
				if (h != null && !h.isEmpty()) {
					Vector v = (Vector) ((Vector) h.get(m_bodyDefaultTableCode))
							.get(0);
					for (int j = 0; j < v.size(); j++) {
						RowAttribute ra = (RowAttribute) v.get(j);
						ra.setRowState(selState);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 作者：薛恩平 创建日期：2008-3-9 下午02:31:54
	 * 
	 * @param rowState
	 */
	protected void setAllRowSelStateExceptSelf(int selState) {
		int rowCount = getbillListPanel().getHeadTable().getRowCount();
		int currHeadRow = getbillListPanel().getHeadTable().getSelectedRow();
		if (rowCount > 0) {
			for (int i = 0; i < rowCount; i++) {
				if (i == currHeadRow)
					continue;

				getbillListPanel().getHeadBillModel().setRowState(i, selState);
				Hashtable h = (Hashtable) getbillListPanel().getHeadBillModel()
						.getRowAttributeObject(i, "BILLLIST");
				if (h != null && !h.isEmpty()) {
					Vector v = (Vector) ((Vector) h.get(m_bodyDefaultTableCode))
							.get(0);
					for (int j = 0; j < v.size(); j++) {
						RowAttribute ra = (RowAttribute) v.get(j);
						ra.setRowState(selState);
					}
				}
			}
		}
	}

	/**
	 * 设置下拉框 作者：薛恩平 创建日期：2008-3-13 下午08:09:14
	 * 
	 * @param billItem
	 * @param values
	 * @param isWhithIndex
	 */
	private void setComboBox(BillItem billItem, Object[] values,
			boolean isWhithIndex) {

		if (billItem != null && billItem.getDataType() == BillItem.COMBO
				&& values != null && values.length > 0) {
			billItem.setWithIndex(isWhithIndex);
			UIComboBox cmb = (UIComboBox) billItem.getComponent();
			cmb.removeAllItems();
			for (int i = 0; i < values.length; i++) {
				cmb.addItem(values[i]);
			}
		}
	}
	
}
