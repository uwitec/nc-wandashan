package nc.bs.wds.dm.storebing;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
import nc.vo.wds.dm.storebing.BdStordocVO;
import nc.vo.wds.load.teamdoc.TeamdocBVO;
import nc.vo.wds.load.teamdoc.TeamdocHVO;
/**
 * 
 * 基本档案 后台校验类 

 * author:mlr
 * */

public class BSCheck implements IBDBusiCheck {
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
//        if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
//        	return;
//        }
        if(vo.getParentVO().getPrimaryKey()==null || vo.getParentVO().getPrimaryKey().trim().length()==0){
        	return;
        }
        BdStordocVO ivo = (BdStordocVO) vo.getParentVO();  //pk_wds_corpseal
		String primary= PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		//新增保存和 修改保存 区分校验
		if(primary == null){//新增校验
			String pk_sendareacl=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_sendareacl());
			if(pk_sendareacl == null){
				return ;
			}
			String condition = " pk_sendareacl='" + pk_sendareacl+ "' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(BdStordocVO.class,condition);
	        if(list != null &&  list.size() >0){
	        	throw new BusinessException("该发货地区已经存在");
	        }
		}else{//修改校验 
			String pk_sendareacl=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_sendareacl());
			if(pk_sendareacl == null){
				return ;
			}
			String condition = " pk_sendareacl='" + pk_sendareacl+ "' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(BdStordocVO.class,condition);
	        if(list != null &&  list.size() >1){
	        	throw new BusinessException("发货地区存在重复");
	        }
	        if(list != null &&  list.size() == 1){
	        	BdStordocVO queryVO = (BdStordocVO)list.get(0);
	        	if(!primary.equalsIgnoreCase(queryVO.getPrimaryKey())){
		        	throw new BusinessException("该发货地区已经存在");

	        	}
	        }
			
		}
        
        
        SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
        if( vos==null || vos.length==0 ){
        	return;
        }
        int size=vos.length;
        for(int i=0;i<size;i++){
        if(BsUniqueCheck.isEmpty(vos[i].getAttributeValue("pk_cumandoc"))){
        	continue;
        }
        BsUniqueCheck.FieldUniqueCheck(vos[i], "pk_cumandoc"," and pk_stordoc <> '"+vo.getParentVO().getPrimaryKey()+"'", " 该客商已在其他仓库绑定");
        }		
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
