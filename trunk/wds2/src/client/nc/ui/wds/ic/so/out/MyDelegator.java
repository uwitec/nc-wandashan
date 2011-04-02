package nc.ui.wds.ic.so.out;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.ic.other.out.TbOutgeneralBVO;
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
  * @���ߣ�lyf
  * @˵�������ɽ������Ŀ �Զ��������
  * @ʱ�䣺2011-3-31����08:20:29
  * @param bodys
  * @param cwhid
  * @param cspaceid
  * @return
  * @throws Exception
  */
 public  Object autoOutStore(TbOutgeneralBVO[] bodys, String cwhid, String cspaceid) throws Exception{
		if(bodys == null || bodys.length == 0)
			return null;
		Class[] ParameterTypes = new Class[]{TbOutgeneralBVO[].class,String.class,String.class};
		Object[] ParameterValues = new Object[]{bodys,cwhid,cspaceid};
		Object os = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "autooutTray", ParameterTypes, ParameterValues, 2);

		return os;
 }

}