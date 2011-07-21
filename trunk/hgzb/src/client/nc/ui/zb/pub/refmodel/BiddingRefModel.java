package nc.ui.zb.pub.refmodel;
/**
 * zhf 现场招标 标书参照类
 */
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class BiddingRefModel extends AbstractRefModel{
	public BiddingRefModel() {
		super();
	}

	@Override
	public String getWherePart() {
		String sql =" isnull(zb_bidding_h.dr,0)= 0 and (zb_bidding_h.vbillstatus = "
			+ IBillStatus.CHECKPASS+") and izbtype = "+ZbPubConst.LOCAL_SUBMIT_PRICE 
			+ " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'"
			+ " and zb_bidding_h.ibusstatus in("+ZbPubConst.BIDDING_BUSINESS_STATUE_INIT +","+ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT+")";
		String re =sql;
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
		return "现场招标标书档案";
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

}

