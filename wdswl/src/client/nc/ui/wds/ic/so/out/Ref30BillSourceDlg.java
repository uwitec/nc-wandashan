package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author Administrator
 * 参照红字销售订单
 */
public class Ref30BillSourceDlg extends WdsBillSourceDLG{
	
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

	
	public Ref30BillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType,
				templateId, currentBillType, parent);
		init();
	}
	public Ref30BillSourceDlg(String pkField, String pkCorp, String operator,
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
	protected boolean isHeadCanMultiSelect() {
		return false;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		//表单参照交换vo添加pk_corp
		hsql.append(" so_sale.pk_corp = '"+getPkCorp()+"' and");

		hsql.append(" isnull(so_sale.dr,0)=0 and so_sale.fstatus = 2 ");
		hsql.append(" and isnull(so_sale.bretinvflag,'N')='N'");
		if(!isStock){
			hsql.append("and so_sale.cwarehouseid='"+pk_stock+"'");//分仓只能看到自己的，总仓可以看到总仓+分仓的
		}
		hsql.append("and so_sale.csaleid in");//只能看到包含当前登录人绑定货位下存货的单据
//		if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct csaleid from so_saleorder_b where isnull(so_saleorder_b.dr,0)=0 ");
			hsql.append(" and coalesce(nnumber,0)-coalesce(ntaldcnum,0)<0");//订单数量->//利用系统销售订单  已参与价保数量(ntaldcnum) 作为  累计发运数量
//			String sub = getTempTableUtil().getSubSql(inv_Pks);
			hsql.append(" and cinventoryid in");
//			hsql.append(")");
//		}else{
//			hsql.append("('')");
//		}
		return hsql.toString();
	}
	
	@Override
	public String getBodyCondition() {
//		String sub = getTempTableUtil().getSubSql(inv_Pks);
		return " and coalesce(nnumber,0)-coalesce(ntaldcnum,0)<0"+//订单数量-出库数量<0
			" and cinventoryid in";
		}
	
//	public String getBodyContinos(){
//		StringBuffer bs = new StringBuffer();
//		bs.append(" isnull(so_saleorder_b.dr,0)=0  ");
//		return bs.toString();
//	}
	
	@Override
	protected boolean isSelfLoadHead(){
		return true;
	}
	@Override
    public boolean isSelfLoadBody() {
		
		return true;
	}
	@Override
    protected Object getUseObjOnRef()throws Exception{
		return inv_Pks;
	}
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
//	public void loadMultiBodyData(String tableCode,String key,String name) throws Exception{
//		if(tableCode != null && key != null && name != null 
//				&& !"".equals(tableCode) && !"".equals(key) && !"".equals(name) ){
//			SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
//			getbillListPanel().setBodyValueVO(tableCode, supervos);
//			getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
//		}
//	}
}
