package nc.itf.hg.pu.pub;


import nc.vo.hg.pu.pub.freeitem.DefdefVO;
import nc.vo.hg.pu.pub.freeitem.VInvVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 
 * 常用接口类
 *
 */
public interface IMedDataOperate {
	
	/**
	 * 批量更新 supervo
	 */
	public void updateSuperVOs(SuperVO[] vos) throws BusinessException;
	
	/**
	 * 自由项使用，查询自定义项VO
	 */
	public DefdefVO findByPrimaryKey(String key)  throws BusinessException;
	
	/**
	 * 查询vo信息--<根据存货管理ID，查询相应的存货vo信息>
	 */
	public VInvVO[] getInvVO(String[] cinvtoryids)  throws BusinessException;
	
}
