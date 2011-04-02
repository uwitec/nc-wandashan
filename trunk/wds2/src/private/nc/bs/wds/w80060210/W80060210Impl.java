package nc.bs.wds.w80060210;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w80060210.Iw80060210;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.vo.dm.confirm.TbFydmxnewVO;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;

public class W80060210Impl implements Iw80060210 {

	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	public AggregatedValueObject saveAndCommit80060210(
			AggregatedValueObject billVO, Object userObj) throws Exception {
		// TODO Auto-generated method stub
		operFydNumData(billVO);

		changeTbOutGeneral(billVO);
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());
		AggregatedValueObject retVo = service.saveBD(billVO, userObj);

		
		return retVo;
	}

	// ���ݿ����
	ArrayList<?> results = null;

	PersistenceManager sessioManager = null;

	public void changeTbOutGeneral(AggregatedValueObject billvo)
			throws BusinessException, DbException {
		TbFydnewVO fydvo = (TbFydnewVO) billvo.getParentVO();

		if (null != fydvo) {

			TbFydnewVO tmpfyd = (TbFydnewVO) iuap.retrieveByPK(
					TbFydnewVO.class, fydvo.getFyd_pk());
			if (null != tmpfyd) {
				if (null != fydvo.getFyd_constatus()
						&& fydvo.getFyd_constatus() == 1
						&& (null == tmpfyd.getFyd_constatus() || 0 == tmpfyd
								.getFyd_constatus())) {
					

						String sql = "update tb_outgeneral_h set vbillstatus = '1' where dr = 0 and  csourcebillhid = '"
								+ fydvo.getFyd_pk() + "'";
						sessioManager = PersistenceManager.getInstance();
						JdbcSession jdbcSession = sessioManager
								.getJdbcSession();
						jdbcSession.executeUpdate(sql);
					}
				
			}

		}

	}

	/**
	 * �Դ�����,�ѷ���,ʣ��,���в����������
	 * 
	 * @param mybillVO
	 *            ���˵��ۺ�VO
	 * @return �Ƿ�ɹ�
	 * @throws BusinessException
	 */
	public boolean operFydNumData(AggregatedValueObject mybillVO)
			throws BusinessException {
		if (null != mybillVO.getParentVO() && null != mybillVO.getChildrenVO()
				&& mybillVO.getChildrenVO().length > 0) {

			TbFydnewVO fydvo = (TbFydnewVO) mybillVO.getParentVO();
			if (null != fydvo.getBilltype() && fydvo.getBilltype() == 0
					&& null != fydvo.getFyd_constatus()
					&& !"".equals(fydvo.getFyd_constatus())
					&& 1 == fydvo.getFyd_constatus()) {

				TbFydmxnewVO[] fydmxvo = (TbFydmxnewVO[]) mybillVO
						.getChildrenVO();
				// ����վ����ʼʱ��ͽ���ʱ��
				String strWhere = " dr = 0 and se_type = 0 and srl_pk = '"
						+ fydvo.getSrl_pkr() + "' and doperatordate between  '"
						+ fydvo.getFyd_begints() + "' and '"
						+ fydvo.getFyd_endts() + "'";

				ArrayList list = (ArrayList) iuap.retrieveByClause(
						TbShipentryVO.class, strWhere);

				List<TbShipentryBVO> shipentryList = new ArrayList<TbShipentryBVO>();

				if (null != list && list.size() > 0) {

					TbShipentryVO shevo = (TbShipentryVO) list.get(0);

					if (null == shevo)
						return false;

					strWhere = " dr = 0 and se_pk = '" + shevo.getSe_pk() + "'";
					list = null;
					list = (ArrayList) iuap.retrieveByClause(
							TbShipentryBVO.class, strWhere);

					if (null != list && list.size() > 0) {

						TbShipentryBVO[] she_bvo = new TbShipentryBVO[list
								.size()];
						she_bvo = (TbShipentryBVO[]) list.toArray(she_bvo);

						if (null != she_bvo && she_bvo.length > 0) {
							for (int i = 0; i < she_bvo.length; i++) {
								TbShipentryBVO shipentry = she_bvo[i];
								for (int j = 0; j < fydmxvo.length; j++) {
									TbFydmxnewVO fydmx = fydmxvo[j];

									if (fydmx.getPk_invbasdoc().equals(
											shipentry.getPk_invbasdoc())) {
										// ���˵�ʵ��������
										double s_num = 0;
										if (null != fydmx.getCfd_sffsl())
											s_num = fydmx.getCfd_sffsl()
													.doubleValue();
										else
											break;
										// �����ѷ���,���ԭ�����ݽ����ۼ�
										if (null != shipentry.getSeb_shipped())
											shipentry
													.setSeb_shipped(new UFDouble(
															shipentry
																	.getSeb_shipped()
																	.doubleValue()
																	+ s_num));
										else
											shipentry
													.setSeb_shipped(new UFDouble(
															s_num));
										// ���ô�����,���ԭ�����ݽ������
										if (null != shipentry.getSeb_travel())
											shipentry
													.setSeb_travel(new UFDouble(
															shipentry
																	.getSeb_travel()
																	.doubleValue()
																	- s_num));
										else
											shipentry
													.setSeb_travel(new UFDouble(
															s_num
																	- (s_num + s_num))); // �������
										// ����ʣ��,���ԭ�����ݽ������
										if (null != shipentry.getSeb_surplus())
											shipentry
													.setSeb_surplus(new UFDouble(
															shipentry
																	.getSeb_surplus()
																	.doubleValue()
																	- s_num));
										else
											shipentry
													.setSeb_surplus(new UFDouble(
															s_num
																	- (s_num + s_num)));// �������
										// ��Ӽ���,�������
										shipentryList.add(shipentry);
									}
								}

							}
							// ִ�и���
							ivo.updateVOList(shipentryList);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
