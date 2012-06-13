package nc.ui.wds.ic.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.w80060206.buttun0206.ISsButtun;
import nc.ui.wl.pub.MutiChildForInUI;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.cargtray.SmallTrayVO;

public class MutiInPubClientUI extends MutiChildForInUI implements ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	private Map<String,List<TbGeneralBBVO>> trayInfor = null;//缓存下  保存后更新到 行号 + 托盘流水
	
	private Map<String,SmallTrayVO[]> lockTrayInfor = null;//虚拟托盘绑定的实际托盘信息 zhf add
	public Map<String,SmallTrayVO[]> getLockTrayInfor(){
		return lockTrayInfor;
	}
	public void setLockTrayInfor(Map<String,SmallTrayVO[]>  newInfor){
		lockTrayInfor = newInfor;
	}
	public MutiInPubClientUI() {
		super();
		setObserve();
	}
	public MutiInPubClientUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	/**
	 * 利用内部类，给当前ui类注册一个观察者监听器
	 * 来监听 缓存的变化 如果缓存发生变化 则利用观察者监听器
	 * 对ui界面中的托盘信息 进行更新 
	 * 主要监听BillUIBuffer 的 setCurrentRow() 方法 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-9-27下午03:10:37
	 */
	private void setObserve() {
		getBufferData().addObserver(new Observer(){
			public void update(Observable arg0, Object arg1) {
				if (!getBufferData().isVOBufferEmpty()){
					int row = getBufferData().getCurrentRow();
					if(row < 0){
						return;
					}
					//更新缓存
					AggregatedValueObject obj = getBufferData().getCurrentVO();
					if(obj != null){
						AggregatedValueObject billvo = getBufferData().getCurrentVO();
						TbGeneralBVO[] bvo = (TbGeneralBVO[])billvo.getChildrenVO();
						setList(bvo);
					}
				}				
			}
		});		
	}
	public void setList(TbGeneralBVO[] bvo){
		Map<String,List<TbGeneralBBVO>> m = new HashMap<String,List<TbGeneralBBVO>>();
		if(bvo!=null && bvo.length>0){
			for(TbGeneralBVO b : bvo){
				String crowno = b.getGeb_crowno();//行号
				m.put(crowno, b.getTrayInfor());
			}
		}
		setTrayInfor(m);
	}
	public void setTrayInfor(Map<String,List<TbGeneralBBVO>> trayInfor2){
		trayInfor = trayInfor2;
	}
	
	public Map<String,List<TbGeneralBBVO>> getTrayInfor(){
		if(trayInfor == null)
			trayInfor = new HashMap<String, List<TbGeneralBBVO>>();
		return trayInfor;
	}
	@Override
	protected void setBillNo() throws Exception {
	
	}
	@Override
	protected AbstractManageController createController() {
		return null;
	}
	protected void setTotalUIState(int intOpType) throws Exception {
		super.setTotalUIState(intOpType);
		switch (intOpType) {
			case OP_ADD: 
			case OP_REFADD: {
				HashMap<String, List<TbGeneralBBVO>> map = 
					new HashMap<String, List<TbGeneralBBVO>>();
				setTrayInfor(map);
			}
			break;
				
		}
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
						OtherInBillVO billvo = (OtherInBillVO)getBufferData().getCurrentVO();
						TbGeneralBVO[] bvo = (TbGeneralBVO[])billvo.getChildrenVO();
						setList(bvo);
					}
				}
			}		
		});
	}
	

	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {

	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {

	}

	@Override
	protected void initSelfData() {
		super.initSelfData();
		// 增加页签切换监听
		UITabbedPane m_CardUITabbedPane = getBillCardPanel().getBodyTabbedPane();
		m_CardUITabbedPane.addChangeListener(this);
	}

	@Override
	public void setDefaultData() throws Exception {

	}
	public AggregatedValueObject getChangedVOFromUI() throws java.lang.Exception {
		OtherInBillVO billvo = (OtherInBillVO)this.getBillCardWrapper().getChangedVOFromUI();	
		OtherInBillVO billvo2 = (OtherInBillVO)this.getBillCardWrapper().getBillVOFromUI();	
		if(getBillOperate() == IBillOperate.OP_ADD){//新增时 应使用 界面 vo 数据  不考虑删行情况
			billvo = billvo2;
		}
		if(billvo == null)
			return null;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])billvo.getChildrenVO();		
		if(bodys == null || bodys.length==0)
			return billvo;
		
		if(trayInfor == null)
			return billvo;
		String key = null;
		for(TbGeneralBVO body:bodys){
			key = body.getGeb_crowno();
			if(trayInfor.containsKey(key)){
				body.setTrayInfor(trayInfor.get(key));
			}
		}		
		billvo.setOUserObj(getLockTrayInfor());//设置虚拟托盘绑定信息
		return billvo;
	}
	
	public AggregatedValueObject getBillVOFromUI() throws Exception {
		AggregatedValueObject billvo = getBillCardWrapper().getBillVOFromUI();
		return billvo;
	}
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
				if("geb_proddate".equals(key)){//生产日期
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "geb_dvalidate");//失效日期
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//保质期天数
						UFBoolean b = vo.getQualitymanflag();//是否保质期
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "geb_dvalidate");//失效日期
						}
					}
				}else if("geb_dvalidate".equals(key)){//失效日期
					String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");
					if(value == null || "".equals(value)){
						getBillCardPanel().setBodyValueAt(null, row, "geb_proddate");//生产日期
					}else{
						if(cinventoryid == null) 
							return;
						InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
						UFDate date = new UFDate(value.toString());
						Integer num = vo.getQualitydaynum();//保质期天数
						UFBoolean b = vo.getQualitymanflag();//是否保质期
						if(b!=null && b.booleanValue()){
							getBillCardPanel().setBodyValueAt(date.getDateBefore(num), row, "geb_proddate");//生产日期
						}
					}
			}
		}
		if("geb_vbatchcode".equalsIgnoreCase(key)){			
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
			getBillCardPanel().setBodyValueAt(date, row, "geb_proddate");
			
			//给失效的日期赋值
			String cinventoryid = (String)getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");		
				if(cinventoryid == null) 
					return;
				InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
				Integer num = vo.getQualitydaynum();//保质期天数
				UFBoolean b = vo.getQualitymanflag();//是否保质期
				if(b!=null && b.booleanValue()){
					getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "geb_dvalidate");//失效日期
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
			getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "geb_space");//货位
		}
		//清除当前缓存
		getTrayInfor().clear();
		getBillCardPanel().getBillModel().execLoadFormula();
	}
	public void afterUpdate() {
		if (!getBufferData().isVOBufferEmpty()){
			int row = getBufferData().getCurrentRow();
			if(row < 0){
				return;
			}
		    //	getBillCardPanel().getBillModel().getBodyItems();
			Object o = getBufferData().getCurrentVO().getParentVO().getAttributeValue(getBillField().getField_BillStatus());
			if(o.equals(IBillStatus.FREE)){//自由
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(true);
			}else{//签字
				getButtonManager().getButton(ISsButtun.Qzqr).setEnabled(false);
				getButtonManager().getButton(ISsButtun.Qxqz).setEnabled(true);
			}
			updateButtons();
		}
	}
	
	public boolean beforeEdit(BillEditEvent e) {

		int row  = e.getRow();
		String key=e.getKey();

		String csourcetype = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
				.getBodyValueAt(row, "csourcetype"));
		//如果是参照过来的不可以编辑 ，如果是自制单据可以编辑
		if ("invcode".equalsIgnoreCase(key)) {
			if(getBillOperate() == IBillOperate.OP_EDIT)//zhf add 20110624  修改时 存货编码不能修改
				return false;
			if (csourcetype != null) {
				return false;
			} else {
				String pk_cargdoc=(String) getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
				if(null==pk_cargdoc || "".equalsIgnoreCase(pk_cargdoc)){
					showWarningMessage("前选择入库货位");
					return false;
				}			
				JComponent c =getBillCardPanel().getBodyItem("invcode").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and tb_spacegoods.pk_cargdoc='"+pk_cargdoc+"' ");
				}
				return true;
			}
		}
		if("geb_snum".equalsIgnoreCase(key)){
			if(getBillOperate() == IBillOperate.OP_EDIT)//zhf add 20110624  修改时 应收数量
				return false;
			if (csourcetype != null) {
				return false;
			} else {
				return true;
			}		
		}
		if("geb_bsnum".equalsIgnoreCase(key)){
			if(getBillOperate() == IBillOperate.OP_EDIT){//zhf add 20110624  修改时 应收辅数量
				if (csourcetype != null) {
					return false;
				} else {
					return true;
				}
			}
			return true;
		}
		if(e.getKey().equalsIgnoreCase("geb_vbatchcode")){
			if(getBillOperate() == IBillOperate.OP_EDIT){
				return false;
			}
		}

		return super.beforeEdit(e);
	}
	public  void stateChanged(javax.swing.event.ChangeEvent arg0){
		
		boolean isrefAdd = getBillOperate() == IBillOperate.OP_REFADD;//是否参照新增
		
		boolean isedit = getBillOperate() == IBillOperate.OP_EDIT;
		if(isedit && getBufferData().getCurrentVO() == null)
			return;
		boolean isEditSelf = false;//是否自制修改
		if(isedit){
			if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(0, "csourcetype"))==null){
				isEditSelf = true;
			}
		}
		
		if(!("tb_general_b".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode()))){
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(true);
			getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
			updateButtons();
			return;
		}
		
		if(isrefAdd || !isEditSelf){
			//			if("tb_general_b".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())){
			getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
			//			}
			updateButtons();
		}
	}
	
}
