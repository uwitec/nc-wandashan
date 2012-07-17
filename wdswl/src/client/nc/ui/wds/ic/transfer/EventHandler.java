package nc.ui.wds.ic.transfer;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.pub.OutPubClientUI;
import nc.ui.wds.ic.pub.OutPubEventHandler;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.transfer.MyBillVO;
import nc.vo.wds.transfer.TransferBVO;
import nc.vo.wds.transfer.TransferVO;
import nc.vo.wl.pub.BillRowNo;
import nc.vo.wl.pub.WdsWlPubConst;


/**
 * 转货位
 * @author yf
 */
public class EventHandler extends OutPubEventHandler {

	public EventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		switch (intBtn) {
		case ISsButtun.zdqh:
			onzdqh();
			break;
		}
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 出库单自动拣货出库时 校验数据
	 * @时间：2011-4-3下午03:18:13
	 * @param card
	 * @param bodys
	 * @throws ValidationException
	 */
	public static void validationOnPickAction(BillCardPanel card,TransferBVO[] bodys) throws ValidationException{
		if (null == bodys || bodys.length == 0) {
			throw new ValidationException("表体无货品数据");
		}
		Object pk_stordoc = card.getHeadItem("srl_pk")
		.getValueObject();
		if (null == pk_stordoc || "".equals(pk_stordoc)) {
			throw new ValidationException("出库仓库不能为空");
		}
		
		String pk_cargdoc = PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("pk_cargdoc").getValueObject());
		if(pk_cargdoc == null){
			throw new ValidationException("出库货位不能为空");
		}
		for(TransferBVO body:bodys){
			body.validationOnZdck();
		}
	}

	/*
	 * mlr
	 * 自动取货(non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	protected void onzdqh() throws Exception {
		int results = ui.showOkCancelMessage("确认自动拣货?");
		if (results != 1) {
			return;
		}
		if (getBillUI().getVOFromUI() == null|| getBillUI().getVOFromUI().getChildrenVO() == null|| getBillUI().getVOFromUI().getChildrenVO().length == 0) {ui.showErrorMessage("获取当前界面数据出错.");
			return;
		}
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TransferBVO[] generalbVOs = (TransferBVO[]) billvo.getChildrenVO();
		// 数据校验 begin
	   validationOnPickAction(getBillCardPanelWrapper().getBillCardPanel(), generalbVOs);
		// 数据校验end
		String pk_stordoc = PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("srl_pk").getValueObject());
		
		String pk_cargdoc=PuPubVO.getString_TrimZeroLenAsNull(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject());
		TransferBVO[] bvos=null;
		try {
			Class[] ParameterTypes = new Class[] { String.class,String.class,TransferBVO[].class };
			Object[] ParameterValues = new Object[] {pk_stordoc,
					pk_cargdoc, generalbVOs };
			Object o = LongTimeTask.callRemoteService(
					WdsWlPubConst.WDS_WL_MODULENAME,
					"nc.vo.wdsnew.pub.PickTool", "autoPick1",
					ParameterTypes, ParameterValues, 2);
			if (o != null) {
				bvos = (TransferBVO[]) o;
			}
		} catch (Exception e) {
			throw e;
		}
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyDataVO(bvos);
        getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormula();
	}

	@Override
	protected void onBoSave() throws Exception {
		valudate();
		super.onBoSave();
	}
	/**
	 * 保存前进行数据校验
	 * @throws Exception 
	 * @throws BusinessException 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-13下午07:33:33
	 */
	private void valudate() throws Exception {
		
		getBillCardPanelWrapper().getBillCardPanel().stopEditing();

		MyBillVO bill=(MyBillVO) getBillUI().getVOFromUI();
		
		if(bill == null)
			throw new BusinessException("数据异常");
		
		TransferVO head = (TransferVO)bill.getParentVO();
		
		if(head == null)
			throw new BusinessException("数据异常");
		head.validate();
		
		TransferBVO[] tbs = (TransferBVO[])bill.getChildrenVO();

		if(tbs == null || tbs.length == 0)
			throw new BusinessException("表体数据为空");


		for(TransferBVO tb:tbs){	
			//校验应发数量 不能小于实出数量
			UFDouble u3=PuPubVO.getUFDouble_NullAsZero(tb.getNshouldoutnum());//应发数量
			UFDouble u4=PuPubVO.getUFDouble_NullAsZero(tb.getNoutnum());//实出数量
			if(u3.sub(u4).doubleValue()<0){
				throw new BusinessException("应发数量   不能小于  实出数量");
			}
			//校验生成日期不能为空 
			String  date=PuPubVO.getString_TrimZeroLenAsNull(tb.getVuserdef7());
			if(date==null){
				throw new Exception("生成日期不能为空");
			}
		}			


	}
	@Override
	protected UIDialog createQueryUI() {
		return new MyQueryDIG(getBillUI(), null, _getCorp().getPk_corp(),
				getBillUI().getModuleCode(), getBillUI()._getOperator(), null);
	}

	@Override
	protected String getHeadCondition() {
		return " pk_corp = '" + _getCorp().getPrimaryKey()
				+ "' and isnull(dr,0) = 0 and pk_billtype = '"
				+ getUIController().getBillType() + "' ";
	}

	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
//		BillRowNo.addLineRowNo(getBillCardPanelWrapper().getBillCardPanel(),
//				getUIController().getBillType(), "crowno");

	}

}