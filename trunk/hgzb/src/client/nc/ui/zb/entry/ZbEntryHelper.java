package nc.ui.zb.entry;

import java.util.ArrayList;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.entry.ZbResultHeadVO;
import nc.vo.zb.pub.ZbPubConst;

public class ZbEntryHelper {
	
	private final static String ZbEntryBo = "nc.bs.zb.entry.ZbEntryBo";
	
	/**
	 * ��ȡ��Ӧ�̵�˰��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static Object getnOrderTaxRate(String custbasid) throws Exception{
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{custbasid};
		Object o = LongTimeTask.callRemoteService("pu", ZbEntryBo, "getnOrderTaxRate", ParameterTypes, ParameterValues, 2);
		return o;
		
	}
	
	/**
	 * ��ȡ�ɹ���֯
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static Object getpk_calbody(String ccorpid) throws Exception{
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{ccorpid};
		Object o  = LongTimeTask.callRemoteService("pu", ZbEntryBo, "getpk_calbody", ParameterTypes, ParameterValues, 2);
		
		return o;
		
	}
	
	/**
	 * У���Ƿ�������ε���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static void isExitDownBill(ZbResultBodyVO bodyvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{ZbResultBodyVO.class};
		Object[] ParameterValues = new Object[]{bodyvo};
		LongTimeTask.callRemoteService("pu",ZbEntryBo, "isExitDownBill", ParameterTypes, ParameterValues, 2);
		
	}
	
	/**
	 * ��ʱ��Ӧ�̶�Ӧ��  ��ʽ��Ӧ��id�������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public  static Object isExitCcustid(ZbResultHeadVO headvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{ZbResultHeadVO.class};
		Object[] ParameterValues = new Object[]{headvo};
		Object o =LongTimeTask.callRemoteService("pu",ZbEntryBo, "isExitCcustid", ParameterTypes, ParameterValues, 2);
		return o;
	}
	
	/**
	 * ��ʱ��Ӧ�̶�Ӧ��  ��ʽ��Ӧ��id�������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public  static void isComplete(String str) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{str};
		LongTimeTask.callRemoteService("pu",ZbEntryBo, "isComplete", ParameterTypes, ParameterValues, 2);
	}
	
	/**
	 * vo����
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-5-16����05:37:41
	 * @param al
	 * @return
	 * @throws Exception 
	 */
	public static OrderVO chgZB05TO21(HYBillVO billVO) throws Exception {
		ZbResultHeadVO headvo = (ZbResultHeadVO) billVO.getParentVO();
		
		
		//��ʱ��Ӧ�̶�Ӧ��  ��ʽ��Ӧ��id�������
		Object ven=isExitCcustid(headvo);
		Object[] os =null;
		if(ven !=null)
			os=(Object[])ven;
		
		ZbResultBodyVO[] bodyvo = (ZbResultBodyVO[]) billVO.getChildrenVO();
		int len = bodyvo.length;
		OrderVO vo = new OrderVO();
		OrderHeaderVO head = new OrderHeaderVO();
		ArrayList<OrderItemVO> al = new ArrayList<OrderItemVO>();
		head.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// ��˾
		head.setDorderdate(ClientEnvironment.getInstance().getDate());// ��������
		head.setCpurorganization(PuPubVO.getString_TrimZeroLenAsNull(getpk_calbody(ClientEnvironment.getInstance().getCorporation().getPk_corp())));// �ɹ���֯
		String s =PuPubVO.getString_TrimZeroLenAsNull(headvo.getCcustbasid());
		if(s.equalsIgnoreCase("null")|| s==null){
			if(os!=null&&os.length>0){
				head.setCvendorbaseid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));// ��Ӧ�̻���id
				head.setCvendormangid(PuPubVO.getString_TrimZeroLenAsNull(os[1]));// ��Ӧ�̹���id
			}
		}else{
			head.setCvendorbaseid(headvo.getCcustbasid());// ��Ӧ�̻���id
			head.setCvendormangid(headvo.getCcustmanid());// ��Ӧ�̹���id
		}
		head.setCdeptid(headvo.getPk_deptdoc());// �ɹ����� 
		head.setForderstatus(new Integer(0));//����̬
		head.setBislatest(UFBoolean.TRUE);//�Ƿ�������
		
		head.setCemployeeid(headvo.getVemployeeid());// �ɹ���Ա
		head.setCoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());// �Ƶ���
		head.setTmaketime(ClientEnvironment.getServerTime()); // �Ƶ�ʱ��
		head.setNversion(1);// �汾��Ϣ
		head.setVordercode(HYPubBO_Client.getBillNo(ScmConst.PO_Order,ClientEnvironment.getInstance().getCorporation().getPk_corp(),null, null));
		head.setNexchangeotobrate(new UFDouble(1.00));// �۱�����
		
		isComplete(headvo.getPrimaryKey());
		
		Object o = getnOrderTaxRate(headvo.getCcustbasid());
		if(PuPubVO.getUFDouble_NullAsZero(o).equals(UFDouble.ZERO_DBL))
			throw new BusinessException("��Ӧ��˰��Ϊ��,��ά��˰��");
		for (int i = 0; i < len; i++) {
			// У���Ƿ�������ε���
			isExitDownBill(bodyvo[i]);
			UFDouble  nzbnum =PuPubVO.getUFDouble_NullAsZero(bodyvo[i].getNzbnum());//�б�����
			UFDouble  nre =PuPubVO.getUFDouble_NullAsZero(bodyvo[i].getReserve10());//�ۼ�����
			UFDouble  norder = nzbnum.sub(nre);//��������
			if(nre.compareTo(UFDouble.ZERO_DBL)<0)
				throw new BusinessException("������������С����");
			if(norder.compareTo(UFDouble.ZERO_DBL)==0)
				continue;
			OrderItemVO item = new OrderItemVO();
			item.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// ��˾
			item.setCrowno(bodyvo[i].getCrowno());// �к�
			item.setCmangid(bodyvo[i].getCinvmanid());// �������ID
			item.setCbaseid(bodyvo[i].getCinvbasid());// �������ID
			item.setNordernum(norder);// ��������
			item.setIdiscounttaxtype(1);// ��˰��� 1---Ӧ˰���
			
			item.setNoriginalcurprice(bodyvo[i].getNorderprice());// ��˰����
			item.setCupsourcebillid(bodyvo[i].getCzbresultid()); // ��Դ����id
			item.setCupsourcebillrowid(bodyvo[i].getCzbresultbid());// ��Դ������id
			item.setCupsourcebilltype(ZbPubConst.ZB_Result_BILLTYPE);// ��Դ��������
			item.setCsourcebillid(bodyvo[i].getCupsourcebillid());
			item.setCsourcerowid(bodyvo[i].getCupsourcebillrowid());
			item.setCsourcebilltype(bodyvo[i].getCupsourcebilltype());
			item.setCcurrencytypeid(ZbPubConst.pk_currtype);// ����
			item.setPk_arrvcorp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// �ջ���˾
			item.setIisactive(new Integer(0));//�Ƿ񼤻�
			item.setNtaxrate(PuPubVO.getUFDouble_NullAsZero(o));//˰��
			
			item.setPk_invoicecorp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// ��Ʊ��˾
			item.setNexchangeotobrate(new UFDouble(1.00));// �۱�����
			
			//��˰����
			UFDouble nOrderNoTaxPrice = null;
			//˰�ʶ���
			UFDouble nOrderTaxRate = null;
			nOrderTaxRate = PuPubVO.getUFDouble_NullAsZero(item.getNtaxrate()).multiply(new UFDouble(0.01));
			nOrderNoTaxPrice = PuPubVO.getUFDouble_NullAsZero(item.getNoriginalcurprice());// ��˰����
			item.setNoriginalnetprice(nOrderNoTaxPrice);// ����˰����
			// ���=����*��˰����
			item.setNoriginalcurmny(nOrderNoTaxPrice.multiply(PuPubVO.getUFDouble_NullAsZero(item.getNordernum())));
			// ��˰����
			item.setNorgtaxprice(nOrderNoTaxPrice.add(nOrderTaxRate.multiply(nOrderNoTaxPrice)));
			// ����˰����
			item.setNorgnettaxprice(nOrderNoTaxPrice.add(nOrderTaxRate.multiply(nOrderNoTaxPrice)));
			// ˰��=���*˰�� ��Ӧ˰���
			item.setNoriginaltaxmny(nOrderTaxRate.multiply(item.getNoriginalcurmny()));
			// ��˰�ϼ�=���+˰��
			item.setNoriginaltaxpricemny(item.getNoriginalcurmny().add(item.getNoriginaltaxmny()));
			item.setForderrowstatus(new Integer(0)); //����̬
			
			al.add(item);
			
		}
		
//		if(al ==null || al.size()==0)
//			throw new BusinessException("�Ѿ����ɺ�ͬ");
		vo.setParentVO(head);
		vo.setChildrenVO(al.toArray(new OrderItemVO[0] ));
		return vo;
	}
}
