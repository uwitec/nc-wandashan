
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 自定义的单据数据查找器
 * 必须继承DefaultDataFinder2这个类
 * @author mlr
 */
public class BillDataFinder2 extends DefaultDataFinder2{
	
	private String currentBillType = null;//设定当前单据
	
	private String srcBillType = null;//设定来源单据
	/**
	 * 根据来源单据类型创建 查找来源单据的  单据id 单据号 的sql
	 * billtype 为来源单据类型
	 */
	@Override
	protected String createSQL1(String billType) {
		String sql = null;		
		if(WdsWlPubConst.WDS3.equals(billType)){//发运订单
			sql = " select distinct h1.PK_SENDORDER,h1.VBILLNO from wds_sendorder h1,wds_sendorder_b b1 where h1.PK_SENDORDER = b1.PK_SENDORDER and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//销售出库
			sql = "  select distinct h1.GENERAL_PK,h1.VBILLCODE from tb_outgeneral_h h1,tb_outgeneral_b b1 where h1.GENERAL_PK = b1.GENERAL_PK and nvl(h1.dr,0) = 0 and nvl(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
	   }else{
			super.createSQL1(billType);
		}
		return sql;
	}
	/**
	 * 根据当前单据类型 创建查找来源单据 的单据类型，单据id,单据号 的查询sql 
	 * 该方法用于注册查找来源单据的sql
	 * billType 为当前的单据类型
	 */
	@Override
	protected String createSQL(String billType) {
		String sql = null;
		if(WdsWlPubConst.WDS3.equals(billType)){//发运订单
			sql = " select distinct ss.CSOURCETYPE,ss.CSOURCEBILLHID,ss.vsourcebillcode from wds_sendorder_b ss  where ss.PK_SENDORDER = ? and nvl(ss.dr,0) = 0 ";
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//销售出库
			sql = " select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID,zz.vsourcebillcode from tb_outgeneral_b  zz where zz.GENERAL_PK = ?  and nvl(zz.dr,0) = 0 ";
		}else{
			super.createSQL(billType);
		}
		return sql;
	}	
	public String getCurrentBillType() {
		return currentBillType;
	}	
	public void setCurrentBillType(String currentBillType) {
		this.currentBillType = currentBillType;
	}
	
	public String getSrcBillType() {
		return srcBillType;
	}
	
	public void setSrcBillType(String srcBillType) {
		this.srcBillType = srcBillType;
	}
}
