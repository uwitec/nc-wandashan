package nc.ui.wds.ic.other.out;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.BeforeSaveValudate;
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

/**
 * 其他出库
 * @author author
 * @version tempProject version
 */

public class OtherOutEventHandler extends OutPubEventHandler {


	
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
				//拣货 存货唯一校验
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"存货编码","批次号"});
				ontpzd();
				
				break;
			case ISsButtun.zdqh:
				//拣货 存货唯一校验
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"存货编码","批次号"});
				onzdqh();
				
				break;
			case ISsButtun.ckmx:
				onckmx();
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
				//设置 仓库和货位的是否可编辑，总仓可以，分仓不可以
				setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
				//设置参照出库中出库仓库为空，则赋值默认仓库为当前操作员仓库
				setInitWarehouse("srl_pk");
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefWDSC:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDSC);
				onBillRef();
				setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
				setInitWarehouse("srl_pk");
				break;
			}
	}
	@Override
	public void onBillRef() throws Exception {
		super.onBillRef();
		//设置单据号
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillno", ((MyClientUI)getBillUI()).getBillNo());
//		AggregatedValueObject[] vos = PfUtilClient.getRetOldVos();
//		AggregatedValueObject vo = vos[0];
//		CgqyHVO headVO = (CgqyHVO) vo.getParentVO();
//		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vnote", "取货单位："+
//				//headVO.getAttributeValue("ccusmandoc")+
//				";取货人："
//				//+vo.getParentVO().getAttributeValue("ccustomer")
//				);
		//增行按钮去掉
		ButtonObject btnobj = getBillUI().getButtonManager().getButton(IBillButton.AddLine);
		if (btnobj != null) {
			btnobj.setEnabled(false);
			btnobj.setHint("参照不允许增行");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("is_yundan", null);
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
	/**
	 * 给界面上一些属性赋值
	 */
	private void setViewPro() throws BusinessException {
		// 设置制单人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
		"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 设置修改人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
		"clastmodiid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 单据日期
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
		"dbilldate").setValue(_getDate().toString());
		//		if (isControl == 3) {
		// 设置收发类别和备注可编辑
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		"cdispatcherid").setEnabled(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vnote")
		.setEnabled(true);
		// 部门
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
		.setEnabled(true);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cdptid")
		.setValue("1021B110000000000BN9");
		// 库管员
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		"cwhsmanagerid").setEnabled(true);
		ui.updateButtons();
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
			getBillManageUI().showErrorMessage("签字失败！");
		}
	}
	
	
	
	
	
	@Override
	protected void onBoSave() throws Exception {
		//对贴签数量    小于    实入数量的校验
		if( getBillUI().getVOFromUI().getChildrenVO()!=null){
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
				int retu = getBillManageUI().showOkCancelMessage("确认取消签字?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbOutgeneralHVO generalh = (TbOutgeneralHVO)aObject.getParentVO();
				if(generalh.getFisload() != null &&generalh.getFisload().equals(UFBoolean.TRUE)){
					getBillUI().showWarningMessage("已经形成装卸费核算单，不能取消签字");
					return ;
				}
				if(generalh.getFistran() != null &&generalh.getFistran().equals(UFBoolean.TRUE)){
					getBillUI().showWarningMessage("已经形成运费核算单，不能取消签字");
					return ;
				}
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
			getBillManageUI().showErrorMessage("取消签字失败！");
		}
	}

	protected void onzzdj(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
	}
	
	
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
	}

	private  EventHandlerTools getEventHanderTools(){
		if(tools == null){
			return new EventHandlerTools();
		}
		return tools;
	}

	@Override
	protected void onBoPrint() throws Exception {
		super.onBoPrint();
		Integer iprintcount =PuPubVO.getInteger_NullAs(getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").getValueObject(), 0) ;
		iprintcount=++iprintcount;
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").setValue(iprintcount);
		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
	
	}
	
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
		super.onBoEdit();
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
		
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