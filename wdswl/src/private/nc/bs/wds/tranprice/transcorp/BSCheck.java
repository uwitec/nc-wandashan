package nc.bs.wds.tranprice.transcorp;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
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
		//校验 运输公司代码和名称不为空
		SuperVO voparent = (SuperVO)(vo.getParentVO());
		Object tcode = voparent.getAttributeValue("ctranscorpcode");
		Object tname =voparent.getAttributeValue("vtranscorpname");
		if(BsUniqueCheck.isEmpty(tcode) || BsUniqueCheck.isEmpty(tname)){            	  
			throw new BusinessException("运输公司代码和运输公司名称不能为空"); 
	    }
		//校验 运输公司代码唯一性
		BsUniqueCheck.FieldUniqueCheck(voparent, "ctranscorpcode", "[运输公司代码] 在数据库中已经存在");
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
