package nc.ui.wl.pub;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wl.pub.WdsWlPubConst;
/**
  ���ղ�ѯ �����������Ĵ�����ʱ���ѯ(��Ҫ���in�������)
*/
public class RefBillPubHelper {
	
	private static String bo = "nc.bs.wl.pub.RefBillPubBO";
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ղ�ѯ ��ѯ��ͷ����
	 * @ʱ�䣺2011-7-5����09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj ����in�еĲ���
	 * @return
	 * @throws Exception
	 */
	public static nc.vo.pub.CircularlyAccessibleValueObject[] queryHeadAllData(String billType,String businessType,String tmpWhere,Object oUserObj) throws Exception {
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Object.class};
		Object[] ParameterValues = new Object[]{billType,businessType,tmpWhere,oUserObj};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryByClassCondtion", ParameterTypes, ParameterValues, 2);
		return (CircularlyAccessibleValueObject[]) os;
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ղ�ѯ ��ѯ��������
	 * @ʱ�䣺2011-7-5����09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj ����in�еĲ���
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] queryBodyAllData(String billType, String id, String bodyCondition,Object oUserObj)throws Exception {
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Object.class};
		Object[] ParameterValues = new Object[]{billType,id,bodyCondition,oUserObj};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "queryBodyByClassCondtion", ParameterTypes, ParameterValues, 2);
		return (CircularlyAccessibleValueObject[]) os;
	}

}
