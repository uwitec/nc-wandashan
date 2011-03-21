package nc.ui.wds.w80020202;

import java.util.ArrayList;

import nc.ui.pub.print.IDataSource;
import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;

/**
 * 此处插入类型说明。 创建日期：(2003-6-3 11:28:26)
 * 
 * @author：赵晓锋
 */
public class MyListPanelPRTS implements nc.ui.pub.print.IDataSource {
	BillListPanel m_listpanel;
	String m_modulecode;

	/**
	 * MonnthApplyPRTS 构造子注解。
	 */
	public MyListPanelPRTS(String modulecode, BillListPanel bp) {
		super();
		m_listpanel = bp;
		m_modulecode = modulecode;
	}

	/**
	 * 
	 * 得到所有的数据项表达式数组 也就是返回所有定义的数据项的表达式
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
	 * 返回依赖项的名称数组，该数据项长度只能为 1 或者 2 返回 null : 没有依赖 长度 1 : 单项依赖 长度 2 : 双向依赖
	 * 
	 */
	public java.lang.String[] getDependentItemExpressByExpress(String itemName) {
		return null;
	}

	/*
	 * 返回所有的数据项对应的内容 参数： 数据项的名字 返回： 数据项对应的内容，只能为 String[]；
	 * 
	 */
	public java.lang.String[] getItemValuesByExpress(String itemExpress) {
		int row = m_listpanel.getHeadTable().getSelectedRow();
		if (itemExpress.startsWith("h_")) {
			BillItem item = m_listpanel.getHeadItem(itemExpress.substring(2));
			if(item==null) return null;
			
			
			Object obj = m_listpanel.getHeadBillModel().getValueAt(row,
					itemExpress.substring(2));
			if(item.getDataType()==4){
				if(item.getValue()==null){
					return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
				}else{
					if(item.getValue().equals("false")){
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "否"*/};
					}else{
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "是"*/};
					}
				}
			}
			
			if (obj != null)
				return new String[] { obj.toString() };
		} else if (itemExpress.startsWith("t_")) {
			Object obj = m_listpanel.getHeadBillModel().getValueAt(row,
					itemExpress.substring(2));
			if (obj != null)
				return new String[] { obj.toString() };
		} else {
			ArrayList retList = new ArrayList();
			if (itemExpress.startsWith("a_")) {
				int rowCount = m_listpanel.getBodyBillModel("tb_acquisition")
						.getRowCount();

				for (int i = 0; i < rowCount; i++) {
					Object obj = m_listpanel.getBodyBillModel("tb_acquisition")
							.getValueAt(i, itemExpress.substring(2));
					if (obj != null)
						retList.add(obj.toString());
					else
						retList.add("");
				}
			}
			if (itemExpress.startsWith("l_")) {
				int rowCount = m_listpanel.getBodyBillModel("tb_logo")
						.getRowCount();

				for (int i = 0; i < rowCount; i++) {
					Object obj = m_listpanel.getBodyBillModel("tb_logo")
							.getValueAt(i, itemExpress.substring(2));
					if (obj != null)
						retList.add(obj.toString());
					else
						retList.add("");
				}
			}
			if (itemExpress.startsWith("p_")) {
				int rowCount = m_listpanel.getBodyBillModel("tb_handlecosts_b")
						.getRowCount();

				for (int i = 0; i < rowCount; i++) {
					Object obj = m_listpanel.getBodyBillModel(
							"tb_handlecosts_b").getValueAt(i,
							itemExpress.substring(2));
					if (obj != null)
						retList.add(obj.toString());
					else
						retList.add("");
				}
			}
			if (itemExpress.startsWith("c_")) {
				int rowCount = m_listpanel.getBodyBillModel("tb_handlecosts_h")
						.getRowCount();

				for (int i = 0; i < rowCount; i++) {
					Object obj = m_listpanel.getBodyBillModel(
							"tb_handlecosts_h").getValueAt(i,
							itemExpress.substring(2));
					if (obj != null)
						retList.add(obj.toString());
					else
						retList.add("");
				}
			}
			String[] retStr = new String[retList.size()];
			for (int i = 0; i < retList.size(); i++) {
				retStr[i]=retList.get(i).toString();
			}
			return retStr;
		}
		return null;
	}

	/*
	 * 返回该数据源对应的节点编码
	 */
	public String getModuleName() {
		return m_modulecode;
	}

	/*
	 * 返回该数据项是否为数字项 数字项可参与运算；非数字项只作为字符串常量 如“数量”为数字项、“存货编码”为非数字项
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
}
