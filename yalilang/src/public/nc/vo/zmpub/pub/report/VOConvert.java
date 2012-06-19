package nc.vo.zmpub.pub.report;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
public class VOConvert {
	/**
	 * CircularlyAccessibleValueObject数组转为SuperVO数组 报表树汇总前调用 作者：chengjun
	 * 创建日期：2006-4-20 13:43:05
	 * 
	 * @param cirVOs
	 * @return
	 */
	public static SuperVO[] circularlyVOToSuperVO(CircularlyAccessibleValueObject[] cirVOs) {

		if (cirVOs == null && cirVOs.length == 0)
			return null;

		String[] cirAttrNames = cirVOs[0].getAttributeNames();
		ReportTreeGatherVO[] supVOs = new ReportTreeGatherVO[cirVOs.length];
		for (int i = 0; i < cirVOs.length; i++) {
			supVOs[i] = new ReportTreeGatherVO();
			for (int j = 0; j < cirAttrNames.length; j++) {
				supVOs[i].setAttributeValue(cirAttrNames[j], cirVOs[i].getAttributeValue(cirAttrNames[j]));
			}
		}

		return supVOs;
	}

	/**
	 * SuperVO转为CircularlyAccessibleValueObject  作者：chengjun
	 * 创建日期：2006-4-20 13:43:08
	 * 
	 * @param supVO
	 * @return
	 */
	public static CircularlyAccessibleValueObject superVOTocircularlyVO(SuperVO supVO) {

		if (supVO == null )
			return null;

		String[] supAttrNames = supVO.getAttributeNames();
		ReportBaseVO cirVO = new ReportBaseVO();
			for (int j = 0; j < supAttrNames.length; j++) {
				cirVO.setAttributeValue(supAttrNames[j], supVO.getAttributeValue(supAttrNames[j]));
		}

		return cirVO;
	}

	/**
	 * SuperVOCircularlyVOConvertTool 构造子注解
	 * 
	 */
	public VOConvert() {
		super();
	}
}
