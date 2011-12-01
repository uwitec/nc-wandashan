package nc.bs.wl.dm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.PlanDealVO;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 发运计划处理后台类
 * @author Administrator
 *
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
		sql.append(" coalesce(wds_sendplanin_b.nplannum,0)-coalesce(wds_sendplanin_b.ndealnum,0) nnum,");//本次安排数量
		sql.append(" coalesce(wds_sendplanin_b.nassplannum,0)-coalesce(wds_sendplanin_b.nassdealnum,0) nassnum,");//本次安排辅数量
		sql.append(" wds_sendplanin_b.hsl hsl, ");
		sql.append(" wds_sendplanin_b.ts, ");
		sql.append(" wds_sendplanin_b.bisdate ");  //是否大日期
		sql.append(" from wds_sendplanin ");
		sql.append(" join wds_sendplanin_b ");
		sql.append(" on wds_sendplanin.pk_sendplanin = wds_sendplanin_b.pk_sendplanin ");
		sql.append(" where "+whereSql);
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
		// 把库存量按照 运单 时间先后 分配
		CircularlyAccessibleValueObject[][] splitVos = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (datas),
				new String[]{"bisdate"});//根据发货仓库和是否大日期分单
		if(splitVos == null || splitVos.length==0){
			return datas;
		}
		PlanDealVO[] vos = null;
		for(int i=0;i<splitVos.length;i++){
			vos = (PlanDealVO[])splitVos[i];
			if(vos != null && vos.length>0){
				UFBoolean fisdate = PuPubVO.getUFBoolean_NullAs(vos[0].getBisdate(), UFBoolean.FALSE);
				PlanDealBOUtil util = new PlanDealBOUtil();
				if(fisdate.booleanValue()){
					util.arrangStornumout(true,vos);
					util.arrangStornumin(true,vos);
				}else{
					util.arrangStornumout(false,vos);
					util.arrangStornumin(false,vos);
				}
			}
		}
		return datas;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 将本次安排数量，回写到发运计划安排累计发运数量
	 * @时间：2011-3-25下午04:44:08
	 * @param dealnumInfor
	 * @throws BusinessException
	 */
	private void reWriteDealNumForPlan(Map<String,ArrayList<UFDouble>> map) throws BusinessException{
		if(map == null || map.size()==0)
			return;
		for(Entry<String, ArrayList<UFDouble>> entry:map.entrySet()){
			String sql = "update wds_sendplanin_b set ndealnum =coalesce(ndealnum,0)+"
				+entry.getValue().get(0)+" where pk_sendplanin_b='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql)==0){
				throw new BusinessException("数据异常：该发运计划可能已被删除，请重新查询数据");
			};
			String sql1 = "update wds_sendplanin_b set nassdealnum =coalesce(nassdealnum,0)+"
				+entry.getValue().get(1)+" where pk_sendplanin_b='"+entry.getKey()+"'";
			if(getDao().executeUpdate(sql1)==0){
				throw new BusinessException("数据异常：该发运计划可能已被删除，请重新查询数据");
			};
			//将计划数量（nplannum）和累计安排数量(ndealnum)比较
			//如果累计安排数量大于计划数量将抛出异常

			String sql2="select count(0) from wds_sendplanin_b where pk_sendplanin_b='"+entry.getKey()+ "'and (coalesce(nplannum,0)-coalesce(ndealnum,0))>=0";			
			Object o=getDao().executeQuery(sql2,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new BusinessException("累计安排数量不能大于计划数量！");
			}
		}
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
		Map<String,UFDateTime> tsInfor = new HashMap<String, UFDateTime>();
		for(PlanDealVO data:ldata){
			tsInfor.put(data.getPrimaryKey(), data.getTs());
		}	
		checkTs(tsInfor);
//		//1.将明细按照 是否大日期 分单，然后分别按照现存量来过滤
		PlanDealBOUtil util= new PlanDealBOUtil();
		CircularlyAccessibleValueObject[][] splitVos = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				new String[]{"pk_outwhouse","bisdate"});//根据发货仓库和是否大日期分单
		if(splitVos == null || splitVos.length==0){
			return ;
		}
		PlanDealVO[] vos = null;
		for(int i=0;i<splitVos.length;i++){
			vos = (PlanDealVO[])splitVos[i];
			if(vos != null && vos.length>0){
				String pk_outwhouse= PuPubVO.getString_TrimZeroLenAsNull(vos[0].getPk_outwhouse());
				if(pk_outwhouse  == null){
					throw  new BusinessException("发货仓库不能未空");
				}
				UFBoolean fisdate = PuPubVO.getUFBoolean_NullAs(vos[0].getBisdate(), UFBoolean.FALSE);
				if(fisdate.booleanValue()){
					util.initInvNumInfor(true,infor.get(1), pk_outwhouse,Arrays.asList(vos));
				}else{
					util.initInvNumInfor(false,infor.get(1), pk_outwhouse, Arrays.asList(vos));

				}
			}
		}
		// 发运安排vo---》发运计划vo
		Map<String,ArrayList<UFDouble>> map = new HashMap<String, ArrayList<UFDouble>>();
		for(int i=0;i<ldata.size();i++){
			String key = ldata.get(i).getPk_sendplanin_b();
			ArrayList<UFDouble> list = new ArrayList<UFDouble>();
			UFDouble num= PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNnum());
			UFDouble nassnum = PuPubVO.getUFDouble_NullAsZero(ldata.get(i).getNassnum());
			if(map.containsKey(key)){
				UFDouble oldValue =PuPubVO.getUFDouble_NullAsZero(map.get(key).get(0));
				UFDouble oldAssValue =PuPubVO.getUFDouble_NullAsZero(map.get(key).get(1));
				num = num.add(oldValue);
				nassnum = nassnum.add(oldAssValue);
				list.add(num);
				list.add(nassnum);
				map.put(key, list);
			}else{
				list.add(num);
				list.add(nassnum);
				map.put(key, list);
			}
		}
		reWriteDealNumForPlan(map);
		// 按 计划号 发货站 收货站 分单
		CircularlyAccessibleValueObject[][] datas = SplitBillVOs.getSplitVOs(
				(CircularlyAccessibleValueObject[]) (ldata
						.toArray(new PlanDealVO[0])),
				WdsWlPubConst.DM_PLAN_DEAL_SPLIT_FIELDS);
		if (datas == null || datas.length == 0)
			return;
		int len = datas.length;
		PlanDealVO[] tmpVOs = null;
		HYBillVO[] planBillVos = new HYBillVO[len];
		for (int i = 0; i < len; i++) {
			tmpVOs = (PlanDealVO[]) datas[i];
			planBillVos[i] = new HYBillVO();
			planBillVos[i].setParentVO(getPlanHead(tmpVOs[0]));
			planBillVos[i].setChildrenVO(tmpVOs);
		}
		// 发运计划vo---》发运订单vo
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator = infor.get(0);
		paraVo.m_coId = infor.get(1);
		paraVo.m_currentDate = infor.get(2);
		// 参量上 设置 日期 操作人
		HYBillVO[] orderVos = (HYBillVO[]) PfUtilTools.runChangeDataAry(
				WdsWlPubConst.WDS1,
				WdsWlPubConst.WDS3, planBillVos, paraVo);
		// 分单---》保存订单
		if(orderVos ==null || orderVos.length==0){
			return;
		}
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
