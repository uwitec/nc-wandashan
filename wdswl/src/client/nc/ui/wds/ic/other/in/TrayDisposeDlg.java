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
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.wds.ic.pub.InPubClientUI;
import nc.ui.wds.ic.pub.MutiInPubClientUI;
import nc.ui.wds.tray.lock.LockTrayDialog;
import nc.ui.wl.pub.MutiChildForOutInUI;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.cargtray.SmallTrayVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

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

	private MutiInPubClientUI myClientUI;

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
	
//	zhf add �����������̵İ�ʵ�����̹��ܰ�ť   ��
	private UIButton ivjbtnLock = null;

	private Map<String, List<TbGeneralBBVO>> map = null;

	private boolean isEdit = true;
	
	private String pk_ware = null;//�ֿ�
	
	private boolean isSign = false;//�Ƿ�ǩ��ͨ��

	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, String m_nodeKey, MutiInPubClientUI myClientUI,
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
		setTitle("ѡ������");
		setContentPane(getUIDialogContentPane());
		// ���ñ༩״̬
		setEdit();
		//
		getbtnOk().addActionListener(this);
		getbtnLock().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getAddLine().addActionListener(this);
		getDeline().addActionListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this);
		getbillListPanel().getHeadTable().getSelectionModel()
				.addListSelectionListener(this);
		getbillListPanel().getBodyScrollPane("tb_general_b_b")
				.addEditListener2(this);
		// ���ر�ͷ����
		loadHeadData();
	}

	public void setEdit() {
		getbillListPanel().setEnabled(isEdit);
		getbtnCancel().setEnabled(true);
		getbtnCancel().setEnabled(isEdit);
		getbtnOk().setEnabled(isEdit);
		getAddLine().setEnabled(isEdit);
		getDeline().setEnabled(isEdit);
		
		getbtnLock().setEnabled(isEdit||!isSign);
	}

	public void loadHeadData() {
		try {
			OtherInBillVO billvo = null;
			if(isEdit){
				billvo = (OtherInBillVO) myClientUI.getVOFromUI();
			}else{
				billvo = (OtherInBillVO)myClientUI.getBufferData().getCurrentVO();
			}
			
			if (billvo != null) {
				getbillListPanel().setHeaderValueVO(billvo.getChildrenVO());
				getbillListPanel().getHeadBillModel().execLoadFormula();
				pk_ware = PuPubVO.getString_TrimZeroLenAsNull(billvo.getParentVO().getAttributeValue("geh_cwarehouseid"));
				isSign = PuPubVO.getInteger_NullAs(billvo.getParentVO().getAttributeValue("pwb_fbillflag"),IBillStatus.FREE)==IBillStatus.CHECKPASS;
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	// ���У����Ĭ��ֵ
	public void setBodyDefaultValue(int row) {
		TbGeneralBVO child = getHeadBVO(getHeadCurrentRow());
		if (child != null) {
			TbGeneralBBVO bbvo = new TbGeneralBBVO();
			// tbGeneralBBVO.setCdt_pk(cdtvo[0].toString());//ָ������
			// bbvo.setGebb_rowno(String.valueOf((i+1)*10));//�к�
			bbvo.setGeb_pk(child.getGeb_pk());// �ӱ�����
			bbvo.setGeh_pk(child.getGeh_pk());// ��������
			bbvo.setPk_invbasdoc(child.getGeb_cinvbasid());// �������ID
			bbvo.setPk_invmandoc(child.getGeb_cinventoryid());// �������ID
			bbvo.setAunit(child.getCastunitid());// ����λ
			bbvo.setUnitid(child.getPk_measdoc());// ����λ
			bbvo.setCreadate(child.getGeb_dbizdate());//��������
		    bbvo.setExpdate(child.getGeb_dvalidate());//ʧЧ����
			bbvo.setPk_cargdoc(child.getGeb_space());// ��λ
			bbvo.setGebb_hsl(child.getGeb_hsl());// ������
			bbvo.setGebb_vbatchcode(child.getGeb_vbatchcode());// ����
			bbvo.setGebb_lvbatchcode(child.getGeb_backvbatchcode());// ��д����
			bbvo.setGebb_nprice(child.getGeb_nprice());// ����
			bbvo.setGebb_nmny(child.getGeb_nmny());// ���
			getbillListPanel().getBodyBillModel().setBodyRowVO(bbvo, row);
			getbillListPanel().getBodyBillModel().execLoadFormula();
//			CircularlyAccessibleValueObject[] bvos = getbillListPanel().getBodyBillModel().getBodyValueVOs(TbGeneralBBVO.class.getName());
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

	// ����
	protected void onLineAdd() {
		getbillListPanel().getBodyBillModel().addLine();
		setBodyDefaultValue(getBodyCurrentRow());
		//�ı�������������
	   int length=getbillListPanel().getBodyBillModel().getRowCount();
		if(length<=0){
			return ;
		}	
		for(int i=0;i<length;i++){
			Object trayName= getbillListPanel().getBodyBillModel().getValueAt(i, "trayname");		
			//��������������������		
			setXNNum(trayName,i);			
		}
	}

	/**
	 * 
	 * @���ߣ�mlr
	 * @˵��������������������
	 * @ʱ�䣺2011-4-7����02:28:26
	 * @param newBillVo  ������ ��浥��vo
	 * @param oldBillVo  ����ǰ��浥��vo
	 * @throws BusinessException
	 */
	private void setXNNum(Object trayName,int i) {
		if(trayName!=null && trayName instanceof String ){
			String s=(String)trayName;
			if(s.length()<WdsWlPubConst.XN_CARGDOC_TRAY_NAME.length()){
				return;
			}
			if(s.substring(0,WdsWlPubConst.XN_CARGDOC_TRAY_NAME.length()).equalsIgnoreCase(WdsWlPubConst.XN_CARGDOC_TRAY_NAME)){
				getbillListPanel().getBodyBillModel().setValueAt(WdsWlPubConst.XN_CARGDOC_TRAY_VO, i, "traymax");
			}			
		}	
	}

	// ɾ��
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
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// ��ѡ
				ivjbillListPanel.getBodyTable().setSelectionMode(
						ListSelectionModel.SINGLE_INTERVAL_SELECTION);// ��ѡ
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
			ivjPanlCmd.add(getbtnLock(),getbtnLock().getName());
		}
		return ivjPanlCmd;
	}

	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText("ȷ��");
		}
		return ivjbtnOk;
	}

	private UIButton getAddLine() {
		if (btn_addline == null) {
			btn_addline = new UIButton();
			btn_addline.setName("addline");
			btn_addline.setText("����");
		}
		return btn_addline;
	}

	private UIButton getDeline() {
		if (btn_deline == null) {
			btn_deline = new UIButton();
			btn_deline.setName("deline");
			btn_deline.setText("ɾ��");
		}
		return btn_deline;
	}

	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {
			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText("ȡ��");
		}
		return ivjbtnCancel;
	}
	
	// ��Ӱ󶨰�ť
	private UIButton getbtnLock() {
		if (ivjbtnLock == null) {
			ivjbtnLock = new UIButton();
			ivjbtnLock.setName("ivjbtnLock");
			ivjbtnLock.setText("��");
		}
		return ivjbtnLock;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getbtnOk())) {
			try {
				saveCurrentData(getHeadCurrentRow());
				check();
				closeOK();
			} catch (Exception e1) {
				MessageDialog.showErrorDlg(this, "����", e1.getMessage());
			}
		} else if (e.getSource().equals(getbtnCancel())) {
			closeCancel();
		} else if (e.getSource().equals(getAddLine())) {
			onLineAdd();
		} else if (e.getSource().equals(getDeline())) {
			onLineDel();
		}else if(e.getSource().equals(getbtnLock())){
			onLock();
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

	// У��û��ָ������
	public void checkZdtp() throws BusinessException {
		Map<String, List<TbGeneralBBVO>> map = getBufferData();
		int count = getbillListPanel().getHeadBillModel().getRowCount();
		if (map.size() == 0 || map.size() != count) {
			throw new BusinessException("����δָ�����̵�����!");
		}
	}

	// У�����̲���Ϊ��&&&У�����̲������ظ�
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
						throw new BusinessException("��������Ϊ�յ����!");
					// if(h.containsKey(b.getCdt_pk()))
					// throw new BusinessException("ָ�����̲������ظ�!");
					// else
					// h.put(b.getCdt_pk(), b.getCdt_pk());
				}
			}
		}
	}

	// ����[��ͷ����]��[��������]����ֵ,������и�ֵ
	public void chekcNumBody() throws BusinessException {
		Map<String, List<TbGeneralBBVO>> map = getBufferData();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<TbGeneralBBVO> list = map.get(key);
			UFDouble v = new UFDouble(0);// ʵ������
			UFDouble v1 = new UFDouble(0);// ʵ�븨����
			for (TbGeneralBBVO l : list) {
				UFDouble b = l.getGebb_num();// ʵ������
				if (b == null || b.doubleValue() == 0)
					throw new BusinessException("����ָ��ʵ������Ϊ0����Ϊ��!");
				UFDouble b1 = l.getNinassistnum();// ʵ�븨����
				if (b1 == null || b1.doubleValue() == 0)
					throw new BusinessException("����ָ��ʵ�븨����Ϊ0����Ϊ��!");
				v = v.add(b);
				v1 = v1.add(b1);
			}
			TbGeneralBVO bvo = getGenBVO(key);

			if(v1.sub(bvo.getGeb_bsnum()).doubleValue() > 0){
				throw new BusinessException("����ָ��ʵ����������Ӧ������!");
			}

		}
		// У���������е�ʵ�������Ƿ������������
	}

	// �����к���VO
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
			// ��������
			// ���¼��ر�������
		}
	}

	// ȡ��������ݻ�
	public void calcMaxTray() {
		// Ŀǰͨ����ʽ�Զ���ѯ����
	}

	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		if ("trayname".equals(key)) {// ����ָ�����������̹��ˣ�
			TbGeneralBBVO bvo = getBodyVO(row);
			String subSql = getSubSql(row);
			UIRefPane ref = (UIRefPane) getbillListPanel().getBodyItem(
			"trayname").getComponent();
			if(!WdsWlPubTool.isZc(pk_ware)){
				//zhf add  �ֲ� ����
				ref.getRefModel().addWherePart(
								//" and  bd_cargdoc_tray.cdt_traystatus = "+StockInvOnHandVO.stock_state_null
								" and bd_cargdoc_tray.pk_cargdoc = '"
										+ bvo.getPk_cargdoc()
										+ "'  and bd_cargdoc_tray.cdt_pk not in"
										+ subSql										

						);
			}else{
				ref.getRefModel().addWherePart(
						" and bd_cargdoc_tray.cdt_invmandoc = '"
								+ bvo.getPk_invmandoc()
								+ "'  and  bd_cargdoc_tray.cdt_traystatus = "+StockInvOnHandVO.stock_state_null
								+" and bd_cargdoc_tray.pk_cargdoc = '"
								+ bvo.getPk_cargdoc()
								+ "'  and bd_cargdoc_tray.cdt_pk not in"
								+ subSql
								+ "   or bd_cargdoc_tray.cdt_invmandoc = '"
								+ bvo.getPk_invmandoc()
								+ " '"
								+ "   and bd_cargdoc_tray.pk_cargdoc = '"
								+ bvo.getPk_cargdoc()
								+ " '"
								+ "   and upper(bd_cargdoc_tray.cdt_traycode) like 'XN%'"
								+"    and isnull(bd_cargdoc_tray.dr,0)=0"
								+ "   and bd_cargdoc_tray.cdt_pk not in"
								+ subSql

				);
			}
		}
		if ("ninassistnum".equalsIgnoreCase(key)) {
			String cdt_id = PuPubVO
					.getString_TrimZeroLenAsNull(getbillListPanel()
							.getBodyBillModel().getValueAt(row, "cdt_pk"));
			if (cdt_id == null) {
				MessageDialog.showErrorDlg(this, "����", "�� " + (row + 1)
						+ "�У�����ѡ������");
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ��õ�ǰ�Ѿ�ʹ�õ�����ID
	 * @ʱ�䣺2011-5-4����02:42:38
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
							.getBodyBillModel().getValueAt(i, "cdt_pk"));// ����id
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
			UFDouble traymax = PuPubVO.getUFDouble_NullAsZero(o.toString());// ��������ݻ�
			UFDouble ninassistnum = PuPubVO
					.getUFDouble_NullAsZero(e.getValue());// ʵ�븨����
			if (ninassistnum.sub(traymax).doubleValue() > 0) {
				getbillListPanel().getBodyBillModel().setValueAt(null, row,
						"ninassistnum");
				getbillListPanel().getBodyBillModel().setValueAt(null, row,
						"gebb_num");
				MessageDialog.showErrorDlg(this, "����", "�� " + (row + 1)
						+ "�У����̴������������������ݻ���");
				return;
			}
		}
		if ("trayname".equalsIgnoreCase(key)) {
			String value = e.getValue() == null ? "" : e.getValue().toString();
					
			if (value.toUpperCase().startsWith(WdsWlPubConst.XN_CARGDOC_TRAY_NAME)) {
				setXNNum(value, row);
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
			// ��������
			int oldrow = e.getOldRow();
			if (oldrow >= 0) {
				saveCurrentData(oldrow);
			}
			// ��ձ�������
			getbillListPanel().getBodyBillModel().clearBodyData();
			// ���¼��ر�������
			int row = e.getRow();
			TbGeneralBVO newbvo = getHeadBVO(row);
			String key2 = newbvo.getGeb_crowno();

			ArrayList<TbGeneralBBVO> list = (ArrayList<TbGeneralBBVO>)getBufferData().get(key2);
			if(list !=null && list.size() > 0){
				getbillListPanel().getBodyBillModel().setBodyDataVO(list.toArray(new TbGeneralBBVO[0]));			

				getbillListPanel().getBodyBillModel().execLoadFormula();
				//�����������̵��������
				TbGeneralBBVO [] bos=list.toArray(new TbGeneralBBVO[0]);
				for(int i=0;i<bos.length;i++){		    
					Object trayName= getbillListPanel().getBodyBillModel().getValueAt(i, "trayname");
					setXNNum(trayName,i);
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
	
	private String getkey(int row){
		return WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "cdt_pk"))+","+
		WdsWlPubTool.getString_NullAsTrimZeroLen(getBodyValue(row, "gebb_vbatchcode"));
	}
	
	private Object getBodyValue(int row,String fieldname){
		return getbillListPanel().getBodyBillModel().getValueAt(row, fieldname);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �ֹ����ʱ  ����������� ʱ �� ʵ������
	 * ���߼�������ѡ��  ������   ѡ�е��е����̱�����  ��������
	 * @ʱ�䣺2011-7-4����05:07:43
	 */
	private void onLock(){
//		�ж��Ƿ�ѡ����
		int row = getbillListPanel().getBodyTable().getSelectedRow();
		if(row < 0){
			MessageDialog.showWarningDlg(this, "����", "δѡ�б�����");
			return;
		}			
//		�ж�ѡ�����Ƿ�Ϊ��������
		String traycode = PuPubVO.getString_TrimZeroLenAsNull(getBodyValue(row,"trayname"));
		
		if(traycode == null){
			MessageDialog.showWarningDlg(this, "����", "���̱��벻��Ϊ��");
			return;
		}
		if(!traycode.substring(0,2).equalsIgnoreCase(WdsWlPubConst.XN_CARGDOC_TRAY_NAME)){
			MessageDialog.showWarningDlg(this, "����", "������������");
			return;
		}
//		�����ݰ���Ϣ ������
		int retFlag = getLockTrayDialog().showModal();
		if(retFlag != UIDialog.ID_OK)
			return;
		if(isEdit){
			SmallTrayVO[] trays = getLockTrayDialog().getRetVos();			String key = getkey(row);			getTrayLockInfor(false).put(key, trays);
		}
//		��λ ��֤ �´� ����ʱ  ���¼�������
		getLockTrayDialog().reset();
	}
	
	private LockTrayDialog lockDlg = null;
	private LockTrayDialog getLockTrayDialog(){
		if(lockDlg == null){
			lockDlg = new LockTrayDialog(myClientUI,this,getbillListPanel(),pk_ware,isEdit());
		}
		return lockDlg;
	}
	
	private Object getHeadValue(String fieldname){
		if(PuPubVO.getString_TrimZeroLenAsNull(fieldname)==null)
			return null;
		int row = getbillListPanel().getHeadTable().getSelectedRow();
		if(row < 0)
			return null;
		return getbillListPanel().getHeadBillModel().getValueAt(row, fieldname);
	}	

	private Map<String,SmallTrayVO[]> trayLockInfor = null;
	public Map<String,SmallTrayVO[]> getTrayLockInfor(boolean isclear){
		if(trayLockInfor == null){
			trayLockInfor = new HashMap<String, SmallTrayVO[]>();
		}
		if(isclear)
			trayLockInfor.clear();
		return trayLockInfor;
	}
}
