package nc.vo.wl.pub;
import nc.vo.scm.sourcebill.LightBillVO;
/**
 * ���鵥�ݲ������ӿ�
 * @author mlr
 *
 */
public interface IBillFinder2 {
	/**
	 * ���ؾ���ĵ������ݲ�������
	 * �������ڣ�(2004-6-21 20:02:15)
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception �쳣˵����
	 */
	IBillDataFinder2 createBillDataFinder(String billType) throws java.lang.Exception;
	/**
	 * ����������Դ��Ӧ�����е�������
	 * �������ڣ�(2004-6-21 19:56:37)
	 * @return java.lang.String[]
	 */
	String[] getAllBillType();
	/**
	 * ���ص��ݹ�ϵ��VO��
	 * �������ڣ�(2004-6-21 19:58:46)
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception �쳣˵����
	 */
	LightBillVO queryBillGraph(String id, String type,String code) throws java.lang.Exception;

}
