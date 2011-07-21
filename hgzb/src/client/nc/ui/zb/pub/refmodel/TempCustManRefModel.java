package nc.ui.zb.pub.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubTool;
/**
 * 
 * @author zhf   临时供应商参照 （存在标书）
 *
 */
public class TempCustManRefModel extends AbstractRefModel {
	
	private String cbiddingid = null;
	public void setBiddingid(String biddingid){
		cbiddingid = biddingid;
	}
	
	public TempCustManRefModel() {
		super();
	}
	
	@Override
	public String getWherePart() {
		String sql = " isnull(h.dr,0)= 0 and (h.vbillstatus = "+ IBillStatus.CHECKPASS+ ") "
				+ " and isnull(bd_cubasdochg.dr,0)= 0 and (bd_cubasdochg.vbillstatus = "+ IBillStatus.CHECKPASS+ ")"
				+ " and isnull(s.dr,0)=0 and coalesce(s.fisclose,'N') = 'N'"+ " and h.pk_corp = '"
				+ ClientEnvironment.getInstance().getCorporation().getPrimaryKey() + "'";

		if (PuPubVO.getString_TrimZeroLenAsNull(cbiddingid) == null)
			return sql;
		sql = sql + " and s.cbiddingid = '" + cbiddingid.trim() + "'";
		String re =sql;
		try {
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
			   re = sql+" and h.reserve1 = '"+s+"'";
			
		} catch (BusinessException e) {
			e.printStackTrace();
			return sql;
		}
		return re;
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
		return " bd_cubasdochg bd_cubasdochg inner join zb_biddingsuppliers s " +
				" on bd_cubasdochg.ccubasdochgid = s.ccustmanid inner join zb_bidding_h h on h.cbiddingid = s.cbiddingid";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}
}
