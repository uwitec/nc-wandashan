package nc.ui.zb.gen;

import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.zb.entry.ZbEntryHelper;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.po.OrderHeaderVO;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.gen.GenOrderVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

//zhf  �ƻ����� ǰ��̨���� ������    
public class GenrationOrderHealper {

	private static String bo = "nc.bs.zb.gen.GenOrderBO";
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ƻ�����  ��ѯ�¼�������ƻ�
	 * 2010-11-24����10:27:12
	 * @param whereSql
	 * @param cl
	 * @param iplantype
	 * @return
	 * @throws Exception
	 */
	public static GenOrderVO[] queryDatas(String whereSql,ClientLink cl) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{whereSql,cl};
		Object o = LongTimeTask.callRemoteService("pu",bo, "queryDataForOrder", ParameterTypes, ParameterValues, 2);
		return o==null?null:(GenOrderVO[])o;
	}
	
	public static OrderVO dealVO(List<GenOrderVO> lseldata) throws Exception{
		
		int size= lseldata.size();
		
		OrderVO vo = new OrderVO();
		OrderHeaderVO head = new OrderHeaderVO();
		OrderItemVO[] body = new OrderItemVO[size];
		
		head.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// ��˾
		head.setDorderdate(ClientEnvironment.getInstance().getDate());// ��������
		head.setCpurorganization(PuPubVO.getString_TrimZeroLenAsNull(ZbEntryHelper.getpk_calbody(ClientEnvironment.getInstance().getCorporation().getPk_corp())));// �ɹ���֯
		String s =PuPubVO.getString_TrimZeroLenAsNull(lseldata.get(0).getCcustbasid());
		GenOrderVO gen = lseldata.get(0);
		//��ʱ��Ӧ�̶�Ӧ��  ��ʽ��Ӧ��id�������
		Object ven=isExitCcustid(gen);
		Object[] os =null;
		if(ven !=null)
			os=(Object[])ven;
		if(s.equalsIgnoreCase("null")|| s==null){
			if(os!=null&&os.length>0){
				head.setCvendorbaseid(PuPubVO.getString_TrimZeroLenAsNull(os[0]));// ��Ӧ�̻���id
				head.setCvendormangid(PuPubVO.getString_TrimZeroLenAsNull(os[1]));// ��Ӧ�̹���id
			}
		}else{
			head.setCvendorbaseid(lseldata.get(0).getCcustbasid());// ��Ӧ�̻���id
			head.setCvendormangid(lseldata.get(0).getCcustmanid());// ��Ӧ�̹���id
		}
		head.setCdeptid(null);// �ɹ����� 
		head.setForderstatus(new Integer(0));//����̬
		
		head.setCemployeeid(null);// �ɹ���Ա
		head.setCoperator(ClientEnvironment.getInstance().getUser().getPrimaryKey());// �Ƶ���
		head.setTmaketime(ClientEnvironment.getServerTime()); // �Ƶ�ʱ��
		head.setNversion(1);// �汾��Ϣ
		head.setVordercode(HYPubBO_Client.getBillNo(ScmConst.PO_Order,ClientEnvironment.getInstance().getCorporation().getPk_corp(),null, null));
		head.setNexchangeotobrate(new UFDouble(1.00));// �۱�����
		head.setBislatest(UFBoolean.TRUE);//�Ƿ�������
		
		Object o = ZbEntryHelper.getnOrderTaxRate(lseldata.get(0).getCcustbasid());
		if(PuPubVO.getUFDouble_NullAsZero(o).equals(UFDouble.ZERO_DBL))
			throw new BusinessException("��Ӧ��˰��Ϊ��,��ά��˰��");
		for(int i=0;i<size;i++){
			gen =lseldata.get(i);
			OrderItemVO item = new OrderItemVO();
			UFDouble  nzbnum =PuPubVO.getUFDouble_NullAsZero(gen.getNzbnum());//�б�����
			UFDouble  nre =PuPubVO.getUFDouble_NullAsZero(gen.getReserve10());//�ۼ�����
			UFDouble  norder = nzbnum.sub(nre);//��������
			item.setPk_corp(ClientEnvironment.getInstance().getCorporation().getPk_corp());// ��˾
			item.setCrowno(gen.getCrowno());// �к�
			item.setCmangid(gen.getCinvmanid());// �������ID
			item.setCbaseid(gen.getCinvbasid());// �������ID
			item.setNordernum(norder);// ��������
			item.setIdiscounttaxtype(1);// ��˰��� 1---Ӧ˰���
			item.setForderrowstatus(new Integer(0)); //����̬
			
			item.setNoriginalcurprice(gen.getNorderprice());// ��˰����
			item.setCupsourcebillid(gen.getCzbresultid()); // ��Դ����id
			item.setCupsourcebillrowid(gen.getCzbresultbid());// ��Դ������id
			item.setCupsourcebilltype(ZbPubConst.ZB_Result_BILLTYPE);// ��Դ��������
			item.setCsourcebillid(gen.getCupsourcebillid());
			item.setCsourcerowid(gen.getCupsourcebillrowid());
			item.setCsourcebilltype(gen.getCupsourcebilltype());			
			
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
			body[i] = item;
		}
		ZbPubTool.setVOsRowNoByRule(body,"crowno");//�����к�
		vo.setParentVO(head);
		vo.setChildrenVO(body);
		return vo;
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
	public  static Object isExitCcustid(GenOrderVO headvo) throws Exception{
	    
		Class[] ParameterTypes = new Class[]{GenOrderVO.class};
		Object[] ParameterValues = new Object[]{headvo};
		Object o =LongTimeTask.callRemoteService("pu",bo, "isExitCcustid", ParameterTypes, ParameterValues, 2);
		return o;
	}
	
}
