package nc.impl.uap.bd.inv.assign;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nc.bs.bd.cache.BDDelLog;
import nc.bs.bd.service.BDOperateServ;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.uap.lock.PKLock;
import nc.impl.uap.bd.produce.ProduceDAO;
import nc.itf.uap.bd.calbody.ICalbodyQry;
import nc.itf.uap.bd.inv.IInventoryAssign;
import nc.itf.uap.bd.inv.IinvBasManDocService;
import nc.itf.uap.bd.loginfo.IBDOperateConst;
import nc.itf.uap.bd.refcheck.IReferenceCheck;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.vo.bd.BDMsg;
import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.bd.b15.CorpInventoryMapping;
import nc.vo.bd.b15.CorpInventoryVO;
import nc.vo.bd.b431.ProduceMapping;
import nc.vo.bd.b431.ProduceVO;
import nc.vo.bd.invdoc.BasManUnionVO;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.invdoc.InvbindleVO;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.bd.invdoc.InvreplVO;
import nc.vo.bd.loginfo.BDLogInfoUtil;
import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.bd.service.IBDOperate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.assign.AssignQueryContext;
import nc.vo.pub.assign.IAssignStatus;
import nc.vo.pub.filemanage.BDAssociateFileUtil;
import nc.vo.pub.filemanage.BDFilePathVO;
import nc.vo.pub.filemanage.IBDFileManageConst;
import nc.vo.pub.general.GeneralExVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;
import nc.vo.trade.voutils.TimeCounter;
import nc.vo.uap.pf.PFBusinessException;

public class InventoryAssignImpl implements IInventoryAssign {
	// 分配加锁
	private final String invmandocAssign = "invmandocAssign";
	private final String invmandocCancelassign = "invmandocCancelassign";
	private final String produceAssign = "produceAssign";
	private final String produceCancelassign = "produceCancelassign";

	private int batchSize = 5000;

	// 公司目录的基本档案缓存
	private IBDAccessor corpBDAccessor;

	// 库存组织的基本档案缓存
	private IBDAccessor calbodyBDAccessor;

	// 模板默认数据加载器
	private TempletValueLoader templetValueLoader;
	private BaseDAO baseDAO;
	private IReferenceCheck referenceCheck;
	private ICalbodyQry calbodyQry;
	private ProduceDAO produceDAO;

	public List<String> queryAllInvbasdocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException {
		String condition = nodeQueryContext.getContext().getQueryCondition();
		String[] targets = nodeQueryContext.getContext().getTargetPks();
		String status = nodeQueryContext.getContext().getAssignStatus();
		return queryAllInvbasdocPksByCorpAndCalbody(status, targets, condition);
	}

	/**
	 * 根据分配状态、条件、目标组织（公司与库存组织）查询存货基本档案主键
	 * @param status
	 * @param targets
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<String> queryAllInvbasdocPksByCorpAndCalbody (final String status, String[] targets, final String condition)
			throws BusinessException {
		
		Set<String> calbodyPkSet = new HashSet<String>();
		Set<String> corpPkSet = new HashSet<String>();
		
		// 将库存组织按公司分类
		Map<String, List<String>> corp_calbodys_map = new HashMap<String, List<String>>();
		getTargetCorpPks(targets, corp_calbodys_map);
		
		for (int i = 0; i < targets.length; i++) {
			if (targets[i].length()==4) {
				corpPkSet.add(targets[i]);
				// 过滤的目标组织中存在的公司的库存组织数据（包含所属公司时，查询库存组织无意义，因为结果数据包含在其所属公司的数据之中）
				corp_calbodys_map.remove(targets[i]);
			}
		}
		
		// 获得不包含上级公司的库存组织
		for (List<String> calbodyPkList : corp_calbodys_map.values()) {
			calbodyPkSet.addAll(calbodyPkList);
		}
		
		List<String> pkList = null;

		if (IAssignStatus.STATUS_NULL.equals(status)) {
			String sql = "select pk_invbasdoc from bd_invbasdoc where pk_corp = '0001' and " + condition
					+ " order by invcode";
			pkList = (List<String>) getBaseDao().executeQuery(sql, new ColumnListProcessor());
		} else {
			TreeSet<InvSort> result = null;
			// 查询公司数据
			boolean isAssign = false;
			if (IAssignStatus.STATUS_ASSINGED.equals(status)){
				isAssign = true;
			}
			
			// 查询公司数据
			if (corpPkSet != null && corpPkSet.size() > 0) {				
				result = queryAllInvbasdocPkAndCodesByCorps(isAssign, corpPkSet.toArray(new String[0]), condition);
			}	
			
			// 查询库存组织数据（现有界面选择状态，当选中库存组织时，必包含上级公司，故查询库存组织数据无意义--设置下述语句不执行，默认条件为false）
			if (calbodyPkSet != null && calbodyPkSet.size() > 0) {	
				TreeSet<InvSort> calbodyResult = queryAllInvbasdocPkAndCodesByCalbodys(isAssign, calbodyPkSet.toArray(new String[0]), condition);
				if (result == null) {
					result = new TreeSet<InvSort>(new InvCodeComparator());
					result.addAll(calbodyResult);
				} else if (isAssign) {
					// 分配数据取并集
					result.addAll(calbodyResult);
				} else if (!isAssign) {
					// 未分配数据取交集
					result.retainAll(calbodyResult);
				}
			}
			
			pkList = new ArrayList();
			if (result != null && result.size() > 0) {
				for (InvSort invCode : result) {
					pkList.add(invCode.getPk_invbasdoc());
				}
			}
		}
		return pkList;
	}
	
	/**
	 * 根据分配状态、条件、目标公司查询存货基本档案主键
	 * 
	 * @param status
	 * @param targets
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<String> queryAllInvbasdocPks(final String status, String[] targets, final String condition)
			throws BusinessException {
		
		List<String> pkList = null;

		if (IAssignStatus.STATUS_NULL.equals(status)) {
			String sql = "select pk_invbasdoc from bd_invbasdoc where pk_corp = '0001' and " + condition
					+ " order by invcode";
			pkList = (List<String>) getBaseDao().executeQuery(sql, new ColumnListProcessor());
		} else {
			boolean isAssign = false;
			if (IAssignStatus.STATUS_ASSINGED.equals(status)){
				isAssign = true;
			}
			
			TreeSet<InvSort> result = queryAllInvbasdocPkAndCodesByCorps(isAssign, targets, condition);

			pkList = new ArrayList();
			for (InvSort invCode : result) {
				pkList.add(invCode.getPk_invbasdoc());
			}
		}
		return pkList;
	}

	/**
	 * 根据是否分配、目标公司、查询条件查询存货基本档案主键与编码的排序树
	 * 
	 * @param isAssign
	 * @param pk_corps
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private TreeSet<InvSort> queryAllInvbasdocPkAndCodesByCorps(final boolean isAssign, String[] pk_corps, final String condition)
			throws BusinessException {
			TreeSet<InvSort> result = null;
			InSqlBatchCaller caller = new InSqlBatchCaller(pk_corps);
			try {
				result = (TreeSet<InvSort>) caller.execute(new IInSqlBatchCallBack() {
					TreeSet<InvSort> resultSet = null;

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						List<InvSort> list = (List<InvSort>) getBaseDao().executeQuery(getSql(inSql),
								new BeanListProcessor(InvSort.class));
						// STATUS_UNASSIGN状态第一次结果应为全部添加，此后为保留交集数据
						if (resultSet == null) {
							resultSet = new TreeSet<InvSort>(new InvCodeComparator());
							resultSet.addAll(list);

						} else if (isAssign) {
							resultSet.addAll(list);
						} else if (!isAssign) {
							resultSet.retainAll(list);
						}
						return resultSet;
					}

					private String getSql(String inSql) {
						StringBuffer sql = new StringBuffer(
								"select pk_invbasdoc, invcode from bd_invbasdoc where pk_corp = '0001' and (");
						sql.append(condition);
						sql.append(") and ");
						if (!isAssign)
							sql.append("not ");
						sql.append("exists (select pk_invbasdoc from bd_invmandoc where bd_invbasdoc.pk_invbasdoc=bd_invmandoc.pk_invbasdoc and pk_corp in ");
						sql.append(inSql);
						sql.append(")");
						return sql.toString();
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}

		
		return result;
	}
	
	/**
	 * 根据是否分配、目标库存组织、查询条件查询存货基本档案主键与编码的排序树
	 * 
	 * @param isAssign
	 * @param pk_calbodys
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private TreeSet<InvSort> queryAllInvbasdocPkAndCodesByCalbodys(final boolean isAssign, String[] pk_calbodys, final String condition)
			throws BusinessException {
			TreeSet<InvSort> result = null;
			InSqlBatchCaller caller = new InSqlBatchCaller(pk_calbodys);
			try {
				result = (TreeSet<InvSort>) caller.execute(new IInSqlBatchCallBack() {
					TreeSet<InvSort> resultSet = null;

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						List<InvSort> list = (List<InvSort>) getBaseDao().executeQuery(getSql(inSql),
								new BeanListProcessor(InvSort.class));
						// STATUS_UNASSIGN状态第一次结果应为全部添加，此后为保留交集数据
						if (resultSet == null) {
							resultSet = new TreeSet<InvSort>(new InvCodeComparator());
							resultSet.addAll(list);

						} else if (isAssign) {
							resultSet.addAll(list);
						} else if (!isAssign) {
							resultSet.retainAll(list);
						}
						return resultSet;
					}

					private String getSql(String inSql) {
						StringBuffer sql = new StringBuffer(
								"select pk_invbasdoc, invcode from bd_invbasdoc where pk_corp = '0001' and (");
						sql.append(condition);
						sql.append(") and ");
						if (!isAssign)
							sql.append("not ");
						sql.append("exists (select pk_invbasdoc from bd_produce where bd_invbasdoc.pk_invbasdoc=bd_produce.pk_invbasdoc and pk_calbody in ");
						sql.append(inSql);
						sql.append(")");
						return sql.toString();
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}

		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> queryInvbasdocByPkSet(Set<String> pkSet) throws BusinessException {
		List<Object> list = null;
		InSqlBatchCaller caller = new InSqlBatchCaller(pkSet.toArray(new String[0]));
		try {
			list = (List<Object>) caller.execute(new IInSqlBatchCallBack() {
				List<CircularlyAccessibleValueObject> pkList = new ArrayList<CircularlyAccessibleValueObject>();

				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					Collection<InvbasdocVO> c = getBaseDao().retrieveByClause(InvbasdocVO.class,
							"pk_invbasdoc in " + inSql);
					pkList.addAll(c);
					return pkList;
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	public ErrLogReturnValue assignInvbasdocByCondition(String assignstatus, String[] targetPks, String condition,
			Map<String, List<String>> defaultTemplateID_targetPk_map) throws BusinessException {
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.invmandocAssign);
		List<String> pkList = queryAllInvbasdocPks(assignstatus, targetPks, condition);
		
		return assignInvbasdocToCorpAndCalbody(targetPks, pkList, defaultTemplateID_targetPk_map);
	}

	public ErrLogReturnValue assignInvbasdocByPkList(String[] targetPks, List<String> pkList, Map<String, List<String>> defaultTemplateID_targetPk_map)
			throws BusinessException {
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.invmandocAssign);
		return assignInvbasdocToCorpAndCalbody(targetPks, pkList, defaultTemplateID_targetPk_map);
	}
	
	/**
	 * 根据公司及库存组织主键、存货基本档案主键、默认模板ID分配存货管理档案数据与物料生产档案数据
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID_targetPk_map
	 * @return
	 * @throws BusinessException
	 */
	private ErrLogReturnValue assignInvbasdocToCorpAndCalbody(String[] targetPks, List<String> pkList,
			Map<String, List<String>> defaultTemplateID_targetPk_map) throws BusinessException {

		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000034")/*不存在待分配存货基本档案!*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
		
		// 获得目标公司
		Map<String, List<String>> corp_calbodys_map = new HashMap<String, List<String>>();
		String[] corpPks = getTargetCorpPks(targetPks, corp_calbodys_map);

		// 记录异常日志的操作信息
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081206", "0001", operator, IBDOperateConst.ASSIGN);

		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		// 处理组织与模板
		if (defaultTemplateID_targetPk_map != null) {
			for (String templetID : defaultTemplateID_targetPk_map.keySet()) {
				List<String> orgPkList = defaultTemplateID_targetPk_map.get(templetID);
				if (orgPkList != null && orgPkList.size() > 0) {
					for (String orgPk : orgPkList) {
						pk_templetID_map.put(orgPk, templetID);
					}
				}
			}
		}

		// 分配存货基本档案到公司
		if (corpPks != null && corpPks.length > 0) {
			assignInvbasdocToCorp(corpPks, pkList, pk_templetID_map, logUtil);
		}

		// 依次分配存货管理档案到库存组织
		for (String pk_corp : corp_calbodys_map.keySet()) {
			List<String> invmandocPkList = queryInvmandocPkByInvbasdocPk(pkList, pk_corp);
			assignInvmandocToCalbody(corp_calbodys_map.get(pk_corp).toArray(new String[0]), invmandocPkList,
					pk_templetID_map, logUtil, pk_corp);
		}

		return logUtil.getErrLogReturnValue(null);
	}

	/**
	 * 根据目标组织主键，获得目标公司（包括目标库存组织所在公司）、目标库存组织的主键
	 * 
	 * @param targetPks
	 *            目标组织
	 * @param corp_calbodys_map
	 *            公司主键与该公司的目标库存组织的列表
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private String[] getTargetCorpPks(String[] targetPks, final Map<String, List<String>> corp_calbodys_map) throws BusinessException{
		Set<String> calbodyPkSet = new HashSet<String>();
		Set<String> corpPkSet = new HashSet<String>();
		for (int i = 0; i < targetPks.length; i++) {
			if (targetPks[i].length()==4) {
				corpPkSet.add(targetPks[i]);
			}else {
				calbodyPkSet.add(targetPks[i]);
			}
		}
		InSqlBatchCaller caller = new InSqlBatchCaller(calbodyPkSet.toArray(new String[0]));
		try {
			 caller.execute(new IInSqlBatchCallBack(){
				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					List<Object[]> list = (List<Object[]>)getBaseDao().executeQuery("select pk_calbody, pk_corp from bd_calbody where pk_calbody in "+inSql, new ArrayListProcessor());
					for (Object[] objs : list) {
						String pk_calbody = (String) objs[0];
						String pk_corp = (String) objs[1];
						List<String> calbodyPkList = corp_calbodys_map.get(pk_corp);
						if (calbodyPkList == null) {
							calbodyPkList = new ArrayList<String>();
							corp_calbodys_map.put(pk_corp, calbodyPkList);
						}
						calbodyPkList.add(pk_calbody);
					
					}
					return null;
				}
				
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		
		corpPkSet.addAll(corp_calbodys_map.keySet());
		
		return corpPkSet.toArray(new String[0]);
	}
	
	/**
	 * 根据存货基本档案主键与公司主键,查询已分配存货管理档案主键
	 * 
	 * @param PkSet
	 * @param pk_corp
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<String> queryInvmandocPkByInvbasdocPk(List<String> PkSet, final String pk_corp) throws BusinessException {
		Set<String> pkSet = new HashSet<String>();
		InSqlBatchCaller caller = new InSqlBatchCaller(PkSet.toArray(new String[0]));
		try {
			pkSet = (Set<String>) caller.execute(new IInSqlBatchCallBack() {
				Set<String> pkSet = new HashSet<String>();

				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					List<String> pkList = (List<String>) getBaseDao().executeQuery(getSql(inSql),
							new ColumnListProcessor());
					pkSet.addAll(pkList);
					return pkSet;
				}

				private String getSql(String inSql) {
					return "select pk_invmandoc from bd_invmandoc where pk_corp = '"
							+ pk_corp
							+ "' and exists (select pk_invbasdoc from bd_invbasdoc where bd_invbasdoc.pk_invbasdoc=bd_invmandoc.pk_invbasdoc and bd_invbasdoc.pk_invbasdoc in "
							+ inSql + ")";
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return new ArrayList<String>(pkSet);
	}
	
	/**
	 * 根据公司、存货基本档案主键、默认模板ID分配存货管理档案数据
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	private void assignInvbasdocToCorp(String[] targetPks, List<String> pkList, Map<String, String> pk_templetID_map, BDLogInfoUtil logUtil)
			throws BusinessException {
		
		InvmandocAssignedVOCreator voCreator = new InvmandocAssignedVOCreator();

		String corpCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000000")/*公司编码为：*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*存货基本档案的编码为：*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// 清空上次循环操作的数据
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// 设置begin=pkList.size()，在执行完本次循环后跳出
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeAssignedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			for (int i = 0; i < targetPks.length; i++) {
				String templetID = pk_templetID_map.get(targetPks[i]);
				GeneralExVO defaultData = getTempletValueLoader().getDefaultDataByTempletID(templetID, "10081208");
				voCreator.setTempletData(defaultData);
				
				// 获得待分配的存货管理档案
				List<CorpInventoryVO> voList = voCreator.getAssignInvmandocVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil);

				// 若待分配数据为null不执行数据插入操作
				if (voList != null && voList.size() > 0) {
					// 构造后台校验服务
					BDOperateServ opServer = new BDOperateServ();
					Map<CorpInventoryVO, String> errMsgMap = new HashMap<CorpInventoryVO, String>();
					// 调用事前校验接口
					for (CorpInventoryVO inventoryVO : voList) {
						try {
							opServer.beforeOperate("10081206", IBDOperate.BDOPERATION_ASSIGN, inventoryVO
									.getPk_invbasdoc(), inventoryVO.getPk_corp(), inventoryVO);
						} catch (BusinessException e) {
							errMsgMap.put(inventoryVO, e.getMessage());	
						}
					}
					if (errMsgMap.keySet()!= null && errMsgMap.keySet().size() >0) {
						BddataVO corpBDVO = getCorpBDAccessor().getDocByPk(targetPks[i]);
						// 记录错误日志及删除不能分配的数据
						for (CorpInventoryVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(corpCodeIs).append(corpBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invbasdoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// 记录日志信息
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();
					String[] keys = getBaseDao().insertObject(voList.toArray(new CorpInventoryVO[0]),
							new CorpInventoryMapping());
					 
					// 调用事后校验接口
					int j = 0;
					for (CorpInventoryVO inventoryVO : voList) {
						// 设置插入后主键
						inventoryVO.setPrimaryKey(keys[j++]);
						opServer.afterOperate("10081206", IBDOperate.BDOPERATION_ASSIGN, inventoryVO.getPk_invbasdoc(),
								inventoryVO.getPk_corp(), inventoryVO);
					}
				}
			}
		}
		nc.bs.bd.cache.CacheProxy.fireDataInserted("bd_invmandoc", null);

		
	}
	
	@SuppressWarnings("unchecked")
	public ErrLogReturnValue cancelInvbasdocAssignByCondition(String assignstatus, String[] targetPks, final String condition)
			throws BusinessException {

		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
		
		
		Set<String> corpPkSet = new HashSet<String>();
		for (int i = 0; i < targetPks.length; i++) {
			if (targetPks[i].length()==4) {
				corpPkSet.add(targetPks[i]);
			}
		}
		
		// 若目标组织中不存在公司主键
		if (corpPkSet.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000045")/*请选择公司进行取消分配！*/);
		}
		
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.invmandocCancelassign);

		List<String> invmandocPKList = null;
		InSqlBatchCaller caller = new InSqlBatchCaller(targetPks);
		try {
			invmandocPKList = (List<String>) caller.execute(new IInSqlBatchCallBack() {
				private List<String> list = new ArrayList<String>();

				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					String sql = "select pk_invmandoc from bd_invmandoc where pk_corp in" + inSql;
					sql += "and pk_invbasdoc in (select pk_invbasdoc from bd_invbasdoc where " + condition + ") order by pk_corp";
					List<String> pkList = (List<String>) getBaseDao().executeQuery(sql, new ColumnListProcessor());
					list.addAll(pkList);
					return list;
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return cancelInvbasdocAssign(invmandocPKList);
	}

	@SuppressWarnings("unchecked")
	public ErrLogReturnValue cancelInvbasdocAssignByPkList(String[] targetPks, List<String> pkList) throws BusinessException {
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
		
		
		Set<String> corpPkSet = new HashSet<String>();
		for (int i = 0; i < targetPks.length; i++) {
			if (targetPks[i].length()==4) {
				corpPkSet.add(targetPks[i]);
			}
		}
		
		// 若目标组织中不存在公司主键
		if (corpPkSet.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000045")/*请选择公司进行取消分配！*/);
		}
		
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.invmandocCancelassign);

		List<String> invmandocPKList = null;
		String sql = "select pk_invmandoc from bd_invmandoc where pk_corp in ";
		String condition = " and pk_invbasdoc in ";
		InSqlBatchCaller caller = new InSqlBatchCaller(targetPks);
		try {
			invmandocPKList = (List<String>) caller.execute(new AssignInSqlBatchCallBack(sql, condition, pkList
					.toArray(new String[0]), "pk_corp"));
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return cancelInvbasdocAssign(invmandocPKList);
	}

	/**
	 * 根据存货管理档案主键列表，删除存货管理档案的分配数据
	 * 
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private ErrLogReturnValue cancelInvbasdocAssign(List<String> pkList) throws BusinessException {
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000037")/*不存在待取消分配存货管理档案!*/);
		}
		
		String corpCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000000")/*公司编码为：*/;
		String corpPkIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000001")/*公司主键为：*/;
		String invmandocCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000002")/*存货管理档案的编码为：*/;
		String invmandocPkIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000003")/*存货管理档案主键为：*/;
		

		// 记录异常日志的操作信息
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081206", "0001", operator, IBDOperateConst.CANCELASSIGN);

		String refMsg = BDMsg.MSG_REF_NOT_DELETE();

		// 构造后台校验服务
		BDOperateServ opServer = new BDOperateServ();
		BDDelLog dellog = new BDDelLog();

		List<String> toBeDeletedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {
			// 清空上次循环操作的数据
			toBeDeletedPKs.clear();
			if (pkList.size() - begin < this.batchSize) {
				toBeDeletedPKs.addAll(pkList.subList(begin, pkList.size()));
				// 设置begin=pkList.size()，在执行完本次循环后跳出
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeDeletedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			// 查询待删除管理档案VO
			InSqlBatchCaller caller = new InSqlBatchCaller(toBeDeletedPKs.toArray(new String[0]));
			List<InvmandocVO> invmandocList = null;
			try {
				invmandocList = (ArrayList<InvmandocVO>) caller.execute(new IInSqlBatchCallBack() {
					List<InvmandocVO> al = new ArrayList<InvmandocVO>();

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						al.addAll(getBaseDao().retrieveByClause(InvmandocVO.class, "pk_invmandoc in " + inSql));
						return al;
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}

			// 查询后数据为空时，不进行下面操作
			if (invmandocList != null && invmandocList.size() > 0) {

				// 引用校验计时
				TimeCounter tc = new TimeCounter("检查引用");

				Map<String, InvmandocVO> pk_vo_map = new HashMap<String, InvmandocVO>();
				for (InvmandocVO invmandocVO : invmandocList) {
					pk_vo_map.put(invmandocVO.getPrimaryKey(), invmandocVO);
				}

				String[] referencedPks = isInvmandocReferenced(pk_vo_map);
				// 处理被引用主键
				if (referencedPks != null && referencedPks.length > 0) {
					for (int i = 0; i < referencedPks.length; i++) {

						// 获取管理档案
						InvmandocVO vo = pk_vo_map.get(referencedPks[i]);

						StringBuffer logmsg = new StringBuffer();
						// 设置公司编码
						BddataVO corpBDVO = getCorpBDAccessor().getDocByPk(vo.getPk_corp());
						if (corpBDVO != null && corpBDVO.getCode() != null && corpBDVO.getCode().trim().length() > 0) {
							logmsg.append(corpCodeIs).append(corpBDVO.getCode());
						} else {
							logmsg.append(corpPkIs).append(vo.getPk_corp());
						}
						// 设置存货档案编码
						IBDAccessor bdAccessor = AccessorManager.getAccessor(IBdinfoConst.INVMANDOC, vo.getPk_corp());
						BddataVO invmandocBDVO = bdAccessor.getDocByPk(referencedPks[i]);
						if (invmandocBDVO != null && invmandocBDVO.getCode() != null
								&& invmandocBDVO.getCode().trim().length() > 0) {
							logmsg.append(" ").append(invmandocCodeIs).append(invmandocBDVO.getCode());
						} else {
							logmsg.append(" ").append(invmandocPkIs).append(referencedPks[i]);
						}
						logmsg.append(" ").append(refMsg);
						// 记录日志信息
						logUtil.addLogMsgBatch(logmsg.toString());
						// 从待删除数据中去掉被引用的存货管理档案
						pk_vo_map.remove(referencedPks[i]);
					}
					// 写入异常日志
					logUtil.writeLogMsgBatch();

				}

				tc.close();

				List<String> invmandocPkList = new ArrayList<String>();
				// 若全部被引用则不执行取消分配操作
				if (pk_vo_map.size() != 0) {
					// 调用事前校验接口
					for (String pk_invmandoc : pk_vo_map.keySet()) {
						invmandocPkList.add(pk_invmandoc);
						InvmandocVO invmandocVO = pk_vo_map.get(pk_invmandoc);
						opServer.beforeOperate("10081206", IBDOperate.BDOPERATION_CANCELASSIGN, invmandocVO
								.getPrimaryKey(), invmandocVO.getPk_corp(), invmandocVO);
					}
					String[] pks = invmandocPkList.toArray(new String[0]);

					// 删除日志
					dellog.delPKs("bd_invmandoc", pks);

					// 删除存货管理档案
					getBaseDao().deleteByPKs(InvmandocVO.class, pks);
					// 删除替换件
					deleteInvrepl(pks);
					// 删除捆绑件
					deleteInvbindle(pks);
					// 删除关联文档
					deleteAssociateFile(pks, pk_vo_map);

					// 调用事后校验接口
					for (String pk_invmandoc : pk_vo_map.keySet()) {
						invmandocPkList.add(pk_invmandoc);
						InvmandocVO invmandocVO = pk_vo_map.get(pk_invmandoc);
						opServer.afterOperate("10081206", IBDOperate.BDOPERATION_CANCELASSIGN, invmandocVO
								.getPrimaryKey(), invmandocVO.getPk_corp(), invmandocVO);
					}
					// 缓存通知
					nc.bs.bd.cache.CacheProxy.fireDataDeletedBatch("bd_invmandoc", pks);

				}
			}
		}

		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * 删除管理档案关联的文档
	 * 
	 * @param pks
	 * @param pk_vo_map
	 * @throws BusinessException
	 */
	private void deleteAssociateFile(String[] pks, Map<String, InvmandocVO> pk_vo_map) throws BusinessException {
		
		List<BDFilePathVO> list = new ArrayList<BDFilePathVO>();
		for (int i = 0; i < pks.length; i++) {
			InvmandocVO vo = pk_vo_map.get(pks[i]);
			BDFilePathVO fileVO = new BDFilePathVO(vo.getPk_invbasdoc(),vo.getPk_corp());
			list.add(fileVO);
		}
		new BDAssociateFileUtil(IBDFileManageConst.INV_FILEMANAGE_PATH).deleteAssociateFiles(list);
	}
	
	/**
	 * 根据存货管理档案主键，删除替换件
	 * 
	 * @param pks
	 * @throws BusinessException
	 */
	private void deleteInvrepl(String[] pks) throws BusinessException {
		InSqlBatchCaller caller = new InSqlBatchCaller(pks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					getBaseDao().deleteByClause(InvreplVO.class, "pk_invmandoc in " + inSql);
					return null;
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 根据存货管理档案主键，删除捆绑件
	 * 
	 * @param pks
	 * @throws BusinessException
	 */
	private void deleteInvbindle(String[] pks) throws BusinessException {
		InSqlBatchCaller caller = new InSqlBatchCaller(pks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					getBaseDao().deleteByClause(InvbindleVO.class, "pk_invmandoc in " + inSql);
					return null;
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	public List<String> queryAllInvmandocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException {
		String condition = nodeQueryContext.getContext().getQueryCondition();
		String[] targets = nodeQueryContext.getContext().getTargetPks();
		String status = nodeQueryContext.getContext().getAssignStatus();
		return queryAllInvmandocPks(status, targets, condition);
	}

	/**
	 * 根据分配状态、条件、目标公司查询存货基本档案主键
	 * 
	 * @param status
	 * @param targets
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<String> queryAllInvmandocPks(final String status, String[] targets, final String condition)
			throws BusinessException {

		List<String> pkList = null;

		if (IAssignStatus.STATUS_NULL.equals(status)) {
			String sql = "select bd_invmandoc.pk_invmandoc from bd_invmandoc left join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc where bd_invmandoc.pk_corp = '"
					+ InvocationInfoProxy.getInstance().getCorpCode()
					+ "' and "
					+ condition
					+ " order by bd_invbasdoc.invcode";
			pkList = (List<String>) getBaseDao().executeQuery(sql, new ColumnListProcessor());
		} else {
			TreeSet<InvSort> result = null;
			InSqlBatchCaller caller = new InSqlBatchCaller(targets);
			try {
				result = (TreeSet<InvSort>) caller.execute(new IInSqlBatchCallBack() {
					TreeSet<InvSort> resultSet = null;

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						List<InvSort> list = (List<InvSort>) getBaseDao().executeQuery(getSql(inSql),
								new BeanListProcessor(InvSort.class));
						// STATUS_UNASSIGN状态第一次结果应为全部添加，此后为保留交集数据
						if (resultSet == null) {
							resultSet = new TreeSet<InvSort>(new InvCodeComparator());
							resultSet.addAll(list);

						} else if (status.equals(IAssignStatus.STATUS_ASSINGED)) {
							resultSet.addAll(list);
						} else if (status.equals(IAssignStatus.STATUS_UNASSIGN)) {
							resultSet.retainAll(list);
						}
						return resultSet;
					}

					private String getSql(String inSql) {
						StringBuffer sql = new StringBuffer(
								"select bd_invmandoc.pk_invmandoc, bd_invbasdoc.invcode from bd_invmandoc left join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc where bd_invmandoc.pk_corp = '");
						sql.append(InvocationInfoProxy.getInstance().getCorpCode());
						sql.append("' and (");
						sql.append(condition);
						sql.append(") and ");
						if (IAssignStatus.STATUS_UNASSIGN.equals(status))
							sql.append("not ");
						sql.append("exists (select pk_invmandoc from bd_produce where bd_invmandoc.pk_invmandoc = bd_produce.pk_invmandoc and pk_calbody in ");
						sql.append(inSql);
						sql.append(")");
						return sql.toString();
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}
			pkList = new ArrayList();
			for (InvSort invCode : result) {
				pkList.add(invCode.getPk_invmandoc());
			}
		}
		return pkList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> queryBasManUnionVOByPkSet(Set<String> pkSet) throws BusinessException {
		IinvBasManDocService iinvBasManDocService = (IinvBasManDocService) NCLocator.getInstance().lookup(
				IinvBasManDocService.class.getName());
		
		List<BasManUnionVO> list = iinvBasManDocService.queryInvManDocByPk_invmandoc(new ArrayList<String>(pkSet), null, null);
		return new ArrayList<Object>(list);
	}

	public ErrLogReturnValue assignInvmandocByCondition(String assignstatus, String[] targetPks, String condition,
			String defaultTemplateID) throws BusinessException {

		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.produceAssign);

		List<String> pkList = queryAllInvmandocPks(assignstatus, targetPks, condition);
		
		return assignInvmandoc(targetPks, pkList, defaultTemplateID);
	}

	public ErrLogReturnValue assignInvmandocByPkList(String[] targetPks, List<String> pkList, String defaultTemplateID)
			throws BusinessException {

		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.produceAssign);
		
		return assignInvmandoc(targetPks, pkList, defaultTemplateID);
	}
	
	private ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID) throws BusinessException{
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000141")/*不存在待分配存货管理档案！*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
		
		// 记录异常日志的操作信息
		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.ASSIGN);
		
		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		for (int i = 0; i < targetPks.length; i++) {
			pk_templetID_map.put(targetPks[i], defaultTemplateID);
		}
		
		// 分配数据
		assignInvmandocToCalbody(targetPks, pkList, pk_templetID_map, logUtil, pk_corp);
		
		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * 根据库存组织、存货管理档案主键、默认模板ID分配物料生产档案数据
	 * 
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	private void assignInvmandocToCalbody(String[] targetPks, List<String> pkList, Map<String, String> pk_templetID_map, BDLogInfoUtil logUtil, String pk_corp)
			throws BusinessException {
		
		ProduceAssignedVOCreator voCreator = new ProduceAssignedVOCreator();
		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*库存组织编码为：*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*存货基本档案的编码为：*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// 清空上次循环操作的数据
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// 设置begin=pkList.size()，在执行完本次循环后跳出
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeAssignedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			for (int i = 0; i < targetPks.length; i++) {
				
				// 设置分配模板数据
				GeneralExVO defaultData = getTempletValueLoader().getDefaultDataByTempletID(pk_templetID_map.get(targetPks[i]), "10081210");
				voCreator.setTempletData(defaultData);

				// 获得待分配的存货管理档案
				List<ProduceVO> voList = voCreator.getAssignBasManUnionVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil, pk_corp); 
				
				//若待分配数据为null不执行数据插入操作
				if (voList != null && voList.size()>0) {

					// 构造校验服务
					BDOperateServ opServer = new BDOperateServ();
					Map<ProduceVO, String> errMsgMap = new HashMap<ProduceVO, String>();
					// 调用事前校验
					for (ProduceVO produceVO : voList) {
						try {
						opServer.beforeOperate("10081208", IBDOperate.BDOPERATION_ASSIGN, produceVO.getPk_invmandoc(),
								produceVO.getPk_calbody(), produceVO);
						} catch (BusinessException e) {
							errMsgMap.put(produceVO, e.getMessage());	
						}
					}
					if (errMsgMap.keySet()!= null && errMsgMap.keySet().size() >0) {
						BddataVO calbodyBDVO = getCalbodyBDAccessor().getDocByPk(targetPks[i]);
						// 记录错误日志及删除不能分配的数据
						for (ProduceVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invmandoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// 记录日志信息
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();

					String[] keys = getProduceDAO().insertArray(voList.toArray(new ProduceVO[0]));

					// 调用事后校验
					int j = 0;
					for (ProduceVO produceVO : voList) {
						produceVO.setPrimaryKey(keys[j++]);
						opServer.afterOperate("10081208", IBDOperate.BDOPERATION_ASSIGN, produceVO.getPk_invmandoc(),
								produceVO.getPk_calbody(), produceVO);
						
					}

				}
			}
		}
		nc.bs.bd.cache.CacheProxy.fireDataInserted("bd_produce", null);

	}

	@SuppressWarnings("unchecked")
	public ErrLogReturnValue cancelInvmandocAssignByCondition(String assignstatus, String[] targetPks, final String condition)
			throws BusinessException {
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
				
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.produceCancelassign);

		List<String> producePKList = null;
		InSqlBatchCaller caller = new InSqlBatchCaller(targetPks);
		try {
			producePKList = (List<String>) caller.execute(new IInSqlBatchCallBack() {
				private List<String> list = new ArrayList<String>();

				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					String sql = "select pk_produce from bd_produce where pk_calbody in" + inSql;
					sql += "and pk_invmandoc in (select bd_invmandoc.pk_invmandoc from bd_invmandoc left join bd_invbasdoc on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc where "
							+ condition + ") order by pk_calbody";
					List<String> pkList = (List<String>) getBaseDao().executeQuery(sql, new ColumnListProcessor());
					list.addAll(pkList);
					return list;
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return cancelInvmandocAssign(producePKList);
	}

	@SuppressWarnings("unchecked")
	public ErrLogReturnValue cancelInvmandocAssignByPkList(String[] targetPks, List<String> pkList) throws BusinessException {

		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
				
		// 加锁（保证每次只有一个用户进行分配操作）
		addlock(this.produceCancelassign);

		List<String> producePKList = null;
		String sql = "select pk_produce from bd_produce where pk_calbody in ";
		String condition = " and pk_invmandoc in ";
		InSqlBatchCaller caller = new InSqlBatchCaller(targetPks);
		try {
			producePKList = (List<String>) caller.execute(new AssignInSqlBatchCallBack(sql, condition, pkList
					.toArray(new String[0]), "pk_calbody"));
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
		return cancelInvmandocAssign(producePKList);
	}

	/**
	 * 根据物料生产档案主键列表，取消物料生产档案的分配数据
	 * 
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private ErrLogReturnValue cancelInvmandocAssign(List<String> pkList) throws BusinessException {
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000142")/*不存在待取消分配物料生产档案!*/);
		}

		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*库存组织编码为：*/;
		String calbodyPkIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000133")/*库存组织主键为：*/;
		String ProduceCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000136")/*物料生产档案的编码为：*/;
		String ProducePkIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000135")/*物料生产档案主键为：*/;
		
		// 记录异常日志的操作信息
		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.CANCELASSIGN);

		String refMsg = BDMsg.MSG_REF_NOT_DELETE();

		// 构造后台校验服务
		BDOperateServ opServer = new BDOperateServ();
		BDDelLog dellog = new BDDelLog();

		List<String> toBeDeletedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {
			// 清空上次循环操作的数据
			toBeDeletedPKs.clear();
			if (pkList.size() - begin < this.batchSize) {
				toBeDeletedPKs.addAll(pkList.subList(begin, pkList.size()));
				// 设置begin=pkList.size()，在执行完本次循环后跳出
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeDeletedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			// 查询待删除物料生产档案VO
			InSqlBatchCaller caller = new InSqlBatchCaller(toBeDeletedPKs.toArray(new String[0]));
			List<ProduceVO> produceList = null;
			try {
				produceList = (ArrayList<ProduceVO>) caller.execute(new IInSqlBatchCallBack() {
					List<ProduceVO> al = new ArrayList<ProduceVO>();

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						al.addAll(getBaseDao().retrieveByClause(ProduceVO.class, new ProduceMapping(),
								"pk_produce in " + inSql));
						return al;
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}

			// 查询后数据为空时，不进行下面操作
			if (produceList != null && produceList.size() > 0) {

				// 引用校验计时
				TimeCounter tc = new TimeCounter("检查引用");

				Map<String, ProduceVO> pk_vo_map = new HashMap<String, ProduceVO>();
				for (ProduceVO produceVO : produceList) {
					pk_vo_map.put(produceVO.getPrimaryKey(), produceVO);
				}

				String[] referencedPks = isProduceReferenced(produceList);
				// 处理被引用主键
				if (referencedPks != null && referencedPks.length > 0) {
					for (int i = 0; i < referencedPks.length; i++) {

						// 获取物料生产档案档案
						ProduceVO vo = pk_vo_map.get(referencedPks[i]);

						StringBuffer logmsg = new StringBuffer();
						// 设置库存组织编码
						BddataVO calbodyBDVO = getCalbodyBDAccessor().getDocByPk(vo.getPk_calbody());
						if (calbodyBDVO != null && calbodyBDVO.getCode() != null
								&& calbodyBDVO.getCode().trim().length() > 0) {
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
						} else {
							logmsg.append(calbodyPkIs).append(vo.getPk_corp());
						}
						// 设置存货档案编码
						IBDAccessor bdAccessor = AccessorManager.getAccessor(IBdinfoConst.PRODUCE, vo.getPk_corp());
						BddataVO produceBDVO = bdAccessor.getDocByPk(referencedPks[i]);
						if (produceBDVO != null && produceBDVO.getCode() != null
								&& produceBDVO.getCode().trim().length() > 0) {
							logmsg.append(" ").append(ProduceCodeIs).append(produceBDVO.getCode());
						} else {
							logmsg.append(" ").append(ProducePkIs).append(referencedPks[i]);
						}
						logmsg.append(" ").append(refMsg);
						// 记录日志信息
						logUtil.addLogMsgBatch(logmsg.toString());
						// 从待删除数据中去掉被引用的物料生产档案
						pk_vo_map.remove(referencedPks[i]);
					}
					// 写入异常日志
					logUtil.writeLogMsgBatch();

				}

				tc.close();

				// 记录待删除档案
				List<String> producePkList = new ArrayList<String>();
				// 若全部被引用则不执行取消分配操作
				if (pk_vo_map.size() != 0) {
					// 调用事前校验接口
					for (String pk_produce : pk_vo_map.keySet()) {
						producePkList.add(pk_produce);
						ProduceVO produceVO = pk_vo_map.get(pk_produce);
						opServer.beforeOperate("10081208", IBDOperate.BDOPERATION_CANCELASSIGN, produceVO
								.getPrimaryKey(), produceVO.getPk_calbody(), produceVO);
					}
					String[] pks = producePkList.toArray(new String[0]);

					// 删除日志
					dellog.delPKs("bd_produce", pks);

					// 删除物料生产档案
					deleteProduceByPks(pks);
					// 删除固定批量列表
					deleteBatchlist(pks);

					// 调用事后校验接口
					for (String pk_produce : pk_vo_map.keySet()) {
						ProduceVO produceVO = pk_vo_map.get(pk_produce);
						opServer.afterOperate("10081208", IBDOperate.BDOPERATION_CANCELASSIGN, produceVO
								.getPrimaryKey(), produceVO.getPk_calbody(), produceVO);
					}
					// 缓存通知
					nc.bs.bd.cache.CacheProxy.fireDataDeletedBatch("bd_produce", pks);

				}
			}
		}
		return logUtil.getErrLogReturnValue(null);
	} 

	/**
	 * 刪除物料生产档案
	 * 
	 * @param pks
	 * @throws BusinessException
	 */
	@SuppressWarnings("unused")
	private void deleteProduceByPks(String[] pks) throws BusinessException {
		InSqlBatchCaller caller = new InSqlBatchCaller(pks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					return getBaseDao().deleteByClause(new ProduceMapping(), "pk_produce in " + inSql);
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 根据物料生产档案主键，删除固定批量列表
	 * 
	 * @param pks
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private void deleteBatchlist(String[] pks) throws BusinessException {
		InSqlBatchCaller caller = new InSqlBatchCaller(pks);
		try {
			caller.execute(new IInSqlBatchCallBack() {
				private String sql = "delete from bd_batchlist where pk_produce in ";

				public Object doWithInSql(String inSql) throws BusinessException, SQLException {
					return getBaseDao().executeUpdate(sql + inSql);
				}
			});
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 取消分配查询的批量处理
	 * 
	 * @author jiangjuna
	 * 
	 */
	private class AssignInSqlBatchCallBack implements IInSqlBatchCallBack {
		private List<String> list = null;
		private String sql = null;
		private String condition = null;
		private String[] pkList = null;
		private String wherePart = null;
		private String orderPart = null;

		/**
		 * 根据形如"select pk_invmandc from bd_invmandoc where pk_corp in (
		 * "外部调用的inSql语句" ) and pk_invbasdoc in （ "pkList的inSql语句" ）"; 其中 sql =
		 * "select pk_invmandc from bd_invmandoc where pk_corp in "; condition = "
		 * and pk_invbasdoc in "
		 * 
		 * @param sql
		 * @param condition
		 * @param pkList
		 */
		public AssignInSqlBatchCallBack(String sql, String condition, String[] pkList, String orderPart) {
			this.sql = sql;
			this.condition = condition;
			this.pkList = pkList;
			this.orderPart = "order by " + orderPart;
		}

		@SuppressWarnings("unchecked")
		public Object doWithInSql(String inSql) throws BusinessException, SQLException {
			InSqlBatchCaller caller = new InSqlBatchCaller(pkList);
			wherePart = sql + inSql;
			try {
				list = (List<String>) caller.execute(new IInSqlBatchCallBack() {
					List<String> pkList = new ArrayList<String>();

					public Object doWithInSql(String inSql) throws BusinessException, SQLException {
						List<String> l = (List<String>) getBaseDao().executeQuery(wherePart + condition + inSql + orderPart,
								new ColumnListProcessor());
						pkList.addAll(l);
						return pkList;
					}
				});
			} catch (SQLException e) {
				Logger.error(e.getMessage(), e);
				throw new BusinessException(e.getMessage());
			}
			return list;
		}

	}

	private BaseDAO getBaseDao() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
			baseDAO.setMaxRows(-1);
		}
		return baseDAO;
	}

	public TempletValueLoader getTempletValueLoader() {
		if (templetValueLoader == null) {
			templetValueLoader = new TempletValueLoader();
		}
		return templetValueLoader;
	}

	/**
	 * 引用校验
	 * 
	 * @param tableName
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private String[] isInvmandocReferenced(Map<String, InvmandocVO> pk_vo_map) throws BusinessException {
		Set<String> referencedPkSet = new HashSet<String>();
		
		// 管理档案引用校验
		String[] refPks = getReferenceCheck().getReferencedKeys("bd_invmandoc", pk_vo_map.keySet().toArray(new String[0]));
		if (refPks != null && refPks.length > 0) {
			referencedPkSet.addAll(Arrays.asList(refPks));
		}
		
		// 校验指定管理档案表中的基本档案主键字段在指定公司是否被引用。
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, String> pk_map = new HashMap<String, String>();
		// 原有是实现为按公司区分数据，故需在查询待取消分配主键时，尽量按公司排序
		for (InvmandocVO vo : pk_vo_map.values()) {
			String pk_corp = vo.getPk_corp();
			String pk_invbasdoc = vo.getPk_invbasdoc();
			// 过滤主键已被引用数据
			if (!referencedPkSet.contains(vo.getPrimaryKey())) {
				
				List<String> pkList = map.get(pk_corp);
				if (pkList == null) {
					pkList = new ArrayList<String>();
					map.put(pk_corp, pkList);
				}
				pkList.add(vo.getPk_invbasdoc());
			}
			pk_map.put(pk_corp +"_"+ pk_invbasdoc, vo.getPrimaryKey());
		}
		// 按公司批量查询引用数据，并将结果加入到被引用数据列表
		for (String pk_corp : map.keySet()) {
			Set<String> basPkSet = getReferenceCheck().getBasePkReferencedInCorp("bd_invmandoc", map.get(pk_corp), pk_corp);
			for (String pk_invbasddoc : basPkSet) {
				referencedPkSet.add(pk_map.get(pk_corp+"_"+pk_invbasddoc));
			}
		}
		
		if (referencedPkSet.size() > 0) {
			return referencedPkSet.toArray(new String[0]);
		}
		
		return null;
	}

	public IReferenceCheck getReferenceCheck() {
		if (referenceCheck == null) {
			referenceCheck = (IReferenceCheck) NCLocator.getInstance().lookup(IReferenceCheck.class.getName());
		}
		return referenceCheck;
	}
	
	public ProduceDAO getProduceDAO() {
		if (produceDAO == null) {
			produceDAO = new ProduceDAO();
		}
		return produceDAO;
	}
	
	/**
	 * 物料档案引用校验
	 * 
	 * @param produceList
	 * @return
	 * @throws BusinessException
	 */
	/**
	 * @param produceList
	 * @return
	 * @throws BusinessException
	 */
	private String[] isProduceReferenced(List<ProduceVO> produceList) throws BusinessException {
		// 记录被引用数组
		Set<String> referencedPkSet = new HashSet<String>();

		if (calbodyQry == null) {
			calbodyQry = (ICalbodyQry) NCLocator.getInstance().lookup(ICalbodyQry.class.getName());
		}

		ArrayList<String> pksList = new ArrayList<String>();
		for (ProduceVO produceVO : produceList) {
			// 存货核算是否引用
			UFBoolean isInvRef = calbodyQry.isContain(produceVO.getPk_corp(), produceVO.getPk_calbody(), produceVO
					.getPk_invmandoc());
			if (isInvRef.booleanValue()) {
				referencedPkSet.add(produceVO.getPrimaryKey());
				continue;
			}
			// 存货系统是否引用
			boolean isStoreRef;
			try {
				isStoreRef = getProduceDAO().isProduceRef(produceVO.getPk_calbody(), produceVO.getPk_invmandoc());
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				isStoreRef = false;
			}
			if (isStoreRef) {
				referencedPkSet.add(produceVO.getPrimaryKey());
				continue;
			}
			pksList.add(produceVO.getPrimaryKey());
		}

		String[] refPks = getReferenceCheck().getReferencedKeys("bd_produce", pksList.toArray(new String[0]));
		if (refPks != null && refPks.length > 0) {
			referencedPkSet.addAll(Arrays.asList(refPks));
		}

		if (referencedPkSet.size() > 0) {
			return referencedPkSet.toArray(new String[0]);
		}
		return null;
	}

	/**
	 * 加锁，失败则抛出异常
	 * 
	 * @param lockedString
	 * @throws BusinessException
	 */
	private void addlock(String lockedString) throws BusinessException {
		boolean isNeedUnLock = PKLock.getInstance().addDynamicLock(
				lockedString + InvocationInfoProxy.getInstance().getCorpCode());

		// 加锁失败，则抛异常
		if (!isNeedUnLock)
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081206-000099")/*已发生并发操作，请稍后重新进行该操作！*/);

	}

	/**
	 * 获得公司目录的基本档案缓存
	 * 
	 * @return
	 */
	private IBDAccessor getCorpBDAccessor() {
		if (corpBDAccessor == null) {
			corpBDAccessor = AccessorManager.getAccessor(IBdinfoConst.CORP, "0001");
		}
		return corpBDAccessor;
	}

	/**
	 * 获得库存组织的基本档案缓存
	 * 
	 * @return
	 */
	private IBDAccessor getCalbodyBDAccessor() {
		if (calbodyBDAccessor == null) {
			String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
			calbodyBDAccessor = AccessorManager.getAccessor(IBdinfoConst.CALBODY, pk_corp);
		}
		return calbodyBDAccessor;
	}

	private class InvCodeComparator implements Comparator<InvSort> {

		public int compare(InvSort o1, InvSort o2) {
			if(o1 == null || o1.getInvcode() == null)
				return -1;
			if(o2 == null || o2.getInvcode() == null)
				return 1;
			return o1.getInvcode().compareTo(o2.getInvcode());
		}

	}

	/**
	 * 根据库存组织、存货管理档案主键、默认模板ID分配物料生产档案数据
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-25上午11:01:17
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @param key 公司
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID,String key) throws BusinessException{
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000141")/*不存在待分配存货管理档案！*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*目标组织不存在！*/);
		}
		
		// 记录异常日志的操作信息
//		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String pk_corp =key;
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.ASSIGN);
		
		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		for (int i = 0; i < targetPks.length; i++) {
			pk_templetID_map.put(targetPks[i], defaultTemplateID);
		}
		
		// 分配数据
		assignInvmandocToCalbody1(targetPks, pkList, pk_templetID_map, logUtil, pk_corp);
		
		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * 根据库存组织、存货管理档案主键、默认模板ID分配物料生产档案数据
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-2-25上午11:01:17
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public void assignInvmandocToCalbody1(String[] targetPks, List<String> pkList, Map<String, String> pk_templetID_map, BDLogInfoUtil logUtil, String pk_corp)
			throws BusinessException {
		
		ProduceAssignedVOCreator voCreator = new ProduceAssignedVOCreator();
		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*库存组织编码为：*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*存货基本档案的编码为：*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// 清空上次循环操作的数据
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// 设置begin=pkList.size()，在执行完本次循环后跳出
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeAssignedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			for (int i = 0; i < targetPks.length; i++) {
				
				// 设置分配模板数据
				GeneralExVO defaultData = getTempletValueLoader().getDefaultDataByTempletID(pk_templetID_map.get(targetPks[i]), "10081210");
				voCreator.setTempletData(defaultData);

				// 获得待分配的存货管理档案
				List<ProduceVO> voList = voCreator.getAssignBasManUnionVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil, pk_corp); 
				
				//若待分配数据为null不执行数据插入操作
				if (voList != null && voList.size()>0) {

					// 构造校验服务
					BDOperateServ opServer = new BDOperateServ();
					Map<ProduceVO, String> errMsgMap = new HashMap<ProduceVO, String>();
					// 调用事前校验
					for (ProduceVO produceVO : voList) {
						try {
						opServer.beforeOperate("10081208", IBDOperate.BDOPERATION_ASSIGN, produceVO.getPk_invmandoc(),
								produceVO.getPk_calbody(), produceVO);
						} catch (BusinessException e) {
							errMsgMap.put(produceVO, e.getMessage());	
						}
					}
					if (errMsgMap.keySet()!= null && errMsgMap.keySet().size() >0) {
						BddataVO calbodyBDVO = getCalbodyBDAccessor().getDocByPk(targetPks[i]);
						// 记录错误日志及删除不能分配的数据
						for (ProduceVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invmandoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// 记录日志信息
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();

					String[] keys = getProduceDAO().insertArray(voList.toArray(new ProduceVO[0]));

					// 调用事后校验
					int j = 0;
					for (ProduceVO produceVO : voList) {
						produceVO.setPrimaryKey(keys[j++]);
						opServer.afterOperate("10081208", IBDOperate.BDOPERATION_ASSIGN, produceVO.getPk_invmandoc(),
								produceVO.getPk_calbody(), produceVO);
						
					}

				}
			}
		}
		nc.bs.bd.cache.CacheProxy.fireDataInserted("bd_produce", null);

	}
}
