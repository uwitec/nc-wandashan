package nc.ui.wds.report.crklsz;

import java.util.List;
import java.util.Map;

import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.ZmReportBaseUI2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wl.pub.report.ReportBaseVO;

/**
 * 
 * @author yf 出入库流水账
 * 
 */
public class ReportUI extends ZmReportBaseUI2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2193523266502400113L;

	// 表头：制单日期、出入库仓库、库管员、来源单据号、单据号、制单人、签字人
	private String[] select_fields_out_h = new String[] { "vbilltype billtype",
			"general_pk pk_bill", "dbilldate dbilldate",
			"srl_pk coutwarehouseid", "cwhsmanagerid psnid",
			"vbillcode vbillcode", "coperatorid coperatorid",
			"cregister cregister" };// 销售出库，其他出库

	private String[] select_fields_in_h = new String[] {
			"geh_billtype billtype", "geh_pk pk_bill",
			"geh_dbilldate dbilldate", "geh_cwarehouseid cinwarehouseid",
			"geh_cwhsmanagerid psnid", "geh_billcode vbillcode",
			"coperatorid coperatorid", "geh_storname cregister" };// 调拨入库,其他入库

	// 表体：货位、托盘、存货、批次、虚拟、出入库数量
	private String[] select_fields_out_b = new String[] {
			"general_b_pk pk_bill_b",
			"cspaceid pk_cargdoc",
			"vfirstbillcode vsourcebillcode",
			// "",//没有托盘信息
			"cinventoryid pk_invmandoc", "vbatchcode vbatchcode",
			"isxnap isxnap", "flargess isgift", "noutnum noutnum",
			"noutassistnum nassoutnum" };// 销售出库,其他出库

	private String[] select_fields_in_b = new String[] { "geb_pk pk_bill_b",
			"geb_space pk_cargdoc", "vfirstbillcode vsourcebillcode",
			// "",//没有托盘信息
			"geb_cinventoryid pk_invmandoc", "geb_vbatchcode vbatchcode",
			// "isxnap isxnap",//没有虚拟属性
			"geb_flargess isgift", "geb_anum ninnum", "geb_banum nassinnum" };// 调拨入库,其他入库

	// 孙表：托盘
	private String[] select_fields_out_bb = new String[] { "cdt_pk" };// 销售出库,其他出库
	private String[] select_fields_in_bb = new String[] { "cdt_pk" };// 销售出库,其他出库

	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	/*
	 * 销售出库
	 */
	private String getQuerySQL1() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		for (int i = 0; i < select_fields_out_h.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("h." + select_fields_out_h[i]);
		}
		sql.append(" , ");
		for (int i = 0; i < select_fields_out_b.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("b." + select_fields_out_b[i]);
		}
		if (getGroupByOrSelectConditon() != null
				&& getGroupByOrSelectConditon().trim().length() > 0) {
			sql.append(" , bb.cdt_pk ");
		}
		sql.append(" from ");
		sql.append(" tb_outgeneral_h h,tb_outgeneral_b b,tb_outgeneral_t bb");

		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0) = 0 ");
		sql.append(" and h.general_pk = b.general_pk ");
		sql.append(" and b.general_b_pk = bb.general_b_pk ");

		if (getQueryConditon(0) != null && getQueryConditon(0).length() != 0) {
			sql.append(" and ");
			sql.append(getQueryConditon(0));
		}
		return sql.toString();
	}

	@Override
	public void setItemsAfter(ReportItem it, List<ReportItem> list) {
		it.setShow(false);
		if (it.getKey().equalsIgnoreCase("cdt_pk")) {
			it
					.setLoadFormula(new String[] { "cdtcode->getColValue(bd_cargdoc_tray,cdt_traycode,cdt_pk,cdt_pk)" });
			ReportItem it1 = ReportPubTool.getItem("cdtcode", "托盘",
					IBillItem.STRING, 2, 80);
			list.add(it1);
		}
	}

	protected String getQueryConditon(int type) throws Exception {
		QueryDLG querylg = getQueryDlg();// 获取查询对话框
		ConditionVO[] vos = querylg.getConditionVO();// 获取已被用户填写的查询条件
		ConditionVO[] vos1 = filterQuery(vos);
		filterQuery2(vos1, type);
		String sql = querylg.getWhereSQL(vos1);
		return sql;
	}

	private void filterQuery2(ConditionVO[] vos, int type) {
		if (vos == null || vos.length == 0)
			return;
		if (type == 0) {
			for (int i = 0; i < vos.length; i++) {
				if ("dbilldate".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("dbilldate");
				}
			}
		} else if (type == 1) {
			for (int i = 0; i < vos.length; i++) {
				if ("dbilldate".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("geh_dbilldate");
				}
			}
		}
	}

	/*
	 * 其他出库
	 */
	private String getQuerySQL2() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		for (int i = 0; i < select_fields_in_h.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("h." + select_fields_in_h[i]);
		}
		sql.append(" , ");
		for (int i = 0; i < select_fields_in_b.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("b." + select_fields_in_b[i]);
		}
		if (getGroupByOrSelectConditon() != null
				&& getGroupByOrSelectConditon().trim().length() > 0) {
			sql.append(" , bb.cdt_pk ");
		}
		sql.append(" from ");
		sql.append(" tb_general_h h,tb_general_b b,tb_general_b_b bb ");

		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0) = 0 ");
		sql.append(" and h.geh_pk = b.geh_pk ");
		sql.append(" and b.geb_pk = bb.geb_pk ");
		if (getQueryConditon(1) != null && getQueryConditon(1).length() != 0) {
			sql.append(" and ");
			sql.append(getQueryConditon(1));
		}
		return sql.toString();
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// 清空表体数据
				clearBody();
				setDynamicColumn1();
				// 得到查询结果
				List<ReportBaseVO[]> list = getReportVO(new String[] {
						getQuerySQL1(), getQuerySQL2() });

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
		// ReportBaseVO[] vos3 = null;
		if (list.get(0) != null || list.get(0).length > 0) {
			vos1 = list.get(0);
		}
		if (list.get(1) != null || list.get(1).length > 0) {
			vos2 = list.get(1);
		}
		// if (list.get(2) != null || list.get(2).length > 0) {
		// vos3 = list.get(2);
		// }
		ReportBaseVO[] combinvo = null;
		combinvo = CombinVO.comin(combinvo, vos1);
		combinvo = CombinVO.comin(combinvo, vos2);
		// combinvo = CombinVO.comin(combinvo, vos3);
		return combinvo;
		// return setVoByContion(combinvo, voCombinConds);
	}

	@Override
	public Map getNewItems() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initReportUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public String _getModelCode() {
		return "80100220";
	}
}
