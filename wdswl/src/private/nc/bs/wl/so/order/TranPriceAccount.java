package nc.bs.wl.so.order;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 销售运单运费核算后台类
 * 
 * @author Administrator
 * 
 */
public class TranPriceAccount {

	private AggregatedValueObject curBillvo = null;
	// 当前登录日期
	private UFDate date = null;
	// 运价表类型
	private String colType = null;
	
	//是否总仓
	private boolean isZC = true;
	// 分仓与客商绑定关系VO
	private TbStorcubasdocVO bingVO = null;
	// 收货地区
	private String reareaid = null;
	// 当前使用的 运价表子表
	private TranspriceBVO curTranpriceBvo = null;

	// 汇总的主数量 辅助数量
	private ArrayList<UFDouble> totalNum = new ArrayList<UFDouble>();

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 计算运费
	 * @时间：2011-5-19下午08:34:49
	 * @param billvo
	 * @param logDate
	 *            登录日期
	 * @param operatorid
	 *            操作员
	 * @return
	 * @throws BusinessException
	 */
	public AggregatedValueObject colTransCost(AggregatedValueObject billvo,
			UFDate logDate, String operatorid) throws BusinessException {
		if (billvo == null || billvo.getChildrenVO() == null
				|| billvo.getChildrenVO().length == 0)
			throw new BusinessException("传入数据异常");
		if (logDate == null) {
			this.date = new UFDate(System.currentTimeMillis());
		} else {
			this.date = logDate;
		}
		this.curBillvo = billvo;
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		head.setVcolpersonid(operatorid);
		// 确定运费计算类型（吨公里，或者箱数）
		setColType();
		// 查找运价表
		if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
			List<TranspriceBVO> lprice = getDsPriceInfor(colType, head
					.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
			if (lprice == null || lprice.size() == 0) {
				throw new BusinessException("未查询到匹配的运价表");
			}
			curTranpriceBvo = lprice.get(0);
		} else {
			List<TranspriceBVO> lprice = getXsPriceInfor(colType, head
					.getPk_transcorp(), head.getPk_outwhouse(), reareaid, true);
			if (lprice == null || lprice.size() == 0) {
				throw new BusinessException("未查询到匹配的运价表");
			}
			curTranpriceBvo = lprice.get(0);
		}
		// 计算运费，并且扫描运费信息到发运单
		appendPriceInfor();
		// 保存运费
		doSavePriceInfor();
		return billvo;
	}

	/**
	 * 
	 * @throws BusinessException
	 * @作者lyf 更新 发运单 运费相关信息
	 * @说明：完达山物流项目
	 * @时间：2011-6-10上午10:21:09
	 */
	public void appendPriceInfor() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		// 公里数
		head.setNgls(bingVO.getKilometer());
		// 运价: 实际价格=原始价格*（1+修正比例）
		UFDouble nrate = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNadjrate());
		head
				.setNtranprice((getTransprice().multiply(nrate.div(100).add(1),
						8)));
		if (isAdjust()) {
			// 零担调整值
			head.setNadjustprice(curTranpriceBvo.getNpriceadj());
			// 零担调整类型
			head.setIadjusttype(curTranpriceBvo.getIadjtype());
		}
		// 计算运费
		setNcolmny();
	}

	/**
	 * 保存运费信息
	 * 
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		getBaseDAO().updateVO(
				head,
				new String[] { "ngls", "ntranprice", "nadjustprice",
						"iadjusttype", "ntransmny", "vcolpersonid","custareaid","icoltype"  });
		String sql = "select ts from wds_soorder where pk_soorder = '"
				+ head.getPrimaryKey() + "'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO()
				.executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		head.setTs(new UFDateTime(ts));
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：判断是否 属于零散发运 零散发运，取零散发运运价
	 * @时间：2011-6-10上午10:25:57
	 * @return
	 */
	public UFDouble getTransprice() {
		UFDouble price = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNtransprice());
		// 比较 汇总数量是否大于零散发运数量
		if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
			UFDouble ntotalNum = PuPubVO
					.getUFDouble_NullAsZero(totalNum.get(0));
			UFDouble nsmallnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
					.getNsmallnum());
			UFDouble num1 = ntotalNum.sub(nsmallnum);
			if (num1.doubleValue() <= 0) {
				price = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
						.getNsmallprice());
			}
		}
		return price;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：判断是否 属于零担发运 零担发运需要设置 零担调整值
	 * @时间：2011-6-10上午10:34:50
	 * @return
	 */
	public boolean isAdjust() {
		if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
			UFDouble ntotalAssNum = PuPubVO.getUFDouble_NullAsZero(totalNum
					.get(1));
			UFDouble nsmallnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
					.getNadjustnum());
			UFDouble num1 = ntotalAssNum.sub(nsmallnum);
			if (num1.doubleValue() <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @throws BusinessException
	 * @作者：lyf
	 * @说明: 设置运费
	 * @时间：2011-5-20下午01:22:14
	 */
	void setNcolmny() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		UFDouble nnum = null;
		UFDouble ngls = null;
		UFDouble nprice = null;
		if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)|| !isZC) {
			nnum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(0));
			ngls = PuPubVO.getUFDouble_NullAsZero(head.getNgls());
			nprice = PuPubVO.getUFDouble_NullAsZero(head.getNtranprice());
			UFDouble nadustmny = null;
			Integer iadjstType = PuPubVO.getInteger_NullAs(head
					.getIadjusttype(), 1);
			if (iadjstType == 0) {// 按照吨调整
				nadustmny = PuPubVO.getUFDouble_NullAsZero(
						head.getNadjustprice()).multiply(nnum);
			} else {// 按照箱调整
				nadustmny = PuPubVO.getUFDouble_NullAsZero(
						head.getNadjustprice()).multiply(
						PuPubVO.getUFDouble_NullAsZero(totalNum.get(1)));
			}
			head.setNtransmny(nprice.multiply(nnum, 8).multiply(ngls, 8).add(
					nadustmny));
		} else {
			nnum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(1));
			nprice = PuPubVO.getUFDouble_NullAsZero(head.getNtranprice());
			head.setNtransmny(nprice.multiply(nnum, 8));
		}
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：根据主数量汇总值，查询分仓与承运商绑定关系，得到零担吨数， 比较 得到运费计算类型
	 * @时间：2011-6-9下午10:39:58
	 * @return
	 */
	public void setColType() throws BusinessException {
		SoorderVO head = (SoorderVO) curBillvo.getParentVO();
		SoorderBVO[] bodys = (SoorderBVO[]) curBillvo.getChildrenVO();
		String pk_transcorp = head.getPk_transcorp();
		if (pk_transcorp == null || "".equalsIgnoreCase(pk_transcorp)) {
			throw new BusinessException("请先选择承运商");
		}
		String pk_outwhouse = head.getPk_outwhouse();
		if (pk_outwhouse == null || "".equalsIgnoreCase(pk_outwhouse)) {
			throw new BusinessException("未取到发货站");
		}
		if(WdsWlPubConst.WDS_WL_ZC.equalsIgnoreCase(pk_outwhouse)){
			isZC = true;
		}else{
			isZC = false;
		}
		String pk_custman = head.getPk_cumandoc();
		if (pk_custman == null || "".equalsIgnoreCase(pk_custman)) {
			throw new BusinessException("未取到客商信息");
		}
		// 获得分仓与客商的绑定VO
		getBingVo(pk_outwhouse, pk_custman);
		this.reareaid = bingVO.getCustareaid();
		if (reareaid == null || "".equalsIgnoreCase(reareaid)) {
			throw new BusinessException("请在分仓客商绑定节点，维护客商所属地区");
		}
		head.setCustareaid(reareaid);
		// 汇总主数量
		UFDouble ntotalNum = new UFDouble(0);
		UFDouble ntotalAssNUm = new UFDouble(0);
		for (SoorderBVO body : bodys) {
			ntotalNum = ntotalNum.add(PuPubVO.getUFDouble_NullAsZero(body
					.getNoutnum()), 8);
			ntotalAssNUm = ntotalAssNUm.add(PuPubVO.getUFDouble_NullAsZero(body
					.getNassoutnum()), 8);			
		}
		totalNum.add(ntotalNum);
		totalNum.add(ntotalAssNUm);
		Integer IcolType = PuPubVO.getInteger_NullAs(head.getIcoltype(), 0);
		if(IcolType==0){
			// 查询分仓与承运商的绑定关系，得到零担吨数
			UFDouble nsmallNum = getNsmallNum(pk_outwhouse, pk_transcorp);
			// 如果汇总主数量的 大于 零担数量 按照吨公里;否则按照箱数
			UFDouble sumNum = ntotalNum.sub(nsmallNum, 8);
			if (sumNum.doubleValue() > 0) {
				colType = WdsWlPubConst.WDSI;
				head.setIcoltype(1);
			} else {
				colType = WdsWlPubConst.WDSJ;
				head.setIcoltype(2);
			}
		
		}else if(IcolType==1){
			colType = WdsWlPubConst.WDSI;
		}else if(IcolType ==2){
			colType = WdsWlPubConst.WDSJ;
		}
	
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-6-9下午10:52:12
	 * @param pk_outwhouse:仓库id
	 *            pk_transcorp 承运商id
	 * @return 零担数量
	 * @throws DAOException
	 */
	public UFDouble getNsmallNum(String pk_outwhouse, String pk_transcorp)
			throws DAOException {
		String sql = "select  nsmallnum from wds_stortranscorp_b where pk_stordoc='"
				+ pk_outwhouse
				+ "' and pk_wds_tanscorp_h='"
				+ pk_transcorp
				+ "' and isnull(dr,0)=0";
		Object o = getBaseDAO().executeQuery(sql,
				WdsPubResulSetProcesser.COLUMNPROCESSOR);
		return PuPubVO.getUFDouble_NullAsZero(o);
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 获得分仓客商绑定关系
	 * @时间：2011-6-9下午11:34:44
	 * @param pk_outwhouse
	 *            发货站
	 * @param pk_custman
	 *            客商
	 * @throws BusinessException
	 */
	public void getBingVo(String pk_outwhouse, String pk_custman)
			throws BusinessException {
		String sql = "select * from tb_storcubasdoc  where pk_stordoc='"
				+ pk_outwhouse + "' and pk_cumandoc='" + pk_custman
				+ "' and isnull(dr,0)=0";
		ArrayList<TbStorcubasdocVO> list = (ArrayList<TbStorcubasdocVO>)getBaseDAO().executeQuery(sql,
				new BeanListProcessor(TbStorcubasdocVO.class));
		if (list == null || list.size()==0) {
			throw new BusinessException("请维护客商与分仓的绑定关系");
		}
		this.bingVO = (TbStorcubasdocVO) list.get(0);
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-6-9下午11:26:45
	 * @param pricetype
	 *            单据类型
	 * @param pk_transcorp
	 *            承运商
	 * @param pk_outwhouse
	 *            发货仓库
	 * @param reareaid
	 *            收货地
	 * @param isDG
	 *            是否继续递归
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getDsPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// 运价表单据类型
		// :吨公里运价表和箱数运价表适用同一个库表，但是单据类型不同
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// 承运商
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// 发货仓库
		sqlb.append(" and (isnull(b.ifw,0) = 0 or b.ifw =1) ");// 应运范围过滤
		sqlb.append(" and h.dstartdate <= '" + date + "' and h.denddate >= '"
				+ date + "'");
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// 如果没有获取对应收货地区的定义，查询有没有上级的定义
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				// 如果没有上级分类，取东三省内或者东三省外定义
				String name = getAreaName(reareaid);
				// 地区分类编码 黑龙江省='01' 吉林省='02' 辽宁省='03'
				if (name.contains("黑龙江") || name.contains("吉林")
						|| name.contains("辽宁")) {
					pk_fatherarea = getAreaIdByCode("0001");
					if (pk_fatherarea == null
							|| "".equalsIgnoreCase(pk_fatherarea)) {
						throw new BusinessException(
								"请以 0001 作为地区分类编码 定义东三省内地区分类");
					}
				} else {
					pk_fatherarea = getAreaIdByCode("0002");
					if (pk_fatherarea == null
							|| "".equalsIgnoreCase(pk_fatherarea)) {
						throw new BusinessException(
								"请以 0002 作为地区分类编码 定义东三省外地区分类");
					}
				}
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// 如果有上进地区分类，递归查询
				lprice =getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：查询箱数运价表
	 * @时间：2011-6-10上午09:59:35
	 * @param pricetype
	 *            运价表类型
	 * @param pk_transcorp
	 *            承运商
	 * @param pk_outwhouse
	 *            发货仓库
	 * @param reareaid
	 *            收货地区
	 * @param isDG
	 *            是否递归
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getXsPriceInfor(String pricetype,
			String pk_transcorp, String pk_outwhouse, String reareaid,
			boolean isDG) throws BusinessException {
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for (String name : names) {
			sqlb.append(" b." + name + ",");
		}
		sqlb.append(" 'aaa'");
		sqlb
				.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb
				.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// 运价表单据类型
		// :吨公里运价表和箱数运价表适用同一个库表，但是单据类型不同
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// 承运商
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// 发货仓库
		sqlb.append(" and (isnull(b.ifw,0)=0 or b.ifw =2) ");// 应运范围过滤
		if (PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql()) != null)// 箱数区间过滤
			sqlb.append(" and " + getPricePeriodWhereSql());
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// 如果没有获取对应收货地区的定义，查询有没有上级的定义
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				// 如果没有上级分类，取东三省内或者东三省外定义
				String name = getAreaName(reareaid);
				// 地区分类编码 黑龙江省='01' 吉林省='02' 辽宁省='03'
				if (name.contains("黑龙江") || name.contains("吉林")
						|| name.contains("辽宁")) {
					pk_fatherarea = getAreaIdByCode("0001");
					if (pk_fatherarea == null
							|| "".equalsIgnoreCase(pk_fatherarea)) {
						throw new BusinessException(
								"请以 0001 作为地区分类编码 定义东三省内地区分类");
					}
				} else {
					pk_fatherarea = getAreaIdByCode("0002");
					if (pk_fatherarea == null
							|| "".equalsIgnoreCase(pk_fatherarea)) {
						throw new BusinessException(
								"请以 0002 作为地区分类编码 定义东三省外地区分类");
					}
				}
				lprice = getXsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// 如果有上进地区分类，递归查询
				lprice = getXsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明:箱数运价表 区间过滤
	 * @时间：2011-6-10上午09:39:39
	 * @return
	 * @throws BusinessException
	 */
	public String getPricePeriodWhereSql() throws BusinessException {
		UFDouble ntotalAssNum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(1));
		Integer 	ipriceunit = 1;
		//分仓
		if(!isZC){
			ntotalAssNum = PuPubVO.getUFDouble_NullAsZero(totalNum.get(0));
			ipriceunit =0;
		}
		
		UFDouble gls = PuPubVO.getUFDouble_NullAsZero(this.bingVO
				.getKilometer());
		return " h.nmincase <= " + ntotalAssNum.doubleValue()
				+ " and h.nmaxcase >= " + ntotalAssNum.doubleValue()
				+ "  and b.nmindistance <= " + gls.doubleValue()
				+ " and b.nmaxdistance >= " + gls.doubleValue()
				+" and h.ipriceunit="+ipriceunit;

	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 获取父级地区分类id
	 * @时间：2011-5-19下午07:15:32
	 * @param reareaid
	 * @return
	 * @throws BusinessException
	 */
	protected String getFatherId(String reareaid) throws BusinessException {
		String sql = "select pk_fatherarea,areaclname from  bd_areacl where pk_areacl = '"
				+ reareaid + "'";
		ArrayList<Object> list = (ArrayList<Object>)getBaseDAO().executeQuery(sql,
				WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		if (list != null && list.size()>0) {
			Object[] obj= (Object[])list.get(0);
			String areaclname =PuPubVO.getString_TrimZeroLenAsNull(obj[1]);
			if (areaclname != null) {
				if (areaclname.contains("省")) {
					return null;
				} else {
					return PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-5-18下午08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaName(String id) throws BusinessException {
		String sql = "select areaclname from  bd_areacl where pk_areacl = '"
				+ id + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-5-18下午08:56:27
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaCode(String id) throws BusinessException {
		String sql = "select areaclcode from  bd_areacl where pk_areacl = '"
				+ id + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-5-19下午07:48:15
	 * @param areaclcode
	 * @return
	 * @throws BusinessException
	 */
	protected String getAreaIdByCode(String areaclcode)
			throws BusinessException {
		String sql = "select  pk_areacl from  bd_areacl where areaclcode = '"
				+ areaclcode + "'";
		return PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));

	}

}
