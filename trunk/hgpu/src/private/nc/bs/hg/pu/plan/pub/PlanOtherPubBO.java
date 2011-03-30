package nc.bs.hg.pu.plan.pub;

import nc.bs.pub.SuperDMO;
import nc.bs.pub.pf.IQueryData;
import nc.vo.hg.pu.plan.month.PlanOtherBVO;
import nc.vo.hg.pu.pub.PlanVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

public class PlanOtherPubBO implements IQueryData{
	
	private SuperDMO dmo = null;
	private SuperDMO getBaseDao(){
		if(dmo == null){
			dmo = new SuperDMO();
		}
		return dmo;
	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {
		// TODO Auto-generated method stub
		String whereS = " pk_plan = '"+key+"'";
		return getBaseDao().queryByWhereClause(PlanOtherBVO.class, whereS);
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		// TODO Auto-generated method stub				
		//查询  月计划   临时计划  专项资金计划
		SuperVO[] heads = getBaseDao().queryByWhereClause(PlanVO.class, whereString);		
		return heads;
	}

}
