package nc.ui.hg.pu.ref;

import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.ui.scm.pub.sourceref.IBillToBillListPanel;

public class SourceRefDlgCtrl25TOHG10 extends DefaultSrcRefCtl{
  RefHG10QueryDlg queryDlg = null;

  public SourceRefDlgCtrl25TOHG10(
      String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel) {
    super(srcbilltype, targerbilltype, pk_corp, refpanel);
    // TODO �Զ����ɹ��캯�����
  }
  
  public SourceRefDlgCtrl25TOHG10(String srcbilltype, String targerbilltype, String pk_corp, IBillToBillListPanel refpanel,RefHG10QueryDlg dlg) {
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
