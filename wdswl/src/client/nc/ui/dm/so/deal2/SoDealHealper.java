package nc.ui.dm.so.deal2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.ui.pub.ToftPanel;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.SoDealVO;
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
 * ���˰��ŵ��ú�̨��
 * @author Administrator
 *
 */
public class SoDealHealper {
	
	private static String bo = "nc.bs.dm.so.deal2.SoDealBO";
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ��ѯ����վ�ǵ�ǰ��¼��Ա�󶨵Ĳֿ�� ���˰���
	 * �����ǰ��¼�����ֵܲ� ���Բ�ѯ���е� ���˰���
	 * @ʱ�䣺2011-3-25����09:16:20
	 * @param wheresql
	 * @return
	 * @throws Exception
	 */

	public static SoDealVO[] doQuery(String wheresql,String pk_storedoc) throws Exception{
		SoDealVO[] dealVos=null;
		Class[] ParameterTypes = new Class[] { String.class,String.class };
		Object[] ParameterValues = new Object[] { wheresql,pk_storedoc };
		Object o = LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, bo, "doQuery", ParameterTypes, ParameterValues, 2);
		if(o != null){
			dealVos = (SoDealVO[])o;
		}
		return dealVos;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  ���Ű�ť������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public static Object doDeal(SoDealBillVO[] billvos, ToftPanel tp) throws Exception {
		if (billvos == null || billvos.length == 0)
			return null;

		SoDealClientUI ui = (SoDealClientUI)tp;
		ArrayList<String> infor = new ArrayList<String>();
		infor.add(ui.cl.getUser());
		infor.add(ui.cl.getCorp());
		infor.add(ui.cl.getLogonDate().toString());
		Class[] ParameterTypes = new Class[] {SoDealBillVO[].class,ArrayList.class };
		Object[] ParameterValues = new Object[] { billvos,infor};
		return LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"���ڴ���...", 2, bo, null, "doDeal", ParameterTypes,
				ParameterValues);


	}	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * ���˼ƻ�  �ֹ����� ������
	 * @ʱ�䣺2011-3-25����02:59:20
	 */
	public static void doHandDeal(List ldata, ToftPanel tp) throws Exception {
		if (ldata == null || ldata.size() == 0)
			return;

//		����У��       ���ڰ���Ϊ0��Ʒ��           �ͻ����������Ƿ���ڿͻ���С������           ���˰�����������ͷ������ÿͻ�
		
		SoDealClientUI ui = (SoDealClientUI)tp;
		List<String> infor = new ArrayList<String>();
		infor.add(ui.cl.getUser());
		infor.add(ui.cl.getCorp());
		infor.add(ui.cl.getLogonDate().toString());
		Class[] ParameterTypes = new Class[] { List.class,List.class };
		Object[] ParameterValues = new Object[] { ldata,infor};
		LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, tp,
				"���ڴ���...", 2, bo, null, "doHandDeal", ParameterTypes,
				ParameterValues);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �ϲ���������
	 * @ʱ�䣺2011-7-7����01:41:54
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
			tmpHead.setDbilldate((UFDate)VOTool.max(vos, "dbilldate"));//Ӧȡ ��С��������
			tmpHead.setIsonsell(UFBoolean.FALSE);
			tmpHead.setCsalecorpid(vos[0].getCsalecorpid());
			String pk_stordoc=vos[0].getCbodywarehouseid();
			if(pk_stordoc==null || pk_stordoc.length()==0)
				pk_stordoc=cwhid;
			tmpHead.setCbodywarehouseid(pk_stordoc);

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
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �Կͻ������������Զ� ����  ������
	 * @ʱ�䣺2011-7-11����11:27:47
	 * @param lcust:��Ҫ�ֶ����ŵĿͻ�
	 * @param lnum����Ҫ�ֶ����ŵĿͻ��漰���Ĵ���Ĵ�����Ϣ��ÿ��������Ϣ�����¼������Ƶ��ô���Ķ�������
	 */
	public static void autoDealNum(List<SoDealBillVO> lcust,List<StoreInvNumVO> lnum){
		if(lcust == null || lcust.size() == 0 || lnum == null || lnum.size() == 0){
			return;
		}
		for(StoreInvNumVO numVO:lnum){
			ArrayList<SoDealVO> bodys = numVO.getLdeal();//�漰���ô�������ж�����ϸ
			Collections.sort(bodys, new Comparator<SoDealVO>(){
				public int compare(SoDealVO o1,SoDealVO o2){
					if(o1 == null)
						return 1;
					if(o2 == null)
						return -1;
					UFDate dbilldate1 = o1.getDbilldate();
					UFDate dbilldate2 = o2.getDbilldate();
					if(dbilldate1 == null || dbilldate2 == null){
						return 0;
					}
					return dbilldate2.compareTo(dbilldate1);
				}
			});
			UFDouble nallnum = numVO.getNassnum();//������������
			UFDouble hsl = numVO.getNnum().div(numVO.getNassnum());			
			if(bodys == null || bodys.size() == 0){
				continue;
			}
			for(SoDealVO body:bodys){
				UFDouble nnum = body.getNassnum();//���ΰ�������������
				if(nallnum.doubleValue()>nnum.doubleValue()){
//					�����������仯���� ��������  ���а���
					nallnum = nallnum.sub(nnum);
				}else if(nallnum.doubleValue()>0){
					body.setNassnum(nallnum);
					body.setNnum(nallnum.multiply(hsl));
					nallnum = WdsWlPubTool.DOUBLE_ZERO;					
				}else{
					body.setNassnum(WdsWlPubTool.DOUBLE_ZERO);
					body.setNnum(WdsWlPubTool.DOUBLE_ZERO);
				}
				
//				ͬ������
				synData(lcust, body);
			}
			
		}
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���۶����ֹ�����ʱ �������ź��ֵ
	 * @ʱ�䣺2011-7-11����02:15:14
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
