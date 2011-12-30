package nc.bs.wds.dm.storebing;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
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
 * �������� ��̨У���� 

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
//        if(vo.getParentVO().getPrimaryKey()==null || vo.getParentVO().getPrimaryKey().trim().length()==0){
//        	return;
//        }
        BdStordocVO ivo = (BdStordocVO) vo.getParentVO();  //pk_wds_corpseal
		String primary= PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		String pk_stordoc=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_stordoc());
		if(pk_stordoc == null){
			return ;
		}
		String pk_sendareacl=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPk_sendareacl());
		if(pk_sendareacl == null){
			return ;
		}
		//��������� �޸ı��� ����У��
		if(primary == null){//����У��
			String condition = " pk_sendareacl='" + pk_sendareacl+"' and pk_stordoc='"+pk_stordoc+"' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(BdStordocVO.class,condition);
	        if(list != null &&  list.size() >0){
	        	throw new BusinessException("ͬһ�ֿ�ͬһ���������Ѿ�����");
	        }
		}else{//�޸�У�� 
			String condition = " pk_sendareacl='" + pk_sendareacl+"' and pk_stordoc='"+pk_stordoc+"' and  isnull(dr,0)=0";
	        List list = (List) getDao().retrieveByClause(BdStordocVO.class,condition);
	        if(list != null &&  list.size() >1){
	        	throw new BusinessException("ͬһ�ֿ�ͬһ���������Ѿ�����");
	        }
	        if(list != null &&  list.size() == 1){
	        	BdStordocVO queryVO = (BdStordocVO)list.get(0);
	        	if(!primary.equalsIgnoreCase(queryVO.getPrimaryKey())){
		        	throw new BusinessException("�÷��������Ѿ�����");

	        	}
	        }			
		}
        //����У�飺��ͬ�ֿ��²����ظ�;ͬһ�ֿ��±�����̺ͷֲ��ǲ�ͬ�ظ��ģ�ǰ̨У�飩
        SuperVO[] vos=(SuperVO[]) vo.getChildrenVO();
        if( vos==null || vos.length==0 ){
        	return;
        }
        int size=vos.length;
        for(int i=0;i<size;i++){
        	HYPubBO pubvo = new HYPubBO();
            String 	pk_cumandoc=(String) vos[i].getAttributeValue("pk_cumandoc");
            if(pk_cumandoc!=null&&pk_cumandoc.length()>0){
                String strWhere=" select pk_wds_storecust_h from tb_storcubasdoc where isnull(dr,0)=0 and pk_cumandoc='"+pk_cumandoc+"'";
                BdStordocVO[] heads= ( BdStordocVO[]) pubvo.queryByCondition(BdStordocVO.class, " pk_wds_storecust_h in( "+strWhere+")");
                if(heads !=null){
                	for(BdStordocVO head:heads){
                		String stordocid = head.getPk_stordoc();
                		if(stordocid == null || "".equalsIgnoreCase(pk_stordoc)){
                			stordocid ="";
                		}
                		if(!stordocid.equalsIgnoreCase(pk_stordoc)){
                			throw new BusinessException("�ͻ�["+getCustName(pk_cumandoc)+"],��[�ֿ�="+getCustCode(stordocid)+",������="+geAreaname(head.getPk_sendareacl())+"]�Ѿ���");
                		}
                	}
                }
             }
//            String 	pk_stordoc1=(String) vos[i].getAttributeValue("pk_stordoc1");
//            if(pk_stordoc1!=null&&pk_stordoc1.length()>0){
//            	 String strWhere=" select pk_wds_storecust_h from tb_storcubasdoc where isnull(dr,0)=0 and pk_stordoc1='"+pk_stordoc1+"'";
//                 BdStordocVO[] heads= ( BdStordocVO[]) pubvo.queryByCondition(BdStordocVO.class, " pk_wds_storecust_h in( "+strWhere+")");
//                 if(heads !=null){
//                 	for(BdStordocVO head:heads){
//                 		String stordocid = head.getPk_stordoc();
//                 		stordocid ="";
//                 		if(stordocid == null || "".equalsIgnoreCase(pk_stordoc)){
//                 			stordocid ="";
//                 		}
//                 		if(!stordocid.equalsIgnoreCase(pk_stordoc)){
//                 			throw new BusinessException("�ֲ��������ֲ��Ѿ���");
//                 		}
//                 	}
//                 }           
//              }
        }		
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��ÿ�������
	 * @ʱ�䣺2011-12-23����02:21:59
	 * @param pk_cusmandoc
	 * @return
	 * @throws DAOException 
	 */
	private String getCustName(String pk_cumandoc)throws DAOException{
		String object  ="";
		String sql =" select custname from bd_cubasdoc where isnull(dr,0)=0 and pk_cubasdoc =(" +
				"select pk_cubasdoc from bd_cumandoc where isnull(dr,0)=0 and pk_cumandoc='"+pk_cumandoc+"')";
		object = (String)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
		return object;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��òֿ����� 
	 * @ʱ�䣺2011-12-23����02:21:59
	 * @param pk_cusmandoc
	 * @return
	 * @throws DAOException 
	 */
	private String getCustCode(String pk_stordoc)throws DAOException{
		String object  ="";
		String sql =" select storname from bd_stordoc where isnull(dr,0)=0 and pk_stordoc='"+pk_stordoc+"'";
		object = (String)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
		return object;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��÷����������� 
	 * @ʱ�䣺2011-12-23����02:21:59
	 * @param pk_cusmandoc
	 * @return
	 * @throws DAOException 
	 */
	private String geAreaname(String pk_areacl)throws DAOException{
		String object  ="";
		String sql =" select areaclname from bd_areacl where isnull(dr,0)=0 and pk_areacl='"+pk_areacl+"'";
		object = (String)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
		return object;
	}
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
