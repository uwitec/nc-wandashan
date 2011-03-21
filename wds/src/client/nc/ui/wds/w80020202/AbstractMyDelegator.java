package nc.ui.wds.w80020202;

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
	                         
           nc.vo.wds.w80020202.TbHandlecostsBVO[] bodyVOs1 =
                       (nc.vo.wds.w80020202.TbHandlecostsBVO[])queryByCondition(nc.vo.wds.w80020202.TbHandlecostsBVO.class,
                                                    getBodyCondition(nc.vo.wds.w80020202.TbHandlecostsBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_handlecosts_b",bodyVOs1);
            } 
	         
	              
           nc.vo.wds.w80020202.TbLogoVO[] bodyVOs2 =
                       (nc.vo.wds.w80020202.TbLogoVO[])queryByCondition(nc.vo.wds.w80020202.TbLogoVO.class,
                                                    getBodyCondition(nc.vo.wds.w80020202.TbLogoVO.class,key));   
            if(bodyVOs2 != null && bodyVOs2.length > 0){
                          
              dataHashTable.put("tb_logo",bodyVOs2);
            } 
	         
	              
           nc.vo.wds.w80020202.TbAcquisitionVO[] bodyVOs3 =
                       (nc.vo.wds.w80020202.TbAcquisitionVO[])queryByCondition(nc.vo.wds.w80020202.TbAcquisitionVO.class,
                                                    getBodyCondition(nc.vo.wds.w80020202.TbAcquisitionVO.class,key));   
            if(bodyVOs3 != null && bodyVOs3.length > 0){
                          
              dataHashTable.put("tb_acquisition",bodyVOs3);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80020202.TbHandlecostsBVO.class)
	   return "hc_pk = '" + key + "' and isnull(dr,0)=0 ";
       	 if(bodyClass == nc.vo.wds.w80020202.TbLogoVO.class)
	   return "hc_pk = '" + key + "' and isnull(dr,0)=0 ";
       	 if(bodyClass == nc.vo.wds.w80020202.TbAcquisitionVO.class)
	   return "hc_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
