package nc.bs.zb.comments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.pf.changedir.CHGZB02TOZB05;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.zb.pub.SingleVOChangeDataBsTool;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.entry.ZbResultBodyVO;
import nc.vo.zb.entry.ZbResultHeadVO;
import nc.vo.zb.pub.ZbPubConst;

public class CommentsBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private  AbstractSplitNumCol col = null;
	public AbstractSplitNumCol getSplitNumCol(){
		if(col == null){
			if(ZbPubConst.comment_split_num_flag)
				col = new SplitNumCol2();
			else
				col = new SplitNumCol();
		}
		return col;
	}
	
	private HYPubBO bo =null;
	private HYPubBO getHYPubBO(){
		if(bo==null){
			bo = new HYPubBO();
		}
		return bo;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）自动分量
	 * 2011-5-24下午08:05:44
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	public BiEvaluationBodyVO[] autoSplitNum(HYBillVO bill) throws Exception{
		if(bill == null)
			throw new BusinessException("传入数据为空");
		BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)bill.getParentVO();
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("品种数据为空");
		String cbiddingid = PuPubVO.getString_TrimZeroLenAsNull(head.getCbiddingid());
		if(cbiddingid == null){
			throw new BusinessException("标书信息为空");
		}


		//入围供应商是一个时不走分量算法

		if(bodys[0].getBidSlvendorVOs().length==1)
			return splitNumWhenOneVendor(bill);

		ISplitNumPara para = null;
		if(ZbPubConst.comment_split_num_flag){
			para = new SplitNumColPara2(getDao(),cbiddingid,bill);
		}else
			para = new SplitNumColPara(getDao(),cbiddingid,bill);
		getSplitNumCol().setPara(para);
		getSplitNumCol().col();
		return (BiEvaluationBodyVO[])para.getBill().getChildrenVO();
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）入围供应商为1个时的分量操作
	 * 2011-6-11下午03:53:20
	 * @param bill
	 * @return
	 * @throws BusinessException
	 */
	private BiEvaluationBodyVO[] splitNumWhenOneVendor(HYBillVO bill) throws BusinessException{
//		HYBillVO bill = para.getBill();
		if(bill == null)
			throw new BusinessException("数据为空");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("品种数据为空");
		
		BidSlvendorVO[] vendors = null;
		
		for(BiEvaluationBodyVO body:bodys){
			vendors = body.getBidSlvendorVOs();
			if(vendors == null || vendors.length == 0||vendors.length != 1)
				throw new BusinessException("供应商数据异常");
			
			vendors[0].setNzbnum(body.getNzbnum());
			vendors[0].setNzbmny(body.getNzbnum().multiply(body.getNzbprice()));
			vendors[0].setNwinpercent(U100);
		}
		return bodys;
	}
	
	private UFDouble U100 = new UFDouble(100);
	
	public BiEvaluationBodyVO[] doSaveSplitNumInfor(String cbiddingid,BiEvaluationBodyVO[] bodys) throws Exception{
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("数据为空");
		Collection c = null;
		for(BiEvaluationBodyVO body:bodys){
			getDao().updateVOArray(body.getBidSlvendorVOs(), BidSlvendorVO.update_fields);
			c = getDao().retrieveByClause(BidSlvendorVO.class, "isnull(dr,0)=0 and cevaluationbid = '"+body.getPrimaryKey()+"'");
			body.setBidSlvendorVOs(c==null?null:(BidSlvendorVO[])c.toArray(new BidSlvendorVO[0]));
		}
		return bodys;
	}
	
	public void pushSaveEntry(HYBillVO billvo,Object o) throws Exception{
		
		ClientLink cl =(ClientLink)o;
		
		Map<String, List<BiEvaluationBodyVO>> vendorInfor = new HashMap<String, List<BiEvaluationBodyVO>>();// key --供应商id  value --品种信息
		List<BiEvaluationBodyVO> ltmp = null;// 品种信息 map的value
		BidEvaluationHeaderVO head =(BidEvaluationHeaderVO)billvo.getParentVO();
		BiEvaluationBodyVO[] bodys =(BiEvaluationBodyVO[])billvo.getChildrenVO();
		if(head ==null || bodys==null || bodys.length==0)
			return;
		BidSlvendorVO[] vendors = bodys[0].getBidSlvendorVOs();// 获取该标段的所有入围供应商
		BidSlvendorVO[] vendorTmps = null;// 该品种的供应商
		String key = null;
		for(BidSlvendorVO vendor:vendors){
			key = vendor.getCcustmanid()+"&"+vendor.getCcustbasid();
			for(BiEvaluationBodyVO body:bodys){
				vendorTmps = body.getBidSlvendorVOs();
				if(vendorTmps ==null || vendorTmps.length==0)
					return;
				for(BidSlvendorVO vendorTmp:vendorTmps){
					if(key.equalsIgnoreCase(vendorTmp.getCcustmanid()+"&"+vendor.getCcustbasid())){
//						if(PuPubVO.getUFDouble_NullAsZero(vendorTmp.getNzbnum()).equals(UFDouble.ZERO_DBL)){
//							break;
//						}
						if(vendorInfor.containsKey(key)){
							ltmp = vendorInfor.get(key);
						}else
							ltmp= new ArrayList<BiEvaluationBodyVO>();
						BiEvaluationBodyVO bodyvo = new BiEvaluationBodyVO();
						bodyvo=(BiEvaluationBodyVO)body.clone();
						bodyvo.setNzbnum(PuPubVO.getUFDouble_NullAsZero(vendorTmp.getNzbnum()));//设置该品种的中标数量
						
						ltmp.add(bodyvo);
						vendorInfor.put(key, ltmp);
						break;
					}
				}
			}
		}
		 for (Map.Entry entry : vendorInfor.entrySet()) {
			 
			HYBillVO zbillvo = new HYBillVO();
			String ekey = entry.getKey().toString();
			String[] strs = ekey.split("&");
			ZbResultHeadVO zhead = dealHeadVO(head, strs[1], strs[0], cl);
			List<BiEvaluationBodyVO> values = (List<BiEvaluationBodyVO>) entry.getValue();
			BiEvaluationBodyVO[] vos =values.toArray(new BiEvaluationBodyVO[0]);
			ZbResultBodyVO[] supervos =(ZbResultBodyVO[])SingleVOChangeDataBsTool.runChangeVOAry(vos,ZbResultBodyVO.class,CHGZB02TOZB05.class.getName());
			checkData(zhead,supervos);
			zbillvo.setParentVO(zhead);
			zbillvo.setChildrenVO(supervos);
			
			new PfUtilBO().processAction("WRITE",ZbPubConst.ZB_Result_BILLTYPE,cl.getLogonDate().toString(), null,zbillvo, null);
		}    
	}

	private ZbResultHeadVO dealHeadVO(BidEvaluationHeaderVO head,String custbasid,String custmanid,ClientLink cl) throws Exception{
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
			if(PuPubVO.getUFDouble_ZeroAsNull(body.getNzbnum())!=null){
				if(PuPubVO.getUFDouble_ZeroAsNull(body.getNzbnmy())==null)
					throw new BusinessException("中标金额不能为空");
			}
		}
	}
	
	public void isHaveBill(String cevaluationid,String cbiddingid) throws BusinessException {
		String sql = " select count(0) from  zb_result_b b join zb_result_h h on h.czbresultid = b.czbresultid "
				+ " where b.csourcebillhid = '"+ cevaluationid+ "' and isnull(b.dr,0)=0 and isnull(h.dr,0)=0 and h.vbillstatus =1";
		Object o = getDao().executeQuery(sql, new ColumnProcessor());
		if (PuPubVO.getInteger_NullAs(o, ZbPubConst.IZBRESULTTYPE).intValue() > 0)
			throw new BusinessException("存在着已经审批下游单据,不能弃审");
		String sql1 =" select count(0) from zb_avnum_h h where h.cbiddingid= '"+cbiddingid+"' and isnull(h.dr,0)=0";
		Object o1 = getDao().executeQuery(sql1, new ColumnProcessor());
		if (PuPubVO.getInteger_NullAs(o1, ZbPubConst.IZBRESULTTYPE).intValue() > 0)
			throw new BusinessException("存在着分量调整单,不能弃审");
	}
	
//  删除下游单据
	public void deleteDownBill(String cevaluationid) throws BusinessException{
		String sql = " update zb_result_h set dr=1 where czbresultid in ( select b.czbresultid from zb_result_b b  where b.csourcebillhid = '"+cevaluationid+"' and isnull(b.dr,0)=0)";
		String sql1 =" update zb_result_b b set b.dr=1  where b.csourcebillhid = '"+cevaluationid+"' and isnull(b.dr,0)=0";
//		String sql2 =" update zb_bidding_h b set b.ibusstatus=3  where b.cbiddingid = '"+cresultid+"' and isnull(b.dr,0)=0";//回写标书业务状态
		int row = getDao().executeUpdate(sql);
		int row1 = getDao().executeUpdate(sql1);
//		int row2 = getDao().executeUpdate(sql2);
//          if(row<=0 || row1<=0)
//  			throw new BusinessException("数据库异常,删除下游单据失败");
	}
	
	    //删除孙表
	public void deleteBill(BiEvaluationBodyVO[] bodys) throws BusinessException {
		for (BiEvaluationBodyVO body : bodys) {
			String cevaluationbid = body.getPrimaryKey();
			String sql = " update zb_slvendor set dr=1 where cevaluationbid = '"+ cevaluationbid + "' and isnull(dr,0)=0 ";
			getDao().executeUpdate(sql);
		}
	  }
	
    //回写标书的业务状态
	public void reWriteBill(BidEvaluationHeaderVO head) throws BusinessException {
		if(head!=null){
			String cbiddingid = head.getCbiddingid();
			String sql = " update zb_bidding_h set ibusstatus=2 where cbiddingid = '"+ cbiddingid + "' and isnull(dr,0)=0 ";
			getDao().executeUpdate(sql);
		}
	  }
	
	public void checkDatas(AggregatedValueObject avo) throws BusinessException{
		BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)avo.getParentVO();
		if(head==null)
			throw new BusinessException("表头信息不存在");
		String cbidding = head.getCbiddingid();
		String sql = " select h.nvendornum from zb_bidding_h h where isnull(h.dr,0)=0 and h.cbiddingid ='"+cbidding+"'";
		String len = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql,ZbBsPubTool.COLUMNPROCESSOR));
		
		
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])avo.getChildrenVO();
		if(bodys==null || bodys.length==0)
			throw new BusinessException("存货信息不存在");
		BidSlvendorVO[] bsvos1 = bodys[0].getBidSlvendorVOs();
		if(bsvos1==null || bsvos1.length==0)
			throw new BusinessException("供应商信息不存在");
		int len1= bsvos1.length;
		if(len1>Integer.parseInt(len))
			throw new BusinessException("入围供应商数量大于标书上可入围的数量");
		for(BiEvaluationBodyVO body:bodys){
			BidSlvendorVO[] bsvos = body.getBidSlvendorVOs();
			if(bsvos==null || bsvos.length==0)
				throw new BusinessException("供应商信息不存在");
			ArrayList al = new ArrayList();
			for(BidSlvendorVO bsvo:bsvos){
				if(al.contains(bsvo.getCcustmanid()))
					throw new BusinessException("存在相同的供应商信息");
					al.add(bsvo.getCinvmanid());
			}
		}
	}
	
	public HYBillVO updateNzbmny(HYBillVO billvo) throws BusinessException{
		if(billvo==null)
			throw new BusinessException("传入数据为空");
		BiEvaluationBodyVO[] bodys =(BiEvaluationBodyVO[])billvo.getChildrenVO();
		if(bodys==null || bodys.length==0)
			throw new BusinessException("表体数据为空");
		HYPubBO bo= new HYPubBO();
		for(BiEvaluationBodyVO body:bodys){
			UFDouble nprice =PuPubVO.getUFDouble_NullAsZero(body.getNzbprice());
			BidSlvendorVO[] svos =(BidSlvendorVO[])bo.queryByCondition(BidSlvendorVO.class, " cevaluationbid='"+body.getPrimaryKey()+"' and isnull(dr,0)=0");
			if(svos == null || svos.length==0)
				throw new BusinessException("供应商信息为空");
			for(BidSlvendorVO svo:svos){
				svo.setNzbmny(nprice.multiply(PuPubVO.getUFDouble_NullAsZero(svo.getNzbnum())));
			}
			getDao().updateVOArray(svos);
			body.setBidSlvendorVOs(svos);
		}
		return billvo;
	}
}
