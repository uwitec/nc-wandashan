package nc.bs.zb.comments;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.pub.ZbPubConst;

public class SplitNumCol implements AbstractSplitNumCol {
/**
 * ��Χ��Ӧ�̷����㷨�� �㷨������  ȡ�б��ܽ����������޶�   ��  ����
 * 1����ȡ��Ӧ�̵÷����
 * 2����ȡ��ε���Χ��Ӧ�̷�̯����
 * 3�������ֺܷͷ�̯���������������Ӧ�̵��޶�
 * 4��׽��Ʒ�ָ��ݹ�Ӧ�̱��۷��ɸߵ��ͽ��з���  ���Ǹ�����Ӧ�̵��޶�
 */
	public SplitNumCol(SplitNumColPara para){
		super();
		setPara(para);
	}
	public SplitNumCol(){
		super();
//		setPara(para);
	}
	private SplitNumColPara para = null;
	public void setPara(ISplitNumPara para){
		this.para = (SplitNumColPara)para;
	}
	
	public void col() throws BusinessException{
		if(para == null)
			throw new BusinessException("����Ϊ��");
//		����Ʒ��   ���ȿ��Ǹ�Ʒ�ֱ��۷���ߵĹ�Ӧ��  ���Ǹù�Ӧ���޶�  ע�������ϼ�
		HYBillVO bill = para.getBill();
		if(bill == null)
			throw new BusinessException("����Ϊ��");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("Ʒ������Ϊ��");
		
		BidSlvendorVO[] vendors = null;
		for(BiEvaluationBodyVO body:bodys){
			vendors = body.getBidSlvendorVOs();
			if(vendors == null || vendors.length == 0)
				throw new BusinessException("��Ӧ�������쳣");
			VOUtil.descSort(vendors, BidSlvendorVO.sort_fields);//����Ӧ�̱��۷ֽ�������
			for(BidSlvendorVO vendor:vendors){
				split(body, vendor);
				if(PuPubVO.getUFDouble_NullAsZero(body.getNsplitnum()).equals(UFDouble.ZERO_DBL))
					break;//��Ʒ�ַ�������   ������� ���䵽��һ����Ӧ����
			}
		}
	}	
	
	public void split(BiEvaluationBodyVO body, BidSlvendorVO vendor)
			throws BusinessException {
//		Ʒ�ֱ��δ���������
		UFDouble nnum = getNum(body);
		// Ʒ�ִ������ܽ��
		UFDouble nmny = nnum.multiply(
				PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()), 8);
		// �����޶�
		UFDouble nxe = getXe(vendor.getCcustmanid());
		
		if (nxe.doubleValue() > nmny.doubleValue()
				- body.getNzbprice().doubleValue()) {// �ù�Ӧ�̸�Ʒ��ȫ���б�
			vendor.setNzbnum(nnum);
			
			vendor.setNzbmny(nmny);
			para.getOldMnyMap().put(vendor.getCcustmanid(),
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(vendor.getCcustmanid())).add(nmny));
		} else {
			vendor.setNzbmny(nxe);
			vendor.setNzbnum(nxe.div(body.getNzbprice()));
			para.getOldMnyMap().put(vendor.getCcustmanid(),
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(vendor.getCcustmanid())).add(nxe));
		}
		//�����б����
		vendor.setNwinpercent(vendor.getNzbnum().div(body.getNzbnum(), ZbPubConst.NUM_DIGIT).multiply(100));
		body.setNsplitnum(nnum.sub(vendor.getNzbnum()));
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� ��ȡ��Ӧ�̿����޶�  ��Ӧ������޶�-��ʹ�ý��
	 * 2011-5-24����07:40:26
	 * @param cvendorid
	 * @return
	 */
	private UFDouble getXe(String cvendorid){
		 return PuPubVO.getUFDouble_NullAsZero(
					para.getMnyMap().get(cvendorid)).sub(
					PuPubVO.getUFDouble_NullAsZero(para.getOldMnyMap().get(cvendorid)));
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡƷ�ִ���������
	 * 2011-5-24����07:40:44
	 * @param body
	 * @return
	 */
	private UFDouble getNum(BiEvaluationBodyVO body){
		return PuPubVO.getUFDouble_NullAsZero(
				body.getNsplitnum() == null ? body.getNzbnum() : body
						.getNsplitnum());
	}
}
