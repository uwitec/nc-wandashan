package nc.ui.zb.comments;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

/**
 * 
 * @author zhw
 * 分量功能对话框
 *
 */
public class AverageCalDlg extends UIDialog implements ActionListener ,IBillRelaSortListener2,BillEditListener {
	private static final long serialVersionUID = -39986234234258916L;
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private BillListPanel listPane = null;
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	private nc.ui.pub.beans.UIButton ivjBnCancle = null;
	private int m_currow = -1;
	private ClientUI ui = null;
	private BiEvaluationBodyVO[] bodyVo = null;
	
	private BiEvaluationBodyVO[] retVos = null;

	private void init() {
		setSize(1000, 600);
		setTitle("分量功能");
		setContentPane(getUIDialogContentPane());
		getBnOk().addActionListener(this);
		getBnCancle().addActionListener(this);
		getBillListPanel().addHeadEditListener(this);
		getBillListPanel().getHeadBillModel().addSortRelaObjectListener2(this);
	}

	public BillListPanel getBillListPanel() {
		if (listPane == null) {
			listPane = new BillListPanel();
			listPane.loadTemplet(ZbPubConst.DATE_CAL_DLG_TEMP_ID);
		}
		return listPane;
	}

	public AverageCalDlg(ClientUI ui) {
		super(ui);
		this.ui = ui;		
		init();	
	}
	
	public boolean setData(HYBillVO billvo){
		if(billvo == null)
			return false;
		try {
			m_currow = -1;
			this.bodyVo = ((ClientBusinessDelegator)ui.getBusiDelegator()).autoSplitNum(billvo,ui);
		} catch (Exception e) {
			e.printStackTrace();
			ui.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
			return false;
		}
		
		setDatasToUI(bodyVo);
		return true;
	}
	
	public BiEvaluationBodyVO[] getRetVos(){
		return retVos;
	}

	public void setDatasToUI(BiEvaluationBodyVO[] bodyVo){
		getBillListPanel().getHeadBillModel().clearBodyData();
		getBillListPanel().getHeadBillModel().setBodyDataVO(bodyVo);
		getBillListPanel().getHeadBillModel().execLoadFormula();	
		setBodyDataToPanle();
	}
	
	private void setBodyDataToPanle(){
		getBillListPanel().getBodyBillModel().clearBodyData();
		BidSlvendorVO[] bbs =bodyVo[m_currow<0?0:m_currow].getBidSlvendorVOs();
		getBillListPanel().getBodyBillModel().setBodyDataVO(bbs);
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
		getBillListPanel().getBodyBillModel().execLoadFormula();
	}
	
	/**
	 * 返回 UIDialogContentPane 特性值.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告:此方法将重新生成. */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getBillListPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 返回 PnButton 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIPanel getPnButton() {
		if (ivjPnButton == null) {
			try {
				ivjPnButton = new nc.ui.pub.beans.UIPanel();
				ivjPnButton.setName("PnButton");	
				getPnButton().add(getBnOk(), getBnOk().getName());		
				getPnButton().add(getBnCancle(), getBnCancle().getName());

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjPnButton;
	}


	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIButton getBnCancle() {
		if (ivjBnCancle == null) {
			try {
				ivjBnCancle = new nc.ui.pub.beans.UIButton();
				ivjBnCancle.setName("ivjBnSave");
				ivjBnCancle.setText("取消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancle;
	}


	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	private nc.ui.pub.beans.UIButton getBnOk() {
		if (ivjBnOk == null) {
			try {
				ivjBnOk = new nc.ui.pub.beans.UIButton();
				ivjBnOk.setName("BnOk");
				ivjBnOk.setText("确定");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnOk;
	}

	private void showErrorMsg(String msg){
		MessageDialog.showErrorDlg(ui, "", msg==null?"操作出现异常，请重新操作":msg);
	}

	public void actionPerformed(ActionEvent bo) {
		try {
			if (bo.getSource() == getBnCancle()) {
				this.closeCancel();
			} else if (bo.getSource() == getBnOk()) {
				onOK();
				this.closeOK();
			}
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}

	}

	private void onOK() throws BusinessException {
//		BiEvaluationBodyVO[] bodys = this.bodyVo;
		
		for(BiEvaluationBodyVO body:bodyVo){
//			确定保存时校验：招标数量之和==中标数量之和    中标数量 * 中标单价=中标金额
			body.validataOnSaveSplitNum();
		}
		
		try {
			BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)ui.getBufferData().getCurrentVO().getParentVO();
			retVos = ((ClientBusinessDelegator)ui.getBusiDelegator()).doSaveSplitNumInfor(head.getCbiddingid(),bodyVo,ui);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e instanceof BusinessException)
				throw (BusinessException)e;
			throw new BusinessException(e);
		}
	}

	
    public void setBidSlvendorVOsToUI(BidSlvendorVO[] svo) throws Exception {
		getBillListPanel().getBodyBillModel().clearBodyData();
		getBillListPanel().getBodyBillModel().setBodyDataVO(svo);
		getBillListPanel().getBodyBillModel().execLoadFormula();
		
	}

	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return bodyVo;
	}
	

	


		public void afterEdit(BillEditEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void bodyRowChange(BillEditEvent e) {
			m_currow = e.getRow();
			setBodyDataToPanle();
		}

}
