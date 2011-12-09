package nc.ui.wds.report.unusenum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ZmReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;

/**
 * 
 * @author yf ���˹���-����ͳ��-��������ѯ���� ������Ϣ����˾���ֿ⣬��������۶���ռ���������˶���ռ������������
 * 
 */
public class ReportUI extends ZmReportBaseUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 615146412290753102L;
	private final static String splitBillField = "billtype";
	private final static String[] combin_types = new String[] { "so", "send",
			"sum" };
	private final static String[] combin_fields = new String[] { "nonsonum",
			"nonsendnum", "nsumnum" };
	private final static String[] combin_fields2 = new String[] {
			"nassonsonum", "nassonsendnum", "nasssumnum" };
	private static String[] voCombinConds = new String[] { "pk_corp",
			"pk_stordoc", "pk_invmandoc" };// �ϲ�����,��ά��

	int fisdate = 0;//0ȫ����ѯ��1�����ڲ�ѯ��2�ϸ�ʹ����ѯ

	@Override
	public Map getNewItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		// ���۶���ռ�����������˶���ռ���������������
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql
				.append(" '"
						+ combin_types[2]
						+ "' "
						+ splitBillField
						+ ",pk_corp,pk_customize1 pk_stordoc,pk_invmandoc,sum(whs_stocktonnage) nnum,sum(whs_stockpieces) nassnum");
		sql.append(" from ");
		sql.append(" tb_warehousestock ");
		sql.append(" where ");
		sql.append(" isnull(dr,0) = 0 ");
		// sql.append(" and pk_corp = '"+corp+"'");
		// sql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		// sql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		if (PuPubVO.getString_TrimZeroLenAsNull(getWhereSql(combin_types[2])) != null) {
			sql.append(" and " + getWhereSql(combin_types[2]));
		}
		// �Ƿ񰴴����ڲ�ѯ
		if (fisdate == 0) {
			sql.append(" and ss_pk in('" + WdsWlPubConst.WDS_STORSTATE_PK_hg
					+ "','" + WdsWlPubConst.WDS_STORSTATE_PK_dj + "','"
					+ WdsWlPubConst.WDS_STORSTATE_PK + "')");
		} else if (fisdate == 1) {
			sql.append(" and ss_pk='" + WdsWlPubConst.WDS_STORSTATE_PK + "'");
		} else {
			sql.append(" and ss_pk in('" + WdsWlPubConst.WDS_STORSTATE_PK_hg
					+ "','" + WdsWlPubConst.WDS_STORSTATE_PK_dj + "')");
		}
		sql.append(" group by ");
		sql.append(" pk_corp,pk_customize1,pk_invmandoc ");
		sql.append(" order by ");
		sql.append(" pk_corp,pk_customize1,pk_invmandoc ");
		return sql.toString();
	}

	private Object getWhereSql(String type) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = getFilterCondition(vos, type);
		return querylg.getWhereSQL(vos1);
	}

	private Object getWhereSql() throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = (ConditionVO[]) ObjectUtils
				.serializableClone(querylg.getConditionVO());// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = getFilterCondition(vos, null);
		return querylg.getWhereSQL(vos1);
	}

	private ConditionVO[] getFilterCondition(ConditionVO[] vos, String type) {
		List<ConditionVO> list = new ArrayList<ConditionVO>();
		if (vos == null || vos.length == 0)
			return null;
		for (int i = 0; i < vos.length; i++) {
			if (vos[i].getFieldCode().equals("fisdate")) {
				fisdate = PuPubVO.getInteger_NullAs(vos[i].getComboIndex(), 0);
				continue;
			}
			if (vos[i].getFieldCode().equals("pk_stordoc")) {
				if (combin_types[2].equals(type)) {
					vos[i].setFieldCode("pk_customize1");
				}
				if (combin_types[0].equals(type)
						|| combin_types[1].equals(type)) {
					vos[i].setFieldCode("pk_outwhouse");
				}
			}
			list.add(vos[i]);
		}
		if (list.size() == 0)
			return null;
		return list.toArray(new ConditionVO[0]);
	}

	public String getQuerySQL2() throws Exception {
		// ���۶���ռ�����������˶���ռ���������������
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql
				.append(" '"
						+ combin_types[1]
						+ "' "
						+ splitBillField
						+ ",h.pk_corp,h.pk_outwhouse pk_stordoc,b.pk_invmandoc,sum(coalesce(b.ndealnum,0.0)-coalesce(b.noutnum,0)) nnum,sum(coalesce(b.nassdealnum,0.0)-coalesce(b.nassoutnum,0.0)) nassnum");
		sql.append(" from ");
		sql.append(" wds_sendorder h,wds_sendorder_b b");
		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0)=0 ");
		sql.append(" and h.pk_sendorder = b.pk_sendorder ");
		sql.append(" and h.vbillstatus=" + IBillStatus.FREE);// ����
		// sql.append(" and pk_corp = '"+corp+"'");
		// sql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		// sql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		if (PuPubVO.getString_TrimZeroLenAsNull(getWhereSql(combin_types[1])) != null) {
			sql.append(" and " + getWhereSql(combin_types[1]));
		}
		// �Ƿ񰴴����ڲ�ѯ
		if (fisdate == 0) {

		} else if (fisdate == 1) {
			sql.append(" and bisdate = 'Y' ");
		} else {
			sql.append(" and coalesce(bisdate,'N') = 'N'");
		}
		sql.append(" group by ");
		sql.append(" h.pk_corp,h.pk_outwhouse,b.pk_invmandoc ");
		sql.append(" order by ");
		sql.append(" h.pk_corp,h.pk_outwhouse,b.pk_invmandoc ");
		return sql.toString();
	}

	public String getQuerySQL3() throws Exception {
		// ���۶���ռ�����������˶���ռ���������������
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql
				.append(" '"
						+ combin_types[0]
						+ "' "
						+ splitBillField
						+ ",h.pk_corp,h.pk_outwhouse pk_stordoc,b.pk_invmandoc,sum(coalesce(b.narrangnmu,0.0)-coalesce(b.noutnum,0)) nnum,sum(coalesce(b.nassarrangnum,0.0)-coalesce(b.nassoutnum,0.0)) nassnum");
		sql.append(" from ");
		sql.append(" wds_soorder h,wds_soorder_b b");
		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0)=0 ");
		sql.append(" and h.pk_soorder = b.pk_soorder ");
		sql.append(" and h.vbillstatus=" + IBillStatus.FREE);// ����
		// sql.append(" and pk_corp = '"+corp+"'");
		// sql.append(" and pk_customize1 = '"+cwarehouseid+"'");
		// sql.append(" and pk_invbasdoc = '"+cinvbasid+"'");
		if (PuPubVO.getString_TrimZeroLenAsNull(getWhereSql(combin_types[0])) != null) {
			sql.append(" and " + getWhereSql(combin_types[0]));
		}
		// �Ƿ񰴴����ڲ�ѯ
		if (fisdate == 0) {

		} else if (fisdate == 1) {
			sql.append(" and bisdate = 'Y' ");
		} else {
			sql.append(" and coalesce(bisdate,'N') = 'N'");
		}
		sql.append(" group by ");
		sql.append(" h.pk_corp,h.pk_outwhouse,b.pk_invmandoc ");
		sql.append(" order by ");
		sql.append(" h.pk_corp,h.pk_outwhouse,b.pk_invmandoc ");
		return sql.toString();
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// ��ձ�������
				clearBody();
				// �õ���ѯ���
				List<ReportBaseVO[]> list = getReportVO(new String[] {
						getQuerySQL(), getQuerySQL2(), getQuerySQL3() });
				ReportBaseVO[] vos = null;
				vos = combinListVOs(list);
				if (vos == null || vos.length == 0)
					return;
				if (vos != null) {
					super.updateBodyDigits();
					setBodyVO(vos);
					setTolal();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private ReportBaseVO[] combinListVOs(List<ReportBaseVO[]> list)
			throws Exception {
		if (list == null || list.size() == 0) {
			return null;
		}
		ReportBaseVO[] vos1 = null;
		ReportBaseVO[] vos2 = null;
		ReportBaseVO[] vos3 = null;
		if (list.get(0) != null || list.get(0).length > 0) {
			vos1 = list.get(0);
		}
		if (list.get(1) != null || list.get(1).length > 0) {
			vos2 = list.get(1);
		}
		if (list.get(2) != null || list.get(2).length > 0) {
			vos3 = list.get(2);
		}
		ReportBaseVO[] combinvo = null;
		combinvo = CombinVO.comin(combinvo, vos1);
		combinvo = CombinVO.comin(combinvo, vos2);
		combinvo = CombinVO.comin(combinvo, vos3);
		return setVoByContion(combinvo, voCombinConds);
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
		String billtype = PuPubVO.getString_TrimZeroLenAsNull(oldVo
				.getAttributeValue(splitBillField));
		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(oldVo
				.getAttributeValue("nnum"));
		UFDouble nmny = PuPubVO.getUFDouble_NullAsZero(oldVo
				.getAttributeValue("nassnum"));
		setVOByBillType(newVo, billtype, nnum, nmny);
	}

	private void setVOByBillType(ReportBaseVO newVo, String billtype,
			UFDouble nnum, UFDouble nmny) {
		if (combin_types[0].equals(billtype)) {
			newVo.setAttributeValue(combin_fields[0], nnum);
			newVo.setAttributeValue(combin_fields2[0], nmny);
		}
		if (combin_types[1].equals(billtype)) {
			newVo.setAttributeValue(combin_fields[1], nnum);
			newVo.setAttributeValue(combin_fields2[1], nmny);
		}
		if (combin_types[2].equals(billtype)) {
			newVo.setAttributeValue(combin_fields[2], nnum);
			newVo.setAttributeValue(combin_fields2[2], nmny);
		}

	}

	/**
	 * �����ѯ
	 */
	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService(
					WdsWlPubConst.WDS_WL_MODULENAME, this, "���ڲ�ѯ...", 1,
					"nc.bs.wds.pub.report.ReportDMO", null, "queryVOBySql",
					ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "����", e.getMessage());
		}
		return reportVOs;
	}

	@Override
	public void initReportUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report_unusenum_node;
	}

}
