package nc.ui.zb.parmset;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.zb.parmset.ParamSetVO;


public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBoSave() throws Exception {
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		ParamSetVO vo =(ParamSetVO)checkVO.getParentVO();
		if(vo==null)
			throw new BusinessException("参数设置不存在");
		vo.validateOnSave();
		super.onBoSave();
	}
	
}
