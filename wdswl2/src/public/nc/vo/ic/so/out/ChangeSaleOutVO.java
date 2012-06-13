package nc.vo.ic.so.out;

import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
/**
 * 
 * @author zpm
 *
 */
public class ChangeSaleOutVO implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		TbOutgeneralBVO[] bodyvos = (TbOutgeneralBVO[])nowVo.getChildrenVO();
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
			TbOutgeneralBVO[] bodyvos = (TbOutgeneralBVO[])nowVos[j].getChildrenVO();
			if(bodyvos !=null && bodyvos.length>0){
				for(int i =0 ;i<bodyvos.length;i++){
					bodyvos[i].setCrowno(String.valueOf((i+1)*10));
				}
			}
		}
		return nowVos;
	}

}
