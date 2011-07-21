package nc.ui.zb.pub.refmodel;
/**
 * zhw ���������(����������)  ���ճ�����̬�� ����  ��������ڵ�ʹ��
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
		return new String[] { "�������", "��������"};
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
		return "���鵵��";
	}

	@Override
	public String getTableName() {
		return " zb_bidding_h ";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//���ò�����
		setCacheEnabled(false);
		return "";
	}

}

