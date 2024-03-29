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
	 * 更新发运录入子表中的待发运数量
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
					// 先根据主键查询出是否存有待发运数
					String sql = "select seb_travel from tb_shipentry_b where seb_pk='"
							+ shbvo.getSeb_pk()
							+ "' and se_pk = '"
							+ shbvo.getSe_pk() + "'";
					results = (ArrayList<?>) jdbcSession.executeQuery(sql,
							new ArrayListProcessor());
					UFDouble num = new UFDouble(0);
					double sum = 0;
					if (null != results && !results.isEmpty()) {
						// 获取结果
						num = WDSTools
								.getUFDouble_NullAsZero(((Object[]) results
										.get(0))[0]);
					}
					if (num.toDouble() > 0) {
						// 如果存有待发运数 进行两着相加 当前界面中的和数据库原有的
						sum = num.toDouble() + shbvo.getSeb_travel().toDouble();
					} else {
						sum = shbvo.getSeb_travel().toDouble();
					}
					// 更新数据库中的待发运数量
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
	 * 保存拆分审批
	 */
	public void updateFydVO(AggregatedValueObject billVO)
			throws BusinessException {
		if (null != billVO) {
			TbShipentryBVO[] shbVO = null;
			TbShipentryBVO shbvo = null;
			TbFydmxnewVO[] fydmxVO = null;
			TbFydmxnewVO fydmxvo = null;
			// 获取数据库访问对象
			IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance()
					.lookup(IVOPersistence.class.getName());
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			ivo.updateVO(fydVO); // 更新主表
			fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			ivo.updateVOArray(fydmxVO); // 更新子表
			// 计划录入子表创建对象
			shbVO = new TbShipentryBVO[fydmxVO.length];
			// 循环子表数组
			for (int i = 0; i < fydmxVO.length; i++) {
				fydmxvo = fydmxVO[i];
				shbvo = new TbShipentryBVO();
				shbvo.setSeb_pk(fydmxvo.getSeb_pk()); // 录入子表主键
				shbvo.setSeb_travel(fydmxvo.getCfd_xs()); // 待发运
				shbvo.setSe_pk(fydVO.getSe_pk()); // 录入主表主键
				shbVO[i] = shbvo;

			}
			// 调用方法更新录入子表待发运数量
			this.updateShipentryBVO(shbVO);

		}
	}

}
