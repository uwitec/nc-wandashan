package nc.bs.wds.ic.stock;

import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class StockInvOnHandBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	
	public StockInvOnHandBO(BaseDAO dao2){
		super();
		dao = dao2;
	}
	public StockInvOnHandBO(){
		super();
//		dao = dao2;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ȡ���״̬����
	 * @ʱ�䣺2011-6-28����04:20:27
	 * @param corp ��˾
	 * @param cwarehouseid �ֿ�
	 * @param pk_cargdoc  ��λ
	 * @param ctrayid ����
	 * @param cinvbasid ���id
	 * @param vbatchcode ����
	 * @return
	 * @throws BusinessException
	 */
	public StockInvOnHandVO[] getStockInvDatas(String corp,String cwarehouseid,
			String pk_cargdoc,String ctrayid,String cinvbasid,String vbatchcode) throws BusinessException{
		StringBuffer whereSql = new StringBuffer();
		if(PuPubVO.getString_TrimZeroLenAsNull(pk_cargdoc)==null){
			throw new BusinessException("��λ����Ϊ��");
		}
		whereSql.append(" isnull(dr,0)=0 and pk_cargdoc = '"+pk_cargdoc+"' and pk_corp = '"+corp+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(cwarehouseid)!=null)
			whereSql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(ctrayid)!=null){
			whereSql.append(" and pplpt_pk = '"+ctrayid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(cinvbasid)!=null){
			whereSql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(vbatchcode)!=null){
			whereSql.append(" and whs_batchcode = '"+vbatchcode+"'");
		}

		List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>)getDao().retrieveByClause(StockInvOnHandVO.class, whereSql.toString());
		if(linv == null||linv.size() ==0)
			return null;
		return linv.toArray(new StockInvOnHandVO[0]);

	}
	
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ����У��   �����Ƕ��� �ܲ�ʵ������  �������� ���Ƿֲ�  ��У�鶼������
	 * @ʱ�䣺2011-6-28����03:10:47
	 * @param corp ��˾
	 * @param pk_cargdoc  ��λ
	 * @param ctrayid ����
	 * @param cinvbasid ���
	 * @param vbatchcode ����
	 * @param nassnum ����   ������
	 * @throws BusinessException
	 */
	public void checkOnHandNum(String corp,String cwarehouseid,String pk_cargdoc,String ctrayid,String cinvbasid,String vbatchcode,UFDouble nassnum) throws BusinessException{

		StockInvOnHandVO[] stocks = getStockInvDatas(corp, cwarehouseid, pk_cargdoc, ctrayid, cinvbasid, vbatchcode);
		
		if(stocks == null || stocks.length == 0){
			throw new BusinessException("��λ��������");
		}
		UFDouble naAllAssNum = WdsWlPubTool.DOUBLE_ZERO;
		for(StockInvOnHandVO stock:stocks){
			naAllAssNum = naAllAssNum.add(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces()));
		}
		if(nassnum.doubleValue()>naAllAssNum.doubleValue())
			throw new  BusinessException("��λ��������");
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��������ˮ��Ϣ�� ���� ������״̬��  ����  ������ⵥʱ
	 * @ʱ�䣺2011-4-7����08:39:17
	 * @param ltray
	 * @throws Exception
	 */
	public  void updateStockOnDelForIn(List<TbGeneralBBVO> ltray) throws Exception {
		// TODO Auto-generated method stub
		if (null == ltray || ltray.size() == 0) {			
			return;
		}
		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ������
		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ��������
//		List linvhand = null;
		StockInvOnHandVO[] stocks = null;
		String pk_corp = SQLHelper.getCorpPk();
		for (TbGeneralBBVO tray:ltray) {				
			noutassistnum = tray.getNinassistnum();
			noutnum = tray.getGebb_num();				

			stocks = getStockInvDatas(pk_corp, null, tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getGebb_vbatchcode());
			// �жϽ�����Ƿ�Ϊ��
			//				if (null != linvhand && generaltList.size() > 0) {
			if(stocks == null || stocks.length == 0)
				return;
			if(stocks.length > 1){
				throw new BusinessException("��ȡ���״̬�����쳣");
			}

			StockInvOnHandVO item = stocks[0];
			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage());
			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces());
			if (noutassistnum.equals(nhandassnum)							
					&& noutnum.equals(nhandnum)){
				item.setWhs_status(1);//------------�������Ϊ  1  ��  ��������  ��  �ֲ���˵  ������
				updateBdcargdocTray(item.getPplpt_pk(),StockInvOnHandVO.stock_state_null);//������״̬����Ϊ  ����  
			}
			item.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
			item.setWhs_stocktonnage(nhandnum.sub(noutnum));
			item.setStatus(VOStatus.UPDATED);
			this.updateWarehousestock(item);
		}

	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݳ�����ˮ��Ϣ�� ���� ������״̬��
	 * @ʱ�䣺2011-4-7����08:39:17
	 * @param ltray
	 * @throws Exception
	 */
	public  void updateStockForOut(String corp,String warehousid,List<TbOutgeneralTVO> ltray) throws Exception {
		// TODO Auto-generated method stub
		if (null == ltray || ltray.size() == 0) {			
			return;
		}
		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ������
		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ��������
        StockInvOnHandVO[] stocks = null;
		for (TbOutgeneralTVO tray:ltray) {				
			noutassistnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum());
			noutnum = PuPubVO.getUFDouble_NullAsZero(tray.getNoutnum());				
			stocks = getStockInvDatas(corp, warehousid, tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getVbatchcode());
			if(stocks == null || stocks.length == 0)
				throw new BusinessException("��������");

			StockInvOnHandVO stock = stocks[0];
			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stocktonnage());
			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces());
			if (noutassistnum.equals(nhandassnum)							
					&& noutnum.equals(nhandnum)){
				stock.setWhs_status(1);
				updateBdcargdocTray(stock.getPplpt_pk(),StockInvOnHandVO.stock_state_null);//������״̬����Ϊ  ����  
			}
			stock.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
			stock.setWhs_stocktonnage(nhandnum.sub(noutnum));
			stock.setStatus(VOStatus.UPDATED);
			this.updateWarehousestock(stock);
		}
	}
	
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�����ִ�����Ҫ�� �ֿ⣬ ��λ�� ���̣���������Σ� ���ά�ȱ���Ψһ 
	          ���� ÿ��Ҫ���Ļ��� ��Ҫ�����ά�Ȳ�ѯ �Ѵ��ڵ����ά�ȵ��ִ���
	          ���������Ļ����ִ����ϲ��γ�һ����¼
	 * @ʱ�䣺2011-4-8����06:52:43
	 * @param newBillVo Ҫ�����ĵ�������
	 * @param iBdAction ����״̬
	 * @param isNew  �Ƿ���������
	 * @throws Exception
	 */
	public void inserStockForIn(List<StockInvOnHandVO> linvInfor) throws Exception {		
		StockInvOnHandVO[] tmps = null;
		for(StockInvOnHandVO stock:linvInfor){
			tmps = getStockInvDatas(stock.getPk_corp(), stock.getPk_customize1()
					, stock.getPk_cargdoc(), stock.getPplpt_pk(), stock.getPk_invbasdoc(), stock.getWhs_batchcode());
			if(tmps == null || tmps.length ==0){
				getDao().insertVO(stock);
			}else if(tmps.length == 1){
				stock.setWhs_oanum(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_oanum()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_oanum())));
				stock.setWhs_omnum(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_omnum()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_omnum())));
				stock.setWhs_stockpieces(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_stockpieces())));
				stock.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stocktonnage()).add(PuPubVO.getUFDouble_NullAsZero(tmps[0].getWhs_stocktonnage())));
				updateWarehousestock(stock);
			}else
				throw new BusinessException("��ȡ���״̬�쳣");
		}		
	}
	
	/**
	 * ���¿���
	 */
	public void updateWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			if(PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces()).doubleValue()<0|| PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage()).doubleValue()<0){
				throw new BusinessException("���ָ����");
			}
//			this.getIvo().updateVO(item);
			getDao().updateVO(item);
		}
	}
	
	// ��������״̬
	public void updateBdcargdocTray(String trayPK,int state) throws BusinessException {
		if(PuPubVO.getString_TrimZeroLenAsNull(trayPK)==null)
			return;
		String sql = "update bd_cargdoc_tray set cdt_traystatus = " + state
		+ " where cdt_pk='" + trayPK + "'";
		if(state == StockInvOnHandVO.stock_state_null){//��������������Զ������Ϊ ��
			sql = sql + " and cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
		}
		getDao().executeUpdate(sql);
	}
	

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��������״̬
	 * @ʱ�䣺2011-4-9����08:39:44
	 * @param state
	 * @param ltrayid
	 * @throws BusinessException
	 */
	public void updateTrayState(int state,List<String> ltrayid,TempTableUtil tempUtil) throws BusinessException{
		String sql = "update bd_cargdoc_tray set cdt_traystatus = "+state+" where cdt_pk in "+tempUtil.getSubSql(ltrayid.toArray(new String[0]));
		if(state == StockInvOnHandVO.stock_state_null){//��������������Զ������Ϊ ��
			sql = sql + " and cdt_traycode not like '"+WdsWlPubConst.XN_CARGDOC_TRAY_NAME+"%'";
		}
		getDao().executeUpdate(sql);
	}

}
