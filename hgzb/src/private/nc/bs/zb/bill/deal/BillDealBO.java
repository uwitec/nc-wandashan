package nc.bs.zb.bill.deal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zb.price.SubmitPriceBO;
import nc.bs.zb.price.grade.PriceGradeBO;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bill.deal.DealInvBillVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BillDealBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public void doOK(List<DealVendorBillVO> ldata,ClientLink cl) throws Exception{
		if(ldata == null || ldata.size() == 0)
			return;
//		������Χ�Ĺ�Ӧ��  �����б������
		
//		������ͷ  ����   ���
		
//		��ȡ������Ϣ
		String biddingid = PuPubVO.getString_TrimZeroLenAsNull(ldata.get(0).getHeader().getCbiddingid());
		if(biddingid == null){
			throw new BusinessException("������ϢΪ��");
		}
		
		HYPubBO bo = new HYPubBO();
		String[] names = new String[]{BiddingBillVO.class.getName(),BiddingHeaderVO.class.getName(),BiddingBodyVO.class.getName()};
		BiddingBillVO billvo = (BiddingBillVO)bo.queryBillVOByPrimaryKey(names, biddingid);
		if(billvo == null)
			throw new BusinessException("��ȡѡ�б�����Ϣ�쳣�����ݲ�����");
	
		billvo.setUserObj(ldata);
		
		//����VO����ת��
		PfParameterVO para = new PfParameterVO();
		para.m_operator = cl.getUser();
		para.m_currentDate = cl.getLogonDate().toString();
		HYBillVO tagVo = (HYBillVO) PfUtilTools.runChangeData(ZbPubConst.ZB_BIDDING_BILLTYPE,
	          ZbPubConst.ZB_EVALUATION_BILLTYPE, billvo, para);
	   
		
//		����С֣ ���� ����� vo
		new PfUtilBO().processAction("PUSHSAVE", ZbPubConst.ZB_EVALUATION_BILLTYPE, cl.getLogonDate().toString(), null, tagVo, null);
//		��ñ���״̬Ϊ �б�
		String sql = "update zb_bidding_h set ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT+" " +
				" where cbiddingid = '"+biddingid+"'";
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ����˾���н�������׶εı���
	 * 2011-5-4����02:45:23
	 * @param corpid
	 * @return
	 * @throws BusinessException
	 */
	public BiddingHeaderVO[] queryAllBiddingByCorp(String corpid) throws BusinessException{
		String s = ZbPubTool.getParam();
		String whereSql = " pk_corp = '"+corpid+"' and isnull(dr,0)=0 and cname is not null and ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_BILL;
		 if(s!=null &&!"".equals(s))
			 whereSql= whereSql+ " and reserve1 ='"+ZbPubTool.getParam()+"'";
		List<BiddingHeaderVO> lbiddVo = (List<BiddingHeaderVO>)getDao().retrieveByClause(BiddingHeaderVO.class, whereSql);
		if(lbiddVo == null || lbiddVo.size() == 0)
			return null;
		BiddingHeaderVO[] vos =lbiddVo.toArray(new BiddingHeaderVO[0]);
		VOUtil.ascSort(vos,new String[]{"vbillno"});
		return vos;
	}
	
	private void sortVendors(DealVendorPriceBVO[] vendors){
		for(DealVendorPriceBVO vendor:vendors){
			vendor.setNallgrade(PuPubVO.getUFDouble_NullAsZero(vendor.getNqualipoints()).add(PuPubVO.getUFDouble_NullAsZero(vendor.getNquotatpoints())));
		}
		nc.vo.trade.voutils.VOUtil.ascSort(vendors, DealVendorPriceBVO.asc_sort_fieldnames);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ���鱨����Ϣ   ������� ʹ��
	 * 2011-5-5����04:01:06
	 * @param cbiddingid ����id
	 * @param isinv �Ƿ���ϸ�б�
	 * @return
	 * @throws BusinessException
	 */
	public Object getBiddingInfor(String cbiddingid,UFBoolean isinv) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null){
			return null;
		}
		//��ѯƷ�ֱ���    ��Ӧ�̱���   ���۱���
		String wheresql = "isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";
		List<DealInvPriceBVO> linv = (List<DealInvPriceBVO>)getDao().retrieveByClause(DealInvPriceBVO.class, wheresql);
		List<DealVendorPriceBVO> lvendor = (List<DealVendorPriceBVO>)getDao().retrieveByClause(DealVendorPriceBVO.class, wheresql +" and coalesce(fisclose,'N') ='N' ");
//		List<SubmitPriceVO> lprice = (List<SubmitPriceVO>)getDao().retrieveByClause(SubmitPriceVO.class, wheresql);
		if(linv == null || linv.size() == 0)
			throw new BusinessException("���������쳣���б�Ʒ����ϢΪ��");
		if(lvendor == null || lvendor.size() == 0)
			throw new BusinessException("���������쳣��Ͷ�깩Ӧ����ϢΪ��");

		
		DealVendorPriceBVO[] vendors = lvendor.toArray(new DealVendorPriceBVO[0]);		
		//���ܷ�����
		sortVendors(vendors);
		
//		���ݷ�װת��
		DealVendorBillVO[] vendorBills = new DealVendorBillVO[lvendor.size()];
		DealVendorBillVO tmp = null;
		int index = 0;
		for(BiddingSuppliersVO vendor:vendors){
			tmp = new DealVendorBillVO();
			tmp.setParentVO(vendor);
			tmp.setChildrenVO(getInvPriceBVos(vendor, linv, isinv));
			vendorBills[index] = tmp;
			index ++;
		}
		
		
		Map<String,List<SubmitPriceVO>> priceinfor = getSubmitPriceBO().getVendorSubmitPrices(cbiddingid);
		Map<String,UFDouble> gradeInfor = new PriceGradeBO().getPriceGrade(cbiddingid);
		if(priceinfor == null||priceinfor.size()==0){
			throw new BusinessException("��ȡ���鱨����ϢΪ��");
		}
		DealInvBillVO[] invBills = new DealInvBillVO[linv.size()];
		index = 0;
		DealInvBillVO tmp2 =  null;
		for(BiddingBodyVO inv:linv){
			tmp2 = new DealInvBillVO();
			tmp2.setParentVO(inv);
			tmp2.setChildrenVO(getVendorPriceBVos(inv,lvendor,priceinfor,gradeInfor));
			invBills[index] = tmp2;
			index ++;
		}
		
		return new Object[]{vendorBills,invBills};
		
	}
	private DealVendorPriceBVO[] getVendorPriceBVos(BiddingBodyVO inv,List<DealVendorPriceBVO> lvendor,Map<String,List<SubmitPriceVO>> priceinfor,Map<String,UFDouble> gradeInfor) throws BusinessException{
		List<DealVendorPriceBVO> lb = new ArrayList<DealVendorPriceBVO>();
		List<SubmitPriceVO> ltmp = null;
		String key = null;
		String gradeKey = null;
		for(DealVendorPriceBVO vendor:lvendor){
			key = ZbPubTool.getString_NullAsTrimZeroLen(inv.getCinvclid())+ZbPubTool.getString_NullAsTrimZeroLen(inv.getCinvmanid())
			+ZbPubTool.getString_NullAsTrimZeroLen(vendor.getCcustmanid());
			
			ltmp = priceinfor.get(key);
			if(ltmp == null || ltmp.size() == 0)
				continue;//�ù�Ӧ��δ�����Ʒ�ֱ���
			lb.addAll(splitVendorPriceBySubmit(vendor, ltmp));
		}
		if(lb.size()<=0)
			return null;
		
		DealVendorPriceBVO[] vendors = lb.toArray(new DealVendorPriceBVO[0]);
		if(gradeInfor == null){
			return vendors;
		}
		for(DealVendorPriceBVO vendor:vendors){
			gradeKey = ZbPubTool.getString_NullAsTrimZeroLen(inv.getCbiddingid())+ZbPubTool.getString_NullAsTrimZeroLen(inv.getCinvmanid())
			+ZbPubTool.getString_NullAsTrimZeroLen(vendor.getCcustmanid());
			vendor.setNquotatpoints(gradeInfor.get(gradeKey));
		}
		
		return vendors;
	}
	private List<DealVendorPriceBVO> splitVendorPriceBySubmit(DealVendorPriceBVO vendor,List<SubmitPriceVO> lprice){
		List<DealVendorPriceBVO> ltmp = new ArrayList<DealVendorPriceBVO>();
		DealVendorPriceBVO tmp = null;
		for(SubmitPriceVO price:lprice){
			tmp = (DealVendorPriceBVO)vendor.clone();
			tmp.setCcircalnoid(price.getCcircalnoid());
			tmp.setNprice(price.getNprice());
			ltmp.add(tmp);
		}
		return ltmp;
	}
	private SubmitPriceBO pricebo = null;
	public SubmitPriceBO getSubmitPriceBO(){
		if(pricebo == null){
			pricebo = new SubmitPriceBO();
		}
		return pricebo;
	}
	private DealInvPriceBVO[] getInvPriceBVos(BiddingSuppliersVO vendor,List<DealInvPriceBVO> linv,UFBoolean isinv) throws Exception{
		String cvendorid = vendor.getCcustmanid();		
		List<DealInvPriceBVO> lb = new ArrayList<DealInvPriceBVO>();
		DealInvPriceBVO invC = null;
		for(DealInvPriceBVO inv:linv){
			invC = (DealInvPriceBVO)ObjectUtils.serializableClone(inv);
			invC.setNllowerprice(getSubmitPriceBO().getVendorLowestPrice(inv.getCbiddingid(), cvendorid, inv.getInvID(isinv.booleanValue()), isinv.booleanValue()));
			invC.setNprice(getSubmitPriceBO().getLowestPrice(inv.getCbiddingid(),null, inv.getInvID(isinv.booleanValue()), isinv.booleanValue()));
			if(invC.getNllowerprice().equals(UFDouble.ZERO_DBL)||invC.getNprice().equals(UFDouble.ZERO_DBL))
				continue;
			lb.add(invC);
		}
		if(lb.size()>0)
			return lb.toArray(new DealInvPriceBVO[0]);
		return null;
	}
}
