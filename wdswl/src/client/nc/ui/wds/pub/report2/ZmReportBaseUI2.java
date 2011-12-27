package nc.ui.wds.pub.report2;
import java.util.List;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 支持分组查询的报表基类
 * 支持数据交叉  分级次的小计 合计  过滤 排序  刷新 以及打印
 * 支持查询动态列 和 初始化动态列
 * @author mlr
 *
 */
public class ZmReportBaseUI2 extends JxReportBaseUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2436203500270302801L;
	
	private String[] sqls=null;

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	@Override
	public void initReportUI() {

	}
	  @Override
		public void onQuery() {
			getQueryDlg().showModal();
			if (getQueryDlg().getResult() == UIDialog.ID_OK) {
				try {
					// 清空表体数据
					clearBody();
					setDynamicColumn1();
					// 得到查询结果
				  	   setColumn();

					List<ReportBaseVO[]> list = getReportVO(getSqls());
                    sqls=getSqls();
					ReportBaseVO[] vos = null;
					vos=dealBeforeSetUI(list);
					if (vos == null || vos.length == 0)
						return;
					if (vos != null) {
						super.updateBodyDigits();
						setBodyVO(vos);
						updateVOFromModel();//重新加载初始化公式  
						dealQueryAfter();//查询后的后续处理 一般用于 默认数据交叉之类的操作
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	/**
	 * 接收查询的组合sql
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		return null;
		
	}
	public void onboRefresh()throws Exception{
    	   //清空表体数据
  	   clearBody();	    	         	
    	  //设置动态列
  	  setDynamicColumn1();
        //得到查询结果
    	  ReportBaseVO[] vos=null;
  	  //设置基本列合并
  	   setColumn();
  	  //设置vo
	  List<ReportBaseVO[]> list = getReportVO(getSqls());
      sqls=getSqls();
	  ReportBaseVO[] vos1 = null;
	  vos1=dealBeforeSetUI(list);
		if (vos1 == null || vos1.length == 0)
			return;
		if (vos1 != null) {
			super.updateBodyDigits();
			setBodyVO(vos1);
			updateVOFromModel();//重新加载初始化公式  
			dealQueryAfter();//查询后的后续处理 一般用于 默认数据交叉之类的操作
		}
       this.showHintMessage("刷新操作结束");
  }
	/**
	 * 设置到ui界面之前 处理分组查询后的数据
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{
		
		return null;
	}
	/**
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter(){
		
		return ;
	}
	
	/**
	 * 分组查询
	 */
	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService(
					null, this, "正在查询...", 1,
					"nc.bs.ca.pub.report.ReportDMO", null, "queryVOBySql",
					ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "警告", e.getMessage());
		}
		return reportVOs;
	}

}
