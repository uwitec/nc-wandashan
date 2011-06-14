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
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :
	 * �ɹ�ȡ��(WDSC),�����ʱ��У�����Σ���������WDS6��
	 * @ʱ�䣺2011-4-13����04:50:34
	 * @param pk_cgqy_h
	 * @throws DAOException 
	 */
	public void checkBeforeUnApprove(String pk_cgqy_h) throws BusinessException{
		String sql = " select count(0) from tb_outgeneral_b where csourcebillhid='"+pk_cgqy_h+"' and isnull(dr,0)=0";
		Integer i =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR),0);
		if(i>0){
			throw new BusinessException("���������������ⵥ,����ɾ�����ε����ٲ���");
		}
	}
}
