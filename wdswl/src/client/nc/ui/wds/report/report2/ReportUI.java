package nc.ui.wds.report.report2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.TableColumnModel;
import nc.bd.accperiod.AccountCalendar;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.WDSWLReportSql;
import nc.ui.wl.pub.report.ZmReportBaseUI;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;

/**
 * 各仓库总库存报表
 * 
 * @author mlr
 */
public class ReportUI extends ZmReportBaseUI {

	private static final long serialVersionUID = -5891956414451757118L;
	// 待检状态主键
	private static String pk_daijian = "1021S31000000009FS99";
	// 基础树合并维度
	private static String[] jcombinFields = { "pk_stordoc" };
	// 仓库合并维度
	private static String[] combinFields = { "pk_stordoc", "days", "creadate" };
	// 合并字段
	private static String[] combinNums = { "num", "bnum" };
	// 计算库龄
	private static int[][] hdays = { { 0, 60 }, { 61, 90 } };
	private AccountCalendar ac = AccountCalendar.getInstance();
	private ClientEnvironment ce = ClientEnvironment.getInstance();

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	public String _getModelCode() {
		return WdsWlPubConst.report2;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return WDSWLReportSql.getStoreSql(getQueryDlg().getWhereSQL());
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// 清空表体数据
				clearBody();
				// 设置动态列
				setDynamicColumn1();
				// 得到查询结果
				ReportBaseVO[] vos = null;
				// 设置基本列合并
				setColumn();
				// 设置vo
				vos = getReportVO(getQuerySQL());
				ReportBaseVO[] rvos = deal(vos);
				if (rvos == null || rvos.length == 0)
					return;
				if (rvos != null) {
					super.updateBodyDigits();
					setBodyVO(rvos);
					setTolal1();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setTolal1() throws Exception {
		new LevelSubTotalAction(this).atuoexecute2();

	}

	/**
	 * 处理报表数据
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2011-12-8下午02:33:15
	 * @param vos
	 * @throws Exception
	 */
	private ReportBaseVO[] deal(ReportBaseVO[] vos) throws Exception {
		if (vos == null || vos.length == 0)
			return null;
		// 构建左侧基础数据树
		ReportBaseVO[] jichus = (ReportBaseVO[]) CombinVO.combinData(
				(ReportBaseVO[]) ObjectUtils.serializableClone(vos),
				jcombinFields, new String[] { "numm", "bnumm" },
				ReportBaseVO.class);
		// 按仓库合并维度 合并数据
		ReportBaseVO[] rvos = (ReportBaseVO[]) CombinVO.combinData(
				(ReportBaseVO[]) ObjectUtils.serializableClone(vos),
				combinFields, combinNums, ReportBaseVO.class);
		// 计算0-90天库存
		calDay1(jichus, rvos);
		calYear(jichus, rvos);
		calDaiJian(jichus, rvos);
		return jichus;
	}

	private void calDaiJian(ReportBaseVO[] jichus, ReportBaseVO[] cvos1)
			throws Exception {
		ReportBaseVO[] cvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos1);
		if (cvos == null || cvos.length == 0)
			return;

		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();// 记录待检
		int loc = 3;// 记录待检的位置
		for (int i = 0; i < cvos.length; i++) {
			String pk = PuPubVO.getString_TrimZeroLenAsNull(cvos[i]
					.getAttributeValue("pk_storestate"));// 获取存货状态
			if (pk == null) {
				continue;
			}
			if (pk.equals(pk_daijian)) {
				list.add(cvos[i]);
			}
		}
		if (list.size() == 0)
			return;
		ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(list
				.toArray(new ReportBaseVO[0]), jcombinFields, combinNums,
				ReportBaseVO.class);
		CombinVO.addByContion1(jichus, nevos, jcombinFields, loc + "");

	}

	/**
	 * 计算0-90天库存中的每月的物品现存量
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2011-12-2下午04:05:13
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calDay1(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {

		for (int i = 0; i < 2; i++) {
			ReportBaseVO[] vos = filterByDays(cvos, hdays[i][0], hdays[i][1]);
			if (vos == null || vos.length == 0)
				continue;
			ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(vos,
					jcombinFields, combinNums, ReportBaseVO.class);
			CombinVO.addByContion1(jichus, nevos, jcombinFields, (i + 1) + "");

		}
	}

	private ReportBaseVO[] filterByDays(ReportBaseVO[] cvos, int satrtDays,
			int endDays) throws Exception {
		ReportBaseVO[] nvos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos);
		if (nvos == null || nvos.length == 0) {
			return null;
		}
		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();
		for (int i = 0; i < nvos.length; i++) {
			int days = PuPubVO.getInteger_NullAs(nvos[i]
					.getAttributeValue("days"), -1);
			if (days >= satrtDays && days <= endDays) {
				list.add(nvos[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;
		return list.toArray(new ReportBaseVO[0]);
	}

	/**
	 * 计算去年 和 前年的大日期物品
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2011-12-3下午03:26:36
	 * @param jichus
	 * @param cvos
	 * @throws Exception
	 */
	private void calYear(ReportBaseVO[] jichus, ReportBaseVO[] cvos)
			throws Exception {
		if (cvos == null || cvos.length == 0)
			return;
		ReportBaseVO[] vos = (ReportBaseVO[]) ObjectUtils
				.serializableClone(cvos);
		List<ReportBaseVO> qus = new ArrayList<ReportBaseVO>();// 存放去年大日期
		List<ReportBaseVO> qis = new ArrayList<ReportBaseVO>();// 存放前年大日期
		int year = ce.getDate().getYear() - 1;// 去年
		int year1 = year - 1;// 前年
		int loc = 5;// 报表去年 和 前年 字段的位置
		for (int i = 0; i < vos.length; i++) {
			String cdate = PuPubVO.getString_TrimZeroLenAsNull(vos[i]
					.getAttributeValue("creadate"));
			if (cdate == null)
				continue;
			if (cdate.startsWith(year + "")) {
				qus.add(vos[i]);
			}
			if (cdate.startsWith(year1 + "")) {
				qis.add(vos[i]);
			}
		}

		if (qus.size() != 0) {
			ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qus
					.toArray(new ReportBaseVO[0]), jcombinFields, combinNums,
					ReportBaseVO.class);
			CombinVO
					.addByContion1(jichus, nevos, jcombinFields, (loc + 1) + "");
		}
		if (qis.size() != 0) {
			ReportBaseVO[] nevos = (ReportBaseVO[]) CombinVO.combinData(qis
					.toArray(new ReportBaseVO[0]), jcombinFields, combinNums,
					ReportBaseVO.class);
			CombinVO.addByContion1(jichus, qis.toArray(new ReportBaseVO[0]),
					jcombinFields, loc + "");
		}
	}

	/**
	 * 基本列合并
	 */
	private void setColumn() {

		// 表体栏目分组设置
		UITable cardTable = getReportBase().getBillTable();
		GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable
				.getTableHeader();
		TableColumnModel cardTcm = cardTable.getColumnModel();
		ColumnGroup shiji = new ColumnGroup("箱粉");
		// 大的分组 有效
		ColumnGroup zgroup = new ColumnGroup("有效");
		int i = -1;// 设置变化赋值量

		zgroup.add(cardTcm.getColumn(3 + i));
		zgroup.add(cardTcm.getColumn(4 + i));

		zgroup.add(cardTcm.getColumn(5 + i));
		zgroup.add(cardTcm.getColumn(6 + i));

		zgroup.add(cardTcm.getColumn(7 + i));
		zgroup.add(cardTcm.getColumn(8 + i));

		zgroup.add(cardTcm.getColumn(9 + i));
		zgroup.add(cardTcm.getColumn(10 + i));

		shiji.add(zgroup);

		ColumnGroup zgroup1 = new ColumnGroup("无效");

		zgroup1.add(cardTcm.getColumn(11 + i));
		zgroup1.add(cardTcm.getColumn(12 + i));

		zgroup1.add(cardTcm.getColumn(13 + i));

		shiji.add(zgroup1);

		shiji.add(cardTcm.getColumn(14 + i));

		cardHeader.addColumnGroup(shiji);

		ColumnGroup zgroup2 = new ColumnGroup("原料粉(吨)");
		i = 3;

		zgroup2.add(cardTcm.getColumn(15 + i));
		zgroup2.add(cardTcm.getColumn(16 + i));

		zgroup2.add(cardTcm.getColumn(17 + i));
		cardHeader.addColumnGroup(zgroup2);

		getReportBase().getBillModel().updateValue();

	}

	@Override
	public void initReportUI() {
		// if(combinNums==null){
		// List<String> nums=new ArrayList<String>();
		// for(int i=1;i<17;i++){
		// nums.add("num"+i);
		// nums.add("bnum"+i);
		// }
		// combinNums=nums.toArray(new String[0]);
		// }
		setColumn();

	}

}
