/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.pu.plan.year;
	
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.pu.pub.PlanBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pu.VariableConst;
	
/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * <p>
 *     �ڴ˴����Ӵ����������Ϣ
 * </p>
 * ��������:2011-11-01 10:37:18
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class PlanYearBVO extends PlanBVO {
	
	//�·ݷ���   �ܺͱ��ֺ�����������������ͬ��
	private String pk_planyear_b;	
	private UFDouble nmonnum1;
	private UFDouble nmonnum2;
	private UFDouble nmonnum3;
	private UFDouble nmonnum4;
	private UFDouble nmonnum5;
	private UFDouble nmonnum6;
	private UFDouble nmonnum7;
	private UFDouble nmonnum8;
	private UFDouble nmonnum9;
	private UFDouble nmonnum10;
	private UFDouble nmonnum11;	
	private UFDouble nmonnum12;
	
//	�ջ��������ջ��ص㡢�ջ���ַ���ջ���֯���ջ��ֿ�
	private String cdestcalbodyid;//
	private String cdestwarehouseid;	
	private String cdestaddressid;
	private String cdestareaid;
	private String cdestsiteid;
 
//	private UFDouble naftenum1;
//	private UFDouble naftenum2; 
//	private UFDouble naftenum3; 
//	private UFDouble naftenum4; 
//	private UFDouble naftenum5; 
//	private UFDouble naftenum6; 
//	private UFDouble naftenum7; 
//	private UFDouble naftenum8;
//	private UFDouble naftenum9; 
//	private UFDouble naftenum10; 
//	private UFDouble nafternum11; 
//	private UFDouble nafternum12; 
	private UFBoolean fisload; //�Ƿ����ù�

	public UFBoolean getFisload() {
		return fisload;
	}
	public void setFisload(UFBoolean fisload) {
		this.fisload = fisload;
	}
//	public UFDouble getNaftenum1() {
//		return naftenum1;
//	}
//	public void setNaftenum1(UFDouble naftenum1) {
//		this.naftenum1 = naftenum1;
//	}
//	public UFDouble getNaftenum2() {
//		return naftenum2;
//	}
//	public void setNaftenum2(UFDouble naftenum2) {
//		this.naftenum2 = naftenum2;
//	}
//	public UFDouble getNaftenum3() {
//		return naftenum3;
//	}
//	public void setNaftenum3(UFDouble naftenum3) {
//		this.naftenum3 = naftenum3;
//	}
//	public UFDouble getNaftenum4() {
//		return naftenum4;
//	}
//	public void setNaftenum4(UFDouble naftenum4) {
//		this.naftenum4 = naftenum4;
//	}
//	public UFDouble getNaftenum5() {
//		return naftenum5;
//	}
//	public void setNaftenum5(UFDouble naftenum5) {
//		this.naftenum5 = naftenum5;
//	}
//	public UFDouble getNaftenum6() {
//		return naftenum6;
//	}
//	public void setNaftenum6(UFDouble naftenum6) {
//		this.naftenum6 = naftenum6;
//	}
//	public UFDouble getNaftenum7() {
//		return naftenum7;
//	}
//	public void setNaftenum7(UFDouble naftenum7) {
//		this.naftenum7 = naftenum7;
//	}
//	public UFDouble getNaftenum8() {
//		return naftenum8;
//	}
//	public void setNaftenum8(UFDouble naftenum8) {
//		this.naftenum8 = naftenum8;
//	}
//	public UFDouble getNaftenum9() {
//		return naftenum9;
//	}
//	public void setNaftenum9(UFDouble naftenum9) {
//		this.naftenum9 = naftenum9;
//	}
//	public UFDouble getNaftenum10() {
//		return naftenum10;
//	}
//	public void setNaftenum10(UFDouble naftenum10) {
//		this.naftenum10 = naftenum10;
//	}
//	public UFDouble getNafternum11() {
//		return nafternum11;
//	}
//	public void setNafternum11(UFDouble nafternum11) {
//		this.nafternum11 = nafternum11;
//	}
//	public UFDouble getNafternum12() {
//		return nafternum12;
//	}
//	public void setNafternum12(UFDouble nafternum12) {
//		this.nafternum12 = nafternum12;
//	}

	public String getCdestcalbodyid() {
		return cdestcalbodyid;
	}
	public void setCdestcalbodyid(String cdestcalbodyid) {
		this.cdestcalbodyid = cdestcalbodyid;
	}
	public String getCdestwarehouseid() {
		return cdestwarehouseid;
	}
	public void setCdestwarehouseid(String cdestwarehouseid) {
		this.cdestwarehouseid = cdestwarehouseid;
	}
	public String getCdestaddressid() {
		return cdestaddressid;
	}
	public void setCdestaddressid(String cdestaddressid) {
		this.cdestaddressid = cdestaddressid;
	}
	public String getCdestareaid() {
		return cdestareaid;
	}
	public void setCdestareaid(String cdestareaid) {
		this.cdestareaid = cdestareaid;
	}
	public String getCdestsiteid() {
		return cdestsiteid;
	}
	public void setCdestsiteid(String cdestsiteid) {
		this.cdestsiteid = cdestsiteid;
	}

	public String getPk_planyear_b() {
		return pk_planyear_b;
	}
	public void setPk_planyear_b(String pk_planyear_b) {
		this.pk_planyear_b = pk_planyear_b;
	}
	public UFDouble getNmonnum1() {
		return nmonnum1;
	}
	public void setNmonnum1(UFDouble nmonnum1) {
		this.nmonnum1 = nmonnum1;
	}
	public UFDouble getNmonnum2() {
		return nmonnum2;
	}
	public void setNmonnum2(UFDouble nmonnum2) {
		this.nmonnum2 = nmonnum2;
	}
	public UFDouble getNmonnum3() {
		return nmonnum3;
	}
	public void setNmonnum3(UFDouble nmonnum3) {
		this.nmonnum3 = nmonnum3;
	}
	public UFDouble getNmonnum4() {
		return nmonnum4;
	}
	public void setNmonnum4(UFDouble nmonnum4) {
		this.nmonnum4 = nmonnum4;
	}
	public UFDouble getNmonnum5() {
		return nmonnum5;
	}
	public void setNmonnum5(UFDouble nmonnum5) {
		this.nmonnum5 = nmonnum5;
	}
	public UFDouble getNmonnum6() {
		return nmonnum6;
	}
	public void setNmonnum6(UFDouble nmonnum6) {
		this.nmonnum6 = nmonnum6;
	}
	public UFDouble getNmonnum7() {
		return nmonnum7;
	}
	public void setNmonnum7(UFDouble nmonnum7) {
		this.nmonnum7 = nmonnum7;
	}
	public UFDouble getNmonnum8() {
		return nmonnum8;
	}
	public void setNmonnum8(UFDouble nmonnum8) {
		this.nmonnum8 = nmonnum8;
	}
	public UFDouble getNmonnum9() {
		return nmonnum9;
	}
	public void setNmonnum9(UFDouble nmonnum9) {
		this.nmonnum9 = nmonnum9;
	}
	public UFDouble getNmonnum10() {
		return nmonnum10;
	}
	public void setNmonnum10(UFDouble nmonnum10) {
		this.nmonnum10 = nmonnum10;
	}
	public UFDouble getNmonnum11() {
		return nmonnum11;
	}
	public void setNmonnum11(UFDouble nmonnum11) {
		this.nmonnum11 = nmonnum11;
	}
	public UFDouble getNmonnum12() {
		return nmonnum12;
	}
	public void setNmonnum12(UFDouble nmonnum12) {
		this.nmonnum12 = nmonnum12;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_planyear_b";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_plan";
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "HG_PLANYEAR_B";
	}
	
	public UFDouble getAllMonthNum(){
		UFDouble nallmonnum =VariableConst.ZERO;
		for(int i=0;i<12;i++){
			nallmonnum =nallmonnum.add(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(HgPubConst.NMONTHNUM[i])));
		}
		return nallmonnum;
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ƻ������������ݱ���ʱУ��
	 * 2011-1-26����12:36:00
	 * @throws ValidationException
	 */
	public void validata() throws ValidationException{
		super.validata();
		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(getNnetnum());
	
		String[] names = HgPubTool.PLAN_MONTH_NUMKEYS;
		for(String name:names){
			if(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).doubleValue()<UFDouble.ZERO_DBL.doubleValue()){
				throw new ValidationException("����С������·ݷ���");
			}
		}
		if(!(nnum.doubleValue()==getAllMonthNum().doubleValue())){
			throw new ValidationException("�·��������꾻������������");
		}		
	}
	
	public void validataClient() throws ValidationException{
		validata();
		super.validataClient();
	}
	public void validataServer() throws BusinessException{
		validata();
		super.validataServer();
	}
} 