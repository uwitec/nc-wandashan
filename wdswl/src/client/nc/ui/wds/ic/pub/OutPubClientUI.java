package nc.ui.wds.ic.pub;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.TableColumn;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.ic.other.out.BillField;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.field.IBillField;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

public class OutPubClientUI extends MutiChildForOutInUI {
	
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//缓存下  保存后更新到 buffer 行号+托盘流水表信息
	public void setTrayInfor(Map<String,List<TbOutgeneralTVO>> trayInfor2){
		trayInfor = trayInfor2;
	}
	public Map<String,List<TbOutgeneralTVO>> getTrayInfor(){
		if(trayInfor == null)
			trayInfor = new HashMap<String, List<TbOutgeneralTVO>>();
		return trayInfor;
	}
	private Map<String,SmallTrayVO[]> lockTrayInfor = null;//虚拟托盘绑定的实际托盘信息 zhf add
	public Map<String,SmallTrayVO[]> getLockTrayInfor(){
		return lockTrayInfor;
	}
	public void setLockTrayInfor(Map<String,SmallTrayVO[]>  newInfor){
		lockTrayInfor = newInfor;
	}
	public OutPubClientUI(){
		super();
		initlize();
	}
	public OutPubClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		initlize();
	}
	/**
	 * 初始化
	 */
	private void initlize() {
		getBufferData().addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if (!getBufferData().isVOBufferEmpty()){
					int row = getBufferData().getCurrentRow();
					if(row < 0){
						return;
					}
					//更新缓存
					AggregatedValueObject obj = getBufferData().getCurrentVO();
					if(obj != null){
						AggregatedValueObject billvo = getBufferData().getCurrentVO();
						TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[])billvo.getChildrenVO();
						setList(bvo);
					}
				}
			}		
		});
	}
	
	protected void setTotalUIState(int intOpType) throws Exception {
		super.setTotalUIState(intOpType);
		switch (intOpType) {
			case OP_ADD: 
			case OP_REFADD: {
				HashMap<String, List<TbOutgeneralTVO>> map = 
					new HashMap<String, List<TbOutgeneralTVO>>();
				setTrayInfor(map);
			}
		}
	}
	@Override
	protected void setBillNo() throws Exception {
	
	}
	
	public void setList(TbOutgeneralBVO[] bvo){
		Map<String,List<TbOutgeneralTVO>> m = new HashMap<String,List<TbOutgeneralTVO>>();
		if(bvo!=null && bvo.length>0){
			for(TbOutgeneralBVO b : bvo){
				String crowno = b.getCrowno();//行号
				m.put(crowno, b.getTrayInfor());
			}
		}
		setTrayInfor(m);
	}

	@Override
	protected AbstractManageController createController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultData() throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * 获得界面变化数据VO。 创建日期：(2004-1-7 10:01:01)
	 *
	 * @return nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public AggregatedValueObject getChangedVOFromUI()
	throws java.lang.Exception {
		MyBillVO billvo = (MyBillVO)this.getBillCardWrapper().getChangedVOFromUI();
		MyBillVO billvo2 = (MyBillVO)this.getBillCardWrapper().getBillVOFromUI();
		if(getBillOperate() == IBillOperate.OP_ADD)
			billvo = billvo2;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[])billvo.getChildrenVO();

		if(bodys == null || bodys.length==0)
			return billvo;
		if(trayInfor == null)
			return billvo;
		String key = null;
		for(TbOutgeneralBVO body:bodys){
			key = body.getCrowno();
			if(trayInfor.containsKey(key)){
				body.setTrayInfor(trayInfor.get(key));
			}
		}
		billvo.setOUserObj(getLockTrayInfor());//设置虚拟托盘解除绑定信息
		return billvo;
	}
	
	@Override
	protected IBillField createBillField() {
		// TODO Auto-generated method stub
		return new BillField();
	}
	
//	public void afterEdit(nc.ui.pub.bill.BillEditEvent e) {
//		if(e.getPos() == BillItem.BODY){
//			String key = e.getKey();
//			int row = e.getRow();
//			if("nshouldoutnum".equalsIgnoreCase(key)||"nshouldoutassistnum".equalsIgnoreCase(key)){
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "noutnum");
//				getBillListPanel().getBodyBillModel().setValueAt(null, row, "noutassistnum");
//			}
//		}
//		super.afterEdit(e);
//	}
	@Override
	public void afterEdit(BillEditEvent e) {
		try{
			String key = e.getKey();
			int row = e.getRow();
			Object value = e.getValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_cargdoc".equals(key)){//货位
					UIRefPane u = (UIRefPane)e.getSource();
					afterHeadCargDoc(u.getRefPK());				
				}
			}else{
				if("cshengchanriqi".equals(key)){//生产日期
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "cshixiaoriqi");//失效日期
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//保质期天数
						UFBoolean b = vo.getQualitymanflag();//是否保质期
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//失效日期
						}
					}
				}else if("cshixiaoriqi".equals(key)){//失效日期
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "cshengchanriqi");//生产日期
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//保质期天数
						UFBoolean b = vo.getQualitymanflag();//是否保质期
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateBefore(num), row, "cshengchanriqi");//生产日期
						}
					}
				}
			}
			
			if("vbatchcode".equalsIgnoreCase(key)){			
				// 验证批次号是否正确
				String va=(String) e.getValue();
				if(va==null ||va.equalsIgnoreCase("")){
					this.showErrorMessage("批次号不能为空");
					return;
				}
				if (va.trim().length() < 8) {
					this.showErrorMessage("批次号不能小于8位");
				     return ;
				}
		
				Pattern p = Pattern
				.compile(
						"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
						+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
						+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
						+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
						Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
				Matcher m = p.matcher(va.trim().substring(0, 8));
				if (!m.find()) {
					this.showErrorMessage(
					"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
					return;
		
				}
			    //如果批次号输入格式正确就给生产日期赋值
				String year=va.substring(0,4);
				String month=va.substring(4,6);
				String day=va.substring(6,8);
				String startdate=year+"-"+month+"-"+day;
				UFDate date=new UFDate(startdate);
				getBillCardPanel().setBodyValueAt(date, row, "cshengchanriqi");
				
				//给失效的日期赋值
				String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "cinventoryid");		
					if(cinventoryid == null) 
						return;
					InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
					Integer num = vo.getQualitydaynum();//保质期天数
					UFBoolean b = vo.getQualitymanflag();//是否保质期
					if(b!=null && b.booleanValue()){
						getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//失效日期
					}			
			}	
				
			super.afterEdit(e);
		}catch(Exception e1){
			Logger.info(e1);
		}
	}
	
	public void afterHeadCargDoc(Object pk_cargdoc){
		//表体重新赋值[货位]
		int row = getBillCardPanel().getBillTable().getRowCount();
		if(row < 0 ){
			return;
		}
		for(int i  = 0 ;i<row; i++){
			getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "cspaceid");//货位
		}
		//清除当前缓存
		getTrayInfor().clear();
		getBillCardPanel().getBillModel().execLoadFormula();
	}
	/**
	 * @author yf
	 * @说明 根据表体 tableCode,清空页签数据
	 * @时间 2011-04-29下午02:06:02
	 * @param tableCodes
	 */
	protected void clearTable(String[] tableCodes) {
		if (tableCodes != null && tableCodes.length > 0) {
			for (int i = 0; i < tableCodes.length; i++) {
				int count = getBillCardPanel().getBillModel(tableCodes[i])
						.getRowCount();
				int[] array = new int[count];
				for (int j = 0; j < count; j++) {
					array[j] = j;
				}
				getBillCardPanel().getBillData().getBillModel(tableCodes[i])
						.delLine(array);
			}
		}
	}
	protected nc.ui.pub.beans.UITabbedPane getUITabbedPane(Component c) {
		if (c instanceof UITabbedPane)
			return (UITabbedPane) c;
		if (c instanceof Container) {
			Component[] comps = ((Container) c).getComponents();
			for (int i = 0; i < comps.length; i++) {
				Component cc = getUITabbedPane(comps[i]);
				if (cc instanceof UITabbedPane)
					return (UITabbedPane) cc;
			}
		}
		return null;
	}
//	private void setUITimeTextField(String tablecode,String cellcode){
//		 TableColumn tablecol = null;
//	     BillItem dsendtime = getBillCardPanel().getBodyItem(tablecode,cellcode);
//	     if (null != dsendtime) {
//	       // 设置时间编辑器
//	       try {
//	         //计划发货时间
//	         tablecol = getBillCardPanel().getBodyPanel(tablecode).getTable().getColumn(dsendtime.getName());
//	         if (null != tablecol) {
//	           BillCellEditor timecelledit = new BillCellEditor(new nc.ui.scm.pattern.pub.UITimeTextField());
//	           tablecol.setCellEditor(timecelledit);
//	         }
//	       } catch (Exception e) {
//	    	   nc.vo.scm.pub.SCMEnv.out(e);
//	       }
//	     }
//	}

	
//	public boolean onClosing() {
//		boolean flag = super.onClosing();
//		MakeBiddingHelper.clear();//销毁 静态变量
//		return flag;
//	}
}
