package nc.ui.hg.to.pf;

import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.ui.scm.pub.sourceref.IBillToBillListPanel;

public class SourceRefDlgCtrlPlanTo5X extends DefaultSrcRefCtl{
  RefPlanQueryDlg queryDlg = null;
  String  billtype=null;

  public SourceRefDlgCtrlPlanTo5X(
      String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel) {
    super(srcbilltype, targerbilltype, pk_corp, refpanel);
    // TODO 自动生成构造函数存根
  }
  
  public SourceRefDlgCtrlPlanTo5X(String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel,RefPlanQueryDlg dlg) {
    super(srcbilltype, targerbilltype, pk_corp, refpanel);
    queryDlg = dlg;
    billtype=srcbilltype;
  }
  
  public Object[] queryAllData(String sWhere) throws Exception {
    Object[] result = new Object[3];       
    Object o = queryDlg.onQuery(sWhere,billtype);
    
    if(o == null)
    	return null;
    
    Object[] os = (Object[])o;
    result[0] = os[0];
    result[1] = os[1];
    return result;
  }
}
