package nc.ui.pf.changedir.after;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillHeaderVO;
/**
 * 
 * @author Administrator
 *销售出库数转换成运费核算单 后台处理类
 */
public class ChgWDS8TOWDSMAfter implements IchangeVO{

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		SendBillHeaderVO hvo = (SendBillHeaderVO)nowVo.getParentVO();
		SendBillBodyVO[] bodyvos = (SendBillBodyVO[])nowVo.getChildrenVO();
		if(bodyvos !=null && bodyvos.length>0){
			for(int i =0 ;i<bodyvos.length;i++){
				bodyvos[i].setCrowno(String.valueOf((i+1)*10));
			}
		}
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos == null)
			return nowVos;
		for( int j = 0 ;j< nowVos.length;j++){
			SendBillBodyVO[] bodyvos = (SendBillBodyVO[])nowVos[j].getChildrenVO();
			if(bodyvos !=null && bodyvos.length>0){
				for(int i =0 ;i<bodyvos.length;i++){
					bodyvos[i].setCrowno(String.valueOf((i+1)*10));
				}
			}
		}
		return nowVos;
	}

}
