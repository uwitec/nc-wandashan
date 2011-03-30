package nc.ui.ic.pub.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.table.TableColumn;

import nc.bs.logging.Logger;
import nc.ui.ic.ic001.BatchCodeDefSetTool;
import nc.ui.ic.ic219.DataBuffer;
import nc.ui.ic.pub.bill.initref.RefFilter;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.scm.pub.def.DefSetTool;
import nc.vo.ic.ic001.BatchcodeVO;
import nc.vo.ic.pub.BillTypeConst;
import nc.vo.ic.pub.GenMethod;
import nc.vo.ic.pub.bc.BarCodeVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.sn.SerialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.formulaset.util.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.bill.FreeVO;
import nc.vo.scm.ic.bill.InvVO;
import nc.vo.scm.ic.bill.WhVO;
import nc.vo.scm.pub.SCMEnv;

/**
 * 创建人：刘家清 创建日期：2008-2-25上午09:33:45 创建原因：编辑动作事件类。
 * 
 */

public class EditCtrl {

	private GeneralBillClientUI m_ui;

	public EditCtrl(GeneralBillClientUI ui) {
		m_ui = ui;
	}

	protected GeneralBillClientUI getClientUI() {
		return m_ui;
	}

	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		int pos = e.getPos();

		if (pos <= 0)
			return;
		// 毛重和皮重编辑后需要执行数量公式等
		if (key.equals("ningrossnum") || key.equals("ntarenum")
				|| key.equals("noutgrossnum"))
			afterGrossAndTarnumEdit(e);
	}

	private void afterGrossAndTarnumEdit(BillEditEvent e) {
		UFDouble gros = (UFDouble) getBillCardPanel().getBodyValueAt(
				e.getRow(), "ningrossnum");

		if (gros == null)
			gros = (UFDouble) getBillCardPanel().getBodyValueAt(e.getRow(),
					"noutgrossnum");

		if (gros == null)
			gros = GenMethod.ZERO;

		UFDouble tare = (UFDouble) getBillCardPanel().getBodyValueAt(
				e.getRow(), "ntarenum");

		if (tare == null)
			tare = GenMethod.ZERO;

		UFDouble num = (UFDouble) getM_voBill().getItemValue(e.getRow(),
				"ninnum");
		if (num == null)
			num = (UFDouble) getM_voBill().getItemValue(e.getRow(), "noutnum");

		if (num == null)
			num = GenMethod.ZERO;

		if (num.compareTo(gros.sub(tare)) == 0)
			return;
		else
			afterNumEdit(e);

	}

	private HashMap<String, Boolean> hsCardEditFlag = new HashMap<String, Boolean>();
	//
	private HashMap<String, Boolean> hsCardRefFilterWherePart = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> hsCardRefFilterAddWherePart = new HashMap<String, Boolean>();

	/**
	 * 
	 */
	public void clearData() {
		hsCardEditFlag.clear();
	}

	/**
	 * 
	 */
	public void saveCardEditFlag(BillCardPanel card) {
		if (card == null)
			return;
		hsCardEditFlag.clear();
		BillItem[] items = card.getHeadItems();
		final String hprev = "h_";
		final String bprev = "b_";
		if (items != null) {
			for (BillItem item : items)
				hsCardEditFlag.put(hprev + item.getKey(),
						item.isEdit() ? Boolean.TRUE : Boolean.FALSE);
		}
		items = card.getBodyItems();
		if (items != null) {
			for (BillItem item : items)
				hsCardEditFlag.put(bprev + item.getKey(),
						item.isEdit() ? Boolean.TRUE : Boolean.FALSE);
		}
	}

	/**
	 * 
	 */
	public void resetCardEditFlag(BillCardPanel card) {
		if (card == null)
			return;
		BillItem[] items = card.getHeadItems();
		final String hprev = "h_";
		final String bprev = "b_";
		Boolean flag = null;

		if (items != null) {
			for (BillItem item : items) {
				flag = hsCardEditFlag.get(hprev + item.getKey());
				if (flag != null)
					item.setEdit(flag.booleanValue());
			}
		}
		items = card.getBodyItems();
		if (items != null) {
			for (BillItem item : items) {
				flag = hsCardEditFlag.get(bprev + item.getKey());
				if (flag != null)
					item.setEdit(flag.booleanValue());
			}
		}
	}

	protected void afterCDptIDEdit(nc.ui.pub.bill.BillEditEvent e) {
		BillItem itDpt = getBillCardPanel().getHeadItem("cdptid");
		// 部门
		String sName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
				.getRefName();

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdptname", sName);

	}

	protected void afterCBizidEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 业务员
		String sName = ((nc.ui.pub.beans.UIRefPane) getClientUI()
				.getBillCardPanel().getHeadItem("cbizid").getComponent())
				.getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getClientUI()
				.getBillCardPanel().getHeadItem("cbizid").getComponent())
				.getRefPK();
		// 需要根据业务员自动带出部门
		//修改人：刘家清 修改时间：2008-8-25 上午10:59:04 修改原因：不根据业务员自动带出部门
		String sDeptPK = null;
		String sDeptName = null;
		if (sPK != null && sPK.trim().length() > 0) {
			try {
				sDeptPK = execFormular(
						"getColValue(bd_psndoc,pk_deptdoc,pk_psndoc,pk_psndoc)",
						sPK);
			} catch (Exception ex) {
				nc.vo.scm.pub.SCMEnv.error(ex);
			}
			BillItem itDpt = getBillCardPanel().getHeadItem("cdptid");
			if (itDpt != null) {
				((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
						.setPK(sDeptPK);
				// 部门
				sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
						.getRefName();
			}
		}
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setHeaderValue("cbizname", sName);
			//getM_voBill().setHeaderValue("cdptname", sDeptName);
		}
	}

	/**
	 * 调用公式 功能： 参数： 返回： 例外： 日期：(2001-11-12 16:47:04) 修改日期，修改人，修改原因，注释标志：
	 */
	private String execFormular(String formula, String value) {
		nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

		if (formula != null && !formula.equals("")) {
			// 设置表达式
			f.setExpress(formula);
			// 获得变量
			nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
			// 给变量付值
			Hashtable h = new Hashtable();
			for (int j = 0; j < varry.getVarry().length; j++) {
				String key = varry.getVarry()[j];

				String[] vs = new String[1];
				vs[0] = value;
				h.put(key, StringUtil.toString(vs));
			}

			f.setDataS(h);
			// 设置结果
			if (varry.getFormulaName() != null
					&& !varry.getFormulaName().trim().equals(""))
				return f.getValueS()[0];
			else
				return f.getValueS()[0];

		} else {
			return null;
		}
	}

	/**
	 * 作者：李俊 功能：供应商编辑后事件：带出简称 参数： 返回： 例外： 日期：(2004-6-21 10:36:30)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefPK();

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cprovidername", sName);

		// 根据客户或供应商过滤发运地址的参照
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null) {
			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					"vdiliveraddress").getComponent())
					.setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"
							+ sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem(
				"cprovidershortname");
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		try {
			// 根据参照带出供应商简称
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// 获得客商基本档案ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdoc != null)
				iPk_cubasdoc.setValue(sPk_cubasdoc);

			if (getM_voBill() != null) {
				getM_voBill().setHeaderValue("cprovidershortname",
						sProviderShortName);
				getM_voBill().setHeaderValue("pk_cubasdoc", sPk_cubasdoc);
			}
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			MessageDialog.showErrorDlg(getClientUI(), null, be.getMessage());
		} catch (BusinessRuntimeException bre) {
			Logger.error(bre.getMessage(), bre);
			MessageDialog.showErrorDlg(getClientUI(), null, bre.getMessage());
		} catch (Exception ee) {
			MessageDialog.showUnknownErrorDlg(getClientUI(), ee);
		}

	}

	/**
	 * 
	 * 方法功能描述：根据客商管理档案获得客商的基本档案。
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param pk_cumangid
	 *            客商管理档案的ID
	 * @return 客商基本档案ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 上午10:50:26
	 */
	private String getPk_cubasdoc(String pk_cumandoc) {
		if (pk_cumandoc == null)
			return null;
		try {
			Object[] pks = (Object[]) nc.ui.scm.pub.CacheTool.getColumnValue(
					"bd_cumandoc", "pk_cumandoc", "pk_cubasdoc",
					new String[] { pk_cumandoc });
			if (pks != null)
				return (String) pks[0];
		} catch (BusinessException e) {
			nc.ui.ic.pub.tools.GenMethod
					.handleException(getClientUI(), null, e);
		}
		return null;
	}

	/**
	 * 作者：李俊 功能：客户编辑后事件 参数： 返回： 例外： 日期：(2004-6-21 10:38:55) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterCustomerEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 客户
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("ccustomerid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("ccustomerid").getComponent()).getRefPK();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("ccustomername", sName);
		// 根据客户或供应商过滤发运地址的参照
		getClientUI().filterVdiliveraddressRef(true, -1);

		// 根据客户或供应商过滤发运地址的参照
		if (getBillCardPanel().getHeadItem("vdiliveraddress") != null
				&& sRefPK != null) {

			((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					"vdiliveraddress").getComponent())
					.setWhereString("pk_cubasdoc = (select pk_cubasdoc from bd_cumandoc where pk_cumandoc = '"
							+ sRefPK + "')");
		}

		BillItem iProvidername = getBillCardPanel().getHeadItem(
				"ccustomershortname");

		BillItem iPk_cubasdocC = getBillCardPanel().getHeadItem("pk_cubasdocC");

		try {
			// 根据参照带出客商简称
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sCustomerShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sCustomerShortName);
			}

			// 获得客商基本档案ID
			String sPk_cubasdoc = getPk_cubasdoc(sRefPK);
			if (iPk_cubasdocC != null)
				iPk_cubasdocC.setValue(sPk_cubasdoc);

			if (getM_voBill() != null) {
				getM_voBill().setHeaderValue("ccustomershortname",
						sCustomerShortName);
				getM_voBill().setHeaderValue("pk_cubasdocC", sPk_cubasdoc);
			}
		} catch (BusinessException be) {
			Logger.error(be.getMessage(), be);
			MessageDialog.showErrorDlg(getClientUI(), null, be.getMessage());
		} catch (BusinessRuntimeException bre) {
			Logger.error(bre.getMessage(), bre);
			MessageDialog.showErrorDlg(getClientUI(), null, bre.getMessage());
		} catch (Exception ee) {
			MessageDialog.showUnknownErrorDlg(getClientUI(), ee);
		}

	}

	

	/**
	 * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit_1(int row) {

		/** 数量清空：if 固定换算率：清空辅数量。 */
		Object oNumValue = getBillCardPanel().getBodyValueAt(row,
				getEnvironment().getNumItemKey());

		if (oNumValue == null || oNumValue.toString().trim().length() < 1) {

			if (!isInitBill()) {
				// 清空本行的业务日期
				getBillCardPanel().setBodyValueAt(null, row, "dbizdate");

			}

			if (getBillCardPanel().getBodyItem("ncountnum") != null) {
				getBillCardPanel().setBodyValueAt(null, row, "ncountnum");

			}
		} else {

			UFDouble nNum = new UFDouble(oNumValue.toString().trim());

			// 如果来源单据为特殊单据，控制数量和辅助数量不应超过应收发数量
			// afterNumEditFromSpe(e);

			if (!isInitBill()) {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null)

					// 非期初单据自动带出业务日期
					getBillCardPanel().setBodyValueAt(getClientUI().getEnvironment().getLogDate(),
							row, "dbizdate");

			} else {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null) {

					// 期初单据自动带出系统启动日期
					nc.vo.pub.lang.UFDate dstart = new nc.vo.pub.lang.UFDate(
							getClientUI().m_sStartDate);
					nc.vo.pub.lang.UFDate dbiz = dstart.getDateBefore(1);
					getBillCardPanel().setBodyValueAt(dbiz.toString(), row,
							"dbizdate");
				}
			}
			UFDouble npacknum = (UFDouble) getM_voBill().getItemValue(row,
					"npacknum");
			if (npacknum != null && npacknum.doubleValue() != 0) {
				double ntemp = nNum.div(npacknum).abs().doubleValue();
				getBillCardPanel().setBodyValueAt(
						new UFDouble(Math.ceil(ntemp)), row, "ncountnum");
			}

		}
		// 如果数量原来的值不为空，并且改变了方向，清空对应单据
		if (getM_voBill().getItemVOs()[row].getInOutFlag() == InOutFlag.IN)
			clearCorrBillInfo(row);

		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
				new String[] { "ncountnum", "dbizdate" }, row);
		getClientUI().execEditFomulas(row, getEnvironment().getNumItemKey());
		afterShouldNumEdit(new BillEditEvent(this, null, null, getEnvironment()
				.getShouldNumItemKey(), row, BillItem.BODY));

		GeneralBillUICtl.calcNordcanoutnumAfterNumEdit(getM_voBill(),
				getBillCardPanel(), getBillType(), row);
		resetSpace(row);
		//getClientUI().freshWHEditable();
	}

	/**
	 * 创建者：王乃军 功能：清空对应单数据 参数： 返回： 例外： 日期：(2001-11-23 18:11:18)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void clearCorrBillInfo(int rownum) {
		// 清空对应单据
		// 对应单据号
		try {
			getBillCardPanel()
					.setBodyValueAt(null, rownum, IItemKey.CORRESCODE);
		} catch (Exception e3) {
		}
		try {
			// 对应单据类型
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondtype");
		} catch (Exception e4) {
		}
		try {
			// 对应单据表头OID
			// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondhid");
		} catch (Exception e5) {
		}
		try {
			// 对应单据表体OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondbid");
		} catch (Exception e6) {
		}
		// 同步改变m_voBill
		getM_voBill().setItemValue(rownum, "ccorrespondbid", null);
		getM_voBill().setItemValue(rownum, "ccorrespondhid", null);
		getM_voBill().setItemValue(rownum, IItemKey.CORRESCODE, null);
		getM_voBill().setItemValue(rownum, "ccorrespondtype", null);

	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {
		int rownum = e.getRow();
		// 辅计量单位
		nc.ui.pub.beans.UIRefPane refCastunit = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("castunitname").getComponent());

		String sPK = refCastunit.getRefPK();
		String sName = refCastunit.getRefName();
		
		getClientUI().clearRowData(0, rownum, "castunitid");

		boolean isFixFlag = false;
		Integer isSolidConvRate = null;
		Integer isstorebyconvert = null;
		Integer issupplierstock = null;

		if (sPK != null && sPK.trim().length() > 0) {

			UFDouble hsl = null;

			getM_voBill().setItemValue(rownum, "castunitid", sPK);
			getM_voBill().setItemValue(rownum, "castunitname", sName);
			getBillCardPanel().setBodyValueAt(sName, rownum, "castunitname");
			getBillCardPanel().setBodyValueAt(sPK, rownum, "castunitid");

			InvVO invvo = getM_voBill().getItemInv(rownum);
			getClientUI().getInvoInfoBYFormula().getInVoOfHSLByHashCach(
					new InvVO[] { invvo });
			if (invvo != null) {
				hsl = invvo.getHsl();
				getBillCardPanel().setBodyValueAt(hsl, rownum, "hsl");
				hsl = (UFDouble) getBillCardPanel().getBodyValueAt(rownum,
						"hsl");
				getM_voBill().setItemValue(rownum, "hsl", hsl);

				isSolidConvRate = invvo.getIsSolidConvRate();
				isstorebyconvert = invvo.getIsStoreByConvert();
				issupplierstock = invvo.getIssupplierstock();
				getM_voBill().setItemValue(rownum, "isSolidConvRate",
						isSolidConvRate);
				getM_voBill().getItemInv(rownum).setIsSolidConvRate(
						isSolidConvRate);
				getM_voBill().setItemValue(rownum, "isstorebyconvert",
						isstorebyconvert);
				getM_voBill().setItemValue(rownum, "issupplierstock",
						issupplierstock);
			}

		}

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getAssistNumItemKey(),
				getEnvironment().getNumItemKey() };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum,
				sfields, isFixFlag);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		sfields = new String[] { "castunitid", "hsl", "ngrossastnum",
				"ngrossnum" };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum,
				sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		sfields = new String[] { "castunitid", "hsl", "ntareastnum", "ntarenum" };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid", rownum,
				sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		if (!isInitBill()) {

			sfields = new String[] { "castunitid", "hsl",
					getEnvironment().getShouldAssistNumItemKey(),
					getEnvironment().getShouldNumItemKey() };
			GeneralBillUICtl.changeNum(getBillCardPanel(), "castunitid",
					rownum, sfields, isFixFlag);
			GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
					sfields, rownum);

		}

		nc.vo.ic.pub.DesassemblyVO voDesa = (nc.vo.ic.pub.DesassemblyVO) getM_voBill()
				.getItemValue(rownum, "desainfo");
		getM_voBill().setItemValue(rownum, "idesatype",
				new Integer(voDesa.getDesaType()));
		getBillCardPanel().setBodyValueAt(new Integer(voDesa.getDesaType()),
				rownum, "idesatype");
		afterNumEdit_1(rownum);

	}

	/**
	 * 创建者：余大英 功能：数量改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit(nc.ui.pub.bill.BillEditEvent e) {

		int row = e.getRow();

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getAssistNumItemKey(),
				getEnvironment().getNumItemKey() };
		boolean isFix = isFixFlag(row);
		if (!isFix && null != getBillType() && (ScmConst.m_lendIn.equals(getBillType())
				||ScmConst.m_borrowOut.equals(getBillType())))
			isFix = true;
		
		//修改人：刘家清 修改时间：2008-12-1 上午09:10:26 修改原因：出库业务时，如果是换算率记结存的存货，并且有对应入库单号，就不要主动修改换算率。
		if (getInOutFlag() == InOutFlag.OUT
				&& getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
				&& getBillCardPanel().getBodyValueAt(row,IItemKey.CORRESCODE) != null
				&& getBillCardPanel().getBodyValueAt(row,IItemKey.CORRESCODE).toString().trim().length() > 0
				&& row >= 0 && getM_voBill().getItemVOs()[row] != null
				&& getM_voBill().getItemVOs()[row].getIsStoreByConvert() != null
				&& 1 == getM_voBill().getItemVOs()[row].getIsStoreByConvert())
			isFix = true;
			
		GeneralBillUICtl.changeNum(getBillCardPanel(), getEnvironment()
				.getNumItemKey(), row, sfields, isFix);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
				new String[] { "castunitid", "hsl",
						getEnvironment().getAssistNumItemKey(),
						getEnvironment().getNumItemKey() }, row);
		afterNumEdit_1(row);

	}

	/**
	 * 创建者：余大英 功能：应发辅数量编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 辅数量*换算率=数量
	 * 
	 */
	public void afterAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		int row = e.getRow();

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getAssistNumItemKey(),
				getEnvironment().getNumItemKey() };
		GeneralBillUICtl.changeNum(getBillCardPanel(), getEnvironment()
				.getAssistNumItemKey(), row, sfields, isFixFlag(row));
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
				new String[] { "castunitid", "hsl",
						getEnvironment().getAssistNumItemKey(),
						getEnvironment().getNumItemKey() }, row);
		afterNumEdit_1(row);

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-7-18 15:01:19) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */

	public void afterShouldNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		if (getBillCardPanel().getBodyItem(
				getEnvironment().getShouldAssistNumItemKey()) == null
				|| getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) == null)
			return;
		int rownum = e.getRow();

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getShouldAssistNumItemKey(),
				getEnvironment().getShouldNumItemKey() };
		GeneralBillUICtl.changeNum(getBillCardPanel(), getEnvironment()
				.getShouldNumItemKey(), rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);
	}

	/**
	 * 创建者：余大英 功能：应发辅数量编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 辅数量*换算率=数量
	 * 
	 */

	public void afterShouldAstNumEdit(nc.ui.pub.bill.BillEditEvent e) {
		int rownum = e.getRow();
		if (getBillCardPanel().getBodyItem(
				getEnvironment().getShouldAssistNumItemKey()) == null
				|| getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) == null)
			return;

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getShouldAssistNumItemKey(),
				getEnvironment().getShouldNumItemKey() };
		GeneralBillUICtl.changeNum(getBillCardPanel(), getEnvironment()
				.getShouldAssistNumItemKey(), rownum, sfields, true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-7-18 15:01:19) 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void afterCorBillEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();
    
		GeneralBillItemVO[] vos = getClientUI().getICCorBillRef().getSelectedVOs();
    getClientUI().getICCorBillRef().clearSelected();
		// ((nc.ui.ic.pub.corbillref.ICCorBillRefModel) getICCorBillRef()
		// .getRefModel()).getSelectedVOs();
    if(getClientUI().isLineCardEdit() && vos!=null && vos.length>1){
      vos = new GeneralBillItemVO[]{vos[0]};
    }

		if (vos == null || vos[0] == null) {
			// 对应单据号
			getBillCardPanel().setBodyValueAt(null, iSelrow,
					IItemKey.CORRESCODE);
			// 对应单据类型
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
			// 对应单据表头OID
			// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
			// 对应单据表体OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondbid");
			getM_voBill().setItemValue(iSelrow, "ccorrespondbid", null);

			getM_voBill().setItemValue(iSelrow, "ccorrespondhid", null);

			getM_voBill().setItemValue(iSelrow, IItemKey.CORRESCODE, null);

			getM_voBill().setItemValue(iSelrow, "ccorrespondtype", null);

			getM_voBill().setItemValue(iSelrow, "vtransfercode", null);

			// m_voBill.setItemValue(iSelrow, "", oValue)
			return;
		}
		synline(vos[vos.length - 1], iSelrow, true);

		if (vos != null && vos.length > 1) {
			for (int j = 0; j < vos.length - 1; j++) {
				getBillCardPanel().copyLine();
				voBillCopyLine();

				getBillCardPanel().pasteLine();
				// 增加的行数
				voBillPastLine();

				int irow = getBillCardPanel().getBillTable().getSelectedRow() - 1;

				synline(vos[j], irow, false);
				// 如果是出库处理界面，那么行号就要重新生成。
				if ("40080822".equals(getFunctionNode())) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel().setBodyValueAt(crowno, irow, "crowno");
					getM_voBill().setItemValue(irow, "crowno", crowno);
					getBillCardPanel()
							.setBodyValueAt(null, irow, "cgeneralbid");
					getM_voBill().setItemValue(irow, "cgeneralbid", null);
				}
			}
			// 如果是出库处理界面，那么行号就要重新生成。
			if ("40080822".equals(getFunctionNode())) {
				return;
				// 不用再处理行号了
			}
			int iRowCount = vos.length;
			if (e.getRow() + iRowCount == getBillCardPanel().getRowCount()) {
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(
						getBillCardPanel(), getBillType(), IItemKey.CROWNO,
						iRowCount);

			} else {
				nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(
						getBillCardPanel(), getBillType(), IItemKey.CROWNO, e
								.getRow()
								+ iRowCount - 1, iRowCount - 1);
			}

			// dw 设置完界面行号后更新 m_voBill
			for (int o = 0; o < getBillCardPanel().getRowCount(); o++) {
				getM_voBill().setItemValue(o, IItemKey.CROWNO,
						getBillCardPanel().getBodyValueAt(o, IItemKey.CROWNO));
			}
		}
	}

	protected nc.ui.ic.pub.lot.LotNumbRefPane getLotNumbRefPane() {
		return getClientUI().getLotNumbRefPane();
	}

	private void synlot(nc.vo.ic.pub.lot.LotNumbRefVO voLot, int irow) {
		getClientUI().synlot(voLot, irow);
	}

	/**
	 * 此处插入方法说明。 创建者：张欣 功能：批次号改变处理 参数： 返回： 例外： 日期：(2001-6-20 21:43:07)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param param
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterLotEdit(nc.ui.pub.bill.BillEditEvent param) {

		String sItemKey = param.getKey();
		int rownum = param.getRow();

		String sLot = (String) getBillCardPanel().getBodyValueAt(rownum,
				"vbatchcode");
		if (sLot == null || sLot.trim().length() == 0
				|| !GenMethod.isStringEqual(sLot, (String) param.getOldValue())) {
			getClientUI().clearRowData(0, rownum, "vbatchcode");
			getM_voBill().setItemValue(rownum, "vbatchcode", null);
//			 得到需清除的itemkey
			String[] saColKey = GeneralBillUICtl.getClearIDs(0, sItemKey,
					getClientUI());
			if (saColKey != null && saColKey.length > 0)
				for (int col = 0; col < saColKey.length; col++)
					getM_voBill().setItemValue(rownum, saColKey[col], null);

		}

		if (sLot == null || sLot.trim().length() == 0)
			return;

		nc.ui.ic.pub.lot.LotNumbRefPane lotRef = (nc.ui.ic.pub.lot.LotNumbRefPane) getBillCardPanel()
				.getBodyItem("vbatchcode").getComponent();

		if (!lotRef.isClicked()
				&& GenMethod.isStringEqual(sLot, (String) param.getOldValue()))
			return;
		/*
		 * if (!lotRef.isClicked()) return;
		 */
		nc.vo.ic.pub.lot.LotNumbRefVO[] voLot = null;
		
		boolean wheaterGo = false;
		
		BatchcodeVO[] batchvos = null;
		try {
			// 手工输入，可能会有异常。
			voLot = lotRef.getLotNumbRefVOs();
      if(getClientUI().isLineCardEdit() && voLot!=null && voLot.length>1){
        voLot = new nc.vo.ic.pub.lot.LotNumbRefVO[]{voLot[0]};
      }
      
//      if (!lotRef.isClicked() && null != voLot && 0 < voLot.length && !sLot.equals(voLot[0].getVbatchcode())){
//    	  getM_voBill().setItemValue(rownum, "vbatchcode", sLot);
//    	  return;
//      }
      
      if (null != voLot && 0 < voLot.length){
    	  for (nc.vo.ic.pub.lot.LotNumbRefVO vo : voLot)
    		  if (sLot.contains(vo.getVbatchcode()))
    			  wheaterGo = true;
      }else
    	  wheaterGo = true; 
      
      if (!wheaterGo){
    	  lotRef.setClicked(false);
    	  return;
      }
    	  
      
      if (null == voLot || voLot.length<=0){
        batchvos = new BatchcodeVO[1];
        String cinvbasid = (String) getBillCardPanel().getBodyValueAt(
            rownum, "cinvbasid");
        for (int i = 0; i < batchvos.length; i++) {
          batchvos[i] = new BatchcodeVO();
          batchvos[i].setPk_invbasdoc(cinvbasid);
          batchvos[i].setVbatchcode(sLot);
        }
        BatchCodeDefSetTool.getBatchCodeInfos(batchvos);
      }else{
  			batchvos = new BatchcodeVO[voLot.length];
  			String cinvbasid = (String) getBillCardPanel().getBodyValueAt(
  					rownum, "cinvbasid");
  			for (int i = 0; i < voLot.length; i++) {
  				batchvos[i] = new BatchcodeVO();
  				batchvos[i].setPk_invbasdoc(cinvbasid);
  				batchvos[i].setVbatchcode(voLot[i].getVbatchcode());
  			}
  			BatchCodeDefSetTool.getBatchCodeInfos(batchvos);
      }
      
      getM_voBill().setItemValue(rownum, "vbatchcode", sLot);
      //liuys add for 鹤岗矿业  出库表体供应商赋值 begin
//      String pk_cumandoc = execFormular(
//				"getColValue(bd_cumandoc,pk_cumandoc,pk_cubasdoc,pk_cubasdoc)",
//				voLot[0].getPk_cubasdoc());
//      getM_voBill().setItemValue(rownum, "cvendorid", pk_cumandoc);
//      getM_voBill().setItemValue(rownum, "pk_cubasdoc", voLot[0].getPk_cubasdoc());
//      getBillCardPanel().setBodyValueAt(pk_cumandoc, rownum, "cvendorid");
//      getBillCardPanel().setBodyValueAt(voLot[0].getPk_cubasdoc(), rownum, "pk_cubasdoc");
//      getBillCardPanel().getBillModel().execLoadFormula();
    //liuys add for 鹤岗矿业  出库表体供应商赋值 end
      
      // BatchCodeDefSetTool.setBatchCodeInfo(getBillCardPanel(),rownum,(String)getM_voBill().getItemValue(rownum,"cinventoryid"),sLot,getEnvironment().getCorpID(),getM_voBill().getItemVOs()[rownum]);
      if (batchvos != null && 0 < batchvos.length)
        BatchCodeDefSetTool.setBatchCodeInfo(batchvos[0],
            getBillCardPanel(), rownum,
            getM_voBill().getItemVOs()[rownum]);

      getClientUI().getLotRefbyHand(sItemKey);
      
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
		}finally{
      lotRef.setClicked(false);
    }
		
		// 修改人：刘家清 修改日期：2007-11-20上午09:18:50 修改原因：当条码录入时，批次号可能会和批次参照上的不一致，就返回
		/*
		 * if(voLot == null || voLot.length == 0 || voLot[voLot.length -
		 * 1]==null || !voLot[voLot.length - 1].getVbatchcode().equals(sLot))
		 * return;
		 */
    
//		if (!getLotNumbRefPane().isClicked()) {
//			LotNumbRefVO vosel = getLotNumbRefPane().getLotNumbRefVO();
//			if (vosel != null)
//				synlot(vosel, rownum);
//			return;
//		}
		
    if(voLot!=null && voLot.length>0)
      synlot(voLot[0], rownum);

		if (voLot != null && voLot.length > 1) {
			for (int j = 1; j < voLot.length; j++) {
				getBillCardPanel().copyLine();
				voBillCopyLine();
				int iRowCount = getBillCardPanel().getBodyPanel()
						.getTableModel().getRowCount();
				getBillCardPanel().pasteLine();
				// 增加的行数
				iRowCount = getBillCardPanel().getBodyPanel().getTableModel()
						.getRowCount()
						- iRowCount;
				nc.ui.scm.pub.report.BillRowNo.pasteLineRowNo(
						getBillCardPanel(), getBillType(), IItemKey.CROWNO,
						iRowCount);
				voBillPastLine();

				int iSelrow = getBillCardPanel().getBillTable()
						.getSelectedRow() - 1;
				if (batchvos != null)
					BatchCodeDefSetTool.setBatchCodeInfo(batchvos[j],
							getBillCardPanel(), iSelrow, getM_voBill()
									.getItemVOs()[iSelrow]);
				synlot(voLot[j], iSelrow);
				// 如果是出库处理界面，那么行号就要重新生成。
				if ("40080822".equals(getFunctionNode())) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel()
							.setBodyValueAt(crowno, iSelrow, "crowno");
					getM_voBill().setItemValue(iSelrow, "crowno", crowno);
					getBillCardPanel().setBodyValueAt(null, iSelrow,
							"cgeneralbid");
					getM_voBill().setItemValue(iSelrow, "cgeneralbid", null);
				}
				//修改人：刘家清 修改时间：2008-9-11 上午10:55:29 修改原因：选取多批次增行时，应发应收数量置空。
				getBillCardPanel().setBodyValueAt(null, iSelrow, "nshouldinnum");
				getBillCardPanel().setBodyValueAt(null, iSelrow, "nneedinassistnum");
				getBillCardPanel().setBodyValueAt(null, iSelrow, "nshouldoutnum");
				getBillCardPanel().setBodyValueAt(null, iSelrow, "nshouldoutassistnum");
				getM_voBill().setItemValue(iSelrow, "nshouldinnum", null);
				getM_voBill().setItemValue(iSelrow, "nneedinassistnum", null);
				getM_voBill().setItemValue(iSelrow, "nshouldoutnum", null);
				getM_voBill().setItemValue(iSelrow, "nshouldoutassistnum", null);
			}
		}
		
	}

	/**
	 * 创建者：仲瑞庆 功能：换算率修改 参数： 返回： 例外： 日期：(2001-11-20 14:01:52) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param row
	 *            int
	 */
	public void afterHslEdit(nc.ui.pub.bill.BillEditEvent e) {
		// 行，选中表头字段时为-1
		int rownum = e.getRow();

		boolean isFixFlag = isFixFlag(rownum);

		String[] sfields = new String[] { "castunitid", "hsl",
				getEnvironment().getAssistNumItemKey(),
				getEnvironment().getNumItemKey() };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields,
				isFixFlag);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		sfields = new String[] { "castunitid", "hsl", "ngrossastnum",
				"ngrossnum" };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields,
				true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		sfields = new String[] { "castunitid", "hsl", "ntareastnum", "ntarenum" };
		GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum, sfields,
				true);
		GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(), sfields,
				rownum);

		if (!isInitBill()) {

			sfields = new String[] { "castunitid", "hsl",
					getEnvironment().getShouldAssistNumItemKey(),
					getEnvironment().getShouldNumItemKey() };
			GeneralBillUICtl.changeNum(getBillCardPanel(), "hsl", rownum,
					sfields, isFixFlag);
			GeneralBillUICtl.synUi2Vo(getBillCardPanel(), getM_voBill(),
					sfields, rownum);

		}

		afterNumEdit_1(rownum);
	}

	/**
	 * ?user> 功能： 参数： 返回： 例外： 日期：(2005-2-1 14:41:41) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterOnRoadEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();

		UFBoolean bonroadflag = new UFBoolean(getBillCardPanel()
				.getBodyValueAt(iSelrow, e.getKey()).toString());

		// 对应单据号
		getBillCardPanel().setBodyValueAt(null, iSelrow, IItemKey.CORRESCODE);
		// 对应单据类型
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
		// 对应单据表头OID
		// 单据模板库中表体位置两个不显示列ccorrespondhid,ccorrespondbid,以保存带出的对应表头，表体OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
		// 对应单据表体OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondbid");
		getM_voBill().setItemValue(iSelrow, "ccorrespondbid", null);
		// getICCorBillRef().getCorBillBid());
		getM_voBill().setItemValue(iSelrow, "ccorrespondhid", null);
		// getICCorBillRef().getCorBillHid());
		getM_voBill().setItemValue(iSelrow, IItemKey.CORRESCODE, null);
		// getICCorBillRef().getCorBillCode());
		getM_voBill().setItemValue(iSelrow, "ccorrespondtype", null);
		// getICCorBillRef().getCorBillType());
		// clearLocSnData(iSelrow,e.getKey());
		if (!bonroadflag.booleanValue()) {
			getM_voBill().setItemValue(e.getRow(), e.getKey(),
					new UFBoolean("N"));

		} else {
			getM_voBill().setItemValue(e.getRow(), e.getKey(),
					new UFBoolean("Y"));

		}

	}

	/**
	 * 此处插入方法说明。 功能： 参数： 返回： 例外： 日期：(2001-11-21 14:47:40) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @param num
	 *            nc.vo.pub.lang.UFDouble
	 * @param assistnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param row
	 *            int
	 * 修改人：刘家清 修改时间：2008-9-1 下午02:32:31 修改原因：修改数量不清除序列号。
	 * 修改人：刘家清 修改时间：2008-10-6 上午10:20:02 修改原因：如果数量为空的话，清空序列号。
	 */
	protected void resetSpace(int row) {
		UFDouble assistnum = null;
		try {
			assistnum = (UFDouble) getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getAssistNumItemKey());
		} catch (Exception e) {
		}
		UFDouble num = null;
		try {
			num = (UFDouble) getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getNumItemKey());
		} catch (Exception e) {
		}
		UFDouble ngrossnum = null;
		try {
			ngrossnum = (UFDouble) getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getGrossNumItemKey());
		} catch (Exception e) {
		}

//		修改人：刘家清 修改时间：2008-10-30 下午08:12:49 修改原因：修改仓库后，可能把货位VO清空，所以没有的话没有。
		LocatorVO[] voLoc = null;		
		if (null != getAlLocatorData() && row < getAlLocatorData().size()){
			voLoc = (LocatorVO[]) getAlLocatorData().get(row);

		if (voLoc != null && voLoc.length == 1) {

			// 辅数量
			if (assistnum == null) {
				voLoc[0].setNinspaceassistnum(assistnum);
				voLoc[0].setNoutspaceassistnum(assistnum);
			} else {
				if (getInOutFlag() > 0) {
					voLoc[0].setNinspaceassistnum(assistnum);
					voLoc[0].setNoutspaceassistnum(null);
				} else {
					voLoc[0].setNinspaceassistnum(null);
					voLoc[0].setNoutspaceassistnum(assistnum);
				}
			}
			// 数量
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (getAlSerialData() != null)
					getAlSerialData().set(row, null);
			} else {
				if (getInOutFlag() > 0) {
					// 入库
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// 出库
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					/*if (getAlSerialData() != null)
						getAlSerialData().set(row, null);*/
				}
				
				if (UFDouble.ZERO_DBL.compareTo(num) == 0 &&getAlSerialData() != null)
					getAlSerialData().set(row, null);
			}
			// 毛重
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				/*if (getAlSerialData() != null)
					getAlSerialData().set(row, null);*/
			} else {
				if (getInOutFlag() > 0) {
					// 入库
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// 出库
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} else
			getAlLocatorData().set(row, null);
		}

	}

	protected void afterBillCodeEdit(BillEditEvent e) {
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue(
					"vbillcode",
					getBillCardPanel().getHeadItem("vbillcode")
							.getValueObject());
	}

	protected void afterDispatcherEdit(BillEditEvent e) {
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cdispatcherid").getComponent()).getRefName();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdispatchername", sName);
	}

	protected void afterCinventoryidEdit(BillEditEvent e) {
		// 加工品
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cinventoryid").getComponent()).getRefName();
		String sID=((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cinventoryid").getComponent()).getRefPK();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null){
			getM_voBill().setHeaderValue("cinventoryname", sName);
			getM_voBill().setHeaderValue("cinventoryid", sID);
		}
		

	}

	protected void afterWhsmanagerEdit(BillEditEvent e) {

		// 库管员

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cwhsmanagerid").getComponent()).getRefName();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cwhsmanagername", sName);

	}

	protected void afterBiztypeEdit(BillEditEvent e) {
		// 业务类型

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cbiztype").getComponent()).getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cbiztype").getComponent()).getRefPK();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cbiztypename", sName);
		// 根据业务类型带出默认的收发类别 updated by cqw after v2.30
		if (sPK != null) {
			String sReceiptID = execFormular(
					"getColValue(bd_busitype,receipttype,pk_busitype,pk_busitype)",
					sPK);
			if (sReceiptID != null && sReceiptID.trim().length() > 0) {
				BillItem it = getBillCardPanel().getHeadItem("cdispatcherid");
				if (it != null && it.getValueObject() == null)
					it.setValue(sReceiptID);
			}
		}
	}

	protected void afterDilivertypeEdit(BillEditEvent e) {
		// 发运方式

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cdilivertypeid").getComponent()).getRefName();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdilivertypename", sName);

	}

	protected void afterOtherWHEdit(BillEditEvent e) {
		// DW 2005-05-31 在改变其它仓库时维护其它库存组织其它公司
		try {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("cotherwhid").getComponent()).getRefName();
			String sCode = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("cotherwhid").getComponent()).getRefPK();
			// 调拨单特殊处理 不清公司和库存组织
			if (getBillType() != "4E" && getBillType() != "4Y") {

				getM_voBill().setHeaderValue("cothercalbodyid", null);
				getM_voBill().setHeaderValue("cothercorpid", null);
				if (sCode != null)
					getM_voBill().setHeaderValue("cothercorpid",
							getEnvironment().getCorpID());
			}

			if (getM_voBill() != null && sCode != null) {
				getM_voBill().setHeaderValue("cotherwhname", sName);
				WhVO voWh = getClientUI().getInvoInfoBYFormula().getWHVO(sCode,
						false);

				if (voWh.getPk_calbody() != null)
					getM_voBill().setHeaderValue("cothercalbodyid",
							voWh.getPk_calbody());
			}
		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	protected void afterCountNumEdit(BillEditEvent e) {
		int row = e.getRow();
		getM_voBill().setItemValue(row, "ncountnum",
				getBillCardPanel().getBodyValueAt(row, "ncountnum"));
	}

	protected void afterScrqEdit(BillEditEvent e) {
		int row = e.getRow();
		nc.vo.scm.ic.bill.InvVO voInv = getM_voBill().getItemInv(row);
		if (voInv != null && voInv.getIsValidateMgt() != null
				&& voInv.getIsValidateMgt().intValue() == 1) {
			nc.vo.pub.lang.UFDate dScrq = null;
			if (e.getValue() != null && !"".equals(e.getValue().toString())
					&& nc.ui.ic.pub.tools.GenMethod.isAllowDate(e.getValue()
							.toString())) {
				dScrq = new nc.vo.pub.lang.UFDate(e.getValue().toString());
				getBillCardPanel().setBodyValueAt(InvVO.calcQualityDate(
            dScrq, voInv.getQualityperiodunit(), voInv.getQualityDay())
						, row, "dvalidate");
			}
		}
	}

	protected void afterDvalidateEdit(BillEditEvent e) {
		int row = e.getRow();
		nc.vo.scm.ic.bill.InvVO voInv = getM_voBill().getItemInv(row);
		if (voInv != null && voInv.getIsValidateMgt() != null
				&& voInv.getIsValidateMgt().intValue() == 1) {
			nc.vo.pub.lang.UFDate dvalidate = null;
			if (e.getValue() != null && !"".equals(e.getValue().toString())
					&& nc.ui.ic.pub.tools.GenMethod.isAllowDate(e.getValue()
							.toString())) {
				dvalidate = new nc.vo.pub.lang.UFDate(e.getValue().toString());
				// 修改人：刘家清 修改日期：2007-7-9下午04:30:30
				// 修改原因：生产日期、失效日期都有值时，变生产日期算失效日期，变失效日期不要去算生产日期(通过保持期)
				/*if (getBillCardPanel().getBodyValueAt(row, "scrq") == null)
					getBillCardPanel().setBodyValueAt(
							dvalidate.getDateBefore(
									voInv.getQualityDay().intValue())
									.toString(), row, "scrq");*/
				if (getBillCardPanel().getBodyValueAt(row, "scrq") == null)
					getBillCardPanel().setBodyValueAt(
							InvVO.calcQualityScrqDate(
									dvalidate, voInv.getQualityperiodunit(), voInv.getQualityDay()), row, "scrq");
				
				/*getBillCardPanel().setBodyValueAt(InvVO.calcQualityDate(
			            dScrq, voInv.getQualityperiodunit(), voInv.getQualityDay())
									, row, "dvalidate");*/
				/*
				 * getBillCardPanel().setBodyValueAt( dvalidate.getDateBefore(
				 * voInv.getQualityDay().intValue()) .toString(), row, "scrq");
				 */
			}
		}

	}

	protected void afterNmnyEdit(BillEditEvent e) {
		int row = e.getRow();

		if (e.getValue() != null && e.getValue().toString().trim().length() > 0) {
			UFDouble nmny = new UFDouble(e.getValue().toString());
			Object ninnum = getBillCardPanel().getBodyValueAt(row,
					getEnvironment().getNumItemKey());
			if (ninnum != null && ninnum.toString().trim().length() > 0) {
				ninnum = new UFDouble(ninnum.toString().trim());
				if (((UFDouble) ninnum).doubleValue() != 0.0) {
					UFDouble nprice = new UFDouble(nmny.doubleValue()
							/ ((UFDouble) ninnum).doubleValue());
					getBillCardPanel().setBodyValueAt(nprice, row, "nprice");
				}

			}

		}

	}

	protected void afterProjectNameEdit(BillEditEvent e) {
		String sName = getRefJob().getRefName(); // uiref.getRefName();
		String sPK = getRefJob().getRefPK(); // uiref.getRefPK();
        //表体参照批填充处理 陈倪娜 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		getBillCardPanel()
				.setBodyValueAt(null, e.getRow(), "cprojectphasename");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphaseid");
		getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectname");
		getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectid");
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "cprojectname", sName);
			getM_voBill().setItemValue(e.getRow(), "cprojectid", sPK);

			getM_voBill().setItemValue(e.getRow(), "cprojectphasename", null);
			getM_voBill().setItemValue(e.getRow(), "cprojectphaseid", null);

		}

	}

	protected void afterProjectPhaseNameEdit(BillEditEvent e) {

		String sName = getRefJobPhase().getRefName(); // uiref.getRefName();
		String sPK = getRefJobPhase().getRefPK(); // uiref.getRefPK();
		//表体参照批填充处理 陈倪娜 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "cprojectphasename", sName);
			getM_voBill().setItemValue(e.getRow(), "cprojectphaseid", sPK);
			getBillCardPanel().setBodyValueAt(sName, e.getRow(),
					"cprojectphasename");
			getBillCardPanel().setBodyValueAt(sPK, e.getRow(),
					"cprojectphaseid");

		}

	}

	protected void afterVendorNameEdit(BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("vvendorname").getComponent()).getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("vvendorname").getComponent()).getRefPK();
		//表体参照批填充处理 陈倪娜 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		String sPk_cubasdoc = getPk_cubasdoc(sPK);

		getBillCardPanel().setBodyValueAt(sName, e.getRow(), "vvendorname");
		getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cvendorid");
		getBillCardPanel().setBodyValueAt(sPk_cubasdoc, e.getRow(),
				"pk_cubasdoc");

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "vvendorname", sName);
			getM_voBill().setItemValue(e.getRow(), "cvendorid", sPK);
			getM_voBill().setItemValue(e.getRow(), "pk_cubasdoc", sPk_cubasdoc);
		}

	}

	protected void afterCostObjectNameEdit(BillEditEvent e) {

		String costobjectname = null;
		String costobjectid = null;
		UIRefPane ref = (UIRefPane) getBillCardPanel().getBodyItem(
				"ccostobjectname").getComponent();
		if (ref != null) {
			costobjectname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("ccostobjectname").getComponent())
					.getRefName();
			costobjectid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("ccostobjectname").getComponent()).getRefPK();
			ref.setPK(null);
		}

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), "ccostobjectname",
					costobjectname);
			getM_voBill().setItemValue(e.getRow(), "ccostobject", costobjectid);
			getBillCardPanel().setBodyValueAt(costobjectname, e.getRow(),
					"ccostobjectname");
			getBillCardPanel().setBodyValueAt(costobjectid, e.getRow(),
					"ccostobject");

		}
		// ((UIRefPane)getBillCardPanel().getBodyItem("ccostobjectname").getComponent()).setPK(null);

	}

	protected void afterReceieveEdit(BillEditEvent e) {
		String vrevcustname = null;
		String creceieveid = null;
		if (getBillCardPanel().getBodyItem("vrevcustname").getComponent() != null) {

			vrevcustname = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vrevcustname").getComponent()).getRefName();
			creceieveid = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getBodyItem("vrevcustname").getComponent()).getRefPK();
		}

		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill()
					.setItemValue(e.getRow(), "vrevcustname", vrevcustname);
			getM_voBill().setItemValue(e.getRow(), "creceieveid", creceieveid);
			getBillCardPanel().setBodyValueAt(vrevcustname, e.getRow(),
					"vrevcustname");
			getBillCardPanel().setBodyValueAt(creceieveid, e.getRow(),
					"creceieveid");

		}
	}

	protected void afterFlargessEdit(BillEditEvent e) {

		UFBoolean ufFlargess = new UFBoolean(e.getValue().toString());
		getM_voBill().setItemValue(e.getRow(), "flargess", ufFlargess);
		// 如果是赠品清除单据上的单价和金额 add by hanwei 2004-6-24
		if (ufFlargess.booleanValue()) {
			if (getBillCardPanel().getBodyItem("nmny") != null) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "nmny");
			}
			if (getBillCardPanel().getBodyItem("nprice") != null) {
				getBillCardPanel().setBodyValueAt(null, e.getRow(), "nprice");
			}
		}

	}

	protected void afterCrownoEdit(BillEditEvent e) {
		int row = e.getRow();
		// row
		// no, after edit process.
		nc.ui.scm.pub.report.BillRowNo.afterEditWhenRowNo(getBillCardPanel(),
				e, getBillType());
		// 同步化VO
		getM_voBill().setItemValue(row, IItemKey.CROWNO,
				getBillCardPanel().getBodyValueAt(row, IItemKey.CROWNO));

	}

	protected void afterVuserDefEdit(BillEditEvent e) {
		int pos = e.getPos();
		String sItemKey = e.getKey();
		int row = e.getRow();
		if (pos == 0) {// 表头
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vuserdef".length());
			//addied by liuzy 2008-04-07 v5.02被注释掉，v5.03改造大基类被删除
			//现发现不应去掉，如果去掉会导致自定义项参照编辑后无法带出PK
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					sItemKey, sVdefPkKey);

			// 同步m_voBill
			getM_voBill().setHeaderValue(sItemKey,
					getBillCardPanel().getHeadItem(sItemKey).getValueObject());
			getM_voBill()
					.setHeaderValue(
							sVdefPkKey,
							getBillCardPanel().getHeadItem(sVdefPkKey)
									.getValueObject());
		} else if (pos == 1) {// 表体
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vuserdef".length());
			//addied by liuzy 2008-04-07 v5.02被注释掉，v5.03改造大基类被删除
			//现发现不应去掉，如果去掉会导致自定义项参照编辑后无法带出PK
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					row, sItemKey, sVdefPkKey);

			// 同步m_voBill
			getM_voBill().setItemValue(row, sItemKey,
					getBillCardPanel().getBodyValueAt(row, sItemKey));
			getM_voBill().setItemValue(row, sVdefPkKey,
					getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
		}
	}

	protected void afterVbcuserEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		int row = e.getRow();
		String sVdefPkKey = IItemKey.PK_DEFDOCBC
				+ sItemKey.substring(IItemKey.VBCUSER.length());

		// 单据表体使用：afterEditBody(BillModel billModel, int iRow,String
		// sVdefValueKey, String sVdefPkKey)
		DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row,
				sItemKey, sVdefPkKey);

		// 同步m_voBill
		getM_voBill().setItemValue(row, sItemKey,
				getBillCardPanel().getBodyValueAt(row, sItemKey));
		getM_voBill().setItemValue(row, sVdefPkKey,
				getBillCardPanel().getBodyValueAt(row, sVdefPkKey));
	}

	protected void afterQualityLevelNameEdit(BillEditEvent e) {
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent())
				.getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem(IItemKey.CQUALITYLEVELNAME).getComponent())
				.getRefPK();
		getBillCardPanel().setBodyValueAt(sName, e.getRow(),
				IItemKey.CQUALITYLEVELNAME);
		getBillCardPanel().setBodyValueAt(sPK, e.getRow(),
				IItemKey.CQUALITYLEVELID);
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null) {
			getM_voBill().setItemValue(e.getRow(), IItemKey.CQUALITYLEVELNAME,
					sName);
			getM_voBill().setItemValue(e.getRow(), IItemKey.CQUALITYLEVELID,
					sPK);
		}
	}

	protected void afterElseDefaultEdit(BillEditEvent e) {
		String sItemKey = e.getKey();
		int row = e.getRow();
		String sIdColName = null;
		nc.ui.pub.beans.UIRefPane ref = null;
		// 在表体
		if (getBillCardPanel().getBodyItem(sItemKey) != null) {

			sIdColName = getBillCardPanel().getBodyItem(sItemKey)
					.getIDColName();
			if (sIdColName != null
					&& getBillCardPanel().getBodyItem(sIdColName) != null
					&& (getBillCardPanel().getBodyItem(sItemKey).getComponent()) != null) {
				ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent());
				// 置pk
				getBillCardPanel().setBodyValueAt(ref.getRefPK(), row,
						sIdColName);
				// 显示name
				getBillCardPanel().setBodyValueAt(ref.getRefName(), row,
						sItemKey);
				// 同步m_voBill
				getM_voBill().setItemValue(row, sIdColName, ref.getRefPK());
				getM_voBill().setItemValue(row, sItemKey, ref.getRefName());
			} else {// if (getBillCardPanel().getBodyItem(sItemKey) != null) {
				Object ovalue = getBillCardPanel()
						.getBodyValueAt(row, sItemKey);
				getM_voBill().setItemValue(row, sItemKey, ovalue);
			}
		} else if (getBillCardPanel().getHeadItem(sItemKey) != null) {
			// 在表头
			sIdColName = getBillCardPanel().getHeadItem(sItemKey)
					.getIDColName();
			if (sIdColName != null
					&& getBillCardPanel().getHeadItem(sIdColName) != null
					&& getBillCardPanel().getHeadItem(sItemKey).getComponent() != null) {
				ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem(sItemKey).getComponent());
				// 置pk
				getBillCardPanel().getHeadItem(sIdColName).setValue(
						ref.getRefPK());
				//deleted by lirr 2009-02-12
			/*	// 显示name
				getBillCardPanel().getHeadItem(sItemKey).setValue(
						ref.getRefName());*/
				// getM_voBill().setHeaderValue(sItemKey,ref.getRefName());
			}
		}
	}

	/**
	 * 创建者：王乃军 功能：仓库改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e, String sNewWhName,
			String sNewWhID) {
		// 仓库
		try {
			getBillCardPanel().rememberFocusComponent();
			if (sNewWhID == null) {
				sNewWhName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem(IItemKey.WAREHOUSE).getComponent())
						.getRefName();
				sNewWhID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem(IItemKey.WAREHOUSE).getComponent())
						.getRefPK();
			} else {
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
						IItemKey.WAREHOUSE).getComponent())
						.setValue(sNewWhName);
				((nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
						IItemKey.WAREHOUSE).getComponent()).setPK(sNewWhID);
			}
			// 请空表体的显示数据
			try {
				if (sNewWhID == null
						|| (getM_voBill() != null && !sNewWhID
								.equals(getM_voBill().getHeaderVO()
										.getCwarehouseid()))) {
//		        	修改人：刘家清 修改时间：2008-10-30 下午07:15:32 修改原因：修改仓库时，要清除货位和序列号信息。
					String sIKs[] = new String[] {"locator","serial", "cspaceid", "vspacename",
							"ccorrespondbid", "ccorrespondcode",
							"ccorrespondhid", "ccorrespondtype",
							"nplannedprice", "nplannedmny" };
					
			          //修改人：刘家清 修改时间：2008-10-30 下午07:15:32 修改原因：修改仓库时，要清除货位和序列号信息。
			        getClientUI().setM_alLocatorData(null);  
			        getClientUI().setM_alLocatorDataBackup(null);  
			        getClientUI().setM_alSerialData(null); 
			        getClientUI().setM_alSerialDataBackup(null);

					int iRowCount = getBillCardPanel().getRowCount();
					for (int row = 0; row < iRowCount; row++)
						getClientUI().clearRowData(row, sIKs);
				}
				// if (iRowCount>0)
				// getBillCardPanel().getBillTable().setRowSelectionInterval(0,
				// 0);
			} catch (Exception e2) {
				SCMEnv.out("可以忽略的错误：" + e2);
			}
			// 清空了仓库
			if (sNewWhID == null) {
				// 清空批次号参照的仓库
				getLotNumbRefPane().setWHParams(null);
				if (getM_voBill() != null)
					getM_voBill().setWh(null);
			} else {

				// 保存名称以在列表形式下显示。
				// 查询仓库信息
				// 查询方式：如果已经录入了存货，需要同时查计划价。
				int iQryMode = QryInfoConst.WH;
				// 参数
				Object oParam = sNewWhID;
				// 当前已录入的存货数据
				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getClientUI().getCurInvID(alAllInvID);

				// 仓库
				WhVO voWh = null;
				// 清空批次号参照的仓库
				getLotNumbRefPane().setWHParams(null);
				if (getM_voBill() != null)
					getM_voBill().setWh(null);

				if (bHaveInv) {
					// 参数：仓库ID,原库存组织ID,单位ID,存货ID
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = QryInfoConst.WH_PLANPRICE;
					// 当前的库存组织,考虑没有仓库的情况。
					if (getM_voBill() != null && getM_voBill().getWh() != null)
						alParam.add(getM_voBill().getWh().getPk_calbody());
					else
						alParam.add(null);
					// 公司
					alParam.add(getEnvironment().getCorpID());
					// 当前的存货
					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(
						new Integer(iQryMode), oParam);
				// Object oRet = invokeClient("queryInfo", new Class[] {
				// Integer.class, Object.class }, new Object[] {
				// new Integer(iQryMode), oParam });

				// 当前已录入的存货数据,并且修改了库存组织才返回一个ArrayList
				if (oRet instanceof ArrayList) {
					ArrayList alRetValue = (ArrayList) oRet;
					if (alRetValue != null && alRetValue.size() >= 2) {
						voWh = (WhVO) alRetValue.get(0);
						// 刷新计划价
						getClientUI().freshPlanprice(
								(ArrayList) alRetValue.get(1));
					}
				} else
					// 否则返回 WhVO
					voWh = (WhVO) oRet;
				// 库存组织处理
				nc.ui.pub.bill.BillItem biCalBody = getBillCardPanel()
						.getHeadItem("pk_calbody");
				if (biCalBody != null) {
					String sNewID = null;
					if (voWh != null){
						biCalBody.setValue(voWh.getPk_calbody());
						sNewID = voWh.getPk_calbody();
					}
					else
						biCalBody.setValue(null);
					
					//修改人：刘家清 修改时间：2008-11-12 下午03:58:37 修改原因：当选择库存组织后，工作中心要根据库存组织过滤，并且清除表体工作中心。
					nc.ui.pub.bill.BillItem cworkcentername = getBillCardPanel().getBodyItem("cworkcentername");
					String[] sReserveds = null;
					if (null != sNewID && !"".equals(sNewID))
						sReserveds = new String[]{" and pd_wk.gcbm = '"+sNewID+"'"};
					if (RefFilter.filtWorkCenter(cworkcentername, getEnvironment().getCorpID(), sReserveds) && null != sNewID && !"".equals(sNewID))
						if (getBillCardPanel().getRowCount() > 0 ){
							for (int i = 0 ;i < getBillCardPanel().getRowCount() ; i++){
								getBillCardPanel().setBodyValueAt(null, i, "cworkcenterid");
								getBillCardPanel().setBodyValueAt(null, i, "cworkcentername");
							}
						}

				}
				nc.ui.pub.bill.BillItem biCalBodyname = getBillCardPanel()
						.getHeadItem("vcalbodyname");
				if (biCalBodyname != null) {
					if (voWh != null)
						biCalBodyname.setValue(voWh.getVcalbodyname());
					else
						biCalBodyname.setValue(null);
				}

				if (getM_voBill() != null) {
					getM_voBill().setWh(voWh);
					// 清表尾现存量
					getM_voBill().clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);

					// DUYONG 刷新计划价
					// 修改人：刘家清 修改时间：2009-8-31 下午03:58:00 修改原因：前面已经刷新计划价了，所以此外不需要刷新计划价。
/*					getClientUI().freshPlanprice(
							getClientUI().getInvoInfoBYFormula().getPlanPrice(
									alAllInvID, voWh.getPk_corp(),voWh.getPk_calbody(),
									voWh.getCwarehouseid()));*/
				}

				// 刷新现存量显示
				getClientUI().setTailValue(0);
				// 设置货位分配按钮是否可用。
				getClientUI().setBtnStatusSpace(true);
			}

			getBillCardPanel().restoreFocusComponent();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * 创建者：王乃军 功能：库存组织改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 修改人：刘家清 修改时间：2008-11-12 下午03:58:37 修改原因：当选择库存组织后，工作中心要根据库存组织过滤，并且清除表体工作中心。
	 * 
	 */
	public void afterCalbodyEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem(e.getKey()).getComponent()).getRefPK();
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			RefFilter.filtWh(bi, getEnvironment().getCorpID(), getClientUI().getFilterWhString(sNewID));
			// 处理:clear warehouse
			WhVO whvo = getM_voBill().getWh();
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			if (biWh != null
					&& (whvo == null || (sNewID != null && !sNewID.equals(whvo
							.getPk_calbody()))))
				biWh.setValue(null);
			
			//修改人：刘家清 修改时间：2008-11-12 下午03:58:37 修改原因：当选择库存组织后，工作中心要根据库存组织过滤，并且清除表体工作中心。
			nc.ui.pub.bill.BillItem cworkcentername = getBillCardPanel().getBodyItem("cworkcentername");
			String[] sReserveds = null;
			if (null != sNewID && !"".equals(sNewID))
				sReserveds = new String[]{" and pd_wk.gcbm = '"+sNewID+"'"};
			if (RefFilter.filtWorkCenter(cworkcentername, getEnvironment().getCorpID(), sReserveds) && null != sNewID && !"".equals(sNewID))
				if (getBillCardPanel().getRowCount() > 0 ){
					for (int i = 0 ;i < getBillCardPanel().getRowCount() ; i++){
						getBillCardPanel().setBodyValueAt(null, i, "cworkcenterid");
						getBillCardPanel().setBodyValueAt(null, i, "cworkcentername");
					}
				}


			// 过滤成本对象
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * 此处插入方法说明。 存货参照多选后设置界面 用于存货多选和条码扫描两种场合 创建日期：(2004-5-7 12:40:43)
	 * 
	 * @param invVOs
	 *            nc.vo.scm.ic.bill.InvVO[]：存货VO iRow：当前行
	 *            sItemKey：当前行的Key:"cinventorycode"
	 */
	public void afterInvMutiEditSetUI(InvVO[] invVOs, int iRow, String sItemKey) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEditSetUI:"
				+ sItemKey + "个数：" + invVOs.length);

		// 界面增行
		boolean isLastRow = false;

		int iLen = invVOs.length;
		if (invVOs != null && invVOs.length > 0) {
			if (iRow == getBillCardPanel().getRowCount() - 1)
				isLastRow = true;

			for (int i = iLen - 1; i >= 0; i--) {
				if (i < iLen - 1) {
					getBillCardPanel().insertLine();
				} else {
					if (getBillCardPanel().getBillModel().getRowState(iRow) == BillModel.NORMAL)
						getBillCardPanel().getBillModel().setRowState(iRow,
								BillModel.MODIFICATION);

				}
			}

			if (isLastRow) {
				nc.ui.scm.pub.report.BillRowNo.addLineRowNos(
						getBillCardPanel(), getBillType(), IItemKey.CROWNO,
						iLen);
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					getM_voBill().setItemValue(i, "crowno", getBillCardPanel().getBodyValueAt(i, "crowno"));
				}

			} else{
				nc.ui.scm.pub.report.BillRowNo.insertLineRowNos(
						getBillCardPanel(), getBillType(), IItemKey.CROWNO,
						iRow + iLen - 1, iLen - 1);
				for (int i = 0; i < getBillCardPanel().getRowCount(); i++) {
					getM_voBill().setItemValue(i, "crowno", getBillCardPanel().getBodyValueAt(i, "crowno"));
				}
			}

		}

		// v5 lj 集中采购快速录入
		getClientUI().setBodyDefaultData(iRow, iLen);

		// 设置界面数据
		boolean bHasSourceBillTypecode = false;
		if (getSourBillTypeCode() == null
				|| getSourBillTypeCode().trim().length() == 0) {
			bHasSourceBillTypecode = false;
		} else
			bHasSourceBillTypecode = true;

		int iCurRow = 0;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		// zhy2005-08-24表头供应商应写到表体
		String sHeadProviderName = null;
		String sHeadProviderID = null;
		String sPk_cusbasdoc = null;
		UIRefPane ref = null;
		if (getBillCardPanel().getHeadItem("cproviderid") != null)
			ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanel().getHeadItem(
					"cproviderid").getComponent();
		if (ref != null) {
			sHeadProviderName = ref.getRefName();
			sHeadProviderID = ref.getRefPK();
		}

		// 获得单据头界面中的供应商基本档案ID
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		Object oPk_cubasdoc = null;
		if (iPk_cubasdoc != null)
			oPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc")
					.getValueObject();

		for (int i = 0; i < iLen; i++) {
			iCurRow = iRow + i;
			// 清批次/自由项等数据
			// 必须放在“m_voBill.setItemInv”代码之前 add by ydy 2003-12-17.01
			getClientUI().clearRowData(0, iCurRow, sItemKey);
			// 表体
			getClientUI().setBodyInvValue(iCurRow, invVOs[i]);

			if (getM_voBill() != null) {
				getM_voBill().setItemInv(iCurRow, invVOs[i]);
				getM_voBill().setItemValue(iCurRow, IItemKey.NAME_HEADID,
						getM_voBill().getHeaderValue(IItemKey.NAME_HEADID));
				// zhy2005-08-24
				getM_voBill().setItemValue(iCurRow, "cvendorid",
						sHeadProviderID);
				getM_voBill().setItemValue(iCurRow, "vvendorname",
						sHeadProviderName);
				getM_voBill()
						.setItemValue(iCurRow, "pk_cubasdoc", oPk_cubasdoc);
				getBillCardPanel().setBodyValueAt(sHeadProviderID, iCurRow,
						"cvendorid");
				getBillCardPanel().setBodyValueAt(sHeadProviderName, iCurRow,
						"vvendorname");
				if (getBillCardPanel().getBodyItem("pk_cubasdoc") != null
						&& oPk_cubasdoc != null)
					getBillCardPanel().setBodyValueAt(oPk_cubasdoc, iCurRow,
							"pk_cubasdoc");
			}

			if (bHasSourceBillTypecode) {
				// 清应数量
				// 同步vo
				if (getM_voBill() != null) {
					getM_voBill().setItemValue(iCurRow,
							getEnvironment().getShouldNumItemKey(), null);
					getM_voBill().setItemValue(iCurRow,
							getEnvironment().getShouldAssistNumItemKey(), null);
				}
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, iCurRow,
							getEnvironment().getShouldNumItemKey());
				if (getBillCardPanel().getBodyItem(
						getEnvironment().getShouldAssistNumItemKey()) != null)
					getBillCardPanel().setBodyValueAt(null, iCurRow,
							getEnvironment().getShouldAssistNumItemKey());
			}
			//修改人：刘家清 修改时间：2008-7-24 下午04:00:14 修改原因：参数IC026"入库货位参照的优先顺序"增加选项"上次入库货位"。
			// 如果参数选择此选项，则生成入库单时自动将本仓库+本存货的上次入库货位自动带入"货位"栏目，可以手工修改。上次入库货位取表体入库日期最新的入库单的入库货位。
			getClientUI().setBodyInSpace(iCurRow, invVOs[i]);
			//修改人：陈倪娜 2009-09-03 将表头加工品带入表体加工品字段
			String cinventoryname=(String) getM_voBill().getHeaderValue("cinventoryname");
			String cinventoryid=(String) getM_voBill().getHeaderValue("cinventoryid");
			if (null != cinventoryname && !"".equals(cinventoryname)) {
		           if (null == getBillCardPanel().getBodyValueAt(iCurRow,
						"ccostobjectname")
						|| "".equals((String) getBillCardPanel()
								.getBodyValueAt(iCurRow, "ccostobjectname"))) {
		        	   getBillCardPanel().setBodyValueAt(cinventoryid, iCurRow,
                 "ccostobject");
		        	   getBillCardPanel().setBodyValueAt(cinventoryname, iCurRow,
                 "ccostobjectname");
		        	   getM_voBill().setItemValue(iCurRow, "ccostobjectname", cinventoryname);
		        	   getM_voBill().setItemValue(iCurRow, "ccostobject", cinventoryid);
		        	   
		        	   
				}
			}
		}
		// 设置当前行序列号是否可用
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

		// 修改人：刘家清 修改时间：2009-9-2 下午03:55:17 修改原因：减少联接数，不处理此种情况。
		//getClientUI().setTailValue(iRow);
		getClientUI().setBtnStatusSN(iRow, true);

		// 设置当前选中行的现存量参照
		if (getClientUI().isM_bOnhandShowHidden())
			getClientUI().showOnHandPnlInfo(iRow);
		// 动态设置界面
		getClientUI().getOnHandRefDeal().setInvCtrlValue(iRow);
		getClientUI().setCardMode();
		getClientUI().getM_layoutManager().show();

	}

	/**
	 * 创建者：王乃军 功能：存货事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 */
	public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEdit:"
				+ e.getKey());
		long ITimeAll = System.currentTimeMillis();

		int row = e.getRow();
		// 字段itemkey
		String sItemKey = e.getKey();
		getM_voBill().setItemValue(row, "desainfo", null);
		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("cinventorycode").getComponent();
		// 管理档案PK
    String[] refPks = invRef.getRefPKs();
    if(getClientUI().isLineCardEdit() && refPks.length>1){
      refPks = new String[]{refPks[0]};
    }
    //处理批修改或行编辑
//    if(!invRef.getUITextField().isFocusOwner()){
//    //if(refPks==null || refPks.length<=0){
//      if(getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid")!=null){
//        refPks = new String[]{(String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid")};
//        invRef.setPK(refPks[0]);
//      }
//    }
		// 如果返回为空，清空当前环境
		if (refPks == null || refPks.length == 0) {

			// 清除当前行的条码VO,清除界面的条码相关标志
			// 修改人：刘家清 修改日期：2007-8-27下午04:54:01 修改原因：存货变为空时，也得处理条码
			BarCodeVO[] bcVOs = getM_voBill().getItemVOs()[row].getBarCodeVOs();
			if (bcVOs != null && getClientUI().getM_utfBarCode() != null) {
				getClientUI().getM_utfBarCode().setRemoveBarcode(bcVOs);
				for (int i = 0; i < bcVOs.length; i++) {
					bcVOs[i].setStatus(nc.vo.pub.VOStatus.DELETED);

				}
			}
			getM_voBill().getItemVOs()[row].setBarcodeClose(new UFBoolean("N"));
			getM_voBill().getItemVOs()[row].setAttributeValue(
					IItemKey.NBARCODENUM, new UFDouble(0.0));

			getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE, row,
					"bbarcodeclose");
			getBillCardPanel().getBillModel().setValueAt(null, row,
					IItemKey.NBARCODENUM);

			nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("参照返回的存货个数getRefPKs:0");
			getClientUI().clearRow(row);

			return;
		}
		invRef.setPK(null);

		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("参照返回的存货个数getRefPKs:"
				+ refPks.length);
		// 仓库和库存组织信息
		String sWhID = null;
		String sCalID = null;
		if (getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE) != null) {
			sWhID = (String) getBillCardPanel().getHeadItem(IItemKey.WAREHOUSE)
					.getValueObject();
			sCalID = (String) getBillCardPanel().getHeadItem(IItemKey.CALBODY)
					.getValueObject();
		}
		long ITime = System.currentTimeMillis();
		if (sCalID == null && sWhID != null) {
			try {
				Object[] ov = (Object[]) nc.ui.scm.pub.CacheTool.getCellValue(
						"bd_stordoc", "pk_stordoc", "pk_calbody", sWhID);
				if (ov != null && ov.length > 0)
					sCalID = ov[0].toString();
			} catch (Exception e1) {
				SCMEnv.out(e1.toString());
			}
		}

		SCMEnv.showTime(ITime, "存货解析参数设置:");

		ITime = System.currentTimeMillis();
		// 存货解析
		// DUYONG 此处需要验证是否需要进行计划价的处理（经过验证，此处已经进行了处理）
		InvVO[] invVOs = getClientUI().getInvoInfoBYFormula()
				.getInvParseWithPlanPriceAndPackType(refPks, sWhID, sCalID,getClientUI().getEnvironment().getCorpID(), true, true);

		SCMEnv.showTime(ITime, "存货解析:");
		InvVO[] invvoBack = new InvVO[invVOs.length];
		for (int i = 0; i < invVOs.length; i++) {
			invVOs[i].setPk_corp(getEnvironment().getCorpID());
			invvoBack[i] = (InvVO) invVOs[i].clone();
		}

		ITime = System.currentTimeMillis();
		// 处理件数
/*		try {
			QueryInfo info = new QueryInfo();
			invVOs = info.dealPackType(invVOs);
		} catch (Exception e1) {
			nc.vo.scm.pub.SCMEnv.error(e1);
			invVOs = invvoBack;
		}*/

		// 清除当前行的条码VO,清除界面的条码相关标志
		BarCodeVO[] bcVOs = getM_voBill().getItemVOs()[row].getBarCodeVOs();

		if (bcVOs != null && getClientUI().getM_utfBarCode() != null) {
			getClientUI().getM_utfBarCode().setRemoveBarcode(bcVOs);
			for (int i = 0; i < bcVOs.length; i++) {
				bcVOs[i].setStatus(nc.vo.pub.VOStatus.DELETED);

			}
		}
		getM_voBill().getItemVOs()[row].setBarcodeClose(new UFBoolean("N"));
		getM_voBill().getItemVOs()[row].setAttributeValue(IItemKey.NBARCODENUM,
				new UFDouble(0.0));

		getBillCardPanel().getBillModel().setValueAt(UFBoolean.FALSE, row,
				"bbarcodeclose");
		getBillCardPanel().getBillModel().setValueAt(null, row,
				IItemKey.NBARCODENUM);

		afterInvMutiEditSetUI(invVOs, row, sItemKey);
		SCMEnv.showTime(ITimeAll, "存货参照多选:");

	}

	/**
	 * 创建者：王乃军 功能：自由项改变事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterFreeItemEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {

			InvVO voInv = (InvVO) getBillCardPanel().getBodyValueAt(e.getRow(),
					"invvo");
			FreeVO oldfreevo = voInv == null ? null : voInv.getFreeItemVO();

			FreeVO voFree = getClientUI().getFreeItemRefPane().getFreeVO();

			// 修改人：刘家清 修改日期：2007-11-13下午01:58:48 修改原因：当自由项vfree0为空时，应该清空自由项
			if (null == getBillCardPanel().getBodyValueAt(e.getRow(), "vfree0")
					|| "".equals(getBillCardPanel().getBodyValueAt(e.getRow(),
							"vfree0").toString()))
				for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++)
					voFree.setAttributeValue("vfree" + i, null);

			getM_voBill().setItemFreeVO(e.getRow(), voFree);
			for (int i = 1; i <= FreeVO.FREE_ITEM_NUM; i++) {
				if (getBillCardPanel().getBodyItem("vfree" + i) != null)
					if (voFree != null) {
						getBillCardPanel().setBodyValueAt(
								voFree.getAttributeValue("vfree" + i),
								e.getRow(), "vfree" + i);
						getM_voBill().setItemValue(e.getRow(), "vfree" + i,
								voFree.getAttributeValue("vfree" + i));
						if (oldfreevo != null)
							oldfreevo.setAttributeValue("vfree" + i, voFree
									.getAttributeValue("vfree" + i));
					} else {
						getBillCardPanel().setBodyValueAt(null, e.getRow(),
								"vfree" + i);
						getM_voBill().setItemValue(e.getRow(), "vfree" + i,
								null);
					}
			}
			// InvVO voInv = (InvVO)
			// getBillCardPanel().getBodyValueAt(e.getRow(),
			// "invvo");
			if (voInv != null) {
				voInv.setFreeItemVO(oldfreevo == null ? voFree : oldfreevo);
				getBillCardPanel().setBodyValueAt(voInv, e.getRow(), "invvo");
			}
			// 清空相关的数据
			// 清空相关的数据
			/*
			 * if(!BillTypeConst.m_purchaseIn.equals(this.getBillType()))
			 * clearRowData(0, e.getRow(), "vfree0");
			 */
			// 修改人：刘家清 修改日期：2007-9-19下午01:53:07 修改原因：根据谢阳、刘辉讨论后，决定修改自由项不清空批次号等信息
			// clearRowData(0, e.getRow(), "vfree0");
			getClientUI().execEditFomulas(e.getRow(), e.getKey());

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * 创建者：余大英 功能：货位修改事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterSpaceEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.ui.pub.beans.UIRefPane refSpace = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("vspacename").getComponent());

		String cspaceid = refSpace.getRefPK();
		String csname = refSpace.getRefName();
		//表体参照批填充处理 刘家清 2009-08-05
		if (null == cspaceid && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			cspaceid = (String)((DefaultConstEnum)e.getValue()).getValue();
			csname = ((DefaultConstEnum)e.getValue()).getName();
		}


		setRowSpaceData(e.getRow(), cspaceid, csname);

	}

	/**
	 * 创建者：余大英
	 * 
	 * 功能：设置行的货位信息
	 * 
	 * 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void setRowSpaceData(int row, String cspaceid, String csname) {
		int rowcount = getBillCardPanel().getRowCount();
		if (rowcount < 0 || row >= rowcount) {
			SCMEnv.out("row e..");
			return;
		}
		getBillCardPanel().setBodyValueAt(csname, row, "vspacename");
		getBillCardPanel().setBodyValueAt(cspaceid, row, "cspaceid");

		// 如果分配货位，构造LocatorVO
		if (cspaceid != null && cspaceid.trim().length() > 0) {
			// resetSpace(row);

			LocatorVO voSpace = new LocatorVO();
			LocatorVO[] lvos = new LocatorVO[1];
			lvos[0] = voSpace;
			voSpace.setCspaceid(cspaceid);
			voSpace.setVspacename(csname);
			getAlLocatorData().remove(row);
			getAlLocatorData().add(row, lvos);
			
			/*getM_voBill().setItemValue(row, "cspaceid", cspaceid);
			getM_voBill().setItemValue(row, "csname", csname);*/
			if (null != getM_voBill() && null != getM_voBill().getItemVOs() && row < getM_voBill().getItemVOs().length)
				getM_voBill().getItemVOs()[row].setLocator(lvos);

			resetSpace(row);

			// 修改序列号数据
			if (getAlSerialData() != null) {
				SerialVO[] snvos = (SerialVO[]) getAlSerialData().get(row);

				if (snvos != null) {
					for (int i = 0; i < snvos.length; i++) {
						snvos[i].setAttributeValue("cspaceid", cspaceid);
						snvos[i].setAttributeValue("vspacename", csname);
					}
				}
			}

		} else {
			getAlLocatorData().remove(row);
			getAlLocatorData().add(row, null);
			getAlSerialData().set(row, null);
			if (null != getM_voBill() && null != getM_voBill().getItemVOs() && row < getM_voBill().getItemVOs().length){
				getM_voBill().getItemVOs()[row].setLocator(null);
				getM_voBill().getItemVOs()[row].setAttributeValue("cspaceid", null);
				getM_voBill().getItemVOs()[row].setAttributeValue("csname", null);
			}
		}

	}

	/**
	 * 创建者：王乃军 功能：清除对应行货位、序列号数据 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearLocSnData(int row, String sItemKey) {
		// 因为仓库修改后，row==-1,所以这里不检查row合法性
		if (sItemKey == null || sItemKey.length() == 0)
			return;
		// 提示信息
		String sHintMsg = "";
		// 是否清货位，序列号
		boolean bClearLoc = false, bClearSn = false;

		if (sItemKey.equals("bonroadflag")) {
			bClearLoc = true;
			// bClearSn=true;
		}

		// 仓库
		if (sItemKey.equals(IItemKey.WAREHOUSE)) {
			// 需要清空 ---------- 所有 ----------- 货位。!!!
			if (getAlLocatorData() != null && getAlSerialData() != null) {
				for (int q = 0; q < getAlLocatorData().size(); q++) {
					getAlLocatorData().set(q, null);
					// 序列号和货位相关，也应该全部清除
					getAlSerialData().set(q, null);
				}
			}
			if (getM_voBill() != null) {
				for (int q = 0; q < getM_voBill().getItemVOs().length; q++) {
					getM_voBill().getItemVOs()[q].setLocator(null);
					getM_voBill().getItemVOs()[q].setSerial(null);
				}
			}

		} else {
			// 仓库是否货位管理---非货位管理时在仓库改变时清空。
			boolean bIsLocatorMgt = getClientUI().isLocatorMgt();
			// 当前行是否序列号管理---无条件去掉。
			// boolean bIsSNmgt = isSNmgt(row);

			// 以下的判断不用else语句
			// 存货
			if (sItemKey.equals("cinventorycode")
					|| sItemKey.equals(getEnvironment().getNumItemKey())
					|| sItemKey.equals(getEnvironment().getAssistNumItemKey())
					|| sItemKey.equals(IItemKey.CORRESCODE)
					|| sItemKey.equals("vvendorname")
					|| sItemKey.equals("headprovider")) {
				// //非期初单时需要清货位。
				// if (!m_bIsInitBill)
				// bClearLoc = true;
				bClearSn = true;
			}
			// 自由项，批次
			if (!bClearSn
					&& (sItemKey.equals("vfree0") || sItemKey
							.equals("vbatchcode")))
				bClearSn = true;

			// 如果出库时，需要清序列号，就需要同时清货位
			// if(bClearSn&&m_voBill.getItemInv(row)!=null&&m_voBill.getItemInv(row).getInOutFlag()!=InOutFlag.IN)
			// bClearLoc=true;
			// //如果是货位管理并且需要清数据
			if (bIsLocatorMgt && bClearLoc) {
				if (getAlLocatorData() != null

				&& row >= 0 && row < getAlLocatorData().size()
						&& getAlLocatorData().get(row) != null) {
					getAlLocatorData().set(row, null);
					getBillCardPanel().setBodyValueAt(null, row, "vspacename");
					getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
					sHintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"common", "UC000-0003830")/* @res "货位" */;
				}
				if (getM_voBill() != null
						&& getM_voBill().getItemVOs().length > row
						&& getM_voBill().getItemVOs()[row] != null) {

					getM_voBill().getItemVOs()[row].setLocator(null);

				}

			}

			// ----------------- 如果是序列号管理并且需要清数据
			// bIsSNmgt
			// &&
			if (bClearSn) {
				if (getAlSerialData() != null && row >= 0
						&& row < getAlSerialData().size()

						&& getAlSerialData().get(row) != null) {
					getAlSerialData().set(row, null);
					// 如果清了货位
					if (sHintMsg != null && sHintMsg.length() > 0)
						sHintMsg = sHintMsg
								+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008bill", "UPP4008bill-000311")/*
																			 * @res
																			 * "和"
																			 */;
					sHintMsg = sHintMsg
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0001819")/* @res "序列号" */;
				}
				if (getM_voBill() != null
						&& null != getM_voBill().getItemVOs()
						&& getM_voBill().getItemVOs().length > row
						&& getM_voBill().getItemVOs()[row] != null) {

					getM_voBill().getItemVOs()[row].setSerial(null);
					 getBillCardPanel().setBodyValueAt(null, row,
      			   "vserialcode");
					

				}

			}
		}
		// if (sHintMsg != null && sHintMsg.length() > 0)
		// showHintMessage(
		// "清空了第 " + (row + 1) + " 行的" + sHintMsg + "数据，请重新执行" + sHintMsg +
		// "分配。");

	}

	/**
	 * 创建者：王乃军 功能：单据编辑事件处理 参数：e单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillEditEvent e) {

//		if (e.getKey().equals(IItemKey.CORRESCODE)
//				&& getClientUI().isCellEditable(true, e.getRow(), e.getKey())) {
//
//			nc.vo.scm.ic.bill.WhVO voWh = getM_voBill().getWh();
//			if (voWh == null)
//				voWh = getM_voBill().getWasteWh();
//
//			getClientUI().getICCorBillRef().setParam(voWh,
//					getM_voBill().getItemVOs()[e.getRow()]);
//		}

		return true;

	}

	/**
	 * beforeEdit 方法注解。[处理表头编辑前事件]
	 */
	public boolean beforeEdit(nc.ui.pub.bill.BillItemEvent e) {
		getBillCardPanel().stopEditing();
		String sItemKey = e.getItem().getKey();
		BillItem bi = e.getItem();
		if (getClientUI().getM_iMode() == BillMode.Browse)
			return false;
		if (!bi.isEdit()) {
			return false;
		}
		if (!bi.isEnabled())
			return false;

		// 仓库编辑前需要按照库存组织过滤
		if (sItemKey.equals(IItemKey.WAREHOUSE)) {
			String sCalID = getBillCardPanel().getHeadItem("pk_calbody") == null ? null
					: (String) getBillCardPanel().getHeadItem("pk_calbody")
							.getValueObject();
			if (sCalID != null) {
				RefFilter.filtWh(getBillCardPanel().getHeadItem(
						IItemKey.WAREHOUSE), getEnvironment().getCorpID(),
						getClientUI().getFilterWhString(sCalID));
			}
		}
		return true;

	}

	/**
	 * 创建者：王乃军 功能：单据体、列表上表编辑事件处理 参数：e 单据编辑事件 返回： 例外： 日期：(2001-5-8 19:08:05)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * 
	 * 
	 * 
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		// 列表形式下的表头行选中
		getBillCardPanel().rememberFocusComponent();
		int row = e.getRow();
		if (e.getSource() == getBillListPanel().getHeadTable()) {

			if (row < 0 || getM_alListData() == null
					&& row >= getM_alListData().size()) {
				SCMEnv.out(" row now ERR ");
				return;
			}
			// 如果未改变行则返回
			if (getClientUI().getM_iLastSelListHeadRow() == row)
				return;
			// 清无关数据
			getClientUI().setM_alLocatorData(null);
			getClientUI().setM_alSerialData( null);
			// 置表体排序主键为空
			getClientUI().m_sLastKey = null;

			// SCMEnv.out(" Line changed to " + row);
			// 改变对应的表体显示
			getClientUI().setLastHeadRow(row);
			getClientUI().selectListBill(
					getClientUI().getM_iLastSelListHeadRow());
			// 清除定位
			// clearOrientColor();
			// setBtnStatusSN(0);
			if (getClientUI().getM_funcExtend() != null) {
				// 支持功能扩展
				getClientUI().getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST,
						nc.ui.pub.bill.BillItem.HEAD);
			}
		} else if (e.getSource() == getBillListPanel().getBodyTable()) {
			getClientUI().setBtnStatusSN(row);
			if (getClientUI().getM_funcExtend() != null) {
				// 支持功能扩展
				getClientUI().getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST,
						nc.ui.pub.bill.BillItem.BODY);
			}
		}

		// 表单形式下的修改或选中。

		else if (e.getSource() == getBillCardPanel().getBillTable()) {
			if (row < 0)
				return;
			SCMEnv.out("line to " + e.getRow());
			getClientUI().setBtnStatusSN(row);
			getClientUI().setTailValue(row);
			if (getM_funcExtend() != null) {
				// 支持功能扩展
				getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.CARD,
						nc.ui.pub.bill.BillItem.BODY);
			}

			// v5 lj 增加自由项目着色器
			getClientUI().getFreeItemCellRender().setRenderer("vfree0");
			getClientUI().getLotRefCellRender().setRenderer("vbatchcode");

			// 刷新现存量Panel显示
			if (getClientUI().isM_bOnhandShowHidden()) {
				getClientUI().showOnHandPnlInfo(e.getRow());
			}
			// 显示
			getClientUI().getOnHandRefDeal().setInvCtrlValue(e.getRow());
			getClientUI().setCardMode();
			getClientUI().getM_layoutManager().show();

		}

		getBillCardPanel().restoreFocusComponent();

	}

	/**
	 * UAP提供的编辑前控制
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 * 
	 */
	public boolean isCellEditable(
			boolean value/* BillModel的isCellEditable的返回值 */,
			int row/* 界面行序号 */, String itemkey/* 当前列的itemkey */) {

		// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("beforeEdit:" + e.getKey());
		if (getM_iMode() == BillMode.Browse)
			return false;
		getBillCardPanel().stopEditing();

		boolean isEditable = true;
		String sItemKey = itemkey;
		nc.ui.pub.bill.BillItem biCol = getBillCardPanel()
				.getBodyItem(sItemKey);
		int iRow = row;
		// int iPos = e.getPos();
		// forHF,临时用。请客户化帮助。由于同一批次辅计量不同而造成afterEdit触发不了。
		Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow,
				"vbatchcode");
		if (oBatchcode != null) {
			getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim(),
					iRow, "vbatchcode");
		}

		if (sItemKey == null || biCol == null) {
			return false;
		}

		// 模版设置
		if (!biCol.isEdit() || !biCol.isEnabled()) {

			return false;
		}

		if (getM_voBill() == null) {
			// biCol.setEnabled(false);
			return false;
		}
		nc.vo.scm.ic.bill.WhVO voWh = getM_voBill().getWh();
		if (voWh == null)
			voWh = getM_voBill().getWasteWh();

		// 直运仓库、直接调拨,不可编辑
		//修改人：刘家清 修改时间：2008-8-25 下午04:39:09 修改原因：直运仓库、直接调拨,可编辑
/*		if (voWh != null
				&& (voWh.getIsdirectstore() != null && voWh.getIsdirectstore()
						.booleanValue())
				|| (getM_voBill().getHeaderVO().getBdirecttranflag() != null && getM_voBill()
						.getHeaderVO().getBdirecttranflag().booleanValue())) {
			// biCol.setEnabled(false);
			return false;
		}*/

		// 来源单据控制：
		String csourcetype = (String) getM_voBill().getItemValue(iRow,
				"csourcetype");

		// 是否配套的其他入、出
		boolean isDispend = false;

		String sthistype = getBillType();



		// 其他出，来源是采购入
		if ((BillTypeConst.m_otherOut.equals(sthistype) && csourcetype != null && csourcetype

				.equals(BillTypeConst.m_purchaseIn))
				|| (BillTypeConst.m_otherIn.equals(sthistype)
						&& csourcetype != null && csourcetype
						.equals(BillTypeConst.m_saleOut)))

			isDispend = true;

		// 来源于库存特殊单
		boolean isFromICSp = false;

		if (csourcetype != null
				&& (csourcetype.equals(BillTypeConst.m_assembly)
						|| csourcetype.equals(BillTypeConst.m_disassembly)
						|| csourcetype.equals(BillTypeConst.m_transform)
						|| csourcetype.equals(BillTypeConst.m_check) || isDispend))
			isFromICSp = true;

		// 是否在途
		UFBoolean bonroadflag = (UFBoolean) getM_voBill().getItemValue(iRow,
				"bonroadflag");
		if (bonroadflag == null)
			bonroadflag = new UFBoolean(false);

		// 存货列
		if (sItemKey.equals("cinventorycode")) {
			((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField()
					.setEditable(true);
			// 获得存货参照的过滤条件
			StringBuilder swhere = getClientUI().getFilterInvString();

			if (swhere.length() > 0) {
				RefFilter.filtInv(biCol, getEnvironment().getCorpID(),
						new String[] { swhere.toString() });
			} else {
				RefFilter.filtInv(biCol, getEnvironment().getCorpID(), null);
			}

			if (csourcetype != null
					&& (csourcetype.equals("23") || csourcetype.equals("21"))) {
				// v5:如果本行上游来源不是是赠品, 且本行是赠品, 则可以修改存货
				boolean isbsourcelargess = getM_voBill().getItemVOs()[iRow]
						.getBsourcelargess().booleanValue();
				UFBoolean bLargess = getM_voBill().getItemVOs()[iRow]
						.getFlargess();
				boolean isblargess = bLargess == null ? false : bLargess
						.booleanValue();

				if (isblargess && !isbsourcelargess) {
					return true;
				}

				return false;
			}
			if (isFromICSp) {
				// biCol.setEnabled(true);
				return false;

			} else if (csourcetype != null) {
				// csourcetype != null
				// && (!csourcetype.startsWith("4") || csourcetype
				// .equals(BillTypeConst.m_transfer))) {
				// 直接根据当前存货编码过滤，避免当单据修改时无法限制时是当前的存货
				// 修改 by hanwei 2003-11-09
				String sPk_invman = (String) getM_voBill().getItemValue(iRow,
						"cinventoryid");
				// 过滤替换件
				RefFilter.filtReplaceInv(biCol, getEnvironment().getCorpID(),
						new String[] { sPk_invman });
				((nc.ui.pub.beans.UIRefPane) biCol.getComponent())
						.getUITextField().setEditable(false);
			}

		}
		// 非存货列，必须先输入存货
		else {

			// 存货编码
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow,
					"cinventorycode");
			// 存货名
			// Object oTempInvName = getBillCardPanel().getBodyValueAt(iRow,
			// "invname");
			// 如果本行未输入存货或清空存货则本行所有列不可编辑。
			if (oTempInvCode == null
					|| oTempInvCode.toString().trim().length() == 0) {
				// biCol.setEnabled(false);
				getClientUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
								"UPP4008bill-000026")/* @res "请先输入存货!" */);
				return false;
			}
		}

		InvVO voInv = getM_voBill().getItemInv(iRow);

		// 如果是辅计量管理的存货，控制先输入(应/实)辅数量:V31,不控制先输入辅助数量
		if (sItemKey.equals(getEnvironment().getAssistNumItemKey())
				|| sItemKey
						.equals(getEnvironment().getShouldAssistNumItemKey())
				|| sItemKey.equals(getEnvironment().getNumItemKey())
				|| sItemKey.equals(getEnvironment().getShouldNumItemKey())
				|| sItemKey.equals("ngrossastnum")
				|| sItemKey.equals("ntareastnum")) {

			// 应发数量,有来源
			if (csourcetype != null
					&& (sItemKey.equals(getEnvironment()
							.getShouldAssistNumItemKey()) || sItemKey
							.equals(getEnvironment().getShouldNumItemKey())))
				isEditable = false;
		}

		// 辅计量
		if (sItemKey.equals("castunitname")
				|| sItemKey
						.equals(getEnvironment().getShouldAssistNumItemKey())
				|| sItemKey.equals(getEnvironment().getAssistNumItemKey())
				|| sItemKey.equals("hsl") || sItemKey.equals("ngrossastnum")
				|| sItemKey.equals("ntareastnum")) {
			if (voInv.getIsAstUOMmgt() == null
					|| voInv.getIsAstUOMmgt().intValue() != 1) {
				isEditable = false;
			}
			// 过滤辅单位
			else {
				if (sItemKey.equals("castunitname"))
					getClientUI().filterMeas(iRow);
				// 固定换算率不可编辑
				else if (sItemKey.equals("hsl")
						&& getM_voBill().getItemVOs()[iRow]
								.getIsSolidConvRate() != null
						&& getM_voBill().getItemVOs()[iRow]
								.getIsSolidConvRate().intValue() == 1) {
					isEditable = false;
				}
				// 如果存在对应入库单信息，则自由项目信息不能修改
				if (getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
						&& getBillCardPanel().getBodyValueAt(row,
								IItemKey.CORRESCODE) != null
						&& getBillCardPanel().getBodyValueAt(row,
								IItemKey.CORRESCODE).toString().trim().length() > 0) {
					if (sItemKey.equals("castunitname")
							|| (sItemKey.equals("hsl") && voInv
									.getIsStoreByConvert().intValue() == 1))
						isEditable = false;
				}

			}

		}
		// 自由项
		else if (sItemKey.equals("vfree0")) {
			if (voInv.getIsFreeItemMgt() == null
					|| voInv.getIsFreeItemMgt().intValue() != 1) {
				isEditable = false;
			}
			// 设置自由项参数
			else {
				// 向自由项参照传入数据
				getClientUI().getFreeItemRefPane().setFreeItemParam(voInv);
				// 如果存在对应入库单信息，则自由项目信息不能修改
				if (getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
						&& getBillCardPanel().getBodyValueAt(row,
								IItemKey.CORRESCODE) != null
						&& getBillCardPanel().getBodyValueAt(row,
								IItemKey.CORRESCODE).toString().trim().length() > 0) {
					isEditable = false;
				} else
					isEditable = true;

			}
		}

		// 过滤货位参照
		else if (sItemKey.equals("vspacename")) {

			if (voWh != null && voWh.getIsLocatorMgt() != null
					&& voWh.getIsLocatorMgt().intValue() == 1) {
				getClientUI().filterSpace(iRow);

			} else {
				isEditable = false;
			}

		}
		// 批次
		else if (sItemKey.equals("vbatchcode")) {
			if (voInv.getIsLotMgt() != null
					&& voInv.getIsLotMgt().intValue() == 1) {
				String ColName = biCol.getName();

				if (oBatchcode != null)
					getBillCardPanel().setBodyValueAt(
							oBatchcode.toString().trim() + " ", iRow,
							"vbatchcode");
				getLotNumbRefPane().setMaxLength(biCol.getLength());
        TableColumn tc = GeneralBillUICtl.getColumn(getBillCardPanel().getBillTable(),ColName);
        if(tc!=null)
          tc.setCellEditor(
								new nc.ui.pub.bill.BillCellEditor(
										getLotNumbRefPane()));
				getLotNumbRefPane().setParameter(voWh, voInv);
				if (BillTypeConst.m_saleOut.equals(getBillTypeCode())

						|| BillTypeConst.m_allocationOut
								.equals(getBillTypeCode())) {
					getLotNumbRefPane().setStrNowSrcBid(
							(String) getBillCardPanel().getBodyValueAt(iRow,
									"cfirstbillbid"));
				} else {
					getLotNumbRefPane().setStrNowSrcBid(
							(String) getBillCardPanel().getBodyValueAt(iRow,
									"csourcebillbid"));
				}
				// }

			} else {
				isEditable = false;
			}
			// 如果存在对应入库单信息，则自由项目信息不能修改
			if (getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE).toString().trim().length() > 0) {
				isEditable = false;
			}

		}

		// 出库跟踪入库
		else if (sItemKey.equals(IItemKey.CORRESCODE)) {
			// 下列情况下出库跟踪入库不可以编辑：
			// 1:入库单据不是退货，2：出库单据是退货；3：入库单据数量为正；4：出库单据数量为负
			
			if (getBillCardPanel().getBillData().getBodyItem(IItemKey.CORRESCODE) != null
					&& (null == getBillCardPanel().getBillData().getBodyItem(IItemKey.CORRESCODE).getComponent()
							|| (getBillCardPanel().getBillData().getBodyItem(IItemKey.CORRESCODE).getComponent() != getClientUI().getICCorBillRef())))
				getBillCardPanel().getBillData().getBodyItem(IItemKey.CORRESCODE).setComponent(getClientUI().getICCorBillRef());
	        
			UFBoolean isReplenish = getM_voBill().getHeaderVO()
					.getFreplenishflag();
			UFBoolean boutretflag = getM_voBill().getHeaderVO()
					.getBoutretflag();
			int iReplenish = 0;
			int iBillInoutFlag = getM_voBill().getBillTypeInt();
			GeneralBillItemVO voItem = null;
			int iRowInoutFlag = 0;
			if (getM_voBill().getItemVOs() != null
					&& getM_voBill().getItemVOs().length > 0) {
				voItem = getM_voBill().getItemVOs()[iRow];
				iRowInoutFlag = voItem.getInOutFlag();
			}
			if (isReplenish.booleanValue()
					|| (boutretflag != null && boutretflag.booleanValue()))
				iReplenish = -1;
			else
				iReplenish = 1;
			iBillInoutFlag = iBillInoutFlag * iReplenish;
			if (iBillInoutFlag == InOutFlag.IN && iRowInoutFlag == InOutFlag.IN) {
				isEditable = false;
			}else{
        if (voWh == null)
          voWh = getM_voBill().getWasteWh();

        getClientUI().getICCorBillRef().setParam(voWh,
            getM_voBill().getItemVOs()[iRow]);
      }
      //else {
				// 可以编辑
//				String ColName = biCol.getName();
//        TableColumn tc = GeneralBillUICtl.getColumn(getBillCardPanel().getBillTable(),ColName);
//        if(tc!=null)
//          tc.setCellEditor(
//								new nc.ui.pub.bill.BillCellEditor(getClientUI()
//										.getICCorBillRef()));
				//ArrayList alparams = new ArrayList();
				//alparams.add(getM_voBill().getHeaderValue("cgeneralhid"));
				// getICCorBillRef().setParams(voWh, voInv, alparams);
				// getICCorBillRef().setParam(voWh,
				// m_voBill.getItemVOs()[iRow]);
//			}

		}// 修改人：刘家清 修改日期：2007-9-7上午10:51:22
		// 修改原因：根据20070907，与谢阳、杨波、刘辉讨论后，决定在代码里不控制普通单的生产日期和失效日期是否可编辑，放到单据模板里控制
		// 修改人：刘家清 修改日期：2007-10-24下午03:50:50 修改原因：非批次管理存货不能编辑生产日期和失效日期
		else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

			if (voInv.getIsLotMgt() != null
					&& voInv.getIsLotMgt().intValue() == 1) {

				if (sItemKey.equals("scrq"))
					isEditable = true;
				else if (voInv.getIsValidateMgt() != null
						&& voInv.getIsValidateMgt().intValue() == 1) {

					/*
					 * * //非期初单据并且不是入库,不能编辑 if (!m_bIsInitBill &&
					 * m_voBill.getItemVOs()[iRow].getInOutFlag() !=
					 * InOutFlag.IN) { isEditable = false; }
					 */

					isEditable = true;
					// 修改人：刘家清 修改日期：2007-9-5下午01:31:31
					// 修改原因：暂不做出入库控制，与供应商批次控制一致，与xy，zhy讨论确认了
					/*
					 * if (getM_voBill().getItemVOs() != null &&
					 * getM_voBill().getItemVOs().length > 0 &&
					 * getM_voBill().getItemVOs()[iRow].getInOutFlag() ==
					 * InOutFlag.OUT) isEditable = false; else isEditable =
					 * true;
					 */
				} else
					isEditable = false;
			} else
				isEditable = false;
			// 修改人：刘家清 修改日期：2007-9-5下午01:31:31
			// 修改原因：这个需要放开，因为50的时候不允许在单据上修改批次号信息，因此将其置成不可编辑，51允许在单据上修改后就应该将其放开，上版忘了放开了，呵呵
			// zhy如果输入的批次号在批次号档案中存在,则生产日期和实效日期不允许编辑
			/*
			 * if(isEditable){ String vbatchcode = (String)
			 * getBillCardPanel().getBodyValueAt( iRow, "vbatchcode");
			 * if(vbatchcode!=null&&isExistInBatch(voInv.getCinventoryid(),vbatchcode))
			 * isEditable=false; }
			 */
		}
		// 项目
		else if (sItemKey.equals("cprojectphasename")
				&& (getM_iMode() == BillMode.New || getM_iMode() == BillMode.Update)) {
			String spk = (String) getM_voBill().getItemValue(iRow,
					"cprojectphaseid");
			String sName = (String) getM_voBill().getItemValue(iRow,
					"cprojectphasename");
			getRefJobPhase().setPK(spk);
			getRefJobPhase().setName(sName);
			String cprojectid = (String) getBillCardPanel().getBodyValueAt(
					iRow, "cprojectid");
			if (cprojectid != null) {
				getClientUI().m_refJobPhaseModel.setJobID(cprojectid);

			} else {
				isEditable = false;
			}

		}
		// 成本对象
		else if (sItemKey.equals(IItemKey.COSTOBJECTNAME)) {
			// 委外发料不需要加工品为成本对象
			if (!getBillType().equals("4F")) {
				getClientUI().filterCostObject();
			}

		}
		// 在途标记
		else if (sItemKey.equals("bonroadflag")) {

			if (getM_voBill().getItemValue(iRow, "ncorrespondnum") != null) {

				UFDouble ncornum = new UFDouble((getM_voBill().getItemValue(
						iRow, "ncorrespondnum").toString()));

				if (getM_voBill().getItemValue(iRow,
						getEnvironment().getNumItemKey()) != null
						&& ncornum != null) {
					if (getInOutFlag() == InOutFlag.OUT) {
						ncornum = ncornum.multiply(-1);

					}

					if (((UFDouble) getM_voBill().getItemValue(iRow,
							getEnvironment().getNumItemKey())).sub(ncornum)
							.doubleValue() == 0) {
						isEditable = false;
					}
				}
			}

		}
		// 赠品flargess
		else if (sItemKey.equals(IItemKey.FLARGESS)) {
			// 自制可以编辑，非自制不可编辑。
			// 对于非自制的情况:来源是采购订单，并且bsourcelargess为否的可以编辑，为是不可编辑
			if (csourcetype == null)
				isEditable = true;
			else if ((sthistype.equals("45") && !(getClientUI()
					.isBrwLendBiztype()))
					|| sthistype.equals("47")) {

				// 修改人：刘家清 修改日期：2007-05-21
				// 修改原因：根据需求，应该判断bsourcelargess，而不是flargess
				/*
				 * UFBoolean bLargess =
				 * m_voBill.getItemVOs()[iRow].getFlargess(); boolean isblargess =
				 * bLargess==null?false:bLargess.booleanValue();
				 * 
				 * if(isblargess||(m_voBill.getHeaderVO().getFreplenishflag()!=null&&m_voBill.getHeaderVO().getFreplenishflag().booleanValue()))
				 */
				UFBoolean bsourcelargess = getM_voBill().getItemVOs()[iRow]
						.getBsourcelargess();
				boolean isbsourcelargess = bsourcelargess == null ? false
						: bsourcelargess.booleanValue();

				if (isbsourcelargess
						|| (getM_voBill().getHeaderVO().getFreplenishflag() != null && getM_voBill()
								.getHeaderVO().getFreplenishflag()
								.booleanValue())
						|| (getM_voBill().getHeaderVO().getBoutretflag() != null && getM_voBill()
								.getHeaderVO().getBoutretflag().booleanValue())){
					isEditable = false;
				}
				//赠品委托加工入库单不应该参与核销 源头是委外订单的不让编辑赠品，因此默认都是非赠品的 陈倪娜 2009-11-18
				else if("61".equals(getM_voBill().getItemVOs()[iRow].getCfirsttype())){
					isEditable = false;
				}
				else
					isEditable = true;

			} else
				isEditable = false;

		} else if (sItemKey.endsWith("prcie") || sItemKey.endsWith("mny")) {
			if (getM_voBill().getItemValue(iRow, "flargess") != null
					&& ((UFBoolean) getM_voBill()
							.getItemValue(iRow, "flargess")).booleanValue())
				isEditable = false;

		} else if (sItemKey.equals("cmeaswarename")) {
			// 过滤存货
			nc.ui.pub.bill.BillItem biBody = getBillCardPanel().getHeadItem(
					IItemKey.CALBODY);
			if (biBody == null)
				return true;

			String pk_calbody = biBody.getValue();
			BillItem bi2 = getBillCardPanel().getBodyItem("cmeaswarename");
			String[] where = new String[1];
			if (pk_calbody != null) {
				if (bi2 != null
						&& bi2.getComponent() != null
						&& ((UIRefPane) bi2.getComponent()).getRefModel()
								.getClass().getName().equals(
										"nc.ui.mm.pub.pub1010.JlcRefModel")) {
					where[0] = " and mm_jldoc.gcbm='" + pk_calbody + "'";
					RefFilter.filtMeasware(bi2, getEnvironment().getCorpID(),
							where);
				}
			}
		}

		// 出库参照入库时候表体供应商不能编辑
		else if (sItemKey.equals("vvendorname")) {
			if (getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE).toString().trim().length() > 0) {
				isEditable = false;
			}
		} else if (sItemKey.startsWith("vuserdef")) {
			GeneralBillUICtl.beforeBodyDefEdit(getBillCardPanel(), sItemKey,
					iRow, "vuserdef", "pk_defdoc");
		} else if (sItemKey.startsWith(IItemKey.VBCUSER)) {// 处理批次号档案相关的自定义项
			GeneralBillUICtl.beforeBodyDefEdit(getBillCardPanel(), sItemKey,
					iRow, IItemKey.VBCUSER, IItemKey.PK_DEFDOCBC);
		}
    else if (sItemKey.equals("nquoteunitnum") || sItemKey.equals("cquoteunitname")
        || sItemKey.equals("nquoteunitrate") || sItemKey.equals("nquoteprice") || sItemKey.equals("nquotemny")) {
      isEditable = false;
    }
		return isEditable;

	}

	protected ArrayList getM_alListData() {
		return getClientUI().getM_alListData();
	}

	protected nc.ui.pub.bill.BillListPanel getBillListPanel() {
		return getClientUI().getBillListPanel();
	}

	public BillCardPanel getBillCardPanel() {
		return getClientUI().getBillCardPanel();

	}

	public GeneralBillVO getM_voBill() {
		return getClientUI().getM_voBill();
	}

	private void synline(GeneralBillItemVO vo, int iSelrow, boolean isFirstLine) {
		getClientUI().synline(vo, iSelrow, isFirstLine);
	}

	protected void voBillCopyLine() {
		getClientUI().voBillCopyLine();
	}

	protected void voBillPastLine() {
		getClientUI().voBillPastLine();
	}

	public String getFunctionNode() {
		return getClientUI().getFunctionNode();
	}

	public String getBillType() {
		return getClientUI().getBillType();
	}

	public Environment getEnvironment() {
		return getClientUI().getEnvironment();
	}

	protected boolean isFixFlag(int row) {
		return getClientUI().isFixFlag(row);
	}

	protected Integer getInOutFlag() {
		return getClientUI().getInOutFlag();
	}

	protected ArrayList getAlLocatorData() {
		return getClientUI().getM_alLocatorData();
	}

	protected ArrayList getAlSerialData() {
		return getClientUI().getM_alSerialData();
	}

	protected String getSourBillTypeCode() {
		return getClientUI().getSourBillTypeCode();
	}

	protected boolean isInitBill() {
		return getClientUI().m_bIsInitBill;
	}

	protected nc.ui.pub.beans.UIRefPane getRefJob() {
		return getClientUI().m_refJob;
	}

	public int getM_iMode() {
		return getClientUI().getM_iMode();
	}

	public java.lang.String getBillTypeCode() {
		return getClientUI().getBillTypeCode();
	}

	public nc.ui.pub.beans.UIRefPane getRefJobPhase() {
		return getClientUI().m_refJobPhase;
	}
	
	public nc.ui.scm.extend.IFuncExtend getM_funcExtend() {
		return getClientUI().getM_funcExtend();
	}
	
	/**
	 * 类型说明：发运地址修改后处理 创建日期：(2005-09-15 14:12:13) 作者：yb 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void afterVdiliveraddress(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("vdiliveraddress").getComponent())
				.getUITextField().getText();// getRefName();
		// 保存名称以在列表形式下显示。
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("vdiliveraddress", sName);
		
		getBillCardPanel().setHeadItem("vdiliveraddress",sName);

		if (e.getPos() == BillItem.HEAD) {
			String vdiliveraddress = ((UIRefPane) getBillCardPanel()
					.getHeadItem("vdiliveraddress").getComponent())
					.getUITextField().getText();
			String bodyaddress = null;
			if (vdiliveraddress != null) {
				for (int i = 0, loop = getBillCardPanel().getRowCount(); i < loop; i++) {
					bodyaddress = (String) getBillCardPanel().getBodyValueAt(i,
							"vreceiveaddress");
					if (bodyaddress == null) {
						getBillCardPanel().setBodyValueAt(vdiliveraddress, i,
								"vreceiveaddress");
						if (getBillCardPanel().getBillModel().getRowState(i) == BillModel.NORMAL)
							getBillCardPanel().getBillModel().setRowState(i,
									BillModel.MODIFICATION);
					}
				}
			}
		} else if (e.getPos() == BillItem.BODY) {

		}
	}

	
	/**
	 * 类型说明：发运地址修改后处理 创建日期：(2005-09-15 14:12:13) 作者：yb 修改日期： 修改人： 修改原因： 算法说明：
	 */
	public void afterVreceiveaddress(nc.ui.pub.bill.BillEditEvent e) {

		if (e.getPos() == BillItem.BODY) {

			
			String vdiliveraddress = ((UIRefPane) getBillCardPanel()
					.getBodyItem("vreceiveaddress").getComponent())
					.getUITextField().getText();
			if (getM_voBill() != null)
				getM_voBill().setItemValue(e.getRow(), "vreceiveaddress", vdiliveraddress);
			getBillCardPanel().setBodyValueAt(vdiliveraddress, e.getRow(),"vreceiveaddress");		
		}
	}

}
