package nc.bs.wds.w8004040204;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.itf.wds.w80060604.Iw80060604;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.CommonUnit;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/***
 * 
 * @author Administrator
 *  �����Զ����
 */
public class W8004040204Impl implements Iw8004040204 {

	// ��ȡ���ݿ���ʶ���
	IVOPersistence ivo = null;
	// ���ݿ��ѯ����
	IUAPQueryBS iuap = null;

	public void deleteGeneralTVO(List<TbOutgeneralTVO> itemList)
			throws Exception {
		// TODO Auto-generated method stub
		// �жϲ����Ƿ�Ϊ��
		if (null != itemList && itemList.size() > 0) {
			// ��ִ��ɾ�� ��ԭ�е�����ɾ����
			this.getIvo().deleteVOList(itemList);
		}
	}
	private BaseDAO m_dao = null;
	public BaseDAO getDao() {
		if (m_dao == null) {
			m_dao = new BaseDAO();
		}
		return m_dao;
	}



	/**
	 * �������ָ����Ϣ
	 */
	public void insertGeneralTVO(List<TbOutgeneralTVO> itemList,
			List<TbOutgeneralTVO> itemList2) throws Exception {
		this.deleteGeneralTVO(itemList);
		if (null != itemList2 && itemList2.size() > 0) {
			// �����²���
			this.getIvo().insertVOList(itemList2);
		}
	}

	/**
	 * ��ӿ����Ϣ
	 */
	public void insertWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			this.getIvo().insertVO(item);
		}
	}

	public List queryGeneralTVO(String cinventoryid, String cfirstbillhid,
			String cfirstbillbid) throws Exception {
		// TODO Auto-generated method stub
		String sWhere = " dr = 0 and cfirstbillhid = '" + cfirstbillhid
				+ "' and pk_invbasdoc ='" + cinventoryid
				+ "' and cfirstbillbid='" + cfirstbillbid + "'";
		// �������ݿ�õ������
		ArrayList generaltList = (ArrayList) this.getIuap().retrieveByClause(
				TbOutgeneralTVO.class, sWhere);
		// �жϽ�����Ƿ�Ϊ��
		if (null != generaltList && generaltList.size() > 0) {
			return generaltList;
		}
		return null;
	}

//	/**
//	 * 
//	 * @���ߣ�zhf
//	 * @˵�������ɽ������Ŀ ���ݳ�����ˮ��Ϣ�� ���� ������״̬��
//	 * @ʱ�䣺2011-4-7����08:39:17
//	 * @param ltray
//	 * @throws Exception
//	 */
//	public  void updateWarehousestock(List<TbOutgeneralTVO> ltray) throws Exception {
//		// TODO Auto-generated method stub
//		if (null == ltray || ltray.size() == 0) {			
//			return;
//		}
//		UFDouble noutnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ������
//		UFDouble noutassistnum = WdsWlPubTool.DOUBLE_ZERO; // ʵ��������
////		int len = ltray.size();
////		int index = 0;
//		ArrayList linvhand = null;
//		for (TbOutgeneralTVO tray:ltray) {				
//			noutassistnum = tray.getNoutassistnum();
//			noutnum = tray.getNoutnum();				
//
//			//			if (index == len - 1) {
//			String sWhere = " isnull(dr,0) = 0 and whs_status = 0 and pplpt_pk = '"
//				+ tray.getCdt_pk()
//				+ "' and pk_invbasdoc = '"
//				+ tray.getPk_invbasdoc() + "' and whs_batchcode = '"+tray.getVbatchcode()+"'";
//			// �������ݿ�õ������
//			linvhand = (ArrayList) getIuap()
//			.retrieveByClause(StockInvOnHandVO.class, sWhere);
//			// �жϽ�����Ƿ�Ϊ��
//			//				if (null != linvhand && generaltList.size() > 0) {
//			if(linvhand == null || linvhand.size() == 0)
//				return;
//
//			StockInvOnHandVO item = (StockInvOnHandVO) linvhand.get(0);
//			UFDouble nhandnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage());
//			UFDouble nhandassnum = PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces());
//			if (noutassistnum.equals(nhandassnum)							
//					&& noutnum.equals(nhandnum)){
//				item.setWhs_status(1);
//				updateBdcargdocTray(item.getPplpt_pk());//������״̬����Ϊ  ����  
//			}
//			item.setWhs_stockpieces(nhandassnum.sub(noutassistnum));
//			item.setWhs_stocktonnage(nhandnum.sub(noutnum));
//			item.setStatus(VOStatus.UPDATED);
//			this.updateWarehousestock(item);
//		}
//
//	}

	/**
	 * ���¿���
	 */
	public void updateWarehousestock(StockInvOnHandVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			if(PuPubVO.getUFDouble_NullAsZero(item.getWhs_stockpieces()).doubleValue()<0|| PuPubVO.getUFDouble_NullAsZero(item.getWhs_stocktonnage()).doubleValue()<0){
				throw new BusinessException("���ָ����");
			}
			this.getIvo().updateVO(item);
		}
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}
	public IUAPQueryBS getIuap() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	// ��������״̬
	public void updateBdcargdocTray(String trayPK) throws Exception {
		// TODO Auto-generated method stub
		if (null != trayPK && !"".equals(trayPK)) {
			PersistenceManager sessioManager = null;
			try {
				sessioManager = PersistenceManager.getInstance();
				JdbcSession jdbcSession = sessioManager.getJdbcSession();

				String sql = "update bd_cargdoc_tray set cdt_traystatus = " + 0
						+ " where cdt_pk='" + trayPK + "'";
				jdbcSession.executeUpdate(sql);

			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/*
	 * ������Ʒ���������Զ�ȡ��(non-Javadoc)--����ʹ�ã���Ϊ֮ǰEJB������Ҫ�õ�������������б���
	 * 
	 * @see nc.itf.wds.w8004040204.Iw8004040204#autoPickAction(java.lang.String)
	 */
	public Map<String,List<TbOutgeneralTVO>> autoPickAction(String pk_user, TbOutgeneralBVO[] bodys,
			String pk_stordoc) throws Exception {
		if(bodys == null || bodys.length == 0)
			return null;
		TbOutgeneralTVO trayVo = null;
		List<StockInvOnHandVO> lware = null; 
		Map<String,List<TbOutgeneralTVO>> trayInfor= new HashMap<String,List<TbOutgeneralTVO>>();//<�кţ�����>
		Map<String,UFDouble> usedNum  = new HashMap<String,UFDouble> ();//��ǰ�������ȳ��Ļ���<���״̬״̬����������ǰ�����Ѿ���������>
		List<TbOutgeneralTVO> ltray = null;//����ʹ�õ�����
		for(TbOutgeneralBVO body:bodys){
			if(body == null)
				throw new BusinessException("�Ƿ���������");
			String oldCdt= getOldCdt(body);
			lware = CommonUnit.getStockDetailByPk_User(pk_user,null,body,oldCdt);//��õ�ǰ���õĴ��
			if(lware == null||lware.size()==0)
				throw new BusinessException("�кţ�"+body.getCrowno()+",��Ʒ�޴���");
			UFDouble asum = PuPubVO.getUFDouble_NullAsZero(body.getNshouldoutassistnum());//Ӧ��������
			StockInvOnHandVO warevo = null;
			for (int j = 0; j < lware.size(); j++) {
				warevo = lware.get(j);
				//��ȥ֮ǰ�ı������Ѿ�ʹ�õ�����
				if(usedNum.containsKey(warevo.getWhs_pk())){
					UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces());
					warevo.setWhs_stockpieces(oldnum.sub(usedNum.get(warevo.getWhs_pk())));
				}
				//�ж��Ƿ��п�渨����
				if(PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces()).doubleValue()<=0)
					continue;
				trayVo = new TbOutgeneralTVO();
				UFDouble tmpsum = asum;
				// ���������ȥӦ������
				asum = warevo.getWhs_stockpieces().sub(asum);
				// ���ʣ���������ڵ���0 ��ǰ���̴��������
				if (asum.doubleValue()> WdsWlPubTool.DOUBLE_ZERO.doubleValue()) {
					// �ѵ�ǰ�������洢ʵ������
					trayVo.setNoutassistnum(tmpsum);
					// ����ʵ��������
					trayVo.setNoutnum(tmpsum.multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), tmpsum);
				} else {
					// ���ʣ������С��0 ��ǰ���̴��������������ǰ���̴����Ϊ������
					trayVo.setNoutassistnum(warevo
							.getWhs_stockpieces());
					trayVo.setNoutnum(trayVo.getNoutassistnum().multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), warevo.getWhs_stockpieces());
				}
				trayVo
				.setCfirstbillhid(body
						.getCsourcebillhid()); // Դͷ����
				// Դͷ�ӱ�
				trayVo
				.setCfirstbillbid(body
						.getCsourcebillbid());
				trayVo.setVsourcebillcode(body
						.getVsourcebillcode()); // ��Դ���ݺ�
				trayVo.setCdt_pk(warevo
						.getPplpt_pk()); // ��������
//				usedTary.add(warevo.getPplpt_pk());
				trayVo.setStockpieces(warevo
						.getWhs_stockpieces()); // �������
				trayVo.setStocktonnage(warevo
						.getWhs_stocktonnage()); // ��渨����
				trayVo.setPk_invbasdoc(warevo
						.getPk_invbasdoc()); // �������id
				trayVo.setPk_invmandoc(warevo.getPk_invmandoc());// �������id
				trayVo.setVbatchcode(warevo
						.getWhs_batchcode()); // ����
				trayVo.setWhs_pk(warevo
						.getWhs_pk()); // ��������
				trayVo.setLvbatchcode(warevo
						.getWhs_lbatchcode()); // ��Դ����
				trayVo.setNprice(warevo
						.getWhs_nprice()); // ����
				trayVo.setNmny(warevo
						.getWhs_nmny()); // ���
				trayVo.setDr(0); // ɾ����־
				
				trayVo.setAunit(body.getCastunitid());//����λ
				trayVo.setUnitid(body.getUnitid());//����λ
				trayVo.setPk_cargdoc(body.getCspaceid());//��λ
				trayVo.setHsl(body.getHsl());// ����
				
	
			
				// ��������
//				body.addTray(trayVo);
				if(trayInfor.containsKey(body.getCrowno())){
					ltray = trayInfor.get(body.getCrowno());
				}else
					ltray = new ArrayList<TbOutgeneralTVO>();
				ltray.add(trayVo);
				trayInfor.put(body.getCrowno(), ltray);
				if (asum.doubleValue() >= 0) 
					break;
				asum = PuPubVO.getUFDouble_NullAsZero(Math.abs(asum.doubleValue()));
			}
		}
		return trayInfor;
	}
	public Map<String,List<TbOutgeneralTVO>> autoPickAction(String pk_user, AggregatedValueObject bill,
			String pk_stordoc) throws Exception {
		if(bill == null || bill.getChildrenVO() ==null || bill.getChildrenVO().length ==0){
			return null;
		}
		TbOutgeneralHVO head =(TbOutgeneralHVO) bill.getParentVO();
		TbOutgeneralBVO[] bodys =(TbOutgeneralBVO[]) bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			return null;
		TbOutgeneralTVO trayVo = null;
		List<StockInvOnHandVO> lware = null; 
		Map<String,List<TbOutgeneralTVO>> trayInfor= new HashMap<String,List<TbOutgeneralTVO>>();//<�кţ�����>
		Map<String,UFDouble> usedNum  = new HashMap<String,UFDouble> ();//��ǰ�������ȳ��Ļ���<���״̬״̬����������ǰ�����Ѿ���������>
		List<TbOutgeneralTVO> ltray = null;//����ʹ�õ�����
		for(TbOutgeneralBVO body:bodys){
			if(body == null)
				throw new BusinessException("�Ƿ���������");
			String oldCdt= getOldCdt(body);
			//��÷����Զ����Ҫ��Ĵ���Ĵ�����Ϣ
			lware = CommonUnit.getStockDetailByPk_User(pk_user,head,body,oldCdt);
			if(lware == null||lware.size()==0)
				throw new BusinessException("�кţ�"+body.getCrowno()+",��Ʒ�޴������������");
			UFDouble asum = PuPubVO.getUFDouble_NullAsZero(body.getNshouldoutassistnum());//Ӧ��������
			StockInvOnHandVO warevo = null;
			for (int j = 0; j < lware.size(); j++) {
				warevo = lware.get(j);
				//��ȥ֮ǰ�ı������Ѿ�ʹ�õ�����
				if(usedNum.containsKey(warevo.getWhs_pk())){
					UFDouble oldnum = PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces());
					warevo.setWhs_stockpieces(oldnum.sub(usedNum.get(warevo.getWhs_pk())));
				}
				//�ж��Ƿ��п�渨����
				if(PuPubVO.getUFDouble_NullAsZero(warevo.getWhs_stockpieces()).doubleValue()<=0)
					continue;
				trayVo = new TbOutgeneralTVO();
				UFDouble tmpsum = asum;
				// ���������ȥӦ������
				asum = warevo.getWhs_stockpieces().sub(asum);
				// ���ʣ���������ڵ���0 ��ǰ���̴��������
				if (asum.doubleValue()> WdsWlPubTool.DOUBLE_ZERO.doubleValue()) {
					// �ѵ�ǰ�������洢ʵ������
					trayVo.setNoutassistnum(tmpsum);
					// ����ʵ��������
					trayVo.setNoutnum(tmpsum.multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), tmpsum);
				} else {
					// ���ʣ������С��0 ��ǰ���̴��������������ǰ���̴����Ϊ������
					trayVo.setNoutassistnum(warevo
							.getWhs_stockpieces());
					trayVo.setNoutnum(trayVo.getNoutassistnum().multiply(PuPubVO.getUFDouble_NullAsZero(body.getHsl())));
					usedNum.put(warevo.getWhs_pk(), warevo.getWhs_stockpieces());
				}
				trayVo
				.setCfirstbillhid(body
						.getCsourcebillhid()); // Դͷ����
				// Դͷ�ӱ�
				trayVo
				.setCfirstbillbid(body
						.getCsourcebillbid());
				trayVo.setVsourcebillcode(body
						.getVsourcebillcode()); // ��Դ���ݺ�
				trayVo.setCdt_pk(warevo
						.getPplpt_pk()); // ��������
//				usedTary.add(warevo.getPplpt_pk());
				trayVo.setStockpieces(warevo
						.getWhs_stockpieces()); // �������
				trayVo.setStocktonnage(warevo
						.getWhs_stocktonnage()); // ��渨����
				trayVo.setPk_invbasdoc(warevo
						.getPk_invbasdoc()); // �������id
				trayVo.setPk_invmandoc(warevo.getPk_invmandoc());// �������id
				trayVo.setVbatchcode(warevo
						.getWhs_batchcode()); // ����
				trayVo.setWhs_pk(warevo
						.getWhs_pk()); // ��������
				trayVo.setLvbatchcode(warevo
						.getWhs_lbatchcode()); // ��Դ����
				trayVo.setNprice(warevo
						.getWhs_nprice()); // ����
				trayVo.setNmny(warevo
						.getWhs_nmny()); // ���
				trayVo.setDr(0); // ɾ����־
				
				trayVo.setAunit(body.getCastunitid());//����λ
				trayVo.setUnitid(body.getUnitid());//����λ
				trayVo.setPk_cargdoc(body.getCspaceid());//��λ
				trayVo.setHsl(body.getHsl());// ����
				
	
			
				// ��������
//				body.addTray(trayVo);
				if(trayInfor.containsKey(body.getCrowno())){
					ltray = trayInfor.get(body.getCrowno());
				}else
					ltray = new ArrayList<TbOutgeneralTVO>();
				ltray.add(trayVo);
				trayInfor.put(body.getCrowno(), ltray);
				if (asum.doubleValue() >= 0) 
					break;
				asum = PuPubVO.getUFDouble_NullAsZero(Math.abs(asum.doubleValue()));
			}
		}
		return trayInfor;
	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-4-20����04:10:09
	 * @param body
	 * @return  ��ǰռ�õ�����id
	 * @throws BusinessException 
	 */
	private String getOldCdt(TbOutgeneralBVO body ) throws BusinessException{
		StringBuffer strWhere =  new StringBuffer(" or o.pplpt_pk in ");
		if(body.getGeneral_b_pk() !=null && !"".equalsIgnoreCase(body.getGeneral_b_pk())){
			String sql ="select cdt_pk  from tb_outgeneral_t where general_b_pk='"+body.getGeneral_b_pk()+"'";
			ArrayList<String> list = (ArrayList<String>)getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNLISTROCESSOR);
			strWhere.append(getTempTableUtil().getSubSql(list));
		}else{
			strWhere.append("('aa')");
		}
		return strWhere.toString();
	}
	

	public AggregatedValueObject deleteBD8004040204(
			AggregatedValueObject billVO, Object userObj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject saveBD8004040204(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		if (null != billVO.getParentVO() && null != billVO.getChildrenVO()
				&& billVO.getChildrenVO().length > 0) {
			List objList = (List) userObj;
			boolean tmp = CommonUnit.operationWare((TbOutgeneralBVO[]) objList
					.get(1), Boolean.parseBoolean(objList.get(3).toString()),
					Boolean.parseBoolean(objList.get(4).toString()), Boolean
							.parseBoolean(objList.get(2).toString()));
			if (!tmp)
				return null;
			Object[] obj = (Object[]) objList.get(5);
			if (Boolean.parseBoolean(obj[0].toString())) {
				// W80060604Impl
				Iw80060604 iw = (Iw80060604) NCLocator.getInstance().lookup(
						Iw80060604.class.getName());
				iw.insertFyd((List) obj[1], (List) obj[2], null);
				List l1 = (List) obj[1];
				TbFydnewVO fyd = (TbFydnewVO) l1.get(0);
				TbOutgeneralBVO[] genb = (TbOutgeneralBVO[]) billVO
						.getChildrenVO();
				if (null != genb && genb.length > 0 && null != fyd) {
					for (int i = 0; i < genb.length; i++) {
						genb[i].setCfirstbillhid(fyd.getFyd_pk());// ����Դͷ���ݱ�ͷ����
						genb[i].setVsourcebillcode(fyd.getVbillno());// ������Դ���ݺ�
					}
				}

			}
			nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
					.getInstance().lookup(IUifService.class.getName());
			return service.saveBD(billVO, objList.get(0));

		}

		return null;
	}

	/*
	 * ��ѯ��Ʒʵ����������������(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w8004040204.Iw8004040204#getNoutNum(java.lang.String,
	 *      java.lang.String)
	 */
	public Object[] getNoutNum(String pk_cfirst, String pk_invbasdoc)
			throws Exception {
		// TODO Auto-generated method stub
		String sql = "select noutnum,noutassistnum,vbatchcode,nprice,nmny,lvbatchcode from tb_outgeneral_t where dr = 0 and cfirstbillbid = '"
				+ pk_cfirst + "' and pk_invbasdoc = '" + pk_invbasdoc + "'";
		ArrayList list = (ArrayList) this.getIuap().executeQuery(sql,
				new ArrayListProcessor());
		if (null != list && list.size() > 0) {
			Object[] results = new Object[6];
			// ʵ��������
			double noutnum = 0;
			// ʵ��������
			double nassnum = 0;
			// ����
			StringBuffer batch = new StringBuffer();
			String tmpbat = null;
			double nprice = 0;
			double nmny = 0;
			String lvbatchcode = null;
			for (int i = 0; i < list.size(); i++) {
				Object[] a = (Object[]) list.get(i);
				if (null != a && a.length > 0) {
					// �ۼ�
					if (null != a[0] && !"".equals(a[0])) {
						noutnum = noutnum + Double.parseDouble(a[0].toString());
					}
					if (null != a[1] && !"".equals(a[1])) {
						nassnum = nassnum + Double.parseDouble(a[1].toString());
					}
					// �ۼ�����ȥ�ظ�
					if (null != a[2] && !"".equals(a[2])) {
						if (null == tmpbat) {
							tmpbat = a[2].toString();
							batch.append(a[2].toString());
						} else {
							if (!tmpbat.equals(a[2].toString())) {
								tmpbat = a[2].toString();
								batch.append(",").append(a[2].toString());
							}
						}
					}
					if (null != a[3] && !"".equals(a[3])) {
						nprice = Double.parseDouble(a[3].toString());
					}
					if (null != a[4] && !"".equals(a[4])) {
						nmny = Double.parseDouble(a[4].toString());
					}
					if (null != a[5] && !"".equals(a[5])) {
						lvbatchcode = a[5].toString();
					}

				}
			}
			results[0] = noutnum;
			results[1] = nassnum;
			results[2] = batch.toString();
			results[3] = nprice;
			results[4] = nmny;
			results[5] = lvbatchcode;
			return results;
		}
		return null;
	}

	public AggregatedValueObject saveBD8004040602(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		if (null != billVO.getParentVO()) {
			StockInvOnHandVO ware = (StockInvOnHandVO) billVO.getParentVO();
			String sql = " update tb_warehousestock set ss_pk = '"
					+ ware.getSs_pk() + "' where " + "pk_invbasdoc = '"
					+ ware.getPk_invbasdoc() + "' " + "and pk_cargdoc = '"
					+ ware.getPk_cargdoc() + "' " + " and whs_batchcode = '"
					+ ware.getWhs_batchcode() + "'";

			ArrayList<?> results = null;
			PersistenceManager sessioManager = null;

			try {
				sessioManager = PersistenceManager.getInstance();
				JdbcSession jdbcSession = sessioManager.getJdbcSession();
				jdbcSession.executeUpdate(sql);
			} catch (DbException e) {
				e.printStackTrace();
			}
			String strWhere = " dr = 0 and ss_pk = '" + ware.getSs_pk()
					+ "' and " + "pk_invbasdoc = '" + ware.getPk_invbasdoc()
					+ "' " + "and pk_cargdoc = '" + ware.getPk_cargdoc() + "' "
					+ " and whs_batchcode = '" + ware.getWhs_batchcode()
					+ "' and rownum = 1";
			ArrayList tmp = (ArrayList) getIuap().retrieveByClause(
					StockInvOnHandVO.class, strWhere);
			if (null != tmp && tmp.size() > 0) {
				StockInvOnHandVO tbware = (StockInvOnHandVO) tmp.get(0);
				billVO.setParentVO(tbware);
				return billVO;
			}

		}

		return null;
	}
	
	private int getSaleAlertDatNo(String invbasid) throws BusinessException{

		// ���ݵ�Ʒ������ѯ���õ�Ʒ�����۾�������
		String sql = "select "
			+ WdsWlPubConst.IC_INV_SALE_ALERT_DAYNO
			+ " from bd_invbasdoc  where pk_invbasdoc = '"
			+ invbasid
			+ "' and  isnull(dr,0) = 0 ";
		
		int iDay = PuPubVO.getInteger_NullAs(getIuap().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
		if(iDay < 0)
			throw new BusinessException("���������û��ά�����۾�������");
		return iDay;
	}

	public Object whs_processAction(String actionName, String actionName2,
			String billType, String currentDate, AggregatedValueObject vo,
			Object outgeneralVo) throws Exception {
		if (null != outgeneralVo)
			getIvo().updateVO((TbOutgeneralHVO) outgeneralVo);
		nc.bs.pub.pf.PfUtilBO pfutilbo = new PfUtilBO();
		// ����ERP�����۳���
		Object o = pfutilbo.processAction(actionName, billType, currentDate,
				null, vo, null);
		if (actionName.equals("CANCELSIGN")) {
			boolean oper = Boolean.parseBoolean(((ArrayList) o).get(0)
					.toString());
			if (!oper)
				return null;
			o = pfutilbo.processAction(actionName2, billType, currentDate,
					null, vo, null);
			return o;
		}
		if (actionName.equals("SAVEPICKSIGN")) {
			return o;
		}
		AggregatedValueObject billVO = null;
		Object[] arrayO = (Object[]) o;
		billVO = (AggregatedValueObject) arrayO[0];
		// ���۳���ǩ��
		o = pfutilbo.processAction(actionName2, billType, currentDate, null,
				billVO, null);
		return o;
	}

	public void queryWarehousestock(List itemList) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void saveGeneralVO(MyBillVO myBillVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
