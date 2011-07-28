package nc.ui.wds.ic.so.out;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.bill.BillListPanelWrapper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.ScaleKey;
import nc.vo.ic.pub.ScaleValue;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 *  0 保管员 1 信息科 2 发运科 3内勤
 *  
 * 	用户类型0---------可以进行出库
	用户类型1---------为可以签字的用户
	用户类型2---------修改操作
	用户类型3---------所有权限
 */

public class MySaleEventHandler extends OutPubEventHandler {

	private LoginInforHelper login=null;
	protected nc.ui.pub.print.PrintEntry m_print = null;
	
	protected ScaleValue m_ScaleValue=new ScaleValue();
	protected ScaleKey m_ScaleKey=new ScaleKey();
	public MySaleEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(
				getBillUI(),null,_getCorp().getPk_corp(),getBillUI().getModuleCode(),_getOperator(),null);
	}

	@Override
	protected String getHeadCondition() {
		return " pk_corp = '"+_getCorp().getPrimaryKey()+"' and isnull(dr,0) = 0 and vbilltype = '"+WdsWlPubConst.BILLTYPE_SALE_OUT+"' ";
	}


	private LoginInforHelper getLoginInfoHelper(){
		if(login==null){
			login=new LoginInforHelper();
		}
		return login;
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
			case ISsButtun.tpzd://托盘指定(手动拣货)
				//拣货 存货唯一校验
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"存货编码","批次号"});
				ontpzd();
				
				break;
			case ISsButtun.zdqh://自动取货
				//拣货 存货唯一校验
				BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(),
						getBillCardPanelWrapper().getBillCardPanel().getBillModel(),
						new String[]{"ccunhuobianma","batchcode"},
						new String[]{"存货编码","批次号"});
				onzdqh();
				
				break;
			case ISsButtun.ckmx://查看明细
				onckmx();
				break;
			case ISsButtun.Qxqz://取消签字
				onQxqz();
				break;
			case ISsButtun.Qzqr://签字确认
				onQzqr();
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefSoOrder://参照销售运单
				((MyClientUI) getBillUI()).setRefBillType(WdsWlPubConst.WDS5);
					onBillRef();
					setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
					//如果 参照的出库仓库为空 设置默认仓库为当前保管员仓库
					setInitWarehouse("srl_pk");
				break;
			case nc.ui.wds.w80020206.buttun0206.ISsButtun.RefRedSoOrder://参照销售运单
				((MyClientUI) getBillUI()).setRefBillType("30");
					onBillRef();
					setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
					//如果 参照的出库仓库为空 设置默认仓库为当前保管员仓库
					setInitWarehouse("srl_pk");
				break;
		}
	}

	
	@Override
	public void onBillRef() throws Exception {
		super.onBillRef();
		//当前库管员绑定的货位
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setValue(getLoginInfoHelper().getSpaceByLogUserForStore(_getOperator()));
		//库管理员赋值
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cwhsmanagerid").setValue(_getOperator());	
		//给表体业务日期赋值
		int rowCounts=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for(int i=0;i<rowCounts;i++){			
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), i, "dbizdate");
	    	getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(getPk_cargDoc(), i, "cspaceid");

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
	
	/**
	 * 更新列表数据
	 */
	protected void updateListVo(int[] rows) throws java.lang.Exception {
		CircularlyAccessibleValueObject vo = null;
		if(rows!=null && rows.length >= 0){
			for(int i : rows){
				vo = getBufferData().getVOByRowNo(i).getParentVO();
				getBillListPanelWrapper().updateListVo(vo,i);
			}
		}
	}
	protected BillListPanelWrapper getBillListPanelWrapper() {
		return getBillManageUI().getBillListWrapper();
	} 
	// 签字确认
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("正在执行签字...");
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
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
		super.onBoPrint();
		Integer iprintcount =PuPubVO.getInteger_NullAs(getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").getValueObject(), 0) ;
		iprintcount=++iprintcount;
		getBillCardPanelWrapper().getBillCardPanel().getTailItem("iprintcount").setValue(iprintcount);
		getBufferData().getCurrentVO().getParentVO().setAttributeValue("iprintcount", iprintcount);
		HYPubBO_Client.update((SuperVO)getBufferData().getCurrentVO().getParentVO());
	}
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		setInitByWhid(new String[]{"srl_pk","pk_cargdoc"});
	}
	
	//
	
}