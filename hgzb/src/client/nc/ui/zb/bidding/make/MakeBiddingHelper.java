package nc.ui.zb.bidding.make;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.HistoryPriceVO;
import nc.vo.zb.bidding.PuPlanInvVO;
import nc.vo.zb.pub.ZbPubTool;

public class MakeBiddingHelper {

	private static String bo = "nc.bs.zb.bidding.make.MakeBiddingBO";
	
	private static QueryConditionClient m_planQryDlg = null;
	private static QueryConditionClient getPlanQryDlg(String pkCorp, 
			String pkOperator, Container parent){
		
		/**
		 * 查询采购计划
		 * 计划单号    需求单位   单据日期   存货分类   存货编码
		 */
		
		if(m_planQryDlg == null){
			m_planQryDlg = new QueryConditionClient(parent);
			m_planQryDlg.setTempletID("0001A110000000011DOP");
//			m_planQryDlg.hideDefine();
			m_planQryDlg.hideNormal();
			m_planQryDlg.hideUnitButton();
		}
		return m_planQryDlg;	
	}
	
	private static MakeBiddingRefDlg m_refDataDlg =  null;
	private static MakeBiddingRefDlg getDataDlg(ToftPanel parent,ClientLink cl){
		if(m_refDataDlg == null){
			m_refDataDlg = new MakeBiddingRefDlg(parent,cl);
		}
		return m_refDataDlg;
	}
	
	
	public static void clear(){
		m_planQryDlg = null;
		m_refDataDlg = null;
	}
	
	public static PuPlanInvVO[] doQuery(Container parent,ClientLink cl) throws Exception{
		QueryConditionClient query = getPlanQryDlg(cl.getCorp(), cl.getUser(), parent);
			
		
		if(query.showModal() != UIDialog.ID_OK)
			return null;
		
		ConditionVO[] cons =query.getConditionVO();
		String str = null;
		List<ConditionVO> lcon = new ArrayList<ConditionVO>();
		for(ConditionVO con:cons){
			//modify by zhw 2011-01-24  根据存货分类查询  按大类查询
			if(con.getFieldCode().equalsIgnoreCase("inv.pk_invcl")){
				String pk_invcl = con.getValue();
				String invcode = PuPubVO.getString_TrimZeroLenAsNull(ZbPubTool.getInvclasscode(pk_invcl));
				str=" and inv.pk_invcl in(select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')";
				con.setFieldCode("1");
				con.setValue("1");
			}
			lcon.add(con);
		}
		cons = lcon.toArray(new ConditionVO[0]);			

		String strWhere = query.getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		if(PuPubVO.getString_TrimZeroLenAsNull(str) != null)
			   strWhere = strWhere + str;
		Class[] ParameterTypes = new Class[]{String.class};
		Object[] ParameterValues = new Object[]{strWhere};
		Object o = LongTimeTask.
		calllongTimeService("pu", parent, "查询采购计划...", 1, bo, null, "loadPlanInvInfor", ParameterTypes, ParameterValues);
		
		PuPlanInvVO[] vos = (PuPlanInvVO[])o;
		return vos;
	}
	
	public static BiddingBillVO[] refAddButtonClicked(ToftPanel parent,ClientLink cl) throws Exception{
//		弹出查询框   查询数据  数据界面    数据转换  保存 返回前台
		PuPlanInvVO[] vos = doQuery(parent,cl);
		if(getPlanQryDlg(cl.getCorp(), cl.getUser(), parent).getResult()!=UIDialog.ID_OK){
//			parent.showHintMessage("未查询到数据");
			return null;
		}
		
		MakeBiddingRefDlg dlg = getDataDlg(parent,cl);
		dlg.setInitDatas(vos);
		if(dlg.showModal()!=UIDialog.ID_OK)
			return null;
		vos = dlg.getRefOutDatas();
		Class[]ParameterTypes = new Class[]{PuPlanInvVO[].class,ClientLink.class};
		Object[]ParameterValues = new Object[]{vos,cl};
		Object o = LongTimeTask.
		calllongTimeService("pu", parent, "正在划分标段...", 1, bo, null, "divideBidding", ParameterTypes, ParameterValues);
		
		return (BiddingBillVO[])o;
	}
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）获取存货 指定时间段内的供货历史情况
	 * 2011-6-4下午03:54:31
	 * @param tp
	 * @param cinvid
	 * @param sstartdate
	 * @param senddate
	 * @param sLogcorp
	 * @return
	 * @throws Exception
	 */
	public static HistoryPriceVO[] getHistoryPriceInfor(ToftPanel tp,String cinvid,String sstartdate,String senddate,String sLogcorp) throws Exception{
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvid)==null)
			return null;
		Class[] ParameterTypes = new Class[]{String[].class,String.class,String.class,String.class};
		Object[] ParameterValues = new Object[]{new String[]{cinvid},sstartdate,senddate,sLogcorp};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "正在获取供货历史信息...", 1, bo, null, "loadHistoryPriceInfor", ParameterTypes, ParameterValues);
		
		return (HistoryPriceVO[])o;
	}
	
	public static String updateBiddingHistoryPrice(String cbiddingid,String cinvid,UFDouble nprice) throws Exception{

		Class[] ParameterTypes = new Class[]{String.class,String.class,UFDouble.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cinvid,nprice};
		return PuPubVO.getString_TrimZeroLenAsNull(LongTimeTask.callRemoteService("pu",bo, "updateBiddingHistoryPrice", ParameterTypes, ParameterValues, 2));
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）留标处理
	 * 2011-6-14下午12:53:20
	 * @param tp
	 * @param biddingids 待处理标段的ID
	 * @param coparate 当前用户
	 * @param uDate 登陆日期
	 * @return 返回表头ts  
	 * @throws Exception
	 */
	public static Object misBidding(ToftPanel tp,String[] biddingids,String coparate,UFDate uDate) throws Exception{
		Class[] ParameterTypes = new Class[]{String[].class,String.class,UFDate.class};
		Object[] ParameterValues = new Object[]{biddingids,coparate,uDate};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "正在处理...", 1, bo, null, "misBidding", ParameterTypes, ParameterValues);
		
		return o;
	}
	
	/**
	 * @throws Exception 
	 * 被恶意报价终止的供应商  在继续校验 
	 */
	public static void isExitBadPrice(String ccustmanid,String cbiddingid) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{ccustmanid,cbiddingid};
		LongTimeTask.callRemoteService("pu",bo, "isExitBadPrice", ParameterTypes, ParameterValues, 2);
	}
}
