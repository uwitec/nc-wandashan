package nc.ui.wds.report.cldbfx;

import java.util.Iterator;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.CombinVO;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI2;

/**
 * �����Աȷ���
 * 
 * @author yf
 * @date 2012-07-27
 * 
 *       �����ڵ� ע�ᵽϵͳ �ֿ���� -- ͳ�Ʒ��� �� ���Ǹ���ѯ ��ʾ ���� ���ɰ�����ģʽ�� Ҳ������ ������ͷ�б���� �� �������б���棩
 *       ���ܰ�ť����ѯ ��ӡ
 *       ��ѯ�������ֿ�pk_stordoc����λpk_cargdoc���������pk_invcl�����pk_invmandoc,pk_invbasdoc
 *       ��˾pk_corp=��ǰ��˾ �����erp���
 *       nerpstornum���������nwlstornum�����ⶩ��ʣ����nwlxnddnum�������˵�ʣ����nwlxnydnum
 *       ��erp�ɹ������nerpcgrknum ��ѯʵ��Ҫ��ÿһ��������Ĳ�ѯ��Ӧһ�� ��ѯ�� ��󽫸����� ƴװ��һ��vo
 */
public class ReportUI extends ZmReportBaseUI2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8238947662610154941L;

	private String[] voCombinConds = new String[] { "pk_corp", "pk_stordoc",
	// "pk_cargdoc",
			"pk_invcl", "pk_invmandoc" };// �ϲ�����,��ά��
	private String[] combinFields = new String[] { "nerpstornum", "nwlstornum",
			"nwlxnddnum", "nwlxnydnum", "nerpcgrknum" };// erp���
	// nerpstornum���������nwlstornum�����ⶩ��ʣ����nwlxnddnum�������˵�ʣ����nwlxnydnum
	private String splitBillField = "pk_invmandoc";// �𵥷��������ֶ�
	private SqlFactory sf = null;

	@Override
	public void onQuery() {
		setDefaultQueryData();
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// ��ձ�������
				clearBody();
				// ���ò�ѯ�Ķ�̬��
				setDynamicColumn1();
				// ���û����кϲ�
				setColumn();
				// ����vo
				List<ReportBaseVO[]> list = getReportVO(getSqls());
				ReportBaseVO[] vos = null;
				vos = combinListVOs(list);
				vos = getCustomVOs(vos);// yf add�Զ��� ����vos
				if (vos != null) {
					super.updateBodyDigits();
					setBodyVO(vos);
					// setTolal1();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				this.showErrorMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				this.showErrorMessage(e.getMessage());
			}
		}

	}

	private ReportBaseVO[] combinListVOs(List<ReportBaseVO[]> list)
			throws Exception {
		if (list == null || list.size() == 0) {
			return null;
		}
		ReportBaseVO[] vos = null;
		Iterator<ReportBaseVO[]> it = list.iterator();
		ReportBaseVO[] combinvo = null;
		while (it.hasNext()) {
			vos = (ReportBaseVO[]) ObjectUtils.serializableClone(it.next());
			combinvo = CombinVO.comin(combinvo, vos);
		}
		return combinvo;
	}

	protected ReportBaseVO[] getCustomVOs(ReportBaseVO[] vos) {
		ReportBaseVO[] newvos = (ReportBaseVO[]) CombinVO.combinData(vos,
				voCombinConds, combinFields, ReportBaseVO.class);
		return newvos;
	}

	protected void setDefaultQueryData() {
		getQueryDlg().setDefaultValue("pk_corp", getCorpPrimaryKey(), "");
	}

	public SqlFactory getSqlFactory() {
		if (sf == null) {
			sf = new SqlFactory().getInstance();
		}
		return sf;
	}

	@Override
	public String[] getSqls() throws Exception {
		getSqlFactory().setQueryDlg(getQueryDlg());
		return new String[] { getSql1(), getSql2(), getSql4() };
	}

	/*
	 * erp��� nerpstornum
	 */
	public String getSql1() throws Exception {
		return getSqlFactory().getSqlFunction(SqlFactory.N_nerpstornum)
				.getSql();
	}

	/*
	 * �������nwlstornum
	 */
	public String getSql2() throws Exception {
		return getSqlFactory().getSqlFunction(SqlFactory.N_nwlstornum).getSql();

	}

	/*
	 * ���ⶩ��ʣ����nwlxnddnum
	 */
	public String getSql3() {
		return null;

	}

	/*
	 * �����˵�ʣ����nwlxnydnum
	 */
	public String getSql4() throws Exception {
		return getSqlFactory().getSqlFunction(SqlFactory.N_nwlxnydnum).getSql();
	}

	/*
	 * erp�ɹ������nerpcgrknum
	 */
	public String getSql5() {
		return null;

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report_cldbfx_node;
	}

	public int[] getReportButtonAry() {
		m_buttonArray = new int[] { IReportButton.QueryBtn,
				IReportButton.LevelSubTotalBtn, IReportButton.PrintBtn, };
		return m_buttonArray;
	}

	@Override
	protected void setDecimalDigits() {
	}
}
