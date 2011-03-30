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
 * ����:���� 
 * 
 *  ��������:(2004-5-15 10:48:03)
 * 
 *  �޸ļ�¼������:
 * 
 *  �޸���:
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
   * �˴����뷽��˵����
   * �������ڣ�(2001-12-1 12:19:04)
   */
  public void setTempletID(String pkCorp, String funNode,String operator, java.lang.String businessType, java.lang.String qrynodekey) 
  {
    super.setTempletID(pkCorp, funNode, operator, businessType,qrynodekey);

    nc.vo.pub.query.QueryConditionVO[] voaConData = getConditionDatas();
//  ���س�������
    hideNormal();
//    setInitDate("h.dbilldate", ClientEnvironment.getInstance().getDate().toString());
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-12-1 12:19:04)
   */
  public void initData(java.lang.String pkCorp, java.lang.String operator,
      java.lang.String funNode, java.lang.String businessType,
      java.lang.String currentBillType, java.lang.String sourceBilltype,
      java.lang.Object userObj) throws java.lang.Exception {

    String querynodekey = "25TOHG10";

    setTempletID(pkCorp, funNode, operator, businessType,querynodekey);
  }

  /**
   * ���"ȷ��"��ť���� �������ڣ�(2004-2-20 10:02:48)
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
