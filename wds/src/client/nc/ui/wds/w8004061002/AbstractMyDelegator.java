package nc.ui.wds.w8004061002;

import java.util.Hashtable;


/**
 *
 *抽象的业务代理类
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable dataHashTable = new Hashtable();
	                         
           nc.vo.wds.w8004061002.BdCargdocTrayVO[] bodyVOs1 =
                       (nc.vo.wds.w8004061002.BdCargdocTrayVO[])queryByCondition(nc.vo.wds.w8004061002.BdCargdocTrayVO.class,
                                                    getBodyCondition(nc.vo.wds.w8004061002.BdCargdocTrayVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("bd_cargdoc_tray",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w8004061002.BdCargdocTrayVO.class)
	   return "pk_cargdoc = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
