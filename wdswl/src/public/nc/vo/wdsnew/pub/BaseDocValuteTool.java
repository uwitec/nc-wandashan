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
 * 基本档案的数据校验
 * 
 * 如货架信息 如果已经有业务数据 不允许删除操作 如果已经有业务数据 货架绑定的存货 和 货架容量不允许修改 或者 删除
 * 
 * @author mlr
 */
public class BaseDocValuteTool {
	private BaseDAO dao = new BaseDAO();

	/**
	 * 校验存货分类 节点 删除时是否有业务数据存在
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteInvclDocDelete(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String invclid = (String) vo.getPrimaryKey();// 存货分类id
		String invclcode = (String) vo.getAttributeValue("vinvclcode");// 存货分类编码
		valuteInvclDocDel(invclid, invclcode);
	}

	/**
	 * 校验存货档案 节点 删除时是否有业务数据存在
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteBaseDocDelete(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String id = (String) vo.getAttributeValue("pk_invmandoc");// 获取存货id
		String pk_invbasdoc = (String) vo.getAttributeValue("pk_invbasdoc");
		String invcode = (String) ZmPubTool
				.execFomular(
						" invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
						new String[] { "pk_invbasdoc" },
						new String[] { pk_invbasdoc });
		valuteBaseDocDel(id, invcode);
	}

	/**
	 * 校验存货档案 节点 删除时是否有业务数据存在
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:37:40
	 * @param billvo
	 * @throws Exception
	 */
	public void valuteBaseDocEdit(AggregatedValueObject billvo)
			throws Exception {
		if (billvo == null || billvo.getParentVO() == null)
			return;
		SuperVO vos = (SuperVO) billvo.getParentVO();
		SuperVO vo = vos;
		String id = (String) vo.getAttributeValue("pk_invmandoc");// 获取存货id
		String pk_invbasdoc = (String) vo.getAttributeValue("pk_invbasdoc");
		String invcode = (String) ZmPubTool
				.execFomular(
						" invcode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
						new String[] { "pk_invbasdoc" },
						new String[] { pk_invbasdoc });
		// 查看 托盘绑定的存货 和 托盘容量 有没有被修改
		boolean b = BsUniqueCheck.valueFields(vo,
				new String[] { "tray_volume" }, null);
		if (b == true) {
			valuteBaseDocDel(id, invcode);
		}
	}

	/**
	 * 校验货位货架信息 节点 删除时是否有业务数据存在
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:37:40
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
			String invclpk = vo.getPrimaryKey();// 获取托盘id
			String invclcode = (String) vo.getAttributeValue("cdt_traycode");
			valuteCdtDel(invclpk, invclcode);
		}
	}

	/**
	 * 校验存货分类删除出时 是否已经被存货档案引用
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteInvclDocDel(String invclpk, String invclcode)
			throws Exception {
		// 是否存在现存量
		if (isExistInvBaseDoc(invclpk)) {
			throw new Exception("  存货分类为：" + invclcode + " 的记录  已经被存货档案引用");

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
	 * 校验存货删除出时 是否存在业务数据
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteBaseDocDel(String pk_invmandoc, String invcode)
			throws Exception {
		// 是否存在现存量
		if (isExistInvHand1(pk_invmandoc)) {
			throw new Exception("  存货编码为：" + invcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改 托盘容量");
		}
		// 是否存在出库单
		if (isExistOut1(pk_invmandoc)) {
			throw new Exception("  存货编码为：" + invcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改  托盘容量");
		}
		// 是否存在入库单
		if (isExistIn1(pk_invmandoc)) {
			throw new Exception("  存货编码为：" + invcode
					+ " 的记录 已经存在业务数据 不能删除  或 修改  托盘容量");
		}
		// 是否存在货位调整单
		if (isExistHwtz1(pk_invmandoc)) {
			throw new Exception("  存货编码为：" + invcode
					+ " 的记录 已经存在业务数据 不能删除  或 修改   托盘容量");
		}
	}

	/**
	 * 校验托盘删除出时 是否存在业务数据
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午02:06:13
	 * @param cdtpk
	 * @param cdtcode
	 * @throws Exception
	 * @throws DAOException
	 */
	private void valuteCdtDel(String cdtpk, String cdtcode) throws Exception {
		// 是否存在现存量
		if (isExistInvHand(cdtpk)) {
			throw new Exception(" 托盘编码为：" + cdtcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改 托盘绑定的存货信息  和 托盘容量,请查看是否存在现存量");
		}
		// 是否存在出库单
		if (isExistOut(cdtpk)) {
			throw new Exception(" 托盘编码为：" + cdtcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改 托盘绑定的存货信息  和 托盘容量,请查看是否存在出库单");
		}
		// 是否存在入库单
		if (isExistIn(cdtpk)) {
			throw new Exception(" 托盘编码为：" + cdtcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改 托盘绑定的存货信息  和 托盘容量,请查看是否存在入库单");
		}
		// 是否存在货位调整单
		if (isExistHwtz(cdtpk)) {
			throw new Exception(" 托盘编码为：" + cdtcode
					+ " 的记录 已经存在业务数据 不能删除 或 修改 托盘绑定的存货信息  和 托盘容量,请查看是否存在货位调整单");
		}
	}

	/**
	 * 校验货位货架信息 节点 删除时是否有业务数据存在
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:37:40
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
				// 查看是否已经存在业务数据
				String cdtpk = vo.getPrimaryKey();// 获取托盘id
				String cdtcode = (String) vo.getAttributeValue("cdt_traycode");
				valuteCdtDel(cdtpk, cdtcode);
			} else if (vo.getStatus() == VOStatus.UPDATED) {
				// 查看 托盘绑定的存货 和 托盘容量 有没有被修改
				boolean b = BsUniqueCheck.valueFields(vo, new String[] {
						"cdt_invmandoc", "nsize" }, null);
				// 查看 是否已经存在业务数据
				String cdtpk = vo.getPrimaryKey();// 获取托盘id
				String cdtcode = (String) vo.getAttributeValue("cdt_traycode");
				if (b == true) {
					valuteCdtDel(cdtpk, cdtcode);
				}
			}
		}
	}

	/**
	 * 是否存在货位调整单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:56:42
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
	 * 是否存在货位调整单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:56:42
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
	 * 是否存在入库单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:56:58
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
	 * 是否存在入库单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:56:58
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
	 * 是否存在现存量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:57:13
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
	 * 是否存在现存量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:57:13
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
	 * 是否存出库单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:57:25
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
	 * 是否存出库单
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-9-22下午01:57:25
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
