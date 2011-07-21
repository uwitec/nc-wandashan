package nc.itf.zb.pub;


import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.freeitem.DefdefVO;
import nc.vo.zb.pub.freeitem.VInvVO;

/**
 * 
 * ���ýӿ���
 *
 */
public interface IMedDataOperate {
	
	/**
	 * �������� supervo
	 */
	public void updateSuperVOs(SuperVO[] vos) throws BusinessException;
	
	/**
	 * ������ʹ�ã���ѯ�Զ�����VO
	 */
	public DefdefVO findByPrimaryKey(String key)  throws BusinessException;
	
	/**
	 * ��ѯvo��Ϣ--<���ݴ������ID����ѯ��Ӧ�Ĵ��vo��Ϣ>
	 */
	public VInvVO[] getInvVO(String[] cinvtoryids)  throws BusinessException;

}
