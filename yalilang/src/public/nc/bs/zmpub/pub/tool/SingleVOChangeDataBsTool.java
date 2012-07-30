package nc.bs.zmpub.pub.tool;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import nc.bs.logging.Logger;
import nc.bs.pf.change.AbstractConversion;
import nc.bs.pf.change.VOConversion;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.pf.INotifyNextBillMaker;
import nc.vo.pub.pf.IPfBillLock;
import nc.vo.pub.pf.IPfRetBackCheckInfo;
import nc.vo.pub.pf.IPfRetCheckInfo;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.BillField;
import nc.vo.trade.field.IBillField;
import nc.vo.trade.pub.HYBillVO;

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

	/**
	 * 聚合vo
	 * 
	 * @author mlr
	 * 
	 */
	private static class MyBillVO extends AggregatedValueObject implements
			IPfBillLock, IPfRetBackCheckInfo, IPfRetCheckInfo,
			INotifyNextBillMaker {
		/**
 * 
 */
		private static final long serialVersionUID = 1L;

		// 主表VO
		CircularlyAccessibleValueObject m_headVo = null;

		// 子表VO
		CircularlyAccessibleValueObject[] m_itemVos = null;

		// 消息提示
		private String m_hintMessage = null;

		// 是否发送工作流或消息
		private Boolean m_isSendMessage = new Boolean(false);

		// 判定是否进行单据锁定
		private boolean m_isBillLock = true;

		// 对应的单据字段常量信息
		private IBillField m_billField = null;

		/**
		 * @return 返回 m_billField。
		 */
		public IBillField getM_billField() {
			return m_billField;
		}

		/**
		 * @param field
		 *            要设置的 m_billField。
		 */
		public void setM_billField(IBillField field) {
			m_billField = field;
		}

		/**
		 * YcBillVO 构造子注解。
		 */
		public MyBillVO() {
			super();
		}

		/**
		 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
		 * 
		 * @return nc.vo.pub.ValueObject[]
		 */
		public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
			return m_itemVos;
		}

		/**
		 * 返回锁的数组。 创建日期：(2003-6-8 8:01:54)
		 * 
		 * @return java.lang.String[]
		 */
		public java.lang.String[] getLockIdAry() throws BusinessException {
			if (!isBillLock())
				return null;
			Hashtable billKeyHas = new Hashtable();
			if (m_headVo.getPrimaryKey() != null
					&& m_headVo.getPrimaryKey().trim().length() != 0)
				billKeyHas.put(m_headVo.getPrimaryKey(), m_headVo
						.getPrimaryKey());
			if (m_itemVos != null)
				for (int i = 0; i < m_itemVos.length; i++) {
					String vLastId = (String) m_itemVos[i]
							.getAttributeValue(BillField.getInstance()
									.getField_LastBillId());
					if (vLastId == null || vLastId.trim().length() == 0)
						continue;
					else {
						if (billKeyHas.containsKey(vLastId))
							continue;
						else
							billKeyHas.put(vLastId, vLastId);
					}
				}
			String[] keyAry = null;
			if (billKeyHas.size() > 0) {
				keyAry = new String[billKeyHas.size()];
				Enumeration e = billKeyHas.keys();
				int i = 0;
				while (e.hasMoreElements())
					keyAry[i++] = (String) e.nextElement();
			}
			return keyAry;

		}

		/**
		 * 此处插入方法说明。 创建日期：(2003-7-3 8:47:41)
		 * 
		 * @return java.lang.String
		 */
		public java.lang.String getMessage() {
			return m_hintMessage;
		}

		/**
		 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
		 * 
		 * @return nc.vo.pub.ValueObject
		 */
		public CircularlyAccessibleValueObject getParentVO() {
			return m_headVo;
		}

		/**
		 * 此处插入方法说明。 创建日期：(2004-3-12 10:55:39)
		 * 
		 * @return boolean
		 */
		public boolean isBillLock() {
			return m_isBillLock;
		}

		/**
		 * 是否发送工作流。 默认不进行工作流发送 创建日期：(2003-7-6 12:30:36)
		 * 
		 * @return java.lang.Boolean
		 */
		public java.lang.Boolean isSendMessage() {
			return m_isSendMessage;
		}

		/**
		 * 设置单据反审后的审批人 创建日期：(2002-10-16 13:39:32)
		 * 
		 * @param icheckState
		 *            int
		 */
		public void setCheckMan(java.lang.String approveid) {
			getParentVO().setAttributeValue(
					BillField.getInstance().getField_CheckMan(), approveid);
		}

		/**
		 * 设置批语 创建日期：(2002-10-16 13:40:13)
		 * 
		 * @param strCheckNote
		 *            java.lang.String
		 */
		public void setCheckNote(java.lang.String strCheckNote) {
			getParentVO().setAttributeValue(
					BillField.getInstance().getField_CheckNote(), strCheckNote);
		}

		/**
		 * 设置审批状态 创建日期：(2002-10-16 13:39:32)
		 * 
		 * @param icheckState
		 *            int
		 */
		public void setCheckState(int icheckState) {
			getParentVO().setAttributeValue(
					BillField.getInstance().getField_BillStatus(),
					new Integer(icheckState));
		}

		/**
		 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
		 * 
		 * @return nc.vo.pub.ValueObject[]
		 */
		public void setChildrenVO(
				nc.vo.pub.CircularlyAccessibleValueObject[] children) {
			if (children == null) {
				m_itemVos = null;
			} else if (children.length == 0) {
				try {
					m_itemVos = (CircularlyAccessibleValueObject[]) children;
				} catch (ClassCastException e) {
					m_itemVos = null;
				}
			} else {
				List l = Arrays.asList(children);
				m_itemVos = (CircularlyAccessibleValueObject[]) l
						.toArray((Object[]) Array.newInstance(children[0]
								.getClass(), 0));
			}
		}

		/**
		 * 此处插入方法说明。 创建日期：(2004-3-12 10:55:39)
		 * 
		 * @param newIsBillLock
		 *            boolean
		 */
		public void setIsBillLock(boolean newIsBillLock) {
			m_isBillLock = newIsBillLock;
		}

		/**
		 * 此处插入方法说明。 创建日期：(2003-7-4 14:37:37)
		 * 
		 * @param msg
		 *            java.lang.String
		 */
		public void setMessage(java.lang.String msg) {
			m_hintMessage = msg;
		}

		/**
		 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
		 * 
		 * @return nc.vo.pub.ValueObject
		 */
		public void setParentVO(CircularlyAccessibleValueObject parent) {
			m_headVo = (CircularlyAccessibleValueObject) parent;
		}

		/**
		 * 设置是否发送消息标志。 创建日期：(2003-7-6 15:04:37)
		 * 
		 * @param param
		 *            java.lang.Boolean
		 */
		public void setSendMessage(java.lang.Boolean param) {
			m_isSendMessage = param;
		}
	}
}
