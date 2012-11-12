package nc.vo.zmpub.pub.tool;

import nc.vo.pub.BusinessException;

/**
 * 该工具类首先从缓存中取数,如果缓存中没有才查询数据库 这样提高了查询效率
 * 
 * @author zhf
 */
public class ZmPubTool {
	public static final Integer INTEGER_ZERO_VALUE = new Integer(0); // 整数零
	private static nc.bs.pub.formulaparse.FormulaParse fp = new nc.bs.pub.formulaparse.FormulaParse();
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将为null的字符串处理为“”。 2010-11-22下午02:51:02
	 * @param value
	 * @return
	 */
	public static String getString_NullAsTrimZeroLen(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString().trim();
	}
	/**
	 * 用于后台
	 * 
	 * @param fomular
	 *            执行的公式
	 *            如查询仓库编码的公式：storcode->getColValue(bd_strodoc,storcode,pk_stordoc
	 *            ,storid)
	 * @param names
	 *            传入值的名字 如：new String[]{"storid"}
	 * @param values
	 *            传入值 如:new String[]{"0001AE10000000018ES9"}
	 * @return
	 * @throws BusinessException
	 */
	public static final Object execFomular(String fomular, String[] names,
			String[] values) throws BusinessException {
		fp.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("传入参数异常");
		}
		int index = 0;
		for (String name : names) {
			fp.addVariable(name, values[index]);
			index++;
		}
		return fp.getValue();
	}

	private static nc.ui.pub.formulaparse.FormulaParse fpClient = new nc.ui.pub.formulaparse.FormulaParse();

	/**
	 * 用于前台
	 * 
	 * @param fomular
	 *            执行的公式
	 *            如查询仓库编码的公式：storcode->getColValue(bd_strodoc,storcode,pk_stordoc
	 *            ,storid)
	 * @param names
	 *            传入值的名字 如：new String[]{"storid"}
	 * @param values
	 *            传入值 如:new String[]{"0001AE10000000018ES9"}
	 * @return
	 * @throws BusinessException
	 */
	public static final Object execFomularClient(String fomular,
			String[] names, String[] values) throws BusinessException {
		fpClient.setExpress(fomular);
		if (names.length != values.length) {
			throw new BusinessException("传入参数异常");
		}
		int index = 0;
		for (String name : names) {
			fpClient.addVariable(name, values[index]);
			index++;
		}
		return fpClient.getValue();
	}
}
