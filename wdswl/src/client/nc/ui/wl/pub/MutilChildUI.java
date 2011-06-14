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
 * 多子表UI,加自定义项
 * 
 */
public abstract class MutilChildUI extends nc.ui.trade.multichild.MultiChildBillManageUI implements ILinkQuery {
		
	private Class bodyVOClass;
	
	private Class aggBillVOClass;
	
	private Class bodyVO1Class;

	public MutilChildUI() {
		super();
		initialize();
	}

	private void initialize() {
		// 初始化设置显示千分位
		initShowThMark(true);
	}

	public MutilChildUI(String pk_corp, String pk_billType,String pk_busitype, String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}

	public MutilChildUI(Boolean useBillSource) {
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
			SuperVO headvo = (SuperVO)getBodyB1Class().newInstance();
			//查询主表数据
			SuperVO[] queryVos = getBusiDelegator().queryHeadAllData(
					getBodyB1Class(),
					getUIControl().getBillType(), " "+headvo.getTableName()+"."+getUIControl().getPkField()+" = '"+id+"' ");

			//查询子表数据
			if(queryVos != null && queryVos.length > 0){
					setCurrentPanel(BillTemplateWrapper.CARDPANEL);
					AggregatedValueObject aggvo = (AggregatedValueObject)getAggVOClass().newInstance();
					aggvo.setParentVO(queryVos[0]);
					getBusiDelegator().setChildData(
								aggvo,
								getBodyVOClass(),
								getUIControl().getBillType(),
								queryVos[0].getPrimaryKey(),null);
					getBufferData().addVOToBuffer(aggvo);
					getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			}
			//
			ButtonObject[] btns = getButtons();
			for (ButtonObject btn : btns) {
				if (("" + (IBillButton.Card)).equals(btn.getTag()) || ("" + (IBillButton.Return)).equals(btn.getTag())) {
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
		//
	}
	private Class getBodyVOClass() throws Exception{
		if(bodyVOClass==null)
			bodyVOClass = Class.forName(getUIControl().getBillVoName()[2]);
		return bodyVOClass;
	}
	
	private Class getAggVOClass()  throws Exception{
		if(aggBillVOClass == null)
				aggBillVOClass = Class.forName(getUIControl().getBillVoName()[0]);
			return aggBillVOClass;
	}
	private Class getBodyB1Class()  throws Exception{
		if( bodyVO1Class == null)
			bodyVO1Class = Class.forName(getUIControl().getBillVoName()[1]);
			return bodyVO1Class ;
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

}
