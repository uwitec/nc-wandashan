package nc.ui.wds.w8006080602;

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
	                         
           nc.vo.wds.w8006080602.TbCarinfBVO[] bodyVOs1 =
                       (nc.vo.wds.w8006080602.TbCarinfBVO[])queryByCondition(nc.vo.wds.w8006080602.TbCarinfBVO.class,
                                                    getBodyCondition(nc.vo.wds.w8006080602.TbCarinfBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_carinf_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w8006080602.TbCarinfBVO.class)
	   return "cif_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
