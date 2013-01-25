package nc.bs.wl.dm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 发运计划处理后台类
 * @author zhf
 */
public class PlanDealBO {

	private BaseDAO m_dao = null;
	private BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	private TempTableUtil ttbo = null;
	private TempTableUtil getTempTableUtil(){
		if(ttbo == null)
			ttbo = new TempTableUtil();
		return ttbo;
	}
	private HYPubBO superbo = null;
	private HYPubBO getSuperBO(){
		if(superbo == null)
			superbo = new HYPubBO();
		return superbo;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询到货站是当前登录人员绑定的仓库的 发运安排
	 * 如果当前登录人是总仓的 可以查询所有的 发运安排
	 * 
	 * wds_sendplanin发运计划主表
	 * wds_sendplanin_b 发运计划子表
	 * @时间：2011-3-25上午09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PlanDealVO[] doQuery(String whereSql) throws Exception{
		String pk_corp = SQLHelper.getCorpPk();
		PlanDealVO[] datas = null;
		//实现查询发运计划的逻辑 
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
		sql.append(" wds_sendplanin.pk_corp ,");
		sql.append(" wds_sendplanin.dmakedate ,");
		sql.append(" wds_sendplanin.voperatorid ,");
		sql.append(" wds_sendplanin.vapprovenote ,");
		sql.append(" wds_sendplanin.pk_billtype ,");
		sql.append(" wds_sendplanin.vbillstatus ,");
		sql.append(" wds_sendplanin.iplantype ,");
		sql.append(" wds_sendplanin. vemployeeid ,");
		sql.append(" wds_sendplanin.pk_busitype ,");
		sql.append(" wds_sendplanin.pk_sendplanin ,");
		sql.append(" wds_sendplanin.dbilldate ,");
		sql.append(" wds_sendplanin.dmakedate,");
		sql.append(" wds_sendplanin.vbillno vbillno,");
		sql.append(" wds_sendplanin.pk_inwhouse ,");
		sql.append(" wds_sendplanin.pk_deptdoc ,");
		sql.append(" wds_sendplanin.pk_outwhouse ,");
		sql.append(" wds_sendplanin.dapprovedate ,");
		sql.append(" wds_sendplanin.vapproveid ,");
		sql.append(" wds_sendplanin_b.pk_sendplanin_b,");
		sql.append(" wds_sendplanin_b.pk_invmandoc,");
		sql.append(" wds_sendplanin_b.pk_invbasdoc,");
		sql.append(" wds_sendplanin_b.unit,");
		sql.append(" wds_sendplanin_b.assunit,");
		sql.append(" wds_sendplanin_b.nplannum nplannum,");//计划数量
		sql.append(" wds_sendplanin_b.nassplannum nassplannum,");//计划辅数量
		sql.append(" wds_sendplanin_b.ndealnum,");//已安排数量
		sql.append(" wds_sendplanin_b.nassdealnum,");//已安排辅数量
//		sql.append(" coalesce(wds_sendplanin_b.nplannum,0)-coalesce(wds_sendplanin_b.ndealnum,0) nnum,");//本次安排数量
//		sql.append(" coalesce(wds_sendplanin_b.nassplannum,0)-coalesce(wds_sendplanin_b.nassdealnum,0) nassnum,");//本次安排辅数量
		sql.append(" wds_sendplanin_b.hsl hsl, ");
		sql.append(" wds_sendplanin_b.ts, ");
		sql.append(" wds_sendplanin_b.bisdate, ");  //是否大日期
		sql.append(" wds_sendplanin.reserve16, ");  //是否虚拟add by yf 2012-07-26 运单安排增加是否虚拟标志
		sql.append(" wds_sendplanin.reserve15 ");  //是否欠发
		sql.append(" from wds_sendplanin ");
		sql.append(" join wds_sendplanin_b ");
		sql.append(" on wds_sendplanin.pk_sendplanin = wds_sendplanin_b.pk_sendplanin ");
		sql.append(" where wds_sendplanin.pk_corp='"+pk_corp+"' ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null){
			sql.append(" and  "+whereSql);
		}
		Object o = getDao().executeQuery(sql.toString(), new BeanListProcessor(PlanDealVO.class));
		if( o != null){
			ArrayList<PlanDealVO> list = (ArrayList<PlanDealVO>)o;
			datas = list.toArray(new PlanDealVO[0]);
		}
		Arrays.sort(datas, new Comparator(){
			public int compare( Object o1, Object o2){
				 String code1 = ((PlanDealVO)o1).getVbillno();
				 if(code1 == null){
					 code1 = "";
				 }
				 String code2 = ((PlanDealVO)o2).getVbillno();
				 if(code2 == null){
					 code2 = "";
				 }
				 return code1.compareTo(code2);
			}
		});
//		PlanDealBOUtil util = new PlanDealBOUtil();
//		util.arrangStornumout(pk_corp,datas);
//		util.arrangStornumin(pk_corp,datas);
		return datas;
	}
	
	private void checkTs(Map<String,UFDateTime> tsInfor) throws Exception{
		if(tsInfor == null || tsInfor.size() ==0)
			return;
		String sql = "select pk_sendplanin_b,ts from wds_sendplanin_b where pk_sendplanin_b in "+getTempTableUtil().getSubSql(tsInfor.keySet().toArray(new String[0]));
		List ldata = (List)getDao().executeQuery(sql, new ArrayListProcessor());
		if(ldata == null || ldata.size() == 0)
			throw new  ValidationException("数据异常");
		Object[] os = null;
		int len = ldata.size();
		String key = null;
		String newts = null;
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(os[0]);
			newts = WdsWlPubTool.getString_NullAsTrimZeroLen(os[1]);
			if(!WdsWlPubTool.getString_NullAsTrimZeroLen(tsInfor.get(key)).equalsIgnoreCase(newts)){
				throw new ValidationException("发生并发操作,请刷新界面重新操作");
			}
		}
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-3-25下午03:58:14
	 * @param ldata
	 * @param infor :登录人，登录公司，登录日期
	 * @throws Exception
	 */
	public void doDeal(List<PlanDealVO> ldata, List<String> infor)
			throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;	
		//校验数据并发
		Map<String,UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
		for(PlanDealVO data:ldata){
			tsInfor.put(data.getPrimaryKey(), data.getTs());
		}	
		checkTs(tsInfor);
		// 按 计划号 发货站 收货站 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		PlanDealVO[] tmpVOs = null;
		//构造发运计划 聚合vo
		HYBillVO[] planBillVos = new HYBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (PlanDealVO[]) datas[i];
			planBillVos[i] = new HYBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// 进行数据交换  发运计划->发运定单
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		// 参量上 设置 日期 操作人
		HYBillVO[] orderVos = (HYBillVO[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.WDS1,
				WdsWlPubConst.WDS3, planBillVos, paraVo);
	  
		if(orderVos ==null || orderVos.length==0){
			return;
		}
		//保存发运订单
		PfUtilBO pfbo = new PfUtilBO();
		for(HYBillVO bill: orderVos){
			pfbo.processAction(WdsWlPubConst.DM_PLAN_TO_ORDER_SAVE, WdsWlPubConst.WDS3, infor.get(2), null, bill, null);
		}
	}
	private SendplaninVO getPlanHead(PlanDealVO dealVo){
		if(dealVo == null)
			return null;
		SendplaninVO head  = new SendplaninVO();
		String[] names  = head.getAttributeNames();
		for(String name:names){
			head.setAttributeValue(name, dealVo.getAttributeValue(name));
		}
		return head;
	}
	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 计划录入行关闭
	 * @时间：2011-6-25下午09:12:02
	 * @param lpara 第一个参数为表头id
	 * @return
	 * @throws Exception
	 */
	public  HYBillVO closeRows(List lpara) throws BusinessException{
		if(lpara == null || lpara.size() ==0 || lpara.size() == 1)
			return null;
		HYBillVO newbill = null;
		
		String billid = PuPubVO.getString_TrimZeroLenAsNull(lpara.get(0));
		lpara.remove(0);
		int len = lpara.size();
		
		String sql = "update wds_sendplanin_b set reserve14 = 'Y' where pk_sendplanin_b in "+getTempTableUtil().getSubSql((ArrayList)lpara);
		int size = getDao().executeUpdate(sql);
		if(size !=len)
			throw new BusinessException("操作失败");
		newbill = (HYBillVO)getSuperBO().queryBillVOByPrimaryKey(new String[]{HYBillVO.class.getName(),SendplaninVO.class.getName(),SendplaninBVO.class.getName()}, billid);
		
		return newbill;
	}
	
}
