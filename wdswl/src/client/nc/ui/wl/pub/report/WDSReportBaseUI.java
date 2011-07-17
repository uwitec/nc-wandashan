package nc.ui.wl.pub.report;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.bd.accperiod.AccountCalendar;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 
 * @���ߣ�mlr
 * @˵�������ɽ������Ŀ
 *        ����ui����
 *        ����Ƿ����� ��λչ���ı���ui
 * @ʱ�䣺2011-7-8����03:20:53
 */
abstract public class WDSReportBaseUI extends ReportBaseUI{	
	private static final long serialVersionUID = -8293771841532487812L;
	//��ѯ������ʼ����
	protected static String ddatefrom =null;
	//��ѯ������������
	protected static String ddateto = null;
	//��ѯ�ֿ�����
    protected static String pk_stordoc=null;
    //�Ƿ񰴻�λչ��
    protected UFBoolean iscargdoc=new UFBoolean(false);
	//�Ƿ�����չ��
    protected UFBoolean isvbanchcode=new UFBoolean(false);
	//��λ�ֶ���
    protected static String cargdoc="csname";
    //�����ֶ���
    protected static  String banchcode="vbatchcode";
    //��¼���μ��صı���Ԫ��  
    protected  ReportItem[]olditems=null;
    //���ֿ� ���ά�ȷ�����ֶ�����
    protected static String[] fields=new String[]{"pk_stordoc","pk_invbasdoc"};
    //���ֿ� ��λ  ���ά�ȷ�����ֶ�����
    protected static String[] fields1=new String[]{"pk_stordoc","pk_cargdoc","pk_invbasdoc"};
    //���ֿ� ��λ  ���ά�ȷ�����ֶ�����
    protected static String[] fields2=new String[]{"pk_stordoc","pk_invbasdoc","vbatchcode"};
    //���ֿ� ��λ  ��� ����ά�ȷ�����ֶ�����
    protected static String[] fields3=new String[]{"pk_stordoc","pk_cargdoc","pk_invbasdoc","vbatchcode"};
    //��ѯ��̬�еĲ���λ�� Ĭ�ϲ����0��
    private  Integer location1=0;
    //����ģ����μ���ʱ ��̬�в���λ��
    protected  Integer location=0;  
    protected  String[] pk_storestates=null;//���״̬���� ����   ��۷�������汨�� ר���ֶ�   ---mlr  
	public WDSReportBaseUI() {
		super();
		initReportUI();
		setDynamicColumn();
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ��ʼ��ui��
	 * @ʱ�䣺2011-7-15����01:47:25
	 */
	public abstract void initReportUI();
	
    /**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     *        ���ò�ѯ��̬��λ��
     * @ʱ�䣺2011-7-15����08:08:09
     * @param location1
     */
	public void setLocation1(Integer location1) {
		this.location1 = location1;
	}
	@Override
	public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException {
		
		return null;
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ����̬���뾲̬�� �ϲ�
	 * @ʱ�䣺2011-7-8����03:30:07
	 * @param olditems һ����Ϊ�Ǿ�̬��
	 * @param newitems һ����Ϊ�Ƕ�̬��
	 * @return
	 */
	protected ReportItem[] combin(ReportItem[] olditems, ReportItem[] newitems,int location) {
		if(newitems==null || newitems.length==0){
		  return olditems;	
		}
		ReportItem[] its=new ReportItem[olditems.length+newitems.length];
		System.arraycopy(olditems, 0, its, 0, location);
	    System.arraycopy(newitems, 0, its,location,newitems.length);
	    System.arraycopy(olditems, location, its, location+newitems.length, olditems.length-location);
		return its;
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 *        ����ĳ��ά��(����)
	 *        �����������������ֶζ�Ӧֵ��ͬ�ĺϲ�
	 *        ���� ��ֵ�ֶ�������������� �ж���Ҫ��͵��ֶ�
	 *        �����������
	 *        
	 *        ʹ�ñ�������ǰ��������
     *        ����vo���鰴ά������ֻ�ܲ鵽һ������������vo
	 *             
	 * @ʱ�䣺2011-7-11����09:12:25
	 * @param vos
	 * @param vos1
	 * @param voCombinConds �����ֶ�����
	 * @param types ��ֵ����
	 * @param combinFields ��ֵ�ֶ�
	 * @return
	 */
	public  ReportBaseVO[] combinVoByFields(ReportBaseVO[] vos, ReportBaseVO[] vos1,
			String[] voCombinConds, int[] types,String[] combinFields) {
		return CombinVO.combinVoByFields(vos, vos1, voCombinConds, types, combinFields);
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ���ò�ѯ���ɵĶ�̬��
	 * @ʱ�䣺2011-7-15����01:10:00
	 */
	private void setDynamicColumn1() {
		 ReportItem[] newitems;
			try {
			        newitems = getNewItems1();
				    ReportItem[] allitems = combin(olditems,newitems,location1);        
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}           		
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ��ò�ѯ��̬��Ԫ��
	 * @ʱ�䣺2011-7-8����09:54:00
	 * @return
	 * @throws Exception 
	 */
	private ReportItem[] getNewItems1() throws Exception {
	 ReportItem it=null;
	 ReportItem it1=null;
     if(iscargdoc.booleanValue()==true){
	   it=ReportPubTool.getItem(cargdoc,"��λ",IBillItem.STRING,1, 80);
     }
     if(isvbanchcode.booleanValue()==true){
    	 it1=ReportPubTool.getItem(banchcode,"����",IBillItem.STRING,2, 80);
     } 
     if(it==null && it1==null){
    	 return null;
     }else if(it!=null && it1!=null){
    	 return new ReportItem[]{it,it1};
     }else if(it ==null && it1!=null){
    	 return new ReportItem[]{it1};
     }else if(it!=null && it1==null){
    	 return new ReportItem[]{it};
     }else{
    	 return null;
     }
	
	}	

	@Override
	public void onQuery() {
	  	//���ò�ѯģ��Ĭ�ϲ�ѯ����
        AccountCalendar  accCal = AccountCalendar.getInstance();     
        getQueryDlg().setDefaultValue("ddatefrom", accCal.getMonthVO().getBegindate().toString(), "");
        getQueryDlg().setDefaultValue("ddateto", accCal.getMonthVO().getEnddate().toString(), "");
		getQueryDlg().showModal();
	     if (getQueryDlg().getResult() == UIDialog.ID_OK) {		  
	    	//��ձ�������
	    	 clearBody();	    	
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
        	//���ò�ѯ����
        	setQueryCondition();           	
        	//���ö�̬��
        	setDynamicColumn1();
        	//���ĵĲ�ѯ����
            
        	String qryconditons = getQueryDlg().getChText();
        	if(!qryconditons.contains("��ֹ����")){
        		qryconditons = qryconditons+"����(��ֹ���� С�ڵ��� '"+ddateto+"')";
        	}
        	getReportBase().setHeadItem("ddatefrom", ddatefrom);
        	getReportBase().setHeadItem("ddateto", ddateto);
        	getReportBase().getHeadItem("qryconditons").setWidth(2);
        	getReportBase().setHeadItem("qryconditons", qryconditons);
	    }	
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ÿ�β�ѯǰ��ձ�������
	 * @ʱ�䣺2011-7-16����02:38:27
	 */
	private void clearBody() {
		 setBodyVO(null);
    	 updateUI();		
	}
	@Override
	public void setUIAfterLoadTemplate() {
		
		
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ö�̬��
	 * @ʱ�䣺2011-7-8����03:22:30
	 */	
	private void setDynamicColumn() {
		 //-----------------------���� ģ��  ֧�ֶ�̬��      
		 getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);	               
	        olditems = getReportBase().getBody_Items();       
	        ReportItem[] newitems=null;
			try {
			        Map map = getNewItems();
			        if(map !=null){
			        location=PuPubVO.getInteger_NullAs(map.get("location"), new Integer(0));
			        newitems=(ReportItem[])map.get("items");
			        }
				    ReportItem[] allitems = combin(olditems,newitems,location);   
				    olditems=allitems;
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}            		
	}
    /**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     *        ��ȡ����ģ���ʼ��ʱ�Ķ�̬��
     *        map key=location ��Ŷ�̬�в���λ��
     *            key=items    ��Ŷ�̬��Ԫ��
     * @ʱ�䣺2011-7-15����02:21:31
     * @return
     */
	abstract public Map getNewItems()throws Exception ;
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *      ���ò�ѯ����
	 * @ʱ�䣺2011-7-15����01:08:49
	 */
	private void setQueryCondition() {
		ConditionVO[] vos=getQueryDlg().getConditionVO();
    	//�Ӳ�ѯ�Ի���,��ȡ�ֿ�����
    	int size=vos.length;
    	//�Ӳ�ѯ�Ի���,��ȡ�Ƿ��λչ��
    	iscargdoc=new UFBoolean(false);
    	//�Ӳ�ѯ�Ի���,��ȡ������չ��
    	isvbanchcode=new UFBoolean(false);
    	pk_stordoc=null;
    	for(int i=0;i<size;i++){
    		if(vos[i].getFieldCode().equalsIgnoreCase("pk_stordoc")){
    			pk_stordoc=vos[i].getValue();
    		}
    		if(vos[i].getFieldCode().equalsIgnoreCase("iscargdoc")){
    			iscargdoc=PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), new UFBoolean(false));           			
    		}
            if(vos[i].getFieldCode().equalsIgnoreCase("isvbanchcode")){
            	isvbanchcode=PuPubVO.getUFBoolean_NullAs(vos[i].getValue(), new UFBoolean(false));
    		}         		
    	}		
	}	

}
