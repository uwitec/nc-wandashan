package nc.itf.uap.bd.inv;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.pub.BusinessException;
import nc.vo.pub.assign.AssignQueryContext;

/**
 * ���������������������������񣨽�����������ڵ����ʱ���ã�
 * 
 * @author jiangjuna
 * 
 */
public interface IInventoryAssign {
	/**
	 * ����������Ŀ�깫˾�������飬����������������Ŀ�깫˾
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
	 * ���ݻ��������������顢Ŀ�깫˾�������飬����������������Ŀ�깫˾
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
	 * ��������ȡ�����������������
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
	 * ���ݻ�����������ȡ�����������������
	 * 
	 * @param targetPks
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvbasdocAssignByPkList(String[] targetPks, List<String> pkList)
			throws BusinessException;

	/**
	 * ����AssignQueryContext��ѯȫ�������б�.
	 * 
	 * @param nodeQueryContext
	 * @return
	 * @throws BusinessException
	 */
	public List<String> queryAllInvbasdocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException;

	/**
	 * ����������ѯ�����������
	 * 
	 * @param pkSet
	 * @return
	 * @throws BusinessException
	 */
	public List<Object> queryInvbasdocByPkSet(Set<String> pkSet) throws BusinessException;

	/**
	 * ����������Ŀ������֯�������飬��������������Ŀ������֯
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
	 * ���ݴ���������������顢Ŀ������֯�������飬��������������Ŀ������֯
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
	 * ��������ȡ���������������
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
	 * ���ݴ������������ȡ���������������
	 * 
	 * @param targetPks
	 * @param pkList
	 * @return
	 * @throws BusinessException
	 */
	public ErrLogReturnValue cancelInvmandocAssignByPkList(String[] targetPks, List<String> pkList)
			throws BusinessException;

	/**
	 * ����AssignQueryContext��ѯȫ�������б�.
	 * 
	 * @param nodeQueryContext
	 * @return
	 * @throws BusinessException
	 */
	public List<String> queryAllInvmandocPksByNodeContext(AssignQueryContext nodeQueryContext) throws BusinessException;

	/**
	 * ����������ѯ������������������������VO
	 * 
	 * @param pkSet
	 * @return
	 * @throws BusinessException
	 */
	public List<Object> queryBasManUnionVOByPkSet(Set<String> pkSet) throws BusinessException;

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
	public ErrLogReturnValue assignInvmandoc(String[] targetPks, List<String> pkList, String defaultTemplateID,String key) throws BusinessException;

}
