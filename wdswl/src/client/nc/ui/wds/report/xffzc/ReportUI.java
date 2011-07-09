package nc.ui.wds.report.xffzc;
import javax.swing.ListSelectionModel;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds.ic.storestate.TbStockstateVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * ��������۱���
 * @author mlr
 */
public class ReportUI extends ReportBaseUI{
	private static final long serialVersionUID = 1L;
    private String ddatefrom =null;
    private String ddateto = null;
    //�ֿ�,��λ,��������,��� 
    private static String[] splitFields=new String[]{"PK_CUSTOMIZE1","pk_cargdoc","dstartdate","pk_invbasdoc"};   	
   //����vo�д��״̬����
    private static String ss_pk="pk_storestate";
    //���״̬������Ϣ�������
    private static String pk="ss_pk";
    //��渨������Ӧ�ֶ�
    private static String bnum="bnum";
    //�����������ֶεĶ�Ӧǰ׺
    private static String num="num";
    //���״̬��̬��,��ʾ�ֶ�
    private static String displayName="ss_state";
    //��̬�д�ԭ���е��ĸ�λ��  ��ʼ���� ��̬��       ���ֶμ�¼��̬�п�ʼ�����λ��
    private static int location=3;
    //�����ܶ����ǻ������ֶ�
    private static String hsl="hsl";
    //�ܶ�����Ӧ�ֶ�
    private static String zton="sumnum";
    //�ϼ��������ֶ�
    private static String total="invname";
    //���״̬���� ����   ��۷�������汨�� �����ֶ�  
    protected  String[] pk_storestates=null;  
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT06;
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
		setDynamicColumn();			
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
	        ReportItem[] olditems = getReportBase().getBody_Items();       
	        ReportItem[] newitems;
			try {
			        newitems = getNewItems();
				    ReportItem[] allitems = combin(olditems,newitems);        
			        getReportBase().setBody_Items(allitems);
			        updateUI();
			} catch (Exception e) {
				e.printStackTrace();
			}            		
	}
	@Override
	public void setUIAfterLoadTemplate() {
          
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *       ����̬���뾲̬�� �ϲ�
	 * @ʱ�䣺2011-7-8����03:30:07
	 * @param olditems
	 * @param newitems
	 * @return
	 */
	private ReportItem[] combin(ReportItem[] olditems, ReportItem[] newitems) {
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
	 *        �Ӵ��״̬���л��Ҫ��װ��̬�е�����
	 * @ʱ�䣺2011-7-8����09:54:00
	 * @return
	 * @throws Exception 
	 */
	private ReportItem[] getNewItems() throws Exception {
		//���˿��״̬Ϊ��������
		String wheresql=" isnull(tb_stockstate.dr,0)=0 and upper(coalesce(tb_stockstate.isok,'N'))='N'";
		SuperVO[] vos= HYPubBO_Client.queryByCondition(TbStockstateVO.class, wheresql);
		if(vos==null || vos.length==0){
			return null;
		}
		//��̬�е�Ԫ��
		ReportItem[] res=new ReportItem[vos.length];
		//��̬�ж�Ӧ�Ŀ��״̬�������
		pk_storestates=new String[vos.length];
		int size=vos.length;
		for(int i=0;i<size;i++){
		  ReportItem it=ReportPubTool.getItem(num+(i+1),(String)vos[i].getAttributeValue(displayName),IBillItem.DECIMAL,i, 80);
		  res[i]=it;
		  pk_storestates[i]=(String) vos[i].getAttributeValue(pk);
		}	
		return res;
	}	
	@Override
	public ReportBaseVO[] getReportVO(String sql) throws BusinessException {
		 ReportBaseVO[] reportVOs = null;
	        try{
	            Class[] ParameterTypes = new Class[]{String.class};
	            Object[] ParameterValues = new Object[]{sql};
	            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
	                    "���ڲ�ѯ...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
	                    "queryVOBySql", ParameterTypes, ParameterValues);
	            if(o != null){
	                reportVOs = (ReportBaseVO[])o;
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
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                if(vos != null){                	
                	//List<ReportBaseVO> list= Arrays.asList(vos);
                	//ReportBaseVO subTotal1 = new ReportBaseVO();
                	//subTotal1.setAttributeValue("", "");
					super.updateBodyDigits();
					ReportBaseVO[] voss=setVoByInvState(vos);
					setReportBaseVO(voss);
					setBodyVO(voss);				
	                setTolal();	                
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
	 *        ��vos ���ֿ�,��λ,��������,��� ���з���
	 *        Ȼ����״̬���� ������������ݽ��кϲ�
	 * @ʱ�䣺2011-7-7����04:46:33
	 * @param vos
	 */
	private ReportBaseVO[] setVoByInvState(ReportBaseVO[] vos) {
		if(vos==null || vos.length==0){
			return vos;
		}
		CircularlyAccessibleValueObject[][] voss=SplitBillVOs.getSplitVOs(vos,splitFields);
		if(voss==null || voss.length==0){
			return vos;
		}
		ReportBaseVO[] newVos=new ReportBaseVO[voss.length];
		int size=voss.length;
		for(int i=0;i<size;i++){
			int size1=voss[i].length;
			//���vo ��������ά�ȷ�����vo����  ��ϵ�һ��vo��
			ReportBaseVO newvo=null;
			for(int j=0;j<size1;j++){
				ReportBaseVO vo=(ReportBaseVO) voss[i][j];
				if(vo==null){
				   continue;
				}
				if(newvo==null){
					newvo=(ReportBaseVO) vo.clone();
				}
				int length=pk_storestates.length;
				//ȡ�ô��״̬������ֵ
				Object s_pk=vo.getAttributeValue(ss_pk);
				String pk_s=null;
				if(s_pk!=null && !s_pk.equals("")){
				   pk_s=(String) s_pk;
				}else{
					continue;
				}				
				for(int k=0;k<length;k++){
					if(pk_storestates[k].equalsIgnoreCase(pk_s)){
					//�����vo��ȡ��Ӧֵ	
					UFDouble znum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(num+(k+1)));
					//��vo��ȡ��Ӧֵ
					UFDouble fnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(bnum));
					newvo.setAttributeValue(num+(k+1), znum.add(fnum));
					break;
					}
				}				
			}
			if(newvo!=null){
				setZton(newvo);
				newVos[i]=newvo;			
			}
		}
		return newVos;
	}
	//�趨��������۵��ܶ���
	private void setZton(ReportBaseVO newvo) {
		if(pk_storestates==null && pk_storestates.length==0){
			return;
		}
		int size =pk_storestates.length;
		UFDouble znum=new UFDouble("0.0");
		for(int i=0;i<size;i++){
			UFDouble inum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(num+(i+1)));
		   	znum=znum.add(inum);		
		}
		//��û����� 
		UFDouble hsl1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(hsl));
		newvo.setAttributeValue(zton, znum.multiply(hsl1));
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
		sql.append(" select");
		sql.append(" min(i.invcode) invcode,");//���ϱ���
		sql.append(" min(i.invname) invname,");//��������
		sql.append(" min(i.invspec) invspec,");//�ͺ�
		sql.append(" w.creadate dstartdate,");//��������
		sql.append(" min(c.csname) cmanager,");//����Ա -->��λ
		sql.append(" min(r.storname) cstore,");//�ֿ�
		sql.append(" sum(w.whs_stocktonnage) num,");//���������  ����ģ����ʾ
		sql.append(" sum(w.whs_stockpieces) bnum,");//��渨����   ����ģ����ʾ
		sql.append(" w.ss_pk  pk_storestate,");//���״̬����
		sql.append(" w.pk_invbasdoc pk_invbasdoc,");//���������������
		sql.append(" min(w.pk_corp) pk_corp,");//��˾
		sql.append(" min(bc.mainmeasrate) "+hsl+",");//������
		sql.append(" w.PK_CUSTOMIZE1 PK_CUSTOMIZE1,");//�ֿ�����
		sql.append(" w.pk_cargdoc pk_cargdoc");//��λ����
//		sql.append(" f.cuserid");//���Ա
		sql.append(" from  tb_warehousestock w ");//���״̬��
		sql.append(" join  tb_stockstate s");//�������״̬��
		sql.append(" on w.ss_pk=s.ss_pk");
//		sql.append(" join tb_stockstaff f");//�����ֿ���Ա�󶨱�
//		sql.append(" on w.PK_CUSTOMIZE1=f.pk_stordoc");
		sql.append(" join bd_invbasdoc i");//�����������
		sql.append(" on w.pk_invbasdoc=i.pk_invbasdoc");
//		sql.append(" join sm_user u");//��������Ա��
//		sql.append(" on f.cuserid=u.cuserid");
		sql.append(" join bd_stordoc r");//�����ֿ�
		sql.append(" on w.PK_CUSTOMIZE1=r.pk_stordoc");
		sql.append(" join bd_cargdoc c");//������λ
		sql.append(" on w.pk_cargdoc =c.pk_cargdoc");
		sql.append(" join bd_convert bc");//����������
		sql.append(" on bc.pk_invbasdoc=w.pk_invbasdoc");
		sql.append(" where isnull(w.dr,0)=0");
		sql.append(" and isnull(s.dr,0)=0");
//		sql.append(" and isnull(f.dr,0)=0");
		sql.append(" and isnull(i.dr,0)=0");
//		sql.append(" and isnull(u.dr,0)=0");
		sql.append(" and isnull(r.dr,0)=0");
		sql.append(" and isnull(c.dr,0)=0");
		sql.append(" and w.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");		
		sql.append(" and upper(coalesce(s.isok,'N'))='N'");//���˷���������
		sql.append(" and  w.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//���˻���ڼ���ڵ�
		sql.append(" group by w.PK_CUSTOMIZE1,w.creadate,w.pk_cargdoc,w.pk_invbasdoc,w.ss_pk");//���ղֿ�  �������� ���Ա ��� ���״̬���з������	
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
        String[] tolfields=new String[pk_storestates.length+1];
        int size=tolfields.length;
        for(int i=0;i<size;i++){
        	if(i==size-1){
        	  tolfields[i]=zton;
        	}else{
        	tolfields[i]=num+(i+1);
        	}
        } 
        int[] types=new int[size];
        for(int i=0;i<size;i++){
        	types[i]=IUFTypes.UFD;
        }
        svo.setValueFlds(tolfields);// ��ֵ��:
        svo.setValueFldTypes(types);// ��ֵ�е�����:
        svo.setTotalDescOnFld(total);// ----�ϼ�---�ֶ� ---- ������
        setSubtotalVO(svo);
        doSubTotal();
    }
}
