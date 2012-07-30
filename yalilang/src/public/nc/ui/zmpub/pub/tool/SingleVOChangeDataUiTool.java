package nc.ui.zmpub.pub.tool;

import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 单表数据交换 用于前台
 * 
 * @author zhf
 */
public class SingleVOChangeDataUiTool {
	/**
	 * 
	 * @author zhf
	 * @说明 获取vo转换类实例
	 * @时间 2010-9-26上午10:47:52
	 * @param classname
	 * @return
	 */
	private static nc.vo.pf.change.IchangeVO getChangeClass(String classname)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(classname) == null)
			return null;
		try {
			Class c = Class.forName(classname);
			Object o = c.newInstance();
			return (nc.vo.pf.change.IchangeVO) o;
		} catch (Exception ex) {
			Logger.error(ex.getMessage(), ex);
			throw ex;
		}
		// return null;
	}

	/**
	 * 
	 * @author zhf
	 * @说明 单vo数据交换 该方法后台调用 前台不能使用
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类
	 * @throws Exception
	 */
	public static void runChangeVO(SuperVO souVo, SuperVO tarVo,
			String chanclassname) throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversionUI)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();

		preBillVo.setParentVO(souVo);
		tarBillVo.setParentVO(tarVo);
		AbstractConversion achange = (AbstractConversion) change;
		// achange.setSourceBilltype(souBilltype);
		// achange.setDestBilltype(destBilltype);
		achange.retChangeBusiVO(preBillVo, tarBillVo);
	}

	public static SuperVO[] runChangeVOAry(SuperVO[] souVos, Class tarVoClass,
			String chanclassname) throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversionUI)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}

		int len = souVos.length;
		if (len <= 0)
			return null;
		// SuperVO[] tarVos = new SuperVO[len];
		SuperVO[] tarVos = (SuperVO[]) java.lang.reflect.Array.newInstance(
				tarVoClass, souVos.length);
		SuperVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = (SuperVO) tarVoClass.newInstance();
			tarVos[i] = tmp;
		}

		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();
		int index = 0;
		for (SuperVO souVo : souVos) {
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			// achange.setSourceBilltype(souBilltype);
			// achange.setDestBilltype(destBilltype);
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index++;
		}

		return tarVos;
	}

	private static HYBillVO tmpBillVo1 = null;

	private static HYBillVO getTmpBIllVo1() {
		if (tmpBillVo1 == null) {
			tmpBillVo1 = new HYBillVO();
		}
		return tmpBillVo1;
	}

	private static HYBillVO tmpBillVo2 = null;

	private static HYBillVO getTmpBIllVo2() {
		if (tmpBillVo2 == null) {
			tmpBillVo2 = new HYBillVO();
		}
		return tmpBillVo2;
	}
}
