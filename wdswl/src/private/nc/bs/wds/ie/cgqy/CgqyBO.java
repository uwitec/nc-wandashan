package nc.bs.wds.ie.cgqy;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class CgqyBO {
	

private BaseDAO dao = null;

	BaseDAO getBaseDAO(){
			if(dao==null){
				dao = new BaseDAO();
			}
			return dao;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 :
	 * 采购取样(WDSC),弃审的时候校验下游（其他出库WDS6）
	 * @时间：2011-4-13下午04:50:34
	 * @param pk_cgqy_h
	 * @throws DAOException 
	 */
	public void checkBeforeUnApprove(String pk_cgqy_h) throws BusinessException{
		String sql = " select count(0) from tb_outgeneral_b where csourcebillhid='"+pk_cgqy_h+"' and isnull(dr,0)=0";
		Integer i =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR),0);
		if(i>0){
			throw new BusinessException("存在下游其他出库单,请先删除下游单据再操作");
		}
	}
}
