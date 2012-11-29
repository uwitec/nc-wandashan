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
 * ����˶Ա� 
 * 
 * @author liuys
 */
public class ReprotUI extends ZmReportBaseUI3 {

	private static final long serialVersionUID = 1L;
	
	/**
	 * ע�ᰴť ����Ҫ�İ�ť����ȥ��
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
	 * ���ղ�ѯ�����sql 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		return new String[]{getSql(),getSqlerp(),getSqlout(),getSqlin(),getSqlotherin(),getSqltj(),getSqlsc(),getSqlwh()
				,getSqlxa(),getSqlxa(),getSqlcd(),getSqlzz()};
	}
	/**
	 * ���õ�ui����֮ǰ ��������ѯ�������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{
         
        //һ��Ҫ��д�÷���   �������ݵ���� ���� ���ܴ���
		
		list.get(0);//������Ϊ getSql()������ѯ����������
		
//		list.get(1);//������Ϊ getSql1()������ѯ����������  �Դ�����
		
		
		//���ݴ���ķ�ʽ��Ҫ�ã������ķ���
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
          
		 return vos; //����Ĭ�ϴ���ʽ
             
	}
	
	/**
	 * ��ѯ��� ���õ�ui����֮�� ��������  
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter()throws Exception{
	  //Ҫ���Զ��彻�� ���Ե���	nc.vo.zmpub.pub.report2.ReportRowColCrossTool ��onCross�ķ���

		
		
		
       super.dealQueryAfter();//����ϵͳ�Դ��� ���� ��Ҫ���� �����������  ���������ļ� ���н��� �ϼ����á�
	}
	
	
	
	/**
	 * ��ѯ�ִ���
	 * @return
	 */
	public String  getSql(){
		
		return " select pk_invmandoc as pk_invmandoc,sum(whs_stockpieces) as wlkc from tb_warehousestock where isnull(dr,0)=0 group by pk_invmandoc  ";
	}


       
   /**
   	 * ��ѯerp�ִ���
   	 * @return
   	 */
   	public String  getSqlerp(){
   		StringBuffer strb = new StringBuffer();
   		strb.append(" select kp.cinventoryid as pk_invmandoc,  SUM(COALESCE(ninspacenum, 0.0)) - SUM(COALESCE(noutspacenum, 0.0)) as erpkc ");
   		strb.append(" from v_ic_onhandnum6 kp  group by kp.cinventoryid");
   		return strb.toString();
   	}
	
   	/**
	 *��ѯ����chuku 
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
	 *��ѯ����ruku 
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
	 *��ѯ����qitaruku
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
	 * �����ֲִ�����
	 * @return
	 */
	public String  getSqltj(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as tjdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%���%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * ����֣�ݷֲִ�����
	 * @return
	 */
	public String  getSqlzz(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as zzdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0 ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%֣��%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * ���人�ֲִ�����
	 * @return
	 */
	public String  getSqlwh(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as whdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%�人%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * ��ɶ��ֲִ�����
	 * @return
	 */
	public String  getSqlcd(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as cddfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%�ɶ�%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * �������ֲִ�����
	 * @return
	 */
	public String  getSqlxa(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as xadfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0  and isnull(e.dr,0)=0  ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%����%' and isnull(c.dr,0)= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	
	/**
	 * ��˫�Ǵ�����
	 * @return
	 */
	public String  getSqlsc(){
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as scdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" where e.csaleid not in (select distinct ib.cfirstbillhid from ic_general_b ib where ib.cfirsttype = '30' and isnull(ib.dr,0)=0 ) ");
		strb.append(" and isnull(b.dr,0)=0 ) and isnull(e.dr,0)=0 ) ");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%˫��%' and isnull(c.dr),= 0) ");
		strb.append("  group by b.cinventoryid ");

		
		return strb.toString();
	}
	/**
	 * ��ѯ����ǰ�ѷ�����
	 * @return
	 */
    public String  getSql2(){
		
		return null;
	}
    /**
     * ��ѯ�����ܲ� ת�ֲ�����
     * @return
     */
    
   public String  getSql3(){
		
		return null;
	}
    
    /**
     * ��ѯ����ǰ�ܲ�ת�ֲ�����
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
