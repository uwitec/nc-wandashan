package nc.ui.hg.pu.ref;

import java.awt.Container;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.Log;
/**
 * 
 * 
 * 作者:崔勇 
 * 
 *  创建日期:(2004-5-15 10:48:03)
 * 
 *  修改记录及日期:
 * 
 *  修改人:
 */
public class RefHG10QueryDlg extends HgPuBillQueryDlg implements
nc.ui.pub.pf.IinitQueryData{

  public RefHG10QueryDlg(
      Container container, String pkCorp, String operator, String funNode, String businessType, String currentBillType, String sourceBilltype, Object userObj) {
    super(container);
    try {
      initData(pkCorp, operator,funNode, businessType,currentBillType, sourceBilltype,userObj);
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e);
    }
  }
  /**
   * 此处插入方法说明。
   * 创建日期：(2001-12-1 12:19:04)
   */
  public void setTempletID(String pkCorp, String funNode,String operator, java.lang.String businessType, java.lang.String qrynodekey) 
  {
    super.setTempletID(pkCorp, funNode, operator, businessType,qrynodekey);

    nc.vo.pub.query.QueryConditionVO[] voaConData = getConditionDatas();
//  隐藏常用条件
    hideNormal();
//    setInitDate("h.dbilldate", ClientEnvironment.getInstance().getDate().toString());
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-12-1 12:19:04)
   */
  public void initData(java.lang.String pkCorp, java.lang.String operator,
      java.lang.String funNode, java.lang.String businessType,
      java.lang.String currentBillType, java.lang.String sourceBilltype,
      java.lang.Object userObj) throws java.lang.Exception {

    String querynodekey = "25TOHG10";

    setTempletID(pkCorp, funNode, operator, businessType,querynodekey);
  }

  /**
   * 点击"确定"按钮触发 创建日期：(2004-2-20 10:02:48)
   * @param where 
   * @return 
   */
  public Object onQuery(String where) {
	  
	  ConditionVO[] cons = getConditionVO();
	  String strWhere = getWhereSQL(cons);
	  if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)!=null)
		  where =where+" and "+strWhere;
	  
	  Class[] Types = new Class[]{String.class};
	  Object[] Values = new Object[]{where};
	  try {
		  Object o = LongTimeTask.callRemoteService("scmpub", "nc.bs.hg.scm.pub.HgScmPubBO", "queryPlanBillForTOHG10", Types, Values, 1);

		  return o;
	  } catch (Exception e) {
		  Log.error(e);
		  MessageDialog.showWarningDlg(this, null, e.getMessage());
		  return null;
	  }
  }
}
