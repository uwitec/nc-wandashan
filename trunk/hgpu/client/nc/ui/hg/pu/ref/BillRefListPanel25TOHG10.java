package nc.ui.hg.pu.ref;

import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;

public class BillRefListPanel25TOHG10 extends BillRefListPanel{
	RefHG10QueryDlg queryDlg = null;

  public BillRefListPanel25TOHG10(
      String biztype, String sourcetype, String targettype, String pk_corp) {
    super(biztype, sourcetype, targettype, pk_corp);
    // TODO 自动生成构造函数存根
  }

  public BillRefListPanel25TOHG10(
      String biztype, String sourcetype, String targettype, String pk_corp,RefHG10QueryDlg Dlg) {
    super(biztype, sourcetype, targettype, pk_corp);
    queryDlg = Dlg;
  }
 

  public DefaultSrcRefCtl getSourcectl(){
    if(sourcectl==null){
         sourcectl = new SourceRefDlgCtrl25TOHG10(getCsourcetype(),getCtargettype(),getPk_corp(),this,queryDlg);
       }
    return sourcectl;
 }
}
