package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.WdsBillQueryDlg;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���۳�����������˵�(WDS5)��ѯ�Ի���
 * @author zpm
 *
 */
public class RefBillQueryDlg extends WdsBillQueryDlg {
	
	private static final long serialVersionUID = 1L;
	
	private static String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	
	private static String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
	
	public RefBillQueryDlg(Container parent) {
//		super(parent,null,pk_corp,WdsWlPubConst.SO_OUT_FUNCODE,userid,null,WdsWlPubConst.SO_OUT_REFWDS5_NODECODE);
		super(parent,null,pk_corp,WdsWlPubConst.SO_OUT_REFWDS5_NODECODE,userid,null,null);
	}


	@Override
	public String getWhereSQL() {
		String sql=super.getWhereSQL();
		String sqlplus = " isnull(wds_soorder."+ WdsWlPubConst.soorder_close + " ,'N')= 'N' ";
		if(sql==null){
			return sqlplus;
		}
		if(sql.contains("wds_soorder.vbillstatus = 0")){
			
		  return sql.replace("wds_soorder.vbillstatus = 0", "wds_soorder.vbillstatus = 8");
		}
		return sql + "and" + sqlplus;
	}

	public void initData(String pkCorp, String operator, String funNode,
			String businessType, String currentBillType, String sourceBilltype,
			String nodeKey, Object userObj) throws Exception {
		//����Ĭ��ֵ
		String sDate = ClientEnvironment.getInstance().getBusinessDate().toString();
		setDefaultValue("wds_soorder.dbilldate", null, sDate);//��������
	}
}
