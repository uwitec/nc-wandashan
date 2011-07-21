package nc.vo.zb.price.grade;

import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bill.deal.DealInvPriceBVO;
import nc.vo.zb.bill.deal.DealVendorBillVO;
import nc.vo.zb.parmset.ParamSetVO;
/**
 * 
 * @author zhf  ��Ӧ�̱��۷���ϸ�ӱ�
 *
 */
public class PriceGradeVO extends SuperVO {
	
	private String cbiddingid;//����id
	private String cvendorid;//��Ӧ�̹���id
	private String cvendorbasid;//��Ӧ�̻���id
	private String cinvbasid;//Ʒ�ֻ���id
	private String cinvmanid;//Ʒ�ֹ���id
	private UFDouble ngrade;//���۷�
	private UFDouble nmaxgrade;//��߱��� ���۷� 611 �㷨����������
	private UFDouble nmingrade;//��ͱ��۱��۷�
	private UFDouble nadjgrade;//���۷ֵ���ֵ
	private String cpricegradeid;
	private String coparator;
	private UFDate dmakedate;
	private String cmodify;
	private UFDate dmodifydate;
	
	public UFDouble getNmaxgrade() {
		return nmaxgrade;
	}

	public void setNmaxgrade(UFDouble nmaxgrade) {
		this.nmaxgrade = nmaxgrade;
	}

	public UFDouble getNmingrade() {
		return nmingrade;
	}

	public void setNmingrade(UFDouble nmingrade) {
		this.nmingrade = nmingrade;
	}
	
	public String getCoparator() {
		return coparator;
	}

	public void setCoparator(String coparator) {
		this.coparator = coparator;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getCmodify() {
		return cmodify;
	}

	public void setCmodify(String cmodify) {
		this.cmodify = cmodify;
	}

	public UFDate getDmodifydate() {
		return dmodifydate;
	}

	public void setDmodifydate(UFDate dmodifydate) {
		this.dmodifydate = dmodifydate;
	}

	public String getCvendorbasid() {
		return cvendorbasid;
	}

	public void setCvendorbasid(String cvendorbasid) {
		this.cvendorbasid = cvendorbasid;
	}
	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public UFDouble getNgrade() {
		return ngrade;
	}

	public void setNgrade(UFDouble ngrade) {
		this.ngrade = ngrade;
	}

	public UFDouble getNadjgrade() {
		return nadjgrade;
	}

	public void setNadjgrade(UFDouble nadjgrade) {
		this.nadjgrade = nadjgrade;
	}

	public String getCpricegradeid() {
		return cpricegradeid;
	}

	public void setCpricegradeid(String cpricegradeid) {
		this.cpricegradeid = cpricegradeid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cpricegradeid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "zb_pricegrade";
	}
	
	//���۷ֶ�α���ʱ   �����ֶ�
	public static String[] update_fieldnames =new String[]{"ngrade","nadjgrade","cmodify","dmodifydate","nmaxgrade","nmingrade"};
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����㱨�۷�ǰ������У��
	 * 2011-5-25����07:30:11
	 * @param bills
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnCol(DealVendorBillVO[] bills,ParamSetVO para) throws ValidationException{
		if(bills == null || bills.length ==0)
			throw new ValidationException("����Ϊ��");
		if(PuPubVO.getUFDouble_NullAsZero(para.getNmaxquotatpoints()).equals(UFDouble.ZERO_DBL))
			throw new ValidationException("��Ӧ��������ʷ������쳣");
		
		if(para.getNquotationlower()==null)
			throw new ValidationException("�б���������������ޱ�����δ����");
		UFDouble dd = PuPubVO.getUFDouble_NullAsZero(para.getNquotationlower());
		if(dd.doubleValue()<0||dd.doubleValue()>1){
			throw new ValidationException("�б���������������ޱ�����ֵ���ô���");
		}
		if(para.getNquotationscoring()==null)
			throw new ValidationException("�б��������׼����±��۵÷�ϵ����δ����");
		if(para.getReserve8()==null)
			throw new ValidationException("�б��������׼����ϱ��۵÷�ϵ����δ����");
		
//		UFDouble tmp = null;
		DealInvPriceBVO[] bodys = null;
		for(DealVendorBillVO bill:bills){
//			tmp = bill.getHeader().getNquotatpoints();
//			if(PuPubVO.getUFDouble_NullAsZero(tmp).equals(UFDouble.ZERO_DBL)){
//				throw new ValidationException("���ڱ��۷�Ϊ�յĹ�Ӧ��");
//			}
//			if(tmp.doubleValue()>nmaxgrade.doubleValue()){
//				throw new ValidationException("���۷ֳ���Ӧ�̱��۷����ֵ����");
//			}
			if(PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCbiddingid())==null)
				throw new ValidationException("�����쳣��������ϢΪ��");
			if(PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getCcustmanid())==null)
				throw new ValidationException("�����쳣����Ӧ����ϢΪ��");
//			if(PuPubVO.getUFDouble_NullAsZero(body.getNprice()).equals(UFDouble.ZERO_DBL))
//				throw new ValidationException("�����ϢΪ��");
			bodys = bill.getBodys();
			for(DealInvPriceBVO body:bodys){
				if(PuPubVO.getString_TrimZeroLenAsNull(body.getCinvbasid())==null || PuPubVO.getString_TrimZeroLenAsNull(body.getCinvmanid())==null)
					throw new ValidationException("�����ϢΪ��");
				if(PuPubVO.getUFDouble_NullAsZero(body.getNmarkprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("��׼�Ϊ�ջ�Ϊ0");
				if(PuPubVO.getUFDouble_NullAsZero(body.getNprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("��Ӧ����߱���Ϊ�ջ�Ϊ0");		
				if(PuPubVO.getUFDouble_NullAsZero(body.getNllowerprice()).equals(UFDouble.ZERO_DBL))
					throw new ValidationException("��Ӧ����ͱ���Ϊ�ջ�Ϊ0");		
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����㱨�۷�ǰ������У��
	 * 2011-5-25����07:30:11
	 * @param bills
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnSave(DealVendorBillVO[] bills,UFDouble nmaxgrade) throws ValidationException{
		for(DealVendorBillVO bill:bills){
			validateDataOnOk(bill, nmaxgrade);
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����۷�ά���ڵ�ȷ��ʱ����У��
	 * 2011-5-25����07:31:31
	 * @param bill
	 * @param nmaxgrade
	 * @throws ValidationException
	 */
	public static void validateDataOnOk(DealVendorBillVO bill,UFDouble nmaxgrade) throws ValidationException{
		if(bill == null)
			throw new ValidationException("����Ϊ��");
		UFDouble tmp = bill.getHeader().getNquotatpoints();
		if(PuPubVO.getUFDouble_NullAsZero(tmp).equals(UFDouble.ZERO_DBL)){
			throw new ValidationException("���ڱ��۷�Ϊ�յĹ�Ӧ��");
		}
		if(tmp.doubleValue()>nmaxgrade.doubleValue()){
			throw new ValidationException("���۷ֳ���Ӧ�̱��۷����ֵ����");
		}

		DealInvPriceBVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			throw new ValidationException("Ʒ������Ϊ��");

//		UFDouble nallgrade = UFDouble.ZERO_DBL;
//		for(DealInvPriceBVO body: bodys){
//			nallgrade = nallgrade.add(PuPubVO.getUFDouble_NullAsZero(body.getNgrade()).add(PuPubVO.getUFDouble_NullAsZero(body.getNadjgrade())));
//		}
//		nallgrade = nallgrade.div(bodys.length);
//
//		if(!nallgrade.equals(tmp)){
//			if(nallgrade.doubleValue()>nmaxgrade.doubleValue()){
//				throw new ValidationException("��Ӧ���ܱ��۷ִ���");
//			}
//			bill.getHeader().setNquotatpoints(nallgrade);
//		}
	}

}
