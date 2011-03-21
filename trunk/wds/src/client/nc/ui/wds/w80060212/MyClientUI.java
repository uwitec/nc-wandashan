package nc.ui.wds.w80060212;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.ui.pub.bill.BillEditEvent;
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
		if (e.getKey().equals("cm_dhrq")) {
			Date begindate = new Date();
			Date enddate = new Date();
			Date ddDate = new Date(); // �����ɳ�ʱ��
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// ��ȡ������˾��������
			Object o = getBillCardPanel().getHeadItem("cm_wlgslc")
					.getValueObject();
			if (null != o && !"".equals(o))
				try {
					begindate = format.parse(o.toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else {
				this.showErrorMessage("û��������˾���������޷����㡰��������ʱ�䡱");
				return;
			}
			// ��ȡ����ʱ��
			Object b = getBillCardPanel().getHeadItem("cm_dhrq")
					.getValueObject();
			if (null != b && !"".equals(b))
				try {
					enddate = format.parse(b.toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else {
				getBillCardPanel().setHeadItem("cm_hwyxsj", null);
				getBillCardPanel().setHeadItem("cm_yxxd", null);
				return;
			}
			// ��ȡ�����ɳ�ʱ��
			Object c = getBillCardPanel().getHeadItem("cm_ddpc")
					.getValueObject();
			if (null != c && !"".equals(c))
				try {
					ddDate = format.parse(c.toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else {
				getBillCardPanel().setHeadItem("cm_bhdshsj", null);
				return;
			}
			long DAY = 24L * 60L * 60L * 1000L;
			// ���� ��ǰ���ں����ݿ����������ڵ�ʱ���
			int com = Integer.parseInt(((enddate.getTime() - begindate
					.getTime()) / DAY)
					+ "");
			int ddcom = Integer
					.parseInt(((enddate.getTime() - ddDate.getTime()) / DAY)
							+ "");
			getBillCardPanel().setHeadItem("cm_hwyxsj", com);
			getBillCardPanel().setHeadItem("cm_bhdshsj", ddcom);
			getBillCardPanel().execHeadEditFormulas();
		}

		if (e.getKey().equals("cm_zcyxsj")) {
			// ��ȡ��������ʱ��
			Object o = getBillCardPanel().getHeadItem("cm_zcyxsj")
					.getValueObject();
			if (null != o && !"".equals(o)) {
				int beginnum = Integer.parseInt(o.toString());
				// ��ȡ��������ʱ��
				Object b = getBillCardPanel().getHeadItem("cm_hwyxsj")
						.getValueObject();
				if (null != b && !"".equals(b)) {
					int endnum = Integer.parseInt(b.toString());
					getBillCardPanel()
							.setHeadItem("cm_yxxd", beginnum - endnum);
				} else {
					getBillCardPanel().setHeadItem("cm_yxxd", null);
				}
			} else {
				getBillCardPanel().setHeadItem("cm_yxxd", null);
			}

		}

	}

}
