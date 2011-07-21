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
	 * @说明：（鹤岗矿业）查询 收发存  汇总明细数据
	 * 2011-6-19下午02:41:28
	 * @param date1 开始日期
	 * @param date2 截止日期
	 * @param cons 用户条件 vo
	 * @return
	 * @throws BusinessException
	 */
	public  void getDatas(IaInvInOutReportVO[] datas,String date1,String date2,ConditionVO[] cons) throws BusinessException{
		String sql = buildSql(date1, date2, dealWhereSql(cons));
		List ldata = (List)dao.executeQuery(sql, new BeanListProcessor(InOutSumPubVO.class));
		if(ldata==null || ldata.size() == 0)
			return;
		InOutSumPubVO[] nums = (InOutSumPubVO[])ldata.toArray(new InOutSumPubVO[0]);
//	    填补数据
		appData(datas, nums);
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）补充数量金额数据
	 * 2011-6-20上午09:49:04
	 * @param datas
	 * @param nums
	 */
	public  void appData(IaInvInOutReportVO[] datas,InOutSumPubVO[] nums){
//		维度：公司 组织  （仓库） 存货 （批次）      数量  金额
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
     * @说明：（鹤岗矿业）具体补充专项数量金额
     * 2011-6-20上午09:49:20
     * @param data
     * @param num
     */
	protected abstract void appData2(IaInvInOutReportVO data,InOutSumPubVO num);
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）构造获取明细数量金额  sql
	 * 2011-6-20上午09:49:51
	 * @param date1
	 * @param date2
	 * @param whereCondition
	 * @return
	 */
	protected abstract String buildSql(String date1,String date2,String whereCondition);
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）处理用户定义过滤条件
	 * 2011-6-20上午09:50:31
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
