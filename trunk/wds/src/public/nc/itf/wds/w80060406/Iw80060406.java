package nc.itf.wds.w80060406;

import java.text.ParseException;

import nc.vo.pub.BusinessException;
import nc.vo.wds.w80060406.TbFydmxnewVO;

public interface Iw80060406 {

	/**
	 * 计划拆分中的查询明细按钮方法 根据条件查询出所有的值 返回拆除明细数组
	 * 
	 * @param strWhere
	 *            查询条件
	 * @param stock
	 *            到货站
	 * @param begindate
	 *            开始时间
	 * @param enddate
	 *            结束时间
	 * @param stockr
	 *            发货站
	 * @return
	 * @throws BusinessException
	 * @throws ParseException
	 */
	public abstract TbFydmxnewVO[] queryFydmxnewVO(String strWhere,
			String stock, String begindate, String enddate, String stockr)
			throws BusinessException, ParseException;

}
