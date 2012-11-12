package nc.ui.dm.plan;
import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8004040204.ssButtun.WdsCloseButtonVO;
import nc.ui.wds.w8004040204.ssButtun.WdsOpenButtonVO;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 *  发运计划录入 
 * @author Administrator
 * 
 */
public class ClientUI extends WdsBillManagUI implements BillCardBeforeEditListener {

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	@Override
	protected void initEventListener() {
	
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("iplantype", 0);//计划类型默认月计划
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDS1);
		getBillCardPanel().setTailItem("dmakedate", _getDate());		
		//调出仓库默认当前登录人关联的仓库	
		getBillCardPanel().setHeadItem("pk_outwhouse", getLoginInforHelper().getLogInfor(_getOperator()).getWhid());
		
	}
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// 单据号
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}
	
		@Override
	public void afterEdit(BillEditEvent e) {
			super.afterEdit(e);
			String key = e.getKey();
			//仓库过滤，只是将属于物流的显示出来
			if(e.getPos() ==BillItem.HEAD){
				//仓库过滤，只属于物流系统的
				if("pk_inwhouse".equalsIgnoreCase(key)){
					String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_inwhouse").getValueObject());
					String pk_outwhouse=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_outwhouse").getValueObject());
					if(pk_outwhouse == null){
						showErrorMessage("当前登录人没有绑定仓库");
					}
					if(pk_outwhouse.equals(pk_inwhouse)){
						showWarningMessage("调入仓库不能和调出仓库相同");
						getBillCardPanel().setHeadItem("pk_inwhouse", null);
					}
				}else if("pk_outwhouse".equalsIgnoreCase(key)){
					JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
					if( c instanceof UIRefPane){
						UIRefPane ref = (UIRefPane)c;
						ref.getRefModel().addWherePart(" def1 = '1' and isnull(dr,0) = 0");
					}
				}
			}else if(e.getPos() == BillItem.BODY){
				//编辑存货
				if("invcode".equalsIgnoreCase(key)){
		//			afterEditWhenAssistUnit(e);
				}else if("nplannum".equalsIgnoreCase(key)){//编辑计划数量				
						afterEditNplannum(e);				
				}else if("nassplannum".equalsIgnoreCase(key)){//编辑计划辅数量				
						afterEditNassplannum(e);
				
				}else if("assunitname".equalsIgnoreCase(key)){//编辑主计量单位
					afterEditWhenAssunit(e);
				}
			}
	}
		/**
		 * 编辑计划数量
		 * liuys add
		 * @param e
		 * @throws Exception 
		 */
		private void afterEditNplannum(BillEditEvent e)  {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// 计划数量
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));

//			if (hsl == null || nplannum == null) {
////				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"hsl");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"nassplannum");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunitname");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunit");
//				return;
//			}
			if(hsl==null || "".equals(hsl)){
				this.showErrorMessage("换算率为空");
				return ;
			}
			convert = new UFDouble(nplannum).div(new UFDouble(hsl));
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nassplannum");
		}
		/**
		 * liuys  add
		 * 编辑计划辅数量
		 * @param e
		 * @throws Exception 
		 */
		private void afterEditNassplannum(BillEditEvent e)  {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// 计划辅数量
			String nassplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nassplannum"));
//
//			if (hsl == null || nassplannum == null) {
////				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"hsl");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"nassplannum");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunitname");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunit");
//				return;
//			}
			if(hsl==null || "".equals(hsl)){
				this.showErrorMessage("换算率为空");
				return;
			}
			convert = new UFDouble(hsl).multiply(new UFDouble(nassplannum));
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nplannum");
			getBillCardPanel().getBillModel()
			.setValueAt(convert, e.getRow(), "reserve8");
			getBillCardPanel().getBillModel()
			.setValueAt(nassplannum, e.getRow(), "reserve9");
			
		}
		
		/**
		 * liuys add 
		 * for 完达山物流项目
		 * 2011-03-30
		 * 辅计量编辑事件 处理： # 辅计量主键编辑时触发处理 ==>> 换算率 | # 选取的计量ID是主计量ID：变换率置为1，固定换算率 | #
		 * 由“换算率”驱动公式计算 > 步完成均处理合格数量是否可编辑 # 同步更新单据模板中的换算率和是否固定换算率属性 | 
		 * # 更新换算率属性列可编辑性
		 * 
		 * @param e
		 *            nc.ui.pub.bill.BillEditEvent
		 */
		private void afterEditWhenAssistUnit(BillEditEvent e) {
			// 存货ID
			 UFDouble convert = new UFDouble(0);
			String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
			// 辅计量主键
			String sCassId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "assunit"));

			if (sCassId == null) {
				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			// 获取换算率
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "hsl");
		}
		
		/**
		 * liuys add
		 * 
		 * 编辑主计量单位
		 * @param e
		 */
		private void afterEditWhenAssunit(BillEditEvent e) {
			// 是否固定变化率改变时要同步更改：strKeys[0]的值
//			boolean isfixed = true;
			// 存货ID
		    UFDouble convert = WdsWlPubTool.DOUBLE_ZERO;
			String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
			// 辅计量主键
			String sCassId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "assunit"));

			if (sCassId == null) {
				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			// 获取换算率
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			// 是否固定换算率
//			isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "hsl");
			// 计划数量
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));
			if(convert == null)
				return;
			if(nplannum != null && convert != null){
				UFDouble nassplannum = new UFDouble(nplannum).div(convert);
				getBillCardPanel().getBillModel().setValueAt(nassplannum, e.getRow(), "nassplannum");
			}
		}
	/**
	 * 增加后台校验
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			//仓库过滤，只属于物流系统的
			if("pk_inwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			if("pk_outwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_outwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			
		}else if(e.getItem().getPos() ==BillItem.BODY){}
		return true;
	}
	
	@Override
	protected void initPrivateButton() {
		
		//辅助功能
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(ButtonCommon.FZGN);
		btnvo.setBtnName("辅助功能");
		btnvo.setBtnChinaName("辅助功能");
		btnvo.setBtnCode(null);// code最好设置为空
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo.setChildAry(new int[] {ButtonCommon.ROWCLOSE});
		addPrivateButton(btnvo);		
		//整单关闭-------------------------------暂不支持整单关闭   后续需要可以加上  zhf
		ButtonVO btnvo3 = new ButtonVO();
		btnvo3.setBtnNo(ButtonCommon.BILLCLOSE);
		btnvo3.setBtnName("关闭");
		btnvo3.setBtnChinaName("关闭");
		btnvo3.setBtnCode(null);// code最好设置为空
		btnvo3.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
//		btnvo3.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo3);	
		//行关闭
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(ButtonCommon.ROWCLOSE);
		btnvo2.setBtnName("行关闭");
		btnvo2.setBtnChinaName("行关闭");
		btnvo2.setBtnCode(null);// code最好设置为空
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
//		btnvo2.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo2);		
		
		//add by yf 2012-07-25
		WdsCloseButtonVO btnclose = new WdsCloseButtonVO();
		addPrivateButton(btnclose.getButton());
		WdsOpenButtonVO btnopen = new WdsOpenButtonVO();
		addPrivateButton(btnopen.getButton());
	
		super.initPrivateButton();		
	}

	@Override
	public String getBillType() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.WDS1;
	}

	@Override
	public boolean isLinkQueryEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAssNumFieldName() {
		// TODO Auto-generated method stub
		return "nassplannum";
	}

	@Override
	public String getHslFieldName() {
		// TODO Auto-generated method stub
		return "hsl";
	}

}