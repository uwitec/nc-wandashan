package nc.ui.wds.pub.report2;
import java.util.Vector;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.wl.pub.report.IReportVO;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * 工具类：用于合并VO
 */
public class CombinVOTool {
	// 合并字段。
	private String[] m_combinFields = null;
	// 求值字段
	private String[] m_valueFields = null;
	// 求值字段类型。
	private int[] m_iTypes = null;
	// 调整VO的工具。
	private IAdjustCombinVOTool m_adjustTool;
	// 是否加合并数据的明细.
	private boolean isAddDetail = true;
	// 分组列是否可为空。
	private boolean m_isGroupFldCanNull = false;
	/**
	 * CombinVOTool 构造子注解。
	 */
	public CombinVOTool(IAdjustCombinVOTool adjustTool) {
		super();
		m_adjustTool = adjustTool;
	}

	private void addDetail(Vector v, IReportVO vo) {
		v.add(vo);
	}

	// 将vo1的数据加到voResult.
	private void combinValue(CircularlyAccessibleValueObject vo1,
			CircularlyAccessibleValueObject voResult) {
		for (int i = 0; i < m_valueFields.length; i++) {
			Object resultobj = voResult.getAttributeValue(m_valueFields[i]);
			Object tmpobj = vo1.getAttributeValue(m_valueFields[i]);
			switch (m_iTypes[i]) {
			case IUFTypes.INT:
				int iresult = (resultobj == null ? 0 : ((Integer) resultobj).intValue());
				int itmp = (tmpobj == null ? 0 : ((Integer) tmpobj).intValue());
				voResult.setAttributeValue(m_valueFields[i], new Integer(iresult + itmp));
				continue;
			case IUFTypes.LONG:
				long lgtmp = (tmpobj == null ? 0 : ((Long) tmpobj).longValue());
				long lgresult = (resultobj == null ? 0 : ((Long) resultobj).longValue());
				if (tmpobj != null)
					voResult.setAttributeValue(m_valueFields[i], new Long(lgresult + lgtmp));
				continue;
			case IUFTypes.UFD:
				UFDouble ufdtmp = (tmpobj == null ? new UFDouble("0"): (UFDouble) tmpobj);
				UFDouble ufdResult = (resultobj == null ? new UFDouble("0"): (UFDouble) resultobj);
				voResult.setAttributeValue(m_valueFields[i], ufdResult.add(ufdtmp));
				continue;
			case IUFTypes.STR:
				String strtmp = (tmpobj == null ? "" : tmpobj.toString());
				String strresult = (resultobj == null ? "" : resultobj.toString());
				voResult.setAttributeValue(m_valueFields[i], strtmp + strresult);
				continue;
			}
		}
	}
	public CircularlyAccessibleValueObject[] combinVO(
			CircularlyAccessibleValueObject[] vosSource, SubtotalVO voSubtotal) {
		if (vosSource == null || voSubtotal == null)
			return null;
		// sort vo by group fields.
		SimpleSortVOTool sortTool = new SimpleSortVOTool();
		sortTool.sortVO(vosSource, voSubtotal.getGroupFlds(), false);
		// copy data to this instance.
		m_combinFields = voSubtotal.getGroupFlds();
		m_valueFields = voSubtotal.getValueFlds();
		m_iTypes = voSubtotal.getValueFldTypes();
		m_isGroupFldCanNull = voSubtotal.isGroupFldCanNUll();
		boolean bAsInitRs = (m_combinFields == null ? false : voSubtotal
				.isAsLeafRs()[m_combinFields.length - 1]);
		// result Container.
		Vector vResult = new Vector();
		// clone the source data.
		CircularlyAccessibleValueObject[] vos = new CircularlyAccessibleValueObject[vosSource.length];
		for (int i = 0; i < vos.length; i++)
			vos[i] = (CircularlyAccessibleValueObject) vosSource[i].clone();
		// combin Data.
		for (int i = 0; i < vos.length; i++) {
			// detail Vector.
			Vector vDetail = new Vector();
			if (!verifyVO(vos[i]))
				continue;
			// get the first VO should be combined.
			CircularlyAccessibleValueObject voBak = (CircularlyAccessibleValueObject) vos[i]
					.clone();
			// adjust detail VO.
			if (m_adjustTool != null)
				m_adjustTool.adjustDetailVO(voBak);
			if (voBak instanceof IReportVO)
				addDetail(vDetail, (IReportVO) vos[i]);
			// combin data from the first VO to the last same one.
			for (int j = i + 1; j < vos.length; j++) {
				CircularlyAccessibleValueObject vo1 = vos[j];
				if (!verifyVO(vos[j])) {
					i++;
					continue;
				}
				if (!isSameItem(vos[j], voBak))
					break;
				combinValue(vos[j], voBak);
				if (vo1 instanceof IReportVO)
					addDetail(vDetail, (IReportVO) vos[j]);
				if (m_adjustTool != null)
					m_adjustTool.adjustDetailVO(vos[j]);
				i++;
			} // add detail to the combined vo.
			if (voBak instanceof IReportVO) {
				((IReportVO) voBak).setDetailVO(vDetail);
				((IReportVO) voBak).setCombined(!bAsInitRs);
			}
			if (m_adjustTool != null)
				m_adjustTool.adjustCombinedVO(voBak, voSubtotal);
			vResult.add(voBak);
		} // convert to Array. and return result.
		CircularlyAccessibleValueObject[] vosResult = new CircularlyAccessibleValueObject[vResult
				.size()];
		vResult.copyInto(vosResult);
		return vosResult;
	}

	private boolean isSameItem(CircularlyAccessibleValueObject vo1,
			CircularlyAccessibleValueObject vo) {
		if (m_combinFields == null || m_combinFields.length < 1)
			return true;
		for (int i = 0; i < m_combinFields.length; i++) {
			if (vo1.getAttributeValue(m_combinFields[i]) != null
					&& vo.getAttributeValue(m_combinFields[i]) == null)
				return false;
			if (vo1.getAttributeValue(m_combinFields[i]) == null
					&& vo.getAttributeValue(m_combinFields[i]) != null)
				return false;
			if (vo1.getAttributeValue(m_combinFields[i]) == null
					&& vo.getAttributeValue(m_combinFields[i]) == null)
				continue;
			if (!vo1.getAttributeValue(m_combinFields[i]).equals(
					vo.getAttributeValue(m_combinFields[i]))) {
				return false;
			}
		}
		return true;
	}

	private boolean verifyVO(CircularlyAccessibleValueObject vo) {
		if (m_combinFields == null || m_combinFields.length < 1)
			return true;
		if (m_isGroupFldCanNull)
			return true;
		for (int i = 0; i < m_combinFields.length; i++) {
			if (vo.getAttributeValue(m_combinFields[i]) == null) {
				return false;
			}
		}
		return true;
	}
}
