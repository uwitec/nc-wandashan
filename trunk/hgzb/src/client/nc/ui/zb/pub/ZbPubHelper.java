package nc.ui.zb.pub;

import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.util.SQLHelper;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;


public class ZbPubHelper {

	private static String bo = "nc.bs.zb.pub.ZbPubBO";
	private static Map<String, Object[]> logInfor = null;
	private static Map<String,Object[]> getLogInforMap(){
		if(logInfor == null){
			logInfor = new HashMap<String,Object[]>();
		}
		return logInfor;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����Ӧ�̵��������ֶ� �����û�  ��ʱ��Ӧ��Ҳ�����ֶι���ϵͳ�û�
	 * 2011-5-11����04:17:20
	 * @param sLogUserid
	 * @return
	 * @throws Exception
	 */
	public static String getCvendoridByLogUser(String sLogUserid) throws Exception{
		//		return sLogUserid;
//		return "0001A1100000000054K9";//�׶�����

		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{sLogUserid};
		return (String)LongTimeTask.callRemoteService("pu",bo, "getCvendoridByLogUser", ParameterTypes, ParameterValues, 2);
	
	}

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ����ȡ������Ϣ  �е�ǰ�û���������
	 * 2011-06-11����01:50:25
	 * @param sLogCorp
	 * @param sLogUser
	 * @return
	 * @throws BusinessException
	 */
	public static Object[] getLogInfor(String sLogCorp,String sLogUser) throws Exception{
		String key = ZbPubTool.getString_NullAsTrimZeroLen(sLogCorp)+ZbPubTool.getString_NullAsTrimZeroLen(sLogUser);
		if(getLogInforMap().containsKey(key)){
			return getLogInforMap().get(key);
		}
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{sLogCorp,sLogUser};
		Object o = LongTimeTask.callRemoteService("pu",bo, "getLogInfor", ParameterTypes, ParameterValues, 2);
		if(o == null)
			return null;
		Object[] infor = (Object[])o;
		getLogInforMap().put(key, infor);
		return infor;
	}
	
}
