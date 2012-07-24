package nc.ui.wds.ic.other.out;

import java.awt.Container;

import nc.bs.logging.Logger;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wdsnew.pub.MBillSourceDLG;
import nc.ui.wdsnew.pub.PowerGetTool;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author Administrator 其他出库 参照 货位调整单（HWTZ）
 */
public class RefHWTZBillSourceDlg extends MBillSourceDLG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sql = null;
	private LoginInforHelper helper = null;
	private boolean isStock = false; // 是否是总仓 true=是 false=否

	private String m_logUser = null;

	private String pk_stock = null; // 当前登录者对应的仓库主键
	private String[] pk_cargdoc = null;// 绑定货位
	private int iType = -1;

	private String[] inv_Pks = null;// 根据当前登录者查询所属仓库和其仓库所存储的产品

	public LoginInforHelper getLoginInforHelper() {
		if (helper == null) {
			helper = new LoginInforHelper();
		}
		return helper;
	}

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

	public RefHWTZBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, parent);
		setSpiltFields(new String[] { "srl_pk" });
		init();
	}

	public RefHWTZBillSourceDlg(String pkField, String pkCorp, String operator,
			String funNode, String queryWhere, String billType,
			String businessType, String templateId, String currentBillType,
			String nodeKey, Object userObj, Container parent) {
		super(pkField, pkCorp, operator, funNode, queryWhere, billType,
				businessType, templateId, currentBillType, nodeKey, userObj,
				parent);
		setSpiltFields(new String[] { "srl_pk" });
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
			pk_cargdoc = getLoginInforHelper().getSpaceByLogUser(m_logUser);// 绑定货位
			if (pk_cargdoc == null || pk_cargdoc.length == 0) {
				throw new BusinessException("当前登录人员没有绑定货位");
			} else if (pk_cargdoc.length > 1) {
				throw new BusinessException("当前登录人员绑定了多个货位");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(
					m_logUser));// 是否是总仓

		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public String getHeadCondition() {

		return "  coalesce(wds_transfer_b.noutnum,0)-coalesce(wds_transfer_b.nacceptnum,0)>0 "// 安排数量-出库数量>0
			//	+ " and wds_transfer.vbillstatus = 1 "
		        +" and coalesce(wds_transfer.fisended,'N')='Y' "//冻结后的货位调整单
				+ " and wds_transfer.pk_billtype = '"
				+ WdsWlPubConst.HWTZ
				+ "' "
				+ " and wds_transfer.pk_cargdoc in "
				+ WdsWlPubTool.getSubSql(pk_cargdoc)// 调出货位
				+ " and wds_transfer_b.cinventoryid in (" + getPowerSql() + ")";

	}

	public String getBodyContinos() {
		return " isnull(wds_transfer_b.dr,0)=0 and coalesce(wds_transfer_b.noutnum,0)-coalesce(wds_transfer_b.nacceptnum,0)>0"
				// 安排数量-出库数量>0
				+ " and cinventoryid in (" + getPowerSql() + ")";
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
		return new nc.ui.wds.ic.transfer.ClientUICtrl(WdsWlPubConst.HWTZ);
	}
}
