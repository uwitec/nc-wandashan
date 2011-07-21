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
	 * @˵�������׸ڿ�ҵ��ȷ����ǰʱ�����ڵı����ִ�
	 * 2011-4-28����04:54:43
	 * @param time ��ǰʱ��
	 * @param times ���������ִ�
	 * @return ��ǰ�ִν׶�
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
	 * @˵�������׸ڿ�ҵ����ȡ��һ���ִ�  ��ǰʱ�䲻���κ��ִ���
	 * 2011-6-11����07:48:32
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
	 * @˵�������׸ڿ�ҵ�����ִ�ʱ�䰲�����ݽ��������� ���¹�ϵ
	 * 2011-4-28����04:51:45
	 * @param times �����ִ�����
	 */
	public static void dealBiddingTimes(BiddingTimesVO[] times){
		if(times == null || times.length == 0){
			return;
		}
		
//		������
		VOUtil.ascSort(times, new String[]{"tendtime"});
//		������ϵ
		int len = times.length;
		for(int i=0;i<len;i++){
			if(i>0)
				times[i].setCprecircalnoid(times[i-1].getPrimaryKey());
			if(i<len-1)
				times[i].setCnextcircalnoid(times[i+1].getPrimaryKey());
		}
	}
	
	public static void validationDataOnSubmit(SubmitPriceVO[] datas,boolean isinv,Integer isubtype) throws ValidationException{
//		У������    Ʒ�ַǿ�У��  �����̷ǿ�У��   ���۲���ȫ��Ϊ�ջ�0
		if(datas == null || datas.length == 0){
			throw new ValidationException("������");
//			return;
		}
			
		UFDouble nallprice = UFDouble.ZERO_DBL;
		for(SubmitPriceVO data:datas){
			data.validationOnSubmit(isinv,isubtype);
//			nallprice = nallprice.add(PuPubVO.getUFDouble_NullAsZero(data.getNprice()));
		}
//		if(nallprice.equals(UFDouble.ZERO_DBL)){
//			throw new ValidationException("���д�����۾�Ϊ0�������ύ���۵�");
//		}
	}
	
	/**
	 * @zhf
	 * @param datas
	 * @throws Exception
	 */
	public static void submitPrice(ToftPanel tp,SubmitPriceVO[] datas,boolean isinv,Integer isubtype,Object userObj) throws Exception{

		validationDataOnSubmit(datas, isinv, isubtype);
		//ת��̨����
		Class[] ParameterTypes = new Class[]{SubmitPriceVO[].class,Integer.class,Object.class};
		Object[] ParameterValues = new Object[]{datas,isubtype,userObj};
		LongTimeTask.calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "submitPrice", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��������ⱨ��
	 * 2011-5-21����12:55:21
	 * @param datas ���ⱨ����ϸ����
	 * @throws Exception
	 */
	public static void dealBadPrice(SubmitPriceVO[] datas) throws Exception{
		//ת��̨����
		Class[] ParameterTypes = new Class[]{SubmitPriceVO[].class};
		Object[] ParameterValues = new Object[]{datas};
		LongTimeTask.callRemoteService("pu", bo, "dealBadSubmit", ParameterTypes, ParameterValues, 1);
	}
	
	public static void cancelSubmitPrice(ToftPanel tp,String cbiddingid,String cvendorid,String ccircalnoid,Integer isubtype) throws Exception{
//		У������    Ʒ�ַǿ�У��  �����̷ǿ�У��   ���۲���ȫ��Ϊ�ջ�0
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null){
			tp.showHintMessage("������");
			return;
		}
			
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE &&  PuPubVO.getString_TrimZeroLenAsNull(ccircalnoid)==null){
			tp.showErrorMessage("��ǰ�����ִ�Ϊ��");
			return;
		}
		//ת��̨����
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,Integer.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cvendorid,ccircalnoid,isubtype};
		LongTimeTask.calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "cancelSubmitPrice", ParameterTypes, ParameterValues);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ִ�id �ӻ����л�ȡ�ִ�vo
	 * 2011-4-29����12:41:07
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
	 * @˵�������׸ڿ�ҵ�������Ƿ����
	 * 2011-4-29����12:30:24
	 * @param time ��ǰ�ִ�
	 * @return ��һ���ִ�id
	 */
	
	public static String isEnd(BiddingTimesVO time){
		return PuPubVO.getString_TrimZeroLenAsNull(time.getCnextcircalnoid());
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ���۵�������  �� ���ֱ���   ������ͱ���
	 * 2011-4-28����03:43:23
	 * @param cbiddingid
	 * @param ccirclenoid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public static SubmitPriceVO[] getPriceVos(ToftPanel tp,String cbiddingid,String ccirclenoid,String cvendorid,boolean isinv,Integer isubtype) throws Exception{
//		return null;

////		У������    Ʒ�ַǿ�У��  �����̷ǿ�У��   ���۲���ȫ��Ϊ�ջ�0
//		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null)
//			return null;
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			throw new BusinessException("�����ϢΪ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null)
			throw new BusinessException("��ǰ��Ӧ����Ϣ������");
		//ת��̨����
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,UFBoolean.class,Integer.class};
		Object[] ParameterValues = new Object[]{cbiddingid,ccirclenoid,cvendorid,isinv?UFBoolean.TRUE:UFBoolean.FALSE,isubtype};
		return (SubmitPriceVO[])LongTimeTask.calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "getPriceVos", ParameterTypes, ParameterValues);
	
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
