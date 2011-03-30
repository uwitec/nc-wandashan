package nc.ui.hg.ic.ic212;

import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.pf.ICSourceRefBaseDlg;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;



/**
 * ���ϳ��ⵥ  ֧�ֲ��ռƻ�
 *
 *
 * �������ڣ�(2001-11-23 15:39:43)
 * @author��zhf
 */
public class HgMaterialOutClientUI extends nc.ui.ic.ic212.ClientUI {

	/**
	 * ����ʵ�ָ÷�������Ӧ��ť�¼���
	 * @version (00-6-1 10:32:59)
	 *
	 * @param bo ButtonObject
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		if(bo == getButtonManager().getButton(ICButtonConst.BTN_BILL_REF_MR)){
			bo.setTag("HG02:");
			ICSourceRefBaseDlg.childButtonClicked(bo, getEnvironment().getCorpID(),getFunctionNode(), getEnvironment().getUserID(), getBillType(),this);
			if (ICSourceRefBaseDlg.isCloseOK()) {
				nc.vo.pub.AggregatedValueObject[] vos = ICSourceRefBaseDlg.getRetsVos();
				onAddToOrder(vos);
			}
		}else{
			super.onButtonClicked(bo);
		}
	}



	private void onAddToOrder(AggregatedValueObject[] vos) {

		if (vos == null) {
			return;
		}
		try {
			if(vos.length<=1){
				setRefBillsFlag(false);
				setBillRefResultVO(null, vos);
			}else{
				//      �޸��ˣ������� �޸����ڣ�2007-04-29 �޸�ԭ�򣺲����������������ɲ��ϳ��ⵥ�󣬱�״̬���ԣ�Ӧ�ð��Ƿ�������ɶ��ŵ������ó���
				setRefBillsFlag(true);
				setBillRefMultiVOs(null,(GeneralBillVO[])vos);
			}
		} catch (Exception e) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000297")/*@res "��������:"*/ + e.getMessage());
		}
	}


}