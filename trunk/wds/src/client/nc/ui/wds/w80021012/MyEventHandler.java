package nc.ui.wds.w80021012;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.bill.BillRendererVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		String s =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").getValue();
		System.out.println(s+"------------");
		if(s==null||"".equals(s)){
			System.out.println("出错了");
			getBillUI().showErrorMessage("请选择单品");
		}
		else{
			System.out.println("没出错");
		super.onBoSave();
		}
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		super.onBoAdd(bo);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		showZeroLikeNull(false);
		super.onBoEdit();
	}
		
}