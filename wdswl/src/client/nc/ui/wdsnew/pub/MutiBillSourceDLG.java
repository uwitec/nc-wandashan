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
 * ֧�ֱ�ͷ�����ѡ�Ĳ��նԻ���
 * 1��֧�ְ�BillTempletID������Դ����ģ�� 
 * 2��֧����Դ���ձ����ҳǩ��ʾ��Ҳ֧����Ϊ��ҳǩ��ʾ 
 * 3��֧�ֱ��岻��ʾ 
 * 4��֧�ֱ�ͷ��ѡ
 * 5��֧�ֱ��岻��ѡ�� 
 * 6��֧�ֱ��嵥ѡ��ǰ���Ǳ�ͷ��ѡ�������ѡ�� 
 * 7��֧�����˫���¼� 
 * 8��֧�ֱ�ͷ������ѡ���¼�ҵ����չ
 * 9��֧������ָ��BillVO���ɲ���pub_votable��ע�ᣩ 
 * 10��֧�ֱ�ͷ�����壨��ҳǩ������ComboBox
 * 11����ʼ��������Դ����ClientUI�ĳ�ʼ�����־��ȴ��� 
 * 12��֧�ְ���ͷԭ�ұ������ñ�ͷ�����壨��ҳǩ�����־��ȼ����ʾ���
 * 
 */
public abstract class MutiBillSourceDLG extends BillSourceDLG {
	/**
	 * 
	 */
	private static final long serialVersionUID = 66807432490855456L;




		// ��ť
	private JPanel ivjUIDialogContentPane1 = null;
	private UIButton ivjbtnAllSel = null;
	private UIButton ivjbtnAllCancelSel = null;
	private UIButton ivjbtnLink = null;

	// ��Դ���նԻ���
	private MutiBillSourceDLG m_sourcedlg = null;
	// ��ǰ��Ŀ�ģ�����UI
	protected BillManageUI m_currdlg = null;
	// ��Դ����UI�������������������־��ȵȣ�
	private BillManageUI m_clientUI = null;

	private boolean m_isBodySingle = false;
	private boolean m_isHasBodyTemplet = true;

	// ��Դ����ģ�����TableCode
	private String m_bodyDefaultTableCode = null;
	private String[] m_bodyTableCodes = null;
	// ��Դ����UI����TableCode
	private String[] m_clientUIBodyTableCodes = null;

	/**
	 * ������� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-6 ����10:32:49
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

			// ���嵥ѡ
			if (m_isBodySingle) {
				// �����ͷȫδѡ�л����ѡ�еĲ��Ǳ�ͷѡ���еģ�����ʾ
				if (headModel.isHasSelectRow()
						&& headModel.getRowState(iheadSelRow) != BillModel.SELECTED) {
					bodyModel.setRowState(ibodyRow, BillModel.UNSTATE);
					MessageDialog.showHintDlg(m_sourcedlg, "��ʾ",
							"���嵥ѡ����ѡ���ͷѡ���ж�Ӧ�ı����У�");
					headModel.addRowStateChangeEventListener(headListener);
					bodyModel.addRowStateChangeEventListener(bodyListener);
					return;
				}
			}

			// ���岻��ѡ
			if (!isBodyCanSelected()) {
				// ��ͷ��ѡ����
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
				// û�������ȫΪ����ѡ
				else {
					bodyModel.setRowState(ibodyRow, BillModel.UNSTATE);
				}
			}
			// �����ѡ
			else {
				if (e.isSelectState()) {

					// ��ͷ��ѡ�����л�ǰ����ͷ������ѡ���У���ǰͷ������г��⣩�����
					if (!isHeadCanMultiSelect())
						setAllRowSelStateExceptSelf(BillModel.UNSTATE);

					headModel.setRowState(iheadSelRow, BillModel.SELECTED);
					// ���嵥ѡ
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

			// ���������Ҫ��չ
			bodyRowStateChange(e);

			headModel.addRowStateChangeEventListener(headListener);
			bodyModel.addRowStateChangeEventListener(bodyListener);
			getbillListPanel().updateUI();
		}

	}

	/**
	 * ��ͷ���� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����11:50:48
	 */
	private class HeadRowStateListener implements
			IBillModelRowStateChangeEventListener {

		public void valueChanged(RowStateChangeEvent e) {

			int iheadRow = e.getRow();
			// ͷ�����л�ʱ�������Զ�ѡ���һ��ҳǩ--------------------------
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

			// ��ͷ��ѡ�����л�ǰ����ͷ������ѡ���ж����
			if (!isHeadCanMultiSelect())
				setAllRowSelState(BillModel.UNSTATE);

			if (iheadRow != getbillListPanel().getHeadTable().getSelectedRow()) {
				headRowChange(iheadRow);
			}

			// ��ͷ��ѡ
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

			// �������ѡ��
			if (e.isSelectState()) {
				// ���õ�ǰ����ģʽ. ---------------------------
				getbillListPanel().getChildListPanel(m_bodyDefaultTableCode)
						.setTableModel(getbillListPanel().getBodyBillModel());
				// -----------------------------------------------------------------------
				// ���Ǳ��嵥ѡ�ģ���ͷѡ��ȫѡ����
				if (!m_isBodySingle) {
					getbillListPanel().getBodyBillModel().setNeedCalculate(false); // �رպϼ�
					getbillListPanel()
							.getChildListPanel(m_bodyDefaultTableCode)
							.selectAllTableRow();
					getbillListPanel().getBodyBillModel().setNeedCalculate(true); // �����ϼ�
				}
			} else {
				getbillListPanel().getChildListPanel(m_bodyDefaultTableCode)
						.cancelSelectAllTableRow();
			}

			// ���������Ҫ��չ
			headRowStateChange(e);

			headModel.addRowStateChangeEventListener(headListener);
			bodyModel.addRowStateChangeEventListener(bodyListener);
			getbillListPanel().updateUI();
		}
	}

	/**
	 * HYUIBillSourceDLG ������ע��
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
	 * HYUIBillSourceDLG ������ע��
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
	 * ����ǧ��Ҫ��д����ñ��������������δ֪���󽫲����κ�����
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#addListenerEvent() ���ߣ�Ѧ��ƽ �������ڣ�2008-3-5
	 *      ����07:11:53
	 * @deprecated
	 */
	public void addListenerEvent() {

		super.addListenerEvent();

		// �������
		getbillListPanel().getHeadBillModel().addRowStateChangeEventListener(
				new HeadRowStateListener());
		getbillListPanel().getBodyBillModel().addRowStateChangeEventListener(
				new BodyRowStateListener());

//		for add mlr 
		// ȥ����ͷ/�����������
//		getbillListPanel().getHeadTable().removeSortListener();
//		getbillListPanel().getBodyTable().removeSortListener();
	}

	/**
	 * ����ɸ�д ����������ݼ���ǰҵ����չ ���ߣ�Ѧ��ƽ �������ڣ�2008-3-17 ����02:41:42
	 * 
	 * @param row
	 */
	protected CircularlyAccessibleValueObject[] beforeLoadBodyData(int headRow,
			CircularlyAccessibleValueObject[] bodyVos) {
		return bodyVos;
	}

	/**
	 * ����ɸ�д ��ͷ�������ݼ���ǰҵ����չ ���ߣ�Ѧ��ƽ �������ڣ�2008-3-17 ����02:44:34
	 */
	protected CircularlyAccessibleValueObject[] beforeLoadHeadData(
			CircularlyAccessibleValueObject[] headVos) {
		return headVos;
	}

	/**
	 * ����ɸ�д ���ߣ�Ѧ��ƽ �������ڣ�2008-3-6 ����08:19:00
	 * 
	 * @param e
	 */
	protected void bodyRowStateChange(RowStateChangeEvent e) {
	}

	/**
	 * 
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-19 ����10:44:57
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

		// ҳǩ����ȣ�ֱ�ӷ��أ�ע��ĿǰĬ��ҳǩ˳����ȣ�
		if (getTableCodes().length == getClientUITableCodes().length)
			return tmpAry;
		// ҳǩ�����ȣ����ˣ�ע��Ŀǰֻ֧����Դ����ģ�����ҳǩ����������Դ����UI�ģ�
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
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-10 ����01:30:43
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private BillTempletVO filterNoShowBody(BillTempletVO vo) throws Exception {

		// ����ģ���粻Clone�ᵼ����Դ���ݽڵ�ģ�����
		BillTempletVO retvo = (BillTempletVO) ObjectUtils.serializableClone(vo);

		BillTempletBodyVO[] bodyvos = (BillTempletBodyVO[]) retvo
				.getChildrenVO();
		if (bodyvos == null && bodyvos.length == 0)
			return retvo;

		// �ֳ���ͷ��β������
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

		// ���岻��ʾ��û�б��壬����
		if (!isBodyShow() || albody.size() == 0) {
			bodyvos = al.toArray(new BillTempletBodyVO[0]);
			retvo.setChildrenVO(bodyvos);
			m_isHasBodyTemplet = false;
			m_bodyTableCodes = null;
			return retvo;
		}

		// ��ҳǩ����
		bodyvos = albody.toArray(new BillTempletBodyVO[0]);
		HashMap hs = Hashlize.hashlizeVOs(bodyvos, new VOHashKeyAdapter(
				new String[] { "table_code" }));

		// ��ҳǩ��ֱ�ӷ���
		if (hs.size() == 1) {
			m_bodyTableCodes = new String[] { bodyvos[0].getTableCode() };
			return retvo;
		}

		// ��ҳǩ�����δ��дgetBodyOnlyShowTableCode()����֯TableCodes����
		if (getBodyOnlyShowTableCode() == null
				|| getBodyOnlyShowTableCode().trim().length() == 0) {
			ArrayList<String> ls = new ArrayList<String>();
			// ҳǩVO
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

		// //��ҳǩ
		// if (getBodyOnlyShowTableCode() == null ||
		// getBodyOnlyShowTableCode().trim().length() == 0)
		// throw new
		// BusinessException("��Դ���ݱ����ҳǩ�����븴дgetBodyShowTableCode()������");
		if (!hs.containsKey(getBodyOnlyShowTableCode()))
			throw new BusinessException(
					"��Դ���ݱ����ҳǩ�����ิдgetBodyShowTableCode()�������ص�ҳǩ�޷��ҵ���");

		albody = (ArrayList) hs.get(getBodyOnlyShowTableCode());
		al.addAll(albody);
		bodyvos = al.toArray(new BillTempletBodyVO[0]);
		retvo.setChildrenVO(bodyvos);
		m_bodyTableCodes = new String[] { getBodyOnlyShowTableCode() };

		return retvo;
	}

	/**
	 * ���಻Ҫ��д
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getbillListPanel() ���ߣ�Ѧ��ƽ �������ڣ�2008-3-6
	 *      ����07:18:25
	 * @return
	 */
	protected final BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				// �����ʾλ��ֵ

				// װ��ģ��
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

				// ���岻��ʾ������ҳǩ��������ʾ��ҳǩȥ����VO����
				vo = filterNoShowBody(vo);

				BillListData billDataVo = new BillListData(vo);

				// �����������ʾλ��--����Ĭ�Ͼ���
				String[][] tmpAry = null;
				if (getHeadShowNum() != null && getHeadShowNum().length > 0)
					tmpAry = getHeadShowNum();
				// else if(getClientUI()!=null){
				// tmpAry = getClientUI().getHeadShowNum();}
				if (tmpAry != null) {
					setVoDecimalDigitsHead(billDataVo, tmpAry);
				}
				// �����ӱ����ʾλ��--����Ĭ�Ͼ���
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

				// �������������е��ж�
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

				// ��ѡ
				// if (isHeadCanMultiSelect())
				ivjbillListPanel.setMultiSelect(true);
				// else
				// ivjbillListPanel.setMultiSelect(false);
				
				// ��ʼ�������Ҽ��˵��Ƿ���� edit by zhangws 2009/05/07
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

				// �ϼ�
				ivjbillListPanel.getParentListPanel().setTotalRowShow(true);
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	/**
	 * ����ɸ�д ����Զ�������Դ����ģ�壬������븴д ���ߣ�Ѧ��ƽ �������ڣ�2008-3-10 ����10:44:03
	 * 
	 * @return
	 */
	protected String getBillTempletID() {
		return null;
	}

	/**
	 * �������಻��д
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getBillVO() ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14
	 *      ����10:17:00
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
	 * ����ɸ�д ����δ��pub_votable��ע��ģ��븴д
	 * 
	 * @see nc.ui.pub.pf.BillSourceDLG#getBillVO() ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14
	 *      ����10:17:00
	 */
	protected String[] getBillVoName() {
		return null;
	}

	/**
	 * ����ɸ�д ���ñ���Ψһ��ʾ��ҳǩ������ҳǩ������ ��Դ���ݱ���Ϊ��ҳǩ�Ŀ��Ը�д��������������д��ȫ��ʾ����ֻ�е�һ��ҳǩ��ѡ��
	 * ��Դ���ݱ���Ϊ��ҳǩ�ĸ�дҲû�� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-7 ����12:50:59
	 * 
	 * @return
	 */
	protected String getBodyOnlyShowTableCode() {
		return null;
	}

	/**
	 * ����ɵ��� ȡ��ָ����ͷ�ж�Ӧ���崦��ѡ��״̬���� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����02:18:41
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
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:46:01
	 * 
	 * @return
	 */
	private UIButton getbtnAllCancelSel() {
		if (ivjbtnAllCancelSel == null) {
			ivjbtnAllCancelSel = new UIButton();
			ivjbtnAllCancelSel.setName("btnAllCancelSel");
			ivjbtnAllCancelSel.setText("ȫ��");

			if (!isHeadCanMultiSelect()) {
				ivjbtnAllCancelSel.setEnabled(false);
			}
		}
		return ivjbtnAllCancelSel;
	}

	/**
	 * 
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:46:05
	 * 
	 * @return
	 */
	private UIButton getbtnAllSel() {
		if (ivjbtnAllSel == null) {
			ivjbtnAllSel = new UIButton();
			ivjbtnAllSel.setName("AllSel");
			ivjbtnAllSel.setText("ȫѡ");

			if (!isHeadCanMultiSelect()) {
				ivjbtnAllSel.setEnabled(false);
			}
		}
		return ivjbtnAllSel;
	}

	/**
	 * 
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:46:09
	 * 
	 * @return
	 */
	private UIButton getbtnLink() {
		if (ivjbtnLink == null) {
			ivjbtnLink = new UIButton();
			ivjbtnLink.setName("btnLink");
			ivjbtnLink.setText("����");

			if (getClientUI() == null || !(getClientUI() instanceof ILinkQuery)) {
				ivjbtnLink.setEnabled(false);
			}
		}
		return ivjbtnLink;
	}

	/**
	 * ��������getbillListPanel()û���κζ����ϵĹ�ϵ ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����10:44:38
	 * 
	 * @return
	 */
	protected final BillManageUI getClientUI() {

		if (m_clientUI == null) {
			try {
				// ����ģ�����(UI)
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
				// "��������ע����δע��UI�࣡����"*/);

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
				System.out.println("��ʼ��ui��Ĺ��캯������");
			}
		}
		return m_clientUI;
	}

	/**
	 * ��Դ����UI����ҳǩ���루������ҳǩ���У�����ͷ����岻��ʾʱΪ�գ� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-19 ����11:03:03
	 * 
	 * @return
	 */
	public final String[] getClientUITableCodes() {
		return m_clientUIBodyTableCodes;
	}

	/**
	 * ����ɵ��� ȡ�ñ�ͷ����ѡ��״̬���� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����01:59:58
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
	 * ��Դ����ģ�����ҳǩ���루������ҳǩ���У�����ͷ����岻��ʾʱΪ�գ� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-18 ����08:54:58
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
	 * @see nc.ui.pub.pf.BillSourceDLG#getUIDialogContentPane() ���ߣ�Ѧ��ƽ
	 *      �������ڣ�2008-3-14 ����04:46:29
	 * @return
	 */
	protected JPanel getUIDialogContentPane() {

		if (ivjUIDialogContentPane1 == null) {
			UIPanel panel = (UIPanel) super.getUIDialogContentPane()
					.getComponent(0);
			int comcount = panel.getComponentCount();
//			if (comcount < 6) {//zpmע��
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
				/*****************************************zpmע�������鰴ť************************************************************/
//				panel.add(getbtnLink());

				// �Ӱ�ť����
				getbtnAllSel().addActionListener(this);
				getbtnAllCancelSel().addActionListener(this);
//				getbtnLink().addActionListener(this);//zpmע��
			}
			ivjUIDialogContentPane1 = super.getUIDialogContentPane();
		}
		return ivjUIDialogContentPane1;
	}

	/**
	 * ֻ�Ա�ͷ���д���
	 * <li>���л� �¼�
	 * <li>˫�� �¼�
	 * <li>WARN::���л��¼�������˫���¼�֮ǰ
	 * 
	 * @param iNewRow
	 */
	protected synchronized void headRowChange(int iNewRow) {
		if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,
				getpkField()) != null) {
			if (!getbillListPanel().setBodyModelData(iNewRow)) {
				// 1.���������������
				loadBodyData(iNewRow);
				// 2.���ݵ�ģ����
				getbillListPanel().setBodyModelDataCopy(iNewRow);
			}
		}
		getbillListPanel().repaint();
	}

	/**
	 * ����ɸ�д ���ߣ�Ѧ��ƽ �������ڣ�2008-3-6 ����08:20:09
	 * 
	 * @param e
	 */
	protected void headRowStateChange(RowStateChangeEvent e) {
	}

	/**
	 * ����ɵ��� ���ñ��������б� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-13 ����08:05:30
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
	 * ����ɵ��� ���ö�ҳǩ���������б� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-13 ����08:05:30
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
	 * ����ɸ�д ��ʼ�������б� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-10 ����10:03:58
	 */
	protected void initComboBox() {

		initHeadComboBox("vbillstatus", IBillStatus.strStateRemark, true);
	}

	/**
	 * ����ɵ��� ���ñ�ͷ�����б� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-13 ����08:05:27
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
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����01:42:29
	 */
	public void initialize() {

		m_sourcedlg = this;

		// ���ص��ݽ���
		getClientUI();

		// ����Ĭ��ҳǩ
		m_bodyDefaultTableCode = (getBodyOnlyShowTableCode() == null || getBodyOnlyShowTableCode()
				.trim().length() == 0) ? getbillListPanel().getChildListPanel()
				.getTableCode() : getBodyOnlyShowTableCode();

		// ���嵥ѡ��������ͷ��ѡ�������ѡʱ��ֵ��Ч
		m_isBodySingle = !isHeadCanMultiSelect() && isBodyCanSelected()
				&& !isBodyCanMultiSelect();

		// ��ʼ��ǧ��λ
		initShowThMark(true);

		// ��ʼ�������б�
		initComboBox();
	}

	/**
	 * ��ʼ��������ʾǧ��λ ���ߣ�Ѧ��ƽ �������ڣ�2008-3-18 ����08:52:22
	 * 
	 * @param isShow
	 */
	protected void initShowThMark(boolean isShow) {

		try {
			// �б��ͷ
			getbillListPanel().getParentListPanel().setShowThMark(isShow);

			// ����
			if (getTableCodes() != null && getTableCodes().length > 0) {
				for (int i = 0; i < getTableCodes().length; i++) {
					// �б����
					BillScrollPane bsp = getbillListPanel().getBodyScrollPane(
							getTableCodes()[i]);
					bsp.setShowThMark(isShow);
				}
			}
		} catch (Exception e) {
			// �б����Ϊ��ʱ�����׿�ָ���쳣
			System.out.println("ǧ��λ��ʼ��ʱ�����쳣�������Ǵ���");
			e.printStackTrace();
		}
	}

	/**
	 * ����ɸ�д �����Ƿ�֧�ֶ�ѡ
	 * Ĭ��Ϊtrue������isHeadCanMultiSelect()Ϊfalse��isBodyCanSelected()Ϊtrueʱ������
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����01:34:04
	 * 
	 * @return
	 */
	protected boolean isBodyCanMultiSelect() {
		return true;
	}

	/**
	 * ����ɸ�д �����Ƿ�֧��ѡ�� Ĭ��Ϊtrue
	 * 
	 * @see nc.ui.trade.billsource.HYUIBillSourceDLG#isBodyCanSelected() ���ߣ�Ѧ��ƽ
	 *      �������ڣ�2008-3-6 ����10:31:44
	 * @return
	 */
	protected boolean isBodyCanSelected() {
		return true;
	}

	/**
	 * ����ɸ�д �Ƿ���ʾ���� Ĭ��Ϊtrue ���ݽ�����ֻ�����ñ�ͷ���� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-13 ����01:05:43
	 * 
	 * @return
	 */
	protected boolean isBodyShow() {
		return true;
	}

	/**
	 * ����ɸ�д ��ͷ�Ƿ�֧�ֶ�ѡ Ĭ��Ϊtrue ���ߣ�Ѧ��ƽ �������ڣ�2008-3-6 ����10:31:22
	 * 
	 * @return
	 */
	protected boolean isHeadCanMultiSelect() {
		return true;
	}

	/**
	 * ���������ȡ�ӱ�����
	 * 
	 * @param row
	 *            ѡ�еı�ͷ��
	 */
	public void loadBodyData(int row) {
		try {
			// �������ID
			String id = getbillListPanel().getHeadBillModel().getValueAt(row,
					getpkField()).toString();
			// ��ѯ�ӱ�VO����
			CircularlyAccessibleValueObject[] tmpBodyVo = PfUtilBO_Client
					.queryBodyAllData(getBillType(), id, getBodyCondition());

			// ��ͷ�������ݼ���ǰҵ����չ
			tmpBodyVo = beforeLoadBodyData(row, tmpBodyVo);

			getbillListPanel().setBodyValueVO(tmpBodyVo);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}
	}
	public abstract String getHeadTableCode();
	/**
	 * @see nc.ui.pub.pf.BillSourceDLG#loadHeadData() ���ߣ�Ѧ��ƽ �������ڣ�2008-3-17
	 *      ����04:13:43
	 */
	public void loadHeadData() {
		try {
			// ���ò�Ʒ�鴫��������뵱ǰ��ѯ�������������������ѯ����
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

			// ��ͷ�������ݼ���ǰҵ����չ
//			tmpHeadVo = beforeLoadHeadData(tmpHeadVo);

			// ��������ԭ�ұ��֣����ñ�ͷ�����屾�һ��ʡ�ԭ�ҽ���
//			updateShowDigits(tmpHeadVo);////////////////////////////////////////////////////////////////////////////////////////////////

			getbillListPanel().setHeaderValueVO(tmpHeadVo);
			getbillListPanel().getHeadBillModel().execLoadFormula();

			// lj+ 2005-4-5
			// selectFirstHeadRow();
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			MessageDialog.showErrorDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000237")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000490")/* @res "���ݼ���ʧ�ܣ�" */);
		}
	}

	/**
	 * ȫ����ť�¼� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:49:00
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
	 * ȫѡ��ť�¼� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:48:58
	 */
	public void onAllSel() {

		int rowcount = getbillListPanel().getHeadBillModel().getRowCount();
		if (rowcount == 0)
			return;

		int selrow = getbillListPanel().getHeadTable().getSelectedRow();

		// ȫѡһ��Ҫ����ͷ����ȫѡ��һ�飬���֤��1��ѡ��
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
	 * ���鰴ť�¼� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-14 ����04:49:03
	 */
	public void onLink() {

		int selrow = getbillListPanel().getHeadTable().getSelectedRow();
		if (selrow < 0) {
			MessageDialog.showHintDlg(this, "��ʾ", "��ѡ����Ҫ����ĵ��ݣ�");
			return;
		}

		// �򿪶Ի���
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
	 * "ȷ��"��ť����Ӧ���ӽ����ȡ��ѡ����VO
	 */
	public void onOk() {
		int headRowCount = getbillListPanel().getHeadBillModel().getRowCount();
		if (headRowCount > 0) {
			boolean isHeadSelState = getbillListPanel().getHeadBillModel()
					.isHasSelectRow();
			if (!isHeadSelState) {
				MessageDialog.showErrorDlg(this, "����", "δѡ���κα�ͷ���ݣ�");
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
					MessageDialog.showErrorDlg(this, "����", "δѡ���κα������ݣ�");
					return;
				}
			}
		}else{
			MessageDialog.showErrorDlg(this, "����", "��ͷ�����ݣ�");
			return;
		}
		super.onOk();
	}
	
	/**
	 * 
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����02:31:54
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
	 * ���ߣ�Ѧ��ƽ �������ڣ�2008-3-9 ����02:31:54
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
	 * ���������� ���ߣ�Ѧ��ƽ �������ڣ�2008-3-13 ����08:09:14
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
