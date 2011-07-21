package nc.ui.zb.gen;

import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.msg.PfLinkData;
import nc.ui.trade.query.HYQueryDLG;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.po.OrderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.gen.GenOrderVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class GenrationEventHandler implements BillEditListener,BillEditListener2{

	private GenrationOrderUI ui = null;
	
	private List<GenOrderVO> lseldata = new ArrayList<GenOrderVO>();
	
	public GenrationQueryDlg queryDialog = null;

	@SuppressWarnings("deprecation")
	protected UIDialog getQueryDialog() {
		if (queryDialog == null) {
			queryDialog = new GenrationQueryDlg(ui,null,ClientEnvironment.getInstance().getCorporation().getPk_corp(),
					ZbPubConst.GENORDER_MODLUECODE,ClientEnvironment.getInstance().getUser().getPrimaryKey(), null, null);
			queryDialog.setTempletID(ZbPubConst.GENORDER_DIALOG_ID);
		}
		return queryDialog;
	}
	
	public GenrationEventHandler(GenrationOrderUI ui){
		super();
		this.ui = ui;
	}	
	
	private void clearCache(){
		lseldata.clear();
	}
	
	public void onAllSel(){
		if(getDataBuffer() == null||getDataBuffer().length == 0)
			return;
		GenOrderVO[] datas = getDataBuffer();
		clearCache();
		for(GenOrderVO data:datas){
			lseldata.add(data);
		}
		
		int rowcount = getDataPanelSel().getRowCount();
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.TRUE, i, "bsel");
		}
	}
	public void onNoSel(){
		int rowcount = getDataPanelSel().getRowCount();
		if(rowcount <= 0)
			return;
		for(int i=0;i<rowcount;i++){
			getDataPanelSel().setValueAt(UFBoolean.FALSE, i, "bsel");
		}
		clearCache();
	}
	
	public void onQuery(){

		//查询   
		if(getQueryDialog().showModal() != UIDialog.ID_OK)
			return;
		String whereSql =  ((HYQueryDLG) getQueryDialog()).getWhereSQL();
		if(!whereSql.contains("h.ccustmanid")&& !whereSql.contains("h.temp")){
			showErrorMessage("请先选择一个供应商");
			return;
		}
		GenOrderVO[] billdatas = null; 
		try{
			billdatas = GenrationOrderHealper.queryDatas(whereSql, ui.cl);
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
		clearCache();
		//缓存  界面
		getDataPanelSel().setBodyDataVO(billdatas);
		int i=0;
		for(GenOrderVO data:billdatas){
				if(PuPubVO.getString_TrimZeroLenAsNull(data.getAttributeValue("ccustbasid")) ==null ||PuPubVO.getString_TrimZeroLenAsNull(data.getAttributeValue("ccustbasid")).equalsIgnoreCase("null")){
					String[] formulas = new String[]{
							"custname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
							"custcode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
							};
					getDataPanelSel().execFormulas(i, formulas);
				}else{
					String[] formulas = new String[]{
							"custname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
							"custcode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
							};
					getDataPanelSel().execFormulas(i, formulas);
				}
			i++;
		}
		getDataPanelSel().execLoadFormula();
		setDataBuffer(billdatas);
		showHintMessage("查询完成");
	}


	public void onGenOrder(){
		if(lseldata.size() == 0){
			showWarnMessage("请选中要合并的数据");
			return;
		}		
		OrderVO vo=null;
		try {
			vo = (OrderVO)GenrationOrderHealper.dealVO(lseldata);
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return;
		}
		PfLinkData linkData = new PfLinkData();
		linkData.setUserObject(vo);
		linkData.setSourceBillType(ZbPubConst.ZB_Result_BILLTYPE);
		SFClientUtil.openLinkedADDDialog("4004020201", ui, linkData);	
		
		List<GenOrderVO> ldata = new ArrayList<GenOrderVO>();
		GenOrderVO[] buffers = getDataBuffer();
		for(GenOrderVO buffer:buffers){
			ldata.add(buffer);
		}
		
		ldata.removeAll(lseldata);
		lseldata.clear();
		
		if(ldata.size() == 0){
			setDataBuffer(null);				
		}else
			setDataBuffer(ldata.toArray(new GenOrderVO[0]));
		ui.update();	
	}

	public boolean beforeEdit(BillEditEvent e) {
		
		return false;
	}
	
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if(e.getKey().equalsIgnoreCase("bsel")){
			UFBoolean bsel = PuPubVO.getUFBoolean_NullAs(getDataPanelSel().getValueAt(row, "bsel"), UFBoolean.FALSE);
			if(bsel.booleanValue()){
			     lseldata.add(getDataBuffer()[row]);
			}else{
				lseldata.remove(getDataBuffer()[row]);
				
			}   
		}
		
	}
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		
	}
	private BillModel getDataPanelSel(){
		return ui.getBillPanel().getBillModel();
	}
	
	
	private GenOrderVO[] getDataBuffer(){
		return ui.getDataBuffer();
	}
	
	private void setDataBuffer(GenOrderVO[] datas){
		ui.setDataBuffer(datas);
	}
	private void showErrorMessage(String msg){
		ui.showErrorMessage(msg);
	}
	private void showWarnMessage(String msg){
		ui.showWarningMessage(msg);
	}
	private void showHintMessage(String msg){
		ui.showHintMessage(msg);
	}

	

}
