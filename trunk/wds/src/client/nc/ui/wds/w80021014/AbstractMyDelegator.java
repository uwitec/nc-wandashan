package nc.ui.wds.w80021014;

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
	                         
           nc.vo.wds.w80021014.TbInvclBasVO[] bodyVOs1 =
                       (nc.vo.wds.w80021014.TbInvclBasVO[])queryByCondition(nc.vo.wds.w80021014.TbInvclBasVO.class,
                                                    getBodyCondition(nc.vo.wds.w80021014.TbInvclBasVO.class,key));   
            if(bodyVOs1 != null && bodyVOs1.length > 0){
                          
              dataHashTable.put("tb_invcl_bas",bodyVOs1);
            } 
	         
	   	   return dataHashTable;
		
	}
	
	
       /**
         *
         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
         *
         */	
       public String getBodyCondition(Class bodyClass,String key){
		
       	 if(bodyClass == nc.vo.wds.w80021014.TbInvclBasVO.class)
	   return "pk_invcl = '" + key + "' and isnull(dr,0)=0 ";
       		
	 return null;
       } 
	
}
