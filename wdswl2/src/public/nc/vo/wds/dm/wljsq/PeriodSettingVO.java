package nc.vo.wds.dm.wljsq;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class PeriodSettingVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Integer datavale;//  下拉值
	public String pk_corp;//公司主键
    public UFDate dmakedate;//制单日期
    public String voperatorid;//制单人
    public Integer dr;
    public UFDateTime ts;
    public String pk_periodsetting;  //主表主键 
    
	public Integer getDatavale() {
		return datavale;
	}

	public void setDatavale(Integer datavale) {
		this.datavale = datavale;
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

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_periodsetting";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_periodsetting_h";
	}
	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2010-7-16
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "wds_periodsetting_h";

	}
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return pk_periodsetting;
	}
	
	@Override
	public void setPrimaryKey(String newpk_periodsetting) {
		// TODO Auto-generated method stub
		pk_periodsetting=newpk_periodsetting;
	}

}
