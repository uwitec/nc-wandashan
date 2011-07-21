package nc.ui.zb.price.pub;

import nc.ui.pub.ToftPanel;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

public class SubmitPriceHelper {
	
	private static String bo = "nc.bs.zb.price.SubmitPriceBO";
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）确定当前时间所在的标书轮次
	 * 2011-4-28下午04:54:43
	 * @param time 当前时间
	 * @param times 标书所有轮次
	 * @return 当前轮次阶段
	 */
	public static BiddingTimesVO getCurrentTime(UFDateTime time,BiddingTimesVO[] circals){
		if(circals == null || circals.length ==0)
			return null;
		for(BiddingTimesVO circal:circals){
			if(time.compareTo(circal.getTbigintime())>0 && time.compareTo(circal.getTendtime())<0)
				return circal;
		}
		return null;
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取下一个轮次  当前时间不在任何轮次内
	 * 2011-6-11下午07:48:32
	 * @param time
	 * @param circals
	 * @return
	 */
	public static BiddingTimesVO getNextTime(UFDateTime time,BiddingTimesVO[] circals){
		if(circals == null || circals.length ==0)
			return null;
		for(BiddingTimesVO circal:circals){
			if(time.compareTo(circal.getTbigintime())<0)
				return circal;
		}
		return null;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）对轮次时间安排数据进行整理建立 上下关系
	 * 2011-4-28下午04:51:45
	 * @param times 标书轮次数据
	 */
	public static void dealBiddingTimes(BiddingTimesVO[] times){
		if(times == null || times.length == 0){
			return;
		}
		
//		先排序
		VOUtil.ascSort(times, new String[]{"tendtime"});
//		建立关系
		int len = times.length;
		for(int i=0;i<len;i++){
			if(i>0)
				times[i].setCprecircalnoid(times[i-1].getPrimaryKey());
			if(i<len-1)
				times[i].setCnextcircalnoid(times[i+1].getPrimaryKey());
		}
	}
	
	public static void validationDataOnSubmit(SubmitPriceVO[] datas,boolean isinv,Integer isubtype) throws ValidationException{
//		校验数据    品种非空校验  供货商非空校验   报价不能全部为空或0
		if(datas == null || datas.length == 0){
			throw new ValidationException("无数据");
//			return;
		}
			
		UFDouble nallprice = UFDouble.ZERO_DBL;
		for(SubmitPriceVO data:datas){
			data.validationOnSubmit(isinv,isubtype);
//			nallprice = nallprice.add(PuPubVO.getUFDouble_NullAsZero(data.getNprice()));
		}
//		if(nallprice.equals(UFDouble.ZERO_DBL)){
//			throw new ValidationException("所有存货报价均为0，不能提交报价单");
//		}
	}
	
	/**
	 * @zhf
	 * @param datas
	 * @throws Exception
	 */
	public static void submitPrice(ToftPanel tp,SubmitPriceVO[] datas,boolean isinv,Integer isubtype,Object userObj) throws Exception{

		validationDataOnSubmit(datas, isinv, isubtype);
		//转后台处理
		Class[] ParameterTypes = new Class[]{SubmitPriceVO[].class,Integer.class,Object.class};
		Object[] ParameterValues = new Object[]{datas,isubtype,userObj};
		LongTimeTask.calllongTimeService("pu", tp, "正在处理...", 1, bo, null, "submitPrice", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）处理恶意报价
	 * 2011-5-21下午12:55:21
	 * @param datas 恶意报价明细数据
	 * @throws Exception
	 */
	public static void dealBadPrice(SubmitPriceVO[] datas) throws Exception{
		//转后台处理
		Class[] ParameterTypes = new Class[]{SubmitPriceVO[].class};
		Object[] ParameterValues = new Object[]{datas};
		LongTimeTask.callRemoteService("pu", bo, "dealBadSubmit", ParameterTypes, ParameterValues, 1);
	}
	
	public static void cancelSubmitPrice(ToftPanel tp,String cbiddingid,String cvendorid,String ccircalnoid,Integer isubtype) throws Exception{
//		校验数据    品种非空校验  供货商非空校验   报价不能全部为空或0
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null){
			tp.showHintMessage("无数据");
			return;
		}
			
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE &&  PuPubVO.getString_TrimZeroLenAsNull(ccircalnoid)==null){
			tp.showErrorMessage("当前报价轮次为空");
			return;
		}
		//转后台处理
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Integer.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cvendorid,ccircalnoid,isubtype};
		LongTimeTask.calllongTimeService("pu", tp, "正在处理...", 1, bo, null, "cancelSubmitPrice", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）根据轮次id 从缓存中获取轮次vo
	 * 2011-4-29下午12:41:07
	 * @param ccircalid
	 * @param times
	 * @return
	 */
	public static BiddingTimesVO getBiddingTimeByID(String ccircalid,BiddingTimesVO[] times){
		if(PuPubVO.getString_TrimZeroLenAsNull(ccircalid)==null)
			return null;
		for(BiddingTimesVO time:times){
			if(time.getCbiddingtimesid().equalsIgnoreCase(ccircalid))
				return time;
		}
		return null;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）报价是否结束
	 * 2011-4-29下午12:30:24
	 * @param time 当前轮次
	 * @return 下一个轮次id
	 */
	
	public static String isEnd(BiddingTimesVO time){
		return PuPubVO.getString_TrimZeroLenAsNull(time.getCnextcircalnoid());
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取报价单体数据  含 上轮报价   上轮最低报价
	 * 2011-4-28下午03:43:23
	 * @param cbiddingid
	 * @param ccirclenoid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public static SubmitPriceVO[] getPriceVos(ToftPanel tp,String cbiddingid,String ccirclenoid,String cvendorid,boolean isinv,Integer isubtype) throws Exception{
//		return null;

////		校验数据    品种非空校验  供货商非空校验   报价不能全部为空或0
//		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null)
//			return null;
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			throw new BusinessException("标段信息为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null)
			throw new BusinessException("当前供应商信息不存在");
		//转后台处理
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,UFBoolean.class,Integer.class};
		Object[] ParameterValues = new Object[]{cbiddingid,ccirclenoid,cvendorid,isinv?UFBoolean.TRUE:UFBoolean.FALSE,isubtype};
		return (SubmitPriceVO[])LongTimeTask.calllongTimeService("pu", tp, "正在处理...", 1, bo, null, "getPriceVos", ParameterTypes, ParameterValues);
	
	}
	
	public static BiddingTimesVO getFirstTime(BiddingTimesVO[] times){
		for(BiddingTimesVO time:times){
			if(PuPubVO.getString_TrimZeroLenAsNull(time.getCprecircalnoid())==null){
				return time;
			}
		}
		return null;
	}
	public static BiddingTimesVO getLastTime(BiddingTimesVO[] times){
		for(BiddingTimesVO time:times){
			if(PuPubVO.getString_TrimZeroLenAsNull(time.getCnextcircalnoid())==null){
				return time;
			}
		}
		return null;
	}
}
