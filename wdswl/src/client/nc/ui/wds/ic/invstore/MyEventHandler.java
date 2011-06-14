package nc.ui.wds.ic.invstore;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;

import nc.vo.pub.SuperVO;
import nc.vo.wds.ic.invstore.CargdocVO;
import nc.vo.wds.ic.invstore.TbSpacegoodsVO;
import nc.vo.wl.pub.LoginInforVO;

/**
 * 
 */

public class MyEventHandler extends ManageEventHandler {
    private LoginInforHelper helper=null;
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
	
	@Override
	protected String getHeadCondition() {		
		return " isnull(dr,0) = 0 ";
	}
	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(
				getBillUI(), null, 
				_getCorp().getPk_corp(), getBillUI().getModuleCode()
				, getBillUI()._getOperator(), null		
		);
	}
	

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		String value =(String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		String pk_storedoc =(String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_stordoc").getValueObject();
		int row =getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(value, row, "pk_cargdoc");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_storedoc, row, "pk_storedoc");
	}

	@Override
	protected void onBoSave() throws Exception {
		TbSpacegoodsVO[] tbSpacegoodsVO = (TbSpacegoodsVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		if (null != tbSpacegoodsVO) {
			for (int i = 0; i < tbSpacegoodsVO.length; i++) {
				String pk_invbasdoc = tbSpacegoodsVO[i].getPk_invbasdoc();
				for (int j = i + 1; j < tbSpacegoodsVO.length; j++) {
					String pk_invbasdocj = tbSpacegoodsVO[j].getPk_invbasdoc();
					if (null != pk_invbasdoc && null != pk_invbasdocj
							&& !"".equals(pk_invbasdoc)
							&& !"".equals(pk_invbasdoc)) {
						if (pk_invbasdoc.equals(pk_invbasdocj)) {
							getBillUI().showErrorMessage("货物种类重复，请重新选择！");
							return;
						}
					}
				}
			}
		}
		super.onBoSave();
	}

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
	
		LoginInforVO login=getLoginInforHelper().getLogInfor(_getOperator());
		
	  SuperVO[] vos=HYPubBO_Client.queryByCondition(CargdocVO.class, " wds_cargdoc1.pk_cargdoc='"+login.getSpaceid()+"' and isnull(wds_cargdoc1.dr,0)=0");
		 if(vos !=null && vos.length>0){
			 throw new Exception("该货位已经存在，请执行查询操作");
		 }
		 
		super.onBoAdd(bo);
	}
	
	
	
}