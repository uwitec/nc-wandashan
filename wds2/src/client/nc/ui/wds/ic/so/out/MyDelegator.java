package nc.ui.wds.ic.so.out;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends AbstractMyDelegator{

 /**
   *
   *
   *该方法用于获取查询条件，用户可以对其进行修改。
   *
   */
 public String getBodyCondition(Class bodyClass,String key){
   return super.getBodyCondition(bodyClass,key);
 }
 
 private String bo = "nc.bs.ic.pub.WdsIcPubBO";
 
 /**
  * 
  * @作者：lyf
  * @说明：完达山物流项目 自动拣货出库
  * @时间：2011-3-31下午08:20:29
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