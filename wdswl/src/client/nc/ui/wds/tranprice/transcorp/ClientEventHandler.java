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

	// ������У���� ����ǰ��У��
	@Override
	protected void beforeSaveValute() {

		// У������
	}

	/**
	 * ǰ̨У��
	 */
	@Override
	protected void onBoSave() throws Exception {
		beforeSaveCheck();
		super.onBoSave();
	}
	
	/**
	 * 
	 * @���ߣ�yf
	 * @˵�������ɽ������Ŀ 
	 * 			У�鿪ʼ���ںͽ�ֹ���ڵ�һ���ԣ����⽻������
	 * @ʱ�䣺2011-7-19����12:47:51
	 * @throws Exception
	 */
	private void beforeSaveCheck() throws Exception {
		// ��� ΨһУ��
		String sdate = "dstartdate";
		String edate = "denddate";
		CircularlyAccessibleValueObject vo = (CircularlyAccessibleValueObject) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		checkSDateEDate(vo, sdate, edate);
	}

	private void checkSDateEDate(CircularlyAccessibleValueObject vo,
			String sdate, String edate) throws Exception {
		//У���Ƿ���������ҪУ��
		if (null == vo) {
			return;
		}
		if (BeforeSaveValudate.isEmpty(sdate) || BeforeSaveValudate.isEmpty(edate)) {
			throw new BusinessException("У����ֶβ�����Ϊ��");
		}
		
		//У�� ���ڲ���Ϊ��
		Object vsdate = vo.getAttributeValue(sdate);
		Object vedate = vo.getAttributeValue(edate);
		if (BeforeSaveValudate.isEmpty(vsdate) || BeforeSaveValudate.isEmpty(vedate)) {
			throw new BusinessException("���ڲ�����Ϊ��");
		}
		//У�� �Ƚ��������ڴ�С��ϵ
		UFDate ufsdate = (UFDate) vsdate;
		UFDate ufedate = (UFDate) vedate;
		if(ufsdate.compareTo(ufedate) > 0){
			throw new BusinessException("����¼����󣺽�ֹ���ڱ�����ڿ�ʼ����");
		}
	}


}
