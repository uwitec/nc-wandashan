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
	 * @作者：zhf
	 * @说明：完达山物流项目 根据当前登陆用户 获取用户的 绑定信息 隶属仓库  隶属货位（仓储人员） 等等
	 * @时间：2011-3-23下午05:02:45
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  LoginInforVO getLogInfor(String userid)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select cuserid loguser,"); //人员id
		sql.append(" pk_stordoc whid,");// 仓库id
		sql.append(" pk_cargdoc spaceid,");//货位信息
		sql.append(" istepi bistp,");//是否特批
		sql.append(" st_type type ");//人员类型
		sql.append(" from tb_stockstaff");
		sql.append(" where isnull(dr,0)=0 ");
		sql.append(" and  cuserid='" + userid +"'");
	    LoginInforVO lif=(LoginInforVO)getDao().executeQuery(sql.toString(),new BeanProcessor(LoginInforVO.class));
		return lif;
	}
	
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目  获取指定仓库的货位信息
	 * @时间：2011-3-23下午05:12:03
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
     * @作者：zhf
     * @说明：完达山物流项目 根据指定仓库获取客商信息
     * @时间：2011-3-23下午05:12:32
     * @param cwhid
     * @return
     * @throws Exception
     */
	public  String[] getCustomManIDByWhid(String cwhid) throws Exception{
		//仓库和客商是一对多的关系
	
		String sql="select pk_cubsdoc  from   bd_stordoc join  tb_storcubasdoc on bd_stordoc.pk_stordoc=tb_storcubasdoc.pk_stordoc and  bd_stordoc.pk_stordoc='"+cwhid+"';  ";
		Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		return (String[])o;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据当前登陆人员如果是保管员返回负责货位如果不是保管员返回隶属仓库所有的货位信息
	 * @时间：2011-3-23下午05:12:57
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
	 * @作者：zhf
	 * @说明：完达山物流项目 根据当前登陆人员获取
	 * @时间：2011-3-23下午05:13:04
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getCustomManIDByLogUser(String userid) throws Exception{
		return null;
	}

}
