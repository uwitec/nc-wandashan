package nc.ui.wl.pub;
import java.util.ArrayList;
import java.util.HashMap;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.multichild.MultiChildBillCardPanelWrapper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IExAggVO;
/**
 * @author mlr
 */
public class MutiCardWarper extends MultiChildBillCardPanelWrapper{

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey);
	}
    private String[] tablecodes=null;
	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, ArrayList defAry)
			throws Exception {
		
		super(ce, ctl, pk_busiType, nodeKey, defAry);
		String cls=ctl.getBillVoName()[0];
		IExAggVO ie= (IExAggVO) Class.forName(cls).newInstance();
		this.tablecodes=ie.getTableCodes();
	}

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, BillData billData,
			ArrayList defAry) throws Exception {
		super(ce, ctl, pk_busiType, nodeKey, billData, defAry);
	}

	public MutiCardWarper(ClientEnvironment ce, ICardController ctl,
			String pk_busiType, String nodeKey, BillData billData)
			throws Exception {
		super(ce, ctl, pk_busiType, nodeKey, billData);
	  
	}









	/**
	 * 更改单据卡片模板数值显示位数（根据产品的返回）
	 * override
	 * 创建日期：(2001-12-17 14:40:29)
	 */
	protected void setCardDecimalDigits(
		int intHeadOrItem,
		BillData billDataVo,
		String[][] strShow)
		throws Exception {
		if (strShow.length < 2)
		{
			return;
		}
		if (strShow[0].length != strShow[1].length)
		{
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000059")/*@res "显示位数组第一、二行不匹配"*/);
		}
		for (int j = 0; j < getTableCodes().length; j++)
		{
			if (strShow.length / 2 <= j)
				return;

			for (int i = 0; i < strShow[2 * j].length; i++)
			{
				String attrName = strShow[2 * j][i];
				Integer attrDigit = new Integer(strShow[2 * j + 1][i]);
				BillItem tmpItem = null;
				switch (intHeadOrItem)
				{
					case HEAD :
						{
							tmpItem = billDataVo.getHeadItem(attrName);
							break;
						}
					case BODY :
						{
							tmpItem = billDataVo.getBodyItem(getTableCodes()[j], attrName);
							break;
						}
				}

				if (tmpItem != null)
				{
					tmpItem.setDecimalDigits(attrDigit.intValue());
				}
			}
		}
	}

	protected void initCardPanel()
	{
		try
		{
			Class BillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			IExAggVO iExAggVO = null;
			try
			{
				iExAggVO = (IExAggVO) BillVOClass.newInstance();
				if (iExAggVO.getTableCodes().length
					!= getUIControl().getBillVoName().length - 2)
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000119")/*@res "MultiChildBillCardPanelWrapper初始化错误。VO中的字表个数与CTL类中制定的子表VO Class个数不同。"*/);
			}
			catch (ClassCastException e)
			{
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000120")/*@res "MultiChildBillCardPanelWrapper初始化错误。多子表UI的VO必须实现IExAggVO接口。"*/);
			}
			m_BodyVOClassMap = new HashMap();
			for (int i = 0; i < iExAggVO.getTableCodes().length; i++)
			{
				m_BodyVOClassMap.put(
					iExAggVO.getTableCodes()[i],
					Class.forName(getUIControl().getBillVoName()[2 + i]));
			}

			m_TableCodes = iExAggVO.getTableCodes();

			super.initCardPanel();

			for (int i = 0; i < getTableCodes().length; i++)
			{
				//增加监听
				getBillCardPanel().addBodyMenuListener(getTableCodes()[i], this);

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(
				"加载单据卡片模板错误::MultiChildBillCardPanelWrapper(initCardPanel)!!");
		}
	}

	private HashMap m_BodyVOClassMap;
		private String[] m_TableCodes;





	/**
	* 增行
	*/
	public void addLine() throws Exception {

		getBillCardPanel().addLine(getCurrentBodyTableCode());

	}

	/**
	* 复制当前所选择的行
	*/
	public void copySelectedLines()
	{
		int selectedRow = getBillCardPanel().getBillTable(getCurrentBodyTableCode()).getSelectedRow();
		if (selectedRow != -1)
		{
			int[] rows = getBillCardPanel().getBillTable(getCurrentBodyTableCode()).getSelectedRows();

			CircularlyAccessibleValueObject[] vos =
				(CircularlyAccessibleValueObject[]) java.lang.reflect.Array.newInstance(
					getCurrentBodyVOClass(),
					rows.length);
			for (int i = 0; i < vos.length; i++)
			{
				vos[i] =
					getBillCardPanel().getBillModel(getCurrentBodyTableCode()).getBodyValueRowVO(
						rows[i],
						getCurrentBodyVOClass().getName());
			}
			getBillCardPanel().getBillData().getBillModel(getCurrentBodyTableCode()).getBodySelectedVOs(
				getCurrentBodyVOClass().getName());
			setCopyedBodyVOs(vos);

		}
	}

	/**
	* 删除当前所选择的行
	*/
	public void deleteSelectedLines()
	{
		getBillCardPanel().stopEditing();
		if (getBillCardPanel().getBillTable().getSelectedRow() > -1) {
			int[] aryRows = getBillCardPanel().getBillTable(getCurrentBodyTableCode()).getSelectedRows();
			getBillCardPanel().delLine(getCurrentBodyTableCode());
		}
	}

	/**
	 * 执行显示公式
	 * 创建日期：(2003-9-12 16:10:25)
	 */
	protected void execCurrentLoadFormula() {
		execHeadLoadFormula();
		getBillCardPanel().getBillModel(getCurrentBodyTableCode()).execLoadFormula();



	}

	/**
	 * 执行表头的加载公式。
	 * 创建日期：(2004-2-2 19:43:47)
	 */
	private void execHeadLoadFormula()
	{
		if (getUIControl().isLoadCardFormula())
		{
			BillItem[] billItems = getBillCardPanel().getHeadItems();
			for (int i = 0; i < billItems.length; i++)
			{
				BillItem tmpItem = billItems[i];
				String[] strLoadFormula = tmpItem.getLoadFormula();
				getBillCardPanel().execHeadFormulas(strLoadFormula);
			}
		}

	}

	/**
	 * 执行显示公式
	 * 创建日期：(2003-9-12 16:10:25)
	 */
	protected void execLoadFormula() {
		execHeadLoadFormula();
		for (int i = 0; i < m_TableCodes.length; i++){
			getBillCardPanel().getBillModel(m_TableCodes[i]).execLoadFormula();
		}


	}

	/**
	 * 从界面上得到VO。
	 * 创建日期：(2004-1-7 10:10:26)
	 * @return nc.vo.pub.AggregatedValueObject
	 */
	public nc.vo.pub.AggregatedValueObject getBillVOFromUI() throws Exception {
		AggregatedValueObject billVO = super.getBillVOFromUI();

		IExAggVO exBillVO = (IExAggVO) billVO;
		for (int i = 0; i < m_TableCodes.length; i++)
		{
			CircularlyAccessibleValueObject[] vos =
				getBillCardPanel().getBillData().getBodyValueVOs(
					m_TableCodes[i],
					getUIControl().getBillVoName()[i + 2]);
			if (vos != null && vos.length != 0)
				exBillVO.setTableVO(m_TableCodes[i], vos);
		}
		return billVO;

	}
	 /**
	  * mlr
	  */
	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		AggregatedValueObject billVO = super.getChangedVOFromUI();
		IExAggVO exBillVO = (IExAggVO) billVO;
		billVO.setChildrenVO(null);
		for (int i = 0; i < tablecodes.length; i++)
		{
			CircularlyAccessibleValueObject[] vos =
				getBillCardPanel().getBillData().getBodyValueChangeVOs(
						tablecodes[i],
					getUIControl().getBillVoName()[i + 2]);
			if (vos != null && vos.length != 0)
				exBillVO.setTableVO(tablecodes[i], vos);
		}
		billVO.setChildrenVO(
				getBillCardPanel().getBillData().getBodyValueChangeVOs(
				tablecodes[0],
				getUIControl().getBillVoName()[2]));
		return billVO;
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-2-2 18:52:13)
	 * @return java.lang.String
	 */
	private String getCurrentBodyTableCode() {
		return getBillCardPanel().getCurrentBodyTableCode();
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-2-2 19:19:10)
	 * @return java.lang.Class
	 */
	private Class getCurrentBodyVOClass() {
		return (Class)m_BodyVOClassMap.get(getCurrentBodyTableCode());
	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2004-2-2 19:50:34)
	 * @return java.lang.String[]
	 */
	private String[] getTableCodes() {
		return m_TableCodes;
	}

	/**
	* 初始化汇总合计行,子类重载父类方法。
	* 创建日期：(2004-2-3 15:13:07)
	*/
	protected void initTotalSumRow()
	{
		//设置是否显示合计行
		//设置是否显示行号
		for (int i = 0; i < getTableCodes().length; i++)
		{
			getBillCardPanel().setTatolRowShow(getTableCodes()[i], m_ctl.isShowCardTotal());
			getBillCardPanel().setRowNOShow(getTableCodes()[i], m_ctl.isShowCardRowNo());
		}
	}

	/**
	* 增行
	*/
	public void insertLine() throws Exception {

		getBillCardPanel().stopEditing();
		getBillCardPanel().insertLine(getCurrentBodyTableCode());

	}

	/**
	 * 此处插入方法说明。
	 * 创建日期：(2001-3-27 11:09:34)
	 * @param e java.awt.event.ActionEvent
	 */
	public void onMenuItemClick(java.awt.event.ActionEvent e)
	{
		try
		{
			BillScrollPane bsp = getBillCardPanel().getBodyPanel();

			UIMenuItem item = (UIMenuItem) e.getSource();

			if (item == bsp.getMiInsertLine())
			{
				insertLine();
			}
			else if (item == bsp.getMiAddLine())
			{
				addLine();
			}
			else if (item == bsp.getMiDelLine())
			{
				deleteSelectedLines();
			}
			else if (item == bsp.getMiCopyLine())
			{
				copySelectedLines();
			}
			else if (item == bsp.getMiPasteLine())
			{
				pasteLines();
			}
			else if (item == bsp.getMiPasteLineToTail())
			{
				pasteLines();
			}
		}
		catch (Exception ex)
		{
			System.out.println("line error!!!!");
		}
	}

	/**
	* 粘贴当前所选择的行
	*/
	public void pasteLines() {
			if(getCopyedBodyVOs()==null||getCopyedBodyVOs().length==0)
				return;
			if(getCopyedBodyVOs()[0].getClass() != getCurrentBodyVOClass())
				return;
			for (int i = 0; i < getCopyedBodyVOs().length; i++){
				getBillCardPanel().stopEditing();
				getBillCardPanel().insertLine();
				int selectedRow = getBillCardPanel().getBillTable().getSelectedRow();
				getBillCardPanel().getBillModel().setBodyRowVO(getCopyedBodyVOs()[i],selectedRow);
			}
			execCurrentLoadFormula();

	}

	/**
	 * 设置单据卡片界面的数据override
	 * 创建日期：(2003-9-12 15:50:15)
	 * @param billVO nc.vo.pub.AggregatedValueObject
	 */
	public void setCardData(AggregatedValueObject billVO)
	{
		if (billVO == null)
			getBillCardPanel().getBillData().clearViewData();
		else
		{
			getBillCardPanel().getBillData().setHeaderValueVO(billVO.getParentVO());


			 for (int i = 0; i < getTableCodes().length; i++)
					getBillCardPanel().getBillModel(getTableCodes()[i]).setBodyDataVO(
						((IExAggVO) billVO).getTableVO(getTableCodes()[i]));


			execLoadFormula();
		}


	}

	/**
	* override 设置行状态为Normal。
	* 创建日期：(2003-9-19 10:21:43)
	*/
	public void setRowStateToNormal()
	{
		for (int j = 0; j < getTableCodes().length; j++)
		{
			for (int i = 0;
				i < getBillCardPanel().getBillModel(getTableCodes()[j]).getRowCount();
				i++)
			{
				getBillCardPanel().getBillModel(getTableCodes()[j]).setRowState(
					i,
					BillModel.NORMAL);
			}

		}
	}



	/**
	 * 添加表体附加编辑监听。
	 * 创建日期：(2001-3-23 2:20:34)
	 * @param ml BillEditListener2
	 */
	public void addBodyEditListener2(BillEditListener2 el) {
		for (int i = 0; i < getTableCodes().length; i++){
			getBillCardPanel().addBodyEditListener2(getTableCodes()[i],el);
		}
	}

	/**
	 * 添加编辑监听。
	 * 创建日期：(2001-3-23 2:20:34)
	 * @param ml BillEditListener
	 */
	public void addEditListener(BillEditListener el) {
		getBillCardPanel().addBillEditListenerHeadTail(el);
		for (int i = 0; i < getTableCodes().length; i++){
			getBillCardPanel().addEditListener(getTableCodes()[i],el);
		}
	}

	

	
	
}
