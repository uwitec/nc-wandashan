package nc.bs.zb.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.zb.price.grade.PriceGradeBO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.pub.ZbPubTool;

public class Zb01toZb02AfterChange implements nc.vo.pf.change.IchangeVO {
	/** zhf
	 * ���� �������� ���� ����� ʱʹ��
	 */
	public Zb01toZb02AfterChange() {
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

		if(sorceVO == null || tagVO == null)
			return null;
		//		ΪtagVO ���θ�ֵ
		BiddingBillVO souBillVo = (BiddingBillVO)sorceVO;
		Object o = souBillVo.getUserObj();
		if(o == null)
			return tagVO;
		if(!(o instanceof List))
			return tagVO;
		List<DealVendorBillVO> ldata = (List<DealVendorBillVO>)o;
		if(ldata == null || ldata.size() == 0){
			return tagVO;
		}
		//		���䱨������  ���ɹ�Ӧ�̱������
		//		����Ʒ���б��     
		HYBillVO billvo = (HYBillVO)tagVO;
		complementData(ldata, billvo);
		//		���ɹ�Ӧ�̱����ӱ�
		complementData2(ldata, billvo);
		ZbPubTool.setVOsRowNoByRule(billvo.getChildrenVO(), "crowno");
		return billvo;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������Ʒ�ֵ��б��
	 * 2011-5-10����04:28:14
	 * @param ldata
	 * @param billvo
	 */
	private void complementData(List<DealVendorBillVO> ldata, HYBillVO billvo) {
		Map<String, UFDouble> priceInfor = new HashMap<String, UFDouble>();// key
																			// ���id
																			// �б��
		DealInvPriceBVO[] invs = null;
		String key = null;
		for (DealVendorBillVO data : ldata) {
			invs = data.getBodys();
			if (invs == null || invs.length == 0) {
				continue;
			}
			for (DealInvPriceBVO inv : invs) {
				key = ZbPubTool.getString_NullAsTrimZeroLen(inv.getCinvclid())
						+ ZbPubTool.getString_NullAsTrimZeroLen(inv
								.getCinvmanid());
				if (priceInfor.containsKey(key))
					continue;
				priceInfor.put(key, PuPubVO.getUFDouble_NullAsZero(inv
						.getNprice()));
			}
		}

		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[]) billvo
				.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;
		for (BiEvaluationBodyVO body : bodys) {
			key = ZbPubTool.getString_NullAsTrimZeroLen(body.getCinvclid())
					+ ZbPubTool
							.getString_NullAsTrimZeroLen(body.getCinvmanid());
			body.setNzbprice(priceInfor.get(key));
		}
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����������Ĺ�Ӧ�̱�����Ϣ
	 * 2011-5-10����04:28:37
	 * @param ldata
	 * @param billvo
	 */
	private void complementData2(List<DealVendorBillVO> ldata, HYBillVO billvo)
			throws BusinessException {
		BidEvaluationHeaderVO head = (BidEvaluationHeaderVO) billvo
				.getParentVO();
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[]) billvo
				.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;
		DealInvPriceBVO[] invs = null;
		String key = null;
		String key2 = null;
		List<BidSlvendorVO> lvendor = new ArrayList<BidSlvendorVO>();
		BidSlvendorVO tmpVendor = null;
		DealVendorPriceBVO vendor = null;
		Map<String, UFDouble> gradeInfor = new PriceGradeBO()
				.getPriceGrade(head.getCbiddingid());
		if (gradeInfor == null || gradeInfor.size() == 0)
			throw new BusinessException("���۷���ϸ��Ϣ������");
		for (BiEvaluationBodyVO body : bodys) {
			key = ZbPubTool.getString_NullAsTrimZeroLen(body.getCinvclid())
					+ ZbPubTool
							.getString_NullAsTrimZeroLen(body.getCinvmanid());
			lvendor.clear();
			for (DealVendorBillVO data : ldata) {
				vendor = data.getHeader();
				invs = data.getBodys();
				if (invs == null || invs.length == 0) {
					continue;
				}

				for (DealInvPriceBVO inv : invs) {
					key2 = ZbPubTool.getString_NullAsTrimZeroLen(inv
							.getCinvclid())
							+ ZbPubTool.getString_NullAsTrimZeroLen(inv
									.getCinvmanid());
					if (key.equalsIgnoreCase(key2)) {
						tmpVendor = new BidSlvendorVO();
						tmpVendor.setCcustbasid(vendor.getCcustbasid());
						tmpVendor.setCcustmanid(vendor.getCcustmanid());
						tmpVendor.setBisfollow(inv.getBisgb());
						tmpVendor.setNminnum(inv.getNllowerprice());
						tmpVendor.setCinvbasid(inv.getCinvbasid());
						tmpVendor.setCinvmanid(inv.getCinvmanid());
						tmpVendor.setNquograde(gradeInfor.get(ZbPubTool
								.getString_NullAsTrimZeroLen(vendor
										.getCbiddingid())
								+ ZbPubTool.getString_NullAsTrimZeroLen(inv
										.getCinvmanid())
								+ ZbPubTool.getString_NullAsTrimZeroLen(vendor
										.getCcustmanid())));// ��Ӧ�̸�Ʒ�ֵı��۷�
						tmpVendor.setStatus(VOStatus.NEW);
						lvendor.add(tmpVendor);
					}
				}
			}
			if (lvendor.size() > 0) {
				BidSlvendorVO[] vos = lvendor.toArray(new BidSlvendorVO[0]);
				ZbPubTool.setVOsRowNoByRule(vos, "crowno");
				body.setBidSlvendorVOs(vos);
			}
		}
	}

	/**
	 * ���ת�����VO�� �������ڣ�(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(
			nc.vo.pub.AggregatedValueObject[] sorceVOs,
			nc.vo.pub.AggregatedValueObject[] tagVOs) throws BusinessException {
		return null;
	}
	
}
