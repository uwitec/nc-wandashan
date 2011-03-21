package nc.ui.ncblk.yjcb.yjcbjs;

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
	                         
           nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO[] bodyVOs1 =
                       (nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO[])queryByCondition(nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO.class,
                                                    getBodyCondition(nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("nc_report_yjcb_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO.class)
	   return "pk_yjcb = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
