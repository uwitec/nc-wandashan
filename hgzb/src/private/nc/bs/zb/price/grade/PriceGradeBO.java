package nc.bs.zb.price.grade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.zb.price.SubmitPriceBO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.bill.deal.DealVendorPriceBVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.price.grade.PriceGradeVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class PriceGradeBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private ParamSetVO para = null;
	
	private PriceGradeColTool priceTool = null;
	private PriceGradeColTool getPriceGradeCol() throws Exception{
		if(priceTool == null){
			priceTool = new PriceGradeColTool(getPara());
		}
		return priceTool;
	}
	private ParamSetVO getPara() throws DAOException,BusinessException{
		if(para != null)
			return para;
	    String whereSql = "isnull(dr,0) = 0 and pk_corp = '"+SQLHelper.getCorpPk()+"'";
	    Collection c = getDao().retrieveByClause(ParamSetVO.class, whereSql);
	    if(c == null)
	    	throw new BusinessException("获取招标系统设置失败");
	    if(c.size()>1)
	    	throw new BusinessException("招标系统设置数据错误,存在多条");
	    para = (ParamSetVO)c.iterator().next();
	    return para;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）报价分计算
	 * 2011-5-23下午04:36:46
	 * @param bills
	 * @param cl
	 * @throws Exception
	 */
	public void doCol(DealVendorBillVO[] bills,ClientLink cl) throws Exception{
		if(bills == null || bills.length == 0)
			throw new BusinessException("传入数据为空");
		for(DealVendorBillVO bill:bills){
			getPriceGradeCol().setBillVO(bill);
			getPriceGradeCol().col();
		}
		
		doSave(bills,cl.getUser(),cl.getLogonDate());
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）特征后确定
	 * 2011-5-23下午04:06:23
	 * @param bills
	 * @param cl
	 * @throws Exception
	 */
	public void doOK(DealVendorBillVO bill,ClientLink cl) throws Exception{
		if(bill == null)
			return;
		DealInvPriceBVO[] bodys = null;
		bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("数据异常");
		
//		更新报价分调整值
		String sql = " update zb_pricegrade set nadjgrade = ?,cmodify = '"+cl.getUser()+"'," +
				" dmodifydate = '"+cl.getLogonDate().toString()+"' where isnull(dr,0) = 0 and cbiddingid = ? " +
				" and cvendorid = ? and cinvmanid = ?";
		
		SQLParameter para = new SQLParameter();
		for(DealInvPriceBVO body:bodys){
			para.clearParams();
			para.addParam(body.getNadjgrade());
			para.addParam(body.getCbiddingid());
			para.addParam(bill.getHeader().getCcustmanid());
			para.addParam(body.getCinvmanid());
			getDao().executeUpdate(sql, para);
		}
//     更新供应商报价分总分
		getDao().updateVO(bill.getHeader(), BiddingSuppliersVO.PRICE_GRIDE_FIELD);
	}
	
	private String getPriceGradeId(String cbiddingid,String cvendorid,String cinvmanid) throws BusinessException{
		String sql = "select cpricegradeid from zb_pricegrade where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'" +
				" and cvendorid = '"+cvendorid+"' and cinvmanid = '"+cinvmanid+"'";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	private void doSave(DealVendorBillVO[] bills,String sUser,UFDate udate) throws Exception{

		PriceGradeVO.validateDataOnSave(bills, getPara().getNmaxquotatpoints());
		//		回写标段供应商子表  报价分   
		BiddingSuppliersVO[] heads = new BiddingSuppliersVO[bills.length];
		int index = 0;
		List<PriceGradeVO> lnew = new ArrayList<PriceGradeVO>();
		List<PriceGradeVO> lupdate = new ArrayList<PriceGradeVO>();
		PriceGradeVO tmp = null;
		DealInvPriceBVO[] bodys = null;
		for(DealVendorBillVO bill:bills){
			heads[index] = (BiddingSuppliersVO)bill.getHeader();
			bodys = bill.getBodys();
			for(DealInvPriceBVO body:bodys){
				tmp = new PriceGradeVO();
				tmp.setCbiddingid(body.getCbiddingid());
				tmp.setCinvbasid(body.getCinvbasid());
				tmp.setCinvmanid(body.getCinvmanid());
				tmp.setCvendorid(bill.getHeader().getCcustmanid());
				tmp.setCvendorbasid(bill.getHeader().getCcustbasid());
				//				tmp.setStatus(VOStatus.NEW);
				tmp.setNgrade(body.getNgrade());
				tmp.setNadjgrade(UFDouble.ZERO_DBL);
				tmp.setNmaxgrade(body.getNmaxgrade());
				tmp.setNmingrade(body.getNmingrade());
				tmp.setCoparator(sUser);
				tmp.setDmakedate(udate);
				String key = getPriceGradeId(tmp.getCbiddingid(), tmp.getCvendorid(), tmp.getCinvmanid());
				tmp.setPrimaryKey(key);
				if(key == null)
					lnew.add(tmp);
				else{
					tmp.setCmodify(sUser);
					tmp.setDmodifydate(udate);
					lupdate.add(tmp);
				}
			}
			index ++;
		}

		getDao().updateVOArray(heads, BiddingSuppliersVO.PRICE_GRIDE_FIELD);

		//		插入标段供应商品种报价分明细表
		if(lnew.size()>0)
			getDao().insertVOArray(lnew.toArray(new PriceGradeVO[0]));	
		if(lupdate.size()>0)
			getDao().updateVOArray(lupdate.toArray(new PriceGradeVO[0]), PriceGradeVO.update_fieldnames);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取本公司所有进入投标阶段的标书   计算报价分
	 * 1、必须有报价 如果是网上招标校验当前时间必须大于最后一个报价轮次的截止时间
	 * 2、计算完毕后标书不能再进行报价  仍能进行报价   但报价分已不准  需要人工再次修订
	 * 2011-5-4下午02:45:23
	 * @param corpid
	 * @return
	 * @throws BusinessException
	 */
	public BiddingHeaderVO[] queryAllBiddingByCorp(String corpid)
			throws BusinessException {
		String whereSql = " pk_corp = '" + corpid + "' and isnull(dr,0)=0"
				+ " and ibusstatus = "
				+ ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT
				+ " and vbillstatus = " + IBillStatus.CHECKPASS;
		String s = ZbPubTool.getParam();
		 if(s!=null &&!"".equals(s))
			 whereSql = whereSql+ " and reserve1 ='"+ZbPubTool.getParam()+"'";
		List<BiddingHeaderVO> lbiddVo = (List<BiddingHeaderVO>) getDao()
				.retrieveByClause(BiddingHeaderVO.class, whereSql);
		if (lbiddVo == null || lbiddVo.size() == 0)
			return null;
		BiddingHeaderVO[] vos =lbiddVo.toArray(new BiddingHeaderVO[0]);
		VOUtil.ascSort(vos,new String[]{"vbillno"});
		
		return vos;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取标书品种信息  封装平均报价
	 * 2011-5-23上午11:46:57
	 * @param cbiddingid
	 * @return
	 * @throws Exception
	 */
	public Object getBiddingInfor(String cbiddingid) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null){
			return null;
		}
		//查询品种表体    供应商表体   报价表体
		String wheresql = "isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";
		List<DealInvPriceBVO> linv = (List<DealInvPriceBVO>)getDao().retrieveByClause(DealInvPriceBVO.class, wheresql);
		List<DealVendorPriceBVO> lvendor = (List<DealVendorPriceBVO>)getDao().retrieveByClause(DealVendorPriceBVO.class, wheresql +" and coalesce(fisclose,'N') ='N' ");

		if(linv == null || linv.size() == 0)
			throw new BusinessException("标书数据异常，招标品种信息为空");
		if(lvendor == null || lvendor.size() == 0)
			throw new BusinessException("标书数据异常，投标供应商信息为空");

		DealVendorPriceBVO[] vendors = lvendor.toArray(new DealVendorPriceBVO[0]);		
		//按总分排序
		sortVendors(vendors);
		
		
//		数据封装转换
//		DealVendorBillVO[] vendorBills = new DealVendorBillVO[vendors.length];
		List<DealVendorBillVO> lbill = new ArrayList<DealVendorBillVO>();
		DealVendorBillVO tmp = null;
		DealInvPriceBVO[] invs = null;
//		int index = 0;
		for(BiddingSuppliersVO vendor:vendors){
			tmp = new DealVendorBillVO();
			tmp.setParentVO(vendor);
			invs = getInvPriceBVos(vendor.getCcustmanid(),linv);
			if(invs == null || invs.length == 0)
				continue;
			tmp.setChildrenVO(invs);
			lbill.add(tmp);
//			index ++;
		}
		if(lbill.size()<=0)
			return null;
		return lbill.toArray(new DealVendorBillVO[0]);		
	}
	
	private void sortVendors(DealVendorPriceBVO[] vendors){
		for(DealVendorPriceBVO vendor:vendors){
			vendor.setNallgrade(PuPubVO.getUFDouble_NullAsZero(vendor.getNqualipoints()).add(PuPubVO.getUFDouble_NullAsZero(vendor.getNquotatpoints())));
		}
		nc.vo.trade.voutils.VOUtil.ascSort(vendors, DealVendorPriceBVO.asc_sort_fieldnames);
	}

	private SubmitPriceBO pricebo = null;
	public SubmitPriceBO getSubmitPriceBO(){
		if(pricebo == null){
			pricebo = new SubmitPriceBO();
		}
		return pricebo;
	}
	private PriceGradeVO getPriceGride(String cinvid,String cvendorid,String cbiddingid) throws BusinessException{
		String whereSql = "isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'" +
				" and cvendorid = '"+cvendorid+"'" +
						" and cinvmanid = '"+cinvid+"'";
		Collection<PriceGradeVO> c = getDao().retrieveByClause(PriceGradeVO.class, whereSql);
		if(c == null||c.size() == 0)
			return null;
		if(c.size()>1)
			throw new BusinessException("获取报价分异常，存在多条数据");
		return c.iterator().next();		
	}
	private DealInvPriceBVO[] getInvPriceBVos(String cvendorid,List<DealInvPriceBVO> linv) throws Exception{	
		DealInvPriceBVO invC = null;
		PriceGradeVO grade = null;
		UFDouble nmaxprice = null;
		UFDouble nminprice = null;
		List<DealInvPriceBVO> lnewInv = new ArrayList<DealInvPriceBVO>();
		for(DealInvPriceBVO inv:linv){
			invC = (DealInvPriceBVO)ObjectUtils.serializableClone(inv);
//			nmaxprice = getSubmitPriceBO().getAveragePrice(inv.getCbiddingid(), cvendorid,inv.getCinvmanid());
			nmaxprice = getSubmitPriceBO().getHignPrice(inv.getCbiddingid(), cvendorid, inv.getCinvmanid(), true);
			nminprice = getSubmitPriceBO().getLowestPrice(inv.getCbiddingid(), cvendorid, inv.getCinvmanid(), true);
			if(nmaxprice.equals(UFDouble.ZERO_DBL)||nminprice.equals(UFDouble.ZERO_DBL))
				return null;//存在没有报价的品种  不能计算报价分
			invC.setNprice(nmaxprice);
			invC.setNllowerprice(nminprice);
			grade = getPriceGride(inv.getCinvmanid(), cvendorid, inv.getCbiddingid());
			if(grade != null){
				invC.setNgrade(grade.getNgrade());
				invC.setNadjgrade(grade.getNadjgrade());
				invC.setNmaxgrade(grade.getNmaxgrade());
				invC.setNmingrade(grade.getNmingrade());
			}
			lnewInv.add(invC);
		}
		return lnewInv.toArray(new DealInvPriceBVO[0]);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 获取标段的报价分明细数据  
	 * 2011-5-27下午01:41:43
	 * @param cbiddingid
	 * @return  key ：标段+存货+供应商
	 * @throws BusinessException
	 */
	public Map<String, UFDouble> getPriceGrade(String cbiddingid)
			throws BusinessException {
		String wheresql = " isnull(dr,0)=0 and cbiddingid = '" + cbiddingid
				+ "'";
		Collection<PriceGradeVO> c = getDao().retrieveByClause(
				PriceGradeVO.class, wheresql);
		if (c == null || c.size() == 0)
			return null;
		Iterator<PriceGradeVO> it = c.iterator();
		Map<String, UFDouble> gradeInfor = new HashMap<String, UFDouble>();
		String key = null;
		PriceGradeVO grade = null;
		while (it.hasNext()) {
			grade = it.next();
			key = ZbPubTool.getString_NullAsTrimZeroLen(grade.getCbiddingid())
					+ ZbPubTool.getString_NullAsTrimZeroLen(grade
							.getCinvmanid())
					+ ZbPubTool.getString_NullAsTrimZeroLen(grade
							.getCvendorid());
			gradeInfor.put(key, PuPubVO.getUFDouble_NullAsZero(
					grade.getNgrade()).add(
					PuPubVO.getUFDouble_NullAsZero(grade.getNadjgrade())));
		}
		return gradeInfor;
	}
}
