
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ��ѯ�����ε��ݵ�ʵ����
 * @author mlr
 *
 */
public class LinkQueryFinder extends AbstractBillFinder2{
	public LinkQueryFinder() {
		super();
	}
	/**
	 * ע���Ӧ�������͵Ĳ�����  ��������if else ������
	 * ��ͬ�ĵ������ʹ�����Ӧ�����������͵Ĳ�����
	 * ���� LinkQueryFinder ��� queryBillGraph(id,type)
	 * ����� ��Ӧ��������ע������ݲ����� getSourceBills(curBillType,curBillType)
	 * ���� ������Դ����
	 *   ���� ��Ӧ��������ע������ݲ�������  getForwardBills(curBillType,curBillType,forwardBillType)
	 *   forwardBillTypeΪ���ε�������
	 *   ���������ε��� 
	 *   
	 *   
	 * ����÷���������д�� ����Ĭ�ϵĲ�����
	 */
	public IBillDataFinder2 createBillDataFinder(String billType) throws Exception {
		return super.createBillDataFinder(billType);
	}
	/**
	 * ע�����εĵ������� 
	 */
	public String[] getAllBillType() {
		String type = getCurrentvo().getType();//��ǰ��������
		//�������õ�ǰ�������� ����if else ����ж� ��ע�ᵱǰ��������
		//�����ε�������		
		return null;
	}

}
