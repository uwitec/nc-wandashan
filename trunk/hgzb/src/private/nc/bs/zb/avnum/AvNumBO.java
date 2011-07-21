package nc.bs.zb.avnum;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvNumHeadVO;
import nc.vo.zb.avnum.AvVendorVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.entry.ZbResultHeadVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;

public class AvNumBO {

	private HYPubBO bo = null;

	private HYPubBO getHYPubBO() {
		if (bo == null) {
			bo = new HYPubBO();
		}
		return bo;
	}
	
	private BaseDAO dao = null;
	public BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}

	public HYBillVO loadAvNumBillVO(String cbiddingid, ClientLink cl)
			throws Exception {
		SuperVO[] head = (SuperVO[]) getHYPubBO().queryByCondition(
				BidEvaluationHeaderVO.class,
				" cbiddingid ='" + cbiddingid + "' and isnull(dr,0)=0");
		if (head == null || head.length == 0)
			return null;
		head[0].setAttributeValue("vbillstatus",IBillStatus.FREE);
		SuperVO[] body = (SuperVO[]) getHYPubBO().queryByCondition(
				BiEvaluationBodyVO.class,
				" cevaluationid ='" + head[0].getPrimaryKey()
						+ "' and isnull(dr,0)=0");

		if (body != null && body.length > 0) {
			int len = body.length;
			for (int i = 0; i < len; i++) {

				SuperVO[] vvo = (SuperVO[]) getHYPubBO().queryByCondition(
						BidSlvendorVO.class,
						" cevaluationbid ='"
								+ body[i].getAttributeValue("cevaluationbid")
								+ "' and  isnull(dr,0)=0");
				((BiEvaluationBodyVO) body[i])
						.setBidSlvendorVOs((BidSlvendorVO[]) vvo);
			}

		}
		HYBillVO bill = new HYBillVO();
		bill.setParentVO(head[0]);
		bill.setChildrenVO(body);
		HYBillVO abill = dealVO(bill, cl);
		return abill;
	}

	private HYBillVO dealVO(HYBillVO bill, ClientLink cl) throws Exception {

		HYBillVO abill = new HYBillVO();

		BidEvaluationHeaderVO bvo = (BidEvaluationHeaderVO) bill.getParentVO();
		AvNumHeadVO ahead = dealHeadVO(bvo, cl);
		BiEvaluationBodyVO[] bbody = (BiEvaluationBodyVO[]) bill
				.getChildrenVO();
		AvNumBodyVO[] abody = dealBodyVO(bbody);
		abill.setParentVO(ahead);
		abill.setChildrenVO(abody);
		return abill;
	}

	private AvNumHeadVO dealHeadVO(BidEvaluationHeaderVO bvo, ClientLink cl)
			throws Exception {

		AvNumHeadVO ahead = new AvNumHeadVO();
		ahead.setCbiddingid(bvo.getCbiddingid());
		ahead.setPk_busitype(bvo.getPk_busitype());
		ahead.setVemployeeid(bvo.getVemployeeid());
		ahead.setDbilldate(cl.getLogonDate());
		ahead.setDmakedate(cl.getLogonDate());
		ahead.setVoperatorid(cl.getUser());
		ahead.setPk_billtype(ZbPubConst.ZB_AVNUM_BILLTYPE);
		ahead.setVbillstatus(IBillStatus.FREE);
		ahead.setPk_corp(cl.getCorp());
		ahead.setPk_deptdoc(bvo.getPk_deptdoc());
		ahead.setVbillno(getHYPubBO().getBillNo(ZbPubConst.ZB_AVNUM_BILLTYPE,
				cl.getCorp(), null, null));
		ZbBsPubTool.dealDef(ahead, bvo);
		return ahead;
	}

	private AvNumBodyVO[] dealBodyVO(BiEvaluationBodyVO[] bbody) {

		if (bbody == null || bbody.length == 0)
			return null;
		int len = bbody.length;
		AvNumBodyVO[] abody = new AvNumBodyVO[len];
		for (int i = 0; i < len; i++) {
			AvNumBodyVO a = new AvNumBodyVO();
			BiEvaluationBodyVO b = bbody[i];
			BidSlvendorVO[] bbvo = b.getBidSlvendorVOs();
			a.setCinvbasid(b.getCinvbasid());
			a.setCinvmanid(b.getCinvmanid());
			a.setCinvclid(b.getCinvclid());
			a.setCrowno(b.getCrowno());
			a.setCsourcebillbid(b.getCevaluationbid());
			a.setCsourcebillhid(b.getCevaluationid());
			a.setCsourcetype(ZbPubConst.ZB_EVALUATION_BILLTYPE);
			a.setCupsourcebillid(b.getCupsourcebillid());
			a.setCupsourcebillrowid(b.getCupsourcebillrowid());
			a.setCupsourcebilltype(b.getCupsourcebilltype());
			a.setCunitid(b.getCunitid());
			a.setNzbnum(b.getNzbnum());
			a.setNzbprice(b.getNzbprice());
			ZbBsPubTool.dealDef(a, b);
			AvVendorVO[] abbvo = dealBbvo(bbvo);
			a.setAvVendorVO(abbvo);
			abody[i] = a;
		}
		return abody;
	}

	private AvVendorVO[] dealBbvo(BidSlvendorVO[] bbvo) {

		if (bbvo == null || bbvo.length == 0)
			return null;
		int len = bbvo.length;
		AvVendorVO[] abbvo = new AvVendorVO[len];
		for (int i = 0; i < len; i++) {
			AvVendorVO a = new AvVendorVO();
			BidSlvendorVO b = bbvo[i];
			a.setBisfollow(b.getBisfollow());
			a.setCcustbasid(b.getCcustbasid());
			a.setCcustmanid(b.getCcustmanid());
			a.setCrowno(b.getCrowno());
			a.setIdef1(b.getIdef1());
			a.setIdef2(b.getIdef2());
			a.setNdef1(b.getNdef1());
			a.setNdef2(b.getNdef2());
			a.setNdef3(b.getNdef3());
			a.setNminnum(b.getNminnum());
			a.setNwinpercent(b.getNwinpercent());
			a.setNzbmny(b.getNzbmny());
			a.setNzbnum(b.getNzbnum());
			a.setVdef1(b.getVdef1());
			a.setVdef2(b.getVdef2());
			a.setVdef3(b.getVdef3());
			a.setVdef4(b.getVdef4());
			a.setVdef5(b.getVdef5());
			abbvo[i] = a;
		}
		return abbvo;
	}
	public HYBillVO saveHyBillVO(HYBillVO billvo) throws Exception {
		if (billvo == null)
			return null;
		AvNumHeadVO head  = (AvNumHeadVO) billvo.getParentVO();
		AvNumBodyVO[] body = (AvNumBodyVO[]) billvo.getChildrenVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey())!=null){
			for(AvNumBodyVO vo :body)
				vo.setStatus(VOStatus.UPDATED);
		}
		
		HashMap<String, AvVendorVO[]> map = new HashMap<String, AvVendorVO[]>();
		int len = body.length;
		for (int i = 0; i < len; i++) {
			AvVendorVO[] bbvos = (AvVendorVO[]) body[i].getAvVendorVO();
			map.put(body[i].getCinvmanid(), bbvos);
		}
		
		ArrayList<AvVendorVO> al = new ArrayList<AvVendorVO>();
		
		HYBillVO bill = (HYBillVO) getHYPubBO().saveBill(billvo);
		for (AvNumBodyVO bvo : body) {
			AvVendorVO[] bbvos = map.get(bvo.getCinvmanid());
			if(bbvos !=null){
				for (AvVendorVO bbvo : bbvos) {
					bbvo.setCavnumbid(bvo.getCavnumbid());
					al.add(bbvo);
				}
			}
			bvo.setAvVendorVO(bbvos);
		}
		bill.setChildrenVO(body);
		
		int size = al.size();
		AvVendorVO[] vos = new AvVendorVO[size];
		for (int i = 0; i < size; i++) {
			vos[i] = (AvVendorVO) al.get(i);
		}
		
		if (PuPubVO.getString_TrimZeroLenAsNull(vos[0].getPrimaryKey()) == null){
			getHYPubBO().insertAry(vos);
		}
		else{
			getHYPubBO().updateAry(vos);
		}
		return bill;
	}
	
	// 对应下游的中标结果若审批通过后不可在进行调整
	public void  isExitResult(AvNumHeadVO head) throws BusinessException{
		if(head ==null)
			return;
		String sql =" select count(0) from zb_avnum_b b join zb_result_b rb on rb.csourcebillbid= b.csourcebillbid join  " +
				"zb_result_h h on h.czbresultid = rb.czbresultid where h.vbillstatus =1 and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and isnull(rb.dr,0)=0 " +
				" and h.cbiddingid ='" +head.getCbiddingid()+"'";
		Object o = getBaseDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR);
		if(PuPubVO.getInteger_NullAs(o,new Integer(-1)).intValue()>0 )
			throw new BusinessException("中标结果审批通过,不可在进行调整");
	}
	//批调整中标审批表的中标数量  中标结果 也调整中标数量
   public void  updateAvNum(HYBillVO newbill) throws BusinessException{
		
	   if(newbill==null)
		   return;
	   AvNumBodyVO[] vos = (AvNumBodyVO[])newbill.getChildrenVO();
	   if(vos==null|| vos.length==0)
		   return;
//	   Map<String,AvVendorVO> map = new HashMap<String,AvVendorVO>();
	   for(AvNumBodyVO vo:vos){
		   AvVendorVO[] bvos =(AvVendorVO[])vo.getAvVendorVO();
		   for(AvVendorVO bvo:bvos){
			   UFDouble zbnum =PuPubVO.getUFDouble_NullAsZero(bvo.getNzbnum());
			   UFDouble zbmny=PuPubVO.getUFDouble_NullAsZero(bvo.getNzbmny());
			   String  ccustmanid =PuPubVO.getString_TrimZeroLenAsNull(bvo.getCcustmanid());
			   UFDouble nwinpercent =PuPubVO.getUFDouble_NullAsZero(bvo.getNwinpercent());
			   
			   
			   
			   String sql =" update zb_result_b set nzbnum ="+zbnum+",nzbnmy="+zbmny+
			   		" where czbresultbid = (select b.czbresultbid from zb_result_h h join zb_result_b b on h.czbresultid=b.czbresultid" +
			   		" where h.ccustmanid = '"+ccustmanid+"' and b.csourcebillbid='"+vo.getCsourcebillbid()+"' and isnull(h.dr,0)=0 and isnull(b.dr,0)=0)";
			   int row = getBaseDao().executeUpdate(sql);
			   
			   
			   
			   
			   String sql1 =" update zb_slvendor set nzbnum ="+zbnum+",nzbmny="+zbmny+",nwinpercent="+nwinpercent+
		   		" where ccustmanid ='"+ccustmanid+"' and cevaluationbid = '"+vo.getCsourcebillbid()+"' and isnull(dr,0)=0";
			   int row1 = getBaseDao().executeUpdate(sql1);
			   if(row1<=0)
		  			throw new BusinessException("数据库异常,中标评审表更新失败");
		   }
	   }
	   
	}
   
//   private void pushSaveEntry(Map<String, AvVendorVO> map, AvNumBodyVO[] bodys) {
//		Map<String, List<AvNumBodyVO>> vendorInfor = new HashMap<String, List<AvNumBodyVO>>();// key// --供应商id// value// --品种信息
//		List<AvNumBodyVO> ltmp = null;// 品种信息 map的value
//		AvVendorVO[] vendorTmps = null;// 该品种的供应商
//		
//		Set<String> key1 = map.keySet();
//		ArrayList<AvVendorVO> al = new ArrayList<AvVendorVO>();
//		for (Iterator it = key1.iterator(); it.hasNext();) {
//			al.add(map.get(it.next()));
//		}
//		
//		if (al == null || al.size() == 0)
//			return;
//		
//		int size = al.size();
//		
//		String key = null;
//		for (int i = 0; i < size; i++) {
//			AvVendorVO ven = al.get(i);
//			key = ven.getCcustmanid() + "&" + ven.getCcustbasid();
//			for (AvNumBodyVO body : bodys) {
//				vendorTmps = body.getAvVendorVO();
//				if (vendorTmps == null || vendorTmps.length == 0)
//					return;
//				
//				for (AvVendorVO vendorTmp : vendorTmps) {
//					if (key.equalsIgnoreCase(vendorTmp.getCcustmanid() + "&"+ ven.getCcustbasid())) {
//						if (PuPubVO.getUFDouble_NullAsZero(vendorTmp.getNzbnum()).equals(UFDouble.ZERO_DBL)) {
//							break;
//						}
//						
//						if (vendorInfor.containsKey(key)) {
//							ltmp = vendorInfor.get(key);
//						} else
//							ltmp = new ArrayList<AvNumBodyVO>();
//						
//						AvNumBodyVO bodyvo = new AvNumBodyVO();
//						bodyvo = (AvNumBodyVO) body.clone();
//						bodyvo.setNzbnum(PuPubVO.getUFDouble_NullAsZero(vendorTmp.getNzbnum()));// 设置该品种的中标数量
//						ltmp.add(bodyvo);
//						vendorInfor.put(key, ltmp);
//						break;
//					}
//				}
//			}
//		}
//		
//		 for (Map.Entry entry : vendorInfor.entrySet()) {
//			 
//				HYBillVO zbillvo = new HYBillVO();
//				String ekey = entry.getKey().toString();
//				String[] strs = ekey.split("&");
//				ZbResultHeadVO zhead = dealHeadVO1(head, strs[1], strs[0], cl);
//				List<BiEvaluationBodyVO> values = (List<BiEvaluationBodyVO>) entry.getValue();
//				BiEvaluationBodyVO[] vos =values.toArray(new BiEvaluationBodyVO[0]);
//				ZbResultBodyVO[] supervos =(ZbResultBodyVO[])SingleVOChangeDataBsTool.runChangeVOAry(vos,ZbResultBodyVO.class,CHGZB02TOZB05.class.getName());
//				checkData(zhead,supervos);
//				zbillvo.setParentVO(zhead);
//				zbillvo.setChildrenVO(supervos);
//				
//				new PfUtilBO().processAction("WRITE",ZbPubConst.ZB_Result_BILLTYPE,cl.getLogonDate().toString(), null,zbillvo, null);
//			}    
//	}
   
	private ZbResultHeadVO dealHeadVO1(BidEvaluationHeaderVO head,String custbasid,String custmanid,ClientLink cl) throws Exception{
		ZbResultHeadVO zhead =new ZbResultHeadVO();
		zhead.setCbiddingid(head.getCbiddingid());
		zhead.setCcustbasid(custbasid);
		zhead.setCcustmanid(custmanid);
		zhead.setDbilldate(cl.getLogonDate());
		zhead.setDmakedate(cl.getLogonDate());
		zhead.setPk_billtype(ZbPubConst.ZB_Result_BILLTYPE);
		zhead.setPk_corp(cl.getCorp());
		zhead.setVemployeeid(head.getVemployeeid());
		zhead.setVoperatorid(cl.getUser());
		zhead.setVbillstatus(IBillStatus.FREE);
		zhead.setPk_deptdoc(head.getPk_deptdoc());
		zhead.setVbillno(getHYPubBO().getBillNo(ZbPubConst.ZB_Result_BILLTYPE,cl.getCorp(),null,null));
		ZbBsPubTool.dealDef(zhead, head);
		return zhead;
	}
	public void checkData(ZbResultHeadVO head,ZbResultBodyVO[] bodys)throws BusinessException{
		
		if(head ==null)
			throw new BusinessException("表头数据不能为空");
		if(bodys==null || bodys.length==0)
			throw new BusinessException("表头数据不能为空");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getCbiddingid())==null)
			throw new BusinessException("标书不能为空");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getCcustbasid())==null || PuPubVO.getString_TrimZeroLenAsNull(head.getCcustmanid())==null)
			throw new BusinessException("供应商不能为空");
		
		for(ZbResultBodyVO body:bodys){
			if(PuPubVO.getString_TrimZeroLenAsNull(body.getCinvbasid())==null || PuPubVO.getString_TrimZeroLenAsNull(body.getCinvmanid())==null)
				throw new BusinessException("品种不能为空");
			if(PuPubVO.getUFDouble_ZeroAsNull(body.getNorderprice())==null)
				throw new BusinessException("中标价格不能为空");
			if(PuPubVO.getUFDouble_ZeroAsNull(body.getNzbnum())==null)
				throw new BusinessException("中标数量不能为空");
			if(PuPubVO.getUFDouble_ZeroAsNull(body.getNzbnmy())==null)
				throw new BusinessException("中标金额不能为空");
		}
		
	}
   
   //删除孙表
	public void deleteBill(AvNumBodyVO[] bodys) throws BusinessException {
		for (AvNumBodyVO body : bodys) {
			String cavnumbid = body.getPrimaryKey();
			String sql = " update zb_avvendor set dr=1 where cavnumbid = '"+ cavnumbid + "' and isnull(dr,0)=0 ";
			getBaseDao().executeUpdate(sql);
		}
	  }
}
