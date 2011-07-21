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
		 * ��ѯ�ɹ��ƻ�
		 * �ƻ�����    ����λ   ��������   �������   �������
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
			//modify by zhw 2011-01-24  ���ݴ�������ѯ  �������ѯ
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
		calllongTimeService("pu", parent, "��ѯ�ɹ��ƻ�...", 1, bo, null, "loadPlanInvInfor", ParameterTypes, ParameterValues);
		
		PuPlanInvVO[] vos = (PuPlanInvVO[])o;
		return vos;
	}
	
	public static BiddingBillVO[] refAddButtonClicked(ToftPanel parent,ClientLink cl) throws Exception{
//		������ѯ��   ��ѯ����  ���ݽ���    ����ת��  ���� ����ǰ̨
		PuPlanInvVO[] vos = doQuery(parent,cl);
		if(getPlanQryDlg(cl.getCorp(), cl.getUser(), parent).getResult()!=UIDialog.ID_OK){
//			parent.showHintMessage("δ��ѯ������");
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
		calllongTimeService("pu", parent, "���ڻ��ֱ��...", 1, bo, null, "divideBidding", ParameterTypes, ParameterValues);
		
		return (BiddingBillVO[])o;
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��� ָ��ʱ����ڵĹ�����ʷ���
	 * 2011-6-4����03:54:31
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
		calllongTimeService("pu", tp, "���ڻ�ȡ������ʷ��Ϣ...", 1, bo, null, "loadHistoryPriceInfor", ParameterTypes, ParameterValues);
		
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
	 * @˵�������׸ڿ�ҵ�����괦��
	 * 2011-6-14����12:53:20
	 * @param tp
	 * @param biddingids �������ε�ID
	 * @param coparate ��ǰ�û�
	 * @param uDate ��½����
	 * @return ���ر�ͷts  
	 * @throws Exception
	 */
	public static Object misBidding(ToftPanel tp,String[] biddingids,String coparate,UFDate uDate) throws Exception{
		Class[] ParameterTypes = new Class[]{String[].class,String.class,UFDate.class};
		Object[] ParameterValues = new Object[]{biddingids,coparate,uDate};
		Object o = LongTimeTask.
		calllongTimeService("pu", tp, "���ڴ���...", 1, bo, null, "misBidding", ParameterTypes, ParameterValues);
		
		return o;
	}
	
	/**
	 * @throws Exception 
	 * �����ⱨ����ֹ�Ĺ�Ӧ��  �ڼ���У�� 
	 */
	public static void isExitBadPrice(String ccustmanid,String cbiddingid) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{ccustmanid,cbiddingid};
		LongTimeTask.callRemoteService("pu",bo, "isExitBadPrice", ParameterTypes, ParameterValues, 2);
	}
}
