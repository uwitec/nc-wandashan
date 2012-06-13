package nc.bs.wl.pub;
import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.IQueryData;
import nc.bs.pub.pf.IQueryData2;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.pf.changeui02.VotableVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 
 * @author mlr
 * 
 * 参照查询 对限制条件的创建临时表查询(主要针对in限制语句)
 *
 */
public class RefBillPubBO {
private static TempTableUtil tt = null;	
private  BaseDAO m_dao = null;
	
	private  BaseDAO getDao(){
		if(m_dao == null){
			m_dao = new BaseDAO();
		}
		return m_dao;
	}

	private static TempTableUtil getTempTableUtil(){
		if(tt==null){
			tt=new TempTableUtil();
		}
		return tt;
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        参照查询 查询表体数据
	 *        调用该类 是有严格条件的
	 *        即 bodyCondition 条件中
	 *        最后的条件语句必须是in 
	 *        例如：hid='3333333333' and pk_invmandoc in 
	 * @时间：2011-7-5上午09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj 数组in中的参数
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] queryBodyByClassCondtion(String billType, String id, String bodyCondition,Object oUserObj)throws Exception {		
		if(oUserObj==null ){
			oUserObj=new String[]{"errorPk"};
		}
	    if(!(oUserObj instanceof String[])){
	    	throw new BusinessException("传入的用户对象必须为数组类型");
	    }
	    String[] pks=(String[])oUserObj;
	    if(pks.length==0){
	       pks=new String[]{"errorPk"};	
	    }
	    String sub =getTempTableUtil().getSubSql(pks);
	    String whereSql=bodyCondition+sub;		    
		return queryBodyAllData(billType,id,whereSql);
	} 
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *        参照查询 查询表头数据
	 *        调用该类 是有严格条件的
	 *        即 tmpWhere 条件中
	 *        最后的条件语句必须是in 
	 *        例如：hid='3333333333' and pk in ( select pk_invmandoc from bd_invmancoc where pk_invmandoc in
	 * @时间：2011-7-5上午09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj 数组in中的参数
	 * @return
	 * @throws Exception
	 */
	public static nc.vo.pub.CircularlyAccessibleValueObject[] queryByClassCondtion(String billType,String businessType,String tmpWhere,Object oUserObj) throws Exception {

		if(oUserObj==null){
			oUserObj=new String[]{"errorPk"};
		}
	    if(!(oUserObj instanceof String[])){
	    	throw new BusinessException("传入的用户对象必须为数组类型");
	    }
	    String[] pks=(String[])oUserObj;
	    if(pks.length==0){
		       pks=new String[]{"errorPk"};	
		}
	    String sub =getTempTableUtil().getSubSql(pks);
	    String whereSql=tmpWhere+sub+")";		
		return queryHeadAllData(billType,businessType,whereSql);
	}
	
	public static CircularlyAccessibleValueObject[] queryHeadAllData(String billType, String businessType,
			String whereString) throws BusinessException {
		Logger.info("*****查询单据的表头数据开始*****");
		// 整合后的条件语句
		StringBuffer retWhere = new StringBuffer();
		String referClzName = getReferClzName(billType, businessType, whereString, retWhere);
		if (referClzName == null || referClzName.equals("")) {
			Logger.error("未注册实现查询数据的Dmo端类文件");
			throw new PFBusinessException("未注册实现查询数据的Dmo端类文件");
		}
		Logger.info("查询主表的条件语句：" + retWhere);
		IQueryData tmpObj = (IQueryData) PfUtilTools.instantizeObject(billType, referClzName.trim());	
		CircularlyAccessibleValueObject[] retVos = tmpObj.queryAllHeadData(retWhere.toString());
		Logger.info("*****查询单据的表头数据结束*****");
		return retVos;
	}
	private static String getReferClzName(String billType, String busiType, String whereString,
			StringBuffer retWhere) {
		BilltypeVO billTypeVO = PfDataCache.getBillTypeInfo(billType);
		// 固化的条件语句与传入的语句进行整合
		boolean isExistDbWhere = true;
		if (billTypeVO.getWherestring() != null && !(billTypeVO.getWherestring().trim().equals(""))) {
			retWhere.append(" ").append(billTypeVO.getWherestring());
		} else {
			isExistDbWhere = false;
			retWhere.append(" ");
		}
		// 判断是否有界面查询条件
		if (whereString != null && (!whereString.trim().equals(""))) {
			if (isExistDbWhere) {
				retWhere.append(" and ").append(whereString);
			} else {
				retWhere.append(whereString);
			}
		}
		// 增加业务类型
		VotableVO votable = PfDataCache.getBillTypeToVO(billType, true);
		String strBusiType = votable.getBusitype();
		if (retWhere.length() > 10) {
			if (busiType != null && !busiType.trim().equals("")) {
				retWhere.append(" and ").append(strBusiType).append("='").append(busiType).append("'");
			}
		} else
			retWhere.delete(0, retWhere.length());
		String referClsName = billTypeVO.getReferclassname();
		return referClsName;
	}
	public static CircularlyAccessibleValueObject[] queryBodyAllData(String billType, String parentPK,
			String bodyCondition) throws BusinessException {
		CircularlyAccessibleValueObject[] retVos = null;
		Logger.info("*****查询单据的表体数据开始*****");
		// 定义类
		BilltypeVO billReferVo = PfDataCache.getBillTypeInfo(billType);
		if (billReferVo.getReferclassname() == null || billReferVo.getReferclassname().equals("")) {
			Logger.error("未注册实现查询数据的Dmo端类文件");
			throw new PFBusinessException("未注册实现查询数据的Dmo端类文件");
		}
		// 分模块实例化查询类
		Object queryObj = PfUtilTools
				.instantizeObject(billType, billReferVo.getReferclassname().trim());
		if (bodyCondition == null || bodyCondition.trim().equals("")) {
			Logger.info("执行实现接口IQueryData的数据");
			IQueryData tmpObj = (IQueryData) queryObj;
			retVos = tmpObj.queryAllBodyData(parentPK);
		} else {
			Logger.info("执行实现接口IQueryData2的数据");
			Logger.info("执行实现接口IQueryData2的数据,子表查询条件为:" + bodyCondition);
			IQueryData2 tmpObj = (IQueryData2) queryObj;
			retVos = tmpObj.queryAllBodyData(parentPK, bodyCondition);
		}
		Logger.info("*****查询单据的表体数据结束*****");
		return retVos;
	}

}
