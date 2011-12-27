package nc.ui.wds.pub.button.report2;
/**
 * 此类是报表预置按钮和响应事件的Map,它记录了按钮的位置信息，以及添加删除按钮的机制
 * 用于nc.ui.pm.pub.report.ReportBaseUI类。
 * @author guanyj1
 */
import java.util.ArrayList;
import java.util.HashMap;

import nc.ui.pub.ButtonObject;
import nc.ui.report.base.EmptyAction;
import nc.ui.report.base.IButtonActionAndState;
import nc.ui.wds.pub.report2.ReportBaseUI;

public class ButtonAssets extends HashMap {
	
	protected ReportBaseUI reportBaseUI = null;
	//按顺序记录按钮
	protected ArrayList<Object> buttonListByOrder = new ArrayList<Object>();
	
	private HashMap<Integer, ButtonObject> m_btnhs=new HashMap<Integer, ButtonObject>();
	
	
	public ButtonAssets(ReportBaseUI reportBaseUI) {
		this.reportBaseUI = reportBaseUI;
		initAllActions();
		initButtonActionMap();
		initButtonHash();
	}
	
/**
 * 
 * date:2007-8-22 下午12:58:08
 * @author:saf
 * void
 */
	private void initButtonHash() {
		m_btnhs.put( IReportButton.QueryBtn, m_boQuery);
		m_btnhs.put(CaPuBtnConst.onboRefresh, refresh);
		m_btnhs.put( IReportButton.FilterBtn, m_boFilter);
	    m_btnhs.put( IReportButton.CrossBtn,m_boCross);//交叉处理
		m_btnhs.put( IReportButton.ColumnFilterBtn,m_boColumnFilter);//栏目设置
		m_btnhs.put( IReportButton.LevelSubTotalBtn,m_boLevelSubTotal);
		m_btnhs.put( IReportButton.CodeLevelSubTotalBtn,m_boCodeLevelSubTotal);
		m_btnhs.put( IReportButton.SubTotalBtn,m_boSubTotal);
		m_btnhs.put( IReportButton.SortBtn,m_boSort);//排序
		m_btnhs.put( IReportButton.PrintPreviewBtn,m_boPrintPreview);
		m_btnhs.put( IReportButton.PrintDirectBtn,m_boPrintDirect);
		m_btnhs.put( IReportButton.PrintTempletBtn,m_boPrintTemplet);
		m_btnhs.put( IReportButton.PrintBtn,m_boPrint);
		m_btnhs.put( IReportButton.PenerateBtn,m_boPenerate);
	}
	
	public static final ButtonObject m_boQuery = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000006")/*@res "查询"*/, 
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000000")/*@res "查询单据"*/,
			0,"查询");

	//protected IButtonActionAndState = new PageAction();
	public static final ButtonObject m_boPrintPreview = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000001")/*@res "打印预览"*/, 
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000001")/*@res "打印预览"*/, 
			0,"打印预览");

	public static final ButtonObject m_boPrintDirect = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000002")/*@res "直接打印"*/,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000003")/*@res "打印单据"*/, 0
			,"直接打印");

	public static final ButtonObject m_boPrintTemplet = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000004")/*@res "模板打印"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000003")/*@res "打印单据"*/, 0
			,"模板打印");

	public static final ButtonObject m_boPrint = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000007")/*@res "打印"*/,
			new ButtonObject[] { m_boPrintPreview, m_boPrintDirect,
				m_boPrintTemplet });
	static{
		m_boPrint.setCode("打印");
	}

	public static final ButtonObject m_boSort = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000005")/*@res "排序"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000006")/*@res "排序设置"*/,
			0,"排序");
	public static final ButtonObject m_boLevelSubTotal = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000007")/*@res "分级小计"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000007")/*@res "分级小计"*/, 0,"分级小计");	/*-=notranslate=-*/
	//public ButtonObject m_boTest = new ButtonObject("测试", "测试", 0);
	public static final ButtonObject m_boCodeLevelSubTotal = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000008")/*@res "编码级次小计"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000008")/*@res "编码级次小计"*/, 0,"编码级次小计");	/*-=notranslate=-*/

	public static final ButtonObject m_boSubTotal =
		new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000009")/*@res "小计合计"*/, new ButtonObject[] {m_boLevelSubTotal, m_boCodeLevelSubTotal});


	public static final ButtonObject m_boColumnFilter = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000010")/*@res "栏目设置"*/, 
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000010")/*@res "栏目设置"*/, 0
			,"栏目设置");

	public static final ButtonObject m_boCross = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000011")/*@res "交叉设置"*/,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000011")/*@res "交叉设置"*/, 0,"交叉设置");

	public static final ButtonObject m_boFilter = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000012")/*@res "过滤"*/,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000012")/*@res "过滤"*/, 0,"过滤");
	public static final ButtonObject refresh = new ButtonObject("刷新", "刷新数据",CaPuBtnConst.onboRefresh);
	
	
	public static final ButtonObject m_boRefresh = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000009")/*@res "刷新"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000009")/*@res "刷新"*/,
			0,"刷新");

	//public static final ButtonObject m_boGroup = new ButtonObject(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000013")/*@res "分组"*/, nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory_report","UPPuifactory_report-000014")/*@res "分组显示"*/,
	//		0,"分组显示");
	
	public static final ButtonObject m_boPenerate = new ButtonObject("穿透","双击业务行/列亦可穿透",0,"穿透");
	
	protected IButtonActionAndState a_filter = null;

	protected IButtonActionAndState a_cross = null;

	protected IButtonActionAndState a_columnFilter = null;

	protected IButtonActionAndState a_query = null;

	protected IButtonActionAndState a_printPreview = null;

	protected IButtonActionAndState a_printDirect = null;

	protected IButtonActionAndState a_printTemplet = null;

	//protected IButtonActionAndState a_totalQueryInfo = null;
	protected IButtonActionAndState a_sort = null;

	protected IButtonActionAndState a_levelSubTotal = null;

	protected IButtonActionAndState a_nextPage = null;

	protected IButtonActionAndState a_beforePage = null;

	protected IButtonActionAndState a_firstPage = null;

	protected IButtonActionAndState a_lastPage = null;
	protected IButtonActionAndState a_refresh = null;

	protected IButtonActionAndState a_codeLevelSubtotal = null;

	//protected IButtonActionAndState a_group = null;
	
	protected IButtonActionAndState a_penerate = null;
	
	private void initButtonActionMap() {
		put(m_boQuery, a_query);
		//put(m_boTotalQueryInfo, a_totalQueryInfo);
		//put(m_boRefresh, new EmptyAction());//刷新 有问题
		put(m_boCross, a_cross);//交叉处理
		put(m_boColumnFilter, a_columnFilter);//栏目设置
		put(m_boLevelSubTotal, a_levelSubTotal);
		put(m_boCodeLevelSubTotal, a_codeLevelSubtotal);
		put(m_boSubTotal, new EmptyAction());
		put(m_boFilter, a_filter);//条件过滤
		//put(m_boGroup, a_group);//分组
		put(m_boSort, a_sort);//排序
		put(m_boPrintPreview, a_printPreview);
		put(m_boPrintDirect, a_printDirect);
		put(m_boPrintTemplet, a_printTemplet);
		put(m_boPrint, new EmptyAction());
		put(m_boPenerate,a_penerate);
	}

	private void initAllActions() {
		a_query = new QueryAction(reportBaseUI);
		a_cross = new CrossAction(reportBaseUI);
		a_filter = new FilterAction(reportBaseUI);
		a_columnFilter = new ColumnFilterAction(reportBaseUI);
		a_levelSubTotal = new LevelSubTotalAction(reportBaseUI);
		a_codeLevelSubtotal = new CodeLevelSubtotalAction(reportBaseUI);
		//a_group = new GroupAction(reportBaseUI);
		a_sort = new SortAction(reportBaseUI);
		a_printPreview = new PrintPreviewAction(reportBaseUI);
		a_printDirect =  new PrintTempletAction(reportBaseUI);
		a_printTemplet = new PrintDirectAction(reportBaseUI);
		a_penerate = new PenerateAction(reportBaseUI);
	}

	public Object put(Object key, Object value)
	{
		return put(key, value, -1);
	}
	/**
	 * 在加入map之前，先用arraylist记录位置信息
	 *
	 * @param key
	 *            ButtonObject
	 * @param value
	 *            IButtonActionAndState
	 * @param pos
	 *            按钮位置
	 * @return
	 */
	public Object put(Object key, Object value, int pos)
	{
		if (key == null || value == null)
			return null;
		if (pos == -1 && !isChild((ButtonObject) key))
			buttonListByOrder.add(key);
		else if (!isChild((ButtonObject) key))
			buttonListByOrder.add(pos, key);
		return super.put(key, value);
	}
	private boolean isChild(ButtonObject o)
	{
		return o.getParent() != null;
	}
	public void clear()
	{
		buttonListByOrder.clear();
		super.clear();
	}
	public Object remove(Object key)
	{
		//移除父按钮对子按钮的引用
		if (isChild((ButtonObject) key))
		{
			((ButtonObject) key).getParent().removeChildButton(
					(ButtonObject) key);
		}
		else if (((ButtonObject) key).getChildCount() > 0)
		{
			buttonListByOrder.remove(key);
			ButtonObject[] btnObjs = ((ButtonObject) key).getChildButtonGroup();
			for (int i = 0; i < btnObjs.length; i++)
			{
				super.remove(btnObjs[i]);
			}
		}
		else
		{
			buttonListByOrder.remove(key);
		}
		return super.remove(key);

	}
	/**
	 * @return 返回子按钮除外的所有按钮 穿透按钮 需要根据reportBaseUI.getPene2NodeInfo() == null
	 * 判断
	 */
	public ArrayList getVisibleButtonsByOrder()
	{
		ArrayList<ButtonObject>al=new ArrayList<ButtonObject>();
		//saf/070822/
		if(reportBaseUI.getReportButtonAry()!=null&&reportBaseUI.getReportButtonAry().length>0){
			int[] buttonary=reportBaseUI.getReportButtonAry();
			for(int i=0;i<buttonary.length;i++){
				if(m_btnhs.containsKey(buttonary[i])){
					al.add(m_btnhs.get(buttonary[i]));
				}
			}
		}
		
		if(reportBaseUI.getPene2NodeInfo() != null){
			al.add(m_boPenerate);
		}
		
		return al;
		
		//guanyj begin
		//if(reportBaseUI.getPene2NodeInfo() == null){
		//	buttonListByOrder.remove(m_boPenerate);
		//}
		//return buttonListByOrder;
		//end
	}
	
	public HashMap getButtonMap(){
		return m_btnhs;
	}
	
}
