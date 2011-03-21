package nc.bs.wds.w80060408;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060408.Iw80060408;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wds.pub.WDSTools;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

public class W80060408Impl implements Iw80060408 {

	/**
	 * ���·���¼���ӱ��еĴ���������
	 * 
	 * @param shb
	 * @throws BusinessException
	 */
	public void updateShipentryBVO(TbShipentryBVO[] shb)
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
					if (num.toDouble() > 0) {
						// ������д������� ����������� ��ǰ�����еĺ����ݿ�ԭ�е�
						sum = num.toDouble() + shbvo.getSeb_travel().toDouble();
					} else {
						sum = shbvo.getSeb_travel().toDouble();
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
	 * ����������
	 */
	public void updateFydVO(AggregatedValueObject billVO)
			throws BusinessException {
		if (null != billVO) {
			TbShipentryBVO[] shbVO = null;
			TbShipentryBVO shbvo = null;
			TbFydmxnewVO[] fydmxVO = null;
			TbFydmxnewVO fydmxvo = null;
			// ��ȡ���ݿ���ʶ���
			IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
					.lookup(IVOPersistence.class.getName());
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			ivo.updateVO(fydVO); // ��������
			fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			ivo.updateVOArray(fydmxVO); // �����ӱ�
			// �ƻ�¼���ӱ�������
			shbVO = new TbShipentryBVO[fydmxVO.length];
			// ѭ���ӱ�����
			for (int i = 0; i < fydmxVO.length; i++) {
				fydmxvo = fydmxVO[i];
				shbvo = new TbShipentryBVO();
				shbvo.setSeb_pk(fydmxvo.getSeb_pk()); // ¼���ӱ�����
				shbvo.setSeb_travel(fydmxvo.getCfd_xs()); // ������
				shbvo.setSe_pk(fydVO.getSe_pk()); // ¼����������
				shbVO[i] = shbvo;

			}
			// ���÷�������¼���ӱ����������
			this.updateShipentryBVO(shbVO);

		}
	}

}
