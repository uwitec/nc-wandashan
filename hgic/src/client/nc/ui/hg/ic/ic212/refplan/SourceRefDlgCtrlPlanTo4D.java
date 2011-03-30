package nc.ui.hg.ic.ic212.refplan;

import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.ui.scm.pub.sourceref.IBillToBillListPanel;

public class SourceRefDlgCtrlPlanTo4D extends DefaultSrcRefCtl{
  RefPlanQueryDlg queryDlg = null;

  public SourceRefDlgCtrlPlanTo4D(
      String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel) {
    super(srcbilltype, targerbilltype, pk_corp, refpanel);
    // TODO 自动生成构造函数存根
  }
  
  public SourceRefDlgCtrlPlanTo4D(String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel,RefPlanQueryDlg dlg) {
    super(srcbilltype, targerbilltype, pk_corp, refpanel);
    queryDlg = dlg;
  }
  
  public Object[] queryAllData(String sWhere) throws Exception {
    Object[] result = new Object[3];       
    Object o = queryDlg.onQuery(sWhere);
    
    if(o == null)
    	return null;
    
    Object[] os = (Object[])o;
    result[0] = os[0];
    result[1] = os[1];
    return result;
  }
}
