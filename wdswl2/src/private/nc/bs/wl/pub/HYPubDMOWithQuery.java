package nc.bs.wl.pub;

import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.trade.business.HYSuperDMO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 参照查询对应的DMO基类.
 * @author zpm
 *
 */

public abstract class HYPubDMOWithQuery extends HYSuperDMO implements IQueryData, IQueryData2 {

	public HYPubDMOWithQuery() {
		super();
	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key) throws BusinessException {
		return queryAllBodyData(key, null);
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] vos  = null;
		try{
			vos  = queryAllHeadData(getHeadVOClass(), whereString);
		}catch(Exception e){
			throw new BusinessException(e);
		}
		return vos;
	}

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key, String whereString) throws BusinessException {
		CircularlyAccessibleValueObject[] retVos = null;
		try{
			retVos = queryAllBodyData(getBillType(), getBodyVOClass(), key, whereString);
		}catch(Exception e){
			throw new BusinessException(e);
		}
		return retVos;
	}
	/**
	 * 单据类型
	 */
	protected abstract String getBillType();

	/**
	 * 表头VO类
	 */
	protected abstract Class getHeadVOClass();

	/**
	 * 表体VO类
	 */
	protected abstract Class getBodyVOClass();

}
