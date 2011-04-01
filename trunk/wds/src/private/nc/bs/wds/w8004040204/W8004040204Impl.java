package nc.bs.wds.w8004040204;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w8004040204.Iw8004040204;
import nc.itf.wds.w80060604.Iw80060604;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w8004040204.MyBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;
import nc.vo.wds.w80060406.TbFydnewVO;

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
	public void insertWarehousestock(TbWarehousestockVO item) throws Exception {
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

	/**
	 * ��������ָ�����ѯ��������Ӧ������ ���в���
	 */
	public void queryWarehousestock(List itemList) throws Exception {
		// TODO Auto-generated method stub
		if (null != itemList && itemList.size() > 0) {
			double noutnum = 0; // ʵ������
			double noutassistnum = 0; // ʵ��������
			for (int i = 0; i < itemList.size(); i++) {
				TbOutgeneralTVO generaltvo = (TbOutgeneralTVO) itemList.get(i);
				noutassistnum = noutassistnum
						+ generaltvo.getNoutassistnum().toDouble();
				noutnum = noutnum + generaltvo.getNoutnum().toDouble();
				if (i == itemList.size() - 1) {
					String sWhere = " dr = 0 and whs_status = 0 and pplpt_pk = '"
							+ generaltvo.getCdt_pk()
							+ "' and pk_invbasdoc = '"
							+ generaltvo.getPk_invbasdoc() + "'";
					// �������ݿ�õ������
					ArrayList generaltList = (ArrayList) this.getIuap()
							.retrieveByClause(TbWarehousestockVO.class, sWhere);
					// �жϽ�����Ƿ�Ϊ��
					if (null != generaltList && generaltList.size() > 0) {
						TbWarehousestockVO item = (TbWarehousestockVO) generaltList
								.get(0);
						if (noutassistnum == item.getWhs_stockpieces()
								.toDouble()
								&& noutnum == item.getWhs_stocktonnage()
										.toDouble()) {
							item.setWhs_status(1);
						}
						item.setWhs_stockpieces(new UFDouble(noutassistnum
								- item.getWhs_stockpieces().toDouble()));
						item.setWhs_stocktonnage(new UFDouble(noutnum
								- item.getWhs_stocktonnage().toDouble()));
						this.updateWarehousestock(item);
						item.setWhs_pk(null);
						item.setWhs_status(1);
						item.setWhs_type(2);
						item.setWhs_stockpieces(new UFDouble(noutassistnum));
						item.setWhs_stocktonnage(new UFDouble(noutnum));
						this.insertWarehousestock(item);
						this.updateBdcargdocTray(item.getPplpt_pk());
					}
				}
			}
		}
	}

	/**
	 * ������ⵥ
	 * 
	 * @MyBillVO �ۺ�VO ��������Ҫ������ֵ
	 */
	public void saveGeneralVO(MyBillVO myBillVO) throws Exception {
		// TODO Auto-generated method stub

		// TbOutgeneralBVO[] generalBVO = (TbOutgeneralBVO[]) myBillVO
		// .getChildrenVO();
		// if (null != generalBVO && generalBVO.length > 0) {
		// TbOutgeneralBVO generalbvo = null;
		// List itemList = null;
		// // TbOutgeneralTVO[] generalTVO
		// for (int i = 0; i < generalBVO.length; i++) {
		// generalbvo = generalBVO[i];
		// if (null != generalbvo.getNoutassistnum()
		// && !"".equals(generalbvo.getNoutassistnum())) {
		// itemList = this.queryGeneralTVO(generalbvo
		// .getCinventoryid(), generalbvo.getCsourcebillhid(),
		// generalbvo.csourcebillbid);
		// this.queryWarehousestock(itemList);
		// } else {
		// itemList = this.queryGeneralTVO(generalbvo
		// .getCinventoryid(), generalbvo.getCfirstbillhid(),
		// generalbvo.getCfirstbillbid());
		// this.deleteGeneralTVO(itemList);
		// }
		// }
		// }
	}

	/**
	 * ���¿���
	 */
	public void updateWarehousestock(TbWarehousestockVO item) throws Exception {
		// TODO Auto-generated method stub
		if (null != item) {
			this.getIvo().updateVO(item);
		}
	}

	public IVOPersistence getIvo() {
		return (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
	}

	public void setIvo(IVOPersistence ivo) {
		this.ivo = ivo;
	}

	public IUAPQueryBS getIuap() {
		return (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
	}

	public void setIuap(IUAPQueryBS iuap) {
		this.iuap = iuap;
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
			}
		}
	}

	/**
	 * ���ݳ����ӱ���Ϣɾ��������������
	 * 
	 * @param generalb
	 *            �����ӱ�
	 * @throws BusinessException
	 */
	private void deleteAutoPickAction(TbOutgeneralBVO[] generalb)
			throws BusinessException {
		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				String strWhere = " pk_invbasdoc='"
						+ generalb[i].getCinventoryid()
						+ "' and cfirstbillbid ='"
						+ generalb[i].getCsourcebillbid() + "'";
				this.getIvo().deleteByClause(TbOutgeneralTVO.class, strWhere);
			}
		}
	}

	/*
	 * ������Ʒ���������Զ�ȡ��(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w8004040204.Iw8004040204#autoPickAction(java.lang.String)
	 */
	public String autoPickAction(String pk_user, TbOutgeneralBVO[] generalb,
			String pk_stordoc, String queryVar) throws Exception {
		// TODO Auto-generated method stub
		deleteAutoPickAction(generalb);
		TbOutgeneralTVO generalt = null;

		if (null != generalb && generalb.length > 0) {
			for (int i = 0; i < generalb.length; i++) {
				if (null == generalb[i])
					continue;
				if (null != generalb[i].getCinventoryid()
						&& !"".equals(generalb[i].getCinventoryid().trim())) {

					List list = CommonUnit.getStockDetailByPk_User(pk_user,
							generalb[i].getCinventoryid().trim());
					TbWarehousestockVO[] wareVO = null;
					double asum = 0;// Ӧ������
					// ��ȡӦ��������
					asum = generalb[i].getNshouldoutassistnum().toDouble();
					if (null != list && list.size() > 0) {
						wareVO = new TbWarehousestockVO[list.size()];
						wareVO = (TbWarehousestockVO[]) list.toArray(wareVO);
						for (int j = 0; j < wareVO.length; j++) {
							TbWarehousestockVO warevo = wareVO[j];
							int com = -1; // ��ǰ���ں����ݿ����������ڵ�ʱ���
							int alertnum = -1; // ����������ѯ���������۾�������
							if (null != warevo) {
								String bathdate = ""; // �洢��ȡ�����������
								if (warevo.getWhs_batchcode().length() >= 8)
									bathdate = warevo.getWhs_batchcode()
											.substring(0, 8);
								long DAY = 24L * 60L * 60L * 1000L;
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyyMMdd");
								Date date = new Date();
								try {
									// ���� ��ǰ���ں����ݿ����������ڵ�ʱ���
									com = Integer
											.parseInt(((date.getTime() - format
													.parse(bathdate).getTime()) / DAY)
													+ "");
								} catch (RuntimeException e) {
									// TODO Auto-generated catch block
									return "���ݿ����������ڲ��ϸ�";
								}
								// ���ݵ�Ʒ������ѯ���õ�Ʒ�����۾�������
								String sql = "select b."
										+ queryVar
										+ ",m.scalefactor from bd_invbasdoc b ,bd_measdoc m  where b.pk_invbasdoc = '"
										+ generalb[i].getCinventoryid().trim()
										+ "' and b.pk_measdoc1 = m.pk_measdoc and b.dr = 0 and m.dr=0";
								ArrayList resultsList = (ArrayList) this
										.getIuap().executeQuery(sql,
												new ArrayListProcessor());
								Object[] a = null;
								if (null != resultsList
										&& resultsList.size() > 0) {
									a = (Object[]) resultsList.get(0);
								}
								if (null != a && a.length > 0 && null != a[0]) {
									alertnum = Integer
											.parseInt(a[0].toString());
								} else {
									return "�к�:" + generalb[i].getCrowno()
											+ " ���������û��ά�����۾�������";
								}

								// �����ǰ���ں����ε�ʱ���С�����۾������� �û�Ʒ���Գ���
								if (com < alertnum) {
									if (null != warevo.getWhs_stockpieces()
											&& !"".equals(warevo
													.getWhs_stockpieces())
											&& warevo.getWhs_stockpieces()
													.toDouble() > 0) {
										generalt = new TbOutgeneralTVO();
										double tmpsum = asum;
										// ���������ȥӦ������
										asum = warevo.getWhs_stockpieces()
												.toDouble()
												- asum;
										// ������
										double noutnum = 0;
										if (null != a[1])
											noutnum = Double.parseDouble(a[1]
													.toString());
										// ���ʣ���������ڵ���0 ��ǰ���̴��������
										if (asum >= 0) {
											// �ѵ�ǰ�������洢ʵ������
											generalt
													.setNoutassistnum(new UFDouble(
															tmpsum));
											// ����ʵ��������
											if (noutnum != 0) {
												generalt
														.setNoutnum(new UFDouble(
																tmpsum
																		* noutnum));
											}
										} else {
											// ���ʣ������С��0 ��ǰ���̴��������������ǰ���̴����Ϊ������
											// tmpsum =
											// warevo.getWhs_stockpieces()
											// .toDouble()
											// - tmpsum;
											// asum = Math.abs(asum);
											generalt.setNoutassistnum(warevo
													.getWhs_stockpieces());
											if (noutnum != 0) {
												generalt
														.setNoutnum(new UFDouble(
																warevo
																		.getWhs_stockpieces()
																		.toDouble()
																		* noutnum));
											}
										}

										if (null != generalb[i]
												.getCsourcebillhid()
												&& !"".equals(generalb[i]
														.getCsourcebillhid())) {
											generalt
													.setCfirstbillhid(generalb[i]
															.getCsourcebillhid()); // Դͷ����
										}
										if (null != generalb[i]
												.getCsourcebillbid()
												&& !"".equals(generalb[i]
														.getCsourcebillbid())) { // Դͷ�ӱ�
											generalt
													.setCfirstbillbid(generalb[i]
															.getCsourcebillbid());
										}
										if (null != generalb[i]
												.getVsourcebillcode()
												&& !"".equals(generalb[i]
														.getVsourcebillcode())) {
											generalt
													.setVsourcebillcode(generalb[i]
															.getVsourcebillcode()); // ��Դ���ݺ�
										}
										if (null != warevo.getPplpt_pk()
												&& !"".equals(warevo
														.getPplpt_pk())) {
											generalt.setCdt_pk(warevo
													.getPplpt_pk()); // ��������
										}
										if (null != warevo.getWhs_stockpieces()
												&& !"".equals(warevo
														.getWhs_stockpieces())) {
											generalt.setStockpieces(warevo
													.getWhs_stockpieces()); // �������
										}
										if (null != warevo
												.getWhs_stocktonnage()
												&& !"".equals(warevo
														.getWhs_stocktonnage())) {
											generalt.setStocktonnage(warevo
													.getWhs_stocktonnage()); // ��渨����
										}
										if (null != warevo.getPk_invbasdoc()
												&& !"".equals(warevo
														.getPk_invbasdoc())) {
											generalt.setPk_invbasdoc(warevo
													.getPk_invbasdoc()); // �������
										}
										if (null != warevo.getWhs_batchcode()
												&& !"".equals(warevo
														.getWhs_batchcode())) {
											generalt.setVbatchcode(warevo
													.getWhs_batchcode()); // ����
										}
										if (null != warevo.getWhs_pk()
												&& !"".equals(warevo
														.getWhs_pk())) {
											generalt.setWhs_pk(warevo
													.getWhs_pk()); // ��������
										}
										if (null != warevo.getWhs_lbatchcode()
												&& !"".equals(warevo
														.getWhs_lbatchcode())) {
											generalt.setLvbatchcode(warevo
													.getWhs_lbatchcode()); // ��Դ����
										}
										if (null != warevo.getWhs_nprice()
												&& !"".equals(warevo
														.getWhs_nprice())) {
											generalt.setNprice(warevo
													.getWhs_nprice()); // ����
										}
										if (null != warevo.getWhs_nmny()
												&& !"".equals(warevo
														.getWhs_nmny())) {
											generalt.setNmny(warevo
													.getWhs_nmny()); // ���
										}
										generalt.setDr(0); // ɾ����־
										// ��������
										this.getIvo().insertVO(generalt);

										if (asum >= 0) {
											break;
										} else {
											asum = Math.abs(asum);
										}
									}

								}
							}
						}
					}

				}
			}

		}
		return null;
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
			TbWarehousestockVO ware = (TbWarehousestockVO) billVO.getParentVO();
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
					TbWarehousestockVO.class, strWhere);
			if (null != tmp && tmp.size() > 0) {
				TbWarehousestockVO tbware = (TbWarehousestockVO) tmp.get(0);
				billVO.setParentVO(tbware);
				return billVO;
			}

		}

		return null;
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

}
