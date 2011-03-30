package nc.ui.hg.to.pf;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import com.informix.util.stringUtil;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.Log;
/**
 * 功能描述:调拨出入库单拉调拨订单
 * 
 * 作者:崔勇 
 * 
 *  创建日期:(2004-5-15 10:48:03)
 * 
 *  修改记录及日期:
 * 
 *  修改人:
 */
public class RefPlanQueryDlg extends nc.ui.scm.pub.query.SCMQueryConditionDlg implements
nc.ui.pub.pf.IinitQueryData{

  public RefPlanQueryDlg(
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
   // setInitDate("h.dbilldate", ClientEnvironment.getInstance().getDate().toString());
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-12-1 12:19:04)
   */
  public void initData(java.lang.String pkCorp, java.lang.String operator,
      java.lang.String funNode, java.lang.String businessType,
      java.lang.String currentBillType, java.lang.String sourceBilltype,
      java.lang.Object userObj) throws java.lang.Exception {

    String querynodekey = "5Xref5A";

    setTempletID(pkCorp, funNode, operator, businessType,querynodekey);
  }

  /**
   * 点击"确定"按钮触发 创建日期：(2004-2-20 10:02:48)
   * @param where 
   * @return 
   */
  public Object onQuery(String where,String billtype) {
	  
	  ConditionVO[] cons = getConditionVO();
	  String strWhere = null;
		String str = null;
		for(ConditionVO con:cons){
			//modify by zhw 2011-01-24  根据存货分类查询  按大类查询
			if(con.getFieldCode().equalsIgnoreCase("h.cinvclassid") ||con.getFieldCode().equalsIgnoreCase("1")){
					String pk_invcl = con.getValue();
					String invcode = PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(pk_invcl));
					if(invcode !=null){
						str=" and c.pk_invcl in(select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')";
						con.setFieldCode("1");
						con.setValue("1");
					}
			}
		}
		strWhere = " and " +getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		if(PuPubVO.getString_TrimZeroLenAsNull(str) ==null)
			str =" and 1=1 ";
		    strWhere = strWhere + str;
		where =where+strWhere;
		
	  Class[] Types = new Class[]{String.class,String.class,String.class};
	  Object[] Values = new Object[]{where,str,billtype};
	  try {
		  Object o = LongTimeTask.callRemoteService("scmpub", "nc.bs.hg.scm.pub.HgScmPubBO", "queryPlanBillForTO5X", Types, Values, 2);

		  return o;
	  } catch (Exception e) {
		  Log.error(e);
		  MessageDialog.showWarningDlg(this, null, e.getMessage());
		  return null;
	  }
  }
  
  
//  /**
//   * 此处插入方法说明。 创建日期：(2004-8-17 10:51:44)
//   * 
//   * @param voCnd
//   *            nc.vo.pub.query.ConditionVO[]
//   * @param sWhere
//   *            java.lang.String
//   * @return 
//   * @throws Exception 
//   */
//  private HYBillVO[] queryData(String sWhere) throws Exception {
//	  
//	  HYBillVO[] billvos = null;
//	  return billvos;
//	  
//  }
  

}
