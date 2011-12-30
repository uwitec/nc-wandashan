package nc.bs.wl.pub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsWlIcPubDealTool {
	
	private static Map dataMap = new HashMap();
	
	
	/**
	 * 
	 * @���ߣ�zhf ������Դid ��  ���κ�  ���л��ܺϲ�  ��浥��������
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-28����03:26:41
	 * @param bill ������Ŀ�浥��
	 * @param isin �Ƿ��������
	 */
	public static void combinItemsBySourceAndInv(GeneralBillVO bill,boolean isin){
		//������ش����κ�  Ӧ�ð���  ��Դ����id + ���κ�  ���л��ܴ���------zhf		
		String key = null;
		GeneralBillItemVO[] items = bill.getItemVOs();
		GeneralBillItemVO tmp = null;
		LocatorVO locavo = null;		
		for(GeneralBillItemVO item:items){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(item.getCsourcebillbid())+WdsWlPubTool.getString_NullAsTrimZeroLen(item.getVbatchcode());
			locavo = item.getLocator()[0];
			if(dataMap.containsKey(key)){
				tmp = (GeneralBillItemVO)(dataMap.get(key));
				if(isin){
					item.setNshouldinnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldinnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum())));
					item.setNneedinassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNneedinassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNneedinassistnum())));
					item.setNinnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNinnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNinnum())));
					item.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNinassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNinassistnum())));
					locavo.setNinspacenum(item.getNinnum());
					locavo.setNinspaceassistnum(item.getNinassistnum());
				}else{
					item.setNshouldoutnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldoutnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum())));
					item.setNshouldoutassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldoutassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutassistnum())));
					item.setNoutnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNoutnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNoutnum())));
					item.setNoutassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNoutassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNoutassistnum())));
					locavo.setNoutspacenum(item.getNoutnum());
					locavo.setNoutspaceassistnum(item.getNoutassistnum());
				}
			}
			dataMap.put(key, item);
		}
		GeneralBillItemVO[] newItems = (GeneralBillItemVO[])(dataMap.values().toArray(new GeneralBillItemVO[0]));
		bill.setChildrenVO(newItems);
		dataMap.clear();
	}
	
	private static void adjustBillData(GeneralBillVO bill,String corp,String date,String coperator,BaseDAO dao)
	throws BusinessException{
		Integer dates= getDefaultDay(corp,dao);
		Date dqdate= new Date();
		int dqday= dqdate.getDay();
		int jzday=dates.intValue();
		//�����ǰ��С�ڵ��ڽ����ڣ���ERP�ĳ���ⵥ������Ϊ��ǰ�ڣ�
		//�����ǰ�ڴ��ڽ����ڣ���EPR����Ϊ��һ����1��
		UFDate dbilldate = new UFDate(date);
		if(dqday >jzday){
			dbilldate = NextMonth();
		}
		bill.getHeaderVO().setDbilldate(dbilldate);
		for(GeneralBillItemVO itemvo :bill.getItemVOs()){
			itemvo.setDbizdate(dbilldate);
		}
	}
	

	//��ǰ�µ���һ����һ��   ����
	private static UFDate NextMonth(){
        Calendar   calendar   =   new   GregorianCalendar(); 
        calendar.set(Calendar.DAY_OF_MONTH,  calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 1); //����ĳ���µ�һ��
		return new UFDate(calendar.getTime());		
	}
	
	/**
	 * 
	 * @���ߣ�lyf ��ѯ������Ĭ��ֵ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-20����10:23:43
	 * @throws BusinessException
	 */
	private static Integer getDefaultDay(String corp,BaseDAO dao) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select datavale from wds_periodsetting_h ");
		sql.append(" where isnull(dr,0) =0 ");
		sql.append(" and pk_corp='"+corp+"'");
		Object value = dao.executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR);
		return PuPubVO.getInteger_NullAs(value, 20);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-28����03:41:43
	 * @param bill ������������ɵĿ�浥��
	 * @param l_map �ѷ�װ�õĻ�λ��Ϣ
	 * @param coperator ��ǰ�û�
	 * @param sdate ��ǰ����
	 * @param fisvbatchcontorl �Ƿ�ش����ε�erp
	 * @param returnBatchcode Ĭ�ϻش������κ� ��2009��
	 */
	public static void appFieldValueForIcNewBill(GeneralBillVO bill,
			Map<String,ArrayList<LocatorVO>> l_map,
			String corp,
			String coperator,
			String sdate,
			UFBoolean fisvbatchcontorl,
			BaseDAO dao) throws BusinessException{
		if(bill == null)
			return;
		//���ݽ��������ã��޸Ĵ�ERP�ĵ��ݵ������ڣ��ͱ�����������
		adjustBillData(bill, corp, sdate, coperator, dao);
		bill.setGetPlanPriceAtBs(false);//����Ҫ��ѯ�ƻ���
		bill.getHeaderVO().setCoperatoridnow(coperator);//��ǰ������///ҵ�������������ǰ������Ա
		bill.getHeaderVO().setDbilldate(new UFDate(sdate));//��������
		bill.getHeaderVO().setStatus(VOStatus.NEW);//��������״̬
		String returnBatchcode = getDefaultVbatchCode(corp);//������ERPĬ�ϵ����κ�
		GeneralBillItemVO[] items = bill.getItemVOs();
		if(items == null || items.length == 0)
			return;
		int index =  1;
		for(GeneralBillItemVO item:items){
			item.setCrowno(String.valueOf((index)*10));//�к�
			item.setStatus(VOStatus.NEW);//��������״̬
			if(item.getDbizdate() == null){
				item.setDbizdate(new UFDate(sdate));//ҵ������
			}	
			if(!fisvbatchcontorl.booleanValue()){
				item.setVbatchcode(returnBatchcode);
			}
			//���û�λ��Ϣ
			String key  = WdsWlPubTool.getString_NullAsTrimZeroLen(item.getAttributeValue(WdsWlPubConst.csourcebid_wds));
			ArrayList<LocatorVO> lvo = l_map.get(key);
			if(lvo!=null && lvo.size()>0){
				item.setLocator(lvo.toArray(new LocatorVO[0]));
			}
			index++;
		}
	}
	
	private static String m_batchcode = "";
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ���ɽ������д��Ӧ�������κţ�Ĭ����2009����ͨ������������
	 * @ʱ�䣺2011-4-20����11:57:57
	 * @return
	 */
	public static  String getDefaultVbatchCode(String corp){
		if(PuPubVO.getString_TrimZeroLenAsNull(m_batchcode)!=null)
			return m_batchcode;
		ISysInitQry sysinitQry = (ISysInitQry) NCLocator.getInstance().lookup(ISysInitQry.class.getName());
		try {
			SysInitVO vo =sysinitQry.queryByParaCode(corp, "WDS00");
			if(vo != null){
				m_batchcode = vo.getValue();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println("��ȡ����WDS00ʧ��");
			return "2009";
		}
		return m_batchcode;
	}

}
