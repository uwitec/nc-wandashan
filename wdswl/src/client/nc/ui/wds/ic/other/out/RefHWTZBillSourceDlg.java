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
 * @author Administrator �������� ���� ��λ��������HWTZ��
 */
public class RefHWTZBillSourceDlg extends MBillSourceDLG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sql = null;
	private LoginInforHelper helper = null;
	private boolean isStock = false; // �Ƿ����ܲ� true=�� false=��

	private String m_logUser = null;

	private String pk_stock = null; // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
	private String[] pk_cargdoc = null;// �󶨻�λ
	private int iType = -1;

	private String[] inv_Pks = null;// ���ݵ�ǰ��¼�߲�ѯ�����ֿ����ֿ����洢�Ĳ�Ʒ

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
			pk_stock = getLoginInforHelper().getWhidByUser(m_logUser); // ��ǰ��¼�߶�Ӧ�Ĳֿ�����
			if (pk_stock == null || "".equalsIgnoreCase(pk_stock)) {
				throw new BusinessException("��ǰ��¼��Աû�а󶨲ֿ�");
			}
			pk_cargdoc = getLoginInforHelper().getSpaceByLogUser(m_logUser);// �󶨻�λ
			if (pk_cargdoc == null || pk_cargdoc.length == 0) {
				throw new BusinessException("��ǰ��¼��Աû�а󶨻�λ");
			} else if (pk_cargdoc.length > 1) {
				throw new BusinessException("��ǰ��¼��Ա���˶����λ");
			}
			isStock = WdsWlPubTool.isZc(getLoginInforHelper().getCwhid(
					m_logUser));// �Ƿ����ܲ�

		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public String getHeadCondition() {

		return "  coalesce(wds_transfer_b.noutnum,0)-coalesce(wds_transfer_b.nacceptnum,0)>0 "// ��������-��������>0
			//	+ " and wds_transfer.vbillstatus = 1 "
		        +" and coalesce(wds_transfer.fisended,'N')='Y' "//�����Ļ�λ������
				+ " and wds_transfer.pk_billtype = '"
				+ WdsWlPubConst.HWTZ
				+ "' "
				+ " and wds_transfer.pk_cargdoc in "
				+ WdsWlPubTool.getSubSql(pk_cargdoc)// ������λ
				+ " and wds_transfer_b.cinventoryid in (" + getPowerSql() + ")";

	}

	public String getBodyContinos() {
		return " isnull(wds_transfer_b.dr,0)=0 and coalesce(wds_transfer_b.noutnum,0)-coalesce(wds_transfer_b.nacceptnum,0)>0"
				// ��������-��������>0
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
