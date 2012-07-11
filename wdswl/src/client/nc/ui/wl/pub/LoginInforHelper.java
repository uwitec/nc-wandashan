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
	 * ��ǰ��½��Ϣ ����pk_corp�������� ԭbo�����ص�½���� ˫�ַ�������userid��pk_corp
	 */
	public  LoginInforVO getLogInfor(String userid)
			throws Exception {
		if (m_loginInfor == null
				|| PuPubVO.getString_TrimZeroLenAsNull(m_loginInfor
						.getLoguser()) == null
				|| !m_loginInfor.getLoguser().equalsIgnoreCase(userid)) {
			//��ǰ��½��˾
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			
			Class[] ParameterTypes = new Class[]{String.class};
			Object[] ParameterValues = new Object[]{userid};
			Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "getLogInfor", ParameterTypes, ParameterValues, 2);
			if ( os == null){	
				throw new BusinessException("��ǰ��¼��Աû�а󶨲ֿ�");
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ  ��ȡָ���ֿ�Ļ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:03
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
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ ����ָ���ֿ��ȡ������Ϣ
     * @ʱ�䣺2011-3-23����05:12:32
     * @param cwhid
     * @return
     * @throws Exception
     */
	public  String[] getCustomManIDByWhid(String cwhid) throws Exception{
		return null;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½��Ա����Ǳ���Ա���ظ����λ
	 *  ������Ǳ���Ա���������ֿ����еĻ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:57
	 * @param userids
	 * @return
	 * @throws Exception
	 */
	public  String[] getSpaceByLogUser(String userid) throws Exception{
		LoginInforVO infor = getLogInfor(userid);
	//	if(infor.getType()==0)//����ǲֹ�Ա
			return new String[]{infor.getSpaceid()};
		//return getSpaceByWhid(infor.getWhid());
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
	public  String getSpaceByLogUserForStore(String userid) throws Exception{
		LoginInforVO infor = getLogInfor(userid);
		
		if(infor==null )
			return null;
			return infor.getSpaceid();
		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ͨ����λid��ȡ�û�λ�����Ĵ������id
	 * @ʱ�䣺2011-3-31����10:24:28
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ͨ����λid��ȡ�û�λ�����Ĵ������id
	 * @ʱ�䣺2011-3-31����10:24:28
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
	 * @���ߣ�
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ�������Ǵ������id,��Ϊ�����Ѿ����úܶ࣬���Ʋ��ٸ���
	 * @ʱ�䣺2011-4-23����08:10:59
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
//			if(infor.getType()!=0){   zhf   ע�͵�    ��ͬ���ֶ� ���Ʋ���Ȩ�� ��ϵͳȨ�޷��� ��� Ȩ������
//				throw new BusinessException("��ǰ��½��Ա���Ǳ���Ա");
//			}
			invInfor.put(userid, getInvBasdocIDsBySpaceID(infor.getSpaceid())) ;
			return invInfor.get(userid);
	}
	
	private  Map<String,String[]> invbasInfor = null;
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ  ��ѯ�������Ǵ������id
	 * @ʱ�䣺2011-4-23����08:12:16
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
//				throw new BusinessException("��ǰ��½��Ա���Ǳ���Ա");
//			}
			invbasInfor.put(userid, getInvbasdocIDsBySpaceID(infor.getSpaceid())) ;
			return invbasInfor.get(userid);
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
	
	private  Map<String,String[]> trayInfor = null;
	
	
	private  Map<String, Integer> trayVolumnInfor = null; //
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡָ����λ���ָ�������ȫ������
	 * @ʱ�䣺2011-3-31����04:34:27
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡ�����������
	 * @ʱ�䣺2011-3-31����04:59:46
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
