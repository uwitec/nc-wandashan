package nc.ui.ncblk.yjcb.yjcbjs;

import java.util.Hashtable;


/**
 *
 *�����ҵ�������
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
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO.class)
	   return "pk_yjcb = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
