package nc.ui.wds.report.report1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableColumnModel;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.wl.pub.report.ZmReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * ���ֲ�Ʒ��� ��Ʒ��ϸ��
 * @author mlr
 */
public class ReportUI extends ZmReportBaseUI {
	private static final long serialVersionUID = -416464210087347398L;
	// ����״̬����
	private static String pk_daijian = "1021S31000000009FS99";
	// ������ʾΪ �޵�����
	public static String pk_ruout = "0001S3100000000MM1PW";
	// ���ݺϲ��ֶ�
	private static String[] combinFields = { "num", "bnum" };
	// ���β�չ���ϲ�ά��
	// �ִ����ϲ�ά��
	private static String[] combinConds = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_storestate", "pk_invbasdoc", "days", "creadate" };
	// �ִ�������׷��ά��
	private static String[] combinConds1 = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_invbasdoc" };
	// ��;�ϲ�ά��
	private static String[] zaiTuCombinFields = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_invbasdoc" };
	// ���۴��� �ϲ�ά��
	private static String[] soDaiFa = { "billcode", "invtype", "pk_stordoc",
			"pk_invcl", "b_pk", "pk_invbasdoc" };

	// ����չ���ϲ�ά��
	// �ִ����ϲ�ά��
	private static String[] combinCondsf = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_storestate", "vbatchcode", "pk_invbasdoc", "days",
			"creadate" };
	// �ִ�������׷��ά��
	private static String[] combinConds1f = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_invbasdoc", "vbatchcode", };
	// ��;�ϲ�ά��
	private static String[] zaiTuCombinFieldsf = { "invtype", "pk_stordoc",
			"pk_invcl", "pk_invbasdoc", "vbatchcode", };
	// ���۴��� �ϲ�ά��
	private static String[] soDaiFaf = { "billcode", "invtype", "pk_stordoc",
			"pk_invcl", "b_pk", "pk_invbasdoc", "vbatchcode", };

	private static int[][] hdays = { { 0, 30 }, { 31, 60 }, { 61, 90 },
			{ 91, 120 }, { 121, 150 }, { 151, 180 }, { 181, 210 },
			{ 211, 240 }, { 241, 270 }, { 271, 300 }, { 301, 330 },
			{ 331, 365 } };
	private boolean isVbanthcode = false;

	private AccountCalendar ac = AccountCalendar.getInstance();
	private ClientEnvironment ce = ClientEnvironment.getInstance();

	// �ϲ�׷�ӵ�ģʽ���㱨������

	// ����������Ʒ���͵��㷨
	// ���ӱ����ȥ�����Ч����㷨
	// ���ô�����㷨
	// ����һ����ѯ

	// ת�ֲ���;�������㷨
	// ����һ����ѯ

	// �������������㷨 ���㱾��ǰ�������� ������������ ����ǰ�������� ����ǰ��������
	// ���һ����ѯ

	// ���������ܲ�ת�ֲֲֿ����
	// ���һ����ѯ

	// ���Ӹ����ϼƵļ����㷨

	@Override
	public Map getNewItems() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// ���ö�̬�в���λ��
		map.put("location", new Integer(7));
		ReportItem[] its = new ReportItem[58];
		String startName = "num";
		String startName1 = "bnum";
		int size = its.length / 2;
		List<ReportItem> list = new ArrayList<ReportItem>();
		for (int i = 0; i < size - 1; i++) {
			ReportItem it = ReportPubTool.getItem(startName + (i + 1), "������",
					IBillItem.DECIMAL, i, 80);
			ReportItem it1 = ReportPubTool.getItem(startName1 + (i + 1), "������",
					IBillItem.DECIMAL, i, 80);
			list.add(it);
			list.add(it1);
		}
		ReportItem[] rets = new ReportItem[list.size()];
		for (int i = 0; i < list.size(); i++) {
			rets[i] = list.get(i);
		}

		map.put("items", rets);
		return map;
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {

				// ÿ�β�ѯ�����Ƿ�����չ���ı�־
				setIsVbantchcode();
				// ��ձ�������
				clearBody();
				// ���ò�ѯ�Ķ�̬��
				setDynamicColumn1();
				// ���û����кϲ�
				setColumn();
				// ����vo
				List<ReportBaseVO[]> list = getReportVO(new String[] {
						getQuerySQL(getQueryConditon()),
						getQuerySQL1(getQueryConditon()),
						getQuerySQL2(getQueryConditon()),
						getQuerySQL3(getQueryConditon()),
						getQuerySQL4(getQueryConditon()), });
				ReportBaseVO[] rvos = null;
				rvos = zcal(list);
				setWait("���ڴ���....");
				if (rvos != null) {
					super.updateBodyDigits();
					setBodyVO(rvos);
					setTolal();
				}

			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void setWait(String msg) {
		nc.ui.pub.tools.BannerDialog m_dlgBanner = new nc.ui.pub.tools.BannerDialog(
				this);
		if (msg == null || msg.trim().length() <= 0)
			msg = "���ڲ�������ȴ�...";
		m_dlgBanner.setStartText(msg);
		MyTh a = new MyTh(m_dlgBanner);
		a.start();
		m_dlgBanner.showModal();
	}

	public class MyTh extends Thread {
		nc.ui.pub.tools.BannerDialog m = null;

		public MyTh(nc.ui.pub.tools.BannerDialog m) {
			super();
			this.m = m;
		}

		public void run() {
			try {
				boolean isStart = false;
				while (!isStart) {
					if (m != null && m.isVisible()) {
						isStart = true;
					}
					this.sleep(200);
				}
				this.sleep(1000 * 60 * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (m != null && m.isVisible()) {
					m.closeOK();
				}
				m = null;

			}
		}
	}

	/**
	 * �����Ƿ�����չ��
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-7����04:32:03
	 */
	public void setIsVbantchcode() {
		ConditionVO[] vos = getQueryDlg().getConditionVOsByFieldCode(
				"isvbatchcode");
		if (vos == null || vos.length == 0) {
			isVbanthcode = false;
		} else if (PuPubVO.getUFBoolean_NullAs(vos[0].getValue(),
				UFBoolean.FALSE).booleanValue() == false) {
			isVbanthcode = false;
		} else {
			isVbanthcode = true;
		}
	}

	/**
	 * ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-7����02:56:07
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private ReportBaseVO[] zcal(List<ReportBaseVO[]> list) throws Exception {
		ReportBaseVO[] rvos = null;
		if (isVbanthcode == false) {
			ReportBaseVO[] vos = list.get(0);
			ReportBaseVO[] cals = cal1(vos);// ��һ����ѯ������ ������� �� ����
			ReportBaseVO[] vos1 = list.get(1);
			ReportBaseVO[] cals1 = cal2(vos1);// �ڶ�����ѯ������ ����ת�ֲ���;
			ReportBaseVO[] zvos = CombinVO.addByContion2(cals, cals1,
					combinConds1, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos2 = list.get(2);
			ReportBaseVO[] cals2 = cal3(vos2, 24, 26);// ��������ѯ������ ���㱾������
			ReportBaseVO[] zvos1 = CombinVO.addByContion2(zvos, cals2,
					combinConds1, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos3 = list.get(3);
			ReportBaseVO[] cals3 = cal4(vos3);// ���ĸ���ѯ������ ���㱾��ǰ����
			ReportBaseVO[] zvos2 = CombinVO.addByContion2(zvos1, cals3,
					combinConds1, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos4 = list.get(4);
			ReportBaseVO[] cals4 = cal5(vos4);// �������ѯ ������ �����ܲ�ת�ֲֵ���������
			ReportBaseVO[] zvos3 = CombinVO.addByContion2(zvos1, cals4,
					combinConds1, null);// ������׷�ӵ�һ��
			cals4(zvos3);// ����ϼ�
			rvos = zvos3;
		} else {
			ReportBaseVO[] vos = list.get(0);
			ReportBaseVO[] cals = cal1(vos);// ��һ����ѯ������ ������� �� ����
			ReportBaseVO[] vos1 = list.get(1);
			ReportBaseVO[] cals1 = cal2(vos1);// �ڶ�����ѯ������ ����ת�ֲ���;
			ReportBaseVO[] zvos = CombinVO.addByContion2(cals, cals1,
					combinConds1f, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos2 = list.get(2);
			ReportBaseVO[] cals2 = cal3(vos2, 24, 26);// ��������ѯ������ ���㱾������
			ReportBaseVO[] zvos1 = CombinVO.addByContion2(zvos, cals2,
					combinConds1f, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos3 = list.get(3);
			ReportBaseVO[] cals3 = cal4(vos3);// ���ĸ���ѯ������ ���㱾��ǰ����
			ReportBaseVO[] zvos2 = CombinVO.addByContion2(zvos1, cals3,
					combinConds1f, null);// ������׷�ӵ�һ��
			ReportBaseVO[] vos4 = list.get(4);
			ReportBaseVO[] cals4 = cal5(vos4);// �������ѯ ������ �����ܲ�ת�ֲֵ���������
			ReportBaseVO[] zvos3 = CombinVO.addByContion2(zvos1, cals4,
					combinConds1f, null);// ������׷�ӵ�һ��
			cals4(zvos3);// ����ϼ�
			rvos = zvos3;
		}
		return rvos;
	}

	private ReportBaseVO[] cal5(ReportBaseVO[] vos4) throws Exception {
		int loc = 27;// ��¼�ܲ�ת�ֲ� �ֶ�λ��
		if (vos4 == null || vos4.length == 0)
			return null;
		ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(
				(ReportBaseVO[]) ObjectUtils.serializableClone(vos4),
				combinConds1, combinFields, ReportBaseVO.class);
		if (cvos == null || cvos.length == 0)
			return null;
		for (int i = 0; i < cvos.length; i++) {
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("num"));
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("bnum"));
			cvos[i].setAttributeValue("num" + loc, num);
			cvos[i].setAttributeValue("bnum" + loc, bnum);
		}
		return cvos;
	}

	/**
	 * ����ϼ�
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-6����03:42:05
	 * @param zvos2
	 */
	private void cals4(ReportBaseVO[] zvos2) {
		calCombin1(zvos2);// 0-90��ϼ�
		calCombin2(zvos2);// 90-365�ϼ� ��� ȥ���ǰ������
		calCombin3(zvos2);// ��ǰ�����ϼ� ��� ��������ܵĺϼ�
		calCombin4(zvos2);// �ϼ� ����Ƿ��֮ǰ������
		calCombin5(zvos2);// �ܼ�
	}

	private void calCombin4(ReportBaseVO[] zvos2) {
		int loc = 22;
		if (zvos2 == null || zvos2.length == 0)
			return;

		for (int i = 0; i < zvos2.length; i++) {
			UFDouble num = new UFDouble(0);
			UFDouble bnum = new UFDouble(0);
			for (int j = 17; j < 22; j++) {
				num = num.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("num" + j)));
				bnum = bnum.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("bnum" + j)));
			}
			zvos2[i].setAttributeValue("num" + loc, num);
			zvos2[i].setAttributeValue("bnum" + loc, bnum);
		}

	}

	private void calCombin5(ReportBaseVO[] zvos2) {
		int loc = 28;
		if (zvos2 == null || zvos2.length == 0)
			return;

		for (int i = 0; i < zvos2.length; i++) {
			UFDouble num = new UFDouble(0);
			UFDouble bnum = new UFDouble(0);
			for (int j = 22; j < 28; j++) {
				num = num.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("num" + j)));
				bnum = bnum.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("bnum" + j)));
			}
			zvos2[i].setAttributeValue("num" + loc, num);
			zvos2[i].setAttributeValue("bnum" + loc, bnum);
		}
	}

	private void calCombin3(ReportBaseVO[] zvos2) {
		int loc = 17;
		if (zvos2 == null || zvos2.length == 0)
			return;
		for (int i = 0; i < zvos2.length; i++) {
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(zvos2[i]
					.getAttributeValue("num" + 4));
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(zvos2[i]
					.getAttributeValue("bnum" + 4));
			UFDouble num1 = PuPubVO.getUFDouble_NullAsZero(zvos2[i]
					.getAttributeValue("num" + 16));
			UFDouble bnum1 = PuPubVO.getUFDouble_NullAsZero(zvos2[i]
					.getAttributeValue("bnum" + 16));
			zvos2[i].setAttributeValue("num" + loc, num.add(num1));
			zvos2[i].setAttributeValue("bnum" + loc, bnum.add(bnum1));
		}
	}

	private void calCombin2(ReportBaseVO[] zvos2) {

		int loc = 16;// 0-90�ϼ��ֶ�λ��
		if (zvos2 == null || zvos2.length == 0)
			return;

		for (int i = 0; i < zvos2.length; i++) {
			UFDouble num = new UFDouble(0);
			UFDouble bnum = new UFDouble(0);
			for (int j = 5; j < 16; j++) {
				num = num.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("num" + j)));
				bnum = bnum.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("bnum" + j)));
			}
			zvos2[i].setAttributeValue("num" + loc, num);
			zvos2[i].setAttributeValue("bnum" + loc, bnum);
		}

	}

	private void calCombin1(ReportBaseVO[] zvos2) {
		int loc = 4;// 0-90�ϼ��ֶ�λ��
		if (zvos2 == null || zvos2.length == 0)
			return;

		for (int i = 0; i < zvos2.length; i++) {
			UFDouble num = new UFDouble(0);
			UFDouble bnum = new UFDouble(0);
			for (int j = 1; j < 4; j++) {
				num = num.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("num" + j)));
				bnum = bnum.add(PuPubVO.getUFDouble_NullAsZero(zvos2[i]
						.getAttributeValue("bnum" + j)));
			}
			zvos2[i].setAttributeValue("num" + loc, num);
			zvos2[i].setAttributeValue("bnum" + loc, bnum);
		}
	}

	private ReportBaseVO[] cal4(ReportBaseVO[] vos3) throws Exception {
		return cal3(vos3, 23, 25);
	}

	/**
	 *���� ���������������� ����ǰ������������ ���²�������(B�ֿ�ͷ) ����ǰ���۲�������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����01:19:24
	 * @param vos2
	 * @param loc
	 *            �����ֶ�λ��
	 * @return loc1 �����ֶ�λ��
	 * @throws Exception
	 */
	private ReportBaseVO[] cal3(ReportBaseVO[] vos2, int loc, int loc1)
			throws Exception {
		ReportBaseVO[] rvos = null;
		if (isVbanthcode == false) {
			if (vos2 == null || vos2.length == 0) {
				return null;
			}
			// ��������������
			ReportBaseVO[] jichus = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos2),
					combinConds1, combinFields, ReportBaseVO.class);
			// �����۶��� ����id �������ݺϲ� �ϲ����۳����������
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos2),
					soDaiFa, combinFields, ReportBaseVO.class);
			calSoDaiFaBas(cvos);// �������۶�������
			calSoDaiFa(jichus, cvos, loc);// �������۶�������ȥ������
			calSoBuHuo(jichus, cvos, loc1);// �������۶�����������
			calType(jichus);
			rvos = jichus;
		} else {
			if (vos2 == null || vos2.length == 0) {
				return null;
			}
			// ��������������
			ReportBaseVO[] jichus = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos2),
					combinConds1f, combinFields, ReportBaseVO.class);
			// �����۶��� ����id �������ݺϲ� �ϲ����۳����������
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos2),
					soDaiFaf, combinFields, ReportBaseVO.class);
			calSoDaiFaBas(cvos);// �������۶�������
			calSoDaiFa(jichus, cvos, loc);// �������۶�������ȥ������
			calSoBuHuo(jichus, cvos, loc1);// �������۶�����������
			calType(jichus);
			rvos = jichus;

		}
		return rvos;
	}

	/**
	 * ��������۴���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����02:41:04
	 * @param cvos
	 * @return
	 */
	private void calSoDaiFaBas(ReportBaseVO[] cvos) {
		if (cvos == null || cvos.length == 0)
			return;
		for (int i = 0; i < cvos.length; i++) {
			UFDouble nnumber = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("nnumber"));// ��������
			UFDouble npacknumber = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("npacknumber"));// ����������
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("num"));// ���۳�������
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("bnum"));// ���۳��⸨����
			cvos[i].setAttributeValue("num", nnumber.sub(num));
			cvos[i].setAttributeValue("bnum", npacknumber.sub(bnum));
		}
	}

	/**
	 * �������۶��� ��������
	 * 
	 * @���� mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����01:55:09
	 * @param jichus
	 * @param cvos
	 * @param loc1
	 * @throws Exception
	 */
	private void calSoBuHuo(ReportBaseVO[] jichus, ReportBaseVO[] cvos1,
			int loc1) throws Exception {
		if (isVbanthcode == false) {
			if (cvos1 == null || cvos1.length == 0)
				return;
			ReportBaseVO[] nvos = fielterSoBuHuo(cvos1);
			ReportBaseVO[] newvos = (ReportBaseVO[]) CombinVO.combinData(nvos,
					combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, newvos, combinConds1, loc1 + "");
		} else {
			if (cvos1 == null || cvos1.length == 0)
				return;
			ReportBaseVO[] nvos = fielterSoBuHuo(cvos1);
			ReportBaseVO[] newvos = (ReportBaseVO[]) CombinVO.combinData(nvos,
					combinConds1f, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, newvos, combinConds1f, loc1 + "");

		}
	}

	/**
	 * �������۶��� ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����01:55:44
	 * @param jichus
	 * @param loc
	 * @param cvos
	 * @throws Exception
	 */
	private void calSoDaiFa(ReportBaseVO[] jichus, ReportBaseVO[] cvos1, int loc)
			throws Exception {
		if (isVbanthcode == false) {
			if (cvos1 == null || cvos1.length == 0)
				return;
			ReportBaseVO[] nvos = fielterSoDaiFa(cvos1);
			ReportBaseVO[] newvos = (ReportBaseVO[]) CombinVO.combinData(nvos,
					combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, newvos, combinConds1, loc + "");
		} else {
			if (cvos1 == null || cvos1.length == 0)
				return;
			ReportBaseVO[] nvos = fielterSoDaiFa(cvos1);
			ReportBaseVO[] newvos = (ReportBaseVO[]) CombinVO.combinData(nvos,
					combinConds1f, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, newvos, combinConds1f, loc + "");

		}
	}

	/**
	 * ���˳����۴���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����02:25:02
	 * @param vos
	 * @return
	 * @throws Exception
	 */
	private ReportBaseVO[] fielterSoDaiFa(ReportBaseVO[] cvos1)
			throws Exception {
		if (cvos1 == null || cvos1.length == 0)
			return null;
		ReportBaseVO[] cvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos1);
		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();// ��¼��������
		for (int i = 0; i < cvos.length; i++) {
			String billcode = PuPubVO.getString_TrimZeroLenAsNull(cvos[i]
					.getAttributeValue("billcode"));
			if (billcode == null)
				continue;
			if (!billcode.startsWith("B")) {
				list.add(cvos[i]);
			}
		}
		return list.toArray(new ReportBaseVO[0]);
	}

	/**
	 * ���˳����۲�������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����02:25:02
	 * @param vos
	 * @return
	 * @throws Exception
	 */
	private ReportBaseVO[] fielterSoBuHuo(ReportBaseVO[] cvos1)
			throws Exception {
		if (cvos1 == null || cvos1.length == 0)
			return null;
		ReportBaseVO[] cvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos1);
		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();// ��¼��������
		for (int i = 0; i < cvos.length; i++) {
			String billcode = PuPubVO.getString_TrimZeroLenAsNull(cvos[i]
					.getAttributeValue("billcode"));
			if (billcode == null)
				continue;
			if (billcode.startsWith("B")) {
				list.add(cvos[i]);
			}
		}
		return list.toArray(new ReportBaseVO[0]);
	}

	/**
	 * ������;����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-4����08:05:29
	 * @param vos1
	 * @return
	 * @throws Exception
	 */
	private ReportBaseVO[] cal2(ReportBaseVO[] vos) throws Exception {
		ReportBaseVO[] rvos = null;
		if (isVbanthcode == false) {
			if (vos == null || vos.length == 0)
				return null;
			// ����Сά�Ƚ������ݺϲ�
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos),
					zaiTuCombinFields, combinFields, ReportBaseVO.class);
			calZaiTu(cvos);// �������
			calType(cvos);// ��������
			rvos = cvos;
		} else {
			if (vos == null || vos.length == 0)
				return null;
			// ����Сά�Ƚ������ݺϲ�
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(vos),
					zaiTuCombinFieldsf, combinFields, ReportBaseVO.class);
			calZaiTu(cvos);// �������
			calType(cvos);// ��������
			rvos = cvos;

		}
		return rvos;
	}

	/**
	 * ������;����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-4����08:17:00
	 * @param cvos
	 */
	private void calZaiTu(ReportBaseVO[] cvos) {
		int loc = 21 - 1;// ��¼��;���ݵ��ֶ�λ��
		if (cvos == null || cvos.length == 0)
			return;
		for (int i = 0; i < cvos.length; i++) {
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("num"));
			UFDouble bnum = PuPubVO.getUFDouble_NullAsZero(cvos[i]
					.getAttributeValue("bnum"));
			cvos[i].setAttributeValue("num" + loc, num);
			cvos[i].setAttributeValue("bnum" + loc, bnum);
		}
	}

	/**
	 * ����������Ʒ���͵��㷨 ���ӱ����ȥ�����Ч����㷨 ���ô�����㷨 ����һ����ѯ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-4����07:31:58
	 * @param vos
	 * @throws Exception
	 */
	private ReportBaseVO[] cal1(ReportBaseVO[] vos) throws Exception {
		ReportBaseVO[] rvos = null;
		if (isVbanthcode == false) {
			if (vos == null || vos.length == 0)
				return null;
			// ����Сά�Ƚ������ݺϲ�
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(vos,
					combinConds, combinFields, ReportBaseVO.class);
			// ��������������
			ReportBaseVO[] jichus = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(cvos),
					combinConds1, combinFields, ReportBaseVO.class);
			calDays(jichus, cvos);// �������
			calType(jichus);// ������Ʒ����
			calYear(jichus, cvos);// ����ȥ���ǰ��Ĵ����ڴ��
			calDaiJian(jichus, cvos);// �����������
			rvos = jichus;
		} else {
			if (vos == null || vos.length == 0)
				return null;
			// ����Сά�Ƚ������ݺϲ�
			ReportBaseVO[] cvos = (ReportBaseVO[]) CombinVO.combinData(vos,
					combinCondsf, combinFields, ReportBaseVO.class);
			// ��������������
			ReportBaseVO[] jichus = (ReportBaseVO[]) CombinVO.combinData(
					(ReportBaseVO[]) ObjectUtils.serializableClone(cvos),
					combinConds1f, combinFields, ReportBaseVO.class);
			calDays(jichus, cvos);// �������
			calType(jichus);// ������Ʒ����
			calYear(jichus, cvos);// ����ȥ���ǰ��Ĵ����ڴ��
			calDaiJian(jichus, cvos);// �����������
			rvos = jichus;

		}
		return rvos;
	}

	/**
	 * ����ȥ�� �� ǰ��Ĵ�������Ʒ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-3����03:26:36
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calYear(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {
		if (cvos == null || cvos.length == 0)
			return;
		ReportBaseVO[] vos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos);
		List<ReportBaseVO> qus = new ArrayList<ReportBaseVO>();// ���ȥ�������
		List<ReportBaseVO> qis = new ArrayList<ReportBaseVO>();// ���ǰ�������
		int year = ce.getDate().getYear() - 1;// ȥ��
		int year1 = year - 1;// ǰ��
		int loc = 14;// ����ȥ�� �� ǰ�� �ֶε�λ��
		for (int i = 0; i < vos.length; i++) {
			String cdate = PuPubVO.getString_TrimZeroLenAsNull(vos[i]
					.getAttributeValue("creadate"));
			if (cdate == null)
				continue;
			if (cdate.startsWith(year + "")) {
				vos[i].setAttributeValue("num" + loc, vos[i]
						.getAttributeValue("num"));
				vos[i].setAttributeValue("bnum" + loc, vos[i]
						.getAttributeValue("bnum"));
				qus.add(vos[i]);
			}
			if (cdate.startsWith(year1 + "")) {
				vos[i].setAttributeValue("num" + (loc + 1), vos[i]
						.getAttributeValue("num"));
				vos[i].setAttributeValue("bnum" + (loc + 1), vos[i]
						.getAttributeValue("bnum"));
				qis.add(vos[i]);
			}
		}
		if (isVbanthcode == false) {
			if (qus.size() != 0) {
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qus
						.toArray(new ReportBaseVO[0]), combinConds1,
						combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1, null);
			}
			if (qis.size() != 0) {
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qis
						.toArray(new ReportBaseVO[0]), combinConds1,
						combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus,
						qis.toArray(new ReportBaseVO[0]), combinConds1, null);
			}
		} else {
			if (qus.size() != 0) {
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qus
						.toArray(new ReportBaseVO[0]), combinConds1f,
						combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1f, null);
			}
			if (qis.size() != 0) {
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qis
						.toArray(new ReportBaseVO[0]), combinConds1f,
						combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus,
						qis.toArray(new ReportBaseVO[0]), combinConds1f, null);
			}

		}
	}

	private void calDaiJian(ReportBaseVO[] jichus, ReportBaseVO[] cvos1)
			throws Exception {
		ReportBaseVO[] cvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos1);
		if (cvos == null || cvos.length == 0)
			return;

		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();// ��¼����
		int loc = 22 - 1;// ��¼�����λ��
		for (int i = 0; i < cvos.length; i++) {
			String pk = PuPubVO.getString_TrimZeroLenAsNull(cvos[i]
					.getAttributeValue("pk_storestate"));// ��ȡ���״̬
			if (pk == null) {
				continue;
			}
			if (pk.equals(pk_daijian)) {
				list.add(cvos[i]);
			}
		}
		if (list.size() == 0)
			return;
		if (isVbanthcode == false) {
			ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(list
					.toArray(new ReportBaseVO[0]), combinConds1, combinFields,
					ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1, loc + "");
		} else {
			ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(list
					.toArray(new ReportBaseVO[0]), combinConds1f, combinFields,
					ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1f, loc + "");
		}
	}

	/**
	 * ������� ��ÿ���¼���ó��Ŀ������ ׷�ӵ� ������������ ���� ���������˿���ı�������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-2����04:00:05
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calDays(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {
		calDay(jichus, cvos);
		calDay1(jichus, cvos);
	}

	/**
	 * ����0-90�����е�ÿ�µ���Ʒ�ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-2����04:05:13
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calDay1(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {
		if (isVbanthcode == false) {
			for (int i = 0; i < 3; i++) {
				ReportBaseVO[] vos = filterByDays(cvos, hdays[i][0],
						hdays[i][1]);
				if (vos == null || vos.length == 0)
					continue;
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(
						vos, combinConds1, combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1, (i + 1)
						+ "");
			}
		} else {
			for (int i = 0; i < 3; i++) {
				ReportBaseVO[] vos = filterByDays(cvos, hdays[i][0],
						hdays[i][1]);
				if (vos == null || vos.length == 0)
					continue;
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(
						vos, combinConds1f, combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1f, (i + 1)
						+ "");
			}
		}
	}

	private ReportBaseVO[] filterByDays(ReportBaseVO[] cvos, int satrtDays,
			int endDays) throws Exception {
		ReportBaseVO[] nvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos);
		if (nvos == null || nvos.length == 0) {
			return null;
		}
		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();
		for (int i = 0; i < nvos.length; i++) {
			int days = PuPubVO.getInteger_NullAs(nvos[i]
					.getAttributeValue("days"), -1);
			if (days >= satrtDays && days <= endDays) {
				list.add(nvos[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;
		return list.toArray(new ReportBaseVO[0]);
	}

	/**
	 * ����90-365�����е�ÿ�µ���Ʒ�ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-2����04:05:58
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calDay(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {
		if (isVbanthcode == false) {
			for (int i = 3; i < 12; i++) {
				ReportBaseVO[] vos = filterByDays(cvos, hdays[i][0],
						hdays[i][1]);
				if (vos == null || vos.length == 0)
					continue;
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(
						vos, combinConds1, combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1, (i + 2)
						+ "");

			}
		} else {
			for (int i = 3; i < 12; i++) {
				ReportBaseVO[] vos = filterByDays(cvos, hdays[i][0],
						hdays[i][1]);
				if (vos == null || vos.length == 0)
					continue;
				ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(
						vos, combinConds1f, combinFields, ReportBaseVO.class);
				CombinVO.addByContion1(jichus, nevos, combinConds1f, (i + 2)
						+ "");

			}
		}
	}

	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService(
					WdsWlPubConst.WDS_WL_MODULENAME, this, "���ڲ�ѯ...", 1,
					"nc.bs.wds.pub.report.ReportDMO", null, "queryVOBySql",
					ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "����", e.getMessage());
		}
		return reportVOs;
	}

	/**
	 * ��ò�ѯ�ִ�����sql ��Ҫ���� ���� ���� ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-4����07:56:28
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL(String whereSql) throws Exception {
		return WDSWLReportSql.getStoreSql(whereSql);
	}

	/**
	 * ��ò�ѯ ���˶�����sql ��Ҫ���ڼ��� ת�ֲ���;
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-4����07:57:34
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL1(String whereSql) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = filterQuery(vos);
		if (vos1 == null || vos1.length == 0) {
			whereSql = null;
		} else {
			for (int i = 0; i < vos1.length; i++) {
				if (vos1[i].getFieldCode().equals("h.pk_customize1")) {
					vos1[i].setFieldCode("h.pk_inwhouse");
				}
				if (vos1[i].getFieldCode().equals("h.pk_invmandoc")) {
					vos1[i].setFieldCode("b.pk_invmandoc");
				}
			}
			whereSql = querylg.getWhereSQL(vos1);
		}
		return WDSWLReportSql.getDmOrderSql(whereSql);
	}

	/**
	 * ��ѯ���۶��� ���� ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����03:02:25
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL2(String whereSql) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = filterQuery(vos);
		if (vos1 == null || vos1.length == 0) {
			whereSql = null;
		} else {
			for (int i = 0; i < vos1.length; i++) {
				if (vos1[i].getFieldCode().equals("h.pk_customize1")) {
					vos1[i].setFieldCode("tbh.pk_stordoc");
				}
				if (vos1[i].getFieldCode().equals("h.pk_invmandoc")) {
					vos1[i].setFieldCode("b.cinventoryid");
				}
			}
			whereSql = querylg.getWhereSQL(vos1);
		}
		String startDate = ac.getMonthVO().getBegindate().toString();
		String endDate = ac.getMonthVO().getEnddate().toString();
		String wsql = "";
		wsql = " h.dbilldate >= '" + startDate + "' and  h.dbilldate <='"
				+ endDate + "' ";

		if (whereSql == null || whereSql.length() == 0) {

			whereSql = wsql;

		} else {

			whereSql = whereSql + " and " + wsql;

		}
		return WDSWLReportSql.getOrderDaiFaSql(whereSql);
	}

	/**
	 * ��ѯ���۶��� ����ǰ ����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����03:02:25
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL3(String whereSql) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = filterQuery(vos);
		if (vos1 == null || vos1.length == 0) {
			whereSql = null;
		} else {
			for (int i = 0; i < vos1.length; i++) {
				if (vos1[i].getFieldCode().equals("h.pk_customize1")) {
					vos1[i].setFieldCode("tbh.pk_stordoc");
				}
				if (vos1[i].getFieldCode().equals("h.pk_invmandoc")) {
					vos1[i].setFieldCode("b.cinventoryid");
				}
			}
			whereSql = querylg.getWhereSQL(vos1);
		}
		String startDate = ac.getMonthVO().getBegindate().toString();
		String endDate = ac.getMonthVO().getEnddate().toString();
		String wsql = "";
		wsql = " h.dbilldate < '" + startDate + "' ";
		if (whereSql == null || whereSql.length() == 0) {
			whereSql = wsql;
		} else {
			whereSql = whereSql + " and " + wsql;
		}
		return WDSWLReportSql.getOrderDaiFaSql(whereSql);
	}

	/**
	 * ��ѯ��Ӧ���������� �����ܲ�ת�ֱֲ���������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-5����03:02:25
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public String getQuerySQL4(String whereSql) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = filterQuery(vos);
		if (vos1 == null || vos1.length == 0) {
			whereSql = null;
		} else {
			for (int i = 0; i < vos1.length; i++) {
				if (vos1[i].getFieldCode().equals("h.pk_customize1")) {
					vos1[i].setFieldCode("h.cwarehouseid");
				}
				if (vos1[i].getFieldCode().equals("h.pk_invmandoc")) {
					vos1[i].setFieldCode("b.cinventoryid");
				}
			}
			whereSql = querylg.getWhereSQL(vos1);
		}
		String startDate = ac.getMonthVO().getBegindate().toString();
		String endDate = ac.getMonthVO().getEnddate().toString();
		String wsql = "";
		wsql = " h.dbilldate >= '" + startDate + "' and  h.dbilldate <='"
				+ endDate + "' ";
		if (whereSql == null || whereSql.length() == 0) {
			whereSql = wsql;
		} else {
			whereSql = whereSql + " and " + wsql;
		}
		return WDSWLReportSql.getZcXuNiSql(whereSql);
	}

	@Override
	public void initReportUI() {
		setColumn();
		setLocation(7);
		type.put(0, "����");
		type.put(1, "������");
	}

	/**
	 * �����кϲ�
	 */
	private void setColumn() {
		// ������Ŀ��������
		UITable cardTable = getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		ColumnGroup shiji = new ColumnGroup("ʵ�ʿ��");
		// ��ķ��� ��Ч
		ColumnGroup zgroup = new ColumnGroup("��Ч");
		int i = 1;// ���ñ仯��ֵ��
		if (isVbanthcode == true) {
			i = i + 1;
		}
		ColumnGroup a1 = new ColumnGroup("30������");
		a1.add(cardTcm.getColumn(6 + i));
		a1.add(cardTcm.getColumn(7 + i));
		zgroup.add(a1);

		ColumnGroup a2 = new ColumnGroup("30-60��");
		a2.add(cardTcm.getColumn(8 + i));
		a2.add(cardTcm.getColumn(9 + i));
		zgroup.add(a2);

		ColumnGroup a3 = new ColumnGroup("60-90��");
		a3.add(cardTcm.getColumn(10 + i));
		a3.add(cardTcm.getColumn(11 + i));
		zgroup.add(a3);

		ColumnGroup a4 = new ColumnGroup("С��");
		a4.add(cardTcm.getColumn(12 + i));
		a4.add(cardTcm.getColumn(13 + i));
		zgroup.add(a4);

		shiji.add(zgroup);

		ColumnGroup zgroup2 = new ColumnGroup("��Ч");
		ColumnGroup b1 = new ColumnGroup("91-120");
		b1.add(cardTcm.getColumn(14 + i));
		b1.add(cardTcm.getColumn(15 + i));
		zgroup2.add(b1);

		ColumnGroup b2 = new ColumnGroup("121-150");
		b2.add(cardTcm.getColumn(16 + i));
		b2.add(cardTcm.getColumn(17 + i));
		zgroup2.add(b2);

		ColumnGroup b3 = new ColumnGroup("151-180");
		b3.add(cardTcm.getColumn(18 + i));
		b3.add(cardTcm.getColumn(19 + i));
		zgroup2.add(b3);

		ColumnGroup b4 = new ColumnGroup("181-210");
		b4.add(cardTcm.getColumn(20 + i));
		b4.add(cardTcm.getColumn(21 + i));
		zgroup2.add(b4);

		ColumnGroup b5 = new ColumnGroup("211-240");
		b5.add(cardTcm.getColumn(22 + i));
		b5.add(cardTcm.getColumn(23 + i));
		zgroup2.add(b5);

		ColumnGroup b6 = new ColumnGroup("241-270");
		b6.add(cardTcm.getColumn(24 + i));
		b6.add(cardTcm.getColumn(25 + i));
		zgroup2.add(b6);

		ColumnGroup b7 = new ColumnGroup("271-300");
		b7.add(cardTcm.getColumn(26 + i));
		b7.add(cardTcm.getColumn(27 + i));
		zgroup2.add(b7);

		ColumnGroup b8 = new ColumnGroup("301-330");
		b8.add(cardTcm.getColumn(28 + i));
		b8.add(cardTcm.getColumn(29 + i));
		zgroup2.add(b8);

		ColumnGroup b9 = new ColumnGroup("331-365");
		b9.add(cardTcm.getColumn(30 + i));
		b9.add(cardTcm.getColumn(31 + i));
		zgroup2.add(b9);

		ColumnGroup b10 = new ColumnGroup("ȥ��");
		b10.add(cardTcm.getColumn(32 + i));
		b10.add(cardTcm.getColumn(33 + i));
		zgroup2.add(b10);

		ColumnGroup b11 = new ColumnGroup("ǰ��");
		b11.add(cardTcm.getColumn(34 + i));
		b11.add(cardTcm.getColumn(35 + i));
		zgroup2.add(b11);

		ColumnGroup b12 = new ColumnGroup("С��");
		b12.add(cardTcm.getColumn(36 + i));
		b12.add(cardTcm.getColumn(37 + i));
		zgroup2.add(b12);
		shiji.add(zgroup2);

		ColumnGroup c1 = new ColumnGroup("С��");
		c1.add(cardTcm.getColumn(38 + i));
		c1.add(cardTcm.getColumn(39 + i));
		shiji.add(c1);

		// ColumnGroup c2=new ColumnGroup("(����Ʒ180g-200g)");
		// c2.add(cardTcm.getColumn(40+i));
		// c2.add(cardTcm.getColumn(41+i));
		//        
		// shiji.add(c2);
		i = i - 2;
		ColumnGroup c3 = new ColumnGroup("ҽ����Ʒ");
		c3.add(cardTcm.getColumn(42 + i));
		c3.add(cardTcm.getColumn(43 + i));
		shiji.add(c3);

		ColumnGroup c4 = new ColumnGroup("�����򷢻�(���δ���)");
		c4.add(cardTcm.getColumn(44 + i));
		c4.add(cardTcm.getColumn(45 + i));
		shiji.add(c4);

		ColumnGroup c5 = new ColumnGroup("ת�ֲ� (��;)");
		c5.add(cardTcm.getColumn(46 + i));
		c5.add(cardTcm.getColumn(47 + i));
		shiji.add(c5);

		ColumnGroup c6 = new ColumnGroup("����(�ӻ��������ֳ���)");
		c6.add(cardTcm.getColumn(48 + i));
		c6.add(cardTcm.getColumn(49 + i));
		shiji.add(c6);

		cardHeader.addColumnGroup(shiji);
		ColumnGroup c7 = new ColumnGroup("�ϼ�");
		c7.add(cardTcm.getColumn(50 + i));
		c7.add(cardTcm.getColumn(51 + i));
		cardHeader.addColumnGroup(c7);

		ColumnGroup qianfa = new ColumnGroup("Ƿ�����");
		ColumnGroup zgroup3 = new ColumnGroup("��������");

		ColumnGroup d1 = new ColumnGroup("����ǰ��������");
		d1.add(cardTcm.getColumn(52 + i));
		d1.add(cardTcm.getColumn(53 + i));
		zgroup3.add(d1);

		ColumnGroup d2 = new ColumnGroup("������������");
		d2.add(cardTcm.getColumn(54 + i));
		d2.add(cardTcm.getColumn(55 + i));
		zgroup3.add(d2);

		ColumnGroup d3 = new ColumnGroup("����ǰ����������B��ͷ��");
		d3.add(cardTcm.getColumn(56 + i));
		d3.add(cardTcm.getColumn(57 + i));
		zgroup3.add(d3);

		ColumnGroup d4 = new ColumnGroup("���²���������B��ͷ��");
		d4.add(cardTcm.getColumn(58 + i));
		d4.add(cardTcm.getColumn(59 + i));
		zgroup3.add(d4);
		qianfa.add(zgroup3);

		ColumnGroup zgroup4 = new ColumnGroup("ת�ֲ�");

		ColumnGroup e1 = new ColumnGroup("���������ܲ�ת�ֲֿ����");
		e1.add(cardTcm.getColumn(60 + i));
		e1.add(cardTcm.getColumn(61 + i));
		zgroup4.add(e1);
		qianfa.add(zgroup4);

		cardHeader.addColumnGroup(qianfa);
		ColumnGroup e2 = new ColumnGroup("�ܼ�");
		e2.add(cardTcm.getColumn(62 + i));
		e2.add(cardTcm.getColumn(63 + i));

		cardHeader.addColumnGroup(e2);

		getReportBase().getBillModel().updateValue();
	}

	public String _getModelCode() {
		return WdsWlPubConst.report1;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	/**
	 * �������ĳ��ò���������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-12-3����02:21:21
	 * @param vos
	 */
	public static Map<Integer, String> type = new HashMap<Integer, String>();// ��Ʒ����

	public void calType(ReportBaseVO[] vos) {
		if (vos == null || vos.length == 0)
			return;
		for (int i = 0; i < vos.length; i++) {
			int t = PuPubVO.getInteger_NullAs(vos[i]
					.getAttributeValue("invtype"), -1);
			String value = type.get(t);
			if (value == null || value.length() == 0)
				continue;
			vos[i].setAttributeValue("typename", value);
		}
	}
}
