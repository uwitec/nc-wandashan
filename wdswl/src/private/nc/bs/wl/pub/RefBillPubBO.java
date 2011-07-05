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
 * ���ղ�ѯ �����������Ĵ�����ʱ���ѯ(��Ҫ���in�������)
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
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ղ�ѯ ��ѯ��������
	 *        ���ø��� �����ϸ�������
	 *        �� bodyCondition ������
	 *        ����������������in 
	 *        ���磺hid='3333333333' and pk_invmandoc in 
	 * @ʱ�䣺2011-7-5����09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj ����in�еĲ���
	 * @return
	 * @throws Exception
	 */
	public static CircularlyAccessibleValueObject[] queryBodyByClassCondtion(String billType, String id, String bodyCondition,Object oUserObj)throws Exception {		
		if(oUserObj==null ){
			oUserObj=new String[]{"errorPk"};
		}
	    if(!(oUserObj instanceof String[])){
	    	throw new BusinessException("������û��������Ϊ��������");
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
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 *        ���ղ�ѯ ��ѯ��ͷ����
	 *        ���ø��� �����ϸ�������
	 *        �� tmpWhere ������
	 *        ����������������in 
	 *        ���磺hid='3333333333' and pk in ( select pk_invmandoc from bd_invmancoc where pk_invmandoc in
	 * @ʱ�䣺2011-7-5����09:35:36
	 * @param billType 
	 * @param businessType 
	 * @param tmpWhere
	 * @param oUserObj ����in�еĲ���
	 * @return
	 * @throws Exception
	 */
	public static nc.vo.pub.CircularlyAccessibleValueObject[] queryByClassCondtion(String billType,String businessType,String tmpWhere,Object oUserObj) throws Exception {

		if(oUserObj==null){
			oUserObj=new String[]{"errorPk"};
		}
	    if(!(oUserObj instanceof String[])){
	    	throw new BusinessException("������û��������Ϊ��������");
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
		Logger.info("*****��ѯ���ݵı�ͷ���ݿ�ʼ*****");
		// ���Ϻ���������
		StringBuffer retWhere = new StringBuffer();
		String referClzName = getReferClzName(billType, businessType, whereString, retWhere);
		if (referClzName == null || referClzName.equals("")) {
			Logger.error("δע��ʵ�ֲ�ѯ���ݵ�Dmo�����ļ�");
			throw new PFBusinessException("δע��ʵ�ֲ�ѯ���ݵ�Dmo�����ļ�");
		}
		Logger.info("��ѯ�����������䣺" + retWhere);
		IQueryData tmpObj = (IQueryData) PfUtilTools.instantizeObject(billType, referClzName.trim());	
		CircularlyAccessibleValueObject[] retVos = tmpObj.queryAllHeadData(retWhere.toString());
		Logger.info("*****��ѯ���ݵı�ͷ���ݽ���*****");
		return retVos;
	}
	private static String getReferClzName(String billType, String busiType, String whereString,
			StringBuffer retWhere) {
		BilltypeVO billTypeVO = PfDataCache.getBillTypeInfo(billType);
		// �̻�����������봫�������������
		boolean isExistDbWhere = true;
		if (billTypeVO.getWherestring() != null && !(billTypeVO.getWherestring().trim().equals(""))) {
			retWhere.append(" ").append(billTypeVO.getWherestring());
		} else {
			isExistDbWhere = false;
			retWhere.append(" ");
		}
		// �ж��Ƿ��н����ѯ����
		if (whereString != null && (!whereString.trim().equals(""))) {
			if (isExistDbWhere) {
				retWhere.append(" and ").append(whereString);
			} else {
				retWhere.append(whereString);
			}
		}
		// ����ҵ������
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
		Logger.info("*****��ѯ���ݵı������ݿ�ʼ*****");
		// ������
		BilltypeVO billReferVo = PfDataCache.getBillTypeInfo(billType);
		if (billReferVo.getReferclassname() == null || billReferVo.getReferclassname().equals("")) {
			Logger.error("δע��ʵ�ֲ�ѯ���ݵ�Dmo�����ļ�");
			throw new PFBusinessException("δע��ʵ�ֲ�ѯ���ݵ�Dmo�����ļ�");
		}
		// ��ģ��ʵ������ѯ��
		Object queryObj = PfUtilTools
				.instantizeObject(billType, billReferVo.getReferclassname().trim());
		if (bodyCondition == null || bodyCondition.trim().equals("")) {
			Logger.info("ִ��ʵ�ֽӿ�IQueryData������");
			IQueryData tmpObj = (IQueryData) queryObj;
			retVos = tmpObj.queryAllBodyData(parentPK);
		} else {
			Logger.info("ִ��ʵ�ֽӿ�IQueryData2������");
			Logger.info("ִ��ʵ�ֽӿ�IQueryData2������,�ӱ��ѯ����Ϊ:" + bodyCondition);
			IQueryData2 tmpObj = (IQueryData2) queryObj;
			retVos = tmpObj.queryAllBodyData(parentPK, bodyCondition);
		}
		Logger.info("*****��ѯ���ݵı������ݽ���*****");
		return retVos;
	}

}
