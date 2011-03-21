/*package nc.ui.wds.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w80060406.TbFydmxnewVO;

public class W80060406Impl  {

	// ���ݿ����
	ArrayList<?> results = null;

	PersistenceManager sessioManager = null;

	*//**
	 * �ƻ�����еĲ�ѯ��ϸ��ť���� ����������ѯ�����е�ֵ ���ز����ϸ����
	 * 
	 * @throws ParseException
	 *//*
	public TbFydmxnewVO[] queryFydmxnewVO(String strWhere, String stock,
			String begindate, String enddate, String stockr)
			throws BusinessException, ParseException {
		// TODO Auto-generated method stub
		TbFydmxnewVO[] spvo = null;
		// ���ڵĲ�ѯ�������ǰ����еķ��������Ķ���ѯ������ �����ƻ���Ϊ�յ�
		String sql = "select t.seb_pk ,t.se_pk,t.pk_invbasdoc,t.seb_plannum,b.se_type from tb_shipentry b,tb_shipentry_b t,bd_invbasdoc i  where b.dr = 0 and t.dr = 0 and b.se_pk = t.se_pk and i.pk_invbasdoc = t.pk_invbasdoc  "
				+ strWhere;

		List<TbFydmxnewVO> fydmxMonthList = new ArrayList<TbFydmxnewVO>();
		List<TbFydmxnewVO> fydmxList = new ArrayList<TbFydmxnewVO>();
		List<TbFydmxnewVO> tempList;
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			// ��ѯ
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		// �ж��Ƿ�Ϊ��
		if (results != null && !results.isEmpty()) {
			// ����ֵ
			String sebpk = "";

			// ѭ����ֵ
			for (int i = 0; i < results.size(); i++) {
				TbFydmxnewVO fydmx = new TbFydmxnewVO();
				boolean type = false; // �ж����¼ƻ�����׷�Ӽƻ�
				// �ж��Ƿ�Ϊ�¼ƻ�
				if (WDSTools.getString_TrimZeroLenAsNull(
						((Object[]) results.get(i))[4]).toString().equals("0")) {
					fydmx.setSeb_pk(WDSTools
							.getString_TrimZeroLenAsNull(((Object[]) results
									.get(i))[0]));
					type = true;
				} else {
					fydmx.setSeb_pk(WDSTools
							.getString_TrimZeroLenAsNull(((Object[]) results
									.get(i))[0]));
				}
				fydmx
						.setCfd_dw(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[1]));
				fydmx
						.setPk_invbasdoc(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[2]));
				fydmx
						.setCfd_plannum(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(i))[3]));
				if (type) {
					fydmxMonthList.add(fydmx); // �¼ƻ����������ֵ
				} else
					fydmxList.add(fydmx); // ׷�Ӽƻ����������ֵ
			}
			List<TbFydmxnewVO> fydmxVOList = new ArrayList<TbFydmxnewVO>();
			// �ж��¼ƻ������Ƿ�Ϊ��
			if (fydmxMonthList.size() > 0) {
				// ѭ���¼ƻ�����
				for (int i = 0; i < fydmxMonthList.size(); i++) {
					TbFydmxnewVO fydmx = fydmxMonthList.get(i); // ��ȡ����
					// �ж�׷�Ӽ����Ƿ�Ϊ��
					if (null != fydmxList && fydmxList.size() > 0) {
						// ѭ��׷�Ӽƻ�����
						for (int j = 0; j < fydmxList.size(); j++) {
							TbFydmxnewVO fydmxvo = fydmxList.get(j); // ��ȡ����
							// �ж����������е���Ʒ�����Ƿ�һ��
							if (fydmx.getPk_invbasdoc().equals(
									fydmxvo.getPk_invbasdoc())) {
								// �ж�׷�Ӽ����еļƻ����Ƿ�Ϊ��
								if (null != fydmxvo.getCfd_plannum()
										&& !"".equals(fydmxvo.getCfd_plannum())) {
									// �ж��¼ƻ������еļƻ����Ƿ�Ϊ��
									if (null != fydmx.getCfd_plannum()
											&& !"".equals(fydmx
													.getCfd_plannum())) {
										// ������߼ƻ�������Ϊ�� ������� ����ܵļƻ�����
										fydmx.setCfd_plannum(new UFDouble(fydmx
												.getCfd_plannum().toDouble()
												+ fydmxvo.getCfd_plannum()
														.toDouble()));
									} else { // ����¼ƻ��мƻ���Ϊ��
										// ��׷�Ӽƻ������еļƻ��������¼ƻ��еļƻ���
										fydmx.setCfd_plannum(new UFDouble(
												fydmxvo.getCfd_plannum()
														.toDouble()));
									}
									// fydmxList.remove(j);
									// j--;
								}
							}
						}
					}
					fydmxVOList.add(fydmx);
				}
				if (fydmxVOList.size() > 0) {
					// ѭ���Ƴ��ƻ���Ϊ�յ���
					for (int i = 0; i < fydmxVOList.size(); i++) {
						TbFydmxnewVO fydmx = fydmxVOList.get(i);
						if (null == fydmx.getCfd_plannum()
								|| "".equals(fydmx.getCfd_plannum())
								|| fydmx.getCfd_plannum().toDouble() == 0) {
							fydmxVOList.remove(i);
							i--;
						}
					}
					////////////���·��� ��ѯ������վ�ͷ���վ�����۴����������ѷ�����///////////////////
					tempList = new ArrayList<TbFydmxnewVO>();
					for (int i = 0; i < fydmxVOList.size(); i++) {

						TbFydmxnewVO fydmx = fydmxVOList.get(i);

						sql = "select sum(case"
								+ " when tb_fydnew.fyd_fyzt = 1 then"
								+ " tb_fydmxnew.cfd_sfsl"
								+ " end ) ,"
								+ " sum(case"
								+ "  when tb_fydnew.fyd_fyzt = 0 then"
								+ "   tb_fydmxnew.cfd_xs"
								+ " end )"
								+ " from  tb_fydnew "
								+ " inner join tb_fydmxnew on tb_fydnew.fyd_pk = tb_fydmxnew.fyd_pk"
								+ " inner join bd_invbasdoc on tb_fydmxnew.pk_invbasdoc ="
								+ "                         bd_invbasdoc.pk_invbasdoc"
								+ " where (tb_fydnew.iprintcount > 0)"
								+ "  and (tb_fydnew.dmakedate >= '" + begindate
								+ "')" + "  and (tb_fydnew.dmakedate <= '"
								+ enddate + "')"
								+ "  and (tb_fydmxnew.pk_invbasdoc = '"
								+ fydmx.getPk_invbasdoc() + "')"
								+ "  and (tb_fydmxnew.dr = '0')";

						// ����վ
						strWhere = "  and (tb_fydnew.srl_pk = '" + stock + "')";
						String tempsql = sql + strWhere;
						this.getStockNum(tempsql, fydmx, true);
						// ����վ
						strWhere = "  and (tb_fydnew.srl_pk = '" + stockr
								+ "')";
						tempsql = "";
						tempsql = sql + strWhere;
						this.getStockNum(tempsql, fydmx, false);
						tempList.add(fydmx);
					}
					spvo = new TbFydmxnewVO[tempList.size()];
					spvo = tempList.toArray(spvo);
				}

				
				 * //////////////////////���·�����Ϊ���������վ���ϸ��µĵ�Ʒ����ֵ//////////////////////////////////////
				 * if (fydmxVOList.size() > 0) { SimpleDateFormat format = new
				 * SimpleDateFormat("yyyy-MM-dd"); Date begin =
				 * format.parse(begindate); format = new SimpleDateFormat("MM");
				 * int beginmonth = Integer.parseInt(format.format(begin)); //
				 * ��ʼ�� format = new SimpleDateFormat("yyyy"); int beginyear =
				 * Integer.parseInt(format.format(begin)); // ��ʼ�� // ����ʱ�� String
				 * edate = beginyear + "-" + beginmonth + "-20"; beginmonth =
				 * beginmonth - 1; // ƴװ��ʼʱ�� if (beginmonth == 0) { beginmonth =
				 * 12; beginyear = beginyear - 1; } String bdate = beginyear +
				 * "-" + beginmonth + "-21"; // ת�� format = new
				 * SimpleDateFormat("yyyy-MM-dd"); bdate =
				 * format.format(format.parse(bdate)); edate =
				 * format.format(format.parse(edate)); sql = "select
				 * sum(ic_general_b.noutassistnum),ic_general_b.cinvbasid " +
				 * "from ic_general_h,ic_general_b " + " where
				 * ic_general_h.cgeneralhid = ic_general_b.cgeneralhid " + " and
				 * fbillflag = 3 " + "and cbilltypecode = '4C' " + "and
				 * cwarehouseid = '" + stock + "' " + " and dbilldate between '" +
				 * bdate + "' and '" + edate + "' " + " group by
				 * ic_general_b.cinvbasid";
				 * 
				 * try { sessioManager = PersistenceManager.getInstance();
				 * JdbcSession jdbcSession = sessioManager .getJdbcSession(); //
				 * ���ݲֿ� ʱ������ ������� �Ƿ������ϣ���ѯ�������ÿ����Ʒ�ĺ� results = (ArrayList<?>)
				 * jdbcSession.executeQuery(sql, new ArrayListProcessor()); }
				 * catch (DbException e) { e.printStackTrace(); } // �жϽ�����Ƿ�Ϊ��
				 * if (null != results && !results.isEmpty()) { // ѭ�� for (int i =
				 * 0; i < results.size(); i++) { // ��ȡ��Ʒ���� String pkinv =
				 * WDSTools .getString_TrimZeroLenAsNull(((Object[]) results
				 * .get(i))[1]); // �ж��Ƿ�Ϊ�� if (null != pkinv &&
				 * !"".equals(pkinv)) { // ѭ���ƻ��ӱ��VO for (int j = 0; j <
				 * fydmxVOList.size(); j++) { // ��ȡ���� TbFydmxnewVO fydmx =
				 * fydmxVOList.get(j); // �ж϶����еĵ�Ʒ�����Ƿ�Ϊ�� if (null !=
				 * fydmx.getPk_invbasdoc() && !"".equals(fydmx
				 * .getPk_invbasdoc())) { // �ж�������Ʒ�Ƿ���� if (pkinv.equals(fydmx
				 * .getPk_invbasdoc())) { // �����Ʒһ�� �Ѳ�ѯ���������ۺ� �����ƻ���VO fydmx
				 * .setCfd_maxstock(WDSTools .getUFDouble_NullAsZero(((Object[])
				 * results .get(i))[0])); } } } } } } }
				 // ////////////////////////////////////////////////////////////////////
			}
		}
		return spvo;
	}

	*//**
	 * ����������ѯ������վ���߷���վ�Ĵ����˺��ѷ�������
	 * 
	 * @param sql
	 *            SQL���
	 * @param fydmx
	 *            �����Ķ�����и�ֵ
	 * @param type
	 *            ���� true Ϊ����վ falseΪ����վ
	 *//*
	private void getStockNum(String sql, TbFydmxnewVO fydmx, boolean type) {
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			// ��ѯ
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		// �ж��Ƿ�Ϊ��
		if (results != null && !results.isEmpty()) {
			if (type) {
				// ����վ�ѷ�����
				fydmx
						.setCfd_toshipped(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
				// ����վ��������
				fydmx
						.setCfd_saletravel(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[1]));
			} else {
				// ����վ�ѷ�����
				fydmx
						.setCfd_outshipped(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]));
				// ����վ��������
				fydmx
						.setCfd_outtravel(WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[1]));
			}
		}
	}
}
*/