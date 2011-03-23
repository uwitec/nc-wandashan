package nc.bs.wl.pub;

import nc.bs.dao.BaseDAO;
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
		
		return null;
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
		return null;
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
