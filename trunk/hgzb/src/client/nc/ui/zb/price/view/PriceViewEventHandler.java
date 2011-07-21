package nc.ui.zb.price.view;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.vo.pub.BusinessException;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class PriceViewEventHandler{

	private PriceViewUI ui = null;
	
	public PriceViewQueryDlg queryDialog = null;

	protected UIDialog getQueryDialog() {
		if (queryDialog == null) {
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setCurrentCorpPk(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setFunNode(ZbPubConst.ZB_SUBMIT_VIEW_FUNDCODE);
			tempinfo.setUserid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
			queryDialog = new PriceViewQueryDlg(ui, null, tempinfo);
		}
		return queryDialog;
	}

	
	
	public PriceViewEventHandler(PriceViewUI ui){
		super();
		this.ui = ui;
	}	
	
	public void onQuery(){

		//查询   
		if(getQueryDialog().showModal() != UIDialog.ID_OK)
			return;
		String whereSql =  ((HYQueryConditionDLG) getQueryDialog()).getWhereSQL();
		if(whereSql !=null){
			if (whereSql.contains("sp.isubmittype = 'N'")) {
				whereSql= whereSql.replace("sp.isubmittype = 'N'","sp.isubmittype = 0 or sp.isubmittype = 1 or sp.isubmittype = 2");
			} else if(whereSql.contains("sp.isubmittype = 'Y'")){
				whereSql= whereSql.replace("sp.isubmittype = 'Y'","sp.isubmittype = 3");
			}
		}
			
		
		SubmitPriceVO[] billdatas = null; 
		try{
			billdatas = PriceViewHealper.queryDatas(whereSql, ui.cl);
		}catch(Exception e){
			e.printStackTrace();
			showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}		
		if(billdatas == null||billdatas.length == 0){
			ui.clearData();
			showHintMessage("查询完成：没有满足条件的数据");
			return;
		}
		
		ui.clearData();
		//处理查询出的计划  缓存  界面
		getDataPanelSel().setBodyDataVO(billdatas);
		int i=0;
		for(SubmitPriceVO data:billdatas){
			try {
				Object o =ZbPubTool.execFomularClient("getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cvendorid)", new String[]{"cvendorid"},new String[]{data.getCvendorid()});
				if(PuPubVO.getString_TrimZeroLenAsNull(o)==null){
					String[] formulas = new String[]{
							"custname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,cvendorid)",
							"vendorcode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,cvendorid)"
							};
					getDataPanelSel().execFormulas(i, formulas);
				}else{
					getDataPanelSel().setValueAt(o,i,"ccustbasid");
					String[] formulas = new String[]{
							"custname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
							"vendorcode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
							};
					getDataPanelSel().execFormulas(i, formulas);
				}
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			}
			i++;
		}
		getDataPanelSel().execLoadFormula();
		setDataBuffer(billdatas);
		showHintMessage("查询完成");
	}
	
	public void onPrint(){
		nc.ui.pub.print.IDataSource dataSource = 
			new CardPanelPRTS(ZbPubConst.ZB_SUBMIT_VIEW_FUNDCODE,ui.getBillPanel());
		nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,dataSource);
		print.setTemplateID(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(),
				ZbPubConst.ZB_SUBMIT_VIEW_FUNDCODE,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(), 
				null,null);
		if (print.selectTemplate() == 1)
			print.preview();
	}
	
	private BillModel getDataPanelSel(){
		return ui.getBillPanel().getBillModel();
	}
	
	private void setDataBuffer(SubmitPriceVO[] datas){
		ui.setDataBuffer(datas);
	}
	private void showErrorMessage(String msg){
		ui.showErrorMessage(msg);
	}
	private void showHintMessage(String msg){
		ui.showHintMessage(msg);
	}
}
