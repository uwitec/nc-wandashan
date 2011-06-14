package nc.vo.ic.other.in;

import nc.ui.pub.ClientEnvironment;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
/**
 * 
 * @author Administrator
 *转换趁其他入库单的后续处理类
 */
public class ChgtoOtherIn implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		TbGeneralHVO hvo = (TbGeneralHVO)nowVo.getParentVO();
		TbGeneralBVO[] bodyvos = (TbGeneralBVO[])nowVo.getChildrenVO();
		if(bodyvos !=null && bodyvos.length>0){
			for(int i =0 ;i<bodyvos.length;i++){
				bodyvos[i].setGeb_crowno(String.valueOf((i+1)*10));
				bodyvos[i].setGeb_dbizdate(ClientEnvironment.getInstance().getDate());//入库业务日期
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
			TbGeneralBVO[] bodyvos = (TbGeneralBVO[])nowVos[j].getChildrenVO();
			if(bodyvos !=null && bodyvos.length>0){
				for(int i =0 ;i<bodyvos.length;i++){
					bodyvos[i].setGeb_crowno(String.valueOf((i+1)*10));
					bodyvos[i].setGeb_dbizdate(ClientEnvironment.getInstance().getDate());//入库业务日期
				}
			}
		}
		return nowVos;
	}

}
