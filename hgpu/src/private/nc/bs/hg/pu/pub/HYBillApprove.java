package nc.bs.hg.pu.pub;


import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.comstatus.BillApprove;
import nc.vo.hg.pu.pub.BillDateGetter;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;


public class HYBillApprove extends BillApprove {


	public AggregatedValueObject approveHYBill(AggregatedValueObject billVo) throws BusinessException {
		
		AggregatedValueObject retVO = null;
		try {
			retVO = super.approveBill(billVo);
			retVO=modifyApproveDate(retVO);
			
		} catch (BusinessException re) {
			throw re;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException("单据审批出现未知异常::", e);
		}
		return retVO;
	}

	/**
	 * 修改审批日期
	 * 
	 * created by chenliang
	 * at 2007-8-21 上午11:14:32
	 */
	private AggregatedValueObject modifyApproveDate(AggregatedValueObject billVo) throws BusinessException{
		if(billVo==null||billVo.getParentVO()==null)
			return billVo;
		String primaryKey = billVo.getParentVO().getPrimaryKey();
		if(primaryKey==null||primaryKey.equals(""))
			return billVo;
		HYSuperDMO dmo=new HYSuperDMO();
		SuperVO headvo=dmo.queryByPrimaryKey(billVo.getParentVO().getClass(), primaryKey);
		UFDate approveDate=BillDateGetter.getApproveDate();
		headvo.setAttributeValue("dapprovedate", approveDate);
		billVo.setParentVO(headvo);
		dmo.update(headvo,new String[]{"dapprovedate"});
		billVo.setParentVO(dmo.queryByPrimaryKey(billVo.getParentVO().getClass(), primaryKey));
		return billVo;
	}
	@Override
	protected void specialApprove(AggregatedValueObject vo, HYSuperDMO dmo) throws Exception {
		super.specialApprove(vo, dmo);
	}

}
