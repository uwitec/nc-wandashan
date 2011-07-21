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
 * @author zhf  �����������vo
 *
 */
public class SplitNumColPara implements ISplitNumPara{
	private BaseDAO dao = null;
	private String cbiddingid = null;
	private UFDouble nallmny = null;
	private String splitrate = null;
	private Object[] grades = null;//  0��cvendorid   1:��Ӧ���ܷ���
	private Map<String,UFDouble> mnyMap = null;//�޶� key��cvendorid   value:��Ӧ�̷����޶�
	private Map<String,UFDouble> oldMnyMap = null;//��ʹ���޶� key��cvendorid   value:��Ӧ����ʹ�÷����޶�
	private HYBillVO bill = null;//���������������
	
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
		initGrades();//��ʼ��Ӧ���ܷ�
		initSplitRate();//��ʼ��Ӧ�̷�̯����
		initMny();//��ʼ��Ӧ�̷����޶�
		initOldMny();//��ʼ��Ӧ����ʹ���޶�
	}
	
	private void initOldMny(){
		if(oldMnyMap == null)
			oldMnyMap = new HashMap<String, UFDouble>();
		else
			oldMnyMap.clear();
	}
	
	public void clear(){
		 splitrate = null;
		 grades = null;//  0��cvendorid   1:��Ӧ���ܷ���
		 mnyMap = null;//�޶� key��cvendorid   value:��Ӧ�̷����޶�
		 oldMnyMap = null;//��ʹ���޶� key��cvendorid   value:��Ӧ����ʹ�÷����޶�
		 bill = null;//���������������
		 nallmny = UFDouble.ZERO_DBL;
	}
	
	public void refresh(String cbiddingid,HYBillVO bill) throws BusinessException{
		clear();
		this.cbiddingid = cbiddingid;
		this.bill = bill;
		init();
	}
	
	private void initMny() throws BusinessException{
//		���ݹ�Ӧ�̵÷����� ��С˳�� �� ��̯���� ��  �б��ܽ��   ���������Ӧ�̵��޶�
		UFDouble allmny = colAllMny();
		int[] rates = ZbPubTool.colBiddingVendorRates(splitrate);
		int len = grades.length;
		int len2 = rates.length;
		if(len2>len)
			throw new BusinessException("�������õ�[��Χ��Ӧ������]����ʵ����Χ�Ĺ�Ӧ������");
		if(len2<len)
			throw new BusinessException("ʵ����Χ��Ӧ�����������������õ�[��Χ��Ӧ������]");
		

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
			throw new BusinessException("����Ϊ��");
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("Ʒ����ϢΪ��");
		nallmny = UFDouble.ZERO_DBL;
		for(BiEvaluationBodyVO body:bodys){
			nallmny = nallmny.add(PuPubVO.getUFDouble_ValueAsValue(body.getNzbnum()).multiply(PuPubVO.getUFDouble_NullAsZero(body.getNzbprice()),8));
		}
		return nallmny;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ�������õķ�̯����
	 * 2011-5-24����01:36:19
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private void initSplitRate() throws BusinessException{
		String sql = "select vsplitrate from zb_bidding_h where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";
		String vsplitrate = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
		if(vsplitrate == null)
			throw new BusinessException("��ȡ�������õ���Χ��Ӧ�̷�̯����Ϊ��");
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
			throw new BusinessException("��ȡ��Ӧ���ܷ�Ϊ��,���Ϊ"+cbiddingid);
		
		int len = ldata.size();
		grades = new Object[len];
		Object[] os = null;
		for(int i=0;i<len ;i++){
			os = (Object[])ldata.get(i);
			if(os == null)
				throw new BusinessException("��ȡ��Ӧ���ܷ�Ϊ��,���Ϊ"+cbiddingid);
			grades[i] = os;
		}
		Arrays.sort(grades, new GradeComparator());
	}
	
	class GradeComparator implements Comparator{
		public int compare(Object o1, Object o2) {//�÷ָߵͽ�������
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
