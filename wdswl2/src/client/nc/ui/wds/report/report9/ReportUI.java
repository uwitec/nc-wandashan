package nc.ui.wds.report.report9;
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
/**
 * @author mlr 箱粉发运台账批次明细表
 */
public class ReportUI extends JxReportBaseUI {
	private static final long serialVersionUID = 2193523266502400113L;
	private String[] combinconds={"ordercode","vbillno","pk_stordoc","dbilldate",
			                      "ccustomerid","pk_invmandoc","pk_invbasdoc","pk_defdoc11"
			                      };//合并条件
	private String[] combinfields={"nnum","bnum"};//合并字段
	

	
   
	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public ReportBaseVO[] dealBeforeSetUI(ReportBaseVO[] vos) throws Exception{
		ReportBaseVO[] rvos=(ReportBaseVO[]) ObjectUtils.serializableClone(filter(vos));//过滤  
		ReportBaseVO[] nrovs=(ReportBaseVO[]) CombinVO.combinData(rvos, combinconds, combinfields, ReportBaseVO.class);			
		return nrovs;
	}	
	/**
	 * 按销售订单表体id 和 物流销售运单表体 id过滤数据 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-29下午09:30:08
	 * @param vos
	 * @return
	 */
	private ReportBaseVO[] filter(ReportBaseVO[] vos) {
	   if(vos==null || vos.length==0){
		   return null;
	   }
	   Map<String,ReportBaseVO> map=new HashMap<String,ReportBaseVO>();//过滤map
	   for(int i=0;i<vos.length;i++){
		   String pk=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("get_pk"));
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
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
			setTolal1();//设置合计
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
		return WDSWLReportSql.getOrdertoYunDan(getQueryConditon());
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report9;
	}
}
