
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
/**
 * @author mlr
 */
public abstract class MBillManagUI extends BillManageUI implements ILinkQuery{

	private static final long serialVersionUID = -8915359082792921730L;
	public MBillManagUI() {
		super();
	}
	
	public MBillManagUI(Boolean useBillSource) {
		super(useBillSource);
	}
	public MBillManagUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
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
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");
			
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
				} else {
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
	private class CommonButtonDef {
		//定义联查按钮
		public ButtonVO getJoinUPButton() {
			ButtonVO btnVo = new ButtonVO();
			btnVo.setBtnNo(999);
			btnVo.setBtnCode("joinup");
			btnVo.setBtnName("联查");
			btnVo.setBtnChinaName("联查");
			btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
			return btnVo;
		}		
	}
}
