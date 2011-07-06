package nc.bs.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wds.tray.lock.LockTrayBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class IcInPubBO {
	
	private BaseDAO m_dao = null;

	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}
	
	private StockInvOnHandBO stockBO = null;
	private StockInvOnHandBO getStockBO(){
		if(stockBO == null)
			stockBO = new StockInvOnHandBO(getDao());
		return stockBO;
	}
	
		
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ⵥ����ʱ���ɿ�������Ϣ
	 * @ʱ�䣺2011-4-9����07:30:24
	 * @param bodys
	 * @param cargdocPK
	 * @throws BusinessException
	 */
	public void insertInvBatchState(TbGeneralBVO[] bodys,String cargdocPK) throws BusinessException{
		if(bodys == null || bodys.length == 0){
			return;
		}
		List<String> lTrayID = new ArrayList<String>();
		List<StockInvOnHandVO> linvInfor = new ArrayList<StockInvOnHandVO>();
		List<TbGeneralBBVO> ltray = null;
		StockInvOnHandVO tmpInvVO = null;
		for(TbGeneralBVO body:bodys){
			ltray = body.getTrayInfor();
			if(ltray == null || ltray.size() == 0)
				throw new BusinessException("��"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getGeb_crowno())+"�޴��������Ϣ");
			for(TbGeneralBBVO tray:ltray){
				tmpInvVO = new StockInvOnHandVO();
				tmpInvVO.setPk_corp(SQLHelper.getCorpPk());
				tmpInvVO.setPk_cargdoc(cargdocPK);//��λid
				// ��������
				tmpInvVO.setPplpt_pk(tray.getCdt_pk());
				if(!lTrayID.contains(tray.getCdt_pk())){
					lTrayID.add(tray.getCdt_pk());
				}				
				//�ֿ�
				tmpInvVO.setPk_customize1(getStroreByCargdoc(cargdocPK));
				// �����������
				tmpInvVO.setPk_invbasdoc(tray.getPk_invbasdoc());
				tmpInvVO.setPk_invmandoc(tray.getPk_invmandoc());
				//��������
				tmpInvVO.setCreadate(tray.getCreadate());
			    //ʧЧ����
				tmpInvVO.setExpdate(tray.getExpdate());
				// ���κ�
				tmpInvVO.setWhs_batchcode(tray.getGebb_vbatchcode());
				// ��д���κ�
				if (null != tray.getGebb_lvbatchcode()
						&& !"".equals(tray.getGebb_lvbatchcode())) {
					tmpInvVO.setWhs_lbatchcode(tray.getGebb_lvbatchcode());
				} else {
					tmpInvVO.setWhs_lbatchcode(WdsWlPubConst.ERP_BANCHCODE);
				}
				tmpInvVO.setWhs_munit(tray.getUnitid());//��������λ
				tmpInvVO.setWhs_aunit(tray.getAunit());//��������λ
				tmpInvVO.setWhs_hsl(tray.getGebb_hsl());//������
				// dr
				tmpInvVO.setDr(0);
				tmpInvVO.setWhs_stockpieces(tray.getNinassistnum());////��渨����
				tmpInvVO.setWhs_oanum(tray.getNinassistnum());//ԭʼ���ʵ�ո�����
				tmpInvVO.setWhs_stocktonnage(tray.getGebb_num());////���������
				tmpInvVO.setWhs_omnum(tray.getGebb_num());////ԭʼ���������
				// ������
				tmpInvVO.setWhs_hsl(tray.getGebb_hsl());
				// ����
				tmpInvVO.setWhs_nprice(tray.getGebb_nprice());
				// ���
				tmpInvVO.setWhs_nmny(tray.getGebb_nmny());
				
				// �����״̬(Ĭ�Ϻ���)
				tmpInvVO.setSs_pk(WdsWlPubConst.default_inv_state);
				// ����״̬
				tmpInvVO.setWhs_status(0);
				// ����
				tmpInvVO.setWhs_type(1);
				tmpInvVO.setPk_headsource(tray.getGeb_pk());//��ⵥ�ӱ�id				
				// ��Դ���ݱ��������� ���������
				tmpInvVO.setPk_bodysource(tray.getGebb_pk());//��ⵥ���ӱ�id
				linvInfor.add(tmpInvVO);
			}
		}
		if(linvInfor.size()>0){		
			try {
				getStockBO().inserStockForIn(linvInfor);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e instanceof BusinessException)
					throw (BusinessException)e;
				else
					throw new BusinessException(e);
			}
		}
		//��������״̬
		if(lTrayID.size()>0){
			getStockBO().updateTrayState(StockInvOnHandVO.stock_state_use, lTrayID,getTempTableUtil());
		}
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵����ͨ����λ�õ��ֿ���Ϣ
	 * @ʱ�䣺2011-4-8����06:52:43
	 * @param newBillVo Ҫ�����ĵ�������
	 * @param iBdAction ����״̬
	 * @param isNew  �Ƿ���������
	 * @throws Exception
	 */
	private String getStroreByCargdoc(String cargdoc) throws DAOException{
		
		String sql=" select pk_stordoc from wds_cargdoc c where c.pk_cargdoc='"+cargdoc+"' and isnull(c.dr,0)=0";
		Object pk_storedoc=getDao().executeQuery(sql,new  ColumnProcessor());
		if(pk_storedoc !=null && !pk_storedoc.equals("") ){
			return (String)pk_storedoc;
		}
		return null;
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ���ɽ������Ŀ 
	 *   ������Ᵽ���д��ERP��������
	 *   ������Ᵽ���д��ERP��������
	 * @ʱ�䣺2011-4-8����06:52:43
	 * @param newBillVo Ҫ�����ĵ�������
	 * @param iBdAction ����״̬
	 * @param isNew  �Ƿ���������
	 * @throws Exception
	 */
	public void writeBackForInBill(OtherInBillVO newBillVo,int  iBdAction,boolean isNew)
			throws BusinessException {
		if(newBillVo == null || newBillVo.getParentVO() == null 
				||newBillVo.getChildrenVO() == null 
				|| newBillVo.getChildrenVO().length==0)
			return;
		TbGeneralBVO[] bodys = (TbGeneralBVO[])newBillVo.getChildrenVO();
		Map<String, UFDouble> numInfor = new HashMap<String, UFDouble>();
		Map<String, UFDouble> numassInfor = new HashMap<String, UFDouble>();
		String sourcetype = null;
		String csourbillbid= null;
		sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype());//һ�ŵ�����ֻ��һ����Դ
		if(sourcetype==null)
			return;
		if(iBdAction == IBDACTION.DELETE){
			for(TbGeneralBVO body:bodys){
				csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
				if(csourbillbid == null)
					continue;
				if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
						numInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).multiply(-1)); 
						numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).multiply(-1)); 

				}
			}
		}else if(iBdAction == IBDACTION.SAVE){
			if(isNew){
				for(TbGeneralBVO body:bodys){
					csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
					if(csourbillbid == null)
						continue;
					if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
							numInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum())); 
							numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum())); 
					}
				}
			}else{
				String sql = "select geb_anum,geb_banum from tb_general_b where isnull(dr,0)=0 and geb_pk = ?";
				UFDouble noldoutnum = null;
				UFDouble noutassistnum = null;
				SQLParameter para = null;
				for(TbGeneralBVO body:bodys){
					csourbillbid = PuPubVO.getString_TrimZeroLenAsNull(body.getCsourcebillbid());
					if(csourbillbid == null)
						continue;
					if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)||ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){		
						if( VOStatus.DELETED==body.getStatus()){
							numInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).multiply(-1));
							numassInfor.put(body.getCsourcebillbid(),PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).multiply(-1)); 
						}else if(VOStatus.UPDATED== body.getStatus()){
							//ȡ��ԭ��������
							if(para == null)
								para = new SQLParameter();
							else
								para.clearParams();
							para.addParam(body.getPrimaryKey());
							Object o =getDao().executeQuery(sql,para, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
							if( o != null){
								ArrayList<Object[]>  list=(ArrayList<Object[]>) o;
								if(list.size() == 0){
									throw new BusinessException("��ȡԭʵ�������쳣");
								}
								Object[] colum =(Object[]) list.get(0);
								 noldoutnum =PuPubVO.getUFDouble_NullAsZero(colum[0]);
								 noutassistnum =PuPubVO.getUFDouble_NullAsZero(colum[1]);
							}	
							
							numInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_anum()).sub(noldoutnum));
							numassInfor.put(body.getCsourcebillbid(), 
									PuPubVO.getUFDouble_NullAsZero(body.getGeb_banum()).sub(noutassistnum));
						}
					}
				}
			}
		}
			if(numInfor.size()>0){
				String sql = null;
				if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)){		
					sql = " update tb_outgeneral_b set nacceptnum=coalesce(nacceptnum,0)+? ,nassacceptnum=coalesce(nassacceptnum,0)+?" +
					" where general_b_pk=? ";
				}else if(ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){
					sql = " update ic_general_b set ntranoutnum = coalesce(ntranoutnum,0)+? ,ntranoutastnum=coalesce(ntranoutastnum,0)+? where cgeneralbid = ?";
				}
				//				BaseDAO dao = new BaseDAO();
				SQLParameter para = null;
				for(String key:numInfor.keySet()){
					if(para == null)
						para = new SQLParameter();
					else
						para.clearParams();
					para.addParam(numInfor.get(key));
					para.addParam(numassInfor.get(key));
					para.addParam(key);
					getDao().executeUpdate(sql, para);
					para.clearParams();
				}			
	
				//			���Ʋ��ܳ���������
				checkNoutNumByOrderNum(sourcetype, numInfor.keySet().toArray(new String[0]));
			}
		}
	
	public void checkNoutNumByOrderNum(String sourcetype,String[] ids) throws BusinessException{
		String sql = null;
		if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)){		
			sql = "select count(0) from tb_outgeneral_b where (coalesce(noutnum,0) - coalesce(nacceptnum,0)) < 0 " +
			" and general_b_pk in "+getTempTableUtil().getSubSql(ids);
		}else if(ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)){
			sql = "select count(0) from ic_general_b where (coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0)) < 0 "+
			" and cgeneralbid in "+getTempTableUtil().getSubSql(ids);
		}
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1)>0){
			throw new BusinessException("ʵ������������Դ���ݷ�������");
		}
	}

	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/**
	 * ��ⵥɾ��ʱ ����У��  �Ƿ��ɾ��  �������  �������״̬   ���������Ϊ�� ��ɾ��������¼
	 */
	public List<TbGeneralBBVO> checkOnDelBill(String inbillid,TbGeneralBVO[] bvos)throws BusinessException, DAOException{
		if(PuPubVO.getString_TrimZeroLenAsNull(inbillid)==null)
			return null;
		if(bvos == null || bvos.length == 0)
			return null;
		List<TbGeneralBBVO> ltray = null;//������ϸ����
		if(bvos[0].getTrayInfor() == null || bvos[0].getTrayInfor().size() == 0){
//			û��������ϸ��Ϣ�Ļ�  �� ���ݿ��ȡ
			String whereSql = " isnull(dr,0)=0 and geh_pk = '"+inbillid+"'";
		    ltray = (List<TbGeneralBBVO>)getDao().retrieveByClause(TbGeneralBBVO.class, whereSql);
		}else{
			for(TbGeneralBVO bvo:bvos){
				if(ltray == null){
					ltray = new ArrayList<TbGeneralBBVO>();
				}
				if(bvo.getTrayInfor() == null || bvo.getTrayInfor().size() ==0)
					throw new ValidationException("�����쳣����ȡ������ϸ��ϢΪ��");
				ltray.addAll(bvo.getTrayInfor());
			}
		}
		
		if(ltray == null || ltray.size() == 0){
			throw new ValidationException("�����쳣����ȡ������ϸ��ϢΪ��");
		}
		String pk_corp = SQLHelper.getCorpPk();
		for(TbGeneralBBVO tray:ltray){
//			�����Ƕ��� �ܲ�ʵ������  �������� ���Ƿֲ�  ��У�鶼������
			getStockBO().checkOnHandNum(pk_corp, null,tray.getPk_cargdoc(), tray.getCdt_pk(), tray.getPk_invbasdoc(), tray.getGebb_vbatchcode(), tray.getNinassistnum());
		}
		return ltray;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������������  �ظ�����  ��ҪУ��������������� �ѱ������� �Ͳ���ɾ����
	 * @ʱ�䣺2011-4-9����07:50:28
	 * @param ltrayid
	 * @param inbillid
	 * @throws BusinessException
	 */
	public void deleteOtherInforOnDelBill(String inbillid,TbGeneralBVO[] bvos) throws BusinessException{
//		zhf   modify 20110627   ֧���������̺��㷨����
		
//		���̴���У��   ������ �Ƿ��Ѿ�  ������  ����� ����� ��ⵥ �����ٴ�����
		List<TbGeneralBBVO> ltray = checkOnDelBill(inbillid, bvos);
//		��������״̬  ������ܲ�ʵ������  ״̬����Ϊ δռ��    �������� �� �ֲ�����  ״̬ ������ά��
		if(ltray!=null && ltray.size()>0){
			try {
				getStockBO().updateStockOnDelForIn(ltray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e instanceof BusinessException)
					throw (BusinessException)e;
				else
					throw new BusinessException(e);
			}			
			
//			������������  ���Ļ�  У���Ƿ� ���� ʵ������ �����  ���н���
			
			List<String> lbbid = new ArrayList<String>();
			for(TbGeneralBBVO bb:ltray){
				lbbid.add(bb.getPrimaryKey());
			}
			LockTrayBO lockbo = new LockTrayBO(getDao(),getStockBO());
			lockbo.reLockTray(lbbid.toArray(new String[0]));
		}
		
//		ɾ��������ϸ��ˮ��
		if(bvos!=null && bvos.length>0){
			for(TbGeneralBVO bvo:bvos){
				String bodyid = bvo.getGeb_pk();
				getDao().deleteByClause(TbGeneralBBVO.class, " geb_pk = '"+bodyid+"'");
			}
		}
		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ֻ�����ڶ� �ܲ� ʵ�����̴�����У��  ��ʷ�汾 ����zhf2011-06027
	 * @ʱ�䣺2011-4-11����01:38:05
	 * @param ltrayid ������Ϣ
	 * @param nassnum 
	 * @throws BusinessException
	 */
	public void checkOnHandNum(List<String> ltrayid,UFDouble noutassnum) throws BusinessException{
		List<StockInvOnHandVO> linv = (List<StockInvOnHandVO>)getDao().retrieveByClause(StockInvOnHandVO.class, " pplpt_pk in "+getTempTableUtil().getSubSql(ltrayid.toArray(new String[0])));
		if(linv == null || linv.size() == 0){
			throw new BusinessException("��λ��������");
		}
		UFDouble naAllAssNum = WdsWlPubTool.DOUBLE_ZERO;
		for(StockInvOnHandVO inv:linv){
			naAllAssNum = naAllAssNum.add(PuPubVO.getUFDouble_NullAsZero(inv.getWhs_stockpieces()));////��渨����
		}
		if(noutassnum.doubleValue()>naAllAssNum.doubleValue())
			throw new  BusinessException("��λ��������");
	}
}
