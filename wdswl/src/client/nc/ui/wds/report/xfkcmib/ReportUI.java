package nc.ui.wds.report.xfkcmib;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.WDSReportBaseUI;
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
 * ��ۿ����ϸ����
 * @author mlr
 */
public class ReportUI extends WDSReportBaseUI{
	private static final long serialVersionUID = 1L;
    //���������۱���
	private String invclcode = "00";
    //����λ ����ǰ׺
    private String unit="unit"; 
    //����λ ����ǰ׺
    private  String bunit="bunit";  
    //�����ֶ�����
    private  String  days="days";
    //�������ÿ�������������Ӧ���������ֶ���
    private  String num="num";
     //�������ÿ�����������Ӧ�ĸ������ֶ���
    private  String bnum="bnum";
    //���״̬ ����  ��Ӧ������ֵ
    private  String  stateid="1021S31000000009FS99";
    //���״̬��Ӧ�������ֶ�����
    private  String  stockstate="ss_pk"; 
    //��������;��Ӧ�������ֶ���
    private  String type="type"; 
    //�ƻ��������ֶ�ֵ����
    private  String numplan="plannum";
    //�ƻ��������ֶ�ֵ����
    private  String bnumplan="bplannum";
    //���ô����ʾ�ֶ�
	private static String  invcommon="���ô��";
	//�����ô����ʾ�ֶ�
	private static String  uninvcommon="�����ô��";
	//�����������
	private static String invtypename="invatypename";
	//��������ֶ�   0��ʾ����     1��ʾ������
	private static String  invtype="invtype";
    //��Ҫ�ϲ�����ֵ������
	
    
    private  int[] types={IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD
    	                  ,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD };
    //��Ҫ�ϲ�����ֵ�Ӷ�
    private  String[] combinFields={"unit1","unit2","unit3","unit4","unit5","unit6","unit7","unit8","unit9",
    	                            "bunit1","bunit2","bunit3","bunit4","bunit5","bunit6","bunit7","bunit8","bunit9"  };
 
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT16;
	}
	public ReportUI() {
		super();
		//��ѯ��̬�в���λ��
		setLocation1(4);
		getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); 
		setColumn();
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
     * �����кϲ�
     */
    private void setColumn() {
        //������Ŀ��������
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        //�����ж��Ƿ񰴻�λ ���� չ��  �������λ����չ�� i=2 ֻ��һ�� i=1 ��û�� 0       
        int i=0;
        if(iscargdoc!=null&&iscargdoc.booleanValue()==true){
     	   i=i+1;
        }
        if(isvbanchcode!=null&&isvbanchcode.booleanValue()==true){
           i=i+1;	
        }  
        ColumnGroup zgroup=new ColumnGroup("����");
        ColumnGroup a1=new ColumnGroup("30������");
        a1.add(cardTcm.getColumn(4+i));
        a1.add(cardTcm.getColumn(5+i));
        zgroup.add(a1);
        ColumnGroup a2=new ColumnGroup("30-60��");
        a2.add(cardTcm.getColumn(6+i));
        a2.add(cardTcm.getColumn(7+i));
        zgroup.add(a2);
        ColumnGroup a3=new ColumnGroup("60-90��");
        a3.add(cardTcm.getColumn(8+i));
        a3.add(cardTcm.getColumn(9+i));
        zgroup.add(a3);
        ColumnGroup a4=new ColumnGroup("90���Ժ�");
        a4.add(cardTcm.getColumn(10+i));
        a4.add(cardTcm.getColumn(11+i));
        zgroup.add(a4);      
        cardHeader.addColumnGroup(zgroup);
              
        ColumnGroup zgroup2=new ColumnGroup("����Ʒ");
        zgroup2.add(cardTcm.getColumn(12+i));
        zgroup2.add(cardTcm.getColumn(13+i));
        cardHeader.addColumnGroup(zgroup2);
                
        ColumnGroup zgroup3=new ColumnGroup("�ϼ�");
        zgroup3.add(cardTcm.getColumn(14+i));
        zgroup3.add(cardTcm.getColumn(15+i));
        cardHeader.addColumnGroup(zgroup3);
        
        ColumnGroup zgroup1=new ColumnGroup("����");
        ColumnGroup a11=new ColumnGroup("����");
        a11.add(cardTcm.getColumn(16+i));
        a11.add(cardTcm.getColumn(17+i));
        zgroup1.add(a11);
        ColumnGroup a22=new ColumnGroup("��;");
        a22.add(cardTcm.getColumn(18+i));
        a22.add(cardTcm.getColumn(19+i));
        zgroup1.add(a22);
        ColumnGroup a33=new ColumnGroup("����");
        a33.add(cardTcm.getColumn(20+i));
        a33.add(cardTcm.getColumn(21+i));
        zgroup1.add(a33);   
        cardHeader.addColumnGroup(zgroup1);
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
            	 List<ReportBaseVO[]> list=null;
            	//���û����кϲ�
            	 setColumn();
            	//�����Ƿ��λչ��  �Ƿ�����չ�� ѡ�����ɱ���vo�Ĳ�ѯ���             	            
            	list=getReportVO(new String[]{getQuerySQL(),getQuerySQL1(),getQuerySQL2()});        	
                ReportBaseVO[] vos1= list.get(1);
                setVbachCode(vos1);
                ReportBaseVO[] vos2=list.get(0);   
                ReportBaseVO[] vos3=list.get(2);
                if(vos1 != null&&vos1.length>0 || vos2!=null&&vos2.length>0 || vos3!=null&&vos3.length>0){                	
					//super.updateBodyDigits();
					//�����Ƿ��λչ�� �� �Ƿ�����չ��  �ϲ���ѯ�����ı���vo
					if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==true){
						 ReportBaseVO[]newVos=setVoByContion(vos2,fields3);
						 ReportBaseVO[]newVos1=setVoByContion(vos1,fields3);					
						 ReportBaseVO[] combins=combinVoByFields(newVos,newVos1,fields3,types,combinFields);
						 ReportBaseVO[] newVos2=setVoByContion(vos3,fields3);
						 ReportBaseVO[] combins1=combinVoByFields(newVos2,combins,fields3,types,combinFields);			 
						 setAfterQuery(combins1);
						 setReportBaseVO(combins1);
						 setBodyVO(combins1);	
						 
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==false){
						 ReportBaseVO[]newVos=setVoByContion(vos2,fields);
						 ReportBaseVO[]newVos1=setVoByContion(vos1,fields);
						 ReportBaseVO[] combins=combinVoByFields(newVos,newVos1,fields,types,combinFields);
						 ReportBaseVO[] newVos2=setVoByContion(vos3, fields3);
						 ReportBaseVO[] combins1=combinVoByFields(newVos2,combins,fields3,types,combinFields);			 
						 setAfterQuery(combins1);
						 setReportBaseVO(combins1);
						 setBodyVO(combins1);
						
					}else if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==false){
						 ReportBaseVO[]newVos=setVoByContion(vos2,fields1);
						 ReportBaseVO[]newVos1=setVoByContion(vos1,fields1);
						 ReportBaseVO[] combins=combinVoByFields(newVos,newVos1,fields1,types,combinFields);
						 ReportBaseVO[] newVos2=setVoByContion(vos3, fields3);
						 ReportBaseVO[] combins1=combinVoByFields(newVos2,combins,fields3,types,combinFields);			 
						 setAfterQuery(combins1);
						 setReportBaseVO(combins1);
						 setBodyVO(combins1);
						
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==true){
						 ReportBaseVO[]newVos=setVoByContion(vos2,fields2);
						 ReportBaseVO[]newVos1=setVoByContion(vos1,fields2);
						 ReportBaseVO[] combins=combinVoByFields(newVos,newVos1,fields2,types,combinFields);
						 ReportBaseVO[] newVos2=setVoByContion(vos3, fields3);
						 ReportBaseVO[] combins1=combinVoByFields(newVos2,combins,fields3,types,combinFields);			 
						 setAfterQuery(combins1);
						 setReportBaseVO(combins1);
						 setBodyVO(combins1);
						
					}
					setDefSubtotal(new String[]{"invclname"}, combinFields);  
					//setTolal();
					
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
	 *       ��û�������ֶε� vo ����Ĭ������
	 * @ʱ�䣺2011-7-14����03:30:19
	 * @param vos1
	 */
	private void setVbachCode(ReportBaseVO[] vos) {
		
		if(vos==null && vos.length==0){
			return;
		}
		int size=vos.length;
		for(int i=0;i<size;i++){
		 vos[i].setAttributeValue(banchcode, "");
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
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ����
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){
		StringBuffer sql = new StringBuffer();		
		        sql.append(" select ");//�ֿ�����	
		        sql.append(" t.pk_customize1 pk_stordoc,");
		        if(iscargdoc.booleanValue()==true){
		        sql.append(" t.pk_cargdoc pk_cargdoc,");
		        sql.append(" min(bc.csname) csname,");
		        }
		        sql.append(" cl.pk_invcl pk_invcl,");//�����������
		        sql.append(" iv.fuesed invtype,");//������� ����0  ������1
		        sql.append(" min(cl.vinvclcode) invclcode,");//����������
		        sql.append(" min(cl.vinvclname) invclname,");//�����������	
		        if(isvbanchcode.booleanValue()==true){
		        sql.append(" t.whs_batchcode vbatchcode,");		
		        }
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
				sql.append(" join wds_invbasdoc iv");//�����������
				sql.append(" on t.pk_invbasdoc=iv.pk_invbasdoc");
				sql.append(" join wds_invcl cl");//�����������
				sql.append(" on iv.vdef1=cl.pk_invcl");
				sql.append(" join bd_cargdoc bc");//������λ����
				sql.append(" on t.pk_cargdoc=bc.pk_cargdoc");//
				sql.append(" where isnull(t.dr,0)=0");//
				sql.append(" and isnull(s.dr,0)=0");//
				sql.append(" and isnull(i.dr,0)=0");
				sql.append(" and isnull(iv.dr,0)=0");//
				sql.append(" and isnull(cl.dr,0)=0");
				sql.append(" and isnull(bc.dr,0)=0");
				sql.append(" and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
				sql.append(" and t.creadate between '"+ddatefrom+"' and '"+ddateto+"'");//�����������
				if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
				sql.append(" and t.pk_customize1='"+pk_stordoc+"'");	
				}
				sql.append(" and t.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
				//���ֿ�  ���   ���״̬  �Լ��������������ϲ�
				sql.append(" group by iv.fuesed,cl.pk_invcl,t.pk_customize1,t.pk_invbasdoc,t.ss_pk,t.creadate");
			    if(iscargdoc.booleanValue()==true){
				sql.append(" ,t.pk_cargdoc");	
				}
				if(isvbanchcode.booleanValue()==true){
				sql.append(" ,t.whs_batchcode");		
				}
				return sql.toString();
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ�������˵�
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL1(){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type "+type+",");//��;���������
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		    sql.append(" w.pk_invcl pk_invcl,");//�����������
	        sql.append(" w.invtype invtype,");//������� ����0  ������1
	        sql.append(" min(w.invclcode) invclcode,");//����������
	        sql.append(" min(w.invclname) invclname,");//�����������	
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
			sql.append("  cg.pk_cargdoc pk_cargdoc,");
		    sql.append(" cl.pk_invcl pk_invcl,");//�����������
		    sql.append(" iv.fuesed invtype,");//������� ����0  ������1
		    sql.append(" cl.vinvclcode invclcode,");//����������
		    sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");   
			sql.append("  b.narrangnmu plannum,");   
			sql.append("  b.nassarrangnum bplannum") ;  
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join tb_spacegoods ts");//������λ������ӱ�
			sql.append("  on b.pk_invbasdoc=ts.pk_invbasdoc");
			sql.append("  join wds_cargdoc1 cg");//������λ���������
			sql.append("  on ts.pk_wds_cargdoc=cg.pk_wds_cargdoc");			
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			sql.append("  and isnull(ts.dr,0)=0");//
			sql.append("  and isnull(cg.dr,0)=0");
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus "+type+",");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  cg.pk_cargdoc pk_cargdoc,");
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  b1.ndealnum "+numplan+",");
			sql.append("  b1.nassdealnum "+bnumplan); 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join tb_spacegoods ts");//������λ������ӱ�
			sql.append("  on b1.pk_invbasdoc=ts.pk_invbasdoc");
			sql.append("  join wds_cargdoc1 cg");//������λ���������
			sql.append("  on ts.pk_wds_cargdoc=cg.pk_wds_cargdoc");			
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			sql.append("  and isnull(ts.dr,0)=0");//
			sql.append("  and isnull(cg.dr,0)=0");
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//�ֿ⵵��
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//������λ����
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=0");//���˴������˵�
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			//���ֿ� ��� ��;���������������
			sql.append("  group by w.invtype,w.pk_invcl,w.pk_outwhouse,w.pk_invbasdoc,w.type");	
			if(iscargdoc.booleanValue()==true){
		      sql.append(",w.pk_cargdoc ");	
		    }
			return sql.toString();
	}
	/**
     * 
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL ֻ��ѯ��;���˵� 
     *  
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL2(){
		    StringBuffer sql = new StringBuffer();	
		    sql.append(" select ");
		    sql.append(" w.type "+type+",");//��;���������
		    sql.append(" w.pk_outwhouse pk_stordoc,");
		    if(iscargdoc.booleanValue()==true){
		    sql.append(" w.pk_cargdoc pk_cargdoc,");
		    sql.append(" min(bc.csname) csname,");
		    }
		    sql.append(" w.pk_invcl pk_invcl,");//�����������
	        sql.append(" w.invtype invtype,");//������� ����0  ������1
	        sql.append(" min(w.invclcode) invclcode,");//����������
	        sql.append(" min(w.invclname) invclname,");//�����������	
		    if(isvbanchcode.booleanValue()==true){
		    sql.append(" w.vbatchcode vbatchcode,");		
		    }
		    sql.append(" w.pk_invbasdoc pk_invbasdoc,");		 
			sql.append(" min(s.storname) storename,");//�ֿ�����
	        sql.append(" min(i.invcode) invcode,");//�������
	        sql.append(" min(i.invname) invname,");//�������
	        sql.append(" min(i.invspec) invspec,");//���
	        sql.append(" sum(w.plannum)   plannum,"); //�ƻ� ������ 
			sql.append(" sum(w.bplannum)  bplannum") ; //�ƻ�������
		    sql.append("  from");
	        sql.append("  ((select h.itransstatus "+type+",");
			sql.append("  h.pk_outwhouse pk_outwhouse,"); 
			sql.append("  cg.pk_cargdoc pk_cargdoc,");
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b.pk_invmandoc pk_invmandoc,");     
			sql.append("  b.pk_invbasdoc pk_invbasdoc,");  
			sql.append("  lb.vbatchcode vbatchcode,");//�������
			sql.append("  lb.noutnum plannum,");//ʵ������   
			sql.append("  lb.noutassistnum bplannum") ; //ʵ�������� 
			sql.append("  from wds_soorder h");
			sql.append("  join wds_soorder_b b on h.pk_soorder = b.pk_soorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join tb_spacegoods ts");//������λ������ӱ�
			sql.append("  on b.pk_invbasdoc=ts.pk_invbasdoc");
			sql.append("  join wds_cargdoc1 cg");//������λ���������
			sql.append("  on ts.pk_wds_cargdoc=cg.pk_wds_cargdoc");	
			sql.append("  join tb_outgeneral_b lb");//�������ⵥ�ӱ�
			sql.append("  on h.pk_soorder=lb.csourcebillhid");
			sql.append("  and b.pk_soorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h.dr, 0) = 0");	
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(b.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			sql.append("  and isnull(ts.dr,0)=0");//
			sql.append("  and isnull(cg.dr,0)=0");
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS5'");//�������۵���
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b.dr, 0) = 0 )");
			sql.append("  union all ");
			sql.append("   (select  h1.itransstatus "+type+",");
			sql.append("  h1.pk_outwhouse pk_outwhouse,");   
			sql.append("  cg.pk_cargdoc pk_cargdoc,");
			sql.append(" cl.pk_invcl pk_invcl,");//�����������
			sql.append(" iv.fuesed invtype,");//������� ����0  ������1
			sql.append(" cl.vinvclcode invclcode,");//����������
			sql.append(" cl.vinvclname invclname,");//�����������	
			sql.append("  b1.pk_invmandoc pk_invmandoc,");  
			sql.append("  b1.pk_invbasdoc pk_invbasdoc,") ;
			sql.append("  lb.vbatchcode vbatchcode,");//�������
			sql.append("  lb.noutnum plannum,");//ʵ������   
			sql.append("  lb.noutassistnum bplannum") ; //ʵ�������� 
			sql.append("  from wds_sendorder h1"); 
			sql.append("  join wds_sendorder_b b1 on h1.pk_sendorder = b1.pk_sendorder");
			sql.append("  join wds_invbasdoc iv");//�����������
			sql.append("  on b1.pk_invbasdoc=iv.pk_invbasdoc");
			sql.append("  join wds_invcl cl");//�����������
			sql.append("  on iv.vdef1=cl.pk_invcl");	
			sql.append("  join tb_spacegoods ts");//������λ������ӱ�
			sql.append("  on b1.pk_invbasdoc=ts.pk_invbasdoc");
			sql.append("  join wds_cargdoc1 cg");//������λ���������
			sql.append("  on ts.pk_wds_cargdoc=cg.pk_wds_cargdoc");		
			sql.append("  join tb_outgeneral_b lb");//�������ⵥ�ӱ�
			sql.append("  on h1.pk_sendorder=lb.csourcebillhid");
			sql.append("  and b1.pk_sendorder_b=lb.csourcebillbid");	
			sql.append("  where isnull(h1.dr, 0) = 0");
			sql.append("  and isnull(b1.dr,0)=0");//
			sql.append("  and isnull(iv.dr,0)=0");//
			sql.append("  and isnull(cl.dr,0)=0");
			sql.append("  and isnull(ts.dr,0)=0");//
			sql.append("  and isnull(cg.dr,0)=0");
			sql.append("  and isnull(lb.dr,0)=0");
			sql.append("  and lb.csourcetype='WDS3'");//�������۵���
			sql.append("  and cl.vinvclcode like '"+invclcode+"%'");//����������۵Ĵ������
			sql.append("  and h1.dmakedate between '"+ddatefrom+"' and '"+ddateto+"'");//�����Ƶ�����
			sql.append("  and isnull(b1.dr, 0) = 0 ))w");
			sql.append("  join bd_stordoc s");//�ֿ⵵��
			sql.append("  on w.pk_outwhouse=s.pk_stordoc");
			sql.append("  join bd_invbasdoc i");
			sql.append("  on w.pk_invbasdoc=i.pk_invbasdoc");
			sql.append("  join bd_cargdoc bc");//������λ����
			sql.append("  on w.pk_cargdoc=bc.pk_cargdoc");//
			sql.append("  where ");
			sql.append("  isnull(s.dr,0)=0");//
			sql.append("  and isnull(i.dr,0)=0");//
			sql.append("  and isnull(bc.dr,0)=0");
			sql.append("  and w.type=1");//������;���˵�
			if(pk_stordoc!=null && !pk_stordoc.equalsIgnoreCase("")){
			  sql.append(" and  w.pk_outwhouse='"+pk_stordoc+"'");	
		    }
			//���ֿ� ��� ��;���������������
			sql.append("  group by w.invtype,w.pk_invcl,w.pk_outwhouse,w.pk_invbasdoc,w.type");	
			if(iscargdoc.booleanValue()==true){
		      sql.append(",w.pk_cargdoc ");	
		    }
			if(isvbanchcode.booleanValue()==true){
			  sql.append(",w.vbatchcode ");		
		    }
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
        svo.setValueFlds(combinFields);// ��ֵ��:
        svo.setValueFldTypes(types);// ��ֵ�е�����:
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
