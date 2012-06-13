package nc.ui.wds.tranprice.transmil;

import java.util.ArrayList;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.SingleBodyEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.wds.tranprice.transmil.TransmilVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class ClientEventHandler extends SingleBodyEventHandler {
	public ExcelDialog m_eDlg = null;

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
		
	}
	
	@Override
	protected UIDialog createQueryUI() {	
		return new QueryDIG(getBillUI(), null, getBillUI()._getCorp().getPrimaryKey(), WdsWlPubConst.TRANS_MIL_NODECODE, getBillUI()._getOperator(), null);
	}

	@Override
	protected void beforeSaveValudate() throws Exception {	
		BeforeSaveValudate.beforeSaveBodyUnique(getBillCardPanelWrapper().getBillCardPanel().getBillTable(), getBillCardPanelWrapper().getBillCardPanel().getBillModel(), new String[]{"pk_relocation","pk_delocation"},new String[]{"发货地点","收货地点"});
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getRowCount()-1;
		if(selectRow > -1){
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getOperator(), selectRow,"pk_firstreperson" );
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(getBillUI()._getServerTime().toString(),selectRow,"firstretime");
		}
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel()
		.getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(),selectRow,"pk_lastreperson");
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(getBillUI()._getServerTime().toString(), selectRow,"lastretime");
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();	
	}
	protected void onBoImport() throws Exception {
		TransmilVO[] voaImport = null;
		try {
			ArrayList<String[]> list = new ArrayList<String[]>();
			String[] sFieldName1 = new String[]{"发货地","pk_delocation"};
			String[] sFieldName2 = new String[]{"收货地","pk_relocation"};
			String[] sFieldName3 = new String[]{"公里数","mileage"};			
			list.add(sFieldName1);
			list.add(sFieldName2);
			list.add(sFieldName3);
			getM_eDlg().setVOName(TransmilVO.class.getName());
			getM_eDlg().setCHandENnames(list.toArray(new String[0][0]));
			getM_eDlg().setckbFirstRowSelected(true);
			getM_eDlg().getUIltlWorkBook().setLeftData(null);//设置待选工作薄为空
			getM_eDlg().getUIltlWorkBook().setRightData(null); //设置已选工作薄为空
			getM_eDlg().getUIpnlField().getBillModel().setBodyDataVO(null);//设置EXCEL范例名为空
			getM_eDlg().showModal();			
			if (getM_eDlg().isExportOK()) {
				voaImport = (TransmilVO[])getM_eDlg().getExportVO();
				if (voaImport == null || voaImport.length == 0 || voaImport[0] == null) {
					nc.ui.pub.beans.MessageDialog.showErrorDlg(getBillUI(), "错误", "Excel文件内容为空！");
					return;
				}
			}else {
				return;
			}
		}catch (Exception e) {
			nc.ui.pub.beans.MessageDialog.showErrorDlg(getBillUI(), "导入Excel文件出错！", e.getMessage());
			throw new RuntimeException(e);
		}
		//匹配
		voaImport = onPipei(voaImport);
		//新增
		onAdd(voaImport);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	public TransmilVO[] onPipei(TransmilVO[] voaImport)throws Exception {
		TransmilVO[] temp = null;
		if(voaImport != null && voaImport.length>0){
			Class[] ParameterTypes = new Class[]{TransmilVO[].class,String.class};
			Object[] ParameterValues = new Object[]{voaImport,_getOperator()};
			Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, getBillUI(), 
					"正在匹配...", 1, "nc.bs.wds.tranprice.transmil.JsdPiPei", null, 
					"queryAllpipeiData", ParameterTypes, ParameterValues);
			temp = (TransmilVO[])o;
		}
		return temp;
	}
	
	public void onAdd(SuperVO[] voaImport) throws Exception{
		getBillUI().setBillOperate(IBillOperate.OP_ADD);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(voaImport);
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulaByKey("pk_costtype");//
	}
	public ExcelDialog getM_eDlg() {
		if(m_eDlg==null){
			m_eDlg = new ExcelDialog(getBillUI());
		}
		return m_eDlg;
	}
	
}
