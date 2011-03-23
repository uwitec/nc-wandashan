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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݵ�ǰ��½�û� ��ȡ�û��� ����Ϣ �����ֿ�  ������λ���ִ���Ա�� �ȵ�
	 * @ʱ�䣺2011-3-23����05:02:45
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ  ��ȡָ���ֿ�Ļ�λ��Ϣ
	 * @ʱ�䣺2011-3-23����05:12:03
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
     * @���ߣ�zhf
     * @˵�������ɽ������Ŀ ����ָ���ֿ��ȡ������Ϣ
     * @ʱ�䣺2011-3-23����05:12:32
     * @param cwhid
     * @return
     * @throws Exception
     */
	public static String[] getCustomManIDByWhid(String cwhid) throws Exception{
		return null;
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
	public static String[] getSpaceByLogUser(String userid) throws Exception{
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
	public static String[] getCustomManIDByLogUser(String userid) throws Exception{
		return null;
	}
	
	

}
