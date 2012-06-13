package nc.ui.wds.ic.other.in;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author mlr
 *  其他入库 参照 其他出库 查询模板
 *
 */
public class RefBillQueryDlgWDS7 extends WdsBillQueryDlg{

private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefBillQueryDlgWDS7(Container parent) {
		super(parent,null,pk_corp,WdsWlPubConst.OTHER_OUT_FUNCODE,userid,null,null);
	}
	@Override
	public String getWhereSQL() {
	String sql=super.getWhereSQL();	
		if(sql==null){
	      return null;
		}
		if(sql.contains("tb_outgeneral_h.vbillstatus = 0")){
			
		  return sql.replace("tb_outgeneral_h.vbillstatus = 0", "tb_outgeneral_h.vbillstatus = 8");
		}
		return sql;
	}
	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("tb_outgeneral_h.dbilldate", null, sDate);//单据日期
	}
}
