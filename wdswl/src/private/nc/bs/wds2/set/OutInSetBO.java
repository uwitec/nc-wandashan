package nc.bs.wds2.set;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.rdcl.RdclVO;

public class OutInSetBO {
	
	private BaseDAO dao = null;
	
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public boolean isReturnErp(String coutinsetid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(coutinsetid)==null)
			return false;
		RdclVO vo = (RdclVO)getDao().retrieveByPK(RdclVO.class, coutinsetid);
		if(vo == null)
			throw new BusinessException("【收发类别】在收发类别档案不存在");
		return PuPubVO.getUFBoolean_NullAs(vo.getUisreturn(), UFBoolean.FALSE).booleanValue();
	}
}
