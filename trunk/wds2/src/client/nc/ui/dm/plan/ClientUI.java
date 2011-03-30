package nc.ui.dm.plan;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.hi.hi_410.PubDocVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  发运计划录入 
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI {

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
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
		getBillCardPanel().setHeadItem("pk_outwhouse", //调出仓库默认当前登录人关联的仓库				
				LoginInforHelper.getLogInfor(_getOperator()).getWhid());
		
		
		
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
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
		@Override
	public void afterEdit(BillEditEvent e) {
			super.afterEdit(e);
			String key = e.getKey();
			Object value =e.getValue();
			Object oldValue = e.getOldValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_inwhouse".equalsIgnoreCase(key)){
					Object pk_inwhouse = getBillCardPanel().getHeadItem("pk_inwhouse").getValueObject();
					Object pk_outwhouse=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_outwhouse").getValueObject());
					if(pk_outwhouse == null){
						showErrorMessage("当前登录人没有绑定仓库");
					}
					if(pk_outwhouse.equals(pk_inwhouse)){
						showWarningMessage("调入仓库不能和调出仓库相同");
						getBillCardPanel().setHeadItem("pk_inwhouse", null);
					}
				}
			}else if(e.getPos() == BillItem.BODY){
				//编辑存货
				if("invcode".equalsIgnoreCase(key)){
					afterEditWhenAssistUnit(e);
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
		 */
		private void afterEditNplannum(BillEditEvent e) {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// 计划数量
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));

			if (hsl == null || nplannum == null) {
//				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
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
			convert = new UFDouble(nplannum).div(new UFDouble(hsl));
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nassplannum");
		}
		/**
		 * liuys  add
		 * 编辑计划辅数量
		 * @param e
		 */
		private void afterEditNassplannum(BillEditEvent e) {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// 计划辅数量
			String nassplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nassplannum"));

			if (hsl == null || nassplannum == null) {
//				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
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
			convert = new UFDouble(hsl).multiply(new UFDouble(nassplannum));
			// 设置辅信息 到界面
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nplannum");
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
			boolean isfixed = true;
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
			// 是否固定换算率
			isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
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

}