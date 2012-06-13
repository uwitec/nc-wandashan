
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 查询上下游单据的实现类
 * @author mlr
 *
 */
public class LinkQueryFinder extends AbstractBillFinder2{
	public LinkQueryFinder() {
		super();
	}
	/**
	 * 注册对应单据类型的查找器  可以利用if else 语句针对
	 * 不同的单据类型创建对应各个单据类型的查找器
	 * 最终 LinkQueryFinder 类的 queryBillGraph(id,type)
	 * 会调用 对应单据类型注册的数据查器的 getSourceBills(curBillType,curBillType)
	 * 方法 查找来源单据
	 *   调用 对应单据类型注册的数据查找器的  getForwardBills(curBillType,curBillType,forwardBillType)
	 *   forwardBillType为下游单据类型
	 *   来查找下游单据 
	 *   
	 *   
	 * 如果该方法不被重写则 调用默认的查找器
	 */
	public IBillDataFinder2 createBillDataFinder(String billType) throws Exception {
		return super.createBillDataFinder(billType);
	}
	/**
	 * 注册下游的单据类型 
	 */
	public String[] getAllBillType() {
		String type = getCurrentvo().getType();//当前单据类型
		//可以利用当前单据类型 利用if else 语句判断 来注册当前单据类型
		//的下游单据类型		
		return null;
	}

}
