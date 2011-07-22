package nc.ui.wds.report.xffzc;
import java.util.HashMap;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.WDSReportBaseUI;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
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
public class ReportUI extends WDSReportBaseUI{
	private static final long serialVersionUID = 1L;
   //����vo�д��״̬����
    private  String ss_pk="pk_storestate";
    
   //���������۱���
	private   String invclcode = "00";
    //��渨������Ӧ�ֶ�
    private  String bnum="bnum";
    //�����������ֶεĶ�Ӧǰ׺
    private  String num="num";
    
    //��̬�д�ԭ���е��ĸ�λ��  ��ʼ���� ��̬��       ���ֶμ�¼��̬�п�ʼ�����λ��
    private  int location=3;
    //�����ܶ����ǻ������ֶ�
    private  String hsl="hsl";
    //�ܶ�����Ӧ�ֶ�
    private  String zton="sumnum";
    //�ϼ��������ֶ�
    private  String total="invname";  
    
	@Override
	public String _getModelCode() {	
		return WdsWlPubConst.REPORT06;
	}
	public ReportUI() {
		super();
		//ȥ���ֶ��Զ�����Ĺ���
		getReportBase().getBillTable().removeSortListener();
	}
	@Override
	public void setUIAfterLoadTemplate() {
          
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        �Ӵ��״̬���л��Ҫ��װ��̬�е�����
	 *        ������ø÷���
	 * @ʱ�䣺2011-7-8����09:54:00
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map getNewItems() throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object>();
		//���ö�̬�в���λ��
	    map.put("location", new Integer(3));
		//���˿��״̬Ϊ��������
		String wheresql=" isnull(tb_stockstate.dr,0)=0 and upper(coalesce(tb_stockstate.isok,'N'))='N'";
		SuperVO[] vos= HYPubBO_Client.queryByCondition(TbStockstateVO.class, wheresql);
		if(vos==null || vos.length==0){
			return null;
		}
		//���״̬��̬��,��ʾ�ֶ�
	     String displayName="ss_state";
	     //���״̬������Ϣ�������      
	     String pk="ss_pk";  
	    //�������ÿ�������������Ӧ���������ֶ���  
	     String num="num";   	     
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
		map.put("items",res);
		
		return map;
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
			    super.onQuery();
            	//�õ��Զ����ѯ����
                //�õ���ѯ���
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                if(vos != null && vos.length>0){                	
					//super.updateBodyDigits();					
					//�����Ƿ��λչ�� �� �Ƿ�����չ��  �ϲ���ѯ�����ı���vo
                	 ReportBaseVO[] newVos=null;
					if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==true){
						newVos=setVoByInvState(vos, fields0);
					     			
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==false){
						newVos=setVoByInvState(vos, fields4);
					    			
					}else if(iscargdoc.booleanValue()==true&&isvbanchcode.booleanValue()==false){
					   newVos=setVoByInvState(vos, fields1);
					    							
					}else if(iscargdoc.booleanValue()==false&&isvbanchcode.booleanValue()==true){
					   newVos=setVoByInvState(vos, fields2);	
					} 
					setReportBaseVO(newVos);
				    setBodyVO(newVos);	
				    setTolal();
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
	 * @param vos  vo����
	 * @param splitFields �����ά������
	 */
	private ReportBaseVO[] setVoByInvState(ReportBaseVO[] vos,String[] splitFields) {
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
		return WDSWLReportSql.getQuerySQL(invclcode,new UFBoolean(false),pk_stordoc,null,new UFBoolean(false),new UFBoolean(false),new UFBoolean(true), iscargdoc, isvbanchcode, ddatefrom, ddateto);
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
	@Override
	public void initReportUI() {
		
		
	}
}
