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
 * �����ˣ������� �������ڣ�2008-2-25����09:33:45 ����ԭ�򣺱༭�����¼��ࡣ
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
		// ë�غ�Ƥ�ر༭����Ҫִ��������ʽ��
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
		// ����
		String sName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
				.getRefName();

		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdptname", sName);

	}

	protected void afterCBizidEdit(nc.ui.pub.bill.BillEditEvent e) {
		// ҵ��Ա
		String sName = ((nc.ui.pub.beans.UIRefPane) getClientUI()
				.getBillCardPanel().getHeadItem("cbizid").getComponent())
				.getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getClientUI()
				.getBillCardPanel().getHeadItem("cbizid").getComponent())
				.getRefPK();
		// ��Ҫ����ҵ��Ա�Զ���������
		//�޸��ˣ������� �޸�ʱ�䣺2008-8-25 ����10:59:04 �޸�ԭ�򣺲�����ҵ��Ա�Զ���������
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
				// ����
				sDeptName = ((nc.ui.pub.beans.UIRefPane) itDpt.getComponent())
						.getRefName();
			}
		}
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null) {
			getM_voBill().setHeaderValue("cbizname", sName);
			//getM_voBill().setHeaderValue("cdptname", sDeptName);
		}
	}

	/**
	 * ���ù�ʽ ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-12 16:47:04) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	private String execFormular(String formula, String value) {
		nc.ui.pub.formulaparse.FormulaParse f = new nc.ui.pub.formulaparse.FormulaParse();

		if (formula != null && !formula.equals("")) {
			// ���ñ��ʽ
			f.setExpress(formula);
			// ��ñ���
			nc.vo.pub.formulaset.VarryVO varry = f.getVarry();
			// ��������ֵ
			Hashtable h = new Hashtable();
			for (int j = 0; j < varry.getVarry().length; j++) {
				String key = varry.getVarry()[j];

				String[] vs = new String[1];
				vs[0] = value;
				h.put(key, StringUtil.toString(vs));
			}

			f.setDataS(h);
			// ���ý��
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
	 * ���ߣ�� ���ܣ���Ӧ�̱༭���¼���������� ������ ���أ� ���⣺ ���ڣ�(2004-6-21 10:36:30)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterProviderEdit(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cproviderid").getComponent()).getRefPK();

		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cprovidername", sName);

		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
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
			// ���ݲ��մ�����Ӧ�̼��
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sProviderShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sProviderShortName);
			}

			// ��ÿ��̻�������ID
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
	 * �����������������ݿ��̹�������ÿ��̵Ļ���������
	 * <p>
	 * <b>����˵��</b>
	 * 
	 * @param pk_cumangid
	 *            ���̹�������ID
	 * @return ���̻�������ID
	 *         <p>
	 * @author duy
	 * @time 2007-3-20 ����10:50:26
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
	 * ���ߣ�� ���ܣ��ͻ��༭���¼� ������ ���أ� ���⣺ ���ڣ�(2004-6-21 10:38:55) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	public void afterCustomerEdit(nc.ui.pub.bill.BillEditEvent e) {
		// �ͻ�
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("ccustomerid").getComponent()).getRefName();
		String sRefPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("ccustomerid").getComponent()).getRefPK();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("ccustomername", sName);
		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
		getClientUI().filterVdiliveraddressRef(true, -1);

		// ���ݿͻ���Ӧ�̹��˷��˵�ַ�Ĳ���
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
			// ���ݲ��մ������̼��
			nc.ui.scm.pub.TwoTableCacheFind twoTable = new nc.ui.scm.pub.TwoTableCacheFind();
			String sCustomerShortName = twoTable.getJoinTableFieldValue(
					"bd_cumandoc", sRefPK, "custshortname");
			if (iProvidername != null) {
				iProvidername.setValue(sCustomerShortName);
			}

			// ��ÿ��̻�������ID
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
	 * �����ߣ����Ӣ ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterNumEdit_1(int row) {

		/** ������գ�if �̶������ʣ���ո������� */
		Object oNumValue = getBillCardPanel().getBodyValueAt(row,
				getEnvironment().getNumItemKey());

		if (oNumValue == null || oNumValue.toString().trim().length() < 1) {

			if (!isInitBill()) {
				// ��ձ��е�ҵ������
				getBillCardPanel().setBodyValueAt(null, row, "dbizdate");

			}

			if (getBillCardPanel().getBodyItem("ncountnum") != null) {
				getBillCardPanel().setBodyValueAt(null, row, "ncountnum");

			}
		} else {

			UFDouble nNum = new UFDouble(oNumValue.toString().trim());

			// �����Դ����Ϊ���ⵥ�ݣ����������͸���������Ӧ����Ӧ�շ�����
			// afterNumEditFromSpe(e);

			if (!isInitBill()) {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null)

					// ���ڳ������Զ�����ҵ������
					getBillCardPanel().setBodyValueAt(getClientUI().getEnvironment().getLogDate(),
							row, "dbizdate");

			} else {
				if (getBillCardPanel().getBodyValueAt(row, "dbizdate") == null) {

					// �ڳ������Զ�����ϵͳ��������
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
		// �������ԭ����ֵ��Ϊ�գ����Ҹı��˷�����ն�Ӧ����
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
	 * �����ߣ����˾� ���ܣ���ն�Ӧ������ ������ ���أ� ���⣺ ���ڣ�(2001-11-23 18:11:18)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void clearCorrBillInfo(int rownum) {
		// ��ն�Ӧ����
		// ��Ӧ���ݺ�
		try {
			getBillCardPanel()
					.setBodyValueAt(null, rownum, IItemKey.CORRESCODE);
		} catch (Exception e3) {
		}
		try {
			// ��Ӧ��������
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondtype");
		} catch (Exception e4) {
		}
		try {
			// ��Ӧ���ݱ�ͷOID
			// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondhid");
		} catch (Exception e5) {
		}
		try {
			// ��Ӧ���ݱ���OID
			getBillCardPanel().setBodyValueAt(null, rownum, "ccorrespondbid");
		} catch (Exception e6) {
		}
		// ͬ���ı�m_voBill
		getM_voBill().setItemValue(rownum, "ccorrespondbid", null);
		getM_voBill().setItemValue(rownum, "ccorrespondhid", null);
		getM_voBill().setItemValue(rownum, IItemKey.CORRESCODE, null);
		getM_voBill().setItemValue(rownum, "ccorrespondtype", null);

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void afterAstUOMEdit(nc.ui.pub.bill.BillEditEvent e) {
		int rownum = e.getRow();
		// ��������λ
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
	 * �����ߣ����Ӣ ���ܣ������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		
		//�޸��ˣ������� �޸�ʱ�䣺2008-12-1 ����09:10:26 �޸�ԭ�򣺳���ҵ��ʱ������ǻ����ʼǽ��Ĵ���������ж�Ӧ��ⵥ�ţ��Ͳ�Ҫ�����޸Ļ����ʡ�
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
	 * �����ߣ����Ӣ ���ܣ�Ӧ���������༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * ������*������=����
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-7-18 15:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * �����ߣ����Ӣ ���ܣ�Ӧ���������༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * ������*������=����
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-7-18 15:01:19) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			// ��Ӧ���ݺ�
			getBillCardPanel().setBodyValueAt(null, iSelrow,
					IItemKey.CORRESCODE);
			// ��Ӧ��������
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
			// ��Ӧ���ݱ�ͷOID
			// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
			getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
			// ��Ӧ���ݱ���OID
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
				// ���ӵ�����
				voBillPastLine();

				int irow = getBillCardPanel().getBillTable().getSelectedRow() - 1;

				synline(vos[j], irow, false);
				// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
				if ("40080822".equals(getFunctionNode())) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel().setBodyValueAt(crowno, irow, "crowno");
					getM_voBill().setItemValue(irow, "crowno", crowno);
					getBillCardPanel()
							.setBodyValueAt(null, irow, "cgeneralbid");
					getM_voBill().setItemValue(irow, "cgeneralbid", null);
				}
			}
			// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
			if ("40080822".equals(getFunctionNode())) {
				return;
				// �����ٴ����к���
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

			// dw ����������кź���� m_voBill
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
	 * �˴����뷽��˵���� �����ߣ����� ���ܣ����κŸı䴦�� ������ ���أ� ���⣺ ���ڣ�(2001-6-20 21:43:07)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
//			 �õ��������itemkey
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
			// �ֹ����룬���ܻ����쳣��
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
      //liuys add for �׸ڿ�ҵ  ������幩Ӧ�̸�ֵ begin
//      String pk_cumandoc = execFormular(
//				"getColValue(bd_cumandoc,pk_cumandoc,pk_cubasdoc,pk_cubasdoc)",
//				voLot[0].getPk_cubasdoc());
//      getM_voBill().setItemValue(rownum, "cvendorid", pk_cumandoc);
//      getM_voBill().setItemValue(rownum, "pk_cubasdoc", voLot[0].getPk_cubasdoc());
//      getBillCardPanel().setBodyValueAt(pk_cumandoc, rownum, "cvendorid");
//      getBillCardPanel().setBodyValueAt(voLot[0].getPk_cubasdoc(), rownum, "pk_cubasdoc");
//      getBillCardPanel().getBillModel().execLoadFormula();
    //liuys add for �׸ڿ�ҵ  ������幩Ӧ�̸�ֵ end
      
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
		
		// �޸��ˣ������� �޸����ڣ�2007-11-20����09:18:50 �޸�ԭ�򣺵�����¼��ʱ�����κſ��ܻ�����β����ϵĲ�һ�£��ͷ���
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
				// ���ӵ�����
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
				// ����ǳ��⴦����棬��ô�кž�Ҫ�������ɡ�
				if ("40080822".equals(getFunctionNode())) {
					String crowno = DataBuffer.getRowno();
					getBillCardPanel()
							.setBodyValueAt(crowno, iSelrow, "crowno");
					getM_voBill().setItemValue(iSelrow, "crowno", crowno);
					getBillCardPanel().setBodyValueAt(null, iSelrow,
							"cgeneralbid");
					getM_voBill().setItemValue(iSelrow, "cgeneralbid", null);
				}
				//�޸��ˣ������� �޸�ʱ�䣺2008-9-11 ����10:55:29 �޸�ԭ��ѡȡ����������ʱ��Ӧ��Ӧ�������ÿա�
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
	 * �����ߣ������� ���ܣ��������޸� ������ ���أ� ���⣺ ���ڣ�(2001-11-20 14:01:52) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param row
	 *            int
	 */
	public void afterHslEdit(nc.ui.pub.bill.BillEditEvent e) {
		// �У�ѡ�б�ͷ�ֶ�ʱΪ-1
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
	 * ?user> ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2005-2-1 14:41:41) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param e
	 *            nc.ui.pub.bill.BillEditEvent
	 */
	protected void afterOnRoadEdit(nc.ui.pub.bill.BillEditEvent e) {
		int iSelrow = e.getRow();

		UFBoolean bonroadflag = new UFBoolean(getBillCardPanel()
				.getBodyValueAt(iSelrow, e.getKey()).toString());

		// ��Ӧ���ݺ�
		getBillCardPanel().setBodyValueAt(null, iSelrow, IItemKey.CORRESCODE);
		// ��Ӧ��������
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondtype");
		// ��Ӧ���ݱ�ͷOID
		// ����ģ����б���λ����������ʾ��ccorrespondhid,ccorrespondbid,�Ա�������Ķ�Ӧ��ͷ������OID
		getBillCardPanel().setBodyValueAt(null, iSelrow, "ccorrespondhid");
		// ��Ӧ���ݱ���OID
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
	 * �˴����뷽��˵���� ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2001-11-21 14:47:40) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @param num
	 *            nc.vo.pub.lang.UFDouble
	 * @param assistnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param row
	 *            int
	 * �޸��ˣ������� �޸�ʱ�䣺2008-9-1 ����02:32:31 �޸�ԭ���޸�������������кš�
	 * �޸��ˣ������� �޸�ʱ�䣺2008-10-6 ����10:20:02 �޸�ԭ���������Ϊ�յĻ���������кš�
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

//		�޸��ˣ������� �޸�ʱ�䣺2008-10-30 ����08:12:49 �޸�ԭ���޸Ĳֿ�󣬿��ܰѻ�λVO��գ�����û�еĻ�û�С�
		LocatorVO[] voLoc = null;		
		if (null != getAlLocatorData() && row < getAlLocatorData().size()){
			voLoc = (LocatorVO[]) getAlLocatorData().get(row);

		if (voLoc != null && voLoc.length == 1) {

			// ������
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
			// ����
			if (num == null) {
				voLoc[0].setNinspacenum(num);
				voLoc[0].setNoutspacenum(num);
				if (getAlSerialData() != null)
					getAlSerialData().set(row, null);
			} else {
				if (getInOutFlag() > 0) {
					// ���
					voLoc[0].setNoutspacenum(null);
					voLoc[0].setNinspacenum(num);
				} else {
					// ����
					voLoc[0].setNinspacenum(null);
					voLoc[0].setNoutspacenum(num);
					/*if (getAlSerialData() != null)
						getAlSerialData().set(row, null);*/
				}
				
				if (UFDouble.ZERO_DBL.compareTo(num) == 0 &&getAlSerialData() != null)
					getAlSerialData().set(row, null);
			}
			// ë��
			if (ngrossnum == null) {
				voLoc[0].setNingrossnum(ngrossnum);
				voLoc[0].setNoutgrossnum(ngrossnum);
				/*if (getAlSerialData() != null)
					getAlSerialData().set(row, null);*/
			} else {
				if (getInOutFlag() > 0) {
					// ���
					voLoc[0].setNoutgrossnum(null);
					voLoc[0].setNingrossnum(ngrossnum);
				} else {
					// ����
					voLoc[0].setNingrossnum(null);
					voLoc[0].setNoutgrossnum(ngrossnum);

				}

			}

		} else
			getAlLocatorData().set(row, null);
		}

	}

	protected void afterBillCodeEdit(BillEditEvent e) {
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue(
					"vbillcode",
					getBillCardPanel().getHeadItem("vbillcode")
							.getValueObject());
	}

	protected void afterDispatcherEdit(BillEditEvent e) {
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cdispatcherid").getComponent()).getRefName();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdispatchername", sName);
	}

	protected void afterCinventoryidEdit(BillEditEvent e) {
		// �ӹ�Ʒ
		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cinventoryid").getComponent()).getRefName();
		String sID=((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cinventoryid").getComponent()).getRefPK();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null){
			getM_voBill().setHeaderValue("cinventoryname", sName);
			getM_voBill().setHeaderValue("cinventoryid", sID);
		}
		

	}

	protected void afterWhsmanagerEdit(BillEditEvent e) {

		// ���Ա

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cwhsmanagerid").getComponent()).getRefName();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cwhsmanagername", sName);

	}

	protected void afterBiztypeEdit(BillEditEvent e) {
		// ҵ������

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cbiztype").getComponent()).getRefName();
		String sPK = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cbiztype").getComponent()).getRefPK();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cbiztypename", sName);
		// ����ҵ�����ʹ���Ĭ�ϵ��շ���� updated by cqw after v2.30
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
		// ���˷�ʽ

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("cdilivertypeid").getComponent()).getRefName();
		// �������������б���ʽ����ʾ��
		if (getM_voBill() != null)
			getM_voBill().setHeaderValue("cdilivertypename", sName);

	}

	protected void afterOtherWHEdit(BillEditEvent e) {
		// DW 2005-05-31 �ڸı������ֿ�ʱά�����������֯������˾
		try {
			String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("cotherwhid").getComponent()).getRefName();
			String sCode = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem("cotherwhid").getComponent()).getRefPK();
			// ���������⴦�� ���幫˾�Ϳ����֯
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
				// �޸��ˣ������� �޸����ڣ�2007-7-9����04:30:30
				// �޸�ԭ���������ڡ�ʧЧ���ڶ���ֵʱ��������������ʧЧ���ڣ���ʧЧ���ڲ�Ҫȥ����������(ͨ��������)
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
        //�����������䴦�� ������ 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		getBillCardPanel()
				.setBodyValueAt(null, e.getRow(), "cprojectphasename");
		getBillCardPanel().setBodyValueAt(null, e.getRow(), "cprojectphaseid");
		getBillCardPanel().setBodyValueAt(sName, e.getRow(), "cprojectname");
		getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cprojectid");
		// �������������б���ʽ����ʾ��
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
		//�����������䴦�� ������ 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		// �������������б���ʽ����ʾ��
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
		//�����������䴦�� ������ 2009-08-05
		if (null == sPK && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			sPK = (String)((DefaultConstEnum)e.getValue()).getValue();
			sName = ((DefaultConstEnum)e.getValue()).getName();
		}
		String sPk_cubasdoc = getPk_cubasdoc(sPK);

		getBillCardPanel().setBodyValueAt(sName, e.getRow(), "vvendorname");
		getBillCardPanel().setBodyValueAt(sPK, e.getRow(), "cvendorid");
		getBillCardPanel().setBodyValueAt(sPk_cubasdoc, e.getRow(),
				"pk_cubasdoc");

		// �������������б���ʽ����ʾ��
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

		// �������������б���ʽ����ʾ��
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

		// �������������б���ʽ����ʾ��
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
		// �������Ʒ��������ϵĵ��ۺͽ�� add by hanwei 2004-6-24
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
		// ͬ����VO
		getM_voBill().setItemValue(row, IItemKey.CROWNO,
				getBillCardPanel().getBodyValueAt(row, IItemKey.CROWNO));

	}

	protected void afterVuserDefEdit(BillEditEvent e) {
		int pos = e.getPos();
		String sItemKey = e.getKey();
		int row = e.getRow();
		if (pos == 0) {// ��ͷ
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vuserdef".length());
			//addied by liuzy 2008-04-07 v5.02��ע�͵���v5.03�������౻ɾ��
			//�ַ��ֲ�Ӧȥ�������ȥ���ᵼ���Զ�������ձ༭���޷�����PK
			DefSetTool.afterEditHead(getBillCardPanel().getBillData(),
					sItemKey, sVdefPkKey);

			// ͬ��m_voBill
			getM_voBill().setHeaderValue(sItemKey,
					getBillCardPanel().getHeadItem(sItemKey).getValueObject());
			getM_voBill()
					.setHeaderValue(
							sVdefPkKey,
							getBillCardPanel().getHeadItem(sVdefPkKey)
									.getValueObject());
		} else if (pos == 1) {// ����
			String sVdefPkKey = "pk_defdoc"
					+ sItemKey.substring("vuserdef".length());
			//addied by liuzy 2008-04-07 v5.02��ע�͵���v5.03�������౻ɾ��
			//�ַ��ֲ�Ӧȥ�������ȥ���ᵼ���Զ�������ձ༭���޷�����PK
			DefSetTool.afterEditBody(getBillCardPanel().getBillModel(),
					row, sItemKey, sVdefPkKey);

			// ͬ��m_voBill
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

		// ���ݱ���ʹ�ã�afterEditBody(BillModel billModel, int iRow,String
		// sVdefValueKey, String sVdefPkKey)
		DefSetTool.afterEditBody(getBillCardPanel().getBillModel(), row,
				sItemKey, sVdefPkKey);

		// ͬ��m_voBill
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
		// �������������б���ʽ����ʾ��
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
		// �ڱ���
		if (getBillCardPanel().getBodyItem(sItemKey) != null) {

			sIdColName = getBillCardPanel().getBodyItem(sItemKey)
					.getIDColName();
			if (sIdColName != null
					&& getBillCardPanel().getBodyItem(sIdColName) != null
					&& (getBillCardPanel().getBodyItem(sItemKey).getComponent()) != null) {
				ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getBodyItem(sItemKey).getComponent());
				// ��pk
				getBillCardPanel().setBodyValueAt(ref.getRefPK(), row,
						sIdColName);
				// ��ʾname
				getBillCardPanel().setBodyValueAt(ref.getRefName(), row,
						sItemKey);
				// ͬ��m_voBill
				getM_voBill().setItemValue(row, sIdColName, ref.getRefPK());
				getM_voBill().setItemValue(row, sItemKey, ref.getRefName());
			} else {// if (getBillCardPanel().getBodyItem(sItemKey) != null) {
				Object ovalue = getBillCardPanel()
						.getBodyValueAt(row, sItemKey);
				getM_voBill().setItemValue(row, sItemKey, ovalue);
			}
		} else if (getBillCardPanel().getHeadItem(sItemKey) != null) {
			// �ڱ�ͷ
			sIdColName = getBillCardPanel().getHeadItem(sItemKey)
					.getIDColName();
			if (sIdColName != null
					&& getBillCardPanel().getHeadItem(sIdColName) != null
					&& getBillCardPanel().getHeadItem(sItemKey).getComponent() != null) {
				ref = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
						.getHeadItem(sItemKey).getComponent());
				// ��pk
				getBillCardPanel().getHeadItem(sIdColName).setValue(
						ref.getRefPK());
				//deleted by lirr 2009-02-12
			/*	// ��ʾname
				getBillCardPanel().getHeadItem(sItemKey).setValue(
						ref.getRefName());*/
				// getM_voBill().setHeaderValue(sItemKey,ref.getRefName());
			}
		}
	}

	/**
	 * �����ߣ����˾� ���ܣ��ֿ�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public void afterWhEdit(nc.ui.pub.bill.BillEditEvent e, String sNewWhName,
			String sNewWhID) {
		// �ֿ�
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
			// ��ձ������ʾ����
			try {
				if (sNewWhID == null
						|| (getM_voBill() != null && !sNewWhID
								.equals(getM_voBill().getHeaderVO()
										.getCwarehouseid()))) {
//		        	�޸��ˣ������� �޸�ʱ�䣺2008-10-30 ����07:15:32 �޸�ԭ���޸Ĳֿ�ʱ��Ҫ�����λ�����к���Ϣ��
					String sIKs[] = new String[] {"locator","serial", "cspaceid", "vspacename",
							"ccorrespondbid", "ccorrespondcode",
							"ccorrespondhid", "ccorrespondtype",
							"nplannedprice", "nplannedmny" };
					
			          //�޸��ˣ������� �޸�ʱ�䣺2008-10-30 ����07:15:32 �޸�ԭ���޸Ĳֿ�ʱ��Ҫ�����λ�����к���Ϣ��
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
				SCMEnv.out("���Ժ��ԵĴ���" + e2);
			}
			// ����˲ֿ�
			if (sNewWhID == null) {
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				if (getM_voBill() != null)
					getM_voBill().setWh(null);
			} else {

				// �������������б���ʽ����ʾ��
				// ��ѯ�ֿ���Ϣ
				// ��ѯ��ʽ������Ѿ�¼���˴������Ҫͬʱ��ƻ��ۡ�
				int iQryMode = QryInfoConst.WH;
				// ����
				Object oParam = sNewWhID;
				// ��ǰ��¼��Ĵ������
				ArrayList alAllInvID = new ArrayList();
				boolean bHaveInv = getClientUI().getCurInvID(alAllInvID);

				// �ֿ�
				WhVO voWh = null;
				// ������κŲ��յĲֿ�
				getLotNumbRefPane().setWHParams(null);
				if (getM_voBill() != null)
					getM_voBill().setWh(null);

				if (bHaveInv) {
					// �������ֿ�ID,ԭ�����֯ID,��λID,���ID
					ArrayList alParam = new ArrayList();
					alParam.add(sNewWhID);
					iQryMode = QryInfoConst.WH_PLANPRICE;
					// ��ǰ�Ŀ����֯,����û�вֿ�������
					if (getM_voBill() != null && getM_voBill().getWh() != null)
						alParam.add(getM_voBill().getWh().getPk_calbody());
					else
						alParam.add(null);
					// ��˾
					alParam.add(getEnvironment().getCorpID());
					// ��ǰ�Ĵ��
					alParam.add(alAllInvID);
					oParam = alParam;
				}

				Object oRet = GeneralBillHelper.queryInfo(
						new Integer(iQryMode), oParam);
				// Object oRet = invokeClient("queryInfo", new Class[] {
				// Integer.class, Object.class }, new Object[] {
				// new Integer(iQryMode), oParam });

				// ��ǰ��¼��Ĵ������,�����޸��˿����֯�ŷ���һ��ArrayList
				if (oRet instanceof ArrayList) {
					ArrayList alRetValue = (ArrayList) oRet;
					if (alRetValue != null && alRetValue.size() >= 2) {
						voWh = (WhVO) alRetValue.get(0);
						// ˢ�¼ƻ���
						getClientUI().freshPlanprice(
								(ArrayList) alRetValue.get(1));
					}
				} else
					// ���򷵻� WhVO
					voWh = (WhVO) oRet;
				// �����֯����
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
					
					//�޸��ˣ������� �޸�ʱ�䣺2008-11-12 ����03:58:37 �޸�ԭ�򣺵�ѡ������֯�󣬹�������Ҫ���ݿ����֯���ˣ�����������幤�����ġ�
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
					// ���β�ִ���
					getM_voBill().clearInvQtyInfo();
					getLotNumbRefPane().setWHParams(voWh);

					// DUYONG ˢ�¼ƻ���
					// �޸��ˣ������� �޸�ʱ�䣺2009-8-31 ����03:58:00 �޸�ԭ��ǰ���Ѿ�ˢ�¼ƻ����ˣ����Դ��ⲻ��Ҫˢ�¼ƻ��ۡ�
/*					getClientUI().freshPlanprice(
							getClientUI().getInvoInfoBYFormula().getPlanPrice(
									alAllInvID, voWh.getPk_corp(),voWh.getPk_calbody(),
									voWh.getCwarehouseid()));*/
				}

				// ˢ���ִ�����ʾ
				getClientUI().setTailValue(0);
				// ���û�λ���䰴ť�Ƿ���á�
				getClientUI().setBtnStatusSpace(true);
			}

			getBillCardPanel().restoreFocusComponent();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * �����ߣ����˾� ���ܣ������֯�ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * �޸��ˣ������� �޸�ʱ�䣺2008-11-12 ����03:58:37 �޸�ԭ�򣺵�ѡ������֯�󣬹�������Ҫ���ݿ����֯���ˣ�����������幤�����ġ�
	 * 
	 */
	public void afterCalbodyEdit(nc.ui.pub.bill.BillEditEvent e) {
		try {
			String sNewID = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
					.getHeadItem(e.getKey()).getComponent()).getRefPK();
			nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			RefFilter.filtWh(bi, getEnvironment().getCorpID(), getClientUI().getFilterWhString(sNewID));
			// ����:clear warehouse
			WhVO whvo = getM_voBill().getWh();
			nc.ui.pub.bill.BillItem biWh = getBillCardPanel().getHeadItem(
					IItemKey.WAREHOUSE);
			if (biWh != null
					&& (whvo == null || (sNewID != null && !sNewID.equals(whvo
							.getPk_calbody()))))
				biWh.setValue(null);
			
			//�޸��ˣ������� �޸�ʱ�䣺2008-11-12 ����03:58:37 �޸�ԭ�򣺵�ѡ������֯�󣬹�������Ҫ���ݿ����֯���ˣ�����������幤�����ġ�
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


			// ���˳ɱ�����
			// filterCostObject();

		} catch (Exception e2) {
			SCMEnv.out(e2);
		}

	}

	/**
	 * �˴����뷽��˵���� ������ն�ѡ�����ý��� ���ڴ����ѡ������ɨ�����ֳ��� �������ڣ�(2004-5-7 12:40:43)
	 * 
	 * @param invVOs
	 *            nc.vo.scm.ic.bill.InvVO[]�����VO iRow����ǰ��
	 *            sItemKey����ǰ�е�Key:"cinventorycode"
	 */
	public void afterInvMutiEditSetUI(InvVO[] invVOs, int iRow, String sItemKey) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEditSetUI:"
				+ sItemKey + "������" + invVOs.length);

		// ��������
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

		// v5 lj ���вɹ�����¼��
		getClientUI().setBodyDefaultData(iRow, iLen);

		// ���ý�������
		boolean bHasSourceBillTypecode = false;
		if (getSourBillTypeCode() == null
				|| getSourBillTypeCode().trim().length() == 0) {
			bHasSourceBillTypecode = false;
		} else
			bHasSourceBillTypecode = true;

		int iCurRow = 0;

		getBillCardPanel().getBillModel().setNeedCalculate(false);
		// zhy2005-08-24��ͷ��Ӧ��Ӧд������
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

		// ��õ���ͷ�����еĹ�Ӧ�̻�������ID
		BillItem iPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc");
		Object oPk_cubasdoc = null;
		if (iPk_cubasdoc != null)
			oPk_cubasdoc = getBillCardPanel().getHeadItem("pk_cubasdoc")
					.getValueObject();

		for (int i = 0; i < iLen; i++) {
			iCurRow = iRow + i;
			// ������/�����������
			// ������ڡ�m_voBill.setItemInv������֮ǰ add by ydy 2003-12-17.01
			getClientUI().clearRowData(0, iCurRow, sItemKey);
			// ����
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
				// ��Ӧ����
				// ͬ��vo
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
			//�޸��ˣ������� �޸�ʱ�䣺2008-7-24 ����04:00:14 �޸�ԭ�򣺲���IC026"����λ���յ�����˳��"����ѡ��"�ϴ�����λ"��
			// �������ѡ���ѡ���������ⵥʱ�Զ������ֿ�+��������ϴ�����λ�Զ�����"��λ"��Ŀ�������ֹ��޸ġ��ϴ�����λȡ��������������µ���ⵥ������λ��
			getClientUI().setBodyInSpace(iCurRow, invVOs[i]);
			//�޸��ˣ������� 2009-09-03 ����ͷ�ӹ�Ʒ�������ӹ�Ʒ�ֶ�
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
		// ���õ�ǰ�����к��Ƿ����
		getBillCardPanel().getBillModel().setNeedCalculate(true);
		getBillCardPanel().getBillModel().reCalcurateAll();

		// �޸��ˣ������� �޸�ʱ�䣺2009-9-2 ����03:55:17 �޸�ԭ�򣺼�������������������������
		//getClientUI().setTailValue(iRow);
		getClientUI().setBtnStatusSN(iRow, true);

		// ���õ�ǰѡ���е��ִ�������
		if (getClientUI().isM_bOnhandShowHidden())
			getClientUI().showOnHandPnlInfo(iRow);
		// ��̬���ý���
		getClientUI().getOnHandRefDeal().setInvCtrlValue(iRow);
		getClientUI().setCardMode();
		getClientUI().getM_layoutManager().show();

	}

	/**
	 * �����ߣ����˾� ���ܣ�����¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 */
	public void afterInvMutiEdit(nc.ui.pub.bill.BillEditEvent e) {
		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("afterInvMutiEdit:"
				+ e.getKey());
		long ITimeAll = System.currentTimeMillis();

		int row = e.getRow();
		// �ֶ�itemkey
		String sItemKey = e.getKey();
		getM_voBill().setItemValue(row, "desainfo", null);
		nc.ui.pub.beans.UIRefPane invRef = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getBodyItem("cinventorycode").getComponent();
		// ������PK
    String[] refPks = invRef.getRefPKs();
    if(getClientUI().isLineCardEdit() && refPks.length>1){
      refPks = new String[]{refPks[0]};
    }
    //�������޸Ļ��б༭
//    if(!invRef.getUITextField().isFocusOwner()){
//    //if(refPks==null || refPks.length<=0){
//      if(getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid")!=null){
//        refPks = new String[]{(String)getBillCardPanel().getBodyValueAt(e.getRow(), "cinventoryid")};
//        invRef.setPK(refPks[0]);
//      }
//    }
		// �������Ϊ�գ���յ�ǰ����
		if (refPks == null || refPks.length == 0) {

			// �����ǰ�е�����VO,��������������ر�־
			// �޸��ˣ������� �޸����ڣ�2007-8-27����04:54:01 �޸�ԭ�򣺴����Ϊ��ʱ��Ҳ�ô�������
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

			nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("���շ��صĴ������getRefPKs:0");
			getClientUI().clearRow(row);

			return;
		}
		invRef.setPK(null);

		nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("���շ��صĴ������getRefPKs:"
				+ refPks.length);
		// �ֿ�Ϳ����֯��Ϣ
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

		SCMEnv.showTime(ITime, "���������������:");

		ITime = System.currentTimeMillis();
		// �������
		// DUYONG �˴���Ҫ��֤�Ƿ���Ҫ���мƻ��۵Ĵ���������֤���˴��Ѿ������˴���
		InvVO[] invVOs = getClientUI().getInvoInfoBYFormula()
				.getInvParseWithPlanPriceAndPackType(refPks, sWhID, sCalID,getClientUI().getEnvironment().getCorpID(), true, true);

		SCMEnv.showTime(ITime, "�������:");
		InvVO[] invvoBack = new InvVO[invVOs.length];
		for (int i = 0; i < invVOs.length; i++) {
			invVOs[i].setPk_corp(getEnvironment().getCorpID());
			invvoBack[i] = (InvVO) invVOs[i].clone();
		}

		ITime = System.currentTimeMillis();
		// �������
/*		try {
			QueryInfo info = new QueryInfo();
			invVOs = info.dealPackType(invVOs);
		} catch (Exception e1) {
			nc.vo.scm.pub.SCMEnv.error(e1);
			invVOs = invvoBack;
		}*/

		// �����ǰ�е�����VO,��������������ر�־
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
		SCMEnv.showTime(ITimeAll, "������ն�ѡ:");

	}

	/**
	 * �����ߣ����˾� ���ܣ�������ı��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

			// �޸��ˣ������� �޸����ڣ�2007-11-13����01:58:48 �޸�ԭ�򣺵�������vfree0Ϊ��ʱ��Ӧ�����������
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
			// �����ص�����
			// �����ص�����
			/*
			 * if(!BillTypeConst.m_purchaseIn.equals(this.getBillType()))
			 * clearRowData(0, e.getRow(), "vfree0");
			 */
			// �޸��ˣ������� �޸����ڣ�2007-9-19����01:53:07 �޸�ԭ�򣺸���л�����������ۺ󣬾����޸������������κŵ���Ϣ
			// clearRowData(0, e.getRow(), "vfree0");
			getClientUI().execEditFomulas(e.getRow(), e.getKey());

		} catch (Exception e2) {
			nc.vo.scm.pub.SCMEnv.error(e2);
		}

	}

	/**
	 * �����ߣ����Ӣ ���ܣ���λ�޸��¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
		//�����������䴦�� ������ 2009-08-05
		if (null == cspaceid && null != e.getValue() && e.getValue() instanceof DefaultConstEnum ){
			cspaceid = (String)((DefaultConstEnum)e.getValue()).getValue();
			csname = ((DefaultConstEnum)e.getValue()).getName();
		}


		setRowSpaceData(e.getRow(), cspaceid, csname);

	}

	/**
	 * �����ߣ����Ӣ
	 * 
	 * ���ܣ������еĻ�λ��Ϣ
	 * 
	 * ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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

		// ��������λ������LocatorVO
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

			// �޸����к�����
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
	 * �����ߣ����˾� ���ܣ������Ӧ�л�λ�����к����� ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	protected void clearLocSnData(int row, String sItemKey) {
		// ��Ϊ�ֿ��޸ĺ�row==-1,�������ﲻ���row�Ϸ���
		if (sItemKey == null || sItemKey.length() == 0)
			return;
		// ��ʾ��Ϣ
		String sHintMsg = "";
		// �Ƿ����λ�����к�
		boolean bClearLoc = false, bClearSn = false;

		if (sItemKey.equals("bonroadflag")) {
			bClearLoc = true;
			// bClearSn=true;
		}

		// �ֿ�
		if (sItemKey.equals(IItemKey.WAREHOUSE)) {
			// ��Ҫ��� ---------- ���� ----------- ��λ��!!!
			if (getAlLocatorData() != null && getAlSerialData() != null) {
				for (int q = 0; q < getAlLocatorData().size(); q++) {
					getAlLocatorData().set(q, null);
					// ���кźͻ�λ��أ�ҲӦ��ȫ�����
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
			// �ֿ��Ƿ��λ����---�ǻ�λ����ʱ�ڲֿ�ı�ʱ��ա�
			boolean bIsLocatorMgt = getClientUI().isLocatorMgt();
			// ��ǰ���Ƿ����кŹ���---������ȥ����
			// boolean bIsSNmgt = isSNmgt(row);

			// ���µ��жϲ���else���
			// ���
			if (sItemKey.equals("cinventorycode")
					|| sItemKey.equals(getEnvironment().getNumItemKey())
					|| sItemKey.equals(getEnvironment().getAssistNumItemKey())
					|| sItemKey.equals(IItemKey.CORRESCODE)
					|| sItemKey.equals("vvendorname")
					|| sItemKey.equals("headprovider")) {
				// //���ڳ���ʱ��Ҫ���λ��
				// if (!m_bIsInitBill)
				// bClearLoc = true;
				bClearSn = true;
			}
			// ���������
			if (!bClearSn
					&& (sItemKey.equals("vfree0") || sItemKey
							.equals("vbatchcode")))
				bClearSn = true;

			// �������ʱ����Ҫ�����кţ�����Ҫͬʱ���λ
			// if(bClearSn&&m_voBill.getItemInv(row)!=null&&m_voBill.getItemInv(row).getInOutFlag()!=InOutFlag.IN)
			// bClearLoc=true;
			// //����ǻ�λ��������Ҫ������
			if (bIsLocatorMgt && bClearLoc) {
				if (getAlLocatorData() != null

				&& row >= 0 && row < getAlLocatorData().size()
						&& getAlLocatorData().get(row) != null) {
					getAlLocatorData().set(row, null);
					getBillCardPanel().setBodyValueAt(null, row, "vspacename");
					getBillCardPanel().setBodyValueAt(null, row, "cspaceid");
					sHintMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"common", "UC000-0003830")/* @res "��λ" */;
				}
				if (getM_voBill() != null
						&& getM_voBill().getItemVOs().length > row
						&& getM_voBill().getItemVOs()[row] != null) {

					getM_voBill().getItemVOs()[row].setLocator(null);

				}

			}

			// ----------------- ��������кŹ�������Ҫ������
			// bIsSNmgt
			// &&
			if (bClearSn) {
				if (getAlSerialData() != null && row >= 0
						&& row < getAlSerialData().size()

						&& getAlSerialData().get(row) != null) {
					getAlSerialData().set(row, null);
					// ������˻�λ
					if (sHintMsg != null && sHintMsg.length() > 0)
						sHintMsg = sHintMsg
								+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
										"4008bill", "UPP4008bill-000311")/*
																			 * @res
																			 * "��"
																			 */;
					sHintMsg = sHintMsg
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"common", "UC000-0001819")/* @res "���к�" */;
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
		// "����˵� " + (row + 1) + " �е�" + sHintMsg + "���ݣ�������ִ��" + sHintMsg +
		// "���䡣");

	}

	/**
	 * �����ߣ����˾� ���ܣ����ݱ༭�¼����� ������e���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 * beforeEdit ����ע�⡣[�����ͷ�༭ǰ�¼�]
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

		// �ֿ�༭ǰ��Ҫ���տ����֯����
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
	 * �����ߣ����˾� ���ܣ������塢�б��ϱ�༭�¼����� ������e ���ݱ༭�¼� ���أ� ���⣺ ���ڣ�(2001-5-8 19:08:05)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * 
	 * 
	 * 
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		// �б���ʽ�µı�ͷ��ѡ��
		getBillCardPanel().rememberFocusComponent();
		int row = e.getRow();
		if (e.getSource() == getBillListPanel().getHeadTable()) {

			if (row < 0 || getM_alListData() == null
					&& row >= getM_alListData().size()) {
				SCMEnv.out(" row now ERR ");
				return;
			}
			// ���δ�ı����򷵻�
			if (getClientUI().getM_iLastSelListHeadRow() == row)
				return;
			// ���޹�����
			getClientUI().setM_alLocatorData(null);
			getClientUI().setM_alSerialData( null);
			// �ñ�����������Ϊ��
			getClientUI().m_sLastKey = null;

			// SCMEnv.out(" Line changed to " + row);
			// �ı��Ӧ�ı�����ʾ
			getClientUI().setLastHeadRow(row);
			getClientUI().selectListBill(
					getClientUI().getM_iLastSelListHeadRow());
			// �����λ
			// clearOrientColor();
			// setBtnStatusSN(0);
			if (getClientUI().getM_funcExtend() != null) {
				// ֧�ֹ�����չ
				getClientUI().getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST,
						nc.ui.pub.bill.BillItem.HEAD);
			}
		} else if (e.getSource() == getBillListPanel().getBodyTable()) {
			getClientUI().setBtnStatusSN(row);
			if (getClientUI().getM_funcExtend() != null) {
				// ֧�ֹ�����չ
				getClientUI().getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.LIST,
						nc.ui.pub.bill.BillItem.BODY);
			}
		}

		// ����ʽ�µ��޸Ļ�ѡ�С�

		else if (e.getSource() == getBillCardPanel().getBillTable()) {
			if (row < 0)
				return;
			SCMEnv.out("line to " + e.getRow());
			getClientUI().setBtnStatusSN(row);
			getClientUI().setTailValue(row);
			if (getM_funcExtend() != null) {
				// ֧�ֹ�����չ
				getM_funcExtend().rowchange(getClientUI(),
						getBillCardPanel(), getBillListPanel(),
						nc.ui.scm.extend.IFuncExtend.CARD,
						nc.ui.pub.bill.BillItem.BODY);
			}

			// v5 lj ����������Ŀ��ɫ��
			getClientUI().getFreeItemCellRender().setRenderer("vfree0");
			getClientUI().getLotRefCellRender().setRenderer("vbatchcode");

			// ˢ���ִ���Panel��ʾ
			if (getClientUI().isM_bOnhandShowHidden()) {
				getClientUI().showOnHandPnlInfo(e.getRow());
			}
			// ��ʾ
			getClientUI().getOnHandRefDeal().setInvCtrlValue(e.getRow());
			getClientUI().setCardMode();
			getClientUI().getM_layoutManager().show();

		}

		getBillCardPanel().restoreFocusComponent();

	}

	/**
	 * UAP�ṩ�ı༭ǰ����
	 * 
	 * @param value
	 * @param row
	 * @param itemkey
	 * @return
	 * 
	 */
	public boolean isCellEditable(
			boolean value/* BillModel��isCellEditable�ķ���ֵ */,
			int row/* ��������� */, String itemkey/* ��ǰ�е�itemkey */) {

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
		// forHF,��ʱ�á���ͻ�������������ͬһ���θ�������ͬ�����afterEdit�������ˡ�
		Object oBatchcode = getBillCardPanel().getBodyValueAt(iRow,
				"vbatchcode");
		if (oBatchcode != null) {
			getBillCardPanel().setBodyValueAt(oBatchcode.toString().trim(),
					iRow, "vbatchcode");
		}

		if (sItemKey == null || biCol == null) {
			return false;
		}

		// ģ������
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

		// ֱ�˲ֿ⡢ֱ�ӵ���,���ɱ༭
		//�޸��ˣ������� �޸�ʱ�䣺2008-8-25 ����04:39:09 �޸�ԭ��ֱ�˲ֿ⡢ֱ�ӵ���,�ɱ༭
/*		if (voWh != null
				&& (voWh.getIsdirectstore() != null && voWh.getIsdirectstore()
						.booleanValue())
				|| (getM_voBill().getHeaderVO().getBdirecttranflag() != null && getM_voBill()
						.getHeaderVO().getBdirecttranflag().booleanValue())) {
			// biCol.setEnabled(false);
			return false;
		}*/

		// ��Դ���ݿ��ƣ�
		String csourcetype = (String) getM_voBill().getItemValue(iRow,
				"csourcetype");

		// �Ƿ����׵������롢��
		boolean isDispend = false;

		String sthistype = getBillType();



		// ����������Դ�ǲɹ���
		if ((BillTypeConst.m_otherOut.equals(sthistype) && csourcetype != null && csourcetype

				.equals(BillTypeConst.m_purchaseIn))
				|| (BillTypeConst.m_otherIn.equals(sthistype)
						&& csourcetype != null && csourcetype
						.equals(BillTypeConst.m_saleOut)))

			isDispend = true;

		// ��Դ�ڿ�����ⵥ
		boolean isFromICSp = false;

		if (csourcetype != null
				&& (csourcetype.equals(BillTypeConst.m_assembly)
						|| csourcetype.equals(BillTypeConst.m_disassembly)
						|| csourcetype.equals(BillTypeConst.m_transform)
						|| csourcetype.equals(BillTypeConst.m_check) || isDispend))
			isFromICSp = true;

		// �Ƿ���;
		UFBoolean bonroadflag = (UFBoolean) getM_voBill().getItemValue(iRow,
				"bonroadflag");
		if (bonroadflag == null)
			bonroadflag = new UFBoolean(false);

		// �����
		if (sItemKey.equals("cinventorycode")) {
			((nc.ui.pub.beans.UIRefPane) biCol.getComponent()).getUITextField()
					.setEditable(true);
			// ��ô�����յĹ�������
			StringBuilder swhere = getClientUI().getFilterInvString();

			if (swhere.length() > 0) {
				RefFilter.filtInv(biCol, getEnvironment().getCorpID(),
						new String[] { swhere.toString() });
			} else {
				RefFilter.filtInv(biCol, getEnvironment().getCorpID(), null);
			}

			if (csourcetype != null
					&& (csourcetype.equals("23") || csourcetype.equals("21"))) {
				// v5:�������������Դ��������Ʒ, �ұ�������Ʒ, ������޸Ĵ��
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
				// ֱ�Ӹ��ݵ�ǰ���������ˣ����⵱�����޸�ʱ�޷�����ʱ�ǵ�ǰ�Ĵ��
				// �޸� by hanwei 2003-11-09
				String sPk_invman = (String) getM_voBill().getItemValue(iRow,
						"cinventoryid");
				// �����滻��
				RefFilter.filtReplaceInv(biCol, getEnvironment().getCorpID(),
						new String[] { sPk_invman });
				((nc.ui.pub.beans.UIRefPane) biCol.getComponent())
						.getUITextField().setEditable(false);
			}

		}
		// �Ǵ���У�������������
		else {

			// �������
			Object oTempInvCode = getBillCardPanel().getBodyValueAt(iRow,
					"cinventorycode");
			// �����
			// Object oTempInvName = getBillCardPanel().getBodyValueAt(iRow,
			// "invname");
			// �������δ����������մ�����������в��ɱ༭��
			if (oTempInvCode == null
					|| oTempInvCode.toString().trim().length() == 0) {
				// biCol.setEnabled(false);
				getClientUI().showHintMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("4008bill",
								"UPP4008bill-000026")/* @res "����������!" */);
				return false;
			}
		}

		InvVO voInv = getM_voBill().getItemInv(iRow);

		// ����Ǹ���������Ĵ��������������(Ӧ/ʵ)������:V31,�����������븨������
		if (sItemKey.equals(getEnvironment().getAssistNumItemKey())
				|| sItemKey
						.equals(getEnvironment().getShouldAssistNumItemKey())
				|| sItemKey.equals(getEnvironment().getNumItemKey())
				|| sItemKey.equals(getEnvironment().getShouldNumItemKey())
				|| sItemKey.equals("ngrossastnum")
				|| sItemKey.equals("ntareastnum")) {

			// Ӧ������,����Դ
			if (csourcetype != null
					&& (sItemKey.equals(getEnvironment()
							.getShouldAssistNumItemKey()) || sItemKey
							.equals(getEnvironment().getShouldNumItemKey())))
				isEditable = false;
		}

		// ������
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
			// ���˸���λ
			else {
				if (sItemKey.equals("castunitname"))
					getClientUI().filterMeas(iRow);
				// �̶������ʲ��ɱ༭
				else if (sItemKey.equals("hsl")
						&& getM_voBill().getItemVOs()[iRow]
								.getIsSolidConvRate() != null
						&& getM_voBill().getItemVOs()[iRow]
								.getIsSolidConvRate().intValue() == 1) {
					isEditable = false;
				}
				// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
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
		// ������
		else if (sItemKey.equals("vfree0")) {
			if (voInv.getIsFreeItemMgt() == null
					|| voInv.getIsFreeItemMgt().intValue() != 1) {
				isEditable = false;
			}
			// �������������
			else {
				// ����������մ�������
				getClientUI().getFreeItemRefPane().setFreeItemParam(voInv);
				// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
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

		// ���˻�λ����
		else if (sItemKey.equals("vspacename")) {

			if (voWh != null && voWh.getIsLocatorMgt() != null
					&& voWh.getIsLocatorMgt().intValue() == 1) {
				getClientUI().filterSpace(iRow);

			} else {
				isEditable = false;
			}

		}
		// ����
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
			// ������ڶ�Ӧ��ⵥ��Ϣ����������Ŀ��Ϣ�����޸�
			if (getBillCardPanel().getBodyItem(IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE) != null
					&& getBillCardPanel().getBodyValueAt(row,
							IItemKey.CORRESCODE).toString().trim().length() > 0) {
				isEditable = false;
			}

		}

		// ����������
		else if (sItemKey.equals(IItemKey.CORRESCODE)) {
			// ��������³��������ⲻ���Ա༭��
			// 1:��ⵥ�ݲ����˻���2�����ⵥ�����˻���3����ⵥ������Ϊ����4�����ⵥ������Ϊ��
			
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
				// ���Ա༭
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

		}// �޸��ˣ������� �޸����ڣ�2007-9-7����10:51:22
		// �޸�ԭ�򣺸���20070907����л��������������ۺ󣬾����ڴ����ﲻ������ͨ�����������ں�ʧЧ�����Ƿ�ɱ༭���ŵ�����ģ�������
		// �޸��ˣ������� �޸����ڣ�2007-10-24����03:50:50 �޸�ԭ�򣺷����ι��������ܱ༭�������ں�ʧЧ����
		else if (sItemKey.equals("dvalidate") || sItemKey.equals("scrq")) {

			if (voInv.getIsLotMgt() != null
					&& voInv.getIsLotMgt().intValue() == 1) {

				if (sItemKey.equals("scrq"))
					isEditable = true;
				else if (voInv.getIsValidateMgt() != null
						&& voInv.getIsValidateMgt().intValue() == 1) {

					/*
					 * * //���ڳ����ݲ��Ҳ������,���ܱ༭ if (!m_bIsInitBill &&
					 * m_voBill.getItemVOs()[iRow].getInOutFlag() !=
					 * InOutFlag.IN) { isEditable = false; }
					 */

					isEditable = true;
					// �޸��ˣ������� �޸����ڣ�2007-9-5����01:31:31
					// �޸�ԭ���ݲ����������ƣ��빩Ӧ�����ο���һ�£���xy��zhy����ȷ����
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
			// �޸��ˣ������� �޸����ڣ�2007-9-5����01:31:31
			// �޸�ԭ�������Ҫ�ſ�����Ϊ50��ʱ�������ڵ������޸����κ���Ϣ����˽����óɲ��ɱ༭��51�����ڵ������޸ĺ��Ӧ�ý���ſ����ϰ����˷ſ��ˣ��Ǻ�
			// zhy�����������κ������κŵ����д���,���������ں�ʵЧ���ڲ�����༭
			/*
			 * if(isEditable){ String vbatchcode = (String)
			 * getBillCardPanel().getBodyValueAt( iRow, "vbatchcode");
			 * if(vbatchcode!=null&&isExistInBatch(voInv.getCinventoryid(),vbatchcode))
			 * isEditable=false; }
			 */
		}
		// ��Ŀ
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
		// �ɱ�����
		else if (sItemKey.equals(IItemKey.COSTOBJECTNAME)) {
			// ί�ⷢ�ϲ���Ҫ�ӹ�ƷΪ�ɱ�����
			if (!getBillType().equals("4F")) {
				getClientUI().filterCostObject();
			}

		}
		// ��;���
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
		// ��Ʒflargess
		else if (sItemKey.equals(IItemKey.FLARGESS)) {
			// ���ƿ��Ա༭�������Ʋ��ɱ༭��
			// ���ڷ����Ƶ����:��Դ�ǲɹ�����������bsourcelargessΪ��Ŀ��Ա༭��Ϊ�ǲ��ɱ༭
			if (csourcetype == null)
				isEditable = true;
			else if ((sthistype.equals("45") && !(getClientUI()
					.isBrwLendBiztype()))
					|| sthistype.equals("47")) {

				// �޸��ˣ������� �޸����ڣ�2007-05-21
				// �޸�ԭ�򣺸�������Ӧ���ж�bsourcelargess��������flargess
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
				//��Ʒί�мӹ���ⵥ��Ӧ�ò������ Դͷ��ί�ⶩ���Ĳ��ñ༭��Ʒ�����Ĭ�϶��Ƿ���Ʒ�� ������ 2009-11-18
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
			// ���˴��
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

		// ����������ʱ����幩Ӧ�̲��ܱ༭
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
		} else if (sItemKey.startsWith(IItemKey.VBCUSER)) {// �������κŵ�����ص��Զ�����
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
	 * ����˵�������˵�ַ�޸ĺ��� �������ڣ�(2005-09-15 14:12:13) ���ߣ�yb �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
	 */
	public void afterVdiliveraddress(nc.ui.pub.bill.BillEditEvent e) {

		String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
				.getHeadItem("vdiliveraddress").getComponent())
				.getUITextField().getText();// getRefName();
		// �������������б���ʽ����ʾ��
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
	 * ����˵�������˵�ַ�޸ĺ��� �������ڣ�(2005-09-15 14:12:13) ���ߣ�yb �޸����ڣ� �޸��ˣ� �޸�ԭ�� �㷨˵����
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
