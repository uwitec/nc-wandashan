package nc.vo.ic.other.out;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ie.cgqy.CgqyHVO;
import nc.vo.wl.pub.WdsWlPubConst;
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
		addNoteForWDSC(preVos,nowVos);
		return nowVos;
	}
   /** 
    * @作者：mlr
    * @说明：完达山物流项目 
    *  其他出库 参照 采购取样时
    *   将采购取样的 取货单位 和 取货人  赋值给  其他出库的 备注字段     
    * @时间：2011-8-4下午06:05:02
    * @param preVos
    * @param nowVos
    */
	private void addNoteForWDSC(AggregatedValueObject[] preVos,
			AggregatedValueObject[] nowVos) {
		if(preVos==null || preVos.length==0){
			return;
		}
		if(nowVos==null || nowVos.length==0){
			return;
		}
		int size=preVos.length;
		int size1=nowVos.length;
		if(size !=size1){
			return;
		}
		for(int i=0;i<size;i++){
			if(preVos[i].getParentVO()==null || nowVos[i].getParentVO()==null)
				continue;
			if(preVos[i].getParentVO().getAttributeValue("pk_billtype")==null){
				continue;
			}
			if(!preVos[i].getParentVO().getAttributeValue("pk_billtype").equals(WdsWlPubConst.WDSC)){
				continue;
			}	
			String corp=PuPubVO.getString_TrimZeroLenAsNull(preVos[i].getParentVO().getAttributeValue("ccusmandoc"));
			if(corp==null){
				corp="";
			}
			String psnren=PuPubVO.getString_TrimZeroLenAsNull(preVos[i].getParentVO().getAttributeValue("ccustomer"));	
			if(psnren==null){
				psnren="";
			}
		       nowVos[i].getParentVO().setAttributeValue("vnote",corp+"  "+psnren);
			}		
		}
}
