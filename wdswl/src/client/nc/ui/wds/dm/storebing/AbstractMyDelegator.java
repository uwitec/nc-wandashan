package nc.ui.wds.dm.storebing;

import java.util.Hashtable;

import nc.vo.wds.dm.storebing.TbStorcubasdocVO;


/**
 *
 *抽象的业务代理类
 *
 * @author author
 * @version tempProject version
 */
public abstract class AbstractMyDelegator extends nc.ui.trade.bsdelegate.BDBusinessDelegator {

	public Hashtable<String, TbStorcubasdocVO[]> loadChildDataAry(String[] tableCodes,String key) 
	                                                 throws Exception{
		
	   Hashtable<String, TbStorcubasdocVO[]> dataHashTable = new Hashtable<String, TbStorcubasdocVO[]>();
	                         
           nc.vo.wds.dm.storebing.TbStorcubasdocVO[] bodyVOs1 =
                       (nc.vo.wds.dm.storebing.TbStorcubasdocVO[])queryByCondition(nc.vo.wds.dm.storebing.TbStorcubasdocVO.class,
                                                    getBodyCondition(nc.vo.wds.dm.storebing.TbStorcubasdocVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_storcubasdoc",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.dm.storebing.TbStorcubasdocVO.class)
	   return "pk_stordoc = '" + key + "' ";
       		
	 return null;
       } 
	
}
