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
			throw new BusinessException("�������Ϊ��");
		DealInvPriceBVO[] bodys = billvo.getBodys();
		if(bodys == null || bodys.length == 0){
			throw new BusinessException("�������Ϊ��");
		}
		UFDouble nallgrade = UFDouble.ZERO_DBL;
		UFDouble tmpgrade = null;
		for(DealInvPriceBVO body:bodys){
//			��߼ۼ���÷�
			getColBO(body,true).doCol();
//			��ͼۼ���÷�
			getColBO(body,false).doCol();
			tmpgrade = (PuPubVO.getUFDouble_NullAsZero(body.getNmaxgrade()).add(PuPubVO.getUFDouble_NullAsZero(body.getNmingrade()))).div(2);
			tmpgrade = tmpgrade.add(UFDouble.ZERO_DBL, ZbPubConst.grade_digit);
			body.setNgrade(tmpgrade);
			nallgrade = nallgrade.add(body.getNgrade(),ZbPubConst.grade_digit);
		}
		
//		�����ܷ�   ����Ʒ�ֱ��۷ֵ�ƽ����
		getBillVO().getHeader().setNquotatpoints(nallgrade.div(bodys.length));
	}

	private AbstractPriceGradeCol getColBO(DealInvPriceBVO body,boolean ismax) throws BusinessException{
//		������ ÿ�� body  ��Ҫ�������ε÷�   
		AbstractPriceGradeCol cobo = null;
		UFDouble a = null;
		if(ismax){
			a = PuPubVO.getUFDouble_NullAsZero(body.getNprice());//��߱���
		}else{
			a = PuPubVO.getUFDouble_NullAsZero(body.getNllowerprice());//��ͱ���
		}
		UFDouble b = PuPubVO.getUFDouble_NullAsZero(body.getNmarkprice());//��׼�
		UFDouble d = SubmitPriceVO.getMinPrice(b, para.getNquotationlower());//PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower());//����������
		if(a.doubleValue() == b.doubleValue()){
			cobo = getEqucol(body,ismax);
		}else if(a.doubleValue()>b.doubleValue()){
			cobo = getMaxcol(body,ismax);
		}else if(a.doubleValue()<b.doubleValue()&&a.doubleValue()>d.doubleValue()){
			cobo = getMincol(body,ismax);
		}else if(a.doubleValue()<d.doubleValue()){//���ⱨ��
			if(PuPubVO.getUFBoolean_NullAs(para.getFisbadquotation(),UFBoolean.FALSE).booleanValue())
				throw new BusinessException("���ڶ��ⱨ��,Ʒ��Ϊ"+body.getCinvbasid());
			cobo = getBadcol(body,ismax);
		}else{
			throw new BusinessException("��Ӧ�̱��������쳣");
		}
		return cobo;
	}

	

}
