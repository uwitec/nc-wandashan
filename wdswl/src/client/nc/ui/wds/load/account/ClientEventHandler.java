package nc.ui.wds.load.account;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog=new ClientUIQueryDlg(	getBillUI(),
					null,
					_getCorp().getPrimaryKey(),
					getBillUI()._getModuleCode(),
					_getOperator(),
					getBillUI().getBusinessType(),
					getBillUI().getNodeKey());
			//queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		//对总费用  分配给班组的费用校验
		ExaggLoadPricVO   billvo= (ExaggLoadPricVO) getBillUI().getVOFromUI();
		LoadpriceHVO h= (LoadpriceHVO) billvo.getParentVO();
		LoadpriceB2VO[] b= (LoadpriceB2VO[]) billvo.getTableVO("wds_loadprice_b2");
	    UFDouble zfee=h.getVzfee();
	    if(h !=null){
	    	UFDouble zbz=new UFDouble();
	    	for(int i=0;i<b.length;i++){
	    		zbz=zbz.add(b[i].getNloadprice());
	    	}
	    	if(zbz!=null){
	    		if(zfee.sub(zbz).intValue()<0){
	    			throw new BusinessException(" 班组装卸费用不能大于总费用");
	    		}
	    	}
	    	
	    }
		
		
		
		super.onBoSave();
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn){
			case ButtonCommon.REFWDS6:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_OUT);
				onBillRef();
				break;
			case ButtonCommon.REFWDS7:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_IN);
				onBillRef();
				break;
			case ButtonCommon.REFWDS8:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_SALE_OUT);
				onBillRef();
				break;
			case ButtonCommon.REFWDS9:
				((ClientUI)getBillUI()).setRefBillType(WdsWlPubConst.BILLTYPE_ALLO_IN);
				onBillRef();
				break;
		}
	}
	@Override
	protected boolean isDataChange() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// 设置单据状态
		
		//项目主键	nloadprice 项目主键	nunloadprice 项目主键	ncodeprice 项目主键	ntagprice
		getBillUI().setCardUIState();
		AggregatedValueObject vo = refVOChange(vos);
		if (vo == null)
			throw new BusinessException("未选择参照单据");
		LoginInforHelper login = new LoginInforHelper();//zhf
		Class[] ParameterTypes = new Class[]{AggregatedValueObject[].class,String.class,String.class};
		Object[] ParameterValues = new Object[]{vos,_getCorp().getPk_corp(),login.getWhidByUser(_getOperator())};
		Object o =LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.wds.load.account.LoadAccountBS", "accoutLoadPrice", ParameterTypes, ParameterValues, 2);
		ExaggLoadPricVO billVo =(ExaggLoadPricVO) o;
	
		//设置表头字段的总费用
	     if(billVo.getTableVO("wds_loadprice_b1")!=null){
	    	 LoadpriceB1VO[]  vss=(LoadpriceB1VO[]) billVo.getTableVO("wds_loadprice_b1");
	    	 UFDouble feess=new UFDouble();
	    	 for(int i=0;i<vss.length;i++){   
	    		 UFDouble fees=new UFDouble();		    	
	    		  fees=PuPubVO.getUFDouble_NullAsZero(vss[i].getNloadprice())
	    		 .add(PuPubVO.getUFDouble_NullAsZero(vss[i].getNunloadprice()))
	    		 .add(PuPubVO.getUFDouble_NullAsZero(vss[i].getNcodeprice()))
	    		 .add(PuPubVO.getUFDouble_NullAsZero(vss[i].getNtagprice()));
	    		 feess=feess.add(fees);
	    	 }
//	    	if(billVo.getParentVO()!=null){
//	    		LoadpriceHVO l=	(LoadpriceHVO)(billVo.getParentVO());
//	    	    l.setVzfee(feess);
//	       }	    	 
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vzfee", feess);	
		// 设置为新增处理
		getBillUI().setBillOperate(IBillOperate.OP_REFADD);
		// 填充界面
		getBillCardPanelWrapper().setCardData(billVo);
		//设置界面默认值
		((ClientUI)getBillUI()).setRefDefalutData();
	}
	

 }
} 