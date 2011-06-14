package nc.ui.wds.ie.storedetail;

import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.pub.linkoperate.*;
import nc.vo.trade.button.ButtonVO;
import nc.ui.trade.base.IBillOperate;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴���Ӵ����������Ϣ
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */

public abstract class AbstractMyClientUI extends
	nc.ui.trade.list.BillListUI implements ILinkQuery {

    /**
     * �˷�������������
     */
    protected IListController createController() {
	return new MyClientUICtrl();
    }

    /**
     * ������ݲ���ƽ̨ʱ��UI����Ҫ���ش˷��������ز���ƽ̨��ҵ�������
     * 
     * @return BusinessDelegator ����ƽ̨��ҵ�������
     */
    protected BusinessDelegator createBusinessDelegator() {
	return new nc.ui.wds.ie.storedetail.MyDelegator();
    }

    /**
     * ע���Զ��尴ť
     */
    protected void initPrivateButton() {
	int[] listButns = getUIControl().getListButtonAry();
	boolean hasCommit = false;
	boolean hasAudit = false;
	boolean hasCancelAudit = false;
	for (int i = 0; i < listButns.length; i++) {
	    if (listButns[i] == nc.ui.trade.button.IBillButton.Commit)
		hasCommit = true;
	    if (listButns[i] == nc.ui.trade.button.IBillButton.Audit)
		hasAudit = true;
	    if (listButns[i] == nc.ui.trade.button.IBillButton.CancelAudit)
		hasCancelAudit = true;
	}
	if (hasCommit) {
	    ButtonVO btnVo = nc.ui.trade.button.ButtonVOFactory.getInstance()
		    .build(nc.ui.trade.button.IBillButton.Commit);
	    btnVo.setBtnCode(null);
	    addPrivateButton(btnVo);
	}

	if (hasAudit) {
	    ButtonVO btnVo2 = nc.ui.trade.button.ButtonVOFactory.getInstance()
		    .build(nc.ui.trade.button.IBillButton.Audit);
	    btnVo2.setBtnCode(null);
	    addPrivateButton(btnVo2);
	}

	if (hasCancelAudit) {
	    ButtonVO btnVo3 = nc.ui.trade.button.ButtonVOFactory.getInstance()
		    .build(nc.ui.trade.button.IBillButton.CancelAudit);
	    btnVo3.setBtnCode(null);
	    addPrivateButton(btnVo3);
	}
    }

    /**
     * ע��ǰ̨У����
     */
    public Object getUserObject() {
	return null;//new MyClientUICheckRuleGetter();
    }

    public void doQueryAction(ILinkQueryData querydata) {
	String billId = querydata.getBillID();
	if (billId != null) {
	    try {
		AggregatedValueObject vo = loadHeadData(billId);
		getBufferData().addVOToBuffer(vo);
		setListHeadData(new CircularlyAccessibleValueObject[] { vo
			.getParentVO() });
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		setBillOperate(IBillOperate.OP_NO_ADDANDEDIT);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }
}
