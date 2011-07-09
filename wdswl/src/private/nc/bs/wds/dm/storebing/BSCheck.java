package nc.bs.wds.dm.storebing;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.sendinvdoc.SendinvdocVO;
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
        if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
        	return;
        }
        if(vo.getParentVO().getPrimaryKey()==null || vo.getParentVO().getPrimaryKey().trim().length()==0){
        	return;
        }
        SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
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
