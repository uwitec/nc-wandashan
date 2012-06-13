package nc.bo.other.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubConsts;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * 
 * @author Administrator ���������̨��
 * 
 * ҵ�����˵���� �����ⵥ ����ʱ ��Ҫ���� ������ϸ���ӱ����� �� �޸� ���̴�����Ϣ������ ǩ��ʱ ��Ҫ������״̬�������̴������� ���� 0Ϊ����
 * 1Ϊ�л� ɾ������Ϊ0�����̴������� ȡ��ǩ��ʱ ǩ�ֵ������ ɾ��ʱ ɾ������ ɾ��������ϸ���ӱ� �ظ����̴�����Ϣ��Ĵ��� �޸ı���ʱ
 * ����ɾ��ʱ�Ĳ�����Ȼ��ͬ���� *
 * 
 */
public class OtherOutBO {

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private StockInvOnHandBO stockBO = null;
	private StockInvOnHandBO getStockBO(){
		if(stockBO == null)
			stockBO = new StockInvOnHandBO(getBaseDAO());
		return stockBO;
	}

	private TempTableUtil ttutil = null;

	private TempTableUtil getTempTableUtil() {
		if (ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ɾ����������� * �س����̴����� ��д��Դ��������
	 * @ʱ�䣺2011-4-8����03:30:07
	 * @param billVo
	 * @throws BusinessException
	 */
	public void deleteOutBill(MyBillVO billVo) throws BusinessException {
		if (billVo == null) {
			return;
		}
		TbOutgeneralHVO head = (TbOutgeneralHVO)billVo.getParentVO();
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) billVo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;

		// ��д����
		writeBack(billVo, IBDACTION.DELETE, false);

		List<TbOutgeneralTVO> ltray = new ArrayList<TbOutgeneralTVO>();
		for (TbOutgeneralBVO body : bodys) {
			if (body.getTrayInfor() == null)
				continue;
			ltray.addAll(body.getTrayInfor());
		}
		if (ltray.size() <= 0)
			return;
		deleteOtherInforOnDelBill(head.getSrl_pk(),ltray);
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ɽ������Ŀ ��ʱδ���������޸ĺ�Ļ�д ���ⵥ��д ���������д�����˶�����WDS3�����ɹ�ȡ��(WDSC)
	 *             ���۳����д�������˵���WDS5��,erp�������۶���(30)
	 * @ʱ�䣺2011-4-8����06:52:43
	 * @param newBillVo
	 *            Ҫ�����ĵ�������
	 * @param iBdAction
	 *            ����״̬
	 * @param isNew
	 *            �Ƿ���������
	 * @throws Exception
	 */
	public void writeBack(MyBillVO newBillVo, int iBdAction, boolean isNew)
			throws BusinessException {
		if (newBillVo == null || newBillVo.getParentVO() == null
				|| newBillVo.getChildrenVO() == null
				|| newBillVo.getChildrenVO().length == 0)
			return;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) newBillVo.getChildrenVO();
		if (bodys == null || bodys.length == 0)
			return;
		Map<String, UFDouble> numInfor = new HashMap<String, UFDouble>();
		Map<String, UFDouble> numassInfor = new HashMap<String, UFDouble>();

		String sourcetype = null;
		sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
				.getCsourcetype());
		if (sourcetype == null)// ������������۳������Ƶ��������Ҫ��д
			return;
		if (iBdAction == IBDACTION.DELETE) {
			for (TbOutgeneralBVO body : bodys) {
				String key = body.getCsourcebillbid();
				if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
						|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
						|| sourcetype.equalsIgnoreCase("30")
						|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
					if(numInfor.containsKey(key)){
						UFDouble noutnum = numInfor.get(key);
						UFDouble noutassistnum = numassInfor.get(key);
						UFDouble nnewoutnum = PuPubVO
						.getUFDouble_NullAsZero(body.getNoutnum())
						.multiply(-1);
						UFDouble nnewoutassistnum = PuPubVO
						.getUFDouble_NullAsZero(body.getNoutassistnum())
						.multiply(-1);
						noutnum = noutnum.add(nnewoutnum);
						noutassistnum = noutassistnum.add(nnewoutassistnum);
						numInfor.put(key, noutnum);
						numassInfor.put(key, noutassistnum);
					}else{
						numInfor.put(key, PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum())
								.multiply(-1));
						numassInfor.put(key, PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum())
								.multiply(-1));
					}
					
				}
			}
		} else if (iBdAction == IBDACTION.SAVE) {
			if (isNew) {
				for (TbOutgeneralBVO body : bodys) {
					String key = body.getCsourcebillbid();
					if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
							|| sourcetype.equalsIgnoreCase("30")
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
					
						if(numInfor.containsKey(key)){
							UFDouble noutnum = numInfor.get(key);
							UFDouble noutassistnum = numassInfor.get(key);
							UFDouble nnewoutnum = PuPubVO
							.getUFDouble_NullAsZero(body.getNoutnum());
							UFDouble nnewoutassistnum = PuPubVO
							.getUFDouble_NullAsZero(body.getNoutassistnum());
							noutnum = noutnum.add(nnewoutnum);
							noutassistnum = noutassistnum.add(nnewoutassistnum);
							numInfor.put(key, noutnum);
							numassInfor.put(key, noutassistnum);
						}else{
							numInfor.put(key, PuPubVO
									.getUFDouble_NullAsZero(body.getNoutnum()));
							numassInfor.put(key, PuPubVO
									.getUFDouble_NullAsZero(body.getNoutassistnum()));
						}

					}
				}
			} else {
				String sql = "select noutnum,noutassistnum from tb_outgeneral_b where isnull(dr,0)=0 and general_b_pk = ?";
				SQLParameter para = null;
				for (TbOutgeneralBVO body : bodys) {
					String key = body.getCsourcebillbid();
					if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)
							|| sourcetype.equalsIgnoreCase("30")
							|| sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
						if (body.getStatus() == VOStatus.NEW) {
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum());
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum());
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(key, PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum()));
								numassInfor.put(key, PuPubVO
										.getUFDouble_NullAsZero(body.getNoutassistnum()));
							}

						} else if (body.getStatus() == VOStatus.DELETED) {
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum().multiply(-1));
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum().multiply(-1));
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum())
										.multiply(-1));
								numassInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(
												body.getNoutassistnum()).multiply(
												-1));
							}
							

						} else if (body.getStatus() == VOStatus.UPDATED) {
							UFDouble noldoutnum = null;
							UFDouble noutassistnum = null;
							// ȡ��ԭ��������
							para = new SQLParameter();
							para.addParam(body.getPrimaryKey());
							Object o = getBaseDAO().executeQuery(sql, para,
									WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
							if (o != null) {
								ArrayList<Object[]> list = (ArrayList<Object[]>) o;
								if (list.size() == 0) {
									throw new BusinessException("��ȡԭʵ�������쳣");
								}
								Object[] colum = (Object[]) list.get(0);
								noldoutnum = PuPubVO
										.getUFDouble_NullAsZero(colum[0]);
								noutassistnum = PuPubVO
										.getUFDouble_NullAsZero(colum[1]);
							}
							if(numInfor.containsKey(key)){
								UFDouble noutnum = numInfor.get(key);
								UFDouble nouassistnum = numassInfor.get(key);
								UFDouble nnewoutnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutnum().sub(noldoutnum));
								UFDouble nnewoutassistnum = PuPubVO
								.getUFDouble_NullAsZero(body.getNoutassistnum().sub(noldoutnum));
								noutnum = noutnum.add(nnewoutnum);
								nouassistnum = nouassistnum.add(nnewoutassistnum);
								numInfor.put(key, noutnum);
								numassInfor.put(key, nouassistnum);
							}else{
								numInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(body.getNoutnum())
										.sub(noldoutnum));
								numassInfor.put(body.getCsourcebillbid(), PuPubVO
										.getUFDouble_NullAsZero(
												body.getNoutassistnum()).sub(
												noutassistnum));
							}
						}
					}
				}
			}
		}

		if (numInfor.size() > 0) {
			String sql = null;
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
				sql = " update wds_sendorder_b set noutnum=coalesce(noutnum,0)+?,nassoutnum=coalesce(nassoutnum,0)+?"
						+ " where pk_sendorder_b=? ";
			} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
				sql = " update wds_soorder_b set noutnum=coalesce(noutnum,0)+? ,nassoutnum=coalesce(nassoutnum,0)+?"
						+ " where pk_soorder_b=? ";
			} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
				sql = " update wds_cgqy_b set noutnum=coalesce(noutnum,0)+? ,nassoutnum=coalesce(nassoutnum,0)+? "
						+ " where pk_cgqy_b=? ";
			} else if (sourcetype.equalsIgnoreCase("30")) {
				sql = "update so_saleorder_b set "
						+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
						+ " = coalesce("
						+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
						+ ",0)+? "
						+ " nasttaldcnum=coalesce(nasttaldcnum,0)+? where corder_bid=?";
			}
			SQLParameter para = null;
			for (String key : numInfor.keySet()) {
				if (para == null)
					para = new SQLParameter();
				else
					para.clearParams();
				para.addParam(numInfor.get(key));
				para.addParam(numassInfor.get(key));
				para.addParam(key);
				getBaseDAO().executeUpdate(sql, para);
				para.clearParams();
			}

			// ���Ʋ��ܳ���������
			checkNoutNumByOrderNum(sourcetype, numInfor.keySet().toArray(
					new String[0]));
		}
	}

	public void checkNoutNumByOrderNum(String sourcetype, String[] ids)
			throws BusinessException {
		String sql = null;
		if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
			sql = "select count(0) from wds_sendorder_b where (coalesce(ndealnum,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_sendorder_b in "
					+ getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)) {
			sql = "select count(0) from wds_soorder_b where (coalesce(narrangnmu,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_soorder_b in "
					+ getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
			sql = "select count(0) from wds_cgqy_b where (coalesce(nplannum,0) - coalesce(noutnum,0)) < 0 "
					+ "and pk_cgqy_b in " + getTempTableUtil().getSubSql(ids);
		} else if (sourcetype.equalsIgnoreCase("30")) {
			sql = "select count(0) from so_saleorder_b where (coalesce(nnumber,0) - coalesce("
					+ WdsWlPubConst.DM_SO_DEALNUM_FIELD_NAME
					+ ",0)) < 0 "
					+ "and corder_bid in " + getTempTableUtil().getSubSql(ids);
		}
		if (PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql,
				WdsPubResulSetProcesser.COLUMNPROCESSOR), -1) > 0) {
			throw new BusinessException("�����˶������⣬���ʵʵ������");
		}
	}

	public void deleteOtherInforOnDelBill(String cwarehouseid,List<TbOutgeneralTVO> ltray)
			throws BusinessException {
		// �ָ����̴���
		backTrayInforOnDelBill(cwarehouseid,ltray);
		// ɾ������������ϸ��
		if (ltray.size() > 0)// �����޷��ظ� Ӳɾ��
			getBaseDAO().deleteVOList(ltray);
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ⵥʱ�������״̬��
	 * @ʱ�䣺2011-6-28����08:29:05
	 * @param ltray
	 * @throws Exception
	 */
	public void updateStockOnSaveBill(String corp,String warehousid,List<TbOutgeneralTVO> ltray) throws Exception{
		getStockBO().updateStockForOut(corp,warehousid,ltray);
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ݿ��������ϸ���� �ظ� ���̴��� ���� ��浥��ɾ��ʱ
	 * @ʱ�䣺2011-4-8����04:00:16
	 * @param ltray
	 * @throws BusinessException
	 */
	public void backTrayInforOnDelBill(String cwarehouseid,List<TbOutgeneralTVO> ltray)
	throws BusinessException {
		StockInvOnHandVO stock = null;
		if (ltray == null || ltray.size() == 0)
			return;
		List<StockInvOnHandVO> linvOnhand = new ArrayList<StockInvOnHandVO>();
		StockInvOnHandVO[] stocks = null;
		String pk_corp = SQLHelper.getCorpPk();
		for (TbOutgeneralTVO tray : ltray) {
			
			stocks = getStockBO().getStockInvDatas(pk_corp, cwarehouseid, tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getVbatchcode()
			);			

			if (stocks == null || stocks.length == 0) {
				throw new BusinessException("ԭ��Ʒ���̴����Ϣ��ʧ���޷�ɾ�����ⵥ");
			}

			if (stocks.length > 1)
				throw new BusinessException("��ȡԭ��Ʒ���̴����Ϣ�쳣");

			stock = stocks[0];
	  			
			stock.setWhs_stockpieces(PuPubVO.getUFDouble_NullAsZero(
					stock.getWhs_stockpieces()).add(
							PuPubVO.getUFDouble_NullAsZero(tray.getNoutassistnum())));
			stock.setWhs_stocktonnage(PuPubVO.getUFDouble_NullAsZero(
					stock.getWhs_stocktonnage()).add(
							PuPubVO.getUFDouble_NullAsZero(tray.getNoutnum())));
			check(stock);
			
			stock.setWhs_status(0);
			stock.setStatus(VOStatus.UPDATED);
			linvOnhand.add(stock);
			getStockBO().updateBdcargdocTray(tray.getCdt_pk(),StockInvOnHandVO.stock_state_use);
		}
		if (linvOnhand.size() > 0)
			getBaseDAO().updateVOArray(
					linvOnhand.toArray(new StockInvOnHandVO[0]),
					WdsWlPubConsts.stockinvonhand_fieldnames);

	}
    /**
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2012-6-1����11:06:09
     * @param stock
     * 
     * У����ⵥɾ��ʱ  �ָ����̴������Ƿ񳬹����̵�����
     * @throws BusinessException 
     */
	private void check(StockInvOnHandVO stock) throws BusinessException {
	  String pk_invmandoc=stock.getPk_invmandoc();
	  UFDouble uf=PuPubVO.getUFDouble_NullAsZero(stock.getWhs_stockpieces());
	  UFDouble uf1=PuPubVO.getUFDouble_NullAsZero( WdsWlPubTool.execFomular("val->getColValue(wds_invbasdoc,tray_volume,pk_invmandoc,pk_invmandoc)", new String[]{"pk_invmandoc"}, new String[]{pk_invmandoc}));
	  if(uf.doubleValue()>uf1.doubleValue()){
		  throw new BusinessException("���������� ,�޷�ɾ��");
	  }	
	}	
}
