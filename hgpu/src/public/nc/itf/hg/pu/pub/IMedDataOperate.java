package nc.itf.hg.pu.pub;


import nc.vo.hg.pu.pub.freeitem.DefdefVO;
import nc.vo.hg.pu.pub.freeitem.VInvVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

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
