package nc.ui.wds.w80021040;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.wl.pub.CommonUnit;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

    private MyClientUI myUI = null;

    private int lineNo = -1;

    public MyEventHandler(BillManageUI billUI, IControllerBase control) {
	super(billUI, control);
    }

    @Override
    protected void onBoEdit() throws Exception {
	// TODO Auto-generated method stub
	super.onBoEdit();

    }

    @Override
    public void onBoAdd(ButtonObject bo) throws Exception {
	// TODO Auto-generated method stub
	super.onBoAdd(bo);
	lineNo = -1;
	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qycgjh_zdrq",
		_getDate());
	// ���õ��ݺ�
	getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
		"qycgjh_ddh").setValue(
		CommonUnit.getBillCode("3040", ClientEnvironment.getInstance()
			.getCorporation().getPk_corp(), "", ""));// ���õ��ݺ�
	getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
		"qycgjh_zdr").setValue(
		ClientEnvironment.getInstance().getUser().getPrimaryKey());

    }

    @Override
    protected void onBoLineAdd() throws Exception {
	// TODO Auto-generated method stub
	super.onBoLineAdd();
	if (lineNo == -1)
	    lineNo = 10;
	else
	    lineNo = lineNo + 10;
	// �к�
	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
		lineNo + "",
		getBillCardPanelWrapper().getBillCardPanel().getBillTable()
			.getSelectedRow(), "qycgjh2_hh");

    }

    @Override
    protected void onBoLineIns() throws Exception {
	super.onBoLineIns();
	if (lineNo != -1) {
	    lineNo = lineNo + 10;
	    getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
		    lineNo + "",
		    getBillCardPanelWrapper().getBillCardPanel().getBillTable()
			    .getSelectedRow(), "qycgjh2_hh");
	}
    }

    @Override
    protected void onBoLinePaste() throws Exception {
	super.onBoLinePaste();
	if (lineNo != -1) {
	    lineNo = lineNo + 10;
	    getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(
		    lineNo + "",
		    getBillCardPanelWrapper().getBillCardPanel().getBillTable()
			    .getSelectedRow(), "qycgjh2_hh");
	}

    }

    private int getRowNo() {
	if (lineNo == -1)
	    lineNo = 10;
	else
	    lineNo = lineNo + 10;
	return lineNo;
    }

}