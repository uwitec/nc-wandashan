package nc.vo.dm;

import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class PlanDealVO extends SendplaninBVO {
	private UFDouble nnum = null;//本次安排数量
	private String pk_corp;
    private UFDate dmakedate;
    private String voperatorid;
    private String vapprovenote;
    private String pk_billtype;
    private Integer vbillstatus;
    /**计划类型  */
    private Integer iplantype;
    private String vemployeeid;
    private String pk_busitype;
    /**主键 */
    private String pk_sendplanin;
    private UFDate dbilldate;
    private String vbillno;
    /**调入仓库 */
    private String pk_inwhouse;
    private String pk_deptdoc;
    /**调出仓库 */
    private String pk_outwhouse;
    private UFDate dapprovedate;
    private String vapproveid;
    
    


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
	 * @作者：zhf
	 * @说明：完达山物流项目 调出仓库不能为空   调入仓库不能为空 两个仓库不能相同  
	 *        本次安排数量不能大于 计划数量-累计安排数量
	 * @时间：2011-3-23下午08:19:19
	 */
	public void validataOnDeal() throws ValidationException{
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_outwhouse())==null){
			throw new ValidationException("发货站不能为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPk_inwhouse())==null){
			throw new ValidationException("收货站不能为空");
		}
		if(getPk_outwhouse().equalsIgnoreCase(getPk_inwhouse())){
			throw new ValidationException("发货站不能和收获站相同");
		}
		UFDouble nchecknum = PuPubVO.getUFDouble_NullAsZero(getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(getNdealnum()));
		if(PuPubVO.getUFDouble_NullAsZero(getNnum()).sub(nchecknum).doubleValue()>WdsWlPubTool.DOUBLE_ZERO.doubleValue())
			throw new ValidationException("不能超计划安排");
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
	
	

}
