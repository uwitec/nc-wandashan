package nc.ui.wl.pub;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;

public class LoginInforHelper {	
	
	private static String bo = "nc.bs.wl.pub.WdsWlPubBO";
	
	private static LoginInforVO m_loginInfor = null;
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据当前登陆用户 获取用户的 绑定信息 隶属仓库  隶属货位（仓储人员） 等等
	 * @时间：2011-3-23下午05:02:45
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public static LoginInforVO getLogInfor(String userid)
			throws Exception {
		if (m_loginInfor == null
				|| PuPubVO.getString_TrimZeroLenAsNull(m_loginInfor
						.getLoguser()) == null
				|| !m_loginInfor.getLoguser().equalsIgnoreCase(userid)) {
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{userid};
			Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getLogInfor", ParameterTypes, ParameterValues, 2);
			
			m_loginInfor = (LoginInforVO)os;
		}
		return m_loginInfor;
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
	public static String[] getSpaceByWhid(String cwhid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)==null)
			return null;
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{cwhid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getSpaceByWhid", ParameterTypes, ParameterValues, 2);

		return (String[])os;
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
	public static String[] getCustomManIDByWhid(String cwhid) throws Exception{
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
	public static String[] getSpaceByLogUser(String userid) throws Exception{
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
	public static String[] getCustomManIDByLogUser(String userid) throws Exception{
		return null;
	}
	
	

}
