package nc.vo.zmpub.pub.report;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * SuperVO与CircularlyAccessibleValueObject转换工具 报表调用平台行业TreeGather组件汇总树数据时调用
 * 注意：仅限报表使用，单据进行树汇总请使用该单据的VO 作者：薛恩平 创建日期：2006-4-20 13:05:07
 */
public class SuperVOCircularlyVOConvertTool {

	/**
	 * CircularlyAccessibleValueObject数组转为SuperVO数组 报表树汇总前调用 作者：薛恩平
	 * 创建日期：2006-4-20 13:43:05
	 * 
	 * @param cirVOs
	 * @return
	 */
	public static SuperVO[] circularlyVOToSuperVO(
			CircularlyAccessibleValueObject[] cirVOs) {

		if (cirVOs == null && cirVOs.length == 0)
			return null;

		String[] cirAttrNames = cirVOs[0].getAttributeNames();
		ReportTreeGatherVO[] supVOs = new ReportTreeGatherVO[cirVOs.length];
		for (int i = 0; i < cirVOs.length; i++) {
			supVOs[i] = new ReportTreeGatherVO();
			for (int j = 0; j < cirAttrNames.length; j++) {
				supVOs[i].setAttributeValue(cirAttrNames[j], cirVOs[i]
						.getAttributeValue(cirAttrNames[j]));
			}
		}

		return supVOs;
	}

	/**
	 * SuperVO数组转为CircularlyAccessibleValueObject数组 报表树汇总后调用 作者：薛恩平
	 * 创建日期：2006-4-20 13:43:08
	 * 
	 * @param supVOs
	 * @return
	 */
	public static CircularlyAccessibleValueObject[] superVOTocircularlyVO(
			SuperVO[] supVOs) {

		if (supVOs == null && supVOs.length == 0)
			return null;

		String[] supAttrNames = supVOs[0].getAttributeNames();
		ReportTreeGatherVO[] cirVOs = new ReportTreeGatherVO[supVOs.length];
		for (int i = 0; i < supVOs.length; i++) {
			cirVOs[i] = new ReportTreeGatherVO();
			for (int j = 0; j < supAttrNames.length; j++) {
				cirVOs[i].setAttributeValue(supAttrNames[j], supVOs[i]
						.getAttributeValue(supAttrNames[j]));
			}
		}

		return supVOs;
	}

	/**
	 * SuperVOCircularlyVOConvertTool 构造子注解
	 * 
	 */
	public SuperVOCircularlyVOConvertTool() {
		super();
	}
}
