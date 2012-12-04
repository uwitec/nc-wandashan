package nc.ui.wds.report.cwhdb;
import java.util.ArrayList;
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
				,getSqlxa(),getSqlcd(),getSqlzz(),getSql2(),getSqlzk(),getSqlotherout(),getSql3(),getSql4(),getSqlxnout(),getSql5(),getSqlmon()};
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
		
//		list.get(0);//������Ϊ getSql()������ѯ����������
		
//		list.get(1);//������Ϊ getSql1()������ѯ����������  �Դ�����
		
		
		//���ݴ���ķ�ʽ��Ҫ�ã������ķ���
		String[] num_condition_fields = new String[]{"pk_invmandoc"}; 
		String[] combinFields = new String[]{"wlqtnum","ninnum","qtcknum","wlkc","erpkc","bycknum","byrknum","qtrknum","tjdfnum","scdfnum","whdfnum","xadfnum","cddfnum","zzdfnum","qfxnnum","byqzgnum","byyfxnnum","erpkcnum","zknum"}; 
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
	 * @throws Exception 
	 */
	public String  getSql() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select k.pk_invmandoc as pk_invmandoc,sum(whs_stockpieces) as wlkc from tb_warehousestock k");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc = k.pk_invmandoc and nvl(invc.dr,0)=0 ");
		strb.append(" where nvl(k.dr,0)=0 and invc.uisso = 'Y' ");
		strb.append(" and k.pk_corp = '"+getCorpPrimaryKey()+"' ");
		
		if(getQuerySQL() !=null && getQuerySQL().length()>0)
			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and k.pk_invmandoc = '"+getDateValue("pk_invbasdoc")+"' ");
		
		strb.append("  group by k.pk_invmandoc ");
		return strb.toString();
	}


       
   /**
   	 * ��ѯerp�ִ���
   	 * @return
 * @throws Exception 
   	 */
   	public String  getSqlerp() throws Exception{
   		StringBuffer strb = new StringBuffer();
   		strb.append(" select kp.cinventoryid as pk_invmandoc,  SUM(COALESCE(ninspacenum, 0.0)) - SUM(COALESCE(noutspacenum, 0.0)) as erpkc ");
   		strb.append(" from v_ic_onhandnum6 kp ");
   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc = kp.cinventoryid and nvl(invc.dr,0)=0 ");
   		strb.append(" where kp.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
   		
   		if(getQuerySQL() !=null && getQuerySQL().length()>0)
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and kp.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   		
   		strb.append(" group by kp.cinventoryid ");
   		return strb.toString();
   	}
	
   	/**
	 *��ѯ���³��� 
	 * @return
   	 * @throws Exception 
	 */
	public String  getSqlout() throws Exception{
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum) as bycknum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.vbilltype in('WDSH','WDS6','WDS8') and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
   		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
   		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
   		strb.append(" and h.dbilldate >= '"+begindate.toString()+"' and h.dbilldate <= '"+enddate.toString()+"' ");
   		
   		if(getQuerySQL() !=null && getQuerySQL().length()>0)
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  			strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   		
   		strb.append("  group by b.cinventoryid ");
   		return strb.toString();
	}
	
	/**
	 *��ѯ������� 
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlin() throws Exception{
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as byrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.geb_cinventoryid and nvl(invc.dr,0)=0 ");
   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.geh_billtype in('WDS9','WDSZ','WDS7')  and h.geh_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
     		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and h.geh_dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and h.geh_dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}  
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.geb_cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		}   		
  		strb.append("  group by b.geb_cinventoryid ");
   		return strb.toString();
	}
	
	//���ݶԻ�����ֶ�ȡֵ
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
	 *��ѯ�����������
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlotherin() throws Exception{
		StringBuffer strb = new StringBuffer();
   		strb.append(" select b.geb_cinventoryid as pk_invmandoc,sum(b.geb_anum) as qtrknum from tb_general_h  h join tb_general_b b on h.geh_pk=b.geh_pk  ");
   		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.geb_cinventoryid and nvl(invc.dr,0)=0 ");
   		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.geh_billtype in('WDS7') and h.geh_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
   		if(getQuerySQL() !=null && getQuerySQL().length()>0){
   			String wsql="";
   			ConditionVO[] vos=getQueryDlg().getConditionVO();
   			for(int i=0;i<vos.length;i++){
   				if(vos[i].getFieldCode().equals("startdate")){
   					wsql=wsql+" and h.geh_dbilldate >= '"+vos[i].getValue()+"'";
   				}
   				if(vos[i].getFieldCode().equals("enddate")){
   					wsql=wsql+" and h.geh_dbilldate <= '"+vos[i].getValue()+"'";					
   				}
   			}
   			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
   			strb.append(" and b.geb_cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
   			strb.append(wsql);
   		}
   		strb.append("  group by b.geb_cinventoryid ");
   		return strb.toString();
	}
	
	
	/**
	 * �����ֲִ�����
	 * @return
	 * @throws Exception 
	 */
	public String  getSqltj() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as tjdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%���%' and nvl(c.dr,0)= 0)) ");
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
	 * ����֣�ݷֲִ�����
	 * @return
	 */
	public String  getSqlzz() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as zzdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%֣��%' and nvl(c.dr,0)= 0)) ");
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
	 * ���人�ֲִ�����
	 * @return
	 */
	public String  getSqlwh() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as whdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0 ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%�人%' and nvl(c.dr,0)= 0)) ");
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
	 * ��ɶ��ֲִ�����
	 * @return
	 */
	public String  getSqlcd() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as cddfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0 ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%�ɶ�%' and nvl(c.dr,0)= 0)) ");
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
	 * �������ֲִ�����
	 * @return
	 */
	public String  getSqlxa() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as xadfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%����%' and nvl(c.dr,0)= 0)) ");
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
	 * ��˫�Ǵ�����
	 * @return
	 */
	public String  getSqlsc() throws Exception{
		StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as scdfnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk and h.vbillstatus = '1' and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0  and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%˫��%' and nvl(c.dr,0)= 0)) ");
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
	 * ��ѯ����ǰǷ��������
	 * @return
	 * @throws Exception 
	 */
    public String  getSql2() throws Exception{

    	StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as qfxnnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid not in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%˫��%' and nvl(c.dr,0)= 0)) ");
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
     * ��ѯ����erp����ת�ⵥ����
     * @return
     * @throws Exception 
     */
    
   public String  getSqlzk() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   //ȡ��Ӧת����
	   strb.append(" select b.cinventoryid  as pk_invmandoc,sum(b.dshldtransnum) as zknum from ic_special_h h join ic_special_b b on h.cspecialhid = b.cspecialhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where isnull(h.dr,0)=  0 and isnull(b.dr,0)=0 and h.vuserdef13='Y' and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   strb.append(" and h.dbilldate >='"+getDateValue("startdate")+"' " );
		   strb.append(" and h.dbilldate <='"+getDateValue("enddate")+"' " );
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
	   strb.append("  group by b.cinventoryid ");
		return strb.toString();
	}
   
   /**
    * ��ѯ������������������
    * @return
 * @throws Exception 
    */
   
  public String  getSqlotherout() throws Exception{
	  StringBuffer strb = new StringBuffer();
 	  strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum)  as qtcknum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
 	 strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
 	  strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.vbilltype in ('WDS6') and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
 	 if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   strb.append(" and h.dbilldate >='"+getDateValue("startdate")+"' " );
		   strb.append(" and h.dbilldate <='"+getDateValue("enddate")+"' " );
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
 	  strb.append("  group by b.cinventoryid ");
 	  return strb.toString();
	}
  
    /**
     * ��ѯ����ǰ�ݹ�����
     * @return
     * @throws Exception 
     */
    
   public String  getSql3() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   strb.append(" select b.cmangid as pk_invmandoc,sum(b.nordernum) as byqzgnum from po_order r join po_order_b b on r.corderid = b.corderid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cmangid and nvl(invc.dr,0)=0 ");
	   strb.append(" where isnull(r.dr,0)=0 and isnull(b.dr,0)=0 and r.pk_defdoc11 ='0001S3100000000OK5HM' and r.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
	   if(getQuerySQL() !=null && getQuerySQL().length()>0){
		   strb.append(" and r.dorderdate >= '"+getDateValue("startdate")+"' " );
		   strb.append(" and r.dorderdate <= '"+getDateValue("enddate")+"' ");
		   if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
		   strb.append(" and b.cmangid = '"+getDateValue("pk_invbasdoc")+"' ");
	   }
 	  strb.append("  group by b.cmangid ");
 	  return strb.toString();
	}
    
    /**
     * ��ѯERP��������ʾΪ�������
     * @return
     * @throws Exception 
     */
   public String  getSql4() throws Exception{
	   StringBuffer strb = new StringBuffer();
	   strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.ninnum) as ninnum from ic_general_b b join ic_general_h h on h.cgeneralhid=b.cgeneralhid ");
	   strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
	   strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.cbilltypecode='4A' and h.pk_corp ='"+getCorpPrimaryKey()+"'  and h.vuserdef13 ='Y' and invc.uisso = 'Y'");
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
	 * ��ѯ�����ѷ�������
	 * @return
	 * @throws Exception 
	 */
   public String  getSql5() throws Exception{

   	StringBuffer strb = new StringBuffer();
		strb.append(" select b.cinventoryid as pk_invmandoc ,sum(nnumber) as byyfxnnum from so_sale e join so_saleorder_b b on e.csaleid=b.csaleid ");
		 strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0 ");
		strb.append(" where e.csaleid in (select distinct gb.cfirstbillhid from tb_outgeneral_h h, tb_outgeneral_b gb where gb.general_pk = h.general_pk  and h.vbilltype = 'WDS8' and nvl(gb.dr, 0) = 0 and nvl(h.dr, 0) = 0) ");
		strb.append(" and nvl(b.dr,0)=0  and nvl(e.dr,0)=0 and e.pk_defdoc11 ='0001S3100000000OK5HM' and e.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
//		strb.append(" and e.cwarehouseid in (select c.pk_stordoc from bd_stordoc c where c.storname like '%˫��%' and nvl(c.dr,0)= 0)) ");
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
	 *��ѯ������������������(����)
	 * @return
	 * @throws Exception 
	 */
	public String  getSqlxnout() throws Exception{
		StringBuffer strb = new StringBuffer();
  		strb.append(" select b.cinventoryid as pk_invmandoc,sum(b.noutnum) as wlqtnum from tb_outgeneral_h  h join tb_outgeneral_b b on h.general_pk=b.general_pk  ");
  		 strb.append(" join wds_invbasdoc invc on invc.pk_invmandoc =  b.cinventoryid and nvl(invc.dr,0)=0  ");
  		strb.append(" where nvl(h.dr,0)=0 and nvl(b.dr,0)=0 and h.isxnap ='Y' and h.vbilltype in('WDS6') and h.pk_corp ='"+getCorpPrimaryKey()+"' and invc.uisso = 'Y'");
  		UFDate begindate =AccountCalendar.getInstance().getMonthVO().getBegindate();
  		UFDate enddate =AccountCalendar.getInstance().getMonthVO().getEnddate();
  		strb.append(" and h.dbilldate >= '"+begindate.toString()+"' and h.dbilldate <= '"+enddate.toString()+"' ");
  		
  		if(getQuerySQL() !=null && getQuerySQL().length()>0)
  			if(PuPubVO.getString_TrimZeroLenAsNull(getDateValue("pk_invbasdoc")) != null)
  				strb.append(" and b.cinventoryid = '"+getDateValue("pk_invbasdoc")+"' ");
  		
  		strb.append("  group by b.cinventoryid ");
  		return strb.toString();
	}
   
	/**
	 *��ѯERP���(�������)
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
