package nc.vo.dm;

import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealVO extends SendplaninBVO {
	/**
	 * 
	 */
	public static final long serialVersionUID = 5274835182681933810L;
	public UFDouble nnum = null;//���ΰ�������
	public UFDouble nassnum = null;//���ΰ��Ÿ�����
	public String pk_corp;
    public UFDate dmakedate;
    public String voperatorid;
    public String vapprovenote;
    public String pk_billtype;
    public Integer vbillstatus;
    /**�ƻ�����  */
    public Integer iplantype;
    public String vemployeeid;
    public String pk_busitype;
    /**���� */
    public String pk_sendplanin;
    public UFDate dbilldate;
    public String vbillno;
    /**����ֿ� */
    public String pk_inwhouse;
    public String pk_deptdoc;
    /**�����ֿ� */
    public String pk_outwhouse;
    public UFDate dapprovedate;
    public String vapproveid; 
	private UFDouble nstorenumout;//���������
	private UFDouble narrstorenumout;//���ź���������
	private UFDouble nusefulnumout;//������
	private UFDouble narrusefulnumout;//���ź��������
	private UFDouble ndrqstorenumout;//�����ڿ��������
	private UFDouble ndrqarrstorenumout;//�����ڰ��ź���������
	private UFDouble ndrqusefulnumout;//�����ڿ������
	private UFDouble ndrqarrusefulnumout;//�����ڰ��ź������
    public UFDouble nstorenumin;//�ջ�վ�������
    public UFDouble narrstorenumin;//�ջ�վ���ź�������  
    public UFDouble ndrqstorenumin;//�ջ�վ�������
    public UFDouble ndrqarrstorenumin;//�ջ�վ���ź�������  
    public UFBoolean bisdate;   //�Ƿ������
   
	public UFDouble getNdrqstorenumin() {
		return ndrqstorenumin;
	}
	public void setNdrqstorenumin(UFDouble ndrqstorenumin) {
		this.ndrqstorenumin = ndrqstorenumin;
	}
	public UFDouble getNdrqarrstorenumin() {
		return ndrqarrstorenumin;
	}
	public void setNdrqarrstorenumin(UFDouble ndrqarrstorenumin) {
		this.ndrqarrstorenumin = ndrqarrstorenumin;
	}
	@Override
    public Object getAttributeValue(String arg) {
    	if("nnum".equalsIgnoreCase(arg)){
    		return this.getNnum();
    	}else if("nassnum".equalsIgnoreCase(arg)){
    		return this.getNassnum();
    	}else if("nstorenumout".equalsIgnoreCase(arg)){
    		return this.getNstorenumout();
    	}else if("narrstorenumout".equalsIgnoreCase(arg)){
    		return this.getNarrstorenumout();
    	}else if("nusefulnumout".equalsIgnoreCase(arg)){
    		return this.getNusefulnumout();
    	}else if("narrusefulnumout".equalsIgnoreCase(arg)){
    		return this.getNarrusefulnumout();
    	}else if("ndrqstorenumout".equalsIgnoreCase(arg)){
    		return this.getNdrqstorenumout();
    	}else if("ndrqarrstorenumout".equalsIgnoreCase(arg)){
    		return this.getNdrqarrstorenumout();
    	}else if("ndrqusefulnumout".equalsIgnoreCase(arg)){
    		return this.getNdrqusefulnumout();
    	}else if("ndrqarrusefulnumout".equalsIgnoreCase(arg)){
    		return this.getNdrqarrusefulnumout();
    	}else if("nstorenumin".equalsIgnoreCase(arg)){
    		return this.getNstorenumin();
    	}else if("narrstorenumin".equalsIgnoreCase(arg)){
    		return this.getNarrstorenumin();
    	}else if("ndrqstorenumin".equalsIgnoreCase(arg)){
    		return this.getNdrqstorenumin();
    	}else if("ndrqarrstorenumin".equalsIgnoreCase(arg)){
    		return this.getNdrqarrstorenumin();
    	}else if("pk_corp".equalsIgnoreCase(arg)){
    		return this.getPk_corp();
    	}else if("dmakedate".equalsIgnoreCase(arg)){
    		return this.getDmakedate();
    	}else if("voperatorid".equalsIgnoreCase(arg)){
    		return this.getVoperatorid();
    	}else if("vapprovenote".equalsIgnoreCase(arg)){
    		return this.getVapprovenote();
    	}else if("pk_billtype".equalsIgnoreCase(arg)){
    		return this.getPk_billtype();
    	}else if("iplantype".equalsIgnoreCase(arg)){
    		return this.getIplantype();
    	}else if("vbillstatus".equalsIgnoreCase(arg)){
    		return this.getVbillstatus();
    	}else if("vemployeeid".equalsIgnoreCase(arg)){
    		return this.getVemployeeid();
    	}else if("pk_busitype".equalsIgnoreCase(arg)){
    		return this.getPk_busitype();
    	}else if("pk_sendplanin".equalsIgnoreCase(arg)){
    		return this.getPk_sendplanin();
    	}else if("dbilldate".equalsIgnoreCase(arg)){
    		return this.getDbilldate();
    	}else if("vbillno".equalsIgnoreCase(arg)){
    		return this.getVbillno();
    	}else if("pk_inwhouse".equalsIgnoreCase(arg)){
    		return this.getPk_inwhouse();
    	}else if("pk_deptdoc".equalsIgnoreCase(arg)){
    		return this.getPk_deptdoc();
    	}else if("pk_outwhouse".equalsIgnoreCase(arg)){
    		return this.getPk_outwhouse();
    	}else if("dapprovedate".equalsIgnoreCase(arg)){
    		return this.getDapprovedate();
    	}else if("vapproveid".equalsIgnoreCase(arg)){
    		return this.getVapproveid();
    	}else if("bisdate".equalsIgnoreCase(arg)){
    		return this.getBisdate();
    	}
    	return super.getAttributeValue(arg);
    }
  public void setAttributeValue(String arg, Object value)  {
    	if("nnum".equalsIgnoreCase(arg)){
    		 setNnum(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("nassnum".equalsIgnoreCase(arg)){
    		setNassnum(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("nstorenumout".equalsIgnoreCase(arg)){
    		setNstorenumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("narrusefulnumout".equalsIgnoreCase(arg)){
    		setNarrusefulnumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("narrstorenumout".equalsIgnoreCase(arg)){
    		setNarrstorenumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("nusefulnumout".equalsIgnoreCase(arg)){
    		setNusefulnumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqstorenumout".equalsIgnoreCase(arg)){
    		setNdrqstorenumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqarrusefulnumout".equalsIgnoreCase(arg)){
    		setNdrqarrusefulnumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqarrstorenumout".equalsIgnoreCase(arg)){
    		setNdrqarrstorenumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqusefulnumout".equalsIgnoreCase(arg)){
    		setNdrqusefulnumout(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("nstorenumin".equalsIgnoreCase(arg)){
    		setNstorenumin(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("narrstorenumin".equalsIgnoreCase(arg)){
    		setNarrstorenumin(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqstorenumin".equalsIgnoreCase(arg)){
    		setNdrqstorenumin(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("ndrqarrstorenumin".equalsIgnoreCase(arg)){
    		setNdrqarrstorenumin(PuPubVO.getUFDouble_NullAsZero(value));
    	}else if("pk_corp".equalsIgnoreCase(arg)){
    		setPk_corp(PuPubVO.getString_TrimZeroLenAsNull(value));
    	}else if("dmakedate".equalsIgnoreCase(arg)){
    		setDmakedate((UFDate)value);
    	}else if("voperatorid".equalsIgnoreCase(arg)){
    		setVoperatorid((String)value);
    	}else if("vapprovenote".equalsIgnoreCase(arg)){
    		setVapprovenote((String)value);
    	}else if("pk_billtype".equalsIgnoreCase(arg)){
    		setPk_billtype((String)value);
    	}else if("iplantype".equalsIgnoreCase(arg)){
    		setIplantype(PuPubVO.getInteger_NullAs(value, WdsWlPubTool.INTEGER_ZERO_VALUE));
    	}else if("vbillstatus".equalsIgnoreCase(arg)){
    		setVbillstatus((Integer)value);
    	}else if("vemployeeid".equalsIgnoreCase(arg)){
    		setVemployeeid((String)value);
    	}else if("pk_busitype".equalsIgnoreCase(arg)){
    		setPk_busitype((String)value);
    	}else if("pk_sendplanin".equalsIgnoreCase(arg)){
    		setPk_sendplanin((String)value);
    		
    	}else if("dbilldate".equalsIgnoreCase(arg)){
    		setDbilldate((UFDate)value);
    	}else if("vbillno".equalsIgnoreCase(arg)){
    		setVbillno((String)value);
    	}else if("pk_inwhouse".equalsIgnoreCase(arg)){
    		setPk_inwhouse((String)value);
    	}else if("pk_deptdoc".equalsIgnoreCase(arg)){
    		setPk_deptdoc((String)value);
    	}else if("pk_outwhouse".equalsIgnoreCase(arg)){
    		setPk_outwhouse((String)value);
    	}else if("dapprovedate".equalsIgnoreCase(arg)){
    		setDapprovedate((UFDate)value);
    	}else if("vapproveid".equalsIgnoreCase(arg)){
    		setVapproveid((String)value);
    	}else if("bisdate".equalsIgnoreCase(arg)){
    		setBisdate((UFBoolean)value);
    	}
    	 super.setAttributeValue(arg, value);
    }
    
	public UFDouble getNassnum() {
		return nassnum;
	}
	
	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public String getVapprovenote() {
		return vapprovenote;
	}

	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public Integer getVbillstatus() {
		return vbillstatus;
	}

	public void setVbillstatus(Integer vbillstatus) {
		this.vbillstatus = vbillstatus;
	}

	public Integer getIplantype() {
		return iplantype;
	}

	public void setIplantype(Integer iplantype) {
		this.iplantype = iplantype;
	}

	public String getVemployeeid() {
		return vemployeeid;
	}

	public void setVemployeeid(String vemployeeid) {
		this.vemployeeid = vemployeeid;
	}

	public String getPk_busitype() {
		return pk_busitype;
	}

	public void setPk_busitype(String pk_busitype) {
		this.pk_busitype = pk_busitype;
	}

	public String getPk_sendplanin() {
		return pk_sendplanin;
	}

	public void setPk_sendplanin(String pk_sendplanin) {
		this.pk_sendplanin = pk_sendplanin;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getVbillno() {
		return vbillno;
	}

	public void setVbillno(String vbillno) {
		this.vbillno = vbillno;
	}

	public String getPk_inwhouse() {
		return pk_inwhouse;
	}

	public void setPk_inwhouse(String pk_inwhouse) {
		this.pk_inwhouse = pk_inwhouse;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getPk_outwhouse() {
		return pk_outwhouse;
	}

	public void setPk_outwhouse(String pk_outwhouse) {
		this.pk_outwhouse = pk_outwhouse;
	}

	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	public void setDapprovedate(UFDate dapprovedate) {
		this.dapprovedate = dapprovedate;
	}

	public String getVapproveid() {
		return vapproveid;
	}

	public void setVapproveid(String vapproveid) {
		this.vapproveid = vapproveid;
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ �����ֿⲻ��Ϊ��   ����ֿⲻ��Ϊ�� �����ֿⲻ����ͬ  
	 *        ���ΰ����������ܴ��� �ƻ�����-�ۼư�������
	 * @ʱ�䣺2011-3-23����08:19:19
	 */
	public void validataOnDeal() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_outwhouse())==null){
			throw new ValidationException("����վ����Ϊ��");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_inwhouse())==null){
			throw new ValidationException("�ջ�վ����Ϊ��");
		}
		if(getPk_outwhouse().equalsIgnoreCase(getPk_inwhouse())){
			throw new ValidationException("����վ���ܺ��ջ�վ��ͬ");
		}
		if(getHsl() != null && getHsl().doubleValue()>0){
			setNassnum(PuPubVO.getUFDouble_NullAsZero(getNnum()).div(getHsl()));
		}
		UFDouble nchecknum = PuPubVO.getUFDouble_NullAsZero(getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(getNdealnum()));
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).sub(nchecknum).doubleValue()>WdsWlPubTool.DOUBLE_ZERO.doubleValue())
			throw new ValidationException("���ܳ��ƻ�����");
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	public UFBoolean getBisdate() {
		return bisdate;
	}
	public void setBisdate(UFBoolean bisdate) {
		this.bisdate = bisdate;
	}
	public UFDouble getNstorenumout() {
		return nstorenumout;
	}
	public void setNstorenumout(UFDouble nstorenumout) {
		this.nstorenumout = nstorenumout;
	}
	public UFDouble getNarrstorenumout() {
		return narrstorenumout;
	}
	public void setNarrstorenumout(UFDouble narrstorenumout) {
		this.narrstorenumout = narrstorenumout;
	}
	public UFDouble getNstorenumin() {
		return nstorenumin;
	}
	public void setNstorenumin(UFDouble nstorenumin) {
		this.nstorenumin = nstorenumin;
	}
	public UFDouble getNarrstorenumin() {
		return narrstorenumin;
	}
	public void setNarrstorenumin(UFDouble narrstorenumin) {
		this.narrstorenumin = narrstorenumin;
	}
	public UFDouble getNusefulnumout() {
		return nusefulnumout;
	}
	public void setNusefulnumout(UFDouble nusefulnumout) {
		this.nusefulnumout = nusefulnumout;
	}
	public UFDouble getNarrusefulnumout() {
		return narrusefulnumout;
	}
	public void setNarrusefulnumout(UFDouble narrusefulnumout) {
		this.narrusefulnumout = narrusefulnumout;
	}
	public UFDouble getNdrqstorenumout() {
		return ndrqstorenumout;
	}
	public void setNdrqstorenumout(UFDouble ndrqstorenumout) {
		this.ndrqstorenumout = ndrqstorenumout;
	}
	public UFDouble getNdrqarrstorenumout() {
		return ndrqarrstorenumout;
	}
	public void setNdrqarrstorenumout(UFDouble ndrqarrstorenumout) {
		this.ndrqarrstorenumout = ndrqarrstorenumout;
	}
	public UFDouble getNdrqusefulnumout() {
		return ndrqusefulnumout;
	}
	public void setNdrqusefulnumout(UFDouble ndrqusefulnumout) {
		this.ndrqusefulnumout = ndrqusefulnumout;
	}
	public UFDouble getNdrqarrusefulnumout() {
		return ndrqarrusefulnumout;
	}
	public void setNdrqarrusefulnumout(UFDouble ndrqarrusefulnumout) {
		this.ndrqarrusefulnumout = ndrqarrusefulnumout;
	}
	

}
