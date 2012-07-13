package nc.ui.wds.ic.pub;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ChangeListener;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.wds.ic.other.out.BillField;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.IBillField;
import nc.vo.wdsnew.pub.PickTool;

public class OutPubClientUI extends MutiChildForOutInUI implements ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PickTool pick=null;
	
	private nc.ui.wdsnew.pub.LogNumRefUFPanel ivjLotNumbRefPane=null;
	//批次档案所需数据
	private String m_strCorpID=null;
	private String m_strWareHouseID=null;
	private String m_spaceId=null;
	private String m_strInventoryID=null;
	private String pk_sspk=null;
   
	protected nc.ui.wdsnew.pub.LogNumRefUFPanel getLotNumbRefPane() {
		if (ivjLotNumbRefPane == null) {
			try {
				ivjLotNumbRefPane = new nc.ui.wdsnew.pub.LogNumRefUFPanel();
				ivjLotNumbRefPane.setName("LotNumbRefPane");
				ivjLotNumbRefPane.setLocation(38, 1);
			//	ivjLotNumbRefPane.setIsMutiSel(true);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				//handleException(ivjExc);
			}
		}
		return ivjLotNumbRefPane;
	}
	private PickTool getPick(){
		if(pick==null){
			pick=new PickTool();
		}
		return pick;
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();		
		int row = e.getRow();
		if (e.getPos() == BillItem.BODY) {
			if("vbatchcode".equalsIgnoreCase(key)){
				nc.ui.pub.bill.BillItem biCol = getBillCardPanel().getBodyItem(key);
						getLotNumbRefPane().setMaxLength(biCol.getLength());
						getBillCardPanel().getBodyPanel().getTable().getColumn(
								biCol.getName()).setCellEditor(
								new nc.ui.pub.bill.BillCellEditor(
										getLotNumbRefPane()));
						m_strCorpID=getClientEnvironment().getInstance().getCorporation().getPrimaryKey();
						m_strWareHouseID=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("srl_pk").getValueObject());
						m_spaceId=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject());
						m_strInventoryID=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "cinventoryid"));
						pk_sspk=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row, "vuserdef9"));
						String[] datas ={m_strCorpID,m_strWareHouseID,m_spaceId,m_strInventoryID,pk_sspk};						
						getLotNumbRefPane().setDatas(datas);
			
			}
		}
		return super.beforeEdit(e);
	}
//	private Map<String,List<TbOutgeneralTVO>> trayInfor = null;//缓存下  保存后更新到 buffer 行号+托盘流水表信息
//	public void setTrayInfor(Map<String,List<TbOutgeneralTVO>> trayInfor2){
//		trayInfor = trayInfor2;
//	}
//	public Map<String,List<TbOutgeneralTVO>> getTrayInfor(){
//		if(trayInfor == null)
//			trayInfor = new HashMap<String, List<TbOutgeneralTVO>>();
//		return trayInfor;
//	}
//	private Map<String,SmallTrayVO[]> lockTrayInfor = null;//虚拟托盘绑定的实际托盘信息 zhf add
//	public Map<String,SmallTrayVO[]> getLockTrayInfor(){
//		return lockTrayInfor;
//	}
//	public void setLockTrayInfor(Map<String,SmallTrayVO[]>  newInfor){
//		lockTrayInfor = newInfor;
//	}
	public OutPubClientUI(){
		super();
		//initlize();
	}
	public OutPubClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
		//initlize();
	}
//	/**
//	 * 初始化
//	 */
//	private void initlize() {
//		getBufferData().addObserver(new Observer() {
//			public void update(Observable o, Object arg) {
//				if (!getBufferData().isVOBufferEmpty()){
//					int row = getBufferData().getCurrentRow();
//					if(row < 0){
//						return;
//					}
//					//更新缓存
//					AggregatedValueObject obj = getBufferData().getCurrentVO();
//					if(obj != null){
//						AggregatedValueObject billvo = getBufferData().getCurrentVO();
//						TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[])billvo.getChildrenVO();
//						setList(bvo);
//					}
//				}
//			}		
//		});
//	}
	
//	protected void setTotalUIState(int intOpType) throws Exception {
//		super.setTotalUIState(intOpType);
//		switch (intOpType) {
//			case OP_ADD: 
//			case OP_REFADD: {
//				HashMap<String, List<TbOutgeneralTVO>> map = 
//					new HashMap<String, List<TbOutgeneralTVO>>();
//				setTrayInfor(map);
//			}
//		}
//	}
	@Override
	protected void setBillNo() throws Exception {
	
	}
	
//	public void setList(TbOutgeneralBVO[] bvo){
//		Map<String,List<TbOutgeneralTVO>> m = new HashMap<String,List<TbOutgeneralTVO>>();
//		if(bvo!=null && bvo.length>0){
//			for(TbOutgeneralBVO b : bvo){
//				String crowno = b.getCrowno();//行号
//				m.put(crowno, b.getTrayInfor());
//			}
//		}
//		setTrayInfor(m);
//	}

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
		// 增加页签切换监听
		UITabbedPane m_CardUITabbedPane = getBillCardPanel().getBodyTabbedPane();
		m_CardUITabbedPane.addChangeListener( this);

	}

	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
	}
	@Override
	protected IBillField createBillField() {
		// TODO Auto-generated method stub
		return new BillField();
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
			      //支持批次号 多选拆行  for add mlr
					List<StockInvOnHandVO> vos=getLotNumbRefPane().getLotNumbDlg().getLis();
					if(vos==null || vos.size()==0){
					   String vbatchcode=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(row, "vbatchcode"));
					   if(vbatchcode!=null&& vbatchcode.length()!=0){
						   setVbatchcode(e);
					   }
					}
					
					pick(vos,row);	
					getLotNumbRefPane().getLotNumbDlg().setLis(null);
			}					
			super.afterEdit(e);
		}catch(Exception e1){
			Logger.info(e1);
		}
	}
	
	private void setVbatchcode(BillEditEvent e) {			
		// 验证批次号是否正确
		String va=(String) e.getValue();
		if(va==null ||va.equalsIgnoreCase("")){
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "vbatchcode");
			this.showErrorMessage("批次号不能为空");
			return;
		}
		if (va.trim().length() < 8) {
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "vbatchcode");
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
			getBillCardPanel().getBillModel().setValueAt(null, e.getRow(), "vbatchcode");
			this.showErrorMessage(
			"批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
			return;

		}
	    //如果批次号输入格式正确  就给生成日期赋值
		String year=va.substring(0,4);
		String month=va.substring(4,6);
		String day=va.substring(6,8);
		String startdate=year+"-"+month+"-"+day;
		UFDate date=new UFDate(startdate);
		getBillCardPanel().setBodyValueAt(date, e.getRow(), "vuserdef7");		
	}
	/**
	 * 完达山物流 出库手动拣货
	 * 支持按批次自动拆行拣货
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-15下午03:33:56
	 * @param vos
	 * @param row 
	 */
	public void pick(List<StockInvOnHandVO> vos, int row) {
		//取出本次出库的总的数量 
		UFDouble zbnum=PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row, "nshouldoutassistnum"));
		if(zbnum.doubleValue()==0)
			return;
		//进行分量  
		//按 批次号  由小到大 依次分量
		if(vos==null )
			return;
		getPick().spiltNum(vos,row,zbnum);//进行拣货分量
		//重新构建表体	
		createBill(vos,row);			
  }
	/**
	 * 根据批次拆行 重新新构建表体数据
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-20上午11:11:57
	 * @param vos
	 * @param row
	 */
   public  void createBill(List<StockInvOnHandVO> vos, int row) {
	   BillModel bm = getBillCardPanel().getBillModel();						
		if(vos == null || vos.size() == 0){
			return ;
		}
		if(vos.size()==1){
			bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
			setDate(vos.get(0).getWhs_batchcode(),row);
			bm.setValueAt(vos.get(0).getSs_pk(), row, "vuserdef9");
			bm.setValueAt(vos.get(0).getWhs_oanum(), row, "noutassistnum");//设置实发辅数量  
		}else{
			//最后一行
			if(row==bm.getRowCount()-1){
				//处理第一行
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getSs_pk(), row, "vuserdef9");

				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//设置应发辅数量
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "noutassistnum");//设置实发辅数量
				for(int i=1;i<vos.size();i++){
				   bm.addLine();
				   bm.setBodyRowVO(bm.getBodyValueRowVO(row, TbOutgeneralBVO.class.getName()), row+i);
				   bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//批次
				   setDate(vos.get(i).getWhs_batchcode(),row+i);
					bm.setValueAt(vos.get(i).getSs_pk(), row+i, "vuserdef9");

				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//设置应发辅数量
				   bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "noutassistnum");//设置实发辅数量					
				}
			}else{
				//处理第一行
				bm.setValueAt(vos.get(0).getWhs_batchcode(), row, "vbatchcode");//批次
				setDate(vos.get(0).getWhs_batchcode(),row);
				bm.setValueAt(vos.get(0).getSs_pk(), row, "vuserdef9");
				bm.setValueAt(vos.get(0).getAttributeValue("whs_omnum"), row, "nshouldoutassistnum");//设置应发辅数量
				bm.setValueAt(vos.get(0).getAttributeValue("whs_oanum"), row, "noutassistnum");//设置实发辅数量
				for(int i=1;i<vos.size();i++){
				   bm.insertRow(row+i);
				   bm.setBodyRowVO(bm.getBodyValueRowVO(row, TbOutgeneralBVO.class.getName()), row+i);
				   bm.setValueAt(vos.get(i).getWhs_batchcode(), row+i, "vbatchcode");//批次
				   bm.setValueAt(vos.get(i).getSs_pk(), row+i, "vuserdef9");
				   setDate(vos.get(i).getWhs_batchcode(),row+i);
				   bm.setValueAt(vos.get(i).getAttributeValue("whs_omnum"), row+i, "nshouldoutassistnum");//设置应发辅数量
				   bm.setValueAt(vos.get(i).getAttributeValue("whs_oanum"), row+i, "noutassistnum");//设置实发辅数量					
				}				
			}
		}
   getBillCardPanel().getBillModel().execLoadFormula();
  // this.updateUI();
  //for end mlr
		
	}

    /**
     * 设置生产失效日期
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-6-28下午02:07:45
     * @param whs_batchcode
     * @param row
     * @throws UifException 
     */
	private void setDate(String va, int row)  {
	    //如果批次号输入格式正确就给生产日期赋值
		String year=va.substring(0,4);
		String month=va.substring(4,6);
		String day=va.substring(6,8);
		String startdate=year+"-"+month+"-"+day;	
		getBillCardPanel().setBodyValueAt(startdate, row, "vuserdef7");		
	}
	public void afterHeadCargDoc(Object pk_cargdoc){
		//清空库管员
		getBillCardPanel().setHeadItem("cwhsmanagerid", null);
		//表体重新赋值[货位]
		int row = getBillCardPanel().getBillTable().getRowCount();
		if(row < 0 ){
			return;
		}
		for(int i  = 0 ;i<row; i++){
			getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "cspaceid");//货位
		}
		//清除当前缓存
	//	getTrayInfor().clear();
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
	public  void stateChanged(javax.swing.event.ChangeEvent arg0){	
//		if(getBillOperate() == IBillOperate.OP_REFADD){
//			if("tb_outgeneral_b".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())){
//				getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//				getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
//			}else{
//				getButtonManager().getButton(IBillButton.AddLine).setEnabled(true);
//				getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
//			}
//			updateButtons();
//		}
		
		

		
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
		
		if(getBillCardPanel().getBodyTabbedPane().getSelectedIndex()!=0){
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

	public nc.ui.wdsnew.pub.LogNumRefUFPanel getIvjLotNumbRefPane() {
		return ivjLotNumbRefPane;
	}

	public void setIvjLotNumbRefPane(
			nc.ui.wdsnew.pub.LogNumRefUFPanel ivjLotNumbRefPane) {
		this.ivjLotNumbRefPane = ivjLotNumbRefPane;
	}

	public String getM_strCorpID() {
		return m_strCorpID;
	}

	public void setM_strCorpID(String corpID) {
		m_strCorpID = corpID;
	}

	public String getM_strWareHouseID() {
		return m_strWareHouseID;
	}

	public void setM_strWareHouseID(String wareHouseID) {
		m_strWareHouseID = wareHouseID;
	}



	public String getM_spaceId() {
		return m_spaceId;
	}

	public void setM_spaceId(String id) {
		m_spaceId = id;
	}



	public String getM_strInventoryID() {
		return m_strInventoryID;
	}

	public void setM_strInventoryID(String inventoryID) {
		m_strInventoryID = inventoryID;
	}


	
	
	
}
