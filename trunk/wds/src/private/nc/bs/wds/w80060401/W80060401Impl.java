package nc.bs.wds.w80060401;

import java.util.*;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w80060401.Iw80060401;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;

public class W80060401Impl implements Iw80060401 {

	// ���ݿ��ѯ����
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());
	// ��ȡ���ݿ���ʶ���
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	public TbShipentryBVO[] queryShipentryBVO(String strWhere)
			throws BusinessException { 
		String sql = "select tb_invcl_bas.pk_invbasdoc from tb_invcl,tb_invcl_bas,bd_invbasdoc "
				+ " where tb_invcl.pk_invcl=tb_invcl_bas.pk_invcl "
				+ " and tb_invcl.dr=0 and tb_invcl_bas.dr=0 and bd_invbasdoc.dr = 0 and bd_invbasdoc.pk_invbasdoc = tb_invcl_bas.pk_invbasdoc "
				+ " order by tb_invcl.invcode,tb_invcl_bas.fr_order";

		ArrayList<?> results = null;
		TbShipentryBVO[] spvo = null;
		PersistenceManager sessioManager = null;

		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			results = (ArrayList<?>) jdbcSession.executeQuery(sql,
					new ArrayListProcessor());
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (results != null && !results.isEmpty()) {
			spvo = new TbShipentryBVO[results.size()];
			for (int i = 0; i < results.size(); i++) {
				spvo[i] = new TbShipentryBVO();
				spvo[i]
						.setPk_invbasdoc(WDSTools
								.getString_TrimZeroLenAsNull(((Object[]) results
										.get(i))[0]));

			}
		}
		return spvo;

	}

	/**
	 * �ж�¼�����ͺ���д洢
	 * 
	 * @param billvo
	 *            �ۺ�VO
	 * @param begindate
	 *            ��ʼʱ��
	 * @param enddate
	 *            ����ʱ��
	 * @param list
	 *            ׷�Ӽƻ���Ӧ�õļ�����������ʣ������
	 * @throws Exception
	 */
	public void insertShipertryVO(AggregatedValueObject billvo,
			String begindate, String enddate, ArrayList shvolist)
			throws Exception {
		// TODO Auto-generated method stub

		if (null != billvo) {
			TbShipentryVO shvo = (TbShipentryVO) billvo.getParentVO();
			TbShipentryBVO[] shbVO = (TbShipentryBVO[]) billvo.getChildrenVO();

			if (null != shvo) {
				// �ж������Ƿ�Ϊ�գ����Ϊ��˵��Ϊ��������ֵ˵�����޸�
				if (null == shvo.getSe_pk() || "".equals(shvo.getSe_pk())) {
					// �жϼƻ����ͣ�0 �¼ƻ� 1 ׷�Ӽƻ�
					if (shvo.getSe_type() == 0) {
						// �ж��ӱ���������Ƿ�Ϊ��
						if (null != shbVO && shbVO.length > 0) {
							for (int i = 0; i < shbVO.length; i++) {
								TbShipentryBVO shbvo = shbVO[i];
								// �жϼƻ����Ƿ�Ϊ��
								if (null != shbvo.getSeb_plannum()
										&& !"".equals(shbvo.getSeb_plannum())) {
									// �Ѽƻ���ת����ʣ��
									shbvo
											.setSeb_surplus(shbvo
													.getSeb_plannum());
									shbVO[i] = shbvo;
								}
							}
							billvo.setChildrenVO(shbVO);
							return;
						}
					} else { // ׷�Ӽƻ�
						ArrayList shbvoList = this.plannumTosurplus(shvolist,
								shbVO, 0);
						// Update �¼ƻ��ӱ���ʣ������
						ivo.updateVOList(shbvoList);
						return;
					}
				} else { // �޸�

					// �жϼƻ����ͣ�0 �¼ƻ� 1 ׷�Ӽƻ�
					if (shvo.getSe_type() == 0) {
						if (null != shbVO && shbVO.length > 0) {

							String sql = "select b.pk_invbasdoc, sum(seb_plannum) "
									+ "from tb_shipentry_b b, tb_shipentry t "
									+ "where b.dr = 0 and t.dr = 0 and b.se_pk = t.se_pk "
									+ " and t.doperatordate between '"
									+ begindate
									+ "' and '"
									+ enddate
									+ "' "
									+ " and t.srl_pk = '"
									+ shvo.getSrl_pk()
									+ "' "
									+ " and t.se_pk <> '"
									+ shvo.getSe_pk()
									+ "' "
									+ " group by b.pk_invbasdoc ";

							ArrayList<?> results = null;
							PersistenceManager sessioManager = null;
							TbShipentryBVO[] shibVO = null;
							// //////��ȡ���ݿ���ʶ��󣬲�ѯ���ݿ⡢��������
							try {
								sessioManager = PersistenceManager
										.getInstance();
								JdbcSession jdbcSession = sessioManager
										.getJdbcSession();
								results = (ArrayList<?>) jdbcSession
										.executeQuery(sql,
												new ArrayListProcessor());
								if (results != null && !results.isEmpty()) {
									// ��ѯ�����ݿ������б��ֵ�Ʒ�ļƻ��������¼ƻ���׷�Ӽƻ�
									shibVO = new TbShipentryBVO[results.size()];
									for (int i = 0; i < results.size(); i++) {
										shibVO[i] = new TbShipentryBVO();
										shibVO[i]
												.setPk_invbasdoc(WDSTools
														.getString_TrimZeroLenAsNull(((Object[]) results
																.get(i))[0]));
										shibVO[i]
												.setSeb_plannum(WDSTools
														.getUFDouble_NullAsZero(((Object[]) results
																.get(i))[1]));
									}
									// update ����
									// ivo.updateVO(shvo);
									billvo.setParentVO(shvo);
									for (int i = 0; i < shbVO.length; i++) {
										TbShipentryBVO shbvo = shbVO[i];
										if (null != shbvo) {

											// �жϵ�ǰҳ���еļƻ����Ƿ�Ϊ��
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												if (null != shibVO
														&& shibVO.length > 0) {
													for (int j = 0; j < shibVO.length; j++) {
														double sum = 0; // �ܵļƻ���
														// �ж�������Ʒ�Ƿ����
														if (shbvo
																.getPk_invbasdoc()
																.equals(
																		shibVO[j]
																				.getPk_invbasdoc())) {
															// �ж����ݿ��еļƻ����Ƿ�Ϊ��
															if (null != shibVO[j]
																	.getSeb_plannum()
																	&& !""
																			.equals(shibVO[j]
																					.getSeb_plannum())) {
																// ��Ϊ�հ����ݿ��еļƻ����͵�ǰ�ļƻ�����ӣ�����ܵļƻ���
																sum = shibVO[j]
																		.getSeb_plannum()
																		.toDouble()
																		+ shbvo
																				.getSeb_plannum()
																				.toDouble();
															} else {
																// ������ݿ��еļƻ���Ϊ��
																// �ѵ�ǰ�ļƻ��������ܵļƻ���
																sum = shbvo
																		.getSeb_plannum()
																		.toDouble();
															}
															// �ж��¼ƻ��е��ѷ������Ƿ�Ϊ��
															if (null != shbvo
																	.getSeb_shipped()
																	&& !""
																			.equals(shbvo
																					.getSeb_shipped())) {
																// ���ܵļƻ�����ȥ�ѷ��˵�����
																// ����
																// ʣ�������
																sum = sum
																		- shbvo
																				.getSeb_shipped()
																				.toDouble();
															}
															// ��ʣ��������ֵ
															shbvo
																	.setSeb_surplus(new UFDouble(
																			sum));

															// update �ӱ�
															// ivo.updateVO(shbvo);
															shbVO[i] = shbvo;
															break;
														}
													}
												}
											}
										}
										billvo.setChildrenVO(shbVO);
									}
								} else {// /////���ݿ���û�м�¼
									// �ж��ӱ���������Ƿ�Ϊ��
									// update ����
									// ivo.updateVO(shvo);
									billvo.setParentVO(shvo);
									if (null != shbVO && shbVO.length > 0) {
										for (int i = 0; i < shbVO.length; i++) {
											TbShipentryBVO shbvo = shbVO[i];
											// �жϼƻ����Ƿ�Ϊ��
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												// �Ѽƻ���ת����ʣ��
												shbvo.setSeb_surplus(shbvo
														.getSeb_plannum());
											}
										}
										// Update�ӱ�
										// ivo.updateVOArray(shbVO);
										billvo.setChildrenVO(shbVO);
									}
								}
							} catch (DbException e) {
								e.printStackTrace();
							}

						}
					} else { // ׷�Ӽƻ�
						ArrayList shbvoList = this.plannumTosurplus(shvolist,
								shbVO, 1);
						// Update �¼ƻ��ӱ���ʣ������
						ivo.updateVOList(shbvoList);
						// // update ����
						// ivo.updateVO(shvo);
						// // update �ӱ�
						// ivo.updateVOArray(shbVO);

					}
				}
			}
		}
	}

	// �ƻ���ת��ʣ������
	private ArrayList<TbShipentryBVO> plannumTosurplus(ArrayList shvolist,
			TbShipentryBVO[] shbVO, int type) throws BusinessException {
		ArrayList<TbShipentryBVO> shbvoList = new ArrayList<TbShipentryBVO>();
		if (null != shvolist && shvolist.size() > 0) {

			TbShipentryVO shipvo = (TbShipentryVO) shvolist.get(0);
			String sWhere = " dr = 0 and se_pk ='" + shipvo.getSe_pk() + "'";
			ArrayList list = (ArrayList) iuap.retrieveByClause(
					TbShipentryBVO.class, sWhere);
			if (null != list && list.size() > 0) {

				// ѭ���¼ƻ��е��ӱ���Ϣ
				for (int i = 0; i < list.size(); i++) {
					// ��ȡ��������
					TbShipentryBVO shbvo = (TbShipentryBVO) list.get(i);
					// �ж��¼ƻ��ж���
					if (null != shbvo) {
						// �ж���ǰ�ӱ���Ϣ
						if (null != shbVO && shbVO.length > 0) {
							// ѭ����ǰ�����е��ӱ���Ϣ
							for (int j = 0; j < shbVO.length; j++) {
								if (null != shbVO[j].getSeb_plannum()
										&& !""
												.equals(shbVO[j]
														.getSeb_plannum())) {
									// �ж������ǰҳ���еĵ�Ʒ���¼ƻ��еĵ�Ʒ��Ϣһ�£��޸��¼ƻ��е�ʣ�����������¼ƻ��е�ʣ����ϵ�ǰ��Ʒ�ļƻ���
									if (shbvo.getPk_invbasdoc().equals(
											shbVO[j].getPk_invbasdoc())) {
										if (type == 0) {
											// �ж��¼ƻ��е�ʣ�����Ƿ�Ϊ��
											if (null != shbvo.getSeb_surplus()
													&& !"".equals(shbvo
															.getSeb_surplus())) {
												double sum = shbvo
														.getSeb_surplus()
														.toDouble()
														+ shbVO[j]
																.getSeb_plannum()
																.toDouble();
												shbvo
														.setSeb_surplus(new UFDouble(
																sum));
											} else {
												// ����¼ƻ���û�мƻ����ĵ�Ʒ��ֱ�ӰѼƻ��������¼ƻ��е�ʣ��
												shbvo.setSeb_surplus(shbVO[j]
														.getSeb_plannum());
											}
											shbvoList.add(shbvo);
											break;
										} else if (type == 1) {
											double sum = 0;
											if (null != shbvo.getSeb_plannum()
													&& !"".equals(shbvo
															.getSeb_plannum())) {
												sum = shbVO[j].getSeb_plannum()
														.toDouble()
														+ shbvo
																.getSeb_plannum()
																.toDouble();
											} else {
												sum = shbVO[j].getSeb_plannum()
														.toDouble();
											}
											// �ж��¼ƻ��е��ѷ������Ƿ�Ϊ��
											if (null != shbvo.getSeb_shipped()
													&& !"".equals(shbvo
															.getSeb_shipped())) {
												// ���ܵļƻ�����ȥ�ѷ��˵����� ���� ʣ�������
												sum = sum
														- shbvo
																.getSeb_shipped()
																.toDouble();
											}
											// ��ʣ��������ֵ
											shbvo.setSeb_surplus(new UFDouble(
													sum));
											shbvoList.add(shbvo);
											break;
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return shbvoList;
	}

	public AggregatedValueObject deleteBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		boolean isupdate = Boolean.parseBoolean(((List) userObj).get(2)
				.toString());
		if (isupdate) {
			List list = ((List) ((List) userObj).get(1));
			if (null != list && list.size() > 0) {
				ivo.updateVOList(list);
			}
		}
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());
		return service.deleteBD(billVO, ((List) userObj).get(0));
		// return HYPubBO_Client.deleteBD(billVO, ((List) userObj).get(0));
	}

	public AggregatedValueObject saveBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		this.insertShipertryVO(billVO, ((List) userObj).get(1).toString(),
				((List) userObj).get(2).toString(),
				((ArrayList) ((List) userObj).get(3)));
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());

		AggregatedValueObject bvo = service.saveBD(billVO, ((List) userObj)
				.get(0));
		// HYPubBO_Client.saveBD(billVO, ((List) userObj).get(0));
		return bvo;
	}

}
