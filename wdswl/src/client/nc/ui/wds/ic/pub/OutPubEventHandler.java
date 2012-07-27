package nc.ui.wds.ic.pub;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.so.out.TrayDisposeDlg;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds2.set.OutInSetHelper;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wdsnew.pub.BillStockBO1;
import nc.vo.wdsnew.pub.StockException;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
public class OutPubEventHandler extends WdsPubEnventHandler {

	public OutPubClientUI ui = null;
	private LoginInforHelper login=null;
    private BillStockBO1 stock=null;
    public BillStockBO1 getStock(){
    	if(stock==null){
    		stock=new BillStockBO1();
    	}
    	return stock;
    }
	
	public OutPubEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
		ui = billUI;
	}
//	//权限过滤查询处理类
//	nc.ui.zmpub.pub.bill.FlowManageEventHandler lt=null;
//	public nc.ui.zmpub.pub.bill.FlowManageEventHandler getETH(){
//		if(lt==null){
//			lt=new nc.ui.zmpub.pub.bill.FlowManageEventHandler(this.getBillManageUI(),this.getUIController());
//		}
//		return lt;
//	}


	private LoginInforHelper getLoginInfoHelper(){
		if(login==null){
			login=new LoginInforHelper();
		}
		return login;
	}
	/*
	 * mlr
	 * 自动取货(non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	protected void onzdqh() throws Exception {
		int results = ui.showOkCancelMessage("确认自动拣货?");
		if (results != 1) {
			return;
		}
		if (getBillUI().getVOFromUI() == null|| getBillUI().getVOFromUI().getChildrenVO() == null|| getBillUI().getVOFromUI().getChildrenVO().length == 0) {ui.showErrorMessage("获取当前界面数据出错.");
			return;
		}
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] generalbVOs = (TbOutgeneralBVO[]) billvo.getChildrenVO();
		// 数据校验 begin
		WdsWlPubTool.validationOnPickAction(getBillCardPanelWrapper().getBillCardPanel(), generalbVOs);
		// 数据校验end
		String pk_stordoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk").getValueObject());
		
		String pk_cargdoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject());
		TbOutgeneralBVO[] bvos=null;
		try {
			Class[] ParameterTypes = new Class[] { String.class,String.class,TbOutgeneralBVO[].class };
			Object[] ParameterValues = new Object[] {pk_stordoc,
					pk_cargdoc, generalbVOs };
			Object o = LongTimeTask.callRemoteService(
					WdsWlPubConst.WDS_WL_MODULENAME,
					"nc.vo.wdsnew.pub.PickTool", "autoPick",
					ParameterTypes, ParameterValues, 2);
			if (o != null) {
				bvos = (TbOutgeneralBVO[]) o;
			}
		} catch (Exception e) {
			
			if(e instanceof StockException){
				StockException se=(StockException) e;	
				bvos=(TbOutgeneralBVO[]) se.getBvos();				
			}else{
			  throw e;
			}
		}
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bvos);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	/**
	 * 
	 * @作者：
	 * @说明：完达山物流项目 
	 * 根据批次号好设置相关的日期信息
	 * @时间：2011-6-24上午08:24:44
	 * @param va
	 * @param row
	 * @throws Exception
	 */
	protected  void setDateInfor(String va,int row) throws Exception{
		UFDate date =_getDate();
	    //如果批次号输入格式正确就给生产日期赋值
		Pattern p = Pattern
		.compile(
				"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
				+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
				+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
				+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher m = p.matcher(va.trim().substring(0, 8));
		if (!m.find()) {
			String year=va.substring(0,4);
			String month=va.substring(4,6);
			String day=va.substring(6,8);
			String startdate=year+"-"+month+"-"+day;
			new UFDate(startdate);
		}
		getBillManageUI().getBillCardPanel().setBodyValueAt(date, row, "cshengchanriqi");	
		//给失效的日期赋值
		String cinventoryid = (String)getBillManageUI().getBillCardPanel().getBodyValueAt(row, "cinventoryid");		
			if(cinventoryid == null) 
				return;
			InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
			Integer num = vo.getQualitydaynum();//保质期天数
			UFBoolean b = vo.getQualitymanflag();//是否保质期
			if(b!=null && b.booleanValue()){
				getBillManageUI().getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//失效日期
			}	
	}
	//zpm--end

	/**
	 * tT
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目
	 * @时间：2011-4-20下午09:09:55
	 */
	protected void setBodyModelState() {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.getRowCount();
		if (row < 0)
			return;
		for (int i = 0; i < row; i++) {
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setRowState(i, BillModel.MODIFICATION);
		}
	}

	// 参照数据交换完毕后， 设置默认值
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		WdsWlPubTool.setVOsRowNoByRule(vos, "crowno");
		super.setRefData(vos);
		getBillUI().setDefaultData();
		setBodySpace();
		getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
		getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
		getBillUI().updateButtons();
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		setBodySpace();
		///zpm start//生成行号
	    BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),WdsWlPubConst.BILLTYPE_OTHER_OUT, "crowno");
		//zpm end

	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
//	    getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//		getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
		if(PuPubVO.getString_TrimZeroLenAsNull(getBufferData().getCurrentVO().getParentVO().getPrimaryKey())!=null){
		 getButtonManager().getButton(ISsButtun.fzgn).setEnabled(false);
		}
		boolean isself = PuPubVO.getString_TrimZeroLenAsNull(
				getBufferData().getCurrentVO().getChildrenVO()[0].getAttributeValue("csourcetype"))
				==null?true:false;
		if(!isself){
			if(getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().getSelectedIndex() == 0){
				getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
			}else{
				getButtonManager().getButton(IBillButton.AddLine).setEnabled(true);
			}

			getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
			getBillUI().updateButtons();
		}
		
		
//		getBillUI().updateButtons();
	}
	// 表体赋货位
	protected void setBodySpace() throws BusinessException {
		String pk_cargdoc = getPk_cargDoc();
		if (pk_cargdoc == null || "".equals(pk_cargdoc)) {
			// throw new BusinessException("请指定货位信息");
			return;
		}
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		for (int i = 0; i < row; i++) {
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setValueAt(pk_cargdoc, i, "cspaceid");
			//liuys add 其他出库 带入默认业务日期
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getDate(), i, "dbizdate");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulasByKey("cspaceid");
		}
	}

	// 表头获取出库货位
	protected String getPk_cargDoc() {
		String pk_cargdoc = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		return pk_cargdoc;
	}

	// 表头获取出库仓库
	protected String getCwhid() {
		String cwhid = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("srl_pk").getValueObject();
		return cwhid;
	}

	/*
	 * 查询明细
	 */
	protected void onckmx() throws Exception {
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(
				WdsWlPubConst.DLG_OUT_TRAY_APPOINT, ui._getOperator(), ui
						._getCorp().getPrimaryKey(), ui, false);
		
		tdpDlg.showModal();
	}
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		onBoRefresh();
	}
	/**zpm start **/
	protected int geLineRowByCrowno(String crowno){
		int row = -1;
		int rowcout = getBillManageUI().getBillCardPanel().getBillModel().getRowCount();
		if(rowcout > 0){
			for(int i = 0 ;i<rowcout;i++){
				String crowno_1 = (String)getBillManageUI().getBillCardPanel().getBillModel().getValueAt(i, "crowno");
					row = i;
					if(crowno.equals(crowno_1)){
					break;
				}
			}
		}
		return row;
	}

	@Override
	protected void onBoSave() throws Exception {
		if(!getUIController().getBillType().equals(WdsWlPubConst.HWTZ)){
			valudate();
		}	
		setStock();
		super.onBoSave();
	}
	/**
	 * 设置库存存量
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-23下午01:12:05
	 */
	private void setStock() throws Exception{	
		//只设置新增单据现存量
		String pk_h=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getPrimaryKey());
		if(pk_h!=null)
			return;
		int rowcount=getBodyRowCount();	
		//设置现存量查询维度
	    StockInvOnHandVO[] vos=new StockInvOnHandVO[rowcount];	    
		for(int i=0;i<rowcount;i++){
			String pk_corp=PuPubVO.getString_TrimZeroLenAsNull(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			String pk_stordoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk").getValueObject());
			String pk_cargdoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject());
			String pk_invmandoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "cinventoryid"));
			String pk_invbasdoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "cinvbasid"));
			String vbancode=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vbatchcode"));
			String pk_state=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vuserdef9")); 
			vos[i]=new StockInvOnHandVO();
			vos[i].setPk_corp(pk_corp);
			vos[i].setPk_customize1(pk_stordoc);
			vos[i].setPk_cargdoc(pk_cargdoc);
			vos[i].setPk_invmandoc(pk_invmandoc);
			vos[i].setPk_invbasdoc(pk_invbasdoc);
			vos[i].setWhs_batchcode(vbancode);
			vos[i].setSs_pk(pk_state);
		}
	    //按设置好的维度 查询现存量
	    StockInvOnHandVO[] nvos=(StockInvOnHandVO[]) getStock().queryStockCombinForClient(vos);
			 
		//设置现存量 到ui	 
		if(nvos==null || nvos.length==0)
				 return;
		   for(int i=0;i<rowcount;i++){
		    	StockInvOnHandVO vo=nvos[i];
		    	if(vo==null)
		    	    continue;
				UFDouble uf1=PuPubVO.getUFDouble_NullAsZero(vo.getWhs_stocktonnage());//库存主数量
				UFDouble uf2=PuPubVO.getUFDouble_NullAsZero(vo.getWhs_stockpieces());//库存辅数量
				UFDouble uf3=PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "noutnum"));//实出主数量
				UFDouble uf4=PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "noutassistnum"));//实出辅数量			
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt((uf1.sub(uf3)).toString(), i, "vuserdef2");
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt((uf2.sub(uf4)).toString(), i, "vuserdef5");					
		}			 
	
	}


	/**
	 * 保存前进行数据校验
	 * @throws Exception 
	 * @throws BusinessException 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-13下午07:33:33
	 */
	private void valudate() throws BusinessException, Exception {
		if( getBillUI().getVOFromUI().getChildrenVO()!=null){
			TbOutgeneralBVO[] tbs=(TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
			for(int i=0;i<tbs.length;i++){
				//校验贴签数量    不能大于  实出数量
				UFDouble u1=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutassistnum());//实发数量
				UFDouble u2=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNtagnum());//贴签数量
				if(u1.sub(u2).doubleValue()<0){
					throw new BusinessException("贴签数量   不能大于 实出数量");
				}	
				//校验应发数量 不能小于实出数量
				UFDouble u3=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNshouldoutnum());//应发数量
				UFDouble u4=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutnum());//实出数量
				if(u3.sub(u4).doubleValue()<0){
					throw new BusinessException("应发数量   不能小于  实出数量");
				}
				//校验生成日期不能为空 
				String  date=PuPubVO.getString_TrimZeroLenAsNull(tbs[i].getVuserdef7());
				if(date==null){
					throw new Exception("生成日期不能为空");
				}
			}			
		}
		
	}


	protected void onPasteLineToTail(int line,String[] vbatchcodes) throws Exception{
		if(vbatchcodes!=null && vbatchcodes.length>0){
			for(int i = 0 ;i<vbatchcodes.length;i++){
				String vbatchcode = vbatchcodes[i];
				if(vbatchcode !=null && "".equals(vbatchcode)){
					CircularlyAccessibleValueObject vo = getSelectedBodyVO(i);//复制粘贴行
					onBoPaste(vo);
				}
			}
		}
	}
	//复制当前行
	public CircularlyAccessibleValueObject getSelectedBodyVO(int row){
		CircularlyAccessibleValueObject vo = getBillManageUI().getBillCardPanel().getBillModel()
		.getBodyValueRowVO(row, getBillManageUI().getUIControl().getBillVoName()[2]);
		return vo;
	}
	//粘贴当前行到行尾前处理
	protected void processCopyedBodyVOsBeforePaste(CircularlyAccessibleValueObject vo) {
		if (vo == null)
			return;
		vo.setAttributeValue(getUIController().getPkField(), null);
		vo.setAttributeValue(getUIController().getChildPkField(), null);
	}
	//粘贴
	protected void onBoPaste(CircularlyAccessibleValueObject vo){
		processCopyedBodyVOsBeforePaste(vo);
		getBillManageUI().getBillCardPanel().stopEditing();
		getBillManageUI().getBillCardPanel().addLine();
		int selectedRow = getBillManageUI().getBillCardPanel().getBillTable().getRowCount()-1;
		String crowno = ""+((selectedRow+1)*10);
		vo.setAttributeValue("crowno", crowno);
		vo.setAttributeValue("general_b_pk", null);
		getBillManageUI().getBillCardPanel().getBillModel().setBodyRowVO(vo,selectedRow);
		execLoadBodyRowFormula(selectedRow);
	}
	//得到新增行的行号
	protected String getLastLineNO(){
		int selectedRow = getBillManageUI().getBillCardPanel().getBillTable().getRowCount()-1;
		String crowno  = (String)getBillManageUI().getBillCardPanel().getBillModel().getValueAt(selectedRow, "crowno");
		return crowno;
	}
	//执行公式
	private void execLoadBodyRowFormula(int intRow){
		try{
			getBillManageUI().getBillCardPanel().getBillModel().execEditFormulas(intRow);
		}catch (Exception ex){
			System.out.println("BillListWrapper:执行行公式加载数据错误");
			ex.printStackTrace();
		}
	}
	public int getBodyRowCount() {
		int rowcount = -1;
		if (ui.getBillListPanel().isShowing()) {
			rowcount = ui.getBillListPanel().getBodyBillModel().getRowCount();
		} else {
			rowcount = ui.getBillCardPanel().getBillModel().getRowCount();
		}
		return rowcount;
	}	
	/**
	 * @author yf
	 * 出入库 对当前用户进行权限校验
	 * 总仓 管理员 isMaster = true 
	 * 分仓 管理员
	 * 设置字段是否可以编辑，不是总仓管理员不能编辑
	 * @throws Exception 
	 */
	protected void setInitByWhid(String[] fields) throws Exception{
		//进行权限校验
		//校验登陆人 的仓库权限，总仓管理员，分仓管理员
		boolean isMaster = false;
		String whid = "";
		LoginInforVO liv = new LoginInforHelper().getLogInfor(_getOperator());
		whid = liv.getWhid();
		if(WdsWlPubConst.WDS_WL_ZC.equalsIgnoreCase(whid)){
			isMaster = true;
		}
		String  pk_stordoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk").getValueObject());
	    if(pk_stordoc==null ||pk_stordoc.length()==0){
	    	return;
//	    	throw new Exception("仓库为空");
	    }
		if(!pk_stordoc.equalsIgnoreCase(getLoginInfoHelper().getCwhid(_getOperator()))){
	    	 throw new Exception("当前保管员  未绑定 当前 被参照的销售运单的出库仓库");
	    }else{
	         //当前库管员绑定的货位
    	     getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setValue(getLoginInfoHelper().getSpaceByLogUserForStore(_getOperator()));
		    //设置仓库，货位是否可编辑，是主仓管理员则可以编辑，否则不可以编辑
		    for(String field : fields){
			 getBillCardPanelWrapper().getBillCardPanel().getHeadItem(field).setEnabled(isMaster);
		    }
	    }	
	}
	
	//如果 参照的出库仓库为空 设置默认仓库为当前保管员仓库
	protected void setInitWarehouse(String warehouseid) throws Exception{
		BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(warehouseid);
		if(null != item && item.getValueObject() == null){
			String  geh_cwarehouseid = ((MutiChildForOutInUI) getBillUI()).getLoginInforHelper().getCwhid(_getOperator());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(warehouseid, geh_cwarehouseid);
		}
	}
	
	public void setOutType() throws BusinessException{
//		设置默认收发类别
		String outintype = OutInSetHelper.getDefaultOutInTypeID(
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), 
				"csourcetype", true);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("cdispatcherid", outintype);	
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		
//		设置默认收发类别
		setOutType();
	}
	
	public void onBillRef() throws Exception {
		super.onBillRef();
		
//		设置默认收发类别
		setOutType();	
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn ==  ButtonCommon.joinup)
			onJoinQuery();
		else
			super.onBoElse(intBtn);
	}
}
