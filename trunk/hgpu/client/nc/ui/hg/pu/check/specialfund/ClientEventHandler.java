package nc.ui.hg.pu.check.specialfund;

import nc.ui.hg.pu.check.pub.CheckHelper;
import nc.ui.hg.pu.pub.DBTManageEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class ClientEventHandler extends DBTManageEventHandler {



	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	
	}
	@Override
	protected UIDialog createQueryUI() {
		return new ClientQueryDLG(getBillUI(), null,
				_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
				_getOperator(), null, null);
	}
	
	private void setBodyItemsEdit(){
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("customer").setEdit(true);
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("imonth").setEdit(true);
	}
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		 setBodyItemsEdit();
		onBoLineAdd();
	}
	
	protected void onBoLineAdd() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		super.onBoLineAdd();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
		UFDate billdate = getBillUI()._getDate();
//		String month=PuPubVO.getString_TrimZeroLenAsNull(billdate.getMonth()-1);
//		String year=PuPubVO.getString_TrimZeroLenAsNull(billdate.getYear());
		PlanApplyInforVO appinfor = ((ClientUI)getBillUI()).m_appInfor;
		if(appinfor==null)
			throw new BusinessException("获取申请信息失败");
		
		if(appinfor.getM_pocorp().equalsIgnoreCase(this._getCorp().getPrimaryKey())){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(true);
		}else {
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("customer").setEdit(false);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(this._getCorp().getPrimaryKey(), selectRow, "pk_corp");//赋值当前公司
		}
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("Y", selectRow, "fcontrol");//是否控制赋值
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "coperatorid");//录入人
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmakedate");//录入日期
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(HgPubConst.FUND_CHECK_SPECIALFUND, selectRow, "ifundtype");//单据类型
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(this._getCorp().getPrimaryKey(), selectRow, "vdef2");//赋值当前公司  用于查询
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	@Override
	public void onBoEdit() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
		setBodyItemsEdit();
		super.onBoEdit();
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), selectRow, "cmodifyman");//修改人
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), selectRow, "dmodifydate");//修改日期
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		String corp = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"pk_corp"));
		String deptname = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"deptname"));
		if(!isNULL(corp)){
			if(_getCorp().getPrimaryKey().equalsIgnoreCase(corp)){//如果登录公司和所选公司不等  部门不可编辑
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(true);
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
			}else{
				getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(false);
			}
		}else if(!isNULL(deptname)){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
		}
		
		Object nlockfund =getBodyCelValue(selectRow,"nlockfund");//预扣
		Object nactfund =getBodyCelValue(selectRow,"nactfund");//实扣
		if(PuPubVO.getUFDouble_ZeroAsNull(nlockfund) != null || PuPubVO.getUFDouble_ZeroAsNull(nactfund) != null){
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("deptname").setEdit(false);
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("corp").setEdit(false);
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("customer").setEdit(false);
			getBillCardPanelWrapper().getBillCardPanel().getBillData().getBodyItem("imonth").setEdit(false);
		}
	}

	private void onCheckBeforeSave() throws BusinessException{
	
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if(selectRow<0){
			return;
		}
		String oldid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"corp"));
		String newid = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"customer"));
		String deptname = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"deptname"));
		String imonth = PuPubVO.getString_TrimZeroLenAsNull(getBodyCelValue(selectRow,"imonth"));
		if(PuPubVO.getString_TrimZeroLenAsNull(imonth)==null)
			throw new BusinessException("月份不可为空");
		if(isNULL(oldid) && isNULL(newid) && isNULL(deptname)){
			 throw new BusinessException("{公司 部门 客户}不可同时为空");
		}else if(!isNULL(oldid) && !isNULL(newid)&& isNULL(deptname)){
			 throw new BusinessException("{公司 部门 客户}不可同时存在");
		}
		UFDouble nfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nfund"));//资金
		UFDouble nlockfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nlockfund"));//预扣
		UFDouble nactfund =PuPubVO.getUFDouble_NullAsZero(getBodyCelValue(selectRow,"nactfund"));//实扣
		if(nfund.compareTo(UFDouble.ZERO_DBL)< 0){
			throw new BusinessException("资金不能小于零");
		}else{
			if(nfund.compareTo(nlockfund.add(nactfund))<0){
				throw new BusinessException("资金不能小于实扣与预扣之和");
			}
		}
	}
	
	private Object getBodyCelValue(int row,String sitemname){
		return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row,sitemname);
	}
	
	private boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
	
	public void onSave() throws Exception{
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		FUNDSETVO bvo =(FUNDSETVO)getBillCardPanel().getBillModel().getBodyValueRowVO(row,FUNDSETVO.class.getName());
		FUNDSETVO[] bvos = new FUNDSETVO[]{bvo};
		FUNDSETVO after =CheckHelper.onSave(bvos,true);
		// 设置保存后状态
		getBillCardPanel().getBillModel().setBodyRowVO(after, row);
		setSaveOperateState();
		
	}
	
	public BillCardPanel getBillCardPanel(){
		return getBillCardPanelWrapper().getBillCardPanel();
	}
	
	protected void onBoDelete() throws Exception {
		int selectRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		Object nlockfund =getBodyCelValue(selectRow,"nlockfund");//预扣
		Object nactfund =getBodyCelValue(selectRow,"nactfund");//实扣
		if(PuPubVO.getUFDouble_ZeroAsNull(nlockfund) != null && PuPubVO.getUFDouble_ZeroAsNull(nactfund) != null)
			throw new BusinessException("存在着预扣或实扣不能删除");
		
		onDelete();
		setSaveOperateState();
		
	}
	
	protected void onDelete() throws Exception {
		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		if (rows.length < 0) {
			MessageDialog.showHintDlg(getBillUI(), "删除", "请选中要删除的行!");
			return;
		}
		if (MessageDialog.showYesNoDlg(getBillUI(), "删除", "是否确认删除选中的数据?") != UIDialog.ID_YES) {
			return;
		}
		
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		FUNDSETVO[] bvos =new FUNDSETVO[rows.length];
		for(int i=0;i<rows.length;i++){
			bvos[i] =(FUNDSETVO)getBillCardPanel().getBillModel().getBodyValueRowVO(rows[i],FUNDSETVO.class.getName());
		}
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().delLine(rows);
		
		try {
			CheckHelper.onSave(bvos,false);
		} catch (Exception e) {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
			getBillUI().showErrorMessage(e.getMessage());
		}
	}
	
	@Override
	protected void onBoSave() throws Exception {
		onCheckBeforeSave();
		dataNotNullValidate();
		onSave();
		onBoRefresh();
	}
}
