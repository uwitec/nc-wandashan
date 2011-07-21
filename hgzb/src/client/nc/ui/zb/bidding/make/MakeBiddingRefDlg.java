package nc.ui.zb.bidding.make;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.IBillRelaSortListener2;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bidding.PuPlanInvVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class MakeBiddingRefDlg extends UIDialog implements ActionListener,IBillRelaSortListener2 {	
	
	private javax.swing.JPanel ivjUIDialogContentPane = null;
//	private PreBidListToListPanel listPane = null;
	
	private nc.ui.pub.beans.UIPanel ivjPnButton = null;
	private nc.ui.pub.beans.UIButton ivjBnCancel = null;
	private nc.ui.pub.beans.UIButton ivjBnOk = null;
	private nc.ui.pub.beans.UIButton ivjBnQry = null;
	
	
	private PuPlanInvVO[] m_datas = null;
//	private List<PuPlanInvVO> m_retDatas = new AList<PuPlanInvVO>();
	private List<PuPlanInvVO> lseldata = new ArrayList<PuPlanInvVO>();
	private BillListPanel m_datapanel = null;
	private ToftPanel tp = null;
	private ClientLink cl = null;
	public MakeBiddingRefDlg(ToftPanel parent,ClientLink cl){
		super(parent);
		tp = parent;
		this.cl = cl;
		init();
	}
	
	private void init() {
		setSize(810,400);
		setTitle("发货数据参照");
		setContentPane(getUIDialogContentPane());		
		
		getBnOk().addActionListener(this);
		getBnCancel().addActionListener(this);
		getDataPanel().getHeadBillModel().addSortRelaObjectListener2(this);
		getBnQry().addActionListener(this);
	}
	
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new javax.swing.JPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnButton(), "South");
				getUIDialogContentPane().add(getDataPanel(), "Center");
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjUIDialogContentPane;
	}
	private BillListPanel getDataPanel(){
		if(m_datapanel == null){
			m_datapanel = new BillListPanel();
			m_datapanel.loadTemplet(ZbPubConst.ZB_BIDDING_BILLTYPE_REF, null,
					ClientEnvironment.getInstance().getCorporation().getPrimaryKey(), ClientEnvironment.getInstance().getUser().getPrimaryKey());
//			m_datapanel.setEnabled(true);
			m_datapanel.getHeadTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return m_datapanel;
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
				getPnButton().add(getBnCancel(), getBnCancel().getName());
				getPnButton().add(getBnQry(),getBnQry().getName());
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
	nc.ui.pub.beans.UIButton getBnOk() {
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
	
	/**
	 * 返回 BnOk 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnQry() {
		if (ivjBnQry == null) {
			try {
				ivjBnQry = new nc.ui.pub.beans.UIButton();
				ivjBnQry.setName("ivjBnQry");
				ivjBnQry.setText("查询");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnQry;
	}
	
	/**
	 * 返回 BnCancel 特性值.
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告:此方法将重新生成. */
	nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText("取消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return ivjBnCancel;
	}	

	
	
	public void clear(){
		m_datas = null;
//		lseldata.clear();
		getDataPanel().getHeadBillModel().clearBodyData();
	}
	public void setInitDatas(PuPlanInvVO[] datas){
		clear();
		m_datas = datas;
		lseldata.clear();
		getDataPanel().getHeadBillModel().setBodyDataVO(m_datas);
		getDataPanel().getHeadBillModel().execLoadFormula();
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == getBnOk()){
			lseldata.clear();
			int[] sels = getDataPanel().getHeadTable().getSelectedRows();
			if(sels == null || sels.length == 0)
			{
				tp.showWarningMessage("请选择数据");
				return;
			}
			for(int sel:sels){
				lseldata.add(m_datas[sel]);
			}
			closeOK();
		}else if(e.getSource() == getBnCancel()){
			closeCancel();
		}else if(e.getSource() == getBnQry()){
			onQry();
		}
	}
	private void onQry(){
		try {
			PuPlanInvVO[] vos = MakeBiddingHelper.doQuery(this, cl);
//			if(vos == null || vos.length == 0)
//				return;
			setInitDatas(vos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tp.showErrorMessage(ZbPubTool.getString_NullAsTrimZeroLen(e.getMessage()));
		}
	}
	
	public PuPlanInvVO[] getRefOutDatas(){
		if(lseldata == null || lseldata.size() == 0)
			return null;
		return lseldata.toArray(new PuPlanInvVO[0]);
	}

	public Object[] getRelaSortObjectArray() {
		// TODO Auto-generated method stub
		return m_datas;
	}
}
