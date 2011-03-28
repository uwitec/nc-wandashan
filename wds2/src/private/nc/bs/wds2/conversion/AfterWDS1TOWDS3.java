package nc.bs.wds2.conversion;

import nc.bs.trade.business.HYPubBO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * WD1TOWDS3交换的类后续处理类
 * @author Administrator
 *
 */
public class AfterWDS1TOWDS3 implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		SendorderVO head = (SendorderVO)nowVo.getParentVO();
		head.setVbillno(new HYPubBO().getBillNo(head.getPk_billtype(), head.getPk_corp(), null, null));
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos ==null || nowVos.length==0){
			return null;
		}
		for(AggregatedValueObject nowVo:nowVos){
			SendorderVO head = (SendorderVO)nowVo.getParentVO();
			head.setVbillno(new HYPubBO().getBillNo(head.getPk_billtype(), head.getPk_corp(), null, null));

		}
		return nowVos;
	}

}
