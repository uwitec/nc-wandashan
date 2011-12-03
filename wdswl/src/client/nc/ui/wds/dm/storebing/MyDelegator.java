package nc.ui.wds.dm.storebing;

import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.SuperVO;

/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends AbstractMyDelegator{

 /**
   *
   *
   *�÷������ڻ�ȡ��ѯ�������û����Զ�������޸ġ�
   *
   */
 public String getBodyCondition(Class bodyClass,String key){
   return super.getBodyCondition(bodyClass,key);
 }

 @Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " pk_stordoc in (SELECT bd_stordoc.pk_stordoc FROM bd_stordoc  "+                //  bd_stordoc          wds_stortranscorp_b
    " join tb_storcubasdoc  on bd_stordoc.pk_stordoc=tb_storcubasdoc.pk_stordoc "
    +" WHERE (isnull(bd_stordoc.dr, 0) = 0)  and  (isnull(tb_storcubasdoc.dr, 0) = 0) and "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
}