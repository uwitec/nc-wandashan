package nc.ui.hg.pu.ref;

import java.awt.Container;

import nc.ui.pub.query.QueryConditionClient;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.scm.pub.sourceref.IBillReferQueryProxy;
import nc.vo.querytemplate.TemplateInfo;

public class BillReferQueryProxy25TOHG10 extends IBillReferQueryProxy{
  public BillReferQueryProxy25TOHG10() {
    super();
    // TODO 自动生成构造函数存根
  }

  public BillReferQueryProxy25TOHG10(
      Container parent, TemplateInfo ti) {
    super(parent, ti);
    // TODO 自动生成构造函数存根
  }

  public BillReferQueryProxy25TOHG10(
      Container parent) {
    super(parent);
    // TODO 自动生成构造函数存根
  }

  private RefHG10QueryDlg dlg = null;
  @Override
  public QueryConditionDLG createNewQryDlg() {
    // TODO 自动生成方法存根
    return null;
  }

  @Override
  public QueryConditionClient createOldQryDlg() {
    if(dlg==null){
      dlg = new RefHG10QueryDlg(getContainer(), getPkCorp(),
          getOperator(), getFunNode(), getBusinessType(),
          getCurrentBillType(), getSourceBilltype(), getUserObj());
    }
    return dlg;
  }

  @Override
  public boolean isNewQryDlg() {
    return false;
  }

@Override
public boolean isShowDoubleTableRef() {
	// TODO Auto-generated method stub
	return true;
}

@Override
public void setUserRefShowMode(boolean isShowDoubleTableRef) {
	// TODO Auto-generated method stub
	
}

}
