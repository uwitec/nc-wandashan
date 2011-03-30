package nc.bs.hg.pu.pub;

import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.trade.business.HYSuperDMO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/*
 * 参照查询对应的DMO基类.
 * */

public abstract class HYPubDMOWithQuery extends HYSuperDMO implements IQueryData, IQueryData2 {

	public HYPubDMOWithQuery() {
		super();
	}

	/**
	 * 此处方法说明为根据主表的关键字段进行查询子表的数据。 创建日期：(2001-7-9 20:06:40)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject
	 * @param key
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key) throws BusinessException {
		return queryAllBodyData(key, null);
	}

	/**
	 * 此处方法说明为根据查询条件获得的条件约束及在固化 的条件语句组成条件语句及该方法中定义的的sql语句 进行组合查询。 创建日期：(2001-7-9
	 * 19:58:37)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @param whereString
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] vos = null;

		vos = queryAllHeadData(getHeadVOClass(), whereString);

		return vos;
	}

	/**
	 * 此处方法说明为根据主表的关键字段与查询条件进行查询子表的数据。 创建日期：(2001-7-9 20:06:40)
	 * 
	 * @return nc.vo.pub.CircularlyAccessibleValueObject
	 * @param key
	 *            java.lang.String
	 */
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] retVos = null;
		retVos = queryAllBodyData(getBillType(), getBodyVOClass(), key, whereString);
		return retVos;
	}

	/**
	 * 单据类型
	 * 
	 * @return twh (2006-10-26 上午09:24:53)<br>
	 */
	protected abstract String getBillType();

	/**
	 * 表头VO类
	 * 
	 * @return twh (2006-10-26 上午09:25:03)<br>
	 */
	protected abstract Class getHeadVOClass();

	/**
	 * 表体VO类
	 * 
	 * @return twh (2006-10-26 上午09:25:15)<br>
	 */
	protected abstract Class getBodyVOClass();

}
