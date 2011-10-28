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
	
	//��ǰ��Ӧ������--����ʹ��
	private UFDouble nshoutnum = new UFDouble(0);
	//��ǰӦ��������--����ʹ��
	private UFDouble nassshoutnum = new UFDouble(0);
	//�Ƿ����һ��
	private boolean flastLine = false;
	/*
	 * �Զ�ȡ��(non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	protected void onzdqh() throws Exception {
		int results = ui.showOkCancelMessage("ȷ���Զ����?");
		if (results != 1) {
			return;
		}
		if (getBillUI().getVOFromUI() == null|| getBillUI().getVOFromUI().getChildrenVO() == null|| getBillUI().getVOFromUI().getChildrenVO().length == 0) {ui.showErrorMessage("��ȡ��ǰ�������ݳ���.");
			return;
		}
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] generalbVOs = (TbOutgeneralBVO[]) billvo.getChildrenVO();
		// ����У�� begin
		WdsWlPubTool.validationOnPickAction(getBillCardPanelWrapper().getBillCardPanel(), generalbVOs);
		// ����У��end
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
			// ����Ϣͬ��������
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * �����ɺ󣬸������κŲ���
	 * @ʱ�䣺2011-6-17����08:48:24
	 * @param trayInfor
	 * @return
	 * @throws Exception
	 */
	private Map<String,List<TbOutgeneralTVO>> splitLine(Map<String,List<TbOutgeneralTVO>> trayInfor) throws Exception{
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) billvo
				.getChildrenVO();
		if(bodys == null && bodys.length ==0) return null;
		Integer crowno =10;	//��ʼ�к�
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
						noutnum = noutnum.add(PuPubVO.getUFDouble_NullAsZero(v.getNoutnum()));// ʵ������
						nassistnum = nassistnum.add(PuPubVO.getUFDouble_NullAsZero(v.getNoutassistnum()));// ʵ��������
					}
					lmap.put(""+crowno, list3);
					//������
					TbOutgeneralBVO vo =(TbOutgeneralBVO) getSelectedBodyVO(row);//������	
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
	 * @���ߣ�
	 * @˵�������ɽ������Ŀ 
	 * �������κź�������ص�������Ϣ
	 * @ʱ�䣺2011-6-24����08:24:44
	 * @param va
	 * @param row
	 * @throws Exception
	 */
	protected  void setDateInfor(String va,int row) throws Exception{
		UFDate date =_getDate();
	    //������κ������ʽ��ȷ�͸��������ڸ�ֵ
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
		//��ʧЧ�����ڸ�ֵ
		String cinventoryid = (String)getBillManageUI().getBillCardPanel().getBodyValueAt(row, "cinventoryid");		
			if(cinventoryid == null) 
				return;
			InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
			Integer num = vo.getQualitydaynum();//����������
			UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
			if(b!=null && b.booleanValue()){
				getBillManageUI().getBillCardPanel().setBodyValueAt(date.getDateAfter(num), row, "cshixiaoriqi");//ʧЧ����
			}	
	}
	//zpm--end

	/**
	 * tT
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-4-20����09:09:55
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

	// �������ݽ�����Ϻ� ����Ĭ��ֵ
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
		///zpm start//�����к�
	    BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),WdsWlPubConst.BILLTYPE_OTHER_OUT, "crowno");
		//zpm end
	}

	// ���帳��λ
	protected void setBodySpace() throws BusinessException {
		String pk_cargdoc = getPk_cargDoc();
		if (pk_cargdoc == null || "".equals(pk_cargdoc)) {
			// throw new BusinessException("��ָ����λ��Ϣ");
			return;
		}
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		for (int i = 0; i < row; i++) {
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setValueAt(pk_cargdoc, i, "cspaceid");
			//liuys add �������� ����Ĭ��ҵ������
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getDate(), i, "dbizdate");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulasByKey("cspaceid");
		}
	}

	// ��ͷ��ȡ�����λ
	protected String getPk_cargDoc() {
		String pk_cargdoc = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		return pk_cargdoc;
	}

	// ��ͷ��ȡ����ֿ�
	protected String getCwhid() {
		String cwhid = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("srl_pk").getValueObject();
		return cwhid;
	}

	/*
	 * ��ѯ��ϸ
	 */
	protected void onckmx() throws Exception {
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(
				WdsWlPubConst.DLG_OUT_TRAY_APPOINT, ui._getOperator(), ui
						._getCorp().getPrimaryKey(), ui, false);
		
		tdpDlg.showModal();
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-6-14����09:43:54
	 * @throws Exception
	 */
	public void chaneColor() throws Exception {
		TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
		OutPubClientUI ui= (OutPubClientUI)getBillUI();
		Map<String,List<TbOutgeneralTVO>> tmap=ui.getTrayInfor();//�к�  ������Ϣ
		// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"nshouldoutassistnum"));// Ӧ��������
				UFDouble tatonum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"noutassistnum"));// ʵ��������
				//yf add
				//ʵ��С��Ӧ������ɫ
				if(tatonum.sub(num, 8).doubleValue() < 0) {
					String[] changFields = {"nshouldoutassistnum","nshouldoutnum","noutassistnum","noutnum"};
					for (String field : changFields) {getBillCardPanelWrapper().getBillCardPanel().getBodyPanel().setCellForeGround(i,field, Color.red);
					}					
				}
				if(tmap.containsKey(generalbVO[i].getCrowno())){
					generalbVO[i].setTrayInfor(tmap.get(generalbVO[i].getCrowno()));
				}			
				//������:��ɫ add mlr
 				List<TbOutgeneralTVO>  trays=generalbVO[i].getTrayInfor();//��ȡ������Ϣ
 				if(trays==null || trays.size()==0){
 					return;
 				}
 				for(TbOutgeneralTVO tvo:trays){
 					//��ѯ������״̬������
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
					CircularlyAccessibleValueObject vo = getSelectedBodyVO(i);//����ճ����
					onBoPaste(vo);
				}
			}
		}
	}
	//���Ƶ�ǰ��
	public CircularlyAccessibleValueObject getSelectedBodyVO(int row){
		CircularlyAccessibleValueObject vo = getBillManageUI().getBillCardPanel().getBillModel()
		.getBodyValueRowVO(row, getBillManageUI().getUIControl().getBillVoName()[2]);
		return vo;
	}
	//ճ����ǰ�е���βǰ����
	protected void processCopyedBodyVOsBeforePaste(CircularlyAccessibleValueObject vo) {
		if (vo == null)
			return;
		vo.setAttributeValue(getUIController().getPkField(), null);
		vo.setAttributeValue(getUIController().getChildPkField(), null);
	}
	//ճ��
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
	//�õ������е��к�
	protected String getLastLineNO(){
		int selectedRow = getBillManageUI().getBillCardPanel().getBillTable().getRowCount()-1;
		String crowno  = (String)getBillManageUI().getBillCardPanel().getBillModel().getValueAt(selectedRow, "crowno");
		return crowno;
	}
	//ִ�й�ʽ
	private void execLoadBodyRowFormula(int intRow){
		try{
			getBillManageUI().getBillCardPanel().getBillModel().execEditFormulas(intRow);
		}catch (Exception ex){
			System.out.println("BillListWrapper:ִ���й�ʽ�������ݴ���");
			ex.printStackTrace();
		}
	}
	/*
	 * ����ָ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#ontpzd()
	 */
	protected int ontpzd() throws Exception {
		String pk_cargdoc = getPk_cargDoc();// ��λ
		if (pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("��ָ����λ��Ϣ");
		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[]) aggvo.getChildrenVO();
		if (bvo == null || bvo.length == 0)
			throw new BusinessException("û�б������ݣ����������! ");
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
								"crowno");// �к�
				UFDouble[] d = getDFromBuffer(crowno);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[0], i, "noutnum");// ������
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[1], i, "noutassistnum");// ������
				List<TbOutgeneralTVO> list = map.get(crowno);
				if (list == null || list.size() <= 0) {
					return;
				}
				// ��ȡ���κ�
				// ���κ�
				String vbachcode = "";
				// ��Դ���κ�
				String svbachcode = "";
				// ��ȡ����
				List vbp = new ArrayList();
				// ��ȡ��Դ����
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
					// ����������κŸ�ֵ
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									vbachcode.substring(1, vbachcode.length()),
									i, "vbatchcode");
				}

				if (svbachcode.length() > 1) {
					// ���������Դ���κŸ�ֵ
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
		d[0] = new UFDouble(0);// ʵ������
		d[1] = new UFDouble(0);// ʵ�븨����
		List<TbOutgeneralTVO> list = ui.getTrayInfor().get(crowno);
		if (list != null && list.size() > 0) {
			for (TbOutgeneralTVO l : list) {
				UFDouble b = l.getNoutnum();
				UFDouble b1 = l.getNoutassistnum();// ʵ�븨����
				d[0] = d[0].add(b);
				d[1] = d[1].add(b1);
			}
		}
		return d;
	}	
	/**
	 * @author yf
	 * ����� �Ե�ǰ�û�����Ȩ��У��
	 * �ܲ� ����Ա isMaster = true 
	 * �ֲ� ����Ա
	 * �����ֶ��Ƿ���Ա༭�������ֹܲ���Ա���ܱ༭
	 * @throws Exception 
	 */
	protected void setInitByWhid(String[] fields) throws Exception{
		//����Ȩ��У��
		//У���½�� �Ĳֿ�Ȩ�ޣ��ֹܲ���Ա���ֲֹ���Ա
		boolean isMaster = false;
		String whid = "";
		LoginInforVO liv = new LoginInforHelper().getLogInfor(_getOperator());
		whid = liv.getWhid();
		if(WdsWlPubConst.WDS_WL_ZC.equalsIgnoreCase(whid)){
			isMaster = true;
		}
		String  pk_stordoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk").getValueObject());
	    if(pk_stordoc==null ||pk_stordoc.length()==0){
	    	throw new Exception("�ֿ�Ϊ��");
	    }
		if(!pk_stordoc.equalsIgnoreCase(getLoginInfoHelper().getCwhid(_getOperator()))){
	    	 throw new Exception("��ǰ����Ա  δ�� ��ǰ �����յ������˵��ĳ���ֿ�");
	    }else{
	         //��ǰ���Ա�󶨵Ļ�λ
    	     getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").setValue(getLoginInfoHelper().getSpaceByLogUserForStore(_getOperator()));
		    //���òֿ⣬��λ�Ƿ�ɱ༭�������ֹ���Ա����Ա༭�����򲻿��Ա༭
		    for(String field : fields){
			 getBillCardPanelWrapper().getBillCardPanel().getHeadItem(field).setEnabled(isMaster);
		    }
	    }	
	}
	
	//��� ���յĳ���ֿ�Ϊ�� ����Ĭ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
	protected void setInitWarehouse(String warehouseid) throws Exception{
		BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(warehouseid);
		if(null != item && item.getValueObject() == null){
			String  geh_cwarehouseid = ((MutiChildForOutInUI) getBillUI()).getLoginInforHelper().getCwhid(_getOperator());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(warehouseid, geh_cwarehouseid);
		}
	}
}
