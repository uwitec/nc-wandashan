package nc.ui.wds.ic.pub;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.other.in.TrayDisposeDlg;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.bd.invdoc.InvmandocVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.WdsWlPubConst;

public abstract class InPubEventHandler extends WdsPubEnventHandler {

	public InPubClientUI ui = null;
	
	
	public InPubEventHandler(InPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
		ui = billUI;
	}
	
	public void onBoAdd(ButtonObject bo) throws Exception {
		super.onBoAdd(bo);
		setHeadPartEdit(true);
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		setBodySpace();
	}
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		super.setRefData(vos);
		getBillManageUI().setDefaultData();
		setBodySpace();//���帳Ĭ��ֵ
		getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}
	//���帳��λ
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
	 * ȡ��ǩ��
	 */
	protected void onQxqz() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("����ִ��ȡ��ǩ��...");
				int retu = getBillManageUI().showOkCancelMessage("ȷ��ȡ��ǩ��?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbGeneralHVO tbGeneralHVOss = (TbGeneralHVO)aObject.getParentVO();
				if(tbGeneralHVOss.getFisload() != null &&tbGeneralHVOss.getFisload().equals(UFBoolean.TRUE)){
					getBillUI().showWarningMessage("�Ѿ��γ�װж�Ѻ��㵥������ȡ��ǩ��");
					return ;
				}
//				if(tbGeneralHVOss.getFistran() != null &&tbGeneralHVOss.getFistran().equals(UFBoolean.TRUE)){
//					getBillUI().showWarningMessage("�Ѿ��γ��˷Ѻ��㵥������ȡ��ǩ��");
//					return ;
//				}
				tbGeneralHVOss.setGeh_storname(null);// �ⷿǩ����
				tbGeneralHVOss.setTaccounttime(null);// ǩ��ʱ��
				tbGeneralHVOss.setClastmodiid(_getOperator());// ����޸���
				tbGeneralHVOss.setClastmodedate(ui._getDate());// ����޸�������
				tbGeneralHVOss.setClastmodetime(ui._getServerTime().toString());// ����޸�ʱ��
				tbGeneralHVOss.setPwb_fbillflag(IBillStatus.FREE);// ����״̬
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//��¼��
				//�����ű�
				PfUtilClient.processAction(getBillManageUI(), "CANELSIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				getBillManageUI().showHintMessage("ȡ��ǩ�����");
				//���»���
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("ȡ��ǩ��ʧ�ܣ�");
		}
		
	}

	/**
	 * ǩ��ȷ��
	 */
	protected void onQzqr() throws Exception {
		try{
			if (getBufferData().getCurrentVO()!=null){
				getBillManageUI().showHintMessage("����ִ��ǩ��...");
				int retu = getBillManageUI().showOkCancelMessage("ȷ��ǩ��?");
				if (retu != 1) {
					return;
				}
				AggregatedValueObject aObject  = getBufferData().getCurrentVOClone();
				TbGeneralHVO tbGeneralHVOss = (TbGeneralHVO)aObject.getParentVO();
				TbGeneralBVO[] bvos =(TbGeneralBVO[]) aObject.getChildrenVO();
				tbGeneralHVOss.setGeh_storname(_getOperator());// �ⷿǩ����
				tbGeneralHVOss.setTaccounttime(ui._getServerTime().toString());// ǩ��ʱ��
				tbGeneralHVOss.setClastmodiid(_getOperator());// ����޸���
				tbGeneralHVOss.setClastmodedate(ui._getDate());// ����޸�������
				tbGeneralHVOss.setClastmodetime(ui._getServerTime().toString());// ����޸�ʱ��
				tbGeneralHVOss.setPwb_fbillflag(IBillStatus.CHECKPASS);// ����״̬
				ArrayList<String> list = new ArrayList<String>();
				list.add(_getDate().toString());
				list.add(_getOperator());//��¼��
				//�����ű�
				PfUtilClient.processAction(getBillManageUI(), "SIGN", getBillManageUI().getUIControl().getBillType(), _getDate().toString(), aObject,list );
				getBillManageUI().showHintMessage("ǩ�����");
				//���»���
				onBoRefresh();
			}
		}catch(Exception e){
			Logger.error(e);
			getBillManageUI().showErrorMessage("ǩ��ʧ�ܣ�");
		}
	
	}
	
	/**
	 * ָ������
	 */
	protected int onZdtp() throws Exception {
		//У�����κ�
		if(! validateBachCode()){
		   throw new  BusinessException("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
		}
		String pk_cargdoc =  getPk_cargDoc();//��λ
		if(pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("��ָ����λ��Ϣ");
		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
		TbGeneralBVO[] bvo  = (TbGeneralBVO[])aggvo.getChildrenVO();
		if(bvo == null || bvo.length == 0)
			throw new BusinessException("���������������");
		for(TbGeneralBVO vo : bvo){
			vo.validateOnZdrk(false);
		}
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(WdsWlPubConst.DLG_IN_TRAY_APPOINT,_getOperator(), 
				_getCorp().getPrimaryKey(), null,ui,true);
		int retflag = UIDialog.ID_CANCEL;
		if(tdpDlg.showModal() == UIDialog.ID_OK){
			Map<String,List<TbGeneralBBVO>> map = tdpDlg.getBufferData();
			ui.setTrayInfor(map);
			
			Map<String,SmallTrayVO[]> lockTrayInfor = tdpDlg.getTrayLockInfor(false);
			ui.setLockTrayInfor(lockTrayInfor);
			setBodyValueToft();
			retflag = UIDialog.ID_OK;
		}
		setBackGround();
		setBodyModelState();
		return retflag;
	}
	protected String getPk_cargDoc(){
		String pk_cargdoc = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		
		return pk_cargdoc;		
	}
	
	protected String getCwhid(){
		String cwhid = (String)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("geh_cwarehouseid").getValueObject();
		return cwhid;
	}
	
	@Override
	protected void onBoLineDel() throws Exception {
		int[] rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRows();
		for(int i = 0 ;i<rows.length; i++){
			String crowno = (String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(rows[i], "geb_crowno");
			ui.getTrayInfor().remove(crowno);
		}
		super.onBoLineDel();
	}
	
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
			   throw new  BusinessException("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
		}
		AggregatedValueObject vo = ui.getChangedVOFromUI();
		TbGeneralHVO hvo = (TbGeneralHVO)vo.getParentVO();
		hvo.validateBeforSave();
		
		TbGeneralBVO[] vos = (TbGeneralBVO[])vo.getChildrenVO();
//		TbGeneralBVO[] vos = (TbGeneralBVO[])nc.vo.trade.voutils.VOUtil.filter(ovos, new filterDelLine());

		if(vos == null || vos.length ==0 && null == PuPubVO.getString_TrimZeroLenAsNull(hvo.getPrimaryKey())){
			//throw new BusinessException("���岻����Ϊ��");
		}
		if(vos != null && vos.length > 0 ){
			for(TbGeneralBVO v : vos){
				v.validateOnSave();
				List<TbGeneralBBVO> list = v.getTrayInfor();
				for(TbGeneralBBVO b : list){
					b.validateOnSave();
				}
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
	
	public void setBodyValueToft(){
		int row  = getBodyRowCount();
		if(row > 0){
			for(int i = 0 ;i < row;i++){
				String crowno = (String)getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_crowno");//�к�
				UFDouble[]  d = getDFromBuffer(crowno);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(d[0], i, "geb_anum");//������
				getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(d[1], i, "geb_banum");//������
			}
		}
	}
	public UFDouble[] getDFromBuffer(String crowno){
		UFDouble[] d = new UFDouble[2];
		d[0] = new UFDouble(0);//ʵ������
		d[1] = new UFDouble(0);//ʵ�븨����
		List<TbGeneralBBVO> list = ui.getTrayInfor().get(crowno);
		if(list != null && list.size() > 0){
			for(TbGeneralBBVO l : list){
				UFDouble b = l.getGebb_num();//ʵ������
				UFDouble b1 = l.getNinassistnum();//ʵ�븨����
				d[0] = d[0].add(b);
				d[1] = d[1].add(b1);
			}
		}
		return d;
	}
	
	public int getBodyRowCount(){
		int rowcount = -1;
		if(ui.getBillListPanel().isShowing()){
			rowcount = ui.getBillListPanel().getBodyBillModel().getRowCount();
		}else{
			rowcount = ui.getBillCardPanel().getBillModel().getRowCount();
		}
		return rowcount;
	}

	//��ɫ��û��ʵ��������
	//��ɫ����ʵ��������������������
	//��ɫ��ʵ��������Ӧ���������
	protected void setBackGround(){
		int row  = getBodyRowCount();
		if(row > 0){
			for(int i = 0 ;i < row;i++){
				UFDouble b1 = PuPubVO.getUFDouble_NullAsZero(//Ӧ��������
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_snum"));
				UFDouble b2 = PuPubVO.getUFDouble_NullAsZero(//ʵ��������
						getBillCardPanelWrapper().getBillCardPanel().getBillModel().getValueAt(i, "geb_anum"));
				//yf add
				//ʵ��С��Ӧ��:��ɫ
				if(b1.sub(b2).doubleValue() < 0 ){
					String[] changFields = {"geb_anum","geb_snum","geb_bsnum","geb_banum"};
					for (String field : changFields) {
						getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
						.setCellForeGround(i, field, Color.red);	
					}
					
				}
				//yf end
				
				
//				if(b1.sub(b2).doubleValue() > 0 ){
//					//��ɫ
//					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
//					.setCellBackGround(i, "invcode", Color.blue);	
//				}else if(b1.sub(b2).doubleValue() == b1.doubleValue()){
//					//��ɫ
//					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
//					.setCellBackGround(i, "invcode", Color.red);	
//				}else if(b1.sub(b2).doubleValue() == 0){
//					//��ɫ
//					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
//					.setCellBackGround(i, "invcode", Color.white);	
//				}
			}
		}
	}

	/**tT
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-20����09:09:55
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

	@Override
	protected void onBoLinePaste() throws Exception {
		super.onBoLinePaste();
	}

	/**
	 * �鿴��ϸ
	 */
   
	protected void onCkmx() throws Exception {
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(WdsWlPubConst.DLG_IN_TRAY_APPOINT,
				_getOperator(), _getCorp().getPrimaryKey(), null,ui,false);
		tdpDlg.showModal();
	}
	
	/**
	 * �Զ����
	 */
	protected void onZdrk() throws Exception {
		if(! validateBachCode()){
			throw new  BusinessException("���κ�����Ĳ���ȷ,����������ȷ������!�磺20100101XXXXXX");
		}
		String cwid = getCwhid();//�ֿ�
		if(cwid == null || "".equals(cwid))
			throw new BusinessException("��ָ���ֿ���Ϣ");
		String pk_cargdoc =  getPk_cargDoc();//��λ
		if(pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("��ָ����λ��Ϣ");
		TbGeneralBVO[] tbGeneralBVOs = (TbGeneralBVO[])getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodyValueVOs(TbGeneralBVO.class.getName());
		if(tbGeneralBVOs == null||tbGeneralBVOs.length ==0){
			throw new BusinessException("��������Ϊ��");
		}
		// ��֤��Ʒ����
		for (TbGeneralBVO body:tbGeneralBVOs) {
			body.validateOnZdrk(false);
		}
		//		for(TbGeneralBVO body:tbGeneralBVOs){
		//			String key = body.getGeb_crowno();
		//			if(ui.getTrayInfor().containsKey(key)){
		//				body.setTrayInfor(ui.getTrayInfor().get(key));
		//			}
		//		}
		ui.showProgressBar(true);
		Object o = null;
		try{
			o = WdsIcPubHelper.autoInStore(tbGeneralBVOs,cwid, pk_cargdoc);
		}catch(Exception e){
			throw e;
		}finally{
			ui.showProgressBar(false);
		}	

		if(o == null){
			ui.showErrorMessage("���ݴ����쳣�������²���");
			return;
		}

		//		��������ֵ
		Object[] os = (Object[])o;
		if(os == null || os.length==0){
			ui.showErrorMessage("���ݴ����쳣�������²���");
			return;
		}
		//		if(os[0]==null)
		//			return;
		if(os[1]!=null&&((Map)os[1]).size()>0){
			StringBuffer msg =new StringBuffer("�Զ�������ʧ��:\n");
			Map<String,Integer> om = (Map<String,Integer>)os[1];
			for(String key:om.keySet()){
				if(om.get(key)==0){
					msg.append(key+"�У���λ��Ÿô�����������̾���ռ��;\n");
				}else if(om.get(key)==1){
					msg.append(key+"�У�������λ��ǰ��������;\n");
				}
			}
			ui.showErrorMessage(msg.toString());
			return;
		}
		// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
		// ��ɫ��û��ʵ������-1����ɫ����ʵ������������������1����ɫ��ʵ��������Ӧ���������0
		Map<String,List<TbGeneralBBVO>> trayInfor = (Map<String,List<TbGeneralBBVO>>) os[0];
		if(trayInfor!=null&&trayInfor.size()==0)
			trayInfor = null;
		Map<String, Integer> retInfor = (Map<String, Integer>)os[1];
		Map<String,List<TbGeneralBBVO>> oldInfor = ui.getTrayInfor();
		ui.setTrayInfor(trayInfor);
		int ret = onZdtp();
		if(ret != UIDialog.ID_OK){
			ui.setTrayInfor(oldInfor);
			return;
		}		

//		changeColor(tbGeneralBVOs, retInfor);
		setBodyModelState();
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
	
	protected InPubClientUI getBillInPubUI() {
		return (InPubClientUI)getBillManageUI();
	}
	@Override
	public void onBillRef() throws Exception {		
		super.onBillRef();	   
		if(getBillUI().getBillOperate() == IBillOperate.OP_REFADD){
			BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc");
			if(item.getValueObject() == null){
				String  cargdoc=((InPubClientUI )getBillUI()).getLoginInforHelper().getSpaceByLogUserForStore(_getOperator());		
				item.setValue(cargdoc);
			}
			getBillInPubUI().afterHeadCargDoc(item.getValueObject());
		}
		
		// ��֤���κ��Ƿ���ȷ
		int num = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for(int i=0;i<num;i++){
			int row=i;
		String va=(String)getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "geb_vbatchcode");
		if( !validateBachCode(va)){
			return;
		}
	    //������κ������ʽ��ȷ�͸��������ڸ�ֵ
		String year=va.substring(0,4);
		String month=va.substring(4,6);
		String day=va.substring(6,8);
		String startdate=year+"-"+month+"-"+day;
		UFDate date=new UFDate(startdate);
		getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(date, row, "geb_proddate");
		
		//��ʧЧ�����ڸ�ֵ
		String cinventoryid = (String)getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "geb_cinventoryid");		
			if(cinventoryid == null) 
				return;
			InvmandocVO vo = (InvmandocVO)HYPubBO_Client.queryByPrimaryKey(InvmandocVO.class, cinventoryid);
			Integer num1 = vo.getQualitydaynum();//����������
			UFBoolean b = vo.getQualitymanflag();//�Ƿ�����
			if(b!=null && b.booleanValue()){
				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(date.getDateAfter(num1), row, "geb_dvalidate");//ʧЧ����
			}			
	}	

		
		
	}
	//������֤���κ��Ƿ���ȷmlr
	protected boolean validateBachCode(){		
		int num = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		for (int i = 0; i < num; i++) {
			int row = i;
			String va = (String) getBillCardPanelWrapper().getBillCardPanel()
					.getBodyValueAt(i, "geb_vbatchcode");
			if (!validateBachCode(va)) {
				return false;
			}
		}
		return true;
	}
	//��֤���κ��Ƿ���ȷ vaΪҪ��֤�����κ�mlr
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
//		��Ŀ����	pk_cargdoc
	}	
     
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
//		zhf add
		setHeadPartEdit(false);
		getButtonManager().getButton(IBillButton.Line).setEnabled(false);
		getBillUI().updateButtons();
	}	
		
	//��� ���յ����ֿ�Ϊ�� ����Ĭ�ϲֿ�Ϊ��ǰ����Ա�ֿ�
	protected void setInitWarehouse(String warehouseid) throws Exception{
		BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(warehouseid);
		if(null != item && item.getValueObject() == null){
			String  geh_cwarehouseid = ((InPubClientUI )getBillUI()).getLoginInforHelper().getCwhid(_getOperator());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem(warehouseid, geh_cwarehouseid);
		}
	}

}
