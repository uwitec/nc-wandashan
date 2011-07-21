package nc.ui.zb.pub.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.trade.pub.IBillStatus;
/**
 * 
 * @author zhf   临时供应商参照全部
 *
 */
public class TempCustManRefModelAll extends AbstractRefModel {
	
	public TempCustManRefModelAll() {
		super();
	}
	
	@Override
	public String getWherePart() {
		return " isnull(bd_cubasdochg.dr,0)= 0 and (bd_cubasdochg.vbillstatus = "
				+ IBillStatus.CHECKPASS+")";
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { 
				
				"bd_cubasdochg.vbillno",
				"bd_cubasdochg.custname",
				"bd_cubasdochg.mnecode",
				"bd_cubasdochg.custshortname",
				"bd_cubasdochg.taxpayerid"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "供应商编码", "供应商名称", "供应商助记码", "供应商简称", "纳税人登记号"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"bd_cubasdochg.ccubasdochgid"};
	}

	@Override
	public int getDefaultFieldCount() {
		return 5;
	}

	@Override
	public String getPkFieldCode() {
		return "bd_cubasdochg.ccubasdochgid";
	}

	@Override
	public String getRefTitle() {
		return "临时供应商档案";
	}

	@Override
	public String getTableName() {
		return " bd_cubasdochg ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}
}
