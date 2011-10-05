package nc.vo.wl.pub;
import nc.vo.scm.sourcebill.LightBillVO;
/**
 * ���������β��ҽӿڡ�
 * �������ڣ�(2004-6-14 13:08:17)
 * @author�����ھ�1
 */
public interface IBillDataFinder2 {
/*
* ����:���ݵ�ǰ�ĵ���ID,��������,���ָ�����͵ĺ�������.
* ����:LightBillVO[],��������VO����,����Ҫ��дLightBillVO��ID,TYPE,CODE��������.
* ����TYPE���Ծ���forwardBillTYPE�Ĳ���ֵ
* ����:
* 1.String curBillType :��ǰ��������
* 2.String curBillID:��ǰ����ID
* 3.String forwardBillType:�������ݵ�����
*
*/
public LightBillVO[] getForwardBills(
	String curBillType,
	String curBillID,
	String forwardBillType);
/*
 * ����:���ݵ�ǰ�ĵ���ID,��������,������е���Դ����
 * ����:LightBillVO[],��Դ����VO����,����Ҫ��дLightBillVO��ID,TYPE,CODE��������.
 * ����:
 * 1.String curBillType :��ǰ��������
 * 2.String curBillID:��ǰ����ID
 *
 */
public LightBillVO[] getSourceBills(String curBillType,String curBillID);
}
