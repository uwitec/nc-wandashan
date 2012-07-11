package nc.ui.wl.pub;
import java.util.HashMap;
import java.util.Map;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
public class LoginInforHelper {		
	private static String bo = "nc.bs.wl.pub.WdsWlPubBO";	
	private  LoginInforVO m_loginInfor = null;	
	/**
	 * @author yf
	 * 当前登陆信息 采用pk_corp过滤条件 原bo中重载登陆方法 双字符串参数userid，pk_corp
	 */
	public  LoginInforVO getLogInfor(String userid)
			throws Exception {
		if (m_loginInfor == null
				|| PuPubVO.getString_TrimZeroLenAsNull(m_loginInfor
						.getLoguser()) == null
				|| !m_loginInfor.getLoguser().equalsIgnoreCase(userid)) {
			//当前登陆公司
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{userid};
			Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getLogInfor", ParameterTypes, ParameterValues, 2);
			if ( os == null){	
				throw new BusinessException("当前登录人员没有绑定仓库");
			}
			m_loginInfor = (LoginInforVO)os;
		}
		return m_loginInfor;
	}
	
	public  String getWhidByUser(String userid) throws Exception{
		return getLogInfor(userid).getWhid();
	}
	
	public  int getITypeByUser(String userid) throws Exception{
		return getLogInfor(userid).getType();
	}
	
	public  String getCwhid(String userid) throws Exception{
		return getLogInfor(userid).getWhid();
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
	public  String[] getCustomManIDByWhid(String cwhid) throws Exception{
		return null;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据当前登陆人员如果是保管员返回负责货位
	 *  如果不是保管员返回隶属仓库所有的货位信息
	 * @时间：2011-3-23下午05:12:57
	 * @param userids
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByLogUser(String userid) throws Exception{
		LoginInforVO infor = getLogInfor(userid);
	//	if(infor.getType()==0)//如果是仓管员
			return new String[]{infor.getSpaceid()};
		//return getSpaceByWhid(infor.getWhid());
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
	public  String getSpaceByLogUserForStore(String userid) throws Exception{
		LoginInforVO infor = getLogInfor(userid);
		
		if(infor==null )
			return null;
			return infor.getSpaceid();
		
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 通过货位id获取该货位关联的存货管理id
	 * @时间：2011-3-31上午10:24:28
	 * @param cspaceid
	 * @return
	 * @throws Exception
	 */
	public  String[] getInvBasdocIDsBySpaceID(String cspaceid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cspaceid)==null)
			return null;
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{cspaceid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getInvBasdocIDsBySpaceID", ParameterTypes, ParameterValues, 2);

		return (String[])os;	
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 通过货位id获取该货位关联的存货管理id
	 * @时间：2011-3-31上午10:24:28
	 * @param cspaceid
	 * @return
	 * @throws Exception
	 */
	public  String[] getInvbasdocIDsBySpaceID(String cspaceid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cspaceid)==null)
			return null;
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{cspaceid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getInvbasdocIDsBySpaceID", ParameterTypes, ParameterValues, 2);

		return (String[])os;	
	}
	
	private  Map<String,String[]> invInfor = null;
	/**
	 * 
	 * @作者：
	 * @说明：完达山物流项目 
	 * 查询出来的是存货管理id,因为方法已经引用很多，名称不再更改
	 * @时间：2011-4-23下午08:10:59
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getInvBasDocIDsByUserID(String userid) throws Exception{
		if(invInfor == null)
			invInfor = new HashMap<String, String[]>();
			if(invInfor.containsKey(userid))
				return invInfor.get(userid);
			LoginInforVO infor = getLogInfor(userid);
//			if(infor.getType()!=0){   zhf   注释掉    不同该字段 控制操作权限 走系统权限分配 解决 权限问题
//				throw new BusinessException("当前登陆人员不是保管员");
//			}
			invInfor.put(userid, getInvBasdocIDsBySpaceID(infor.getSpaceid())) ;
			return invInfor.get(userid);
	}
	
	private  Map<String,String[]> invbasInfor = null;
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目  查询出来的是存货基本id
	 * @时间：2011-4-23下午08:12:16
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String[] getInvbasDocIDsByUserID(String userid) throws Exception{
		if(invbasInfor == null)
			invbasInfor = new HashMap<String, String[]>();
			if(invbasInfor.containsKey(userid))
				return invbasInfor.get(userid);
			LoginInforVO infor = getLogInfor(userid);
//			if(infor.getType()!=0){
//				throw new BusinessException("当前登陆人员不是保管员");
//			}
			invbasInfor.put(userid, getInvbasdocIDsBySpaceID(infor.getSpaceid())) ;
			return invbasInfor.get(userid);
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
	
	private  Map<String,String[]> trayInfor = null;
	
	
	private  Map<String, Integer> trayVolumnInfor = null; //
	
	
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
	public  String[] getTrayInfor(String cspaceid,String cinvbasid) throws Exception{
		String key = cspaceid+cinvbasid;
		String[] trays = null;
		if(trayInfor == null||!trayInfor.containsKey(key)){
			Class[] ParameterTypes = new Class[]{String.class,String.class};
			Object[] ParameterValues = new Object[]{cspaceid,cinvbasid};
			trays = (String[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getTrayInfor", ParameterTypes, ParameterValues, 2);
			
			if(trays == null)
				return null;
			
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
	public  Integer getTrayVolumeByInvbasid(String cinvbasid) throws Exception{
		int volumn = 0;
		if(trayVolumnInfor == null||!trayVolumnInfor.containsKey(cinvbasid)){
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{cinvbasid};
			volumn = (Integer)LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getTrayVolumeByInvbasid", ParameterTypes, ParameterValues, 2);
			
			if(trayVolumnInfor == null)
				trayVolumnInfor = new HashMap<String, Integer>();
			trayVolumnInfor.put(cinvbasid, volumn);
		}
		return trayVolumnInfor.get(cinvbasid);
	}

}
