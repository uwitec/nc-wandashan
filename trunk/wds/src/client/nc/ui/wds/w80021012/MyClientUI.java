package nc.ui.wds.w80021012;

import java.util.ArrayList;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w80021012.TbHandlingcolVO;

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
public class MyClientUI extends AbstractMyClientUI {

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
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub

		if (e.getKey().equals("pk_invbasdoc")) {
			getBillCardPanel().execHeadEditFormulas();

			// ��ñ�ͷ����
			int rowNum = getBufferData().getVOBufferSize();
			String pk_invbasdoc2 = getBillCardPanel().getHeadItem(
					"pk_invbasdoc").getValue();
			for (int i = 0; i < rowNum; i++) {
				TbHandlingcolVO vo = (TbHandlingcolVO) (getBufferData()
						.getVOByRowNo(i)).getParentVO();

				String pk_invbasdoc = vo.getPk_invbasdoc();

				if (pk_invbasdoc.equals(pk_invbasdoc2)) {
					this.showErrorMessage("�˵�Ʒ�Ѵ��ڲɼ�����");
					getBillCardPanel().setHeadItem("pk_invbasdoc", "");
					getBillCardPanel().setHeadItem("bm", "");
					getBillCardPanel().setHeadItem("xm", "");
					getBillCardPanel().setHeadItem("gg", "");
					
				}
			}
		}
		if (e.getKey().equals("chargecol")) {
			Object s = getBillCardPanel().getHeadItem("chargecol")
					.getValueObject();
			if (s != null) {
				double d = Double.parseDouble(s.toString());
				if (d < 0) {
					this.showErrorMessage("�ɼ����۲���Ϊ����");
					getBillCardPanel().setHeadItem("chargecol", "");
				}
			}
		}
		super.afterEdit(e);
	}

}
