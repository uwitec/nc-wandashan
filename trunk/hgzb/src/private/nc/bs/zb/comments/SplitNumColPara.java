package nc.bs.zb.comments;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubTool;

/**
 * 
 * @author zhf  分量计算参量vo
 *
 */
public class SplitNumColPara implements ISplitNumPara{
	private BaseDAO dao = null;
	private String cbiddingid = null;
	private UFDouble nallmny = null;
	private String splitrate = null;
	private Object[] grades = null;//  0：cvendorid   1:供应商总分数
	private Map<String,UFDouble> mnyMap = null;//限额 key：cvendorid   value:供应商分量限额
	private Map<String,UFDouble> oldMnyMap = null;//已使用限额 key：cvendorid   value:供应商已使用分量限额
	private HYBillVO bill = null;//待分量计算的数据
	
	public SplitNumColPara(BaseDAO dao,String cbiddingid,HYBillVO bill) throws BusinessException{
		super();
		this.dao = dao;
		this.bill = bill;
		this.cbiddingid=cbiddingid;
		init();
	}
	
	public void setData(HYBillVO bill){
		this.bill = bill;
	}
	
	private void init() throws BusinessException{
		initGrades();//初始供应商总分
		initSplitRate();//初始供应商分摊比例
		initMny();//初始供应商分量限额
		initOldMny();//初始供应商已使用限额
	}
	
	private void initOldMny(){
		if(oldMnyMap == null)
			oldMnyMap = new HashMap<String, UFDouble>();
		else
			oldMnyMap.clear();
	}
	
	public void clear(){
		 splitrate = null;
		 grades = null;//  0：cvendorid   1:供应商总分数
		 mnyMap = null;//限额 key：cvendorid   value:供应商分量限额
		 oldMnyMap = null;//已使用限额 key：cvendorid   value:供应商已使用分量限额
		 bill = null;//待分量计算的数据
		 nallmny = UFDouble.ZERO_DBL;
	}
	
	public void refresh(String cbiddingid,HYBillVO bill) throws BusinessException{
		clear();
		this.cbiddingid = cbiddingid;
		this.bill = bill;
		init();
	}
	
	private void initMny() throws BusinessException{
//		根据供应商得分总数 大小顺序 和 分摊比例 和  招标总金额   计算各个供应商的限额
		UFDouble allmny = colAllMny();
		int[] rates = ZbPubTool.colBiddingVendorRates(splitrate);
		int len = grades.length;
		int len2 = rates.length;
		if(len2>len)
			throw new BusinessException("标书设置的[入围供应商数量]大于实际入围的供应商数量");
		if(len2<len)
			throw new BusinessException("实际入围供应商数量超过标书设置的[入围供应商数量]");
		

		Object[] os = null;
		
		if(mnyMap == null)
			mnyMap = new HashMap<String, UFDouble>();
		else
			mnyMap.clear();
		
		int iall = 0;
		for(int rate:rates){
			iall = iall + rate;
		}
		
		for(int i = 0;i < len;i++){
			os = (Object[])grades[i];
			mnyMap.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), allmny.multiply(rates[i]).div(iall));
		}		
	}
	
	
	
	
	
	private UFDouble colAllMny() throws BusinessException {
		if(!PuPubVO.getUFDouble_NullAsZero(nallmny).equals(UFDouble.ZERO_DBL))
			return nallmny;
		if(bill == null)
			throw new BusinessException("数据为空");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("品种信息为空");
		nallmny = UFDouble.ZERO_DBL;
		for(BiEvaluationBodyVO body:bodys){
			nallmny = nallmny.add(PuPubVO.getUFDouble_ValueAsValue(body.getNzbnum()).multiply(PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()),8));
		}
		return nallmny;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取标书设置的分摊比例
	 * 2011-5-24下午01:36:19
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private void initSplitRate() throws BusinessException{
		String sql = "select vsplitrate from zb_bidding_h where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";
		String vsplitrate = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
		if(vsplitrate == null)
			throw new BusinessException("获取标书设置的入围供应商分摊比例为空");
		this.splitrate = vsplitrate;
	}
	
	
	private void initGrades() throws BusinessException{
		String[] strs =null;	
		if(bill!=null){
			BiEvaluationBodyVO[] bvos = (BiEvaluationBodyVO[])bill.getChildrenVO();
			if(bvos!=null &&bvos.length>0){
				BidSlvendorVO[] bbs =bvos[0].getBidSlvendorVOs();
				if(bbs!=null && bbs.length>0){
					strs= new String[bbs.length];
					int index =0;
					for(BidSlvendorVO bb:bbs){
						if(PuPubVO.getString_TrimZeroLenAsNull(bb.getCcustmanid())==null)
							continue;
						strs[index]=bb.getCcustmanid();
						index++;
					}
				}
				
			}
		}
		String sql = "select ccustmanid,nquotatpoints,nqualipoints from zb_biddingsuppliers where isnull(dr,0) = 0 and " +
		" cbiddingid = '"+cbiddingid+"'and ccustmanid in"+ZbPubTool.getSubSql(strs);
		List ldata = (List)dao.executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("获取供应商总分为空,标段为"+cbiddingid);
		
		int len = ldata.size();
		grades = new Object[len];
		Object[] os = null;
		for(int i=0;i<len ;i++){
			os = (Object[])ldata.get(i);
			if(os == null)
				throw new BusinessException("获取供应商总分为空,标段为"+cbiddingid);
			grades[i] = os;
		}
		Arrays.sort(grades, new GradeComparator());
	}
	
	class GradeComparator implements Comparator{
		public int compare(Object o1, Object o2) {//得分高低降序排列
			Object[] os1 = (Object[])o1;
			Object[] os2 = (Object[])o2;
			UFDouble d1 = PuPubVO.getUFDouble_NullAsZero(os1[1]).add(PuPubVO.getUFDouble_NullAsZero(os1[2]));
			UFDouble d2 = PuPubVO.getUFDouble_NullAsZero(os2[1]).add(PuPubVO.getUFDouble_NullAsZero(os2[2]));
			return d2.compareTo(d1);
		}		
	}
	
	

	public BaseDAO getDao() {
		return dao;
	}

	public void setDao(BaseDAO dao) {
		this.dao = dao;
	}

	public String getCbiddingid() {
		return cbiddingid;
	}

	public UFDouble getNallmny() {
		return nallmny;
	}

	public String getSplitrate() {
		return splitrate;
	}

	public Object[] getGrades() {
		return grades;
	}

	public Map<String, UFDouble> getMnyMap() {
		return mnyMap;
	}

	public Map<String, UFDouble> getOldMnyMap() {
		return oldMnyMap;
	}

	public HYBillVO getBill() {
		return bill;
	}	
}
