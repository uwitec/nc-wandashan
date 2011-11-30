package nc.bs.dm.so;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;
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
public class SoDealBoUtils {
	
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
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-28����08:29:20
	 * @param fisdate :�Ƿ������
	 * @param pk_corp
	 * @param pk_stordoc:����ֿ�
	 * @param bodys
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, StoreInvNumVO> initInvNumInfor(boolean fisdate ,String pk_corp,String pk_stordoc,List<SoDealVO> bodys) throws BusinessException{
		Map<String, StoreInvNumVO> invNumInfor=  new HashMap<String, StoreInvNumVO>();
		if(bodys == null || bodys.size()==0){
			return invNumInfor;
		}
		Set<String> cinvids = new HashSet<String>();// ���ΰ��ŵ����д��id
		StoreInvNumVO tmpNumVO = null;
		String key = null;
		for (SoDealVO body : bodys) {
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(body
					.getCinvbasdocid());
			cinvids.add(body.getCinvbasdocid());
			if (invNumInfor.containsKey(key)) {
				tmpNumVO = invNumInfor.get(key);
			} else {
				tmpNumVO = new StoreInvNumVO();
				tmpNumVO.setPk_corp(pk_corp);
				tmpNumVO.setCstoreid(pk_stordoc);
				tmpNumVO.setCinvbasid(body.getCinvbasdocid());
				tmpNumVO.setCinvmanid(body.getCinventoryid());
				String strWhere= null;
				//��ѯ�����
				if(fisdate){
					strWhere= "ss_pk in('"+WdsWlPubConst.WDS_STORSTATE_PK_hg+"','"+WdsWlPubConst.WDS_STORSTATE_PK_dj+"')";
				}else{
					strWhere= " ss_pk='"+WdsWlPubConst.WDS_STORSTATE_PK+"'";
				}
				UFDouble[]stocknums = getStockBO().getInvStockNum(pk_corp,
						tmpNumVO.getCstoreid(), null,
						tmpNumVO.getCinvbasid(), null, null,strWhere);
				if (stocknums == null || stocknums.length == 0){
					String reason=" ���"
						+ WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
						+ " �޿����";
					if(fisdate){
						reason="�����ڣ�"+reason;
					}else{
						reason="�ϸ񣬴��죺"+reason;
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
			Logger.info("���ΰ��ŵĴ���������Ѱ���δ������");
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
			Logger.info("���ΰ��ŵĴ���������Ѱ���δ������");
			if (invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
		}
		for (String key2 : invNumInfor.keySet()) {
			tmpNumVO = invNumInfor.get(key2);
			UFDouble[] stocknums = invNumInfor1.get(key2);
			UFDouble[] stocknum2 = invNumInfor2.get(key2);
			if (tmpNumVO == null)
				continue;
			// �Ѱ����˵�ռ����
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
			tmpNumVO.setNdealnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
					: stocknums[0]);
			tmpNumVO
					.setNdealassnum(stocknums == null ? WdsWlPubTool.DOUBLE_ZERO
							: stocknums[1]);
			tmpNumVO.setNplannum(nplannum);
			tmpNumVO.setNplanassnum(nplanassnum);
			// ��ǰ������=�����-�Ѿ����ŵ��˵�ռ����
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
					+ " ��ǰ������" + tmpNumVO.getNstockassnum() + " �Ѱ���δ��������"
					+ tmpNumVO.getNdealassnum() + " ���ο�������"
					+ tmpNumVO.getNassnum() + " ���δ�����������"
					+ tmpNumVO.getNplanassnum();
				if(fisdate){
					reason="������"+reason;
				}else{
					reason="�ϸ񣬴���"+reason;
				}
				Logger.info(reason);
				throw new BusinessException(reason);
			}
				
		}
		return invNumInfor;
			
	}
	

}
