package nc.ui.zb.bidding.make;

import java.awt.Component;
import java.awt.Container;

import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.scm.pattern.pub.UITimeTextField;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.MultiChildBillManageUI;
import nc.ui.zb.pub.ZbPubHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.pub.BillDateGetter;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubTool;

/**
 * ��������UI
 * 
 * @author Administrator
 * 
 */

public class MakeBiddingUI extends MultiChildBillManageUI implements BillCardBeforeEditListener {

	public MakeBiddingUI() {
		super();
		initlize();
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

	public MakeBiddingUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public MakeBiddingUI(String pk_corp, String pk_billType,
			String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientController();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	/**
	 * ��ʼ��
	 */
	private void initlize() {
		
	}

	@Override
	protected void initPrivateButton() {

		// �޶�����
		ButtonVO btnvo6 = new ButtonVO();
		btnvo6.setBtnNo(ZbPuBtnConst.REVISED);
		btnvo6.setBtnName("�޶�");
		btnvo6.setBtnChinaName("�޶�");
		btnvo6.setBtnCode(null);// code�������Ϊ��
		btnvo6.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT});
		btnvo6.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo6);
		
		//��ֹ��Ӧ��
		ButtonVO btnvo1 = new ButtonVO();
		btnvo1.setBtnNo(ZbPuBtnConst.StopVen);
		btnvo1.setBtnName("��ֹ��Ӧ��");
		btnvo1.setBtnChinaName("��ֹ��Ӧ��");
		btnvo1.setBtnCode(null);// code�������Ϊ��
		btnvo1.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo1.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo1);
		
		//��ֹ��Ӧ��
		ButtonVO btnvo3 = new ButtonVO();
		btnvo3.setBtnNo(ZbPuBtnConst.StartVen);
		btnvo3.setBtnName("ȡ����ֹ��Ӧ��");
		btnvo3.setBtnChinaName("ȡ����ֹ��Ӧ��");
		btnvo3.setBtnCode(null);// code�������Ϊ��
		btnvo3.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo3.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo3);
		
		//�鿴������ʷ
		ButtonVO btnvo2 = new ButtonVO();
		btnvo2.setBtnNo(ZbPuBtnConst.ViewHis);
		btnvo2.setBtnName("�鿴������ʷ");
		btnvo2.setBtnChinaName("�鿴������ʷ");
		btnvo2.setBtnCode(null);// code�������Ϊ��
		btnvo2.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		addPrivateButton(btnvo2);
		
		//�鿴������ʷ
		ButtonVO btnvo5 = new ButtonVO();
		btnvo5.setBtnNo(ZbPuBtnConst.MISS);
		btnvo5.setBtnName("����");
		btnvo5.setBtnChinaName("����");
		btnvo5.setBtnCode(null);// code�������Ϊ��
		btnvo5.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo5.setBusinessStatus( new int[]{IBillStatus.CHECKPASS});
		addPrivateButton(btnvo5);
		
		//��������
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(ZbPuBtnConst.FZGN);
		btnvo.setBtnName("��������");
		btnvo.setBtnChinaName("��������");
		btnvo.setBtnCode(null);// code�������Ϊ��
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT });
		btnvo.setChildAry(new int[] {ZbPuBtnConst.REVISED,ZbPuBtnConst.StopVen,ZbPuBtnConst.ViewHis,ZbPuBtnConst.StartVen,ZbPuBtnConst.MISS});
		addPrivateButton(btnvo);
		
		// ����
		ButtonVO btnvo4 = new ButtonVO();
		btnvo4.setBtnNo(ZbPuBtnConst.LINKQUERY);
		btnvo4.setBtnName("����");
		btnvo4.setBtnChinaName("����");
		btnvo4.setBtnCode(null);// code�������Ϊ��
		btnvo4.setOperateStatus(new int[] { IBillOperate.OP_NOTEDIT,IBillOperate.OP_NO_ADDANDEDIT });
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
	protected void initSelfData() {
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);// BillItemEvent��׽����BillItemEvent�¼�
		setUITimeTextField("zb_biddingtimes","tstart");
		setUITimeTextField("zb_biddingtimes","tend");	
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
		
			getBillCardPanel().setBodyMenuShow(BiddingBillVO.tablecode_body, false);
			getBillCardPanel().setBodyMenuShow(BiddingBillVO.tablecode_suppliers, false);
			getBillCardPanel().setBodyMenuShow(BiddingBillVO.tablecode_times, false);
		
	}

	/**
	 * �Ƿ�Ƭ����
	 * 
	 * @author zpm
	 */
	public boolean isCardPanelSelected() {
		return m_CurrentPanel.equals(BillTemplateWrapper.CARDPANEL);
	}

	@Override
	public void setDefaultData() throws Exception {
		//
		super.setDefaultData();
		//
		getBillCardPanel().setHeadItem("pk_corp", getCorpPrimaryKey());
		// �Ƶ�����
		getBillCardPanel().setTailItem("dmakedate",BillDateGetter.getMakeDate());
		// �Ƶ���
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		// ��������
		getBillCardPanel().setHeadItem("pk_billtype",getUIControl().getBillType());
		// ��������
		getBillCardPanel().setHeadItem("dbilldate",BillDateGetter.getMakeDate());
		// ����״̬
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		// �Ƿ�����
		getBillCardPanel().setHeadItem("fisself", UFBoolean.FALSE);
		String para = ZbPubTool.getParam();
		if(para!=null&&!"".equalsIgnoreCase(para))
		    getBillCardPanel().setHeadItem("reserve1",para);
		
		getBillCardPanel().getBodyTabbedPane().setEnabledAt(2, true);
		
		getBillCardPanel().setHeadItem("izbtype",new Integer(1));//�б�����
		getBillCardPanel().setHeadItem("ibusstatus",new Integer(0));//ҵ��״̬
		
		if (m_logInfor==null || m_logInfor.length==0 ) {
			showHintMessage("��ǰ�û�δ���ù���ҵ��Ա");
			return;
		}
		getBillCardPanel().setHeadItem("pk_deptdoc", m_logInfor[1]);
		getBillCardPanel().setHeadItem("vemployeeid", m_logInfor[0]);
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return null;
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator();
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
//			if(PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getHeadItem("fisinvcl").getValueObject(), UFBoolean.FALSE).booleanValue())
//				return false;
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
//		}else 
		if("gyscode".equalsIgnoreCase(key)){//��Ӧ�̱���
			if(PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row,"fistemp"),UFBoolean.FALSE).booleanValue())
				return false;
		}else if("temp".equalsIgnoreCase(key)){//��ʱ��Ӧ�̱���
			if(!PuPubVO.getUFBoolean_NullAs(getBillCardPanel().getBillModel().getValueAt(row,"fistemp"),UFBoolean.FALSE).booleanValue())
				return false;
		}else if("invcode".equalsIgnoreCase(key)||"mesname".equalsIgnoreCase(key)){
			if(PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBillModel().getValueAt(row,"cupsourcebilltype"))!=null)
			     return false;			
		}
		return super.beforeEdit(e);
	}
	
	private Object getHeadValue(String itemKey){
		return getBillCardPanel().getHeadItem(itemKey).getValueObject();
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			
			if("pk_deptdoc".equalsIgnoreCase(key)){//����  ���ҵ��Ա
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// ҵ��Ա  ��������
				String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null)
				    getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("vemployeeid"));
			}
//			else if("fisinvcl".equalsIgnoreCase(key)){//����������б�
//				String[] tablecodes = new String[] { "zb_bidding_b" };//�����ӱ���Ϣ
//				clearTable(tablecodes);
//			}
			else if("izbtype".equalsIgnoreCase(key)){//�б�����  �ֳ��б� ������ִΰ��Ų��ɱ༭
				String izbtype = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("izbtype").getValueObject());
				if("1".equalsIgnoreCase(izbtype)){
					//������ִΰ��Ų��ɱ༭
					String[] tablecodes = new String[] { "zb_biddingtimes" };//������ִΰ���
					clearTable(tablecodes);
					getBillCardPanel().getBodyTabbedPane().setEnabledAt(2, false);
				}else{
					getBillCardPanel().getBodyTabbedPane().setEnabledAt(2, true);
				}
			}

		} else if (e.getPos() == BillItem.BODY) {
			int row =e.getRow();
            
//			if("invclcode".equalsIgnoreCase(key)){//�������  ��մ����Ϣ 
//                 getBillCardPanel().setBodyValueAt(null,row,"cinvbasid");
//                 getBillCardPanel().setBodyValueAt(null,row,"cinvmanid");
//                 getBillCardPanel().setBodyValueAt(null,row,"invcode");
//                 getBillCardPanel().execBodyFormula(row,"invcode");
//             }else 
            	 if("invcode".equalsIgnoreCase(key)){//���   �����������
            	 String invclcode =PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getBodyValueAt(row,"invclcode"));//����������
            	
            	 if(invclcode ==null){
            		 String[] aryAssistunit = 
                		 new String[] {
            				 //"cinvclid->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,cinvbasid)",
                			    "invclcode->getColValue(bd_invcl,invclasscode,pk_invcl,cinvclid)",
                		 		"invclname->getColValue(bd_invcl,invclassname,pk_invcl,cinvclid)"};
     				 getBillCardPanel().getBillModel().execFormulas(e.getRow(),aryAssistunit);
            	 }
             }else if("fistemp".equalsIgnoreCase(key)){//�Ƿ���ʱ��Ӧ��
            	 getBillCardPanel().getBillModel().setValueAt(null,row,"gyscode");
            	 getBillCardPanel().getBillModel().setValueAt(null, row,"temp");
            	 getBillCardPanel().getBillModel().setValueAt(null, row,"gysname");
            	 getBillCardPanel().getBillModel().setValueAt(null, row,"ccustmanid");
            	 getBillCardPanel().getBillModel().setValueAt(null, row,"ccustbasid");	    
             }else if("temp".equalsIgnoreCase(key)){
            	 getBillCardPanel().getBillModel().setValueAt(null,row,"gyscode");
             }else if("gyscode".equalsIgnoreCase(key)){
            	 getBillCardPanel().getBillModel().setValueAt(null, row,"temp");
             }
		}

	}

	/**
	 * ���Ӻ�̨У��
	 */
	public Object getUserObject() {
		return null;
	}

	@Override
	public boolean isSaveAndCommitTogether() {
		return false;
	}

	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @˵�� ���ݱ��� tableCode,���ҳǩ����
	 * @ʱ�� 2011-04-29����02:06:02
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
	
	private void setUITimeTextField(String tablecode,String cellcode){
		 TableColumn tablecol = null;
	     BillItem dsendtime = getBillCardPanel().getBodyItem(tablecode,cellcode);
	     if (null != dsendtime) {
	       // ����ʱ��༭��
	       try {
	         //�ƻ�����ʱ��
	         tablecol = getBillCardPanel().getBodyPanel(tablecode).getTable().getColumn(dsendtime.getName());
	         if (null != tablecol) {
	           BillCellEditor timecelledit = new BillCellEditor(new UITimeTextField());
	           tablecol.setCellEditor(timecelledit);
	         }
	       } catch (Exception e) {
	         SCMEnv.out(e);
	       }
	     }
	}
	
	/**
	 * ��ȡ���е�TableCode
	 */
	public String[] getTableCodes() {
		return new String[]{"zb_bidding_b","zb_biddingsuppliers","zb_biddingtimes"};
	}
	
	public boolean onClosing() {
		boolean flag = super.onClosing();
		MakeBiddingHelper.clear();//���� ��̬����
		return flag;
	}
	

}
