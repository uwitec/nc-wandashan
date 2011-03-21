package nc.itf.wds.w80060401;

import java.util.ArrayList;
import java.util.List;
 
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;

public interface Iw80060401 {
	/**
	 * ��ѯ�����еķ��൥Ʒ
	 * 
	 * @param strWhere
	 *            ����
	 * @return ¼���ӱ����� ֻ�е�Ʒ������ֵ
	 * @throws BusinessException
	 */
	public abstract TbShipentryBVO[] queryShipentryBVO(String strWhere)
			throws BusinessException;

	public AggregatedValueObject saveBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception;

	public AggregatedValueObject deleteBD80060401(AggregatedValueObject billVO,
			Object userObj) throws Exception;
}
