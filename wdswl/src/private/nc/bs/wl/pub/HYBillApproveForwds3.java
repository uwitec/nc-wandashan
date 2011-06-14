package nc.bs.wl.pub;


import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.comstatus.BillApprove;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * wds3的审批类
 * @author spf
 *
 */

public class HYBillApproveForwds3 extends BillApprove {


	public AggregatedValueObject approveHYBill(AggregatedValueObject billVo) throws BusinessException {
		
		AggregatedValueObject retVO = null;
		try {
			retVO = super.approveBill(billVo);
			retVO = rebackWds3(billVo);
			retVO=modifyApproveDate(retVO);
			
		} catch (BusinessException re) {
			throw re;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException("单据审批出现未知异常::", e);
		}
		return retVO;
	}

	private AggregatedValueObject rebackWds3(AggregatedValueObject billvo) throws BusinessException {
		if(billvo==null||billvo.getParentVO()==null)
			return billvo;
		String primaryKey = billvo.getParentVO().getPrimaryKey();
		if(primaryKey==null||primaryKey.equals(""))
			return billvo;
		HYSuperDMO dmo=new HYSuperDMO();
		
		SuperVO headvo=dmo.queryByPrimaryKey(billvo.getParentVO().getClass(), primaryKey);
		headvo.setAttributeValue("denddate",ClientEnvironment.getInstance().getDate());
		billvo.setParentVO(headvo);
		dmo.update(headvo,new String[]{"denddate"});
		
		headvo.setAttributeValue("itransstatus",2);
		billvo.setParentVO(headvo);
		dmo.update(headvo,new String[]{"itransstatus"});
		
		billvo.setParentVO(dmo.queryByPrimaryKey(billvo.getParentVO().getClass(), primaryKey));
		return billvo;
		
	}
	
	/**
	 * 修改审批日期
	 * 
	 * created by spf
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
		headvo.setAttributeValue("dapprovedate",ClientEnvironment.getInstance().getDate());
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
