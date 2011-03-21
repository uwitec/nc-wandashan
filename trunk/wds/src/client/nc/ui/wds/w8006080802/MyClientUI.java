package nc.ui.wds.w8006080802;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI implements
		ListSelectionListener {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	protected void initEventListener() {
		// TODO Auto-generated method stub
		super.initEventListener();

		// 卡片下表体监听
		getBillCardPanel().getBillTable().getSelectionModel()
				.addListSelectionListener(this);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		/*
		 * if (e.getKey().equals("cdhz")) { UIRefPane uipane = (UIRefPane)
		 * getBillCardPanel().getBodyItem( "cdhz").getComponent();
		 * 
		 * String ss = uipane.getRefModel().getPkValue();
		 * 
		 * getBillCardPanel().setBodyValueAt(ss,
		 * getBillCardPanel().getBillTable().getSelectedRow(), "pk_wlxx");
		 * 
		 * 
		 * if(e.getKey().equals("pk_wlxx")){
		 * getBillCardPanel().execHeadEditFormulas(); } }
		 */
		
		try {
			if (e.getKey().equals("area")) {
				
				/*Object oo = getBillCardPanel().getBodyValueAt(
						getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_wlxx");*/
				
				//卡片下获得表体的总行数
				ArrayList arr = new ArrayList();
				int rowNum = getBillCardPanel().getBillTable().getRowCount();
				
				
				for(int i=0;i<rowNum;i++){
					String pk_wlxx = (String)getBillCardPanel().getBillModel()
					.getValueAt(i, "pk_wlxx");
					if (!arr.contains(pk_wlxx)) {
						
						arr.add(pk_wlxx);
					} else {
						this.showErrorMessage("此到货站已存在");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "area");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "cdhz");
					}
				/*if (oo != null) {
					
					IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance()
							.lookup(IUAPQueryBS.class.getName());

					List list = (List) query
							.executeQuery(
									"select pk_wlxx from tb_freightstandrad_b_t where dr =0",
									new ArrayListProcessor());
					Iterator it = list.iterator();
					while (it.hasNext()) {
						Object[] ob = (Object[]) it.next();
						String ss = ob[0].toString();
						if(oo.equals(ss)){
							b=true;
						}
					}*/
				
					/*if(b){
						this.showErrorMessage("此到货站已存在");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "qy");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "cdhz");
					}*/
				}
				
			}
			getBillCardPanel().execHeadEditFormulas();
			super.afterEdit(e);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		/*
		 * if ("fyd_sjdh".equals(e.getItem().getKey())) { String cif_pk =
		 * (String) this.getBillCardPanel()
		 * .getHeadItem("cif_pk").getValueObject(); if (cif_pk != null &&
		 * cif_pk.length() > 0) { // 得到合同参照 UIRefPane panel = (UIRefPane)
		 * this.getBillCardPanel() .getHeadItem("fyd_sjdh").getComponent(); //
		 * this.getBillCardPanel() //
		 * .getHeadItem("cif_pk").setDecimalDigits(iDecimalDigits); //
		 * 加上客户做为条件去过滤 panel.getRefModel().setWherePart( " cif_pk = '" + cif_pk + "'
		 * and dr=0 "); } else {
		 * 
		 * this.showErrorMessage("您没有选择车辆信息，将不显示任何司机电话"); UIRefPane panel =
		 * (UIRefPane) this.getBillCardPanel()
		 * .getHeadItem("fyd_sjdh").getComponent(); // 加上客户做为条件去过滤
		 * panel.getRefModel().setWherePart(" dr=0 and 1=2 "); } }
		 */
//		if ("qy".equals(e.getItem().getKey())) {
//			UIRefPane panel = (UIRefPane) this.getBillCardPanel().getHeadItem(
//					"qy").getComponent();
//		}
		return false;
	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
