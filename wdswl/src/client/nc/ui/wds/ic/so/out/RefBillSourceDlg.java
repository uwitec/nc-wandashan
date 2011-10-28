package nc.ui.wds.ic.so.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.WdsBillSourceDLG;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 销售出库参照销售运单(WDS5)对话框
 * @author zpm
 *
 */
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
		return true;
	}
	@Override
	protected boolean isBodyCanSelected() {
		return true;
	}
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		hsql.append(" wds_soorder.pk_soorder in ");//只能看到包含当前登录人绑定货位下存货的单据
		if(inv_Pks !=null && inv_Pks.length>0){
			hsql.append("(");
			hsql.append("select distinct wds_soorder.pk_soorder  ");
			hsql.append(" from wds_soorder ");//销售运单主表
			hsql.append(" join wds_soorder_b ");//销售运单字表
			hsql.append(" on  wds_soorder.pk_soorder = wds_soorder_b.pk_soorder ");
			hsql.append(" where isnull(wds_soorder.dr,0)=0 ");
			hsql.append(" and wds_soorder.pk_corp = '"+getPkCorp()+"'");
			hsql.append(" and wds_soorder.vbillstatus =8 ");//自由态的销售运单
			hsql.append(" and isnull(iprintcount,0)>0 ");//并且打印过的
			if(!isStock){
				hsql.append(" and wds_soorder.pk_outwhouse='"+pk_stock+"'");//分仓只能看到自己的，总仓可以看到总仓+分仓的
			}
			hsql.append(" and isnull(wds_soorder_b.dr,0)= 0");
			hsql.append(" and coalesce(wds_soorder_b.narrangnmu,0)-coalesce(wds_soorder_b.noutnum,0)>0");//安排数量-出库数量>0
			String sub = getInvSub(inv_Pks);
			hsql.append(" and pk_invmandoc in"+sub);
			hsql.append(" )");
		}else{
			hsql.append("('')");
		}
		return hsql.toString();
		
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
	public void loadBodyData(int row) {
		try {
			// 获得主表ID
			String key = getbillListPanel().getHeadBillModel().getValueAt(row,getpkField()).toString();
			loadMultiBodyData(key);
		} catch (Exception e) {
			Logger.error(e.getMessage()+"表体数据加载失败!", e);
		}
	}
	public String[][] getMultiTableCode(){
		return new String[][]{
				{"wds_soorder_b",SoorderBVO.class.getName()},
		   	};  
	}
	
	
	public String getBodyContinos(){
		String sub =getInvSub(inv_Pks);
		return " isnull(dr,0)=0 and coalesce(narrangnmu,0)-coalesce(noutnum,0)>0"+//安排数量-出库数量>0
			" and pk_invmandoc in"+sub;
	}
	
	@Override
	public boolean getIsBusinessType() {
		return false;
	}
	public void loadMultiBodyData(String key) throws Exception{
		String[][] codes = getMultiTableCode();
		if(codes!=null && codes.length>0){
			for(int i = 0 ;i<codes.length;i++){
				String[] tablecodes = codes[i];
				String tableCode = tablecodes[0];
				String name = tablecodes[1];
				SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
				getbillListPanel().setBodyValueVO(tableCode, supervos);
				getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
			}
		}
	}
	
	public void loadMultiBodyData(String tableCode,String key,String name) throws Exception{
		if(tableCode != null && key != null && name != null 
				&& !"".equals(tableCode) && !"".equals(key) && !"".equals(name) ){
			SuperVO[] supervos = HYPubBO_Client.queryByCondition(Class.forName(name), " pk_soorder='" + key + "' and "+getBodyContinos());
			getbillListPanel().setBodyValueVO(tableCode, supervos);
			getbillListPanel().getBodyBillModel(tableCode).execLoadFormula();
		}
	}
	
	@Override
	protected synchronized void headRowChange(int iNewRow) {
		try{
			if (getbillListPanel().getHeadBillModel().getValueAt(iNewRow,getpkField()) != null) {
				String[][] codes = getMultiTableCode();
				if(codes != null && codes.length>0){
					// 获得主表ID
					String key = getbillListPanel().getHeadBillModel().getValueAt(iNewRow,getpkField()).toString();
					for(int i = 0 ;i < codes.length;i++){
						String[] tablecodes = codes[i];
						String tableCode = tablecodes[0];
						String name = tablecodes[1];
						if (!getbillListPanel().setBodyModelData(tableCode,iNewRow)) {
							// 1.初次载入表体数据
							loadMultiBodyData(tableCode,key,name);
							// 2.备份到模型中
							getbillListPanel().setBodyModelDataCopy(tableCode,iNewRow);
						}
					}
				}
			}
			getbillListPanel().repaint();
		} catch (Exception e) {
			Logger.error(e.getMessage()+"表体数据加载失败!", e);
		}
	}
}
