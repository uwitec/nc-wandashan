package nc.ui.wds.report.kcsszt;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.buttonaction.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 库存实时状态报表（物流有效箱粉库存和物流无效箱粉库存）------------------报表
 * @author Administrator
 */
public class ReportUI extends ReportBaseUI {
    
    private static final long serialVersionUID = 1L;

    public String _getModelCode() {
        return WdsWlPubConst.REPORT02;
    }
    
    @Override
    public void init() {
        super.init();
        getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
    }
  
    @Override
    /*去掉小计合计按钮*/
    public int[] getReportButtonAry() {
        return new int[] { 
              IReportButton.QueryBtn, 
              IReportButton.ColumnFilterBtn, 
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
            getQueryDlg().showModal();
            if (getQueryDlg().getResult() == UIDialog.ID_OK) {
                String wheresql = getQueryDlg().getWhereSQL();
                ReportBaseVO[] vos = getReportVO(getQuerySQL(wheresql));
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
        SubtotalVO svo = new SubtotalVO();
        svo.setGroupFldCanNUll(true);// 分组列的数据是否可以为空。
        svo.setAsLeafRs(new boolean[] { true });// 分组列合并后是否作为末级节点记录。
        svo.setValueFlds(new String[] { "nnum","nmny" });// 求值列:
        svo.setValueFldTypes(new int[] { IUFTypes.UFD,IUFTypes.UFD });// 求值列的类型:
        svo.setTotalDescOnFld("invname");// ----合计---字段 ---- 所在列
        setSubtotalVO(svo);
        doSubTotal();
    }
    public String getQuerySQL(String wheresql){
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tb_warehousestock ");
        return sql.toString();
    }
    
    public ReportBaseVO[] getReportVO(String sql) throws BusinessException{
        ReportBaseVO[] reportVOs = null;
        try{
            Class[] ParameterTypes = new Class[]{String.class};
            Object[] ParameterValues = new Object[]{sql};
            Object o = LongTimeTask.calllongTimeService("", this, 
                    "正在查询...", 1, "nc.bs.sdm.pub.report.ReportDMO", null, 
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
    
    @Override
    public void setUIAfterLoadTemplate() {
    }
//    @Override
//    public void onPrint() throws Exception {
//        ReportBaseVO[] baseVOs =(ReportBaseVO[])getReportBase().getBillModel().getBodyValueVOs(ReportBaseVO.class.getName());
//        String chnl_manager = getReportBase().getHeadItem("chnl_manager").getValue();//渠道经理
//        String chnl_area = getReportBase().getHeadItem("chnl_area").getValue();//业务区域
//        String chnl_name = getReportBase().getHeadItem("chnl_name").getValue();//渠道成员
//        String voperatname = ClientEnvironment.getInstance().getUser().getUserName();//操作人
//        String dbilldate = ClientEnvironment.getInstance().getDate().toString();//制单日期
//        //
//        if(baseVOs!=null && baseVOs.length>0){
//            ExportReport export = new ExportReport(this,baseVOs,chnl_manager,chnl_area,voperatname,dbilldate,chnl_name);
//            export.exportData();
//        }
//    }
}
