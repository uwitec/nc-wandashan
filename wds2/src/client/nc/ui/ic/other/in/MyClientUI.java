package nc.ui.ic.other.in;

import java.util.Date;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

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
		//��ǰ��˾ ��ǰ�����֯  ��ǰ�ֿ�  ��ǰ��λ
		getBillCardPanel().setHeadItem("geh_corp", _getCorp());
		getBillCardPanel().setHeadItem("geh_calbody", WdsWlPubConst.DEFAULT_CALBODY);
		try{
			getBillCardPanel().setHeadItem("geh_cwarehouseid", LoginInforHelper.getCwhid(_getOperator()));
			getBillCardPanel().setHeadItem("pk_cargdoc", LoginInforHelper.getSpaceByLogUserForStore(_getOperator()));
		}catch(Exception e){
			e.printStackTrace();//zhf  �쳣������
		}
		//�Ƶ���  �Ƶ�����   
		getBillCardPanel().setHeadItem("tmaketime",_getServerTime());
		getBillCardPanel().setHeadItem("geh_dbilldate",_getDate());
		getBillCardPanel().setHeadItem("coperatorid",_getOperator());
		getBillCardPanel().setHeadItem("geh_billtype",WdsWlPubConst.BILLTYPE_OTHER_IN);
		getBillCardPanel().setHeadItem("pwb_fbillflag",2);
		getBillCardPanel().setHeadItem("geh_bbillcode", 
				HYPubBO_Client.getBillNo(WdsWlPubConst.BILLTYPE_OTHER_IN, _getOperator(), null, null));		
	}


	public boolean beforeEdit(BillEditEvent e) {
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// �������������ѡ���˿ͻ�����ѡ��ͬʱ����Ҫ���ͻ����˾�����ͬ
		if ("invcode".equals(e.getKey())) {
			//          ���˴������ֻ�ܲ��յ�����Ա��Ӧ��λ�Ļ�Ʒ
			//			��ȡ��ǰ��¼�����ڻ�λ      �ǲִ�����Ա�޻�λ
			try {
				if (!WdsWlPubTool.isZc(LoginInforHelper.getCwhid(_getOperator()))) {
					return true;
				}
			
			String[] pk_cargdoc = LoginInforHelper.getSpaceByLogUser(_getOperator());
			if(pk_cargdoc==null||pk_cargdoc.length ==0)
				return true;

			// �õ���ͬ����
			UIRefPane panel = (UIRefPane) this.getBillCardPanel()
			.getBodyItem("invcode").getComponent();


			panel
			.getRefModel()
			.setWherePart(
					" pk_invbasdoc in (select pk_invbasdoc from tb_spacegoods where dr = 0 and pk_cargdoc = '"
					+ pk_cargdoc.toString()
					+ "') and dr=0 ");


			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return true;
			}
		}
		if ("geb_customize2".equals(e.getKey())) {
			String stordocid="";
			try {
				stordocid = LoginInforHelper.getCwhid(_getOperator());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return true;
			}
			// �õ���ͬ����
			UIRefPane panel = (UIRefPane) this.getBillCardPanel()
			.getBodyItem("geb_customize2").getComponent();

			panel.getRefModel().setWherePart(
					" pk_stordoc = '" + stordocid
					+ "' and dr=0 ");
		}

		return true;
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();
		getBillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e) {
//		// TODO Auto-generated method stub
//		String isType = null;
//		try {
//			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
//					.getUser().getPrimaryKey());
//		} catch (BusinessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if (null != isType && isType.equals("1")
//				&& getBufferData().getVOBufferSize() > 0) {
//			int index = 0;
//			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
//				index = getBillListPanel().getHeadTable().getSelectedRow();
//			}
//			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
//			TbGeneralHVO generalhvo = (TbGeneralHVO) billvo.getParentVO(); //
//
//			// ǩ�ֺ�
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getButtonManager().getButton(IBillButton.Edit)
//						.setEnabled(false);
//			} else { // ǩ��ǰ
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(true);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(false);
//				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//			}
//		} else if (null != isType && isType.equals("3")
//				&& getBufferData().getVOBufferSize() > 0) {
//			int index = 0;
//			if (getBillListPanel().getHeadTable().getSelectedRow() != -1) {
//				index = getBillListPanel().getHeadTable().getSelectedRow();
//			}
//			AggregatedValueObject billvo = getBufferData().getVOByRowNo(index);
//			TbGeneralHVO generalhvo = (TbGeneralHVO) billvo.getParentVO(); //
//
//			// ǩ�ֺ�
//			if (null != generalhvo.getPwb_fbillflag()
//					&& generalhvo.getPwb_fbillflag() == 3) {
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(false);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(true);
//				getButtonManager().getButton(IBillButton.Edit)
//						.setEnabled(false);
//			} else { // ǩ��ǰ
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qzqr)
//						.setEnabled(true);
//				getButtonManager().getButton(
//						nc.ui.wds.w80020206.buttun0206.ISsButtun.Qxqz)
//						.setEnabled(false);
//				getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//			}
//		}
//		try {
//			this.updateButtonUI();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
}
