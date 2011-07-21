package nc.bs.hg.ic.pub;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;

public class HgICPubBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）通过匹配合同功能 和合同建立关联的入库单在入库前可以进行取消合同
	 * 2011-4-23下午01:13:36
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public Object cancelPactMatch(GeneralBillVO gbillvo)
	throws BusinessException {
		if(gbillvo == null){
			throw new BusinessException("异常，传入数据为空");
		}
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		if(!HgPubTool.getString_NullAsTrimZeroLen(head.getAttributeValue(HgPubConst.F_IS_SELF)).equalsIgnoreCase(HgPubConst.SELF)){
			throw new BusinessException("来源于合同的入库验收单不能进行此操作");
		}
		GeneralBillItemVO[] gitems = gbillvo.getItemVOs();
		if (gitems == null || gitems.length == 0)
			throw new BusinessException("异常，传入数据为空");
		// 入库单并发校验
		nc.bs.ic.pub.check.CheckDMO icdmo = null;
		try {
			icdmo = new CheckDMO();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

		icdmo.checkTimeStamp(gbillvo);	
		HgScmPubBO scmbo = new HgScmPubBO();
		UFBoolean isIcLock = UFBoolean.FALSE;
		try {
			isIcLock = scmbo.lockPkForVo(gbillvo);
			List<String> lid = new ArrayList<String>();
			for (GeneralBillItemVO gitem : gitems) {
				if (gitem == null)
					throw new BusinessException("异常，传入数据存在空");
				if (PuPubVO.getString_TrimZeroLenAsNull(gitem.getCsourcebillbid()) == null
						|| !HgPubTool.getString_NullAsTrimZeroLen(
								gitem.getCsourcetype()).equalsIgnoreCase(
										ScmConst.PO_Order)) {
					throw new BusinessException("未关联合同");
				}

				lid.add(gitem.getPrimaryKey());				
			}			
			Map<String,String> tsInfor = splitPactInforfromInBill(lid);
			rewriteBackArrNum(gitems, null, false);
			return tsInfor;
		} catch (Exception ee) {
			if (ee instanceof BusinessException)
				throw (BusinessException) ee;
			throw new BusinessException(ee);
		} finally {
			if (isIcLock.booleanValue()) {
				try {
					scmbo.freePkForVo(gbillvo);
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）
	 * 2011-4-23下午12:17:27
	 * @param pactInfor
	 * @throws BusinessException
	 */
	private Map<String,String> splitPactInforfromInBill(List<String> lid)
			throws BusinessException {
		String sql = " update ic_general_b set csourcebillbid = ?,csourcebillhid = ?,csourcetype = ? , nshouldinnum = 0.0 where cgeneralbid in "
				+ HgPubTool.getSubSql(lid.toArray(new String[0]));

		SQLParameter para = new SQLParameter();
		para.addNullParam(Types.CHAR);
		para.addNullParam(Types.CHAR);
		para.addNullParam(Types.CHAR);
		getBaseDao().executeUpdate(sql, para);
		
		sql = " select cgeneralbid,ts from ic_general_b where cgeneralbid in "
			+ HgPubTool.getSubSql(lid.toArray(new String[0]));
		List ldata = (List)getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("后台数据操作异常");
		Object[] os = null;
		Map<String,String> tsInfor = new HashMap<String, String>();
		for(Object o:ldata){
			os = (Object[])o;
			tsInfor.put(HgPubTool.getString_NullAsTrimZeroLen(os[0]), HgPubTool.getString_NullAsTrimZeroLen(os[1]));
		}
		return tsInfor;
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
			
		    GeneralBillItemVO[] items=null;
			if(o != null){
				Object[] os = (Object[])o;
			    items=(GeneralBillItemVO[])os[1];
			    int len =items.length;
			    GeneralBillItemVO[] olditems= new GeneralBillItemVO[items.length] ;
			    for(int i=0;i<len;i++){
			    	gbillvo.getChildrenVO()[i].setAttributeValue("ts", items[i].getTs());
			    	olditems[i]=(GeneralBillItemVO) gbillvo.getChildrenVO()[i];
			    }
			    o= new Object[]{os[0],olditems};
			}
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
		str.append(" select h.vordercode,h.cvendormangid,h.cvendorbaseid,b.corder_bid"
						+ " ,b.corderid,b.cmangid,b.cbaseid,b.nordernum,b.cassistunit,b.nassistnum,b.blargess,b.crowno,"
						+ "b.naccumarrvnum,b.naccumstorenum,b.ts,b.noriginalcurprice "
						+ " from po_order h inner join po_order_b b on h.corderid = b.corderid"
						+ " where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.cvendormangid = '"+ cvendormanid+"'"
						 //未完全入库  modify by zhw  2011-04-15  鹤岗项目  提前容差
						// 没有入库完成的");// 累计到货数量为空
						+ " and coalesce(b.vdef20,'N')= 'N'");
						

		if (PuPubVO.getString_TrimZeroLenAsNull(cbiztypeid) != null)
			str.append(" and h.cbiztype = '" + cbiztypeid + "'");
		str.append(" and h.forderstatus = 3");// 审批后
		
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(PactVO.class));
		
		if (o == null)
			return null;
		
		java.util.List<PactVO> ldata = (java.util.List<PactVO>) o;
		java.util.List<PactVO> data = new ArrayList<PactVO>();
		
		//modify by zhw 2011-04-16  可用数量=订单数量*容差-累计入库数量
		int  size = ldata.size();
		for(int i=0;i<size;i++){
			
			PactVO pvo=	ldata.get(i);
			String basid = PuPubVO.getString_TrimZeroLenAsNull(pvo.getCbaseid());
			UFDouble allowance= PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(basid)).div(new UFDouble(100));
			UFDouble nordernum =PuPubVO.getUFDouble_NullAsZero(pvo.getNordernum());// 合同数量;
			
			if(!UFDouble.ZERO_DBL.equals(allowance))
				 nordernum=allowance.multiply(PuPubVO.getUFDouble_NullAsZero(pvo.getNordernum()));//乘以容差以后的数量
             UFDouble  nusenum= nordernum.sub(PuPubVO.getUFDouble_NullAsZero(pvo.getNaccumstorenum()));
			
             if(UFDouble.ZERO_DBL.compareTo(nusenum)<0){
				pvo.setNordernum(nordernum);
				 data.add(pvo);
			 }
             
		}
		return data.toArray(new PactVO[0]);
	}

	/**
	 * 自制采购入库单   合同匹配时  回写采购订单
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-23下午05:05:07
	 * @param items
	 * @param olditems
	 * @param isSave
	 * @throws BusinessException
	 */
	public void rewriteBackArrNum(GeneralBillItemVO[] items,GeneralBillItemVO[] olditems, boolean isSave)
			throws BusinessException {

		if (items == null || items.length == 0)
			return;

		// 回写订单 累计到货数量 = 应收数量+   vdef20 是否完成入库
		String sql = "update po_order_b set vdef20 = ?,naccumstorenum  = coalesce(naccumstorenum ,0.0)+? where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;

		UFDouble ninnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;

		if (isSave) {
			int index = 0;
			for (GeneralBillItemVO item : items) {

				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());

				parameter.clearParams();

				
				// modify by zhw 2011-04-16 判读累计数量是否大于订单数量乘以容差
				String sourceid = item.getCsourcebillbid();
				Object o = getNaccumstorenum(sourceid);

				UFDouble nordernum = UFDouble.ZERO_DBL;
				UFDouble accumstorenum = UFDouble.ZERO_DBL;
				UFDouble allowance = UFDouble.ZERO_DBL;
				
				if (o != null) {

					Object[] uds = (Object[]) o;
					nordernum = PuPubVO.getUFDouble_NullAsZero(uds[0]);
					accumstorenum = PuPubVO.getUFDouble_NullAsZero(uds[1]);
					allowance = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item.getCinvbasid()).div(100));
				}
				
				if (UFDouble.ZERO_DBL.equals(ninnum)) {// 如果没有入库数量 需要回写实收数量到订单的累计入库数量
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
					if (!UFDouble.ZERO_DBL.equals(nnum)
							&& nordernum.multiply(allowance).equals(accumstorenum.add(nnum))) {
						// 如果有本次合同数量 并且合同数量等于实收数量+累计入口数量 则完成入库

						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}

					if (olditems != null) {
						nnum = nnum.sub(PuPubVO.getUFDouble_NullAsZero(olditems[index].getNshouldinnum()));
					}
					
				} else {//如果有入库数量 系统会自动回写入库数量不需要在此处控制（但需要减去回写的实收数量）
					
					if(VOStatus.UPDATED ==  item.getStatus())
						// 入库后再修改   如果表体没有修改不需要减去回写的实收数量
						nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
				        nnum =PuPubVO.getUFDouble_NullAsZero(nnum).multiply(-1);
					
					if (accumstorenum.add(ninnum).equals(nordernum.multiply(allowance))) {// 合同数量等于实入数量 则完成入库
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
				}

				parameter.addParam(nnum);
				parameter.addParam(sourceid);
				getBaseDao().executeUpdate(sql, parameter);
			}

			return;
		}

		for (GeneralBillItemVO item : items) {

			parameter = new SQLParameter();
			ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
			
			if (UFDouble.ZERO_DBL.equals(ninnum)) 
			    nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum()).multiply(-1);
			
			parameter.clearParams();

			parameter.addParam("N");
			parameter.addParam(nnum);
			parameter.addParam(item.getCsourcebillbid());
			getBaseDao().executeUpdate(sql, parameter);
		}
	}

	/**
	 * 采购入库单参照采购订单时 采购订单的回写
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-23下午05:04:24
	 * @param items
	 * @param old
	 * @param isSave
	 * @throws BusinessException
	 */
	public void rewriteBackNinNum(GeneralBillItemVO[] items,GeneralBillItemVO[] old, boolean isSave) throws BusinessException {
		
		if (items == null || items.length == 0)
			return;

		// 回写订单 累计入库数量 = 实收数量   vdef20 是否完成入库
		String sql = "update po_order_b set vdef20 = ?,naccumstorenum = coalesce(naccumstorenum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		UFDouble ninnum = UFDouble.ZERO_DBL;
		UFDouble oldnum = UFDouble.ZERO_DBL;
		UFDouble nshouldinnum = UFDouble.ZERO_DBL;

		SQLParameter parameter = null;
		if (isSave) {
			for (GeneralBillItemVO item : items) {
				
				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
				nshouldinnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());// 合同数量
				
				if (ninnum.compareTo(UFDouble.ZERO_DBL) < 0)// 如果入库数量小于零
					return;
				
				if (UFDouble.ZERO_DBL.equals(ninnum)) {// 如果没有入库数量   需要回写 实收数量到订单的累计入库数量
					
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
					if(item.getStatus()==VOStatus.DELETED)
						nnum=UFDouble.ZERO_DBL;
					
					if (nnum.equals(nshouldinnum)) {// 合同数量等于实入数量 则完成入库
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
					
					if (old != null && old.length >0) {
						
						for (GeneralBillItemVO olditem : old) {
							
							if (item.getPrimaryKey().equalsIgnoreCase(olditem.getPrimaryKey())) {
								
								oldnum = PuPubVO.getUFDouble_NullAsZero(olditem.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = nnum.sub(oldnum);
							}
						}
					}
					
				} else {  //如果有入库数量 系统会自动回写入库数量不需要在此处控制（但需要减去回写的实收数量）
					
					if(VOStatus.UPDATED ==  item.getStatus()) // 入库后再修改   如果表体没有修改不需要减去回写的实收数量
				        nnum =PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
					
					if (ninnum.equals(nshouldinnum)) {// 合同数量等于实入数量 则完成入库
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
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
			if (ninnum.compareTo(UFDouble.ZERO_DBL) < 0)// 如果入库数量小于零
				return;
			
			if (UFDouble.ZERO_DBL.equals(ninnum)) 
				nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
				
				parameter.addParam("N");
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
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
	
	
	HgScmPubBO bo = null;
	private HgScmPubBO getScmPubBO(){
	
		if(bo == null){
			bo = new HgScmPubBO();
		}
		
		return bo;
	}
	
	/**
	 * 获取存货的到货容差
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-15下午01:46:49
	 * @param pk_invbasid
	 * @param fieldname
	 * @param where
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getAllowanceSet(String pk_invbasid) throws BusinessException{
		
		UFDouble allowance =PuPubVO.getUFDouble_NullAsZero(getScmPubBO().getAllowanceSet(pk_invbasid,"narrival",null));
		
		return allowance;
		
	}
	
	/**
	 *   获取采购订单上的  订单数量  累计入库数量
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-23下午05:08:16
	 * @param sourceid
	 * @return
	 * @throws BusinessException
	 */
	private Object getNaccumstorenum(String sourceid) throws BusinessException{
	
		String sql= "select nordernum,naccumstorenum from po_order_b where corder_bid = '"+sourceid+"' and isnull(dr,0)=0 ";
		
		Object o = getBaseDao().executeQuery(sql,HgBsPubTool.ARRAYPROCESSOR);
		
		return o;
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
	public ArrayList getcvendor(ArrayList al)throws BusinessException{
		
		if(al ==null || al.size()==0)
			return null;
		
		int size= al.size();
		Object object = null;
		ArrayList als= new ArrayList();
		
		String infor=null;
		String[] strs = new String [2];
		
		for(int i=0;i<size;i++){
			
			infor = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
			
			if(infor != null){
				strs=infor.split("&");
				
				if(strs !=null && strs.length>0){
					
					String cinventoryid = strs[1];
					String vbatchcode=strs[0];
				    object = getcvendor(cinventoryid,vbatchcode);
				    als.add(object);
		    	}
			}
		}
		return als;
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
	public UFDouble callPurchasePactPrice(String vbatch,String invbasid)
			throws Exception {
		
		if (PuPubVO.getString_TrimZeroLenAsNull(invbasid) == null
				||  PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		String sql = " select pi.nprice,pi.vuserdef11,man.pk_corp,pi.vbatchcode,bas.invcode from ic_general_b pi inner join bd_invmandoc man on pi.cinventoryid = man.pk_invmandoc "
                     + " inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc where isnull(pi.dr, 0) = 0 and isnull(bas.dr, 0) = 0 and isnull(man.dr, 0) = 0 and " 
                     + " man.pk_invbasdoc = '"+invbasid+"' and pi.vbatchcode='"+vbatch+"' and man.pk_corp ='"+1002+"' and  pi.vuserdef11 is not null";
	
		Object o = getBaseDao().executeQuery(sql,HgBsPubTool.ARRAYLISTPROCESSOR);
		
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
	public ArrayList getInfoByBatchCode(ArrayList al)throws Exception{
		if(al ==null || al.size()==0)
			return null;
		int size= al.size();
		
		ArrayList als= new ArrayList();
		String infor=null;
		String[] strs = new String [4];
		
		for(int i=0;i<size;i++){
			
			infor = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
			
			if(infor != null){
			
				strs=infor.split("&");
			
				if(strs !=null && strs.length>0){
				
					String vbatchcode=strs[0];
					String cinventoryid = strs[1];
					String invbasid=strs[2];
					String corp =strs[3];
				
					if (HgPubConst.GYC_CORPID.equalsIgnoreCase(corp)) {
					
						Object object = getcvendor(cinventoryid,vbatchcode);
				        als.add(object);
					
					}else{
					
						UFDouble price =callPurchasePactPrice(vbatchcode,invbasid);
				
					}
		    	}
			}
		}
		return als;
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
	public String getcvendor(String cinventoryid, String vbatchcode)throws BusinessException{
		
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
	
	public void checkDatas(GeneralBillVO gbillvo) throws BusinessException{
		if(gbillvo==null)
			return;
		GeneralBillHeaderVO head= gbillvo.getHeaderVO();
		if(head ==null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if(items==null || items.length==0)
			return;
		int len = items.length;
		for(int i=0;i<len;i++){
			UFDouble nkdnum = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA));// 验收合格数量
	        UFDouble arr = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_ARR));// 到货数量
	        UFDouble fac = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_FAC));// 实收数量
            UFDouble ninnum = PuPubVO .getUFDouble_NullAsZero(items[i].getAttributeValue( "ninnum"));// 实入数量
			if (!PuPubVO.getUFBoolean_NullAs(head.getFreplenishflag(), UFBoolean.FALSE).booleanValue()) {
				
				if (UFDouble.ZERO_DBL.compareTo(arr) >= 0) 
					throw new BusinessException("第" + (i + 1) + "行到货数量必须大于零");
					
				if (UFDouble.ZERO_DBL.compareTo(fac) >= 0) 
					throw new BusinessException("第" + (i + 1) + "行实收数量必须大于零");
					
				if (arr.compareTo(fac) < 0) 
					throw new BusinessException("第" + (i + 1) + "行实收数量不能大于到货数量");
				
				if (!UFDouble.ZERO_DBL.equals(nkdnum)) 
					if (UFDouble.ZERO_DBL.compareTo(ninnum) >= 0) 
						throw new BusinessException("第" + (i + 1) + "行入库数量必须大于零");
						
					 else if (ninnum.compareTo(nkdnum) > 0) 
						throw new BusinessException("第" + (i + 1) + "行入库数量不能大于验收数量");
				}
			}
	}
	//校验回写数量
	public void check(AggregatedValueObject gbillvo) throws BusinessException{
		if(gbillvo==null)
			return;
		GeneralBillHeaderVO head= (GeneralBillHeaderVO)gbillvo.getParentVO();
		if(head ==null)
			return;
		GeneralBillItemVO[] items =(GeneralBillItemVO[]) gbillvo.getChildrenVO();
		if(items==null || items.length==0)
			return;
		for(GeneralBillItemVO item:items){
			String csql =" select ib.ninnum,ib.vuserdef19 from ic_general_b ib where  isnull(ib.dr, 0) = 0 and ib.csourcebillbid = '"+item.getCsourcebillbid()+"'";
	        String psql =" select pb.naccumstorenum from po_order_b pb where isnull(pb.dr, 0) = 0 and pb.corder_bid= '"+item.getCsourcebillbid()+"'";
			Object o = getBaseDao().executeQuery(csql,new ArrayListProcessor());
			Object o1 = getBaseDao().executeQuery(psql,new ColumnProcessor());
			UFDouble sum = UFDouble.ZERO_DBL;
			if(o ==null)
				return;
			ArrayList al =(ArrayList)o;
			if(al==null || al.size()==0)
				return;
			int size = al.size();
			for(int i=0;i<size;i++){
				Object alo = al.get(i);
				if(alo==null)
					return;
				Object[] os =(Object[])alo;
				if(PuPubVO.getUFDouble_NullAsZero(os[0]).compareTo(UFDouble.ZERO_DBL)==0)
					sum=sum.add(PuPubVO.getUFDouble_NullAsZero(os[1]));
				else
					sum = sum.add(PuPubVO.getUFDouble_NullAsZero(os[0]));
			}
	       if(sum.compareTo(PuPubVO.getUFDouble_NullAsZero(o1))!=0)
		      throw new BusinessException("回写累计数量出错");
		}
		}
	
	//根据存货仓库  判断是否进行了货位管理
	public  boolean getCsflag(String cwarehouseid) throws BusinessException{
		String sql =" select csflag from bd_stordoc where isnull(dr,0)=0 and pk_stordoc = '"+cwarehouseid+"'";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object o=query.executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR );
		
		if(PuPubVO.getUFBoolean_NullAs(o,UFBoolean.FALSE).booleanValue()){
			return true;
		}
		return false;
	}
		
}
