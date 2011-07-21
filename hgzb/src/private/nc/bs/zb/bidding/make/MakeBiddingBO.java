package nc.bs.zb.bidding.make;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.self.changedir.CHGPlanTOBidding;
import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.comsave.BillSave;
import nc.bs.zb.pub.SingleVOChangeDataBsTool;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.bidding.HistoryPriceVO;
import nc.vo.zb.bidding.PuPlanInvVO;
import nc.vo.zb.bidding.SmallBiddingHeaderVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class MakeBiddingBO {

	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private TempTableUtil ttUtil = null;

	private TempTableUtil getTempTableBO() {
		if (ttUtil == null) {
			ttUtil = new TempTableUtil();
		}
		return ttUtil;
	}

	private HYPubBO bo = null;

	private HYPubBO getPubBO() {
		if (bo == null) {
			bo = new HYPubBO();
		}
		return bo;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ղɹ��ƻ����ֱ�� 2011-5-18����02:04:32
	 * @param whereSql
	 * @throws BusinessException
	 */
	public PuPlanInvVO[] loadPlanInvInfor(String whereSql)
			throws BusinessException {
//		��Ҫ����Ĭ������   ����ͨ��  ��Դ��ƻ�  ����
		whereSql = whereSql+" and h.cauditpsn is not null " +
//				" and b.csourcebilltype = '"+ZbPubConst.YEAR_PLAN_BILLTYPE+"'" +
				" and coalesce(b.npraynum,0.0)-coalesce(b.naccumulatenum,0.0) > 0 ";
		
		String sql = PuPlanInvVO.buildSelectSql(whereSql);
		List<PuPlanInvVO> linv = (List<PuPlanInvVO>) getDao().executeQuery(sql,
				new BeanListProcessor(PuPlanInvVO.class));
		if (linv == null || linv.size() == 0)
			return null;
		return linv.toArray(new PuPlanInvVO[0]);
	}
	
//	�� Ĭ�ϲ�ѯ6����ǰ�ù�����ʷ
	private int getHistoryPara(String scorpid) throws BusinessException{
		String sql = "select ireferencelimits from zb_parameter_settings where isnull(dr,0) = 0 and pk_corp = '"+scorpid+"'";
		return PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 6);
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�� ���ֱ�����ɱ���
	 * 2011-5-19����05:39:37
	 * @param plans
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	public Object divideBidding(PuPlanInvVO[] plans, ClientLink cl)
			throws Exception {
		if (plans == null || plans.length == 0)
			return null;

		int ihistory = getHistoryPara(cl.getCorp());// �� Ĭ�ϲ�ѯ6����ǰ�ù�����ʷ
		String senddate = cl.getLogonDate().toString();
		String sstartdate = cl.getLogonDate().getDateBefore(ihistory * 30)//"2011-06-01";
				.toString();
		String[] invids = new String[plans.length];
		int index = 0;
		String[] planids = new String[plans.length];
		for (PuPlanInvVO plan : plans) {
			invids[index] = plan.getCbaseid();
			planids[index] = plan.getPrimaryKey();
			index ++;
		}

		HistoryPriceVO[] historys = loadHistoryPriceInfor(invids, sstartdate,
				senddate, cl.getCorp());

//		��д�ƻ��ۼ��б�����  ��Ϊ�Ǻ�ֱ̨�ӱ����
		String sql = " update po_praybill_b set naccumulatenum = npraynum where cpraybill_bid in "+getTempTableBO().getSubSql(planids);
		getDao().executeUpdate(sql);
//		
		return dividBiddingAndSave(plans, historys, cl);
	}
	
	public void reWritePONumWhenOrderSave(String[] praybids) throws BusinessException{
		
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��
	 * 2011-6-14����10:43:10
	 * @param biddingbids  ����Ʒ��ID  
	 * @param prayNumInfor  Ʒ�ֺ�ͬ����  key:���BID  value:��ͬ����
	 * @throws BusinessException
	 */
	public void reWritePONumOnDel(String[] biddingbids,Map<String,UFDouble> prayNumInfor,boolean isOrderDel) throws BusinessException{
		if((biddingbids == null || biddingbids.length == 0)&&prayNumInfor == null)
			return;
		
		if(biddingbids == null || biddingbids.length ==0)
			biddingbids = prayNumInfor.keySet().toArray(new String[0]);
		
		String sql = "select cupsourcebillrowid,cbiddingbid,nzbnum from zb_bidding_b where isnull(dr,0) = 0 and cbiddingbid  in "+getTempTableBO().getSubSql(biddingbids)+" and cupsourcebilltype = '"+ScmConst.PO_Pray+"'";
		
		Map<String,UFDouble> numInfor = new HashMap<String, UFDouble>();
		
	    List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
				
		if(ldata == null || ldata.size() == 0)
			return;
		
		int len = ldata.size();
		Object[] os = null;
		UFDouble num = null;
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			if(prayNumInfor == null)
				num = PuPubVO.getUFDouble_NullAsZero(os[2]);
			else
				num = PuPubVO.getUFDouble_NullAsZero(os[2]).sub(PuPubVO.getUFDouble_NullAsZero(prayNumInfor.get(ZbPubTool.getString_NullAsTrimZeroLen(os[1]))));
			numInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), num);
		}
		
		if(numInfor.size()==0)
			return;
//		Map<String,UFDouble> numInfor = new HashMap<String, UFDouble>();
		if(isOrderDel){
			for(String key:numInfor.keySet()){
				numInfor.put(key, numInfor.get(key).multiply(-1));
			}
		}
		adjustPlanAccountNum(numInfor);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ƻ��ۼƶ�����   ע��sql�е� -  ��  ����  ��ͬ�����ɾ��ʱ  ����ɾ���ͱ��ɾ��ʱ
	 * 2011-6-14����10:59:36
	 * @param adjustNumInfor  key  �ƻ�BID   NUM��Ҫ��ȥ������
	 * @throws BusinessException
	 */
	public void adjustPlanAccountNum(Map<String,UFDouble> adjustNumInfor) throws BusinessException{
		if(adjustNumInfor == null || adjustNumInfor.size() == 0)
			return;
		String sql2 = " update po_praybill_b set naccumulatenum = coalesce(naccumulatenum,0.0) -? where cpraybill_bid = ? ";
		SQLParameter para = new SQLParameter();
		for(String id:adjustNumInfor.keySet()){
			para.clearParams();
			para.addParam(PuPubVO.getUFDouble_NullAsZero(adjustNumInfor.get(id)));
			para.addParam(ZbPubTool.getString_NullAsTrimZeroLen(id));			
			getDao().executeUpdate(sql2,para);
		}
	}

	private BiddingBodyVO[] tranPlansToBiddingBody(PuPlanInvVO[] plans,boolean isrowno)
	throws Exception {
		if (plans == null || plans.length == 0)
			return null;

		BiddingBodyVO[] bodys = (BiddingBodyVO[]) SingleVOChangeDataBsTool
				.runChangeVOAry(plans, BiddingBodyVO.class,
						CHGPlanTOBidding.class.getName());
		
		BiddingBillVO.dealBiddingInvBodys(bodys);

		if (isrowno)
			ZbPubTool.setVOsRowNoByRule(bodys, "crowno");
		return bodys;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���޹�����ʷ���ɱ���
	 * 2011-6-11����05:21:18
	 * @param plans
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	private BiddingBillVO[] generateBiddingBillNoHistory(PuPlanInvVO[] plans,
			ClientLink cl) throws Exception {
		if (plans == null || plans.length == 0)
			return null;
//		BiddingBillVO bill = new BiddingBillVO();
//		BiddingHeaderVO head = getBiddingHeader(cl);
		BiddingBodyVO[] bodys = tranPlansToBiddingBody(plans,false);
		
		
		List<BiddingBillVO> lbill = dividBiddingByInvClass(bodys, cl);
//		bill.setParentVO(head);
//		bill.setChildrenVO(bodys);
		if(lbill.size()==0)
			throw new BusinessException("�����쳣");
		return pushSaveBidding(lbill.toArray(new BiddingBillVO[0]));
//		return null;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ݷ�����Ϣ���ֱ��
	 * 2011-6-11����05:20:52
	 * @param bodys
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	private List<BiddingBillVO> dividBiddingByInvClass(BiddingBodyVO[] bodys,ClientLink cl) throws Exception{

		CircularlyAccessibleValueObject[][] os = SplitBillVOs.getSplitVOs(bodys, BiddingBodyVO.split_names);

		List<BiddingBillVO> lbill = new ArrayList<BiddingBillVO>();
		CircularlyAccessibleValueObject[] vos = null;
		int len = os.length;
		BiddingBillVO bill = null;
		BiddingHeaderVO head = null;
		for(int i= 0;i<len;i++){
			vos = os[i];
			bill = new BiddingBillVO();
			head = getBiddingHeader(cl);
			bill.setParentVO(head);
			ZbPubTool.setVOsRowNoByRule(vos, "crowno");
			bill.setChildrenVO(vos);
			lbill.add(bill);
		}
		return lbill;
	}
	
	public void setBodyData(BiddingBillVO bill) throws BusinessException{
		if(bill == null)
			return;
		String headkey = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getPrimaryKey());
		if(headkey == null)
			return;
		bill.setChildrenVO(getBiddingBody(headkey));
		bill.setTableVO(BiddingBillVO.tablecode_suppliers, getBiddingSuppliers(headkey));
		bill.setTableVO(BiddingBillVO.tablecode_times, getBiddingTimes(headkey));
	}
	
	private BiddingBodyVO[] getBiddingBody(String headkey) throws BusinessException{
		return (BiddingBodyVO[])getPubBO().queryByCondition(
				BiddingBodyVO.class,
				" isnull(dr,0)=0 and cbiddingid = '" + headkey + "'");
	}
	
	private BiddingSuppliersVO[] getBiddingSuppliers(String headkey) throws BusinessException{
		return (BiddingSuppliersVO[])getPubBO().queryByCondition(
				BiddingSuppliersVO.class,
				" isnull(dr,0)=0 and cbiddingid = '" + headkey + "'");
	}
	
	private BiddingTimesVO[] getBiddingTimes(String headkey) throws BusinessException{
		return (BiddingTimesVO[])getPubBO().queryByCondition(
				BiddingTimesVO.class,
				" isnull(dr,0)=0 and cbiddingid = '" + headkey + "'");
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ֱ̨�ӱ��������� 2011-5-19����04:22:42
	 * @param bills
	 * @return
	 * @throws BusinessException
	 */
	public BiddingBillVO[] pushSaveBidding(BiddingBillVO[] bills)
			throws BusinessException {
		if (bills == null || bills.length == 0)
			return null;
		ArrayList al = new ArrayList();
		int sum =0;
		for (BiddingBillVO bill : bills) {
			bill.validateOnSave(false);
			
			BiddingHeaderVO head = (BiddingHeaderVO)bill.getParentVO();
			if(head == null)
				throw new ValidationException("�����쳣");
			
			BiddingBodyVO[] bodys = (BiddingBodyVO[])bill.getChildrenVO();
			if(bodys == null || bodys.length == 0)
				throw new ValidationException("�����쳣�����ڱ�������Ϊ�յĵ���");
			
			MakeBiddingBO bo = new MakeBiddingBO();
			String[] ids = new String[bodys.length];
			int index =0;
			for(BiddingBodyVO body:bodys){
				ids[index]=body.getCinvmanid();
				if(!al.contains(body.getCinvmanid()))
				   al.add(body.getCinvmanid());
				index++;
			}
			sum=sum+bodys.length;
			bo.checkInvUnique(ids,head.getDmakedate());
		}
		if(al.size()!=sum)
			throw new BusinessException("�����ظ��б�Ʒ��");
			
		BillSave savebo = new BillSave();
		List ltmp = null;
		for(BiddingBillVO bill:bills){
			ltmp = savebo.saveBill(bill);
			bill = (BiddingBillVO)ltmp.get(1);
			setBodyData(bill);
		}

//		AggregatedValueObject[] newbills = savebo.saveBillComVos(bills);
//		String tmpkey = null;
//		for (AggregatedValueObject newbill : newbills) {
//			tmpkey = newbill.getParentVO().getPrimaryKey();
//			newbill.setChildrenVO(getPubBO().queryByCondition(
//					BiddingBodyVO.class,
//					" isnull(dr,0)=0 and cbiddingid = '" + tmpkey + "'"));
//		}
		return bills;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ֱ�����ɱ��鵥�ݲ�����
	 * 2011-5-19����05:37:59
	 * @param plans �ɹ��ƻ�Ʒ����Ϣ
	 * @param prices Ʒ�ֵĹ�����ʷ��Ϣ
	 * @param cl �ͻ��˻���
	 * @return
	 * @throws Exception
	 */
	private BiddingBillVO[] dividBiddingAndSave(PuPlanInvVO[] plans,
			HistoryPriceVO[] prices, ClientLink cl) throws Exception {
		if (plans == null || plans.length == 0)
			return null;
		if (prices == null || prices.length == 0) {// �޹�����ʷ ֱ�����ɱ��
			return generateBiddingBillNoHistory(plans, cl);
		}

		Map<String, String> vendorMap = new HashMap<String, String>();// ��Ӧ�̻���id
																		// --
																		// ��Ӧ�̹���id
		for (HistoryPriceVO price : prices) {
			if (vendorMap.containsKey(price.getCvendorbaseid()))
				continue;
			vendorMap.put(price.getCvendorbaseid(), price.getCvendormangid());
		}

		AbstractDivideBiddingTool tool = null;
		if(ZbPubConst.div_bidding_by_inv)
			tool = new DivideBiddingTool_inv();
		else
		tool = new DivideBiddingTool_vendor();
		// ���ڹ�����ʷ ������ʷ���б��ϸ��
		Object o = tool.divid(prices);
		
		if (o == null) {
			return null;
		}
		Object[] os = (Object[]) o;
		if (os == null || os.length == 0)
			return null;

		Map<String, UFDouble> hisPriceInfor = (Map<String, UFDouble>) os[0];
		List ldata = (List) os[1];
		if (ldata == null || ldata.size() == 0)
			return null;

		BiddingBodyVO[] allbodys = tranPlansToBiddingBody(plans,false);
		appHistoryPrice(allbodys, hisPriceInfor);

		BiddingBillVO[] bills = generateBiddingBills(ldata, allbodys,
				vendorMap, cl);

		return pushSaveBidding(bills);
	}

	private BiddingBillVO[] generateBiddingBills(List ldata,
			BiddingBodyVO[] bodys, Map<String, String> vendorMap, ClientLink cl)
			throws Exception {
		// �����ֵı�� ������ɵ���

		// δ��������ʷ���ֽ���ε�Ʒ�� ��ϳ�һ�����
		Set<BiddingBodyVO> sRemainBody = new HashSet<BiddingBodyVO>();

//		��֯������
		Map<String, BiddingBodyVO> invBodyInfor = new HashMap<String, BiddingBodyVO>();
		for (BiddingBodyVO body : bodys) {
			sRemainBody.add(body);
			invBodyInfor.put(body.getCinvbasid(), body);
		}

		int len = ldata.size();
		Object[] os = null;
		List<String> linv = null;
		Set<String> svendor = null;

		List<BiddingBillVO> lbill = new ArrayList<BiddingBillVO>();
		BiddingBillVO tmpbill = null;
		// BiddingHeaderVO tmphead = null;

		Iterator<String> it = null;
		List<BiddingBodyVO> ltmpbody = new ArrayList<BiddingBodyVO>();
		BiddingBodyVO tmpbody = null;
		List<BiddingSuppliersVO> ltmpvendor = new ArrayList<BiddingSuppliersVO>();
		BiddingSuppliersVO tmpvendor = null;
		for (int i = 0; i < len; i++) {
			os = (Object[]) ldata.get(i);
			linv = (List<String>) os[0];
			svendor = (Set<String>) os[1];

			if (linv == null || linv.size() == 0)
				continue;

			tmpbill = new BiddingBillVO();
			tmpbill.setParentVO(getBiddingHeader(cl));

			// �����Ʒ�ִ���
			it = linv.iterator();
			ltmpbody.clear();
			while (it.hasNext()) {
				tmpbody = invBodyInfor.get(it.next());
				ltmpbody.add(tmpbody);
				sRemainBody.remove(tmpbody);
			}

			if (ltmpbody.size() == 0)// û��Ʒ�ֱ��û�д��ڼ�ֵ
				continue;
			BiddingBodyVO[] tmpbodys = ltmpbody.toArray(new BiddingBodyVO[0]);
			ZbPubTool.setVOsRowNoByRule(tmpbodys, "crowno");
			tmpbill.setChildrenVO(tmpbodys);

			// ����ڹ�Ӧ�̴���
			if (svendor != null && svendor.size() > 0) {
				it = svendor.iterator();
				ltmpvendor.clear();
				while (it.hasNext()) {
					tmpvendor = new BiddingSuppliersVO();
					tmpvendor.setCcustbasid(it.next());
					tmpvendor.setCcustmanid(vendorMap.get(tmpvendor
							.getCcustbasid()));
					tmpvendor.setFisclose(UFBoolean.FALSE);
					tmpvendor.setFistemp(UFBoolean.FALSE);
					ltmpvendor.add(tmpvendor);
				}

				if (ltmpvendor.size() > 0) {
					BiddingSuppliersVO[] tmpvendors = ltmpvendor
							.toArray(new BiddingSuppliersVO[0]);
					ZbPubTool.setVOsRowNoByRule(tmpvendors, "crowno");
					tmpbill.setTableVO("zb_biddingsuppliers", tmpvendors);
				}
			}

			lbill.add(tmpbill);
		}

//		ʣ��û�й�����ʷ��Ʒ��  ��װ��һ������
		if (sRemainBody.size() > 0) {
//			tmpbill = new BiddingBillVO();
//			tmpbill.setParentVO(getBiddingHeader(cl));
//			BiddingBodyVO[] tmpbodys = sRemainBody
//					.toArray(new BiddingBodyVO[0]);
//			ZbPubTool.setVOsRowNoByRule(tmpbodys, "crowno");
//			tmpbill.setChildrenVO(tmpbodys);
//			;
			lbill.addAll(dividBiddingByInvClass(sRemainBody.toArray(new BiddingBodyVO[0]), cl));
		}

		if (lbill.size() == 0)
			return null;

		return lbill.toArray(new BiddingBillVO[0]);
	}

	private void appHistoryPrice(BiddingBodyVO[] bodys,
			Map<String, UFDouble> price) {
		if (price == null || price.size() == 0)
			return;
		for (BiddingBodyVO body : bodys)
			body.setNaverageprice(price.get(body.getCinvbasid()));
	}

	private BiddingHeaderVO getBiddingHeader(ClientLink cl) throws BusinessException {
		BiddingHeaderVO header = new BiddingHeaderVO();
		header.setPk_corp(cl.getCorp());
		header.setPk_billtype(ZbPubConst.ZB_BIDDING_BILLTYPE);

		header.setFisinvcl(UFBoolean.FALSE);
		header.setFisself(UFBoolean.FALSE);
		header.setVoperatorid(cl.getUser());
		header.setDmakedate(cl.getLogonDate());
		header.setDbilldate(cl.getLogonDate());
//		header.setVbillno(getPubBO().getBillNo(ZbPubConst.ZB_BIDDING_BILLTYPE,
//				header.getPk_corp(), null, null));
		header.setVbillstatus(IBillStatus.FREE);
		header.setIbusstatus(ZbPubConst.BIDDING_BUSINESS_STATUE_INIT);
		String para = ZbPubTool.getParam();
		if(para!=null&&!"".equalsIgnoreCase(para))
		   header.setReserve1(para);

		return header;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��ʷ������Ϣ 2011-5-18����03:58:07
	 * @param invids
	 *            Ʒ��ά��
	 * @param sStartdate
	 *            ��ʷʱ�䷶Χ
	 * @param sEnddate
	 * @param sLogCorp
	 *            ��½��˾
	 * @return
	 * @throws BusinessException
	 */
	public HistoryPriceVO[] loadHistoryPriceInfor(String[] invids,
			String sStartdate, String sEnddate, String sLogCorp)
			throws Exception {
		if (invids == null || invids.length == 0)
			return null;
		String whereSql = " h.dorderdate >= '"
				+ sStartdate
				+ "' and h.dorderdate <= '"
				+ sEnddate
				+ "'"
				+ " and h.cauditpsn is not null "
				+ " and h.pk_corp = '"
				+ sLogCorp
				+ "' and b.cbaseid in "
				+ getTempTableBO().getSubSql(invids)
				+ " and coalesce(h.bislatest,'N') = 'Y' and coalesce(h.breturn,'N') = 'N' order by h.dorderdate desc";// �Ƿ����°汾
																						// �Ƿ񷵻�
		String sql = HistoryPriceVO.buildSelectSql(whereSql, true);
		List<HistoryPriceVO> lprice = (List<HistoryPriceVO>) getDao()
				.executeQuery(sql, new BeanListProcessor(HistoryPriceVO.class));
		if (lprice == null || lprice.size() == 0)
			return null;
		return lprice.toArray(new HistoryPriceVO[0]);
	}
	
	public int getBiddingBusiState(String cbiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			throw new BusinessException("�������Ϊ��");
		String sql= "select ibusstatus from zb_bidding_h where cbiddingid = '"+cbiddingid+"'";
		return PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
	}
	
	public void updateBiddingBusiState(String cbiddingid,int istate) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return;
		String sql = "update zb_bidding_h set ibusstatus = '"+istate+"' where cbiddingid = '"+cbiddingid+"'";
		int row = getDao().executeUpdate(sql);
		if(row<=0)
			throw new BusinessException("���ݿ��쳣,��������ҵ��״̬ʧ��");
	}
	
	public void updateBiddingMarkPrice(String cbiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return;
		String sql = "update zb_bidding_b set nmarkprice = "+0+" where cbiddingid = '"+cbiddingid+"'";
		int row = getDao().executeUpdate(sql);
		if(row<=0)
			throw new BusinessException("���ݿ��쳣,���������׼�ʧ��");
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ������ָ�����ָ��Ʒ�ֵ���ʷƽ����
	 * 2011-6-4����04:30:56
	 * @param cbiddingid
	 * @param cinvid
	 * @param nprice
	 * @return
	 * @throws BusinessException
	 */
	public String updateBiddingHistoryPrice(String cbiddingid,String cinvid,UFDouble nprice) throws BusinessException{
		String sql = "update zb_bidding_b set naverageprice = ? where isnull(dr,0) = 0 and cbiddingid = ? and cinvbasid = ?";
		SQLParameter para = new SQLParameter();
		para.addParam(nprice);
		para.addParam(cbiddingid);
		para.addParam(cinvid);
		int i = getDao().executeUpdate(sql,para);
		if(i<=0)
			throw new BusinessException("�޶���ʷƽ����ʧ��");
		sql = "select ts from zb_bidding_b where isnull(dr,0) = 0 and cbiddingid = ? and cinvbasid = ?";
		para.clearParams();
		para.addParam(cbiddingid);
		para.addParam(cinvid);
		String newts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql,para, ResultSetProcessorTool.COLUMNPROCESSOR));
		if(newts == null)
			throw new BusinessException("�޶���ʷƽ����ʧ��");
		return newts;
	}
	
	//�޸ı�������ۼ�����
	public void updatePraBill(BiddingBodyVO body) throws BusinessException{
		//set naccumulatenum = 0.0
		if(body == null)
			return;
		UFDouble nzbnum = PuPubVO.getUFDouble_NullAsZero(body.getNzbnum());
		String csourcebid = PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebillrowid());
		if(csourcebid!=null){
			String  sql=" select coalesce(b.npraynum,0)-coalesce(b.naccumulatenum,0)  from  po_praybill_b b where b.cpraybill_bid ='"+csourcebid+"' and isnull(b.dr,0)=0 ";
			Object o =getDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR);
			String  sqlb=" select b.nzbnum  from  zb_bidding_b b where b.cbiddingbid ='"+body.getPrimaryKey()+"' and isnull(b.dr,0)=0 ";
			Object o1 =getDao().executeQuery(sqlb,ZbBsPubTool.COLUMNPROCESSOR);
			UFDouble temp = nzbnum.sub(PuPubVO.getUFDouble_NullAsZero(o1));
			if(temp.compareTo(PuPubVO.getUFDouble_NullAsZero(o))>0)
				throw new BusinessException("�б��������빺������");
			String sqlu =" update po_praybill_b set naccumulatenum= coalesce(naccumulatenum,0) + "+temp+" where cpraybill_bid = '"+csourcebid+"' and isnull(dr,0)=0";
			getDao().executeUpdate(sqlu);
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У��Ʒ�ֲ����ظ��б�    ��У����Ϻ�������ݽ�������
	 * 2011-6-13����02:24:49
	 * @param ids �б�Ʒ�ֹ���id
	 * @param uLogDate ��ǰ��¼����
	 * @throws BusinessException
	 */
	public void checkInvUnique(String[] ids,UFDate uLogDate) throws BusinessException{
		if(ids == null || ids.length == 0)
			return;
		int idays = ZbPubConst.bidding_inv_unique_time/2;
		UFDate dbegin = uLogDate.getDateBefore(idays);
		UFDate dend = uLogDate.getDateAfter(idays);
		String sql = "select count(0) from zb_bidding_h h inner join zb_bidding_b b on h.cbiddingid = b.cbiddingid " +
				" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0  " +
						"and h.ibusstatus < "+ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE+" and b.cinvmanid in "+getTempTableBO().getSubSql(ids);
		String s = ZbPubTool.getParam();
	    if(s!=null &&!"".equals(s))
	    	sql = sql+" and h.reserve1 = '"+s+"'";
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
			throw new BusinessException("�����ظ��б�Ʒ��");
		
	}
	
	public  Object misBidding(String[] biddingids,String coparate,UFDate uDate) throws Exception{
		if(biddingids == null || biddingids.length == 0)
			return null;
//		У������Ƿ��������
//		��������  ��   ҵ��״̬  û�н���  �б�  �׶�  ��֮��׶�
		String sql = " isnull(dr,0)=0 and cbiddingid in "+getTempTableBO().getSubSql(biddingids);
		Collection c = getDao().retrieveByClause(SmallBiddingHeaderVO.class, sql);
		if(c == null || c.size() ==0 ||c.size()!=biddingids.length)
			throw new BusinessException("�����쳣����ȡ������ϢΪ��");
		List<String> lbiddingid = new ArrayList<String>();
		StringBuffer error = null;
		Iterator<SmallBiddingHeaderVO> it = c.iterator();
		SmallBiddingHeaderVO head = null;
		while(it.hasNext()){
			head = it.next();
			int flag = PuPubVO.getInteger_NullAs(head.getIbusstatus(), -1);
			if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_RESULT){
				if(error == null)
					error = new StringBuffer("���±����������ʧ��");
				error.append(":"+head.getVbillno()+"���б�");
				continue;
			}else if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE){
				if(error == null)
					error = new StringBuffer("���±����������ʧ��");
				error.append(":"+head.getVbillno()+"�б����");
				continue;
			}else if(flag == ZbPubConst.BIDDING_BUSINESS_STATUE_MISS){
				if(error == null)
					error = new StringBuffer("���±����������ʧ��");
				error.append(":"+head.getVbillno()+"������");
				continue;
			}
			lbiddingid.add(head.getCbiddingid());
		}
		
		
		Map<String,UFDateTime> tsInfor = null;
		if(lbiddingid.size()>0){
			sql = "update zb_bidding_h set ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_MISS + " where cbiddingid in "+getTempTableBO().getSubSql((ArrayList)lbiddingid);
			getDao().executeUpdate(sql);


			//�޶� �����ۼƶ�������
			sql = "select distinct cbiddingbid from zb_bidding_b where isnull(dr,0) = 0 and cupsourcebilltype = '"+ScmConst.PO_Pray+"'" +
			" and cbiddingid in"+getTempTableBO().getSubSql((ArrayList)lbiddingid);

			List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNLISTPROCESSOR);
			if(ldata != null && ldata.size() > 0){
				reWritePONumOnDel((String[])ldata.toArray(new String[0]), null,false);
			}
			sql = "select cbiddingid,ts from zb_bidding_h where cbiddingid in"+getTempTableBO().getSubSql((ArrayList)lbiddingid);

			ldata  = (List)getDao().executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
			if(ldata == null || ldata.size() == 0)
				throw new BusinessException("���ݲ����쳣");

			Object[] os = null;
			int len = ldata.size();
			tsInfor = new HashMap<String, UFDateTime>();
			for(int i=0;i<len;i++){
				os = (Object[])ldata.get(i);
				tsInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), new UFDateTime(ZbPubTool.getString_NullAsTrimZeroLen(os[1])));
			}
		}
		return new Object[]{error==null?null:error.toString(),tsInfor};
	}

	public void isExitBadPrice(String ccustmanid,String cbiddingid) throws Exception{
		String sql =" select count(0) from zb_submitprice  where cbiddingid = '"+cbiddingid+"' and cvendorid ='"+ccustmanid+"' and isnull(dr,0)=0 and isubmittype =3 ";
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
			throw new BusinessException("�ù�Ӧ�̴����Ŷ��ⱨ��");
	}
}
