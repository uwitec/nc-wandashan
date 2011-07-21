package nc.bs.zb.comments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.comments.ISplitNumPara;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubTool;

/**
 * 
 * @author zhf  分量计算参量vo
 *
 */
public class SplitNumColPara2 implements ISplitNumPara{
	private BaseDAO dao = null;
	private String cbiddingid = null;
	private String splitrate = null;
	private Object[] grades = null;//  0：cvendorid   1:供应商总分数
	private HYBillVO bill = null;//待分量计算的数据
	
	public SplitNumColPara2(BaseDAO dao,String cbiddingid,HYBillVO bill) throws Exception{
		super();
		this.dao = dao;
		this.bill = bill;
		this.cbiddingid=cbiddingid;
		init();
	}
	
	public void setData(HYBillVO bill){
		this.bill = bill;
	}
	
	private void init() throws Exception{
		initGrades();//初始供应商总分
		initSplitRate();//初始供应商分摊比例
	}
	
	public void clear(){
		 splitrate = null;
		 grades = null;//  0：cvendorid   1:供应商总分数
		 bill = null;//待分量计算的数据
	}
	
	public void refresh(String cbiddingid,HYBillVO bill) throws Exception{
		clear();
		this.cbiddingid = cbiddingid;
		this.bill = bill;
		init();
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
		ParamSetVO vo = ZbBsPubTool.getParam();
		String vsplitrate =null;
		if(!PuPubVO.getUFBoolean_NullAs(vo.getFiscoltotal(),UFBoolean.FALSE).booleanValue()){
			String sql = "select vsplitrate from zb_bidding_h where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";
			vsplitrate = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
			if(vsplitrate == null)
				throw new BusinessException("获取标书设置的入围供应商分摊比例为空");
		}else{//获取参数 是否按总分分量  add by zhw  2011-06-21 
			if(grades==null ||grades.length==0)
				throw new BusinessException("");
			int len =grades.length;
			for(int i=0;i<len-1;i++){
				Object[] os= (Object[]) grades[i];
				UFDouble total = PuPubVO.getUFDouble_NullAsZero(os[1]).add(PuPubVO.getUFDouble_NullAsZero(os[2]));
				BigDecimal x = total.toBigDecimal().setScale(2,RoundingMode.HALF_UP);
				int ix=x.multiply(new BigDecimal(100)).intValue();
				if(vsplitrate==null){
					vsplitrate= PuPubVO.getString_TrimZeroLenAsNull(ix)+"/";
				}	
				else{
					vsplitrate= vsplitrate+PuPubVO.getString_TrimZeroLenAsNull(ix)+"/";
				}
			}
			Object[] osl= (Object[]) grades[len-1];
			UFDouble totall= PuPubVO.getUFDouble_NullAsZero(osl[1]).add(PuPubVO.getUFDouble_NullAsZero(osl[2]));
			BigDecimal xl = totall.toBigDecimal().setScale(2,RoundingMode.HALF_UP);
			int ixl=xl.multiply(new BigDecimal(100)).intValue();
			vsplitrate =vsplitrate+PuPubVO.getString_TrimZeroLenAsNull(ixl);
		}
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

	public String getSplitrate() {
		return splitrate;
	}

	public Object[] getGrades() {
		return grades;
	}

	public HYBillVO getBill() {
		return bill;
	}	
}
