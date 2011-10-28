package nc.ui.wds.ic.pub;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.so.out.TrayDisposeDlg;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.LoginInforVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class OutPubEventHandler extends WdsPubEnventHandler {

	public OutPubClientUI ui = null;
	private LoginInforHelper login=null;

	public OutPubEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
		ui = billUI;
	}

	private LoginInforHelper getLoginInfoHelper(){
		if(login==null){
			login=new LoginInforHelper();
		}
		return login;
	}
	
	//当前行应发数量--拆行使用
	private UFDouble nshoutnum = new UFDouble(0);
	//当前应发辅数量--拆行使用
	private UFDouble nassshoutnum = new UFDouble(0);
	//是否最后一行
	private boolean flastLine = false;
	/*
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
		Map<String, List<TbOutgeneralTVO>> trayInfor = null;
		// ui.showProgressBar(true);
		try {
			Class[] ParameterTypes = new Class[] { String.class,
					AggregatedValueObject.class, String.class };
			Object[] ParameterValues = new Object[] { ui._getOperator(),
					billvo, pk_stordoc };
			Object o = LongTimeTask.callRemoteService(
					WdsWlPubConst.WDS_WL_MODULENAME,
					"nc.bs.wds.w8004040204.W8004040204Impl", "autoPickAction",
					ParameterTypes, ParameterValues, 2);
			if (o != null) {
				trayInfor = (Map<String, List<TbOutgeneralTVO>>) o;
			}
		} catch (Exception e) {
			throw e;
		}
		if (null == trayInfor || trayInfor.size() == 0) {
			chaneColor();
			return;
		}
		trayInfor = splitLine(trayInfor);
		Map<String, List<TbOutgeneralTVO>> oldInfor = ui.getTrayInfor();
		ui.setTrayInfor(trayInfor);
		if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			// 将信息同步到缓存
			TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) getBufferData()
					.getCurrentVO().getChildrenVO();
			if (bodys != null && bodys.length != 0) {
				String key = null;
				for (TbOutgeneralBVO body : bodys) {
					key = body.getCrowno();
					body.setTrayInfor(trayInfor.get(key));
				}
			}
		}
		
		int ret = ontpzd();
		if(ret != UIDialog.ID_OK){
			ui.setTrayInfor(oldInfor);
			return;
		}	

    	chaneColor();
		setBodyModelState();
//		ontpzd();
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 拣货完成后，根据批次号拆行
	 * @时间：2011-6-17下午08:48:24
	 * @param trayInfor
	 * @return
	 * @throws Exception
	 */
	private Map<String,List<TbOutgeneralTVO>> splitLine(Map<String,List<TbOutgeneralTVO>> trayInfor) throws Exception{
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) billvo
				.getChildrenVO();
		if(bodys == null && bodys.length ==0) return null;
		Integer crowno =10;	//初始行号
		String key = null;
		ArrayList<TbOutgeneralBVO> newBodys = new ArrayList<TbOutgeneralBVO>();
		Map<String,List<TbOutgeneralTVO>> lmap = new HashMap<String,List<TbOutgeneralTVO>>();
		for(TbOutgeneralBVO body:bodys){
			flastLine = false;
			nshoutnum = PuPubVO.getUFDouble_NullAsZero(body.getNshouldoutnum());
			nassshoutnum =PuPubVO.getUFDouble_NullAsZero( body.getNshouldoutassistnum());
			key = body.getCrowno();
			List<TbOutgeneralTVO> list = trayInfor.get(key);
			int row = geLineRowByCrowno(key);
			Map<String,ArrayList<TbOutgeneralTVO>> map = new HashMap<String,ArrayList<TbOutgeneralTVO>>();
			ArrayList<String> vbatchCode = new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				String code = list.get(i).getVbatchcode();
				if(map.containsKey(code)){
					map.get(code).add(list.get(i));
				}else{
					ArrayList<TbOutgeneralTVO> list2 = new ArrayList<TbOutgeneralTVO>();
					list2.add(list.get(i));
					map.put(code, list2);
					vbatchCode.add(code);
				}
			}
			if(map.size()>0){
				for(int i=0;i<map.size();i++){
					String key2 = vbatchCode.get(i);
					if(i == map.size()-1){
						flastLine=true;
					}
					ArrayList<TbOutgeneralTVO> list3 = map.get(key2);
					UFDouble noutnum = new UFDouble(0);
					UFDouble nassistnum = new UFDouble(0);
					for(TbOutgeneralTVO v:list3){
						noutnum = noutnum.add(PuPubVO.getUFDouble_NullAsZero(v.getNoutnum()));// 实出数量
						nassistnum = nassistnum.add(PuPubVO.getUFDouble_NullAsZero(v.getNoutassistnum()));// 实出辅数量
					}
					lmap.put(""+crowno, list3);
					//复制行
					TbOutgeneralBVO vo =(TbOutgeneralBVO) getSelectedBodyVO(row);//复制行	
					if(flastLine){
						vo.setNoutnum(noutnum);
						vo.setNoutassistnum(nassistnum);
						vo.setNshouldoutnum(nshoutnum);
						vo.setNshouldoutassistnum(nassshoutnum);
					}else{
						vo.setNoutnum(noutnum);
						vo.setNoutassistnum(nassistnum);
						vo.setNshouldoutnum(noutnum);
						vo.setNshouldoutassistnum(nassistnum);
						nshoutnum = nshoutnum.sub(noutnum);
						nassshoutnum = nassshoutnum.sub(nassistnum);
					}
					vo.setCrowno(""+crowno);
					newBodys.add(vo);
					crowno=crowno+10;
				}
			}
		}
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(newBodys.toArray(new TbOutgeneralBVO[0]));
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
		return lmap;
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
		super.setRefData(vos);
		getBillUI().setDefaultData();
		setBodySpace();
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		setBodySpace();
		///zpm start//生成行号
	    BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),WdsWlPubConst.BILLTYPE_OTHER_OUT, "crowno");
		//zpm end
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

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-6-14下午09:43:54
	 * @throws Exception
	 */
	public void chaneColor() throws Exception {
		TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
		OutPubClientUI ui= (OutPubClientUI)getBillUI();
		Map<String,List<TbOutgeneralTVO>> tmap=ui.getTrayInfor();//行号  托盘信息
		// 获取并判断表体是否有值，进行循环
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// 获取当前表体行的应发辅数量和实发辅数量进行比较，根据比较结果进行颜色显示
				// 红色：没有实发数量；蓝色：有实发数量但是数量不够；白色：实发数量与应发数量相等
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"nshouldoutassistnum"));// 应发辅数量
				UFDouble tatonum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"noutassistnum"));// 实发辅数量
				//yf add
				//实发小于应发：红色
				if(tatonum.sub(num, 8).doubleValue() < 0) {
					String[] changFields = {"nshouldoutassistnum","nshouldoutnum","noutassistnum","noutnum"};
					for (String field : changFields) {getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setCellForeGround(i,field, Color.red);
					}					
				}
				if(tmap.containsKey(generalbVO[i].getCrowno())){
					generalbVO[i].setTrayInfor(tmap.get(generalbVO[i].getCrowno()));
				}			
				//大日期:蓝色 add mlr
 				List<TbOutgeneralTVO>  trays=generalbVO[i].getTrayInfor();//获取托盘信息
 				if(trays==null || trays.size()==0){
 					return;
 				}
 				for(TbOutgeneralTVO tvo:trays){
 					//查询大日期状态的主键
 	 				String whs_pk= PuPubVO.getString_TrimZeroLenAsNull(WdsWlPubTool.execFomularClient("ss_pk->getColValue(tb_warehousestock,ss_pk,pplpt_pk,pk)", new String[]{"pk"}, new String[]{tvo.getCdt_pk()}));		
 					if(WdsWlPubConst.WDS_STORSTATE_PK.equalsIgnoreCase(whs_pk)){
 						String[] changFields = {"nshouldoutassistnum","nshouldoutnum","noutassistnum","noutnum"};
 						for (String field : changFields) {
 						  getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setCellForeGround(i,field, Color.BLUE);
 						}		
 					}
 				}
			}
		}
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
		super.onBoSave();
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
	/*
	 * 托盘指定(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#ontpzd()
	 */
	protected int ontpzd() throws Exception {
		String pk_cargdoc = getPk_cargDoc();// 货位
		if (pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("请指定货位信息");
		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[]) aggvo.getChildrenVO();
		if (bvo == null || bvo.length == 0)
			throw new BusinessException("没有表体数据，不允许操作! ");
		for (TbOutgeneralBVO vo : bvo) {
			vo.validationOnZdck();
		}
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(
				WdsWlPubConst.DLG_OUT_TRAY_APPOINT, ui._getOperator(), ui
						._getCorp().getPrimaryKey(), ui, true);
		int retflag = UIDialog.ID_CANCEL;
		if (tdpDlg.showModal() == UIDialog.ID_OK) {
			Map<String, List<TbOutgeneralTVO>> map2 = tdpDlg.getBufferData();
			ui.setTrayInfor(map2);
			Map<String,SmallTrayVO[]> lockTrayInfor = tdpDlg.getTrayLockInfor(false);
			ui.setLockTrayInfor(lockTrayInfor);
			setBodyValueToft();
			retflag = UIDialog.ID_OK;
		}
		chaneColor();
		setBodyModelState();
		return retflag;
	}

	public void setBodyValueToft() {
		int row = getBodyRowCount();
		Map<String, List<TbOutgeneralTVO>> map = ((OutPubClientUI) getBillUI())
				.getTrayInfor();
		if (row > 0) {
			for (int i = 0; i < row; i++) {
				String crowno = (String) getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"crowno");// 行号
				UFDouble[] d = getDFromBuffer(crowno);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[0], i, "noutnum");// 主数量
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[1], i, "noutassistnum");// 辅数量
				List<TbOutgeneralTVO> list = map.get(crowno);
				if (list == null || list.size() <= 0) {
					return;
				}
				// 获取批次号
				// 批次号
				String vbachcode = "";
				// 来源批次号
				String svbachcode = "";
				// 存取批次
				List vbp = new ArrayList();
				// 存取来源批次
				List vbl = new ArrayList();
				for (int j = 0; j < list.size(); j++) {
					TbOutgeneralTVO vo = list.get(j);
					if (vo.getVbatchcode() != null
							&& !vo.getVbatchcode().equalsIgnoreCase("")) {
						if (!vbp.contains(vo.getVbatchcode())) {
							vbachcode = vbachcode + "," + vo.getVbatchcode();
							vbp.add(vo.getVbatchcode());
						}
					}
					if (vo.getLvbatchcode() != null
							&& !vo.getLvbatchcode().equalsIgnoreCase("")) {
						if (!vbl.contains(vo.getVbatchcode())) {
							svbachcode = svbachcode + "," + vo.getLvbatchcode();
							vbl.add(vo.getLvbatchcode());
						}
					}
				}
				if (vbachcode.length() > 1) {
					// 给界面的批次号赋值
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									vbachcode.substring(1, vbachcode.length()),
									i, "vbatchcode");
				}

				if (svbachcode.length() > 1) {
					// 给界面的来源批次号赋值
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									svbachcode
											.substring(1, svbachcode.length()),
									i, "lvbatchcode");
				}
			}
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

	public UFDouble[] getDFromBuffer(String crowno) {
		UFDouble[] d = new UFDouble[2];
		d[0] = new UFDouble(0);// 实入数量
		d[1] = new UFDouble(0);// 实入辅数量
		List<TbOutgeneralTVO> list = ui.getTrayInfor().get(crowno);
		if (list != null && list.size() > 0) {
			for (TbOutgeneralTVO l : list) {
				UFDouble b = l.getNoutnum();
				UFDouble b1 = l.getNoutassistnum();// 实入辅数量
				d[0] = d[0].add(b);
				d[1] = d[1].add(b1);
			}
		}
		return d;
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
	    	throw new Exception("仓库为空");
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
}
