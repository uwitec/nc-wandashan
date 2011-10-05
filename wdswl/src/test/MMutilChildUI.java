
import nc.bs.logging.Logger;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.button.IBillButton;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.button.ButtonVO;
/**
 * 多字表ui(实现了联查)
 * @author mlr
 */
public abstract class MMutilChildUI extends nc.ui.trade.multichild.MultiChildBillManageUI implements ILinkQuery{

	private static final long serialVersionUID = 3644877813886240358L;

	private Class aggBillVOClass;
	
	private Class headVOClass;
	
	public MMutilChildUI() {
		super();
		initialize();
	}

	private void initialize() {
		// 初始化设置显示千分位
		initShowThMark(true);
	}

	public MMutilChildUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public MMutilChildUI(Boolean useBillSource) {
		super();
		// 初始化设置显示千分位
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
	 * 联查动作
	 */
	public void doQueryAction(ILinkQueryData querydata) {
		try {
			if(querydata == null)
				return;
			String id = querydata.getBillID();
			if(id == null || "".equals(id))
				return;
			//查询主表数据
			SuperVO headvo = (SuperVO)getHeadClass().newInstance();
			//查询主表数据
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getHeadClass(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");
			
			//查询子表数据
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
				if (("" + (IBillButton.Print)).equals(btn.getTag())) {
					btn.setEnabled(true);
					btn.setVisible(true);
				} else {
					btn.setEnabled(false);
					btn.setVisible(false);
				}
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
	 * 千分位处理
	 */
	protected void initShowThMark(boolean isShow) {
		try {
			// 卡片表头
			getBillCardPanel().setHeadTailShowThMark(getBillCardPanel().getHeadItems(), isShow);
			// 列表表头
			getBillListPanel().getParentListPanel().setShowThMark(isShow);
			// 表体
			if (getTableCodes() != null && getTableCodes().length > 0) {
				for (int i = 0; i < getTableCodes().length; i++) {
					if (getBillListPanel().getBodyScrollPane(getTableCodes()[i]) != null) {
						// 列表表体
						getBillListPanel().getBodyScrollPane(getTableCodes()[i]).setShowThMark(isShow);
						// 卡片表体
						getBillCardPanel().setShowThMark(getTableCodes()[i],isShow);
					}
				}
			}
		} catch (Exception e) {
			// 列表表体为空时，会抛空指针异常
			Logger.debug("千分位初始化时出现异常，但不是错误");
		}
	}

	public String[] getTableCodes() {
		return null;
	}
	@Override
	protected void initPrivateButton() {
		super.initPrivateButton();
		CommonButtonDef def = new CommonButtonDef();
		ButtonVO  joinup = def.getJoinUPButton();
		addPrivateButton(joinup);
	}
	private class CommonButtonDef {
		//定义联查按钮
		public ButtonVO getJoinUPButton() {
			ButtonVO btnVo = new ButtonVO();
			btnVo.setBtnNo(999);
			btnVo.setBtnCode("joinup");
			btnVo.setBtnName("联查");
			btnVo.setBtnChinaName("联查");
			btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
			return btnVo;
		}		
	}
}
