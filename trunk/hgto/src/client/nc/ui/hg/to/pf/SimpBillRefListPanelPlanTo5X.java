package nc.ui.hg.to.pf;

import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.sourceref.DefaultSrcRefCtl;
import nc.ui.scm.pub.sourceref.SimpBillRefListPanel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.bill.BillTempletVO;

public class SimpBillRefListPanelPlanTo5X extends SimpBillRefListPanel{
	RefPlanQueryDlg queryDlg = null;

  public SimpBillRefListPanelPlanTo5X(
      String biztype, String sourcetype, String targettype, String pk_corp) {
    super(biztype, sourcetype, targettype, pk_corp);
    // TODO �Զ����ɹ��캯�����
  }

  public SimpBillRefListPanelPlanTo5X(
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
      nc.vo.scm.pub.ctrl.GenMsgCtrl.printErr("û�л�õ�ǰ��˾�����ݾ���,�����!");/*-=notranslate=-*/
    }
    /*int[0] ----- �����ľ���
    int[1] ----- ���۵ľ���
    int[2] ----- ���ľ���
    int[3] ----- �����������ľ���
    int[4] ----- �����ʵľ���*/
    BillItem[] biaItems = getBillListData().getHeadItems();
    if(biaItems==null) return;
    for (int i = 0; i < biaItems.length; i++)
    {
      String sKey = biaItems[i].getKey();
      if(sKey.indexOf("num") >= 0 && sKey.indexOf("ass") < 0)//����
      {
        //���ÿ�����ĳ���,ֻ�б���Ҫ=����λ+С����ռ��һλ+С������λ��
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
   * ��ȡ���յ���ģ��Ľ���
   */
  public String getRefNodeCode(){
    return "5A5Xsource";
  }
  
}
