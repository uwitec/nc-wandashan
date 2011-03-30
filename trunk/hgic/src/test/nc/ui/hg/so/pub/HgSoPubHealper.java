package nc.ui.hg.so.pub;

import java.util.HashMap;
import java.util.Map;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class HgSoPubHealper {
	
	private static String bo = "nc.bs.hg.so.pub.HgSoPubBO";
	
	private static UFDouble naddrate = null;//���۶����Ӽ��� ���۲�������
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� ��ȡ���ۼӼ���
	 * 2012-2-21����04:40:36
	 * @return
	 */
	public static UFDouble getSaleOrderPriceAddRate(){
		if(naddrate == null){
			try{
				naddrate = SysInitBO_Client.getParaDbl(ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), HgPubConst.HG_SO_PARA_01);
			}catch(Exception e){
				e.printStackTrace();
				naddrate = UFDouble.ZERO_DBL;
			}			
		}
		return naddrate;
	}
	
	private static Map<String, UFDouble[]> m_pactPriceInfor = null;
	
	private static Map<String,UFDouble[]> getPactPriceInfor(){
		if(m_pactPriceInfor == null)
			m_pactPriceInfor = new HashMap<String, UFDouble[]>();
		return m_pactPriceInfor;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����۶���ѯ�ɹ���ͬ������
	 * 2012-2-21����02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static UFDouble[] callPurchasePactPrice(String cinvmanid,String cbatchid) throws Exception{
		UFDouble[] npriceinfor = null; 
		if(PuPubVO.getString_TrimZeroLenAsNull(cbatchid)==null||PuPubVO.getString_TrimZeroLenAsNull(cinvmanid)==null)
			return null;
		String key = cinvmanid.trim()+cbatchid.trim();
		if(getPactPriceInfor().containsKey(key)){
			return getPactPriceInfor().get(key);
		}
		
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{cinvmanid,cbatchid};
		Object o = LongTimeTask.callRemoteService("so", bo, "callPurchasePactPrice", ParameterTypes, ParameterValues, 2);
		
		if(o == null||!(o instanceof UFDouble[]))
			return null;
		
		npriceinfor = (UFDouble[])o;
		getPactPriceInfor().put(key, npriceinfor);
		return npriceinfor;		
	}
}
