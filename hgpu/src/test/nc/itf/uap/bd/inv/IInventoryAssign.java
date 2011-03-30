package nc.itf.uap.bd.inv;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.pub.BusinessException;
import nc.vo.pub.assign.AssignQueryContext;

/**
 * 存货基本档案与存货管理档案分配服务（仅供存货档案节点分配时调用）
 * 
 * @author jiangjuna
 * 
 */
public interface IInventoryAssign {
	/**
	 * 根据条件、目标公司主键数组，分配存货基本档案到目标公司
	 * 
	 * @param assignstatus
	 * @param targetPks
	 * @param condition
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvbasdocByCondition(String assignstatus, String[] targetPks, String condition,
			Map<String, List<String>> defaultTemplateID_targetPk_map) throws BusinessException;

	/**
	 * 根据基本档案主键数组、目标公司主键数组，分配存货基本档案到目标公司
	 * 
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvbasdocByPkList(String[] targetPks, List<String> pkList,
			Map<String, List<String>> defaultTemplateID_targetPk_map) throws BusinessException;

	/**
	 * 根据条件取消存货基本档案分配
	 * 
	 * @param assignstatus
	 * @param targetPks
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvbasdocAssignByCondition(String assignstatus, String[] targetPks, String condition)
			throws BusinessException;

	/**
	 * 根据基本档案主键取消存货基本档案分配
	 * 
	 * @param targetPks
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvbasdocAssignByPkList(String[] targetPks, List<String> pkList)
			throws BusinessException;

	/**
	 * 根据AssignQueryContext查询全部主键列表.
	 * 
	 * @param nodeQueryContext
	 * @return
	 * @throws BusinessException
	 */
	public List<String> queryAllInvbasdocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException;

	/**
	 * 根据主键查询存货基本档案
	 * 
	 * @param pkSet
	 * @return
	 * @throws BusinessException
	 */
	public List<Object> queryInvbasdocByPkSet(Set<String> pkSet) throws BusinessException;

	/**
	 * 根据条件、目标库存组织主键数组，分配存货管理档案到目标库存组织
	 * 
	 * @param assignstatus
	 * @param targetPks
	 * @param condition
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvmandocByCondition(String assignstatus, String[] targetPks, String condition,
			String defaultTemplateID) throws BusinessException;

	/**
	 * 根据存货管理档案主键数组、目标库存组织主键数组，分配存货管理档案到目标库存组织
	 * 
	 * @param targetPks
	 * @param pkList
	 * @param defaultTemplateID
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue assignInvmandocByPkList(String[] targetPks, List<String> pkList, String defaultTemplateID)
			throws BusinessException;

	/**
	 * 根据条件取消存货管理档案分配
	 * 
	 * @param assignstatus
	 * @param targetPks
	 * @param condition
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvmandocAssignByCondition(String assignstatus, String[] targetPks, String condition)
			throws BusinessException;

	/**
	 * 根据存货管理档案主键取消存货管理档案分配
	 * 
	 * @param targetPks
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvmandocAssignByPkList(String[] targetPks, List<String> pkList)
			throws BusinessException;

	/**
	 * 根据AssignQueryContext查询全部主键列表.
	 * 
	 * @param nodeQueryContext
	 * @return
	 * @throws BusinessException
	 */
	public List<String> queryAllInvmandocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException;

	/**
	 * 根据主键查询存货基本档案与管理档案的联合VO
	 * 
	 * @param pkSet
	 * @return
	 * @throws BusinessException
	 */
	public List<Object> queryBasManUnionVOByPkSet(Set<String> pkSet) throws BusinessException;

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
	public ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID,String key) throws BusinessException;

}
