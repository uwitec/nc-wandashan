package nc.bs.zmpub.pub.excel;

import java.util.ArrayList;
import java.util.List;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.pub.bill.MyBillVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * Excel数据处理
 * 
 * @author zhf
 */
public abstract class AbstractExcetBO {

	/**
	 * 对单体excel导入的数据进行后台处理
	 * @param vos
	 * @throws BusinessException
	 */
	public void dealSingleImportDatas(CircularlyAccessibleValueObject[] vos) throws BusinessException{
		if(vos == null || vos.length == 0)
			return;
		check(vos);
		TransCodeToIDBO.getInstance().transCodeToID(vos, getBodyTransFieldInfor());
		doSingleSave(vos);		
	}
	
	/**
	 * 单体导入数据校验  必须严密校验 否则会影响后续的编码转换
	 * @param vos
	 * @throws ValidationException
	 */
	protected abstract void check(CircularlyAccessibleValueObject[] vos) throws ValidationException;
	
	/**
	 * 单体导入的数据保存  子类个性化实现
	 * @param vos
	 * @throws BusinessException
	 */
	protected abstract void doSingleSave(CircularlyAccessibleValueObject[] vos) throws BusinessException;
	
	public void dealBillImportDatas(ReportBaseVO[] rvos,String billtype,String tmpSourBilltype) throws Exception{
		if(rvos == null || rvos.length == 0)
			return;
		if(PuPubVO.getString_TrimZeroLenAsNull(billtype) == null)
			throw new BusinessException("单据类型为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(tmpSourBilltype) == null){
			tmpSourBilltype = "EXCEL";
		}
		
//		数据转换
		AggregatedValueObject[] bills = transData(rvos,billtype);
		
		AggregatedValueObject[] newBills = PfUtilTools.runChangeDataAry(tmpSourBilltype, billtype, bills);
		
		if(newBills == null || newBills.length == 0)
			throw new BusinessException("数据转换异常");
		
//		数据校验
		check(newBills);
//		编码转换
		transCodeToID(newBills);
//		数据补充
		appData(newBills);
//		推事保存
		pushBillSave(newBills,billtype);
//		保存后处理
		afterSave(newBills);
	}
	
	protected abstract void afterSave(AggregatedValueObject[] newBills);
	
	/**
	 * 推事保存
	 * @param newBills
	 * @param billtype
	 * @throws Exception
	 */
	protected void pushBillSave(AggregatedValueObject[] newBills,String billtype) throws Exception{
		PfUtilBO pfbo = new PfUtilBO();
		if (isPushSaveBatch()) {
			pfbo.processBatch(getPushSaveActionName(), billtype, new UFDate(
					System.currentTimeMillis()).toString(), newBills, null, null);
		}else{
			String date = new UFDate(System.currentTimeMillis()).toString();
			for(AggregatedValueObject newBill:newBills){
				pfbo.processAction(getPushSaveActionName(), billtype, date, null, newBill, null);
			}
		}
	}
	
	
	/**
	 * 单据结构导入数据  编码转换
	 * @param newBills
	 * @throws BusinessException
	 */
	protected  void transCodeToID(AggregatedValueObject[] newBills) throws BusinessException{
		CircularlyAccessibleValueObject[] heads = new CircularlyAccessibleValueObject[newBills.length];
		int index = 0;
		CircularlyAccessibleValueObject[] bodys = null;
		for(AggregatedValueObject newBill:newBills){
			heads[index] = newBill.getParentVO();
			bodys = newBill.getChildrenVO();
			if(bodys == null || bodys.length == 0)
				continue;
			for(CircularlyAccessibleValueObject body:bodys){
				body.setAttributeValue("pk_custom1", heads[index].getAttributeValue("pk_corp"));
			}
			TransCodeToIDBO.getInstance().transCodeToID(bodys, getBodyTransFieldInfor());
			index++;
		}
		
		TransCodeToIDBO.getInstance().transCodeToID(heads, getHeadTransFieldInfor());
		TransCodeToIDBO.getInstance().clearCathe();
	}
	
	/**
	 * 获取表头需要进行编码转换ID的字段
	 * @return
	 */
	protected abstract CodeToIDInfor[] getHeadTransFieldInfor();
	/**
	 * 获取表体需要进行编码转换ID的字段  如果未单体时  为单体需要进行编码转换ID的字段
	 * 表体若存在  公司级 档案 必须提供  公司编码的 转换  若表体vo不含有 公司  可以启用临时字段 用于公司
	 * 否则无法完成公司级表体编码向ID的转换
	 * @return
	 */
	protected abstract CodeToIDInfor[] getBodyTransFieldInfor();
	protected abstract boolean isPushSaveBatch();
	protected abstract String getPushSaveActionName() throws BusinessException;
	protected abstract void appData(AggregatedValueObject[] newBills) throws BusinessException;
	
	protected abstract void check(AggregatedValueObject[] newBills) throws ValidationException;
	
	protected abstract void beForeTransData(ReportBaseVO[] rvos);//支持子类扩展功能	
    protected abstract void afterTransData(ReportBaseVO[] rvos,AggregatedValueObject[] bills);//支持子类扩展功能		
    
    protected String getHeadFlag(){
    	return "head";
    }
	
    /**
     * 将数据封转成标准聚合vo
     * @param rvos
     * @param billtype
     * @return
     * @throws BusinessException
     */
	protected AggregatedValueObject[] transData(ReportBaseVO[] rvos,String billtype) throws BusinessException{
		beForeTransData(rvos);
		
		String[] voNames = PfDataCache.getStrBillVo(billtype);
		if(voNames == null || voNames.length == 0){
			throw new BusinessException("vo信息未注册");
		}
		
		if(!PuPubVO.getUFBoolean_NullAs(rvos[0].getAttributeValue(getHeadFlag()), UFBoolean.FALSE).booleanValue()){
			throw new BusinessException("表头信息为空");
		}
		
		List<MyBillVO> lbill = new ArrayList<MyBillVO>();
		MyBillVO bill = null;
		for(ReportBaseVO rvo:rvos){			
			if(PuPubVO.getUFBoolean_NullAs(rvo.getAttributeValue(getHeadFlag()), UFBoolean.FALSE).booleanValue()){
				bill = new MyBillVO();
				bill.setParentVO(rvo);
				lbill.add(bill);
			}else{
				lbill.get(lbill.size()-1).getLbody().add(rvo);
			}
			
		}
		
		if(lbill.size() <= 0)
			return null;
		
		for(MyBillVO tmpBill:lbill){
			List<CircularlyAccessibleValueObject> lbody = tmpBill.getLbody();
			if(lbody == null || lbody.size() == 0)
				continue;
			
			tmpBill.setChildrenVO(lbody.toArray(new CircularlyAccessibleValueObject[0]));
		}			
		
		if(lbill==null || lbill.size() == 0)
			return null;
		AggregatedValueObject[] bills = lbill.toArray(new AggregatedValueObject[0]);
		
		afterTransData(rvos,bills);
		return bills;
	}	
	
	
	
	
	
	
	
	
//	
//	
////---------------------------------------------以下是mlr版----------------------------------------------------
//	/**
//	 * 从excelt 报表vo到 期初数据vo
//	 * 
//	 * @author mlr
//	 * @说明：（鹤岗矿业） 2011-11-4下午03:21:24
//	 * @param vos
//	 * @param m_user
//	 * @param logDate
//	 * @return
//	 * @throws Exception
//	 */
//	public SuperVO[] excelChangeToVO(String returnClass, ReportBaseVO[] vos,
//			String[] fieldsNames, String queryIds[], String queryTables[],
//			String querySelectIDs[], String[] queryCodeNames,
//			boolean[] isVlCorp, boolean[] isMutiTables, String[] setValueIds,
//			String pk_corp, String m_user, String logDate) throws Exception {
//		if (vos == null || vos.length == 0)
//			return null;
//		List<SuperVO> list = new ArrayList<SuperVO>();
//		String[] chafields = getNotQueryFields(queryIds, fieldsNames);
//		Class cl = Class.forName(returnClass);
//		for (int i = 0; i < vos.length; i++) {
//			SuperVO vo = (SuperVO) Class.forName(returnClass).newInstance();
//			for (int j = 0; j < queryIds.length; j++) {
//				String code = PuPubVO.getString_TrimZeroLenAsNull(vos[i]
//						.getAttributeValue(queryIds[j]));
//				String id = (String) querypkall(querySelectIDs[j],
//						queryTables[j], queryCodeNames[j], code, isVlCorp[j],
//						pk_corp, isMutiTables[j]);
//				vo.setAttributeValue(setValueIds[j], id);
//			}
//			for (int l = 0; l < chafields.length; l++) {
//				Object value = vos[i].getAttributeValue(chafields[l]);
//				vo.setAttributeValue(chafields[l], value);
//			}
//			vo.setAttributeValue("pk_corp", pk_corp);
//			list.add(vo);
//		}
//		if (list.size() != 0)
//			return list.toArray((SuperVO[]) java.lang.reflect.Array
//					.newInstance(cl, 0));
//		return null;
//	}
//
//	private String[] getNotQueryFields(String[] queryIds, String[] fields)
//			throws Exception {
//		List<String> list = new ArrayList<String>();
//		for (int i = 0; i < fields.length; i++) {
//			boolean flag = false;
//			for (int j = 0; j < queryIds.length; j++) {
//				if (fields[i].equalsIgnoreCase(queryIds[j])) {
//					flag = true;
//					break;
//				}
//			}
//			if (flag == false) {
//				list.add(fields[i]);
//			}
//		}
//		if (list.size() != 0)
//			return list.toArray(new String[0]);
//		return null;
//	}
//
//	/**
//	 * liuys 根据各种档案编码查询其pK值
//	 * 
//	 * @param tablename
//	 * @param selectname
//	 * @param passvalue
//	 * @param isMutitable
//	 *            是否多张表联 如果是 就不能过滤dr了
//	 * @return
//	 * @throws DAOException
//	 */
//	public Object querypkall(String selectname, String tablename,
//			String codename, String passvalue, boolean bag, String pk_corp,
//			boolean isMutitable) throws DAOException {
//		String sqlpkcorp = "select " + selectname + " from " + tablename
//				+ " where " + codename + " = '" + passvalue + "'";
//		if (isMutitable == false) {
//			sqlpkcorp = sqlpkcorp + "  and isnull(dr,0)=0 ";
//		}
//		if (bag == true)
//			sqlpkcorp += " and pk_corp = '" + pk_corp + "'";
//		else {
//			sqlpkcorp += " ;";
//		}
//		Object obj = getDAO().executeQuery(sqlpkcorp,
//				ResultSetProcessorTool.COLUMNPROCESSOR);
//		return obj;
//	}

}
