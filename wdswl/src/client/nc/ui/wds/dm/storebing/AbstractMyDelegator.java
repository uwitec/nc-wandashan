package nc.ui.wds.dm.storebing;

import java.util.Hashtable;

import nc.vo.wds.dm.storebing.TbStorcubasdocVO;


/**
 *
 *�����ҵ�������
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
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.dm.storebing.TbStorcubasdocVO.class)
	   return "pk_stordoc = '" + key + "' ";
       		
	 return null;
       } 
	
}
