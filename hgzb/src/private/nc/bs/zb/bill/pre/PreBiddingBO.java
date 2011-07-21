package nc.bs.zb.bill.pre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.SmallBiddingHeaderVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

/**
 * �������׼���Ի���
 * @author zhf
 * 
 *
 */

public class PreBiddingBO{
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
	 * @˵�������׸ڿ�ҵ�� ���۷�У��  ���۷ֱ����Ѽ���  ��������  ������
	 * 2011-5-26����06:51:05
	 * @param heads
	 * @throws BusinessException
	 */
	private void checkVendorGrade(SmallBiddingHeaderVO[] heads,String[] ids) throws BusinessException{

		if(heads ==  null ||  heads.length == 0)
			throw new BusinessException("����Ϊ��");

//		String[] ids = new String[heads.length];
//		int index = 0;
//		for(SmallBiddingHeaderVO head:heads){
//			ids[index] = head.getPrimaryKey();
//			index ++;
//		}
//		null;
//		String sql = " select cbiddingid,cvendorid,sum(ngrade+nadjgrade) grade from zb_pricegrade where isnull(dr,0) = 0 and cbiddingid in "+ZbPubTool.getSubSql(ids)+" group by cbiddingid,cvendorid";
//		��ȡƷ�ֵ÷���ϸ
		
//		List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
//		Map<String,UFDouble> invGradeInfor = new HashMap<String, UFDouble>();
//		Object[] os = null;
//		String key = null;
//		for(int i = 0;i<ldata.size();i++){
//			os = (Object[])ldata.get(i);
//			key = ZbPubTool.getString_NullAsTrimZeroLen(os[0])+ZbPubTool.getString_NullAsTrimZeroLen(os[1]);
//			invGradeInfor.put(key, PuPubVO.getUFDouble_NullAsZero(os[2]));
//		}
//		ldata.clear();
		
//		��ñ��Ʒ������
		String sql2 = "select count(0) from zb_bidding_b where isnull(dr,0)=0 and cbiddingid = ?";
		
		String sql =  " select ccustmanid,nqualipoints,nquotatpoints from zb_biddingsuppliers where cbiddingid = ?  and  coalesce(fisclose,'N') = 'N' and isnull(dr,0)=0 ";
		SQLParameter para = new SQLParameter();
		Map vendorGrade = null;
//		PriceGradeVO[] grades = null;
	
//		UFDouble tmpDouble = null;
		UFDouble ngrade = null;
		List ldata = null;
		for(SmallBiddingHeaderVO head:heads){
			//			����У��
			para.clearParams();
			para.addParam(head.getCbiddingid());
			ldata = (List)getDao().executeQuery(sql,para, ResultSetProcessorTool.MAPLISTPROCESSOR);
			if(ldata == null || ldata.size() == 0){
				throw new BusinessException("���["+head.getCname()+"]δ���ڹ�Ӧ����Ϣ");
			}
			
			int num = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql2,para, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
			if(num == 0)
				throw new BusinessException("���["+head.getCname()+"]δ�����б�Ʒ����Ϣ");
			
//			UFDouble nnum = new UFDouble(num);
			
			int len = ldata.size();
			for(int i=0;i<len;i++){
				vendorGrade = (Map)ldata.get(i);
//				key = head.getCbiddingid()+ZbPubTool.getString_NullAsTrimZeroLen(vendorGrade.get("ccustmanid"));
//				tmpDouble = PuPubVO.getUFDouble_NullAsZero(invGradeInfor.get(key)).div(nnum, ZbPubConst.grade_digit);
				ngrade = PuPubVO.getUFDouble_NullAsZero(vendorGrade.get("nqualipoints"));//���ʷ�
				if(ngrade.equals(UFDouble.ZERO_DBL))
					throw new BusinessException("���["+head.getCname()+"]�������ʷ�Ϊ�ջ�0�Ĺ�Ӧ��");
				ngrade = PuPubVO.getUFDouble_NullAsZero(vendorGrade.get("nquotatpoints"));//���۷�
//				ngrade = ngrade.add(UFDouble.ZERO_DBL,ZbPubConst.grade_digit);
				if(ngrade.equals(UFDouble.ZERO_DBL))
					throw new BusinessException("���["+head.getCname()+"]���ڱ��۷�Ϊ�ջ�0�Ĺ�Ӧ��");
//				if(!ngrade.equals(tmpDouble)){
//					throw new BusinessException("���["+head.getCname()+"]���ڹ�Ӧ�̱��۷��ܺʹ���Ĺ�Ӧ��,��Ӧ��Ϊ"+ZbPubTool.getString_NullAsTrimZeroLen(vendorGrade.get("ccustmanid")));
//				}
			}				
		}
	}
	
	private void checkTimes(SmallBiddingHeaderVO[] heads)
			throws BusinessException {
		if(heads ==  null ||  heads.length == 0)
			throw new BusinessException("����Ϊ��");
		UFDateTime date = new UFDateTime(System.currentTimeMillis());
		String sql1 = "select count(0) from zb_biddingtimes where isnull(dr,0)=0  and cbiddingid = ?"
				+ " and tendtime > '" + date.toString() + "'";
		String sql2 = "select count(0) from zb_submitprice where isnull(dr,0)=0 and cbiddingid = ?";

		SQLParameter para = new SQLParameter();
		String sql = null;
		for (SmallBiddingHeaderVO head : heads) {
			// ����У��
			para.clearParams();
			para.addParam(head.getPrimaryKey());
			int num = -1;
			if (head.getIzbtype() == ZbPubConst.WEB_SUBMIT_PRICE) {
				sql = sql1;
			} else
				sql = sql2;
			num = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, para,
					ResultSetProcessorTool.COLUMNPROCESSOR),
					ZbPubTool.INTEGER_ZERO_VALUE);
			//			if (num <= 0) {
			if (head.getIzbtype() == ZbPubConst.WEB_SUBMIT_PRICE){
				if(num<0)
					throw new BusinessException("���[" + head.getCname()
							+ "]�����ִ�δִ����");
			}else{
				if(num<=0)
					throw new BusinessException("���[" + head.getCname()
							+ "]δ���ڱ�����Ϣ");
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У���β��뱨�۵Ĺ�Ӧ������������ڱ�����õ���Χ��Ӧ������
	 * 2011-6-5����02:57:57
	 * @param ids
	 * @throws BusinessException
	 */
	private void checkSubVendor(SmallBiddingHeaderVO[] heads,String[] ids) throws BusinessException{
		if(ids == null || ids.length == 0)
			return;
//		����У��
		String sql = "select cbiddingid,nvendornum from zb_bidding_h where isnull(dr,0) = 0 and cbiddingid in "+ZbPubTool.getSubSql(ids);
		List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
		Object[] os = null;
		if(ldata == null || ldata.size() ==0)
			throw new BusinessException("�����쳣");
		int len = ldata.size();
		Map<String, Integer> numInfor = new HashMap<String, Integer>(); 
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			numInfor.put(PuPubVO.getString_TrimZeroLenAsNull(os[0]), PuPubVO.getInteger_NullAs(os[1],-1));
		}
		
		sql = "select distinct g.cvendorid from zb_pricegrade g inner join zb_biddingsuppliers s on g.cvendorid = s.ccustmanid where isnull(g.dr,0)=0  and isnull(s.dr,0)=0 " +
				" and g.cbiddingid = ? and coalesce(s.fisclose,'N') = 'N'";
		int index = 0;
		SQLParameter para = new SQLParameter();
		for(String id:ids){
			para.clearParams();
			para.addParam(id);
			ldata = (List)getDao().executeQuery(sql, para,ResultSetProcessorTool.COLUMNLISTPROCESSOR);
			int size = ldata==null?0:ldata.size();
			if(!numInfor.containsKey(id))
				throw new BusinessException("�����쳣");
			if(size >= numInfor.get(id)){
				index ++;
				continue;
			}
//				continue;
			throw new BusinessException("��Ρ�"+heads[index].getCname()+"�����뱨�۵Ĺ�Ӧ������("+size+")���ڱ�����õġ���Χ��Ӧ��������("+numInfor.get(id)+")");
		}	
	}
	
	public void dealBidding(SuperVO[] vos) throws BusinessException{
		if(vos == null || vos.length == 0)
			return;
		
		SmallBiddingHeaderVO[] heads = new SmallBiddingHeaderVO[vos.length];
		String[] ids = new String[vos.length];
		int index = 0;
		for(SuperVO vo:vos){
			heads[index] = (SmallBiddingHeaderVO)vo;
			ids[index] = heads[index].getCbiddingid();
			index ++;
		}
//		У�������Ƿ��ѱ������   �����б꣺��������˸���׶�  �ֳ��б꣺�����������һ����������
		checkTimes(heads);
		checkSubVendor(heads,ids);
		checkVendorGrade(heads,ids);

		String sql = "update zb_bidding_h set ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_BILL+" where cbiddingid in "+new TempTableUtil().getSubSql(ids);
		getDao().executeUpdate(sql);
	}
}
