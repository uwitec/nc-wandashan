package nc.ui.zb.ref;

	import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.querytemplate.normalpanel.INormalQueryPanel;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.zb.pub.ZbPubConst;

	/**
	 *中标录入结果参照对话框，上游是中标审批表
	 */
	public class RefZb02BillQueryDLG extends ZbBillQueryDlg {

		private static final long serialVersionUID = 1L;
		
		public RefZb02BillQueryDLG(Container parent, TemplateInfo ti) {
			super(parent, getTemplateInfo());
		}
		
		public RefZb02BillQueryDLG(Container parent, INormalQueryPanel normalPnl, TemplateInfo ti) {
			super(parent, normalPnl, getTemplateInfo());
		}
		
		public static TemplateInfo getTemplateInfo(){
			String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
			String userid = ClientEnvironment.getInstance().getUser().getPrimaryKey();
			TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(pk_corp);
			tempinfo.setCurrentCorpPk(pk_corp);
			tempinfo.setFunNode(ZbPubConst.ZB_Result_BILLTYPE);//中标结果录入
			tempinfo.setUserid(userid);
			tempinfo.setNodekey(ZbPubConst.ZB_EVALUATION_BILLTYPE);//中标评审表
			return tempinfo;
		}
	    @Override
	    public String getWhereSQL() {
	    	String pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
	    	String swhere = " isnull(dr,0)=0 and pk_corp = '"+pk_corp+"' and pk_billtype = '"+ZbPubConst.ZB_EVALUATION_BILLTYPE+"'" +
	    			" and (nvl(nzbnum, 0) - nvl(ntotalnum, 0))>0 ";
	    	//审批状态
	    	String sql = super.getWhereSQL();
	    	if(sql != null && sql.length()>0){
	    		sql = sql + " and " + swhere;
	    	}else{
	    		sql = swhere;
	    	}
	    	return sql;
	    }

		public void initData(String pkCorp, String operator, String funNode,
				String businessType, String currentBillType, String sourceBilltype,
				String nodeKey, Object userObj) throws Exception {
		}
	}
