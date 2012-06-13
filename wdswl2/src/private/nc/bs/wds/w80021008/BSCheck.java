package nc.bs.wds.w80021008;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.ie.storepersons.TbStockstaffVO;


public class BSCheck implements IBDBusiCheck{
	private BaseDAO dao=null;
	
   private BaseDAO getDao(){
	   if(dao==null){
		   dao=new BaseDAO();
	   }
	   return dao;	   
   }
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		
		if(intBdAction==IBDACTION.SAVE){
	    if(vo!=null){
	    	if(vo.getParentVO()!=null){
	    		TbStockstaffVO bb=(TbStockstaffVO)vo.getParentVO();
	    	
	    	
	    		String sql="select count(*) from tb_stockstaff where cuserid='"+bb.getCuserid()+"'";
	    		Object obj=getDao().executeQuery(sql, new ColumnProcessor());
	    		
	    		if(obj!=null){
	    			Integer count=(Integer)obj;
	    			if(count.intValue()>0){
	    				throw new BusinessException("该人员已经和仓库绑定不能在绑定！");
	    			}
	    			
	    		}
	    		
	    		
	    	}
	    	
	    }
		}
		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
		
	}

}
