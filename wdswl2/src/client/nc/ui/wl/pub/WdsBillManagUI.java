package nc.ui.wl.pub;
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.Button.CommonButtonDef;
/**
 * @author mlr
 */
public abstract class WdsBillManagUI extends BillManageUI implements ILinkQuery{

	public WdsBillManagUI() {
		super();
	}
	
	public WdsBillManagUI(Boolean useBillSource) {
		super(useBillSource);
	}
	public WdsBillManagUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}	
	private Class aggBillVOClass;
	
	private Class headVOClass;
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
			SuperVO headvo = (SuperVO)getHeadClass().newInstance();
			//查询主表数据
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getHeadClass(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+headvo.getPKFieldName()+" = '"+id+"' ");
			
			//查询子表数据
			if(queryVos != null && queryVos.length > 0){
					setCurrentPanel(BillTemplateWrapper.CARDPANEL);
					AggregatedValueObject aggvo = (AggregatedValueObject)getAggVOClass().newInstance();
					aggvo.setParentVO(queryVos[0]);
					getBufferData().clear();
					getBufferData().addVOToBuffer(aggvo);
					getBufferData().setCurrentRow(0);
			}
			ButtonObject[] btns = getButtons();
			for (ButtonObject btn : btns) {
				if (("" + (IBillButton.Print)).equals(btn.getTag())) {
					btn.setEnabled(true);
					btn.setVisible(true);
				}else{
					btn.setEnabled(false);
					btn.setVisible(false);
				}
			}
			updateButtons();
			
		}  catch (Exception e) {
			Logger.error(e);
		}
	
	}
	
	private Class getAggVOClass()  throws Exception{
		if(aggBillVOClass == null)
				aggBillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			return aggBillVOClass;
	}
	private Class getHeadClass()  throws Exception{
		if( headVOClass == null)
			headVOClass = Class.forName(getUIControl().getBillVoName()[1]);
			return headVOClass;
	}
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
	}
}
