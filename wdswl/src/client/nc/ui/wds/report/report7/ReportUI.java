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
 * @author mlr 物流箱粉待发运台账
 */
public class ReportUI extends JxReportBaseUI {
	private static final long serialVersionUID = 2193523266502400113L;
	//左侧基础树 合并维度
    private String[] combinconds={"billcode","ccustomerid","reordedate",
    		                        "chtype","pk_invcl","pk_invmandoc",
    		                        "pk_invbasdoc","pk_defdoc11",
      };
    //按销售订单 汇总合并数据
    private String[] combinconds1={"billcode","ccustomerid","reordedate",
            "chtype","pk_invcl","pk_invmandoc",
            "pk_invbasdoc","pk_defdoc11","b_pk"
    };	
    private String[] combinfs1={"num"};//物流出库单实出数量
	// 出入库标示为 无的主键
	public static String pk_ruout =WdsWlPubConst.WDS_IC_FLAG_wu;

    private String[] combinfs={"nnumber","num","ntaldcnum"};//合并字段

	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	/**
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
//		ReportRowColCrossTool.onCross(this, new String[]{"cubcode","cubname","billcode",
//				"reordedate","storname","xsadress","bauorder"},
//                new String[]{"invtypename","chinvcl","invcode","invname","invspec"}, 
//                new String[]{"num"});
//		setTolal1();//设置合计
	}
	/**
     * 合计
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
		//构建左侧基础数据树
		ReportBaseVO[] jichus=(ReportBaseVO[]) CombinVO.combinData((ReportBaseVO[])ObjectUtils.serializableClone(vos), combinconds,combinfs, ReportBaseVO.class);
		calXuNi(jichus,vos);
		calZheChang(jichus,vos);		
		return jichus;
	}
	/**
	 * 计算正常订单的待发
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-29下午02:02:38
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
		//按销售订单 表体 id 数据汇总合并  物流出库单的实出数量
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
     * 计算虚拟的 订单的 待发
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2011-12-29下午02:01:44
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
		//按销售订单 表体 id 数据汇总合并  物流出库单的实出数量
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
