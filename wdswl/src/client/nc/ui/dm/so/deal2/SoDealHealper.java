package nc.ui.dm.so.deal2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.VOTool;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 发运安排调用后台累
 * @author Administrator
 *
 */
public class SoDealHealper {
	
	private static String bo = "nc.bs.dm.so.deal2.SoDealBO";
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 查询到货站是当前登录人员绑定的仓库的 发运安排
	 * 如果当前登录人是总仓的 可以查询所有的 发运安排
	 * @时间：2011-3-25上午09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */

	public static SoDealVO[] doQuery(String wheresql) throws Exception{
		SoDealVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { wheresql };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (SoDealVO[])o;
		}
		return dealVos;
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  安排按钮处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public static Object doDeal(SoDealBillVO[] billvos, ToftPanel tp) throws Exception {
		if (billvos == null || billvos.length == 0)
			return null;

		SoDealClientUI ui = (SoDealClientUI)tp;
		List<String> infor = new ArrayList<String>();
		infor.add(ui.cl.getUser());
		infor.add(ui.cl.getCorp());
		infor.add(ui.cl.getLogonDate().toString());
		Class[] ParameterTypes = new Class[] { SoDealBillVO[].class,List.class };
		Object[] ParameterValues = new Object[] { billvos,infor};
		return LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doDeal", ParameterTypes,
				ParameterValues);


	}	
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * 发运计划  手工安排 处理方法
	 * @时间：2011-3-25下午02:59:20
	 */
	public static void doHandDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;

//		数据校验       存在安排为0的品种           客户安排总量是否低于客户最小发货量           过滤安排量低于最低发货量得客户
		
		SoDealClientUI ui = (SoDealClientUI)tp;
		List<String> infor = new ArrayList<String>();
		infor.add(ui.cl.getUser());
		infor.add(ui.cl.getCorp());
		infor.add(ui.cl.getLogonDate().toString());
		Class[] ParameterTypes = new Class[] { List.class,List.class };
		Object[] ParameterValues = new Object[] { ldata,infor};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"正在处理...", 2, bo, null, "doHandDeal", ParameterTypes,
				ParameterValues);
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 合并订单数据
	 * @时间：2011-7-7下午01:41:54
	 * @param datas
	 * @return
	 */
	public static SoDealBillVO[] combinDatas(String cwhid,SoDealVO[] datas){
		if(datas == null || datas.length == 0)
			return null;
		CircularlyAccessibleValueObject[][]  voss = SplitBillVOs.getSplitVOs(datas, SoDealHeaderVo.split_fields);
		if(voss == null || voss.length ==0)
			return null;
		int len = voss.length;
		SoDealBillVO[] billvos = new SoDealBillVO[len];
		SoDealBillVO tmpbill = null;
		SoDealHeaderVo tmpHead = null;
		SoDealVO[] vos = null;
		for(int i=0;i<len;i++){
			vos = (SoDealVO[])voss[i];
			tmpHead = new SoDealHeaderVo();
			tmpHead.setCcustomerid(vos[0].getCcustomerid());
			tmpHead.setDbilldate((UFDate)VOTool.min(vos, "dbilldate"));//应取 最小订单日期
			tmpHead.setBisspecial(UFBoolean.FALSE);
			tmpHead.setCsalecorpid(vos[0].getCsalecorpid());
			tmpHead.setCbodywarehouseid(cwhid==null?vos[0].getCbodywarehouseid():cwhid);
			tmpHead.setStatus(VOStatus.NEW);
			tmpbill = new SoDealBillVO();
			tmpbill.setParentVO(tmpHead);
			tmpbill.setChildrenVO(vos);
			billvos[i] = tmpbill;
		}
		return billvos;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 对客户按订单日期自动 分配  发运量
	 * @时间：2011-7-11上午11:27:47
	 * @param lcust
	 * @param lnum
	 */
	public static void autoDealNum(List<SoDealBillVO> lcust,List<StoreInvNumVO> lnum){
		if(lcust == null || lcust.size() == 0 || lnum == null || lnum.size() == 0){
			return;
		}

//		自动分配
		Set<SoDealVO> bodys = null;
		UFDouble nallnum = null;//辅计量进行处理
		UFDouble nnum = null;
		UFDouble hsl = null;
		for(StoreInvNumVO num:lnum){
			bodys = num.getLdeal();
			nallnum = num.getNassnum();
			hsl = num.getNnum().div(num.getNassnum());
			
			if(bodys == null || bodys.size() == 0){
//				throw new BusinessException("数据异常");
				return;
			}
			for(SoDealVO body:bodys){
				nnum = body.getNassnum();
				if(nallnum.doubleValue()>nnum.doubleValue()){
//					数量不发生变化按照  安排数量  进行安排
					nallnum = nallnum.sub(nnum);
				}else if(nallnum.doubleValue()>0){
					body.setNassnum(nallnum);
					body.setNnum(nallnum.multiply(hsl));
					nallnum = WdsWlPubTool.DOUBLE_ZERO;					
//					continue;
				}else{
					body.setNassnum(WdsWlPubTool.DOUBLE_ZERO);
					body.setNnum(WdsWlPubTool.DOUBLE_ZERO);
				}
				
//				同步数据
				synData(lcust, body);
			}
			
		}
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 销售订单手工安排时 调整安排后的值
	 * @时间：2011-7-11下午02:15:14
	 * @param lcust
	 * @param deal
	 */
	public static void synData(List<SoDealBillVO> lcust,SoDealVO deal){
		if(lcust == null || lcust.size() == 0)
			return;
		if(deal == null){
			return;
		}
		String key = null;
		SoDealVO[] bodys = null;
		for(SoDealBillVO cust:lcust){
			if(!cust.getHeader().getCcustomerid().equalsIgnoreCase(deal.getCcustomerid()))
				continue;
			bodys = cust.getBodyVos();
			if(bodys == null || bodys.length ==0)
				return;
			for(SoDealVO body:bodys){
				key = body.getCorder_bid();
				if(deal.getCorder_bid().equalsIgnoreCase(key)){
					body.setNnum(deal.getNnum());
					body.setNassnum(deal.getNassnum());
					return;
				}
			}
		}
	}
}
