package nc.vo.zmpub.pub.report2;
import java.util.HashMap;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.zmpub.pub.report.buttonaction2.CrossAction;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.ui.zmpub.pub.report.buttonaction2.LevelSubTotalAction;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
/**
 * @author mlr
 *增加报表配置文件的管理
 */
public class ZmReportBaseUI3 extends ZmReportBaseUI2{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6092657701658462045L;
	
	private Map<String,ReportBuffer> buff = null;// 报表配置缓存

	public ReportBuffer getBuff() {
		
		if (buff == null || buff.get(getConfigPk())==null) {		
			ReportBuffer buf= queryBuffer(getConfigPk());
			buff=new HashMap<String, ReportBuffer>();
			buff.put(getConfigPk(), buf);
		}	
		return buff.get(getConfigPk());
	}
	
	/**
	 * 根据节点号从数据库中查询配置
	 * 
	 * @param modelCode
	 * @return
	 */
	public ReportBuffer queryBuffer(String modelCode) {
		ReportBuffer buffer = null;
		try {
			Class[] ParameterTypes = new Class[] { String.class };
			Object[] ParameterValues = new Object[] { modelCode };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.report.BufferDMO", null,
					"queryById", ParameterTypes, ParameterValues);
			if (o != null) {
				buffer = (ReportBuffer) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return buffer;
	}
	
	/**
	 * 报表保存设置按钮
	 */
	public  void onBoSave() {
		ConfigDlg dig=new ConfigDlg(this);
		if (dig.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			boolean isnew =dig.getIsNew();
			String  detatil=dig.getDetail();
			if (getBuff() == null ) {
				return;
			} else {
				ReportBuffer buff=getBuff();
				if(isnew){
					buff.setPk_config(null);
				}
				buff.setDetatil(detatil);
				
				updateBuffer(getBuff());
			}
		}
	}
	/**
	 * 将缓存配置更新到数据库
	 * 
	 * @param modelCode
	 * @return
	 */
	public ReportBuffer updateBuffer(ReportBuffer buff) {

		
		ReportBuffer buffer = null;
		try {
			Class[] ParameterTypes = new Class[] { ReportBuffer.class };
			Object[] ParameterValues = new Object[] { buff };
			Object o = LongTimeTask.calllongTimeService("zmpub", this,
					"正在查询...", 1, "nc.bs.zmpub.pub.report.BufferDMO", null,
					"updateBuffer", ParameterTypes, ParameterValues);
			if (o != null) {
				buffer = (ReportBuffer) o;
			}
			this.showHintMessage("配置保存成功");
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return buffer;
	}
	
	/**
	 * 从查询模版获得配置文件的主键
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-9-5下午06:35:37
	 *
	 */
	public String getConfigPk(){
		nc.vo.pub.query.ConditionVO[] vos= getQueryDlg().getConditionVO();
		if(vos==null|| vos.length==0)
			return null;
		String pk=null;
		for(int i=0;i<vos.length;i++){
			if(vos[i].getFieldCode().startsWith("sz")){
				return vos[i].getValue();
			}
		}		
		return null;
	}
	/**
	 * 查询完成 设置到ui界面之后 后续处理
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter() throws Exception {
		ReportBuffer buff = getBuff();
		if (buff == null) {
			return;
		}
		String[] rows = buff.getStrRows();
		String[] cols = buff.getStrCols();
		String[] vals = buff.getStrVals();
		UFBoolean istotal = buff.getIstotal();
		Integer lel = buff.getLel();
		/**
		 * 执行数据交叉
		 */
		if (rows != null && cols != null && vals != null && rows.length != 0
				&& cols.length != 0 && vals.length != 0) {
			nc.ui.pub.ButtonObject bo = (ButtonObject) getButton_action_map()
					.getButtonMap().get(IReportButton.CrossBtn);
			CrossAction action = (CrossAction) getButton_action_map().get(bo);
			if (istotal == null)
				istotal = UFBoolean.FALSE;
			action.execute2(rows, cols, vals, istotal, lel);

		}
		/**
		 * 执行合计
		 */
		UFBoolean issub = buff.getIssub();
		UFBoolean issum = buff.getIssum();
		String[] totalfields = buff.getTotfields();
		String[] totalfieldsName = buff.getTotfieldsNames();
		if (totalfields != null && totalfieldsName != null
				&& totalfields.length != 0 && totalfieldsName.length != 0) {
			nc.ui.pub.ButtonObject bo = (ButtonObject) getButton_action_map()
					.getButtonMap().get(IReportButton.LevelSubTotalBtn);
			LevelSubTotalAction action = (LevelSubTotalAction) getButton_action_map()
					.get(bo);
			if (istotal == null)
				istotal = UFBoolean.FALSE;
			action.atuoexecute2(PuPubVO.getUFBoolean_NullAs(issub,
					UFBoolean.FALSE).booleanValue(),
					PuPubVO.getUFBoolean_NullAs(issum, UFBoolean.FALSE)
							.booleanValue(), totalfields, totalfieldsName);
		}
		return;
	}

}
