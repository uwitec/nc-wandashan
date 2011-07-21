package nc.bs.zb.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pf.changedir.CHGBiddingBodyTOSubPrice;
import nc.bs.pf.changedir.CHGSubBodyTOSubPrice;
import nc.bs.zb.pub.SingleVOChangeDataBsTool;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.price.bill.SubmitPriceBillVO;
import nc.vo.zb.price.bill.SubmitPriceBodyVO;
import nc.vo.zb.price.bill.SubmitPriceHeaderVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class SubmitPriceBO {

	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）用于现场报价获取上轮报价轮次
	 * 2011-5-6下午02:37:04
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getNewCircalNoIdForLocal(String cbiddingid,String cvendorid) throws BusinessException{

		if(cbiddingid == null)
			throw new BusinessException("标书信息为空");
		if(cvendorid == null){
			throw new BusinessException("报价供应商信息为空");
		}
		
		String sql = "select count(0) from zb_biddingtimes where cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0";
		//标书报价次数
		int num = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
//		当前供应商报价次数
		sql = "select count(distinct ccircalnoid) from zb_submitprice where cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0" +
				" and cvendorid = '"+cvendorid+"'";
		int vendornum = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
		if(vendornum>num){
			throw new BusinessException("报价次数维护异常，供应商报价次数大于标书的报价次数");
		}
		if(vendornum == num){//产生新的报价次数
			BiddingTimesVO time = new BiddingTimesVO();
			num++;
			time.setCbiddingid(cbiddingid);
			time.setVname("第"+ZbPubTool.tranIntNumToStringNum(num)+"次");			
			time.setCrowno(String.valueOf(num*10));
			time.setStatus(VOStatus.NEW);
			
			return getDao().insertVO(time);
		}else if(vendornum<num){
//			获取已存在的报价次数的id作为本次报价次数
			vendornum++;
			String crowno = String.valueOf(vendornum*10);
			sql = "select cbiddingtimesid from zb_biddingtimes where isnull(dr,0)=0 and crowno = '"+crowno+"' and cbiddingid = '"+cbiddingid+"'";
			String id = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
			if(id == null){
				throw new BusinessException("报价序号为"+crowno+"的报价轮次不存在");
			}
			return id;
		}	
		return null;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）恶意报价处理  记录恶意报价明细  中止该供应商继续报价
	 * 2011-5-21下午12:53:45
	 * @param prices
	 * @throws BusinessException
	 */
	public void dealBadSubmit(SubmitPriceVO[] prices) throws BusinessException{
		for(SubmitPriceVO price:prices){
			if(price.getIsubmittype() != ZbPubConst.BAD_SUBMIT_PRICE)
				throw new BusinessException("数据错误,存在不是恶意报价的数据");
			
			SubmitPriceVO newprice = getSubmitPrice(price);
//			price.setIsubmittype(isubtype);			
			if(newprice!=null)
				throw new BusinessException("您在本轮次已完成报价,恶意报价信息无效");
			
		}
		getDao().insertVOArray(prices);
//		中止供应商报价
		String sql = " update zb_biddingsuppliers set fisclose = 'Y',icloseno = isnull(icloseno,0)+1,cclosetime = '"+new UFDateTime(System.currentTimeMillis())+"'" +
				" where ccustmanid = '"+prices[0].getCvendorid()+"' and cbiddingid = '"+prices[0].getCbiddingid()+"'";
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）
	 * 报价时校验轮次的时间戳是否发生变化 若发生变化 提示供应商重新刷新数据后 再报价
	 * 报价时校验供应商是否已中止，若中止不得报价
	 * 报价时校验轮次长度是否已发生变化若发生变化自动更新前台轮次缓存
	 * 报价提交时校验轮次是否已超出本轮次规定范围
	 * 2011-5-21下午02:02:44
	 * @param prices
	 * @param isubtype
	 * @param userObj
	 * @throws BusinessException
	 */
	private void checkSubmitPrices(SubmitPriceVO[] prices,int isubtype,Object userObj) throws BusinessException{
		String sql;
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && userObj != null){
//			校验轮次ts
			if(userObj == null)
				throw new BusinessException("传入参数异常");
			Object[] os = (Object[])userObj;
			String clientts = PuPubVO.getString_TrimZeroLenAsNull(os[0]);
			int num = PuPubVO.getInteger_NullAs(os[1], -1);
			if(clientts == null || num == -1)
				throw new BusinessException("传入参数异常");
			sql = "select ts from zb_biddingtimes where cbiddingtimesid = '"+prices[0].getCcircalnoid()+"'";
			String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
			if(ts == null)
				throw new BusinessException("数据异常");
			if(!ts.trim().equalsIgnoreCase(clientts.trim()))
				throw new BusinessException("本轮次时间安排已经调整,请刷新数据重新提报");
//			校验轮次安排是否调整
			sql = "select count(0) num from zb_biddingtimes where isnull(dr,0)=0 and  cbiddingid = '"+prices[0].getCbiddingid()+"' and  coalesce(bisfollow, 'N') = 'Y'";
			int newnum = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0);
			if(num != newnum){
				throw new BusinessException("本标段的轮次时间安排已经调整,请刷新数据重新提报");
			}
			
//			报价提交时校验轮次是否已超出本轮次规定范围
			BiddingTimesVO time = (BiddingTimesVO)getDao().retrieveByPK(BiddingTimesVO.class, prices[0].getCcircalnoid());
			if(time == null)
				throw new BusinessException("本标段的轮次时间安排已经调整,该轮次不存在,请刷新数据重新提报");
//			校验时间范围是否超限制
			UFDateTime tbegin = time.getTbigintime();
			UFDateTime tend = time.getTendtime();

//			考虑网络延迟
			sql = "select ndelaytime from zb_parameter_settings where isnull(dr,0) = 0 and pk_corp = '"+SQLHelper.getCorpPk()+"'";
			UFDouble ndelaytime = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
//			当前时间
			UFDateTime tcurrent = new UFDateTime(System.currentTimeMillis());
			if(tcurrent.compareTo(tbegin)<0)
				throw new BusinessException("该投标轮次还没有开始,轮次开始时间为：["+tbegin.toString()+"],服务器当前时间为" +
						"["+tcurrent.toString()+"]");
			if(tcurrent.getMillis()>tend.getMillis()+ndelaytime.doubleValue()){
				throw new BusinessException("该投标轮次已经结束,轮次截止时间为：["+tend.toString()+"],服务器当前时间为" +
						"["+tcurrent.toString()+"]");
			}
		}
		checkBidibusstatus(prices[0].getCbiddingid());
		checkMinPrice(prices);
//		校验报价供应商  当前是否具有该标段的报价能力
		checkVendorForBidding(prices[0].getCbiddingid(), prices[0].getCvendorid());		
	}

	/**
	 * 校验标书的业务状态
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-6-19下午04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	private void checkBidibusstatus(String cbiddingid) throws BusinessException{
		String sql =" select count(0) from zb_bidding_h where cbiddingid ='"+cbiddingid+"' and ibusstatus >1 and isnull(dr,0)=0";
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
			throw new BusinessException("该标书不能继续报价");
	}
	
	/**
	 * 校验本轮报价是否小于上次报价
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-6-19下午04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	private void checkMinPrice(SubmitPriceVO[] prices) throws BusinessException{
		if(prices==null || prices.length==0)
			return;
		String cbiddingid = prices[0].getCbiddingid();
		String vendorid = prices[0].getCvendorid();
		String sql = " select e.cinvmanid,min(e.nprice) from zb_submitprice e where e.cbiddingid ='"+cbiddingid+"' and e.cvendorid ='"+vendorid+"' and coalesce(e.isubmittype,0) <>3 and isnull(e.dr,0)=0 group by e.cinvmanid";
		ArrayList al = (ArrayList)getDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if(al ==null || al.size()==0)
			return;
		int size = al.size();
		Map map = new HashMap();
		for(SubmitPriceVO price:prices){
			if(!map.containsKey(price.getCinvmanid()))
				map.put(price.getCinvmanid(),price.getNprice());
		}
		for(int i=0;i<size;i++){
			Object o = al.get(i);
			if(o==null)
				continue;
			Object[] os = (Object[])o;
			if(os ==null || os.length==0)
				continue;
			if(map.containsKey(os[0]))
				if(PuPubVO.getUFDouble_NullAsZero(map.get(os[0])).compareTo(PuPubVO.getUFDouble_NullAsZero(os[1]))>0)
					throw new BusinessException("本轮报价超出上轮报价");
		}
		
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）提交报价
	 * 2011-5-6下午01:19:51
	 * @param prices 
	 * @param isubtype 报价类型
	 * @throws BusinessException
	 */
	public void submitPrice(SubmitPriceVO[] prices,Integer isubtype,Object userObj) throws BusinessException{
		if(prices == null || prices.length == 0)
			return;
		
		checkSubmitPrices(prices, isubtype,userObj);
		
		if(isubtype == ZbPubConst.BAD_SUBMIT_PRICE){//恶意报价明细记录
			dealBadSubmit(prices);
			return;
		}
		
		// 标书 + 供应商 +品种 唯一   如果存在 update  不存在 insert
		SubmitPriceVO newprice = null;
		List<SubmitPriceVO> lnew = new ArrayList<SubmitPriceVO>();
		List<SubmitPriceVO> lupdate = new ArrayList<SubmitPriceVO>();
		
		String newcircleid = null;
		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
//			现场招标自动产生报价轮次
			newcircleid = getNewCircalNoIdForLocal(prices[0].getCbiddingid(),prices[0].getCvendorid());
		}
		for(SubmitPriceVO price:prices){
			newprice = getSubmitPrice(price);
			price.setIsubmittype(isubtype);			
			if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && newprice!=null)
				throw new BusinessException("您在本轮次已完成报价");
			if(newprice == null){
				if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
					price.setCcircalnoid(newcircleid);
				}
				lnew.add(price);
			}else{
				newprice.setNprice(price.getNprice());
				newprice.setCmodifyid(price.getCoprator());
				newprice.setTmodifytime(price.getTmodifytime());
				lupdate.add(newprice);
			}
		}

		if(lnew.size() > 0)
			getDao().insertVOArray(lnew.toArray(new SubmitPriceVO[0]));
		if(lupdate.size()>0)
			getDao().updateVOArray(lupdate.toArray(new SubmitPriceVO[0]), ZbPubConst.SUBMIT_PRICE_UPDATE_FIELD);
	}

	public void cancelSubmitPrice(String cbiddingid,String cvendorid,String ccircalnoid,Integer isubtype) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return;
		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
			ccircalnoid = getPreCircalnoForLocal(cbiddingid,cvendorid);
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(ccircalnoid)==null)
			return;
		String sql = " update zb_submitprice set dr = 1 where cbiddingid = '"+cbiddingid+"' and ccircalnoid = '"+ccircalnoid+"' and cvendorid = '"+cvendorid+"' and isnull(dr,0)=0 ";
		getDao().executeUpdate(sql);
		
//		是否撤销  标书报价轮次  
	}

	private SubmitPriceVO getSubmitPrice(SubmitPriceVO price)
			throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(price.getCcircalnoid()) == null)
			return null;
		StringBuffer str = new StringBuffer("select * from zb_submitprice "
				+ "where cbiddingid = '" + price.getCbiddingid() + "' "
				+ "and cvendorid = '" + price.getCvendorid() + "' " +
				// "and cinvclid = '"+price.getCinvclid()+"'" +
				" and isnull(dr,0)=0");
		// if(PuPubVO.getString_TrimZeroLenAsNull(price.getCinvmanid())!=null)
		str.append(" and cinvmanid = '" + price.getCinvmanid() + "'");
		str.append(" and ccircalnoid = '" + price.getCcircalnoid() + "'");
		SubmitPriceVO newPrice = (SubmitPriceVO) getDao().executeQuery(
				str.toString(), new BeanProcessor(SubmitPriceVO.class));
		return newPrice;
	}

	/**
	 * 查询轮次最低报价
	 * @param cbiddingid
	 * @param ccirclenoid
	 * @param cinvid
	 * @param isinv
	 * @throws Exception
	 */
	public UFDouble getLastLowerPrice(String cbiddingid,String ccirclenoid,String cinvid,boolean isinv) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null||PuPubVO.getString_TrimZeroLenAsNull(cinvid)==null)
			return null;

		StringBuffer str = new StringBuffer();
		str.append(" select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 ");
		str.append(" and cbiddingid = '"+cbiddingid+"' ");
		str.append(" and ccircalnoid = '"+ccirclenoid+"'");
		if(isinv){
			str.append(" and cinvmanid = '"+cinvid+"'");
		}else{
			str.append(" and cinvclid = '"+cinvid+"'");
		}
		
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(str.toString(), ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	public Map<String, UFDouble> getSubmitPriceInfor(String cbiddingid,String ccirclid,String cvendorid,boolean isinv) throws BusinessException{
		StringBuffer sql = new StringBuffer("select ");
		if(isinv)
			sql.append("cinvmanid");
		else 
			sql.append("cinvclid");
		sql.append(",nprice from zb_submitprice where");
		sql.append(" cbiddingid = '"+cbiddingid+"' and cvendorid = '"+cvendorid+"' and ccircalnoid = '"+ccirclid+"' and isnull(dr,0)=0");
		
		List ldata = (List)getDao().executeQuery(sql.toString(), ResultSetProcessorTool.ARRAYLISTPROCESSOR);
		if(ldata ==  null || ldata.size() == 0)
			return null;
		Map<String,UFDouble> priceInfor = new HashMap<String, UFDouble>();
		Object[] os = null;
		int len = ldata.size();
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			if(os == null || os.length == 0)
				return null;
			priceInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), PuPubVO.getUFDouble_NullAsZero(os[1]));
		}
		return priceInfor;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）校验标书和供应商是否关联
	 * 2011-5-4下午01:20:11
	 * @param cbiddingid 标书id
	 * @param cvendorid 供应商管理id
	 * @throws BusinessException
	 */
	public void checkVendorForBidding(String cbiddingid, String cvendorid)
			throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(cbiddingid) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(cvendorid) == null)
			throw new BusinessException("数据异常");
		String sql = "select count(0) from zb_biddingsuppliers where cbiddingid = '"
				+ cbiddingid
				+ "' "
				+ "and ccustmanid = '"
				+ cvendorid
				+ "' and isnull(dr,0)=0  and coalesce(fisclose,'N') = 'N'";
		int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
				ResultSetProcessorTool.COLUMNPROCESSOR),
				ZbPubTool.INTEGER_ZERO_VALUE);
		if (index > 0)
			return;
		throw new BusinessException("该供应商不能参与选择标书的报价");
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取现场报价产生的标书最新报价次数
	 * 2011-5-7下午01:48:13
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getPreCircalnoForLocal(String cbiddingid,String cvendorid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return null;
		StringBuffer sql = new StringBuffer("select ccircalnoid from (select ccircalnoid from zb_submitprice " +
				" where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null){
			sql.append(" and cvendorid = '"+cvendorid+"'");
		}
		sql.append(" order by ts desc) where  rownum < 2");
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql.toString(), ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取网上报价的第一轮次  且 不属于网上报价的轮次
	 * 2011-5-7下午01:48:13
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getFirstCircalnoForWeb(String cbiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return null;
		String sql = "select cbiddingtimesid from (select cbiddingtimesid from zb_biddingtimes where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"' " +
				" and  coalesce(bisfollow,'N')='N' order by tbigintime) where  rownum < 2";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取标书报价信息 如果轮次id为空 则认为没有报过价
	 * 2011-5-4上午10:46:22
	 * @param cbiddingid 标书id
	 * @param ccirclenoid 上轮次id  为空表示第一次报价
	 * @param cvendorid 供应商id
	 * @param isinv 是否明细招标
	 * @return
	 * @throws BusinessException
	 */
	public  SubmitPriceVO[] getPriceVos(String cbiddingid,String ccirclenoid,String cvendorid,UFBoolean isinv,Integer isubtype) throws Exception{
		//校验数据
		checkVendorForBidding(cbiddingid, cvendorid);
		//获取标书的报价 品种  信息     获取各个品种在报价轮次内的最低报价  报价
		String whereSql = "cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0";
		List<BiddingBodyVO> ldata = (List<BiddingBodyVO>)getDao().retrieveByClause(BiddingBodyVO.class, whereSql);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("标书信息为空");

		BiddingBodyVO[] biddVos = ldata.toArray(new BiddingBodyVO[0]);
		SubmitPriceVO[] prices = (SubmitPriceVO[])SingleVOChangeDataBsTool.
		runChangeVOAry(biddVos, SubmitPriceVO.class, CHGBiddingBodyTOSubPrice.class.getName());

		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null){
			//			获取上次报价轮次
			ccirclenoid = getPreCircalnoForLocal(cbiddingid,cvendorid);
		}

		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE)//现场报价获取 最低报价
			for(SubmitPriceVO price:prices){
				price.setNllowerprice(getLowestPrice(cbiddingid, null,price.getInvID(isinv), isinv.booleanValue()));
			}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null){//第一次报价
			return prices;
		}


		//获取该供应商上轮次报价
		Map<String,UFDouble> priceInfor = getSubmitPriceInfor(cbiddingid, ccirclenoid, cvendorid, isinv.booleanValue());
		if(priceInfor != null && priceInfor.size() > 0){
			for(SubmitPriceVO price:prices){
				String key = price.getInvID(isinv);
				price.setNlastprice(priceInfor.get(key));
				if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE)
					price.setNllowerprice(getLastLowerPrice(cbiddingid, ccirclenoid,key , isinv.booleanValue()));

			}
		}

		

		return prices;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取供应商最低报价  最后一轮次的报价
	 * 2011-5-5下午02:08:55
	 * @param cbiddingid 标书id
	 * @param cvendorid 供应商id
	 * @param cinvid 品种
	 * @param isinv 是否明细报价
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getVendorLowestPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{
//		String sql = " select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"' and cvendorid = '"+cvendorid+"'";
//		if(isinv)
//			sql = sql+" and cinvmanid = '"+cinvid+"'";
//		else 
//			sql = sql + " and cinvclid = '"+cinvid+"'";
//		sql = sql + " order by nprice";
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null||PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			throw new BusinessException("数据异常");
		UFDouble nprice = getLowestPrice(cbiddingid, cvendorid, cinvid, isinv);
//			PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
		return nprice;
	}
    
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）	获取标段该品种的最低报价   如果传入供应商则返回该供应商的最低报价
	 * 2011-5-5下午02:12:11
	 * @param cbiddingid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getLowestPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{
		String sql = " select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";// and cvendorid = '"+cvendorid+"'";
		if(isinv)
			sql = sql+" and cinvmanid = '"+cinvid+"'";
		else 
			sql = sql + " and cinvclid = '"+cinvid+"'";

		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null)
			sql = sql + " and cvendorid = '"+cvendorid+"'";
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取报价中的最高价  
	 * 2011-5-23上午11:15:32
	 * @param cbiddingid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getHignPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{

		String sql = " select max(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";// and cvendorid = '"+cvendorid+"'";
		if(isinv)
			sql = sql+" and cinvmanid = '"+cinvid+"'";
		else 
			sql = sql + " and cinvclid = '"+cinvid+"'";
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null)
			sql = sql + " and cvendorid = '"+cvendorid+"'";

		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取平均报价   最高价+最低价/2
	 * 2011-5-23上午11:06:14
	 * @param cbiddingid
	 * @param cinvid
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getAveragePrice(String cbiddingid,String cvendorid , String cinvid) throws BusinessException{
		UFDouble nmin = getLowestPrice(cbiddingid, cvendorid,cinvid, true);
		UFDouble nmax = getHignPrice(cbiddingid, cvendorid,cinvid, true);
		return (nmin.add(nmax)).div(2);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取供应商报价信息
	 * 2011-5-5下午03:08:03
	 * @param cbiddingid
	 * @return MAP  //key invclid + invmanid + cvendorid   value:submitvo
	 * @throws BusinessException
	 */
	public Map<String,List<SubmitPriceVO>> getVendorSubmitPrices(String cbiddingid) throws BusinessException{
		List<SubmitPriceVO> lprice = (List<SubmitPriceVO>)getDao().retrieveByClause(SubmitPriceVO.class, " isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'");
		if(lprice == null || lprice.size() == 0)
			throw new BusinessException("获取该标书的报价信息为空");
		Map<String,List<SubmitPriceVO>> priceInfor = new HashMap<String, List<SubmitPriceVO>>();
		
		List<SubmitPriceVO> ltmp = null;
		String key = null;
		for(SubmitPriceVO price:lprice){
			key = ZbPubTool.getString_NullAsTrimZeroLen(price.getCinvclid())+ZbPubTool.getString_NullAsTrimZeroLen(price.getCinvmanid())
			+ZbPubTool.getString_NullAsTrimZeroLen(price.getCvendorid());
			if(priceInfor.containsKey(key)){
				ltmp = priceInfor.get(key);
			}else
				ltmp = new ArrayList<SubmitPriceVO>();
			ltmp.add(price);
			priceInfor.put(key, ltmp);
		}
		return priceInfor;
	}
	
	private int getSubType(String cbiddingid) throws BusinessException{
		String sql = "select izbtype from zb_bidding_h where cbiddingid = '"+cbiddingid+"'";
		return PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubConst.LOCAL_SUBMIT_PRICE);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）报价单审批通过插入报价明细数据
	 * 2011-5-25下午08:01:06
	 * @param bill
	 * @throws Exception
	 */
	public void PushSaveSubmitPriceByBill(SubmitPriceBillVO bill) throws Exception{
		if(bill == null)
			throw new BusinessException("数据为空");
		SubmitPriceBodyVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length ==0)
			throw new BusinessException("数据为空");
		SubmitPriceHeaderVO head = bill.getHeader();
		int isubtype = getSubType(head.getCbiddingid());
		
		String circalid = null;
		/**
		 * 暂时在报价单前台界面未支持轮次的参照录入  如有必要后续 的网上报价类型的 标书 可支持选择 不是网上报价的轮次  在此处录入
		 */
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && PuPubVO.getString_TrimZeroLenAsNull(circalid)==null){
			circalid = getFirstCircalnoForWeb(head.getCbiddingid());
		}
		
		SubmitPriceVO[] prices = new SubmitPriceVO[bodys.length];
		SubmitPriceVO tmp = null;
		int index = 0;
		for(SubmitPriceBodyVO body:bodys){
			tmp = new SubmitPriceVO();
			tmp.setPk_corp(head.getPk_corp());
			tmp.setCbiddingid(head.getCbiddingid());
			tmp.setCcircalnoid(circalid);
			tmp.setCoprator(head.getVapproveid());
			tmp.setTmaketime(new UFDateTime(System.currentTimeMillis()).toString());
			tmp.setStatus(VOStatus.NEW);
			prices[index] = tmp;
			index++;
		}
		
		SingleVOChangeDataBsTool.runChangeVOAry(bodys, prices, CHGSubBodyTOSubPrice.class.getName());
		
		for(SubmitPriceVO price:prices){
			price.setCvendorid(head.getCvendorid());
			price.setIsubmittype(ZbPubConst.SELF_SUBMIT_PRICE);
			price.validationOnSubmit(true, isubtype);
		}		
//		getDao().insertVOArray(prices);		
		submitPrice(prices, ZbPubConst.SELF_SUBMIT_PRICE, null);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）此处暂时这样处理  后续可能要考虑  是否可弃审的问题
	 * 2011-5-25下午08:16:59
	 * @throws BusinessException
	 */
	public void deletePrice(SubmitPriceBillVO bill) throws BusinessException{
		if(bill == null)
			return;
		SubmitPriceHeaderVO head = bill.getHeader();
		String cbiddingid = head.getCbiddingid();
		String sql = " select ibusstatus from zb_bidding_h where cbiddingid = '"+cbiddingid+"'";
		int ista = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR),-1);
		if(ista != ZbPubConst.BIDDING_BUSINESS_STATUE_INIT)
			throw new BusinessException("标书已进入投标阶段,不能弃审报价单");
		
		SubmitPriceBodyVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			return;
		String[] ids = new String[bodys.length];
		int index = 0;
		for(SubmitPriceBodyVO body:bodys){
			ids[index] = body.getPrimaryKey();
			index ++;
		}
		sql = "update zb_submitprice set dr = 1 where vdef1  in "+ZbPubTool.getSubSql(ids);
		getDao().executeUpdate(sql);
	}
	

	/**
	 * 校验本轮报价是否小于上次报价
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-6-19下午04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	public void checkMinPriceZB09(SubmitPriceBillVO billvo) throws BusinessException{
		if(billvo==null)
			return;
		SubmitPriceHeaderVO head = billvo.getHeader();
		SubmitPriceBodyVO[] bodys = billvo.getBodys();
		if(head == null)
			return;
		String cbiddingid = head.getCbiddingid();
		String vendorid = head.getCvendorid();
		String sql = " select e.cinvmanid,min(e.nprice) from zb_submitprice e where e.cbiddingid ='"+cbiddingid+"' and e.cvendorid ='"+vendorid+"' and isnull(e.dr,0)=0 group by e.cinvmanid";
		ArrayList al = (ArrayList)getDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if(al ==null || al.size()==0)
			return;
		int size = al.size();
		if(bodys==null || bodys.length==0)
			return;
		Map map = new HashMap();
		for(SubmitPriceBodyVO price:bodys){
			if(!map.containsKey(price.getCinvmanid()))
				map.put(price.getCinvmanid(),price.getNprice());
		}
		for(int i=0;i<size;i++){
			Object o = al.get(i);
			if(o==null)
				continue;
			Object[] os = (Object[])o;
			if(os ==null || os.length==0)
				continue;
			if(map.containsKey(os[0]))
				if(PuPubVO.getUFDouble_NullAsZero(map.get(os[0])).compareTo(PuPubVO.getUFDouble_NullAsZero(os[1]))>0)
					throw new BusinessException("本轮报价超出上轮报价");
		}
		
	}
}
