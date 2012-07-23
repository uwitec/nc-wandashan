package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wdsnew.pub.MBillSourceDLG;
import nc.ui.wdsnew.pub.PowerGetTool;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * modify by yf 2012-07-16 继承MBillSourceDLG
 * @author Administrator 其他出库 参照 采购取样（WDSC）
 */
public class RefWDSCBillSourceDlg extends MBillSourceDLG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isStock = false; // 是否是总仓 true=是 false=否

	private String m_logUser = null;

	private String pk_stock = null; // 当前登录者对应的仓库主键

	private int iType = -1;

	private String[] inv_Pks = null;// 根据当前登录者查询所属仓库和其仓库所存储的产品

	private LoginInforHelper helper = null;

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		init();
	}

	public RefWDSCBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);
		init();
	}

	public void init() {
		try {
			m_logUser = ClientEnvironment.getInstance().getUser()
					.getPrimaryKey();
			pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // 当前登录者对应的仓库主键
			if (pk_stock == null || "".equalsIgnoreCase(pk_stock)) {
				throw new BusinessException("当前登录人员没有绑定仓库");
			}
			inv_Pks = getLoginInforHelper().getInvBasDocIDsByUserID(m_logUser);
			if (inv_Pks == null || inv_Pks.length == 0) {
				throw new BusinessException("当前登录人员货位下没有绑定存货");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(
					m_logUser));// 是否是总仓
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "参照采购取样";
	}

	// 获得权限过滤的sql
	String sql = null;

	private String getPowerSql() {
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

	@Override
	public String getHeadCondition() {
		StringBuffer hsql = new StringBuffer();
		// 表单参照交换vo添加pk_corp
		hsql.append(" wds_cgqy_h.pk_corp = '" + getPkCorp() + "' and");

		hsql
				.append(" isnull(wds_cgqy_h.dr,0)=0 and wds_cgqy_h.vbillstatus =1 ");
		if (!isStock) {
			// wds_cgqy_h.reserve3是仓库
			// hsql.append("and wds_cgqy_h.pk_outwhouse='"+pk_stock+"'");//分仓只能看到自己的，总仓可以看到总仓+分仓的
			hsql.append(" and wds_cgqy_h.reserve3='" + pk_stock + "'");// 分仓只能看到自己的，总仓可以看到总仓+分仓的
		}
		hsql
				.append(" and isnull(wds_cgqy_b.dr,0)=0 and coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"
						+ // 安排数量-出库数量>0
						" and pk_invmandoc in (" + getPowerSql() + ")");

		return hsql.toString();

	}

	@Override
	public String getBodyCondition() {
		return " isnull(wds_cgqy_b.dr,0)=0 and coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"
				+ // 安排数量-出库数量>0
				" and pk_invmandoc in (" + getPowerSql() + ")";
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
	public boolean getIsBusinessType() {
		return false;
	}

	@Override
	public String getPk_invbasdocName() {
		// TODO Auto-generated method stub
		return "pk_invbasdoc";
	}

	@Override
	public String getPk_invmandocName() {
		// TODO Auto-generated method stub
		return "pk_invmandoc";
	}

	@Override
	public IControllerBase getUIController() {
		// TODO Auto-generated method stub
		return new nc.ui.wds.ie.cgqy.ClientController();
	}

}
