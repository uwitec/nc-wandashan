package nc.bs.hg.ic.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class HgICPubBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public Object splitGItemsOnPactMatch(GeneralBillVO gbillvo)
			throws BusinessException {
		if (gbillvo == null)
			return null;

		// 并发控制 加锁处理 入库单并发控制 入库单体加锁
		HgScmPubBO scmbo = new HgScmPubBO();
		// 合同并发控制
		Map<String, UFDateTime> pactTsInfor = (Map<String, UFDateTime>) gbillvo
				.getHeaderVO().getAttributeValue("pacttsinfor");
		List<String> lid = new ArrayList<String>();
		GeneralBillItemVO[] bodys = gbillvo.getItemVOs();
		for (GeneralBillItemVO body : bodys) {
			if (lid.contains(body.getCsourcebillbid()))
				continue;
			lid.add(body.getCsourcebillbid());
		}
		if (lid.size() == 0)
			throw new BusinessException("传入数据无合同信息");

		if (pactTsInfor != null) {
			try {
				Map<String, UFDateTime> pactTsInfor2 = new HashMap<String, UFDateTime>();
				for (String key : pactTsInfor.keySet()) {// 去掉未匹配到合同ts信息
					if (lid.contains(key))
						continue;
					pactTsInfor2.put(key, pactTsInfor.get(key));
				}

				scmbo.checkTime("po_order_b", "corder_bid", pactTsInfor2);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new BusinessException(se);
			}

		}

		// 入库单并发校验
		nc.bs.ic.pub.check.CheckDMO icdmo = null;
		try {
			icdmo = new CheckDMO();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

		icdmo.checkTimeStamp(gbillvo);

		// 合同体加锁
		String user = HgPubTool.getString_NullAsTrimZeroLen(gbillvo
				.getHeaderVO().getAttributeValue("cuserid"));
		String[] pactbids = lid.toArray(new String[0]);
		UFBoolean isPactLock = UFBoolean.FALSE;
		UFBoolean isIcLock = UFBoolean.FALSE;
		Object o = null;
		try {
			isPactLock = scmbo.lockPkForKey(user, pactbids);
			isIcLock = scmbo.lockPkForVo(gbillvo);

			HgGeneralbillBO gbo = new HgGeneralbillBO();
			o = gbo.updateThisBill2(gbillvo);

			rewriteBackArrNum(gbillvo.getItemVOs(), null, true);
		} catch (Exception ee) {

			if (ee instanceof BusinessException)
				throw (BusinessException) ee;
			throw new BusinessException(ee);

		} finally {
			if (isPactLock.booleanValue()) {
				try {
					scmbo.freePkForKey(user, pactbids);
				} catch (Exception e) {
				}
			}
			if (isIcLock.booleanValue()) {
				try {
					scmbo.freePkForVo(gbillvo);
				} catch (Exception e) {
				}
			}
		}
		return o;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）入库单关联合同查询 2011-2-17上午11:06:12
	 * @param cvendormanid
	 * @param cinvbasids
	 * @return
	 * @throws BusinessException
	 */
	public PactVO[] queryPactsByVendor(String cbiztypeid, String cvendormanid,
			String[] cinvbasids) throws BusinessException {
		// PactVO[] pvos = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(cvendormanid) == null)
			return null;
		if (cinvbasids == null || cinvbasids.length == 0)
			return null;
		// 查询订单 查询什么样的订单呢
		StringBuffer str = new StringBuffer();
		str
				.append(" select h.vordercode,h.cvendormangid,h.cvendorbaseid,b.corder_bid"
						+ " ,b.corderid,b.cmangid,b.cbaseid,b.nordernum,b.cassistunit,b.nassistnum,b.blargess,b.crowno,"
						+ "b.naccumarrvnum,b.naccumstorenum,b.ts "
						+ " from po_order h inner join po_order_b b on h.corderid = b.corderid"
						+ " where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.cvendormangid = '"
						+ cvendormanid
						+ "'"
						+ " and coalesce(nordernum,0.0) - coalesce(naccumarrvnum,0.0)>0.0 ");// 累计到货数量为空

		if (PuPubVO.getString_TrimZeroLenAsNull(cbiztypeid) != null)
			str.append(" and h.cbiztype = '" + cbiztypeid + "'");
		str.append(" and h.forderstatus = 3");// 审批后
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(PactVO.class));
		if (o == null)
			return null;
		java.util.List<PactVO> ldata = (java.util.List<PactVO>) o;
		return ldata.toArray(new PactVO[0]);
	}

	public void rewriteBackArrNum(GeneralBillItemVO[] items,
			GeneralBillItemVO[] olditems, boolean isSave)
			throws BusinessException {
		if (items == null || items.length == 0)
			return;
		// 回写订单 累计到货数量 = 应收数量
		String sql = "update po_order_b set naccumarrvnum = coalesce(naccumarrvnum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;
		if (isSave) {
			int index = 0;
			for (GeneralBillItemVO item : items) {
				parameter = new SQLParameter();

				nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
				if (olditems != null) {
					nnum = nnum.sub(PuPubVO
							.getUFDouble_NullAsZero(olditems[index]
									.getNshouldinnum()));
				}
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
			return;
		}

		for (GeneralBillItemVO item : items) {
			parameter = new SQLParameter();
			nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum())
					.multiply(-1);
			parameter.addParam(nnum);
			parameter.addParam(item.getCsourcebillbid());
			getBaseDao().executeUpdate(sql, parameter);
		}
	}

	public void rewriteBackNinNum(GeneralBillItemVO[] items,
			GeneralBillItemVO[] old, boolean isSave) throws BusinessException {
		if (items == null || items.length == 0)
			return;

		// 回写订单 累计入库数量 = 实收数量
		String sql = "update po_order_b set naccumstorenum = coalesce(naccumstorenum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		UFDouble ninnum = UFDouble.ZERO_DBL;
		UFDouble oldnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;
		if (isSave) {
			for (GeneralBillItemVO item : items) {
				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
				if(ninnum.compareTo(UFDouble.ZERO_DBL)<0)//如果入库数量小于零
					return;
			   if (UFDouble.ZERO_DBL.equals(ninnum)) {// 如果有入库数量 系统会自动回写入库数量不需要在此处控制（但需要减去回写的实收数量）
					if (old == null || old.length == 0) {
						nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
					} else {
						for (GeneralBillItemVO olditem : old) {
							if (item.getPrimaryKey().equalsIgnoreCase(
									olditem.getPrimaryKey())) {
								oldnum = PuPubVO.getUFDouble_NullAsZero(olditem.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = nnum.sub(oldnum);
							}
						}
					}
				}else  {
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
				}
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
			return;
		}

		for (GeneralBillItemVO item : items) {
			parameter = new SQLParameter();
			ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
			if(ninnum.compareTo(UFDouble.ZERO_DBL)<0)//如果入库数量小于零
				return;
			if (UFDouble.ZERO_DBL.equals(ninnum)) {
				nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
		}
	}

	/**
	 * 根据pk获取修改前的数量
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-1-13下午11:17:01
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public UFDouble getOldNum(String pk) throws DAOException {
		String sql = "select " + HgPubConst.NUM_DEF_FAC
				+ " from  ic_general_b where cgeneralbid = '" + pk + "'";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		return PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	public void compareTONum(GeneralBillVO vo) throws BusinessException{
		if(vo == null)
			return;
		GeneralBillItemVO[] items = vo.getItemVOs();
		UFDouble outnum = UFDouble.ZERO_DBL;
		for(GeneralBillItemVO item:items){
			outnum = PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
			if(UFDouble.ZERO_DBL.compareTo(outnum)>=0)
				throw new BusinessException("实发数量不能小于零");
		}
	}
	/**
	 * 根据批次号和存货获取供应商
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-3-7上午09:34:07
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws BusinessException
	 */
	public String getcvendor(String cinventoryid ,String vbatchcode)throws BusinessException{
		ArrayList<String> object = null;
		String cvendor=null;
    	if(cinventoryid!=null&&cinventoryid.trim().length()>0&&vbatchcode!=null&&vbatchcode.trim().length()>0 ){
			String sql = "select distinct bb.cvendorid from ic_general_b  bb where  bb.dr=0 and cbodybilltypecode in('40', '41', '43','45', '46', '47', '48', '4A', '4B', '4E', '4X')"+
					"and cinventoryid = '"+cinventoryid.trim()+"' and vbatchcode = '"+vbatchcode.trim()+"' and isnull(bb.dr,0)=0 and  bb.cvendorid is not null";
			object = (ArrayList<String>)getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNLISTPROCESSOR);
			if (object != null && object.size() == 1 && object.get(0) != null){
				cvendor= PuPubVO.getString_TrimZeroLenAsNull(object.get(0));
			}
    	}
		return cvendor;
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）根据计划项目找出库方式 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public String getIOutWay(String pk_planproject) throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_planproject) == null)
			return null;
		String sql = "select ioutway from hg_planproject where pk_planproject = '"
				+ pk_planproject + "' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);

		if (o == null)
			return null;
		String string = null;
		String str = PuPubVO.getString_TrimZeroLenAsNull(o);
		if ("0".equalsIgnoreCase(str))
			string = "计划价";
		if ("1".equalsIgnoreCase(str))
			string = "合同价";
		return string;
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）材料出库采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public UFDouble callPurchasePactPrice(String invcode, String pk_corp,String vbatch)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(invcode) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null || PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		String sql = " select ord.noriginalcurprice,pi.vuserdef11 from po_order_b ord inner join ic_general_b pi on pi.csourcebillbid = ord.corder_bid "
				+ " inner join bd_invmandoc man on pi.cinventoryid = man.pk_invmandoc inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc "
				+ " where isnull(ord.dr, 0) = 0 and isnull(pi.dr, 0) = 0 and isnull(bas.dr,0)=0 and isnull(man.dr,0)=0" 
				+" and bas.invcode='"+invcode+"' and man.pk_corp ='"+pk_corp+"' and pi.vbatchcode = '"+vbatch+"'";
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
		if (o == null)
			return null;
		ArrayList ldata = (ArrayList) o;
		if (ldata.size() == 0)
			return null;
		Object ob= (Object)ldata.get(0);
		if(ob ==null)
			return null;
		Object[] os = (Object[]) ob;
		String str= PuPubVO.getString_TrimZeroLenAsNull(os[1]);
		UFDouble price = PuPubVO.getUFDouble_NullAsZero(os[0]);
		String  s =getIOutWay(str);
		if("合同价".equalsIgnoreCase(s))
			return price;
		return UFDouble.ZERO_DBL;
	}
	
	/**
	 * 根据批次号和存货获取供应商
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-3-7上午09:34:07
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws Exception 
	 */
	public Object[] getInfoByBatchCode(String cinventoryid ,String invcode, String pk_corp,String vbatch)throws Exception{
		String cvendor = getcvendor(cinventoryid, vbatch);
		UFDouble price = callPurchasePactPrice(invcode, pk_corp, vbatch);
		Object[] o = {cvendor,price};
		return o;
	}
}
