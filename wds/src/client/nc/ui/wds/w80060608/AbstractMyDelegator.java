package nc.ui.wds.w80060608;

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
	                         
           nc.vo.wds.w80060608.IcGeneralBVO[] bodyVOs1 =
                       (nc.vo.wds.w80060608.IcGeneralBVO[])queryByCondition(nc.vo.wds.w80060608.IcGeneralBVO.class,
                                                    getBodyCondition(nc.vo.wds.w80060608.IcGeneralBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("ic_general_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80060608.IcGeneralBVO.class)
	   return "cgeneralhid = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
