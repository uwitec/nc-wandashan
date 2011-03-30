package nc.ui.dm.plan;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.scm.pub.panel.RelationsCal;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.hi.hi_410.PubDocVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 *  ���˼ƻ�¼�� 
 * @author Administrator
 * 
 */
public class ClientUI extends BillManageUI {

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	public ClientUI(Boolean useBillSource) {
		super(useBillSource);
	}

	public ClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
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

	@Override
	protected void initSelfData() {
		//
		ButtonObject btn = getButtonManager().getButton(IBillButton.Line);
		if (btn != null) {
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.CopyLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.PasteLine));
			btn.removeChildButton(getButtonManager().getButton(
					IBillButton.InsLine));
		}
	}

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("iplantype", 0);//�ƻ�����Ĭ���¼ƻ�
		getBillCardPanel().setHeadItem("vbillstatus", IBillStatus.FREE);
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		getBillCardPanel().setTailItem("voperatorid", _getOperator());
		getBillCardPanel().setHeadItem("pk_billtype", WdsWlPubConst.WDS1);
		getBillCardPanel().setTailItem("dmakedate", _getDate());		
		getBillCardPanel().setHeadItem("pk_outwhouse", //�����ֿ�Ĭ�ϵ�ǰ��¼�˹����Ĳֿ�				
				LoginInforHelper.getLogInfor(_getOperator()).getWhid());
		
		
		
	}

	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, getUIControl());
	}

	// ���ݺ�
	public String getBillNo() throws java.lang.Exception {
		return HYPubBO_Client.getBillNo(getUIControl().getBillType(),
				_getCorp().getPrimaryKey(), null, null);
	}

	protected BusinessDelegator createBusinessDelegator() {
		return new ClientBusinessDelegator(this);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		return super.beforeEdit(e);
	}
		@Override
	public void afterEdit(BillEditEvent e) {
			super.afterEdit(e);
			String key = e.getKey();
			Object value =e.getValue();
			Object oldValue = e.getOldValue();
			if(e.getPos() == BillItem.HEAD){
				if("pk_inwhouse".equalsIgnoreCase(key)){
					Object pk_inwhouse = getBillCardPanel().getHeadItem("pk_inwhouse").getValueObject();
					Object pk_outwhouse=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_outwhouse").getValueObject());
					if(pk_outwhouse == null){
						showErrorMessage("��ǰ��¼��û�а󶨲ֿ�");
					}
					if(pk_outwhouse.equals(pk_inwhouse)){
						showWarningMessage("����ֿⲻ�ܺ͵����ֿ���ͬ");
						getBillCardPanel().setHeadItem("pk_inwhouse", null);
					}
				}
			}else if(e.getPos() == BillItem.BODY){
				//�༭���
				if("invcode".equalsIgnoreCase(key)){
					afterEditWhenAssistUnit(e);
				}else if("nplannum".equalsIgnoreCase(key)){//�༭�ƻ�����
					afterEditNplannum(e);
				}else if("nassplannum".equalsIgnoreCase(key)){//�༭�ƻ�������
					afterEditNassplannum(e);
				}else if("assunitname".equalsIgnoreCase(key)){//�༭��������λ
					afterEditWhenAssunit(e);
				}
			}
	}
		/**
		 * �༭�ƻ�����
		 * liuys add
		 * @param e
		 */
		private void afterEditNplannum(BillEditEvent e) {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// �ƻ�����
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));

			if (hsl == null || nplannum == null) {
//				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			convert = new UFDouble(nplannum).div(new UFDouble(hsl));
			// ���ø���Ϣ ������
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nassplannum");
		}
		/**
		 * liuys  add
		 * �༭�ƻ�������
		 * @param e
		 */
		private void afterEditNassplannum(BillEditEvent e) {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// �ƻ�������
			String nassplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nassplannum"));

			if (hsl == null || nassplannum == null) {
//				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
//				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			convert = new UFDouble(hsl).multiply(new UFDouble(nassplannum));
			// ���ø���Ϣ ������
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "nplannum");
		}
		
		/**
		 * liuys add 
		 * for ���ɽ������Ŀ
		 * 2011-03-30
		 * �������༭�¼� ���� # �����������༭ʱ�������� ==>> ������ | # ѡȡ�ļ���ID��������ID���任����Ϊ1���̶������� | #
		 * �ɡ������ʡ�������ʽ���� > ����ɾ�����ϸ������Ƿ�ɱ༭ # ͬ�����µ���ģ���еĻ����ʺ��Ƿ�̶����������� | 
		 * # ���»����������пɱ༭��
		 * 
		 * @param e
		 *            nc.ui.pub.bill.BillEditEvent
		 */
		private void afterEditWhenAssistUnit(BillEditEvent e) {
			// ���ID
			 UFDouble convert = new UFDouble(0);
			String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
			// ����������
			String sCassId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "assunit"));

			if (sCassId == null) {
				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			// ��ȡ������
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			// ���ø���Ϣ ������
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "hsl");
		}
		
		/**
		 * liuys add
		 * 
		 * �༭��������λ
		 * @param e
		 */
		private void afterEditWhenAssunit(BillEditEvent e) {
			// �Ƿ�̶��仯�ʸı�ʱҪͬ�����ģ�strKeys[0]��ֵ
			boolean isfixed = true;
			// ���ID
			 UFDouble convert = new UFDouble(0);
			String sBaseID = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "pk_invbasdoc"));
			// ����������
			String sCassId = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "assunit"));

			if (sCassId == null) {
				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"hsl");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"nassplannum");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunitname");
				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
						"assunit");
				return;
			}
			// ��ȡ������
			convert = nc.ui.pu.pub.PuTool.getInvConvRateValue(sBaseID, sCassId);
			// �Ƿ�̶�������
			isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
			// ���ø���Ϣ ������
			getBillCardPanel().getBillModel()
					.setValueAt(convert, e.getRow(), "hsl");
			// �ƻ�����
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));
			if(convert == null)
				return;
			if(nplannum != null && convert != null){
				UFDouble nassplannum = new UFDouble(nplannum).div(convert);
				getBillCardPanel().getBillModel().setValueAt(nassplannum, e.getRow(), "nassplannum");
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}