package nc.ui.zb.pub.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubTool;
/**
 * 
 * @author zhf  自定义供应商档案参照   关联标书  标书备选供应商参照
 *
 */
public class CustManRefModelSelf extends AbstractRefModel {
	
	private String cbiddingid = null;
	public void setBiddingid(String biddingid){
		cbiddingid = biddingid;
	}
	
	@Override
	public String getWherePart() {
			String sql =  " isnull(h.dr,0)= 0 and (h.vbillstatus = "
					+ IBillStatus.CHECKPASS
					+ ") " 
					+ " and isnull(bas.dr,0)=0 and isnull(man.dr,0)=0"
					+ " and isnull(s.dr,0)=0 and coalesce(s.fisclose,'N') = 'N'"
					+ " and h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
			if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
				return sql;
			sql = sql + " and s.cbiddingid = '"
			+ cbiddingid.trim()+"'";
		//	return sql;
			
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
				
				"bas.custcode",
				"bas.custname",
				"man.pk_cumandoc"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "供应商编码", "供应商名称"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"man.pk_cumandoc"};
	}

	@Override
	public int getDefaultFieldCount() {
		return 5;
	}

	@Override
	public String getPkFieldCode() {
		return "man.pk_cumandoc";
	}

	@Override
	public String getRefTitle() {
		return "供应商档案";
	}

	@Override
	public String getTableName() {
		String sql =" bd_cubasdoc bas inner join" +
				" bd_cumandoc  man on bas.pk_cubasdoc = man.pk_cubasdoc inner join zb_biddingsuppliers s " +
				" on man.pk_cumandoc = s.ccustmanid inner join zb_bidding_h h on h.cbiddingid = s.cbiddingid ";
		
		return sql;
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}
