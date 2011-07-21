package nc.ui.zb.parmset;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.parmset.ParamSetVO;

/**
 * 报价参数设置
 * 4004090415
 */
public class ClientUI extends BillCardUI {

	public ClientUI() {
		super();
		String corp =ClientEnvironment.getInstance().getCorporation().getPk_corp();
		SuperVO[] vos =null;
			try {
				vos =HYPubBO_Client.queryByCondition(ParamSetVO.class, " isnull(dr,0)=0 and pk_corp ='"+corp+"'");
			} catch (UifException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(vos == null || vos.length == 0){
				try {
					setBillOperate(IBillOperate.OP_NOTEDIT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
				
			
			getBillCardPanel().getBillData().setHeaderValueVO(vos[0]);
			
			
			HYBillVO billvo = new HYBillVO();
//			getBillCardPanel().getHeadItem("nmaxquotatpoints").setValue(((ParamSetVO)vos[0]).getNmaxquotatpoints());
			billvo.setParentVO(vos[0]);
			getBufferData().addVOToBuffer(billvo);
			try {
				setBillOperate(IBillOperate.OP_NOTEDIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//				getBillCardPanel().getBillData().setBillValueVO(billvo);
			updateUI();
		
	}

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ClientController();
	}

	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initSelfData() {
		
	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub
		String corp =ClientEnvironment.getInstance().getCorporation().getPk_corp();
		getBillCardPanel().getHeadItem("pk_corp").setValue(corp);
		getBillCardPanel().getHeadItem("ndelaytime").setValue(new UFDouble(8));//网上招标网络合理延迟时间  单位s  默认 8
		getBillCardPanel().getHeadItem("ireferencelimits").setValue(new Integer(6));//历史供货价格历史时间范围 单位 月 默认6 
	}

	protected CardEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}
}
