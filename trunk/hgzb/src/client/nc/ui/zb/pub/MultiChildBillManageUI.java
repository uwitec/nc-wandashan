package nc.ui.zb.pub;

import java.util.ArrayList;

import javax.swing.Action;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.action.BillViewMaxAction;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.vo.bd.def.DefVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 多子表UI,加自定义项
 * 
 */
public abstract class MultiChildBillManageUI extends nc.ui.trade.multichild.MultiChildBillManageUI implements ILinkQuery,IMultiChild {
	
	private DefVO[] m_defBody = null;

	private DefVO[] m_defHead = null;
	
	private Class bodyVOClass;
	
	private Class aggBillVOClass;
	
	private Class bodyVO1Class;

	public MultiChildBillManageUI() {
		super();
		initialize();
	}

	private void initialize() {
		// 初始化设置显示千分位
		initShowThMark(true);

		// 表头表体最大化
		ArrayList<Action> actsBody = new ArrayList<Action>();
		ArrayList<Action> actsHead = new ArrayList<Action>();
		//
		actsBody.add(new BillViewMaxAction(getBillCardPanel(), IBillItem.BODY));
		actsHead.add(new BillViewMaxAction(getBillCardPanel(), IBillItem.HEAD));

		getBillCardPanel().addTabAction(IBillItem.BODY, actsBody);
		getBillCardPanel().addTabAction(IBillItem.HEAD, actsHead);

	}

	public MultiChildBillManageUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public MultiChildBillManageUI(Boolean useBillSource) {
		super();
		// 初始化设置显示千分位
		initShowThMark(true);
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)throws Exception {
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
	}

	@Override
	protected void initSelfData() {
	}

	@Override
	public void setDefaultData() throws Exception {

	}
	/**
	 * 联查动作
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		try {
			if(querydata == null)
				return;
			String id = querydata.getBillID();
			if(id == null || "".equals(id))
				return;
			//查询主表数据
			SuperVO headvo = (SuperVO)getBodyB1Class().newInstance();
			//查询主表数据
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getBodyB1Class(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");

			//查询子表数据
			if(queryVos != null && queryVos.length > 0){
					setCurrentPanel(BillTemplateWrapper.CARDPANEL);
					AggregatedValueObject aggvo = (AggregatedValueObject)getAggVOClass().newInstance();
					aggvo.setParentVO(queryVos[0]);
					getBusiDelegator().setChildData(
								aggvo,
								getBodyVOClass(),
								getUIControl().getBillType(),
								queryVos[0].getPrimaryKey(),null);
					getBufferData().addVOToBuffer(aggvo);
					getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
			//
			ButtonObject[] btns = getButtons();
			for (ButtonObject btn : btns) {
				if (("" + (IBillButton.Card)).equals(btn.getTag()) || ("" + (IBillButton.Return)).equals(btn.getTag())) {
					btn.setEnabled(true);
					btn.setVisible(true);
				} else {
					btn.setEnabled(false);
					btn.setVisible(false);
				}
			}
			updateButtons();
		}  catch (Exception e) {
			Logger.error(e);
		}
		//
	}
	private Class getBodyVOClass() throws Exception{
		if(bodyVOClass==null)
			bodyVOClass = Class.forName(getUIControl().getBillVoName()[2]);
		return bodyVOClass;
	}
	
	private Class getAggVOClass()  throws Exception{
		if(aggBillVOClass == null)
				aggBillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			return aggBillVOClass;
	}
	private Class getBodyB1Class()  throws Exception{
		if( bodyVO1Class == null)
			bodyVO1Class = Class.forName(getUIControl().getBillVoName()[1]);
			return bodyVO1Class ;
	}
	/**
	 * 千分位处理
	 */
	protected void initShowThMark(boolean isShow) {
		try {
			// 卡片表头
			getBillCardPanel().setHeadTailShowThMark(getBillCardPanel().getHeadItems(), isShow);
			// 列表表头
			getBillListPanel().getParentListPanel().setShowThMark(isShow);
			// 表体
			if (getTableCodes() != null && getTableCodes().length > 0) {
				for (int i = 0; i < getTableCodes().length; i++) {
					if (getBillListPanel().getBodyScrollPane(getTableCodes()[i]) != null) {
						// 列表表体
						getBillListPanel().getBodyScrollPane(getTableCodes()[i]).setShowThMark(isShow);
						// 卡片表体
						getBillCardPanel().setShowThMark(getTableCodes()[i],isShow);
					}
				}
			}
		} catch (Exception e) {
			// 列表表体为空时，会抛空指针异常
			Logger.debug("千分位初始化时出现异常，但不是错误");
		}
	}

	public String[] getTableCodes() {
		return null;
	}



	@Override
	public void afterEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		if (sItemKey.startsWith("vdef")) {
			// 自定义项编辑后事件
			afterVuserDefEdit(e);
		}
		super.afterEdit(e);
	}

	@Override
	protected BillListPanelWrapper createBillListPanelWrapper()
			throws Exception {
		BillListPanelWrapper list = super.createBillListPanelWrapper();
		BillListPanel billist = list.getBillListPanel();
		BillListData bd = billist.getBillListData();
		if (bd != null) {
			// 修改自定义项
			bd = changeBillListDataByUserDef(getDefHeadVO(), getDefBodyVO(), bd);
			billist.setListData(bd);
		}
		return list;
	}

	@Override
	protected BillCardPanelWrapper createBillCardPanelWrapper()
			throws Exception {
		BillCardPanelWrapper card = super.createBillCardPanelWrapper();
		BillCardPanel cardPanel = card.getBillCardPanel();
		BillData billdate = cardPanel.getBillData();
		if (billdate != null) {
			// 修改自定义项
			billdate = changeBillDataByUserDef(getDefHeadVO(), getDefBodyVO(),
					billdate);
			cardPanel.setBillData(billdate);
		}
		return card;
	}

	/**
	 * 自定义项定义(卡片状态)
	 * 
	 * @param defHead
	 * @param defBody
	 * @param oldBillData
	 * @return
	 */
	protected BillData changeBillDataByUserDef(DefVO[] defHead,
			DefVO[] defBody, BillData oldBillData) {
		try {
			// 进行自定义项定义用
			if (defHead != null) {
				oldBillData.updateItemByDef(defHead, "vdef", true);
				for (int i = 1; i <= 20; i++) {
					nc.ui.pub.bill.BillItem item = oldBillData
							.getHeadItem("vdef" + i);
					if (item != null) {
						((nc.ui.pub.beans.UIRefPane) item.getComponent())
								.setAutoCheck(true);
					}
				}
			}
			// 表体
			if ((defBody != null)) {
				oldBillData.updateItemByDef(defBody, "vdef", false);
				for (int i = 1; i <= 20; i++) {
					nc.ui.pub.bill.BillItem item = oldBillData
							.getBodyItem("vdef" + i);
					if (item != null) {
						((nc.ui.pub.beans.UIRefPane) item.getComponent())
								.setAutoCheck(true);
					}
					//
					if (item != null && item.getComponent() != null)
						((nc.ui.pub.beans.UIRefPane) item.getComponent())
								.setEditable(item.isEdit());
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		return oldBillData;
	}

	/**
	 * 自定义项定义(列表状态)
	 * 
	 * @param defHead
	 * @param defBody
	 * @param oldBillData
	 * @return
	 */
	protected BillListData changeBillListDataByUserDef(DefVO[] defHead,
			DefVO[] defBody, BillListData oldBillData) {
		try {
			if (defHead != null) // 表头
				oldBillData.updateItemByDef(defHead, "vdef", true);
			if (defBody != null) // 表体
				oldBillData.updateItemByDef(defBody, "vdef", false);
			return oldBillData;
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		return oldBillData;
	}

	/**
	 * 获取表头自定义项VO
	 * 
	 * @return
	 */
	public DefVO[] getDefHeadVO() {
		if (m_defHead == null) {
			try {
				m_defHead = DefSetTool.getDefHead(getCorpPrimaryKey(),
						getUIControl().getBillType());
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
		}
		return m_defHead;
	}

	/**
	 * 获取表体自定义项VO
	 * 
	 * @return
	 */
	public DefVO[] getDefBodyVO() {
		if (m_defBody == null) {
			try {
				m_defBody = DefSetTool.getDefBody(getCorpPrimaryKey(),
						getUIControl().getBillType());
			} catch (Exception e) {
				Logger.error(e.getMessage());
			}
		}
		return m_defBody;
	}

	protected void afterVuserDefEdit(BillEditEvent e) {
		int pos = e.getPos();
		String sItemKey = e.getKey();
		int row = e.getRow();
		if (pos == 0) {// 表头
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vdef".length());
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					sItemKey, sVdefPkKey);
		} else if (pos == 1) {// 表体
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vdef".length());
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row,
					sItemKey, sVdefPkKey);
		}
	}
}
