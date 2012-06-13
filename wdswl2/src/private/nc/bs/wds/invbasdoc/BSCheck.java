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
		
		//�ж����޸ĺ�ı��滹��������ı���
		InvbasdocVO ivo=(InvbasdocVO) vo.getParentVO();
		ivo.validateOnSave();
		
		//ΨһУ������pk_corp����
		BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " �ô�������ݿ����Ѿ����� ");
		
		//�����������ı���ִ������Ĵ���
//		if(ivo.getPrimaryKey()==null ||ivo.getPrimaryKey().trim().equals("")){
////		
////			String condition=" pk_invbasdoc='"+ivo.getPk_invbasdoc()+"' and isnull(dr,0)=0";
////			List list=(List) getDao().retrieveByClause(InvbasdocVO.class, condition);	
////			if(list==null || list.size()==0){
////				return;
////			}else{
////				throw new BusinessException(" �ô�������ݿ����Ѿ����� ");
////			}
//			//ΨһУ������pk_corp����
//			BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " �ô�������ݿ����Ѿ����� ");
//		}else{
//		//����ִ������Ĵ���
//			String condition=" pk_wds_invbasdoc='"+ivo.getPrimaryKey()+"' and  isnull(dr,0)=0";
//			List list=(List) getDao().retrieveByClause(InvbasdocVO.class, condition);	
//			
//			 if(list==null){
//				 return;
//			 }
//			 //�ж��޸ĺ�ļ�¼���Ƿ�ı��˴�����������ݿ��еļ�¼��ui�еĵ�ǰ��¼���бȽϣ�
//			 InvbasdocVO ivo2=(InvbasdocVO)list.get(0);
//			 if(ivo.getPk_invbasdoc().equalsIgnoreCase(ivo2.getPk_invbasdoc())){
//				 return;
//			 }
////		 String condition1=" pk_invbasdoc='"+ivo.getPk_invbasdoc()+"' and  isnull(dr,0)=0";
////			List list1=(List) getDao().retrieveByClause(InvbasdocVO.class, condition1);	
////			if(list1==null || list1.size()==0){
////				return;
////			}else{
////				throw new BusinessException(" �ô�������ݿ����Ѿ����� ");
////			}
//		//ΨһУ������pk_corp����
//			BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"pk_invbasdoc","pk_corp"}, " �ô�������ݿ����Ѿ����� ");
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
















