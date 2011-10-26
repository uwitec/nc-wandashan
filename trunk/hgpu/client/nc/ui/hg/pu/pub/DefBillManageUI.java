package nc.ui.hg.pu.pub;

import nc.bs.logging.Logger;
import nc.ui.hg.pu.pub.freeitem.FreeItemRefPane;
import nc.ui.hg.pu.pub.freeitem.InvAttrCellRenderer;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListData;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.def.DefVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.freeitem.FreeVO;
import nc.vo.hg.pu.pub.freeitem.VInvVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * <支持自定义项> BillManageUI <单表头、表体数据>  <支持自由项处理>
 * 
 * @author Administrator
 * 
 */
public abstract class DefBillManageUI extends BillManageUI implements ILinkQuery{

	private DefVO[] m_defBody = null;

	private DefVO[] m_defHead = null;
	
	private Class bodyVOClass;
	
	private Class bodyVO1Class;
	
	private Class aggBillVOClass;
	
	// 自由项参照
	protected static FreeItemRefPane ivjFreeItemRefPane = null;


	public DefBillManageUI() {
		super();
		initialize();
		init();
	}
	
	private void initialize() {
		// 初始化设置显示千分位
		initShowThMark(true);
	}
	/**
	 * 初始化设置显示千分位 作者：薛恩平 创建日期：2007-7-22 下午03:04:06
	 */
	protected void initShowThMark(boolean isShow) {
		try {
			// 卡片表头
			getBillCardPanel().setHeadTailShowThMark(getBillCardPanel().getHeadItems(), isShow);
			// 列表表头
			getBillListPanel().getParentListPanel().setShowThMark(isShow);
			// 卡片表体
			getBillCardPanel().setShowThMark(isShow);
			// 列表表体
			getBillListPanel().getChildListPanel().setShowThMark(isShow);
		} catch (Exception e) {
			// 列表表体为空时，会抛空指针异常
			System.out.println("千分位初始化时出现异常，但不是错误");
			e.printStackTrace();
		}
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		if (sItemKey.startsWith("vdef")) {
			// 自定义项编辑后事件
			afterVuserDefEdit(e);
		}else if(e.getPos() == IBillItem.BODY && "vfree0".equals(sItemKey)){
			//表体自由项处理
			afterFreeItemEdit(e);
		}else{
			super.afterEdit(e);
		}
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		//表体自由项处理
		if(e.getPos() == IBillItem.BODY && "vfree0".equals(sItemKey)){
			beforeFreeItemEdit(e);
		}
		return super.beforeEdit(e);
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
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row,sItemKey, sVdefPkKey);
		}
	}
	
	/**
	 * 自由项处理
	 */
	protected void afterFreeItemEdit(BillEditEvent e){
		try {
			FreeItemRefPane ref = (FreeItemRefPane) getBillCardPanel().getBodyItem("vfree0").getComponent();
			FreeVO voFree = ref.getFreeVO();
			// 将自由项填入表体
			for (int i = 0; i <= 10; i++) {
				String fieldname = "vfree" + i;
				Object o = voFree.getAttributeValue(fieldname);
				getBillCardPanel().setBodyValueAt(o, e.getRow(), fieldname);
			}
		} catch (Exception e1) {
			Logger.error(e);
		}
		// 自由项着色
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel());
	}
	/**
	 * 存货编辑事件处理（多选存货）
	 */
	public void afterInventoryMutiEdit(BillEditEvent e ) {
		int editrow = e.getRow();
		String key = e.getKey();
		UIRefPane invRef = (UIRefPane) getBillCardPanel().getBodyItem(key).getComponent();
		//存货为空，清空存货相关数据
		String[] refPks = invRef.getRefPKs();
		if (refPks == null || refPks.length == 0) {
//			afterInvEditClear(editrow);
			return;
		}
		if (e.getOldValue() != null && e.getValue() != null) {
			//清空原有行相关记录
//			afterInvEditClear(editrow);
		}
		
		// 批量获取存货信息,通过后台查询，返回存货信息
		VInvVO[] invvos = null;
		

		boolean bisCalculate = getBillCardPanel().getBillModel().isNeedCalculate();
		getBillCardPanel().getBillModel().setNeedCalculate(false);

		// 增加新空行
		if (refPks.length > 1) {
			if (editrow == getBillCardPanel().getRowCount() - 1) {
				addNullLine(e.getRow(), refPks.length - 1);
			} else {
				insertNullLine(e.getRow(), refPks.length - 1);
			}
		}
		//表体存货赋值
		for (int i = 0; i < refPks.length; i++) {
			// 设置存货相关信息
//			setBodyValueByInvVO(invvos[i], e.getRow() + i);
		}
		//变色龙
		InvAttrCellRenderer ficr = new InvAttrCellRenderer();
		ficr.setFreeItemRenderer(getBillCardPanel());
		//重算合计行
		getBillCardPanel().getBillModel().setNeedCalculate(bisCalculate);
		if (bisCalculate){
			getBillCardPanel().getBillModel().reCalcurateAll();
		}
	}
	
	/**
	 * 增加空行
	 */
	public void addNullLine(int istartrow, int count) {
		if (count>0) {
			for (int i = 1; i <= count; i++) {
				getBillCardPanel().addLine();
			}
		}
	}
	/**
	 * 插入空行
	 */
	public void insertNullLine(int istartrow, int count)  {
		if (count>0) {
			for (int i = 1; i <= count; i++) {
				getBillCardPanel().insertLine();
			}
		}
	}
	/**
	 * 自由项编辑前事件
	 */
	protected void beforeFreeItemEdit(BillEditEvent e){
		// 获得存货VO
		try {
			int row = e.getRow();
			VInvVO voInv = (VInvVO)getBillCardPanel().getBodyValueAt(row, "invvo");
			getBillCardPanel().getBillModel().addRowAttributeObject(row, e.getKey(), null);
			if(voInv != null){
				getFreeItemRefPane().setFreeItemParam(voInv);
			}
		} catch (Exception ex) {
			Logger.info("自由项设置失败!");
		}
	}
//	/**
//	 * 存货编辑后，表体行存取存货信息
//	 */
//	public void setBodyFreeItemValue(String tablecode ,int row){
//		 	if (row < 0)
//	            return;
//		 	if(tablecode == null || "".equals(tablecode)){
//		 		tablecode = getBillCardPanel().getCurrentBodyTableCode();
//		 	}
//	        Object o = getBillCardPanel().getBillModel(tablecode).getRowAttributeObject(row,"BILLCARD");
//	        VInvo voInv = null;
//	        if(voInv != null)
//	        	voInv = (VInvo)o;
//	        else
//	        	voInv = 
//	        //根据存货查询---存货信息
//	        getBillCardPanel().getBillModel(tablecode).addRowAttributeObject(row,"BILLCARD",voInv);
//	}
	/**
	 * 获取自由项参照
	 * @return
	 */
	protected FreeItemRefPane getFreeItemRefPane() {
		if (ivjFreeItemRefPane == null) {
			try {
				ivjFreeItemRefPane = new FreeItemRefPane();
				ivjFreeItemRefPane.setName("FreeItemRefPane");
				ivjFreeItemRefPane.setLocation(209, 4);
			} catch (java.lang.Throwable ivjExc) {
				ivjExc.printStackTrace();
			}
		}
		return ivjFreeItemRefPane;
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
	 * 是否卡片界面
	 * @author zpm
	 */
	public boolean isCardPanelSelected() {
		return m_CurrentPanel.equals(BillTemplateWrapper.CARDPANEL);
	}
	
	// 添加自定义按钮
	public void initPrivateButton() {
		//修改
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(HgPuBtnConst.Editor);
		btnvo1.setBtnName("修改");
		btnvo1.setBtnChinaName("修改");
		btnvo1.setBtnCode(null);// code最好设置为空
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_INIT,
				IBillOperate.OP_NOTEDIT});
		btnvo1.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo1);
	}
	
	//卡片界面不要设置0显示为空
	private void init() {
			//getBillCardPanel().setAutoExecHeadEditFormula(true);
			//卡片界面不要设置0显示为空
			BillItem[] items = getBillCardPanel().getBodyItems();
			if ((items != null) && (items.length > 0)) {
				for (int i = 0; i < items.length; i++) {
					BillItem item = items[i];
					if ((item.getDataType() == BillItem.INTEGER) || (item.getDataType() == BillItem.DECIMAL))
						if (item.isShow() && item.getNumberFormat() != null) {
							item.getNumberFormat().setShowZeroLikeNull(false);
						}
				}
			}
		}
}
