package nc.bs.wds.ic.transfer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.transfer.MyBillVO;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.wds.transfer.TransferVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * ��λ������
 * 
 * @author yf
 * 
 */
public class TransferBO {
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}

	/**
	 * 
	 * @���ߣ�yf
	 * @˵�������ɽ������Ŀ ����ǰУ��
	 * @ʱ�䣺2012-7-17����09:25:52
	 * @param billVo
	 *            nc.vo.wds.transfer.MyBillVO
	 * @throws BusinessException
	 */
	public void beforeSave(HYBillVO billVo) throws BusinessException {
		if (!(billVo instanceof MyBillVO)) {
			return;
		}
		MyBillVO billvo = (MyBillVO) billVo;
		TransferVO hvo = (TransferVO) billvo.getParentVO();
		TransferBVO[] bvos = (TransferBVO[]) billvo.getChildrenVO();
		if (hvo == null || bvos == null || bvos.length == 0) {
			return;
		}
		checkBodyInCargdocInv(bvos, hvo);
		// checkHeadCargdoc2Inv(bvos, hvo);
	}

	/**
	 * 
	 * @���ߣ�yf
	 * @˵�������ɽ������Ŀ У���������λ�Ƿ�󶨴��
	 * @ʱ�䣺2012-7-17����09:30:07
	 * @param bvos
	 * @param hvo
	 * @throws BusinessException
	 */
	private void checkBodyInCargdocInv(TransferBVO[] bvos, TransferVO hvo)
			throws BusinessException {
		// String pk_cargdoc = hvo.getPk_cargdoc2();
		Map<String, List<String>> map = new Hashtable<String, List<String>>();
		int length = bvos.length;
		String[] invids = new String[length];

		List<String> invcodes = new ArrayList<String>();

		String pk_cargdoc = "";
		String invbasdoc = "";
		for (int i = 0; i < length; i++) {
			pk_cargdoc = bvos[i].getPk_cargdoc2();
			invbasdoc = bvos[i].getCinvbasid();

			if (map.containsKey(pk_cargdoc)) {
				map.get(pk_cargdoc).add(invbasdoc);
			} else {
				List<String> l = new ArrayList<String>();
				l.add(invbasdoc);
				map.put(pk_cargdoc, l);
			}
		}
		if (map == null || map.size() == 0) {
			return;
		}
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String cargdoc = it.next();
			if (PuPubVO.getString_TrimZeroLenAsNull(cargdoc) == null
					|| map.get(cargdoc) == null || map.get(cargdoc).size() == 0) {
				continue;
			}
			//������in�������鲻�ܳ���1000�������������Ҫ�޸�
			if(map.get(cargdoc).size() >= 1000){
				throw new BusinessException("����������1000����");
			}
			List l2 = getNotHasInv(hvo.getPk_corp(), hvo.getSrl_pk(), cargdoc,
					map.get(cargdoc));
			if (l2 != null && l2.size() > 0) {
				invcodes.addAll(l2);
			}
		}

		if (invcodes.size() > 0) {
			throw new BusinessException("���´��������λû�а�:\n"
					+ WdsWlPubTool.getSubSql2(invcodes.toArray(new String[0])));
		}
	}

	/**
	 * 
	 * @���ߣ�yf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-8-6����11:25:42
	 * @param pk_corp ��˾
	 * @param srl_pk �ֿ�
	 * @param cargdoc ��λ
	 * @param list ���,������in�������鲻�ܳ���1000�������������Ҫ�޸�
	 * @return
	 * @throws DAOException
	 */
	private List getNotHasInv(String pk_corp, String srl_pk, String cargdoc,
			List<String> list) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select bas.invcode from bd_invbasdoc bas");
		sql.append(" where isnull(bas.dr,0) = 0 ");
		sql.append(" and bas.pk_invbasdoc in "
				+ WdsWlPubTool.getSubSql(list.toArray(new String[0])));
		sql.append(" and not exists( ");
		sql.append(" select b.pk_invbasdoc ");
		sql.append(" from wds_cargdoc1 h ");
		sql
				.append(" join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc and isnull(b.dr,0) = 0 ");

		sql.append(" where isnull(h.dr,0) = 0 ");
		sql.append(" and h.pk_corp = '" + pk_corp + "' ");
		sql.append(" and h.pk_stordoc = '" + srl_pk + "' ");
		sql.append(" and h.pk_cargdoc = '" + cargdoc + "' ");
		sql.append(" and b.pk_invbasdoc = bas.pk_invbasdoc");
		sql.append(" ) ");
		Object o = getDao().executeQuery(sql.toString(),
				WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if (o != null) {
			return (List) o;
		}
		return null;
	}

	/**
	 * 
	 * @���ߣ�yf
	 * @˵�������ɽ������Ŀ У���ͷ����λ�Ƿ�󶨴��
	 * @ʱ�䣺2012-7-17����09:30:07
	 * @param bvos
	 * @param hvo
	 * @throws BusinessException
	 */
	private void checkHeadCargdoc2Inv(TransferBVO[] bvos, TransferVO hvo)
			throws BusinessException {
		String pk_cargdoc = hvo.getPk_cargdoc2();
		int length = bvos.length;
		String[] invids = new String[length];
		StringBuffer sql = new StringBuffer();
		List<String> invcodes = null;
		for (int i = 0; i < length; i++) {
			invids[i] = bvos[i].getCinvbasid();
		}
		sql.append(" select bas.invcode from bd_invbasdoc bas");
		sql.append(" where isnull(bas.dr,0) = 0 ");
		sql
				.append(" and bas.pk_invbasdoc in "
						+ WdsWlPubTool.getSubSql(invids));
		sql.append(" and not exists( ");
		sql.append(" select b.pk_invbasdoc ");
		sql.append(" from wds_cargdoc1 h ");
		sql
				.append(" join tb_spacegoods b on h.pk_wds_cargdoc = b.pk_wds_cargdoc and isnull(b.dr,0) = 0 ");

		sql.append(" where isnull(h.dr,0) = 0 ");
		sql.append(" and h.pk_corp = '" + hvo.getPk_corp() + "' ");
		sql.append(" and h.pk_stordoc = '" + hvo.getSrl_pk() + "' ");
		sql.append(" and h.pk_cargdoc = '" + pk_cargdoc + "' ");
		sql.append(" and b.pk_invbasdoc = bas.pk_invbasdoc");
		sql.append(" ) ");

		Object o = getDao().executeQuery(sql.toString(),
				WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if (o != null) {
			invcodes = (List<String>) o;
			if (invcodes.size() > 0) {
				throw new BusinessException("���´��������λû�а�:\n"
						+ WdsWlPubTool.getSubSql2(invcodes
								.toArray(new String[0])));
			}
		}
	}
}
