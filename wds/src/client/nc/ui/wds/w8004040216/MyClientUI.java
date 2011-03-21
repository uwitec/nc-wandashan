package nc.ui.wds.w8004040216;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.bd.ref.RefUtil;
import nc.ui.bd.ref.UFRefManage;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

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
		BillCardBeforeEditListener,ValueChangedListener {

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
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
//		getBillCardPanel().
//		getBillCardPanel().

		// getBillCardPanel().addBillEditListenerHeadTail(billEditListener)

	}

	public void setDefaultData() throws Exception {
	}

	public boolean beforeEdit(BillItemEvent e) {
		if (getBillCardPanel().isEnabled() == false) {
			return false;
		}
		// �������������ѡ���˷ֳ���˾����ѡ��ͬʱ����Ҫ���ֳ���˾���˾�����ͬ
		if ("pk_fcalbody".equals(e.getItem().getKey())) {
			String pk_fcorp = (String) this.getBillCardPanel().getHeadItem(
					"pk_fcorp").getValueObject();
			if (pk_fcorp != null && pk_fcorp.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_fcalbody").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(
						" pk_corp = '" + pk_fcorp + "' and dr=0 ");

			} else {

				this.showErrorMessage("��û��ѡ��ֳ���˾��������ʾ�κηֳ������֯��Ϣ");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_fcalbody").getComponent();
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}

		}
		if ("pk_fstordoc".equals(e.getItem().getKey())) {
			String pk_fcalbody = (String) this.getBillCardPanel().getHeadItem(
					"pk_fcalbody").getValueObject();
			String pk_fcorp = (String) this.getBillCardPanel().getHeadItem(
					"pk_fcorp").getValueObject();
			if (pk_fcalbody != null && pk_fcalbody.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_fstordoc").getComponent();
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(
						" pk_calbody = '" + pk_fcalbody + "' and pk_corp = '"
								+ pk_fcorp + "' and dr=0 ");

			} else {

				this.showErrorMessage("��û��ѡ��ֳ������֯��������ʾ�κηֳ��ֿ���Ϣ");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_fstordoc").getComponent();
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}
		}
		if ("pk_cargdoc".equals(e.getItem().getKey())) {
			String srl_pkr = (String) this.getBillCardPanel().getHeadItem(
					"srl_pkr").getValueObject();
			if (srl_pkr != null && srl_pkr.length() > 0) {
				// �õ���ͬ����
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_cargdoc").getComponent();
//				
//				UIRefPane panel1 = (UIRefPane) this.getBillCardPanel()
//				.getHeadItem("srl_pkr").getComponent();
//				panel.setRefNodeName(panel1.getRefNodeName());
//				panel.setRefModel(panel1.getRefModel());
//				String a=panel.getRefModel().getRefSql();
				
				// this.getBillCardPanel()
				// .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits);
				// ���Ͽͻ���Ϊ����ȥ����
//				String s = panel.getRefModel().getRefNodeName();
//				String b=panel.getRefModel().getRefQueryDlgClaseName();
//				panel.setRefNodeName(
//						"nc.ui.wds.w80020202.refer.Hfpfer");
//				panel.setRefModel(RefUtil.getRefModel("nc.ui.wds.w80020202.refer.Hfpfer"));
				
//				panel.getRefModel().setRefQueryDlgClaseName("nc.ui.wds.w80060804.ppRefer.PpRefer");
//				panel.getRefModel().setDefaultFieldCount(2);
//				panel.getRefModel().setFieldCode(
//						new String[] { "storcode", "storname" });
//				panel.getRefModel().setFieldName(
//						new String[] { "�ֱֲ���", "�ֲ�����" });
//				panel.getRefModel().setPkFieldCode("pk_stordoc");
//				panel.getRefModel().setRefTitle("�����Ϣ");
//				String a = panel.getRefModel().getRefSql();
				
//				panel
//						.getRefModel()
//						.setTableName(
//								"(select pk_stordoc, storcode ,storname " +
//								"from bd_stordoc " +
//								"where pk_stordoc in ('1021A91000000004YZ0P','1021A91000000004UXCH'," +
//								"'1021A91000000004UWVU','1021A91000000004UXCM','1021A91000000004UXCN'," +
//								"'1021A91000000004UWVO','1021A91000000004UWVT') and dr=0)tmp");
//				
//				panel=null;
//				this.getBillCardPanel().getHeadItem("pk_cargdoc").setComponent(
//						panel);
//				
//				this.getBillCardPanel().execHeadLoadFormulas();
//				this.getBillCardPanel().execHeadEditFormulas();

				 panel.getRefModel().setWherePart(
				 " pk_stordoc = '" + srl_pkr + "' and dr=0 ");

			} else {

				this.showErrorMessage("��û��ѡ��ֿ⣬������ʾ�κλ�λ");
				UIRefPane panel = (UIRefPane) this.getBillCardPanel()
						.getHeadItem("pk_cargdoc").getComponent();
				// ���Ͽͻ���Ϊ����ȥ����
				panel.getRefModel().setWherePart(" dr=0 and 1=2  ");
			}
		}
		return true;
	}

	public void valueChanged(ValueChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
