package nc.bs.hg.pu.nmr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.uap.bd.inv.IInventoryAssign;
import nc.itf.uif.pub.IUifService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.invdoc.InvbasdocVO;
import nc.vo.bd.loginfo.BDErrLogResult;
import nc.vo.bd.loginfo.ErrLogReturnValue;
import nc.vo.bd.loginfo.ErrlogmsgVO;
import nc.vo.hg.pu.nmr.NewMaterialsVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class NewMaterialsBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 插入存货基本档案 分配到管理档案
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-2-25上午10:35:26
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertInvbasdoc(NewMaterialsVO vo) throws BusinessException {
		
		if (vo == null)
			throw new BusinessException("传入数据为空");
	    
		InvbasdocVO invVO =changeNewMaterialsVOToInvbasdocVO(vo);

		// 向存货基本档案中插入数据
		IUifService service = (IUifService) NCLocator.getInstance().lookup(IUifService.class.getName());
		String retBill = service.insert(invVO);
		if (retBill == null) {
			throw new BusinessException("插入存货失败");
		}

		// 分配到指定公司的存货管理档案
		Object os = shareInvmandoc(retBill);
		
		// 抛出返回异常
		getLogMsg(os);
		
		// 分配到指定公司的物料生成档案
		shareCalbody(retBill);
		
		// // 分配计划价
		updatePrice(retBill, vo);
		
		String sql = " select pk_invmandoc from bd_invmandoc where pk_invbasdoc = '"+ retBill + "' and pk_corp ='" + vo.getPk_corp() + "'";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		if (o == null) {
			throw new BusinessException("插入系统存货档案异常");
		}
		
		String str = (String) o;
		String querySQL = "select nnum,pk_planother_b from hg_planother_b  where vreserve1 = '"+ vo.getPk_materials() + "'";
		
		ArrayList al = (ArrayList) getBaseDao().executeQuery(querySQL,HgBsPubTool.ARRAYLISTPROCESSOR);
		
		if (al == null || al.size() == 0)
			return;
		
		int size = al.size();
		
		for (int i = 0; i < size; i++) {
			Object o1 = al.get(i);
			
			if (o1 == null)
				return;
			Object[] ob1 = (Object[]) o1;
			
			if (ob1 == null || ob1.length == 0)
				return;
			
			// 向计划表体更新已经编码完成的存货 并且将是否使用临时物资更新为N 只有临时计划存在临时物资编码申请
			String sqlother = " update hg_planother_b set vreserve1 = null, cinventoryid = '"
					+ str+ "',pk_invbasdoc ='"+ retBill+ "',bisuse ='N',nprice ='"+ vo.getNplanprice()
					+ "',"+ " nmny='"+ vo.getNplanprice().multiply(PuPubVO.getUFDouble_NullAsZero(ob1[0]))
					+ "',pk_measdoc='"+ vo.getPk_measdoc()+ "'   where vreserve1 = '"+ vo.getPk_materials()
					+ "' and pk_planother_b = '"+ ob1[1]+ "' and isnull(dr,0)=0 ";
			getBaseDao().executeUpdate(sqlother);
		}

	}

	/**
	 * 获取采购方式 自结 统结
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-2-25上午10:40:51
	 * @param vmi
	 * @return
	 * @throws BusinessException
	 */
	private String getInvbasVmi(Integer vmi) throws BusinessException {
		
		Map<String, String> infor = HgBsPubTool.getDefDoc_PuchType();
		
		if (vmi == 1)
			return infor.get("统结");
		
		return infor.get("自结");
	}

	/**
	 * 根据基本档案主键数组、目标公司主键数组，分配存货基本档案到目标公司
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-2-23下午03:27:13
	 * @param retBill
	 * @throws BusinessException
	 */
	public Object shareInvmandoc(String retBill) throws BusinessException {
		IInventoryAssign service2 = (IInventoryAssign) NCLocator.getInstance()
				.lookup(IInventoryAssign.class);

		String querySQL = "select pk_corp from bd_corp where isnull(dr,0)=0 and length(innercode)>2";// 除集团的外的公司
		Object corp = getBaseDao().executeQuery(querySQL,HgBsPubTool.COLUMNLISTPROCESSOR);
		
		if (corp == null)
			throw new BusinessException("获取公司失败");
		List<?> list = (List<?>) corp;
		
		if (list == null || list.size() == 0)
			throw new BusinessException("获取公司失败");
		int size = list.size();
		
		String[] targetPks = new String[size];
		for (int i = 0; i < size; i++) {
			targetPks[i] = PuPubVO.getString_TrimZeroLenAsNull(list.get(i));
		}
		List<String> pkList = new ArrayList<String>();
		pkList.add(retBill);// 待分配的存货基本档案id
		
		return service2.assignInvbasdocByPkList(targetPks, pkList, null);
	}

	/**
	 * 根据库存组织、存货管理档案主键、默认模板ID分配物料生产档案数据
	 * 
	 * @author zhw
	 * @throws BusinessException
	 * @说明：（鹤岗矿业） 2011-2-23下午03:26:58
	 */
	public void shareCalbody(String retBill) throws BusinessException {
		
		String query = "select pk_calbody,pk_corp from bd_calbody where isnull(dr,0)=0 ";
		String sql1 = "select pk_invmandoc,pk_corp from bd_invmandoc where pk_invbasdoc ='"+ retBill + "' and isnull(dr,0) =0";
		
		IInventoryAssign service = (IInventoryAssign) NCLocator.getInstance().lookup(IInventoryAssign.class);
		Object calbody = getBaseDao().executeQuery(query,HgBsPubTool.ARRAYLISTPROCESSOR);
		Object pk_mandoc = getBaseDao().executeQuery(sql1,HgBsPubTool.ARRAYLISTPROCESSOR);
		
		Map<String, String> map = new HashMap<String, String>();
		if (calbody == null)
			throw new BusinessException("获取库存组织失败");
		
		List<?> list = (List<?>) calbody;
		if (list == null || list.size() == 0)
			throw new BusinessException("获取库存组织失败");
		
		int size = list.size();
		
		for (int i = 0; i < size; i++) {
			Object[] o = (Object[]) list.get(i);
			String key = PuPubVO.getString_TrimZeroLenAsNull(o[1]);
			String value = PuPubVO.getString_TrimZeroLenAsNull(o[0]);
			map.put(key, value);
		}
		
		if (pk_mandoc == null)
			throw new BusinessException("获取存货管理档案主键失败");
		
		List<?> pList = (List<?>) pk_mandoc;
		if (list == null || list.size() == 0)
			throw new BusinessException("获取存货管理档案主键失败");
		
		int size1 = pList.size();
		String[] targetPks = new String[1];
		List<String> pkList = new ArrayList<String>();

		for (int i = 0; i < size1; i++) {
			Object[] o = (Object[]) pList.get(i);
			String key = PuPubVO.getString_TrimZeroLenAsNull(o[1]);
			String value = PuPubVO.getString_TrimZeroLenAsNull(o[0]);
			targetPks[0] = map.get(key);
			pkList.add(value);
			Object msg = service.assignInvmandoc(targetPks, pkList, null, key);
			
			// 抛出异常日志
			getLogMsg(msg);
			pkList.clear();
		}
	}

	/**
	 * 分配计划价到存货管理档案和物资生产档案
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-3-7下午02:53:10
	 * @param pk_invbasdoc
	 * @param vo
	 * @throws BusinessException
	 */
	public void updatePrice(String pk_invbasdoc, NewMaterialsVO vo)
			throws BusinessException {
		
		//分配计划价到存货管理档案
		String sqlman = "update bd_invmandoc set "+ HgPubConst.INVBAS_NPP_FIELDNAME + "= 0,planprice = "
				+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())+ " where pk_invbasdoc ='" + pk_invbasdoc
				+ "' and isnull(dr,0)=0";
		getBaseDao().executeUpdate(sqlman);
		
		//分配计划价到物资生产档案
		String sqlpro = "update bd_produce set jhj = "+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())
				+ ",nbzyj = "+ PuPubVO.getUFDouble_NullAsZero(vo.getNplanprice())
				+ " where pk_invbasdoc ='" + pk_invbasdoc+ "' and isnull(dr,0)=0";
		getBaseDao().executeUpdate(sqlpro);
		
		//分配可用量计算方案到物资生产档案
		String  sqlinfo = " update bd_produce set usableamount ='"+HgPubConst.usableamount+"',usableamountbyfree='"+HgPubConst.usableamountbyfree+"' where pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(dr,0)=0 ";
		getBaseDao().executeUpdate(sqlinfo);
	}

	/**
	 * 获取抛出的异常日志
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-4-13上午10:28:34
	 * @param os
	 * @throws BusinessException
	 */
	public void getLogMsg(Object os) throws BusinessException {
		
		if (os != null) {
			ErrLogReturnValue errmsg = (ErrLogReturnValue) os;
			
			if (errmsg != null) {
				BDErrLogResult log = errmsg.getErrLogResult();
				
				if(log != null){
					String str = log.getErrorMessage();
					
					if (PuPubVO.getString_TrimZeroLenAsNull(str) != null) {
						throw new BusinessException(str);
					}
					
					ErrlogmsgVO[] logmsgvos = log.getErrlogmsgs();
					if (logmsgvos != null && logmsgvos.length > 0) {
						
						int num = logmsgvos.length;
						for (int i = 0; i < num; i++) {
							String logmsg = logmsgvos[i].getErrormsg();
							if (PuPubVO.getString_TrimZeroLenAsNull(logmsg) != null)
								throw new BusinessException(logmsg);
						}
					}
				}
			}
		}
	}

	public void isHaveChildren(String pk_invcl) throws BusinessException {

		String sql = " select invclasscode from bd_invcl where pk_invcl = '"+ pk_invcl + "'";
		Object oinvcode = getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR);
		String invcode = PuPubVO.getString_TrimZeroLenAsNull(oinvcode);
		sql = "select count(pk_invcl) from bd_invcl where (invclasscode like '"+ invcode + "%')";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);

		if (o != null) {
			int number = 1;
			number = new Integer(o.toString()).intValue();

			if (number > 1)
				throw new BusinessException("该存货存在着下级分类");
		}
	}

	/**
	 * 新物资需求申请VO转换成基本档案VO
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-4-27下午04:42:05
	 * @param vo新物资需求申请VO
	 * @return
	 * @throws BusinessException
	 */
	private InvbasdocVO changeNewMaterialsVOToInvbasdocVO(NewMaterialsVO vo) throws BusinessException{
		
		InvbasdocVO invVO = new InvbasdocVO();
		// 采购方式
		invVO.setAttributeValue(HgPubConst.INVBAS_PURTYPE_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(getInvbasVmi(vo.getIjjway())));
		// 是否代储代销
		invVO.setAttributeValue(HgPubConst.INVBAS_VMI_FIELDNAME, PuPubVO
				.getString_TrimZeroLenAsNull(PuPubVO.getUFBoolean_NullAs(vo.getBisdcdx(), UFBoolean.FALSE)));
		// 是否重要物资
		invVO.setAttributeValue(HgPubConst.INVBAS_IMPROT_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getBiszywz()));
		// 是否交旧物资
		invVO.setAttributeValue(HgPubConst.INVBAS_OLD_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getBisjjwz()));
		
		invVO.setInvcode(vo.getInvcode());// 编码
		invVO.setInvname(vo.getVinvname());// 名称
		invVO.setInvspec(vo.getVinvspec());// 规格
		invVO.setInvtype(vo.getVinvtype());// 型号
		invVO.setPk_measdoc(vo.getPk_measdoc());// 单位
		invVO.setPk_invcl(vo.getPk_invcl());// 分类
		
		invVO.setAttributeValue(HgPubConst.INVBAS_VMT_FIELDNAME, vo.getVmaterial());// 材质
		invVO.setAttributeValue(HgPubConst.INVBAS_NPP_FIELDNAME, PuPubVO.getString_TrimZeroLenAsNull(vo.getNplanprice()));// 计划价
		invVO.setAttributeValue(HgPubConst.INVBAS_VTS_FIELDNAME, vo.getVtechstan());// 技术标准
		invVO.setInvmnecode(vo.getInvmnecode());// 助记码
		invVO.setPk_taxitems(vo.getPk_taxitems());// 税目
		
		invVO.setDiscountflag(PuPubVO.getUFBoolean_NullAs(vo.getDiscountflag(),UFBoolean.FALSE));// 价格折扣
		invVO.setLaborflag(PuPubVO.getUFBoolean_NullAs(vo.getLaborflag(),UFBoolean.FALSE));// 应税劳务
		invVO.setPk_corp(HgPubConst.PK_CORP);
		
		invVO.setStoreunitnum(vo.getStoreunitnum());//多少标准存储单位
		invVO.setPk_prodline(vo.getPk_prodline());//产品线属性
		invVO.setSealflag(vo.getSealflag());//封存标志
	    invVO.setIsmngstockbygrswt(vo.getIsmngstockbygrswt());//按毛重管理库存
	    
	    invVO.setGraphid(vo.getGraphid());//图号
	    invVO.setForinvname(vo.getForinvname());//外文名称
	    invVO.setSetpartsflag(vo.getSetpartsflag());//是否成套件
		invVO.setPk_measdoc1(vo.getPk_measdoc1());//销售默认单位
		invVO.setPk_measdoc2(vo.getPk_measdoc2());//采购默认单位
		invVO.setPk_measdoc3(vo.getPk_measdoc3());//库存默认单位
		invVO.setPk_measdoc5(vo.getPk_measdoc5());//生产默认单位
		
		invVO.setInvpinpai(vo.getInvpinpai());//品牌
		invVO.setWeitunitnum(vo.getWeitunitnum());//多少标准重量单位
		invVO.setLength(PuPubVO.getString_TrimZeroLenAsNull(vo.getLength()));//长度
		invVO.setIsstorebyconvert(vo.getIsstorebyconvert());//换算率结存
		invVO.setAssistunit(PuPubVO.getUFBoolean_NullAs(vo.getAssistunit(),UFBoolean.FALSE));//是否辅计量管理
		invVO.setShipunitnum(vo.getShipunitnum());//多少标准运输单位
		invVO.setAutobalancemeas(vo.getAutobalancemeas());//自动平衡主辅计量
		invVO.setInvbarcode(vo.getInvbarcode());//条形码
		invVO.setInvshortname(vo.getInvshortname());//存货简称

		invVO.setUnitvolume(vo.getUnitvolume());//单位重量
		invVO.setUnitweight(vo.getUnitweight());////单位体积
		invVO.setWidth(PuPubVO.getString_TrimZeroLenAsNull(vo.getWidth()));//宽度
		invVO.setHeight(PuPubVO.getString_TrimZeroLenAsNull(vo.getHeight()));//高度
		invVO.setPk_measdoc6(vo.getPk_measdoc6());//零售计量单位
		invVO.setIsretail(vo.getIsretail());//是否零售
		invVO.setStatus(VOStatus.NEW);
		invVO.setDr(0);
		invVO.setCreator(vo.getVapproveid());
		invVO.setCreatetime(vo.getTs());
		
		return invVO;
	}
	
	public void  isExitVcode(NewMaterialsVO vo) throws BusinessException{
		String invcode = vo.getInvcode();
		// 校验存货基本档案中是否存在该存货编码的存货

		if (invcode != null) {
			String sql = "select count(*) from bd_invbasdoc where invcode ='"+ invcode + "' and isnull(dr,0)=0";

			int o = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR), HgPubTool.INTEGER_ZERO_VALUE);

			if (o > 0) {
				throw new BusinessException("该系统中已有该存货!");
			}
		}
	}
	
	public void  isExitVcnameAndVspec(NewMaterialsVO vo) throws BusinessException{
		String invname = vo.getVinvname();
		String invspec = vo.getVinvspec();
		// 校验存货基本档案中是否存在该名称+规格

		if (invspec != null || invname !=null ) {
			String sql = "select count(*) from bd_invbasdoc where invname ='"+ invname + "' and invspec ='"+invspec+"' and isnull(dr,0)=0";

			int o = PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR), HgPubTool.INTEGER_ZERO_VALUE);

			if (o > 0) {
				throw new BusinessException("该系统中已有该存货!");
			}
		}
	}

}
