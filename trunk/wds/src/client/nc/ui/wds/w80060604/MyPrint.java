package nc.ui.wds.w80060604;

import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MyPrint extends ListPanelPRTS {

	public MyPrint(String modulecode, BillListPanel bp) {
		super(modulecode, bp);
		// TODO Auto-generated constructor stub
	}

	BillListPanel m_listpanel;
	String m_modulecode;
	private int rowno;

	public int getRowno() {
		return rowno;
	}

	public void setRowno(int rowno) {
		this.rowno = rowno;
	}
	
	private nc.ui.trade.buffer.BillUIBuffer buffdata;

	private MyClientUI myClientUI;
	
	public MyClientUI getMyClientUI() {
		return myClientUI;
	}

	public void setMyClientUI(MyClientUI myClientUI) {
		this.myClientUI = myClientUI;
	}

	public MyPrint(String modulecode, BillListPanel bp, int row,
			nc.ui.trade.buffer.BillUIBuffer buffdata,MyClientUI myClientUI) {
		super(modulecode, bp);
		m_listpanel = bp;
		m_modulecode = modulecode;
		this.setRowno(row);
		this.setBuffdata(buffdata); 
		this.setMyClientUI(myClientUI);
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * �õ����е���������ʽ���� Ҳ���Ƿ������ж����������ı��ʽ
	 * 
	 */
	public java.lang.String[] getAllDataItemExpress() {
		int headCount = 0;
		int bodyCount = 0;
		
		
		if (m_listpanel.getHeadBillModel().getBodyItems() != null) {
			headCount = m_listpanel.getHeadBillModel().getBodyItems().length;
		}
		if (m_listpanel.getBodyBillModel().getBodyItems() != null) {
			bodyCount = m_listpanel.getBodyBillModel().getBodyItems().length;
		}
		int count = headCount + bodyCount;
		String[] expfields = new String[count];
		try {
			for (int i = 0; i < headCount; i++) {
				expfields[i] = "h_"
						+ m_listpanel.getHeadBillModel().getBodyItems()[i]
								.getKey();
			}
			for (int j = 0; j < bodyCount; j++) {
				expfields[j + headCount] = m_listpanel.getBodyBillModel()
						.getBodyItems()[j].getKey();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemExpress()");
		}
		return expfields;
	}

	public java.lang.String[] getAllDataItemNames() {
		int headCount = 0;
		int bodyCount = 0;

		if (m_listpanel.getHeadBillModel().getBodyItems() != null) {
			headCount = m_listpanel.getHeadBillModel().getBodyItems().length;
		}
		if (m_listpanel.getBodyBillModel().getBodyItems() != null) {
			bodyCount = m_listpanel.getBodyBillModel().getBodyItems().length;
		}

		int count = headCount + bodyCount;
		String[] namefields = new String[count];
		try {
			for (int i = 0; i < headCount; i++) {
				namefields[i] = m_listpanel.getHeadBillModel().getBodyItems()[i]
						.getName();
			}
			for (int j = 0; j < bodyCount; j++) {
				namefields[j + headCount] = m_listpanel.getBodyBillModel()
						.getBodyItems()[j].getName();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemNames()");
		}
		return namefields;
	}

	/**
	 * 
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2 ���� null : û������ ���� 1 : �������� ���� 2 : ˫������
	 * 
	 */
	public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
		return null;
	}
	
	private int temprow = -1;

	/*
	 * �������е��������Ӧ������ ������ ����������� ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��
	 * 
	 */
	public java.lang.String[] getItemValuesByExpress(String itemExpress) {
		int row = -1;
		
//		AggregatedValueObject item = this.getBuffdata().getVOByRowNo(this.getRowno());
		//myClientUI.getBillListPanel().getBillListData().setBodyValueVO(item.getChildrenVO());
//		this.getBuffdata().addVOToBuffer(item);
		
//		m_listpanel.setBodyValueVO(item.getChildrenVO());
//		myClientUI.getBillCardPanel().ex
		if(temprow != this.getRowno()){
			temprow=this.getRowno();
			this.getBuffdata().setCurrentRow(temprow);
			
		}
		
		if (itemExpress.startsWith("h_")) {
			Object obj = m_listpanel.getHeadBillModel().getValueAt(this.getRowno(),
					itemExpress.substring(2));
			if (obj != null)
				return new String[] { obj.toString() };
		} else if (itemExpress.startsWith("t_")) {
			Object obj = m_listpanel.getHeadBillModel().getValueAt(this.getRowno(),
					itemExpress.substring(2));
			if (obj != null)
				return new String[] { obj.toString() };
		} else {
			int rowCount = m_listpanel.getBodyBillModel().getRowCount();
			String[] retStr = new String[rowCount];
			for (int i = 0; i < rowCount; i++) {
				Object obj = m_listpanel.getBodyBillModel().getValueAt(i,
						itemExpress);
				if (obj != null)
					retStr[i] = obj.toString();
				else
					retStr[i] = "";
			}
			return retStr;
		}
		return null;
	}

	/*
	 * ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public String getModuleName() {
		return m_modulecode;
	}

	/*
	 * ���ظ��������Ƿ�Ϊ������ ������ɲ������㣻��������ֻ��Ϊ�ַ������� �硰������Ϊ�������������롱Ϊ��������
	 */
	public boolean isNumber(String itemExpress) {
		try {
			if (itemExpress.startsWith("h_")) {
				BillItem item = m_listpanel.getHeadItem(itemExpress
						.substring(2));
				if (item == null)
					return false;
				if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			} else if (itemExpress.startsWith("t_")) {
				BillItem item = m_listpanel.getBodyItem(itemExpress
						.substring(2));
				if (item == null)
					return false;
				if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			} else {

				BillItem item = m_listpanel.getBodyItem(itemExpress);
				if (item == null) {
					return false;
				} else if (item.getDataType() == 1 || item.getDataType() == 2) {
					return true;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  isNumber()");
			return false;
		}
		return false;
	}

	public nc.ui.trade.buffer.BillUIBuffer getBuffdata() {
		return buffdata;
	}

	public void setBuffdata(nc.ui.trade.buffer.BillUIBuffer buffdata) {
		this.buffdata = buffdata;
	}

}
