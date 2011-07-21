package nc.ui.zb.comments;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.zb.pub.DefBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.pub.ZbPuBtnConst;
import nc.vo.zb.pub.ZbPubConst;



/**
 * �б������UI
 * 
 * @author Administrator
 * 
 */
public class ClientUI extends DefBillManageUI implements ChangeListener,BillCardBeforeEditListener {

	private ClientLink cl=null;
	public ClientUI() {
		super();
		cl = new ClientLink(ClientEnvironment.getInstance());
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
		getBillCardPanel().setBodyMenuShow(false);
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);// ����״̬
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPrimaryKey());
		getBillCardPanel().setHeadItem("pk_billtype", ZbPubConst.ZB_EVALUATION_BILLTYPE);
		getBillCardPanel().setTailItem("voperatorid", _getOperator());// �Ƶ���
		getBillCardPanel().setTailItem("dmakedate", _getDate());// �Ƶ�����
		getBillCardPanel().setHeadItem("dbilldate", _getDate());
		
	}

	// ����Զ��尴ť
	public void initPrivateButton() {
		//�������鰴ť
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ZbPuBtnConst.FLBTN);
		btnVo.setBtnName("����");
		btnVo.setHintStr("����������");
		btnVo.setOperateStatus(new int[] {IBillOperate.OP_NOTEDIT,IBillOperate.OP_INIT});
		btnVo.setBusinessStatus(new int[]{IBillStatus.FREE});
		
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
		addPrivateButton(btnVo);
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
		return HYPubBO_Client.getBillNo(ZbPubConst.ZB_EVALUATION_BILLTYPE, _getCorp().getPrimaryKey(), null, null);
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
		return cl;
	}
	
	/**
	 * ��ͷ�༭ǰ
	 */
	public boolean beforeEdit(BillItemEvent e) {
		// TODO Auto-generated method stub
		String key = e.getItem().getKey();
		return false;
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row =e.getRow();
		return super.beforeEdit(e);
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		String key = e.getKey();
		if (e.getPos() == BillItem.HEAD) {
			if("cbiddingid".equalsIgnoreCase(key)){
				getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem(key));
			}else if("pk_deptdoc".equalsIgnoreCase(key)){//����  ���ҵ��Ա
				getBillCardPanel().setHeadItem("vemployeeid",null);
			}else if ("vemployeeid".equalsIgnoreCase(key)) {// ҵ��Ա  ��������
				String deptdoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_deptdoc").getValueObject());
				if(deptdoc ==null)
				    getBillCardPanel().execHeadTailEditFormulas(getBillCardPanel().getHeadItem("vemployeeid"));
			}
		} else if(e.getPos()==BillItem.BODY){
			int row = e.getRow();
			if("nzbprice".equalsIgnoreCase(key)){
				UFDouble nzbprice = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel().getBillModel().getValueAt(row,"nzbprice"));
				BidSlvendorVO[] svos =((BiEvaluationBodyVO)getBufferData().getCurrentVO().getChildrenVO()[row]).getBidSlvendorVOs();
				for(BidSlvendorVO svo:svos){
					svo.setNzbmny(PuPubVO.getUFDouble_NullAsZero(svo.getNzbnum()).multiply(nzbprice));
				}
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
	
	//ҳǩ�л��¼�
	public void stateChanged(ChangeEvent e) {

		int row =-1;
		if (isListPanelSelected()) {// //�б�ҳǩ
			if ("zb_slvendor".equals(getBillListPanel().getBodyTabbedPane().getSelectedTableCode())) {
				row =getBillListPanel().getBodyTable("zb_evaluation_b").getSelectedRow();
				if(row<0){
					
					getBillListPanel().getBodyTabbedPane().setSelectedIndex(0);
					showErrorMessage("��ѡ���д����Ϣ");
					return;
				}
				BiEvaluationBodyVO bvo =(BiEvaluationBodyVO)getBufferData().getCurrentVO().getChildrenVO()[row];
				BidSlvendorVO[] bbs =bvo.getBidSlvendorVOs();

				if(bbs==null){
					String cevaluationbid = bvo.getPrimaryKey();
					try {
						bbs =loadBidSlvendorVO(cevaluationbid);
					} catch (Exception e1) {
						e1.printStackTrace();
						showWarningMessage(PuPubVO.getString_TrimZeroLenAsNull(e1.getMessage()));
					}
				}
				bvo.setBidSlvendorVOs(bbs);
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
			if ("zb_slvendor".equals(getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())) {
				row = getBillCardPanel().getBillTable("zb_evaluation_b").getSelectedRow();
				if(row<0){
					getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
					showErrorMessage("��ѡ���д����Ϣ");
					return;
				}
				BiEvaluationBodyVO bvo =(BiEvaluationBodyVO)getBufferData().getCurrentVO().getChildrenVO()[row];
				
				BidSlvendorVO[] bbs =bvo.getBidSlvendorVOs();
				if(bbs==null){
					String cevaluationbid = bvo.getPrimaryKey();
					try {
						bbs =loadBidSlvendorVO(cevaluationbid);
					} catch (Exception e1) {
						e1.printStackTrace();
						showWarningMessage(PuPubVO.getString_TrimZeroLenAsNull(e1.getMessage()));
					}
					bvo.setBidSlvendorVOs(bbs);
				}
				
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
		return new String[] { "zb_evaluation_b", "zb_slvendor" };
	}
	
	private BidSlvendorVO[] loadBidSlvendorVO(String cevaluationbid) throws Exception{
		BidSlvendorVO[]  vens =null;
		if(PuPubVO.getString_TrimZeroLenAsNull(cevaluationbid)!=null)
			vens = (BidSlvendorVO[])HYPubBO_Client.queryByCondition(BidSlvendorVO.class, "cevaluationbid = '"+cevaluationbid+"'");
			
		return vens ;
		
	}
}
