package nc.ui.wds.report.cldbfx;

import java.util.Iterator;
import java.util.List;

import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
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

	private String[] voCombinConds = new String[] { "pk_corp" };// �ϲ�����,��ά��
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
		ReportBaseVO[] newvos = setVoByContion(vos, voCombinConds);
		return newvos;
	}

	/**
	 * ��ͬһά�� ӵ����ͬ���� �� ���� �ϲ�Ϊһ������ �����Բ�ͬ ������Ӧ��
	 * 
	 * @param vos
	 * @param fields
	 *            ����������ȷ��ά��, �ϲ�����
	 * @return
	 */
	private ReportBaseVO[] setVoByContion(ReportBaseVO[] vos, String[] fields) {
		if (vos == null || vos.length == 0) {
			return vos;
		}
		// ���� �����ֶΣ���ԭvo���з���
		CircularlyAccessibleValueObject[][] voss = SplitBillVOs.getSplitVOs(
				vos, fields);
		if (voss == null || voss.length == 0) {
			return vos;
		}
		// new ��ͷ��voΪ������װ��������vo
		ReportBaseVO[] newVos = new ReportBaseVO[voss.length];
		int size = voss.length;
		for (int i = 0; i < size; i++) {
			ReportBaseVO newVo = null;
			int size1 = voss[i].length;
			for (int j = 0; j < size1; j++) {
				ReportBaseVO oldVo = (ReportBaseVO) voss[i][j];
				if (newVo == null) {
					newVo = (ReportBaseVO) oldVo.clone();
				}
				// ���ݵ����������飺 ���������ֶ� ���°� �ֶ� ���к�����Ѻϲ�
				setVOByBillType(newVo, oldVo);
			}
			newVos[i] = newVo;
		}
		return newVos;
	}

	/**
	 * ���ݵ������� ��vo���к�����ѣ�ͬһ��� ����ͬ�������� �� ���� �� �ϲ�Ϊһ������
	 * 
	 * @param newVo
	 * @param oldVo
	 */
	private void setVOByBillType(ReportBaseVO newVo, ReportBaseVO oldVo) {
		String id = PuPubVO.getString_TrimZeroLenAsNull(oldVo
				.getAttributeValue(splitBillField));
		String newid = PuPubVO.getString_TrimZeroLenAsNull(newVo
				.getAttributeValue(splitBillField));
		if (id == null || !id.equalsIgnoreCase(newid)) {
			return;
		}

		int size = combinFields.length;
		UFDouble value = null;
		UFDouble value2 = null;
		for (int i = 0; i < size; i++) {
			value = PuPubVO.getUFDouble_NullAsZero(oldVo
					.getAttributeValue(combinFields[i]));
			value2 = PuPubVO.getUFDouble_NullAsZero(newVo
					.getAttributeValue(combinFields[i]));//
			newVo.setAttributeValue(combinFields[i], value.add(value2));
		}
	}

	protected void setDefaultQueryData() {

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
		return new String[] { getSql1() };
	}

	/*
	 * erp��� nerpstornum
	 */
	public String getSql1() throws Exception {
		return getSqlFactory().getSqlFunction(SqlFactory.nerpstornum).getSql();
	}

	/*
	 * �������nwlstornum
	 */
	public String getSql2() {
		return null;

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
	public String getSql4() {
		return null;

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

}
