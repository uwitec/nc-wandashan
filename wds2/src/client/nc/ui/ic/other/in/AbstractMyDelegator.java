package nc.ui.ic.other.in;

import java.util.Hashtable;
import nc.vo.ic.pub.TbGeneralBVO;


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
	                         
           TbGeneralBVO[] bodyVOs1 =
                       (TbGeneralBVO[])queryByCondition(TbGeneralBVO.class,
                                                    getBodyCondition(TbGeneralBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_general_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == TbGeneralBVO.class)
	   return "${voAndTable.fkName} = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
