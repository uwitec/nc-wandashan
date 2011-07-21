package nc.ui.hg.pu.conversion;

import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;

public class PlanVOTO5X implements nc.vo.pf.change.IchangeVO {
	/**
	 * SOrderVOTOPPrayVO ������ע�⡣
	 */
	public PlanVOTO5X() {
		super();
	}

	/**
	 * ���ת�����VO�� �������ڣ�(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject retChangeBusiVO(
			nc.vo.pub.AggregatedValueObject sorceVO,
			nc.vo.pub.AggregatedValueObject tagVO) throws BusinessException {
//		����繫˾ת��   �кŴ���    �ֵ�
		if(tagVO==null)
			return null;
		CircularlyAccessibleValueObject[] bodys = tagVO.getChildrenVO();
		if(bodys==null||bodys.length==0)
			return null;
		BillVO billvo = (BillVO)tagVO;
		BillHeaderVO head = billvo.getHeaderVO();
		BillItemVO[] items = billvo.getItemVOs();
		if(head.getCincorpid().equalsIgnoreCase(head.getCoutcorpid()))
			return tagVO;
		String outcorp = head.getCoutcorpid();
		String invman = null;
		String foumu = "cinvmanid ->getcolvalue2(bd_invmandoc,pk_invmandoc,pk_invbasdoc,invbasid,pk_corp,outcorp)";
		String[] names = new String[]{"invbasid","outcorp"};
		String[] values = null;
		String cinvbasid = null;
		for(BillItemVO item:items){
			cinvbasid = item.getCinvbasid();
			values = new String[]{cinvbasid,outcorp};
			invman = PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.execFomularClient(foumu, names, values));
			if(invman == null){
				throw new BusinessException("���δ���䵽������λ");
			}
			item.setCoutinvid(invman);
		}	
		
		return billvo;
	}

	/**
	 * ���ת�����VO�� �������ڣ�(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(
			nc.vo.pub.AggregatedValueObject[] sorceVOs,
			nc.vo.pub.AggregatedValueObject[] tagVOs) throws BusinessException {

		if(tagVOs == null||tagVOs.length==0)
			return null;
		for(AggregatedValueObject tagVO:tagVOs){
			retChangeBusiVO(null, tagVO);
		}
		return tagVOs;
	}
		
}
