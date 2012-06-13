package nc.ui.wds.tranprice;

import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/*
 * 销售出库，其他出库->运费核算单，后续处理类
 */
public class ChgAfterVo implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(preVo == null)
			return preVo;
		TbOutgeneralBVO[] bodyvos = (TbOutgeneralBVO[])preVo.getChildrenVO();
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
