package nc.bs.dm.so.deal2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dm.so.SoDealBO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author zhf   ���ۼƻ������㷨��
 * 
 * ���� �����ŵ�����  ���а��Ŵ���  ������Ҫ�ֹ����ŵ�����
 *
 */
public class SoDealCol {
	
	private SoDealBillVO[] bills = null;
	
	private List lpara = null;
	public SoDealCol(){
		super();
	}
	public SoDealCol(SoDealBillVO[] bills,List lpara){
		super();
		this.bills = bills;
		this.lpara = lpara;
	}
	
	public void setData(SoDealBillVO[] bills,List lpara){
		this.bills = bills;
		this.lpara = lpara;
	}
	
	private StockInvOnHandBO stockbo = null;
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	private StockInvOnHandBO getStockBO(){
		if(stockbo == null){
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}
	
	private Map<String,UFDouble> custMinNumInfor = null;
	public void clearCustMinNumInfor(){
		if(custMinNumInfor != null)
			custMinNumInfor.clear();
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ͨ���ֲֿ��̰󶨻�ȡ  �ֲֿ��̵���С����������
	 * @ʱ�䣺2011-7-7����04:09:28
	 * @param ccustid
	 * @param pk_store
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getMinSendNumForCust(String ccustid,String pk_store) throws BusinessException{

		if(PuPubVO.getString_TrimZeroLenAsNull(ccustid)==null)
			return null;
		String key = WdsWlPubTool.getString_NullAsTrimZeroLen(pk_store)+WdsWlPubTool.getString_NullAsTrimZeroLen(ccustid);
		if(custMinNumInfor == null){
			custMinNumInfor = new HashMap<String, UFDouble>();
		}
		if(!custMinNumInfor.containsKey(key)){
			String sql = " select ndef1 from tb_storcubasdoc where isnull(dr,0)=0 and " +
			" pk_cumandoc = '"+ccustid+"' and pk_stordoc = '"+pk_store+"'";// (select pk_stordoc from bd_stordoc where " +
			//					"isnull(dr,0)=0 and )";
			custMinNumInfor.put(key,PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR)));
		}
		return custMinNumInfor.get(key);
	}
	
	private void initInvNumInfor(Map<String, StoreInvNumVO> invNumInfor) throws BusinessException {

		SoDealVO[] bodys = null;
		SoDealHeaderVo head = null;
		String key = null;

		//	�������Ϣ  key �ֿ�+���    
		//	Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();

		StoreInvNumVO tmpNumVO = null;
		//	��ȡ�����
		UFDouble[] stocknums = null;
		String pk_corp = SQLHelper.getCorpPk();
		Set<String> invs = new HashSet<String>();
		Logger.info("��ȡ��浱ǰ����...");
		for(SoDealBillVO bill:bills){
			head = bill.getHeader();
			bodys = bill.getBodyVos();

			if(bodys == null || bodys.length ==0)
				continue;

			for(SoDealVO body:bodys){
				key = //WdsWlPubTool.getString_NullAsTrimZeroLen(head.getCbodywarehouseid())+
				WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCinvbasdocid());
				invs.add(body.getCinvbasdocid());
				if(invNumInfor.containsKey(key)){
					tmpNumVO = invNumInfor.get(key);
				}else{
					tmpNumVO = new StoreInvNumVO();
					tmpNumVO.setPk_corp(pk_corp);
					tmpNumVO.setCstoreid(head.getCbodywarehouseid());
					tmpNumVO.setCinvbasid(body.getCinvbasdocid());
					tmpNumVO.setCinvmanid(body.getCinventoryid());

					stocknums = getStockBO()
					.getInvStockNum(pk_corp, tmpNumVO.getCstoreid(), null, tmpNumVO.getCinvbasid(), null, null);
					
					if(stocknums == null || stocknums.length == 0)
						continue;
					tmpNumVO.setNstocknum(stocknums[0]);
					tmpNumVO.setNstockassnum(stocknums[1]);

				}

				tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNplannum())
						.add(PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
				tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNplanassnum())
						.add(PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));				

				invNumInfor.put(key, tmpNumVO);				
			}			
		}


		if(invNumInfor.size()==0){
			Logger.info("���δ����Ŵ������Ϊ�գ��޷����ţ��Ƴ�");
			return;
		}
		


		//	��ȡռ����
		Logger.info("��ȡ����Ѱ���δ������...");
		Map<String,UFDouble[]> invNumInfor2 = getStockBO().getNdealNumInfor(pk_corp, head.getCbodywarehouseid(), invs.toArray(new String[0]), new TempTableUtil());
		if(invNumInfor2 == null || invNumInfor2.size() == 0){
			Logger.info("���ΰ��ŵĴ���������Ѱ���δ������");
			if(invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
//			return;
		}
			

		Logger.info("���δ����Ŵ�����״����");
		for(String key2:invNumInfor.keySet()){
			tmpNumVO = invNumInfor.get(key2);
			stocknums = invNumInfor2.get(key2);
			if(tmpNumVO == null)
				continue;
			tmpNumVO.setNdealnum(stocknums == null?WdsWlPubTool.DOUBLE_ZERO:stocknums[0]);
			tmpNumVO.setNdealassnum(stocknums == null?WdsWlPubTool.DOUBLE_ZERO:stocknums[1]);
			//		��ǰ������
			tmpNumVO.setNnum(tmpNumVO.getNstocknum().sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(tmpNumVO.getNdealassnum()));
			
			if(tmpNumVO.getNassnum().doubleValue()>tmpNumVO.getNplanassnum().doubleValue())
				tmpNumVO.setBisok(UFBoolean.TRUE);
			else
				tmpNumVO.setBisok(UFBoolean.FALSE);
			Logger.info(" ���"+WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
					+" ��ǰ������"+tmpNumVO.getNstockassnum()
					+" �Ѱ���δ��������"+ tmpNumVO.getNdealassnum()
					+" ���ο�������"+ tmpNumVO.getNassnum()
					+" ���δ�����������"+ tmpNumVO.getNplanassnum());
		}	
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �㷨������
	 * 1����Ʒ�ֻ�ȡ����Ʒ�ֵĿ�����
	 * 2����Ʒ�ֻ��ܸ���Ʒ�ֵ�������
	 * 3����Ʒ�ַ�Ϊ���ࣺ1������������ 2������������
	 * 4�����ͻ���Ϊ���ࣺ1����ֱ�Ӱ����� 2�����ڴ��������Ʒ�ֵĿͻ���
	 * 5����ֱ�Ӱ��ŵĿͻ� ֱ�Ӱ��������˵�
	 * 6������Ŀͻ����ݿͻ���С���������˿�����������������ȿͻ�����С��������С �����ÿͻ�
	 * 7����ʣ��Ŀͻ����ݷ������ �ȴ� �û��ֹ�����
	 * @ʱ�䣺2011-7-7����04:58:32
	 * ���ز������ŵĿͻ�   ��  �������ŵ�Ʒ�ֿ��״̬
	 */
	public Object col() throws Exception {
		if(bills==null || bills.length ==0)
			return null;
//		�������Ϣ  key �ֿ�+���    -----------ȥ���ֿ�   ����վ���ܲ�ͬ
		Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();
		initInvNumInfor(invNumInfor);
        if(invNumInfor.size() == 0)
        	throw new BusinessException("���д���Ĵ�����Ϊ��,�޷����ţ��˳�");
//      ���ݴ������˿ͻ������ŵ�����
//        Logger.info("###########################################################");
        Logger.info("������С���������˿�������ƫ�͵Ĵ��");
        List<SoDealVO> ldeal = null;
        List<SoDealVO> lnodeal = null;
        SoDealVO[] bodys = null;
        List<SoDealBillVO> lcust = new ArrayList<SoDealBillVO>();//���ܷ����Ŀͻ���Ϣ
        StoreInvNumVO tmpNumVO = null;
        for(SoDealBillVO bill:bills){
        	bodys = bill.getBodyVos();
        	if(bodys == null || bodys.length ==0)
        		continue;
        	boolean isdeal = true;
        	boolean pass = false;
        	for(SoDealVO body:bodys){
//        		body.setDbilldate(dbilldate)
        		tmpNumVO = invNumInfor.get(body.getCinvbasdocid());
        		if(tmpNumVO == null || PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNnum()).equals(WdsWlPubTool.DOUBLE_ZERO)){
//        			���ڴ���Ŀ�����Ϊ�յ�     �漰�ô���Ŀͻ�  �����ɰ��� ����  �ÿͻ�ֱ�Ӷ���

        			Logger.info("���"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"������Ϊ��,�ͻ�["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]�����޷�����");
        			
        			pass = true;
        			break;
        		}
        		
//        		������������ ���ڸÿͻ�����С������  ����  ���ÿͻ�

        		if(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNassnum())
        				.compareTo(
        						getMinSendNumForCust(bill.getHeader().getCcustomerid(), 
        								bill.getHeader().getCbodywarehouseid()))<0){
        			Logger.info("���"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"���������ڿͻ�����С������,�ͻ�["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]�����޷�����");
        			pass = true;
        			break;
        		}
        		if(!PuPubVO.getUFBoolean_NullAs(invNumInfor.get(body.getCinvbasdocid()).getBisok(),UFBoolean.FALSE).booleanValue()){
        			isdeal = false;
//        			Logger.info("���"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"���������ڱ��δ���������,�ͻ�["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]���ֹ�����");
        			continue;
        		}
        	}
        	
        	
        	if(pass)//�ÿͻ��ı��η���Ҫ�� ����  ����� ����  ��  ����
        		continue;
        	if(isdeal){
        		if(ldeal == null){
        			ldeal = new ArrayList<SoDealVO>();
        		}
        		ldeal.addAll(Arrays.asList(bodys));
        		Logger.info("##�ͻ�["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]���ο�ֱ�Ӱ���");
        	}else{
        		if(lnodeal == null){
        			lnodeal = new ArrayList<SoDealVO>();
        		}
        		lnodeal.addAll(Arrays.asList(bodys));
        		lcust.add(bill);
        	}        	
        }
        
        if(ldeal == null || ldeal.size()==0){
        	Logger.info("���ΰ���δ���ڿ�ֱ�Ӱ��ŵĿͻ�");
        }else{//ֱ�Ӱ���
        	SoDealBO dealbo = new SoDealBO();
        	dealbo.doDeal(ldeal, lpara);
        	Logger.info("ϵͳֱ�Ӱ��ųɹ�");
        }
        if(lnodeal!= null && lnodeal.size()>0){//������С��������  �ֶ�����
//        	��֪����ι���      �ݲ����� ��С������       �������ֱ�ӽ����ֹ�����
//        	return new Object[]{lnodeal,invNumInfor};
//        	������ת����װ����
        	Collection<StoreInvNumVO> c = invNumInfor.values();
        	Iterator<StoreInvNumVO> it = c.iterator();
        	StoreInvNumVO tmp = null;
        	List<StoreInvNumVO> ltmp = new ArrayList<StoreInvNumVO>();
        	
        	while(it.hasNext()){
        		tmp = it.next();
        		for(SoDealVO deal:lnodeal){
        			if(deal.getCinvbasdocid().equalsIgnoreCase(tmp.getCinvbasid())){
        				tmp.getLdeal().add(deal);
        			}
        		}
        		if(tmp.getLdeal().size()>0)
        			ltmp.add(tmp);
        	}
        	if(lcust.size()>0)
        	Logger.info("����"+lcust.size()+"���ͻ����ڿ�治����Ҫ�ֹ����а���");
//        	UFDateTime time2 = new UFDateTime(System.currentTimeMillis());
        	Logger.info("���ΰ��Ŵ������,���ؽ����ֹ�����");
        	Logger.info("#####################################################");
        	return new Object[]{lcust,ltmp};
        } else{
        	Logger.info("���ΰ���δ������Ҫ�û��ֹ����ŵ�����");
        	Logger.info("���ΰ��Ŵ������");
        	Logger.info("#####################################################");
        }
        return null;
	}

}
