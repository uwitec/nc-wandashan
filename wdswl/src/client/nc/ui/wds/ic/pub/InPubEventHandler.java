package nc.ui.wds.ic.pub;

import java.awt.Color;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.w8004040214.buttun0214.ISsButtun;
import nc.ui.wds2.set.OutInSetHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.MutiChildForInUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wdsnew.pub.StockException;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

public abstract class InPubEventHandler extends WdsPubEnventHandler {

	public MutiInPubClientUI ui = null;
	
	
	public InPubEventHandler(MutiInPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
		ui = billUI;
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		setInType();
		setHeadPartEdit(true);
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		setBodySpace();
		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
				getBillType(), "geb_crowno");
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		super.setRefData(vos);
		getBillManageUI().setDefaultData();
		setBodySpace();//表体赋默认值
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
		getButtonManager().getButton(IBillButton.DelLine).setEnabled(true);
		getBillUI().updateButtons();
	}
	//表体赋货位
	protected void setBodySpace()throws BusinessException{
		String pk_cargdoc = getPk_cargDoc();
		if(pk_cargdoc == null || "".equals(pk_cargdoc)){
			return ;
		}
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for(int i = 0 ;i<row;i++){
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(pk_cargdoc, i, "geb_space");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getDate(), i, "geb_dbizdate");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulasByKey("geb_space");
		}
	}

	/**
	 * 取消签字
	 */
	protected void onQxqz() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("正在执行取消签字...");
				int retu = getBillManageUI().showOkCancelMessage("取消签字，会删除装卸费核算单，确认取消签字?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbGeneralHVO tbGeneralHVOss = (TbGeneralHVO)aObject.getParentVO();
				tbGeneralHVOss.setGeh_storname(null);// 库房签字人
				tbGeneralHVOss.setTaccounttime(null);// 签字时间
				tbGeneralHVOss.setClastmodiid(_getOperator());// 最后修改人
				tbGeneralHVOss.setClastmodedate(ui._getDate());// 最后修改人日期
				tbGeneralHVOss.setClastmodetime(ui._getServerTime().toString());// 最后修改时间
				tbGeneralHVOss.setPwb_fbillflag(IBillStatus.FREE);// 单据状态
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//登录人
				//动作脚本
				PfUtilClient.processAction(getBillManageUI(), "CANELSIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				getBillManageUI().showHintMessage("取消签字完成");
				//更新缓存
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("取消签字失败:"+e.getMessage());
		}
		
	}

	/**
	 * 签字确认
	 */
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("正在执行签字...");
				int retu = getBillManageUI().showOkCancelMessage("确认签字?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbGeneralHVO tbGeneralHVOss = (TbGeneralHVO)aObject.getParentVO();
				TbGeneralBVO[] bvos =(TbGeneralBVO[]) aObject.getChildrenVO();
				tbGeneralHVOss.setGeh_storname(_getOperator());// 库房签字人
				tbGeneralHVOss.setTaccounttime(ui._getServerTime().toString());// 签字时间
				tbGeneralHVOss.setClastmodiid(_getOperator());// 最后修改人
				tbGeneralHVOss.setClastmodedate(ui._getDate());// 最后修改人日期
				tbGeneralHVOss.setClastmodetime(ui._getServerTime().toString());// 最后修改时间
				tbGeneralHVOss.setPwb_fbillflag(IBillStatus.CHECKPASS);// 单据状态
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//登录人
				//动作脚本
				PfUtilClient.processAction(getBillManageUI(), "SIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				getBillManageUI().showHintMessage("签字完成");
				//更新缓存
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("签字失败:"+e.getMessage());
		}
	
	}
	
	/**
	 * 指定托盘
	 */
//	protected int onZdtp() throws Exception {
//		getBillCardPanelWrapper().getBillCardPanel().stopEditing();
//		
//		
//		String pk_cargdoc =  getPk_cargDoc();//货位
//		if(pk_cargdoc == null || "".equals(pk_cargdoc))
//			throw new BusinessException("请指定货位信息");
//		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
//		TbGeneralBVO[] bvo  = (TbGeneralBVO[])aggvo.getChildrenVO();
//		if(bvo == null || bvo.length == 0)
//			throw new BusinessException("请先输入表体数据");
//		for(TbGeneralBVO vo : bvo){
//			vo.validateOnZdrk(false);
//		}		
//		//校验批次号
//		if(! validateBachCode()){
//		   throw new  BusinessException("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
//		}
//		
//		
//		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(WdsWlPubConst.DLG_IN_TRAY_APPOINT,_getOperator(), 
//				_getCorp().getPrimaryKey(), null,ui,true);
//		int retflag = UIDialog.ID_CANCEL;
//		if(tdpDlg.showModal() == UIDialog.ID_OK){
//			Map<String,List<TbGeneralBBVO>> map = tdpDlg.getBufferData();
//			ui.setTrayInfor(map);
//			
//			Map<String,SmallTrayVO[]> lockTrayInfor = tdpDlg.getTrayLockInfor(false);
//			ui.setLockTrayInfor(lockTrayInfor);
//			setBodyValueToft();
//			retflag = UIDialog.ID_OK;
//		}
//		setBackGround();
//		chaneColor();
//		setBodyModelState();
//		return retflag;
//	}
	protected String getPk_cargDoc(){
		String pk_cargdoc = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		
		return pk_cargdoc;		
	}
	
	protected String getCwhid(){
		String cwhid = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cwarehouseid").getValueObject();
		return cwhid;
	}
	
//	@Override
//	protected void onBoLineDel() throws Exception {
//		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
//		for(int i = 0 ;i<rows.length; i++){
//			String crowno = (String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(rows[i], "geb_crowno");
//			ui.getTrayInfor().remove(crowno);
//		}
//		super.onBoLineDel();
//	}
	
//	class filterDelLine implements IFilter{
//
//		public boolean accept(Object o) {
//			// TODO Auto-generated method stub
//			if(o == null)
//				return false;
//			SuperVO vo = (SuperVO)o;
//			if(vo.getStatus() == VOStatus.DELETED)
//				return false;
//			return true;
//		}
//		
//	}
//	
	@Override
	protected void onBoSave() throws Exception {
		if(! validateBachCode()){
			   throw new  BusinessException("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
		}
		AggregatedValueObject vo = ui.getChangedVOFromUI();
		TbGeneralHVO hvo = (TbGeneralHVO)vo.getParentVO();
		hvo.validateBeforSave();
		
		TbGeneralBVO[] vos = (TbGeneralBVO[])vo.getChildrenVO();
//		TbGeneralBVO[] vos = (TbGeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(ovos, new filterDelLine());

		if(vos == null || vos.length ==0 && null == PuPubVO.getString_TrimZeroLenAsNull(hvo.getPrimaryKey())){
			//throw new BusinessException("表体不允许为空");
		}
		if(vos != null && vos.length > 0 ){
			for(TbGeneralBVO v : vos){
			//	if(v.getPrimaryKey() !=null && v.getPrimaryKey().length()!=0){
				if(v.getStatus() == VOStatus.DELETED)//删除的行不进行校验
					continue;
				v.validateOnSave();
//				List<TbGeneralBBVO> list = v.getTrayInfor();
//				for(TbGeneralBBVO b : list){
//					b.validateOnSave();
//				}
//		//		}
			}
		}
		super.onBoSave();
//		onBoRefresh();
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		onBoRefresh();
	}
	
//	public void setBodyValueToft(){
//		int row  = getBodyRowCount();
//		if(row > 0){
//			for(int i = 0 ;i < row;i++){
//				String crowno = (String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_crowno");//行号
//				UFDouble[]  d = getDFromBuffer(crowno);
//				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(d[0], i, "geb_anum");//主数量
//				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(d[1], i, "geb_banum");//辅数量
//			}
//		}
//	}
//	public UFDouble[] getDFromBuffer(String crowno){
//		UFDouble[] d = new UFDouble[2];
//		d[0] = new UFDouble(0);//实入数量
//		d[1] = new UFDouble(0);//实入辅数量
//		List<TbGeneralBBVO> list = ui.getTrayInfor().get(crowno);
//		if(list != null && list.size() > 0){
//			for(TbGeneralBBVO l : list){
//				UFDouble b = l.getGebb_num();//实入数量
//				UFDouble b1 = l.getNinassistnum();//实入辅数量
//				d[0] = d[0].add(b);
//				d[1] = d[1].add(b1);
//			}
//		}
//		return d;
//	}
//	
	public int getBodyRowCount(){
		int rowcount = -1;
		if(ui.getBillListPanel().isShowing()){
			rowcount = ui.getBillListPanel().getBodyBillModel().getRowCount();
		}else{
			rowcount = ui.getBillCardPanel().getBillModel().getRowCount();
		}
		return rowcount;
	}

	//红色：没有实发数量；
	//灰色：有实发数量但是数量不够；
	//白色：实发数量与应发数量相等
	protected void setBackGround(){
		int row  = getBodyRowCount();
		if(row > 0){
			for(int i = 0 ;i < row;i++){
				UFDouble b1 = PuPubVO.getUFDouble_NullAsZero(//应发主数量
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_snum"));
				UFDouble b2 = PuPubVO.getUFDouble_NullAsZero(//实发主数量
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_anum"));
				//yf add
				//实入小于应入:红色
				if(b2.sub(b1).doubleValue() < 0 ){
					String[] changFields = {"geb_anum","geb_snum","geb_bsnum","geb_banum"};
					for (String field : changFields) {
						getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
						.setCellForeGround(i, field, Color.red);	
					}
					
				}else if(b1.sub(b2).doubleValue() == 0){
					String[] changFields = {"geb_anum","geb_snum","geb_bsnum","geb_banum"};
					for (String field : changFields) {
						getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
						.setCellForeGround(i, field, Color.black);	
					}
				}
			}
		}
	}

	/**tT
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-4-20下午09:09:55
	 */
	 protected void setBodyModelState(){
		 int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
		 if(row < 0)return;
		 for(int i = 0 ;i<row;i++){
			 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
		 }
	 }
	  
	 
	 
	 
	@Override
	protected void onBoLineCopy() throws Exception {
		
		super.onBoLineCopy();
	}
	
	protected abstract String getBillType();

	@Override
	protected void onBoLinePaste() throws Exception {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		super.onBoLinePaste();
		getBillCardPanelWrapper()
		.getBillCardPanel().getBillTable().getSelectionModel().setSelectionInterval(row+1, row+1);
//		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
//				getBillType(), "geb_crowno");
		BillRowNo.pasteLineRowNo(getBillCardPanelWrapper().getBillCardPanel(), getBillType(), "geb_crowno", 1);
	}

//	/**
//	 * 查看明细
//	 */
//   
//	protected void onCkmx() throws Exception {
//		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(WdsWlPubConst.DLG_IN_TRAY_APPOINT,
//				_getOperator(), _getCorp().getPrimaryKey(), null,ui,false);
//		tdpDlg.showModal();
//	}
	
	/**
	 * 自动入库
	 */
	protected void onZdrk() throws Exception {
		if(! validateBachCode()){
			throw new  BusinessException("批次号输入的不正确,请您输入正确的日期!如：20100101XXXXXX");
		}
		String cwid = getCwhid();//仓库
		if(cwid == null || "".equals(cwid))
			throw new BusinessException("请指定仓库信息");
		String pk_cargdoc =  getPk_cargDoc();//货位
		if(pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("请指定货位信息");
		TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodyValueVOs(TbGeneralBVO.class.getName());
		if(tbGeneralBVOs == null||tbGeneralBVOs.length ==0){
			throw new BusinessException("表体数据为空");
		}
		// 数据校验
		for (TbGeneralBVO body:tbGeneralBVOs) {
			body.validateOnZdrk(false);
		}
	//	ui.showProgressBar(true);
		TbGeneralBVO[] bvos=null;
		try {
			Class[] ParameterTypes = new Class[] { String.class,String.class,TbGeneralBVO[].class };
			Object[] ParameterValues = new Object[] {cwid,
					pk_cargdoc, tbGeneralBVOs };
			Object o = LongTimeTask.callRemoteService(
					WdsWlPubConst.WDS_WL_MODULENAME,
					"nc.vo.wdsnew.pub.PickTool", "autoPick2",
					ParameterTypes, ParameterValues, 2);
			if (o != null) {
				bvos = (TbGeneralBVO[]) o;
			}
		} catch (Exception e) {
			StringBuffer pickMsg=new StringBuffer();//拣货信息
			if(e instanceof StockException){
				StockException se=(StockException) e;	
				bvos=(TbGeneralBVO[]) se.getBvos();		
				if(bvos!=null || bvos.length>0){
				    for(int i=0;i<bvos.length;i++){
				    	String msg=bvos[i].getVnote();
				    	if(msg!=null && msg.length()>0){
				    		pickMsg.append(" 表体行第 "+(i+1)+" 行  " +msg+" \n\n");
				    	}
				    }
				}
				if(pickMsg.toString()!=null && pickMsg.toString().length()>0){
					ui.showErrorMessage(pickMsg.toString());
				}
				
			}else{
			  throw e;
			}
		}
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bvos);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
        //设置行号
        BillRowNo.addLineRowNos(getBillCardPanelWrapper().getBillCardPanel(), getUIController().getBillType(), "crowno", bvos.length-1);

	}
	
	
	public void chaneColor() throws Exception {
		TbGeneralBVO[] generalbVO = (TbGeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
		// 获取并判断表体是否有值，进行循环
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
				// 红色：没有实发数量；蓝色：有实发数量但是数量不够；白色：实发数量与应发数量相等
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"nshouldoutassistnum"));// 应发辅数量
				UFDouble tatonum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"noutassistnum"));// 实发辅数量
				//yf add
				//实发小于应发：红色
				if(tatonum.sub(num, 8).doubleValue() > 0) {
					String[] changFields = {"nshouldoutassistnum","nshouldoutnum","noutassistnum","noutnum"};
					for (String field : changFields) {getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setCellForeGround(i,field, Color.red);
					}					
				}
 				}				
				//yf end 
//				if (tatonum.doubleValue() == 0) {
//					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().
//							.setCellBackGround(i, "ccunhuobianma", Color.red);
//				} else {
//					if (tatonum.sub(num, 8).doubleValue() == 0) {
//						getBillCardPanelWrapper().getBillCardPanel()
//								.getBodyPanel().setCellBackGround(i,
//										"ccunhuobianma", Color.white);
//					} else if (tatonum.sub(num, 8).doubleValue() < 0) {
//						getBillCardPanelWrapper().getBillCardPanel()
//								.getBodyPanel().setCellBackGround(i,
//										"ccunhuobianma", Color.blue);
//					}
//				}

			}

		}

	

//	private void changeColor(TbGeneralBVO[] bodys,Map<String, Integer> retInfor){
//		int row = 0;
//		for(TbGeneralBVO body:bodys){
//			int flag = PuPubVO.getInteger_NullAs(retInfor.get(body.getGeb_cinvbasid()), 0);
//			if(flag == -1){
//				getBillCardPanelWrapper().getBillCardPanel()
//				.getBodyPanel().setCellBackGround(row,
//						"invcode", Color.red);
//			}else if(flag == 0){
//				getBillCardPanelWrapper().getBillCardPanel()
//				.getBodyPanel().setCellBackGround(row,
//						"invcode", Color.white);
//				setCardPanelBodyValue(row, "geb_banum", getCardPanelBodyValue(row, "geb_bsnum"));
//				setCardPanelBodyValue(row, "geb_anum", getCardPanelBodyValue(row, "geb_snum"));
//			}else{
//				getBillCardPanelWrapper().getBillCardPanel()
//				.getBodyPanel().setCellBackGround(row,
//						"invcode", Color.white);
//			}
//			row ++;
//		}
//	}
	
	
	private Object getCardPanelBodyValue(int rowIndex, String strKey){
		return getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(rowIndex, strKey);
	}
	
	private void setCardPanelBodyValue(int row,String key,Object oValue){
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(oValue, row, key);
	}
	
	protected MutiInPubClientUI getBillInPubUI() {
		return (MutiInPubClientUI)getBillManageUI();
	}
	@Override
	public void onBillRef() throws Exception {		
		super.onBillRef();	   
		setInType();
		if(getBillUI().getBillOperate() == IBillOperate.OP_REFADD){
			BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc");
			if(item.getValueObject() == null){
				String  cargdoc=((MutiChildForInUI )getBillUI()).getLoginInforHelper().getSpaceByLogUserForStore(_getOperator());		
				item.setValue(cargdoc);
			}
			getBillInPubUI().afterHeadCargDoc(item.getValueObject());
		}		
				
	}
	//批量验证批次号是否正确mlr
	protected boolean validateBachCode(){		
		int num = getBillCardPanelWrapper().getBillCardPanel().getBillTable("tb_general_b").getRowCount();
		for (int i = 0; i < num; i++) {
			String va = (String) getBillCardPanelWrapper().getBillCardPanel().getBillModel("tb_general_b")
			.getValueAt(i, "geb_vbatchcode");
			if (!validateBachCode(va)) {
				return false;
			}
		}
		return true;
	}
	//验证批次号是否正确 va为要验证的批次号mlr
	private boolean validateBachCode(String va){		
			if (va == null || va.equalsIgnoreCase("")) {
				return false;
			}
			if (va.trim().length() < 8) {
				return false;
			}
			Pattern p = Pattern.compile(
							"^((((1[6-9]|[2-9]\\d)\\d{2})(0?[13578]|1[02])(0?[1-9]|[12]\\d|3[01]))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})(0?[13456789]|1[012])(0?[1-9]|[12]\\d|30))|"
									+ "(((1[6-9]|[2-9]\\d)\\d{2})0?2(0?[1-9]|1\\d|2[0-8]))|"
									+ "(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))0?229))$",
							Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(va.trim().substring(0, 8));
			if (!m.find()) {
				return false;
			}	
		return true;
	}
	
	private void setHeadPartEdit(boolean flag){
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cwarehouseid").setEdit(flag);
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setEdit(flag);
	}	
     
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		//		zhf add
		setHeadPartEdit(false);
		//修改封掉自动拣货按钮
		getButtonManager().getButton(ISsButtun.Zdrk).setEnabled(false);
		
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
	}	
		
	//如果 参照的入库仓库为空 设置默认仓库为当前保管员仓库
	protected void setInitWarehouse(String warehouseid) throws Exception{
		BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(warehouseid);
		if(null != item && item.getValueObject() == null){
			String  geh_cwarehouseid = ((MutiInPubClientUI )getBillUI()).getLoginInforHelper().getCwhid(_getOperator());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(warehouseid, geh_cwarehouseid);
		}
	}
	
	
	private void setInType() throws BusinessException{
		//		设置默认收发类别

		// 优先考虑是否虚拟
		UFBoolean isxn = PuPubVO.getUFBoolean_NullAs(getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("isxnap").getValueObject(),
				UFBoolean.FALSE);
		String outintype = null;
		if (isxn.booleanValue()) {
			outintype = OutInSetHelper.getDefaultOutInTypeID(
					Wds2WlPubConst.virtual, false);
		} else

			outintype = OutInSetHelper
			.getDefaultOutInTypeID(getBillCardPanelWrapper()
					.getBillCardPanel().getBillModel(), "geb_customize9",
					false);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
				"geh_cdispatcherid", outintype);
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn ==  ButtonCommon.joinup)
			onJoinQuery();
		else
			super.onBoElse(intBtn);
	}

}
