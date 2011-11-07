package nc.bs.wds.dm.corpseal;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
/**
 * 客户公司图章 后台校验类
 * @author Administrator
 *xjx  add
 */
public class BdbusiCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		CorpsealVO ivo = (CorpsealVO) vo.getParentVO();  //pk_wds_corpseal
		String primary= PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		//新增保存和 修改保存 区分校验
		if(primary == null){//新增校验
			String pk_cumandoc=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_cumandoc());
			if(pk_cumandoc == null){
				return ;
			}
			String condition = " pk_cumandoc='" + pk_cumandoc+ "' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(CorpsealVO.class,condition);
	        if(list != null &&  list.size() >0){
	        	throw new BusinessException("该客户已经存在");
	        }
		}else{//修改校验 
			String pk_cumandoc=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_cumandoc());
			if(pk_cumandoc == null){
				return ;
			}
			String condition = " pk_cumandoc='" + pk_cumandoc+ "' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(CorpsealVO.class,condition);
	        if(list != null &&  list.size() >1){
	        	throw new BusinessException("客户存在重复");
	        }
	        if(list != null &&  list.size() == 1){
	        	CorpsealVO queryVO = (CorpsealVO)list.get(0);
	        	if(!primary.equalsIgnoreCase(queryVO.getPrimaryKey())){
		        	throw new BusinessException("该客户已经存在");

	        	}
	        }
			
		}
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
