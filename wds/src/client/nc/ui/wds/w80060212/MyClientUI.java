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
			Date ddDate = new Date(); // 订单派车时间
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 获取物流公司来车日期
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
				this.showErrorMessage("没有物流公司来车日期无法计算“货物运行时间”");
				return;
			}
			// 获取到货时间
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
			// 获取订单派车时间
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
			// 计算 当前日期和数据库中批次日期的时间差
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
			// 获取正常运行时间
			Object o = getBillCardPanel().getHeadItem("cm_zcyxsj")
					.getValueObject();
			if (null != o && !"".equals(o)) {
				int beginnum = Integer.parseInt(o.toString());
				// 获取货物运行时间
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
