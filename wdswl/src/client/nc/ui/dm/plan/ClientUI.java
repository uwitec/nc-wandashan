package nc.ui.dm.plan;
import javax.swing.JComponent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.wl.pub.WdsBillManagUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 *  ���˼ƻ�¼�� 
 * @author Administrator
 * 
 */
public class ClientUI extends WdsBillManagUI implements BillCardBeforeEditListener {

	private static final long serialVersionUID = -3998675844592858916L;
	
	public ClientUI() {
		super();
	}

	@Override
	protected void initEventListener() {
	
		super.initEventListener();
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
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
		getBillCardPanel().setHeadItem("pk_outwhouse", getLoginInforHelper().getLogInfor(_getOperator()).getWhid());
		//�����ֿ�Ĭ�ϵ�ǰ��¼�˹����Ĳֿ�	
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
	public void afterEdit(BillEditEvent e) {
			super.afterEdit(e);
			String key = e.getKey();
			//�ֿ���ˣ�ֻ�ǽ�������������ʾ����
			if(e.getPos() ==BillItem.HEAD){
				//�ֿ���ˣ�ֻ��������ϵͳ��
				if("pk_inwhouse".equalsIgnoreCase(key)){
					String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_inwhouse").getValueObject());
					String pk_outwhouse=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel().getHeadItem("pk_outwhouse").getValueObject());
					if(pk_outwhouse == null){
						showErrorMessage("��ǰ��¼��û�а󶨲ֿ�");
					}
					if(pk_outwhouse.equals(pk_inwhouse)){
						showWarningMessage("����ֿⲻ�ܺ͵����ֿ���ͬ");
						getBillCardPanel().setHeadItem("pk_inwhouse", null);
					}
				}else if("pk_outwhouse".equalsIgnoreCase(key)){
					JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
					if( c instanceof UIRefPane){
						UIRefPane ref = (UIRefPane)c;
						ref.getRefModel().addWherePart(" def1 = '1' and isnull(dr,0) = 0");
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
		 * @throws Exception 
		 */
		private void afterEditNplannum(BillEditEvent e)  {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// �ƻ�����
			String nplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nplannum"));

//			if (hsl == null || nplannum == null) {
////				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"hsl");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"nassplannum");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunitname");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunit");
//				return;
//			}
			if(hsl==null || "".equals(hsl)){
				this.showErrorMessage("������Ϊ��");
				return ;
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
		 * @throws Exception 
		 */
		private void afterEditNassplannum(BillEditEvent e)  {
			UFDouble convert = new UFDouble(0);
			String hsl = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "hsl"));
			// �ƻ�������
			String nassplannum = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanel()
					.getBillModel().getValueAt(e.getRow(), "nassplannum"));
//
//			if (hsl == null || nassplannum == null) {
////				getBillCardPanel().setCellEditable(e.getRow(), "hsl", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "nassplannum", false);
////				getBillCardPanel().setCellEditable(e.getRow(), "assunitname", false);
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"hsl");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"nassplannum");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunitname");
//				getBillCardPanel().getBillModel().setValueAt(null, e.getRow(),
//						"assunit");
//				return;
//			}
			if(hsl==null || "".equals(hsl)){
				this.showErrorMessage("������Ϊ��");
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
		 * �������༭�¼� ������ # �����������༭ʱ�������� ==>> ������ | # ѡȡ�ļ���ID��������ID���任����Ϊ1���̶������� | #
		 * �ɡ������ʡ�������ʽ���� > ����ɾ������ϸ������Ƿ�ɱ༭ # ͬ�����µ���ģ���еĻ����ʺ��Ƿ�̶����������� | 
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
//			boolean isfixed = true;
			// ���ID
		    UFDouble convert = WdsWlPubTool.DOUBLE_ZERO;
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
//			isfixed = PuTool.isFixedConvertRate(sBaseID, sCassId);
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

	public boolean beforeEdit(BillItemEvent e) {
		String key = e.getItem().getKey();
		if(e.getItem().getPos() ==BillItem.HEAD){
			//�ֿ���ˣ�ֻ��������ϵͳ��
			if("pk_inwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_inwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			if("pk_outwhouse".equalsIgnoreCase(key)){
				JComponent c =getBillCardPanel().getHeadItem("pk_outwhouse").getComponent();
				if( c instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)c;
					ref.getRefModel().addWherePart("  and def1 = '1' and isnull(dr,0) = 0");
				}
			}
			
		}else if(e.getItem().getPos() ==BillItem.BODY){}
		return true;
	}

}