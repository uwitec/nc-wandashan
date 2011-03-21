package nc.ui.wds.w8006080206;

import java.util.ArrayList;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.w8006080206.BdStordocVO;
import nc.vo.wds.w8006080206.TbStorcubasdocVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class MyClientUI extends AbstractMyClientUI {

	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
	}

	public void setDefaultData() throws Exception {
	}

	@Override
	public void afterEdit(BillEditEvent e) {
		// TODO Auto-generated method stub
		if ("custcode".equals(e.getKey())) {
			// 获得销售区域
			String pk_cubasdoc1 = (String) getBillCardPanel().getBillModel()
					.getValueAt(
							getBillCardPanel().getBillTable().getSelectedRow(),
							"pk_cubasdoc");
			String cmdsql = "select def1,pk_cumandoc from bd_cumandoc  where pk_corp='1021' and  pk_cubasdoc='"
					+ pk_cubasdoc1 + "'  and dr=0 and custflag=2 ";

			IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			ArrayList os = new ArrayList();
			try {
				os = (ArrayList) query.executeQuery(cmdsql.toString(),
						new ArrayListProcessor());
			} catch (BusinessException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			// pk_custom9
			if (null != os && os.size() > 0) {
				Object[] gvo = (Object[]) os.get(0);
				if (null != gvo[0]) {
					String def1sql = "select pk_defdoc ,docname from bd_defdoc where pk_defdoc='"
							+ gvo[0].toString() + "' and dr=0 ";
					ArrayList os1 = new ArrayList();
					try {
						os1 = (ArrayList) query.executeQuery(
								def1sql.toString(), new ArrayListProcessor());
					} catch (BusinessException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					if (null != os1 && os1.size() > 0) {
						Object[] gvo1 = (Object[]) os1.get(0);
						if (null != gvo1[0]) {
							getBillCardPanel().getBillModel().setValueAt(
									gvo1[0].toString(),
									getBillCardPanel().getBillTable()
											.getSelectedRow(), "pk_defdoc");
						} else {
							getBillCardPanel().getBillModel().setValueAt(
									"",
									getBillCardPanel().getBillTable()
											.getSelectedRow(), "pk_defdoc");

						}
						if (null != gvo1[1]) {
							getBillCardPanel().getBillModel().setValueAt(
									gvo1[1].toString(),
									getBillCardPanel().getBillTable()
											.getSelectedRow(), "defdocname");
						} else {

							getBillCardPanel().getBillModel().setValueAt(
									"",
									getBillCardPanel().getBillTable()
											.getSelectedRow(), "defdocname");
						}
					} else {
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "pk_defdoc");
						getBillCardPanel().getBillModel().setValueAt(
								"",
								getBillCardPanel().getBillTable()
										.getSelectedRow(), "defdocname");
					}
				} else {
					getBillCardPanel().getBillModel().setValueAt("",
							getBillCardPanel().getBillTable().getSelectedRow(),
							"pk_defdoc");
					getBillCardPanel().getBillModel().setValueAt("",
							getBillCardPanel().getBillTable().getSelectedRow(),
							"defdocname");
				}
				if (null != gvo[1]) {
					getBillCardPanel().getBillModel().setValueAt(
							gvo[1].toString(),
							getBillCardPanel().getBillTable().getSelectedRow(),
							"pk_custom9");
				} else {
					getBillCardPanel().getBillModel().setValueAt("",
							getBillCardPanel().getBillTable().getSelectedRow(),
							"pk_custom9");
				}
			} else {
				getBillCardPanel().getBillModel().setValueAt("",
						getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_defdoc");
				getBillCardPanel().getBillModel().setValueAt("",
						getBillCardPanel().getBillTable().getSelectedRow(),
						"defdocname");
				getBillCardPanel().getBillModel().setValueAt("",
						getBillCardPanel().getBillTable().getSelectedRow(),
						"pk_custom9");
			}
			//
			AggregatedValueObject mybillVO1 = null;
			try {
				mybillVO1 = this.getVOFromUI();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			BdStordocVO bdStordocVO = (BdStordocVO) mybillVO1.getParentVO();
			TbStorcubasdocVO[] tbStorcubasdocFormVOs = (TbStorcubasdocVO[]) mybillVO1
					.getChildrenVO();
			if (null != tbStorcubasdocFormVOs) {
				for (int i = 0; i < tbStorcubasdocFormVOs.length; i++) {
					String pk_cubasdoc = tbStorcubasdocFormVOs[i]
							.getPk_cubasdoc();
					for (int j = i + 1; j < tbStorcubasdocFormVOs.length; j++) {
						String pk_cubasdocj = tbStorcubasdocFormVOs[j]
								.getPk_cubasdoc();
						if (null != pk_cubasdoc && null != pk_cubasdocj
								&& !"".equals(pk_cubasdoc)
								&& !"".equals(pk_cubasdocj)) {
							if (pk_cubasdoc.equals(pk_cubasdocj)) {
								this.showErrorMessage("添加的客商已经和本分仓绑定，请您重新选择！");
								// getBillCardPanel().setBodyValueAt("", j,
								// "custcode");
								// getBillCardPanel().setBodyValueAt("", j,
								// "custname");
								// getBillCardPanel().setBodyValueAt("", j,
								// "pk_cubasdoc");
								return;
							}
						}
					}
				}
			}

			// 除了本仓外其他仓库的绑定客商
			StringBuffer sql_srcbd = new StringBuffer();
			sql_srcbd.append(" dr=0 ");
			if (null != bdStordocVO && null != bdStordocVO.getPk_stordoc()) {
				sql_srcbd.append("and  pk_stordoc!='");
				sql_srcbd.append(bdStordocVO.getPk_stordoc());
				sql_srcbd.append("' ");
			}
			// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
			// IUAPQueryBS.class.getName());
			ArrayList tbStorcubasdocVOs = null;
			try {
				tbStorcubasdocVOs = (ArrayList) query.retrieveByClause(
						TbStorcubasdocVO.class, sql_srcbd.toString());
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			AggregatedValueObject mybillVO = null;
			try {
				mybillVO = this.getVOFromUI();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			TbStorcubasdocVO[] tbStorcubasdocChangeVOs = (TbStorcubasdocVO[]) mybillVO
					.getChildrenVO();
			if (null != tbStorcubasdocVOs && null != tbStorcubasdocChangeVOs) {
				for (int i = 0; i < tbStorcubasdocVOs.size(); i++) {
					TbStorcubasdocVO tbStorcubasdocVO = (TbStorcubasdocVO) tbStorcubasdocVOs
							.get(i);
					for (int j = 0; j < tbStorcubasdocChangeVOs.length; j++) {
						TbStorcubasdocVO tbStorcubasdocChangeVO = tbStorcubasdocChangeVOs[j];
						if (null != tbStorcubasdocVO.getPk_cubasdoc()
								&& null != tbStorcubasdocChangeVO
										.getPk_cubasdoc()) {
							if (tbStorcubasdocVO.getPk_cubasdoc().trim()
									.equals(
											tbStorcubasdocChangeVO
													.getPk_cubasdoc().trim())) {
								this
										.showErrorMessage("添加的客商已经和其它分仓绑定，请您先解除绑定关系！");
								// getBillCardPanel().setBodyValueAt("", j,
								// "custcode");
								// getBillCardPanel().setBodyValueAt("", j,
								// "custname");
								// getBillCardPanel().setBodyValueAt("", j,
								// "pk_cubasdoc");
								return;
							}
						}
					}
				}
			}

		}
	}

}
