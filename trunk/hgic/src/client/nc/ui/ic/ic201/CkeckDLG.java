package nc.ui.ic.ic201;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.vo.hg.ic.pub.bill.GenCheckVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
/**
 * ������Ϣ�Ի���
 * @author zhw
 *
 */
public class CkeckDLG extends UIDialog implements ActionListener  {
	private static final long serialVersionUID = -39986234234258916L;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillCardPanel cardPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	
	private ToftPanel parent = null;
	private GeneralBillVO gbillvo = null;
	
	private  ClientEnvironment ce=null;
	
	public CkeckDLG(ToftPanel tp,ClientEnvironment ce) {
		super(tp);
		this.parent = tp;
		this.ce=ce;
		init();
		initDataPanel();
	}
	
	public void initDataPanel(){
		getBillCardPanel().setHeadItem("fisqualified","Y");
		getBillCardPanel().setHeadItem("vqualityproblem", null);
		getBillCardPanel().setHeadItem("vproblemcause", null);
		getBillCardPanel().setHeadItem("ccheckid",ce.getUser().getPrimaryKey());
		getBillCardPanel().setHeadItem("dcheckdate",ce.getDate());
		getBillCardPanel().execHeadFormula("ccheckid");
	}
	private void init() {
		setSize(500, 500);
		setTitle("������Ϣ");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
	}
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getBillCardPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	
	public BillCardPanel getBillCardPanel() {
		if (cardPane == null) {
			cardPane = new BillCardPanel();
			cardPane.loadTemplet(HgPubConst.CHECK_TEMP_ID);
		}
		return cardPane;
	}
	
	/**
	 * ���� PnButton ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ����:�˷�������������. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");
				getPnButton().add(getBnOk(), getBnOk().getName());	
				getPnButton().add(getBnCancel(), getBnCancel().getName());

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}

	/**
	 * ���� BnOk ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	private nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("ȷ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}

	/**
	 * ���� BnCancel ����ֵ.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ����:�˷�������������. */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("ȡ��");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}
	
	public void actionPerformed(ActionEvent bo) {
		try{
			if (bo.getSource() == getBnOk()) {
				
				onOK();
				this.closeOK();
			} else if (bo.getSource() == getBnCancel()) {
				this.closeCancel();
			}
		}catch(Exception e){
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void onOK() throws Exception{
		CircularlyAccessibleValueObject vos =getBillCardPanel().getBillData().getHeaderValueVO(GenCheckVO.class.getName());
		if(vos == null)
			throw new BusinessException("��ȡ������Ϣ����");
		GenCheckVO vo= (GenCheckVO)vos;
		changeVO(vo,gbillvo);//VOת��
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		GeneralBillItemVO[] items =(GeneralBillItemVO[])gbillvo.getChildrenVO();
		int len=items.length;
		if(PuPubVO.getUFBoolean_NullAs(vo.getFisqualified(),UFBoolean.FALSE).booleanValue()){// �ϸ�
			for(int i=0;i<len;i++){
				items[i].setAttributeValue(HgPubConst.NUM_DEF_QUA,PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_FAC)));
			}
		}else {//���ϸ�
			//��浥��,  ��湩Ӧ���ݲ�����			
			if(PuPubVO.getString_TrimZeroLenAsNull(vo.getVqualityproblem())==null){
				throw new BusinessException("�������ⲻ��Ϊ��");
			}
			if(PuPubVO.getString_TrimZeroLenAsNull(vo.getVqualityproblem())==null){
				throw new BusinessException("��������ԭ����Ϊ��");
			}
			head.setAttributeValue(HgPubConst.VUSERDEF[5],"Y");	
		}
		Class[] ParameterTypes = new Class[]{GeneralBillVO.class};
		Object[] ParameterValues = new Object[]{gbillvo};
		Object o=LongTimeTask.callRemoteService("ic","nc.bs.hg.ic.ic201.SaveGeneral", "onSaveGeneral", ParameterTypes, ParameterValues, 2);
		if (o != null) {
			Map map = (Map) o;
			if (map.containsKey(head.getCgeneralhid())) {
				head.setTs((map.get(head.getCgeneralhid()).toString()));
			}
			for (int i = 0; i < len; i++) {
				if (map.containsKey(items[i].getCgeneralbid())) {
					items[i].setTs((map.get(items[i].getCgeneralbid())
							.toString()));
				}
			}
		}
		setGbillvo(gbillvo);
	}

	private void  changeVO(GenCheckVO check,GeneralBillVO vo){
		GeneralBillHeaderVO head = vo.getHeaderVO();
		head.setAttributeValue(HgPubConst.VUSERDEF[0],PuPubVO.getString_TrimZeroLenAsNull(check.getFisqualified()));
		head.setAttributeValue(HgPubConst.VUSERDEF[1],PuPubVO.getString_TrimZeroLenAsNull(check.getVqualityproblem()));
		head.setAttributeValue(HgPubConst.VUSERDEF[2],PuPubVO.getString_TrimZeroLenAsNull(check.getVproblemcause()));
		head.setAttributeValue(HgPubConst.VUSERDEF[3],PuPubVO.getString_TrimZeroLenAsNull(check.getCcheckid()));
		head.setAttributeValue(HgPubConst.VUSERDEF[4],PuPubVO.getString_TrimZeroLenAsNull(check.getDcheckdate()));
		head.setAttributeValue(HgPubConst.VUSERDEF[5],"N");
	}
	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(parent, "", msg==null?"���������쳣�������²���":msg);
	}

	public GeneralBillVO getGbillvo() {
		return gbillvo;
	}

	public void setGbillvo(GeneralBillVO gbillvo) {
		this.gbillvo = (GeneralBillVO)gbillvo.clone();
	}
}
