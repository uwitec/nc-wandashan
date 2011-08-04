package nc.vo.ic.other.out;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.ie.cgqy.CgqyHVO;
/**
 * 
 * @author Administrator
 *  采购取样（WDSC），发运订单(WDS3)交换成其他出库单（WDS6）的后续处理类
 */
public class ChangTOOtherOutVO implements IchangeVO{

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		TbOutgeneralHVO hvo = (TbOutgeneralHVO)nowVo.getParentVO();
		//在 其他出库参照采购取样时  将取货单位  和 取货人 拼接一个字符串  放到备注里面
		CgqyHVO cgqyHVO = (CgqyHVO) preVo.getParentVO();
		if(null != cgqyHVO){
			hvo.setVnote(cgqyHVO.getCcusmandoc()+cgqyHVO.getCcustomer());
		}
		
		//end
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
