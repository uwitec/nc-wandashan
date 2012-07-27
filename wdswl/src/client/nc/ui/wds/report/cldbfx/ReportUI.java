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
 * 存量对比分析
 * 
 * @author yf
 * @date 2012-07-27
 * 
 *       新增节点 注册到系统 仓库管理 -- 统计分析 下 仅是个查询 显示 界面 （可按报表模式做 也可做成 单但表头列表界面 或 单表体列表界面）
 *       功能按钮：查询 打印
 *       查询条件：仓库pk_stordoc、货位pk_cargdoc、存货分类pk_invcl、存货pk_invmandoc,pk_invbasdoc
 *       公司pk_corp=当前公司 数据项：erp库存
 *       nerpstornum、物流库存nwlstornum、虚拟订单剩余量nwlxnddnum、虚拟运单剩余量nwlxnydnum
 *       、erp采购入库量nerpcgrknum 查询实现要求：每一个数据项的查询对应一个 查询类 最后将各个项 拼装在一个vo
 */
public class ReportUI extends ZmReportBaseUI2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8238947662610154941L;

	private String[] voCombinConds = new String[] { "pk_corp" };// 合并条件,拆单维度
	private String[] combinFields = new String[] { "nerpstornum", "nwlstornum",
			"nwlxnddnum", "nwlxnydnum", "nerpcgrknum" };// erp库存
	// nerpstornum、物流库存nwlstornum、虚拟订单剩余量nwlxnddnum、虚拟运单剩余量nwlxnydnum
	private String splitBillField = "pk_invmandoc";// 拆单分裂条件字段
	private SqlFactory sf = null;

	@Override
	public void onQuery() {
		setDefaultQueryData();
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// 清空表体数据
				clearBody();
				// 设置查询的动态列
				setDynamicColumn1();
				// 设置基本列合并
				setColumn();
				// 设置vo
				List<ReportBaseVO[]> list = getReportVO(getSqls());
				ReportBaseVO[] vos = null;
				vos = combinListVOs(list);
				vos = getCustomVOs(vos);// yf add自定义 改造vos
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
	 * 将同一维度 拥有相同特性 的 数组 合并为一条数据 按特性不同 增加相应列
	 * 
	 * @param vos
	 * @param fields
	 *            分组条件：确定维度, 合并条件
	 * @return
	 */
	private ReportBaseVO[] setVoByContion(ReportBaseVO[] vos, String[] fields) {
		if (vos == null || vos.length == 0) {
			return vos;
		}
		// 根据 分组字段，对原vo进行分组
		CircularlyAccessibleValueObject[][] voss = SplitBillVOs.getSplitVOs(
				vos, fields);
		if (voss == null || voss.length == 0) {
			return vos;
		}
		// new 开头的vo为重新组装放入界面的vo
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
				// 根据单据类型重组： 将分组后的字段 重新按 字段 进行横向分裂合并
				setVOByBillType(newVo, oldVo);
			}
			newVos[i] = newVo;
		}
		return newVos;
	}

	/**
	 * 根据单据类型 对vo进行横向分裂：同一存货 （不同单据类型 的 数量 金额） 合并为一条数据
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
	 * erp库存 nerpstornum
	 */
	public String getSql1() throws Exception {
		return getSqlFactory().getSqlFunction(SqlFactory.nerpstornum).getSql();
	}

	/*
	 * 物流库存nwlstornum
	 */
	public String getSql2() {
		return null;

	}

	/*
	 * 虚拟订单剩余量nwlxnddnum
	 */
	public String getSql3() {
		return null;

	}

	/*
	 * 虚拟运单剩余量nwlxnydnum
	 */
	public String getSql4() {
		return null;

	}

	/*
	 * erp采购入库量nerpcgrknum
	 */
	public String getSql5() {
		return null;

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report_cldbfx_node;
	}

}
