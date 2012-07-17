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
 * ת��λ
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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ⵥ�Զ��������ʱ У������
	 * @ʱ�䣺2011-4-3����03:18:13
	 * @param card
	 * @param bodys
	 * @throws ValidationException
	 */
	public static void validationOnPickAction(BillCardPanel card,TransferBVO[] bodys) throws ValidationException{
		if (null == bodys || bodys.length == 0) {
			throw new ValidationException("�����޻�Ʒ����");
		}
		Object pk_stordoc = card.getHeadItem("srl_pk")
		.getValueObject();
		if (null == pk_stordoc || "".equals(pk_stordoc)) {
			throw new ValidationException("����ֿⲻ��Ϊ��");
		}
		
		String pk_cargdoc = PuPubVO.getString_TrimZeroLenAsNull(card.getHeadItem("pk_cargdoc").getValueObject());
		if(pk_cargdoc == null){
			throw new ValidationException("�����λ����Ϊ��");
		}
		for(TransferBVO body:bodys){
			body.validationOnZdck();
		}
	}

	/*
	 * mlr
	 * �Զ�ȡ��(non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	protected void onzdqh() throws Exception {
		int results = ui.showOkCancelMessage("ȷ���Զ����?");
		if (results != 1) {
			return;
		}
		if (getBillUI().getVOFromUI() == null|| getBillUI().getVOFromUI().getChildrenVO() == null|| getBillUI().getVOFromUI().getChildrenVO().length == 0) {ui.showErrorMessage("��ȡ��ǰ�������ݳ���.");
			return;
		}
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TransferBVO[] generalbVOs = (TransferBVO[]) billvo.getChildrenVO();
		// ����У�� begin
	   validationOnPickAction(getBillCardPanelWrapper().getBillCardPanel(), generalbVOs);
		// ����У��end
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
	 * ����ǰ��������У��
	 * @throws Exception 
	 * @throws BusinessException 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-13����07:33:33
	 */
	private void valudate() throws Exception {
		
		getBillCardPanelWrapper().getBillCardPanel().stopEditing();

		MyBillVO bill=(MyBillVO) getBillUI().getVOFromUI();
		
		if(bill == null)
			throw new BusinessException("�����쳣");
		
		TransferVO head = (TransferVO)bill.getParentVO();
		
		if(head == null)
			throw new BusinessException("�����쳣");
		head.validate();
		
		TransferBVO[] tbs = (TransferBVO[])bill.getChildrenVO();

		if(tbs == null || tbs.length == 0)
			throw new BusinessException("��������Ϊ��");


		for(TransferBVO tb:tbs){	
			//У��Ӧ������ ����С��ʵ������
			UFDouble u3=PuPubVO.getUFDouble_NullAsZero(tb.getNshouldoutnum());//Ӧ������
			UFDouble u4=PuPubVO.getUFDouble_NullAsZero(tb.getNoutnum());//ʵ������
			if(u3.sub(u4).doubleValue()<0){
				throw new BusinessException("Ӧ������   ����С��  ʵ������");
			}
			//У���������ڲ���Ϊ�� 
			String  date=PuPubVO.getString_TrimZeroLenAsNull(tb.getVuserdef7());
			if(date==null){
				throw new Exception("�������ڲ���Ϊ��");
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