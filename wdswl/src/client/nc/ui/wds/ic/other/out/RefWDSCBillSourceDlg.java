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
 * modify by yf 2012-07-16 �̳�MBillSourceDLG
 * @author Administrator �������� ���� �ɹ�ȡ����WDSC��
 */
public class RefWDSCBillSourceDlg extends MBillSourceDLG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isStock = false; // �Ƿ����ܲ� true=�� false=��

	private String m_logUser = null;

	private String pk_stock = null; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����

	private int iType = -1;

	private String[] inv_Pks = null;// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ

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
			pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
			if (pk_stock == null || "".equalsIgnoreCase(pk_stock)) {
				throw new BusinessException("��ǰ��¼��Աû�а󶨲ֿ�");
			}
			inv_Pks = getLoginInforHelper().getInvBasDocIDsByUserID(m_logUser);
			if (inv_Pks == null || inv_Pks.length == 0) {
				throw new BusinessException("��ǰ��¼��Ա��λ��û�а󶨴��");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(
					m_logUser));// �Ƿ����ܲ�
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "���ղɹ�ȡ��";
	}

	// ���Ȩ�޹��˵�sql
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
		// �����ս���vo���pk_corp
		hsql.append(" wds_cgqy_h.pk_corp = '" + getPkCorp() + "' and");

		hsql
				.append(" isnull(wds_cgqy_h.dr,0)=0 and wds_cgqy_h.vbillstatus =1 ");
		if (!isStock) {
			// wds_cgqy_h.reserve3�ǲֿ�
			// hsql.append("and wds_cgqy_h.pk_outwhouse='"+pk_stock+"'");//�ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
			hsql.append(" and wds_cgqy_h.reserve3='" + pk_stock + "'");// �ֲ�ֻ�ܿ����Լ��ģ��ֿܲ��Կ����ܲ�+�ֲֵ�
		}
		hsql
				.append(" and isnull(wds_cgqy_b.dr,0)=0 and coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"
						+ // ��������-��������>0
						" and pk_invmandoc in (" + getPowerSql() + ")");

		return hsql.toString();

	}

	@Override
	public String getBodyCondition() {
		return " isnull(wds_cgqy_b.dr,0)=0 and coalesce(wds_cgqy_b.nplannum,0)-coalesce(wds_cgqy_b.noutnum,0)>0"
				+ // ��������-��������>0
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
