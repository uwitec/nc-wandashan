package nc.ui.wds.ic.so.out;

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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillEditListener2;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
/**
 * 
 * @author Administrator
 * �������⣨���۳��⣩�ֶ�����Ի���
 */
public class TrayDisposeDlg extends nc.ui.pub.beans.UIDialog implements
		ActionListener, BillEditListener,BillEditListener2{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected OutPubClientUI myClientUI = null;

	private JPanel ivjUIDialogContentPane = null;

	protected BillListPanel ivjbillListPanel = null;

	// ��˾Id
	private String m_pkcorp = null;

	// ������id
	private String m_operator = null;

	// ��������
	private String m_billType = null;

	private UIPanel ivjPanlCmd = null;

	// ȷ����ť
	private UIButton ivjbtnOk = null;

	// ȡ����ť
	private UIButton ivjbtnCancel = null;
	
	private UIButton btn_addline = null;
	
	private UIButton btn_deline = null;
	
	private boolean isEdit = true;
	
	private String pk_cargdoc=null;
	
	private Map<String,List<TbOutgeneralTVO>> map = null;
	

	@SuppressWarnings("deprecation")
	public TrayDisposeDlg(String m_billType, String m_operator,
			String m_pkcorp, OutPubClientUI parent,boolean isEdit) {
		super(parent);
		this.m_billType = m_billType;
		this.m_operator = m_operator;
		this.m_pkcorp = m_pkcorp;
		this.myClientUI = parent;
		this.isEdit = isEdit;
		init();
	}

	private void init() {
		setName("BillSourceUI");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 650);
		setTitle("ѡ������");
		setContentPane(getUIDialogContentPane());
		setEdit();
		addListenerEvent();
		//���ر�ͷ����
		loadHeadData();
	}
	protected JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			ivjUIDialogContentPane.add(getbillListPanel(), BorderLayout.CENTER);
			ivjUIDialogContentPane.add(getPanlCmd(), BorderLayout.SOUTH);
		}
		return ivjUIDialogContentPane;
	}
	
	public void setEdit(){
		getbillListPanel().setEnabled(isEdit);
		getbtnCancel().setEnabled(true);
		getbtnCancel().setEnabled(isEdit);
		getbtnOk().setEnabled(isEdit);
		getAddLine().setEnabled(isEdit);
		getDeline().setEnabled(isEdit);
	}
	
	protected BillListPanel getbillListPanel() {
		if (ivjbillListPanel == null) {
			try {
				ivjbillListPanel = new BillListPanel();
				ivjbillListPanel.setName("billListPanel");
				ivjbillListPanel.loadTemplet(m_billType, null,m_operator, m_pkcorp);
				ivjbillListPanel.getHeadTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//��ѡ
				ivjbillListPanel.getBodyTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);//��ѡ
				ivjbillListPanel.getChildListPanel().setTotalRowShow(true);
				ivjbillListPanel.setEnabled(true);
			} catch (java.lang.Throwable e) {
				Logger.error(e.getMessage(), e);
			}
		}
		return ivjbillListPanel;
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

	// ���ȷ����ť
	private UIButton getbtnOk() {
		if (ivjbtnOk == null) {
			ivjbtnOk = new UIButton();
			ivjbtnOk.setName("btnOk");
			ivjbtnOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/* @res "ȷ��" */);
		}
		return ivjbtnOk;
	}

	// ���ȡ����ť
	private UIButton getbtnCancel() {
		if (ivjbtnCancel == null) {

			ivjbtnCancel = new UIButton();
			ivjbtnCancel.setName("btnCancel");
			ivjbtnCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/* @res "ȡ��" */);
		}
		return ivjbtnCancel;
	}

	public void loadHeadData() {
		try{
			AggregatedValueObject billvo = null;
			OutPubClientUI ui = (OutPubClientUI)myClientUI;
			pk_cargdoc = PuPubVO.getString_TrimZeroLenAsNull(ui.getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject());
			billvo =ui.getVOFromUI();
			if(billvo!=null){
				getbillListPanel().setHeaderValueVO(billvo.getChildrenVO());
				getbillListPanel().getHeadBillModel().execLoadFormula();
				getbillListPanel().getHeadTable().setColumnSelectionInterval(0, 1);
			}
		}catch(Exception e){
			Logger.error(e);
		}
	}
	// ����
	public void addListenerEvent() {
		getbtnOk().addActionListener(this);
		getbtnCancel().addActionListener(this);
		getAddLine().addActionListener(this);
		getDeline().addActionListener(this);
		getbillListPanel().addHeadEditListener(this);
		getbillListPanel().addEditListener(this);
		getbillListPanel().addBodyEditListener(this); // ����༭���¼�����
		getbillListPanel().getBodyScrollPane("tb_outgeneral_t").addEditListener2(this);//��ͷ�༭ǰ����
		}
	
	public UIPanel getIvjPanlCmd() {
		return ivjPanlCmd;
	}
	// �����ť��ļ����¼�
	public void actionPerformed(ActionEvent e) {
		// �ж��Ƿ�Ϊȡ����ť
		if (e.getSource().equals(getbtnCancel())) {
			// �رմ���
			this.closeCancel();
		}
		// �ж��Ƿ�Ϊȷ����ť
		if (e.getSource().equals(getbtnOk())) {
//			int rowcount = getbillListPanel().getBodyBillModel().getRowCount();
//			int[] rows = new int[rowcount];
//			for(int i=0;i<rowcount;i++){
//				rows[i] = 0;
//			}
//			boolean flag  = getbillListPanel().getBodyBillModel().execValidateForumlas(null, new String[]{"shengyuliang"}, rows);
//			if(!flag)
//				return;
			try{
				//ȷ��ǰ���ݺϷ���У��
				validute();
				saveCurrentData(getHeadCurrentRow());
				chekcNumBody();
				
			}catch(Exception e1){
				MessageDialog.showErrorDlg(this, "����", e1.getMessage());
				return;
			}
			this.closeOK();
		}
		else if (e.getSource().equals(getAddLine())) {
			onLineAdd();
		}else if (e.getSource().equals(getDeline())) {
			onLineDel();
		}
	}		
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ
	 *        ����ȷ��ǰ��У��
	 *        ��ҪУ������Ƿ�Ϊ�� 
	 * @ʱ�䣺2011-7-5����02:15:18
	 */
	private void validute() throws Exception{	
		Map<String,List<TbOutgeneralTVO>> map =getBufferData();
        Iterator<String> it= map.keySet().iterator();
        String errorMsg="";
        while(it.hasNext()){
          String key=it.next();
          if(map.get(key)==null || map.get(key).size()==0){
        	  errorMsg=errorMsg+","+key;     	  
          }
        }
        if(!errorMsg.equalsIgnoreCase("")){
       	 throw new BusinessException(" ��ͷ��"+errorMsg.substring(1)+"�еı���Ϊ��");
        }	
	}

	public void saveCurrentData(int row) throws BusinessException{
		if(row<0){
			return;
		}
		TbOutgeneralBVO  bvo = getHeadBVO(row);
		String key = bvo.getCrowno();
		TbOutgeneralTVO[] bvos = (TbOutgeneralTVO[])getbillListPanel().getBodyBillModel().getBodyValueVOs(TbOutgeneralTVO.class.getName());
        if(bvos==null || bvos.length==0){
          throw new BusinessException("�������ݲ�����Ϊ��");	
        }
//		zhf  add 
		 CircularlyAccessibleValueObject[][] os = SplitBillVOs.getSplitVOs(bvos, TbOutgeneralTVO.combin_fields);
		 if(os == null || os.length ==0)
			 throw new BusinessException("���ݴ����쳣");
		 int len = os.length;
		
		 TbOutgeneralTVO[] tmpvos = null;
//		 List<TbOutgeneralTVO> ldata = new ArrayList<TbOutgeneralTVO>();
		 for(int i=0;i<len;i++){
			 tmpvos = (TbOutgeneralTVO[])os[i];
			 if(tmpvos.length>1){
				 throw new BusinessException("ͬһ����ͬһ���δ��ڶ���,�����²���");
			 }
		 }

		for(TbOutgeneralTVO bb:bvos)
			bb.validateOnTP();

		if(bvos!=null && bvos.length>0){
			getBufferData().put(key, arrayToList(bvos));
		}else{
			getBufferData().remove(key);
		}
	}
	
	public ArrayList<TbOutgeneralTVO> arrayToList(TbOutgeneralTVO[] o){
		if(o == null || o.length == 0)
			return null;
		ArrayList<TbOutgeneralTVO> list = new ArrayList<TbOutgeneralTVO>();
		for(TbOutgeneralTVO s : o){
			list.add(s);
		}
		return list;
	}
	//����[��ͷ����]��[��������]����ֵ,������и�ֵ
	public void chekcNumBody() throws BusinessException{
		Map<String,List<TbOutgeneralTVO>> map = getBufferData();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<TbOutgeneralTVO> list  = map.get(key);
			UFDouble v = new UFDouble(0);//ʵ������
			UFDouble v1 = new UFDouble(0);//ʵ��������
			for(TbOutgeneralTVO l :list ){
				UFDouble b = l.getNoutnum();//ʵ������
				if(b==null || b.doubleValue() == 0)
					throw new BusinessException("����ָ��ʵ������Ϊ0����Ϊ��!");
				UFDouble b1 = l.getNoutassistnum();//ʵ��������
				if(b1==null || b1.doubleValue() == 0)
					throw new BusinessException("����ָ��ʵ�븨����Ϊ0����Ϊ��!");
				v = v.add(b);
				v1 = v1.add(b1);
			}
			TbOutgeneralBVO bvo = getGenBVO(key);
			if(v.sub(bvo.getNshouldoutnum()).doubleValue() > 0){
				throw new BusinessException("����ָ��ʵ����������Ӧ������!");
			}
		}
	}
	
	//�����к���VO
	public TbOutgeneralBVO getGenBVO(String crowno){
		TbOutgeneralBVO bvo = null;
		int row = getbillListPanel().getHeadBillModel().getRowCount();
		for(int i = 0 ;i < row; i++){
			Object o = getbillListPanel().getHeadBillModel().getValueAt(i, "crowno");
			if(o.equals(crowno)){
				bvo = getHeadBVO(i);
			}
		}
		return bvo;
	}
	//����
	protected void onLineAdd(){
		getbillListPanel().getBodyBillModel().addLine();
		setBodyDefaultValue(getBodyCurrentRow());
	}
	//ɾ��
	protected void onLineDel(){
		int[] rows = getbillListPanel().getBodyTable().getSelectedRows();
		getbillListPanel().getBodyBillModel().delLine(rows);
	}

	protected int getBodyCurrentRow(){
		int row = getbillListPanel().getBodyTable().getRowCount()-1;
		return row;
	}
	protected int getHeadCurrentRow(){
		int row = getbillListPanel().getHeadTable().getSelectedRow();
		return row;
	}
		
	//���У����Ĭ��ֵ
	public void setBodyDefaultValue(int row){
		TbOutgeneralBVO child = getHeadBVO(getHeadCurrentRow());
		if(child != null){
			TbOutgeneralTVO bbvo = new TbOutgeneralTVO();
//				tbGeneralBBVO.setCdt_pk(cdtvo[0].toString());//ָ������
//				bbvo.setGebb_rowno(String.valueOf((i+1)*10));//�к�
//				bbvo.setPwb_pk(child.getGeb_cgeneralbid());
			bbvo.setGeneral_b_pk(child.getGeneral_b_pk());//�ӱ�����
			bbvo.setGeneral_pk(child.getGeneral_pk());//��������
			bbvo.setPk_invbasdoc(child.getCinvbasid());//�������ID
			bbvo.setPk_invmandoc(child.getCinventoryid());//�������ID
			bbvo.setAunit(child.getCastunitid());//����λ
			bbvo.setUnitid(child.getUnitid());//����λ
			bbvo.setPk_cargdoc(child.getCspaceid());//��λ
			bbvo.setHsl(child.getHsl());// ����
			bbvo.setVbatchcode(child.getVbatchcode());// ����
			bbvo.setLvbatchcode(child.getLvbatchcode());// ��д����
			bbvo.setNprice(child.getNprice());// ����
			bbvo.setNmny(child.getNmny());// ���
			getbillListPanel().getBodyBillModel().setBodyRowVO(bbvo, row);
			getbillListPanel().getBodyBillModel().execLoadFormula();
		}
	}
	protected TbOutgeneralBVO getHeadBVO(int row){
		TbOutgeneralBVO vo  = (TbOutgeneralBVO)getbillListPanel().getHeadBillModel().getBodyValueRowVO(row, TbOutgeneralBVO.class.getName());
		return vo;
	}

	// �༭���¼�
	public void afterEdit(BillEditEvent e) {
		int row = e.getRow();
		//���̱༭�󣬴�����������Ϳ�渨����
		if("ctuopanbianma".equalsIgnoreCase(e.getKey())){
			JComponent jc = getbillListPanel().getBodyItem("ctuopanbianma").getComponent();
			if( jc instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)jc;
				getbillListPanel().getBodyBillModel().setValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stocktonnage"), row, "stocktonnage");
				getbillListPanel().getBodyBillModel().setValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_stockpieces"), row, "stockpieces");
				//�༭���¼��������̲�����ȡ���κţ������帳ֵ
				getbillListPanel().getBodyBillModel().setValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_batchcode"), row, "vbatchcode");	
				//�༭���¼��������̲�����ȡ��Դ���κţ������帳ֵ
				getbillListPanel().getBodyBillModel().setValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_lbatchcode"), row, "lvbatchcode");
//				���״̬��ID
				getbillListPanel().getBodyBillModel().setValueAt(ref.getRefModel().getValue("tb_warehousestock.whs_pk"), row, "whs_pk");
				
			}
		}else if("noutassistnum".equalsIgnoreCase(e.getKey())){
			UFDouble nshengyu = PuPubVO.getUFDouble_NullAsZero(getbillListPanel().getBodyBillModel().getValueAt(e.getRow(), "shengyuliang"));
			if(nshengyu.compareTo(new UFDouble(0))<0){
				MessageDialog.showErrorDlg(this, "����", "���̴�������");
			}
		}
}

	public void bodyRowChange(BillEditEvent e) {
		if(e.getSource() == getbillListPanel().getParentListPanel().getTable()){
			//��������
			int oldrow = e.getOldRow();
			if(oldrow >= 0){
				TbOutgeneralBVO bvo = getHeadBVO(oldrow);
				String key = bvo.getCrowno();
				TbOutgeneralTVO[] bvos = (TbOutgeneralTVO[])getbillListPanel().getBodyBillModel().getBodyValueVOs(TbOutgeneralTVO.class.getName());
				getBufferData().put(key, arrayToList(bvos));
			}
			//��ձ�������
			getbillListPanel().getBodyBillModel().clearBodyData();
			//���¼��ر�������
			int row = e.getRow();
			TbOutgeneralBVO newbvo = getHeadBVO(row);
			String key2 = newbvo.getCrowno();
			List<TbOutgeneralTVO> list = getBufferData().get(key2);
			if(list !=null && list.size() > 0){
				getbillListPanel().getBodyBillModel().setBodyDataVO(list.toArray(new TbOutgeneralTVO[0]));
				getbillListPanel().getBodyBillModel().execLoadFormula();
			}
		}
	}
	
	public boolean beforeEdit(BillEditEvent e) {
		String key = e.getKey();
		int row = e.getRow();
		TbOutgeneralBVO child = getHeadBVO(getHeadCurrentRow());
		String invtoryid = child.getCinventoryid();
		String vbatchcode = child.getVbatchcode();
		String subsql=getSubSql(e.getRow());
		if("ctuopanbianma".equals(key)){//����ָ�����������̹��ˣ�
			if (pk_cargdoc == null){
				MessageDialog.showWarningDlg(this, "", "����ѡ���ͷ��λ");
				return false;
			}
			UIRefPane ref = (UIRefPane)getbillListPanel().getBodyItem("ctuopanbianma").getComponent();			
			ref.getRefModel().addWherePart(" and isnull(bd_cargdoc_tray.cdt_traystatus,0) =1 and" +
					" tb_warehousestock.whs_stocktonnage > 0" +
					" and tb_warehousestock.pk_invmandoc ='"+invtoryid+"' and bd_cargdoc_tray.pk_cargdoc='"+pk_cargdoc+"'" +
					" and bd_cargdoc_tray.cdt_pk not in "+subsql +
					" or isnull(bd_cargdoc_tray.cdt_traystatus,0) =1 "+
					" and tb_warehousestock.whs_stocktonnage > 0"+
					" and tb_warehousestock.pk_invmandoc ='"+invtoryid+"' and bd_cargdoc_tray.pk_cargdoc='"+pk_cargdoc+"'" +
					" and bd_cargdoc_tray.cdt_traycode like 'XN%'"+
					" and isnull(bd_cargdoc_tray.dr,0)=0 "+
	    	        " and isnull(tb_warehousestock.dr,0)=0 "+	    	
	    	        " and isnull(bd_invmandoc.dr,0)=0 "+
	                " and isnull(bd_invbasdoc.dr,0)=0 "+	    	
	    	        " and tb_warehousestock.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
		
		}
		return true;
	}
	//Ҫ���˵�����
	private String getSubSql( int curRow ){
		StringBuffer sql = new StringBuffer();
		sql.append("('aa'");
		int rowCount = getbillListPanel().getBodyTable().getRowCount();
		for(int i=0;i<rowCount;i++){
			if(i == curRow)
				continue;
			String cdt_id =PuPubVO.getString_TrimZeroLenAsNull(getbillListPanel().getBodyBillModel().getValueAt(i, "cdt_pk"));//����id
			if(cdt_id == null)
				continue;
			sql.append(",'"+cdt_id+"'");
		}
		sql.append(")");
		return sql.toString();
	}
	//Ҫ���˵���������
	private String getSubSql1(int curRow){
		
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
	
	
//	public Map<String,List<TbOutgeneralTVO>> getBufferData(){
//		map = ((OutPubClientUI)myClientUI).getTrayInfor();
//		if(map == null){
//			map = new HashMap<String,List<TbOutgeneralTVO>>();
//		}
//		return map;
//	}
	public Map<String,List<TbOutgeneralTVO>> getBufferData(){
		if(map == null){
			map = cloneBufferData();
		}
		return map;
	}
	public Map<String,List<TbOutgeneralTVO>> cloneBufferData(){
		Map<String,List<TbOutgeneralTVO>> map1 = ((OutPubClientUI)myClientUI).getTrayInfor();
		Map<String,List<TbOutgeneralTVO>> map2 = new HashMap<String, List<TbOutgeneralTVO>>();
		if(map1.size()>0){
			Iterator<String> it = map1.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				List<TbOutgeneralTVO> list = map1.get(key);
				map2.put(key, cloneBBVO(list));
			}	
		}
		return map2;
	}
	public List<TbOutgeneralTVO> cloneBBVO(List<TbOutgeneralTVO> list){
		List<TbOutgeneralTVO> list1 = new ArrayList<TbOutgeneralTVO>();
		if(list!=null&& list.size()>0){
			for(TbOutgeneralTVO b:list){
				list1.add((TbOutgeneralTVO)b.clone());
			}
		}
		return list1;
	}



}
