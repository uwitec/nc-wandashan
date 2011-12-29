package nc.ui.wds.report.report7;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wds.pub.report2.JxReportBaseUI;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * @author mlr ������۴�����̨��
 */
public class ReportUI extends JxReportBaseUI {
	private static final long serialVersionUID = 2193523266502400113L;
	//�������� �ϲ�ά��
    private String[] combinconds={"billcode","ccustomerid","reordedate",
    		                        "chtype","pk_invcl","pk_invmandoc",
    		                        "pk_invbasdoc","pk_defdoc11",
      };
    //�����۶��� ���ܺϲ�����
    private String[] combinconds1={"billcode","ccustomerid","reordedate",
            "chtype","pk_invcl","pk_invmandoc",
            "pk_invbasdoc","pk_defdoc11","b_pk"
    };	
    private String[] combinfs1={"num"};//�������ⵥʵ������
	// ������ʾΪ �޵�����
	public static String pk_ruout =WdsWlPubConst.WDS_IC_FLAG_wu;

    private String[] combinfs={"nnumber","num","ntaldcnum"};//�ϲ��ֶ�

	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
//		ReportRowColCrossTool.onCross(this, new String[]{"cubcode","cubname","billcode",
//				"reordedate","storname","xsadress","bauorder"},
//                new String[]{"invtypename","chinvcl","invcode","invname","invspec"}, 
//                new String[]{"num"});
//		setTolal1();//���úϼ�
	}
	/**
     * �ϼ�
     */
    public void setTolal1() throws Exception {
      new LevelSubTotalAction(this).atuoexecute2();  	
    }  

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return WDSWLReportSql.getOrderDaiFaSql1(getQueryConditon());
	}

	public ReportBaseVO[] dealBeforeSetUI(ReportBaseVO[] vos) throws Exception {
		if(vos==null || vos.length==0)
			return null;
		//����������������
		ReportBaseVO[] jichus=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(vos), combinconds,combinfs, ReportBaseVO.class);
		calXuNi(jichus,vos);
		calZheChang(jichus,vos);		
		return jichus;
	}
	/**
	 * �������������Ĵ���
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-29����02:02:38
	 * @param jichus
	 * @param vos
	 * @throws Exception 
	 */
    private void calZheChang(ReportBaseVO[] jichus, ReportBaseVO[] vos) throws Exception {
      	List<ReportBaseVO>	list=new ArrayList<ReportBaseVO>();
		if(vos==null ||vos.length==0){
		  return;	
		}
		for(int i=0;i<vos.length;i++){
		  String pk=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("pk_defdoc11"));
		  if(!pk_ruout.equals(pk)){
			  list.add(vos[i]);
		  }
		}
		ReportBaseVO[] nvos=(ReportBaseVO[]) ObjectUtils.serializableClone(list.toArray(new ReportBaseVO[0]));
		//�����۶��� ���� id ���ݻ��ܺϲ�  �������ⵥ��ʵ������
		ReportBaseVO[] rvos=(ReportBaseVO[]) CombinVO.combinData(nvos, combinconds1,combinfs1, ReportBaseVO.class);
		  if(rvos==null ||rvos.length==0)
	    	   return;
		
		for(int i=0;i<rvos.length;i++){
        	UFDouble uf=PuPubVO.getUFDouble_NullAsZero(rvos[i].getAttributeValue("nnumber"));
        	UFDouble uf1=PuPubVO.getUFDouble_NullAsZero(rvos[i].getAttributeValue("num"));
        	rvos[i].setAttributeValue("num", uf.sub(uf1));
        }
		ReportBaseVO[] rvos1=(ReportBaseVO[]) CombinVO.combinData(rvos, combinconds, combinfs, ReportBaseVO.class);
		CombinVO.addByContion1(jichus, rvos1, combinconds, null);

	}

	/**
     * ��������� ������ ����
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2011-12-29����02:01:44
     * @param jichus
     * @param vos
	 * @throws Exception 
     */
	private void calXuNi(ReportBaseVO[] jichus, ReportBaseVO[] vos) throws Exception {
	  	List<ReportBaseVO>	list=new ArrayList<ReportBaseVO>();
		if(vos==null ||vos.length==0){
		  return;	
		}
		for(int i=0;i<vos.length;i++){
		  String pk=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("pk_defdoc11"));
		  if(pk_ruout.equals(pk)){
			  list.add(vos[i]);
		  }
		}
		ReportBaseVO[] nvos=(ReportBaseVO[]) ObjectUtils.serializableClone(list.toArray(new ReportBaseVO[0]));
		//�����۶��� ���� id ���ݻ��ܺϲ�  �������ⵥ��ʵ������
		ReportBaseVO[] rvos=(ReportBaseVO[]) CombinVO.combinData(nvos, combinconds1,combinfs1, ReportBaseVO.class);
       if(rvos==null ||rvos.length==0)
    	   return;
		for(int i=0;i<rvos.length;i++){
        	UFDouble uf=PuPubVO.getUFDouble_NullAsZero(rvos[i].getAttributeValue("ntaldcnum"));
        	UFDouble uf1=PuPubVO.getUFDouble_NullAsZero(rvos[i].getAttributeValue("num"));
        	rvos[i].setAttributeValue("num", uf.sub(uf1));
        }
		ReportBaseVO[] rvos1=(ReportBaseVO[]) CombinVO.combinData(rvos, combinconds, combinfs, ReportBaseVO.class);
		CombinVO.addByContion1(jichus, rvos1, combinconds, null);
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report7;
	}
}
