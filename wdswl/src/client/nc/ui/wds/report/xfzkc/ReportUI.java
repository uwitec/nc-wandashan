package nc.ui.wds.report.xfzkc;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.CombinVO;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * ����п�汨��
 * @author mlr
 */
public class ReportUI extends ReportBaseUI{
	private static final long serialVersionUID = 1L;
    private String ddatefrom =null;
    private String ddateto = null;
    //����λ ����ǰ׺
    private static String unit="unit";
    //����λ ����ǰ׺
    private static String bunit="bunit";  
    //���ֿ� ���ά�ȷ�����ֶ�����
    private static String[] fields=new String[]{"pk_stordoc","pk_invbasdoc"};
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
    //vo�ϲ�������
    private static String[] voCombinConds={"pk_stordoc","pk_invbasdoc"};
    //��Ҫ�ϲ�����ֵ������
    private static int[] types={IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD
    	                        ,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD };
    //��Ҫ�ϲ�����ֵ�Ӷ�
    private static String[] combinFields={"unit1","unit2","unit3","unit4","unit5","unit6","unit7","unit8","unit9",
    	                                  "bunit1","bunit2","bunit3","bunit4","bunit5","bunit6","bunit7","bunit8","bunit9"  };
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT10;
	}
	public ReportUI() {
		super();
		initReportUI();
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 *        ���ui��ʼ������ 
	 * @ʱ�䣺2011-7-8����03:20:53
	 */
	private void initReportUI() {		
	  		
	}
	 /**
     * �����кϲ�
     */
    private void setColumn() {
        //������Ŀ��������
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        
        ColumnGroup zgroup=new ColumnGroup("����");
        ColumnGroup a1=new ColumnGroup("30������");
        a1.add(cardTcm.getColumn(4));
        a1.add(cardTcm.getColumn(5));
        zgroup.add(a1);
        ColumnGroup a2=new ColumnGroup("30-60��");
        a2.add(cardTcm.getColumn(6));
        a2.add(cardTcm.getColumn(7));
        zgroup.add(a2);
        ColumnGroup a3=new ColumnGroup("60-90��");
        a3.add(cardTcm.getColumn(8));
        a3.add(cardTcm.getColumn(9));
        zgroup.add(a3);
        ColumnGroup a4=new ColumnGroup("90���Ժ�");
        a4.add(cardTcm.getColumn(10));
        a4.add(cardTcm.getColumn(11));
        zgroup.add(a4);      
        cardHeader.addColumnGroup(zgroup);
              
        ColumnGroup zgroup2=new ColumnGroup("����Ʒ");
        zgroup2.add(cardTcm.getColumn(12));
        zgroup2.add(cardTcm.getColumn(13));
        cardHeader.addColumnGroup(zgroup2);
                
        ColumnGroup zgroup3=new ColumnGroup("�ϼ�");
        zgroup3.add(cardTcm.getColumn(14));
        zgroup3.add(cardTcm.getColumn(15));
        cardHeader.addColumnGroup(zgroup3);
        
        ColumnGroup zgroup1=new ColumnGroup("����");
        ColumnGroup a11=new ColumnGroup("����");
        a11.add(cardTcm.getColumn(16));
        a11.add(cardTcm.getColumn(17));
        zgroup1.add(a11);
        ColumnGroup a22=new ColumnGroup("��;");
        a22.add(cardTcm.getColumn(18));
        a22.add(cardTcm.getColumn(19));
        zgroup1.add(a22);
        ColumnGroup a33=new ColumnGroup("����");
        a33.add(cardTcm.getColumn(20));
        a33.add(cardTcm.getColumn(21));
        zgroup1.add(a33);   
        cardHeader.addColumnGroup(zgroup1);
        getReportBase().getBillModel().updateValue();
    }
	@Override
	public void setUIAfterLoadTemplate() {
		 getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		 setColumn();
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
		  	//���ò�ѯģ��Ĭ�ϲ�ѯ����
	        AccountCalendar  accCal = AccountCalendar.getInstance();     
	        getQueryDlg().setDefaultValue("ddatefrom", accCal.getMonthVO().getBegindate().toString(), "");
	        getQueryDlg().setDefaultValue("ddateto", accCal.getMonthVO().getEnddate().toString(), "");
			getQueryDlg().showModal();
		     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
            	//У�鿪ʼ���ڣ���ֹ����
            	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatefrom");
            	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddateto");
            	ddatefrom = obj1.getRefName();
            	if(ddatefrom == null ||"".equalsIgnoreCase(ddatefrom)){
            		showErrorMessage("�����뿪ʼ����");
            		return ;
            	}
            	//��ֹ�������Ϊ�գ���Ĭ��Ϊ��ǰ����
            	ddateto = obj2.getRefName();
            	if(ddateto == null || "".equalsIgnoreCase(ddateto)){
            		ddateto = _getCurrDate().toString();
            	}
            	//���ĵĲ�ѯ����
            	String qryconditons = getQueryDlg().getChText();
            	if(!qryconditons.contains("��ֹ����")){
            		qryconditons = qryconditons+"����(��ֹ���� С�ڵ��� '"+ddateto+"')";
            	}
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
            	getReportBase().getHeadItem("qryconditons").setWidth(2);
            	getReportBase().setHeadItem("qryconditons", qryconditons);
            	//�õ��Զ����ѯ����
                //�õ���ѯ���
                List<ReportBaseVO[]> list=getReportVO(new String[]{getQuerySQL(),getQuerySQL1()});
                ReportBaseVO[] vos1= list.get(1);
                ReportBaseVO[] vos=list.get(0);            
                if(vos1 != null || vos!=null){                	
					super.updateBodyDigits();
				    ReportBaseVO[]newVos=setVoByContion(vos);
				    ReportBaseVO[]newVos1=setVoByContion(vos1);
				    ReportBaseVO[] combins=CombinVO.combinVoByCondition(newVos,newVos1,voCombinConds,types,combinFields);
					setReportBaseVO(combins);
					setBodyVO(combins);	
	           //     setTolal();	                
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
	 *      ���ձ�������,�ӹ����β�ѯ�γɵı���vo
	 *      �ӹ�������
	 *      ���� �� �ֿ� ��� ���з���,Ȼ���ÿ��vo���кϲ�,��ÿ��vo�ϲ���Ҫ��ϲ���һ��vo
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
	private ReportBaseVO[]  setVoByContion(ReportBaseVO[] vos) {
		if(vos==null && vos.length==0){
			return vos;
		}
		CircularlyAccessibleValueObject[][]voss =SplitBillVOs.getSplitVOs(vos,fields);
		if(voss==null && voss.length==0){
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
    	Integer daynum=PuPubVO.getInteger_NullAs(oldVo.getAttributeValue(days), new Integer(0));	
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
		    UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"9"));
		    newVo.setAttributeValue(unit+"9", oldnum.add(planNum));
		     //���������� ��ʾ�ֶ�unit9
		    UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"9"));
		    newVo.setAttributeValue(bunit+"9", boldnum.add(bplanNum));			
		}else if(itype==1){
		    //��;��������ʾ�ֶ� unit8
			//���������� ��ʾ�ֶ�unit9
			UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"8"));
			newVo.setAttributeValue(unit+"8", oldnum.add(planNum));
			//���������� ��ʾ�ֶ�unit9
			UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"8"));
			newVo.setAttributeValue(bunit+"8", boldnum.add(bplanNum));	
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
		UFDouble oldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(unit+"7"));
		newVo.setAttributeValue(unit+"7",oldnum.add(num));
		//���ԭ���Ĵ��츨���� ��unit7�ֶα�ʾ
		UFDouble boldnum=PuPubVO.getUFDouble_NullAsZero(newVo.getAttributeValue(bunit+"7"));
		newVo.setAttributeValue(bunit+"7",boldnum.add(bnum));		
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
		if(daynum==0){
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
     * �����Զ��������õ���ѯ�������ݵ�SQL
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){
		StringBuffer sql = new StringBuffer();		
		        sql.append(" select ");//�ֿ�����	
		        sql.append(" t.pk_customize1 pk_stordoc,");
		        sql.append(" t.pk_invbasdoc pk_invbasdoc,");
		        sql.append(" min(s.storname) storename,");//�ֿ�����
		        sql.append(" min(i.invcode) invcode,");//�������
		        sql.append(" min(i.invname) invname,");//�������
		        sql.append(" min(i.invspec) invspec,");//���
		        sql.append(" round(sysdate-to_date(t.creadate,'YYYY-MM-DD HH24-MI-SS'),0) "+days+",");//�������
		        sql.append(" t.creadate creadate,");//�������
		        sql.append(" t.ss_pk "+stockstate+",");//���״̬����    
		        sql.append(" sum(t.whs_stocktonnage) "+num+",");//����еĵ�Ʒ�������� ��Ҫ�������ÿ���������
		        sql.append(" sum(t.whs_stockpieces) "+bnum); //����еĵ�Ʒ�ĸ����� ��Ҫ�������ÿ��丨���� 
		        sql.append(" from ");	        	 
				sql.append(" tb_warehousestock t");
				sql.append(" join bd_stordoc s");//�����ֿ�
				sql.append(" on t.pk_customize1=s.pk_stordoc");
				sql.append(" join bd_invbasdoc i");//���������������
				sql.append(" on t.pk_invbasdoc=i.pk_invbasdoc");	
				sql.append(" where isnull(t.dr,0)=0");//
				sql.append(" and isnull(s.dr,0)=0");//
				sql.append(" and isnull(i.dr,0)=0");
				sql.append(" and t.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//�����������
				sql.append(" and t.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
				//���ֿ�  ���   ���״̬  �Լ��������������ϲ�
				sql.append(" group by t.pk_customize1,t.pk_invbasdoc,t.ss_pk,t.creadate");				
				return sql.toString();
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL1(){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type "+type+",");//��;���������
		    sql.append(" w.pk_outwhouse pk_stordoc,");
//		    sql.append(" w.pk_invmandoc pk_invmandoc,");
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");
		    sql.append(" sum(w.plannum)   plannum,"); //�ƻ� ������ 
			sql.append(" sum(w.bplannum)  bplannum,") ; //�ƻ�������
			sql.append(" min(s.storname) storename,");//�ֿ�����
	        sql.append(" min(i.invcode) invcode,");//�������
	        sql.append(" min(i.invname) invname,");//�������
	        sql.append(" min(i.invspec) invspec");//���
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus "+type+",");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");   
			sql.append("  b.narrangnmu plannum,");   
			sql.append("  b.nassarrangnum bplannum") ;  
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  where isnull(h.dr, 0) = 0");
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus "+type+",");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");     
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  b1.ndealnum "+numplan+",");
			sql.append("  b1.nassdealnum "+bnumplan); 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//�ֿ⵵��
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			//���ֿ� ��� ��;���������������
			sql.append("  group by w.pk_outwhouse,w.pk_invbasdoc,w.type");			
			return sql.toString();
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
        svo.setValueFlds(new String[]{"",""});// ��ֵ��:
        svo.setValueFldTypes(new int[]{1,2});// ��ֵ�е�����:
        svo.setTotalDescOnFld("");// ----�ϼ�---�ֶ� ---- ������
        setSubtotalVO(svo);
        doSubTotal();
    }
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
	   
		return null;
	}
}
