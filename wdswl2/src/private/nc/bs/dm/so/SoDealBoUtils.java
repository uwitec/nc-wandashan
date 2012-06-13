package nc.bs.dm.so;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * ���˼ƻ����� ������
 * 
 * @author Administrator
 * 
 */
public class SoDealBoUtils {

	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	private StockInvOnHandBO stockbo = null;

	private StockInvOnHandBO getStockBO() {
		if (stockbo == null) {
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-11-28����08:29:20
	 * @param fisdate
	 *            :�Ƿ������
	 * @param pk_corp
	 * @param pk_stordoc:����ֿ�
	 * @param bodys
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, StoreInvNumVO> initInvNumInfor(boolean fisdate,
			String pk_corp, String pk_stordoc, List<SoDealVO> bodys)
			throws BusinessException {
		Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();
		if (bodys == null || bodys.size() == 0) {
			return invNumInfor;
		}
		Set<String> cinvids = new HashSet<String>();// ���ΰ��ŵ����д��id
		StoreInvNumVO tmpNumVO = null;
		String key = null;
		for (SoDealVO body : bodys) {
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(body
					.getCinvbasdocid());
			cinvids.add(body.getCinvbasdocid());
			if (invNumInfor.containsKey(key)) {
				tmpNumVO = invNumInfor.get(key);
			} else {
				tmpNumVO = new StoreInvNumVO();
				tmpNumVO.setPk_corp(pk_corp);
				tmpNumVO.setCstoreid(pk_stordoc);
				tmpNumVO.setCinvbasid(body.getCinvbasdocid());
				tmpNumVO.setCinvmanid(body.getCinventoryid());
				String strWhere = null;
				// ��ѯ�����
				if (fisdate) {
					strWhere = " ss_pk='" + WdsWlPubConst.WDS_STORSTATE_PK
							+ "'";
				} else {
					strWhere = " ss_pk in('"
							+ WdsWlPubConst.WDS_STORSTATE_PK_hg + "')";
					// strWhere= " ss_pk
					// in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
				}
				UFDouble[] stocknums = getStockBO().getInvStockNum(pk_corp,
						tmpNumVO.getCstoreid(), null, tmpNumVO.getCinvbasid(),
						null, null, strWhere);
				if (stocknums == null || stocknums.length == 0) {
					String reason = " ���:"
							+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO
									.getCinvbasid()) + " �޿����";
					if (fisdate) {
						reason = "������״̬��" + reason;
					} else {
						reason = "�ϸ񣬴���״̬��" + reason;
					}
					Logger.info(reason);
					throw new BusinessException(reason);
				}
				tmpNumVO.setNstocknum(stocknums[0]);
				tmpNumVO.setNstockassnum(stocknums[1]);
			}
			// ������Ҫ��������
			tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplannum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
			tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplanassnum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));
			invNumInfor.put(key, tmpNumVO);
		}
		if (invNumInfor.size() == 0) {
			Logger.info("���δ����Ŵ������Ϊ�գ��޷����ţ��˳�");
			return invNumInfor;
		}
		// 2.��������˵�ռ�����ͷ��˶���ռ����
		Logger.info("��ȡ����Ѱ���δ������...");
		TempTableUtil tt = new TempTableUtil();
		String[] invs = cinvids.toArray(new String[0]);
		String strWhereSO = null;
		if (fisdate) {
			strWhereSO = " coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		} else {
			strWhereSO = " coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor1 = getStockBO()
				.getSoOrderNdealNumInfor(pk_corp, pk_stordoc, strWhereSO);
		if (invNumInfor1 == null || invNumInfor1.size() == 0) {
			Logger.info("���ΰ��ŵĴ���������Ѱ���δ������");
			if (invNumInfor1 == null)
				invNumInfor1 = new HashMap<String, UFDouble[]>();
		}
		String strWherePlan = null;
		if (fisdate) {
			strWherePlan = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		} else {
			strWherePlan = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor2 = getStockBO()
				.getPlanOrderNdealNumInfor(pk_corp, pk_stordoc, strWherePlan);
		if (invNumInfor2 == null || invNumInfor2.size() == 0) {
			Logger.info("���ΰ��ŵĴ���������Ѱ���δ������");
			if (invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
		}
		for (String key2 : invNumInfor.keySet()) {
			tmpNumVO = invNumInfor.get(key2);
			UFDouble[] stocknums = invNumInfor1.get(key2);
			UFDouble[] stocknum2 = invNumInfor2.get(key2);
			if (tmpNumVO == null)
				continue;
			// 1.�Ѱ����˵�ռ����
			UFDouble nplannum = PuPubVO.getUFDouble_NullAsZero(null);
			UFDouble nplanassnum = PuPubVO.getUFDouble_NullAsZero(null);
			if (stocknums != null) {
				nplannum = nplannum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknums[0]));
				nplanassnum = nplanassnum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknums[1]));

			}
			if (stocknum2 != null) {
				nplannum = nplannum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknum2[0]));
				nplanassnum = nplanassnum.add(PuPubVO
						.getUFDouble_NullAsZero(stocknum2[1]));

			}
			tmpNumVO.setNdealnum(nplannum);
			tmpNumVO.setNdealassnum(nplanassnum);
			// 2. ��ǰ������=�����-�Ѿ����ŵ��˵�ռ����
			tmpNumVO.setNnum(tmpNumVO.getNstocknum()
					.sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(
					tmpNumVO.getNdealassnum()));
			// 3. ��������� > ���ΰ����� �����Ϊ�ɰ���
			if (tmpNumVO.getNassnum().doubleValue() >= tmpNumVO
					.getNplanassnum().doubleValue()) {
				tmpNumVO.setBisok(UFBoolean.TRUE);
			} else {
				tmpNumVO.setBisok(UFBoolean.FALSE);
				String reason = " ���"
						+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO
								.getCinvbasid()) + " ��ǰ������"
						+ tmpNumVO.getNstockassnum() + " �Ѱ���δ��������"
						+ tmpNumVO.getNdealassnum() + " ���ο�������"
						+ tmpNumVO.getNassnum() + " ���δ�����������"
						+ tmpNumVO.getNplanassnum();
				if (fisdate) {
					reason = "������״̬" + reason;
				} else {
					reason = "�ϸ񣬴���״̬" + reason;
				}
				Logger.info(reason);
				throw new BusinessException(reason);
			}

		}
		return invNumInfor;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ������վ������Ͱ��ź���������
	 * @ʱ�䣺2011-11-4����10:26:39
	 * @param pk_storedoc:�����ֿ�
	 * @param datas
	 * @throws BusinessException
	 */
	void arrangStornumout(String pk_corp, String pk_storedoc, SoDealVO[] datas)
			throws BusinessException {
		if (datas == null || datas.length == 0) {
			return;
		}
		if (pk_corp == null || "".equalsIgnoreCase(pk_corp)) {
			pk_corp = SQLHelper.getCorpPk();
		}
		String pk_outwhous = PuPubVO.getString_TrimZeroLenAsNull(pk_storedoc);
		if (pk_outwhous == null) {
			throw new BusinessException("�����ֿⲻ��Ϊ�գ����鵱ǰ������Ա�󶨲ֿ�");
		}
		// 1.һ�β�ѯ��ǰ�ֿ������д���Ŀ����������ȽϷ���
		ArrayList<String> pk_invbasdocs = new ArrayList<String>();
		for (int j = 0; j < datas.length; j++) {
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(datas[j]
					.getAttributeValue("cinvbasdocid"));
			if (pk_invbasdoc == null || pk_invbasdocs.contains(pk_invbasdoc)) {// ����������Ϊ�գ������Ѿ������������
				continue;
			}
			pk_invbasdocs.add(pk_invbasdoc);
		}
		if (pk_invbasdocs.size() == 0) {
			return;
		}
		// 2 �������ѯ
		// 2.1����������������Ʒ�ʹ�����
		Map<String, UFDouble[]> invStornum_drq = getStockNum(true, pk_corp,
				pk_outwhous, pk_invbasdocs);// �����ڿ����
		Map<String, UFDouble[]> invStornum_hg = getStockNum(false, pk_corp,
				pk_outwhous, pk_invbasdocs);// �ϸ�Ʒ�����
		// 3 ��ѯ�����˵�ռ��������������Ʒ�ʹ�����
		Map<String, UFDouble[]> soordernum_drq = getSoOrderNdealNumInfor(true,
				pk_corp, pk_outwhous, pk_invbasdocs);
		Map<String, UFDouble[]> soordernum_hg = getSoOrderNdealNumInfor(false,
				pk_corp, pk_outwhous, pk_invbasdocs);
		// 4 ��ѯ���˶���ռ��������������Ʒ�ʹ�����
		Map<String, UFDouble[]> sendordernum_drq = getPlanOrderNdealNumInfor(
				true, pk_corp, pk_outwhous, pk_invbasdocs);
		Map<String, UFDouble[]> sendordernum_hg = getPlanOrderNdealNumInfor(
				false, pk_corp, pk_outwhous, pk_invbasdocs);
		dealNum(true, invStornum_drq, soordernum_drq, sendordernum_drq, datas);
		dealNum(false, invStornum_hg, soordernum_hg, sendordernum_hg, datas);
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-1-6����08:51:05
	 * @param fisdate
	 * @param invStornum:�����
	 * @param invNumInfor1�������˵�ռ����
	 * @param invNumInfor2:���˶���ռ����
	 * @param datas
	 */
	protected void dealNum(boolean fisdate, Map<String, UFDouble[]> invStornum,
			Map<String, UFDouble[]> invNumInfor1,
			Map<String, UFDouble[]> invNumInfor2, SoDealVO[] datas) {
		if(datas == null || datas.length ==0){
			return;
		}
		if (invStornum == null || invStornum.size() ==0) {//�޿�������򲻽��з���
			return;
		}
		// 3.���ݴ������ ��������� ������
		for (String key : invStornum.keySet()) {
			UFDouble stornum = PuPubVO.getUFDouble_NullAsZero(0);// ����վ���������
			UFDouble nuesfulnum = PuPubVO.getUFDouble_NullAsZero(0);// ������==�����-�����˵�ռ����-���˶���ռ����
			
			UFDouble[] stornums = invStornum.get(key);
			if (stornums !=null && stornums.length > 0) {
				stornum = PuPubVO.getUFDouble_NullAsZero(stornums[0]);
			}
			//�����˵�ռ����
			UFDouble nsoordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if (invNumInfor1 != null && invNumInfor1.size() > 0) {
				UFDouble[] nsoordernums = invNumInfor1.get(key);
				if (nsoordernums != null && nsoordernums.length > 0) {
					nsoordernum = PuPubVO
							.getUFDouble_NullAsZero(nsoordernums[0]);
				}
			}
			//���˶���ռ����
			UFDouble nsendordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if (invNumInfor2 != null && invNumInfor2.size() > 0) {
				UFDouble[] nsendordernums = invNumInfor2.get(key);
				if (nsendordernums !=null && nsendordernums.length > 0) {
					nsendordernum = PuPubVO
							.getUFDouble_NullAsZero(nsendordernums[0]);
				}
				nuesfulnum = stornum.sub(nsoordernum).sub(nsendordernum);
			}
			for (int j = 0; j < datas.length; j++) {
				String pk_invbasdoc = PuPubVO
						.getString_TrimZeroLenAsNull(datas[j]
								.getAttributeValue("cinvbasdocid"));
				if (pk_invbasdoc == null) {// ����������Ϊ�������
					continue;
				}
				UFDouble ndealnum = PuPubVO.getUFDouble_NullAsZero(datas[j].getNnumber()).sub(PuPubVO.getUFDouble_NullAsZero(datas[j].getNtaldcnum()));
				if (pk_invbasdoc.equalsIgnoreCase(key)) {
					if (fisdate) {
						datas[j].setNdrqstorenumout(stornum);
						datas[j].setNdrqarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNdrqusefulnumout(nuesfulnum);
						datas[j].setNdrqarrusefulnumout(nuesfulnum
								.sub(ndealnum));
					} else {
						datas[j].setNstorenumout(stornum);
						datas[j].setNarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNusefulnumout(nuesfulnum);
						datas[j].setNarrusefulnumout(nuesfulnum.sub(ndealnum));

					}
				}
			}
		}

	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-1-6����08:09:28
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getStockNum(boolean fisdate,
			String pk_corp, String pk_outwhous, ArrayList<String> pk_invbasdocs)
			throws BusinessException {
		String strWhere = null;
		if (fisdate) {
			strWhere = " ss_pk='" + WdsWlPubConst.WDS_STORSTATE_PK + "'";// ss_pk
																			// ���״̬id

		} else {
			strWhere = " ss_pk in('" + WdsWlPubConst.WDS_STORSTATE_PK_hg + "')";
		}
		return getStockBO().getStockNum(pk_corp, pk_outwhous, null,
				pk_invbasdocs, null, null, strWhere);
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-1-6����08:13:13
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getSoOrderNdealNumInfor(boolean fisdate,
			String pk_corp, String pk_outwhous, ArrayList<String> pk_invbasdocs)
			throws BusinessException {
		String strWhere = null;
		TempTableUtil tt = new TempTableUtil();
		if (fisdate) {
			strWhere = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		} else {
			strWhere = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor = getStockBO()
				.getSoOrderNdealNumInfor(pk_corp, pk_outwhous, strWhere);

		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("���β�ѯ�Ĵ�����˶����������Ѱ���δ������");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;

	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-1-6����08:13:13
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getPlanOrderNdealNumInfor(
			boolean fisdate, String pk_corp, String pk_outwhous,
			ArrayList<String> pk_invbasdocs) throws BusinessException {
		TempTableUtil tt = new TempTableUtil();
		String strWherePlan = null;
		if (fisdate) {
			strWherePlan = "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		} else {
			strWherePlan = "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "
					+ tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor = getStockBO()
				.getPlanOrderNdealNumInfor(pk_corp, pk_outwhous, strWherePlan);
		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("���β�ѯ�Ĵ�����˶����������Ѱ���δ������");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;
	}
}
