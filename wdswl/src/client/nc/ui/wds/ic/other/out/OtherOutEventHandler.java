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
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
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
				ontpzd();
				break;
			case ISsButtun.zdqh:
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
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefWDSC:
				((MyClientUI)getBillUI()).setRefBillType(WdsWlPubConst.WDSC);
				onBillRef();
				break;
			}
	}
	@Override
	public void onBillRef() throws Exception {
		super.onBillRef();
		//设置单据号
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vbillno", ((MyClientUI)getBillUI()).getBillNo());
		//增行按钮去掉
		ButtonObject btnobj = getBillUI().getButtonManager().getButton(IBillButton.AddLine);
		if (btnobj != null) {
			btnobj.setEnabled(false);
			btnobj.setHint("参照不允许增行");
		}
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

}