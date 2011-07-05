package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author Administrator
 *其他出库 参照 采购取样（WDSC）
 */
public class RefWDSCBillSourceDlg  extends WdsBillSourceDLG {

	

	/**
	 * 
	 */
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
	private TempTableUtil ttutil = null;
	private TempTableUtil getTempTableUtil(){
		if(ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}	
	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, nodeKey, userObj, parent);
		init();
	}
	public void init(){
		try{
			m_logUser = ClientEnvironment.getInstance().getUser().getPrimaryKey();
			pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // 当前登录者对应的仓库主键
			if(pk_stock== null || "".equalsIgnoreCase(pk_stock)){
				throw new BusinessException("当前登录人员没有绑定仓库");
			}
			iType = getLoginInforHelper().getITypeByUser(m_logUser);//人员类型
			inv_Pks = getLoginInforHelper().getInvBasDocIDsByUserID(m_logUser);
			if(inv_Pks ==null || inv_Pks.length==0){
				throw new BusinessException("当前登录人员货位下没有绑定存货");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(m_logUser));//是否是总仓
		}catch(Exception e){
			Logger.error(e);
		}
	}
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "参照采购取样";
	}
	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(wds_cgqy_h.dr,0)=0 and wds_cgqy_h.vbillstatus =1 ");
		if(!isStock){
			hsql.append("and wds_cgqy_h.pk_outwhouse='"+pk_stock+"'");//分仓只能看到自己的，总仓可以看到总仓+分仓的
		}
		hsql.append("and wds_cgqy_h.pk_cgqy_h in");//只能看到包含当前登录人绑定货位下存货的单据
	    //if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct pk_cgqy_h from wds_cgqy_b where isnull(wds_cgqy_b.dr,0)=0");
			hsql.append(" and coalesce(nplannum,0)-coalesce(noutnum,0)>0");//安排数量-出库数量>0
			String sub = getTempTableUtil().getSubSql(inv_Pks);
			hsql.append(" and pk_invmandoc in");
			
		
		return hsql.toString();
	}
	
	
	@Override
	public String getBodyCondition() {	
		String sub = getTempTableUtil().getSubSql(inv_Pks);
		return " coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"+//安排数量-出库数量>0
		" and wds_cgqy_b.pk_invmandoc in";
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
	protected Object getUseObjOnRef() throws Exception {		
		return inv_Pks;
	}
	@Override
	public boolean isSelfLoadBody() {		
		return true;
	}
	@Override
	protected boolean isSelfLoadHead() {
		
		return true;
	}
	@Override
	public boolean getIsBusinessType() {
		return false;
	}


}
