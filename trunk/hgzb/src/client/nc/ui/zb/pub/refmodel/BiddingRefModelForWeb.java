package nc.ui.zb.pub.refmodel;

/**
 * zhf 网上招标 标书参照类  关联供应商信息
 */
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BiddingRefModelForWeb extends AbstractRefModel{
	public BiddingRefModelForWeb() {
		super();
	}
	
	private String cvendorid = null;
	public BiddingRefModelForWeb(String cvendorid) {
		super();
		this.cvendorid = cvendorid;
	}

	@Override
	public String getWherePart() {
		String sql =null;
		String re= null;
		if (PuPubVO.getString_TrimZeroLenAsNull(cvendorid) == null) {
			
			sql = " isnull(zb_bidding_h.dr,0)= 0 and (zb_bidding_h.vbillstatus = "
					+ IBillStatus.CHECKPASS
					+ ") and izbtype = "
					+ ZbPubConst.WEB_SUBMIT_PRICE
					+ " and zb_bidding_h.ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT
					+ " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		} else{
			sql= " isnull(zb_bidding_h.dr,0)= 0 and (zb_bidding_h.vbillstatus = "
			+ IBillStatus.CHECKPASS+ ") and zb_bidding_h.izbtype = "+ ZbPubConst.WEB_SUBMIT_PRICE
			+ " and zb_biddingsuppliers.ccustmanid = '"+ cvendorid
			+ "' and isnull(zb_bidding_h.dr,0)=0 and zb_bidding_h.ibusstatus = "+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT
			+ " and isnull(zb_biddingsuppliers.dr,0)=0 and coalesce(zb_biddingsuppliers.fisclose,'N') = 'N'"
			+ " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		}
		
		try {
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
			   re = sql+" and zb_bidding_h.reserve1 = '"+s+"'";
			
		} catch (BusinessException e) {
			e.printStackTrace();
			return sql;
		}
		return re;
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { 
				
				"zb_bidding_h.vbillno",
				"zb_bidding_h.cname",
				"zb_bidding_h.cbiddingid"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "标书编码", "标书名称"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"zb_bidding_h.cbiddingid"};
	}

	@Override
	public int getDefaultFieldCount() {
		return 5;
	}

	@Override
	public String getPkFieldCode() {
		return "zb_bidding_h.cbiddingid";
	}

	@Override
	public String getRefTitle() {
		return "网上招标标书档案";
	}

	@Override
	public String getTableName() {
		return PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null?" zb_bidding_h ":" zb_bidding_h inner join" +
				" zb_biddingsuppliers on zb_bidding_h.cbiddingid = zb_biddingsuppliers.cbiddingid ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}

