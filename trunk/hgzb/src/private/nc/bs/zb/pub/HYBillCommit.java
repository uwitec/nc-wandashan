package nc.bs.zb.pub;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.comstatus.BillCommit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.HYBillVO;
/**
 * �ύ
 */
public class HYBillCommit extends BillCommit {

	public final ArrayList commitHYBill(AggregatedValueObject billVo)
			throws BusinessException {

		ArrayList retList = null;
		try {
			if(billVo!=null && billVo instanceof HYBillVO){
				HYBillVO hyvo = (HYBillVO)billVo;
				hyvo.setM_billField(BillField.getInstance());
			}
			retList = super.commitBill(billVo);
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			throw be;
		}catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException("�����ύ����δ֪�쳣::", e);
		}
		return retList;	
	}
	/**
	 * ��ҵ������ɽ�������
	 * @param vo	nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception	�쳣˵����
	 */
	protected void specialCommit(AggregatedValueObject vo, HYSuperDMO dmo) throws java.lang.Exception {
	}
}
