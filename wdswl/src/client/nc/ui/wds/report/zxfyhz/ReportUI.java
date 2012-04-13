package nc.ui.wds.report.zxfyhz;

import javax.swing.ListSelectionModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ReportBaseUI;
/**
 * 装卸费月汇总表------------------报表
 * @author Administrator
 */
public class ReportUI extends ReportBaseUI {
    
    private static final long serialVersionUID = 1L;
   
    private String ddatefrom =null;
    private String ddateto = null;
    /**
     * 提高效率
     */
    public String _getModelCode() {
        return WdsWlPubConst.REPORT13;
    }
    @Override
    public void setUIAfterLoadTemplate() {
    	  setColumn();
          getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    /**
     * 基本列合并
     */
    private void setColumn() {
//        //表体栏目分组设置
//        UITable cardTable = getReportBase().getBillTable();
//        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
//        TableColumnModel cardTcm = cardTable.getColumnModel();
//        ColumnGroup[] card2 = new ColumnGroup[4];
//        //
//        card2[0]=new ColumnGroup("期初结存");
//        card2[0].add(cardTcm.getColumn(9));
//        card2[0].add(cardTcm.getColumn(10));
//        cardHeader.addColumnGroup(card2[0]);
//        //
//        card2[1]=new ColumnGroup("本期收入");
//        card2[1].add(cardTcm.getColumn(11));
//        card2[1].add(cardTcm.getColumn(12));
//        cardHeader.addColumnGroup(card2[1]);
//        //
//        card2[2]=new ColumnGroup("本期发出");
//        card2[2].add(cardTcm.getColumn(13));
//        card2[2].add(cardTcm.getColumn(14));
//        cardHeader.addColumnGroup(card2[2]);
//        //
//        card2[3]=new ColumnGroup("期末结存");
//        card2[3].add(cardTcm.getColumn(15));
//        card2[3].add(cardTcm.getColumn(16));
//        cardHeader.addColumnGroup(card2[3]);
//        getReportBase().getBillModel().updateValue();
    }
    @Override
    /*去掉小计合计按钮*/
    public int[] getReportButtonAry() {
    
        return new int[] { 
              IReportButton.QueryBtn, 
//              IReportButton.ColumnFilterBtn, 
              IReportButton.CrossBtn,
              IReportButton.FilterBtn, 
              IReportButton.SortBtn, 
              IReportButton.PrintBtn,
              IReportButton.PrintDirectBtn,
              IReportButton.RefreshBtn
//              IReportButton.SubTotalBtn
              };
    }
    @Override
    public void onQuery() {
    	  try { 	
          	//设置查询模板默认查询条件
              AccountCalendar  accCal = AccountCalendar.getInstance();     
              getQueryDlg().setDefaultValue("ddatafrom", accCal.getMonthVO().getBegindate().toString(), "");
              getQueryDlg().setDefaultValue("ddatato", accCal.getMonthVO().getEnddate().toString(), "");
              getQueryDlg().showModal();
              if (getQueryDlg().getResult() == UIDialog.ID_OK) {
              	//校验开始日期，截止日期
              	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatafrom");
              	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatato");
              	ddatefrom = obj1.getRefName();
              	if(ddatefrom == null ||"".equalsIgnoreCase(ddatefrom)){
              		showErrorMessage("请输入开始日期");
              		return ;
              	}
              	//截止日期如果为空，则默认为当前日期
              	ddateto = obj2.getRefName();
              	if(ddateto == null || "".equalsIgnoreCase(ddateto)){
              		ddateto = _getCurrDate().toString();
              	}
              	//中文的查询条件
              	String qryconditons = getQueryDlg().getChText();
              	getReportBase().setHeadItem("ddatafrom", ddatefrom);
              	getReportBase().setHeadItem("ddatato", ddateto);
              	getReportBase().getHeadItem("qryconditons").setWidth(2);
              	getReportBase().setHeadItem("qryconditons", qryconditons);
              	//得到自定义查询条件
                  //得到查询结果
                  ReportBaseVO[] vos = getReportVO(getQuerySQL());
                  super.updateBodyDigits();
                  setReportBaseVO(vos);
                  setBodyVO(vos);
                  //合计处理
                  if ( vos != null && vos.length > 0 ) {
                      setTolal();//合计
                  }
              }
          } catch (Exception e) {
              e.printStackTrace();
              showWarningMessage(e.getMessage());
          }

    }
    /**
     * 
     * @作者：lyf
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
    public String getQuerySQL(){
    	StringBuffer sql = new StringBuffer();
           
    	sql.append("select ");
    	sql.append( "  min(wds_loadprice_h.dapprovedate) approvedate");//审批日期
    	sql.append( " ,min(wds_loadprice_b2.pk_wds_teamdoc_h) pk_teamdoc");//班组id   	
    	sql.append( " ,wds_teamdoc_h.teamcode classcode");//班组编码
    	sql.append( " ,min(wds_teamdoc_h.teamname) classname");//班组名称
    	sql.append( " ,min(wds_teamdoc_h.teamaddr) teamaddr");//班组地址
    	sql.append( " ,sum(wds_loadprice_b2.nloadprice) accountload");//装卸费用
    	sql.append( " ,min(wds_teamdoc_b.psncode) psncode");//队长编码               [该字段目前没有使用 备用]
    	sql.append( " ,min(wds_teamdoc_b.psnname) psnname");//队长名称                [该字段目前没有使用 备用]
    	sql.append( " ,min(wds_teamdoc_b.contact1) cont1");//队长联系方式          [该字段目前没有使用 备用]
    	sql.append( " ,min(wds_teamdoc_b.contact2) cont2");//队长联系方式2   [该字段目前没有使用 备用]
    	sql.append( " ,min(wds_teamdoc_b.contact3) cont3");//队长联系方式3    [该字段目前没有使用 备用]   	
    	sql.append( " from wds_loadprice_h join wds_loadprice_b2 on");
    	sql.append( " wds_loadprice_h.pk_loadprice=wds_loadprice_b2.pk_loadprice");
    	sql.append( " join wds_teamdoc_h on");
    	sql.append( " wds_loadprice_b2.pk_wds_teamdoc_h=wds_teamdoc_h.pk_wds_teamdoc_h");
    	sql.append( " join wds_teamdoc_b on");
    	sql.append( " wds_teamdoc_h.pk_wds_teamdoc_h= wds_teamdoc_b.pk_wds_teamdoc_h");
    	sql.append( " where upper(isnull(wds_teamdoc_b.isteam,'N'))='Y'");//过滤出队长
    	sql.append( " and isnull(wds_loadprice_h.dr,0)=0");//查询没有被删除的
    	sql.append( " and wds_loadprice_h.pk_corp='"+_getCorpID()+"'");
    	sql.append( " and wds_loadprice_h.vbillstatus=1");//审批通过的
    	sql.append( " and wds_loadprice_h.dapprovedate ");
    	sql.append( " between '"+ddatefrom+"' and '"+ddateto+"'");
    	sql.append( " group by wds_teamdoc_h.teamcode");
        return sql.toString();
    } 
    
    public ReportBaseVO[] getReportVO(String sql) throws BusinessException{
        ReportBaseVO[] reportVOs = null;
        try{
            Class[] ParameterTypes = new Class[]{String.class};
            Object[] ParameterValues = new Object[]{sql};
            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
                    "正在查询...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
                    "queryVOBySql", ParameterTypes, ParameterValues);
            if(o != null){
                reportVOs = (ReportBaseVO[])o;
            }
        }catch(Exception e){
            Logger.error(e);
            MessageDialog.showErrorDlg(this, "警告", e.getMessage());
        }
        return reportVOs;
    }
    
    
    //设置序号
    public void setReportBaseVO(ReportBaseVO[] vos){
        if(vos!=null && vos.length>0){
            for(int i = 0 ;i < vos.length;i++){
                vos[i].setAttributeValue("lineno", (i+1));
            }
        }
    }
    /**
     * 合计
     */
    public void setTolal() throws Exception {
//        SubtotalVO svo = new SubtotalVO();
//        svo.setGroupFldCanNUll(true);// 分组列的数据是否可以为空。
//        svo.setAsLeafRs(new boolean[] { true });// 分组列合并后是否作为末级节点记录。
//        svo.setValueFlds(new String[] { "nstartnum","nstartassnum","ninnum","ninassnum","noutnum","noutassnum","nendnum","nendassnum",});// 求值列:
//        svo.setValueFldTypes(new int[] { IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD });// 求值列的类型:
//        svo.setTotalDescOnFld("invname");// ----合计---字段 ---- 所在列
//        setSubtotalVO(svo);
//        doSubTotal();
 
    }
}
