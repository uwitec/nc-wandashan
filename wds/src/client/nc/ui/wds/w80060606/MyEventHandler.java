package nc.ui.wds.w80060606;

import java.util.ArrayList;
import java.util.Vector;


import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MessageEvent;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.buffer.RecordNotFoundExcetption;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060606.MyBillVO;
import nc.vo.wds.w80060606.TbProdwaybillBVO;
import nc.vo.wds.w80060606.TbProdwaybillVO;

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

	private MyClientUI myClientUI;

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
	}
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow=-1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow=0;
					
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow=getBufferData().getCurrentRow();
				}
			}
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if(nCurrentRow>=0){
			getBufferData().setCurrentRow(nCurrentRow);
		}
		
		
//		MyBillVO myBillVO=(MyBillVO) getBillUI().getVOFromUI();
//		TbProdwaybillBVO[] tbProdwaybillBVO=(TbProdwaybillBVO[]) myBillVO.getChildrenVO();
//		for (int i = 0; i < tbProdwaybillBVO.length; i++) {
//			
//		}
//		
		getButtonManager().getButton(nc.ui.wds.w80060606.pwButtun.IPwButtun.PwAdd).setEnabled(
				true);
		
	}
	protected void onPwAdd() {
		// TODO 请实现此按钮事件的逻辑

		ProdWaybillDlg pwbQuery = new ProdWaybillDlg(myClientUI);

		AggregatedValueObject[] vos = pwbQuery.getReturnVOs(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"0206", "4Y", "80060606", "8006060601", myClientUI);
		

 try {
//			if (TOBillTool.isNull(vos)) {
//				showHintMessage("没有可安排的物资需求");
//				return;
//			}
			if (null == vos||vos.length==0) {
				return;
			}
			
//			if (((BillManageUI)getBillUI()).isListPanelSelected())
//				((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);
			
			MyBillVO voForSave = changeGentoTbpwb(vos);
			getBufferData().clear();
			getBufferData().addVOToBuffer(voForSave);
			updateBuffer();
//			getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			
			
			getBillUI().updateButtonUI();
			
			 
			getButtonManager().getButton(IBillButton.Print).setEnabled(
						true);
		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
	}
	
	private MyBillVO changeGentoTbpwb(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		//获取主表VO
		TbProdwaybillVO tbProdwaybillVO = new TbProdwaybillVO();
		
		GeneralBillHeaderVO firstVO = (GeneralBillHeaderVO) vos[0].getParentVO();
		tbProdwaybillVO.setPwb_vbillcode(firstVO.getVbillcode());
		if(null!=firstVO.getAttributeValue("cotherwhid")){
			tbProdwaybillVO.setPwb_cwarehouseid(firstVO.getAttributeValue("cotherwhid").toString());
		}
		tbProdwaybillVO.setPwb_cwhsmanagerid(firstVO.getCwhsmanagerid());
		if(null!=firstVO.getAttributeValue("cothercorpid")){
			tbProdwaybillVO.setPwb_corp(firstVO.getAttributeValue("cothercorpid").toString());
		}
		tbProdwaybillVO.setPwb_cdptid(firstVO.getCdptid());

		if(null!=firstVO.getAttributeValue("cothercalbodyid")){
			tbProdwaybillVO.setPwb_calbody(firstVO.getAttributeValue("cothercalbodyid").toString());
		}
		tbProdwaybillVO.setPwb_cotherwhid(firstVO.getCwarehouseid());
		tbProdwaybillVO.setPwb_cothercorpid(firstVO.getPk_corp());
		tbProdwaybillVO.setPwb_cothercalbodyid(firstVO.getPk_calbody());
//		tbProdwaybillVO.setPwb_cdilivertypeid(firstVO.getCdilivertypeid());
//		tbProdwaybillVO.setPwb_cinventoryid(firstVO.getCdilivertypeid());
		tbProdwaybillVO.setPwb_cbizid(firstVO.getCbizid());
		
		myBillVO.setParentVO(tbProdwaybillVO);
		
		
		
//		reVOHead.setCincbid(firstVO.getCreqcalbodyid());
//		reVOHead.setCinwhid(firstVO.getCreqwarehouseid());
//		reVOHead.setCindeptid(firstVO.getCapplydeptid());
//
//		reVOHead.setCprojectid(firstVO.getCprojectid());
//		reVOHead.setPk_jobobjpha(firstVO.getCprojectphaseid());
//		reVOHead.setIfundsource(firstVO.getMoneysource());
//		if (firstVO.getMoneysource() != null)
//			reVOHead.setCfundsourcedesc(ConstVO.getFundsourceDescName(firstVO
//					.getMoneysource()));

//		reVO.setParentVO(reVOHead);
		//获取子表vo
		String pk=firstVO.getCgeneralhid().toString();
		String sql="select cinvbasid ,cinventoryid,crowno,vbatchcode " +
				",dvalidate,noutnum,ntranoutnum ,nprice,cgeneralbid  from ic_general_b where cgeneralhid='"
			+firstVO.getCgeneralhid().toString()+"' and (noutnum <> ntranoutnum or ntranoutnum is null) and dr=0" ;
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Vector myTemp = new Vector();
		try {
			ArrayList ttcs = (ArrayList) query.executeQuery(sql.toString(),new ArrayListProcessor());
			for (int i = 0; i < ttcs.size(); i++) {
				Object[] gbvo=(Object[])ttcs.get(i);
				TbProdwaybillBVO tbProdwaybillBVO=new TbProdwaybillBVO();
				double noutnum=0.00;
				double ntranoutnum=0.00;
				tbProdwaybillBVO.setPk_customize8(gbvo[8].toString());
				if(null!=gbvo[0]){
					tbProdwaybillBVO.setPwbb_cinvbasid(gbvo[0].toString());
				}
				if(null!=gbvo[1]){
					tbProdwaybillBVO.setPwbb_cinventoryid(gbvo[1].toString());
				}
				
				if(null!=gbvo[2]){
					tbProdwaybillBVO.setPwbb_crowno(Integer.parseInt(gbvo[2].toString()));
				}
				if(null!=gbvo[3]){
					tbProdwaybillBVO.setPwbb_vbatchcode(gbvo[3].toString());	
				}
				String vbcsql="select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
					+tbProdwaybillBVO.getPwbb_cinvbasid()+"' and vbatchcode='"+
					tbProdwaybillBVO.getPwbb_vbatchcode()+"' and dr=0";
				ArrayList vbc=(ArrayList) query.executeQuery(vbcsql,new ArrayListProcessor());
				tbProdwaybillBVO.setPwbb_proddate(new UFDate(((Object[])vbc.get(0))[0].toString()));
				tbProdwaybillBVO.setPwbb_dvalidate(new UFDate(((Object[])vbc.get(0))[1].toString()));
//				if(null!=gbvo[4]){
//					tbProdwaybillBVO.setPwbb_dvalidate(new UFDate(gbvo[4].toString()));
//				}
				if(null!=gbvo[5]){
					noutnum=Double.parseDouble(gbvo[5].toString());
				}
				if(null!=gbvo[6]){
					ntranoutnum=Double.parseDouble(gbvo[6].toString());
				}
				tbProdwaybillBVO.setPwbb_snum(new UFDouble(noutnum-ntranoutnum));
				tbProdwaybillBVO.setPwbb_anum(tbProdwaybillBVO.getPwbb_snum());
				if(null!=gbvo[7]){
					tbProdwaybillBVO.setPwbb_nprice(new UFDouble(gbvo[7].toString()));
					tbProdwaybillBVO.setPwbb_nmny
					(new UFDouble(Double.parseDouble(gbvo[7].toString())*(noutnum-ntranoutnum)));
				}
				
				tbProdwaybillBVO.setStatus(VOStatus.NEW);
				myTemp.add(tbProdwaybillBVO);
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		for (int i = 0; i < vos.length; i++) {
//			NYBalanceVO balanceVO = (NYBalanceVO) vos[i].getParentVO();
//
//			BillItemVO item = new BillItemVO();
//			item.setCsourcetypecode("422Y");
//			item.setCininvid(balanceVO.getCinventoryid());// 存货
//			item.setCastunitid(balanceVO.getCastunitid());// 辅单位
//			item.setNchangerate(balanceVO.getHsl());// 换算率
//			item.setNassistnum(balanceVO.getNassistnum());// 辅数量
//			item.setNprice(balanceVO.getNprice());// 单价
//			item.setNmny(balanceVO.getNmny());// 金额
//			item.setNnum(balanceVO.getNcurrmeetnum()
//					.sub(balanceVO.getExecnum()));// 满足数量-执行数量
//			item.setCsourceid(balanceVO.getCrequireappid());
//			item.setCsourcebid(balanceVO.getCrequireapp_bid());
//
//			// 以下几项在表体设置可否
//			item.setCincbid(balanceVO.getCreqcalbodyid());
//			item.setCinwhid(balanceVO.getCreqwarehouseid());
//			item.setCindeptid(balanceVO.getCapplydeptid());
//
//			vTemp.add(item);
//
//		}
		TbProdwaybillBVO[] tbProdwaybillBVOs=new TbProdwaybillBVO[myTemp.size()];
		tbProdwaybillBVOs=(TbProdwaybillBVO[])myTemp.toArray(tbProdwaybillBVOs);
		
		myBillVO.setChildrenVO(tbProdwaybillBVOs);
//		BillItemVO[] reVOItem = new BillItemVO[vTemp.size()];
//		reVOItem = (BillItemVO[]) vTemp.toArray(reVOItem);
//		reVO.setChildrenVO(reVOItem);

		return myBillVO;

	}
	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDelete();
		getButtonManager().getButton(nc.ui.wds.w80060606.pwButtun.IPwButtun.PwAdd).setEnabled(
				true);
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		getBufferData().clear();
		super.onBoCancel();
		
	}
	
	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
	}
}