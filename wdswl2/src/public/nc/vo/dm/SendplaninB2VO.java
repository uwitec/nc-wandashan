package nc.vo.dm;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 发运计划录入子表2：记录来源的追加计划信息，以及对应的月计划表体
 * @author Administrator
 *
 */
public class SendplaninB2VO extends SuperVO{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**子表主键 */
    public String pk_sendplanin_b;
    /**父键 */
    public String pk_sendplanin;
    /**主键 */
    public String pk_sendplanin_b2;
	//来源单据表头序列号
	public String csourcebillhid;
	//来源单据表体序列号	
	public String csourcebillbid;
	//来源单据类型
	public String csourcetype;
	//来源单据号
	public String vsourcebillcode;

    /**来源安排主数量 */
    public UFDouble sorce_ndealnum;
    /**来源安排辅数量 */
    public UFDouble sorce_nassdealnum;
	public String getPk_sendplanin_b() {
		return pk_sendplanin_b;
	}

	public void setPk_sendplanin_b(String pk_sendplanin_b) {
		this.pk_sendplanin_b = pk_sendplanin_b;
	}

	public String getPk_sendplanin() {
		return pk_sendplanin;
	}

	public void setPk_sendplanin(String pk_sendplanin) {
		this.pk_sendplanin = pk_sendplanin;
	}

	public String getPk_sendplanin_b2() {
		return pk_sendplanin_b2;
	}

	public void setPk_sendplanin_b2(String pk_sendplanin_b2) {
		this.pk_sendplanin_b2 = pk_sendplanin_b2;
	}

	public UFDouble getSorce_ndealnum() {
		return sorce_ndealnum;
	}

	public void setSorce_ndealnum(UFDouble sorce_ndealnum) {
		this.sorce_ndealnum = sorce_ndealnum;
	}

	public UFDouble getSorce_nassdealnum() {
		return sorce_nassdealnum;
	}

	public void setSorce_nassdealnum(UFDouble sorce_nassdealnum) {
		this.sorce_nassdealnum = sorce_nassdealnum;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sendplanin_b2";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sendplanin";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_sendplanin_b2";
	}

	public String getCsourcebillhid() {
		return csourcebillhid;
	}

	public void setCsourcebillhid(String csourcebillhid) {
		this.csourcebillhid = csourcebillhid;
	}

	public String getCsourcebillbid() {
		return csourcebillbid;
	}

	public void setCsourcebillbid(String csourcebillbid) {
		this.csourcebillbid = csourcebillbid;
	}

	public String getCsourcetype() {
		return csourcetype;
	}

	public void setCsourcetype(String csourcetype) {
		this.csourcetype = csourcetype;
	}

	public String getVsourcebillcode() {
		return vsourcebillcode;
	}

	public void setVsourcebillcode(String vsourcebillcode) {
		this.vsourcebillcode = vsourcebillcode;
	}

}
