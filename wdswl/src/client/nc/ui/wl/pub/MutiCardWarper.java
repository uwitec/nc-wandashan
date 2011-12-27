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
	 * ���ĵ��ݿ�Ƭģ����ֵ��ʾλ�������ݲ�Ʒ�ķ��أ�
	 * override
	 * �������ڣ�(2001-12-17 14:40:29)
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
			throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000059")/*@res "��ʾλ�����һ�����в�ƥ��"*/);
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
					throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000119")/*@res "MultiChildBillCardPanelWrapper��ʼ������VO�е��ֱ������CTL�����ƶ����ӱ�VO Class������ͬ��"*/);
			}
			catch (ClassCastException e)
			{
				throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000120")/*@res "MultiChildBillCardPanelWrapper��ʼ�����󡣶��ӱ�UI��VO����ʵ��IExAggVO�ӿڡ�"*/);
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
				//���Ӽ���
				getBillCardPanel().addBodyMenuListener(getTableCodes()[i], this);

			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println(
				"���ص��ݿ�Ƭģ�����::MultiChildBillCardPanelWrapper(initCardPanel)!!");
		}
	}

	private HashMap m_BodyVOClassMap;
		private String[] m_TableCodes;





	/**
	* ����
	*/
	public void addLine() throws Exception {

		getBillCardPanel().addLine(getCurrentBodyTableCode());

	}

	/**
	* ���Ƶ�ǰ��ѡ�����
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
	* ɾ����ǰ��ѡ�����
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
	 * ִ����ʾ��ʽ
	 * �������ڣ�(2003-9-12 16:10:25)
	 */
	protected void execCurrentLoadFormula() {
		execHeadLoadFormula();
		getBillCardPanel().getBillModel(getCurrentBodyTableCode()).execLoadFormula();



	}

	/**
	 * ִ�б�ͷ�ļ��ع�ʽ��
	 * �������ڣ�(2004-2-2 19:43:47)
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
	 * ִ����ʾ��ʽ
	 * �������ڣ�(2003-9-12 16:10:25)
	 */
	protected void execLoadFormula() {
		execHeadLoadFormula();
		for (int i = 0; i < m_TableCodes.length; i++){
			getBillCardPanel().getBillModel(m_TableCodes[i]).execLoadFormula();
		}


	}

	/**
	 * �ӽ����ϵõ�VO��
	 * �������ڣ�(2004-1-7 10:10:26)
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
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-2-2 18:52:13)
	 * @return java.lang.String
	 */
	private String getCurrentBodyTableCode() {
		return getBillCardPanel().getCurrentBodyTableCode();
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-2-2 19:19:10)
	 * @return java.lang.Class
	 */
	private Class getCurrentBodyVOClass() {
		return (Class)m_BodyVOClassMap.get(getCurrentBodyTableCode());
	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2004-2-2 19:50:34)
	 * @return java.lang.String[]
	 */
	private String[] getTableCodes() {
		return m_TableCodes;
	}

	/**
	* ��ʼ�����ܺϼ���,�������ظ��෽����
	* �������ڣ�(2004-2-3 15:13:07)
	*/
	protected void initTotalSumRow()
	{
		//�����Ƿ���ʾ�ϼ���
		//�����Ƿ���ʾ�к�
		for (int i = 0; i < getTableCodes().length; i++)
		{
			getBillCardPanel().setTatolRowShow(getTableCodes()[i], m_ctl.isShowCardTotal());
			getBillCardPanel().setRowNOShow(getTableCodes()[i], m_ctl.isShowCardRowNo());
		}
	}

	/**
	* ����
	*/
	public void insertLine() throws Exception {

		getBillCardPanel().stopEditing();
		getBillCardPanel().insertLine(getCurrentBodyTableCode());

	}

	/**
	 * �˴����뷽��˵����
	 * �������ڣ�(2001-3-27 11:09:34)
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
	* ճ����ǰ��ѡ�����
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
	 * ���õ��ݿ�Ƭ���������override
	 * �������ڣ�(2003-9-12 15:50:15)
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
	* override ������״̬ΪNormal��
	* �������ڣ�(2003-9-19 10:21:43)
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
	 * ��ӱ��帽�ӱ༭������
	 * �������ڣ�(2001-3-23 2:20:34)
	 * @param ml BillEditListener2
	 */
	public void addBodyEditListener2(BillEditListener2 el) {
		for (int i = 0; i < getTableCodes().length; i++){
			getBillCardPanel().addBodyEditListener2(getTableCodes()[i],el);
		}
	}

	/**
	 * ��ӱ༭������
	 * �������ڣ�(2001-3-23 2:20:34)
	 * @param ml BillEditListener
	 */
	public void addEditListener(BillEditListener el) {
		getBillCardPanel().addBillEditListenerHeadTail(el);
		for (int i = 0; i < getTableCodes().length; i++){
			getBillCardPanel().addEditListener(getTableCodes()[i],el);
		}
	}

	

	
	
}
