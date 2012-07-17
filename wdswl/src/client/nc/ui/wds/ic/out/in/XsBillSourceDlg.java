package nc.ui.wds.ic.out.in;
import java.awt.Container;
import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;
public class XsBillSourceDlg extends WdsBillSourceDLG{
	
	private static final long serialVersionUID = 1L;
	private boolean isStock = false; //是否是总仓 true=是 false=否
	
	private String m_logUser = null;
	
	private String pk_stock = null; // 当前登录者对应的仓库主键
	
	private int iType = -1;
	
	private String[] inv_Pks = null;// 根据当前登录者查询所属仓库和其仓库所存储的产品
	
	private LoginInforHelper helper = null;
	
	public LoginInforHelper getLoginInforHelper(){
		if(helper == null){
			helper = new LoginInforHelper();
		}
		return helper;
	}	

	public XsBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public XsBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	public void init(){try{
		m_logUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
		pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // 当前登录者对应的仓库主键
		if(pk_stock== null || "".equalsIgnoreCase(pk_stock)){
			throw new BusinessException("当前登录人员没有绑定仓库");
		}
		inv_Pks = getLoginInforHelper().getInvBasDocIDsByUserID(m_logUser);
		if(inv_Pks ==null || inv_Pks.length==0){
			throw new BusinessException("当前登录人员货位下没有绑定存货");
		}
		isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(m_logUser));//是否是总仓
	}catch(Exception e){
		Logger.error(e);
	}}
	
	@Override
	public String getTitle() {
		return "参照销售订单";
	}
	@Override
	public String getHeadCondition() {
		String  pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();		
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(head.dr,0)=0 and head.pk_corp ='"+pk_corp+"' ");//and head.fbillflag=3 //查询 供应链调拨出库 ----调入公司等于当前公司，单据类型为4Y		
		hsql.append("and head.cotherwhid='"+pk_stock+"'");//
		hsql.append(" and head.cgeneralhid not in(select distinct gylbillhid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0)");
		hsql.append("and head.cgeneralhid in");//只能看到包含当前登录人绑定货位下存货的单据
		if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct cgeneralhid from ic_general_b where isnull(ic_general_b.dr,0)=0");
			hsql.append(" and coalesce(nshouldoutnum,0)-coalesce(nacceptnum,0)>0");//应入数量-转出数量>0
			hsql.append(" and cinventoryid in");
			hsql.append(")");
		}else{
			hsql.append("('')");
		}	
		return hsql.toString();
		//head.fbillflag=3 签字状态
		}
	@Override
	public String getBodyCondition() {
	return " and body.cgeneralbid not in(select distinct gylbillbid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0) and body.cinventoryid in ";
	
	}
	
	private String getInvSub(String [] inv_Pks){
		if(inv_Pks == null ){
			return "('')";
		}
		StringBuffer bur = new StringBuffer();
		bur.append("( ");
		for(int i=0;i<inv_Pks.length;i++){
			String pk_invmandoc = inv_Pks[i]==null?" ":inv_Pks[i];
			bur.append("'"+pk_invmandoc+"'");
			if(i<inv_Pks.length-1){
				bur.append(",");
			}
		}
		bur.append(" )");
		return bur.toString();
	}
	@Override
	public boolean isSelfLoadHead(){
		return true;
	}	
	
	@Override
	public boolean isSelfLoadBody(){
		return true;
	}	
	@Override
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	@Override
	public Object getUseObjOnRef() throws Exception{	
		return inv_Pks;
	}

	@Override
	public boolean getIsBusinessType() {
		return false;
	}
}
