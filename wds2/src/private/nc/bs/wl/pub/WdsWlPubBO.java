package nc.bs.wl.pub;

import java.util.HashMap;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.LoginInforVO;

public class WdsWlPubBO {
	
	private  static BaseDAO m_dao = null;
	private static  BaseDAO getDao(){
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
	
	public String[] getInvBasdocIDsBySpaceID(String cspaceid) throws BusinessException{
		String sql = "select pk_invbasdoc from tb_spacegoods where isnull(dr,0)=0 and pk_cargdoc = '"+cspaceid+"'";
		List ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
		if(ldata == null||ldata.size()==0)
			return null;
		return (String[])ldata.toArray(new String[0]);
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
	
	private static java.util.Map<String,String[]> trayInfor = null;
	private static java.util.Map<String, Integer> trayVolumnInfor = null;
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取指定货位存放指定存货的全部托盘
	 * @时间：2011-3-31下午04:34:27
	 * @param cspaceid
	 * @param cinvbasid
	 * @return
	 * @throws BusinessException
	 */
	public static String[] getTrayInfor(String cspaceid,String cinvbasid) throws BusinessException{
		String key = cspaceid+cinvbasid;
		String[] trays = null;
		List ldata = null;
		if(trayInfor == null||!trayInfor.containsKey(key)){
			String sql = "select cdt_pk from bd_cargdoc_tray where cdt_invbasdoc='"
				+ cinvbasid
				+ "' and cdt_traystatus=0 and isnull(dr,0)=0 and  pk_cargdoc='"
				+ cspaceid + "' ";
			ldata = (List)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
			
			if(ldata == null || ldata.size() == 0)
				return null;
			trays = ((List<String>)ldata).toArray(new String[0]);
//				throw new BusinessException("存货不属于人员所在仓库货位");
			if(trayInfor == null)
				trayInfor = new HashMap<String, String[]>();
			trayInfor.put(key, trays);
		}
		return trayInfor.get(key);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 获取存货托盘容量
	 * @时间：2011-3-31下午04:59:46
	 * @param cinvbasid
	 * @return
	 * @throws BusinessException
	 */
	public static Integer getTrayVolumeByInvbasid(String cinvbasid) throws BusinessException{
		int volumn = 0;
		if(trayVolumnInfor == null||!trayVolumnInfor.containsKey(cinvbasid)){
			String sql = "select def20 from bd_invbasdoc where pk_invbasdoc='"+cinvbasid+"'";
			volumn = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), 1);
			if(volumn == -1)
				throw new BusinessException("该存货托盘容量未定义");
			if(trayVolumnInfor == null)
				trayVolumnInfor = new HashMap<String, Integer>();
			trayVolumnInfor.put(cinvbasid, volumn);
		}
		return trayVolumnInfor.get(cinvbasid);
	}

}
