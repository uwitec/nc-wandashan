package nc.ui.zb.pub.refmodel;

/**
 *  标书参照类  关联供应商信息
 */
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BiddingRefModelAll2 extends AbstractRefModel{
	public BiddingRefModelAll2() {
		super();
	}
	
	private String cvendorid = null;
	public BiddingRefModelAll2(String cvendorid) {
		super();
		this.cvendorid = cvendorid;
	}
	
	public void setVendor(String cvendorid){
		this.cvendorid = cvendorid;
	}

	@Override
	public String getWherePart() {
			String sql =  " isnull(zb_bidding_h.dr,0)= 0 and (zb_bidding_h.vbillstatus = "+ IBillStatus.CHECKPASS+") " 
					+ " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'"
					+ " and isnull(zb_bidding_h.dr,0)=0 and zb_bidding_h.ibusstatus in("+ZbPubConst.BIDDING_BUSINESS_STATUE_INIT 
					+ ","+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT+")";
			String re =sql;
			try {
				String s =ZbPubTool.getParam();
				if(s!=null &&!"".equals(s))
				   re = sql+" and zb_bidding_h.reserve1 = '"+s+"'";
			if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null)
				return re;
			re = re + " and zb_bidding_h.cbiddingid in (select zb_biddingsuppliers.cbiddingid from zb_biddingsuppliers where isnull(zb_biddingsuppliers.dr,0)=0 "
			+ " and coalesce(zb_biddingsuppliers.fisclose,'N') = 'N' and zb_biddingsuppliers.ccustmanid = '"
			+ cvendorid.trim()+"')";
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
		return "标书档案";
	}

	@Override
	public String getTableName() {
		return " zb_bidding_h ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}
	
	public String getOrderPart(){
		return "zb_bidding_h.vbillno";
	}

}

