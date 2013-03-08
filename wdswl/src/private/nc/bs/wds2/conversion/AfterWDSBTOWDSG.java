package nc.bs.wds2.conversion;

import nc.bs.trade.business.HYPubBO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * ��������--�������˵� 
 * �����������������
 *
 */
public class AfterWDSBTOWDSG implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		SuperVO head = (SuperVO)nowVo.getParentVO();
		head.setAttributeValue("vbillno",new HYPubBO().getBillNo(head.getAttributeValue("pk_billtype").toString(), head.getAttributeValue("pk_corp").toString(), null, null));
		head.setAttributeValue("itransstatus", 0);//Ĭ����;
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos ==null || nowVos.length==0){
			return null;
		}
		HYPubBO bo = new HYPubBO();
		for(AggregatedValueObject nowVo:nowVos){
			SuperVO head = (SuperVO)nowVo.getParentVO();
			head.setAttributeValue("vbillno", bo.getBillNo(head.getAttributeValue("pk_billtype").toString(), head.getAttributeValue("pk_corp").toString(), null, null));
			head.setAttributeValue("itransstatus", 0);//Ĭ����;
		}
		return nowVos;
	}
	
	

}
