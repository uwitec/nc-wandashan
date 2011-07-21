package nc.bs.ia.hg.report;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.hg.ia.report.IaInvInOutReportVO;
import nc.vo.hg.ia.report.InOutSumPubVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;

public abstract class AbstractInOutSumQryBO {
	protected BaseDAO dao = null;
	public AbstractInOutSumQryBO(BaseDAO dao){
		super();
		this.dao = dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ѯ �շ���  ������ϸ����
	 * 2011-6-19����02:41:28
	 * @param date1 ��ʼ����
	 * @param date2 ��ֹ����
	 * @param cons �û����� vo
	 * @return
	 * @throws BusinessException
	 */
	public  void getDatas(IaInvInOutReportVO[] datas,String date1,String date2,ConditionVO[] cons) throws BusinessException{
		String sql = buildSql(date1, date2, dealWhereSql(cons));
		List ldata = (List)dao.executeQuery(sql, new BeanListProcessor(InOutSumPubVO.class));
		if(ldata==null || ldata.size() == 0)
			return;
		InOutSumPubVO[] nums = (InOutSumPubVO[])ldata.toArray(new InOutSumPubVO[0]);
//	    �����
		appData(datas, nums);
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����������������
	 * 2011-6-20����09:49:04
	 * @param datas
	 * @param nums
	 */
	public  void appData(IaInvInOutReportVO[] datas,InOutSumPubVO[] nums){
//		ά�ȣ���˾ ��֯  ���ֿ⣩ ��� �����Σ�      ����  ���
		if(datas == null || datas.length ==0)
			return;
		Map<String, InOutSumPubVO> numInfor = InOutSumPubVO.dealData(nums);
		if(numInfor == null || numInfor.size() == 0)
			return;
		StringBuffer keybuf = new StringBuffer();
		String key = null;
		for(IaInvInOutReportVO data:datas){
			keybuf = new StringBuffer();
			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getPk_corp()));
			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCrdcenterid()));
			//    		if(InOutSumPubVO.isware.booleanValue())
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCwarehouseid()));
			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinvclid()));
			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinvbasid()));
			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getCinventoryid()));
			//    		if(InOutSumPubVO.isqrybatch.booleanValue())
//			keybuf.append(HgPubTool.getString_NullAsTrimZeroLen(data.getVbatch()));
			key = keybuf.toString();
			appData2(data, numInfor.get(key));
		}
	}
    /**
     * 
     * @author zhf
     * @˵�������׸ڿ�ҵ�����岹��ר���������
     * 2011-6-20����09:49:20
     * @param data
     * @param num
     */
	protected abstract void appData2(IaInvInOutReportVO data,InOutSumPubVO num);
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ȡ��ϸ�������  sql
	 * 2011-6-20����09:49:51
	 * @param date1
	 * @param date2
	 * @param whereCondition
	 * @return
	 */
	protected abstract String buildSql(String date1,String date2,String whereCondition);
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������û������������
	 * 2011-6-20����09:50:31
	 * @param cons
	 * @return
	 */
	protected  String dealWhereSql(ConditionVO[] cons){
		return InOutSumPubVO.dealCons(cons);
	}
	protected String getOrderBySql(){
		if(InOutSumPubVO.isinvcl.booleanValue())
			return " order by crdcenterid,cinvclid";
		else
			return " order by crdcenterid,cinvbasid";
	}
}
