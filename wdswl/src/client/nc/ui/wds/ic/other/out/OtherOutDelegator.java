package nc.ui.wds.ic.other.out;

import java.util.Hashtable;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;


/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class OtherOutDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{



	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.ic.other.out.TbOutgeneralBVO[] bodyVOs1 =
                       (nc.vo.ic.other.out.TbOutgeneralBVO[])queryByCondition(nc.vo.ic.other.out.TbOutgeneralBVO.class,
                                                    getBodyCondition(nc.vo.ic.other.out.TbOutgeneralBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_outgeneral_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	/**
	 * ���������������ݲ�ѯ  zhf  �����ѯ����  ������ϸ���ӱ���Ϣ
	 */
	public SuperVO[] queryByCondition(Class voClass, String strWhere)
			throws Exception {		
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
		
		return os;
	}
	
	
	/**
	 * queryBodyVOs ����ע�⡣
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = strWhere +=" general_pk = '"+key+"'";
		else
			strWhere = strWhere +=" and general_pk = '"+key+"'";
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
		
		return os;
		
//		
//		if (billType == null || billType.trim().length() == 0)
//			return null;
//		else
//			return HYPubBO_Client.queryAllBodyData(billType, voClass, key,
//					strWhere);
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ic.other.out.TbOutgeneralBVO.class)
	   return "general_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	


}