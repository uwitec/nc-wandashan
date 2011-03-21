package nc.bs.wds.w80060202;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060202.Iw80060202;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

public class W80060202Impl implements Iw80060202 {

	/**
	 * ���·���¼���ӱ��еĴ���������
	 * 
	 * @param shb
	 * @throws BusinessException
	 */
	public void updateShipentryBVO(TbShipentryBVO[] shb, boolean type)
			throws BusinessException {
		PersistenceManager sessioManager = null;
		try {
			sessioManager = PersistenceManager.getInstance();
			JdbcSession jdbcSession = sessioManager.getJdbcSession();
			ArrayList<?> results = null;
			if (null != shb && shb.length > 0) {
				for (int i = 0; i < shb.length; i++) {
					TbShipentryBVO shbvo = shb[i];
					// �ȸ���������ѯ���Ƿ���д�������
					String sql = "select seb_travel from tb_shipentry_b where seb_pk='"
							+ shbvo.getSeb_pk()
							+ "' and se_pk = '"
							+ shbvo.getSe_pk() + "'";
					results = (ArrayList<?>) jdbcSession.executeQuery(sql,
							new ArrayListProcessor());
					UFDouble num = new UFDouble(0);
					double sum = 0;
					if (null != results && !results.isEmpty()) {
						// ��ȡ���
						num = WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]);
					}
					// �ж������ֲ��� true���� false ɾ��
					if (type) {
						if (num.toDouble() > 0) {
							// ������д������� ����������� ��ǰ�����еĺ����ݿ�ԭ�е�
							sum = num.toDouble()
									+ shbvo.getSeb_travel().toDouble();
						} else {
							sum = shbvo.getSeb_travel().toDouble();
						}
					} else {
						if (num.toDouble() > 0) {
							sum = num.toDouble()
									- shbvo.getSeb_travel().toDouble();
							if (sum < 0)
								sum = 0;
						} else {
							sum = 0;
						}
					}
					// �������ݿ��еĴ���������
					sql = "update tb_shipentry_b set seb_travel = " + sum
							+ " where seb_pk='" + shbvo.getSeb_pk() + "'";
					jdbcSession.executeUpdate(sql);
				}

			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �����˵�
	 */
	public void saveFydVO(AggregatedValueObject billVO)
			throws BusinessException {
		if (null != billVO) {
			TbFydmxnewVO[] fydmxVO = null;
			// ��ȡ���ݿ���ʶ���
			IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
					.lookup(IVOPersistence.class.getName());
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			ivo.updateVO(fydVO); // ��������
			fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			ivo.updateVOArray(fydmxVO); // �����ӱ�
			this.opDB(fydVO, fydmxVO, true);

		}
	}

	private void opDB(TbFydnewVO fydVO, TbFydmxnewVO[] fydmxVO, boolean type)
			throws BusinessException {
		// �ƻ�¼���ӱ�������
		TbShipentryBVO[] shbVO = new TbShipentryBVO[fydmxVO.length];
		// ѭ���ӱ�����
		for (int i = 0; i < fydmxVO.length; i++) {
			TbFydmxnewVO fydmxvo = fydmxVO[i];
			TbShipentryBVO shbvo = new TbShipentryBVO();
			shbvo.setSeb_pk(fydmxvo.getSeb_pk()); // ¼���ӱ�����
			shbvo.setSeb_travel(fydmxvo.getCfd_xs()); // ������
			shbvo.setSe_pk(fydVO.getSe_pk()); // ¼����������
			shbVO[i] = shbvo;

		}
		// ���÷�������¼���ӱ����������
		this.updateShipentryBVO(shbVO, type);
	}

	public void deleteFydVO(AggregatedValueObject billVO)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (null != billVO) {
			TbFydmxnewVO[] fydmxVO = null;
			// ��ȡ���ݿ���ʶ���
			IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
					.lookup(IVOPersistence.class.getName());
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			fydVO.setVbillstatus(0); //����״̬
			fydVO.setFyd_fyzt(null);
			fydVO.setFyd_approstate(1);
			fydVO.setIprintcount(null);
			fydVO.setIprintdate(null);
			fydVO.setFyd_zdr(null);
			fydVO.setFyd_dby(null);
			fydVO.setFyd_zdsj(null);
			fydVO.setFyd_yhfs(null);
			ivo.updateVO(fydVO); // ��������
			fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			//ivo.deleteVOArray(fydmxVO); // �����ӱ�
			this.opDB(fydVO, fydmxVO, false);

		}
	}

}
