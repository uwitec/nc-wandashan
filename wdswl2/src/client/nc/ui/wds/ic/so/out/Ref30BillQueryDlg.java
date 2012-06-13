package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator
 *参照红字销售订单查询模板
 */
public class Ref30BillQueryDlg extends WdsBillQueryDlg{
	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public Ref30BillQueryDlg(Container parent) {
		super(parent,null,pk_corp,WdsWlPubConst.SO_OUT_FUNCODE,userid,null,WdsWlPubConst.SO_OUT_REF30_NODECODE);
	}


	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//增加默认值
		setDefaultValue("so_saleorder_b.cbodywarehouseid", getLoginInforHelper().getCwhid(userid), null);//
	}
	private LoginInforHelper helper = null;
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}
}
