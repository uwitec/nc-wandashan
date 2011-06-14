package nc.vo.dm.so.order;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
/**
 * 发运订单 销售运单 虚拟安排 使用的vo
 * @author Administrator
 *
 */
public class XnapVo extends SuperVO {
	
	public String 	pk_outwhouse;
	public String	pk_inwhouse;
	public String	pk_invmandoc;
	public String	pk_invbasdoc;
	public String	pk_cumandoc;
	public String	pk_cubasdoc;
	public String	cunitid;
	public String	cassunitid;
	public UFDouble nnum;
	public UFDouble	nassnum;
	
	
	public static String NNUM="nnum";
	public static String NASSNUM="nassnum";
	public static String PK_OUTWHOUSE="pk_outwhouse";
	public static String PK_INWHOUSE="pk_inwhouse";
	public static String PK_INVMANDOC="pk_invmandoc";
	public static String PK_INVBASDOC="pk_invbasdoc";
	public static String PK_CUMANDOC="pk_cumandoc";
	public static String PK_CUBASDOC="pk_cubasdoc";
	public static String CUNITID="cunitid";
	public static String CASSUNITID="cassunitid";

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

	public String getPk_outwhouse() {
		return pk_outwhouse;
	}

	public void setPk_outwhouse(String pk_outwhouse) {
		this.pk_outwhouse = pk_outwhouse;
	}

	public String getPk_inwhouse() {
		return pk_inwhouse;
	}

	public void setPk_inwhouse(String pk_inwhouse) {
		this.pk_inwhouse = pk_inwhouse;
	}

	public String getPk_invmandoc() {
		return pk_invmandoc;
	}

	public void setPk_invmandoc(String pk_invmandoc) {
		this.pk_invmandoc = pk_invmandoc;
	}

	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}

	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}

	public String getPk_cumandoc() {
		return pk_cumandoc;
	}

	public void setPk_cumandoc(String pk_cumandoc) {
		this.pk_cumandoc = pk_cumandoc;
	}

	public String getPk_cubasdoc() {
		return pk_cubasdoc;
	}

	public void setPk_cubasdoc(String pk_cubasdoc) {
		this.pk_cubasdoc = pk_cubasdoc;
	}

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getCassunitid() {
		return cassunitid;
	}

	public void setCassunitid(String cassunitid) {
		this.cassunitid = cassunitid;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNassnum() {
		return nassnum;
	}

	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}

}
