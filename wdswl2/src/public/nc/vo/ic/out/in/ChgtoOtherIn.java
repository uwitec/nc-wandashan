package nc.vo.ic.out.in;

import nc.ui.pub.ClientEnvironment;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
/**
 * 
 * @author Administrator
 *ת����ⵥ�ĺ���������
 */
public class ChgtoOtherIn implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
//		TbGeneralHVO hvo = (TbGeneralHVO)nowVo.getParentVO();
		TbGeneralBVO[] bodyvos = (TbGeneralBVO[])nowVo.getChildrenVO();
		if(bodyvos !=null && bodyvos.length>0){
			for(int i =0 ;i<bodyvos.length;i++){
				bodyvos[i].setGeb_crowno(String.valueOf((i+1)*10));
				bodyvos[i].setGeb_dbizdate(ClientEnvironment.getInstance().getDate());//���ҵ������
				bodyvos[i].setGeb_snum(bodyvos[i].getGeb_snum().abs());
				bodyvos[i].setGeb_bsnum(bodyvos[i].getGeb_bsnum().abs());
				bodyvos[i].setGeb_nmny(bodyvos[i].getGeb_nprice().multiply(bodyvos[i].getGeb_snum()));
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
					bodyvos[i].setGeb_dbizdate(ClientEnvironment.getInstance().getDate());//���ҵ������
					bodyvos[i].setGeb_snum(bodyvos[i].getGeb_snum().abs());
					bodyvos[i].setGeb_bsnum(bodyvos[i].getGeb_bsnum().abs());
					bodyvos[i].setGeb_nmny(bodyvos[i].getGeb_nprice().multiply(bodyvos[i].getGeb_snum()));
				}
			}
		}
		return nowVos;
	}

}
