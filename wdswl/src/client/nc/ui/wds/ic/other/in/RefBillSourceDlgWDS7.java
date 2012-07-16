package nc.ui.wds.ic.other.in;
import java.awt.Container;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wdsnew.pub.MBillSourceDLG;
import nc.ui.wdsnew.pub.PowerGetTool;
import nc.ui.wl.pub.LoginInforHelper;
/**
 * @author mlr
 * ������� ���� �������� ����
 */
public class RefBillSourceDlgWDS7 extends MBillSourceDLG{
	private static final long serialVersionUID = 4237270665256372871L;

	private LoginInforHelper helper = null;
	

	//���Ȩ�޹��˵�sql
	String sql=null;
	private String getPowerSql(){
		if (sql == null || sql.length() == 0)
			try {
				sql = PowerGetTool.queryClassPowerSql(ClientEnvironment
						.getInstance().getUser().getPrimaryKey());
			} catch (Exception e) {
				this.getClientUI().showErrorMessage(e.getMessage());		
				e.printStackTrace();
			}
		return sql;
	}
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}	
	public RefBillSourceDlgWDS7(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefBillSourceDlgWDS7(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	public void init() {
		try {
			setSpiltFields(new String[] { "srl_pkr" });// �����ֿ�ֵ�
			setSpiltFields1(new String[] { "pk_defdoc1" });// ����������λ�ֵ�
		} catch (Exception e) {
			Logger.error(e);
		}
	}	
	@Override
	public String getTitle() {
		return "�����������ⵥ";
	}
	@Override
	public String getHeadCondition() {
		//head.fbillflag=3 ǩ��״̬
			String sql=null;
			try {
				sql= "  coalesce(tb_outgeneral_b.noutnum,0)-coalesce(tb_outgeneral_b.nacceptnum,0)>0 " +//ʵ������-���������>0
				" and tb_outgeneral_b.cinventoryid in ("+getPowerSql()+")" +
				" and tb_outgeneral_h.srl_pkr= '"+getLoginInforHelper().getCwhid(ClientEnvironment.getInstance().getUser().getPrimaryKey())+"'"+//�������ֿ�
			    " and ( tb_outgeneral_b.pk_defdoc1 is null or " +
			    " tb_outgeneral_b.pk_defdoc1 ='"+getLoginInforHelper().getSpaceByLogUserForStore(ClientEnvironment.getInstance().getUser().getPrimaryKey())+"' )";//���˳�����λ  �����˿ջ�λ
			} catch (Exception e) {
				e.printStackTrace();
			}			
		return sql;	
		}
	@Override
	public String getBodyCondition() {
		
	    return " isnull(tb_outgeneral_b.dr,0)=0 and coalesce(tb_outgeneral_b.noutnum,0)-coalesce(tb_outgeneral_b.nacceptnum,0)>0"+//ʵ������-���������>0
		       " and cinventoryid in ("+getPowerSql()+")";
	}
	
	@Override
	protected boolean isHeadCanMultiSelect() {
		return true;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	

	@Override
	public String getPk_invbasdocName() {
		return "cinvbasid";
	}
	@Override
	public String getPk_invmandocName() {
		return "cinventoryid";
	}
	@Override
	public IControllerBase getUIController() {
		return new nc.ui.wds.ic.other.out.OtherOutClientUICtrl();
	}
	

		
	
	
	
}
