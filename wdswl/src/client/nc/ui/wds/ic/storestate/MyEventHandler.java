package nc.ui.wds.ic.storestate;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.SuperVO;
import nc.vo.wds.ic.storestate.TbStockstateVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		// TODO Auto-generated method stub
		return new MyQueryDIG(
				getBillUI(), null, 
				
				_getCorp().getPk_corp(), getBillUI().getModuleCode()
				
				, getBillUI()._getOperator(), null		
		);
	}

	
	
	
	@Override
	protected void onBoDelete() throws Exception {
		//校验库存状态是否存在下游
		AggregatedValueObject  billvo= getBillUI().getVOFromUI();
		if(billvo.getParentVO()!=null){
			TbStockstateVO tb=(TbStockstateVO) billvo.getParentVO();
			if(tb.getPrimaryKey()!=null && ! tb.equals("")){
				String whereSql=" ss_pk='"+tb.getPrimaryKey()+"' and isnull(dr,0)=0";
			 SuperVO[] sus=	HYPubBO_Client.queryByCondition(StockInvOnHandVO.class,whereSql);
			 if(sus.length >0){
				 throw new BusinessException("已存在下游数据不能删除");
			 }
				
			}			
		}
		super.onBoDelete();
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		Object tate = getBillCardPanelWrapper().getBillCardPanel()
		.getHeadTailItem("ss_state").getValueObject();
		if(null==tate||"".equals(tate)){
			getBillUI().showErrorMessage("请填写状态");
			return ;
		}
		// 获取是否可出库状态状态
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("ss_isout").getValueObject();
		if (null != temp && !"".equals(temp)) {
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
					"ss_isout", temp);
		}
			
		super.onBoSave();
	}
}