package nc.bs.wl.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * ���˼ƻ����� ������
 * @author Administrator
 *
 */
public class PlanDealBOUtil {
	
	private BaseDAO dao = null;

	private BaseDAO getDao() {
		if (dao == null)
			dao = new BaseDAO();
		return dao;
	}
	private StockInvOnHandBO stockbo = null;
	private StockInvOnHandBO getStockBO() {
		if (stockbo == null) {
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��ѯ���ΰ��ŵĴ������������˶���ռ�����������˵�ռ���� 
	 * @ʱ�䣺2011-11-28����08:29:20
	 * @param fisdate :�Ƿ������
	 * @param pk_corp
	 * @param pk_stordoc:����ֿ�
	 * @param bodys
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, StoreInvNumVo> initInvNumInfor(boolean fisdate ,String pk_corp,String pk_stordoc,List<PlanDealVO> bodys) throws BusinessException{
		Map<String, StoreInvNumVo> invNumInfor=  new HashMap<String, StoreInvNumVo>();
		if(bodys == null || bodys.size()==0){
			return invNumInfor;
		}
		Set<String> cinvids = new HashSet<String>();// ���ΰ��ŵ����д��id
		StoreInvNumVo tmpNumVO = null;
		String key = null;
		for (PlanDealVO body : bodys) {
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(body
					.getPk_invbasdoc());
			cinvids.add(body.getPk_invbasdoc());
			if (invNumInfor.containsKey(key)) {
				tmpNumVO = invNumInfor.get(key);
			} else {
				tmpNumVO = new StoreInvNumVo();
				tmpNumVO.setPk_corp(pk_corp);
				tmpNumVO.setCstoreid(pk_stordoc);
				tmpNumVO.setCinvbasid(body.getPk_invbasdoc());
				tmpNumVO.setCinvmanid(body.getPk_invmandoc());
				String strWhere= null;
				//��ѯ�����
				if(fisdate){
					strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";
				}else{
					strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"')";
//					strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
				}
				UFDouble[]stocknums = getStockBO().getInvStockNum(pk_corp,
						tmpNumVO.getCstoreid(), null,
						tmpNumVO.getCinvbasid(), null, null,strWhere);
				if (stocknums == null || stocknums.length == 0){
					String reason=" ���"
						+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
						+ " �޿����";
					if(fisdate){
						reason="������״̬��"+reason;
					}else{
						reason="�ϸ�״̬��"+reason;
					}
					Logger.info(reason);
					throw new BusinessException(reason);
				}
				tmpNumVO.setNstocknum(stocknums[0]);
				tmpNumVO.setNstockassnum(stocknums[1]);
			}
			//������Ҫ��������
			tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplannum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
			tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(
					tmpNumVO.getNplanassnum()).add(
					PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));
			invNumInfor.put(key, tmpNumVO);
		}
		if (invNumInfor.size() == 0) {
			Logger.info("���δ����Ŵ������Ϊ�գ��޷����ţ��˳�");
			return invNumInfor;
		}
		//2.��������˵�ռ�����ͷ��˶���ռ����
		Logger.info("��ȡ����Ѱ���δ������...");
		TempTableUtil tt = new TempTableUtil();
		String[] invs= cinvids.toArray(new String[0]);
		String  strWhereSO= null;
		if(fisdate){
			strWhereSO= "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWhereSO= "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor1 = getStockBO().getSoOrderNdealNumInfor(pk_corp,pk_stordoc, strWhereSO);
		if (invNumInfor1 == null || invNumInfor1.size() == 0) {
			Logger.info("���ΰ��ŵĴ�������˵��������Ѱ���δ������");
			if (invNumInfor1 == null)
				invNumInfor1 = new HashMap<String, UFDouble[]>();
		}
		String  strWherePlan= null;
		if(fisdate){
			strWherePlan= "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWherePlan= "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor2 = getStockBO().getPlanOrderNdealNumInfor(pk_corp,pk_stordoc,strWherePlan);
		if (invNumInfor2 == null || invNumInfor2.size() == 0) {
			Logger.info("���ΰ��ŵĴ�����˶����������Ѱ���δ������");
			if (invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
		}
		for (String key2 : invNumInfor.keySet()) {
			tmpNumVO = invNumInfor.get(key2);
			UFDouble[] stocknums = invNumInfor1.get(key2);
			UFDouble[] stocknum2 = invNumInfor2.get(key2);
			if (tmpNumVO == null)
				continue;
			//1. �Ѱ����˵�ռ����
			UFDouble nplannum= PuPubVO.getUFDouble_NullAsZero(null);
			UFDouble nplanassnum= PuPubVO.getUFDouble_NullAsZero(null);
			if(stocknums != null){
				nplannum= nplannum.add(PuPubVO.getUFDouble_NullAsZero(stocknums[0]));
				nplanassnum= nplanassnum.add(PuPubVO.getUFDouble_NullAsZero(stocknums[1]));

			}
			if(stocknum2 != null){
				nplannum= nplannum.add(PuPubVO.getUFDouble_NullAsZero(stocknum2[0]));
				nplanassnum= nplanassnum.add(PuPubVO.getUFDouble_NullAsZero(stocknum2[1]));

			}
			tmpNumVO.setNdealnum(nplannum);
			tmpNumVO.setNdealassnum(nplanassnum);
			//2.��ǰ������=�����-�Ѿ����ŵ��˵�ռ����
			tmpNumVO.setNnum(tmpNumVO.getNstocknum()
					.sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(
					tmpNumVO.getNdealassnum()));
			// ��������� > ���ΰ����� �����Ϊ�ɰ���
			if (tmpNumVO.getNassnum().doubleValue() >= tmpNumVO.getNplanassnum()
					.doubleValue()){
				tmpNumVO.setBisok(UFBoolean.TRUE);
			}else{
				tmpNumVO.setBisok(UFBoolean.FALSE);
				String reason=" ���"
					+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
					+ " ��ǰ�������" + tmpNumVO.getNstockassnum() + " �Ѱ���δ��������"
					+ tmpNumVO.getNdealassnum() + " ��ǰ��������"
					+ tmpNumVO.getNassnum() + " ���δ�����������"
					+ tmpNumVO.getNplanassnum();
				if(fisdate){
					reason="������״̬��"+reason;
				}else{
					reason="�ϸ񣬴���״̬��"+reason;
				}
				Logger.info(reason);
				throw new BusinessException(reason);
			}
				
		}
		return invNumInfor;
			
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ������վ������Ͱ��ź���������
	 * @ʱ�䣺2011-11-4����10:26:39
	 * @param datas
	 * @throws BusinessException
	 */
	protected void arrangStornumout(String pk_corp,PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		if(pk_corp == null || "".equalsIgnoreCase(pk_corp)){
			pk_corp = SQLHelper.getCorpPk();
		}
		//�������ֿ����
		CircularlyAccessibleValueObject[][]  splitvos = SplitBillVOs.getSplitVOs(datas, new String[]{"pk_inwhouse"});
		for(int i=0;i<splitvos.length;i++){
			PlanDealVO[] splitvos_second= (PlanDealVO[])splitvos[i];
			if(splitvos_second == null || splitvos_second.length ==0){
				continue;
			}
			String pk_outwhous =  PuPubVO.getString_TrimZeroLenAsNull(splitvos_second[0].getPk_outwhouse());
			//1.һ�β�ѯ��ǰ�ֿ������д���Ŀ����������ȽϷ���
			ArrayList<String> pk_invbasdocs = new ArrayList<String>();
			for(int j=0;j<splitvos_second.length;j++){
				String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(splitvos_second[j].getPk_invbasdoc());
				if(pk_invbasdoc == null || pk_invbasdocs.contains(pk_invbasdoc)){//����������Ϊ�գ������Ѿ������������
					continue;
				}
				pk_invbasdocs.add(pk_invbasdoc);
			}
			if(pk_invbasdocs.size() ==0){
				return ;
			}
			//2 �������ѯ
			//2.1����������������Ʒ�ʹ�����
			Map<String, UFDouble[]> invStornum_drq = getStockNum(true, pk_corp, pk_outwhous, pk_invbasdocs);//�����ڿ����
			Map<String, UFDouble[]> invStornum_hg = getStockNum(false, pk_corp, pk_outwhous, pk_invbasdocs);//�ϸ�Ʒ�����
			//3 ��ѯ�����˵�ռ��������������Ʒ�ʹ�����
			Map<String, UFDouble[]>  soordernum_drq = getSoOrderNdealNumInfor(true, pk_corp, pk_outwhous, pk_invbasdocs);
			Map<String, UFDouble[]>  soordernum_hg= getSoOrderNdealNumInfor(false, pk_corp, pk_outwhous, pk_invbasdocs);
			//4 ��ѯ���˶���ռ��������������Ʒ�ʹ�����
			Map<String, UFDouble[]>  sendordernum_drq = getPlanOrderNdealNumInfor(true, pk_corp, pk_outwhous, pk_invbasdocs);
			Map<String, UFDouble[]>  sendordernum_hg = getPlanOrderNdealNumInfor(false, pk_corp, pk_outwhous, pk_invbasdocs);
			dealOutNum(true,invStornum_drq,soordernum_drq,sendordernum_drq,splitvos_second);
			dealOutNum(false,invStornum_hg,soordernum_hg,sendordernum_hg,splitvos_second);
		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-1-6����08:51:05
	 * @param fisdate
	 * @param invStornum:�����
	 * @param invNumInfor1�������˵�ռ����
	 * @param invNumInfor2:���˶���ռ����
	 * @param datas
	 */
	protected void dealOutNum(boolean fisdate,Map<String, UFDouble[]> invStornum,Map<String, UFDouble[]> invNumInfor1 ,Map<String, UFDouble[]> invNumInfor2,PlanDealVO[] datas){
		//3.���ݴ������ ��������� ������
		if(datas == null || datas.length ==0){
			return;
		}
		if(invStornum == null || invStornum.size() ==0){//�޿������������
			return;
		}
		for(String key:invStornum.keySet()){
			UFDouble [] stornums = invStornum.get(key);
			UFDouble stornum = PuPubVO.getUFDouble_NullAsZero(0);//����վ���������
			UFDouble nuesfulnum = PuPubVO.getUFDouble_NullAsZero(0);//������==�����-�����˵�ռ����-���˶���ռ����
			if(stornums !=null && stornums.length >0){
				stornum = PuPubVO.getUFDouble_NullAsZero(stornums[0]);
			}
			UFDouble nsoordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if(invNumInfor1 !=null && invNumInfor1.size()>0){
				UFDouble[] nsoordernums = invNumInfor1.get(key);
				if(nsoordernums !=null && nsoordernums.length >0){
					nsoordernum = PuPubVO.getUFDouble_NullAsZero(nsoordernums[0]);
				}
			}
			UFDouble nsendordernum = PuPubVO.getUFDouble_NullAsZero(0);
			if(invNumInfor2 !=null && invNumInfor2.size()>0){
				UFDouble[] nsendordernums = invNumInfor2.get(key);
				if(nsendordernums !=null && nsendordernums.length >0){
					nsendordernum = PuPubVO.getUFDouble_NullAsZero(nsendordernums[0]);
				}
			}
			nuesfulnum = stornum.sub(nsoordernum).sub(nsendordernum);
			for(int j=0;j<datas.length;j++){
				String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(datas[j].getAttributeValue("pk_invbasdoc"));
				if(pk_invbasdoc == null){//����������Ϊ�������
					continue;
				}
				UFDouble ndealnum = PuPubVO.getUFDouble_NullAsZero(datas[j].getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(datas[j].getNdealnum()));
				UFBoolean bisdate = PuPubVO.getUFBoolean_NullAs(datas[j].getBisdate(), UFBoolean.FALSE);
				if(pk_invbasdoc.equalsIgnoreCase(key)){
					if(fisdate && bisdate.booleanValue()){
						datas[j].setNdrqstorenumout(stornum);
						datas[j].setNdrqarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNdrqusefulnumout(nuesfulnum);
						datas[j].setNdrqarrusefulnumout(nuesfulnum.sub(ndealnum));
					}else if(!fisdate && !bisdate.booleanValue()){
						datas[j].setNstorenumout(stornum);
						datas[j].setNarrstorenumout(stornum.sub(ndealnum));
						datas[j].setNusefulnumout(nuesfulnum);
						datas[j].setNarrusefulnumout(nuesfulnum.sub(ndealnum));
				
					}
				}
			}
		}
	
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-1-6����08:09:28
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, UFDouble[]> getStockNum(boolean fisdate,String pk_corp,String pk_outwhous,ArrayList<String> pk_invbasdocs) throws BusinessException{
		String strWhere= null;
		if(fisdate){
			strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";//ss_pk ���״̬id

		}else{
			strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"')";
		}
		return getStockBO().getStockNum(pk_corp, pk_outwhous, null, pk_invbasdocs, null, null, strWhere);
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-1-6����08:13:13
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException 
	 */
	protected Map<String, UFDouble[]> getSoOrderNdealNumInfor(boolean fisdate,String pk_corp,String pk_outwhous,ArrayList<String> pk_invbasdocs) throws BusinessException{
		String strWhere= null;
		TempTableUtil tt = new TempTableUtil();
		if(fisdate){
			strWhere= "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(pk_invbasdocs);
		}else{
			strWhere= "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor= getStockBO().getSoOrderNdealNumInfor(pk_corp,pk_outwhous, strWhere);
		
		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("���β�ѯ�Ĵ�����˶����������Ѱ���δ������");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;

	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-1-6����08:13:13
	 * @param fisdate:�Ƿ������
	 * @param pk_corp
	 * @param pk_outwhous
	 * @param pk_invbasdocs
	 * @return
	 * @throws BusinessException 
	 */
	protected Map<String, UFDouble[]> getPlanOrderNdealNumInfor(boolean fisdate,String pk_corp,String pk_outwhous,ArrayList<String> pk_invbasdocs) throws BusinessException{
		TempTableUtil tt = new TempTableUtil();
		String  strWherePlan= null;
		if(fisdate){
			strWherePlan= "  coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(pk_invbasdocs);
		}else{
			strWherePlan= "  coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(pk_invbasdocs);
		}
		Map<String, UFDouble[]> invNumInfor = getStockBO().getPlanOrderNdealNumInfor(pk_corp,pk_outwhous,strWherePlan);
		if (invNumInfor == null || invNumInfor.size() == 0) {
			Logger.info("���β�ѯ�Ĵ�����˶����������Ѱ���δ������");
			if (invNumInfor == null)
				invNumInfor = new HashMap<String, UFDouble[]>();
		}
		return invNumInfor;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ���ջ�վ������Ͱ��ź���������
	 * @ʱ�䣺2011-11-4����10:26:39
	 * @param datas
	 * @param fisdate :�Ƿ������
	 * @throws BusinessException
	 */
	protected void arrangStornumin(String pk_corp,PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		if(pk_corp == null || "".equalsIgnoreCase(pk_corp)){
			pk_corp = SQLHelper.getCorpPk();
		}
		//�������ֿ����
		CircularlyAccessibleValueObject[][]  splitvos = SplitBillVOs.getSplitVOs(datas, new String[]{"pk_inwhouse"});
		for(int i=0;i<splitvos.length;i++){
			PlanDealVO[] splitvos_second= (PlanDealVO[])splitvos[i];
			if(splitvos_second == null || splitvos_second.length ==0){
				continue;
			}
			String pk_outwhous =  PuPubVO.getString_TrimZeroLenAsNull(splitvos_second[0].getAttributeValue("pk_inwhouse"));
			//һ�β�ѯ��ǰ�ֿ������д���Ŀ����������ȽϷ���
			ArrayList<String> pk_invbasdocs = new ArrayList<String>();
			for(int j=0;j<splitvos_second.length;j++){
				String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(splitvos_second[j].getAttributeValue("pk_invbasdoc"));
				if(pk_invbasdoc == null || pk_invbasdocs.contains(pk_invbasdoc)){//����������Ϊ�գ������Ѿ������������
					continue;
				}
				pk_invbasdocs.add(pk_invbasdoc);
			}
			//�����Ƿ���գ����Զ�������
			Map<String, UFDouble[]> invStornum_drq = getStockNum(true, pk_corp, pk_outwhous, pk_invbasdocs);
			Map<String, UFDouble[]> invStornum_hg = getStockNum(false, pk_corp, pk_outwhous, pk_invbasdocs);
			dealInNmu(true,invStornum_drq,splitvos_second);
			dealInNmu(false,invStornum_hg,splitvos_second);

		}
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-1-6����09:14:25
	 * @param fisdate:�Ƿ������
	 * @param invStornum
	 * @param datas
	 */
	void dealInNmu(boolean fisdate,Map<String, UFDouble[]> invStornum,PlanDealVO[] datas){
		if(datas == null || datas.length ==0){
			return ;
		}
		for(int j=0;j<datas.length;j++){
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(datas[j].getAttributeValue("pk_invbasdoc"));
			if(pk_invbasdoc == null){//����������Ϊ�������
				continue;
			}
			UFDouble stornum = PuPubVO.getUFDouble_NullAsZero(0);//����վ���������
			UFDouble [] stornums = invStornum.get(pk_invbasdoc);
			if(stornums != null && stornums.length>0){
				stornum = PuPubVO.getUFDouble_NullAsZero(stornums[0]);
			}
			UFDouble ndealnum = PuPubVO.getUFDouble_NullAsZero(datas[j].getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(datas[j].getNdealnum()));
			UFBoolean bisdate = PuPubVO.getUFBoolean_NullAs(datas[j].getBisdate(), UFBoolean.FALSE);
			if(fisdate && bisdate.booleanValue() ){
				datas[j].setNdrqstorenumin(stornum);
				datas[j].setNdrqarrstorenumin(stornum.add(ndealnum));
			}else if(!fisdate && !bisdate.booleanValue() ){//�Ǵ����ڵĿ������ֻ���䵽�Ǵ����ڵĿ����
				datas[j].setNstorenumin(stornum);
				datas[j].setNarrstorenumin(stornum.add(ndealnum));
			}
		}
	}
	
}
