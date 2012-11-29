package nc.ui.wds.report.cwhdb;


import java.util.ArrayList;
import java.util.List;

import nc.bd.accperiod.AccountCalendar;
import nc.ui.zmpub.pub.report.buttonaction2.CaPuBtnConst;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.vo.pub.lang.UFDate;
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
		return new String[]{getSql(),getSqlerp(),getSqlout(),getSqlin(),getSqlotherin(),getSqltj(),getSqlsc(),getSqlwh()
				,getSqlxa(),getSqlxa(),getSqlcd(),getSqlzz()};
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
		
		list.get(0);//的数据为 getSql()方法查询出来的数据
		
//		list.get(1);//的数据为 getSql1()方法查询出来的数据  以此类推
		
		
		//数据处理的方式主要用，这个类的方法
		String[] num_condition_fields = new String[]{"pk_invmandoc"}; 
		String[] combinFields = new String[]{"wlkc","erpkc","zczfcnum","qfxnnum","byqzgnum","byqzgycnum","byqzgwcnum","bycknum",
				"byrknum","qtrknum"}; 
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
	 */
	public String  getSql(){
		
		return " select pk_invmandoc as pk_invmandoc,sum(whs_stockpieces) as wlkc from tb_warehousestock where isnull(dr,0)=0 group by pk_invmandoc  ";
	}


       
   /**
   	 * 查询erp现存量
   	 * @return
   	 */
   	public String  getSqlerp(){
   		StringBuffer strb = new StringBuffer();
   		strb.append(" select kp.cinventoryid as pk_invmandoc,  SUM(COALESCE(ninspacenum, 0.0)) - SUM(COALESCE(noutspacenum, 0.0)) as erpkc ");
   		strb.append(" from v_ic_onhandnum6 kp  group by kp.cinventoryid");
   		return strb.toString();
   	}
	
   	/**
	 *查询本月chuku 
	 * @return
	 */
	public String  getSqlout(){
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum) as bycknum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
   		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbilltype in('WDSH','WDS6','WDS8') ");
   		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
   		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
   		strb.append(" and h.dbilldate >= '"+begindate.toString()+"' and h.dbilldate <= '"+enddate.toString()+"' ");
   		strb.append("  group by b.cinventoryid ");
   		return strb.toString();
	}
	
	/**
	 *查询本月ruku 
	 * @return
	 */
	public String  getSqlin(){
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as byrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
   		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.geh_billtype in('WDS9','WDSZ','WDS7')  ");
   		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
   		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
   		strb.append(" and h.geh_dbilldate >= '"+begindate.toString()+"' and h.geh_dbilldate <= '"+enddate.toString()+"' ");
   		strb.append("  group by b.geb_cinventoryid ");
   		return strb.toString();
	}
	
	/**
	 *查询本月qitaruku
	 * @return
	 */
	public String  getSqlotherin(){
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as qtrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
   		strb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.geh_billtype in('WDS7') ");
   		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
   		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
   		strb.append(" and h.geh_dbilldate >= '"+begindate.toString()+"' and h.geh_dbilldate <= '"+enddate.toString()+"' ");
   		strb.append("  group by b.geb_cinventoryid ");
   		return strb.toString();
	}
	
	
	/**
	 * 查天津分仓待发量
	 * @return
	 */
	public String  getSqltj(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as tjdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%天津%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查天郑州分仓待发量
	 * @return
	 */
	public String  getSqlzz(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as zzdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0 ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%郑州%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查武汉分仓待发量
	 * @return
	 */
	public String  getSqlwh(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as whdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%武汉%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查成都分仓待发量
	 * @return
	 */
	public String  getSqlcd(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as cddfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%成都%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查西安分仓待发量
	 * @return
	 */
	public String  getSqlxa(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as xadfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%西安%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * 查双城待发量
	 * @return
	 */
	public String  getSqlsc(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as scdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0 ) and isnull(e.dr,0)=0 ) ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%双城%' and isnull(c.dr),= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	/**
	 * 查询本月前已发虚拟
	 * @return
	 */
    public String  getSql2(){
		
		return null;
	}
    /**
     * 查询本月总仓 转分仓数量
     * @return
     */
    
   public String  getSql3(){
		
		return null;
	}
    
    /**
     * 查询本月前总仓转分仓数量
     * @return
     */
   public String  getSql4(){
		
		return null;
	}
   
   @Override
	public String _getModelCode() {
		return WdsWlPubConst.zwhdb;
	}
	
}
