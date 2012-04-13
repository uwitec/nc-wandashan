package nc.ui.wds.report.report5;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.ui.scm.util.ObjectUtils;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.zmpub.pub.report.buttonaction2.LevelSubTotalAction;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.CombinVO;
import nc.vo.zmpub.pub.report2.JxReportBaseUI;
import nc.vo.zmpub.pub.report2.ReportRowColCrossTool;
/**
 * @author mlr ������۷���̨��
 */
public class ReportUI extends JxReportBaseUI {
	private static final long serialVersionUID = 2193523266502400113L;
	private String[] combinconds={"ordercode","vbillno","pk_stordoc",
			                      "ccustomerid","pk_invmandoc","pk_invbasdoc","pk_defdoc11"
			                      ,"b_pk","b_pk1"};//�ϲ�����
	private String[] combinfields={"num"};//�ϲ��ֶ�
	
	private String[] combinconds1={"ordercode","vbillno","pk_stordoc", "ccustomerid","pk_defdoc11",
            };//װж�Ѻϲ�����

	private String[] combinfields1={"zxfzx","zxfzx1","zxftq","zxfcm"};//װж�Ѻϲ��ֶ�

	
   
	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public ReportBaseVO[] dealBeforeSetUI(ReportBaseVO[] vos) throws Exception{
		ReportBaseVO[] rvos=(ReportBaseVO[]) ObjectUtils.serializableClone(filter(vos));//����  
		ReportBaseVO[] nrovs=(ReportBaseVO[]) CombinVO.combinData(rvos, combinconds, combinfields, ReportBaseVO.class);
		
		ReportBaseVO[] rvos2=(ReportBaseVO[]) ObjectUtils.serializableClone(filter1(vos));//
		ReportBaseVO[] nrvos2=(ReportBaseVO[]) CombinVO.combinData(rvos2, combinconds1, combinfields1, ReportBaseVO.class);
		
		
		//����׷�� ���������װж��׷�ӵ�  nrvos
	
		CombinVO.addByContion1(nrovs, nrvos2, combinconds1, "xe");
		
		return nrovs;
	}	
	/**
	 * �����۶�������id �� ���������˵����� id�������� 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-29����09:30:08
	 * @param vos
	 * @return
	 */
	private ReportBaseVO[] filter(ReportBaseVO[] vos) {
	   if(vos==null || vos.length==0){
		   return null;
	   }
	   Map<String,ReportBaseVO> map=new HashMap<String,ReportBaseVO>();//����map
	   for(int i=0;i<vos.length;i++){
		   String pk=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("b_pk"));
		   String pk1=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("b_pk1"));
		   map.put(pk+pk1, vos[i]);
	   }
	   if(map.size()==0)
		   return null;
	   List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
	   for(String key:map.keySet()){
		   list.add(map.get(key));
	   }   
		return list.toArray(new ReportBaseVO[0]);
	}
	/**
	 * ����װж�Ѻ��㵥 ����id �������� ���ڼ���װж��
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-29����09:30:08
	 * @param vos
	 * @return
	 */
	private ReportBaseVO[] filter1(ReportBaseVO[] vos) {
	   if(vos==null || vos.length==0){
		   return null;
	   }
	   Map<String,ReportBaseVO> map=new HashMap<String,ReportBaseVO>();//����map
	   for(int i=0;i<vos.length;i++){
		   String pk=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("b_pk2"));
		   map.put(pk, vos[i]);
	   }
	   if(map.size()==0)
		   return null;
	   List<ReportBaseVO> list=new ArrayList<ReportBaseVO>();
	   for(String key:map.keySet()){
		   list.add(map.get(key));
	   }   
		return list.toArray(new ReportBaseVO[0]);
	}
	/**
	 * ��ѯ��� ���õ�ui����֮�� ��������  
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
		ReportRowColCrossTool.onCross(this, new String[]{"storname","custcode","custname","ordercode","vbillno","isxuni"
				,"carcode","vdrivername","vdrivercorp","sorderdate","cartime","forderdate",
				"djrfh","xsadress","jxstel","zcyxtime","daodz",
				"yfprice","yfgls","yfhj",
				"zxfzxxe","zxfzx1xe","zxftqxe","zxfcmxe"
				},
                new String[]{"invtypename","chinvcl","invcode","invname","invspec"}, 
                new String[]{"num"});
		setTolal1();//���úϼ�
	}
	/**
     * �ϼ�
     */
    public void setTolal1() throws Exception {
    	   new LevelSubTotalAction(this).atuoexecute2(true,true,
    	    		  new String[]{"isxuni."},
    	    		  new String[]{"�Ƿ�����"});  	
	
    }  

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return WDSWLReportSql.getOrdertoYunDan(getQueryConditon());
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report5;
	}
}
