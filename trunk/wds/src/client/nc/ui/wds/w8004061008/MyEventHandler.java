package nc.ui.wds.w8004061008;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.query.INormalQuery;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.wds.w8004061008.MyBillVO;
import nc.vo.wds.w9000.BdInvbasdocVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

    private MyClientUI myUI = null;

    public MyEventHandler(BillListUI billUI, IListController control) {
	super(billUI, control);
	myUI = (MyClientUI) billUI;
    }

    @Override
    protected void onBoEdit() throws Exception {
	// TODO Auto-generated method stub
	super.onBoEdit();
	BillItem billItems[] = myUI.getBillListPanel().getHeadBillModel()
		.getBodyItems();
	billItems[58].setEnabled(true);
	billItems[58].setEdit(true);
	billItems[59].setEnabled(true);
	billItems[59].setEdit(true);
	billItems[60].setEnabled(true);
	billItems[60].setEdit(true);
	billItems[61].setEnabled(true);
	billItems[61].setEdit(true);
	billItems[62].setEnabled(true);
	billItems[62].setEdit(true);
	billItems[63].setEnabled(true);
	billItems[63].setEdit(true);
	billItems[64].setEnabled(true);
	billItems[64].setEdit(true);

    }

    @Override
    protected void onBoSave() throws Exception {
	// TODO Auto-generated method stub

	int rownum = getBillListPanelWrapper().getBillListPanel()
		.getHeadBillModel().getRowCount();
	for (int i = 0; i < rownum; i++) {
	    String def20 = (String) getBillListPanelWrapper()
		    .getBillListPanel().getHeadTable().getValueAt(i, 3);
	    if (null != def20) {
		if (!def20.matches("\\d+")) {
		    getBillUI().showErrorMessage("托盘容量必须是数字！");
		    return;
		}
	    }
	    String def19 = (String) getBillListPanelWrapper()
		    .getBillListPanel().getHeadTable().getValueAt(i, 4);
	    if (null != def19) {
		if (!def19.matches("\\d+")) {
		    getBillUI().showErrorMessage("托盘存货层数必须是数字！");
		    return;
		}
	    }
	    String def18 = (String) getBillListPanelWrapper()
		    .getBillListPanel().getHeadTable().getValueAt(i, 5);
	    if (null != def18) {
		if (!def18.matches("\\d+")) {
		    getBillUI().showErrorMessage("销售预警天数必须是数字！");
		    return;
		}
	    }
	    String def17 = (String) getBillListPanelWrapper()
		    .getBillListPanel().getHeadTable().getValueAt(i, 6);
	    if (null != def17) {
		if (!def17.matches("\\d+")) {
		    getBillUI().showErrorMessage("销售警戒天数必须是数字！");
		    return;
		}
	    }

	    String def16 = (String) getBillListPanelWrapper()
		    .getBillListPanel().getHeadTable().getValueAt(i, 7);
	    if (null != def16) {
		if (!def16.matches("\\d+")) {
		    getBillUI().showErrorMessage("调拨警戒天数1必须是数字！");
		    return;
		}
	    }
	    String def15 = (String) getBillListPanelWrapper()
	    .getBillListPanel().getHeadTable().getValueAt(i, 8);
	    if (null != def15) {
		if (!def15.matches("\\d+")) {
		    getBillUI().showErrorMessage("调拨警戒天数2必须是数字！");
		    return;
	}
    }
	}
	// 判断是否有存盘数据
	BdInvbasdocVO[] b = (BdInvbasdocVO[]) getBillListPanelWrapper()
		.getBillListPanel().getHeadBillModel().getBodyValueVOs(
			BdInvbasdocVO.class.getName());
	IVOPersistence iPersistence = (IVOPersistence) NCLocator.getInstance()
		.lookup(IVOPersistence.class.getName());
	iPersistence.updateVOArray(b);
	AggregatedValueObject billVO = new MyBillVO();
	billVO.setParentVO(b[0]);
	setTSFormBufferToVO(billVO);
	AggregatedValueObject checkVO = new MyBillVO();
	checkVO.setParentVO(b[0]);
	setTSFormBufferToVO(checkVO);
	// 进行数据晴空
	Object o = null;
	ISingleController sCtrl = null;
	if (getUIController() instanceof ISingleController) {
	    sCtrl = (ISingleController) getUIController();
	    if (sCtrl.isSingleDetail()) {
		o = billVO.getParentVO();
		billVO.setParentVO(null);
	    } else {
		o = billVO.getChildrenVO();
		billVO.setChildrenVO(null);
	    }
	}

	boolean isSave = true;

	// 进行数据恢复处理
	if (sCtrl != null) {
	    if (sCtrl.isSingleDetail())
		billVO.setParentVO((CircularlyAccessibleValueObject) o);
	}
	int nCurrentRow = -1;
	if (isSave) {
	    if (isEditing()) {
		if (getBufferData().isVOBufferEmpty()) {
		    getBufferData().addVOToBuffer(billVO);
		    nCurrentRow = 0;

		} else {
		    getBufferData().setCurrentVO(billVO);
		    nCurrentRow = getBufferData().getCurrentRow();
		}
	    }
	    // 新增后操作处理
	    setAddNewOperate(isAdding(), billVO);
	}
	// 设置保存后状态
	getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	if (nCurrentRow >= 0) {
	    getBufferData().setCurrentRow(nCurrentRow);
	}

    }

    @Override
    protected void onBoQuery() throws Exception {
	// TODO Auto-generated method stub
	StringBuffer strWhere = new StringBuffer();

	if (askForQueryCondition(strWhere) == false)
	    return;// 用户放弃了查询

	strWhere
		.append(" and pk_invcl in"
			+ " (select pk_invcl from bd_invcl where invclasscode like '30101%')"
			+ " and def14 = 0 or def14 = 1");

	SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

	getBufferData().clear();
	// 增加数据到Buffer
	addDataToBuffer(queryVos);

	updateBuffer();
    }

    protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
	    throws Exception {
	if (sqlWhereBuf == null)
	    throw new IllegalArgumentException(
		    "askForQueryCondition().sqlWhereBuf cann't be null");
	UIDialog querydialog = getQueryUI();

	if (querydialog.showModal() != UIDialog.ID_OK)
	    return false;
	INormalQuery query = (INormalQuery) querydialog;

	String strWhere = query.getWhereSql();
	if (strWhere == null)
	    strWhere = "1=1";

	if (getButtonManager().getButton(IBillButton.Busitype) != null) {
	    if (getBillIsUseBusiCode().booleanValue())
		// 业务类型编码
		strWhere = "(" + strWhere + ") and "
			+ getBillField().getField_BusiCode() + "='"
			+ getBillUI().getBusicode() + "'";

	    else
		// 业务类型
		strWhere = "(" + strWhere + ") and "
			+ getBillField().getField_Busitype() + "='"
			+ getBillUI().getBusinessType() + "'";

	}

	strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

	// if (getHeadCondition() != null)
	// strWhere = strWhere + " and " + getHeadCondition();
	// 现在我先直接把这个拼好的串放到StringBuffer中而不去优化拼串的过程
	sqlWhereBuf.append(strWhere);
	return true;
    }

}