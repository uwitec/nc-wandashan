package nc.bs.hg.ic.ic201;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.itf.vrm.freeze.IVendorfreeze;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.vrm.freeze.VendorfreezeVO;

public class SaveGeneral {
	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	private Map<String, String> map = null;

	private Map<String, String> getTsMap() {
		if (map == null) {
			map = new HashMap<String, String>();
		}
		return map;
	}

	/**
	 * 取消验收处理 将验收的所有信息进行清空
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-8下午08:34:13
	 * @param cgeneralhid
	 *            入库单主表主键
	 * @throws BusinessException
	 */
	public Map<String, String> unCheck(String cgeneralhid) throws BusinessException {
//		for (int i = 0; i < HgPubConst.VUSERDEF.length; i++) {
//			String fieldnamei = HgPubConst.VUSERDEF[i];
//		}
		String fieldname0 = HgPubConst.VUSERDEF[0];
		String fieldname1 = HgPubConst.VUSERDEF[1];
		String fieldname2 = HgPubConst.VUSERDEF[2];
		String fieldname3 = HgPubConst.VUSERDEF[3];
		String fieldname4 = HgPubConst.VUSERDEF[4];
		String fieldname5 = HgPubConst.VUSERDEF[5];
		String fieldname6 = HgPubConst.VUSERDEF[6];
		String fieldname7 = HgPubConst.VUSERDEF[7];
		String sql = "update ic_general_h set " + fieldname0 + " = '' ,"
				+ fieldname1 + " = '' ," + fieldname2 + " = '' ," + fieldname3
				+ " = '' ," + fieldname4 + " = '' ," + fieldname5 + " = '' ,"
				+ fieldname6 + " = '' ," + fieldname7 + " = '' " + " where cgeneralhid = '" + cgeneralhid + "'";
		getBaseDao().executeUpdate(sql);

		sql = " update ic_general_b set "+HgPubConst.NUM_DEF_QUA+" = 0 where cgeneralhid = '"
				+ cgeneralhid + "'";
		getBaseDao().executeUpdate(sql);
		map =getTs(cgeneralhid);
		return map;
	}

	/**
	 * 验收合格处理 将验收合格后的信息更新到数据库中 更新 验收人，验收时间，是否合格，质量问题，质量问题原因 合格更新验收数量 不合格封存单据 供应商
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-8下午08:35:53
	 * @param vo
	 *            入库单聚合vo
	 * @throws BusinessException
	 */
	public Map<String, String> onSaveGeneral(GeneralBillVO vo) throws BusinessException {
		if (vo == null)
			throw new BusinessException("传入数据为空");

		//并发加锁
		CheckDMO checkdmo = null;
		try{
			checkdmo = new CheckDMO();
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e);
		}

		checkdmo.checkTimeStamp(vo);

		HgScmPubBO scmbo = new HgScmPubBO();
		UFBoolean isLock = UFBoolean.FALSE;
		try{
			isLock = scmbo.lockPkForVo(vo);			
			GeneralBillHeaderVO head = vo.getHeaderVO();
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
			if (vo == null)
				throw new BusinessException("传入数据为空");
			StringBuffer sb = new StringBuffer();

			sb.append("update ic_general_h set ");
			String fieldname0 = HgPubConst.VUSERDEF[0];
			String fieldname1 = HgPubConst.VUSERDEF[1];
			String fieldname2 = HgPubConst.VUSERDEF[2];
			String fieldname3 = HgPubConst.VUSERDEF[3];
			String fieldname4 = HgPubConst.VUSERDEF[4];
			String fieldname5 = HgPubConst.VUSERDEF[5];

			String value0 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[0]));
			String value1 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[1]));
			String value2 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[2]));
			String value3 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[3]));
			String value4 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[4]));
			String value5 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[5]));

			if (value0 != null)
				sb.append(fieldname0 + " = '" + value0 + "'");
			if (value1 != null)
				sb.append("," + fieldname1 + " = '" + value1 + "'");
			if (value2 != null)
				sb.append("," + fieldname2 + " = '" + value2 + "'");
			if (value3 != null)
				sb.append("," + fieldname3 + " = '" + value3 + "'");
			if (value4 != null)
				sb.append("," + fieldname4 + " = '" + value4 + "'");
			if (value5 != null)
				sb.append("," + fieldname5 + " = '" + value5 + "'");

			sb.append(" where cgeneralhid ='" + head.getPrimaryKey() + "'");

			getBaseDao().executeUpdate(sb.toString());


			if(value0.equalsIgnoreCase("Y")){
				// 更新数量
				int len = items.length;
				for (int i = 0; i < len; i++) {
					String sql1 = "update ic_general_b set "+HgPubConst.NUM_DEF_QUA +" = '"
					+ PuPubVO.getUFDouble_NullAsZero(items[i]
					                                       .getAttributeValue(HgPubConst.NUM_DEF_FAC))
					                                       + "' where cgeneralbid = '" + items[i].getCgeneralbid()
					                                       + "'";
					getBaseDao().executeUpdate(sql1);
				}
			}else{
				//封存供应商
				VendorfreezeVO vvo = new VendorfreezeVO();
				vvo.setCfreezepsn(HgPubTool.getString_NullAsTrimZeroLen(value3));
				vvo.setDfreezedate(new UFDate(value4));
				vvo.setCvendorbaseid(getCubasdocidByManid(head.getCproviderid()));
				vvo.setCvendormangid(head.getCproviderid());
				vvo.setVfreezereason(HgPubConst.VENDOR_FREEZE_EWASON);
				vvo.setPk_corp(head.getPk_corp());
				vvo.setStatus(VOStatus.NEW);

				IVendorfreeze vendBO =(IVendorfreeze) NCLocator.getInstance().lookup(IVendorfreeze.class.getName());
				vendBO.insertArrayMy(new VendorfreezeVO[]{vvo});			
			}

			map =getTs(head.getPrimaryKey());
		}catch(Exception e){
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			throw new BusinessException(e);
		}finally{
			if(isLock.booleanValue()){
				try{
					scmbo.freePkForVo(vo);
				}catch(Exception e){}			
			}
		}

		return map;

	}
	
	private String getCubasdocidByManid(String cmanid) throws BusinessException{
		String formu = "cubasid->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,cumandoc);";
		String[] names = new String[]{"cumandoc"};
		String[] values = new String[]{cmanid};
		return PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.execFomular(formu, names, values));
	}

	/**
	 * 不合格处理 更新验收数量， 处理人，处理时间 解封到货单 ，供应商
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-8下午06:50:18
	 * @param vo
	 * @throws BusinessException
	 */
	public Map<String, String> onUnDeal(GeneralBillVO vo) throws BusinessException {
		if (vo == null)
			throw new BusinessException("传入数据为空");

		//并发加锁
		CheckDMO checkdmo = null;
		try{
			checkdmo = new CheckDMO();
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e);
		}

		checkdmo.checkTimeStamp(vo);

		HgScmPubBO scmbo = new HgScmPubBO();
		UFBoolean isLock = UFBoolean.FALSE;
		try{
			isLock = scmbo.lockPkForVo(vo);	
			GeneralBillHeaderVO head = vo.getHeaderVO();
			StringBuffer sb = new StringBuffer();

			sb.append("update ic_general_h set ");
			String fieldname5 = HgPubConst.VUSERDEF[5];
			String fieldname6 = HgPubConst.VUSERDEF[6];
			String fieldname7 = HgPubConst.VUSERDEF[7];

			String value5 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[5]));
			String value6 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[6]));
			String value7 = PuPubVO.getString_TrimZeroLenAsNull(head
					.getAttributeValue(HgPubConst.VUSERDEF[7]));

			if (value5 != null)
				sb.append(fieldname5 + " = '" + value5 + "'");
			if (value6 != null)
				sb.append("," + fieldname6 + " = '" + value6 + "'");
			if (value7 != null)
				sb.append("," + fieldname7 + " = '" + value7 + "'");

			sb.append(" where cgeneralhid ='" + head.getPrimaryKey() + "'");

			getBaseDao().executeUpdate(sb.toString());
			GeneralBillItemVO[] items = (GeneralBillItemVO[]) vo.getChildrenVO();
			int len = items.length;
			for (int i = 0; i < len; i++) {
				String sql1 = "update ic_general_b set "+HgPubConst.NUM_DEF_QUA +" = '"
				+ items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA)
				+ "' where cgeneralbid = '" + items[i].getCgeneralbid()
				+ "'";
				getBaseDao().executeUpdate(sql1);
			}		

			//解封供应商
			unFreezeVendor(head, value7, new UFDate(value6));

			map =getTs(head.getPrimaryKey());
		}catch(Exception e){
			if(e instanceof BusinessException){
				throw (BusinessException)e;
			}
			throw new BusinessException(e);
		}finally{
			if(isLock.booleanValue()){
				try{
					scmbo.freePkForVo(vo);
				}catch(Exception e){}			
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）解封不合格时冻结的供应商
	 * 2011-2-18下午02:07:28
	 * @param head
	 * @param cuserid
	 * @param dLogdate
	 * @throws BusinessException
	 */
	private void unFreezeVendor(GeneralBillHeaderVO head,String cuserid,UFDate dLogdate) throws BusinessException{

		if(head == null)
			return;		
		IVendorfreeze vendBO =(IVendorfreeze) NCLocator.getInstance().lookup(IVendorfreeze.class.getName());		

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select cvendorfreezeid, a.pk_corp, cvendorbaseid, cvendormangid,  vfreezereason, cfreezepsn, dfreezedate, cunfreezepsn, cunfreezereason, dunfreezedate");
		sql.append(" from vrm_vendorfreeze a, sm_user b, bd_cubasdoc c, bd_cumandoc d");
		sql.append(" where a.pk_corp = '");
		sql.append(head.getPk_corp());

		sql.append(
		"' and cvendorbaseid = c.pk_cubasdoc and c.pk_cubasdoc = d.pk_cubasdoc ");
		sql.append(" and b.cUserId = cfreezepsn");
		sql.append(" and (frozenflag = 'Y' or frozenflag = 'y')");
		sql.append(" and (dunfreezedate is null or dunfreezedate = '')");
		sql.append(" and a.cvendormangid = '"+head.getCproviderid()+"'");

		VendorfreezeVO[] vvos = vendBO.queryFreezeMy(sql.toString());

		if(vvos != null&&vvos.length>0){
			VendorfreezeVO vvo = vvos[0];
			vvo.setCunfreezepsn(HgPubTool.getString_NullAsTrimZeroLen(cuserid));
			vvo.setDunfreezedate(dLogdate);
			vvo.setCvendorbaseid(getCubasdocidByManid(head.getCproviderid()));
			vvo.setCvendormangid(head.getCproviderid());
			vvo.setCunfreezereason(HgPubConst.VENDOR_UNFREEZE_EWASON);
			vvo.setPk_corp(head.getPk_corp());
			vvo.setStatus(VOStatus.NEW);
			vendBO.updateArrayMy_Vendorfreeze(vvos);	
		}
	}
	
	
	/**
	 * 获取更新后数据的TS
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-12-8下午10:28:15
	 * @param al
	 * @throws DAOException 
	 * @throws BusinessException
	 */
     public Map<String,String> getTs(String cgeneralhid) throws DAOException{
    	 String queryHeadSql = "select ts from ic_general_h where cgeneralhid = '"+ cgeneralhid + "'";
 		Object head = getBaseDao().executeQuery(queryHeadSql,HgBsPubTool.COLUMNPROCESSOR);
 		String ts=null;
 		String queryItemsSql = "select ts,cgeneralbid from ic_general_b where cgeneralhid = '"
 			+ cgeneralhid + "'";
 		
 	   Object items = getBaseDao().executeQuery(queryItemsSql,HgBsPubTool.ARRAYLISTPROCESSOR);
 		if(head!=null){
 			ts = head.toString();
 		}
 		if (getTsMap() != null)
 			getTsMap().clear();
 		getTsMap().put(cgeneralhid, ts);
 		if(items!=null){
 			ArrayList al=(ArrayList)items;
 			if(al!=null && al.size()>0){
 				int size=al.size();
 				for(int i=0;i<size;i++){
 					Object o=al.get(i);
 					if(o!=null){
 						Object[] obj =(Object[])o;
 						getTsMap().put(obj[1].toString(),obj[0].toString());
 					}
 				}
 			}
 		}
 		return map;
     }
}
