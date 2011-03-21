package nc.itf.wds.w80060401;

import java.util.ArrayList;
import java.util.List;
 
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;

public interface Iw80060401 {
	/**
	 * 查询出所有的粉类单品
	 * 
	 * @param strWhere
	 *            条件
	 * @return 录入子表数组 只有单品主键有值
	 * @throws BusinessException
	 */
	public abstract TbShipentryBVO[] queryShipentryBVO(String strWhere)
			throws BusinessException;

	public AggregatedValueObject saveBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception;

	public AggregatedValueObject deleteBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception;
}
