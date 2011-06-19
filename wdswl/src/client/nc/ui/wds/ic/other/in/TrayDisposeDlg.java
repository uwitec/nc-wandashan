package nc.ui.wds.ic.other.in;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

/**
 * @author zpm
 */
public class TrayDisposeDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, ListSelectionListener, BillEditListener,
		BillEditListener2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InPubClientUI myClientUI;

	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	private String m_pkcorp = null;

	private String m_operator = null;

	private String m_billType = null;

	private UIPanel ivjPanlCmd = null;

	private UIButton ivjbtnOk = null;

	private UIButton ivjbtnCancel = null;

	private UIButton btn_addline = null;

	private UIButton btn_deline = null;

	private Map<String, List<TbGeneralBBVO>> map = null;

	private boolean isEdit = true;

	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, InPubClientUI myClientUI,
			boolean isEdit) {
		super(myClientUI);
		this.myClientUI = myClientUI;
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.isEdit = isEdit;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(750, 550);
		setTitle("选择托盘");
		setContentPane(getUIDialogContentPane());
		// 设置编缉状态
		setEdit();
		//
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getAddLine().addActionListener(this);
		getDeline().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this);
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getbillListPanel().getBodyScrollPane("tb_general_b_b")
				.addEditListener2(this);
		// 加载表头数据
		loadHeadData();
	}

	public void setEdit() {
		getbillListPanel().setEnabled(isEdit);
		getbtnCancel().setEnabled(true);
		getbtnCancel().setEnabled(isEdit);
		getbtnOk().setEnabled(isEdit);
		getAddLine().setEnabled(isEdit);
		getDeline().setEnabled(isEdit);
	}

	public void loadHeadData() {
		try {
			OtherInBillVO billvo = (OtherInBillVO) myClientUI.getBillVOFromUI();
			if (billvo != null) {
				getbillListPanel().setHeaderValueVO(billvo.getChildrenVO());
				getbillListPanel().getHeadBillModel().execLoadFormula();
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	// 增行，孙表默认值
	public void setBodyDefaultValue(int row) {
		TbGeneralBVO child = getHeadBVO(getHeadCurrentRow());
		if (child != null) {
			TbGeneralBBVO bbvo = new TbGeneralBBVO();
			// tbGeneralBBVO.setCdt_pk(cdtvo[0].toString());//指定托盘
			// bbvo.setGebb_rowno(String.valueOf((i+1)*10));//行号
			bbvo.setGeb_pk(child.getGeb_pk());// 子表主键
			bbvo.setGeh_pk(child.getGeh_pk());// 主表主键
			bbvo.setPk_invbasdoc(child.getGeb_cinvbasid());// 存货基本ID
			bbvo.setPk_invmandoc(child.getGeb_cinventoryid());// 存货管理ID
			bbvo.setAunit(child.getCastunitid());// 辅单位
			bbvo.setUnitid(child.getPk_measdoc());// 主单位
			bbvo.setPk_cargdoc(child.getGeb_space());// 货位
			bbvo.setGebb_hsl(child.getGeb_hsl());// 换算率
			bbvo.setGebb_vbatchcode(child.getGeb_vbatchcode());// 批次
			bbvo.setGebb_lvbatchcode(child.getGeb_backvbatchcode());// 回写批次
			bbvo.setGebb_nprice(child.getGeb_nprice());// 单价
			bbvo.setGebb_nmny(child.getGeb_nmny());// 金额
			getbillListPanel().getBodyBillModel().setBodyRowVO(bbvo, row);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		}
	}

	public Map<String, List<TbGeneralBBVO>> getBufferData() {
		if (map == null) {
			map = cloneBufferData();
		}
		return map;
	}

	public Map<String, List<TbGeneralBBVO>> cloneBufferData() {
		Map<String, List<TbGeneralBBVO>> map1 = myClientUI.getTrayInfor();
		Map<String, List<TbGeneralBBVO>> map2 = new HashMap<String, List<TbGeneralBBVO>>();
		if (map1.size() > 0) {
			Iterator<String> it = map1.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<TbGeneralBBVO> list = map1.get(key);
				map2.put(key, cloneBBVO(list));
			}
		}
		return map2;
	}

	public List<TbGeneralBBVO> cloneBBVO(List<TbGeneralBBVO> list) {
		List<TbGeneralBBVO> list1 = new ArrayList<TbGeneralBBVO>();
		if (list != null && list.size() > 0) {
			for (TbGeneralBBVO b : list) {
				list1.add((TbGeneralBBVO) b.clone());
			}
		}
		return list1;
	}

	protected TbGeneralBVO getHeadBVO(int row) {
		TbGeneralBVO vo = (TbGeneralBVO) getbillListPanel().getHeadBillModel()
				.getBodyValueRowVO(row, TbGeneralBVO.class.getName());
		return vo;
	}

	protected TbGeneralBBVO getBodyVO(int row) {
		TbGeneralBBVO vo = (TbGeneralBBVO) getbillListPanel()
				.getBodyBillModel().getBodyValueRowVO(row,
						TbGeneralBBVO.class.getName());
		return vo;
	}

	protected int getBodyCurrentRow() {
		int row = getbillListPanel().getBodyTable().getRowCount() - 1;
		return row;
	}

	protected int getHeadCurrentRow() {
		int row = getbillListPanel().getHeadTable().getSelectedRow();
		return row;
	}

	// 增行
	protected void onLineAdd() {
		getbillListPanel().getBodyBillModel().addLine();
		setBodyDefaultValue(getBodyCurrentRow());
	}

	// 删行
	protected void onLineDel() {
		int[] rows = getbillListPanel().getBodyTable().getSelectedRows();
		getbillListPanel().getBodyBillModel().delLine(rows);
	}

	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet(m_billType, null, m_operator,
						m_pkcorp);
				ivjbillListPanel.getHeadTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// 单选
				ivjbillListPanel.getBodyTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// 单选
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
	}

	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getbillListPanel(), "Center");
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}

	private UIPanel getPanlCmd() {
		if (ivjPanlCmd == null) {
			ivjPanlCmd = new UIPanel();
			ivjPanlCmd.setName("PanlCmd");
			ivjPanlCmd.setPreferredSize(new Dimension(0, 40));
			ivjPanlCmd.setLayout(new FlowLayout());
			ivjPanlCmd.add(getAddLine(), getAddLine().getName());
			ivjPanlCmd.add(getDeline(), getDeline().getName());
			ivjPanlCmd.add(getbtnOk(), getbtnOk().getName());
			ivjPanlCmd.add(getbtnCancel(), getbtnCancel().getName());
		}
		return ivjPanlCmd;
	}

	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText("确定");
		}
		return ivjbtnOk;
	}

	private UIButton getAddLine() {
		if (btn_addline == null) {
			btn_addline = new UIButton();
			btn_addline.setName("addline");
			btn_addline.setText("增行");
		}
		return btn_addline;
	}

	private UIButton getDeline() {
		if (btn_deline == null) {
			btn_deline = new UIButton();
			btn_deline.setName("deline");
			btn_deline.setText("删行");
		}
		return btn_deline;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText("取消");
		}
		return ivjbtnCancel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {
			try {
				saveCurrentData(getHeadCurrentRow());
				check();
				closeOK();
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this, "警告", e1.getMessage());
			}
		} else if (e.getSource().equals(getbtnCancel())) {
			closeCancel();
		} else if (e.getSource().equals(getAddLine())) {
			onLineAdd();
		} else if (e.getSource().equals(getDeline())) {
			onLineDel();
		}
	}

	public void saveCurrentData(int row) {
		if (row < 0) {
			return;
		}
		TbGeneralBVO bvo = getHeadBVO(row);
		String key = bvo.getGeb_crowno();
		TbGeneralBBVO[] bvos = (TbGeneralBBVO[]) getbillListPanel()
				.getBodyBillModel().getBodyValueVOs(
						TbGeneralBBVO.class.getName());
		// getbillListPanel().getBodyBillModel().execLoadFormula();
		// getbillListPanel().getBodyItem("traymax").getValueObject();
		if (bvos != null && bvos.length > 0) {
			getBufferData().put(key, arrayToList(bvos));
		} else {
			getBufferData().remove(key);
		}
	}

	public void check() throws BusinessException {
		checkZdtp();
		checkIsNullTp();
		chekcNumBody();
	}

	// 校验没有指定托盘
	public void checkZdtp() throws BusinessException {
		Map<String, List<TbGeneralBBVO>> map = getBufferData();
		int count = getbillListPanel().getHeadBillModel().getRowCount();
		if (map.size() == 0 || map.size() != count) {
			throw new BusinessException("存在未指定托盘的数据!");
		}
	}

	// 校验托盘不能为空&&&校验托盘不允许重复
	public void checkIsNullTp() throws BusinessException {
		// Map<String,String> h = new HashMap<String,String>();
		Map<String, List<TbGeneralBBVO>> map = getBufferData();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<TbGeneralBBVO> l1 = map.get(key);
			if (l1 != null && l1.size() > 0) {
				for (TbGeneralBBVO b : l1) {
					if (b.getCdt_pk() == null || "".equals(b.getCdt_pk()))
						throw new BusinessException("存在托盘为空的情况!");
					// if(h.containsKey(b.getCdt_pk()))
					// throw new BusinessException("指定托盘不允许重复!");
					// else
					// h.put(b.getCdt_pk(), b.getCdt_pk());
				}
			}
		}
	}

	// 控制[表头数量]与[表体数量]汇总值,表体进行赋值
	public void chekcNumBody() throws BusinessException {
		Map<String, List<TbGeneralBBVO>> map = getBufferData();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<TbGeneralBBVO> list = map.get(key);
			UFDouble v = new UFDouble(0);// 实入数量
			UFDouble v1 = new UFDouble(0);// 实入辅数量
			for (TbGeneralBBVO l : list) {
				UFDouble b = l.getGebb_num();// 实入数量
				if (b == null || b.doubleValue() == 0)
					throw new BusinessException("托盘指定实入数量为0或者为空!");
				UFDouble b1 = l.getNinassistnum();// 实入辅数量
				if (b1 == null || b1.doubleValue() == 0)
					throw new BusinessException("托盘指定实入辅数量为0或者为空!");
				v = v.add(b);
				v1 = v1.add(b1);
			}
			TbGeneralBVO bvo = getGenBVO(key);
			if (v.sub(bvo.getGeb_snum()).doubleValue() > 0) {
				throw new BusinessException("托盘指定实入数量大于应收数量!");
			}
		}
		// 校验表体界面中的实入数量是否大于托盘容量
	}

	// 根据行号找VO
	public TbGeneralBVO getGenBVO(String crowno) {
		TbGeneralBVO bvo = null;
		int row = getbillListPanel().getHeadBillModel().getRowCount();
		for (int i = 0; i < row; i++) {
			Object o = getbillListPanel().getHeadBillModel().getValueAt(i,
					"geb_crowno");
			if (o.equals(crowno)) {
				bvo = getHeadBVO(i);
			}
		}
		return bvo;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == getbillListPanel().getHeadTable()
				.getSelectionModel()) {
			// 备份数据
			// 重新加载表体数据
		}
	}

	// 取托盘最大容积
	public void calcMaxTray() {
		// 目前通过公式自动查询出来
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if ("trayname".equals(key)) {// 托盘指定，增加托盘过滤，
			TbGeneralBBVO bvo = getBodyVO(row);
			String subSql = getSubSql(row);
			UIRefPane ref = (UIRefPane) getbillListPanel().getBodyItem(
					"trayname").getComponent();
			ref
					.getRefModel()
					.addWherePart(
							" and bd_cargdoc_tray.cdt_invmandoc = '"
									+ bvo.getPk_invmandoc()
									+ "'  and  bd_cargdoc_tray.cdt_traystatus = 0 and bd_cargdoc_tray.pk_cargdoc = '"
									+ bvo.getPk_cargdoc()
									+ "'  and bd_cargdoc_tray.cdt_pk not in"
									+ subSql
									+ "   or bd_cargdoc_tray.cdt_invmandoc = '"
									+ bvo.getPk_invmandoc()
									+ " '"
									+ "   and bd_cargdoc_tray.pk_cargdoc = '"
									+ bvo.getPk_cargdoc()
									+ " '"
									+ "   and upper(bd_cargdoc_tray.cdt_traycode) like 'XN%' and"
									+ "   bd_cargdoc_tray.cdt_traystatus = 0"
									+ "   and bd_cargdoc_tray.cdt_pk not in"
									+ subSql

					);

		}
		if ("ninassistnum".equalsIgnoreCase(key)) {
			String cdt_id = PuPubVO
					.getString_TrimZeroLenAsNull(getbillListPanel()
							.getBodyBillModel().getValueAt(row, "cdt_pk"));
			if (cdt_id == null) {
				MessageDialog.showErrorDlg(this, "错误", "第 " + (row + 1)
						+ "行，请先选择托盘");
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 获得当前已经使用的托盘ID
	 * @时间：2011-5-4下午02:42:38
	 * @param curRow
	 * @return
	 */
	private String getSubSql(int curRow) {
		StringBuffer sql = new StringBuffer();
		sql.append("('aa'");
		int rowCount = getbillListPanel().getBodyTable().getRowCount();
		for (int i = 0; i < rowCount; i++) {
			if (i == curRow)
				continue;
			String cdt_id = PuPubVO
					.getString_TrimZeroLenAsNull(getbillListPanel()
							.getBodyBillModel().getValueAt(i, "cdt_pk"));// 托盘id
			if (cdt_id == null)
				continue;
			sql.append(",'" + cdt_id + "'");
		}
		sql.append(")");
		return sql.toString();
	}

	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if ("ninassistnum".equals(key)) {
			Object o = getbillListPanel().getBodyBillModel().getValueAt(row,
					"traymax");
			UFDouble traymax = PuPubVO.getUFDouble_NullAsZero(o.toString());// 托盘最大容积
			UFDouble ninassistnum = PuPubVO
					.getUFDouble_NullAsZero(e.getValue());// 实入辅数量
			if (ninassistnum.sub(traymax).doubleValue() > 0) {
				getbillListPanel().getBodyBillModel().setValueAt(null, row,
						"ninassistnum");
				getbillListPanel().getBodyBillModel().setValueAt(null, row,
						"gebb_num");
				MessageDialog.showErrorDlg(this, "错误", "第 " + (row + 1)
						+ "行，托盘存放数量大于托盘最大容积！");
				return;
			}
		}
		if ("trayname".equalsIgnoreCase(key)) {
			String value = e.getValue() == null ? "" : e.getValue().toString();
			if (value.toUpperCase().contains("XN")) {
				getbillListPanel().getBodyBillModel().setValueAt(100000000,
						row, "traymax");
			} else {
				getbillListPanel().getBodyBillModel().execLoadFormulaByKey(
						"pk_invmandoc");
			}
		}
		saveCurrentData(getHeadCurrentRow());
	}

	public ArrayList<TbGeneralBBVO> arrayToList(TbGeneralBBVO[] o) {
		if (o == null || o.length == 0)
			return null;
		ArrayList<TbGeneralBBVO> list = new ArrayList<TbGeneralBBVO>();
		for (TbGeneralBBVO s : o) {
			list.add(s);
		}
		return list;
	}

	public void bodyRowChange(BillEditEvent e) {
		if (e.getSource() == getbillListPanel().getParentListPanel().getTable()) {
			// 备份数据
			int oldrow = e.getOldRow();
			if (oldrow >= 0) {
				saveCurrentData(oldrow);
			}
			// 清空表体数据
			getbillListPanel().getBodyBillModel().clearBodyData();
			// 重新加载表体数据
			int row = e.getRow();
			TbGeneralBVO newbvo = getHeadBVO(row);
			String key2 = newbvo.getGeb_crowno();
			ArrayList<TbGeneralBBVO> list = (ArrayList<TbGeneralBBVO>) getBufferData()
					.get(key2);
			if (list != null && list.size() > 0) {
				getbillListPanel().getBodyBillModel().setBodyDataVO(
						list.toArray(new TbGeneralBBVO[0]));
				CircularlyAccessibleValueObject[] bvos = getbillListPanel()
						.getBodyBillModel().getBodyValueVOs(
								TbGeneralBBVO.class.getName());
				for (int o = 0; o < bvos.length; o++) {//spf add for wds
					String st = (String) bvos[o].getAttributeValue("cdt_pk");
					try {
						st = (String) HYPubBO_Client.findColValue("bd_cargdoc_tray", "cdt_traycode", "cdt_pk = '"+st+"'");
					} catch (UifException e1) {
						e1.printStackTrace();
					}
					if ((st).toUpperCase().contains("XN")) {
						getbillListPanel().getBodyBillModel().execLoadFormula();
						getbillListPanel().getBodyBillModel().setValueAt(
								100000000, row, "traymax");
					}
				}
			}

		}
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
}
