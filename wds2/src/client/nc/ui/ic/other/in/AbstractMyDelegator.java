package nc.ui.ic.other.in;

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
	                         
           nc.vo.wds.w8004040210.TbGeneralBVO[] bodyVOs1 =
                       (nc.vo.wds.w8004040210.TbGeneralBVO[])queryByCondition(nc.vo.wds.w8004040210.TbGeneralBVO.class,
                                                    getBodyCondition(nc.vo.wds.w8004040210.TbGeneralBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_general_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w8004040210.TbGeneralBVO.class)
	   return "${voAndTable.fkName} = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
