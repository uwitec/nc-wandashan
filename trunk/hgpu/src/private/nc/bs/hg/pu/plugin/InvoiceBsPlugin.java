package nc.bs.hg.pu.plugin;

import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pi.InvoiceHeaderVO;
import nc.vo.pi.InvoiceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;

public class InvoiceBsPlugin implements IScmBSPlugin {

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if (billvos == null || billvos.length == 0)
			return;
		InvoiceVO[] vos = (InvoiceVO[]) billvos;
		if (action == Action.AUDIT) {// 库存签字 实扣
			for (InvoiceVO vo : vos) {
				InvoiceHeaderVO head = vo.getHeadVO();
				 Object oc = head.getVdef20();
				 if(PuPubVO.getString_TrimZeroLenAsNull(oc)==null){
					 throw new BusinessException("该发票还没有形成报账单,不能审批");
				 }
			}
		}else if (action == Action.DELETE) {
			for (InvoiceVO vo : vos) {
				InvoiceHeaderVO head = vo.getHeadVO();
				 Object oc = head.getVdef20();
				 if(PuPubVO.getString_TrimZeroLenAsNull(oc)!=null){
					 throw new BusinessException("该发票有报账单,不能删除");
				 }
			}
		}
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
	}

	public AggregatedValueObject[] checkOutVO(AggregatedValueObject[] avos)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}
}
