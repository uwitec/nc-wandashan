package nc.ui.wds.w80060804;

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
	                         
           nc.vo.wds.w80060804.TbTranscompanyBVO[] bodyVOs1 =
                       (nc.vo.wds.w80060804.TbTranscompanyBVO[])queryByCondition(nc.vo.wds.w80060804.TbTranscompanyBVO.class,
                                                    getBodyCondition(nc.vo.wds.w80060804.TbTranscompanyBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_transcompany_b",bodyVOs1);
            } 
	         
	              
           nc.vo.wds.w80060804.TbTranscompanyKVO[] bodyVOs2 =
                       (nc.vo.wds.w80060804.TbTranscompanyKVO[])queryByCondition(nc.vo.wds.w80060804.TbTranscompanyKVO.class,
                                                    getBodyCondition(nc.vo.wds.w80060804.TbTranscompanyKVO.class,key));   
            if(bodyVOs2 != null && bodyVOs2.length > 0){
                          
              dataHashTable.put("tb_transcompany_k",bodyVOs2);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80060804.TbTranscompanyBVO.class)
	   return "tc_pk = '" + key + "' and isnull(dr,0)=0 ";
       	 if(bodyClass == nc.vo.wds.w80060804.TbTranscompanyKVO.class)
	   return "tc_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
