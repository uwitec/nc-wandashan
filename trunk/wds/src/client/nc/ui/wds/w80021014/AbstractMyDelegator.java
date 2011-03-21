package nc.ui.wds.w80021014;

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
	                         
           nc.vo.wds.w80021014.TbInvclBasVO[] bodyVOs1 =
                       (nc.vo.wds.w80021014.TbInvclBasVO[])queryByCondition(nc.vo.wds.w80021014.TbInvclBasVO.class,
                                                    getBodyCondition(nc.vo.wds.w80021014.TbInvclBasVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_invcl_bas",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80021014.TbInvclBasVO.class)
	   return "pk_invcl = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
