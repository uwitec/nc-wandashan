package nc.ui.wds.report.cwhdb;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nc.bd.accperiod.AccountCalendar;
import nc.ui.zmpub.pub.report.buttonaction2.CaPuBtnConst;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI3;
/**
 * 账务核对表 
 * 
 * @author liuys
 */
public class ReprotUI extends ZmReportBaseUI3 {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 注册按钮 不需要的按钮可以去掉
	 */
    public int[] getReportButtonAry() {
        m_buttonArray = new int[] { 
        		IReportButton.QueryBtn,    
                IReportButton.LevelSubTotalBtn,  
                IReportButton.CrossBtn,
        		IReportButton.PrintBtn,
        		CaPuBtnConst.onboRefresh,
        		CaPuBtnConst.save,
                };
        return m_buttonArray;
    }
	/**
	 * 接收查询的组合sql 
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		////getSqlout(),getSqlin(),getSqlotherin(),getSqlzk(),getSqlotherout(),
		return new String[]{getSqlbyzkxn(),getSqlxnout(),getSql(),getSqlerp(),getSqltj(),getSqltj1(),getSqlsc(),getSqlsc1(),getSqlwh(),getSqlwh1()
				,getSqlxa(),getSqlxa1(),getSqlcd(),getSqlcd1(),getSqlzz(),getSqlzz1(),getSql2(),getSql21(),getSql3(),getSql4()
				,getSql5(),getSqlmon(),getSql22(),getSql33(),getSql44(),getSqlzk(),getSqlotherout()};
	}
	/**
	 * 设置到ui界面之前 处理分组查询后的数据
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{
         
        //一般要重写该方法   进行数据的组合 过滤 汇总处理
		
//		list.get(0);//的数据为 getSql()方法查询出来的数据
		
//		list.get(1);//的数据为 getSql1()方法查询出来的数据  以此类推
		
		
		//数据处理的方式主要用，这个类的方法
		String[] num_condition_fields = new String[]{"pk_invmandoc"}; 
		String[] combinFields = new String[]{"qtcknum","zknum","wlqtnum","zczfcxnyfnum","byzkxnnum","byqwfnum","byqzgycnum","ninnum","wlkc","erpkc","tjdfnum","scdfnum","whdfnum","xadfnum","cddfnum","zzdfnum","qfxnnum","byqzgnum","byyfxnnum","erpkcnum"}; 
		if(list == null || list.size()==0)
			return null;
		int size = list.size();
		
		ArrayList<ReportBaseVO> al = new ArrayList<ReportBaseVO>();
		for(int i=0;i<size;i++){
			Object o = list.get(i);
			if(o == null)
				continue;
			ReportBaseVO[] rvos = (ReportBaseVO[])o;
			
			if(rvos == null || rvos.length==0)
				continue;
			
			int len = rvos.length;
			
			for(int j=0;j<len;j++){
				al.add(rvos[j]);
			}
		}
		
		if(al == null || al.size()==0)
			return null;
		ReportBaseVO[] vos= nc.vo.zmpub.pub.report2.CombinVO.combinVoByFields(al.toArray(new ReportBaseVO[0]), num_condition_fields, combinFields);
		//
          
		 return vos; //这是默认处理方式
             
	}
	
	/**
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter()throws Exception{
	  //要想自定义交叉 可以调用	nc.vo.zmpub.pub.report2.ReportRowColCrossTool 的onCross的方法

		
		
		
       super.dealQueryAfter();//这是系统自带的 方法 主要用来 将界面的数据  根据配置文件 进行交叉 合计设置。
	}
	
	
	
	/**
	 * 查询现存量
	 * @return
	 * @throws Exception 
	 */
	public String  getSql() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select k.pk_invmandoc as pk_invmandoc,sum(whs_stocktonnage) as wlkc from tb_warehousestock k");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc = k.pk_invmandoc and nvl(invc.dr,0)=0 ");
		strb.append(" where nvl(k.dr,0)=0 and invc.uisso = 'Y' ");
		strb.append(" and k.pk_corp = '"+getCorpPrimaryKey()+"' ");
		strb.append(" and k.pk_customize1 in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and  def1='1' and nvl(c.dr,0)= 0) ");
		
		if(getQuerySQL() !=null && getQuerySQL().length()>0)
			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and k.pk_invmandoc = '"+getDateValue("pk_invbasdoc")+"' ");
		
		strb.append("  group by k.pk_invmandoc ");
		return strb.toString();
	}


       
   /**
   	 * 查询erp现存量
   	 * @return
 * @throws Exception 
   	 */
   	public String  getSqlerp() throws Exception{
   		StringBuffer strb = new StringBuffer();
   		strb.append(" select kp.cinventoryid as pk_invmandoc,  SUM(COALESCE(ninspacenum, 0.0)) - SUM(COALESCE(noutspacenum, 0.0)) as erpkc ");
   		strb.append(" from v_ic_onhandnum6 kp ");
   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc = kp.cinventoryid and nvl(invc.dr,0)=0 ");
   		strb.append(" where kp.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
   		strb.append(" and kp.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and  def1='1' and nvl(c.dr,0)= 0) ");
   		if(getQuerySQL() !=null && getQuerySQL().length()>0)
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and kp.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   		
   		strb.append(" group by kp.cinventoryid ");
   		return strb.toString();
   	}
	
   	/**
	 *查询本月出库 
	 * @return
   	 * @throws Exception 
	 */
//	public String  getSqlout() throws Exception{
//		StringBuffer strb = new StringBuffer();
//   		strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum) as bycknum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
//   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
//   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.vbilltype in('WDSH','WDS6','WDS8') and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//   		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
//   		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
//   		strb.append(" and h.dbilldate >= '"+begindate.toString()+"' and h.dbilldate <= '"+enddate.toString()+"' ");
//   		
//   		if(getQuerySQL() !=null && getQuerySQL().length()>0){
// 		   strb.append(" and h.dbilldate >='"+getDateValue("startdate")+"' " );
// 		   strb.append(" and h.dbilldate <='"+getDateValue("enddate")+"' " );
// 		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
// 		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
// 	   }
//   		
//   		strb.append("  group by b.cinventoryid ");
//   		return strb.toString();
//	}
	
	/**
	 *查询本月入库 
	 * @return
	 * @throws Exception 
	 */
//	public String  getSqlin() throws Exception{
//		StringBuffer strb = new StringBuffer();
//   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as byrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
//   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.geb_cinventoryid and nvl(invc.dr,0)=0 ");
//   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.geh_billtype in('WDS9','WDSZ','WDS7')  and h.geh_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//     		if(getQuerySQL() !=null && getQuerySQL().length()>0){
//   			String wsql="";
//   			ConditionVO[] vos=getQueryDlg().getConditionVO();
//   			for(int i=0;i<vos.length;i++){
//   				if(vos[i].getFieldCode().equals("startdate")){
//   					wsql=wsql+" and h.geh_dbilldate >= '"+vos[i].getValue()+"'";
//   				}
//   				if(vos[i].getFieldCode().equals("enddate")){
//   					wsql=wsql+" and h.geh_dbilldate <= '"+vos[i].getValue()+"'";					
//   				}
//   			}  
//   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
//   			strb.append(" and b.geb_cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
//   			strb.append(wsql);
//   		}   		
//  		strb.append("  group by b.geb_cinventoryid ");
//   		return strb.toString();
//	}
	
	//根据对话框的字段取值
	private String getDateValue(String fieldcode) {
		ConditionVO[] vos=getQueryDlg().getConditionVO();
			for(int i=0;i<vos.length;i++){
				if(vos[i].getFieldCode().equals(fieldcode)){
					return vos[i].getValue();
				}
			}  
		return null;
	}
	/**
	 *查询本月其他入库
	 * @return
	 * @throws Exception 
	 */
//	public String  getSqlotherin() throws Exception{
//		StringBuffer strb = new StringBuffer();
//   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as qtrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
//   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.geb_cinventoryid and nvl(invc.dr,0)=0 ");
//   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.geh_billtype in('WDS7') and h.geh_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//   		if(getQuerySQL() !=null && getQuerySQL().length()>0){
//   			String wsql="";
//   			ConditionVO[] vos=getQueryDlg().getConditionVO();
//   			for(int i=0;i<vos.length;i++){
//   				if(vos[i].getFieldCode().equals("startdate")){
//   					wsql=wsql+" and h.geh_dbilldate >= '"+vos[i].getValue()+"'";
//   				}
//   				if(vos[i].getFieldCode().equals("enddate")){
//   					wsql=wsql+" and h.geh_dbilldate <= '"+vos[i].getValue()+"'";					
//   				}
//   			}
//   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
//   			strb.append(" and b.geb_cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
//   			strb.append(wsql);
//   		}
//   		strb.append("  group by b.geb_cinventoryid ");
//   		return strb.toString();
//	}
	
	
	/**
	 * 查天津分仓待发量 没生成出库的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqltj() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as tjdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%天津%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%天津%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查天津分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqltj1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as tjdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%天津%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查天郑州分仓待发量
	 * @return
	 */
	public String  getSqlzz() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as zzdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%郑州%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%郑州%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查郑州分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlzz1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as zzdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and  storh.pk_stordoc  in (select c.pk_stordoc from bd_stordoc c where c.storname like '%郑州%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查武汉分仓待发量 没生成出库的
	 * @return
	 */
	public String  getSqlwh() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as whdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%武汉%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%武汉%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查武汉分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlwh1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as whdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%武汉%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查成都分仓待发量 没生成出库的
	 * @return
	 */
	public String  getSqlcd() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as cddfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%成都%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%成都%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查成都分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlcd1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as cddfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%成都%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查西安分仓待发量 没生成出库的
	 * @return
	 */
	public String  getSqlxa() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as xadfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%西安%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%西安%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		}
		
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查西安分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlxa1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as xadfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%西安%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查双城待发量 没生成出库的
	 * @return
	 */
	public String  getSqlsc() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as scdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%双城%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		}
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查双城分仓待发量 生成出库剩余的
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlsc1() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(noutnum) as scdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where e.csaleid in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk   and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and  storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and  def1='1' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and e.dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and e.dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		} 
		strb.append("  group by b.cinventoryid ");	
		return strb.toString();
	}
	
	/**
	 * 查询本月前欠发虚拟数 没有生成出库单的
	 * @return
	 * @throws Exception 
	 */
    public String  getSql2() throws Exception{

    	StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as qfxnnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
			 String startdate =getDateValue("startdate");
		     String sdate = getDate(startdate,-6).toString();
		     strb.append(" and e.dbilldate >='"+sdate+"' " );
			 strb.append(" and e.dbilldate <'"+startdate+"' " );
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   		}
		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
    

	/**
	 * 查询本月前欠发虚拟数 生成出库单 生成出库数而没有出完的
	 * @return
	 * @throws Exception 
	 */
	public String  getSql21() throws Exception{
	
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(ww.noutnum) as qfxnnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
	//	strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0)) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
			 String startdate =getDateValue("startdate");
		     String sdate = getDate(startdate,-6).toString();
		     strb.append(" and e.dbilldate >='"+sdate+"' " );
			 strb.append(" and e.dbilldate <'"+startdate+"' " );
				if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
				strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
			}
		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}

    /**
     * 查询所有erp虚拟转库单的量()前6个月
     * @return
     * @throws Exception 
     */
    
   public String  getSqlzk() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   //取的应转数量
	   strb.append(" select b.cinventoryid  as pk_invmandoc,sum(b.dshldtransnum) as zknum from ic_special_h h join ic_special_b b on h.cspecialhid = b.cspecialhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where isnull(h.dr,0)=  0 and isnull(b.dr,0)=0 and h.pk_defdoc11 ='0001S3100000000OK5HM' and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		  String startdate =getDateValue("startdate");
	      String sdate = getDate(startdate,-6).toString();
	      strb.append(" and h.dbilldate >='"+sdate+"' " );
		  strb.append(" and h.dbilldate <='"+startdate+"' " );
		  if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
			  strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   
   private UFDate getDate( String startdate,int m) {
	   Calendar c = Calendar.getInstance();//获得一个日历的实例
       Date date =new UFDate(startdate).toDate();
       c.setTime(date);//设置日历时间
       c.add(Calendar.MONTH,m);//在日历的月份上增加m个月  
       return new UFDate(c.getTimeInMillis());
   }
   
   /**
    * 查询物流其他出库虚拟量 前6个月
    * @return
 * @throws Exception 
    */
   
  public String  getSqlotherout() throws Exception{
	  StringBuffer strb = new StringBuffer();
 	  strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum)  as qtcknum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
 	  strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
 	  strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.vbilltype in ('WDS6') and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y' and h.isxnap ='Y' ");
 	  if(getQuerySQL() !=null && getQuerySQL().length()>0){
 		  String startdate =getDateValue("startdate");
	      String sdate = getDate(startdate,-6).toString();
		  strb.append(" and h.dbilldate >='"+sdate+"' " );
		  strb.append(" and h.dbilldate <='"+startdate+"' " );
		  if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
			  strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
 	  strb.append("  group by b.cinventoryid ");
 	  return strb.toString();
	}
  
    /**
     * 查询本月前暂估数量
     * @return
     * @throws Exception 
     */
   public String  getSql3() throws Exception{
 	 StringBuffer strb = new StringBuffer();
	   strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.ninnum) as byqzgnum from ic_general_b b join ic_general_h h on h.cgeneralhid=b.cgeneralhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.cbilltypecode='45' and h.pk_corp ='"+getCorpPrimaryKey()+"'  and h.pk_defdoc11 ='0001S3100000000OK5HM' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   String startdate =getDateValue("startdate");
		   String sdate = getDate(startdate,-6).toString();
		   strb.append(" and h.dbilldate >='"+sdate+"' " );
		   strb.append(" and h.dbilldate <='"+startdate+"' " );
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
	 	return strb.toString();
	}
    
    /**
     * 本月前暂估已冲数量
     * 
     * 
     * 查询ERP其他入库标示为虚拟的量
     * @return
     * @throws Exception 
     */
   public String  getSql4() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.ninnum) as ninnum from ic_general_b b join ic_general_h h on h.cgeneralhid=b.cgeneralhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.cbilltypecode='4A' and h.pk_corp ='"+getCorpPrimaryKey()+"'  and h.pk_defdoc11 ='0001S3100000000OK5HM' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   strb.append(" and h.dbilldate >= '"+getDateValue("startdate")+"' " );
		   strb.append(" and h.dbilldate <= '"+getDateValue("enddate")+"' ");
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
	 	return strb.toString();
	}
   
   /**
    * 本月前暂估已冲数量
    * 
    * @return
    * @throws Exception 
    */
  public String  getSql22() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.ninnum) as byqzgycnum from ic_general_b b join ic_general_h h on h.cgeneralhid=b.cgeneralhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.cbilltypecode='4A' and h.pk_corp ='"+getCorpPrimaryKey()+"'  and h.pk_defdoc11 ='0001S3100000000OK5HM' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   String startdate =getDateValue("startdate");
		   String sdate = getDate(startdate,-6).toString();
		   strb.append(" and h.dbilldate >='"+sdate+"' " );
		   strb.append(" and h.dbilldate <='"+startdate+"' " );
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
	 	return strb.toString();
	}
  
   /**
	 * 查询本月已发虚拟数
	 * @return
	 * @throws Exception 
	 */
   public String  getSql5() throws Exception{
	   StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(ww.noutnum) as byyfxnnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
			 String startdate =getDateValue("startdate");
		     strb.append(" and e.dbilldate >='"+startdate+"' " );
			 strb.append(" and e.dbilldate <='"+startdate+"' " );
				if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
				strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
			}
		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   
	/**
	 * 查询本月未发虚拟数 没有生成出库单的
	 * @return
	 * @throws Exception 
	 */
   public String  getSql33() throws Exception{

   	StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as byqwfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" where b.corder_bid not in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0) ");
		strb.append(" and  storh.pk_stordoc in(select c.pk_stordoc  from bd_stordoc c where c.storname like '%双城%' and def1 = '1' and nvl(c.dr, 0) = 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
			 String startdate =getDateValue("startdate");
		     strb.append(" and e.dbilldate >='"+startdate+"' " );
			 strb.append(" and e.dbilldate <='"+startdate+"' " );
  			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
  		}
		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   

	/**
	* 查询本月未发虚拟数 生成出库单的
	* @return
	* @throws Exception 
	*/
	   public String  getSql44() throws Exception{
	
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber)-sum(ww.noutnum) as byqwfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" join tb_storcubasdoc storb on e.ccustomerid = storb.pk_cumandoc and nvl(storb.dr,0)=0 ");
		strb.append(" join wds_storecust_h storh on storb.pk_wds_storecust_h = storh.pk_wds_storecust_h and  nvl(storh.dr,0)=0 ");
		strb.append(" join (select wb.cfirstbillbid,sum(wb.noutnum) noutnum from wds_soorder_b wb where nvl(wb.dr,0)=0 group by wb.cfirstbillbid) ww ");
		strb.append(" on ww.cfirstbillbid = b.corder_bid ");
		strb.append(" where b.corder_bid in (select distinct gb.cfirstbillbid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and storh.pk_stordoc in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0) ");
		if(getQuerySQL() !=null && getQuerySQL().length()>0){
			 String startdate =getDateValue("startdate");
		     strb.append(" and e.dbilldate >='"+startdate+"' " );
			 strb.append(" and e.dbilldate <='"+startdate+"' " );
				if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
				strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
			}
		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   
   
   /**
    * 查询所有erp虚拟转库单的量(本月)
    * @return
    * @throws Exception 
    */
   
  public String  getSqlbyzkxn() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   //取的应转数量
	   strb.append(" select b.cinventoryid  as pk_invmandoc,sum(b.dshldtransnum) as byzkxnnum from ic_special_h h join ic_special_b b on h.cspecialhid = b.cspecialhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where isnull(h.dr,0)=  0 and isnull(b.dr,0)=0 and h.pk_defdoc11 ='0001S3100000000OK5HM' and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		  strb.append(" and h.dbilldate >= '" + getDateValue("startdate")+ "' ");
			strb.append(" and h.dbilldate <= '" + getDateValue("enddate")+ "' ");
		  if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
			  strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
  
	/**
	 *查询物流本月总仓转分仓虚拟已发数
	 *虚拟的, 转分仓类型的,双城库的 
	 * @return
	 * @throws Exception 
	 */
	public String getSqlxnout() throws Exception {
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum) as zczfcxnyfnum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0  ");
		strb.append(" where h.cdispatcherid='1021A2100000000FAI3G' and nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.isxnap ='Y' and h.vbilltype in('WDS6') and h.pk_corp ='"+ getCorpPrimaryKey() + "' and invc.uisso = 'Y'");
		strb.append(" and h.srl_pk in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and nvl(c.dr,0)= 0) ");
		if (getQuerySQL() != null && getQuerySQL().length() > 0) {
			strb.append(" and h.dbilldate >= '" + getDateValue("startdate")+ "' ");
			strb.append(" and h.dbilldate <= '" + getDateValue("enddate")+ "' ");
			if (PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
				strb.append(" and b.cinventoryid = '"+ getDateValue("pk_invbasdoc") + "' ");
		}

		strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   
	/**
	 *查询ERP库存(虚拟后库存)
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlmon() throws Exception{
		StringBuffer strb = new StringBuffer();
  		strb.append(" select t.cinventoryid as pk_invmandoc,sum(t.nabnum) as erpkcnum  from ia_periodaccount t   ");
  		 strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  t.cinventoryid and nvl(invc.dr,0)=0 ");
  		strb.append(" where  nvl(t.dr,0)=0 and t.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y' ");
  		String mon =AccountCalendar.getInstance().getMonthVO().getBegindate().getStrMonth();
  		int year =AccountCalendar.getInstance().getMonthVO().getBegindate().getYear();
  		strb.append(" and t.caccountmonth = '"+mon+"' and t.caccountyear = '"+year+"' ");
  		
  		if(getQuerySQL() !=null && getQuerySQL().length()>0)
  			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  				strb.append(" and t.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
  		
  		strb.append("  group by t.cinventoryid ");
  		return strb.toString();
	}
	
   @Override
	public String _getModelCode() {
		return WdsWlPubConst.zwhdb;
	}
	
}
