package nc.ui.wds.ic.other.out;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 其他出库
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
		//当前公司 当前库存组织  当前仓库  当前货位
		getBillCardPanel().setHeadItem("comp", _getCorp());
		getBillCardPanel().setHeadItem("pk_calbody", WdsWlPubConst.DEFAULT_CALBODY);
		try{
			getBillCardPanel().setHeadItem("srl_pk", LoginInforHelper.getCwhid(_getOperator()));
			getBillCardPanel().setHeadItem("pk_cargdoc", LoginInforHelper.getSpaceByLogUserForStore(_getOperator()));
		}catch(Exception e){
			e.printStackTrace();//zhf  异常不处理
		}
		//制单人  制单日期   
		getBillCardPanel().setHeadItem("tmaketime",_getServerTime());
		getBillCardPanel().setHeadItem("dbilldate",_getDate());
		getBillCardPanel().setHeadItem("coperatorid",_getOperator());
		getBillCardPanel().setHeadItem("vbilltype",WdsWlPubConst.BILLTYPE_OTHER_OUT);
//		getBillCardPanel().setHeadItem("pwb_fbillflag",2);
		getBillCardPanel().setHeadItem("vbillcode", 
				HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_OTHER_OUT, _getOperator(), null, null));		
	
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		int row = e.getRow();
		if (e.getKey().equals("ccunhuobianma")) {
			String code = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(
					row,
					"ccunhuobianma"));
			if (null == code) {
				getBillCardPanel().setBodyValueAt(null,
						row,
						"vbatchcode");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"nshouldoutassistnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"nshouldoutnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"noutnum");
				getBillCardPanel().setBodyValueAt(null,
						row,
						"noutassistnum");
			}

		}

		super.afterEdit(e);

	}
//zhf  注释 按钮状态和可用性的控制通过权限分配来实现
	public void valueChanged(ListSelectionEvent arg0) {	
		int index = 0;
		TbOutgeneralHVO generalhvo = null;
		if (getBufferData().getVOBufferSize() <= 0) 
		    return;
		index = getBillListPanel().getHeadTable().getSelectedRow();
			if (index == -1) {
				index = 0;
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			generalhvo = (TbOutgeneralHVO) billvo.getParentVO(); //
		
			int vbillstatus = PuPubVO.getInteger_NullAs(generalhvo.getVbillstatus(), -1);
//			// 签字后
			if (vbillstatus == 0) {
				this.setButtonEnabled(false);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(false);
			} else{ // 签字前
				this.setButtonEnabled(true);
				getButtonManager().getButton(IBillButton.Edit)
						.setEnabled(true);
			} 
	}

	/**
	 * 控制签字，取消签字和修改按钮状态
	 */
	private void setButtonEnabled(boolean value) {
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qzqr)
				.setEnabled(value);
		getButtonManager().getButton(
				nc.ui.wds.w80060206.buttun0206.ISsButtun.Qxqz).setEnabled(
				!value);
//		getButtonManager().getButton(IBillButton.Edit).setEnabled(value);
	}

}
