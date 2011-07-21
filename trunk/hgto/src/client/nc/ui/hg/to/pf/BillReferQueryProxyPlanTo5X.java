package nc.ui.hg.to.pf;

import java.awt.Container;

import nc.ui.pub.query.QueryConditionClient;
import nc.ui.querytemplate.QueryConditionDLG;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.scm.pub.sourceref.IBillReferQueryProxy;
import nc.vo.querytemplate.TemplateInfo;

public class BillReferQueryProxyPlanTo5X extends IBillReferQueryProxy{
  public BillReferQueryProxyPlanTo5X() {
    super();
    // TODO 自动生成构造函数存根
  }

  public BillReferQueryProxyPlanTo5X(
      Container parent, TemplateInfo ti) {
    super(parent, ti);
    // TODO 自动生成构造函数存根
  }

  public BillReferQueryProxyPlanTo5X(
      Container parent) {
    super(parent);
    // TODO 自动生成构造函数存根
  }

  private RefPlanQueryDlg dlg = null;
  @Override
  public QueryConditionDLG createNewQryDlg() {
    // TODO 自动生成方法存根
    return null;
  }

  @Override
  public QueryConditionClient createOldQryDlg() {
    if(dlg==null){
      dlg = new RefPlanQueryDlg(getContainer(), getPkCorp(),
          getOperator(), getFunNode(), getBusinessType(),
          getCurrentBillType(), getSourceBilltype(), getUserObj());
      dlg.setBillRefModeSelPanel(true);
    }
    return dlg;
  }

  @Override
  public boolean isNewQryDlg() {
    return false;
  }

  @Override
  public boolean isShowDoubleTableRef() {
    return ((SCMQueryConditionDlg) createOldQryDlg())
    .isShowDoubleTableRef();
  }

  @Override
  public void setUserRefShowMode(boolean isShowDoubleTableRef) {
    ((SCMQueryConditionDlg) createOldQryDlg())
    .setBillRefShowMode(isShowDoubleTableRef);
  }

}
