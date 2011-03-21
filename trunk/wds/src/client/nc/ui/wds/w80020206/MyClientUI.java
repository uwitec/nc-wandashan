package nc.ui.wds.w80020206;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w8004040210.TbGeneralHVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8000.CommonUnit;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
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

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	public void setDefaultData() throws Exception {
	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		String isType = null;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != isType && isType.equals("1")
				&& getBufferData().getVOBufferSize() > 0) {
			int index = 0;
			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
				index = getBillListPanel().getHeadTable().getSelectedRow();
			}
			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
			SoSaleVO soSaleVO = (SoSaleVO) billvo.getParentVO(); //

			// 签字后
			if (null != soSaleVO.getVdef6() && "3".equals(soSaleVO.getVdef6())) {
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(false);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(true);
			} else { // 签字前
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
						.setEnabled(true);
				getButtonManager().getButton(
						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
						.setEnabled(false);
			}
		}
	}

}
