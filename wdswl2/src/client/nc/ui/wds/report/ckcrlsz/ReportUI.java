package nc.ui.wds.report.ckcrlsz;

import javax.swing.ListSelectionModel;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.IUFTypes;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report.SubtotalVO;
import nc.vo.zmpub.pub.report2.ReportBaseUI;

/**
 * 各个仓库出入库流水账--报表
 * @author Administrator
 */
public class ReportUI extends ReportBaseUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String ddatefrom =null;
    private String ddateto = null;

	@Override
	public String _getModelCode() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.REPORT08;
	}
	@Override
	public void setUIAfterLoadTemplate() {
        getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
	}
	
	@Override
	public ReportBaseVO[] getReportVO(String sql) throws BusinessException {
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

	@Override
	public void onQuery() {
		try{
			getQueryDlg().showModal();
		     if (getQueryDlg().getResult() == UIDialog.ID_OK) {
            	//校验开始日期，截止日期
            	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatefrom");
            	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddateto");
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
            	if(!qryconditons.contains("截止日期")){
            		qryconditons = qryconditons+"并且(截止日期 小于等于 '"+ddateto+"')";
            	}
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
            	getReportBase().getHeadItem("qryconditons").setWidth(2);
            	getReportBase().setHeadItem("qryconditons", qryconditons);
            	//得到自定义查询条件
                //得到查询结果
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                if(vos != null){                	
                	//List<ReportBaseVO> list= Arrays.asList(vos);
                	//ReportBaseVO subTotal1 = new ReportBaseVO();
                	//subTotal1.setAttributeValue("", "");
					super.updateBodyDigits();
					setReportBaseVO(vos);
					setBodyVO(vos);
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
	private String getQuerySQL(){
		StringBuffer sql = new StringBuffer();
		//--外层查询,生成报表vo
		sql.append(" select ");
		sql.append(" A.cgeneralhid cgeneralhid,");
		sql.append(" A.cgeneralbid cgeneralbid,");
		sql.append(" A.dbilldate dbilldate,");//单据日期
		sql.append(" case when upper(A.dbilltype)='WDS6' then '其他出库'");
		sql.append(" when upper(A.dbilltype)='WDS7' then '其他入库'");
		sql.append(" when upper(A.dbilltype)='WDS8' then '销售出库'");
		sql.append(" when upper(A.dbilltype)='WDS9' then '调拨入库' end "); 
		sql.append("  dbilltype,");//单据类型
		sql.append(" A.vbillno vbillno,");//单据号
		sql.append(" bd_rdcl.rdname rdname,");//收发类别
		sql.append(" bd_stordoc.storcode storcode,");//仓库编码
		sql.append(" bd_stordoc.storname storname,");//仓库名称
		sql.append(" bd_cargdoc.csname cargname,");//货位名称
		sql.append(" bd_deptdoc.deptname deptname,");//部门名称
		sql.append(" bd_psndoc.psnname psnname,");//库管员名称
		sql.append(" bd_invcl.invclasscode invclasscode,");//存货分类编码
		sql.append(" bd_invcl.invclassname invclassname,");//存货分类名称
		sql.append(" bd_invbasdoc.invname invname,");//存货名称
        sql.append(" bd_invbasdoc.invcode invcode,");//存货编码
        sql.append(" bd_invbasdoc.invspec invspec,");//规格
        sql.append(" bd_invbasdoc.invtype invtype,");//型号
        sql.append(" meas1.measname unitname,");//辅单位
        sql.append(" meas2.measname assunitname,");//主单位
		sql.append(" A.nhsl nhsl,");//换算率
		sql.append(" A.isgift isgift,");//是否赠品
		sql.append(" A.vbatchcode vbatchcode,");//批次号
		sql.append(" A.ninnum ninnum,");//人库数量
		sql.append(" A.nassinnum nassinnum,");//人库辅数量
		sql.append(" A.noutnum  noutnum,");//出库数量
		sql.append(" A.nassoutnum nassoutnum,");//出库辅数量
		sql.append(" A.vsourcebillcode vsourcebillcode,");
		sql.append(" A.nshouldnum nshouldnum,");//应发数量
		sql.append(" A.nassshouldnum nassshouldnum");//应发辅数量
		sql.append(" from (");
		//--1查询出库单开始
		sql.append("(");
		sql.append("select ");
		sql.append(" tb_outgeneral_b.general_pk cgeneralhid,");//单据id
		sql.append(" tb_outgeneral_b.general_b_pk cgeneralbid,");//单据子表id
		sql.append(" tb_outgeneral_h.dbilldate dbilldate,");//单据日期
		sql.append(" tb_outgeneral_h.vbilltype dbilltype,");//单据类型
		sql.append(" tb_outgeneral_h.vbillcode vbillno,");//单据号
		sql.append(" tb_outgeneral_h.cdispatcherid pk_rdcl,");//收发类别id
		sql.append(" tb_outgeneral_h.srl_pk pk_stordoc,");//仓库id出库仓库
		sql.append(" tb_outgeneral_h.pk_cargdoc pk_cargdoc,");//货位id
		sql.append(" tb_outgeneral_h.cdptid pk_deptdoc,");//部门id
		sql.append(" tb_outgeneral_h.cwhsmanagerid pk_psndoc,");//库管员id
		sql.append(" tb_outgeneral_b.cinventoryid pk_invmandoc,");//存货管理id
		sql.append("  null isgift,");//是否赠品
		sql.append(" tb_outgeneral_b.hsl nhsl,");//换算率
		sql.append(" tb_outgeneral_b.vbatchcode vbatchcode,");//批次号
		sql.append(" null ninnum,");
		sql.append(" null nassinnum,");
		sql.append(" tb_outgeneral_b.noutnum noutnum,");//出库数量
		sql.append(" tb_outgeneral_b.noutassistnum nassoutnum,");//出库辅数量
		sql.append(" tb_outgeneral_b.vsourcebillcode vsourcebillcode,");//来源单据号
		sql.append(" tb_outgeneral_b.nshouldoutnum nshouldnum,");//应发数量
		sql.append(" tb_outgeneral_b.nshouldoutassistnum nassshouldnum");//应发辅数量
		sql.append(" from tb_outgeneral_h");//--出库单主表
		sql.append(" join  tb_outgeneral_b ");//--出库子表
        sql.append(" on  tb_outgeneral_h.general_pk = tb_outgeneral_b.general_pk and isnull(tb_outgeneral_b.dr,0)=0");
        sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus=1");//过滤条件：审核通过
        sql.append(" and dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );//过滤条件:单据日期 
        sql.append(" and pk_corp='"+_getCorpID()+"'");//过滤条件:公司
        sql.append(")");
        //--1查询出库单结算
        sql.append(" union ");
		//--2查询入库单开始
		sql.append("(");
		sql.append("select ");
		sql.append(" tb_general_b.geh_pk cgeneralhid,");//单据id
		sql.append(" tb_general_b.geb_pk cgeneralbid,");//单据子表id
		sql.append(" tb_general_h.geh_dbilldate dbilldate,");//单据日期
		sql.append(" tb_general_h.geh_cbilltypecode dbilltype,");//单据类型
		sql.append(" tb_general_h.geh_billcode vbillno,");//单据号
		sql.append(" tb_general_h.geh_cdispatcherid pk_rdcl,");//收发类别id
		sql.append(" tb_general_h.geh_cwarehouseid pk_stordoc,");//仓库id入库仓库
		sql.append(" tb_general_h.pk_cargdoc pk_cargdoc,");//货位id
		sql.append(" tb_general_h.geh_cdptid pk_deptdoc,");//部门id
		sql.append(" tb_general_h.geh_cwhsmanagerid pk_psndoc,");//库管员id
		sql.append(" tb_general_b.geb_cinventoryid pk_invmandoc,");//存货管理id
		sql.append("  tb_general_b.geb_flargess isgift,");//是否赠品
		sql.append(" tb_general_b.geb_hsl nhsl,");//换算率
		sql.append(" tb_general_b.geb_vbatchcode vbatchcode,");//批次号
		sql.append(" tb_general_b.geb_anum ninnum,");
		sql.append(" tb_general_b.geb_banum nassinnum,");
		sql.append(" null noutnum,");//出库数量
		sql.append(" null nassoutnum,");//出库辅数量
		sql.append(" tb_general_b.vsourcebillcode vsourcebillcode,");//来源单据号
		sql.append(" tb_general_b.geb_snum nshouldnum,");//应发数量
		sql.append(" tb_general_b.geb_bsnum nassshouldnum");//应发辅数量
		sql.append(" from tb_general_h ");//--入库主表
        sql.append(" join tb_general_b");//--入库子表
        sql.append(" on tb_general_h.geh_pk=tb_general_b.geh_pk and isnull(tb_general_b.dr,0)=0");
        sql.append(" where isnull(tb_general_h.dr,0)=0 and tb_general_h.pwb_fbillflag=1");//过滤条件：审核通过
        sql.append(" and geh_dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );//过滤条件:单据日期 
        sql.append(" and pk_corp='"+_getCorpID()+"'");//过滤条件:公司
        sql.append(")");
        //--2查询入库单结算
        sql.append(")A");
        //--外层查询,生成报表vo
        sql.append(" left join bd_rdcl");//--收发类别表
        sql.append(" on A.pk_rdcl = bd_rdcl.pk_rdcl ");
        sql.append(" left join bd_deptdoc ");//--部门档案
        sql.append(" on A.pk_deptdoc =bd_deptdoc.pk_deptdoc");
        sql.append(" left join bd_psndoc");//--人员档案
        sql.append(" on A.pk_psndoc = bd_psndoc.pk_psndoc");
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
        sql.append(" join bd_invcl ");//--存货分类
        sql.append(" on  bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl");
		return sql.toString();
	}
	 /**
	  * 
	  * @作者：lyf
	  * @说明：完达山物流项目:设置序号
	  * @时间：2011-5-11下午02:13:25
	  * @param vos
	  */
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
