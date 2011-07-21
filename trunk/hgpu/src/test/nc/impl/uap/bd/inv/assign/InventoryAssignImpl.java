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
	// �������
	private final String invmandocAssign = "invmandocAssign";
	private final String invmandocCancelassign = "invmandocCancelassign";
	private final String produceAssign = "produceAssign";
	private final String produceCancelassign = "produceCancelassign";

	private int batchSize = 5000;

	// ��˾Ŀ¼�Ļ�����������
	private IBDAccessor corpBDAccessor;

	// �����֯�Ļ�����������
	private IBDAccessor calbodyBDAccessor;

	// ģ��Ĭ�����ݼ�����
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
	 * ���ݷ���״̬��������Ŀ����֯����˾������֯����ѯ���������������
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
		
		// �������֯����˾����
		Map<String, List<String>> corp_calbodys_map = new HashMap<String, List<String>>();
		getTargetCorpPks(targets, corp_calbodys_map);
		
		for (int i = 0; i < targets.length; i++) {
			if (targets[i].length()==4) {
				corpPkSet.add(targets[i]);
				// ���˵�Ŀ����֯�д��ڵĹ�˾�Ŀ����֯���ݣ�����������˾ʱ����ѯ�����֯�����壬��Ϊ������ݰ�������������˾������֮�У�
				corp_calbodys_map.remove(targets[i]);
			}
		}
		
		// ��ò������ϼ���˾�Ŀ����֯
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
			// ��ѯ��˾����
			boolean isAssign = false;
			if (IAssignStatus.STATUS_ASSINGED.equals(status)){
				isAssign = true;
			}
			
			// ��ѯ��˾����
			if (corpPkSet != null && corpPkSet.size() > 0) {				
				result = queryAllInvbasdocPkAndCodesByCorps(isAssign, corpPkSet.toArray(new String[0]), condition);
			}	
			
			// ��ѯ�����֯���ݣ����н���ѡ��״̬����ѡ�п����֯ʱ���ذ����ϼ���˾���ʲ�ѯ�����֯����������--����������䲻ִ�У�Ĭ������Ϊfalse��
			if (calbodyPkSet != null && calbodyPkSet.size() > 0) {	
				TreeSet<InvSort> calbodyResult = queryAllInvbasdocPkAndCodesByCalbodys(isAssign, calbodyPkSet.toArray(new String[0]), condition);
				if (result == null) {
					result = new TreeSet<InvSort>(new InvCodeComparator());
					result.addAll(calbodyResult);
				} else if (isAssign) {
					// ��������ȡ����
					result.addAll(calbodyResult);
				} else if (!isAssign) {
					// δ��������ȡ����
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
	 * ���ݷ���״̬��������Ŀ�깫˾��ѯ���������������
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
	 * �����Ƿ���䡢Ŀ�깫˾����ѯ������ѯ���������������������������
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
						// STATUS_UNASSIGN״̬��һ�ν��ӦΪȫ����ӣ��˺�Ϊ������������
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
	 * �����Ƿ���䡢Ŀ������֯����ѯ������ѯ���������������������������
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
						// STATUS_UNASSIGN״̬��һ�ν��ӦΪȫ����ӣ��˺�Ϊ������������
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
		// ��������֤ÿ��ֻ��һ���û����з��������
		addlock(this.invmandocAssign);
		List<String> pkList = queryAllInvbasdocPks(assignstatus, targetPks, condition);
		
		return assignInvbasdocToCorpAndCalbody(targetPks, pkList, defaultTemplateID_targetPk_map);
	}

	public ErrLogReturnValue assignInvbasdocByPkList(String[] targetPks, List<String> pkList, Map<String, List<String>> defaultTemplateID_targetPk_map)
			throws BusinessException {
		// ��������֤ÿ��ֻ��һ���û����з��������
		addlock(this.invmandocAssign);
		return assignInvbasdocToCorpAndCalbody(targetPks, pkList, defaultTemplateID_targetPk_map);
	}
	
	/**
	 * ���ݹ�˾�������֯�����������������������Ĭ��ģ��ID����������������������������������
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID_targetPk_map
	 * @return
	 * @throws BusinessException
	 */
	private ErrLogReturnValue assignInvbasdocToCorpAndCalbody(String[] targetPks, List<String> pkList,
			Map<String, List<String>> defaultTemplateID_targetPk_map) throws BusinessException {

		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000034")/*�����ڴ���������������!*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
		
		// ���Ŀ�깫˾
		Map<String, List<String>> corp_calbodys_map = new HashMap<String, List<String>>();
		String[] corpPks = getTargetCorpPks(targetPks, corp_calbodys_map);

		// ��¼�쳣��־�Ĳ�����Ϣ
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081206", "0001", operator, IBDOperateConst.ASSIGN);

		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		// ������֯��ģ��
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

		// ������������������˾
		if (corpPks != null && corpPks.length > 0) {
			assignInvbasdocToCorp(corpPks, pkList, pk_templetID_map, logUtil);
		}

		// ���η������������������֯
		for (String pk_corp : corp_calbodys_map.keySet()) {
			List<String> invmandocPkList = queryInvmandocPkByInvbasdocPk(pkList, pk_corp);
			assignInvmandocToCalbody(corp_calbodys_map.get(pk_corp).toArray(new String[0]), invmandocPkList,
					pk_templetID_map, logUtil, pk_corp);
		}

		return logUtil.getErrLogReturnValue(null);
	}

	/**
	 * ����Ŀ����֯���������Ŀ�깫˾������Ŀ������֯���ڹ�˾����Ŀ������֯������
	 * 
	 * @param targetPks
	 *            Ŀ����֯
	 * @param corp_calbodys_map
	 *            ��˾������ù�˾��Ŀ������֯���б�
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
	 * ���ݴ���������������빫˾����,��ѯ�ѷ���������������
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
	 * ���ݹ�˾�������������������Ĭ��ģ��ID����������������
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	private void assignInvbasdocToCorp(String[] targetPks, List<String> pkList, Map<String, String> pk_templetID_map, BDLogInfoUtil logUtil)
			throws BusinessException {
		
		InvmandocAssignedVOCreator voCreator = new InvmandocAssignedVOCreator();

		String corpCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000000")/*��˾����Ϊ��*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*������������ı���Ϊ��*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// ����ϴ�ѭ������������
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// ����begin=pkList.size()����ִ���걾��ѭ��������
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
				
				// ��ô�����Ĵ��������
				List<CorpInventoryVO> voList = voCreator.getAssignInvmandocVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil);

				// ������������Ϊnull��ִ�����ݲ������
				if (voList != null && voList.size() > 0) {
					// �����̨У�����
					BDOperateServ opServer = new BDOperateServ();
					Map<CorpInventoryVO, String> errMsgMap = new HashMap<CorpInventoryVO, String>();
					// ������ǰУ��ӿ�
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
						// ��¼������־��ɾ�����ܷ��������
						for (CorpInventoryVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(corpCodeIs).append(corpBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invbasdoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// ��¼��־��Ϣ
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();
					String[] keys = getBaseDao().insertObject(voList.toArray(new CorpInventoryVO[0]),
							new CorpInventoryMapping());
					 
					// �����º�У��ӿ�
					int j = 0;
					for (CorpInventoryVO inventoryVO : voList) {
						// ���ò��������
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
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
		
		
		Set<String> corpPkSet = new HashSet<String>();
		for (int i = 0; i < targetPks.length; i++) {
			if (targetPks[i].length()==4) {
				corpPkSet.add(targetPks[i]);
			}
		}
		
		// ��Ŀ����֯�в����ڹ�˾����
		if (corpPkSet.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000045")/*��ѡ��˾����ȡ�����䣡*/);
		}
		
		// ��������֤ÿ��ֻ��һ���û����з��������
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
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
		
		
		Set<String> corpPkSet = new HashSet<String>();
		for (int i = 0; i < targetPks.length; i++) {
			if (targetPks[i].length()==4) {
				corpPkSet.add(targetPks[i]);
			}
		}
		
		// ��Ŀ����֯�в����ڹ�˾����
		if (corpPkSet.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000045")/*��ѡ��˾����ȡ�����䣡*/);
		}
		
		// ��������֤ÿ��ֻ��һ���û����з��������
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
	 * ���ݴ�������������б�ɾ������������ķ�������
	 * 
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private ErrLogReturnValue cancelInvbasdocAssign(List<String> pkList) throws BusinessException {
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000037")/*�����ڴ�ȡ��������������!*/);
		}
		
		String corpCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000000")/*��˾����Ϊ��*/;
		String corpPkIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000001")/*��˾����Ϊ��*/;
		String invmandocCodeIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000002")/*����������ı���Ϊ��*/;
		String invmandocPkIs = NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000003")/*�������������Ϊ��*/;
		

		// ��¼�쳣��־�Ĳ�����Ϣ
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081206", "0001", operator, IBDOperateConst.CANCELASSIGN);

		String refMsg = BDMsg.MSG_REF_NOT_DELETE();

		// �����̨У�����
		BDOperateServ opServer = new BDOperateServ();
		BDDelLog dellog = new BDDelLog();

		List<String> toBeDeletedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {
			// ����ϴ�ѭ������������
			toBeDeletedPKs.clear();
			if (pkList.size() - begin < this.batchSize) {
				toBeDeletedPKs.addAll(pkList.subList(begin, pkList.size()));
				// ����begin=pkList.size()����ִ���걾��ѭ��������
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeDeletedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			// ��ѯ��ɾ��������VO
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

			// ��ѯ������Ϊ��ʱ���������������
			if (invmandocList != null && invmandocList.size() > 0) {

				// ����У���ʱ
				TimeCounter tc = new TimeCounter("�������");

				Map<String, InvmandocVO> pk_vo_map = new HashMap<String, InvmandocVO>();
				for (InvmandocVO invmandocVO : invmandocList) {
					pk_vo_map.put(invmandocVO.getPrimaryKey(), invmandocVO);
				}

				String[] referencedPks = isInvmandocReferenced(pk_vo_map);
				// ������������
				if (referencedPks != null && referencedPks.length > 0) {
					for (int i = 0; i < referencedPks.length; i++) {

						// ��ȡ������
						InvmandocVO vo = pk_vo_map.get(referencedPks[i]);

						StringBuffer logmsg = new StringBuffer();
						// ���ù�˾����
						BddataVO corpBDVO = getCorpBDAccessor().getDocByPk(vo.getPk_corp());
						if (corpBDVO != null && corpBDVO.getCode() != null && corpBDVO.getCode().trim().length() > 0) {
							logmsg.append(corpCodeIs).append(corpBDVO.getCode());
						} else {
							logmsg.append(corpPkIs).append(vo.getPk_corp());
						}
						// ���ô����������
						IBDAccessor bdAccessor = AccessorManager.getAccessor(IBdinfoConst.INVMANDOC, vo.getPk_corp());
						BddataVO invmandocBDVO = bdAccessor.getDocByPk(referencedPks[i]);
						if (invmandocBDVO != null && invmandocBDVO.getCode() != null
								&& invmandocBDVO.getCode().trim().length() > 0) {
							logmsg.append(" ").append(invmandocCodeIs).append(invmandocBDVO.getCode());
						} else {
							logmsg.append(" ").append(invmandocPkIs).append(referencedPks[i]);
						}
						logmsg.append(" ").append(refMsg);
						// ��¼��־��Ϣ
						logUtil.addLogMsgBatch(logmsg.toString());
						// �Ӵ�ɾ��������ȥ�������õĴ��������
						pk_vo_map.remove(referencedPks[i]);
					}
					// д���쳣��־
					logUtil.writeLogMsgBatch();

				}

				tc.close();

				List<String> invmandocPkList = new ArrayList<String>();
				// ��ȫ����������ִ��ȡ���������
				if (pk_vo_map.size() != 0) {
					// ������ǰУ��ӿ�
					for (String pk_invmandoc : pk_vo_map.keySet()) {
						invmandocPkList.add(pk_invmandoc);
						InvmandocVO invmandocVO = pk_vo_map.get(pk_invmandoc);
						opServer.beforeOperate("10081206", IBDOperate.BDOPERATION_CANCELASSIGN, invmandocVO
								.getPrimaryKey(), invmandocVO.getPk_corp(), invmandocVO);
					}
					String[] pks = invmandocPkList.toArray(new String[0]);

					// ɾ����־
					dellog.delPKs("bd_invmandoc", pks);

					// ɾ�����������
					getBaseDao().deleteByPKs(InvmandocVO.class, pks);
					// ɾ���滻��
					deleteInvrepl(pks);
					// ɾ�������
					deleteInvbindle(pks);
					// ɾ�������ĵ�
					deleteAssociateFile(pks, pk_vo_map);

					// �����º�У��ӿ�
					for (String pk_invmandoc : pk_vo_map.keySet()) {
						invmandocPkList.add(pk_invmandoc);
						InvmandocVO invmandocVO = pk_vo_map.get(pk_invmandoc);
						opServer.afterOperate("10081206", IBDOperate.BDOPERATION_CANCELASSIGN, invmandocVO
								.getPrimaryKey(), invmandocVO.getPk_corp(), invmandocVO);
					}
					// ����֪ͨ
					nc.bs.bd.cache.CacheProxy.fireDataDeletedBatch("bd_invmandoc", pks);

				}
			}
		}

		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * ɾ���������������ĵ�
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
	 * ���ݴ��������������ɾ���滻��
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
	 * ���ݴ��������������ɾ�������
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
	 * ���ݷ���״̬��������Ŀ�깫˾��ѯ���������������
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
						// STATUS_UNASSIGN״̬��һ�ν��ӦΪȫ����ӣ��˺�Ϊ������������
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

		// ��������֤ÿ��ֻ��һ���û����з��������
		addlock(this.produceAssign);

		List<String> pkList = queryAllInvmandocPks(assignstatus, targetPks, condition);
		
		return assignInvmandoc(targetPks, pkList, defaultTemplateID);
	}

	public ErrLogReturnValue assignInvmandocByPkList(String[] targetPks, List<String> pkList, String defaultTemplateID)
			throws BusinessException {

		// ��������֤ÿ��ֻ��һ���û����з��������
		addlock(this.produceAssign);
		
		return assignInvmandoc(targetPks, pkList, defaultTemplateID);
	}
	
	private ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID) throws BusinessException{
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000141")/*�����ڴ���������������*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
		
		// ��¼�쳣��־�Ĳ�����Ϣ
		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.ASSIGN);
		
		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		for (int i = 0; i < targetPks.length; i++) {
			pk_templetID_map.put(targetPks[i], defaultTemplateID);
		}
		
		// ��������
		assignInvmandocToCalbody(targetPks, pkList, pk_templetID_map, logUtil, pk_corp);
		
		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * ���ݿ����֯�����������������Ĭ��ģ��ID��������������������
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
		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*�����֯����Ϊ��*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*������������ı���Ϊ��*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// ����ϴ�ѭ������������
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// ����begin=pkList.size()����ִ���걾��ѭ��������
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeAssignedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			for (int i = 0; i < targetPks.length; i++) {
				
				// ���÷���ģ������
				GeneralExVO defaultData = getTempletValueLoader().getDefaultDataByTempletID(pk_templetID_map.get(targetPks[i]), "10081210");
				voCreator.setTempletData(defaultData);

				// ��ô�����Ĵ��������
				List<ProduceVO> voList = voCreator.getAssignBasManUnionVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil, pk_corp); 
				
				//������������Ϊnull��ִ�����ݲ������
				if (voList != null && voList.size()>0) {

					// ����У�����
					BDOperateServ opServer = new BDOperateServ();
					Map<ProduceVO, String> errMsgMap = new HashMap<ProduceVO, String>();
					// ������ǰУ��
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
						// ��¼������־��ɾ�����ܷ��������
						for (ProduceVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invmandoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// ��¼��־��Ϣ
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();

					String[] keys = getProduceDAO().insertArray(voList.toArray(new ProduceVO[0]));

					// �����º�У��
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
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
				
		// ��������֤ÿ��ֻ��һ���û����з��������
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
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
				
		// ��������֤ÿ��ֻ��һ���û����з��������
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
	 * ���������������������б�ȡ���������������ķ�������
	 * 
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private ErrLogReturnValue cancelInvmandocAssign(List<String> pkList) throws BusinessException {
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000142")/*�����ڴ�ȡ������������������!*/);
		}

		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*�����֯����Ϊ��*/;
		String calbodyPkIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000133")/*�����֯����Ϊ��*/;
		String ProduceCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000136")/*�������������ı���Ϊ��*/;
		String ProducePkIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000135")/*����������������Ϊ��*/;
		
		// ��¼�쳣��־�Ĳ�����Ϣ
		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.CANCELASSIGN);

		String refMsg = BDMsg.MSG_REF_NOT_DELETE();

		// �����̨У�����
		BDOperateServ opServer = new BDOperateServ();
		BDDelLog dellog = new BDDelLog();

		List<String> toBeDeletedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {
			// ����ϴ�ѭ������������
			toBeDeletedPKs.clear();
			if (pkList.size() - begin < this.batchSize) {
				toBeDeletedPKs.addAll(pkList.subList(begin, pkList.size()));
				// ����begin=pkList.size()����ִ���걾��ѭ��������
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeDeletedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			// ��ѯ��ɾ��������������VO
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

			// ��ѯ������Ϊ��ʱ���������������
			if (produceList != null && produceList.size() > 0) {

				// ����У���ʱ
				TimeCounter tc = new TimeCounter("�������");

				Map<String, ProduceVO> pk_vo_map = new HashMap<String, ProduceVO>();
				for (ProduceVO produceVO : produceList) {
					pk_vo_map.put(produceVO.getPrimaryKey(), produceVO);
				}

				String[] referencedPks = isProduceReferenced(produceList);
				// ������������
				if (referencedPks != null && referencedPks.length > 0) {
					for (int i = 0; i < referencedPks.length; i++) {

						// ��ȡ����������������
						ProduceVO vo = pk_vo_map.get(referencedPks[i]);

						StringBuffer logmsg = new StringBuffer();
						// ���ÿ����֯����
						BddataVO calbodyBDVO = getCalbodyBDAccessor().getDocByPk(vo.getPk_calbody());
						if (calbodyBDVO != null && calbodyBDVO.getCode() != null
								&& calbodyBDVO.getCode().trim().length() > 0) {
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
						} else {
							logmsg.append(calbodyPkIs).append(vo.getPk_corp());
						}
						// ���ô����������
						IBDAccessor bdAccessor = AccessorManager.getAccessor(IBdinfoConst.PRODUCE, vo.getPk_corp());
						BddataVO produceBDVO = bdAccessor.getDocByPk(referencedPks[i]);
						if (produceBDVO != null && produceBDVO.getCode() != null
								&& produceBDVO.getCode().trim().length() > 0) {
							logmsg.append(" ").append(ProduceCodeIs).append(produceBDVO.getCode());
						} else {
							logmsg.append(" ").append(ProducePkIs).append(referencedPks[i]);
						}
						logmsg.append(" ").append(refMsg);
						// ��¼��־��Ϣ
						logUtil.addLogMsgBatch(logmsg.toString());
						// �Ӵ�ɾ��������ȥ�������õ�������������
						pk_vo_map.remove(referencedPks[i]);
					}
					// д���쳣��־
					logUtil.writeLogMsgBatch();

				}

				tc.close();

				// ��¼��ɾ������
				List<String> producePkList = new ArrayList<String>();
				// ��ȫ����������ִ��ȡ���������
				if (pk_vo_map.size() != 0) {
					// ������ǰУ��ӿ�
					for (String pk_produce : pk_vo_map.keySet()) {
						producePkList.add(pk_produce);
						ProduceVO produceVO = pk_vo_map.get(pk_produce);
						opServer.beforeOperate("10081208", IBDOperate.BDOPERATION_CANCELASSIGN, produceVO
								.getPrimaryKey(), produceVO.getPk_calbody(), produceVO);
					}
					String[] pks = producePkList.toArray(new String[0]);

					// ɾ����־
					dellog.delPKs("bd_produce", pks);

					// ɾ��������������
					deleteProduceByPks(pks);
					// ɾ���̶������б�
					deleteBatchlist(pks);

					// �����º�У��ӿ�
					for (String pk_produce : pk_vo_map.keySet()) {
						ProduceVO produceVO = pk_vo_map.get(pk_produce);
						opServer.afterOperate("10081208", IBDOperate.BDOPERATION_CANCELASSIGN, produceVO
								.getPrimaryKey(), produceVO.getPk_calbody(), produceVO);
					}
					// ����֪ͨ
					nc.bs.bd.cache.CacheProxy.fireDataDeletedBatch("bd_produce", pks);

				}
			}
		}
		return logUtil.getErrLogReturnValue(null);
	} 

	/**
	 * �h��������������
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
	 * ����������������������ɾ���̶������б�
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
	 * ȡ�������ѯ����������
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
		 * ��������"select pk_invmandc from bd_invmandoc where pk_corp in (
		 * "�ⲿ���õ�inSql���" ) and pk_invbasdoc in �� "pkList��inSql���" ��"; ���� sql =
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
	 * ����У��
	 * 
	 * @param tableName
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	private String[] isInvmandocReferenced(Map<String, InvmandocVO> pk_vo_map) throws BusinessException {
		Set<String> referencedPkSet = new HashSet<String>();
		
		// ����������У��
		String[] refPks = getReferenceCheck().getReferencedKeys("bd_invmandoc", pk_vo_map.keySet().toArray(new String[0]));
		if (refPks != null && refPks.length > 0) {
			referencedPkSet.addAll(Arrays.asList(refPks));
		}
		
		// У��ָ�����������еĻ������������ֶ���ָ����˾�Ƿ����á�
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		Map<String, String> pk_map = new HashMap<String, String>();
		// ԭ����ʵ��Ϊ����˾�������ݣ������ڲ�ѯ��ȡ����������ʱ����������˾����
		for (InvmandocVO vo : pk_vo_map.values()) {
			String pk_corp = vo.getPk_corp();
			String pk_invbasdoc = vo.getPk_invbasdoc();
			// ���������ѱ���������
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
		// ����˾������ѯ�������ݣ�����������뵽�����������б�
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
	 * ���ϵ�������У��
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
		// ��¼����������
		Set<String> referencedPkSet = new HashSet<String>();

		if (calbodyQry == null) {
			calbodyQry = (ICalbodyQry) NCLocator.getInstance().lookup(ICalbodyQry.class.getName());
		}

		ArrayList<String> pksList = new ArrayList<String>();
		for (ProduceVO produceVO : produceList) {
			// ��������Ƿ�����
			UFBoolean isInvRef = calbodyQry.isContain(produceVO.getPk_corp(), produceVO.getPk_calbody(), produceVO
					.getPk_invmandoc());
			if (isInvRef.booleanValue()) {
				referencedPkSet.add(produceVO.getPrimaryKey());
				continue;
			}
			// ���ϵͳ�Ƿ�����
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
	 * ������ʧ�����׳��쳣
	 * 
	 * @param lockedString
	 * @throws BusinessException
	 */
	private void addlock(String lockedString) throws BusinessException {
		boolean isNeedUnLock = PKLock.getInstance().addDynamicLock(
				lockedString + InvocationInfoProxy.getInstance().getCorpCode());

		// ����ʧ�ܣ������쳣
		if (!isNeedUnLock)
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081206-000099")/*�ѷ����������������Ժ����½��иò�����*/);

	}

	/**
	 * ��ù�˾Ŀ¼�Ļ�����������
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
	 * ��ÿ����֯�Ļ�����������
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
	 * ���ݿ����֯�����������������Ĭ��ģ��ID��������������������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-2-25����11:01:17
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @param key ��˾
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID,String key) throws BusinessException{
		if (pkList == null || pkList.size() == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081208", "UPP10081208-000141")/*�����ڴ���������������*/);
		}
		
		if (targetPks == null || targetPks.length == 0) {
			throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000046")/*Ŀ����֯�����ڣ�*/);
		}
		
		// ��¼�쳣��־�Ĳ�����Ϣ
//		String pk_corp = InvocationInfoProxy.getInstance().getCorpCode();
		String pk_corp =key;
		String operator = InvocationInfoProxy.getInstance().getUserCode();
		BDLogInfoUtil logUtil = new BDLogInfoUtil("10081208", pk_corp, operator, IBDOperateConst.ASSIGN);
		
		Map<String, String> pk_templetID_map = new HashMap<String, String>();
		for (int i = 0; i < targetPks.length; i++) {
			pk_templetID_map.put(targetPks[i], defaultTemplateID);
		}
		
		// ��������
		assignInvmandocToCalbody1(targetPks, pkList, pk_templetID_map, logUtil, pk_corp);
		
		return logUtil.getErrLogReturnValue(null);
	}
	
	/**
	 * ���ݿ����֯�����������������Ĭ��ģ��ID��������������������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-2-25����11:01:17
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public void assignInvmandocToCalbody1(String[] targetPks, List<String> pkList, Map<String, String> pk_templetID_map, BDLogInfoUtil logUtil, String pk_corp)
			throws BusinessException {
		
		ProduceAssignedVOCreator voCreator = new ProduceAssignedVOCreator();
		String calbodyCodeIs = NCLangResOnserver.getInstance().getStrByID("10081210", "UPP10081210-000134")/*�����֯����Ϊ��*/;
		String invCodeIs = NCLangResOnserver.getInstance().getStrByID("10081206", "UPT10081206-000041")/*������������ı���Ϊ��*/;
		
		List<String> toBeAssignedPKs = new ArrayList<String>();
		int begin = 0;
		while (begin < pkList.size()) {

			// ����ϴ�ѭ������������
			toBeAssignedPKs.clear();
			voCreator.clearCache();

			if (pkList.size() - begin < this.batchSize) {
				toBeAssignedPKs.addAll(pkList.subList(begin, pkList.size()));
				// ����begin=pkList.size()����ִ���걾��ѭ��������
				begin = pkList.size();
			} else {
				int end = begin + this.batchSize;
				toBeAssignedPKs.addAll(pkList.subList(begin, end));
				begin = end;
			}

			for (int i = 0; i < targetPks.length; i++) {
				
				// ���÷���ģ������
				GeneralExVO defaultData = getTempletValueLoader().getDefaultDataByTempletID(pk_templetID_map.get(targetPks[i]), "10081210");
				voCreator.setTempletData(defaultData);

				// ��ô�����Ĵ��������
				List<ProduceVO> voList = voCreator.getAssignBasManUnionVOByinvbasdocPkList(toBeAssignedPKs,
						targetPks[i], logUtil, pk_corp); 
				
				//������������Ϊnull��ִ�����ݲ������
				if (voList != null && voList.size()>0) {

					// ����У�����
					BDOperateServ opServer = new BDOperateServ();
					Map<ProduceVO, String> errMsgMap = new HashMap<ProduceVO, String>();
					// ������ǰУ��
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
						// ��¼������־��ɾ�����ܷ��������
						for (ProduceVO vo : errMsgMap.keySet()) {
							voList.remove(vo);
							StringBuffer logmsg = new StringBuffer();
							logmsg.append(calbodyCodeIs).append(calbodyBDVO.getCode());
							logmsg.append(" ").append(invCodeIs).append(voCreator.getInvCodeByPk(vo.getPk_invmandoc()));
							logmsg.append(" ").append(errMsgMap.get(vo));
							// ��¼��־��Ϣ
							logUtil.addLogMsgBatch(logmsg.toString());
						}
					}
					logUtil.writeLogMsgBatch();

					String[] keys = getProduceDAO().insertArray(voList.toArray(new ProduceVO[0]));

					// �����º�У��
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
