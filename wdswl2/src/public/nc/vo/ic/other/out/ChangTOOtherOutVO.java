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
 *  �ɹ�ȡ����WDSC�������˶���(WDS3)�������������ⵥ��WDS6���ĺ���������
 */
public class ChangTOOtherOutVO implements IchangeVO{

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		TbOutgeneralHVO hvo = (TbOutgeneralHVO)nowVo.getParentVO();
		//�� ����������ղɹ�ȡ��ʱ  ��ȡ����λ  �� ȡ���� ƴ��һ���ַ���  �ŵ���ע����
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
    * @���ߣ�mlr
    * @˵�������ɽ������Ŀ 
    *  �������� ���� �ɹ�ȡ��ʱ
    *   ���ɹ�ȡ���� ȡ����λ �� ȡ����  ��ֵ��  ��������� ��ע�ֶ�     
    * @ʱ�䣺2011-8-4����06:05:02
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
