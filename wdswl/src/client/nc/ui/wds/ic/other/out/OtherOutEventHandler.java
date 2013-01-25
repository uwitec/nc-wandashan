package nc.ui.wds.ic.other.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.uif.pub.exception.UifException;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 其他出库
 * @author author
 * @version tempProject version
 */
public class OtherOutEventHandler extends OutPubEventHandler {

	private String vbillno = getBillUI().getBillField().getField_BillNo(); // 单据号字段
	
	private EventHandlerTools tools;
	
	public OtherOutEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.zzdj:
				onzzdj(null);
				break;
			case ISsButtun.tpzd:
				valudateWhereYeqian();
				//拣货 存货唯一校验
//				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
//						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
//						new String[]{"ccunhuobianma","batchcode"},
//						new String[]{"存货编码","批次号"});
			///	ontpzd();
				
				break;
			case ISsButtun.zdqh:
				valudateWhereYeqian();
				onzdqh();
				
				break;
			case ISsButtun.ckmx:
			//	onckmx();
				break;
			case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz:
				onQxqz();
				break;
			case nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr:
				onQzqr();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSendOrder:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDS3);
				onBillRef();				
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefWDSC:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDSC);
				onBillRef();
				
				setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
				setInitWarehouse("srl_pk");
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefHWTZ:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.HWTZ);
				onBillRef();	
				getButtonManager().getButton(ISsButtun.zdqh).setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem("ss_state").setEdit(false);
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.tsyd:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDSS);
				onBillRef();	
				//getButtonManager().getButton(ISsButtun.zdqh).setEnabled(false);
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem("ss_state").setEdit(false);
				break;	
			}
	}
	
	/**
	 * 拣货对应的页签
	 * 必须在出库子表页签
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-9-22下午02:56:45
	 */
    private void valudateWhereYeqian()throws Exception{
	   String tablecode=getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableCode();
	   if(!"tb_outgeneral_b".equalsIgnoreCase(tablecode)){
		 throw new Exception("请选择表体存货页签");   
	   }
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// TODO Auto-generated method stub
		super.setRefData(vos);
		//设置 仓库和货位的是否可编辑，总仓可以，分仓不可以
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
		//设置参照出库中出库仓库为空，则赋值默认仓库为当前操作员仓库
		setInitWarehouse("srl_pk");
		//设置单据号
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillno", ((MyClientUI)getBillUI()).getBillNo());
	 //	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("is_yundan", null);
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
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and vbilltype = '"+WdsWlPubConst.BILLTYPE_OTHER_OUT+"' ";
	}

	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
	    BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),WdsWlPubConst.BILLTYPE_OTHER_OUT, "crowno");
	    
	}
	
	// 签字确认
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("执行签字...");
				int retu = getBillManageUI().showOkCancelMessage("确认签字?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
				generalh.setVbillstatus(IBillStatus.CHECKPASS);// 审批状态
				generalh.setCregister(_getOperator());// 签字人主键
				generalh.setTaccounttime(getBillUI()._getServerTime().toString());// 签字时间
				generalh.setQianzidate(_getDate());// 签字日期
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//登录人
				//动作脚本
				PfUtilClient.processAction(getBillManageUI(), "SIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				//更新缓存
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("签字失败:"+e.getMessage());
		}
	}
	
	
	
	
	
	@Override
	protected void onBoSave() throws Exception {
		//对贴签数量    小于    实入数量的校验
		if( getBillUI().getVOFromUI().getChildrenVO()!=null){
			TbOutgeneralHVO head = (TbOutgeneralHVO) getBillUI().getVOFromUI().getParentVO();
			String cdispatchid = WdsWlPubTool.getString_NullAsTrimZeroLen(head.getCdispatcherid()) ;
			String srl_pkr = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pkr());
			if(WdsWlPubConst.cklb_zhwku.equalsIgnoreCase(cdispatchid)){
				if(srl_pkr == null){
					throw new BusinessException("转货位出库，入库仓库必填");
				}
			}else{
				if(srl_pkr != null){
					throw new BusinessException("转货位出库，入库仓库不能录入");
				}
			}
			TbOutgeneralBVO[] tbs=(TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
			for(int i=0;i<tbs.length;i++){
				UFDouble u1=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutassistnum());
				UFDouble u2=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNtagnum());
				if(u1.sub(u2).doubleValue()<0){
					throw new BusinessException("贴签数量   不能大于 实入数量");
				}
			}			
		}
		super.onBoSave();
	}

	// 取消签字
	protected void onQxqz() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("正在执行取消签字...");
				int retu = getBillManageUI().showOkCancelMessage("取消签字会删除下游装卸费核算单，是否确认取消签字?");
				if (retu != UIDialog.ID_OK) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
//				if(generalh.getFisload() != null &&generalh.getFisload().equals(UFBoolean.TRUE)){
//					getBillUI().showWarningMessage("已经形成装卸费核算单，不能取消签字");
//					return ;
//				}
//				if(generalh.getFistran() != null &&generalh.getFistran().equals(UFBoolean.TRUE)){
//					getBillUI().showWarningMessage("已经形成运费核算单，不能取消签字");
//					return ;
//				}
				generalh.setVbillstatus(IBillStatus.FREE);// 自由状态
				generalh.setCregister(null);// 签字人主键
				generalh.setTaccounttime(null);// 签字时间
				generalh.setQianzidate(null);// 签字日期
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//登录人
				//动作脚本
				PfUtilClient.processAction(getBillManageUI(), "CANELSIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				//更新缓存
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("取消签字失败:"+e.getMessage());
		}
	}

	protected void onzzdj(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		getButtonManager().getButton(ISsButtun.fzgn).setEnabled(true);
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
	}

	private  EventHandlerTools getEventHanderTools(){
		if(tools == null){
			return new EventHandlerTools();
		}
		return tools;
	}

//	@Override
//	protected void onBoPrint() throws Exception {
//		super.onBoPrint();
//		Integer iprintcount =PuPubVO.getInteger_NullAs(getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").getValueObject(), 0) ;
//		iprintcount=++iprintcount;
//		getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").setValue(iprintcount);
//		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
//		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
//	
//		
//	}
	
	/**
	 * zhf add  不支持修改时 行操作
	 */
	protected void onBoEdit() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vuserdef15"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("用于调整的出库单不能修改");
			return;
		}
        valuteOrder();
			
		super.onBoEdit();
		
		
		
		
		
		
		
		
		
//		zhf add
	//	setHeadPartEdit(false);
//		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
//		getBillUI().updateButtons();
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
		
	}

	private void valuteOrder() throws Exception {
		AggregatedValueObject  vo=getBufferData().getCurrentVO();
		SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
		if(vos==null || vos.length==0)
			return;
		  SuperVO bvo=(SuperVO) vos[0];
		  String csid=PuPubVO.getString_TrimZeroLenAsNull(bvo.getAttributeValue("csourcebillhid"));
		  //查询发运订单看是否已经冻结
			 SuperVO[] ovs=  HYPubBO_Client.queryByCondition(SendorderVO.class, " isnull(dr,0)=0 and pk_sendorder='"+csid+"'");
			 if(ovs==null || ovs.length==0)
				 return;
			 SendorderVO head=(SendorderVO) ovs[0];
			 UFBoolean isdj=PuPubVO.getUFBoolean_NullAs(head.getFisended(), new UFBoolean(false));
			 if(isdj.booleanValue()==true){
				 throw new Exception("上游运单已经冻结 ,不能作废");
			 }
	
}

	@Override  //xjx  add   打印
	protected void onBoPrint() throws Exception {
		//　如果是列表界面，使用ListPanelPRTS数据源
		if( getBillManageUI().isListPanelSelected() ){
			nc.ui.pub.print.IDataSource dataSource = new MyListDateSource(
					getBillUI()._getModuleCode(), ((BillManageUI) getBillUI())
					.getBillListPanel());
			nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
					dataSource);
			print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
					._getModuleCode(), getBillUI()._getOperator(), getBillUI()
					.getBusinessType(), getBillUI().getNodeKey());
			if (print.selectTemplate() == 1)
				print.preview();
		}else{
		final nc.ui.pub.print.IDataSource dataSource = new MyDuoDateSource(
				getBillUI()._getModuleCode(), getBillCardPanelWrapper()
						.getBillCardPanel());
		final nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
				null, dataSource);
		print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
				getBillUI()._getModuleCode(), getBillUI()._getOperator(),
				getBillUI().getBusinessType(), getBillUI().getNodeKey());
		if (print.selectTemplate() == 1)
			print.preview();
		// 更改数据源，支持托盘打印
	//	super.onBoPrint();
		}
		Integer iprintcount = PuPubVO.getInteger_NullAs(getBufferData()
				.getCurrentVO().getParentVO().getAttributeValue(
						"cdt_pk"), 0);
		iprintcount = iprintcount + 1;
		getBufferData().getCurrentVO().getParentVO().setAttributeValue(
				"iprintcount", iprintcount);
		try {
			HYPubBO_Client.update((SuperVO) getBufferData().getCurrentVO()
					.getParentVO());
			onBoRefresh();
		} catch (final UifException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   
	   }
	}
	
	protected void onBoDel() throws Exception {
		if (getBufferData().getCurrentVO() == null)
			return;
		UFBoolean isadjust = PuPubVO.getUFBoolean_NullAs(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vuserdef15"), UFBoolean.FALSE);
		if(isadjust.booleanValue()){
			getBillUI().showHintMessage("用于调整的出库单不能删除");
			return;
		}
		super.onBoDel();
		
	}

	
}