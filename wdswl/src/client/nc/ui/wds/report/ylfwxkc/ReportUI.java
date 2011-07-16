package nc.ui.wds.report.ylfwxkc;

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
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds.ic.storestate.TbStockstateVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * ����ԭ�Ϸ���Ч��汨��
 * @author yf
 *
 */
public class ReportUI extends ReportBaseUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**ԭ�Ϸۿ�汨��ڵ� */
	public static String  REPORT_YLFWXKC="80100213";
	private int splength = 0;
    
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
    private static int location=4;
    //�����ܶ����ǻ������ֶ�
    private static String hsl="hsl";
    //�ܶ�����Ӧ�ֶ�
    private static String zton="sumnum";
    //�ܴ���
    private static String zdai="sumnum1";
    //�ϼ��������ֶ�
    private static String total="invname";
    //���״̬���� ����   ��۷�������汨�� �����ֶ�  
    protected  String[] pk_storestates=null;  	

    private String[] displaynames = null;
    
    private String pk_stordoc=null;
  //�������ԭ�Ϸ۱���
	private   String invclcode = "01";
    
	private void setCustomColumns() {
		//������Ŀ��������
    	//��ǰ���ݱ�
        UITable cardTable = getReportBase().getBillTable();
        //����ͷ
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        //�� �ж���
        TableColumnModel cardTcm = cardTable.getColumnModel();
        //�½� �б�
        ColumnGroup zgroup1=new ColumnGroup("��������");
        //�ж��� �������б�
        zgroup1.add(cardTcm.getColumn(2));
        zgroup1.add(cardTcm.getColumn(3));
        
        //�½� �б�
        ColumnGroup zgroup=new ColumnGroup("���ϸ�ԭ��");
        //�ж��� �������б�
        ColumnGroup[] zgroups = new ColumnGroup[splength];
        int j = 0;
        for(int i = 0; i < splength; i++){
        	zgroups[i] = new ColumnGroup(displaynames[i]);
        	zgroups[i].add(cardTcm.getColumn(location+j));
        	j++;
        	zgroups[i].add(cardTcm.getColumn(location+j));
        	j++;
        	zgroup.add(zgroups[i]);
        }
        //�б� ���� ��ͷ
        cardHeader.addColumnGroup(zgroup1);
        cardHeader.addColumnGroup(zgroup);
        //���� ���ݱ�
        getReportBase().getBillModel().updateValue();
	}	
	
	@Override
	public String _getModelCode() {
		return this.REPORT_YLFWXKC;
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
		setCustomColumns();//���úϲ�����
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
		ReportItem[] res=new ReportItem[vos.length*2];
		//��̬�ж�Ӧ�Ŀ��״̬�������
		pk_storestates=new String[vos.length];
		int size=vos.length;
		int j = 0;
		splength = size;
		displaynames = new String[size];
		for(int i=0;i<size;i++){
			//��Ч��� ״̬��
			//(String)vos[i].getAttributeValue(displayName)
			//ColumnGroup[] cols=new ColumnGroup[displays.length];
			displaynames[i] = (String)vos[i].getAttributeValue(displayName);
		  ReportItem it=ReportPubTool.getItem(num+(i+1),"������",IBillItem.DECIMAL,i, 80);
		  ReportItem it1=ReportPubTool.getItem(bnum+(i+1),"������",IBillItem.DECIMAL,i, 80);
		  res[j]=it;
		  j++;
		  res[j]=it1;
		  pk_storestates[i]=(String) vos[i].getAttributeValue(pk);
		  j++;
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
               
            	
            	//�õ���ѯ����VOs
            	ConditionVO[] conditionVOs=getQueryDlg().getConditionVO();
            	//�Ӳ�ѯ�Ի���,��ȡ�ֿ�����
            	int size=conditionVOs.length;
            	//�ֿ�����
            	pk_stordoc=null;
            	for(int i=0;i<size;i++){
            		if(conditionVOs[i].getFieldCode().equalsIgnoreCase("pk_stordoc")){
            			pk_stordoc=conditionVOs[i].getValue();
            		}        		
            	}	
            	 //�õ�sql��ѯ���
            	
            	clearBody();
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                
                if(vos != null){                	
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
	private void clearBody() {
		setBodyVO(null);
		updateUI();
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
					UFDouble fnum=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(num));
					newvo.setAttributeValue(num+(k+1), znum.add(fnum));
					//�����vo��ȡ��Ӧֵ	
					UFDouble znum1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(bnum+(k+1)));
					//��vo��ȡ��Ӧֵ
					UFDouble fnum1=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(bnum));
					newvo.setAttributeValue(bnum+(k+1), znum1.add(fnum1));
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
			UFDouble inum=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(bnum+(i+1)));
		   	znum=znum.add(inum);		
		}
		//��û����� 
		UFDouble hsl1=PuPubVO.getUFDouble_NullAsZero(newvo.getAttributeValue(hsl));
		newvo.setAttributeValue(zton, znum.multiply(hsl1));
		newvo.setAttributeValue(zdai, znum);
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
		sql.append(" min(i.invspec) invspec,");//���
		sql.append(" w.creadate dstartdate,");//��������		
		sql.append(" min(r.storname) cstore,");//�ֿ�
		sql.append(" sum(w.whs_stocktonnage) num,");//���������  ����ģ����ʾ
		sql.append(" sum(w.whs_stockpieces) bnum,");//��渨����   ����ģ����ʾ
		sql.append(" w.ss_pk  pk_storestate,");//���״̬����
		sql.append(" w.pk_invbasdoc pk_invbasdoc,");//���������������
		sql.append(" min(w.pk_corp) pk_corp,");//��˾
		sql.append(" min(bc.mainmeasrate) "+hsl+",");//������
//		if(iscargdoc.booleanValue()==true){
//		sql.append(" w.pk_cargdoc pk_cargdoc,");//��λ����
//		sql.append(" min(c.csname) "+cargdoc+",");//����Ա -->��λ
//	    }
//		if(isvbanchcode.booleanValue()==true){
//		sql.append(" w.whs_batchcode "+banchcode+",");//����
//		}
		sql.append(" w.PK_CUSTOMIZE1 PK_CUSTOMIZE1");//�ֿ�����		
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
		sql.append(" join wds_invbasdoc iv");//�����������
		sql.append(" on w.pk_invbasdoc=iv.pk_invbasdoc");
		sql.append(" join wds_invcl cl");//�����������
		sql.append(" on iv.vdef1=cl.pk_invcl");
		sql.append(" where isnull(w.dr,0)=0");
		sql.append(" and isnull(s.dr,0)=0");
//		sql.append(" and isnull(f.dr,0)=0");
		sql.append(" and isnull(i.dr,0)=0");
//		sql.append(" and isnull(u.dr,0)=0");
		sql.append(" and isnull(r.dr,0)=0");
		sql.append(" and isnull(c.dr,0)=0");
		sql.append(" and isnull(iv.dr,0)=0");
		sql.append(" and isnull(cl.dr,0)=0");
		sql.append(" and cl.vinvclcode like '"+invclcode+"%'");//��������ԭ�Ϸ۵Ĵ������
		sql.append(" and w.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");		
		sql.append(" and upper(coalesce(s.isok,'N'))='N'");//���˷���������
		if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			sql.append(" and w.PK_CUSTOMIZE1='"+pk_stordoc+"'");	
		}
		sql.append(" and  w.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//���˻���ڼ���ڵ�
		sql.append(" group by w.PK_CUSTOMIZE1,w.creadate,w.pk_invbasdoc,w.ss_pk");//���ղֿ�  �������� ���Ա ��� ���״̬���з������
//		if(iscargdoc.booleanValue()==true){
//			sql.append(" ,w.pk_cargdoc");//��λ����
//		}
//		if(isvbanchcode.booleanValue()==true){
//			sql.append(" ,w.whs_batchcode ");//����
//	    }
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
        String[] tolfields=new String[pk_storestates.length*2+2];
        int size=tolfields.length;
        int j = 0;
        for(int i=0;i<pk_storestates.length+2;i++){
        	if(j==size-1){
        		tolfields[j]=zton;
        	}else if(j==size-2){
        		tolfields[j]=zdai;
        	}else{
        		tolfields[j]=num+(i+1);
        		j++;
        		tolfields[j]=bnum+(i+1);
        	}
        	j++;
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

