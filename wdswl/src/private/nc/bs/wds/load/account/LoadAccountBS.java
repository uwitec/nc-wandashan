package nc.bs.wds.load.account;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.LoadpriceVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator 装卸费核算后台类
 */
public class LoadAccountBS {
	// 当前登录公司
	public static int LOADFEE=0;//出库装货类型
	public static int UNLOADFEE=1;//入库卸货类型
	
	private String pk_corp = null;
//	当前登录仓库
	private String cwarehouseid = null;
	// <存货管理id,存货装卸价格定义VO>
	Map<String, LoadpriceVO> invLoadPrice = null;

	ArrayList<LoadpriceB1VO> list = new ArrayList<LoadpriceB1VO>();

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

//	/**
//	 * 
//	 * @作者：lyf
//	 * @说明：完达山物流项目 :装卸费核算
//	 * @时间：2011-5-16下午03:12:30
//	 * @param vos
//	 *            参照上游选择的上游数据，来源（其他出库单(WDS6)，销售出库单(WDS8)，其他入库单(WDS7)，调拨入库单(WDS9)）
//	 *            来源数据能是同一种类型 其他出库和销售出库是同一个库表，同一个VO 其他入库和调拨入库是同一个库表，同一个VO
//	 * @return
//	 * @throws BusinessException
//	 */
//	public AggregatedValueObject accoutLoadPrice(AggregatedValueObject[] vos,
//			String corpid,String cwarehouseid) throws BusinessException {
//		this.pk_corp = corpid;
//		this.cwarehouseid = cwarehouseid;
//		// 得到装卸费价格定义
//		getInvLoadPrice();
//		// 根据来源数据的不同转换成实际的数据类型
//		AggregatedValueObject vo = vos[0];
//		String billType = (String) vo.getParentVO().getAttributeValue(
//				"vbilltype");
//		if (billType == null || "".equalsIgnoreCase(billType)) {
//			billType = (String) vo.getParentVO().getAttributeValue(
//					"geh_cbilltypecode");
//		}
//		// 其它出库，销售出库装卸费核算
//		if (WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(billType)
//				|| WdsWlPubConst.BILLTYPE_SALE_OUT.equalsIgnoreCase(billType)) {
//			for (AggregatedValueObject value : vos) {
//				accountOutBill(value);
//			}
//		}
//		// 其他入库，调拨入库装卸费核算
//		else if (WdsWlPubConst.BILLTYPE_OTHER_IN.equalsIgnoreCase(billType)
//				|| WdsWlPubConst.BILLTYPE_ALLO_IN.equalsIgnoreCase(billType)) {
//			for (AggregatedValueObject value : vos) {
//				accountInBill(value);
//			}
//		}
//		ExaggLoadPricVO agg = new ExaggLoadPricVO();
//		agg.setParentVO(null);
//		agg.setTableVO(agg.getTableCodes()[0], list
//				.toArray(new LoadpriceB1VO[0]));
//		agg.setTableVO(agg.getTableCodes()[1], null);
//		return agg;
//	}
	/**
	 * 装卸费计算
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-9-23下午01:41:33
	 * @param vos
	 * @param corpid
	 * @param cwarehouseid
	 * @return
	 * @throws BusinessException
	 */
	public void accoutLoadPrice(AggregatedValueObject billvo,String corpid,String cwarehouseid,int lodytype) throws BusinessException {
		this.pk_corp = corpid;
		this.cwarehouseid = cwarehouseid;
		// 得到装卸费价格定义
		getInvLoadPrice();
		//计算装卸费
		accountBill(billvo,lodytype);
	}	


	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 查询 存货的装卸价格定义
	 * @时间：2011-5-16下午03:56:46
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, LoadpriceVO> getInvLoadPrice() throws BusinessException {
		String sql = "select * from wds_loadprice where pk_corp='" + pk_corp
				+ "' and isnull(dr,0)=0 and cwarehouseid = '"+cwarehouseid+"'";
		Object o = getBaseDAO().executeQuery(sql,
				new BeanListProcessor(LoadpriceVO.class));
		if (o == null) {
			throw new BusinessException("请先维护存装卸费价格");
		} else {
			invLoadPrice = new HashMap<String, LoadpriceVO>();
			List<LoadpriceVO> list = (List<LoadpriceVO>) o;
			for (int i = 0; i < list.size(); i++) {
				invLoadPrice.put(list.get(i).getPk_invmandoc(), list.get(i));
			}
		}
		return invLoadPrice;
	}
	/**
	 * 
	 * 计算装卸费
	 * @作者：mlr 
	 * @说明：完达山物流项目 
	 * @时间：2011-5-16下午07:26:09
	 */
	private void accountBill(AggregatedValueObject billVO,int lodytype)throws BusinessException {
		if(billVO==null || billVO.getParentVO()==null){
			return;
		}
		ExaggLoadPricVO mybill=(ExaggLoadPricVO)billVO;
		
		if(mybill.getTableVO(mybill.getTableCodes()[0])==null || mybill.getTableVO(mybill.getTableCodes()[0]).length==0){
			return;
		}
		LoadpriceB1VO[] vos1=(LoadpriceB1VO[]) mybill.getTableVO(mybill.getTableCodes()[0]);
		 UFDouble feess=new UFDouble();
		for (LoadpriceB1VO bvo : vos1) {
			// 判断存货是否定义装卸费价格，如果没有定义，
			if (!invLoadPrice.containsKey(bvo.getPk_invmandoc())) {
				String invcode = getInvCodeByManid(bvo.getPk_invmandoc());
				throw new BusinessException("请维护存货装卸费价格,存货编码:" + invcode);
			}
			LoadpriceVO loadPricvo = invLoadPrice.get(bvo.getPk_invmandoc());
			// 计算费用
		    if(lodytype==LOADFEE){
			bvo.setNloadprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNloadprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// 装货费用
		    }else if(lodytype==UNLOADFEE){
				bvo.setNunloadprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNunloadprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// 卸货费用	              
		    }
			bvo.setNtagprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNtagprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNtagnum())));// 贴签费用
			if (loadPricvo.getFiscoded() != null&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {
				// 是否采码
				bvo.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNcodeprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// 采码费用
			}
			 UFDouble fees=new UFDouble();		    	
   		   fees=PuPubVO.getUFDouble_NullAsZero( bvo.getNloadprice())
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNunloadprice()))
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNcodeprice()))
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNtagprice()));
   		   feess=feess.add(fees);			      	    
		}
		//设置表头的总费用		
		LoadpriceHVO hvo=(LoadpriceHVO)(mybill.getParentVO());
		hvo.setVzfee(feess);
		hvo.setFloadtype(lodytype);
		
		if(mybill.getTableVO(mybill.getTableCodes()[1])==null|| mybill.getTableVO(mybill.getTableCodes()[1]).length==0){
			return;
		}
		//如果只有一个班组，则将总费用赋值给给这个班组
		if(mybill.getTableVO(mybill.getTableCodes()[1]).length==1){
			mybill.getTableVO(mybill.getTableCodes()[1])[0].setAttributeValue("nloadprice",feess);
			//liuys add 增加表体2装卸费金额
			mybill.getTableVO(mybill.getTableCodes()[0])[0].setAttributeValue("nloadprice",feess);
		}
	}

//
//	/**
//	 * 
//	 * @throws BusinessException
//	 * @作者：lyf
//	 * @说明：完达山物流项目 出库单计算生成 装卸费结算单来源明细VO
//	 * @时间：2011-5-16下午07:26:09
//	 */
//	private void accountOutBill(AggregatedValueObject vo)
//			throws BusinessException {
//		TbOutgeneralHVO hvo = (TbOutgeneralHVO) vo.getParentVO();
//		TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) vo.getChildrenVO();
//		for (TbOutgeneralBVO bvo : bvos) {
//			// 判断存货是否定义装卸费价格，如果没有定义，
//			if (!invLoadPrice.containsKey(bvo.getCinventoryid())) {
//				String invcode = getInvCodeByManid(bvo.getCinventoryid());
//				throw new BusinessException("请维护存货装卸费价格,存货编码:" + invcode);
//			}
//			LoadpriceVO loadPricvo = invLoadPrice.get(bvo.getCinventoryid());
//			LoadpriceB1VO b1 = new LoadpriceB1VO();
//			// 计算费用
//			b1.setNloadprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNloadprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassistnum())));// 装货费用
//			b1.setNtagprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNtagprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getNtagnum())));// 贴签费用
//			if (loadPricvo.getFiscoded() != null
//					&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {// 是否采码
//				b1
//						.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(
//								loadPricvo.getNcodeprice()).multiply(
//								PuPubVO.getUFDouble_NullAsZero(bvo
//										.getNoutassistnum())));// 采码费用
//			}
//			// 设置存货数量信息
//			b1.setPk_invmandoc(bvo.getCinventoryid());
//			b1.setPk_invbasdoc(bvo.getCinvbasid());
//			b1.setVbatchecode(bvo.getVbatchcode());// 批次号
//			b1.setNhsl(bvo.getHsl());// 换算率
//			b1.setCunitid(bvo.getUnitid());// 计量主单位
//			b1.setCassunitid(bvo.getCastunitid());// 辅计量单位
//			b1.setNoutnum(bvo.getNoutnum());// 实发数量
//			b1.setNassoutnum(bvo.getNoutassistnum());// 实发辅数量
//			b1.setNshouldoutnum(bvo.getNshouldoutnum());// 应发数量
//			b1.setNassshouldoutnum(bvo.getNshouldoutassistnum());// 应发辅数量
//			b1.setNtagnum(bvo.getNtagnum());// 贴签数量
//
//			// 来源单据信息
//			b1.setVsourcebillcode(hvo.getVbillcode());
//			b1.setCsourcetype(hvo.getVbilltype());
//			b1.setCsourcebillhid(bvo.getGeneral_pk());
//			b1.setCsourcebillbid(bvo.getGeneral_b_pk());
//			b1.setVfirstbillcode(bvo.getVfirstbillcode());
//			b1.setCfirsttype(bvo.getCfirsttyp());
//			b1.setCfirstbillhid(bvo.getCfirstbillhid());
//			b1.setCfirstbillbid(bvo.getCfirstbillbid());
//			list.add(b1);
//
//		}
//	}

//	/**
//	 * 
//	 * @throws BusinessException
//	 * @作者：lyf
//	 * @说明：完达山物流项目 入库单计算生成 装卸费结算单
//	 * @时间：2011-5-16下午07:26:09
//	 */
//	private void accountInBill(AggregatedValueObject vo)
//			throws BusinessException {
//
//		TbGeneralHVO hvo = (TbGeneralHVO) vo.getParentVO();
//		TbGeneralBVO[] bvos = (TbGeneralBVO[]) vo.getChildrenVO();
//		for (TbGeneralBVO bvo : bvos) {
//			// 判断存货是否定义装卸费价格，如果没有定义，
//			if (!invLoadPrice.containsKey(bvo.getGeb_cinventoryid())) {
//				String invcode = getInvCodeByManid(bvo.getGeb_cinventoryid());
//				throw new BusinessException("请维护存货装卸费价格,存货编码:" + invcode);
//			}
//			LoadpriceVO loadPricvo = invLoadPrice
//					.get(bvo.getGeb_cinventoryid());
//			LoadpriceB1VO b1 = new LoadpriceB1VO();
//			// 计算费用
//			b1.setNunloadprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNunloadprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getGeb_banum())));// 卸货费用
//			
////			zhf   add  入库不需要核算采码费     注释掉
////			if (loadPricvo.getFiscoded() != null
////					&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {// 是否采码
////				b1.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(
////						loadPricvo.getNcodeprice()).multiply(
////						PuPubVO.getUFDouble_NullAsZero(bvo.getGeb_banum())));// 采码费用
////			}
//			// 设置存货数量信息
//			b1.setPk_invmandoc(bvo.getGeb_cinventoryid());
//			b1.setPk_invbasdoc(bvo.getGeb_cinvbasid());
//			b1.setVbatchecode(bvo.getGeb_vbatchcode());// 批次号
//			b1.setNhsl(bvo.getGeb_hsl());// 换算率
//			b1.setCunitid(bvo.getPk_measdoc());// 计量主单位
//			b1.setCassunitid(bvo.getCastunitid());// 辅计量单位
//			b1.setNoutnum(bvo.getGeb_anum());// 实发数量
//			b1.setNassoutnum(bvo.getGeb_banum());// 实发辅数量
//			b1.setNshouldoutnum(bvo.getGeb_snum());// 应发数量
//			b1.setNassshouldoutnum(bvo.getGeb_bsnum());// 应发辅数量
//
//			// 来源单据信息
//			b1.setVsourcebillcode(hvo.getGeh_billcode());
//			b1.setCsourcetype(hvo.getGeh_billtype());
//			b1.setCsourcebillhid(bvo.getGeh_pk());
//			b1.setCsourcebillbid(bvo.getGeb_pk());
//			b1.setVfirstbillcode(bvo.getVfirstbillcode());
//			b1.setCfirsttype(bvo.getCfirsttype());
//			b1.setCfirstbillhid(bvo.getCfirstbillhid());
//			b1.setCfirstbillbid(bvo.getCfirstbillbid());
//			list.add(b1);
//		}
//
//	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 " 根据存货管理id 得到 存货编码
	 * @时间：2011-5-16下午08:58:34
	 * @param pk_invmandoc
	 * @return
	 * @throws DAOException
	 */
	private String getInvCodeByManid(String pk_invmandoc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select invcode from bd_invbasdoc where pk_invbasdoc in (");
		sql.append(" select pk_invbasdoc from bd_invmandoc ");
		sql.append(" where isnull(dr,0)=0 ");
		sql.append(" and pk_corp='" + pk_corp + "'");
		sql.append(" and pk_invmandoc='" + pk_invmandoc + "'");
		sql.append(" )");
		Object o = getBaseDAO().executeQuery(sql.toString(),
				WdsPubResulSetProcesser.COLUMNPROCESSOR);
		if (o == null) {
			throw new BusinessException("查询存货管理档案异常:存货管理id='" + pk_invmandoc
					+ "',不存在");
		}
		return o.toString();
	}

	/**
	 * 
	 * @throws BusinessException 
	 * @作者：lyf
	 * @说明：完达山物流项目 装卸费计算完成，回写出入库单装卸费计算完成标识
	 * 只有参照新增和作废的时候需要回写
	 * @时间：2011-5-17下午08:02:04
	 */
	public void writeBack(AggregatedValueObject billvo) throws BusinessException {
		if (billvo == null)
			return;
		ExaggLoadPricVO axbillvo = (ExaggLoadPricVO)billvo;
		CircularlyAccessibleValueObject[] b1vos = axbillvo.getTableVO(axbillvo
				.getTableCodes()[0]);
		if(b1vos == null || b1vos.length ==0)
			return;
	   //过滤来源单据表
		ArrayList<String> csourcebillhidout = new ArrayList<String>();// 出库单来源单据表头id
		ArrayList<String> csourcebillhidin = new ArrayList<String>();// 入库单来源单据表头id	
		for (CircularlyAccessibleValueObject b1vo : b1vos) {
			String csourcetype = (String) b1vo.getAttributeValue("csourcetype");
			if (csourcetype == null || csourcetype.equals(""))
				continue;
			String cousbillhid = (String)b1vo.getAttributeValue("csourcebillhid");
			if (WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(csourcetype)
					|| WdsWlPubConst.BILLTYPE_SALE_OUT
							.equalsIgnoreCase(csourcetype)) {
				
				if(csourcebillhidout.contains(cousbillhid))
					continue;
				csourcebillhidout.add(cousbillhid);
			} else if (WdsWlPubConst.BILLTYPE_OTHER_IN
					.equalsIgnoreCase(csourcetype)
					|| WdsWlPubConst.BILLTYPE_ALLO_IN
							.equalsIgnoreCase(csourcetype)) {
				if(csourcebillhidin.contains(cousbillhid))
					continue;
				csourcebillhidin.add(cousbillhid);
			}
		}
		String key = axbillvo.getParentVO().getPrimaryKey();
		String vaule = null;
		if( key == null || "".equalsIgnoreCase(key)){
			vaule ="Y";
		}else{
			vaule ="N";
		}
		String outSql =" update tb_outgeneral_h set fisload='"+vaule+"' where general_pk=?";
		String inSql =" update tb_general_h set fisload='"+vaule+"' where geh_pk=?";
		SQLParameter parameter = new SQLParameter();
		//出库
		for(int i=0;i<csourcebillhidout.size();i++){
			parameter.addParam(csourcebillhidout.get(i));
			getBaseDAO().executeUpdate(outSql, parameter);
			parameter.clearParams();
		}
		//liuys modify  修改低级错误入库
		for(int i=0;i<csourcebillhidin.size();i++){
			parameter.addParam(csourcebillhidin.get(i));
			getBaseDAO().executeUpdate(inSql, parameter);
			parameter.clearParams();
		}
	}

}
