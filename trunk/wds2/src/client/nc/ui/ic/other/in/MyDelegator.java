package nc.ui.ic.other.in;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

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
 private String bo = "nc.bs.ic.pub.WdsIcPubBO";
 
 /**
  * 
  * @���ߣ�zhf
  * @˵�������ɽ������Ŀ �Զ�������
  * @ʱ�䣺2011-3-31����08:20:29
  * @param bodys
  * @param cwhid
  * @param cspaceid
  * @return
  * @throws Exception
  */
 public  Object autoInStore(TbGeneralBVO[] bodys, String cwhid, String cspaceid) throws Exception{
		if(bodys == null || bodys.length == 0)
			return null;
		Class[] ParameterTypes = new Class[]{TbGeneralBVO[].class,String.class,String.class};
		Object[] ParameterValues = new Object[]{bodys,cwhid,cspaceid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "autoInTray", ParameterTypes, ParameterValues, 2);

		return os;
 }

}