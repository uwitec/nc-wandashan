package nc.bs.wds.invbasdoc;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.invbasdoc.InvbasdocVO;


public class BSCheck implements IBDBusiCheck{
	private  BaseDAO dao;
	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}
	
	

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if(intBdAction !=IBDACTION.SAVE){
			return ;
		}
		if(vo==null || vo.getParentVO()==null){
			return ;
		}
		
		//判断是修改后的保存还是新增后的保存
		InvbasdocVO ivo=(InvbasdocVO) vo.getParentVO();
		ivo.validateOnSave();
		
		//唯一校验增加pk_corp条件
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " 该存货在数据库中已经存在 ");
		
		//如果是新增后的保存执行下面的代码
//		if(ivo.getPrimaryKey()==null ||ivo.getPrimaryKey().trim().equals("")){
////		
////			String condition=" pk_invbasdoc='"+ivo.getPk_invbasdoc()+"' and isnull(dr,0)=0";
////			List list=(List) getDao().retrieveByClause(InvbasdocVO.class, condition);	
////			if(list==null || list.size()==0){
////				return;
////			}else{
////				throw new BusinessException(" 该存货在数据库中已经存在 ");
////			}
//			//唯一校验增加pk_corp条件
//			BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " 该存货在数据库中已经存在 ");
//		}else{
//		//否则执行下面的代码
//			String condition=" pk_wds_invbasdoc='"+ivo.getPrimaryKey()+"' and  isnull(dr,0)=0";
//			List list=(List) getDao().retrieveByClause(InvbasdocVO.class, condition);	
//			
//			 if(list==null){
//				 return;
//			 }
//			 //判断修改后的记录，是否改变了存货（即拿数据库中的记录和ui中的当前记录进行比较）
//			 InvbasdocVO ivo2=(InvbasdocVO)list.get(0);
//			 if(ivo.getPk_invbasdoc().equalsIgnoreCase(ivo2.getPk_invbasdoc())){
//				 return;
//			 }
////		 String condition1=" pk_invbasdoc='"+ivo.getPk_invbasdoc()+"' and  isnull(dr,0)=0";
////			List list1=(List) getDao().retrieveByClause(InvbasdocVO.class, condition1);	
////			if(list1==null || list1.size()==0){
////				return;
////			}else{
////				throw new BusinessException(" 该存货在数据库中已经存在 ");
////			}
//		//唯一校验增加pk_corp条件
//			BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " 该存货在数据库中已经存在 ");
//		 }	 
//		
//		
//		
		
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
















