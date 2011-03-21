package nc.ui.wds.w80061006;


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
	                         
           nc.vo.wds.w80061006.TbProdwaybillBVO[] bodyVOs1 =
                       (nc.vo.wds.w80061006.TbProdwaybillBVO[])queryByCondition(nc.vo.wds.w80061006.TbProdwaybillBVO.class,
                                                    getBodyCondition(nc.vo.wds.w80061006.TbProdwaybillBVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_prodwaybill_b",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80061006.TbProdwaybillBVO.class)
	   return "pwb_pk = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
