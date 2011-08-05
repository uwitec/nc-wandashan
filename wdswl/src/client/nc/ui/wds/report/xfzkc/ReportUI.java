package nc.ui.wds.report.xfzkc;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.WDSReportBaseUI;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.CombinVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * ��������ܿ�汨��
 * @author mlr
 */
public class ReportUI extends WDSReportBaseUI{
	private static final long serialVersionUID = 1L;
	//���������۱���
	private static  String invclcode = "00";
	//���ô����ʾ�ֶ�
	private static String  invcommon="���ô��";
	//�����ô����ʾ�ֶ�
	private static String  uninvcommon="�����ô��";
	//��������ֶ�   0��ʾ����     1��ʾ������
	private static String  invtype="invtype";	
    //�����������
	private static String invtypename="invatypename";
    //����λ ����ǰ׺
    private static String unit="unit";
    //����λ ����ǰ׺
    private static String bunit="bunit";  
    //�����ֶ�����
    private static String  days="days";
    //�������ÿ�������������Ӧ���������ֶ���
    private static String num="num";
     //�������ÿ�����������Ӧ�ĸ������ֶ���
    private static String bnum="bnum";
    //���״̬ ����  ��Ӧ������ֵ
    private static String  stateid="1021S31000000009FS99";
    //���״̬��Ӧ�������ֶ�����
    private static String  stockstate="ss_pk"; 
    //��������;��Ӧ�������ֶ���
    private static String type="type"; 
    //�ƻ��������ֶ�ֵ����
    private static String numplan="plannum";
    //�ƻ��������ֶ�ֵ����
    private static String bnumplan="bplannum";
//    //vo�ϲ�������
//    private static String[] voCombinConds={"invtype","pk_invcl","pk_invbasdoc"};
    //��Ҫ�ϲ�����ֵ������
    private static int[] types={IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD
    	                        ,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD};
    //��Ҫ�ϲ�����ֵ�Ӷ�
    private static String[] combinFields={"unit1","unit2","unit3","unit4","unit5","unit6","unit7","unit8","unit9","unit10","unit11",
    	                                  "bunit1","bunit2","bunit3","bunit4","bunit5","bunit6","bunit7","bunit8","bunit9","bunit10","bunit11",  };
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT15;
	}
	public ReportUI() {
		super();
		setLocation1(2);
		getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		//ȥ���ֶ��Զ�����Ĺ���
		getReportBase().getBillTable().removeSortListener();
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
        int i=0;
        if(isstordoc.booleanValue()==true){
        	i=i+1;
        }
        ColumnGroup zgroup=new ColumnGroup("����");
        ColumnGroup a1=new ColumnGroup("30������");
        a1.add(cardTcm.getColumn(i+5));
        a1.add(cardTcm.getColumn(i+6));
        zgroup.add(a1);
        ColumnGroup a2=new ColumnGroup("30-60��");
        a2.add(cardTcm.getColumn(i+7));
        a2.add(cardTcm.getColumn(i+8));
        zgroup.add(a2);
        ColumnGroup a3=new ColumnGroup("60-90��");
        a3.add(cardTcm.getColumn(i+9));
        a3.add(cardTcm.getColumn(i+10));
        zgroup.add(a3);
        ColumnGroup a4=new ColumnGroup("90���Ժ�");
        a4.add(cardTcm.getColumn(i+11));
        a4.add(cardTcm.getColumn(i+12));
        zgroup.add(a4);      
        cardHeader.addColumnGroup(zgroup);
              
        ColumnGroup zgroup2=new ColumnGroup("С��");
        zgroup2.add(cardTcm.getColumn(i+13));
        zgroup2.add(cardTcm.getColumn(i+14));
        cardHeader.addColumnGroup(zgroup2);
                
        ColumnGroup zgroup3=new ColumnGroup("����Ʒ");
        zgroup3.add(cardTcm.getColumn(i+15));
        zgroup3.add(cardTcm.getColumn(i+16));
        cardHeader.addColumnGroup(zgroup3);
        
        ColumnGroup a11=new ColumnGroup("��;");
        a11.add(cardTcm.getColumn(i+17));
        a11.add(cardTcm.getColumn(i+18));
        cardHeader.addColumnGroup(a11);
        
        
        ColumnGroup a22=new ColumnGroup("����");
        a22.add(cardTcm.getColumn(i+19));
        a22.add(cardTcm.getColumn(i+20));
        cardHeader.addColumnGroup(a22);
        
        
        ColumnGroup zgroup4=new ColumnGroup("�ϼ�");
        zgroup4.add(cardTcm.getColumn(i+21));
        zgroup4.add(cardTcm.getColumn(i+22));
        cardHeader.addColumnGroup(zgroup4);
        
        ColumnGroup a33=new ColumnGroup("����");
        a33.add(cardTcm.getColumn(i+23));
        a33.add(cardTcm.getColumn(i+24));
        cardHeader.addColumnGroup(a33); 
        
        ColumnGroup a5=new ColumnGroup("Ԥ������");
        a5.add(cardTcm.getColumn(i+25));
        a5.add(cardTcm.getColumn(i+26));
        cardHeader.addColumnGroup(a5);      
        getReportBase().getBillModel().updateValue();
    }
	@Override
	public void setUIAfterLoadTemplate() {
		
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
	@Override
	public void onQuery() {
		try{
		        super.onQuery();
            	//�õ��Զ����ѯ����
                //�õ���ѯ���
		        //���û����кϲ�
              	 setColumn();
                List<ReportBaseVO[]> list=getReportVO(new String[]{getQuerySQL(),getQuerySQL1(),getQuerySQL2()});
                ReportBaseVO[] vos1= list.get(1);
                ReportBaseVO[] vos=list.get(0);  
                ReportBaseVO[] vos2= list.get(2); 
                if(vos1 != null&&vos1.length>0 || vos!=null&&vos1.length>0 || vos2!=null&&vos2.length>0 ){                	
					super.updateBodyDigits();
					if(isstordoc.booleanValue()==true){
				    ReportBaseVO[]newVos=setVoByContion(vos,fields4);
				    ReportBaseVO[]newVos1=setVoByContion(vos1,fields4);
				    ReportBaseVO[] combins=(ReportBaseVO[]) CombinVO.combinVoByFields(newVos,newVos1,fields4,types,combinFields);
				    ReportBaseVO[]newVos2=setVoByContion(vos2,fields4);
				    ReportBaseVO[] combins1=(ReportBaseVO[])CombinVO.combinVoByFields(newVos2,combins,fields4,types,combinFields);		    
				    setAfterQuery(combins1);
				    setReportBaseVO(combins1);
					setBodyVO(combins1);	
					setDefSubtotal(new String[]{"invclname"}, combinFields);  
					}else{
					super.updateBodyDigits();			
					ReportBaseVO[]newVos=setVoByContion(vos,fields7);
					ReportBaseVO[]newVos1=setVoByContion(vos1,fields7);
					ReportBaseVO[] combins=(ReportBaseVO[])CombinVO.combinVoByFields(newVos,newVos1,fields7,types,combinFields);
					ReportBaseVO[]newVos2=setVoByContion(vos2,fields7);
					ReportBaseVO[] combins1=(ReportBaseVO[])CombinVO.combinVoByFields(newVos2,combins,fields7,types,combinFields);		    
					setAfterQuery(combins1);
					setReportBaseVO(combins1);
				    setBodyVO(combins1);	
				    setDefSubtotal(new String[]{"invclname"}, combinFields);  											
				  }
              }                
	          
		} catch (Exception e) {
            e.printStackTrace();
            showWarningMessage(e.getMessage());
        }
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ��ѯ������,��vo�ĺ�������
	 * @ʱ�䣺2011-7-13����03:07:52
	 * @param combins
	 */
	private void setAfterQuery(ReportBaseVO[] combins) {
		if (combins == null || combins.length == 0) {
			return;
		}
		int size = combins.length;
		for (int i = 0; i < size; i++) {
			Integer type = PuPubVO.getInteger_NullAs(combins[i].getAttributeValue(invtype), new Integer(0));
			if (type == 0) {
				combins[i].setAttributeValue(invtypename, invcommon);
			} else if (type == 1) {
				combins[i].setAttributeValue(invtypename, uninvcommon);
			}
		}	
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ���ձ�������,�ӹ����β�ѯ�γɵı���vo
	 *      �ӹ�������
	 *      ���� �� ������� ������� ��� ���з���,Ȼ���ÿ��vo���кϲ�,��ÿ��vo�ϲ���Ҫ��ϲ���һ��vo
	 *      ��ʲô�����ϲ��أ�
	 *      �����жϻ���:  �����30������ ����vo�Ŀ�������ӵ���ʾ30�����ڵ��ֶ���
	 *                    �����30-60��  ����vo�Ŀ�������ӵ���ʾ30-60����ֶ���
	 *                    �����60-90��  ����vo�Ŀ�������ӵ���ʾ60-90����ֶ���
	 *                    �����90������ ����vo�Ŀ�������ӵ���ʾ90��������ֶ���
	 *      Ȼ���ж�:     ͨ�����״̬������ �鿴�ô���Ƿ�Ϊ����,
	 *                    ����� ���ƻ������ͼƻ��������ӵ������ֶ���
	 *      ����ж�:     ͨ����������;����,�жϸô���Ǵ���������;
	 *                    ����Ǵ��� ���ô���ļƻ������ͼƻ��������ӵ������ֶ���
	 *                    �������; �Ӹô���ļƻ������ͼƻ��������ӵ���;�ֶ��� 
	 *                                     
	 * @ʱ�䣺2011-7-11����01:01:57
	 * @param vos
	 * @return
	 */
	private ReportBaseVO[]  setVoByContion(ReportBaseVO[] vos,String[] fields) {
		if(vos==null || vos.length==0){
			return vos;
		}
		CircularlyAccessibleValueObject[][]voss =SplitBillVOs.getSplitVOs(vos,fields);
		if(voss==null || voss.length==0){
			return vos;
		}
		//new ��ͷ��voΪ������װ��������vo
		ReportBaseVO[] newVos=new ReportBaseVO[voss.length];
		int size=voss.length;
		for(int i=0;i<size;i++){
			ReportBaseVO newVo=null;
			int size1=voss[i].length;
		    for(int j=0;j<size1;j++){
		    	ReportBaseVO oldVo=(ReportBaseVO) voss[i][j];
		    	if(newVo==null){
		    	   newVo=(ReportBaseVO) oldVo.clone();
		        }
		    	//���ݿ������ô������
		    	setDayNum(newVo,oldVo);
		    	//���ô����������
		    	setDaiJian(newVo,oldVo);    		    	
		    	//���ô����;�����������
		    	setZaiTuorDaiFa(newVo,oldVo);		    	
		    }
		    newVos[i]=newVo;
		}	
		return newVos;
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ݿ������ô������
	 * @ʱ�䣺2011-7-11����03:12:30
	 * @param newVo
	 * @param oldVo
	 */
	private void setDayNum(ReportBaseVO newVo, ReportBaseVO oldVo) {
		//��ô���Ŀ���
    	Integer daynum=PuPubVO.getInteger_NullAs(oldVo.getAttributeValue(days), new Integer(-1));	
    	//���������
    	UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(num));
    	//��ø�����
    	UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnum));		    	
    	//���ÿ���
    	setDayNum(newVo,daynum, oldnum,boldnum);		
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ���ô���Ĵ�������
	 * @ʱ�䣺2011-7-11����03:10:43
	 * @param newVo
	 * @param oldVo
	 */
	private void setDaiJian(ReportBaseVO newVo,ReportBaseVO oldVo) {
		//��ô��״̬����
    	String pk_state=PuPubVO.getString_TrimZeroLenAsNull(oldVo.getAttributeValue(stockstate));		    	
    	//��ÿ��������
    	UFDouble cnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(num));
    	//��ÿ�渨����
    	UFDouble bcnum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnum));
    	if(stateid.equalsIgnoreCase(pk_state)){
    		setDaiJian(newVo,cnum,bcnum);
    	}			
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ������;���������
	 * @ʱ�䣺2011-7-11����03:03:06
	 * @param newVo
	 * @param oldVo
	 */
	private void setZaiTuorDaiFa(ReportBaseVO newVo, ReportBaseVO oldVo) {
		    //��ô�������;������
		    Integer itype=PuPubVO.getInteger_NullAs(oldVo.getAttributeValue(type),new Integer(2));	
		    //��üƻ�������
    	    UFDouble planNum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(numplan));
    	    //��üƻ�������
    	    UFDouble bplanNum=PuPubVO.getUFDouble_NullAsZero(oldVo.getAttributeValue(bnumplan));   	
    	    //����Ϊ   0   ��ʾ����   ����Ϊ  1 ��ʾ�ѷ�
		if(itype==0){
			//���������� ��ʾ�ֶ�unit9
		    UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"10"));
		    newVo.setAttributeValue(unit+"10", oldnum.add(planNum));
		     //���������� ��ʾ�ֶ�unit9
		    UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"10"));
		    newVo.setAttributeValue(bunit+"10", boldnum.add(bplanNum));			
		}else if(itype==1){
		    //��;��������ʾ�ֶ� unit7
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"7"));
			newVo.setAttributeValue(unit+"7", oldnum.add(planNum));
			//���������� ��ʾ�ֶ�unit7
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"7"));
			newVo.setAttributeValue(bunit+"7", boldnum.add(bplanNum));	
		}	
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ���ô��������������͸�����
	 * @ʱ�䣺2011-7-11����02:51:22
	 * @param newVo
	 * @param planNum ���������
	 * @param bplanNum ��渨������
	 */
	private void setDaiJian(ReportBaseVO newVo, UFDouble num,
			UFDouble bnum) {
		//���ԭ���Ĵ��������� ��unit7�ֶα�ʾ
		UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"8"));
		newVo.setAttributeValue(unit+"8",oldnum.add(num));
		//���ԭ���Ĵ��츨���� ��unit7�ֶα�ʾ
		UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"8"));
		newVo.setAttributeValue(bunit+"8",boldnum.add(bnum));		
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ���ÿ��� �ֶε�ֵ
	 *      
	 *      �����30������ ����vo�Ŀ�������ӵ���ʾ30�����ڵ��ֶ���
	 *                    �����30-60��  ����vo�Ŀ�������ӵ���ʾ30-60����ֶ���
	 *                    �����60-90��  ����vo�Ŀ�������ӵ���ʾ60-90����ֶ���
	 *                    �����90������ ����vo�Ŀ�������ӵ���ʾ90��������ֶ���
	 *      
	 * @ʱ�䣺2011-7-11����02:07:43
	 * @param newVo
	 * @param daynum
	 */
	private void setDayNum(ReportBaseVO newVo, Integer daynum,UFDouble num,UFDouble bnum) {
		if(daynum<0){
			return;
		}
		if(daynum<=30){
			//unit1Ϊ30�����ڿ����ֶε�������
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"1"));
			newVo.setAttributeValue(unit+"1",num.add(oldnum));	
			//bunit1Ϊ30�����ڿ����ֶεĸ�����
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"1"));
			newVo.setAttributeValue(bunit+"1",bnum.add(boldnum));			  
		}
		if(daynum>30 && daynum<=60){
			//unit1Ϊ30�����ڿ����ֶε�������
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"2"));
			newVo.setAttributeValue(unit+"2",num.add(oldnum));	
			//bunit1Ϊ30�����ڿ����ֶεĸ�����
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"2"));
			newVo.setAttributeValue(bunit+"2",bnum.add(boldnum));					
		}
		if(daynum>60 && daynum<=90){
			//unit1Ϊ30�����ڿ����ֶε�������
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"3"));
			newVo.setAttributeValue(unit+"3",num.add(oldnum));	
			//bunit1Ϊ30�����ڿ����ֶεĸ�����
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"3"));
			newVo.setAttributeValue(bunit+"3",bnum.add(boldnum));				
		}
		if(daynum>90){
			//unit1Ϊ30�����ڿ����ֶε�������
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"4"));
			newVo.setAttributeValue(unit+"4",num.add(oldnum));	
			//bunit1Ϊ30�����ڿ����ֶεĸ�����
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"4"));
			newVo.setAttributeValue(bunit+"4",bnum.add(boldnum));			
		}		
	}
	
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ���
     * 
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){		
	   return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(true),pk_stordoc,null,new UFBoolean(true),new UFBoolean(true),isstordoc, iscargdoc, isvbanchcode, ddatefrom, ddateto);
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ������
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL1(){		
		return WDSWLReportSql.getQuerySQL1(invclcode,pk_stordoc,null,new UFBoolean(true),new UFBoolean(true),isstordoc, iscargdoc, isvbanchcode, ddatefrom, ddateto);
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL,ֻ��ѯ��;
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL2(){	
	   return WDSWLReportSql.getQuerySQL2(invclcode,pk_stordoc,null,new UFBoolean(true),new UFBoolean(true),isstordoc, iscargdoc, isvbanchcode, ddatefrom, ddateto);
	}
	 /**
	  * 
	  * @���ߣ�mlr
	  * @˵�������ɽ������Ŀ:�������
	  * @ʱ�䣺2011-5-11����02:13:25
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
     * �ϼ�
     */
    public void setTolal() throws Exception {
        SubtotalVO svo = new SubtotalVO();
        svo.setGroupFldCanNUll(true);// �����е������Ƿ����Ϊ�ա�
        svo.setAsLeafRs(new boolean[] { true });// �����кϲ����Ƿ���Ϊĩ���ڵ��¼��    
//        svo.setValueFlds(new String[]{"",""});// ��ֵ��:
//        svo.setValueFldTypes(new int[]{1,2});// ��ֵ�е�����:
        svo.setTotalDescOnFld("invclname");// ----�ϼ�---�ֶ� ---- ������
        setSubtotalVO(svo);
        doSubTotal();
    }
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
	   
		return null;
	}
	@Override
	public Map getNewItems() throws Exception {
		
		return null;
	}
	@Override
	public void initReportUI() {
				
	}	
}
