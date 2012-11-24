package nc.vo.wl.pub;
import nc.vo.scm.sourcebill.LightBillVO;
/**
 * 联查单据查找器接口
 * @author mlr
 *
 */
public interface IBillFinder2 {
	/**
	 * 返回具体的单据数据查找器。
	 * 创建日期：(2004-6-21 20:02:15)
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception 异常说明。
	 */
	IBillDataFinder2 createBillDataFinder(String billType) throws java.lang.Exception;
	/**
	 * 返回数据来源对应的所有单据类型
	 * 创建日期：(2004-6-21 19:56:37)
	 * @return java.lang.String[]
	 */
	String[] getAllBillType();
	/**
	 * 返回单据关系的VO。
	 * 创建日期：(2004-6-21 19:58:46)
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception 异常说明。
	 */
	LightBillVO queryBillGraph(String id, String type,String code) throws java.lang.Exception;

}
