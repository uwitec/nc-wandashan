package nc.ui.wds.w80020202;

import java.util.ArrayList;

import nc.ui.pub.bill.*;
import nc.ui.pub.beans.*;

/**
 * �˴���������˵����
 * �������ڣ�(2001-9-10 19:11:01)
 * @author���￭��
 */
public class MyCardPanelPRTS implements nc.ui.pub.print.IDataSource {

	private String m_sModuleName= "";
/**
 * �˴����뷽��˵����
 * �������ڣ�(2001-9-10 19:39:41)
 * @param m_sModuleName java.lang.String
 * @param billcardpanel nc.ui.pub.bill.BillCardPanel
 */
public MyCardPanelPRTS(String m_sModuleName, BillCardPanel billcardpanel) {
	super();
	this.m_billcardpanel = billcardpanel;
	this.m_sModuleName = m_sModuleName;
}
/**
 *
 * �õ����е���������ʽ����
 * Ҳ���Ƿ������ж����������ı��ʽ
 *
 */
public String[] getAllDataItemExpress() {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null && m_billcardpanel.getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	int count= headCount + bodyCount + tailCount ;
	String[] expfields = new String[count];
	try{
		for (int i = 0; i < headCount; i++){
			expfields[i] = "h_"+m_billcardpanel.getHeadItems()[i].getKey();
		}
		for (int j = 0; j < bodyCount ; j++){
			expfields[j+headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getKey();
		}
		for (int k = 0; k < tailCount ; k++){
			expfields[k+headCount+bodyCount] = "t_" + m_billcardpanel.getTailItems()[k].getKey();
		}
	}catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemExpress()");
	}
	return expfields;
}
/**
 *
 * �õ����е���������ʽ����
 * Ҳ���Ƿ������ж����������Ĵ�ӡ�ֶ���
 *
 */
public String[] getAllDataItemNames() {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	int count = headCount + bodyCount + tailCount;
	String[] namefields = new String[count];
	try{
		for (int i = 0; i < headCount; i++){
			namefields[i] = m_billcardpanel.getHeadItems()[i].getName();
		}
		for (int j = 0; j < bodyCount ; j++){
			namefields[j + headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getName();
		}
		for (int k = 0 ; k < tailCount ; k++){
			namefields[k + headCount+bodyCount] = m_billcardpanel.getTailItems()[k].getName();
		}
	}catch (Throwable e) {
		e.printStackTrace();
		System.out.print("error at  getAllDataItemNames()");
	}
	return namefields;
}
/**
 *
 * ������������������飬���������ֻ��Ϊ 1 ���� 2
 * ���� null : 		û������
 * ���� 1 :			��������
 * ���� 2 :			˫������
 *
 */
public String[] getDependentItemExpressByExpress(String itemName) {
	return null;
}
/*
 * �������е��������Ӧ������
 * ������ �����������
 * ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��

 */
public String[] getItemValuesByExpress(String itemExpress) {
	int headCount = 0;
	int bodyCount = 0;
	int tailCount = 0;
	if (m_billcardpanel.getHeadItems() != null){
		headCount = m_billcardpanel.getHeadItems().length;
	}
	if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
		bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
	}
	if (m_billcardpanel.getTailItems() != null){
		tailCount = m_billcardpanel.getTailItems().length;
	}
	
	try {
		//��ͷ
		if(itemExpress.startsWith("h_")){
			BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
			if(item==null) return null;
			if (item.getKey().equals(itemExpress.substring(2))) {
				//UICheckbox
				if(item.getDataType()==4){
					if(item.getValue()==null){
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
					}else{
						if(item.getValue().equals("false")){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
						}else{
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/};
						}
					}
				}
				//UICombox
				else if(item.getDataType()==6){
					String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
					return new String[] { sc };
				}
				//UIRefPane
				else if (item.getDataType() == 5){
  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
	  				return new String[] { sr };
				}
               //	���ı�
				else if (item.getDataType() == 9) {
				String wb = ((UITextAreaScrollPane)item.getComponent()).getText();
				return new String[] { wb };
				}

				//�����ı�
				else{
					String wb = ((UIRefPane)item.getComponent()).getText();
				  	//cf add �������ֲ�������С��λ��format
				   	try{
					   	if(item.getDataType() == 2){
							UIRefPane item_h=(UIRefPane)item.getComponent();
							nc.vo.pub.lang.UFDouble value=new nc.vo.pub.lang.UFDouble(wb);
							value=value.setScale(item_h.getNumPoint(),4);
							wb=value.toString();
						}
					}catch(Exception e){
						System.out.println("�������ֲ�������С��λ��format����:"+e);
				  	}
				   	//cf add
				 	return new String[] { wb };
				}
			}

		}
		//��β
		else if(itemExpress.startsWith("t_")){
			BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
			if(item==null) return null;
			if (item.getKey().equals(itemExpress.substring(2))) {
				//UICheckbox
				if(item.getDataType()==4){
					if(item.getValue()==null){
						return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
					}else{
						if(item.getValue().equals("false")){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
						}else{
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/};
						}
					}
				}
				//UICombox
				else if(item.getDataType()==6){
					String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
					return new String[] { sc };
				}
				//UIRefPane
				else if (item.getDataType() == 5){
  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
	  				return new String[] { sr };
				}
				//�����ı�
				else{
					String wb = ((UIRefPane)item.getComponent()).getText();
				    return new String[] { wb };
				}
			}
		}
		//����
		else{
			ArrayList retList = new ArrayList();
			if (itemExpress.startsWith("a_")) {
				if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
					bodyCount = m_billcardpanel.getBillModel("tb_acquisition").getBodyItems().length;
				}
				for (int i = 0; i < bodyCount; i++){
					BillItem item = m_billcardpanel.getBillModel("tb_acquisition").getBodyItems()[i];
					if(item==null) return null;
					
						int rowCount  = m_billcardpanel.getRowCount("tb_acquisition");
						if (item.getKey().equals(itemExpress.substring(2))) {
							//UICheckbox
							if(item.getDataType()==4){
								for (int j = 0; j < rowCount; j++) {
									if(m_billcardpanel.getBillModel("tb_acquisition").getValueAt(j, item.getKey())==null){
										retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/);
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
									}else{
										if(m_billcardpanel.getBillModel("tb_acquisition").getValueAt(j, item.getKey()).toString().equals("false")){
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
										}else{
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
										}
									}
								}
							}
							//UIRefPane or UICombox
							else{
								for (int j = 0; j < rowCount; j++) {
									retList.add(m_billcardpanel.getBillModel("tb_acquisition").getValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBillModel("tb_acquisition").getValueAt(j, item.getKey()).toString());
//									rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
								}
							}
//							return rslt;
						}	
				}
			}
			if (itemExpress.startsWith("l_")) {
				if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
					bodyCount = m_billcardpanel.getBillModel("tb_logo").getBodyItems().length;
				}
				for (int i = 0; i < bodyCount; i++){
					BillItem item = m_billcardpanel.getBillModel("tb_logo").getBodyItems()[i];
					if(item==null) return null;
					
						int rowCount  = m_billcardpanel.getRowCount("tb_logo");
						if (item.getKey().equals(itemExpress.substring(2))) {
							//UICheckbox
							if(item.getDataType()==4){
								for (int j = 0; j < rowCount; j++) {
									if(m_billcardpanel.getBillModel("tb_logo").getValueAt(j, item.getKey())==null){
										retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/);
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
									}else{
										if(m_billcardpanel.getBillModel("tb_logo").getValueAt(j, item.getKey()).toString().equals("false")){
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
										}else{
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
										}
									}
								}
							}
							//UIRefPane or UICombox
							else{
								for (int j = 0; j < rowCount; j++) {
									retList.add(m_billcardpanel.getBillModel("tb_logo").getValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBillModel("tb_logo").getValueAt(j, item.getKey()).toString());
//									rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
								}
							}
//							return rslt;
						}
				}
			}
			if (itemExpress.startsWith("p_")) {
				if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
					bodyCount = m_billcardpanel.getBillModel("tb_handlecosts_b").getBodyItems().length;
				}
				for (int i = 0; i < bodyCount; i++){
					BillItem item = m_billcardpanel.getBillModel("tb_handlecosts_b").getBodyItems()[i];
					if(item==null) return null;
					
						int rowCount  = m_billcardpanel.getRowCount("tb_handlecosts_b");
						if (item.getKey().equals(itemExpress.substring(2))) {
							//UICheckbox
							if(item.getDataType()==4){
								for (int j = 0; j < rowCount; j++) {
									if(m_billcardpanel.getBillModel("tb_handlecosts_b").getValueAt(j, item.getKey())==null){
										retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/);
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
									}else{
										if(m_billcardpanel.getBillModel("tb_handlecosts_b").getValueAt(j, item.getKey()).toString().equals("false")){
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
										}else{
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
										}
									}
								}
							}
							//UIRefPane or UICombox
							else{
								for (int j = 0; j < rowCount; j++) {
									retList.add(m_billcardpanel.getBillModel("tb_handlecosts_b").getValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBillModel("tb_handlecosts_b").getValueAt(j, item.getKey()).toString());
//									rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
								}
							}
//							return rslt;
						}
				}
			}
			if (itemExpress.startsWith("c_")) {
				if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
					bodyCount = m_billcardpanel.getBillModel("tb_handlecosts_h").getBodyItems().length;
				}
				for (int i = 0; i < bodyCount; i++){
					BillItem item = m_billcardpanel.getBillModel("tb_handlecosts_h").getBodyItems()[i];
					if(item==null) return null;
					
						int rowCount  = m_billcardpanel.getRowCount("tb_handlecosts_h");
						if (item.getKey().equals(itemExpress.substring(2))) {
							//UICheckbox
							if(item.getDataType()==4){
								for (int j = 0; j < rowCount; j++) {
									if(m_billcardpanel.getBillModel("tb_handlecosts_h").getValueAt(j, item.getKey())==null){
										retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/);
//										rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
									}else{
										if(m_billcardpanel.getBillModel("tb_handlecosts_h").getValueAt(j, item.getKey()).toString().equals("false")){
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
										}else{
											retList.add(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164"));
//											rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
										}
									}
								}
							}
							//UIRefPane or UICombox
							else{
								for (int j = 0; j < rowCount; j++) {
									retList.add(m_billcardpanel.getBillModel("tb_handlecosts_h").getValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBillModel("tb_handlecosts_h").getValueAt(j, item.getKey()).toString());
//									rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
								}
							}
//							return rslt;
						}
				}
			}
			String[] rslt = new String[retList.size()];
			retList.toArray(rslt);
			return rslt;
		}

	} catch (Throwable e) {
		e.printStackTrace();
	    System.out.print("error at getItemValueByExpress()");
	    return null;
	}
	return null;
}

/*
 *  ���ظ�����Դ��Ӧ�Ľڵ����
 */
public String getModuleName() {
	return m_sModuleName;
}
/*
 * ���ظ��������Ƿ�Ϊ������
 * ������ɲ������㣻��������ֻ��Ϊ�ַ�������
 * �硰������Ϊ�������������롱Ϊ��������
 */
public boolean isNumber(String itemExpress) {
	/**�������� */
	//int headCount = 0;
	//int bodyCount = 0;
	//int tailCount = 0;
	//if (m_billcardpanel.getHeadItems() != null){
		//headCount = m_billcardpanel.getHeadItems().length;
	//}
	//if (m_billcardpanel.getBodyItems() != null){
		//bodyCount =m_billcardpanel.getBillModel().getBodyItems().length;
	//}
	//if (m_billcardpanel.getTailItems() != null){
		//tailCount = m_billcardpanel.getTailItems().length;
	//}

	try {
		if (itemExpress.startsWith("h_")){
			BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
			if (item==null) return false;
			if (item.getDataType() == 1 || item.getDataType() == 2){
				return true;
			}
		}
		else if (itemExpress.startsWith("t_")){
			BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
			if (item==null) return false;
			if (item.getDataType() == 1 || item.getDataType() == 2){
				return true;
			}
		}
		else {
			//��Ҫ��֤billModel��Ϊ��
			if(m_billcardpanel.getBillModel()==null)
				return false;
			BillItem[] items=m_billcardpanel.getBillModel().getBodyItems();
			BillItem item=null;
			for(int i=0;i<items.length;i++){
				if (items[i].getKey().equals(itemExpress)){
					item=items[i];
					break;
				}
			}
			if (item==null) return false;
			if (item ==null){
				return false;
			}
			else if (item.getDataType() == 1 || item.getDataType() == 2){
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


	private BillCardPanel m_billcardpanel = null;
}
