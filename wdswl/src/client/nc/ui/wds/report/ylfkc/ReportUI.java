package nc.ui.wds.report.ylfkc;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
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
 * 原料粉收发存汇总表------------------报表
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
        return WdsWlPubConst.REPORT04;
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
        //表体栏目分组设置
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        ColumnGroup[] card2 = new ColumnGroup[4];
        //
        card2[0]=new ColumnGroup("期初结存");
        card2[0].add(cardTcm.getColumn(9));
        card2[0].add(cardTcm.getColumn(10));
        cardHeader.addColumnGroup(card2[0]);
        //
        card2[1]=new ColumnGroup("本期收入");
        card2[1].add(cardTcm.getColumn(11));
        card2[1].add(cardTcm.getColumn(12));
        cardHeader.addColumnGroup(card2[1]);
        //
        card2[2]=new ColumnGroup("本期发出");
        card2[2].add(cardTcm.getColumn(13));
        card2[2].add(cardTcm.getColumn(14));
        cardHeader.addColumnGroup(card2[2]);
        //
        card2[3]=new ColumnGroup("期末结存");
        card2[3].add(cardTcm.getColumn(15));
        card2[3].add(cardTcm.getColumn(16));
        cardHeader.addColumnGroup(card2[3]);
        getReportBase().getBillModel().updateValue();
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
              IReportButton.RefreshBtn,
              IReportButton.SubTotalBtn
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
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
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
    	//--外层查询,生成报表vo
    	sql.append("select ");
    	sql.append(" bd_stordoc.pk_stordoc pk_stordoc, ");//仓库Id(模板隐藏)
    	sql.append(" bd_cargdoc.pk_cargdoc pk_cargdoc, ");//货位id(模板隐藏)
    	sql.append(" bd_invmandoc.pk_invmandoc pk_invmandoc,");//存货id(模板隐藏)
        sql.append(" bd_invmandoc.pk_invbasdoc pk_invbasdoc,");//存货基本id(模板隐藏)
        sql.append(" bd_stordoc.storname storname,");//仓库名称
        sql.append(" bd_stordoc.storcode storcode,");//仓库编码
        sql.append(" bd_cargdoc.csname cargname,");//货位名称
        sql.append(" bd_invbasdoc.invname invname,");//存货名称
        sql.append(" bd_invbasdoc.invcode invcode,");//存货编码
        sql.append(" bd_invbasdoc.invspec invspec,");//规格
        sql.append(" bd_invbasdoc.invtype invtype,");//型号
        sql.append(" meas1.measname unitname,");//辅单位
        sql.append(" meas2.measname assunitname,");//主单位
        sql.append(" sum(A.ninnum) ninnum,");//本期入数量
        sql.append(" sum(A.ninassnum) ninassnum,");//本期入辅数量
        sql.append(" sum(A.noutnum) noutnum,");//本期出数量
        sql.append(" sum(A.noutassnum) noutassnum,");//本期出辅数量
        sql.append(" sum(A.nendnum) nendnum,");//本期结存
        sql.append(" sum(A.nendassnum) nendassnum");//本期结存
    	sql.append(" from (");
    	//----1查询出库单 本期出库数量开始
    	sql.append("(");
    	sql.append("select ");
    	//sql.append(" tb_outgeneral_h.srl_pk pk_stordoc,");//仓库id
    	sql.append(" tb_outgeneral_h.pk_cargdoc pk_cargdoc,");//货位id
    	sql.append(" tb_outgeneral_b.cinventoryid pk_invmandoc,");//存货管理id
        sql.append(" null ninnum,");
        sql.append(" null ninassnum,");
        sql.append(" tb_outgeneral_b.noutnum noutnum,");//本期发出
        sql.append(" tb_outgeneral_b.noutassistnum noutassnum,");//本期发出数量
        sql.append(" null nendnum,");
        sql.append(" null nendassnum");
        sql.append("  from tb_outgeneral_h ");//--出库主表
        sql.append(" join  tb_outgeneral_b ");//--出库子表
        sql.append(" on  tb_outgeneral_h.general_pk = tb_outgeneral_b.general_pk and isnull(tb_outgeneral_b.dr,0)=0");
        sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus=1");//过滤条件：审核通过
        sql.append(" and dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );
        sql.append(" and pk_corp='"+_getCorpID()+"'");//过滤条件公司
        sql.append(")");
        //----1查询出库单 本期出库数量结束
        sql.append(" union ");
       //----2查询入库单 本期入库数量开始
        sql.append("(");
        sql.append("select ");
      	//sql.append("  tb_general_h.geh_cwarehouseid pk_stordoc,");//仓库id
    	sql.append("  tb_general_h.pk_cargdoc pk_cargdoc,");//货位id
    	sql.append(" tb_general_b.geb_cinventoryid pk_invmandoc,");//存货管理id
        sql.append(" tb_general_b.geb_anum ninnum,");//本期收入
        sql.append(" tb_general_b.geb_banum ninassnum,");//本期收入辅数量
        sql.append(" null noutnum,");
        sql.append(" null noutassnum,");
        sql.append(" null nendnum,");
        sql.append(" null nendassnum");
        sql.append(" from tb_general_h ");//--入库主表
        sql.append(" join tb_general_b");//--入库子表
        sql.append(" on tb_general_h.geh_pk=tb_general_b.geh_pk and isnull(tb_general_b.dr,0)=0");
        sql.append(" where isnull(tb_general_h.dr,0)=0 and tb_general_h.pwb_fbillflag=1");
        sql.append(" and geh_dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );
        sql.append(" and pk_corp='"+_getCorpID()+"'");//过滤条件:公司
        sql.append(")");
        //----2查询入库单 本期入库数量结束
        sql.append(" union ");
        //----3查询存货状态表 期末结存
        sql.append("(");
        sql.append("select ");
      	//sql.append("  tb_general_h.geh_cwarehouseid pk_stordoc,");//仓库id
    	sql.append("   tb_warehousestock.pk_cargdoc pk_cargdoc,");//货位id
    	sql.append(" tb_warehousestock.pk_invmandoc pk_invmandoc,");//存货管理id
        sql.append(" null ninnum,");
        sql.append(" null ninassnum,");
        sql.append(" null noutnum,");
        sql.append(" null noutassnum,");
        sql.append(" tb_warehousestock.whs_stocktonnage nendnum,");//本期结存
        sql.append(" tb_warehousestock.whs_stockpieces nendassnum");//本期结存
        sql.append(" from tb_warehousestock ");//--查询存货状态表
        sql.append(" where isnull(tb_warehousestock.dr,0)=0");
        sql.append(" and pk_corp='"+_getCorpID()+"'");//过滤条件:公司
        sql.append(")");
        //----3查询存货状态表 期末结存  
        sql.append(") A ");
        //--最外层查询,生成报表vo
        sql.append(" left join bd_cargdoc ");//--货位档案
        sql.append(" on A.pk_cargdoc =bd_cargdoc.pk_cargdoc and isnull(bd_cargdoc.dr,0)=0");//
        sql.append(" left join bd_stordoc ");//--仓库档案
        sql.append(" on bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and isnull(bd_stordoc.dr,0)=0");
        sql.append(" left join  bd_invmandoc ");//--存货管理id
        sql.append(" on A.pk_invmandoc =bd_invmandoc.pk_invmandoc and isnull(bd_invmandoc.dr,0)=0");
        sql.append(" join bd_invbasdoc ");//--存货基本id
        sql.append(" on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc and isnull(bd_invbasdoc.dr,0)=0");
        sql.append(" join bd_measdoc meas1 ");//--计量档案（主）
        sql.append(" on bd_invbasdoc.pk_measdoc=meas1.pk_measdoc and isnull(meas1.dr,0)=0");
        sql.append(" join bd_measdoc meas2 ");//--计量档案（辅）
        sql.append(" on bd_invbasdoc.pk_measdoc1=meas2.pk_measdoc and isnull(meas2.dr,0)=0");
        sql.append(" group by bd_stordoc.pk_stordoc,bd_cargdoc.pk_cargdoc,bd_invmandoc.pk_invmandoc,");
        sql.append(" bd_invmandoc.pk_invbasdoc ,");
        sql.append(" bd_stordoc.storname ,");
        sql.append(" bd_stordoc.storcode ,");
        sql.append(" bd_cargdoc.csname ,");
        sql.append(" bd_invbasdoc.invname ,");
        sql.append(" bd_invbasdoc.invcode ,");
        sql.append(" bd_invbasdoc.invspec ,");
        sql.append(" bd_invbasdoc.invtype ,");
        sql.append(" meas1.measname,");
        sql.append(" meas2.measname ");
        sql.append(" order by storname, cargname,invcode");
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
        SubtotalVO svo = new SubtotalVO();
        svo.setGroupFldCanNUll(true);// 分组列的数据是否可以为空。
        svo.setAsLeafRs(new boolean[] { true });// 分组列合并后是否作为末级节点记录。
        svo.setValueFlds(new String[] { "nstartnum","nstartassnum","ninnum","ninassnum","noutnum","noutassnum","nendnum","nendassnum",});// 求值列:
        svo.setValueFldTypes(new int[] { IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD });// 求值列的类型:
        svo.setTotalDescOnFld("invname");// ----合计---字段 ---- 所在列
        setSubtotalVO(svo);
        doSubTotal();
    }
}
