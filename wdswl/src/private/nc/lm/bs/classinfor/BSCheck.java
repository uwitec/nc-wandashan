package nc.lm.bs.classinfor;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
/**
 * @author mlr
 * 后台校验类
 *  动作执行前 执行check方法
 *  传来的 AggregatedValueObject 对象是通过
 *  getBillUI().getChangedVOFromUI()方法得到的
 *  聚合vo
 */
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
		 //班级号 数据库唯一 （都是指公司级）
		if(vo.getParentVO()==null){
			return;
		}				
		BsUniqueCheck.FieldUniqueChecks(new SuperVO[]{(SuperVO) vo.getParentVO()},new String[]{"pk_corp","cclasscode"},"","班级编号在数据库中已经存在");	
		//学号 数据库唯一（都是指公司级）	
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}
		BsUniqueCheck.FieldUniqueChecks(vo,new String[]{"ccstucode"}," and pk_corp='"+vo.getParentVO().getAttributeValue("pk_corp")+"'","","学生编号在数据库中已经存在");	
		}
	}
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
	}
}
