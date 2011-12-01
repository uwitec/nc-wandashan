package nc.bs.wds.dm.order;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.dm.storebing.TbStorcubasdocVO;
import nc.vo.wds.dm.storetranscorp.StortranscorpBVO;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wds.tranprice.freight.ZhbzHVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 发运订单运单运费核算后台类
 * 
 * @author Administrator
 * 
 */
public class TranPriceAccount {

	private AggregatedValueObject curBillvo = null;
	// 当前登录日期
	private UFDate date = null;
	// 运价表类型
	private String colType = WdsWlPubConst.WDSI;
	// 是否总仓
	private boolean isZC = true;
	//是否原料粉
	private boolean fisbigflour = false;
	// 是否零担
	private boolean isSmall = false;
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
		SendorderVO head = (SendorderVO) curBillvo.getParentVO();
		head.setVcolpersonid(operatorid);
		// 确定运费计算类型（吨公里，或者箱数）
		setColType();
		// 查找运价表
		if(isZC){
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
		}else{
			List<TranspriceBVO> lprice = getFCPriceInfor(colType, head
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
		SendorderVO head = (SendorderVO) curBillvo.getParentVO();
		// 公里数
		head.setNgls(bingVO.getKilometer());
		// 运价: 实际价格=原始价格*（1+修正比例）
		UFDouble nrate = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNadjrate());
		head
				.setNtranprice((getTransprice().multiply(nrate.div(100).add(1),
						8)));
		if (isSmall&&WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
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
		SendorderVO head = (SendorderVO) curBillvo.getParentVO();
		getBaseDAO().updateVO(
				head,
				new String[] { "ngls", "ntranprice", "nadjustprice",
						"iadjusttype", "ntransmny", "vcolpersonid",
						"custareaid", "icoltype" });
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
		SendorderVO head = (SendorderVO) curBillvo.getParentVO();
		UFDouble nnum = null;
		UFDouble ngls = null;
		UFDouble nprice = null;
		if(isZC){
			if (WdsWlPubConst.WDSI.equalsIgnoreCase(colType)) {
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
		}else{
			Integer icoltype = PuPubVO.getInteger_NullAs(curTranpriceBvo.getIcoltype(), 0);
			UFDouble nmny = null;
			UFDouble nadjmny = null;
			if(icoltype==0){//总费用
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice());
			}else if(icoltype==1){//每箱运价
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice()).multiply(totalNum.get(1), 8);
			}else if(icoltype ==2){//每吨运价
				nmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice()).multiply(totalNum.get(0), 8);
			}
			UFDouble nsmall = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallnum());
			if(nsmall.sub(totalNum.get(0)).doubleValue()>0){  //判断发货数量是否大于最小发货数量
				Integer iadjtype = PuPubVO.getInteger_NullAs(curTranpriceBvo.getIadjtype(), 0);
				if(iadjtype==0){//总费用
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj());//	npriceadj   追加零带
				}else if(iadjtype==1){//每箱补贴价格
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(totalNum.get(1), 8);
				}else if(iadjtype ==2){//每吨补贴价格
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(totalNum.get(0), 8);
				}else if(iadjtype ==3){//按照最小吨数来计算总费用
					nadjmny = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNpriceadj()).multiply(nsmall, 8);
					nmny =  PuPubVO.getUFDouble_NullAsZero(null);
				}
			}
			head.setNtransmny(nmny.add(nadjmny, 8));
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
		SendorderVO head = (SendorderVO) curBillvo.getParentVO();
		SendorderBVO[] bodys = (SendorderBVO[]) curBillvo.getChildrenVO();
		String pk_transcorp = head.getPk_transcorp();
		if (pk_transcorp == null || "".equalsIgnoreCase(pk_transcorp)) {
			throw new BusinessException("请先选择承运商");
		}
		String pk_outwhouse = head.getPk_outwhouse();
		if (pk_outwhouse == null || "".equalsIgnoreCase(pk_outwhouse)) {
			throw new BusinessException("未取到发货站");
		}
		String pk_custman = head.getPk_inwhouse();
		if (pk_custman == null || "".equalsIgnoreCase(pk_custman)) {
			throw new BusinessException("未取到收货仓库信息");
		}
		// 获得分仓与客商(分仓)的绑定VO
		getBingVo(pk_outwhouse, pk_custman);
		this.reareaid = bingVO.getCustareaid();
		if (reareaid == null || "".equalsIgnoreCase(reareaid)) {
			throw new BusinessException("维护分仓所属地区");
		}
		head.setCustareaid(reareaid);
		//2 根据承运商查询 实际折合标准，并计算实际折合吨数
		// 将运单表体存货分成两类：参与实际折合，不参与实际折合
		for (SendorderBVO body : bodys) {
			body.setFiszh(UFBoolean.FALSE);
		}
		UFDouble nexNum = PuPubVO.getUFDouble_NullAsZero(null);//符合实际折合标准的存货根据实际折合率计算的吨数
		AggregatedValueObject[] zhbzBills = getZhbz(pk_transcorp);//每个承运商可能有多个折合标准，但是每个存货只能属于一个折合标准
		if(zhbzBills !=null && zhbzBills.length>0){
			for(int i=0;i<zhbzBills.length;i++){
				ZhbzHVO hvo = (ZhbzHVO)zhbzBills[i].getParentVO();
				ZhbzBVO[] bvos =(ZhbzBVO[])  zhbzBills[i].getChildrenVO();
				UFDouble standardtune = PuPubVO.getUFDouble_NullAsZero(hvo.getStandardtune());//折合数量(箱数)
				UFDouble tuneunits = PuPubVO.getUFDouble_NullAsZero(hvo.getTuneunits()).div(1000);//实际换算率= （公斤/箱）除以 1000
				ArrayList<SendorderBVO> list = new ArrayList<SendorderBVO>();
				UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(null);//运单中属于该折合标准的存货总量（箱数）
				UFDouble nminhsl = new UFDouble(1);;//运单中属于该折合标准的存货最小换算率
				if(bvos !=null && bvos.length>0 ){
					for(ZhbzBVO bvo:bvos){
						String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_invmandoc());
						if(pk_invmandoc == null){
							continue;
						}
						for (SendorderBVO body : bodys) {
							UFBoolean fiszh = PuPubVO.getUFBoolean_NullAs(body.getFiszh(), UFBoolean.FALSE);
							if(fiszh.booleanValue()){
								continue;
							}
							String pk_invmandoc2 = body.getPk_invmandoc();
							if(pk_invmandoc.equalsIgnoreCase(pk_invmandoc2)){
								UFDouble nhgrate = PuPubVO.getUFDouble_NullAsZero(body.getNhsl());
								if(nhgrate.sub(nminhsl).doubleValue()<0){
									nminhsl = nhgrate;
								}
								nnum = nnum.add(PuPubVO.getUFDouble_NullAsZero(body.getNassoutnum()));
								list.add(body);
							}
						}
					}
				}
				//超出的箱数，按照实际换算率来计算吨数
				//未超出的部分，按照这些存货中的最小折合率来计算
				UFDouble nexAssNum = nnum.sub(standardtune);
				if(nexAssNum.doubleValue()>0){
					UFDouble nds = standardtune.multiply(nminhsl);
					UFDouble nexds = nexAssNum.multiply(tuneunits);
					nexNum = nexNum.add(nds);
					nexNum = nexNum.add(nexds);
					for(int j=0;j<list.size();j++){
						SendorderBVO body = list.get(j);
						body.setFiszh(UFBoolean.TRUE);
					}
				}
				
			}
		}
		// 汇总出库主(辅)数量
		UFDouble ntotalNum = new UFDouble(0);
		UFDouble ntotalAssNUm = new UFDouble(0);
		for (SendorderBVO body : bodys) {
			UFBoolean fiszh = PuPubVO.getUFBoolean_NullAs(body.getFiszh(), UFBoolean.FALSE);
			if( !fiszh.booleanValue()){
				ntotalNum = ntotalNum.add(PuPubVO.getUFDouble_NullAsZero(body
						.getNoutnum()), 8);
			}
			ntotalAssNUm = ntotalAssNUm.add(PuPubVO.getUFDouble_NullAsZero(body
					.getNassoutnum()), 8);			
		}
		ntotalNum = ntotalNum.add(nexNum);
		totalNum.add(ntotalNum);
		totalNum.add(ntotalAssNUm);
		fisbigflour = PuPubVO.getUFBoolean_NullAs(head.getFisbigflour(), UFBoolean.FALSE).booleanValue();
		// 判断是否总仓
		if (WdsWlPubConst.WDS_WL_ZC.equalsIgnoreCase(pk_outwhouse)) {
			isZC = true;
			// 仓库与承运商绑定的VO
			List<StortranscorpBVO> stroCorp = getStroCorpVO(pk_outwhouse,
					pk_transcorp, reareaid,true);
			// 如果没有声明有零担标准，默认按照吨公里来计算
			if (stroCorp == null || stroCorp.size() == 0) {
				colType = WdsWlPubConst.WDSI;
			} else {
				Integer ismalltype = stroCorp.get(0).getIsmalltype();
				Integer ismallprice = stroCorp.get(0).getIsmallprice();
				UFDouble nsmallnum = PuPubVO.getUFDouble_NullAsZero(stroCorp.get(0).getNsmallnum());
				if (0 == ismalltype) {// 以主数量作为区分标准
					if (nsmallnum.sub(totalNum.get(0)).doubleValue() >= 0) {
						isSmall = true;
						if (0 == ismallprice) {
							colType = WdsWlPubConst.WDSI;
						} else if (1 == ismallprice) {
							colType = WdsWlPubConst.WDSJ;
						}
					} else {
						isSmall = false;
						colType = WdsWlPubConst.WDSI;
					}
				} else {// 以辅数量作为区分标准
					if (nsmallnum.sub(totalNum.get(1)).doubleValue() >= 0) {
						isSmall = true;
						if (0 == ismallprice) {
							colType = WdsWlPubConst.WDSI;
						} else if (1 == ismallprice) {
							colType = WdsWlPubConst.WDSJ;
						}
					} else {
						isSmall = false;
						colType = WdsWlPubConst.WDSI;
					}
				}
			}
		} else {
			isZC = false;
			colType = WdsWlPubConst.WDSK;
		}

	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目:根据承运商查询实际折合标准
	 * @时间：2011-11-24下午07:24:55
	 * @param carriersid 承运商主键
	 * @return
	 * @throws UifException 
	 */
	public AggregatedValueObject[] getZhbz(String carriersid) throws UifException{
		AggregatedValueObject[] bills = null;
		if(carriersid == null || "".equalsIgnoreCase(carriersid)){
			return bills;
		}
		String[] obj = new String[]{HYBillVO.class.getName(),ZhbzHVO.class.getName(),ZhbzBVO.class.getName()}; 
		String strWhere = " carriersid='"+carriersid+"'";
		bills = new HYPubBO().queryBillVOByCondition(obj, strWhere);
		return bills;
	}
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目
	 * @时间：2011-6-9下午10:52:12
	 * @param pk_outwhouse:仓库id
	 *            pk_transcorp 承运商id 发货地区 id
	 *            isDG 是否递归
	 * @return 仓库承运商绑定VO
	 * @throws BusinessException
	 */
	public ArrayList<StortranscorpBVO>  getStroCorpVO(String pk_outwhouse,
			String pk_transcorp, String caredid,boolean isDG) throws BusinessException {
		ArrayList<StortranscorpBVO> obj = null;
		String fatherId = null;
		String sql = "select  * from wds_stortranscorp_b where pk_stordoc='"
				+ pk_outwhouse + "' and pk_wds_tanscorp_h='" + pk_transcorp
				+ "' and isnull(dr,0)=0 and careaid='" + caredid + "'";
		obj = (ArrayList<StortranscorpBVO>) getBaseDAO().executeQuery(sql,
				new BeanListProcessor(StortranscorpBVO.class));
		if ((obj == null ||obj.size() == 0) && isDG) {
			fatherId = getFatherId(caredid);
			if (fatherId == null || "".equalsIgnoreCase(fatherId)) {
				obj = getStroCorpVO(pk_outwhouse, pk_transcorp, fatherId,false);
			} else {
				obj = getStroCorpVO(pk_outwhouse, pk_transcorp, fatherId,true);
			}
		}
		return obj;
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
	public void getBingVo(String pk_outwhouse, String pk_inwhouse)
			throws BusinessException {
		String sql = "select * from tb_storcubasdoc  where pk_stordoc='"
				+ pk_outwhouse + "' and pk_stordoc1='" + pk_inwhouse
				+ "' and isnull(dr,0)=0";
		ArrayList<TbStorcubasdocVO> list = (ArrayList<TbStorcubasdocVO>) getBaseDAO()
				.executeQuery(sql,
						new BeanListProcessor(TbStorcubasdocVO.class));
		if (list == null || list.size() == 0) {
			throw new BusinessException("请维护分仓与分仓的绑定关系");
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
		sqlb.append(" and (isnull(b.ifw,0) = 0 or isnull(b.ifw,0) =2) ");// 应运范围过滤
		if(fisbigflour){
			sqlb.append(" and isnull(fiseffect,'N')='Y'");
		}else{
			sqlb.append(" and isnull(fiseffect,'N')='N'");
		}
		sqlb.append(" and h.dstartdate <= '" + date + "' and h.denddate >= '"
				+ date + "'");
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// 如果没有获取对应收货地区的定义，查询有没有上级的定义
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// 如果有上进地区分类，递归查询
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
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
		sqlb.append(" and (isnull(b.ifw,0)=0 or isnull(b.ifw,0) =2) ");// 应运范围过滤
		if (PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql()) != null)// 箱数区间过滤
			sqlb.append(" and " + getPricePeriodWhereSql());
		sqlb.append(" and b.pk_replace='" + reareaid + "'");
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// 如果没有获取对应收货地区的定义，查询有没有上级的定义
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// 如果有上进地区分类，递归查询
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, true);
			}
		}
		return lprice;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：查询分仓运价表 
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
	public List<TranspriceBVO> getFCPriceInfor(String pricetype,
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
		sqlb.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "
						+ IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '" + pricetype + "'");// 运价表单据类型
		// :吨公里运价表和箱数运价表适用同一个库表，但是单据类型不同
		sqlb.append(" and h.carriersid='" + pk_transcorp + "'");// 承运商
		sqlb.append(" and h.reserve1='" + pk_outwhouse + "'");// 发货仓库
		sqlb.append(" and (isnull(b.ifw,0)=0 or isnull(b.ifw,0) =2) ");// 应运范围过滤
		sqlb.append(" and h.nmincase <= " + totalNum.get(0).doubleValue());    //最小数量
		sqlb.append(" and h.nmaxcase > " + totalNum.get(0).doubleValue());      //最大数量
		sqlb.append(" and b.pk_replace='" + reareaid + "'");      //  收货地
		lprice = (List<TranspriceBVO>) getBaseDAO().executeQuery(
				sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		// 如果没有获取对应收货地区的定义，查询有没有上级的定义
		if ((lprice == null || lprice.size() == 0) && isDG) {
			String pk_fatherarea = getFatherId(reareaid);
			if (pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)) {
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
						pk_fatherarea, false);
			} else {
				// 如果有上进地区分类，递归查询
				lprice = getDsPriceInfor(pricetype, pk_transcorp, pk_outwhouse,
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
		UFDouble gls = PuPubVO.getUFDouble_NullAsZero(this.bingVO
				.getKilometer());
		return " h.nmincase <= " + ntotalAssNum.doubleValue()
				+ " and h.nmaxcase >= " + ntotalAssNum.doubleValue()
				+ "  and b.nmindistance <= " + gls.doubleValue()
				+ " and b.nmaxdistance >= " + gls.doubleValue();

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
		String sql = "select pk_fatherarea from  bd_areacl where pk_areacl = '"
				+ reareaid + "'";
		ArrayList<Object> list = (ArrayList<Object>) getBaseDAO().executeQuery(
				sql, WdsPubResulSetProcesser.ARRAYLISTPROCESSOR);
		if (list != null && list.size() > 0) {
			Object[] obj = (Object[]) list.get(0);
			//String areaclname = PuPubVO.getString_TrimZeroLenAsNull(obj[1]);
			//			if (areaclname != null) {
			//				if (areaclname.contains("省")) {
			//					return null;
			//				} else {
			return PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
			//				}
			//			}
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
