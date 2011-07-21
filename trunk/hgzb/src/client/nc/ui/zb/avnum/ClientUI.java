package nc.ui.zb.avnum;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.DefBillManageUI;
import nc.ui.zb.pub.ZbPubHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvNumHeadVO;
import nc.vo.zb.avnum.AvVendorVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;



/**
 * ����������UI
 * 4004090510
 * 
 * @author Administrator
 * 
 */
public class ClientUI extends DefBillManageUI implements ChangeListener,BillCardBeforeEditListener {

    public HashMap<String, HYBillVO> amap = new HashMap<String,HYBillVO>();
    private ClientLink cl;
    
	public ClientUI() {
		super();
		cl = new ClientLink(ClientEnvironment.getInstance());
		initLogInfor();
	}
	
	public Object[] m_logInfor = null;// 

	private void initLogInfor() {
		try {
			m_logInfor = ZbPubHelper.getLogInfor(_getCorp().getPrimaryKey(),
					_getOperator());
		} catch (Exception e) {
			e.printStackTrace();// ��ȡ������Ϣʧ�� ����Ӱ��������
			m_logInfor = null;
		}
	}
	/**
	 * �¼��ļ��������� �������ڣ�(2003-9-15 11:03:30)
	 */
	protected void initEventListener() {
		super.initEventListener();
	getBillCardPanel().addEditListener("zb_avvendor",new AvvendorBodyListAfterListener());//��ʼ����Ӧ����Ϣҵǩ�ı༭���¼�
	}
	@Override
	protected void initSelfData() {
		
		// ����ҳǩ�л�����
		UITabbedPane m_CardUITabbedPane = getBillCardPanel().getBodyTabbedPane();
		UITabbedPane m_ListUITabbedPane = getUITabbedPane(getBillListPanel());
		m_CardUITabbedPane.addChangeListener(this);
		m_ListUITabbedPane.addChangeListener(this);
		
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);// BillItemEvent��׽����BillItemEvent�¼�
		// ��ȥ�в������ఴť
		ButtonObject btnobj = getButtonManager().getButton(IBillButton.Line);
		if (btnobj != null) {
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
			btnobj.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLinetoTail));
		}
		getBillCardPanel().setBodyMenuShow(false);//���ñ���Ĳ˵�������
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);// ����״̬
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_billtype", ZbPubConst.ZB_AVNUM_BILLTYPE);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());// �Ƶ���
		getBillCardPanel().setTailItem("dmakedate", _getDate());// �Ƶ�����
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		
		if (m_logInfor==null || m_logInfor.length==0 ) {
			showHintMessage("��ǰ�û�δ���ù���ҵ��Ա");
			return;
		}
		getBillCardPanel().setHeadItem("pk_deptdoc", m_logInfor[1]);
		getBillCardPanel().setHeadItem("vemployeeid", m_logInfor[0]);
	}

	// ����Զ��尴ť
	public void initPrivateButton() {
		// ����
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(ZbPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("����");
		btnvo4.setBtnChinaName("����");
		btnvo4.setBtnCode(null);// code�������Ϊ��
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,
				IBillOperate.OP_NO_ADDANDEDIT });
		addPrivateButton(btnvo4);
		
		// ������ѯ
		ButtonVO btnvo8 = new ButtonVO();
		btnvo8.setBtnNo(ZbPuBtnConst.ASSQUERY);
		btnvo8.setBtnName("������ѯ");
		btnvo8.setBtnChinaName("������ѯ");
		btnvo8.setBtnCode(null);// code�������Ϊ��
		btnvo8.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo8.setChildAry(new int[] { ZbPuBtnConst.LINKQUERY,IBillButton.ApproveInfo});
		addPrivateButton(btnvo8);
		
		// ��ӡ����
		ButtonVO btnvo9 = new ButtonVO();
		btnvo9.setBtnNo(ZbPuBtnConst.ASSPRINT);
		btnvo9.setBtnName("��ӡ����");
		btnvo9.setBtnChinaName("��ӡ����");
		btnvo9.setBtnCode(null);// code�������Ϊ��
		btnvo9.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo9.setChildAry(new int[] { IBillButton.Print,IBillButton.DirectPrint});
		addPrivateButton(btnvo9);
		
		//�޸�
		ButtonVO btnvo10 = new ButtonVO();
		btnvo10.setBtnNo(ZbPuBtnConst.Editor);
		btnvo10.setBtnName("�޸�");
		btnvo10.setBtnChinaName("�޸�");
		btnvo10.setBtnCode(null);// code�������Ϊ��
		btnvo10.setOperateStatus(new int[] { IBillOperate.OP_INIT,IBillOperate.OP_NOTEDIT});
		btnvo10.setBusinessStatus(new int[]{IBillStatus.FREE});
		addPrivateButton(btnvo10);
		super.initPrivateButton();
	}

	@Override
	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == getBillListPanel().getParentListPanel().getTable()) {
			getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
		}
		super.bodyRowChange(e);
		
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
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
	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator();
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		return false;
	}
	@Override
	protected String getBillNo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public String getRefBillType() {
//		// TODO Auto-generated method stub
//		return SrmBillStatus.SRMD;
//	}
	/**
	 * ���Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}
	
	/**
	 * ��ͷ�༭ǰ
	 */
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		String key = e.getItem().getKey();
		if ("vemployeeid".equalsIgnoreCase(key)) {//ҵ��Ա    ���ݲ��Ź���ҵ��Ա
			
			String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
			UIRefPane invRefPane = (UIRefPane) e.getItem().getComponent();
			AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
			
			if (deptdoc == null) {
				refModel.addWherePart(" and 1=1");
				return true;
			}
			refModel.addWherePart("  and bd_psndoc.pk_deptdoc = '" + deptdoc + "'");
			
		}
		return false;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row =e.getRow();
//		if ("invcode".equalsIgnoreCase(key)) {// ���  ���ݴ��������˴��
//			 String invcode =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invclcode"));//����������
//	         UIRefPane invRefPane = (UIRefPane) getBillCardPanel().getBodyItem("invcode").getComponent();
//	         AbstractRefModel refModel = (AbstractRefModel) invRefPane.getRefModel();
//	       
//	         if(invcode==null){
//		            refModel.addWherePart("  and 1=1 ");
//		         return true;
//	           }else{
//	  	        refModel.addWherePart(" and bd_invbasdoc.pk_invcl in (select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')"); 
//	         }
//		}
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			if("cbiddingid".equalsIgnoreCase(key)){
				getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
				getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(key));
				clearTable(getTableCodes());//�����������
				String  cbiddingid = ZbPubTool.getString_NullAsTrimZeroLen(getBillCardPanel().getHeadItem(key).getValueObject());
				if("".equalsIgnoreCase(cbiddingid) ||cbiddingid==null)
					return;
				try {
					loadAvNumBillVO(cbiddingid);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					showErrorMessage(e1.getMessage());
				}
				
			}else if("pk_deptdoc".equalsIgnoreCase(key)){//����  ���ҵ��Ա
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// ҵ��Ա  ��������
				String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null)
				    getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("vemployeeid"));
			}
		} else if (e.getPos() == BillItem.BODY) {
			
		}
		super.afterEdit(e);
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
	
	//ҳǩ�л��¼�
	public void stateChanged(ChangeEvent e) {

		int row =-1;
		
		if (isListPanelSelected()) {// //��Ƭҳǩ�б�ҳǩ
			if ("zb_avvendor".equals(getBillListPanel().getBodyTabbedPane().getSelectedTableCode())) {
				row =getBillListPanel().getBodyTable("zb_avnum_b").getSelectedRow();
				if(row<0){
					getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
					showErrorMessage("��ѡ���д����Ϣ");
					return;
				}
				String cbiddingid =PuPubVO.getString_TrimZeroLenAsNull(
						           getBillListPanel().getHeadBillModel().getValueAt(getBillListPanel().getHeadTable().getSelectedRow(),"cbiddingid"));
				AvNumBodyVO bvo =(AvNumBodyVO)getBuffer(cbiddingid).getChildrenVO()[row];
				AvVendorVO[] bbs =bvo.getAvVendorVO();
				getBillListPanel().getBodyBillModel().setBodyDataVO(bbs);//����������	
				if(bbs!=null){
					int len = bbs.length;
					for(int i=0;i<len;i++){
						if(PuPubVO.getString_TrimZeroLenAsNull(bbs[i].getAttributeValue("ccustbasid"))==null){
							String[] formulas = new String[]{
									"gysname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
									"gyscode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
									};
							getBillListPanel().getBodyBillModel().execFormulas(i, formulas);
						}else{
							String[] formulas = new String[]{
									"gysname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
									"gyscode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
									};
							getBillListPanel().getBodyBillModel().execFormulas(i, formulas);
						}
					}
				}
				Vector v = getBillListPanel().getBodyBillModel().getBillModelData();
				getBillListPanel().getBodyBillModel().setBillModelData(v);
				getBillListPanel().getBodyBillModel().execLoadFormula();
			}
		} else {// ��Ƭҳǩ
			if ("zb_avvendor".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())) {
				row = getBillCardPanel().getBillTable("zb_avnum_b").getSelectedRow();
				String cbiddingid =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cbiddingid").getValueObject());
				if(row<0){
					getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
					showErrorMessage("��ѡ���д����Ϣ");
					return;
				}
				AvNumBodyVO bvo =(AvNumBodyVO)getBuffer(cbiddingid).getChildrenVO()[row];
				AvVendorVO[] bbs =bvo.getAvVendorVO();
				getBillCardPanel().getBillModel().setBodyDataVO(bbs);//����������	
				if(bbs!=null){
					int len = bbs.length;
					for(int i=0;i<len;i++){
						if(PuPubVO.getString_TrimZeroLenAsNull(bbs[i].getAttributeValue("ccustbasid"))==null){
							String[] formulas = new String[]{
									"gysname->getColvalue(bd_cubasdochg,custname,ccubasdochgid,ccustmanid)",
									"gyscode->getColvalue(bd_cubasdochg,vbillno,ccubasdochgid,ccustmanid)"
									};
							getBillCardPanel().getBillModel().execFormulas(i, formulas);
						}else{
							String[] formulas = new String[]{
									"gysname->getColvalue(bd_cubasdoc,custname,pk_cubasdoc,ccustbasid)",
									"gyscode->getColvalue(bd_cubasdoc,custcode,pk_cubasdoc,ccustbasid)"
									};
							getBillCardPanel().getBillModel().execFormulas(i, formulas);
						}
					}
				}
				Vector v = getBillCardPanel().getBillModel().getBillModelData();
				getBillCardPanel().getBillModel().setBillModelData(v);
				getBillCardPanel().getBillModel().execLoadFormula();
			}
		}
	}
	/**
	 * ��ȡ���е�TableCode
	 */
	public String[] getTableCodes() {
		return new String[] { "zb_avnum_b", "zb_avvendor" };
	}
	
	/**
	 * @˵�� ���ݱ��� tableCode,���ҳǩ����
	 * @ʱ�� 2010-9-14����02:06:02
	 * @param tableCodes
	 */
	protected void clearTable(String[] tableCodes) {
		if (tableCodes != null && tableCodes.length > 0) {
			for (int i = 0; i < tableCodes.length; i++) {
				int count = getBillCardPanel().getBillModel(tableCodes[i]).getRowCount();
				int[] array = new int[count];
				for (int j = 0; j < count; j++) {
					array[j] = j;
				}
				getBillCardPanel().getBillData().getBillModel(tableCodes[i]).delLine(array);
			}
		}
	}
	
	//���� ����  ����  �������Ϣ
	private void loadAvNumBillVO(String cbiddingid) throws Exception{
		
		HYBillVO billvo  = amap.get(cbiddingid);
		if(billvo==null){
			billvo=AvNumHelper.loadAvNumBillVO(cbiddingid,cl);
			
			amap.put(cbiddingid, billvo);
		}
		setDatasToUI(billvo);
	}
	//ѡ�����κ� ������������
	private void setDatasToUI(HYBillVO billvo){
		if(billvo ==null)
			return;
		AvNumHeadVO ahead = (AvNumHeadVO)billvo.getParentVO();
		AvNumBodyVO[] abody = (AvNumBodyVO[])billvo.getChildrenVO();
		getBillCardPanel().getBillData().setHeaderValueVO(ahead);
		getBillCardPanel().getBillModel("zb_avnum_b").setBodyDataVO(abody);
		
		getBillCardPanel().execHeadTailEditFormulas();
		getBillCardPanel().getBillModel().execLoadFormula();
	}
	
	//������������
	public HYBillVO getBuffer(String cbiddingid){
		HYBillVO billvo =null;
		 billvo=(HYBillVO)((HYBillVO)amap.get(cbiddingid));
		 if(billvo ==null){
			 if(!getBufferData().isVOBufferEmpty())
				 billvo=(HYBillVO)getBufferData().getCurrentVO();
		 }
		return billvo;
	}
	
	//�༭���¼����»���
	private void setNum(String cbiddingid,int row,int selrow,UFDouble bzbnum,UFDouble hzbprice,UFDouble nwinpercent ){
		
		AvNumBodyVO[] bvo =(AvNumBodyVO[])getBuffer(cbiddingid).getChildrenVO();
		((AvNumBodyVO)bvo[selrow]).getAvVendorVO()[row].setNzbnum(bzbnum);
		((AvNumBodyVO)bvo[selrow]).getAvVendorVO()[row].setNzbmny(hzbprice.multiply(bzbnum));
		((AvNumBodyVO)bvo[selrow]).getAvVendorVO()[row].setNwinpercent(nwinpercent);
		getBuffer(cbiddingid).setChildrenVO(bvo);
	}
	
	//���� ��Ӧ��ҵǩ�ı༭���¼�
	class AvvendorBodyListAfterListener implements BillEditListener {

		public void afterEdit(BillEditEvent e) {
			// TODO Auto-generated method stub
			String key = e.getKey();
			int row = e.getRow();
			int selrow = getBillCardPanel().getBillTable("zb_avnum_b").getSelectedRow();
			UFDouble hzbnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel("zb_avnum_b").getValueAt(selrow, "nzbnum"));
			UFDouble hzbprice = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel("zb_avnum_b").getValueAt(selrow, "nzbprice"));
			String cbiddingid =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("cbiddingid").getValueObject());
			UFDouble bzbnum = UFDouble.ZERO_DBL;
			UFDouble nwinpercent = UFDouble.ZERO_DBL;
			if (e.getPos() == BillItem.BODY) {
				if ("nzbnum".equals(key)) {//��̯����
					bzbnum = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel("zb_avvendor").getValueAt(row, key));
					nwinpercent = bzbnum.div(hzbnum).multiply(new UFDouble(100));
					getBillCardPanel().getBillModel("zb_avvendor").setValueAt(nwinpercent, row, "nwinpercent");
					getBillCardPanel().getBillModel("zb_avvendor").setValueAt(hzbprice.multiply(bzbnum), row, "nzbmny");
					checkZbNum(hzbnum);
					
				} else if ("nwinpercent".equals(key)) {//����
					nwinpercent = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel("zb_avvendor").getValueAt(row, key));
					bzbnum = hzbnum.multiply(nwinpercent).div(new UFDouble(100));
					getBillCardPanel().getBillModel("zb_avvendor").setValueAt(bzbnum, row, "nzbnum");
					getBillCardPanel().getBillModel("zb_avvendor").setValueAt(hzbprice.multiply(bzbnum), row, "nzbmny");
					checkZbNum(hzbnum);
				}
				setNum(cbiddingid,row,selrow,bzbnum,hzbprice,nwinpercent);
			}
		}
		public void bodyRowChange(BillEditEvent e) {
			// TODO Auto-generated method stub
		}
	}
	//У���̯�� �������б���
	private void checkZbNum(UFDouble hzbnum){
		int rowCounts = getBillCardPanel().getBillTable("zb_avvendor").getRowCount();
		UFDouble zbnum=UFDouble.ZERO_DBL;
		for(int i=0;i<rowCounts;i++){
			zbnum=zbnum.add(PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel("zb_avvendor").getValueAt(i,"nzbnum")));
		}
		if(zbnum.compareTo(hzbnum)>0){
			showErrorMessage("��̯�����������б���");
		}
			
	}
}
