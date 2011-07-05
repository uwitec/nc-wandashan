package nc.ui.wds.ic.other.in;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

public class RefBillSourceDlg extends WdsBillSourceDLG{
	

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

	public RefBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public RefBillSourceDlg(String pkField, String pkCorp, String operator,
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
		iType = getLoginInforHelper().getITypeByUser(m_logUser);//人员类型
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
		// TODO Auto-generated method stub
		return "参照其他出库单";
	}
	@Override
	public String getHeadCondition() {
		String  pk_corp = ClientEnvironment.getInstance().getCorporation().getPrimaryKey();		
		StringBuffer hsql = new StringBuffer();
		hsql.append(" isnull(head.dr,0)=0 and head.pk_corp ='"+pk_corp+"' and head.cbilltypecode = '4I' ");//and head.fbillflag=3 //查询 供应链调拨出库 ----调入公司等于当前公司，单据类型为4Y
		hsql.append("and head.cotherwhid='"+pk_stock+"'");//
		hsql.append(" and head.cgeneralhid not in(select distinct gylbillhid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0)");
		hsql.append("and head.cgeneralhid in");//只能看到包含当前登录人绑定货位下存货的单据
		//if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct cgeneralhid from ic_general_b where isnull(ic_general_b.dr,0)=0");
			//hsql.append(" and coalesce(nshouldoutnum,0)-coalesce(nacceptnum,0)>0");//应入数量-转出数量>0
		    //	String sub = getIvnSubSql(inv_Pks);
			hsql.append(" and cinventoryid in");
		//	hsql.append(")");
		//}else{
		//	hsql.append("('')");
		//}	
		return hsql.toString();
		//head.fbillflag=3 签字状态
		}
	/**
	 * 
	 * @作者：lyf---调用临时表出错
	 * @说明：完达山物流项目 
	 * @时间：2011-7-3上午09:43:58
	 * @param inv_Pks
	 * @return
	 */
	private String getIvnSubSql(String[] inv_Pks){
		StringBuffer invSbuSql = new StringBuffer();
		invSbuSql.append("('aa'");
		if(inv_Pks != null && inv_Pks.length>0){
			for(int i=0 ;i<inv_Pks.length;i++){
				invSbuSql.append(",'"+inv_Pks[i]+"'");
			}
		}
		invSbuSql.append(")");
		return invSbuSql.toString();
	}
	@Override
	public String getBodyCondition() {
		String sub = getTempTableUtil().getSubSql(inv_Pks);
	return " and body.cgeneralbid not in(select distinct gylbillbid from tb_general_b where gylbillhid is not null and gylbillbid is not null and isnull(dr,0)=0) and body.cinventoryid in ";
	
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
