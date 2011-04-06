package nc.itf.hg.pu.pub;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public interface IFundCheck {

	// �����뷽 �ʽ� ����
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��Ԥ�� 2011-2-2����04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid ����ʱ �������ʵĸ�����˾����
	 * @param sdeptid ����˾Ϊ��ʱ  ����ͻ�����id  ��������
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  ����  billtype ����  �ҳ��޸�ǰ�Ľ��
	 */
	public void useFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * Ԥ��ʱ�۳�Ԥ����ר��ƻ���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-3-21����03:07:53
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @param pk
	 * @param billtype
	 * @param planotherbmny
	 * @throws BusinessException
	 */
	public void useSpeacialFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny,String pk,String billtype,Object[][] o)
			throws BusinessException;
	// public void reUseFund_Before(String pk_plan,int ifundtype,String scorpid,
	// String sdeptid, UFDate uDate,UFDouble nmny) throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ʵ�� 2011-2-2����04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid����ʱ �������ʵĸ�����˾����
	 * @param sdeptid����˾Ϊ��ʱ  ����ͻ�����id  ��������
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  ����  billtype ����  �ҳ��޸�ǰ�Ľ��
	 */
	public void useFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� 2011-4-2����11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� 2011-4-2����11:47:47
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  ����  billtype ����  �ҳ��޸�ǰ�Ľ��
	 */
	public void reUseFund_before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException;

	public void checkFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У���Ƿ���Խ���ʵ�� 2011-2-3����04:06:57
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void checkFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;
	
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��ʵ�� 2011-2-2����04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param dept   ���ڲ��ϳ��ⵥ
	 * @param cust
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void useFundMater(String pk_plan, int ifundtype, String dept,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-4-2����11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param dept ���ڲ��ϳ��ⵥ
	 * @param cust
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFundMater(String pk_plan, int ifundtype, String dept,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhw  ���ڵ�������
	 * @˵�������׸ڿ�ҵ��ʵ�� 2011-2-2����04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid����ʱ �������ʵĸ�����˾����
	 * @param sdeptid����˾Ϊ��ʱ  ����ͻ�����id  ��������
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  ����  billtype ����  �ҳ��޸�ǰ�Ľ��
	 */
	public void useFund1(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype,UFDouble nmny1)
			throws BusinessException;

	/**
	 * 
	 * @author zhw  ���ڵ�������
	 * @˵�������׸ڿ�ҵ�� 2011-4-2����11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFund1(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,UFDouble nmny1)
			throws BusinessException;

}
