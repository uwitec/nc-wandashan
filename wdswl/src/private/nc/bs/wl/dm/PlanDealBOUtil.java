package nc.bs.wl.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dm.PlanDealVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
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
					strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
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
						reason="�ϸ񣬴���״̬��"+reason;
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
			strWhereSO= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWhereSO= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor1 = getStockBO().getSoOrderNdealNumInfor(pk_corp, strWhereSO);
		if (invNumInfor1 == null || invNumInfor1.size() == 0) {
			Logger.info("���ΰ��ŵĴ�������˵��������Ѱ���δ������");
			if (invNumInfor1 == null)
				invNumInfor1 = new HashMap<String, UFDouble[]>();
		}
		String  strWherePlan= null;
		if(fisdate){
			strWherePlan= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='Y' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}else{
			strWherePlan= " h.pk_outwhouse='"+pk_stordoc+"' and coalesce(b.bisdate,'N')='N' and b.pk_invbasdoc in "+tt.getSubSql(invs);
		}
		Map<String, UFDouble[]> invNumInfor2 = getStockBO().getPlanOrderNdealNumInfor(pk_corp, strWherePlan);
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
			if (tmpNumVO.getNassnum().doubleValue() > tmpNumVO.getNplanassnum()
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
	 * @param fisdate:�Ƿ������ 
	 * @param datas
	 * @throws BusinessException
	 */
	protected void arrangStornumout(boolean fisdate,PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		//���շ����ֿ� ����
		Map<String,ArrayList<PlanDealVO>> map = new HashMap<String ,ArrayList<PlanDealVO>>();
		for(PlanDealVO data:datas){
			String key = PuPubVO.getString_TrimZeroLenAsNull(data.getPk_outwhouse());
			if(key == null){
				continue;
			}
			if(map.containsKey(key)){
				map.get(key).add(data);
			}else{
				ArrayList<PlanDealVO> list = new ArrayList<PlanDealVO>();
				list.add(data);
				map.put(key, list);
			}
		}
		String strWhere = null;
		//��ѯ�����
		if(fisdate){
			strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";
		}else{
			strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
		}
		//��ѯ�����ֿ� ���д���Ŀ����
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_invmandoc,sum(whs_stocktonnage) num ");
		sql.append(" from tb_warehousestock ");
		sql.append(" where pk_customize1 = ?");
		if(strWhere !=null){
			sql.append(" and "+strWhere);
		}
		sql.append(" group by pk_invmandoc ");
		SQLParameter param = new SQLParameter();
		ArrayListProcessor processor = new ArrayListProcessor();
		for(Entry<String,ArrayList<PlanDealVO>> entry:map.entrySet()){
			String key = entry.getKey();
			ArrayList<PlanDealVO> values= entry.getValue();
			param.addParam(key);
			ArrayList<Object> list =(ArrayList<Object>)getDao().executeQuery(sql.toString(), param, processor);
			if(list != null && list.size() >0){
				for(int i=0;i<list.size();i++){
					Object[] obj =(Object[]) list.get(i);
					String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
					UFDouble num = PuPubVO.getUFDouble_NullAsZero(obj[1]);
					if(pk_invmandoc== null || num.doubleValue() <= 0){//����������Ϊ0�����ټ�������
						continue;
					}
					setNstoreNum(pk_invmandoc,num,values);
				}
			}
			param.clearParams();
		}
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
	protected void arrangStornumin(boolean fisdate,PlanDealVO[] datas) throws BusinessException{
		if(datas == null || datas.length == 0){
			return ;
		}
		//���շ����ֿ� ����
		Map<String,ArrayList<PlanDealVO>> map = new HashMap<String ,ArrayList<PlanDealVO>>();
		for(PlanDealVO data:datas){
			String key = PuPubVO.getString_TrimZeroLenAsNull(data.getPk_inwhouse());
			if(key == null){
				continue;
			}
			if(map.containsKey(key)){
				map.get(key).add(data);
			}else{
				ArrayList<PlanDealVO> list = new ArrayList<PlanDealVO>();
				list.add(data);
				map.put(key, list);
			}
		}
		String strWhere = null;
		//��ѯ�����
		if(fisdate){
			strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";
		}else{
			strWhere= " ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
		}
		//��ѯ�ջ��ֿ� ���д���Ŀ����
		StringBuffer sql = new StringBuffer();
		sql.append(" select pk_invmandoc,sum(whs_stocktonnage) num ");
		sql.append(" from tb_warehousestock ");
		sql.append(" where pk_customize1 = ?");
		if(strWhere !=null){
			sql.append(" and "+strWhere);
		}
		sql.append(" group by pk_invmandoc ");
		SQLParameter param = new SQLParameter();
		ArrayListProcessor processor = new ArrayListProcessor();
		for(Entry<String,ArrayList<PlanDealVO>> entry:map.entrySet()){
			String key = entry.getKey();
			ArrayList<PlanDealVO> values= entry.getValue();
			param.addParam(key);
			ArrayList<Object> list =(ArrayList<Object>)getDao().executeQuery(sql.toString(), param, processor);
			Map<String,UFDouble> curStornum = new HashMap<String, UFDouble>();//<�������,��ǰ�����>
			for(PlanDealVO value:values){
				String pk_ivnmandoc = value.getPk_invmandoc();
				if(!curStornum.containsKey(pk_ivnmandoc)){
					if(list != null && list.size() >0){
						for(int i=0;i<list.size();i++){
							Object[] obj =(Object[]) list.get(i);
							String invmanid = PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
							UFDouble num = PuPubVO.getUFDouble_NullAsZero(obj[1]);
							if(pk_ivnmandoc.equalsIgnoreCase(invmanid)){
								curStornum.put(pk_ivnmandoc, num);
							}
						}
					}else{
						curStornum.put(pk_ivnmandoc, new UFDouble(0));
					}
					
				
				}
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(curStornum.get(pk_ivnmandoc));
				value.setNstorenumin(num);
				num = num.add( PuPubVO.getUFDouble_NullAsZero(value.getNnum()));
				value.setNarrstorenumin(num);
				curStornum.put(pk_ivnmandoc, num);
			}
			param.clearParams();
		}
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ :
	 * @ʱ�䣺2011-11-4����09:59:52
	 * @param pk_invmandoc
	 * @param num
	 * @param values
	 */
	void setNstoreNum(String pk_invmandoc,UFDouble num,ArrayList<PlanDealVO> values){
		for(int i=0;i<values.size();i++){
			if(num.doubleValue()<=0){
				return;
			}
			if(!pk_invmandoc.equalsIgnoreCase(values.get(i).getPk_invmandoc())){
				continue;
			}
			values.get(i).setNstorenumout(num);
			UFDouble dealNum = PuPubVO.getUFDouble_NullAsZero(values.get(i).getNnum());//���ΰ�������
			num = num.sub(dealNum);
			values.get(i).setNarrstorenumout(num);
		}
	}

}
