package nc.bs.wds.tranprice.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator 运费核算 核心算法类
 */
public class TranPriceColFactory {

	private DsTranPriceCol m_dscol = null;
	private XsTranPriceCol m_xscol = null;
	// 来源单据表头id 运价表子表vo
	private Map<String, TranspriceBVO> tranprice = new HashMap<String, TranspriceBVO>();
	// 按照发货仓+收货地（收货仓或者客商）
	private Map<String, ArrayList<UFDouble>> groupNum = new HashMap<String, ArrayList<UFDouble>>();
	// 当前操作行
	private SendBillBodyVO curBody = null;
	// 当前使用的 运价表子表
	private TranspriceBVO curTranpriceBvo = null;

	private BaseDAO dao = null;

	protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public TranPriceColFactory(SendBillVO billvo) {
		super();
		this.billvo = billvo;
	}

	private SendBillVO billvo = null;

	public void setBillVO(SendBillVO bill) {
		billvo = bill;
	}

	public SendBillVO getBillVO() {
		return billvo;
	}

	private UFBoolean issale = UFBoolean.FALSE;// true:销售出库 false:转分仓出库

	public boolean isSale() {
		return issale.booleanValue();
	}

	public void setIsSale(UFBoolean bsale) {
		issale = bsale;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明： 运费核算
	 * @时间：2011-6-8下午08:11:52
	 * @return
	 * @throws BusinessException
	 */
	public SendBillVO col() throws BusinessException {
		if (billvo == null)
			throw new BusinessException("传入参数为空");
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		if (bodys == null || bodys.length == 0) {
			throw new BusinessException("传入表体参数为空");
		}
		// 分组统计
		groupByReserver();
		int icoltype = SendBillVO.HAND_COLTYPE;
		for (SendBillBodyVO body : bodys) {
			// 获得运价表
			icoltype = PuPubVO.getInteger_NullAs(body.getIcoltype(),
					SendBillVO.HAND_COLTYPE);
			if (icoltype == SendBillVO.HAND_COLTYPE)
				continue;
			if (tranprice.containsKey(body.getCsourcebillhid())) {
				curTranpriceBvo = tranprice.get(body.getCsourcebillhid());
			} else {
				curTranpriceBvo = getColBO(icoltype, body).getTranspriceBvo();
				tranprice.put(body.getCsourcebillhid(), curTranpriceBvo);
			}
			curBody = body;
			// 扫描运价表到运费核算单
			appendPriceInfor();
			// 设置运费
			setNcolmny();
			// 保存
			doSavePriceInfor();
		}
		return billvo;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：按照发货仓+收货地（分仓或者经销商）,汇总
	 * @时间：2011-6-8下午07:40:47
	 */
	void groupByReserver() {
		SendBillBodyVO[] bodys = billvo.getBodyVos();
		for (SendBillBodyVO body : bodys) {
			String key = "";
			if (WdsWlPubConst.WDS3.equalsIgnoreCase(body.getCsourcetype())) {
				key = body.getPk_destore() + body.getPk_restore();
			} else {
				key = body.getPk_destore() + body.getPk_trader();
			}
			if (groupNum.containsKey(key)) {
				ArrayList<UFDouble> sumNum = groupNum.get(key);
				UFDouble oldNum = PuPubVO.getUFDouble_NullAsZero(sumNum.get(0));
				UFDouble oldAssNum = PuPubVO.getUFDouble_NullAsZero(sumNum
						.get(1));
				UFDouble newNum = PuPubVO
						.getUFDouble_NullAsZero(body.getNnum());
				UFDouble newAssNum = PuPubVO.getUFDouble_NullAsZero(body
						.getNassnum());
				oldNum = oldNum.add(newNum);
				oldAssNum = oldAssNum.add(newAssNum);
				sumNum.add(0, oldNum);
				sumNum.add(1, oldAssNum);
				groupNum.put(key, sumNum);
			} else {
				ArrayList<UFDouble> sumNum = new ArrayList<UFDouble>();
				sumNum.add(PuPubVO.getUFDouble_NullAsZero(body.getNnum()));
				sumNum.add(PuPubVO.getUFDouble_NullAsZero(body.getNassnum()));
				groupNum.put(key, sumNum);
			}
		}
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 根据运费计算类型 获取不同的 运费计算实现类
	 * @时间：2011-5-19下午05:07:40
	 * @param icoltype
	 * @param body
	 * @return
	 */
	private AbstractTranPriceCol getColBO(int icoltype, SendBillBodyVO body) {
		AbstractTranPriceCol cobo = null;
		if (icoltype == SendBillVO.DS_COLTYPE) {
			cobo = getDscol();
		} else if (icoltype == SendBillVO.XS_COLTYPE) {
			cobo = getXscol();
		}
		cobo.setBillvo(billvo);
		cobo.setSendBody(body);
		cobo.setIsSale(getBillVO().isSale());
		cobo.setM_logDate(getBillVO().getM_logDate());
		return cobo;
	}

	public DsTranPriceCol getDscol() {
		if (m_dscol == null)
			m_dscol = new DsTranPriceCol();
		return m_dscol;
	}

	public XsTranPriceCol getXscol() {
		if (m_xscol == null)
			m_xscol = new XsTranPriceCol();
		return m_xscol;
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 补充运价信息 用于运价扫描后将运价信息补齐到运费单上
	 * @时间：2011-6-8下午06:58:47
	 * @throws BusinessException
	 */
	void appendPriceInfor() throws BusinessException {
		if (curBody == null || curTranpriceBvo == null)
			return;
		// 运价表主键
		curBody.setCpricehid(curTranpriceBvo.getPk_wds_transprice_h());
		curBody.setCpriceid(curTranpriceBvo.getPrimaryKey());
		// 判断是否需要 运价调整
		if (isAdjust()) {
			curBody.setNadjustprice(curTranpriceBvo.getNpriceadj());// 运价调整值
			curBody.setIadjusttype(curTranpriceBvo.getIadjtype());// 运价调整类型
		}
		// 单价按修订比例修订
		// 实际价格=原始价格*（1+修正比例）
		// getTransprice()--判断是取正常运价，还是取零散运价
		UFDouble nrate = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo
				.getNadjrate());
		curBody.setNprice(getTransprice().multiply(nrate.div(100).add(1), 8));
		colAdjust();
	}

	/**
	 * 
	 * @作者：lyf
	 * @说明：判断是否需要 运价调整
	 * @时间：2011-6-8下午07:19:48
	 * @return
	 */
	public boolean isAdjust() {
		 //比较 汇总数量是否大于零散发运数量
		String key = "";
		if(WdsWlPubConst.WDS3.equalsIgnoreCase(curBody.getCsourcetype())){
			key =curBody.getPk_destore()+curBody.getPk_restore();
		}else{
			key = curBody.getPk_destore()+curBody.getPk_trader();
		}
		if(groupNum.containsKey(key)){
			ArrayList<UFDouble> sumNum = groupNum.get(key);
			UFDouble nassum = PuPubVO.getUFDouble_NullAsZero(sumNum.get(1));
			UFDouble nadjustnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNadjustnum());
			UFDouble num1 = nassum.sub(nadjustnum);
			if(num1.doubleValue()<=0){
				return true;
			}
		}
		return false;
	}

	public UFDouble getTransprice(){
		 UFDouble price = null;
		 price = PuPubVO
			.getUFDouble_NullAsZero(curTranpriceBvo.getNtransprice());
		 //比较 汇总数量是否大于零散发运数量
		String key = "";
		if(WdsWlPubConst.WDS3.equalsIgnoreCase(curBody.getCsourcetype())){
			key =curBody.getPk_destore()+curBody.getPk_restore();
		}else{
			key = curBody.getPk_destore()+curBody.getPk_trader();
		}
		if(groupNum.containsKey(key)){
			ArrayList<UFDouble> sumNum = groupNum.get(key);
			UFDouble num = PuPubVO.getUFDouble_NullAsZero(sumNum.get(0));
			UFDouble  nsmallnum = PuPubVO.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallnum());
			UFDouble num1 = num.sub(nsmallnum);
			if(num1.doubleValue()<=0){
				price = PuPubVO
				.getUFDouble_NullAsZero(curTranpriceBvo.getNsmallprice());
			}
		}
		
		return price;
	 }

	/***************************************************************************
	 * 
	 * @作者：zhf
	 * @说明：运价表定义了调整值的,需要定义调整值
	 * @时间：2011-6-8下午07:20:25
	 * @throws BusinessException
	 */
	public void colAdjust() throws BusinessException {
		UFDouble nadjustprice = PuPubVO.getUFDouble_NullAsZero(curBody
				.getNadjustprice());
		UFDouble nnum = null;
		if (PuPubVO.getInteger_NullAs(curBody.getIadjusttype(),
				SendBillVO.DS_PRICEUNIT) == SendBillVO.DS_PRICEUNIT) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNnum());
		} else {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNassnum());
		}
		curBody.setNadjustmny(nnum.multiply(nadjustprice, 8));

	}

	/**
	 * 
	 * @throws BusinessException
	 * @作者：zhf
	 * @说明: 设置运费
	 * @时间：2011-5-20下午01:22:14
	 */
	void setNcolmny() throws BusinessException {
		int icoltype = SendBillVO.HAND_COLTYPE;
		icoltype = curBody.getIcoltype();
		UFDouble nprice = curBody.getNprice();
		UFDouble nnum = null;
		UFDouble ngls = null;
		if (icoltype == SendBillVO.DS_COLTYPE) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNnum());
			String key = curBody.getCsendareaid() + curBody.getCreceiverealid();
			// if(kilometers.containsKey(key)){
			// ngls = kilometers.get(key);
			// }else{
			// ngls =getGls();
			// kilometers.put(key, ngls);
			// }
			ngls = PuPubVO.getUFDouble_NullAsZero(curBody.getNgl());
			curBody.setNcolmny(nprice.multiply(nnum, 8).multiply(ngls, 8));
			curBody.setNmny(nprice.multiply(nnum, 8).multiply(ngls, 8).add(
					curBody.getNadjustmny()));
		} else if (icoltype == SendBillVO.XS_COLTYPE) {
			nnum = PuPubVO.getUFDouble_NullAsZero(curBody.getNassnum());
			curBody.setNcolmny(nprice.multiply(nnum, 8));
			curBody.setNmny(nprice.multiply(nnum, 8).add(
					curBody.getNadjustmny()));
		}
	}

	/**
	 * 保存运费信息
	 * 
	 * @throws BusinessException
	 */
	protected void doSavePriceInfor() throws BusinessException {
		// 校验 单价 计算金额 运价表id 总金额;
		curBody.validateOnColSave();
		getDao().updateVO(curBody, SendBillVO.priceInfor_fieldNames);
		String sql = "select ts from wds_transprice_b where pk_wds_transprice_b = '"
				+ curBody.getPrimaryKey() + "'";
		String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(
				sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
		curBody.setTs(new UFDateTime(ts));
	}

	// /**
	// *
	// * @作者：lyf
	// * @说明：完达山物流项目
	// * 从里程表上 获取两个地区的公里数
	// * @时间：2011-5-18下午07:58:28
	// * @return
	// * @throws BusinessException
	// */
	// public UFDouble getGls() throws BusinessException{
	// String sql = " select mileage from wds_transmil where isnull(dr,0)=0 and
	// pk_delocation='"+curBody.getCsendareaid()+"' and
	// pk_relocation='"+curBody.getCreceiverealid()+"'" ;
	// return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql,
	// WdsPubResulSetProcesser.COLUMNPROCESSOR));
	// }

}
