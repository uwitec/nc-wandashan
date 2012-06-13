package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator
 *其他出库 参照 采购取样（WDSC）的查询对话框
 */
public class RefWDSCBillQueryDlg extends WdsBillQueryDlg{

	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefWDSCBillQueryDlg(Container parent) {
		super(parent,null,pk_corp,
				WdsWlPubConst.OTHER_OUT_FUNCODE,userid,null,WdsWlPubConst.OTHER_OUT_REFWDSC_NODECODE);
	}


	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		
		if(sql==null){
	      return null;
		}
		if(sql.contains("wds_cgqy_h.vbillstatus = 0")){
			
		  return sql.replace("wds_cgqy_h.vbillstatus = 0", "wds_cgqy_h.vbillstatus = 8");
		}
		return sql;
	}
	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("wds_cgqy_h.dmakedate", null, sDate);//单据
	}


}
