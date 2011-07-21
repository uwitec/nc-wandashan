package nc.bs.zb.pub;

import nc.bs.pub.formulaparse.FormulaParse;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.parmset.ParamSetVO;

/**
 * 
 * @author zhf
 * 鹤岗项目 后台公共工具类
 *
 */

public class ZbBsPubTool {
	
//	/常用结果集处理器定义
	public static final ColumnProcessor COLUMNPROCESSOR = new ColumnProcessor();
	public static final ColumnListProcessor COLUMNLISTPROCESSOR = new ColumnListProcessor();
	public static final ArrayListProcessor ARRAYLISTPROCESSOR = new ArrayListProcessor();
	public static final ArrayProcessor ARRAYPROCESSOR = new ArrayProcessor();
	
	private static FormulaParse fp = new FormulaParse();
	//后台公式执行
	public static final Object execFomular(String fomular,String[] names,String []values) throws BusinessException{
		fp.setExpress(fomular);
		if(names.length!=values.length){
			throw new BusinessException("传入参数异常");
		}
		int index = 0;
		for(String name:names){
			fp.addVariable(name, values[index]);
			index ++;
		}		
		return fp.getValue();
	}
	
	// 自定义项和预留字段的转换
	public static void dealDef(SuperVO avo, SuperVO bvo) {
		for (int i = 1; i < 21; i++) {
			avo.setAttributeValue("pk_defdoc" + i, PuPubVO
					.getString_TrimZeroLenAsNull(bvo
							.getAttributeValue("pk_defdoc" + i)));
			avo.setAttributeValue("vdef" + i, PuPubVO
					.getString_TrimZeroLenAsNull(bvo.getAttributeValue("vdef"
							+ i)));
		}
		for (int n = 1; n < 6; n++) {
			avo.setAttributeValue("reserve" + n, PuPubVO
					.getString_TrimZeroLenAsNull(bvo
							.getAttributeValue("reserve" + n)));
		}
		for (int n = 6; n < 11; n++) {
			avo.setAttributeValue("reserve" + n, PuPubVO
					.getUFDouble_ZeroAsNull(bvo
							.getAttributeValue("reserve" + n)));
		}
		for (int n = 11; n < 14; n++) {
			avo.setAttributeValue("reserve" + n, PuPubVO
					.getString_TrimZeroLenAsNull(bvo
							.getAttributeValue("reserve" + n)));
		}
		for (int n = 14; n < 17; n++) {
			avo.setAttributeValue("reserve" + n, PuPubVO.getUFBoolean_NullAs(
					(bvo.getAttributeValue("reserve" + n)), UFBoolean.FALSE));
		}
	}

	public static ParamSetVO  getParam() throws BusinessException{
		ParamSetVO [] vos = (ParamSetVO[])new HYPubBO().queryByCondition(ParamSetVO.class, "isnull(dr,0)=0 and pk_corp = '"+SQLHelper.getCorpPk()+"'");
		if(vos == null || vos.length == 0||vos.length>1)
			throw new BusinessException("招标参数设置异常");
		return vos[0];
	}
	
}
