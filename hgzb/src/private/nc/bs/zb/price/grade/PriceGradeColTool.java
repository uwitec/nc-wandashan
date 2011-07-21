package nc.bs.zb.price.grade;

import nc.bs.zb.price.grade.col.AbstractPriceGradeCol;
import nc.bs.zb.price.grade.col.BadPriceCol;
import nc.bs.zb.price.grade.col.PriceEquCol;
import nc.bs.zb.price.grade.col.PriceMaxCol;
import nc.bs.zb.price.grade.col.PriceMinCol;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.parmset.ParamSetVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

public class PriceGradeColTool {

	private DealVendorBillVO billvo = null;
	private ParamSetVO para = null;
	
	private PriceEquCol m_equcol = null;
	private PriceMaxCol m_maxcol = null;
	private PriceMinCol m_mincol= null;
	private BadPriceCol m_badcol = null;

	public PriceGradeColTool(ParamSetVO para){
		super();
		this.para = para;
	}
	
	public void clear(){
		billvo = null;
		para = null;
	}
	

	public PriceEquCol getEqucol(DealInvPriceBVO body,boolean ismax) {
		if(m_equcol == null)
			m_equcol = new PriceEquCol(para);
		m_equcol.setBody(body);
		m_equcol.setIsMax(ismax);
		return m_equcol;
	}
	public PriceMaxCol getMaxcol(DealInvPriceBVO body,boolean ismax) {
		if(m_maxcol == null)
			m_maxcol = new PriceMaxCol(para);
		m_maxcol.setBody(body);
		m_maxcol.setIsMax(ismax);
		return m_maxcol;
	}
	public PriceMinCol getMincol(DealInvPriceBVO body,boolean ismax) {
		if(m_mincol == null)
			m_mincol = new PriceMinCol(para);
		m_mincol.setBody(body);
		m_mincol.setIsMax(ismax);
		return m_mincol;
	}
	
	public BadPriceCol getBadcol(DealInvPriceBVO body,boolean ismax) {
		if(m_badcol == null)
			m_badcol = new BadPriceCol(para);
		m_badcol.setBody(body);
		m_badcol.setIsMax(ismax);
		return m_badcol;
	}

	public void setBillVO(DealVendorBillVO bill){
		billvo = bill;
	}
	public DealVendorBillVO getBillVO(){
		return billvo;
	}

	public void col() throws BusinessException {
		if(billvo==null)
			throw new BusinessException("传入参数为空");
		DealInvPriceBVO[] bodys = billvo.getBodys();
		if(bodys == null || bodys.length == 0){
			throw new BusinessException("传入参数为空");
		}
		UFDouble nallgrade = UFDouble.ZERO_DBL;
		UFDouble tmpgrade = null;
		for(DealInvPriceBVO body:bodys){
//			最高价计算得分
			getColBO(body,true).doCol();
//			最低价计算得分
			getColBO(body,false).doCol();
			tmpgrade = (PuPubVO.getUFDouble_NullAsZero(body.getNmaxgrade()).add(PuPubVO.getUFDouble_NullAsZero(body.getNmingrade()))).div(2);
			tmpgrade = tmpgrade.add(UFDouble.ZERO_DBL, ZbPubConst.grade_digit);
			body.setNgrade(tmpgrade);
			nallgrade = nallgrade.add(body.getNgrade(),ZbPubConst.grade_digit);
		}
		
//		计算总分   各个品种报价分的平均分
		getBillVO().getHeader().setNquotatpoints(nallgrade.div(bodys.length));
	}

	private AbstractPriceGradeCol getColBO(DealInvPriceBVO body,boolean ismax) throws BusinessException{
//		调整后 每个 body  需要计算两次得分   
		AbstractPriceGradeCol cobo = null;
		UFDouble a = null;
		if(ismax){
			a = PuPubVO.getUFDouble_NullAsZero(body.getNprice());//最高报价
		}else{
			a = PuPubVO.getUFDouble_NullAsZero(body.getNllowerprice());//最低报价
		}
		UFDouble b = PuPubVO.getUFDouble_NullAsZero(body.getNmarkprice());//标底价
		UFDouble d = SubmitPriceVO.getMinPrice(b, para.getNquotationlower());//PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower());//合理报价下限
		if(a.doubleValue() == b.doubleValue()){
			cobo = getEqucol(body,ismax);
		}else if(a.doubleValue()>b.doubleValue()){
			cobo = getMaxcol(body,ismax);
		}else if(a.doubleValue()<b.doubleValue()&&a.doubleValue()>d.doubleValue()){
			cobo = getMincol(body,ismax);
		}else if(a.doubleValue()<d.doubleValue()){//恶意报价
			if(PuPubVO.getUFBoolean_NullAs(para.getFisbadquotation(),UFBoolean.FALSE).booleanValue())
				throw new BusinessException("存在恶意报价,品种为"+body.getCinvbasid());
			cobo = getBadcol(body,ismax);
		}else{
			throw new BusinessException("供应商报价数据异常");
		}
		return cobo;
	}

	

}
