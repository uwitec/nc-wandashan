package nc.ui.wds.report.report1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableColumnModel;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.wl.pub.report.ZmReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/** 
 * ���ֲ�Ʒ��� ��Ʒ��ϸ��
 * @author mlr
 */
public class ReportUI extends ZmReportBaseUI{
	private static final long serialVersionUID = -416464210087347398L;
	//�ִ����ϲ�ά��
	private static String[] combinConds={"invtype","pk_stordoc","pk_invcl","pk_storestate","pk_invbasdoc","days"};
	//�ִ����ϲ��ֶ�
	private static String[] combinFields={"num","bnum"};
	//�ִ�������׷��ά��
	private static String[] combinConds1={"invtype","pk_stordoc","pk_invcl","pk_invbasdoc"};
	
	private static int[][] hdays= {{0,30},{31,60},{61,90},{91,120},{121,150},{151,180},
	                               {181,210},{211,240},{241,270},{271,300},{301,330},{331,365}};
	@Override
	public Map getNewItems() throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		//���ö�̬�в���λ��
	    map.put("location", new Integer(7));
		ReportItem[] its=new ReportItem[60];
		String startName="num";
		String startName1="bnum";
		int size=its.length/2;
		List<ReportItem> list=new ArrayList<ReportItem>();
		for(int i=0;i<size-1;i++){
		  ReportItem it=ReportPubTool.getItem(startName+(i+1),"������",IBillItem.DECIMAL,i, 80);
		  ReportItem it1=ReportPubTool.getItem(startName1+(i+1),"������",IBillItem.DECIMAL,i, 80);
		  list.add(it);
		  list.add(it1);
		}
		ReportItem[] rets=new ReportItem[list.size()]; 
		for(int i=0;i<list.size();i++){
		   rets[i]=list.get(i);
		}
		
		map.put("items",rets);	
		return map;
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
	     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
	      try {
	    	 //��ձ�������
	    	 clearBody();	    	         	
        	//���ö�̬��
        	setDynamicColumn1();
            //�õ���ѯ���
        	ReportBaseVO[] vos=null;
        	//���û����кϲ�
        	 setColumn();
        	 //����vo
			 List<ReportBaseVO[]> list=getReportVO(new String[]{getQuerySQL(getQueryConditon())});	 
			vos=list.get(0);
             if(vos==null || vos.length==0)
            	 return;       
             //����Сά�Ƚ������ݺϲ�
             ReportBaseVO[] cvos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds, combinFields, ReportBaseVO.class);
             //��������������
             ReportBaseVO[]  jichus=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(cvos), 
            		 combinConds1, combinFields, ReportBaseVO.class);
             calDays(jichus,cvos);//�������
                         
             if(jichus != null){                	
				super.updateBodyDigits();
				setBodyVO(jichus);				
	            setTolal();	                
             }  
	  	} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	   }  
	}
	/**
	 * ������� ��ÿ���¼���ó��Ŀ������ ׷�ӵ� ������������ ����
	 * ���������˿���ı�������
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-2����04:00:05
	 * @param jichus
	 * @param cvos
	 * @throws Exception 
	 */
	private void calDays(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {	
		calDay(jichus,cvos);
		calDay1(jichus,cvos);		
	}
    /**
     * ����0-90�����е�ÿ�µ���Ʒ�ִ���
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2011-12-2����04:05:13
     * @param jichus
     * @param cvos
     * @throws Exception 
     */
	private void calDay1(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {
		for(int i=0;i<3;i++){
			ReportBaseVO[] vos=filterByDays(cvos,hdays[i][0],hdays[i][1]);
			if(vos==null || vos.length==0)
				continue;
			ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1, (i+1)+"");
		}		
	}
	private ReportBaseVO[] filterByDays(ReportBaseVO[] cvos,int satrtDays,int endDays) throws Exception{
		ReportBaseVO[] nvos=(ReportBaseVO[]) ObjectUtils.serializableClone(cvos);
		if(nvos==null || nvos.length==0){
			return null;
		}
		List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
		for(int i=0;i<nvos.length;i++){
		   int days=PuPubVO.getInteger_NullAs(nvos[i].getAttributeValue("days"), -1);
		   if(days>=satrtDays && days<=endDays){
			   list.add(nvos[i]);
		   }
		}
		if(list==null || list.size()==0)
			return null;
		return list.toArray(new ReportBaseVO[0]);
	}
   /**
    * ����90-365�����е�ÿ�µ���Ʒ�ִ���
    * @���ߣ�mlr
    * @˵�������ɽ������Ŀ 
    * @ʱ�䣺2011-12-2����04:05:58
    * @param jichus
    * @param cvos
 * @throws Exception 
    */
	private void calDay(ReportBaseVO[] jichus, ReportBaseVO[] cvos) throws Exception {
		for(int i=3;i<12;i++){
			ReportBaseVO[] vos=filterByDays(cvos,hdays[i][0],hdays[i][1]);
			if(vos==null || vos.length==0)
				continue;
			ReportBaseVO[] nevos=(ReportBaseVO[]) CombinVO.combinData(vos, combinConds1, combinFields, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, combinConds1, (i+2)+"");
			
		}
	}

	public List<ReportBaseVO[]> getReportVO(String[] sqls) throws BusinessException {
		 List<ReportBaseVO[]> reportVOs = null;
	        try{
	            Class[] ParameterTypes = new Class[]{String[].class};
	            Object[] ParameterValues = new Object[]{sqls};
	            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
	                    "���ڲ�ѯ...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
	                    "queryVOBySql", ParameterTypes, ParameterValues);
	            if(o != null){
	                reportVOs = (List<ReportBaseVO[]>)o;
	            }
	        }catch(Exception e){
	            Logger.error(e);
	            MessageDialog.showErrorDlg(this, "����", e.getMessage());
	        }
	        return reportVOs;
	}
	public String getQuerySQL(String whereSql) throws Exception {
		return WDSWLReportSql.getStoreSql(whereSql);
	}
	public String getQuerySQL1() throws Exception {
		return null;
	}

	public String getQuerySQL2() throws Exception {
		return null;
	}

	public String getQuerySQL3() throws Exception {
		return null;
	}


	@Override
	public void initReportUI() {
		setColumn();
	}
	 /**
     * �����кϲ�
     */
    private void setColumn() {
        //������Ŀ��������
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        
        ColumnGroup shiji =new ColumnGroup("ʵ�ʿ��");
        
       //��ķ��� ��Ч
        ColumnGroup zgroup=new ColumnGroup("��Ч");
        int i=1;//���ñ仯��ֵ��
        ColumnGroup a1=new ColumnGroup("30������");
        a1.add(cardTcm.getColumn(6+i));
        a1.add(cardTcm.getColumn(7+i));
        zgroup.add(a1);
      
        
        ColumnGroup a2=new ColumnGroup("30-60��");
        a2.add(cardTcm.getColumn(8+i));
        a2.add(cardTcm.getColumn(9+i));
        zgroup.add(a2);
        
        ColumnGroup a3=new ColumnGroup("60-90��");
        a3.add(cardTcm.getColumn(10+i));
        a3.add(cardTcm.getColumn(11+i));
        zgroup.add(a3);
        
        ColumnGroup a4=new ColumnGroup("С��");
        a4.add(cardTcm.getColumn(12+i));
        a4.add(cardTcm.getColumn(13+i));
        zgroup.add(a4); 
        
        shiji.add(zgroup);
                    
        ColumnGroup zgroup2=new ColumnGroup("��Ч");
        
        ColumnGroup b1=new ColumnGroup("91-120");
        b1.add(cardTcm.getColumn(14+i));
        b1.add(cardTcm.getColumn(15+i));
        zgroup2.add(b1);
        
        ColumnGroup b2=new ColumnGroup("121-150");
        b2.add(cardTcm.getColumn(16+i));
        b2.add(cardTcm.getColumn(17+i));
        zgroup2.add(b2);
        
        ColumnGroup b3=new ColumnGroup("151-180");
        b3.add(cardTcm.getColumn(18+i));
        b3.add(cardTcm.getColumn(19+i));
        zgroup2.add(b3);
        
        ColumnGroup b4=new ColumnGroup("181-210");
        b4.add(cardTcm.getColumn(20+i));
        b4.add(cardTcm.getColumn(21+i));
        zgroup2.add(b4);
        
        ColumnGroup b5=new ColumnGroup("211-240");
        b5.add(cardTcm.getColumn(22+i));
        b5.add(cardTcm.getColumn(23+i));
        zgroup2.add(b5);
        
        ColumnGroup b6=new ColumnGroup("241-270");
        b6.add(cardTcm.getColumn(24+i));
        b6.add(cardTcm.getColumn(25+i));
        zgroup2.add(b6);
        
        ColumnGroup b7=new ColumnGroup("271-300");
        b7.add(cardTcm.getColumn(26+i));
        b7.add(cardTcm.getColumn(27+i));
        zgroup2.add(b7);
        
        
        ColumnGroup b8=new ColumnGroup("301-330");
        b8.add(cardTcm.getColumn(28+i));
        b8.add(cardTcm.getColumn(29+i));
        zgroup2.add(b8);
        
        ColumnGroup b9=new ColumnGroup("331-365");
        b9.add(cardTcm.getColumn(30+i));
        b9.add(cardTcm.getColumn(31+i));
        zgroup2.add(b9);
        
        ColumnGroup b10=new ColumnGroup("����");
        b10.add(cardTcm.getColumn(32+i));
        b10.add(cardTcm.getColumn(33+i));
        zgroup2.add(b10);
        
        ColumnGroup b11=new ColumnGroup("ȥ��");
        b11.add(cardTcm.getColumn(34+i));
        b11.add(cardTcm.getColumn(35+i));
        zgroup2.add(b11);
        
      
        
        ColumnGroup b12=new ColumnGroup("С��");
        b12.add(cardTcm.getColumn(36+i));
        b12.add(cardTcm.getColumn(37+i));
        zgroup2.add(b12);
        
        shiji.add(zgroup2);
        
        ColumnGroup c1=new ColumnGroup("С��");
        c1.add(cardTcm.getColumn(38+i));
        c1.add(cardTcm.getColumn(39+i));
        
        shiji.add(c1);
        
        ColumnGroup c2=new ColumnGroup("(����Ʒ180g-200g)");
        c2.add(cardTcm.getColumn(40+i));
        c2.add(cardTcm.getColumn(41+i));
        
        shiji.add(c2);
        
        
        ColumnGroup c3=new ColumnGroup("ҽ����Ʒ");
        c3.add(cardTcm.getColumn(42+i));
        c3.add(cardTcm.getColumn(43+i));
        
        shiji.add(c3);

        
        ColumnGroup c4=new ColumnGroup("�����򷢻�(���δ���)");
        c4.add(cardTcm.getColumn(44+i));
        c4.add(cardTcm.getColumn(45+i));
        
        
        shiji.add(c4);
        

        ColumnGroup c5=new ColumnGroup("ת�ֲ� (��;)");
        c5.add(cardTcm.getColumn(46+i));
        c5.add(cardTcm.getColumn(47+i));
        
        shiji.add(c5);
        
        ColumnGroup c6=new ColumnGroup("����(�ӻ��������ֳ���)");
        c6.add(cardTcm.getColumn(48+i));
        c6.add(cardTcm.getColumn(49+i));
        
        shiji.add(c6);
        
        cardHeader.addColumnGroup(shiji);
        
        ColumnGroup c7=new ColumnGroup("�ϼ�");
        c7.add(cardTcm.getColumn(50+i));
        c7.add(cardTcm.getColumn(51+i));     
        cardHeader.addColumnGroup(c7);
        
        ColumnGroup qianfa=new ColumnGroup("Ƿ�����");  
        
     ColumnGroup zgroup3=new ColumnGroup("��������");
        
        ColumnGroup d1=new ColumnGroup("����ǰ��������");
        d1.add(cardTcm.getColumn(52+i));
        d1.add(cardTcm.getColumn(53+i));
        zgroup3.add(d1);
        
        ColumnGroup d2=new ColumnGroup("������������");
        d2.add(cardTcm.getColumn(54+i));
        d2.add(cardTcm.getColumn(55+i));
        zgroup3.add(d2);
        
        ColumnGroup d3=new ColumnGroup("����ǰ����������B��ͷ��");
        d3.add(cardTcm.getColumn(56+i));
        d3.add(cardTcm.getColumn(57+i));
        zgroup3.add(d3);
        
        ColumnGroup d4=new ColumnGroup("���²���������B��ͷ��");
        d4.add(cardTcm.getColumn(58+i));
        d4.add(cardTcm.getColumn(59+i));
        zgroup3.add(d4); 
        
        qianfa.add(zgroup3);
        
        
    ColumnGroup zgroup4=new ColumnGroup("ת�ֲ�");
        
        ColumnGroup e1=new ColumnGroup("���������ܲ�ת�ֲֿ����");
        e1.add(cardTcm.getColumn(60+i));
        e1.add(cardTcm.getColumn(61+i));
        zgroup4.add(e1);
             
        qianfa.add(zgroup4);
        
        cardHeader.addColumnGroup(qianfa);
           
         ColumnGroup e2=new ColumnGroup("�ܼ�");
         e2.add(cardTcm.getColumn(62+i));
         e2.add(cardTcm.getColumn(63+i));
              
      cardHeader.addColumnGroup(e2);

        getReportBase().getBillModel().updateValue();
    }
    public String _getModelCode() {
        return WdsWlPubConst.report1;
    }

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

}
