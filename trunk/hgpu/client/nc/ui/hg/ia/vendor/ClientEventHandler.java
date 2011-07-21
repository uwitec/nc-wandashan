package nc.ui.hg.ia.vendor;

import java.util.HashMap;
import java.util.Map;

import nc.ui.hg.pu.pub.DBTManageEventHandler;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

public class ClientEventHandler extends DBTManageEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);

	}

	public ClientQueryDLG queryDialog = null;

	@Override
	protected UIDialog createQueryUI() {
		return new ClientQueryDLG(getBillUI(), null,
				_getCorp().getPrimaryKey(), getBillUI().getModuleCode(),
				_getOperator(), null, null);
	}

	protected UIDialog getQry() {
		if (queryDialog == null) {
			queryDialog = new ClientQueryDLG(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI().getModuleCode(),
					_getOperator(), null,"zhw");
			queryDialog.setTempletID("zhw1AA10000000010S2U");
		}
		return queryDialog;
	}

	// @Override
	// public void onBoAdd(ButtonObject bo) throws Exception {
	// getBillUI().setBillOperate(IBillOperate.OP_EDIT);
	// int selectRow = getBillCardPanel().getBillTable().getSelectedRow();
	// onAdd();
	// getBillCardPanel().getBillModel().setValueAt(UFBoolean.TRUE,
	// selectRow,"fisrozen");
	// }

	private void onAdd(UFDouble nre,String custbasid,String sx) throws Exception {
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
		onBoLineAdd();
		int selectRow = getBillCardPanel().getBillTable().getSelectedRow();
		BillModel model = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getTableModel();
		model.setRowEditState(true);
		model.setEditRow(selectRow);
		setOper("centryid", "dentrydate",selectRow);
		getBillCardPanel().getBillModel().setValueAt(_getCorp().getPrimaryKey(), selectRow, "pk_corp");
		getBillCardPanel().getBillModel().setValueAt(nre, selectRow, "ncurbalance");
		getBillCardPanel().getBillModel().setValueAt(custbasid, selectRow, "ccustbasid");
		Object o=HgPubTool.execFomularClient("ccustmanid->getcolvalue2(bd_cumandoc,pk_cumandoc,pk_cubasdoc,pk_cubasdoc,pk_corp,pk_corp)",new String[]{"pk_cubasdoc","pk_corp"},new String[]{custbasid,_getCorp().getPrimaryKey()});
		getBillCardPanel().getBillModel().setValueAt(o, selectRow, "ccustmanid");
		getBillCardPanel().getBillModel().setValueAt(sx, selectRow, "szxmid");
		getBillCardPanel().getBillModel().execLoadFormulaByRow(selectRow);
	}

	public void setOper(String oper, String date,int selectRow) {
		getBillCardPanel().getBillModel().setValueAt(_getOperator(), selectRow,
				oper);
		getBillCardPanel().getBillModel().setValueAt(_getDate(), selectRow,
				date);
		getBillCardPanel().getBillModel().execLoadFormulaByRow(selectRow);
	}

	@Override
	public void onBoEdit() throws Exception {
		super.onBoEdit();
		int selectRow = getBillCardPanel().getBillTable().getSelectedRow();
		setOper("cmodifyid", "cmodifydate",selectRow);
	}

	@Override
	protected void onBoSave() throws Exception {
		int row = getBillCardPanel().getBillTable().getSelectedRow();
		UFDouble cur = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row,"ncurbalance"));
		UFDouble freeze = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row,"nfreamount"));
		if(freeze.compareTo(UFDouble.ZERO_DBL)<0)
			throw new BusinessException("冻结金额不能小于零");
		if(cur.compareTo(freeze)<0 )
			throw new BusinessException("冻结金额不能大于当前剩余金额");
		super.onBoSave();
	}

	public BillCardPanel getBillCardPanel() {
		return getBillCardPanelWrapper().getBillCardPanel();
	}

	private void onStopVen(boolean flag) throws Exception {

		int[] rows = getBillCardPanel().getBillTable().getSelectedRows();
		if (rows ==null || rows.length==0) {
			getBillUI().showErrorMessage("请选择一行供应商");
			return;
		}
		for(int row:rows){
			HYBillVO bill = (HYBillVO) getBufferData().getCurrentVO();
			BalFreezeVO free = (BalFreezeVO) bill.getChildrenVO()[row];
			
			if(flag){
				if(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row,"nfreamount")).compareTo(UFDouble.ZERO_DBL)<0)
					throw new BusinessException("冻结金额不能小于零");
			}

			if (flag&& PuPubVO.getUFBoolean_NullAs(free.getFisrozen(),
							UFBoolean.FALSE).booleanValue())
				continue;
//				throw new BusinessException("供应商"
//						+ getBillCardPanel().getBillModel().getValueAt(row, "name")
//						+ "已经冻结");
			    
			if (!flag&& !PuPubVO.getUFBoolean_NullAs(free.getFisrozen(),
							UFBoolean.FALSE).booleanValue())
				continue;
//				throw new BusinessException("供应商"
//						+ getBillCardPanel().getBillModel().getValueAt(row, "name")
//						+ "没有冻结");

			free.setFisrozen(new UFBoolean(flag));

			free.setCoperatorid(_getOperator());
			free.setDmakedate(_getDate());

			HYPubBO_Client.update(free);
			getBillCardPanel().getBillModel().setValueAt(new UFBoolean(flag), row,
					"fisrozen");
			setOper("coperatorid", "dmakedate",row);
			getBillCardPanel().getBillModel().execLoadFormulaByRow(row);
			if(flag)
                getBillUI().showHintMessage("冻结完成");
            else
            	getBillUI().showHintMessage("解冻完成");
		}
	}

	protected void onBoElse(int intBtn) throws Exception {
		if (intBtn == HgPuBtnConst.FREEZE) {
			onStopVen(true);// 冻结
		} else if (intBtn == HgPuBtnConst.UNFREEZE) {
			onStopVen(false);
		} else if (intBtn == HgPuBtnConst.VIEW) {//查看
			 onBoBodyQuery1();
		} else {
			super.onBoElse(intBtn);
		}
	}
	//查看按钮使用
	protected void doBodyQuery1(String strWhere) throws Exception,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String[] strs1 =strWhere.split("'", 20);
		if(strs1 !=null && strs1.length==5)
			strWhere = strWhere +" and szxmid is null ";
		
		SuperVO[] queryVos = getBusiDelegator().queryByCondition(
				Class.forName(getUIController().getBillVoName()[2]),
				strWhere == null ? "" : strWhere);

		getBufferData().clear();
		
		BalFreezeVO[] vos = getBySueprVO(queryVos);
		if (queryVos == null || queryVos.length == 0) {
			String[] strs =strWhere.split("'", 20);
			UFDouble nre =  UFDouble.ZERO_DBL;
			if(strs !=null && strs.length==5){
			   nre =VendorHelper.getByString(strs[1],null,strs[3]);
			   onAdd(nre,strs[1],null);
			}
			else if(strs !=null && strs.length==7){
				nre =VendorHelper.getByString(strs[1],strs[3],strs[5]);
				onAdd(nre,strs[1],strs[3]);
			}
				
		} else {
			AggregatedValueObject vo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			vo.setChildrenVO(vos);
			getBufferData().addVOToBuffer(vo);
			updateBuffer();
		}
	}
	//查寻按钮使用
	protected void doBodyQuery(String strWhere) throws Exception,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		SuperVO[] queryVos = getBusiDelegator().queryByCondition(
				Class.forName(getUIController().getBillVoName()[2]),
				strWhere == null ? "" : strWhere);

		getBufferData().clear();

		
		BalFreezeVO[] vos = getBySueprVO(queryVos);
			AggregatedValueObject vo = (AggregatedValueObject) Class.forName(
					getUIController().getBillVoName()[0]).newInstance();
			if(vos ==null || vos.length==0)
				vo.setChildrenVO(queryVos);
			else
			vo.setChildrenVO(vos);
			getBufferData().addVOToBuffer(vo);
			updateBuffer();
	}
//获取当前剩余金额
	private BalFreezeVO[] getBySueprVO(SuperVO[] queryVos) throws Exception {
		BalFreezeVO[] vos = null;
		if (queryVos != null && queryVos.length > 0) {
			vos = new BalFreezeVO[queryVos.length];
			Map<String, SuperVO> map = new HashMap<String, SuperVO>();
			for (SuperVO vo : queryVos) {
				String str = PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("ccustbasid"))
						+ "&"+ PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("szxmid"))
						+ "&"+ PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_corp"));
				if (!map.containsKey(str))
					map.put(str, vo);
			}
			vos = VendorHelper.getBySueprVO(map);
		}
		return vos;
	}
   //查看按钮使用
	protected void onBoBodyQuery1() throws Exception {
		StringBuffer strWhere = new StringBuffer();
		if (askForBodyQueryCondition1(strWhere) == false)
			return;// 用户放弃了查询
		doBodyQuery1(strWhere.toString());
	}
	//查看按钮使用
	protected boolean askForBodyQueryCondition1(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQry();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		if (getUIController().getBodyCondition() != null)
			strWhere = strWhere + " and "
					+ getUIController().getBodyCondition();
		// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
		sqlWhereBuf.append(strWhere);
		return true;
	}
}
