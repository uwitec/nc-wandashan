package nc.itf.wds.w80060604;

import java.util.List;

import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;

public interface Iw80060604 {

	/**
	 * 根据前台传来的集合进行插入发运单和回写销售主表中的打印次数和打印时间
	 * 
	 * @param fydList
	 *            发运单主表集合
	 * @param fydmxList
	 *            发运单子表集合
	 * @param saletempList
	 *            销售主表集合
	 * @throws Exception
	 */
	public void insertFyd(List<TbFydnewVO> fydList,
			List<TbFydmxnewVO[]> fydmxList, List<SoSaleVO> saletempList)
			throws Exception;

	/**
	 * 更新操作类别
	 * 
	 * @param list
	 *            需要更新的集合
	 * @throws Exception
	 */
	public void updateSosale(List list) throws Exception;

}
