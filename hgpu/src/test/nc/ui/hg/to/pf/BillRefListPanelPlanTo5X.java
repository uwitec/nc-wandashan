package nc.ui.hg.to.pf;

import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.sourceref.BillRefListPanel;
import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.vo.pub.BusinessException;

public class BillRefListPanelPlanTo5X extends BillRefListPanel{
	RefPlanQueryDlg queryDlg = null;

  public BillRefListPanelPlanTo5X(
      String biztype, String sourcetype, String targettype, String pk_corp) {
    super(biztype, sourcetype, targettype, pk_corp);
    // TODO 自动生成构造函数存根
  }

  public BillRefListPanelPlanTo5X(
      String biztype, String sourcetype, String targettype, String pk_corp,RefPlanQueryDlg Dlg) {
    super(biztype, sourcetype, targettype, pk_corp);
    queryDlg = Dlg;
  }
  
  @Override
  protected void initPrecition() {
    nc.ui.to.pub.TOEnvironment te = new nc.ui.to.pub.TOEnvironment();
    Integer[] pre = null;
    try {
      pre = te.getDataPrecision(getPk_corp());
    }
    catch (BusinessException e) {
      nc.vo.scm.pub.ctrl.GenMsgCtrl.printErr("没有获得当前公司的数据精度,请调整!");/*-=notranslate=-*/
    }
    /*int[0] ----- 数量的精度
    int[1] ----- 单价的精度
    int[2] ----- 金额的精度
    int[3] ----- 辅计量数量的精度
    int[4] ----- 换算率的精度*/
    BillItem[] biaItems = getBillListData().getBodyItems();
    if(biaItems==null) return;
    for (int i = 0; i < biaItems.length; i++)
    {
      String sKey = biaItems[i].getKey();
      if(sKey.indexOf("num") >= 0 && sKey.indexOf("ass") < 0)//数量
      {
        //设置可输入的长度,只有表单需要=整数位+小数点占的一位+小数点后的位数
        biaItems[i].setLength(nc.vo.scm.pub.bill.SCMDoubleScale.INT_LENGTH + 1 + pre[0].intValue());
        biaItems[i].setDecimalDigits(pre[0].intValue());
      }
      else if(sKey.indexOf("price") >= 0)
      {
        biaItems[i].setLength(nc.vo.scm.pub.bill.SCMDoubleScale.INT_LENGTH + 1 + pre[1].intValue());
        biaItems[i].setDecimalDigits(pre[1].intValue());
      }
      else if(sKey.indexOf("mny") >= 0)
      {
        biaItems[i].setLength(nc.vo.scm.pub.bill.SCMDoubleScale.INT_LENGTH + 1 + pre[2].intValue());
        biaItems[i].setDecimalDigits(pre[2].intValue());
      }
      else if(sKey.indexOf("num") >= 0 && sKey.indexOf("ass") >= 0)
      {
        biaItems[i].setLength(nc.vo.scm.pub.bill.SCMDoubleScale.INT_LENGTH + 1 + pre[3].intValue());
        biaItems[i].setDecimalDigits(pre[3].intValue());
      }
      else if(sKey.indexOf("changerate") >= 0)
      {
        biaItems[i].setLength(nc.vo.scm.pub.bill.SCMDoubleScale.INT_LENGTH + 1 + pre[4].intValue());
        biaItems[i].setDecimalDigits(pre[4].intValue());
      }
    }
  }


  public DefaultSrcRefCtl getSourcectl(){
    if(sourcectl==null){
         sourcectl = new SourceRefDlgCtrlPlanTo5X(getCsourcetype(),getCtargettype(),getPk_corp(),this,queryDlg);
       }
    return sourcectl;
 }
  
  /**
   * 获取参照单据模板的结点号
   */
  public String getRefNodeCode(){
    return "5A5Xsource";
  }
}
