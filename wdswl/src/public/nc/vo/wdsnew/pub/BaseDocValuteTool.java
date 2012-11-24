package nc.vo.wdsnew.pub;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.tool.ZmPubTool;

/**
 * ��������������У��
 * 
 * �������Ϣ ����Ѿ���ҵ������ ������ɾ������ ����Ѿ���ҵ������ ���ܰ󶨵Ĵ�� �� ���������������޸� ���� ɾ��
 * 
 * @author mlr
 */
public class BaseDocValuteTool {
	private BaseDAO dao = new BaseDAO();

	/**
	 * У�������� �ڵ� ɾ��ʱ�Ƿ���ҵ�����ݴ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteInvclDocDelete(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String invclid = (String) vo.getPrimaryKey();// �������id
		String invclcode = (String) vo.getAttributeValue("vinvclcode");// ����������
		valuteInvclDocDel(invclid, invclcode);
	}

	/**
	 * У�������� �ڵ� ɾ��ʱ�Ƿ���ҵ�����ݴ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteBaseDocDelete(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String id = (String) vo.getAttributeValue("pk_invmandoc");// ��ȡ���id
		String pk_invbasdoc = (String) vo.getAttributeValue("pk_invbasdoc");
		String invcode = (String) ZmPubTool
				.execFomular(
						" invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
						new String[] { "pk_invbasdoc" },
						new String[] { pk_invbasdoc });
		valuteBaseDocDel(id, invcode);
	}

	/**
	 * У�������� �ڵ� ɾ��ʱ�Ƿ���ҵ�����ݴ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteBaseDocEdit(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String id = (String) vo.getAttributeValue("pk_invmandoc");// ��ȡ���id
		String pk_invbasdoc = (String) vo.getAttributeValue("pk_invbasdoc");
		String invcode = (String) ZmPubTool
				.execFomular(
						" invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
						new String[] { "pk_invbasdoc" },
						new String[] { pk_invbasdoc });
		// �鿴 ���̰󶨵Ĵ�� �� �������� ��û�б��޸�
		boolean b = BsUniqueCheck.valueFields(vo,
				new String[] { "tray_volume" }, null);
		if (b == true) {
			valuteBaseDocDel(id, invcode);
		}
	}

	/**
	 * У���λ������Ϣ �ڵ� ɾ��ʱ�Ƿ���ҵ�����ݴ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteCdtDelete(AggregatedValueObject billvo) throws Exception {
		if (billvo == null || billvo.getChildrenVO() == null
				|| billvo.getChildrenVO().length == 0)
			return;
		SuperVO[] vos = (SuperVO[]) billvo.getChildrenVO();
		for (int i = 0; i < vos.length; i++) {
			SuperVO vo = vos[i];
			String invclpk = vo.getPrimaryKey();// ��ȡ����id
			String invclcode = (String) vo.getAttributeValue("cdt_traycode");
			valuteCdtDel(invclpk, invclcode);
		}
	}

	/**
	 * У��������ɾ����ʱ �Ƿ��Ѿ��������������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteInvclDocDel(String invclpk, String invclcode)
			throws Exception {
		// �Ƿ�����ִ���
		if (isExistInvBaseDoc(invclpk)) {
			throw new Exception("  �������Ϊ��" + invclcode + " �ļ�¼  �Ѿ��������������");

		}
	}

	private boolean isExistInvBaseDoc(String invclpk) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(0) from wds_invbasdoc  h where  h.vdef1 = '"
				+ invclpk + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * У����ɾ����ʱ �Ƿ����ҵ������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteBaseDocDel(String pk_invmandoc, String invcode)
			throws Exception {
		// �Ƿ�����ִ���
		if (isExistInvHand1(pk_invmandoc)) {
			throw new Exception("  �������Ϊ��" + invcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸� ��������");
		}
		// �Ƿ���ڳ��ⵥ
		if (isExistOut1(pk_invmandoc)) {
			throw new Exception("  �������Ϊ��" + invcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸�  ��������");
		}
		// �Ƿ������ⵥ
		if (isExistIn1(pk_invmandoc)) {
			throw new Exception("  �������Ϊ��" + invcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ��  �� �޸�  ��������");
		}
		// �Ƿ���ڻ�λ������
		if (isExistHwtz1(pk_invmandoc)) {
			throw new Exception("  �������Ϊ��" + invcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ��  �� �޸�   ��������");
		}
	}

	/**
	 * У������ɾ����ʱ �Ƿ����ҵ������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteCdtDel(String cdtpk, String cdtcode) throws Exception {
		// �Ƿ�����ִ���
		if (isExistInvHand(cdtpk)) {
			throw new Exception(" ���̱���Ϊ��" + cdtcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸� ���̰󶨵Ĵ����Ϣ  �� ��������,��鿴�Ƿ�����ִ���");
		}
		// �Ƿ���ڳ��ⵥ
		if (isExistOut(cdtpk)) {
			throw new Exception(" ���̱���Ϊ��" + cdtcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸� ���̰󶨵Ĵ����Ϣ  �� ��������,��鿴�Ƿ���ڳ��ⵥ");
		}
		// �Ƿ������ⵥ
		if (isExistIn(cdtpk)) {
			throw new Exception(" ���̱���Ϊ��" + cdtcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸� ���̰󶨵Ĵ����Ϣ  �� ��������,��鿴�Ƿ������ⵥ");
		}
		// �Ƿ���ڻ�λ������
		if (isExistHwtz(cdtpk)) {
			throw new Exception(" ���̱���Ϊ��" + cdtcode
					+ " �ļ�¼ �Ѿ�����ҵ������ ����ɾ�� �� �޸� ���̰󶨵Ĵ����Ϣ  �� ��������,��鿴�Ƿ���ڻ�λ������");
		}
	}

	/**
	 * У���λ������Ϣ �ڵ� ɾ��ʱ�Ƿ���ҵ�����ݴ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteCdtEdit(AggregatedValueObject billvo) throws Exception {
		if (billvo == null || billvo.getChildrenVO() == null
				|| billvo.getChildrenVO().length == 0)
			return;
		SuperVO[] vos = (SuperVO[]) billvo.getChildrenVO();
		for (int i = 0; i < vos.length; i++) {
			SuperVO vo = vos[i];
			if (vo.getStatus() == VOStatus.DELETED) {
				// �鿴�Ƿ��Ѿ�����ҵ������
				String cdtpk = vo.getPrimaryKey();// ��ȡ����id
				String cdtcode = (String) vo.getAttributeValue("cdt_traycode");
				valuteCdtDel(cdtpk, cdtcode);
			} else if (vo.getStatus() == VOStatus.UPDATED) {
				// �鿴 ���̰󶨵Ĵ�� �� �������� ��û�б��޸�
				boolean b = BsUniqueCheck.valueFields(vo, new String[] {
						"cdt_invmandoc", "nsize" }, null);
				// �鿴 �Ƿ��Ѿ�����ҵ������
				String cdtpk = vo.getPrimaryKey();// ��ȡ����id
				String cdtcode = (String) vo.getAttributeValue("cdt_traycode");
				if (b == true) {
					valuteCdtDel(cdtpk, cdtcode);
				}
			}
		}
	}

	/**
	 * �Ƿ���ڻ�λ������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:56:42
	 * @param cdtpk
	 * @return
	 * @throws Exception
	 */
	public boolean isExistHwtz(String cdtpk) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from wds_transfer_b  h where  h.vuserdef8 = '"
						+ cdtpk + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ���ڻ�λ������
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:56:42
	 * @param cdtpk
	 * @return
	 * @throws Exception
	 */
	public boolean isExistHwtz1(String pk_invmandoc) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from wds_transfer_b  h where  h.cinventoryid = '"
						+ pk_invmandoc + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ������ⵥ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:56:58
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistIn(String cdtpk) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_general_b  h where  h.geb_customize4 = '"
						+ cdtpk + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ������ⵥ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:56:58
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistIn1(String pk_invmandoc) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_general_b  h where  h.geb_cinventoryid = '"
						+ pk_invmandoc + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ�����ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:57:13
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistInvHand(String cdtpk) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_warehousestock  h where  h.pplpt_pk = '"
						+ cdtpk + "' and isnull(h.dr,0)=0  and  nvl(h.whs_stockpieces,0)>0 and nvl(h.whs_stocktonnage,0)>0");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ�����ִ���
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:57:13
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistInvHand1(String pk_invmandoc) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_warehousestock  h where  h.pk_invmandoc = '"
						+ pk_invmandoc + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ����ⵥ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:57:25
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistOut(String cdtpk) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_outgeneral_b  h where  h.vuserdef8 = '"
						+ cdtpk + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �Ƿ����ⵥ
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2012-9-22����01:57:25
	 * @param cdtpk
	 * @return
	 * @throws DAOException
	 */
	public boolean isExistOut1(String pk_invmandoc) throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select count(0) from tb_outgeneral_b  h where  h.cinventoryid = '"
						+ pk_invmandoc + "' and isnull(h.dr,0)=0 ");
		Integer count = PuPubVO.getInteger_NullAs(dao.executeQuery(sql
				.toString(), new ColumnProcessor()), 0);
		if (count > 0) {
			return true;
		}
		return false;
	}

}
