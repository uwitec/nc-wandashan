package nc.ui.wds.w80020206;

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
	                         
           nc.vo.wds.w80060406.TbFydnewVO[] bodyVOs1 =
                       (nc.vo.wds.w80060406.TbFydnewVO[])queryByCondition(nc.vo.wds.w80060406.TbFydnewVO.class,
                                                    getBodyCondition(nc.vo.wds.w80060406.TbFydnewVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_fydnew",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80060406.TbFydnewVO.class)
	   return "${voAndTable.fkName} = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
