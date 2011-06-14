package nc.ui.wds.report.yfmxb;

import javax.swing.ListSelectionModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.buttonaction.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 运费明细表------------------报表
 * @author mlr
 */
public class ReportUI extends ReportBaseUI {
    
    private static final long serialVersionUID = 1L;
   
    private String ddatefrom =null;
    private String ddateto = null;
    /**
     * 提高效率
     */
    public String _getModelCode() {
        return WdsWlPubConst.REPORT12;
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
     * @作者：mlr
     * @说明：完达山物流项目  
     * 根据自定义条件得到查询报表数据的SQL
     * @时间：2011-5-10上午09:41:31
     * @param wheresql
     * @return
     */
    public String getQuerySQL(){
    	
    	StringBuffer sql = new StringBuffer(); 
    	
    	sql.append("  select ");
    	sql.append("  min(WDS_TRANPRICEBILL_H.carriersid)  carriersid,");//承运商ID
    	sql.append("  WDS_TRANPRICEBILL_H.vbillno vbillno,");//运费核算单据号    	
    	sql.append("  wds_tanscorp_h.ctranscorpcode ctranscorpcode,");//承运商编码
    	sql.append("  min(wds_tanscorp_h.vtranscorpname) vtranscorpname,");//承运商名称
    	sql.append("  min(wds_tanscorp_h.vtranscorpaddr) vtranscorpaddr,");//承运商地址
    	
//    	sql.append("  min(wds_tanscorp_h.vofficespace) vofficespace,");//办公地点
//    	sql.append("  min(wds_tanscorp_h.vlawpsn) vlawpsn,");//法人
//    	sql.append("  min(wds_tanscorp_h.vemail) vemail,");//邮箱
//    	sql.append("  min(wds_tanscorp_h.vfax) vfax,");//传真
//    	sql.append("  min(wds_tanscorp_h.denddate) denddate,");//合同结束日期
//    	sql.append("  min(wds_tanscorp_h.dstartdate) dstartdate,");//合同开始日期
//    	sql.append("  min(wds_tanscorp_h.pk_stordoc) pk_stordoc,");//承运商关联仓库   	  	
//    	sql.append( " WDS_TRANPRICE_B.pk_destore  pk_destore,");//发货仓库ID
//    	sql.append( " bd_stordocf.storcode storcodef,");//发货仓库编码
//    	sql.append( " bd_stordocf.storname stornamef,");//发货仓库名称     
    	
    	sql.append(" min(wds_tranpricebill_b.pk_trader)  pk_trader,");//发货仓库绑定的经销商ID（即：客商档案ID）（销售出库才客商，其他出库没有）
     	sql.append(" bd_cubasdoc.custcode custcode,");//客商编码
    	sql.append(" min(bd_cubasdoc.custname) custname,");//客商名称
    	sql.append(" min(bd_cubasdoc.conaddr) conaddr,");//客商地址   
    	
    	sql.append(" min(wds_tranpricebill_b.pk_restore) pk_restore,");//收获仓库ID     	
    	sql.append(" bd_stordocs.storcode storcodes,");//收获仓库编码
    	sql.append(" min(bd_stordocs.storname) stornames,");//收获仓库名称     	
    	
    	sql.append(" min(wds_tranpricebill_b.csendareaid) csendareaid,");//表体发货地区   	
    	sql.append(" bd_areaclf.areaclcode areaclcodef,");//发货地区编码
    	sql.append(" min(bd_areaclf.areaclname) areaclnamef,");//发货地区名称    
    	
    	sql.append(" min(wds_tranpricebill_b.creceiverealid) creceiverealid,");//表体收获地区   	    	
    	sql.append(" bd_areacls.areaclcode areaclcodes,");//收获地区编码
    	sql.append(" min(bd_areacls.areaclname) areaclnames,");//收获地区名称        	
    	
    	sql.append(" min(wds_tranpricebill_b.cinvbasdocid)  cinvbasdocid,");//存货基本ID   	
    	sql.append(" bd_invbasdoc.invcode invcode,");//存货编码
    	sql.append(" min(bd_invbasdoc.invname) invname,");//存货名称
    	sql.append(" min(bd_invbasdoc.invspec) invspec,");//规格
    	sql.append(" min(bd_invbasdoc.invtype) invtype,");//型号        	
    	sql.append(" min(wds_tranpricebill_b.cinvmandocid) cinvmandocid,");//存货管理ID
    	
    	sql.append(" min(wds_tranpricebill_b.cpricehid) cpricehid,");//运价表主表ID	
    	sql.append(" min(wds_tranpricebill_b.cpriceid) cpriceid ,");//运价表子表ID 
    	sql.append(" min(wds_tranpricebill_b.nprice) nprice,");//单价
    	
  //  	sql.append(" wds_transprice_h.ipriceunit ipriceunit,");//运价单位  	
    	sql.append(" min(wds_transprice_b.denddate) denddate,");//运输日期 
    	
    	sql.append(" min(wds_tranpricebill_b.ngl) ngl,");//里程数(运费核算单需添加的字段)    	
    	sql.append(" sum(wds_tranpricebill_b.nnum) nnum,");//出库数量
    	sql.append(" sum(wds_tranpricebill_b.nassnum) nassnum,");//出库辅数量 
    	sql.append(" sum(wds_tranpricebill_b.ncolmny) ncolmny,");//计算金额
    	sql.append(" sum(wds_tranpricebill_b.nadjustmny) nadjustmny,");//运费调整额	  
    	sql.append(" sum(wds_tranpricebill_b.nmny) nmny,");//运费   
    	
    	sql.append(" min(wds_tranpricebill_b.pk_cardoc) pk_cardoc,");//车辆信息主键(运费核算单需添加的自段)
    	sql.append(" wds_cardoc_h.ccarcode ccarcode,");//车牌号
    	sql.append(" min(wds_cardoc_h.vcarpsnname) vcarpsnname,");//车主姓名
    	sql.append(" min(wds_cardoc_h.vphone) vphone");//联系电话    
    	
    	sql.append( " from WDS_TRANPRICEBILL_H join wds_tranpricebill_b on" );
    	sql.append( " WDS_TRANPRICEBILL_H.pk_tranpricebill_h=wds_tranpricebill_b.pk_tranpricebill_h");
    	sql.append( " left join wds_tanscorp_h on ");
    	sql.append( " WDS_TRANPRICEBILL_H.carriersid=wds_tanscorp_h.pk_wds_tanscorp_h");//关联 承运商    	
    	sql.append( " left join bd_areacl bd_areaclf on ");// 关联 发货地区
    	sql.append( " WDS_TRANPRICEBILL_b.csendareaid=bd_areaclf.pk_areacl");
    	sql.append( " left join bd_areacl bd_areacls on"); // 关联 收获地区
    	sql.append( " WDS_TRANPRICEBILL_b.creceiverealid=bd_areacls.pk_areacl");
    	sql.append( " left join bd_invbasdoc on"); //关联  存货基本 档案
    	sql.append( " wds_tranpricebill_b.cinvbasdocid=bd_invbasdoc.pk_invbasdoc");
    	sql.append( " left join bd_invmandoc on");//关联 存货管理档案
    	sql.append( " wds_tranpricebill_b.cinvmandocid=bd_invmandoc.pk_invmandoc");
    	sql.append( " left join wds_transprice_h on");//关联 运价表主表
    	sql.append( " wds_tranpricebill_b.cpricehid=wds_transprice_h.pk_wds_transprice_h");
    	sql.append( " left join wds_transprice_b on"); //关联  运价表子表
    	sql.append( " wds_tranpricebill_b.cpriceid=wds_transprice_b.pk_wds_transprice_b");
    	sql.append( " left join bd_stordoc bd_stordocf on");//关联 发货仓库
    	sql.append( " wds_tranpricebill_b.pk_destore=bd_stordocf.pk_stordoc");
    	sql.append( " left join bd_cubasdoc on");//关联  客商
    	sql.append( " wds_tranpricebill_b.pk_trader=bd_cubasdoc.pk_cubasdoc");
    	sql.append( " left join bd_stordoc bd_stordocs on");//关联  收获仓库
    	sql.append( " wds_tranpricebill_b.pk_restore=bd_stordocs.pk_stordoc");
    	sql.append("  left join wds_cardoc_h on");//关联 车辆信息
    	sql.append("  wds_tranpricebill_b.pk_cardoc=wds_cardoc_h.pk_wds_cardoc_h");
    	sql.append(" where ");
    	sql.append( " WDS_TRANPRICEBILL_H.pk_corp='"+_getCorpID()+"'");
    	sql.append( " and isnull(WDS_TRANPRICEBILL_H.dr,0)=0");
    	sql.append( " and WDS_TRANPRICEBILL_H.vbillstatus=1");//过滤审批通过的	
    	sql.append( " and WDS_TRANPRICEBILL_H.dapprovedate ");//审批日期
    	sql.append( " between '"+ddatefrom+"' and '"+ddateto+"'");
    	sql.append( " group by ");
    	sql.append( " WDS_TRANPRICEBILL_H.vbillno,bd_invbasdoc.invcode,bd_areaclf.areaclcode,bd_areacls.areaclcode," +
    			"bd_stordocs.storcode,bd_cubasdoc.custcode,wds_tanscorp_h.ctranscorpcode,wds_cardoc_h.ccarcode");
    	sql.append(" order by WDS_TRANPRICEBILL_H.vbillno");
    	
    
    	
    	
  	
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
