package nc.ui.wds.ic.other.out;

import java.util.Hashtable;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;


/**
  *
  *抽象业务代理类的缺省实现
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
	 * 根据条件进行数据查询  zhf  改造查询补充  托盘明细子子表信息
	 */
	public SuperVO[] queryByCondition(Class voClass, String strWhere)
			throws Exception {		
		
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
		
		return os;
	}
	
	
	/**
	 * queryBodyVOs 方法注解。
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
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ic.other.out.TbOutgeneralBVO.class)
	   return "general_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	


}