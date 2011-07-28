package nc.ui.wds.tranprice.transcorp;

import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BaseManageEventHandler;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

public class ClientEventHandler extends BaseManageEventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
			// queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	// 必输项校验后的 保存前的校验
	@Override
	protected void beforeSaveValute() {

		// 校验内容
	}

	/**
	 * 前台校验
	 */
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheck();
		super.onBoSave();
	}
	
	/**
	 * 
	 * @作者：yf
	 * @说明：完达山物流项目 
	 * 			校验开始日期和截止日期的一致性，避免交叉日期
	 * @时间：2011-7-19下午12:47:51
	 * @throws Exception
	 */
	private void beforeSaveCheck() throws Exception {
		// 存货 唯一校验
		String sdate = "dstartdate";
		String edate = "denddate";
		CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		checkSDateEDate(vo, sdate, edate);
	}

	private void checkSDateEDate(CircularlyAccessibleValueObject vo,
			String sdate, String edate) throws Exception {
		//校验是否有数据需要校验
		if (null == vo) {
			return;
		}
		if (BeforeSaveValudate.isEmpty(sdate) || BeforeSaveValudate.isEmpty(edate)) {
			throw new BusinessException("校验的字段不允许为空");
		}
		
		//校验 日期不能为空
		Object vsdate = vo.getAttributeValue(sdate);
		Object vedate = vo.getAttributeValue(edate);
		if (BeforeSaveValudate.isEmpty(vsdate) || BeforeSaveValudate.isEmpty(vedate)) {
			throw new BusinessException("日期不允许为空");
		}
		//校验 比较两个日期大小关系
		UFDate ufsdate = (UFDate) vsdate;
		UFDate ufedate = (UFDate) vedate;
		if(ufsdate.compareTo(ufedate) > 0){
			throw new BusinessException("日期录入错误：截止日期必须大于开始日期");
		}
	}


}
