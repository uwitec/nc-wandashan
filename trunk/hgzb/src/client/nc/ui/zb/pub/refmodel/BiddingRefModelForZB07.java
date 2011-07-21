package nc.ui.zb.pub.refmodel;
/**
 * zhw  分量调整单的标书参照
 */
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BiddingRefModelForZB07 extends AbstractRefModel{
	public BiddingRefModelForZB07() {
		super();
	}
    // 标书业务状态完成  审批通过   中标审批表审批通过  调整单审批通过
	@Override
	public String getWherePart() {
		String re=null;
		String sql = " isnull(zb_bidding_h.dr,0)= 0 and (zb_bidding_h.vbillstatus = "
			   + IBillStatus.CHECKPASS+") and (zb_bidding_h.cbiddingid not in (select distinct ah.cbiddingid "
               +" from zb_avnum_h ah where ah.vbillstatus <>"+IBillStatus.CHECKPASS+" and isnull(ah.dr, 0) = 0 and ah.cbiddingid is not null))"
               + " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'"
               + " and zb_bidding_h.ibusstatus in("+ZbPubConst.BIDDING_BUSINESS_STATUE_CLOSE +")  and zb_bidevaluation_h.vbillstatus=1";
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
		return "标书档案";
	}

	@Override
	public String getTableName() {
		return " zb_bidding_h join zb_bidevaluation_h on zb_bidding_h.cbiddingid =zb_bidevaluation_h.cbiddingid";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}

