package nc.ui.wds.load.lgfy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillBodyMenuListener;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.ref.TeamDocBRefModel;
import nc.ui.zmpub.pub.check.BeforeSaveValudate;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.load.account.LoadpirceTVO;
import nc.vo.wds.load.account.LoadpriceB2VO;

/**
 * 编辑人员信息
 * 
 * @author yf
 * 
 */
public class WDSVEditDlg extends nc.ui.pub.beans.UIDialog implements
		BillCardBeforeEditListener, BillEditListener2,
		nc.ui.pub.bill.BillEditListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8630658272830413342L;
	// 快速编辑模板
	private BillCardPanel m_billCardPanel_q = null;
	private ToftPanel parent = null;

	private UIButton btn_save = null;
	private UIButton btn_cancle = null;

	private UIButton btn_addline = null;
	private UIButton btn_delline = null;

	private UIButton btn_return = null;

	private LoadpirceTVO[] datas = null;

	private LoadpriceB2VO parentvo = null;

	public WDSVEditDlg(ToftPanel tp) {
		super(tp);
		this.parent = tp;

		initialize();
		initData();
	}

	private void showErrorMsg(String msg) {
		MessageDialog.showErrorDlg(parent, "", msg == null ? "操作出现异常，请重新操作"
				: msg);
	}

	private void initialize() {
		this.setName("QuickDlg");
		setSize(800, 540);
		setTitle("编辑人员信息");
//		setJMenuBar(getJMenuBar());
		setContentPane(getUIDialogContentPane());
		getBillCardPanel_q().addEditListener(this);
		getBillCardPanel_q().addBillEditListenerHeadTail(this);
		getBillCardPanel_q().setBillBeforeEditListenerHeadTail(this);
		getBillCardPanel_q().addBodyEditListener2(this);
		addListenerEvent();
	}
	private JPanel ivjUIDialogContentPane = null;
	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(this.getBillCardPanel_q(), BorderLayout.CENTER);
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}
	
	private UIPanel ivjPanlCmd = null;
	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getSaveButton(), getSaveButton().getName());
			ivjPanlCmd.add(getAddLineButton(), getAddLineButton().getName());
			ivjPanlCmd.add(getDelLineButton(), getDelLineButton().getName());
			ivjPanlCmd.add(getCancleButton(), getCancleButton().getName());
			ivjPanlCmd.add(getReturnButton(), getReturnButton().getName());
			
		}
		return ivjPanlCmd;
	}
	
	public void addListenerEvent() {
		getSaveButton().addActionListener(this);
		getCancleButton().addActionListener(this);
		getReturnButton().addActionListener(this);
		getAddLineButton().addActionListener(this);
		getDelLineButton().addActionListener(this);
	}
	
	public JMenuBar getJMenuBar() {
		JMenuBar menu = new JMenuBar();
		getSaveButton().addActionListener(this);
		getCancleButton().addActionListener(this);
		getReturnButton().addActionListener(this);
		getAddLineButton().addActionListener(this);
		getDelLineButton().addActionListener(this);
		menu.add(getAddLineButton());
		menu.add(getDelLineButton());
		menu.add(getSaveButton());
		menu.add(getCancleButton());
		menu.add(getReturnButton());
		return menu;
	}

	/**
	 * 保存
	 * 
	 * @return 按钮
	 */
	private UIButton getSaveButton() {
		if (btn_save == null) {
			try {
				btn_save = new nc.ui.pub.beans.UIButton();
				btn_save.setName("Save");
				btn_save.setText("保存");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return btn_save;
	}

	/**
	 * 取消
	 * 
	 * @return 按钮
	 */
	private UIButton getCancleButton() {
		if (btn_cancle == null) {
			try {
				btn_cancle = new nc.ui.pub.beans.UIButton();
				btn_cancle.setName("Cancle");
				btn_cancle.setText("取消");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return btn_cancle;
	}

	private UIButton getAddLineButton() {
		if (btn_addline == null) {
			try {
				btn_addline = new nc.ui.pub.beans.UIButton();
				btn_addline.setName("btn_addline");
				btn_addline.setText("增行");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return btn_addline;
	}

	/**
	 * 取消
	 * 
	 * @return 按钮
	 */
	private UIButton getDelLineButton() {
		if (btn_delline == null) {
			try {
				btn_delline = new nc.ui.pub.beans.UIButton();
				btn_delline.setName("btn_delline");
				btn_delline.setText("删行");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return btn_delline;
	}

	/**
	 * 返回
	 * 
	 * @return 按钮
	 */
	private UIButton getReturnButton() {
		if (btn_return == null) {
			try {
				btn_return = new nc.ui.pub.beans.UIButton();
				btn_return.setName("Return");
				btn_return.setText("返回");

			} catch (java.lang.Throwable ivjExc) {

			}
		}
		return btn_return;
	}

	/**
	 * 
	 * @return 卡片界面
	 */
	public BillCardPanel getBillCardPanel_q() {
		if (m_billCardPanel_q == null) {

			m_billCardPanel_q = new BillCardPanel();

			BillData bd = new BillData(m_billCardPanel_q
					.getTempletData("0001S3100000000OFB46"));

			m_billCardPanel_q.setBillData(bd);
			m_billCardPanel_q.setBodyMenuShow(true);
			m_billCardPanel_q.addBodyMenuListener(new quickEditMenu());
			m_billCardPanel_q.setEnabled(true);
			reSetMenu();
		}

		return m_billCardPanel_q;
	}

	class quickEditMenu implements BillBodyMenuListener {
		public void onMenuItemClick(java.awt.event.ActionEvent e) {
			UIMenuItem menuItem = (UIMenuItem) e.getSource();
			if (menuItem.equals(getBillCardPanel_q().getAddLineMenuItem()))
				// onBoLineAdd();
				onAddLine();
			else if (menuItem.equals(getBillCardPanel_q().getDelLineMenuItem()))
				// onBoLineDel();
				onDelLine();

		}

		public void actionPerformed(ActionEvent e) {
			// TODO 自动生成方法存根

		}

	}

	private void reSetMenu() {
		UIMenuItem[] bodyMenuItems = m_billCardPanel_q.getBodyMenuItems();
		UIMenuItem[] bodyMenuItemsRet = new UIMenuItem[2];
		bodyMenuItemsRet[0] = bodyMenuItems[0];
		bodyMenuItemsRet[1] = bodyMenuItems[1];
		m_billCardPanel_q.setBodyMenu(bodyMenuItemsRet);
	}

	// 表体
	public boolean beforeEdit(BillEditEvent e) {
		if (e.getKey().equalsIgnoreCase("rycode")) {// 过滤人员档案
			String pk_wds_teamdoc_h = PuPubVO
					.getString_TrimZeroLenAsNull(getBillCardPanel_q()
							.getHeadItem("pk_wds_teamdoc_h").getValueObject());
			if (pk_wds_teamdoc_h != null && pk_wds_teamdoc_h.length() > 0) {
				UIRefPane pane = (UIRefPane) getBillCardPanel_q()
						.getBillModel().getItemByKey("rycode").getComponent();
				((TeamDocBRefModel) pane.getRefModel())
						.setPk_wds_teamdoc_h(pk_wds_teamdoc_h);
			}

		}
		return true;
	}

	// 表头
	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		return true;
	}

	public void afterEdit(BillEditEvent e) {

	}

	public void bodyRowChange(BillEditEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent bo) {
		try {

			if (bo.getSource() == getSaveButton()) {
				this.onSave();
			} else if (bo.getSource() == getCancleButton()) {
				this.onCancle();
			} else if (bo.getSource() == getReturnButton()) {
				this.onReturn();
			} else if (bo.getSource() == getAddLineButton()) {
				onAddLine();
			} else if (bo.getSource() == getDelLineButton()) {
				onDelLine();
			}
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
			e.printStackTrace();
		}

	}

	private void onAddLine() {
		getBillCardPanel_q().addLine();
	}

	private void onDelLine() {

		getBillCardPanel_q().stopEditing();
		if (getBillCardPanel_q().getBillTable().getSelectedRow() > -1) {
			// int[] aryRows =
			// getBillCardPanel().getBillTable().getSelectedRows();
			getBillCardPanel_q().delLine();
		}

	}

	private void onSave() throws Exception {
		getBillCardPanel_q().stopEditing();
		BeforeSaveValudate.BodyNotNULL(getBillCardPanel_q().getBillTable());
		getBillCardPanel_q().dataNotNullValidate();

		// this.datas = (LoadpirceTVO[]) getBillCardPanel_q().getBillModel()
		// .getBodyValueVOs(LoadpirceTVO.class.getName());
		AggregatedValueObject changevo = getBillCardPanel_q().getBillData()
				.getBillValueChangeVO(HYBillVO.class.getName(),
						LoadpriceB2VO.class.getName(),
						LoadpirceTVO.class.getName());
		if (changevo == null) {
			return;
		}
		this.datas = (LoadpirceTVO[]) changevo.getChildrenVO();

		String pk_loadprice_b2 = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanel_q().getHeadItem(
						"pk_loadprice_b2").getValueObject());

		for (LoadpirceTVO vo : datas) {
			vo.setPk_loadprice_b2(pk_loadprice_b2);
		}
		beforeSaveCheck(datas);
		save(datas);
		afterSave();
		// 自动返回
		onCancle();
		closeOK();
		// 将datas放置列表界面

	}

	private void afterSave() throws Exception {
		((ClientUI) this.parent).refreshTVOs(null);
	}

	private void beforeSaveCheck(LoadpirceTVO[] datas2)
			throws BusinessException {
		UFDouble nmny = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel_q()
				.getHeadItem("nloadprice").getValueObject());
		int rowcount = getBillCardPanel_q().getBillTable().getRowCount();
		UFDouble bodynmny = new UFDouble(0);
		UFDouble bnmny = null;
		String ry = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < rowcount; i++) {
			bnmny = PuPubVO.getUFDouble_NullAsZero(getBillCardPanel_q()
					.getBodyValueAt(i, "nloadprice"));
			ry = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel_q()
					.getBodyValueAt(i, "pk_wds_teamdoc_b"));
			if (bnmny.toDouble() <= 0) {
				throw new BusinessException("费用不能为空");
			}
			if (map.containsKey(ry)) {
				throw new BusinessException("表体第" + (i + 1) + "行人员有重复");
			}
			map.put(ry, i);
			bodynmny = bodynmny.add(bnmny);
		}
		if (bodynmny.compareTo(nmny) > 0) {
			throw new BusinessException("费用不能大于班组总费用");
		}
	}

	private void save(LoadpirceTVO[] datas2) throws Exception {
		HYBillVO bill = new HYBillVO();
		bill.setChildrenVO(datas2);

		// getuserobject
		AggregatedValueObject billVO = HYPubBO_Client.saveBD(bill, null);
		CircularlyAccessibleValueObject[] vos = billVO.getChildrenVO();
		if (vos != null && vos.length > 0) {
			for (CircularlyAccessibleValueObject vo : vos) {
				vo.setStatus(VOStatus.UNCHANGED);
			}
		}
		billVO.setChildrenVO(vos);
	}

	private void onCancle() throws ValidationException, UifException {
		setParentVO((LoadpriceB2VO) getBillCardPanel_q().getBillData()
				.getHeaderValueVO(LoadpriceB2VO.class.getName()));
	}

	private void initData() {
		this.datas = null;

		getBillCardPanel_q().getBillData().setHeaderValueVO(null);
		getBillCardPanel_q().getBillData().setBodyValueVO(null);
		getBillCardPanel_q().updateUI();
	}

	private void onReturn() throws ValidationException {
		closeCancel();
	}

	@Override
	protected void close() {
		initData();
		super.close();
	}

	public void setParentVO(LoadpriceB2VO vo) throws UifException {
		getBillCardPanel_q().getBillData().setHeaderValueVO(vo);
		setBodyValueVO(vo);
	}

	private void setBodyValueVO(LoadpriceB2VO vo) throws UifException {
		LoadpirceTVO[] tvos = queryBodyValueVO(vo);
		if (null != tvos && tvos.length > 0) {
			getBillCardPanel_q().getBillData().setBodyValueVO(tvos);
			for (int i = 0; i < tvos.length; i++) {
				getBillCardPanel_q().getBillModel().setRowState(i,
						BillModel.MODIFICATION);
				getBillCardPanel_q().execBodyFormula(i, "pk_wds_teamdoc_b");

			}

		}

	}

	private LoadpirceTVO[] queryBodyValueVO(LoadpriceB2VO vo)
			throws UifException {
		Object o = HYPubBO_Client.queryByCondition(LoadpirceTVO.class,
				" pk_loadprice_b2 = '" + vo.getPk_loadprice_b2() + "' ");
		if (o == null) {
			return null;
		}
		return (LoadpirceTVO[]) o;
	}

}