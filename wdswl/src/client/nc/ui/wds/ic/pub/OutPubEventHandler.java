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
//	//Ȩ�޹��˲�ѯ������
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
		///zpm start//�����к�
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
	 * ���ÿ�����
	 * @throws Exception 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-23����01:12:05
	 */
	private void setStock() throws Exception{	
		//ֻ�������������ִ���
		String pk_h=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getPrimaryKey());
		if(pk_h!=null)
			return;
		int rowcount=getBodyRowCount();	
		//�����ִ�����ѯά��
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
	    //�����úõ�ά�� ��ѯ�ִ���
	    StockInvOnHandVO[] nvos=(StockInvOnHandVO[]) getStock().queryStockCombinForClient(vos);
			 
		//�����ִ��� ��ui	 
		if(nvos==null || nvos.length==0)
				 return;
		   for(int i=0;i<rowcount;i++){
		    	StockInvOnHandVO vo=nvos[i];
		    	if(vo==null)
		    	    continue;
				UFDouble uf1=PuPubVO.getUFDouble_NullAsZero(vo.getWhs_stocktonnage());//���������
				UFDouble uf2=PuPubVO.getUFDouble_NullAsZero(vo.getWhs_stockpieces());//��渨����
				UFDouble uf3=PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "noutnum"));//ʵ��������
				UFDouble uf4=PuPubVO.getUFDouble_NullAsZero(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "noutassistnum"));//ʵ��������			
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt((uf1.sub(uf3)).toString(), i, "vuserdef2");
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt((uf2.sub(uf4)).toString(), i, "vuserdef5");					
		}			 
	
	}


	/**
	 * ����ǰ��������У��
	 * @throws Exception 
	 * @throws BusinessException 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-13����07:33:33
	 */
	private void valudate() throws BusinessException, Exception {
		if( getBillUI().getVOFromUI().getChildrenVO()!=null){
			TbOutgeneralBVO[] tbs=(TbOutgeneralBVO[]) getBillUI().getVOFromUI().getChildrenVO();
			for(int i=0;i<tbs.length;i++){
				//У����ǩ����    ���ܴ���  ʵ������
				UFDouble u1=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutassistnum());//ʵ������
				UFDouble u2=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNtagnum());//��ǩ����
				if(u1.sub(u2).doubleValue()<0){
					throw new BusinessException("��ǩ����   ���ܴ��� ʵ������");
				}	
				//У��Ӧ������ ����С��ʵ������
				UFDouble u3=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNshouldoutnum());//Ӧ������
				UFDouble u4=PuPubVO.getUFDouble_NullAsZero(tbs[i].getNoutnum());//ʵ������
				if(u3.sub(u4).doubleValue()<0){
					throw new BusinessException("Ӧ������   ����С��  ʵ������");
				}
				//У���������ڲ���Ϊ�� 
				String  date=PuPubVO.getString_TrimZeroLenAsNull(tbs[i].getVuserdef7());
				if(date==null){
					throw new Exception("�������ڲ���Ϊ��");
				}
			}			
		}
		
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
	    	return;
//	    	throw new Exception("�ֿ�Ϊ��");
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
	
	public void setOutType() throws BusinessException{
//		����Ĭ���շ����
		String outintype = OutInSetHelper.getDefaultOutInTypeID(
				getBillCardPanelWrapper().getBillCardPanel().getBillModel(), 
				"csourcetype", true);
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("cdispatcherid", outintype);	
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		
//		����Ĭ���շ����
		setOutType();
	}
	
	public void onBillRef() throws Exception {
		super.onBillRef();
		
//		����Ĭ���շ����
		setOutType();	
	}
	
	protected void onBoElse(int intBtn) throws Exception {
		if(intBtn ==  ButtonCommon.joinup)
			onJoinQuery();
		else
			super.onBoElse(intBtn);
	}
}
