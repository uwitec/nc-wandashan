package nc.itf.wds.w80060204;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;

public interface Iw80060204 {

	/**
	 * 合并订单的保存方法 根据传来的BillVO进行后台保存 首先保存的是一个合并后的单据 其次是拆开后的单据 最后是给销售主表中回写状态
	 * 
	 * @param billVO
	 *            聚合VO
	 * @throws Exception
	 */
	public void saveFyd(AggregatedValueObject billVO) throws Exception;

	
}
