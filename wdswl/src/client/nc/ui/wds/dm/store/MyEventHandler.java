package nc.ui.wds.dm.store;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.wds.dm.store.MyBillVO;
import nc.vo.wds.dm.store.TbStorareaclVO;
import nc.vo.wds.w80060804.TbTranscompanyVO;

/**
  *
  *该类是AbstractMyEventHandler抽象类的实现类，
  *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	private String eapk_areacl="";
	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
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
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		MyBillVO myBillVO=(MyBillVO)this.getBillCardPanelWrapper().getChangedVOFromUI();
		TbStorareaclVO saVO=(TbStorareaclVO)myBillVO.getParentVO();
		saVO.validate();
		String pk_areacl = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_areacl").getValueObject();
		if (!(pk_areacl.equals(eapk_areacl))) {
			String wheresql=" pk_areacl='"+pk_areacl+"' and dr=0";
			IUAPQueryBS query=(IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList ttcs=(ArrayList) query.retrieveByClause(TbStorareaclVO.class, wheresql);
			if(ttcs.size()>0){
				getBillUI().showErrorMessage("该地区已经选择了分仓！");
				return;
			}
		}
		
	
		String a=eapk_areacl;
		super.onBoSave();
		eapk_areacl="";
	}
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
	
		super.onBoEdit();
		eapk_areacl=(String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_areacl").getValueObject();
		String wheresql=" pk_areacl ";
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		eapk_areacl="";
	}

		
}