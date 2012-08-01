package nc.bs.zmpub.pub.tool;

import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zmpub.pub.bill.MyBillVO;

/**
 * 该工具类主要用于单表之间的数据交换 交换类 交换字段 必须以H_开头 交换类必须放到 public包里面
 * 
 * @author zhf
 * 
 */
public class SingleVOChangeDataBsTool {
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
	 *            vo交换类 vo交换类的书写方式 都以H_开头 如：H_cdeptid->H_pk_deptdoc
	 * 
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

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();

		preBillVo.setParentVO(souVo);
		tarBillVo.setParentVO(tarVo);
		AbstractConversion achange = (AbstractConversion) change;
		achange.retChangeBusiVO(preBillVo, tarBillVo);

	}

	public static SuperVO[] runChangeVOAry(SuperVO[] souVos, Class tarVoClass,
			String chanclassname) throws Exception {

		int len = souVos.length;
		if (len <= 0)
			return null;
		SuperVO[] tarVos = (SuperVO[]) java.lang.reflect.Array.newInstance(
				tarVoClass, len);
		SuperVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = (SuperVO) tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}

	/**
	 * 支持聚合vo->单表vo 现存量 表都看作表体 即 b_ss->h_b b_s1->b_s1 的交换
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-14上午11:21:51
	 * 
	 */
	public static SuperVO[] runChangeVOAry(AggregatedValueObject souVos,
			Class tarVoClass, String chanclassname) throws Exception {
		if (souVos == null) {
			return null;
		}

		int len = souVos.getChildrenVO().length;
		if (len <= 0)
			return null;
		SuperVO[] tarVos = (SuperVO[]) java.lang.reflect.Array.newInstance(
				tarVoClass, len);
		SuperVO tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = (SuperVO) tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}

	private static void runChangeVOAry(AggregatedValueObject souVos,
			SuperVO[] tarVos, String chanclassname) throws BusinessException {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = souVos;

		AggregatedValueObject tarBillVo = getTmpBIllVo22();
		tarBillVo.setChildrenVO(tarVos);
		AbstractConversion achange = (AbstractConversion) change;
		achange.retChangeBusiVO(preBillVo, tarBillVo);

	}

	/**
	 * 
	 * @author mlr 支持CircularlyAccessibleValueObject类型
	 * @说明：（鹤岗矿业） 2011-10-21下午02:14:50
	 * @param souVos
	 * @param tarVoClass
	 * @param chanclassname
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] runChangeVOAry(
			CircularlyAccessibleValueObject[] souVos, Class tarVoClass,
			String chanclassname) throws Exception {

		int len = souVos.length;
		if (len <= 0)
			return null;
		CircularlyAccessibleValueObject[] tarVos = (CircularlyAccessibleValueObject[]) java.lang.reflect.Array
				.newInstance(tarVoClass, len);
		CircularlyAccessibleValueObject tmp = null;
		for (int i = 0; i < len; i++) {
			tmp = (CircularlyAccessibleValueObject) tarVoClass.newInstance();
			tarVos[i] = tmp;
		}
		runChangeVOAry(souVos, tarVos, chanclassname);

		return tarVos;
	}

	/**
	 * 
	 * @author mlr
	 * @说明 单vo数据交换 该方法后台调用 前台不能使用
	 * @时间 2010-9-26上午11:30:42
	 * @param souVo
	 *            来源vo
	 * @param tarVo
	 *            目标vo
	 * @param chanclassname
	 *            vo交换类 vo交换类的书写方式 都以H_开头 如：H_cdeptid->H_pk_deptdoc
	 * 
	 * @throws Exception
	 */
	public static void runChangeVOAry(CircularlyAccessibleValueObject[] souVos,
			CircularlyAccessibleValueObject[] tarVos, String chanclassname)
			throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo11();
		AggregatedValueObject tarBillVo = getTmpBIllVo22();
		int index = 0;
		for (CircularlyAccessibleValueObject souVo : souVos) {
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index++;
		}
	}

	// CircularlyAccessibleValueObject
	public static void runChangeVOAry(SuperVO[] souVos, SuperVO[] tarVos,
			String chanclassname) throws Exception {
		IchangeVO change = null;
		try {
			change = getChangeClass(chanclassname);
		} catch (Exception e) {// 可能存在类型转换异常 此处要求
			// changeClassName类需要继承VOConversion
			e.printStackTrace();
			throw new BusinessException(e);
		}

		if (!(change instanceof VOConversion)) {
			throw new BusinessException("数据转换组件异常，" + change.toString());
		}
		AggregatedValueObject preBillVo = getTmpBIllVo1();
		AggregatedValueObject tarBillVo = getTmpBIllVo2();
		int index = 0;
		for (SuperVO souVo : souVos) {
			preBillVo.setParentVO(souVo);
			tarBillVo.setParentVO(tarVos[index]);
			AbstractConversion achange = (AbstractConversion) change;
			achange.retChangeBusiVO(preBillVo, tarBillVo);
			index++;
		}

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

	private static MyBillVO tmpBillVo11 = null;

	private static MyBillVO getTmpBIllVo11() {
		if (tmpBillVo11 == null) {
			tmpBillVo11 = new MyBillVO();
		}
		return tmpBillVo11;
	}

	private static MyBillVO tmpBillVo22 = null;

	private static MyBillVO getTmpBIllVo22() {
		if (tmpBillVo22 == null) {
			tmpBillVo22 = new MyBillVO();
		}
		return tmpBillVo22;
	}

	
}
