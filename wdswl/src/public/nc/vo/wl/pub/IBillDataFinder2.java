package nc.vo.wl.pub;
import nc.vo.scm.sourcebill.LightBillVO;
/**
 * 单据上下游查找接口。
 * 创建日期：(2004-6-14 13:08:17)
 * @author：樊冠军1
 */
public interface IBillDataFinder2 {
/*
* 功能:根据当前的单据ID,单据类型,获得指定类型的后续单据.
* 返回:LightBillVO[],后续单据VO数组,至少要填写LightBillVO的ID,TYPE,CODE三个属性.
* 其中TYPE属性就是forwardBillTYPE的参数值
* 参数:
* 1.String curBillType :当前单据类型
* 2.String curBillID:当前单据ID
* 3.String forwardBillType:后续单据的类型
*
*/
public LightBillVO[] getForwardBills(
	String curBillType,
	String curBillID,
	String forwardBillType);
/*
 * 功能:根据当前的单据ID,单据类型,获得所有的来源单据
 * 返回:LightBillVO[],来源单据VO数组,至少要填写LightBillVO的ID,TYPE,CODE三个属性.
 * 参数:
 * 1.String curBillType :当前单据类型
 * 2.String curBillID:当前单据ID
 *
 */
public LightBillVO[] getSourceBills(String curBillType,String curBillID);
}
