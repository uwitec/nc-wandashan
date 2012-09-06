package nc.vo.zmpub.pub.report2;
import java.awt.Component;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIRefPane;
/**
 * 支持报表缓存的查询对话框
 * @author mlr
 * 缓存对应字段  szreportconfig
 */
public class ZmReportBaseQueryDlg extends nc.ui.trade.report.query.QueryDLG
{
	/**
	 * 
	 */
	private String nodecode=null;
	private static final long serialVersionUID = 1L;

	public ZmReportBaseQueryDlg(java.awt.Container parent,String nodecode) {
		super(parent);
		this.nodecode=nodecode;
		
	}
	@Override
	public void initData(){
		super.initData();
		init();
	}
	public void init() {
		try {
			((UIRefPane) getComponent("szreportconfig")).getRefModel()
					.addWherePart(" and nodecode = '" + nodecode + "' and isnull(dr,0)=0 ");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}


	protected Component getComponent(String filedcode) {
		Object o = getValueRefObjectByFieldCode(filedcode);
		Component jb = null;
		if (o instanceof UIRefCellEditor) {
			jb = ((UIRefCellEditor) o).getComponent();// getUITabInput().getCellEditor(1,
			// 4);
		} else {
			jb = (Component) o;
		}

		return jb;
	}
	

}
