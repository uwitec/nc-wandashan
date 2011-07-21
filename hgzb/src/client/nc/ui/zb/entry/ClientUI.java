package nc.ui.zb.entry;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.DefBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;

/**
 * 中标结果录入
 * 
 * @author Administrator
 * 
 */
public class ClientUI extends DefBillManageUI implements BillCardBeforeEditListener {

	public ClientUI() {
		super();
	}

	@Override
	protected void initSelfData() {
		
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);// BillItemEvent捕捉到的BillItemEvent事件
		// 除去行操作多余按钮
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLinetoTail));
		}
		getBillCardPanel().setTatolRowShow(true);
		getBillCardPanel().setBodyMenuShow(false);
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);// 单据状态
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_billtype", ZbPubConst.ZB_Result_BILLTYPE);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());// 制单人
		getBillCardPanel().setTailItem("dmakedate", _getDate());// 制单日期
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		
	}

	// 添加自定义按钮
	public void initPrivateButton() {
		// 生成合同
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZbPuBtnConst.GenPurOrder);
		btnvo6.setBtnName("生成合同");
		btnvo6.setBtnChinaName("生成合同");
		btnvo6.setBtnCode(null);// code最好设置为空
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setBusinessStatus(new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo6);
		// 联查
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(ZbPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("联查");
		btnvo4.setBtnChinaName("联查");
		btnvo4.setBtnCode(null);// code最好设置为空
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// 辅助查询
		ButtonVO btnvo8 = new ButtonVO();
		btnvo8.setBtnNo(ZbPuBtnConst.ASSQUERY);
		btnvo8.setBtnName("辅助查询");
		btnvo8.setBtnChinaName("辅助查询");
		btnvo8.setBtnCode(null);// code最好设置为空
		btnvo8.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo8.setChildAry(new int[] { ZbPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo8);
		
		// 打印管理
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("打印管理");
		btnvo9.setBtnChinaName("打印管理");
		btnvo9.setBtnCode(null);// code最好设置为空
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);
		
		//修改
		ButtonVO btnvo10 = new ButtonVO();
		btnvo10.setBtnNo(ZbPuBtnConst.Editor);
		btnvo10.setBtnName("修改");
		btnvo10.setBtnChinaName("修改");
		btnvo10.setBtnCode(null);// code最好设置为空
		btnvo10.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
		btnvo10.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo10);
		super.initPrivateButton();
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		super.bodyRowChange(e);
		
	}

	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

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
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		return false;
	}

//	@Override
//	public String getRefBillType() {
//		return ZbPubConst.ZB_EVALUATION_BILLTYPE;
//	}
	@Override
	protected String getBillNo() throws Exception {
		// TODO Auto-generated method stub
		return HYPubBO_Client.getBillNo(ZbPubConst.ZB_Result_BILLTYPE, _getCorp().getPrimaryKey(), null, null);
	}
	@Override
	public Object getUserObject() {
		return null;
	}
	
	/**
	 * 表头编辑前
	 */
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		String key = e.getItem().getKey();
		if ("vemployeeid".equalsIgnoreCase(key)) {//业务员    根据部门过滤业务员
			
			String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
			
			if (deptdoc == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_psndoc.pk_deptdoc = '" + deptdoc + "'");
			
		}
		return false;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row =e.getRow();
//		if ("invcode".equalsIgnoreCase(key)) {// 存货  根据存货分类过滤存货
//			 String invcode =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invclcode"));//存货分类编码
//	         UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
//	         AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
//	       
//	         if(invcode==null){
//		            refModel.addWherePart("  and 1=1 ");
//		         return true;
//	           }else{
//	  	        refModel.addWherePart(" and bd_invbasdoc.pk_invcl in (select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')"); 
//	         }
//		}
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			if("cbiddingid".equalsIgnoreCase(key)){
				getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(key));				
			}else if("ccustmanid".equalsIgnoreCase(key)){
				 getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(key));
			}else if("pk_deptdoc".equalsIgnoreCase(key)){//部门  清空业务员
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// 业务员  带出部门
				String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null)
				    getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("vemployeeid"));
			}
		} else if (e.getPos() == BillItem.BODY) {
			int row =e.getRow();
            
//			if("invclcode".equalsIgnoreCase(key)){//存货分类  清空存货信息 
//                 getBillCardPanel().setBodyValueAt(null,row,"cinvbasid");
//                 getBillCardPanel().setBodyValueAt(null,row,"cinvmanid");
//                 getBillCardPanel().setBodyValueAt(null,row,"invcode");
//                 getBillCardPanel().execBodyFormula(row,"invcode");
//             }else if("invcode".equalsIgnoreCase(key)){//存货   带出存货分类
//            	 String invclcode =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invclcode"));//存货分类编码
//            	
//            	 if(invclcode ==null){
//            		 String[] aryAssistunit = 
//                		 new String[] {"cinvclid->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,cinvbasid)",
//                			    "invclcode->getColValue(bd_invcl,invclasscode,pk_invcl,cinvclid)",
//                		 		"invclname->getColValue(bd_invcl,invclassname,pk_invcl,cinvclid)"};
//     				 getBillCardPanel().getBillModel().execFormulas(e.getRow(),aryAssistunit);
//            	 }
//            	
//             }
		}
	}
}
