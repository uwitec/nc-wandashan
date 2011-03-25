package nc.bs.wl.pub;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.wl.pub.LoginInforVO;

public class WdsWlPubBO {
	
	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½�û� ��ȡ�û��� ����Ϣ �����ֿ�  ������λ���ִ���Ա�� �ȵ�
	 * @ʱ�䣺2011-3-23����05:02:45
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  LoginInforVO getLogInfor(String userid)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select cuserid loguser,"); //��Աid
		sql.append(" pk_stordoc whid,");// �ֿ�id
		sql.append(" pk_cargdoc spaceid,");//��λ��Ϣ
		sql.append(" istepi bistp,");//�Ƿ�����
		sql.append(" st_type type ");//��Ա����
		sql.append(" from tb_stockstaff");
		sql.append(" where isnull(dr,0)=0 ");
		sql.append(" and  cuserid='" + userid +"'");
	    LoginInforVO lif=(LoginInforVO)getDao().executeQuery(sql.toString(),new BeanProcessor(LoginInforVO.class));
		return lif;
	}
	
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ  ��ȡָ���ֿ�Ļ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:03
	 * @param cwhid
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByWhid(String cwhid) throws Exception{
		String sql = "select pk_cargdoc from bd_cargdoc where pk_stordoc = '"+cwhid+"'";
		Object o = getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYROCESSOR);
		return (String[])o;
	}
	
    /**
     * 
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ ����ָ���ֿ��ȡ������Ϣ
     * @ʱ�䣺2011-3-23����05:12:32
     * @param cwhid
     * @return
     * @throws Exception
     */
	public  String[] getCustomManIDByWhid(String cwhid) throws Exception{
		//�ֿ�Ϳ�����һ�Զ�Ĺ�ϵ
	
		String sql="select pk_cubsdoc  from   bd_stordoc join  tb_storcubasdoc on bd_stordoc.pk_stordoc=tb_storcubasdoc.pk_stordoc and  bd_stordoc.pk_stordoc='"+cwhid+"';  ";
		Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		return (String[])o;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½��Ա����Ǳ���Ա���ظ����λ������Ǳ���Ա���������ֿ����еĻ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:57
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByLogUser(String userid) throws Exception{
		
	    String sql="select  st_type  from   tb_stockstaff   where cuserid='"+userid+"' ";
	    Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
	    if(o !=null){
	    	Integer i=(Integer)o;
	    	if(i.intValue()==0){
	    		String sql1="select pk_cargdoc from tb_stockstaff where cuserid='"+userid+"'";
	    		Object o1=getDao().executeQuery(sql1, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
	    		return (String[])o1;
	    	}else{
	    		String sql2="select bd.pk_cargdoc  from  tb_stockstaff tb join bd_cargdoc bd  on tb.pk_stordoc=bd.pk_stordoc and tb.cuserid='"+userid+"'";
	    		Object o2=getDao().executeQuery(sql2, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
	    		
	    		return (String[])o2;
	    		
	    	}
	    	
	    }
	    
		return null;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½��Ա��ȡ
	 * @ʱ�䣺2011-3-23����05:13:04
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getCustomManIDByLogUser(String userid) throws Exception{
		return null;
	}

}
