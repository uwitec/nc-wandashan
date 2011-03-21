package nc.ui.wds.w8004061002;

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
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w8004061002.BdCargdocTrayVO.class)
	   return "pk_cargdoc = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
