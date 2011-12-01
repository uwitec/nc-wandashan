package nc.vo.dm.so.deal2;

import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.so.so001.SaleorderHVO;

/**
 * 
 * @author zhf
 * 合并后的订单头vo
 *
 */

public class SoDealHeaderVo extends SaleorderHVO {
	
//	需要的字段 客户id  订单日期（合并前订单的最小日期）  是否特殊安排 销售组织 销售公司
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5983083411953222727L;
	private UFDate dbilldate;
	private String ccustomerid;
	private String csalecorpid;
	private String cbodywarehouseid; //发货仓库
	
	private UFDouble nminnum;//客户最低发货量
	
//	private UFBoolean bisspecial= UFBoolean.FALSE;//是否特殊安排
	private UFBoolean isonsell = UFBoolean.FALSE;//是否特殊安排
	
	public static String[] split_fields = new String[]{"ccustomerid"};
	
	
	public UFDouble getNminnum() {
		return nminnum;
	}

	public void setNminnum(UFDouble nminnum) {
		this.nminnum = nminnum;
	}

	public String getCbodywarehouseid() {
		return cbodywarehouseid;
	}

	public void setCbodywarehouseid(String cbodywarehouseid) {
		this.cbodywarehouseid = cbodywarehouseid;
	}

	public UFDate getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

	public String getCsalecorpid() {
		return csalecorpid;
	}

	public void setCsalecorpid(String csalecorpid) {
		this.csalecorpid = csalecorpid;
	}

	
	public UFBoolean getIsonsell() {
		return isonsell;
	}

	public void setIsonsell(UFBoolean isonsell) {
		this.isonsell = isonsell;
	}

	
	public void validate() throws ValidationException {
		if(PuPubVO.getString_TrimZeroLenAsNull(getCcustomerid())==null){
			throw new ValidationException("客户为空");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getCbodywarehouseid())==null){
			throw new ValidationException("发货仓库为空");
		}
	}

}
