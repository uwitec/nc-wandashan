package nc.itf.hg.pu.pub;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IHgInvOnHandQuery {

	//��ȡָ����λ ָ�����ڵĴ���   ά�ȣ���˾+�����֯
	public void getOnHandNum(String sLogCorp,String sCalbody,String sWareHouse,UFDate denddate,String[] invmanids) throws BusinessException;
	
	//��ȡʣ������     û�����Ĳɹ���ͬ��
	public void getRemainNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
////	��ȡ����1-N������
//	public void getLastCurrNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
//	��ȡ�����ȫ��������
	public void getLastAllNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
//	��ȡ��ǰ���1-N ������
	public void getCurrNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
}
