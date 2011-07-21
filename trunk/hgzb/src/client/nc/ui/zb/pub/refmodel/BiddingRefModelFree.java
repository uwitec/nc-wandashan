package nc.ui.zb.pub.refmodel;
/**
 * zhw 标书参照类(不区分类型)  参照出自由态的 标书  标书调整节点使用
 */
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.BusinessException;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.pub.ZbPubTool;

public class BiddingRefModelFree extends AbstractRefModel{
	public BiddingRefModelFree() {
		super();
	}

	@Override
	public String getWherePart() {
		String sql =" isnull(zb_bidding_h.dr,0)= 0 and cname is not null and vbillstatus =  "+IBillStatus.FREE 
		 + " and zb_bidding_h.pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'";
		String re= null;
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
		return " zb_bidding_h ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//设置不缓存
		setCacheEnabled(false);
		return "";
	}

}

