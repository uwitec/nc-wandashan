package nc.ui.wl.pub;
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
/**
 * ���ֱ�ui(ʵ��������)
 * @author mlr
 *
 */
public abstract class MutilChildUI extends nc.ui.trade.multichild.MultiChildBillManageUI implements ILinkQuery{

	
	private Class aggBillVOClass;
	
	private Class headVOClass;
	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public MutilChildUI() {
		super();
		initialize();
	}

	private void initialize() {
		// ��ʼ��������ʾǧ��λ
		initShowThMark(true);
	}

	public MutilChildUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public MutilChildUI(Boolean useBillSource) {
		super();
		// ��ʼ��������ʾǧ��λ
		initShowThMark(true);
	}

	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)throws Exception {
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,int intRow) throws Exception {
	}

	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
	}

	@Override
	protected void initSelfData() {
	}

	@Override
	public void setDefaultData() throws Exception {

	}
	/**
	 * ���鶯��
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		try {
			if(querydata == null)
				return;
			String id = querydata.getBillID();
			if(id == null || "".equals(id))
				return;
			//��ѯ��������
			SuperVO headvo = (SuperVO)getHeadClass().newInstance();
			//��ѯ��������
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getHeadClass(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+headvo.getPKFieldName()+" = '"+id+"' ");
			
			//��ѯ�ӱ�����
			if(queryVos != null && queryVos.length > 0){
					setCurrentPanel(BillTemplateWrapper.CARDPANEL);
					AggregatedValueObject aggvo = (AggregatedValueObject)getAggVOClass().newInstance();
					aggvo.setParentVO(queryVos[0]);
					getBufferData().clear();
					getBufferData().addVOToBuffer(aggvo);
					getBufferData().setCurrentRow(0);
			}
			ButtonObject[] btns = getButtons();
			for (ButtonObject btn : btns) {
				btn.setEnabled(true);
				btn.setVisible(true);
			}
			updateButtons();
			
		}  catch (Exception e) {
			Logger.error(e);
		}
	
	}
	private Class getAggVOClass()  throws Exception{
		if(aggBillVOClass == null)
				aggBillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			return aggBillVOClass;
	}
	private Class getHeadClass()  throws Exception{
		if( headVOClass == null)
			headVOClass = Class.forName(getUIControl().getBillVoName()[1]);
			return headVOClass ;
	}
	/**
	 * ǧ��λ����
	 */
	protected void initShowThMark(boolean isShow) {
		try {
			// ��Ƭ��ͷ
			getBillCardPanel().setHeadTailShowThMark(getBillCardPanel().getHeadItems(), isShow);
			// �б��ͷ
			getBillListPanel().getParentListPanel().setShowThMark(isShow);
			// ����
			if (getTableCodes() != null && getTableCodes().length > 0) {
				for (int i = 0; i < getTableCodes().length; i++) {
					if (getBillListPanel().getBodyScrollPane(getTableCodes()[i]) != null) {
						// �б����
						getBillListPanel().getBodyScrollPane(getTableCodes()[i]).setShowThMark(isShow);
						// ��Ƭ����
						getBillCardPanel().setShowThMark(getTableCodes()[i],isShow);
					}
				}
			}
		} catch (Exception e) {
			// �б����Ϊ��ʱ�����׿�ָ���쳣
			Logger.debug("ǧ��λ��ʼ��ʱ�����쳣�������Ǵ���");
		}
	}

	public String[] getTableCodes() {
		return null;
	}

}
