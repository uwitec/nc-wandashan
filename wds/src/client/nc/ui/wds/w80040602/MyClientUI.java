package nc.ui.wds.w80040602;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w80040602.TbSpacegoodsVO;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;


/**
 * <b> 在此处简要描述此类的功能 </b>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject version
 */
 public class MyClientUI extends AbstractMyClientUI{
       
       protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}
       
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {	}

	protected void initSelfData() {	}

	public void setDefaultData() throws Exception {
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if ("sgcode".equals(e.getKey())) {
			getBillCardPanel().execHeadEditFormulas();
			TbSpacegoodsVO[] tbSpacegoodsVO=null;
			try {
				if(null!=getBillCardWrapper().getBillVOFromUI().getChildrenVO()){
					tbSpacegoodsVO=(TbSpacegoodsVO[]) this.getBillCardWrapper().getBillVOFromUI().getChildrenVO();
				}
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String pk_invbasdoc="";
			if(null!=getBillCardPanel().getBillModel().getValueAt(
					getBillCardPanel().getBillTable().getSelectedRow(),
					"pk_invbasdoc")){
				pk_invbasdoc=getBillCardPanel().getBillModel().getValueAt(
						getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_invbasdoc").toString();
			}
			
			boolean isinv=false;
			int invnum=0;
			if(null!=tbSpacegoodsVO){
				for(int i=0;i<tbSpacegoodsVO.length;i++){
					String tbpk_invbasdoc="";
					if(null!=tbSpacegoodsVO[i]){
						if(null!=tbSpacegoodsVO[i].getPk_invbasdoc()&&!"".equals(tbSpacegoodsVO[i].getPk_invbasdoc())){
							tbpk_invbasdoc=tbSpacegoodsVO[i].getPk_invbasdoc();
						}
					}
					if(!"".equals(tbpk_invbasdoc)&&!"".equals(pk_invbasdoc)){
						if(pk_invbasdoc.equals(tbpk_invbasdoc)){
							invnum++;
						}
					}
				}
			}
			if(invnum>1){
				isinv=true;
			}
			if(isinv){
				showErrorMessage("此货物该货位已存在！");
//				getBillCardPanel().getBillModel().setValueAt("",
//						getBillCardPanel().getBillTable().getSelectedRow(),
//						"sgcode");
//				UIRefPane panel = (UIRefPane)getBillCardPanel().getBodyItem("sgcode").getComponent();
//				getBillCardPanel().getBodyItem("sgcode").setComponent(null);
			}
			
		}
	}


}
