package nc.impl.so.sointerface;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.bs.pub.para.SysInitBO;
import nc.bs.scm.pub.smart.SmartDMO;
import nc.impl.scm.so.so001.SaleOrderDMO;
import nc.impl.scm.so.so016.SOToolsDMO;
import nc.itf.ic.service.IICToPU_StoreadminDMO;
import nc.itf.so.service.ISOToIC_DRP;
import nc.itf.uap.pf.IPFConfig;
import nc.jdbc.framework.crossdb.CrossDBConnection;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pfflow00.VerifyruleVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

public class SaleToICDRPDMO extends DataManageObject implements ISOToIC_DRP {

	public SaleToICDRPDMO() throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super();
	}

	public SaleToICDRPDMO(String dbName) throws javax.naming.NamingException,
			nc.bs.pub.SystemException {
		super(dbName);
	}

	private UFDouble assignOOSNum(nc.vo.so.so008.OosinfoItemVO[] itemVO,
			UFDouble nnum, UFDate date, String operid) throws Exception {

		if (itemVO == null)
			return null;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();

		// ����
		for (int i = 0; i < itemVO.length; i++) {
			UFDouble oldnum = itemVO[i].getNnumber();
			UFDouble fillnumber = itemVO[i].getNfillnumber();
			if (fillnumber == null)
				fillnumber = new UFDouble(0);
			if (nnum.doubleValue() >= oldnum.sub(fillnumber).doubleValue()) {
				// ����
				itemVO[i].setNfillnumber(oldnum);
				itemVO[i].setBfillflag(new UFBoolean(true));
				itemVO[i].setDfilldate(date);
				nnum = nnum.sub(oldnum.sub(fillnumber));

				// ����Ϣ
				try {
					nc.impl.scm.so.so003.SendMsgImpl mess = new nc.impl.scm.so.so003.SendMsgImpl();
					// ����ID
					String id = itemVO[i].getCsaleid();
					// �ͻ�ID
					String custid = itemVO[i].getCcustomerid();
					// ���ID
					String invid = itemVO[i].getCinventoryid();
					// ������
					String sid = operid;
					// ������
					String rid = itemVO[i].getCoperatorid();
					mess.send(id, custid, invid, sid, rid);
				} catch (Exception e) {
					SCMEnv.out("��Ϣ����ʧ�ܣ�");
					SCMEnv.out(e.getMessage());
					throw e;
				}
			} else {
				itemVO[i].setNfillnumber(nnum.add(fillnumber));
				itemVO[i].setBfillflag(new UFBoolean(false));
				itemVO[i].setDfilldate(null);
				nnum = new UFDouble(0);
			}

			// nnum = nnum.sub(oldnum);

			dmo.updateOosinItem(itemVO[i]);

			if (nnum.doubleValue() <= 0)
				return null;

		} // end for

		return nnum;
	}

	/**
	 * ������Դ����ID��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String getInvoiceSourceBillTypeID(String strDetailID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {
		String strSourceID = null;

		String strSQL = "Select cupreceipttype from so_saleinvoice_b Where cinvoice_bid ";

		strSQL = strSQL + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {

				strSourceID = rs.getString(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return strSourceID;
	}

	/**
	 * ������Դ����ID��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String[] getInvoiceSourceID(String strBillType, String strDetailID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {
		String[] strSourceID = null;
		String strField = null;

		String strSQL = "Select cupsourcebillid,cupsourcebillbodyid from ";

		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)
				|| strBillType.equals(SaleBillType.SaleInitInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		
		/**v5.5������Ϊ�������ݣ�ԭ���߼������ã���ʱע�͵�*/
		// ������
		/*if (strBillType.equals(SaleBillType.SaleReceipt)) {
			strSQL = strSQL + " so_salereceipt_b ";
			strField = "creceipt_bid";
		}*/

		strSQL = strSQL + " Where " + strField + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				strSourceID = new String[2];
				strSourceID[0] = rs.getString(1);
				strSourceID[1] = rs.getString(2);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return strSourceID;
	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param pk_corp
	 *            java.lang.String
	 * @param pk_calbody
	 *            java.lang.String
	 * @param pk_invmandoc
	 *            java.lang.String
	 * @param bisFree
	 *            nc.vo.pub.lang.UFBoolean
	 * @param vfree1
	 *            java.lang.String
	 * @param vfree2
	 *            java.lang.String
	 * @param vfree3
	 *            java.lang.String
	 * @param vfree4
	 *            java.lang.String
	 * @param vfree5
	 *            java.lang.String
	 * @param vfree6
	 *            java.lang.String
	 * @param vfree7
	 *            java.lang.String
	 * @param vfree8
	 *            java.lang.String
	 * @param vfree9
	 *            java.lang.String
	 * @param vfree10
	 *            java.lang.String
	 * @param startDate
	 *            nc.vo.pub.lang.UFDate
	 * @param endDate
	 *            nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDouble[] getONReceiptNum(String pk_corp,
			String pk_calbody, String pk_invmandoc,
			nc.vo.pub.lang.UFBoolean bisFree, String vfree1, String vfree2,
			String vfree3, String vfree4, String vfree5, String vfree6,
			String vfree7, String vfree8, String vfree9, String vfree10,
			String startDate, String endDate) throws java.sql.SQLException,
			nc.vo.pub.BusinessException {
		if (pk_corp == null || pk_corp.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000034")/* @res "��˾����Ϊ���쳣" */);
		}
		if (pk_calbody == null || pk_calbody.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000035")/* @res "�����֯Ϊ���쳣" */);
		}
		if (pk_invmandoc == null || pk_invmandoc.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000036")/* @res "�������������Ϊ���쳣" */);
		}
		if (bisFree == null || bisFree.toString().trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000037")/* @res "�Ƿ�������չ��Ϊ���쳣" */);
		}
		if (startDate == null || startDate.equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000038")/* @res "��ʼ����Ϊ���쳣" */);
		}
		if (endDate == null || endDate.equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000039")/* @res "��������Ϊ���쳣" */);
		}

		UFDouble[] retNums = null;
		UFDouble retNUm = null;
		UFDate start = null;
		UFDate end = null;
		try {
			start = new UFDate(startDate);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000040")/* @res "��ʼ���ڲ�����ʽ����ȷ" */);
		}
		try {
			end = new UFDate(endDate);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000041")/* @res "�������ڲ�����ʽ����ȷ" */);
		}
		String selectpart = "select ";
		String selectpart1 = " sum(so_salereceipt_b.nnumber - (case when so_saleexecute.ntotalinventorynumber is null then isnull(so_saleexecute.ntotalreceivenumber,0) else isnull(so_saleexecute.ntotalinventorynumber,0) end))";
		String frompart = "  From so_salereceipt inner JOIN  so_salereceipt_b ON so_salereceipt.csaleid = so_salereceipt_b.csaleid inner JOIN  so_saleexecute ON so_salereceipt.csaleid = so_saleexecute.csaleid AND ";
		frompart += " so_salereceipt_b.creceipt_bid = so_saleexecute.csale_bid";
		// �Ѿ�������δ�����Ķ���
		String where = " and so_salereceipt.fstatus = "
				+ nc.ui.pub.bill.BillStatus.AUDIT;
		where += " and (so_salereceipt_b.nnumber>so_saleexecute.ntotalinventorynumber or so_saleexecute.ntotalinventorynumber is null ) ";

		String sql = selectpart + selectpart1 + frompart + where;

		String wherepart = " and so_salereceipt.pk_corp ='" + pk_corp + "' ";
		wherepart += " and so_salereceipt.ccalbodyid  ='" + pk_calbody + "' ";
		wherepart += " and so_salereceipt_b.cinventoryid  = '" + pk_invmandoc
				+ "' ";
		// ���۷����� (���ݳ���)
		wherepart += " and  so_salereceipt.creceipttype  = '31'  ";

		// ��������չ��
		if (bisFree.booleanValue()) {
			if (vfree1 == null) {
				wherepart += " and (so_saleexecute.vfree1 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
			} else {
				wherepart += " and so_saleexecute.vfree1 = '" + vfree1 + "' ";
			}
			if (vfree2 == null) {
				wherepart += " and (so_saleexecute.vfree2 is null  or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
			} else {
				wherepart += " and so_saleexecute.vfree2 = '" + vfree2 + "' ";
			}
			if (vfree3 == null) {
				wherepart += " and (so_saleexecute.vfree3 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
			} else {
				wherepart += " and so_saleexecute.vfree3 = '" + vfree3 + "' ";
			}
			if (vfree4 == null) {
				wherepart += " and (so_saleexecute.vfree4 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
			} else {
				wherepart += " and so_saleexecute.vfree4 = '" + vfree4 + "' ";
			}
			if (vfree5 == null) {
				wherepart += " and (so_saleexecute.vfree5 is null or len(rtrim(so_saleexecute.vfree1)) =0 ) ";
			} else {
				wherepart += " and so_saleexecute.vfree5 = '" + vfree5 + "' ";
			}
		}

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Vector vrslt = new Vector();
		String sql0 = sql;
		String sql1 = "";
		String wherepart0 = wherepart;
		String wherepart1 = wherepart;
		try {
			con = getConnection();
			// ������ʼ����֮ǰ����������֮��
			wherepart0 += " and so_salereceipt.dbilldate < ? ";
			sql0 += wherepart0;

			// SCMEnv.out("***************************************");
			// SCMEnv.out("SQL:������δ��������������ʼ����֮ǰ����������֮��");
			// SCMEnv.out("SQL:������δ��������������ʼ����֮ǰ����������֮��");

			// SCMEnv.out("***************************************");

			stmt = con.prepareStatement(sql0);
			stmt.setString(1, start.toString());
			rs = stmt.executeQuery();
			retNUm = null;
			while (rs.next()) {
				BigDecimal bretNum = rs.getBigDecimal(1);
				retNUm = (bretNum == null ? new UFDouble(0) : new UFDouble(
						bretNum));
			}
			vrslt.addElement(retNUm);

			// ȡ�õ�ǰ���ڣ�������ǰ���ڣ�֮���δ������
			sql1 = selectpart + " so_salereceipt.dbilldate, " + selectpart1
					+ frompart + where + wherepart1
					+ " and so_salereceipt.dbilldate >= '" + start
					+ "' and so_salereceipt.dbilldate <= '" + endDate + "'";
			sql1 += "group by so_salereceipt.dbilldate";
			stmt = con.prepareStatement(sql1);
			rs = stmt.executeQuery();
			Vector tempDate = new Vector();
			Vector tempV = new Vector();
			while (rs.next()) {
				String d = rs.getString(1);
				UFDate date = (d == null ? null : new UFDate(d.trim()));
				tempDate.addElement(date);
				BigDecimal n = rs.getBigDecimal(2);
				UFDouble num = (n == null ? new UFDouble(0) : new UFDouble(n));
				tempV.addElement(num);
			}
			rs.close();
			UFDate currDate = null;
			for (int i = 0; i <= end.getDaysAfter(start); i++) {
				retNUm = null;
				currDate = start.getDateAfter(i);
				int ind = tempDate.indexOf(currDate);
				if (ind >= 0) {
					retNUm = (UFDouble) tempV.get(ind);
				}
				vrslt.addElement(retNUm);
			}
			if (vrslt.size() > 0) {
				retNums = new UFDouble[vrslt.size()];
				vrslt.copyInto(retNums);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else
				SCMEnv.out(e.getMessage());

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		}

		return retNums;
	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param pk_corp
	 *            java.lang.String
	 * @param pk_calbody
	 *            java.lang.String
	 * @param pk_invmandoc
	 *            java.lang.String
	 * @param bisFree
	 *            nc.vo.pub.lang.UFBoolean
	 * @param vfree1
	 *            java.lang.String
	 * @param vfree2
	 *            java.lang.String
	 * @param vfree3
	 *            java.lang.String
	 * @param vfree4
	 *            java.lang.String
	 * @param vfree5
	 *            java.lang.String
	 * @param vfree6
	 *            java.lang.String
	 * @param vfree7
	 *            java.lang.String
	 * @param vfree8
	 *            java.lang.String
	 * @param vfree9
	 *            java.lang.String
	 * @param vfree10
	 *            java.lang.String
	 * @param startDate
	 *            nc.vo.pub.lang.UFDate
	 * @param endDate
	 *            nc.vo.pub.lang.UFDate
	 */
	public UFDouble[] getOnSONum(String pk_corp, String pk_calbody,
			String pk_invmandoc, nc.vo.pub.lang.UFBoolean bisFree,
			String vfree1, String vfree2, String vfree3, String vfree4,
			String vfree5, String vfree6, String vfree7, String vfree8,
			String vfree9, String vfree10, String startDate, String endDate)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {

		// 1�� ��������������۶��������ε��ݣ�ȡ���۶���δ��������
		// 2�� ������۳��ⵥ�����۶��������ε��ݣ�ȡ���۶���δ��������

		if (pk_corp == null || pk_corp.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000034")/* @res "��˾����Ϊ���쳣" */);
		}
		if (pk_calbody == null || pk_calbody.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000035")/* @res "�����֯Ϊ���쳣" */);
		}
		if (pk_invmandoc == null || pk_invmandoc.trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000036")/* @res "�������������Ϊ���쳣" */);
		}
		if (bisFree == null || bisFree.toString().trim().equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000037")/* @res "�Ƿ�������չ��Ϊ���쳣" */);
		}
		if (startDate == null || startDate.equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000038")/* @res "��ʼ����Ϊ���쳣" */);
		}
		if (endDate == null || endDate.equals("")) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000039")/* @res "��������Ϊ���쳣" */);
		}

		UFDouble[] retNums = null;
		UFDouble retNUm = null;
		UFDate start = null;
		UFDate end = null;
		try {
			start = new UFDate(startDate);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000040")/* @res "��ʼ���ڲ�����ʽ����ȷ" */);
		}
		try {
			end = new UFDate(endDate);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000041")/* @res "�������ڲ�����ʽ����ȷ" */);
		}
		String selectpart = "select ";
		String selectpart1 = " sum(so_saleorder_b.nnumber - (case when so_saleexecute.ntotalreceivenumber is null  then isnull(so_saleexecute.ntotalinventorynumber,0) else isnull(so_saleexecute.ntotalreceivenumber,0) end)) From ";
		// 2001��11��30��ע��

		// String frompart = " so_sale LEFT OUTER JOIN so_saleorder_b ON
		// so_sale.csaleid = so_saleorder_b.csaleid LEFT OUTER JOIN
		// so_saleexecute ON so_sale.csaleid = so_saleexecute.csaleid AND " ;
		// frompart += " so_saleorder_b.corder_bid = so_saleexecute.csale_bid";

		String frompart = "  so_sale ,so_saleorder_b,so_saleexecute where so_sale.csaleid = so_saleorder_b.csaleid  and so_sale.csaleid = so_saleexecute.csaleid ";
		// �������ҵ��
		// frompart += " AND so_saleorder_b.corder_bid =
		// so_saleexecute.csale_bid and (NOT (so_saleorder_b.creceipttype = '4H'
		// OR so_saleorder_b.creceipttype = '42')) " ;
		// edit by sj
		frompart += " AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid and ((so_saleorder_b.creceipttype != '4H' and so_saleorder_b.creceipttype != '42') or so_saleorder_b.creceipttype is null) ";

		// �Ѿ�������δ�����Ķ���
		String where = " AND (so_sale.fstatus != "
				+ nc.ui.pub.bill.BillStatus.BLANKOUT
				+ " and  so_sale.fstatus !=" + nc.ui.pub.bill.BillStatus.FINISH
				+ ") ";

		where += " and ((so_saleorder_b.nnumber>so_saleexecute.ntotalinventorynumber or so_saleexecute.ntotalinventorynumber is null ) or (so_saleorder_b.nnumber>so_saleexecute.ntotalreceivenumber or so_saleexecute.ntotalreceivenumber is null ))";

		String sql = selectpart + selectpart1 + frompart + where;

		String wherepart = " and so_sale.pk_corp ='" + pk_corp + "' ";
		wherepart += " and so_sale.ccalbodyid  ='" + pk_calbody + "' ";
		wherepart += " and so_saleorder_b.cinventoryid  = '" + pk_invmandoc
				+ "' ";
		// ���۶���\�ڳ����۶��� (�������ͳ���)
		wherepart += " and  (so_sale.creceipttype  = '30' or so_sale.creceipttype  = '3A')  ";

		// ��������չ��
		if (bisFree.booleanValue()) {
			if (vfree1 == null) {
				wherepart += " and so_saleexecute.vfree1 is null ";
			} else {
				wherepart += " and so_saleexecute.vfree1 = '" + vfree1 + "' ";
			}
			if (vfree2 == null) {
				wherepart += " and so_saleexecute.vfree2 is null ";
			} else {
				wherepart += " and so_saleexecute.vfree2 = '" + vfree2 + "' ";
			}
			if (vfree3 == null) {
				wherepart += " and so_saleexecute.vfree3 is null ";
			} else {
				wherepart += " and so_saleexecute.vfree3 = '" + vfree3 + "' ";
			}
			if (vfree4 == null) {
				wherepart += " and so_saleexecute.vfree4 is null ";
			} else {
				wherepart += " and so_saleexecute.vfree4 = '" + vfree4 + "' ";
			}
			if (vfree5 == null) {
				wherepart += " and so_saleexecute.vfree5 is null ";
			} else {
				wherepart += " and so_saleexecute.vfree5 = '" + vfree5 + "' ";
			}
		}

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Vector vrslt = new Vector();
		String sql0 = sql;
		String sql1 = "";
		String wherepart0 = wherepart;
		String wherepart1 = wherepart;
		try {
			con = getConnection();
			// ������ʼ����֮ǰ����������֮��
			wherepart0 += " and so_saleorder_b.dconsigndate < ? ";
			sql0 += wherepart0;
			stmt = con.prepareStatement(sql0);
			stmt.setString(1, start.toString());
			rs = stmt.executeQuery();
			retNUm = null;
			while (rs.next()) {
				BigDecimal bretNum = rs.getBigDecimal(1);
				retNUm = (bretNum == null ? new UFDouble(0) : new UFDouble(
						bretNum));
			}
			vrslt.addElement(retNUm);

			// ȡ�õ�ǰ���ڣ�������ǰ���ڣ�֮���δ������
			sql1 = selectpart + " so_saleorder_b.dconsigndate, " + selectpart1
					+ frompart + where + wherepart1
					+ " and so_saleorder_b.dconsigndate >= '" + start
					+ "' and so_saleorder_b.dconsigndate <= '" + endDate + "'";
			sql1 += " group by so_saleorder_b.dconsigndate";
			stmt = con.prepareStatement(sql1);
			rs = stmt.executeQuery();
			Vector tempDate = new Vector();
			Vector tempV = new Vector();
			while (rs.next()) {
				String d = rs.getString(1);
				UFDate date = (d == null ? null : new UFDate(d.trim()));
				tempDate.addElement(date);
				BigDecimal n = rs.getBigDecimal(2);
				UFDouble num = (n == null ? new UFDouble(0) : new UFDouble(n));

				tempV.addElement(num);
			}
			rs.close();
			UFDate currDate = null;
			for (int i = 0; i <= end.getDaysAfter(start); i++) {
				retNUm = null;
				currDate = start.getDateAfter(i);
				int ind = tempDate.indexOf(currDate);
				if (ind >= 0) {
					retNUm = (UFDouble) tempV.get(ind);
				}
				vrslt.addElement(retNUm);
			}
			if (vrslt.size() > 0) {
				retNums = new UFDouble[vrslt.size()];
				vrslt.copyInto(retNums);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else
				SCMEnv.out(e.getMessage());

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		}

		return retNums;
	}

	/**
	 * �����ṩȡ�ɳ�������������������ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getOutNum(String salebillType,
			String SaleID, String SaleDetailID)
			throws nc.vo.pub.BusinessException {
		nc.vo.pub.lang.UFDouble ntotalinventorynumber = null;
		String strSQL = "Select ntotalinventorynumber From so_saleexecute where creceipttype = ? and csaleid = ? and corder_bid = ? ";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// creceipttype
			stmt.setString(1, salebillType);
			// csaleid
			stmt.setString(2, SaleID);
			// corder_bid
			stmt.setString(3, SaleDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				ntotalinventorynumber = (n == null ? new UFDouble(0)
						: new UFDouble(n));
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return ntotalinventorynumber;
	}

	/**
	 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 */
	public nc.vo.pub.lang.UFDouble getOutSourceNum(Object[] obj)
			throws java.sql.SQLException, nc.vo.pub.BusinessException,
			java.lang.Exception {
		// ƽ̨�ӿڣ�ȷ��ί�д����ĵ�������
		// ҵ������
		String SQLBussiness = null;
		String arrBussiness[] = null;

		IPFConfig bsPfConfig = (IPFConfig) NCLocator.getInstance().lookup(
				IPFConfig.class.getName());
		arrBussiness = bsPfConfig.getBusitypeByCorpAndStyle(obj[0].toString(),
				VerifyruleVO.WTDX);
		if (arrBussiness != null) {
			for (int i = 0; i < arrBussiness.length; i++) {
				if (SQLBussiness == null) {
					SQLBussiness = " so_sale.cbiztype = '"
							+ arrBussiness[i].toString() + "'";
				} else {
					SQLBussiness = SQLBussiness + " or so_sale.cbiztype = '"
							+ arrBussiness[i].toString() + "'";
				}
			}

			if (SQLBussiness != null) {
				SQLBussiness = " AND (" + SQLBussiness + " ) ";
			} else {
				return new UFDouble(0);
			}

			SCMEnv.out("ί�д���ҵ������:" + SQLBussiness);

		} else {
			return new UFDouble(0);
		}
		// ������
		// [0]String CorpID,��λID
		// [1]String WhClassID�ֿ�ID
		// [2]String InvID ���������ID
		// [3]UFBoolean IsExtractByFreeVO �Ƿ�������չ��
		// [4]String Free1������
		// [5]String Free2,
		// [6]String Free3,
		// [7]String Free4,
		// [8]String Free5,
		// [9]String Free6,
		// [10]String Free7,
		// [11]String Free8,
		// [12]String Free9,
		// [13]String Free10,
		// [14]String vbatchcode,���κ�
		// ����ֵ
		if (obj == null) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000042")/* @res "��洫�ݵĲ���Ϊ��" */);
		}
		UFDouble dbltrust = null;
		// SQL
		String SQLTrust = "";
		// ������
		String SQLSubWhere = "";
		// ��������
		String SQLGroupBy = "";
		// �������� �� �Ѿ���������
		SQLTrust = "SELECT sum(ISNULL(so_saleexecute.ntotalinventorynumber, 0) - ISNULL(so_saleexecute.ntotalbalancenumber, 0)) AS dblTrustQuantity ";
		SQLTrust += " FROM so_sale inner JOIN  so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid inner JOIN ";
		SQLTrust += "so_saleexecute ON so_sale.csaleid = so_saleexecute.csaleid AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";
		// ���۶������� ������δ���� �������Ѿ�����
		SQLTrust += " WHERE so_sale.creceipttype = '30' AND (so_saleexecute.bifpaybalance = 'N' or so_saleexecute.bifpaybalance is NULL) and 	so_sale.fstatus = "
				+ nc.ui.pub.bill.BillStatus.AUDIT;
		// ��˾����
		if (obj[0] != null)
			SQLSubWhere += " and so_sale.pk_corp ='" + obj[0].toString() + "' ";
		// //�ֿ�
		// if (obj[1] != null)
		// SQLSubWhere += "and so_sale.cwarehouseid ='" + obj[1].toString() + "'
		// ";
		// �����֯
		if (obj[1] != null)
			SQLSubWhere += "and so_sale.ccalbodyid ='" + obj[1].toString()
					+ "' ";
		// ���������
		if (obj[2] != null)
			SQLSubWhere += "and so_saleorder_b.cinventoryid ='"
					+ obj[2].toString() + "' ";
		// ���κ�
		if (obj[14] != null)
			SQLSubWhere += "and so_saleorder_b.cbatchid ='"
					+ obj[14].toString() + "' ";
		// ������
		// ��������չ��
		if (((UFBoolean) obj[3]).booleanValue()) {
			if (obj[4] == null) {
				SQLSubWhere += "and so_saleexecute.vfree1 is null ";
			} else {
				SQLSubWhere += "and so_saleexecute.vfree1 = '"
						+ obj[4].toString() + "' ";
			}
			if (obj[5] == null) {
				SQLSubWhere += "and so_saleexecute.vfree2 is null ";
			} else {
				SQLSubWhere += "and so_saleexecute.vfree2 = '"
						+ obj[5].toString() + "' ";
			}
			if (obj[6] == null) {
				SQLSubWhere += "and so_saleexecute.vfree3 is null ";
			} else {
				SQLSubWhere += "and so_saleexecute.vfree3 = '"
						+ obj[6].toString() + "' ";
			}
			if (obj[7] == null) {
				SQLSubWhere += "and so_saleexecute.vfree4 is null ";
			} else {
				SQLSubWhere += "and so_saleexecute.vfree4 = '"
						+ obj[7].toString() + "' ";
			}
			if (obj[8] == null) {
				SQLSubWhere += "and so_saleexecute.vfree5 is null ";
			} else {
				SQLSubWhere += "and so_saleexecute.vfree5 = '"
						+ obj[8].toString() + "' ";
			}
		}
		// ��ѯ���
		SQLTrust = SQLTrust + SQLSubWhere + SQLBussiness + SQLGroupBy;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLTrust);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal("dblTrustQuantity");
				dbltrust = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return dbltrust;
	}

	/**
	 * ���۶���δִ����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getPreSaleNum(
			nc.vo.pub.lang.UFDate StartDay, nc.vo.pub.lang.UFDate EndDay,
			String PKCalbody, String InvID) throws java.sql.SQLException,
			nc.vo.pub.BusinessException {
		nc.vo.pub.lang.UFDouble nnumber = null;
		String strSQL = "Select sum(so_saleorder_b.nnumber - isnull(so_saleexecute.ntotalinventorynumber,0)) "
				+ "FROM so_saleexecute RIGHT OUTER JOIN so_saleorder_b ON so_saleexecute.csale_bid = so_saleorder_b.corder_bid AND so_saleexecute.csaleid = so_saleorder_b.csaleid RIGHT OUTER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid "
				+ "where so_sale.dbilldate >= ? and so_sale.dbilldate <= ? and so_sale.ccalbodyid = ? and so_saleorder_b.cinvbasdocid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// start
			if (StartDay != null)
				stmt.setString(1, StartDay.toString());
			else
				stmt.setNull(1, Types.CHAR);
			// end
			if (EndDay != null)
				stmt.setString(2, EndDay.toString());
			else
				stmt.setNull(2, Types.CHAR);
			// ccalbodyid
			stmt.setString(3, PKCalbody);
			// cinvbasdocid
			stmt.setString(4, InvID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nnumber;
	}

	/**
	 * ʵ����������
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public nc.vo.pub.lang.UFDouble getSaleNum(nc.vo.pub.lang.UFDate StartDay,
			nc.vo.pub.lang.UFDate EndDay, String PKCalbody, String InvID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {
		nc.vo.pub.lang.UFDouble nnumber = null;
		String strSQL = "Select sum(so_saleexecute.ntotalinventorynumber) "
				+ "FROM so_saleexecute RIGHT OUTER JOIN so_saleorder_b ON so_saleexecute.csaleid = so_saleorder_b.csaleid AND "
				+ "so_saleexecute.csale_bid = so_saleorder_b.corder_bid RIGHT OUTER JOIN so_sale ON so_saleorder_b.csaleid = so_sale.csaleid "
				+ "where so_sale.dbilldate >= ? and so_sale.dbilldate <= ? and so_sale.ccalbodyid = ? and so_saleorder_b.cinventoryid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// start
			if (StartDay != null)
				stmt.setString(1, StartDay.toString());
			else
				stmt.setNull(1, Types.CHAR);
			// end
			if (EndDay != null)
				stmt.setString(2, EndDay.toString());
			else
				stmt.setNull(2, Types.CHAR);
			// ccalbodyid
			stmt.setString(3, PKCalbody);
			// cinvbasdocid
			stmt.setString(4, InvID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				BigDecimal n = rs.getBigDecimal(1);
				nnumber = (n == null ? new UFDouble(0) : new UFDouble(n));
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return nnumber;
	}

	/**
	 * ���ܣ�����Դ���ݲ�ѯ��Ӧ���� ������Object bids Ҫ���������id ���أ�nc.vo.so.so001.SaleOrderVO[]
	 * ���⣺BusinessException, SQLException ���ڣ�(2002-5-16 9:31:12)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 */
	public nc.vo.so.so001.SaleOrderVO[] getSaleOrderFromDrp(Object bids)
			throws BusinessException, SQLException {
		// �жϲ����Ƿ�Ϸ�
		if (bids == null && (bids instanceof ArrayList)) {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000043")/* @res "���������ϣ���������" */);
		}
		// ������ת��Ϊ��Ӧ����������
		ArrayList alBids = (ArrayList) bids;
		// where�Ӿ�
		StringBuffer sbfWhereStr = new StringBuffer();
		for (int i = 0; i < alBids.size(); i++) {
			// ����ӱ�id�ǿ�ƴwhere�Ӿ�
			if (alBids.get(i) != null) {
				if (sbfWhereStr.toString().trim().equals("")) {
					sbfWhereStr.append(" where (csourcebillbodyid='");
					sbfWhereStr.append((String) alBids.get(i));
					sbfWhereStr.append("' ");
				} else {
					sbfWhereStr.append(" or csourcebillbodyid='");
					sbfWhereStr.append((String) alBids.get(i));
					sbfWhereStr.append("' ");
				}
			}
		}
		if (!sbfWhereStr.toString().trim().equals("")) {
			// where�Ӿ�Ϸ���ѯ��������
			sbfWhereStr.append(")");
			return getSaleOrders(sbfWhereStr.toString());
		} else {
			throw new BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("sointerface",
							"UPPsointerface-000043")/* @res "���������ϣ���������" */);
		}

	}

	/**
	 * ���ܣ�������Դ����id��ѯ���۶��� ������ ���أ� ���⣺ ���ڣ�(2002-5-16 10:01:33) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.vo.so.so001.SaleOrderVO[]
	 * @param WhereStr
	 *            java.lang.String
	 */
	private nc.vo.so.so001.SaleOrderVO[] getSaleOrders(String WhereStr)
			throws SQLException {

		if (WhereStr != null)
			WhereStr = WhereStr + " and so_saleorder_b.frowstatus != "
					+ nc.ui.pub.bill.BillStatus.BLANKOUT;

		StringBuffer sbfSQL = new StringBuffer();
		sbfSQL
				.append("SELECT so_sale.csaleid,so_sale.pk_corp, so_sale.vreceiptcode,");
		sbfSQL
				.append("so_sale.creceipttype, so_sale.cbiztype, so_sale.finvoiceclass,");
		sbfSQL
				.append("so_sale.finvoicetype, so_sale.vaccountyear, so_sale.binitflag, so_sale.dbilldate,");
		sbfSQL
				.append("so_sale.ccustomerid, so_sale.cdeptid, so_sale.cemployeeid, so_sale.coperatorid, ");
		sbfSQL
				.append("so_sale.ctermprotocolid, so_sale.csalecorpid, so_sale.creceiptcustomerid,");
		sbfSQL
				.append("so_sale.vreceiveaddress, so_sale.creceiptcorpid, so_sale.ctransmodeid, ");
		sbfSQL
				.append("so_sale.ndiscountrate, so_sale.cwarehouseid, so_sale.veditreason, so_sale.bfreecustflag,");
		sbfSQL
				.append("so_sale.cfreecustid, so_sale.ibalanceflag, so_sale.nsubscription, so_sale.ccreditnum, ");
		sbfSQL
				.append("so_sale.nevaluatecarriage, so_sale.dmakedate, so_sale.capproveid, so_sale.dapprovedate,");
		sbfSQL
				.append("so_sale.fstatus, so_sale.vnote, so_sale.vdef1, so_sale.vdef2, so_sale.vdef3, so_sale.vdef4, so_sale.vdef5, so_sale.vdef6,");
		sbfSQL
				.append("so_sale.vdef7,so_sale.vdef8, so_sale.vdef9, so_sale.vdef10,so_sale.ccalbodyid,");
		sbfSQL
				.append("so_sale.bretinvflag,so_sale.boutendflag,so_sale.binvoicendflag,so_sale.breceiptendflag, ");
		sbfSQL
				.append("so_saleorder_b.corder_bid, so_saleorder_b.csaleid, so_saleorder_b.pk_corp, ");
		sbfSQL
				.append("so_saleorder_b.creceipttype, so_saleorder_b.csourcebillid, so_saleorder_b.csourcebillbodyid,");
		sbfSQL
				.append("so_saleorder_b.cinventoryid, so_saleorder_b.cunitid, so_saleorder_b.cpackunitid,");
		sbfSQL
				.append("so_saleorder_b.nnumber, so_saleorder_b.npacknumber, so_saleorder_b.cbodywarehouseid,");
		sbfSQL
				.append("so_saleorder_b.dconsigndate, so_saleorder_b.ddeliverdate, so_saleorder_b.blargessflag,");
		sbfSQL
				.append("so_saleorder_b.frownote, so_saleorder_b.fbatchstatus, so_saleorder_b.veditreason,");
		sbfSQL
				.append("so_saleorder_b.ccurrencytypeid, so_saleorder_b.nitemdiscountrate, so_saleorder_b.ndiscountrate,");
		sbfSQL
				.append("so_saleorder_b.nexchangeotobrate, so_saleorder_b.cbomorderid, so_saleorder_b.ntaxrate,");
		sbfSQL
				.append("so_saleorder_b.noriginalcurprice, so_saleorder_b.noriginalcurtaxprice, so_saleorder_b.noriginalcurnetprice,");
		sbfSQL
				.append("so_saleorder_b.noriginalcurtaxnetprice, so_saleorder_b.noriginalcurtaxmny, so_saleorder_b.noriginalcurmny,");
		sbfSQL
				.append("so_saleorder_b.noriginalcursummny, so_saleorder_b.noriginalcurdiscountmny, so_saleorder_b.nprice,");
		sbfSQL
				.append("so_saleorder_b.ntaxprice, so_saleorder_b.nnetprice, so_saleorder_b.ntaxnetprice,");
		sbfSQL
				.append("so_saleorder_b.ntaxmny, so_saleorder_b.nmny, so_saleorder_b.nsummny,");
		sbfSQL
				.append("so_saleorder_b.ndiscountmny, so_saleorder_b.coperatorid, so_saleorder_b.frowstatus ");
		sbfSQL.append("FROM so_saleorder_b LEFT OUTER JOIN ");
		sbfSQL.append("so_sale ON so_saleorder_b.csaleid = so_sale.csaleid ");
		sbfSQL.append(WhereStr);
		sbfSQL.append(" order by so_sale.csaleid ");

		SaleorderHVO saleHeader = null;
		SaleOrderVO hvo = null;

		SaleorderBVO[] items = null;
		SaleorderBVO saleItem = null;
		ArrayList alitems = null;
		ArrayList alHvos = new ArrayList();
		SCMEnv.out(sbfSQL.toString());

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSQL.toString());

			ResultSet rs = stmt.executeQuery();
			//
			String headerid = "";
			while (rs.next()) {
				saleItem = new SaleorderBVO();
				// drp_demandbill_h.cdemandbillhid
				String Cdemandbillhid = rs.getString(1);
				if (!Cdemandbillhid.equals(headerid)) {
					headerid = Cdemandbillhid;
					if (saleHeader != null && alitems != null
							&& alitems.size() > 0) {
						items = new SaleorderBVO[alitems.size()];
						for (int i = 0; i < alitems.size(); i++) {
							if (alitems.get(i) != null) {
								items[i] = (SaleorderBVO) alitems.get(i);
							}
						}
						hvo = new SaleOrderVO();
						hvo.setParentVO(saleHeader);
						if (items != null && items.length > 0) {
							hvo.setChildrenVO(items);
							alHvos.add(hvo);
						}
					}
					alitems = new ArrayList();
					saleHeader = new SaleorderHVO();
					saleHeader.setCsaleid(Cdemandbillhid == null ? null
							: Cdemandbillhid.trim());
					// pk_corp :
					String pk_corp = rs.getString(2);
					saleHeader.setPk_corp(pk_corp == null ? null : pk_corp
							.trim());
					// vreceiptcode :
					String vreceiptcode = rs.getString(3);
					saleHeader.setVreceiptcode(vreceiptcode == null ? null
							: vreceiptcode.trim());
					// creceipttype :
					String creceipttype = rs.getString(4);
					saleHeader.setCreceipttype(creceipttype == null ? null
							: creceipttype.trim());
					// cbiztype :
					String cbiztype = rs.getString(5);
					saleHeader.setCbiztype(cbiztype == null ? null : cbiztype
							.trim());
					// finvoiceclass :
					Integer finvoiceclass = (Integer) rs.getObject(6);
					saleHeader.setFinvoiceclass(finvoiceclass == null ? null
							: finvoiceclass);
					// finvoicetype :
					Integer finvoicetype = (Integer) rs.getObject(7);
					saleHeader.setFinvoicetype(finvoicetype == null ? null
							: finvoicetype);
					// vaccountyear :
					String vaccountyear = rs.getString(8);
					saleHeader.setVaccountyear(vaccountyear == null ? null
							: vaccountyear.trim());
					// binitflag :
					String binitflag = rs.getString(9);
					saleHeader.setBinitflag(binitflag == null ? null
							: new UFBoolean(binitflag.trim()));
					// dbilldate :
					String dbilldate = rs.getString(10);
					saleHeader.setDbilldate(dbilldate == null ? null
							: new UFDate(dbilldate.trim()));
					// ccustomerid :
					String ccustomerid = rs.getString(11);
					saleHeader.setCcustomerid(ccustomerid == null ? null
							: ccustomerid.trim());
					// cdeptid :
					String cdeptid = rs.getString(12);
					saleHeader.setCdeptid(cdeptid == null ? null : cdeptid
							.trim());
					// cemployeeid :
					String cemployeeid = rs.getString(13);
					saleHeader.setCemployeeid(cemployeeid == null ? null
							: cemployeeid.trim());
					// coperatorid :
					String coperatorid = rs.getString(14);
					saleHeader.setCoperatorid(coperatorid == null ? null
							: coperatorid.trim());
					// ctermprotocolid :
					String ctermprotocolid = rs.getString(15);
					saleHeader
							.setCtermprotocolid(ctermprotocolid == null ? null
									: ctermprotocolid.trim());
					// csalecorpid :
					String csalecorpid = rs.getString(16);
					saleHeader.setCsalecorpid(csalecorpid == null ? null
							: csalecorpid.trim());
					// creceiptcustomerid :
					String creceiptcustomerid = rs.getString(17);
					saleHeader
							.setCreceiptcustomerid(creceiptcustomerid == null ? null
									: creceiptcustomerid.trim());
					// vreceiveaddress :
					String vreceiveaddress = rs.getString(18);
					saleHeader
							.setVreceiveaddress(vreceiveaddress == null ? null
									: vreceiveaddress.trim());
					// creceiptcorpid :
					String creceiptcorpid = rs.getString(19);
					saleHeader.setCreceiptcorpid(creceiptcorpid == null ? null
							: creceiptcorpid.trim());
					// ctransmodeid :
					String ctransmodeid = rs.getString(20);
					saleHeader.setCtransmodeid(ctransmodeid == null ? null
							: ctransmodeid.trim());
					// ndiscountrate :
					BigDecimal ndiscountrate = (BigDecimal) rs.getObject(21);
					saleHeader.setNdiscountrate(ndiscountrate == null ? null
							: new UFDouble(ndiscountrate));
					// cwarehouseid :
					String cwarehouseid = rs.getString(22);
					saleHeader.setCwarehouseid(cwarehouseid == null ? null
							: cwarehouseid.trim());
					// veditreason :
					String veditreason = rs.getString(23);
					saleHeader.setVeditreason(veditreason == null ? null
							: veditreason.trim());
					// bfreecustflag :
					String bfreecustflag = rs.getString(24);
					saleHeader.setBfreecustflag(bfreecustflag == null ? null
							: new UFBoolean(bfreecustflag.trim()));
					// cfreecustid :
					String cfreecustid = rs.getString(25);
					saleHeader.setCfreecustid(cfreecustid == null ? null
							: cfreecustid.trim());
					// ibalanceflag :
					Integer ibalanceflag = (Integer) rs.getObject(26);
					saleHeader.setIbalanceflag(ibalanceflag == null ? null
							: ibalanceflag);
					// nsubscription :
					BigDecimal nsubscription = (BigDecimal) rs.getObject(27);
					saleHeader.setNsubscription(nsubscription == null ? null
							: new UFDouble(nsubscription));
					// ccreditnum :
					String ccreditnum = rs.getString(28);
					saleHeader.setCcreditnum(ccreditnum == null ? null
							: ccreditnum.trim());
					// nevaluatecarriage :
					BigDecimal nevaluatecarriage = (BigDecimal) rs
							.getObject(29);
					saleHeader
							.setNevaluatecarriage(nevaluatecarriage == null ? null
									: new UFDouble(nevaluatecarriage));
					// dmakedate :
					String dmakedate = rs.getString(30);
					saleHeader.setDmakedate(dmakedate == null ? null
							: new UFDate(dmakedate.trim()));
					// capproveid :
					String capproveid = rs.getString(31);
					saleHeader.setCapproveid(capproveid == null ? null
							: capproveid.trim());
					// dapprovedate :
					String dapprovedate = rs.getString(32);
					saleHeader.setDapprovedate(dapprovedate == null ? null
							: new UFDate(dapprovedate.trim()));
					// fstatus :
					Integer fstatus = (Integer) rs.getObject(33);
					saleHeader.setFstatus(fstatus == null ? null : fstatus);
					// vnote :
					String vnote = rs.getString(34);
					saleHeader.setVnote(vnote == null ? null : vnote.trim());
					// vdef1 :
					String vdef1 = rs.getString(35);
					saleHeader.setVdef1(vdef1 == null ? null : vdef1.trim());
					// vdef2 :
					String vdef2 = rs.getString(36);
					saleHeader.setVdef2(vdef2 == null ? null : vdef2.trim());
					// vdef3 :
					String vdef3 = rs.getString(37);
					saleHeader.setVdef3(vdef3 == null ? null : vdef3.trim());
					// vdef4 :
					String vdef4 = rs.getString(38);
					saleHeader.setVdef4(vdef4 == null ? null : vdef4.trim());
					// vdef5 :
					String vdef5 = rs.getString(39);
					saleHeader.setVdef5(vdef5 == null ? null : vdef5.trim());
					// vdef6 :
					String vdef6 = rs.getString(40);
					saleHeader.setVdef6(vdef6 == null ? null : vdef6.trim());
					// vdef7 :
					String vdef7 = rs.getString(41);
					saleHeader.setVdef7(vdef7 == null ? null : vdef7.trim());
					// vdef8 :
					String vdef8 = rs.getString(42);
					saleHeader.setVdef8(vdef8 == null ? null : vdef8.trim());
					// vdef9 :
					String vdef9 = rs.getString(43);
					saleHeader.setVdef9(vdef9 == null ? null : vdef9.trim());
					// vdef10 :
					String vdef10 = rs.getString(44);
					saleHeader.setVdef10(vdef10 == null ? null : vdef10.trim());
					// ccalbodyid :
					String ccalbodyid = rs.getString(45);
					saleHeader.setCcalbodyid(ccalbodyid == null ? null
							: ccalbodyid.trim());
					// bretinvflag :
					String bretinvflag = rs.getString(46);
					saleHeader.setBretinvflag(bretinvflag == null ? null
							: new UFBoolean(bretinvflag.trim()));
					// boutendflag :
					String boutendflag = rs.getString(47);
					saleHeader.setBoutendflag(boutendflag == null ? null
							: new UFBoolean(boutendflag.trim()));
					// binvoicendflag :
					String binvoicendflag = rs.getString(48);
					saleHeader.setBinvoicendflag(binvoicendflag == null ? null
							: new UFBoolean(binvoicendflag.trim()));
					// breceiptendflag :
					String breceiptendflag = rs.getString(49);
					saleHeader
							.setBreceiptendflag(breceiptendflag == null ? null
									: new UFBoolean(breceiptendflag.trim()));
					//
				}

				//
				String corder_bid = rs.getString(50);
				saleItem.setCorder_bid(corder_bid == null ? null : corder_bid
						.trim());
				//
				String csaleid = rs.getString(51);
				saleItem.setCsaleid(csaleid == null ? null : csaleid.trim());
				//
				String ccorpid = rs.getString(52);
				saleItem.setPkcorp(ccorpid == null ? null : ccorpid.trim());
				//
				String creceipttype = rs.getString(53);
				saleItem.setCreceipttype(creceipttype == null ? null
						: creceipttype.trim());
				//
				String csourcebillid = rs.getString(54);
				saleItem.setCsourcebillid(csourcebillid == null ? null
						: csourcebillid.trim());
				//
				String csourcebillbodyid = rs.getString(55);
				saleItem.setCsourcebillbodyid(csourcebillbodyid == null ? null
						: csourcebillbodyid.trim());
				//
				String cinventoryid = rs.getString(56);
				saleItem.setCinventoryid(cinventoryid == null ? null
						: cinventoryid.trim());
				//
				String cunitid = rs.getString(57);
				saleItem.setCunitid(cunitid == null ? null : cunitid.trim());
				//
				String cpackunitid = rs.getString(58);
				saleItem.setCpackunitid(cpackunitid == null ? null
						: cpackunitid.trim());
				//
				BigDecimal nnumber = (BigDecimal) rs.getObject(59);
				saleItem.setNnumber(nnumber == null ? null : new UFDouble(
						nnumber));
				//
				BigDecimal npacknumber = (BigDecimal) rs.getObject(60);
				saleItem.setNpacknumber(npacknumber == null ? null
						: new UFDouble(npacknumber));
				//
				String cbodywarehouseid = rs.getString(61);
				saleItem.setCbodywarehouseid(cbodywarehouseid == null ? null
						: cbodywarehouseid.trim());
				//
				String dconsigndate = rs.getString(62);
				saleItem.setDconsigndate(dconsigndate == null ? null
						: new UFDate(dconsigndate.trim()));
				//
				String ddeliverdate = rs.getString(63);
				saleItem.setDdeliverdate(ddeliverdate == null ? null
						: new UFDate(ddeliverdate.trim()));
				//
				String blargessflag = rs.getString(64);
				saleItem.setBlargessflag(blargessflag == null ? null
						: new UFBoolean(blargessflag.trim()));
				//
				String frownote = rs.getString(65);
				saleItem.setFrownote(frownote == null ? null : frownote.trim());
				//
				Integer fbatchstatus = (Integer) rs.getObject(66);
				saleItem.setFbatchstatus(fbatchstatus == null ? null
						: fbatchstatus);
				//
				String veditreason = rs.getString(67);
				saleItem.setVeditreason(veditreason == null ? null
						: veditreason.trim());
				//
				String ccurrencytypeid = rs.getString(68);
				saleItem.setCcurrencytypeid(ccurrencytypeid == null ? null
						: ccurrencytypeid.trim());
				//
				BigDecimal nitemdiscountrate = (BigDecimal) rs.getObject(69);
				saleItem.setNitemdiscountrate(nitemdiscountrate == null ? null
						: new UFDouble(nitemdiscountrate));
				//
				BigDecimal ndiscountrate = (BigDecimal) rs.getObject(70);
				saleItem.setNdiscountrate(ndiscountrate == null ? null
						: new UFDouble(ndiscountrate));
				//
				BigDecimal nexchangeotobrate = (BigDecimal) rs.getObject(71);
				saleItem.setNexchangeotobrate(nexchangeotobrate == null ? null
						: new UFDouble(nexchangeotobrate));
				//
				String cbomorderid = rs.getString(72);
				saleItem.setCbomorderid(cbomorderid == null ? null
						: cbomorderid.trim());
				//
				BigDecimal ntaxrate = (BigDecimal) rs.getObject(73);
				saleItem.setNtaxrate(ntaxrate == null ? null : new UFDouble(
						ntaxrate));
				//
				BigDecimal noriginalcurprice = (BigDecimal) rs.getObject(74);
				saleItem.setNoriginalcurprice(noriginalcurprice == null ? null
						: new UFDouble(noriginalcurprice));
				//
				BigDecimal noriginalcurtaxprice = (BigDecimal) rs.getObject(75);
				saleItem
						.setNoriginalcurtaxprice(noriginalcurtaxprice == null ? null
								: new UFDouble(noriginalcurtaxprice));
				//
				BigDecimal noriginalcurnetprice = (BigDecimal) rs.getObject(76);
				saleItem
						.setNoriginalcurnetprice(noriginalcurnetprice == null ? null
								: new UFDouble(noriginalcurnetprice));
				//
				BigDecimal noriginalcurtaxnetprice = (BigDecimal) rs
						.getObject(77);
				saleItem
						.setNoriginalcurtaxnetprice(noriginalcurtaxnetprice == null ? null
								: new UFDouble(noriginalcurtaxnetprice));
				//
				BigDecimal noriginalcurtaxmny = (BigDecimal) rs.getObject(78);
				saleItem
						.setNoriginalcurtaxmny(noriginalcurtaxmny == null ? null
								: new UFDouble(noriginalcurtaxmny));
				//
				BigDecimal noriginalcurmny = (BigDecimal) rs.getObject(79);
				saleItem.setNoriginalcurmny(noriginalcurmny == null ? null
						: new UFDouble(noriginalcurmny));
				//
				BigDecimal noriginalcursummny = (BigDecimal) rs.getObject(80);
				saleItem
						.setNoriginalcursummny(noriginalcursummny == null ? null
								: new UFDouble(noriginalcursummny));
				//
				BigDecimal noriginalcurdiscountmny = (BigDecimal) rs
						.getObject(81);
				saleItem
						.setNoriginalcurdiscountmny(noriginalcurdiscountmny == null ? null
								: new UFDouble(noriginalcurdiscountmny));
				//
				BigDecimal nprice = (BigDecimal) rs.getObject(82);
				saleItem
						.setNprice(nprice == null ? null : new UFDouble(nprice));
				//
				BigDecimal ntaxprice = (BigDecimal) rs.getObject(83);
				saleItem.setNtaxprice(ntaxprice == null ? null : new UFDouble(
						ntaxprice));
				//
				BigDecimal nnetprice = (BigDecimal) rs.getObject(84);
				saleItem.setNnetprice(nnetprice == null ? null : new UFDouble(
						nnetprice));
				//
				BigDecimal ntaxnetprice = (BigDecimal) rs.getObject(85);
				saleItem.setNtaxnetprice(ntaxnetprice == null ? null
						: new UFDouble(ntaxnetprice));
				//
				BigDecimal ntaxmny = (BigDecimal) rs.getObject(86);
				saleItem.setNtaxmny(ntaxmny == null ? null : new UFDouble(
						ntaxmny));
				//
				BigDecimal nmny = (BigDecimal) rs.getObject(87);
				saleItem.setNmny(nmny == null ? null : new UFDouble(nmny));
				//
				BigDecimal nsummny = (BigDecimal) rs.getObject(88);
				saleItem.setNsummny(nsummny == null ? null : new UFDouble(
						nsummny));
				//
				BigDecimal ndiscountmny = (BigDecimal) rs.getObject(89);
				saleItem.setNdiscountmny(ndiscountmny == null ? null
						: new UFDouble(ndiscountmny));
				//
				String coperatorid = rs.getString(90);
				saleItem.setCoperatorid(coperatorid == null ? null
						: coperatorid.trim());
				//
				Integer frowstatus = (Integer) rs.getObject(91);
				saleItem.setFrowstatus(frowstatus == null ? null : frowstatus);
				//
	
				alitems.add(saleItem);

			}
			if (saleHeader != null && alitems != null && alitems.size() > 0) {
				items = new SaleorderBVO[alitems.size()];
				for (int i = 0; i < alitems.size(); i++) {
					if (alitems.get(i) != null) {
						items[i] = (SaleorderBVO) alitems.get(i);
					}
				}
				hvo = new SaleOrderVO();
				hvo.setParentVO(saleHeader);
				if (items != null && items.length > 0) {
					hvo.setChildrenVO(items);
					alHvos.add(hvo);
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		SaleOrderVO[] vos = null;
		if (alHvos != null && alHvos.size() > 0) {
			vos = new SaleOrderVO[alHvos.size()];
			for (int i = 0; i < alHvos.size(); i++) {
				if (alHvos.get(i) != null) {
					vos[i] = (SaleOrderVO) alHvos.get(i);
				}
			}
			return vos;
		}
		return null;

	}

	/**
	 * ������Դ����ID��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String getSourceBillTypeID(String strDetailID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {
		String strSourceID = null;

		String strSQL = "Select creceipttype from so_saleinvoice_b Where cinvoice_bid ";

		strSQL = strSQL + " =  ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			// pk
			stmt.setString(1, strDetailID);

			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {

				strSourceID = rs.getString(1);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return strSourceID;
	}

	/**
	 * ������Դ����ID��
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String[] getSourceID(String strBillType, String strDetailID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {
		String[] strSourceID = null;
		String strField = null;
		String strSQL = "Select csourcebillid,csourcebillbodyid from ";
		// ����
		if (strBillType.equals(SaleBillType.SaleOrder)) {
			strSQL = strSQL + " so_saleorder_b ";
			strField = "corder_bid";
		}
		// ��Ʊ
		if (strBillType.equals(SaleBillType.SaleInvoice)
				|| strBillType.equals(SaleBillType.SaleInitInvoice)) {
			strSQL = strSQL + " so_saleinvoice_b ";
			strField = "cinvoice_bid";
		}
		
		//v5.5������Ϊ�������ݣ�ԭ���߼������ã���ʱע�͵�
		// ������
		/*if (strBillType.equals(SaleBillType.SaleReceipt)) {
			strSQL = strSQL + " so_salereceipt_b ";
			strField = "creceipt_bid";
		}*/
	
		strSQL = strSQL + " Where " + strField + " =  ?";
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);
			// pk
			stmt.setString(1, strDetailID);
			ResultSet rs = stmt.executeQuery();
			//
			if (rs.next()) {
				strSourceID = new String[2];
				strSourceID[0] = rs.getString(1);
				strSourceID[1] = rs.getString(2);
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
		return strSourceID;
	}

	/**
	 * �����ṩ�����ж����۳��ⵥ�ܷ�ȡ��ǩ�֡�������ȡ��ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return boolean
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public boolean isSaleOut(String SaleID, String SaleDetailID)
			throws nc.vo.pub.BusinessException {

		// �������۳��ⵥ����Դ��ͬ�����ж�
		// �����۶������ڳ��������۷�Ʊ���ڳ��������۷�����
		// BillType.equals(nc.ui.scm.so.SaleBillType.SaleInitOrder)

		// �ۼƽ���������Ϊ����������

		String SQLRelation = "SELECT ntotalbalancenumber FROM so_saleexecute WHERE ";
		SQLRelation = SQLRelation + " csaleid = '" + SaleID + "'";
		SQLRelation = SQLRelation + "  and csale_bid = '" + SaleDetailID + "'";

		BigDecimal dblNumber = new BigDecimal(0);

		// ���ݿ������
		Connection con = null;
		PreparedStatement stmt = null;

		UFBoolean bResult = new UFBoolean(false);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRelation);

			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {

				Object o = rstNumber.getObject("ntotalbalancenumber");

				if (o != null) {

					dblNumber = new BigDecimal(o.toString());

					bResult = (dblNumber.doubleValue() == 0 ? new UFBoolean(
							false) : new UFBoolean(true));

				}

				else

					bResult = new UFBoolean(false);
			}

		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return bResult.booleanValue();
	}

	/**
	 * �����ṩ�����ж����ת���۳��ⵥ�����ڳ����ܷ�ȡ��ǩ�֡�������ȡ��ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return boolean
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public boolean isSaleOutFor4Hand42(String SaleID, String SaleDetailID)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {

		// ���ת���۳��ⵥ�����ڳ���

		String SQLRelation = "SELECT csaleid FROM so_saleorder_b WHERE ";
		SQLRelation = SQLRelation + " csaleid = '" + SaleID + "'";
		SQLRelation = SQLRelation + "  and corder_bid = '" + SaleDetailID + "'";
		// ��������Ϊ���۶��������ţ�30,3A��
		SQLRelation = SQLRelation + "  and  (creceipttype = "
				+ nc.ui.scm.so.SaleBillType.SaleOrder
				+ ")";
		// ���۶��������Ѿ����ϵĵ��ݳ���
		SQLRelation = SQLRelation + "  and frowstatus = "
				+ nc.ui.pub.bill.BillStatus.BLANKOUT;

		// ���ݿ������
		Connection con = null;
		PreparedStatement stmt = null;

		UFBoolean bResult = new UFBoolean(false);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRelation);

			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {

				bResult = (rstNumber.getString("csaleid") == null ? new UFBoolean(
						false)
						: new UFBoolean(true));

			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return bResult.booleanValue();
	}

	/**
	 * ����Ƿ񳬹�����������
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return boolean
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private boolean isSaleOverOut(String IC003, UFDouble IC004,
			String SaleDetailID, UFDouble SaleOutNum)
			throws java.sql.SQLException, nc.vo.pub.BusinessException {

		// if (IC003.equals("Y")) return false;

		String SQLRelation = "SELECT so_saleorder_b.nnumber, (CASE WHEN so_saleexecute.ntotalinventorynumber IS NULL  THEN 0 ELSE so_saleexecute.ntotalinventorynumber END) AS inventorynumber  ";

		SQLRelation = SQLRelation
				+ " FROM so_saleorder_b ,so_saleexecute  Where  so_saleorder_b.csaleid = so_saleexecute.csaleid AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";
		SQLRelation = SQLRelation + "  and csale_bid = '" + SaleDetailID + "'";

		BigDecimal dblNumber = new BigDecimal(0);
		BigDecimal dblinventorynumber = new BigDecimal(0);

		// ���ݿ������
		Connection con = null;
		PreparedStatement stmt = null;
		UFDouble nResult;
		UFBoolean bResult = new UFBoolean(false);

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRelation);

			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			if (rstNumber.next()) {

				Object oNnumber = rstNumber.getObject("nnumber");
				Object oInventorynumber = rstNumber
						.getObject("inventorynumber");


				if (oNnumber != null && oInventorynumber != null) {

					dblNumber = new BigDecimal(oNnumber.toString());
					dblinventorynumber = new BigDecimal(oInventorynumber
							.toString());
					// ���������������� ((((IC * 0.01) + 1) * NUM) - OUTNUM) -
					// OLDOUTNUM

					SCMEnv.out("dblNumber:"
							+ dblNumber.doubleValue());
					SCMEnv.out("dblinventorynumber:"
							+ dblinventorynumber.doubleValue());
					SCMEnv.out("SaleOutNum:" + SaleOutNum.doubleValue());

					// ��������-�ۼƷ�������-��������
					if (dblNumber.floatValue() >= 0) {
						if (IC003.equals("Y")) {
							nResult =new UFDouble(dblNumber.doubleValue()-
											dblinventorynumber.doubleValue()-SaleOutNum.doubleValue());
						} else

							nResult = new UFDouble(1).multiply(
									dblNumber.doubleValue()).sub(
									dblinventorynumber.doubleValue()).sub(
									SaleOutNum.doubleValue());

						bResult = (nResult.doubleValue() >= 0 ? new UFBoolean(
								false) : new UFBoolean(true));
					} else {
						// ����Ϊ���������
						if (IC003.equals("Y")) {
							nResult = new UFDouble(dblNumber.doubleValue()-
											dblinventorynumber.doubleValue()-SaleOutNum.doubleValue());
						} else

							nResult = new UFDouble(1).multiply(
									dblNumber.doubleValue()).sub(
									dblinventorynumber.doubleValue()).sub(
									SaleOutNum.doubleValue());

						bResult = (nResult.doubleValue() <= 0 ? new UFBoolean(
								false) : new UFBoolean(true));

					}

				} else
					bResult = new UFBoolean(false);
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return bResult.booleanValue();
	}

	/**
	 * �����ṩ��д�ۼƳ�����������������ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setDesOutNum(AggregatedValueObject outBillVO)
			throws java.sql.SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {

		// ��˾ID
		String pk_corp = outBillVO.getParentVO().getAttributeValue("pk_corp")
				.toString();

		// ȡ������
		nc.bs.pub.para.SysInitDMO initReferDMO = new nc.bs.pub.para.SysInitDMO();
		// ������IC003 (�Ƿ�������������)
		String IC003 = initReferDMO.getParaString(pk_corp, "SO23");
		// IC004 (���������ķ�Χ���ٷֱ�ֵ)
		String IC004 = initReferDMO.getParaString(pk_corp, "SO24");

		UFDouble nPercent = (IC004 == null ? new UFDouble("0") : new UFDouble(
				IC004));

		// ��Դ����ID csourcebillhid
		String csourcebillhid = null;
		// ��Դ���ݸ���ID csourcebiibid
		String csourcebiibid = null;

		UFDouble noutnum;
		String sourceBillID[];

		String SourceBillTypeID;

		String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			for (int i = 0; i < outBillVO.getChildrenVO().length; i++) {
				// ���۳��ⵥ�ӱ�VO
				CircularlyAccessibleValueObject bodyVO = outBillVO
						.getChildrenVO()[i];

				if (bodyVO.getAttributeValue("csourcetype") != null) {
					// ��Դ����ID csourcebillhid
					csourcebillhid = bodyVO.getAttributeValue("csourcebillhid")
							.toString();
					// ��Դ���ݸ���ID csourcebiibid
					csourcebiibid = bodyVO.getAttributeValue("csourcebillbid")
							.toString();
					// ���� noutnum
					noutnum = (bodyVO.getAttributeValue("noutnum") == null ? new UFDouble(
							"0")
							: new UFDouble(bodyVO.getAttributeValue("noutnum")
									.toString()));

					if (noutnum.doubleValue() != 0) {

						// �����Դ�����۶��� ֱ�ӻ�д���۶�����ʵ�ʳ�������
						if (bodyVO.getAttributeValue("csourcetype").equals(
								nc.ui.scm.so.SaleBillType.SaleOrder)) {
							// �ȽϷ�Χ
							if (isSaleOverOut(IC003, nPercent, csourcebiibid,
									noutnum)) {
								nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
										nc.bs.ml.NCLangResOnserver
												.getInstance()
												.getStrByID("sointerface",
														"UPPsointerface-000044")/*
																				 * @res
																				 * "������������Χ����������"
																				 */);
								throw e;
							}

							con = getConnection();
							stmt = con.prepareStatement(SQLUpdate);
							stmt.setBigDecimal(1, noutnum.toBigDecimal());
							// csale_bid
							stmt.setString(2, csourcebiibid);
							// csaleid
							stmt.setString(3, csourcebillhid);
							stmt.executeUpdate();
							// �ر�����
							stmt.close();
							con.close();

						} else {
							
							/**v5.5������Ϊ�������ݣ�ԭ���߼������ã���ʱע�͵�*/
							/*// �����Դ�Ƿ����� ͬ����д���۷�������������Ӧ�������嵥��ʵ�ʳ�������

							if (bodyVO.getAttributeValue("csourcetype").equals(
									nc.ui.scm.so.SaleBillType.SaleReceipt)) {

								// ��һ�� ������
								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, noutnum.toBigDecimal());
								// csale_bid
								stmt.setString(2, csourcebiibid);
								// csaleid
								stmt.setString(3, csourcebillhid);
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();
								// �ڶ��� ���۶���sourceBillID[1]
								sourceBillID = getSourceID(
										nc.ui.scm.so.SaleBillType.SaleReceipt,
										csourcebiibid);

								// �ȽϷ�Χ
								if (isSaleOverOut(IC003, nPercent,
										sourceBillID[1], noutnum)) {
									nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
											nc.bs.ml.NCLangResOnserver
													.getInstance()
													.getStrByID("sointerface",
															"UPPsointerface-000044")
																					 * @res
																					 * "������������Χ����������"
																					 );
									throw e;
								}

								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, noutnum.toBigDecimal());
								// csale_bid
								stmt.setString(2, sourceBillID[1]);
								// csaleid
								stmt.setString(3, sourceBillID[0]);
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();

							} */
								// �����Դ�����۷�Ʊ ͬ����д���۷�Ʊ����Ʊ���ε�ʵ�ʳ�������
								if (bodyVO
										.getAttributeValue("csourcetype")
										.equals(
												nc.ui.scm.so.SaleBillType.SaleInvoice)
										|| bodyVO
												.getAttributeValue(
														"csourcetype")
												.equals(
														nc.ui.scm.so.SaleBillType.SaleInitInvoice)) {

									// ��һ�� ���۷�Ʊ
									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									stmt.setBigDecimal(1, noutnum
											.toBigDecimal());
									// csale_bid
									stmt.setString(2, csourcebiibid);
									// csaleid
									stmt.setString(3, csourcebillhid);
									stmt.executeUpdate();
									// �ر�����
									stmt.close();
									con.close();
									// �ڶ��� ����������۶���sourceBillID[1]

									SourceBillTypeID = getSourceBillTypeID(csourcebiibid);

									if (SourceBillTypeID
											.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {

										sourceBillID = getSourceID(
												SaleBillType.SaleInvoice,
												csourcebiibid);

										// �ȽϷ�Χ
										if (isSaleOverOut(IC003, nPercent,
												sourceBillID[1], noutnum)) {
											nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
													nc.bs.ml.NCLangResOnserver
															.getInstance()
															.getStrByID(
																	"sointerface",
																	"UPPsointerface-000044")/*
																							 * @res
																							 * "������������Χ����������"
																							 */);
											throw e;
										}

										con = getConnection();
										stmt = con.prepareStatement(SQLUpdate);
										stmt.setBigDecimal(1, noutnum
												.toBigDecimal());
										// csale_bid
										stmt.setString(2, sourceBillID[1]);
										// csaleid
										stmt.setString(3, sourceBillID[0]);
										stmt.executeUpdate();
										// �ر�����
										stmt.close();
										con.close();
									}

								}

							

						}
					}
				}
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * �����ṩ��дӲ����������
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLockedFlag(ArrayList sorder_bid, ArrayList cfreezeids)
			throws java.sql.SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {
		if (sorder_bid == null || sorder_bid.size() == 0)
			return;

		String SQLUpdate = "update so_saleorder_b set cfreezeid = ? where corder_bid = ?";

		Connection con = getConnection();
		PreparedStatement stmt = con.prepareStatement(SQLUpdate);

		for (int i = 0; i < sorder_bid.size(); i++) {
			if ((String) cfreezeids.get(i) == null)
				stmt.setNull(1, Types.CHAR);
			else
				stmt.setString(1, (String) cfreezeids.get(i));
			// csale_bid
			stmt.setString(2, (String) sorder_bid.get(i));

			stmt.executeUpdate();

		}
		// �ر�����
		stmt.close();
		con.close();
	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 */
	private void setOOSNum(String pk_corp, String ccalbodyid, String cinvid,
			UFDouble ninnum, UFDate date, String csaleorderno, String operid,
			String[] freeitem) throws Exception {

		if (ninnum == null)
			return;

		if (ninnum.doubleValue() <= 0)
			return;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();
		nc.vo.so.so008.OosinfoItemVO[] itemVO = null;

		String freewhere = "";
		if (freeitem != null) {
			for (int i = 0; i < freeitem.length; i++) {
				if (freeitem[i] == null)
					freewhere = freewhere + " and (so_oosinfo_b.vfree"
							+ (i + 1) + " is null or so_oosinfo_b.vfree"
							+ (i + 1) + " = '') ";
				else
					freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1)
							+ "='" + freeitem[i] + "' ";
			}
		}

		UFDouble nnum = ninnum;
		// ָ��������
		if (csaleorderno != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and so_oosinfo_b.bfillflag = 'N' and so_oosinfo.vreceiptcode = '"
					+ csaleorderno
					+ "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
			itemVO = dmo.queryAllByCondition(where);
			if (itemVO == null || itemVO.length == 0)
				throw new BusinessException(NCLangResOnserver.getInstance()
						.getStrByID("sointerface", "UPPsointerface-000045",
								null, new String[] { csaleorderno }));
			// throw new BusinessException("û���ҵ����۵���Ϊ[" + csaleorderno
			// + "]�Ĳ�����¼!");
			nnum = assignOOSNum(itemVO, nnum, date, operid);
		}

		if (nnum != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

			itemVO = dmo.queryAllByCondition(where);
			nnum = assignOOSNum(itemVO, nnum, date, operid);
			if (nnum != null && nnum.doubleValue() > 0) {
				// ��ȡȱ����Ϣ
				where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
						+ pk_corp
						+ "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '"
						+ cinvid
						+ "'"
						+ " and so_oosinfo.ccalbodyid = '"
						+ ccalbodyid
						+ "' "
						+ freewhere
						+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
				itemVO = dmo.queryAllByCondition(where);
				nnum = assignOOSNum(itemVO, nnum, date, operid);
			}

		}

	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 */
	public void setOOSNum(nc.vo.ic.pub.bill.GeneralBillVO outBillVO)
			throws Exception {
		// String pk_corp,String cinvid,UFDouble ninnum,UFDate date,String
		// cbillhid,String operid
		nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = outBillVO.getHeaderVO();
		nc.vo.ic.pub.bill.GeneralBillItemVO[] bodyVOs = outBillVO.getItemVOs();

		String pk_corp = headVO.getPk_corp();
		UFDate date = headVO.getDbilldate();
		String operid = headVO.getCregister();
		String calbody = headVO.getPk_calbody();

		for (int i = 0; i < bodyVOs.length; i++) {
			String cinvid = bodyVOs[i].getCinventoryid();
			UFDouble ninnum = bodyVOs[i].getNinnum();

			String csaleorderno = bodyVOs[i].getVproductbatch();

			String[] freeitem = new String[5];

			freeitem[0] = bodyVOs[i].getVfree1();
			freeitem[1] = bodyVOs[i].getVfree2();
			freeitem[2] = bodyVOs[i].getVfree3();
			freeitem[3] = bodyVOs[i].getVfree4();
			freeitem[4] = bodyVOs[i].getVfree5();

			setOOSNum(pk_corp, calbody, cinvid, ninnum, date, csaleorderno,
					operid, freeitem);
		}
	}

	/**
	 * �����ṩ��д�ۼƳ�����������������ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setOutNum_bak(String pk_corp, String SourceBillTypeID,
			String csourcebillhid, String csourcebiibid, UFDouble noutnum)
			throws java.sql.SQLException, nc.vo.pub.BusinessException,
			javax.naming.NamingException, nc.bs.pub.SystemException {

		// ȡ������
		nc.bs.pub.para.SysInitDMO initReferDMO = new nc.bs.pub.para.SysInitDMO();
		// ������IC003 (�Ƿ�������������)
		String IC003 = initReferDMO.getParaString(pk_corp, "SO23");
		// IC004 (���������ķ�Χ���ٷֱ�ֵ)
		String IC004 = initReferDMO.getParaString(pk_corp, "SO24");
		UFDouble nPercent = (IC004 == null ? new UFDouble("0") : new UFDouble(
				IC004));

		String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=isnull(ntotalinventorynumber,0) + ? where csale_bid = ? and csaleid = ? and creceipttype = '30' ";
		String[] sourceBillID = null;
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			if (SourceBillTypeID != null) {
				// �����Դ�����۶��� ֱ�ӻ�д���۶�����ʵ�ʳ�������
				if (SourceBillTypeID
						.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
					// �ȽϷ�Χ
					if (isSaleOverOut(IC003, nPercent, csourcebiibid, noutnum)) {
						nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
								nc.bs.ml.NCLangResOnserver.getInstance()
										.getStrByID("sointerface",
												"UPPsointerface-000044")/*
																		 * @res
																		 * "������������Χ����������"
																		 */);
						throw e;
					}
					con = getConnection();
					stmt = con.prepareStatement(SQLUpdate);
					stmt.setBigDecimal(1, noutnum.toBigDecimal());
					// csale_bid
					stmt.setString(2, csourcebiibid);
					// csaleid
					stmt.setString(3, csourcebillhid);
					stmt.executeUpdate();
					// �ر�����
					stmt.close();
					con.close();
				} else {
					// �����Դ�Ƿ��˵� ͬ����д���۶�����ʵ�ʳ�������
					if (SourceBillTypeID.equals(SaleBillType.SoReceipt)
							|| SourceBillTypeID
									.equals(SaleBillType.SoReceiptPlan)) {
						if (SourceBillTypeID.equals(SaleBillType.SoReceipt)) {
							sourceBillID = getSourceID(SaleBillType.SoReceipt,
									csourcebiibid);
						}
						if (SourceBillTypeID.equals(SaleBillType.SoReceiptPlan)) {
							sourceBillID = getSourceID(
									SaleBillType.SoReceiptPlan, csourcebiibid);
						} // �ȽϷ�Χ
						if (isSaleOverOut(IC003, nPercent, sourceBillID[1],
								noutnum)) {
							nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
									nc.bs.ml.NCLangResOnserver.getInstance()
											.getStrByID("sointerface",
													"UPPsointerface-000044")/*
																			 * @res
																			 * "������������Χ����������"
																			 */);
							throw e;
						}
						con = getConnection();
						stmt = con.prepareStatement(SQLUpdate);
						stmt.setBigDecimal(1, noutnum.toBigDecimal());
						// csale_bid
						stmt.setString(2, sourceBillID[1]);
						// csaleid
						stmt.setString(3, sourceBillID[0]);
						stmt.executeUpdate();
						// �ر�����
						stmt.close();
						con.close();
					} else { 
						
						/**v5.5������Ϊ�������ݣ�ԭ���߼������ã���ʱע�͵�*/
						// �����Դ�Ƿ����� ͬ����д���۷�������������Ӧ�������嵥��ʵ�ʳ�������
						/*if (SourceBillTypeID
								.equals(nc.ui.scm.so.SaleBillType.SaleReceipt)) {
							SCMEnv.out("*****************"
									+ SourceBillTypeID);
							// ��һ�� ������
							con = getConnection();
							stmt = con.prepareStatement(SQLUpdate);
							stmt.setBigDecimal(1, noutnum.toBigDecimal());
							// csale_bid
							stmt.setString(2, csourcebiibid);
							// csaleid
							stmt.setString(3, csourcebillhid);
							stmt.executeUpdate();
							// �ر�����
							stmt.close();
							con.close();
							// �ڶ��� ���۶���sourceBillID[1]
							sourceBillID = getSourceID(
									nc.ui.scm.so.SaleBillType.SaleReceipt,
									csourcebiibid);
							// �ȽϷ�Χ
							if (isSaleOverOut(IC003, nPercent, sourceBillID[1],
									noutnum)) {
								nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
										nc.bs.ml.NCLangResOnserver
												.getInstance()
												.getStrByID("sointerface",
														"UPPsointerface-000044")
																				 * @res
																				 * "������������Χ����������"
																				 );
								throw e;
							}
							con = getConnection();
							stmt = con.prepareStatement(SQLUpdate);
							stmt.setBigDecimal(1, noutnum.toBigDecimal());
							// csale_bid
							stmt.setString(2, sourceBillID[1]);
							// csaleid
							stmt.setString(3, sourceBillID[0]);
							stmt.executeUpdate();
							// �ر�����
							stmt.close();
							con.close();
						}*/ 
						
						// �����Դ�����۷�Ʊ ͬ����д���۷�Ʊ����Ʊ���ε�ʵ�ʳ�������
							if (SourceBillTypeID
									.equals(nc.ui.scm.so.SaleBillType.SaleInvoice)
									|| SourceBillTypeID
											.equals(nc.ui.scm.so.SaleBillType.SaleInitInvoice)) {
								// ��һ�� ���۷�Ʊ
								con = getConnection();
								stmt = con.prepareStatement(SQLUpdate);
								stmt.setBigDecimal(1, noutnum.toBigDecimal());
								// csale_bid
								stmt.setString(2, csourcebiibid);
								// csaleid
								stmt.setString(3, csourcebillhid);
								stmt.executeUpdate();
								// �ر�����
								stmt.close();
								con.close();
								// �ڶ��� ����������۶���sourceBillID[1]
								String sSourceBillTypeID = getInvoiceSourceBillTypeID(csourcebiibid);
								SCMEnv
										.out("_______________________"
												+ SourceBillTypeID);
								if (sSourceBillTypeID != null
										&& sSourceBillTypeID
												.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
									sourceBillID = getInvoiceSourceID(
											SaleBillType.SaleInvoice,
											csourcebiibid);
									// �ȽϷ�Χ
									if (isSaleOverOut(IC003, nPercent,
											sourceBillID[1], noutnum)) {
										nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
												nc.bs.ml.NCLangResOnserver
														.getInstance()
														.getStrByID(
																"sointerface",
																"UPPsointerface-000044")/*
																						 * @res
																						 * "������������Χ����������"
																						 */);
										throw e;
									}
									con = getConnection();
									stmt = con.prepareStatement(SQLUpdate);
									stmt.setBigDecimal(1, noutnum
											.toBigDecimal());
									// csale_bid
									stmt.setString(2, sourceBillID[1]);
									// csaleid
									stmt.setString(3, sourceBillID[0]);
									stmt.executeUpdate();
									// �ر�����
									stmt.close();
									con.close();
								}
							}
						
					}
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public void setOutNum(String pk_corp, String SourceBillTypeID,
			String csourcebillhid, String csourcebiibid, UFDouble noutnum)
			throws nc.vo.pub.BusinessException {
		// 20050914 ���˽ӿ�
		throw new BusinessException(
				"This Interface has been closed,please contact SMA ");
	}

	/**
	 * �����ṩ��д�ۼƳ�����������������ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setOutNum(AggregatedValueObject outBillVO)
			throws nc.vo.pub.BusinessException {

		/** ��������� �����������null liujiaqing zhongwei* */
		// if (outBillVO == null || outBillVO.getParentVO() == null)
		// return;
		if ((outBillVO == null) || (outBillVO.getParentVO() == null) || (outBillVO.getChildrenVO() == null)
				|| (outBillVO.getChildrenVO().length == 0))
			/** it should never happen* */
			throw new BusinessException("ϵͳ�쳣,������");

		String csourcebiibid = null;

		UFDouble noutnum;
		// 2005-9-5 wsy ���� ��дӦ������
		UFDouble nshouldoutnum = null;

		CircularlyAccessibleValueObject[] billbody = outBillVO.getChildrenVO();
		if (billbody == null || billbody.length <= 0)
			return;

		ArrayList alistids = new ArrayList();
		String[] corder_bids = null;
		HashMap hm_saleoutclosed = new HashMap();
		Object id, closed;

		for (int i = 0, loop = billbody.length; i < loop; i++) {
			Object cfirsttype = billbody[i].getAttributeValue("cfirsttype");
			if (!"30".equals(cfirsttype))
				continue;

			// ��������id
			id = billbody[i].getAttributeValue("cfirstbillbid");
			if (id != null && !alistids.contains(id)) {
				alistids.add(id);
			}

			// �����ֹ��رձ�־
			closed = billbody[i].getAttributeValue("bcloseord");
			if (closed != null && id != null) {
				hm_saleoutclosed.put(id, closed);
			}

		}// end for

		// ����ǰ���������������������״̬
		HashMap hsntotalinventorynumber = null;
		if (alistids.size() > 0) {
			corder_bids = (String[]) alistids.toArray(new String[alistids
					.size()]);
			try {
				hsntotalinventorynumber = SOToolsDMO.getAnyValueUFDouble(
						"so_saleexecute", "ntotalinventorynumber", "csale_bid",
						corder_bids, " creceipttype = '30' ");	
				
			} catch (Exception e) {
				SCMEnv.out(e.getMessage());
				throw new BusinessException(e.getMessage());
			}
		}

		String SQLUpdate = "update so_saleexecute set ntotalinventorynumber=coalesce(ntotalinventorynumber,0) + ?,ntotalshouldoutnum= coalesce(ntotalshouldoutnum,0) + ? where csale_bid = ? and creceipttype = ?  ";

//		String sqlCheck = "select so_saleexecute.csale_bid from so_saleexecute,so_saleinvoice_b "
//				+ "where so_saleexecute.csale_bid=so_saleinvoice_b.cinvoice_bid "
//				+ "and so_saleexecute.ntotalinventorynumber>so_saleinvoice_b.nnumber "
//				+ "and csale_bid = ? and creceipttype = ?";
//		Connection con = null;
//		PreparedStatement stmt = null;

		try {

//			con = getConnection();
//			stmt = prepareStatement(con, SQLUpdate);
//			prepareStatement(con, sqlCheck);
			
			ArrayList<ArrayList> alValue = new ArrayList<ArrayList>();
			ArrayList alitem32,alitem30 = null;
			for (int i = 0; i < outBillVO.getChildrenVO().length; i++) {
				// ���۳��ⵥ�ӱ�VO
				CircularlyAccessibleValueObject bodyVO = outBillVO
						.getChildrenVO()[i];

				if (bodyVO.getAttributeValue("csourcetype") != null) {

					// ����
					noutnum = (bodyVO.getAttributeValue("noutnum") == null ? new UFDouble(
							"0")
							: new UFDouble(bodyVO.getAttributeValue("noutnum")
									.toString()));
					nshouldoutnum = (bodyVO.getAttributeValue("nshouldoutnum") == null ? new UFDouble(
							0)
							: new UFDouble(bodyVO.getAttributeValue(
									"nshouldoutnum").toString()));
					// �����Դ�����۷�Ʊ ͬ����д���۷�Ʊ����Ʊ���ε�ʵ�ʳ�������
					if (bodyVO.getAttributeValue("csourcetype").equals(
							nc.ui.scm.so.SaleBillType.SaleInvoice)
							|| bodyVO.getAttributeValue("csourcetype").equals(
									nc.ui.scm.so.SaleBillType.SaleInitInvoice)) {
						// ����ID
						csourcebiibid = bodyVO.getAttributeValue(
								"csourcebillbid").toString();
						
						// ��һ�� ���۷�Ʊ
						alitem32 = new ArrayList();
						alitem32.add(noutnum);
						alitem32.add(nshouldoutnum);
						alitem32.add(csourcebiibid);
						alitem32.add("32");
						alValue.add(alitem32);
						
						// ��һ�� ���۷�Ʊ
//						stmt.setBigDecimal(1, noutnum.toBigDecimal());
//						// /
//						stmt.setBigDecimal(2, nshouldoutnum.toBigDecimal());
//						// csale_bid
//						stmt.setString(3, csourcebiibid);
//						// csaleid
//						stmt.setString(4, "32");
//
//						executeUpdate(stmt);

						// csale_bid
						// stmtCheck.setString(3, csourcebiibid);
						// //csaleid
						// stmtCheck.setString(4, "32");
						// if (stmtCheck.executeQuery().next()){
						// throw new BusinessException("��������Ʊ���⣡");
						// }
						// �ر�����
						// stmt.close();
						// con.close();
						
						
						// �ڶ��� ����������۶���sourceBillID[1]
						csourcebiibid = bodyVO.getAttributeValue("cfirstbillbid").toString();
						alitem30 = new ArrayList();
						alitem30.add(noutnum);
						alitem30.add(nshouldoutnum);
						alitem30.add(csourcebiibid);
						alitem30.add(SaleBillType.SaleOrder);
						alValue.add(alitem30);
						
						// ����ID
//						csourcebiibid = bodyVO.getAttributeValue(
//								"cfirstbillbid").toString();
//						// con = getConnection();
//						// stmt = con.prepareStatement(SQLUpdate);
//						stmt.setBigDecimal(1, noutnum.toBigDecimal());
//						// /
//						stmt.setBigDecimal(2, nshouldoutnum.toBigDecimal());
//						// csale_bid
//						stmt.setString(3, csourcebiibid);
//						// csaleid
//						stmt.setString(4, "30");
//
//						executeUpdate(stmt);
						// �ر�����
						// stmt.close();
						// con.close();
					} else {
						//
						csourcebiibid = bodyVO.getAttributeValue("cfirstbillbid").toString();
						//
						String csourcetype = bodyVO.getAttributeValue("cfirsttype").toString();
						// ����
						noutnum = (bodyVO.getAttributeValue("noutnum") == null ? UFDouble.ZERO_DBL
								: new UFDouble(bodyVO.getAttributeValue("noutnum").toString()));
						nshouldoutnum = (bodyVO
								.getAttributeValue("nshouldoutnum") == null ? UFDouble.ZERO_DBL
								: new UFDouble(bodyVO.getAttributeValue("nshouldoutnum").toString()));
						
						alitem30 = new ArrayList();
						alitem30.add(noutnum);
						alitem30.add(nshouldoutnum);
						alitem30.add(csourcebiibid);
						alitem30.add(csourcetype);
						alValue.add(alitem30);						
					}
				}
			}
			
			ArrayList<Integer> alType = new ArrayList<Integer>();
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_UFDOUBLE));
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_UFDOUBLE));
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_STRING));
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_STRING));
			
			new SmartDMO().executeUpdateBatch(SQLUpdate, alValue, alType);
			
//			executeBatch(stmt);
			
			// ����������״̬
			if (corder_bids != null && corder_bids.length > 0) {
				// ��ѯ��������Ϣ
				nc.impl.scm.so.so001.SaleOrderDMO saledmo = new nc.impl.scm.so.so001.SaleOrderDMO();
				nc.vo.so.so001.SaleorderBVO[] oldordbvos = (nc.vo.so.so001.SaleorderBVO[]) saledmo
						.queryBodyDataForUpdateStatus(corder_bids);
				if (oldordbvos == null || oldordbvos.length <= 0)
					return;

				if (hsntotalinventorynumber != null) {
					// �����޸�ǰ�ĳ�������
					for (int i = 0, loop = oldordbvos.length; i < loop; i++) {
						oldordbvos[i]
								.setNtotalinventorynumber_old((UFDouble) hsntotalinventorynumber
										.get(oldordbvos[i].getCorder_bid()));
					}
				}

				// ��¼�����ֹ��رձ��
				Object value;
				for (SaleorderBVO bvo : oldordbvos) {
					value = hm_saleoutclosed.get(bvo.getCorder_bid());
					bvo.bsaleoutclosed = value == null ? null
							: (UFBoolean) value;
					bvo.setAttributeValue("ifRetOut", outBillVO.getParentVO().getAttributeValue("boutretflag"));
				}

				// �������״̬�߼�
				saledmo.processOutState(oldordbvos);

			}

		} catch (Exception e) {
			SCMEnv.out(e);
			throw new BusinessException(e.getMessage());
		}

	}

	/**
	 * ���ߣ��ν� ���ܣ�Ϊʵ�ַ�Ʊ��ʽ���ɳ��ⵥ����VOת��ǰ�ķֵ� ������vos ��Ҫ�ֵ������۶������� ���أ��ֵ���ɺ�����۶������� ���⣺
	 * ���ڣ�(2002-4-2 9:57:36) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־�� �޸����ڣ�2003-11-06 �޸��ˣ�����
	 * 
	 * @return nc.vo.po.OrderVO[]
	 * @param vos
	 *            nc.vo.po.OrderVO[]
	 * @exception java.sql.SQLException
	 *                �쳣˵����
	 */
	public nc.vo.so.so002.SaleinvoiceVO[] splitInvoiceVOForIC(
			nc.vo.so.so002.SaleinvoiceVO vos) throws java.sql.SQLException,
			BusinessException, javax.naming.NamingException, SystemException {
		nc.vo.so.so002.SaleinvoiceVO[] resultVOs = null;
		nc.impl.scm.so.pub.FetchValueDMO soDMO = new nc.impl.scm.so.pub.FetchValueDMO();
		try {

			// Ĭ�Ϸֵ���ʽΪ���ֿ⡱
			String splitMode = "�ֿ�";/*-=notranslate=-*/
			// String pk_corp =
			// ((nc.vo.so.so001.SaleorderHVO)vos.getParentVO()).getPk_corp();
			// update yt 2003-11-06
			String pk_corp = ((nc.vo.so.so002.SaleVO) vos.getParentVO())
					.getPk_corp();
			try {
				// ȡ������
				nc.bs.pub.para.SysInitDMO initReferDMO = new nc.bs.pub.para.SysInitDMO();
				// ������IC035 ()
				String IC035 = initReferDMO.getParaString(pk_corp, "IC035");
				if (IC035 != null)
					splitMode = IC035;
			} catch (Exception e) {
				throw new BusinessException(e.getMessage());
			}
			// ��������С��ֿ⡱�ֶ�Ϊ�գ�ȡ���Ĭ�ϲֿ�
			Hashtable table = null;
			Vector vCbaseids = new Vector();
			nc.vo.so.so002.SaleinvoiceBVO[] items = null;
			String[] cBaseids = null;
			items = (nc.vo.so.so002.SaleinvoiceBVO[]) vos.getChildrenVO();
			// ȡ��û�ж�Ӧ�ֿ�Ĵ������ID����
			for (int j = 0; j < items.length; j++) {
				if (items[j].getCbodywarehouseid() == null
						|| items[j].getCbodywarehouseid().trim().equals("")) {
					if (!vCbaseids.contains(items[j].getCinvbasdocid())) {
						vCbaseids.addElement(items[j].getCinvbasdocid());
					}
				}
			}
			// ȡ�ô����Ĭ�ϲֿ�
			if (vCbaseids != null && vCbaseids.size() > 0) {
				cBaseids = new String[vCbaseids.size()];
				vCbaseids.copyInto(cBaseids);
				table = soDMO.fetchArrayValue("bd_produce", "pk_stordoc",
						"pk_invbasdoc", cBaseids);
				if (table == null || table.size() <= 0)
					throw new BusinessException(nc.bs.ml.NCLangResOnserver
							.getInstance().getStrByID("sointerface",
									"UPPsointerface-000048")/*
															 * @res
															 * "û���ҵ������Ӧ�Ĳֿ⣬�޷��������۳��ⵥ��"
															 */);
			}
			if (table != null && table.size() > 0) {
				for (int j = 0; j < items.length; j++) {
					if (items[j].getCbodywarehouseid() == null
							|| items[j].getCbodywarehouseid().trim() == "") {
						if (table.containsKey(items[j].getCinvbasdocid()))
							items[j].setCbodywarehouseid((String) table
									.get(items[j].getCinvbasdocid()));
						else
							throw new BusinessException(
									nc.bs.ml.NCLangResOnserver.getInstance()
											.getStrByID("sointerface",
													"UPPsointerface-000049")/*
																			 * @res
																			 * "û���ҵ������Ӧ�Ĳֿ⣬�޷����ɲɹ���ⵥ��"
																			 */);
					}
				}
			}
			// ���ֿ�ֵ�
			if (splitMode.equalsIgnoreCase("�ֿ�")) {/*-=notranslate=-*/
				resultVOs = (nc.vo.so.so002.SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
						.getSplitVO("nc.vo.so.so002.SaleinvoiceVO",
								"nc.vo.so.so002.SaleVO",
								"nc.vo.so.so002.SaleinvoiceBVO", vos, null,
								new String[] { "cbodywarehouseid" });
			}
			// ���ֿ�+����Ա�ֵ�
			else if (splitMode.equalsIgnoreCase("�ֿ�+����Ա")) {/*-=notranslate=-*/
				//
				String[] cInventoryids = null;
				String[] cWarehouseids = null;
				String cinvid = null;
				String cwarehouseid = null;
				Vector vInvids = new Vector();
				Vector vWarehouseids = new Vector();
				Vector vInvAndStores = new Vector();
				for (int j = 0; j < items.length; j++) {
					cinvid = items[j].getCinventoryid();
					cwarehouseid = items[j].getCbodywarehouseid();
					if (items[j].getCbodywarehouseid() != null
							&& items[j].getCbodywarehouseid().trim() != ""
							&& !vInvAndStores.contains(cinvid + cWarehouseids)) {
						vInvids.addElement(cinvid == null ? "" : cinvid);
						vWarehouseids.addElement(cWarehouseids);
					}
				}
				if (vInvAndStores.size() > 0) {
					cInventoryids = new String[vInvids.size()];
					vInvids.copyInto(cInventoryids);
					cWarehouseids = new String[vWarehouseids.size()];
					vWarehouseids.copyInto(cWarehouseids);
				}
				// ���ÿ�����ӿ�ȡ�ÿ��Ա
				// StoreadminDMO storedmo = new StoreadminDMO();
				IICToPU_StoreadminDMO storedmo = (IICToPU_StoreadminDMO) NCLocator
						.getInstance().lookup(
								IICToPU_StoreadminDMO.class.getName());

				// ���Ա��ֵ
				for (int j = 0; j < items.length; j++) {
					cinvid = items[j].getCinventoryid();
					cwarehouseid = items[j].getCbodywarehouseid();
					if (items[j].getCbodywarehouseid() != null
							&& items[j].getCbodywarehouseid().trim() != "") {
						items[j].setStoreAdmin(storedmo.getWHManager(pk_corp,
								null, cwarehouseid, cinvid));
					}
				}
				// �ֵ�
				resultVOs = (nc.vo.so.so002.SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
						.getSplitVO("nc.vo.so.so002.SaleinvoiceVO",
								"nc.vo.so.so002.SaleVO",
								"nc.vo.so.so002.SaleinvoiceBVO", vos, null,
								new String[] { "cbodywarehouseid",
										"cstoreadmin" });
			}
			// ���ֿ�+�������ֵ����ֿ�+�������ĩ��
			else if (splitMode.equalsIgnoreCase("�ֿ�+�������")/*-=notranslate=-*/
					|| splitMode.equalsIgnoreCase("�ֿ�+�������ĩ��")) {/*-=notranslate=-*/
				Vector vBaseids = new Vector();
				String cinvid = null;
				String[] cinvids = null;
				// ȡ�ô����Ӧ�Ĵ������
				for (int j = 0; j < items.length; j++) {
					cinvid = items[j].getCinvbasdocid();
					if (!vBaseids.contains(cinvid))
						vBaseids.addElement(cinvid);
				}
				if (vBaseids.size() > 0) {
					cinvids = new String[vBaseids.size()];
					vBaseids.copyInto(cinvids);
					table = soDMO.fetchArrayValue("bd_invbasdoc", "pk_invcl",
							"pk_invbasdoc", cinvids);
					// ������ำֵ
					for (int j = 0; j < items.length; j++) {
						cinvid = items[j].getCinvbasdocid();
						if (vBaseids.contains(cinvid))
							items[j].setInvSort((String) table.get(cinvid));
					}
				}
				// �ֵ�
				resultVOs = (nc.vo.so.so002.SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
						.getSplitVO("nc.vo.so.so002.SaleinvoiceVO",
								"nc.vo.so.so002.SaleVO",
								"nc.vo.so.so002.SaleinvoiceBVO", vos, null,
								new String[] { "cbodywarehouseid", "cinvsort" });
			}
			// ���ֿ�+��Ʒ�ֵ�
			else if (splitMode.equalsIgnoreCase("�ֿ�+����Ʒ")) {/*-=notranslate=-*/
				// �ֵ�
				resultVOs = (nc.vo.so.so002.SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
						.getSplitVO("nc.vo.so.so002.SaleinvoiceVO",
								"nc.vo.so.so002.SaleVO",
								"nc.vo.so.so002.SaleinvoiceBVO", vos, null,
								new String[] { "cbodywarehouseid",
										"cinventoryid" });
			}
			// �������޸�Ϊ����
			if (resultVOs != null) {
				for (int i = 0; i < resultVOs.length; i++) {
					if (resultVOs[i].getChildrenVO() != null) {
						for (int j = 0; j < resultVOs[i].getChildrenVO().length; j++) {
							resultVOs[i].getChildrenVO()[j]
									.setStatus(VOStatus.NEW);
						}
					}
				}
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else
				throw new BusinessException(nc.bs.ml.NCLangResOnserver
						.getInstance().getStrByID("sointerface",
								"UPPsointerface-000054")/*
														 * @res
														 * "���۷�Ʊ��ʽ���ɳ��ⵥ���ֵ�����"
														 */);
		}
		return resultVOs;
	}

	// ���˷�ʽ����
	java.util.HashMap hssendtype = null;

	/**
	 * �˷�����asignOOSNum()���Ż��汾
	 * 
	 * �������ڣ�(2003-12-1 14:46:12)
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param itemVO
	 *            nc.vo.so.so008.OosinfoItemVO[]
	 * @param nnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param date
	 *            nc.vo.pub.lang.UFDate
	 * @param operid
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private UFDouble assignOOSNum2(nc.vo.so.so008.OosinfoItemVO[] itemVO,
			UFDouble nnum, UFDate date, String operid)
			throws java.lang.Exception {

		if (itemVO == null)
			return null;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();

		CrossDBConnection con = null;
		PreparedStatement stmt = null;

		String sql = "update so_oosinfo_b set  bfillflag = ?, nfillnumber = ?, dfilldate = ?,noriginalcurtaxnetprice=?,noriginalcursummny=?,cnote=? where coosinfo_bid = ?";

		try {
			con = (CrossDBConnection) getConnection();
			con.enableSQLTranslator(false);
			stmt = prepareStatement(con, sql);

			// ����
			for (int i = 0; i < itemVO.length; i++) {
				UFDouble oldnum = itemVO[i].getNnumber();
				UFDouble fillnumber = itemVO[i].getNfillnumber();
				if (fillnumber == null)
					fillnumber = new UFDouble(0);
				if (nnum.doubleValue() >= oldnum.sub(fillnumber).doubleValue()) {
					// ����
					itemVO[i].setNfillnumber(oldnum);
					itemVO[i].setBfillflag(new UFBoolean(true));
					itemVO[i].setDfilldate(date);

					nnum = nnum.sub(oldnum.sub(fillnumber));

					// ����Ϣ
					try {
						nc.impl.scm.so.so003.SendMsgImpl mess = new nc.impl.scm.so.so003.SendMsgImpl();
						// ����ID
						String id = itemVO[i].getCsaleid();
						// �ͻ�ID
						String custid = itemVO[i].getCcustomerid();
						// ���ID
						String invid = itemVO[i].getCinventoryid();
						// ������
						String sid = operid;
						// ������
						String rid = itemVO[i].getCoperatorid();
						mess.send(id, custid, invid, sid, rid);
					} catch (Exception e) {
						SCMEnv.out("��Ϣ����ʧ�ܣ�");
						SCMEnv.out(e.getMessage());
						throw e;
					}
				} else {
					itemVO[i].setNfillnumber(nnum.add(fillnumber));
					itemVO[i].setBfillflag(new UFBoolean(false));
					itemVO[i].setDfilldate(null);

					nnum = new UFDouble(0);
				}

				// nnum = nnum.sub(oldnum);

				dmo.updateOosinItem(itemVO[i], stmt);

				if (nnum.doubleValue() <= 0)
					break;

			} // end for

			executeBatch(stmt); // ������

		} catch (SQLException e) {
			throw e;

		}

		if (nnum.doubleValue() <= 0)
			return null;

		return nnum;

	}

	/**
	 * ��ѯȱ�����Ǽ��������Ƿ����� �������ڣ�(2003-1-22 9:11:43)
	 * 
	 * @param htNum
	 *            java.util.Hashtable
	 */
	public Hashtable getOosNumber(ArrayList sID) throws BusinessException {
		if (sID == null || sID.size() == 0) {
			return null;
		}

		Hashtable hasnumber = null;

		// ƴwhere�Ӿ�
		StringBuffer sbfWhereStr = new StringBuffer();
		sbfWhereStr
				.append(" where so_saleexecute.creceipttype='30' and so_oosinfo_b.bfillflag = 'N' and (1<0 ");
		for (int i = 0; i < sID.size(); i++) {
			sbfWhereStr.append(" or so_oosinfo_b.corder_bid = '");
			sbfWhereStr.append((String) sID.get(i));
			sbfWhereStr.append("' ");
		}
		sbfWhereStr.append(") ");
		// ƴsql
		StringBuffer sbfSql = new StringBuffer();
		sbfSql
				.append("SELECT so_oosinfo_b.corder_bid,isnull(so_oosinfo_b.nfillnumber,0)");
		sbfSql.append("FROM so_oosinfo_b ");
		sbfSql.append(sbfWhereStr.toString());
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSql.toString());
			ResultSet rs = stmt.executeQuery();

			//
			String sbid = "";
			UFDouble dbNum = null;

			while (rs.next()) {
				if (hasnumber == null)
					hasnumber = new Hashtable();

				sbid = rs.getString(1);

				BigDecimal obj = rs.getBigDecimal(2);
				dbNum = (obj == null ? new UFDouble(0) : new UFDouble(obj));

				hasnumber.put(sbid, dbNum);
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return hasnumber;
	}

	public String getOutSourceNumSQL(String corp, String ccalbodyid)
			throws java.sql.SQLException, nc.vo.pub.BusinessException,
			java.lang.Exception {
		// ƽ̨�ӿڣ�ȷ��ί�д����ĵ�������
		// ҵ������
		String SQLBussiness = null;
		String arrBussiness[] = null;
		IPFConfig bsPfConfig = (IPFConfig) NCLocator.getInstance().lookup(
				IPFConfig.class.getName());
		arrBussiness = bsPfConfig.getBusitypeByCorpAndStyle(corp,
				VerifyruleVO.WTDX);
		if (arrBussiness != null) {
			for (int i = 0; i < arrBussiness.length; i++) {
				if (SQLBussiness == null) {
					SQLBussiness = " so_sale.cbiztype = '"
							+ arrBussiness[i].toString() + "'";
				} else {
					SQLBussiness = SQLBussiness + " or so_sale.cbiztype = '"
							+ arrBussiness[i].toString() + "'";
				}
			}

			if (SQLBussiness != null) {
				SQLBussiness = " AND (" + SQLBussiness + " ) ";
			} else {
				return null;
			}

			SCMEnv.out("ί�д���ҵ������:" + SQLBussiness);

		} else {
			return null;
		}
		// ������
		// [0]String CorpID,��λID
		// [1]String WhClassID�ֿ�ID
		// [2]String InvID ���������ID
		// [3]UFBoolean IsExtractByFreeVO �Ƿ�������չ��
		// [4]String Free1������
		// [5]String Free2,
		// [6]String Free3,
		// [7]String Free4,
		// [8]String Free5,
		// [9]String Free6,
		// [10]String Free7,
		// [11]String Free8,
		// [12]String Free9,
		// [13]String Free10,
		// [14]String vbatchcode,���κ�
		// ����ֵ

		// SQL
		String SQLTrust = "";
		// ������
		String SQLSubWhere = "";
		// ��������
		String SQLGroupBy = "";
		// �������� �� �Ѿ���������
		SQLTrust = "SELECT so_sale.pk_corp,so_saleorder_b.cadvisecalbodyid as pk_calbody,so_saleorder_b.cinventoryid,so_saleexecute.vfree1,so_saleexecute.vfree2,so_saleexecute.vfree3,so_saleexecute.vfree4,so_saleexecute.vfree5,so_saleorder_b.cbatchid as vbatchcode,sum(ISNULL(so_saleexecute.ntotalinventorynumber, 0) - ISNULL(so_saleexecute.ntotalbalancenumber, 0)) AS outsourcenum ";
		SQLTrust += " FROM so_sale inner JOIN  so_saleorder_b ON so_sale.csaleid = so_saleorder_b.csaleid inner JOIN ";
		SQLTrust += "so_saleexecute ON so_sale.csaleid = so_saleexecute.csaleid AND so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";
		// ���۶������� ������δ���� �������Ѿ�����
		SQLTrust += " WHERE so_sale.creceipttype = '30' AND (so_saleexecute.bifpaybalance = 'N' or so_saleexecute.bifpaybalance is NULL) and 	so_sale.fstatus = "
				+ nc.ui.pub.bill.BillStatus.AUDIT;
		// ��˾����
		if (corp != null)
			SQLSubWhere += " and so_sale.pk_corp ='" + corp + "' ";
		// �����֯
		if (ccalbodyid != null)
			SQLSubWhere += "and so_saleorder_b.cadvisecalbodyid ='"
					+ ccalbodyid + "' ";

		SQLGroupBy = " group by so_sale.pk_corp,so_saleorder_b.cadvisecalbodyid,so_saleorder_b.cinventoryid,so_saleexecute.vfree1,so_saleexecute.vfree2,so_saleexecute.vfree3,so_saleexecute.vfree4,so_saleexecute.vfree5,so_saleorder_b.cbatchid ";

		// ��ѯ���
		SQLTrust = SQLTrust + SQLSubWhere + SQLBussiness + SQLGroupBy;

		return SQLTrust;
	}

	/**
	 * ���۶����۸�
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public Hashtable getSaleOrderCust(String[] strID)
			throws nc.vo.pub.BusinessException {

		// �������
		String where = " IN(";

		Hashtable hcustomer = new Hashtable();

		for (int i = 0; i < strID.length; i++) {
			if (i == 0)
				where = where + "'" + strID[i] + "'";
			else
				where = where + ",'" + strID[i] + "'";
		}

		where = where + ")";

		String strSQL = "select csaleid,ccustomerid FROM so_sale where csaleid "
				+ where;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {

				String id = rs.getString(1);

				String customerid = rs.getString(2);

				hcustomer.put(id, customerid);
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return hcustomer;
	}

	/**
	 * ���۶����۸�
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	public Hashtable getSaleOrderPrice(String[] strBodyID)
			throws nc.vo.pub.BusinessException {

		// �������
		String where = " IN(";

		Hashtable hprice = new Hashtable();

		for (int i = 0; i < strBodyID.length; i++) {
			if (i == 0)
				where = where + "'" + strBodyID[i] + "'";
			else
				where = where + ",'" + strBodyID[i] + "'";
		}

		where = where + ")";

		nc.vo.pub.lang.UFDouble nprice = new nc.vo.pub.lang.UFDouble(0);
		String strSQL = "select corder_bid,abs(nsummny/nnumber) FROM so_saleorder_b where corder_bid "
				+ where;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(strSQL);

			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {

				String id = rs.getString(1);

				BigDecimal n = rs.getBigDecimal(2);
				nprice = (n == null ? new UFDouble(0) : new UFDouble(n));

				hprice.put(id, nprice);
			}
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		return hprice;
	}

	/**
	 * ��ѯȱ�����Ǽ��������Ƿ�����
	 * 
	 * �������ڣ�(2003-1-22 9:11:43)
	 * 
	 * @param htNum
	 *            java.util.Hashtable
	 */
	public void isEnough(Hashtable htNum) throws BusinessException,
			SQLException {
		if (htNum == null) {
			return;
		}
		// ƴwhere�Ӿ�
		Object objTemp = null;
		StringBuffer sbfWhereStr = new StringBuffer();
		sbfWhereStr
				.append(" where so_saleexecute.creceipttype='30' and so_oosinfo_b.bfillflag = 'N' and (1<0 ");
		Enumeration enumer = htNum.keys();
		while (enumer.hasMoreElements()) {
			objTemp = enumer.nextElement();
			sbfWhereStr.append(" or so_oosinfo_b.corder_bid = '");
			sbfWhereStr.append(objTemp.toString());
			sbfWhereStr.append("' ");
		}
		sbfWhereStr.append(") ");
		// ƴsql
		StringBuffer sbfSql = new StringBuffer();
		sbfSql
				.append("SELECT so_oosinfo_b.corder_bid,isnull(so_oosinfo_b.nfillnumber,0)-isnull(so_saleexecute.ntotalinventorynumber,0), bd_invbasdoc.invcode ");
		sbfSql.append("FROM so_oosinfo_b LEFT OUTER JOIN ");
		sbfSql
				.append("bd_invbasdoc ON so_oosinfo_b.cinvbasdocid = bd_invbasdoc.pk_invbasdoc ");
		sbfSql.append("LEFT OUTER JOIN so_saleexecute ");
		sbfSql.append("on so_oosinfo_b.corder_bid=so_saleexecute.csale_bid ");
		sbfSql.append(sbfWhereStr.toString());
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = getConnection();
			stmt = con.prepareStatement(sbfSql.toString());
			ResultSet rs = stmt.executeQuery();

			//
			Object obj = null;
			String sbid = "";
			String sCode = "";
			UFDouble dbNum = null;
			UFDouble dbFill = null;
			UFDouble dbZero = new UFDouble(0);
			StringBuffer sbfExp = new StringBuffer();
			while (rs.next()) {
				sbid = rs.getString(1);
				if (sbid != null && sbid.length() > 0) {
					obj = rs.getObject(2);
					dbNum = (obj == null ? dbZero
							: new UFDouble(obj.toString()));
					sCode = rs.getString(3);
					sCode = (sCode == null ? "" : sCode);
					obj = htNum.get(sbid);
					if (obj != null) {
						dbFill = (new UFDouble(obj.toString())).sub(dbNum);
						if (dbFill.compareTo(dbZero) > 0) {
							sbfExp.append(NCLangResOnserver.getInstance()
									.getStrByID("sointerface",
											"UPPsointerface-000062", null,
											new String[] { sCode }));
							// sbfExp.append("���[");
							// sbfExp.append(sCode);
							// sbfExp.append("]ȱ��");
							// sbfExp.append(dbFill.toString());
							sbfExp.append("\n");
						}
					}
				}
			}
			if (sbfExp.toString().length() > 0) {
				throw new BusinessException(sbfExp.toString());
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

	}

	/**
	 * �����ṩ��д�ۼƳ�������������������/ǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void isOverSaleOrder(AggregatedValueObject outBillVO)
			throws nc.vo.pub.BusinessException {

		// ��˾ID
		String pk_corp = outBillVO.getParentVO().getAttributeValue("pk_corp")
				.toString();

		// ȡ������
		nc.bs.pub.para.SysInitDMO initReferDMO = new nc.bs.pub.para.SysInitDMO();
		// ������IC003 (�Ƿ�������������)
		String IC003 = "N";
		try{
			IC003 = initReferDMO.getParaString(pk_corp, "SO23");
		}catch(BusinessException e){
			//e.printStackTrace();
		}
		
		// IC004 (��������ķ�Χ���ٷֱ�ֵ)
		String IC004 = null;
		try{
			IC004 = initReferDMO.getParaString(pk_corp, "SO24");
		}catch(BusinessException e){
			//e.printStackTrace();
		}

		UFDouble nPercent = (IC004 == null ? new UFDouble("0") : new UFDouble(
				IC004));

		// ���ⵥID
		String id = outBillVO.getParentVO().getPrimaryKey();

		// ��������ID
		String corderbid = null;

		UFDouble noutnum = null;
		UFDouble nshouldoutnum = null;
		Hashtable houtnum = new Hashtable();

		// �ϲ㵥������
		String SourceBillTypeID = null;

		if (outBillVO.getChildrenVO() == null
				|| outBillVO.getChildrenVO().length == 0)
			return;

		String subSQL = null;

		CircularlyAccessibleValueObject bodyVO = outBillVO.getChildrenVO()[0];
		if (bodyVO.getAttributeValue("csourcetype") != null) {
			SourceBillTypeID = bodyVO.getAttributeValue("csourcetype")
					.toString();
			if (SourceBillTypeID.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
				subSQL = "select csourcebillbid from ic_general_b where cgeneralhid = '"
						+ id + "'";
			} else {
				subSQL = "select cfirstbillbid from ic_general_b where cgeneralhid = '"
						+ id + "'";
			}
		}

		// ���۳��ⵥ�ӱ�VO
		for (int i = 0; i < outBillVO.getChildrenVO().length; i++) {

			bodyVO = outBillVO.getChildrenVO()[i];

			// �����Դ�����۶���
			if (SourceBillTypeID.equals(nc.ui.scm.so.SaleBillType.SaleOrder)) {
				// ����ID
				corderbid = bodyVO.getAttributeValue("csourcebillbid")
						.toString();
			} else {
				// ����ID
				corderbid = bodyVO.getAttributeValue("cfirstbillbid")
						.toString();
			}
			// ����
			noutnum = (bodyVO.getAttributeValue("noutnum") == null ? new UFDouble(
					"0")
					: new UFDouble(bodyVO.getAttributeValue("noutnum")
							.toString()));
			nshouldoutnum = (bodyVO.getAttributeValue("nshouldoutnum") == null ? new UFDouble(
					0)
					: new UFDouble(bodyVO.getAttributeValue("nshouldoutnum")
							.toString()));

			noutnum = noutnum.add(nshouldoutnum);

			/** ��¼�к������쳣��ʾ* */
			Object[] objs = new Object[3];
			objs[0] = noutnum;
			objs[1] = bodyVO.getAttributeValue("crowno");
			objs[2] = pk_corp;
			houtnum.put(corderbid, objs);
		}

		// �ȽϷ�Χ
		//liuys ע�͸ô���,�ж������Ƿ񳬹���������
//		String error = null;
//		try {
//			error = isSaleOverOut2(IC003, nPercent, subSQL, houtnum);
//		} catch (Exception e) {
//			SCMEnv.out(e.getMessage());
//			throw new BusinessException(e.getMessage());
//		}
//
//		if (error != null) {
//			nc.vo.pub.BusinessException e = new nc.vo.pub.BusinessException(
//					error);
//			throw e;
//		}
	}

	/**
	 * ����Ƿ񳬹��������⡣
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @return boolean
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 */
	private String isSaleOverOut2(String IC003, UFDouble IC004, String subSQL,
			Hashtable hSaleOutNum) throws java.sql.SQLException,
			nc.vo.pub.BusinessException {
		String SQLRelation = "SELECT so_saleorder_b.corder_bid,so_saleorder_b.nnumber, isnull(so_saleexecute.ntotalinventorynumber,0) + isnull(so_saleexecute.ntotalshouldoutnum,0)-isnull(so_saleexecute.ntranslossnum,0) AS inventorynumber, so_saleexecute.bifinventoryfinish  ";
		SQLRelation = SQLRelation
				+ " FROM so_saleorder_b ,so_saleexecute  Where  so_saleorder_b.csaleid = so_saleexecute.csaleid AND  so_saleorder_b.corder_bid = so_saleexecute.csale_bid ";
		SQLRelation = SQLRelation + "  and csale_bid IN (" + subSQL + ")";
		UFDouble dblNumber = new UFDouble(.0);
		BigDecimal dblinventorynumber = new BigDecimal(0);

		// ���ݿ������
		Connection con = null;
		PreparedStatement stmt = null;
		UFDouble nResult = null;
		String sResult = null;
		String corder_bid = null;
		
//		UFBoolean bifinventoryfinish = null;

		IC004 = IC004.multiply(0.01).add(1);

		boolean isOverOut = false;

		UFDouble uf1 = new UFDouble(1);

		if ("Y".equals(IC003))
			isOverOut = true;

		try {
			con = getConnection();
			stmt = con.prepareStatement(SQLRelation);
			ResultSet rstNumber = stmt.executeQuery();
			// �ж��Ƿ�Ϊ�ռ�¼
			while (rstNumber.next()) {
				corder_bid = rstNumber.getString("corder_bid");
				Object oNnumber = rstNumber.getObject("nnumber");
				Object oInventorynumber = rstNumber
						.getObject("inventorynumber");

				// �ж��Ƿ�����ڴ���Ŀ��VO���ж�Ӧ��
				if (!hSaleOutNum.containsKey(corder_bid))
					continue;
				UFDouble SaleOutNum = (UFDouble)((Object[]) hSaleOutNum.get(corder_bid))[0];
				if (oNnumber != null && oInventorynumber != null) {
					dblNumber = new UFDouble(oNnumber.toString());
					
					//�����ݲ�
					dblNumber = isOverOut ? dblNumber.multiply(IC004) : dblNumber;
					
					dblinventorynumber = new BigDecimal(oInventorynumber
							.toString());

					SCMEnv.out("dblNumber:"
							+ dblNumber.doubleValue());
					SCMEnv.out("dblinventorynumber:"
							+ dblinventorynumber.doubleValue());
					SCMEnv.out("SaleOutNum:" + SaleOutNum.doubleValue());

					// ��������-�ۼƳ�������-��������
					if (dblNumber.floatValue() >= 0) {
						if (isOverOut) {
							nResult = new UFDouble(1.0).multiply(
									dblNumber.doubleValue()).sub(
									dblinventorynumber.doubleValue()).sub(
									SaleOutNum.doubleValue());
						} else
							nResult = uf1.multiply(dblNumber.doubleValue())
									.sub(dblinventorynumber.doubleValue()).sub(
											SaleOutNum.doubleValue());
						// bResult =
						// (nResult.doubleValue() >= 0 ? new UFBoolean(false) :
						// new UFBoolean(true));
						SCMEnv.out("nResult:"
								+ nResult.doubleValue());
						if (nResult.doubleValue() < 0) {
							sResult = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sointerface",
											"UPPsointerface-000044")/*
																	 * @res
																	 * "������������Χ����������"
																	 */;
							break;
						}
					} else {
						// ����Ϊ���������
						if (isOverOut) {
							nResult = new UFDouble(1.0).multiply(
									dblNumber.doubleValue()).sub(
									dblinventorynumber.doubleValue()).sub(
									SaleOutNum.doubleValue());

						} else
							nResult = uf1.multiply(dblNumber.doubleValue())
									.sub(dblinventorynumber.doubleValue()).sub(
											SaleOutNum.doubleValue());
						// bResult =
						// (nResult.doubleValue() <= 0 ? new UFBoolean(false) :
						// new UFBoolean(true));
						if (nResult.doubleValue() > 0) {
							sResult = nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("sointerface",
											"UPPsointerface-000044")/*
																	 * @res
																	 * "��������������ⷶΧ�����������"
																	 */;
							break;
						}
					}
				} else {
					sResult = nc.bs.ml.NCLangResOnserver.getInstance()
							.getStrByID("sointerface", "UPPsointerface-000044")/*
																				 * @res
																				 * "��������������ⷶΧ�����������"
																				 */;
					break;
				}
			}//end while
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}

		if (sResult != null) {
			sResult += "\n���ⵥ�кţ�" + ((Object[]) hSaleOutNum.get(corder_bid))[1];
			String pk_corp = ((Object[]) hSaleOutNum.get(corder_bid))[2]
					.toString();
			int power = Integer.parseInt(new SysInitBO().queryByParaCode(
					pk_corp, "BD501").m_value.toString());
			sResult += "\n����������"
					+ nResult.setScale(power, UFDouble.ROUND_HALF_UP).abs();
		}

		return sResult;
	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ��������, ���������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * �˷�������һͬ�������������Ż�����
	 * 
	 * �������ڣ�(2003-12-1 14:34:24) wj
	 * 
	 * @param outBillVO
	 *            nc.vo.ic.pub.bill.GeneralBillVO[]
	 * 
	 */

	public void setOOSNum(nc.vo.ic.pub.bill.GeneralBillVO[] outBillVOs)
			throws Exception {

		if (outBillVOs == null)
			return;

		for (int billnum = 0; billnum < outBillVOs.length; billnum++) {
			nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = outBillVOs[billnum]
					.getHeaderVO();
			nc.vo.ic.pub.bill.GeneralBillItemVO[] bodyVOs = outBillVOs[billnum]
					.getItemVOs();

			String pk_corp = headVO.getPk_corp();
			UFDate date = headVO.getDbilldate();
			String operid = headVO.getCregister();
			String calbody = headVO.getPk_calbody();

			for (int i = 0; i < bodyVOs.length; i++) {
				String cinvid = bodyVOs[i].getCinventoryid();
				UFDouble ninnum = bodyVOs[i].getNinnum();

				String csaleorderno = bodyVOs[i].getVproductbatch();
				if (csaleorderno == null)
					continue;

				String[] freeitem = new String[5];

				freeitem[0] = bodyVOs[i].getVfree1();
				freeitem[1] = bodyVOs[i].getVfree2();
				freeitem[2] = bodyVOs[i].getVfree3();
				freeitem[3] = bodyVOs[i].getVfree4();
				freeitem[4] = bodyVOs[i].getVfree5();

				setOOSNum2(pk_corp, calbody, cinvid, ninnum, date,
						csaleorderno, operid, freeitem); // �Ż���
			}

		} // end for billnum

	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * �˷�����setOOSNum()���Ż���,�������������õ���assignOOSNum2()
	 * 
	 * �������ڣ�(2003-12-1 14:42:15) wj
	 * 
	 * @param pk_corp
	 *            java.lang.String
	 * @param ccalbodyid
	 *            java.lang.String
	 * @param cinvid
	 *            java.lang.String
	 * @param ninnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param date
	 *            nc.vo.pub.lang.UFDate
	 * @param csaleorderno
	 *            java.lang.String
	 * @param operid
	 *            java.lang.String
	 * @param freeitem
	 *            java.lang.String[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void setOOSNum2(String pk_corp, String ccalbodyid, String cinvid,
			UFDouble ninnum, UFDate date, String csaleorderno, String operid,
			String[] freeitem) throws java.lang.Exception {

		if (ninnum == null)
			return;

		if (ninnum.doubleValue() <= 0)
			return;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();
		nc.vo.so.so008.OosinfoItemVO[] itemVO = null;

		String freewhere = "";
		if (freeitem != null) {
			for (int i = 0; i < freeitem.length; i++) {
				if (freeitem[i] == null)
					freewhere = freewhere + " and (so_oosinfo_b.vfree"
							+ (i + 1) + " is null or so_oosinfo_b.vfree"
							+ (i + 1) + " = '') ";
				else
					freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1)
							+ "='" + freeitem[i] + "' ";
			}
		}

		UFDouble nnum = ninnum;
		// ָ��������
		if (csaleorderno != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and so_oosinfo_b.bfillflag = 'N' and so_oosinfo.vreceiptcode = '"
					+ csaleorderno
					+ "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
			itemVO = dmo.queryAllByCondition(where);
			if (itemVO == null || itemVO.length == 0)
				// 2007-5-25 yangb������ڿ�����������ŵĶ�����,�˴���Ϊֱ�ӷ���
				return;
			// throw new
			// BusinessException(NCLangResOnserver.getInstance().getStrByID("sointerface","UPPsointerface-000045",
			// null,new String[]{csaleorderno}));
			// throw new BusinessException("û���ҵ����۵���Ϊ[" + csaleorderno
			// + "]�Ĳ�����¼!");
			nnum = assignOOSNum2(itemVO, nnum, date, operid);
		}

		if (nnum != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

			itemVO = dmo.queryAllByCondition(where);
			nnum = assignOOSNum2(itemVO, nnum, date, operid);
			if (nnum != null && nnum.doubleValue() > 0) {
				// ��ȡȱ����Ϣ
				where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
						+ pk_corp
						+ "' and  so_oosinfo_b.bfillflag = 'N' and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '"
						+ cinvid
						+ "'"
						+ " and so_oosinfo.ccalbodyid = '"
						+ ccalbodyid
						+ "' "
						+ freewhere
						+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
				itemVO = dmo.queryAllByCondition(where);
				nnum = assignOOSNum2(itemVO, nnum, date, operid);
			}

		}

	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ��������, ���������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * �˷�������һͬ�������������Ż�����
	 * 
	 * �������ڣ�(2003-12-1 14:34:24) wj
	 * 
	 * @param outBillVO
	 *            nc.vo.ic.pub.bill.GeneralBillVO[]
	 * 
	 */

	public void setOOSNumUnDo(nc.vo.ic.pub.bill.GeneralBillVO[] outBillVOs)
			throws Exception {

		if (outBillVOs == null)
			return;

		for (int billnum = 0; billnum < outBillVOs.length; billnum++) {
			nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = outBillVOs[billnum]
					.getHeaderVO();
			nc.vo.ic.pub.bill.GeneralBillItemVO[] bodyVOs = outBillVOs[billnum]
					.getItemVOs();

			String pk_corp = headVO.getPk_corp();
			UFDate date = headVO.getDbilldate();
			String operid = headVO.getCregister();
			String calbody = headVO.getPk_calbody();

			for (int i = 0; i < bodyVOs.length; i++) {
				String cinvid = bodyVOs[i].getCinventoryid();
				UFDouble ninnum = bodyVOs[i].getNinnum();

				String csaleorderno = bodyVOs[i].getVproductbatch();
				if (csaleorderno == null)
					continue;

				String[] freeitem = new String[5];

				freeitem[0] = bodyVOs[i].getVfree1();
				freeitem[1] = bodyVOs[i].getVfree2();
				freeitem[2] = bodyVOs[i].getVfree3();
				freeitem[3] = bodyVOs[i].getVfree4();
				freeitem[4] = bodyVOs[i].getVfree5();

				setOOSNumUnDo2(pk_corp, calbody, cinvid, ninnum, date,
						csaleorderno, operid, freeitem); // �Ż���
			}

		} // end for billnum

	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 */
	private void setOOSNumUnDo(String pk_corp, String ccalbodyid,
			String cinvid, UFDouble ninnum, UFDate date, String csaleorderno,
			String operid, String[] freeitem) throws Exception {

		if (ninnum == null)
			return;

		if (ninnum.doubleValue() <= 0)
			return;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();
		nc.vo.so.so008.OosinfoItemVO[] itemVO = null;

		String freewhere = "";
		if (freeitem != null) {
			for (int i = 0; i < freeitem.length; i++) {
				if (freeitem[i] == null)
					freewhere = freewhere + " and (so_oosinfo_b.vfree"
							+ (i + 1) + " is null or so_oosinfo_b.vfree"
							+ (i + 1) + " = '') ";
				else
					freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1)
							+ "='" + freeitem[i] + "' ";
			}
		}

		UFDouble nnum = ninnum;
		// ָ��������
		if (csaleorderno != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and so_oosinfo.vreceiptcode = '"
					+ csaleorderno
					+ "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
			itemVO = dmo.queryAllByCondition(where);
			if (itemVO == null || itemVO.length == 0)
				throw new BusinessException(NCLangResOnserver.getInstance()
						.getStrByID("sointerface", "UPPsointerface-000045",
								null, new String[] { csaleorderno }));
			// throw new BusinessException("û���ҵ����۵���Ϊ[" + csaleorderno
			// + "]�Ĳ�����¼!");
			nnum = assignOOSNum(itemVO, nnum, date, operid);
		}

		if (nnum != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

			itemVO = dmo.queryAllByCondition(where);
			nnum = assignOOSNum(itemVO, nnum, date, operid);
			if (nnum != null && nnum.doubleValue() > 0) {
				// ��ȡȱ����Ϣ
				where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
						+ pk_corp
						+ "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '"
						+ cinvid
						+ "'"
						+ " and so_oosinfo.ccalbodyid = '"
						+ ccalbodyid
						+ "' "
						+ freewhere
						+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
				itemVO = dmo.queryAllByCondition(where);
				nnum = assignOOSNum(itemVO, nnum, date, operid);
			}

		}

	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 */
	public void setOOSNumUnDo(nc.vo.ic.pub.bill.GeneralBillVO outBillVO)
			throws Exception {
		// String pk_corp,String cinvid,UFDouble ninnum,UFDate date,String
		// cbillhid,String operid
		nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = outBillVO.getHeaderVO();
		nc.vo.ic.pub.bill.GeneralBillItemVO[] bodyVOs = outBillVO.getItemVOs();

		String pk_corp = headVO.getPk_corp();
		UFDate date = headVO.getDbilldate();
		String operid = headVO.getCregister();
		String calbody = headVO.getPk_calbody();

		for (int i = 0; i < bodyVOs.length; i++) {
			String cinvid = bodyVOs[i].getCinventoryid();
			UFDouble ninnum = bodyVOs[i].getNinnum();

			String csaleorderno = bodyVOs[i].getVproductbatch();

			String[] freeitem = new String[5];

			freeitem[0] = bodyVOs[i].getVfree1();
			freeitem[1] = bodyVOs[i].getVfree2();
			freeitem[2] = bodyVOs[i].getVfree3();
			freeitem[3] = bodyVOs[i].getVfree4();
			freeitem[4] = bodyVOs[i].getVfree5();

			setOOSNumUnDo(pk_corp, calbody, cinvid, ninnum, date, csaleorderno,
					operid, freeitem);
		}
	}

	/**
	 * �����ṩ��д�ۼƲ�ȱ�������������������Ʒ��ⵥǩ��ʱ���ø÷�����
	 * 
	 * �˷�����setOOSNum()���Ż���,�������������õ���assignOOSNum2()
	 * 
	 * �������ڣ�(2003-12-1 14:42:15) wj
	 * 
	 * @param pk_corp
	 *            java.lang.String
	 * @param ccalbodyid
	 *            java.lang.String
	 * @param cinvid
	 *            java.lang.String
	 * @param ninnum
	 *            nc.vo.pub.lang.UFDouble
	 * @param date
	 *            nc.vo.pub.lang.UFDate
	 * @param csaleorderno
	 *            java.lang.String
	 * @param operid
	 *            java.lang.String
	 * @param freeitem
	 *            java.lang.String[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void setOOSNumUnDo2(String pk_corp, String ccalbodyid,
			String cinvid, UFDouble ninnum, UFDate date, String csaleorderno,
			String operid, String[] freeitem) throws java.lang.Exception {

		if (ninnum == null)
			return;

		if (ninnum.doubleValue() <= 0)
			return;

		nc.impl.scm.so.so008.OosinfoDMO dmo = new nc.impl.scm.so.so008.OosinfoDMO();
		nc.vo.so.so008.OosinfoItemVO[] itemVO = null;

		String freewhere = "";
		if (freeitem != null) {
			for (int i = 0; i < freeitem.length; i++) {
				if (freeitem[i] == null)
					freewhere = freewhere + " and (so_oosinfo_b.vfree"
							+ (i + 1) + " is null or so_oosinfo_b.vfree"
							+ (i + 1) + " = '') ";
				else
					freewhere = freewhere + " and so_oosinfo_b.vfree" + (i + 1)
							+ "='" + freeitem[i] + "' ";
			}
		}

		UFDouble nnum = ninnum;
		// ָ��������
		if (csaleorderno != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and so_oosinfo.vreceiptcode = '"
					+ csaleorderno
					+ "' and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
			itemVO = dmo.queryAllByCondition(where);
			if (itemVO == null || itemVO.length == 0)
				throw new BusinessException(NCLangResOnserver.getInstance()
						.getStrByID("sointerface", "UPPsointerface-000045",
								null, new String[] { csaleorderno }));
			// throw new BusinessException("û���ҵ����۵���Ϊ[" + csaleorderno
			// + "]�Ĳ�����¼!");
			nnum = assignOOSNum2(itemVO, nnum, date, operid);
		}

		if (nnum != null) {
			// ��ȡ������Ϣ
			String where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
					+ pk_corp
					+ "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.bsupplyflag = 'Y' and so_oosinfo_b.cinventoryid = '"
					+ cinvid
					+ "'"
					+ " and so_oosinfo.ccalbodyid = '"
					+ ccalbodyid
					+ "' "
					+ freewhere
					+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";

			itemVO = dmo.queryAllByCondition(where);
			nnum = assignOOSNum2(itemVO, nnum, date, operid);
			if (nnum != null && nnum.doubleValue() > 0) {
				// ��ȡȱ����Ϣ
				where = " where so_oosinfo.dr=0 and so_oosinfo_b.dr=0 and  so_oosinfo.pk_corp = '"
						+ pk_corp
						+ "' and  so_oosinfo_b.nfillnumber > 0 and so_oosinfo_b.boosflag = 'Y' and so_oosinfo_b.cinventoryid = '"
						+ cinvid
						+ "'"
						+ " and so_oosinfo.ccalbodyid = '"
						+ ccalbodyid
						+ "' "
						+ freewhere
						+ " order by so_oosinfo.dmakedate,so_oosinfo_b.ts";
				itemVO = dmo.queryAllByCondition(where);
				nnum = assignOOSNum2(itemVO, nnum, date, operid);
			}

		}

	}

	/**
	 * �����ṩ�˻������д�˻�����������
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setReturnNum(CircularlyAccessibleValueObject[] toretvos)
			throws nc.vo.pub.BusinessException {

		if (toretvos == null || toretvos.length <= 0)
			return;
		try {
			nc.impl.scm.so.so016.SOToolsDMO.updateNoAllBatch(toretvos,
					new String[] { "ntotalreturnnumber" }, "so_saleexecute",
					new String[] { "csale_bid" });
			String[] sIds = new String[toretvos.length];
			for (int i = 0; i < toretvos.length; i++) {
				sIds[i] = (String) toretvos[i].getAttributeValue("csale_bid");
			}
			SaleOrderDMO.checkIsExecuteNumRigth(sIds);
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

	}

	/**
	 * �����ṩ��дǩ����������������ǩ�ջ�дʱ���ø÷�����
	 * 
	 * @exception �쳣����
	 * 
	 * @see ��Ҫ�μ�����������
	 * 
	 * @since �������һ���汾���˷�������ӽ���������ѡ��
	 * 
	 * @deprecated�÷����������һ���汾���Ѿ������������滻������ѡ��
	 * 
	 * 
	 * @param salebillType
	 *            java.lang.String
	 * @param SaleID
	 *            java.lang.String
	 * @param SaleDetailID
	 *            java.lang.String
	 * @param OutNum
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setSignNum(String pk_corp, String cbillhid, String cbillbid,
			UFDouble noutnum) throws nc.vo.pub.BusinessException {

		String SQLUpdate = "update so_square_b set nsignnum = ? ";

		SQLUpdate = SQLUpdate + " where corder_bid = ? and csaleid = ?";

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = getConnection();
			stmt = con.prepareStatement(SQLUpdate);
			if (noutnum == null)
				stmt.setNull(1, Types.INTEGER);
			else
				stmt.setBigDecimal(1, noutnum.toBigDecimal());

			int n = 2;

			// corder_bid
			stmt.setString(n, cbillbid);
			// csaleid
			stmt.setString(n + 1, cbillhid);
			stmt.executeUpdate();
			// �ر�����
			stmt.close();
			con.close();

		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ��д�����ۼ�;�������� 
	 * String[] ids ���۶���������ID 
	 * UFDouble[] divnums ;�������� 
	 * UFDouble[] signnums ǩ��������--�������� by zhangcheng ����v5.02��������ǩ��;��ǩ�ն��� 2008-03-05
	 */
	public void setTransLossNum(String[] ids, UFDouble[] divnums,UFDouble[] signnums)
			throws BusinessException {
		if (ids == null || divnums == null  
				|| signnums == null
				|| ids.length == 0
				|| ids.length != divnums.length 
				|| ids.length != signnums.length
				)
			return;
		try {
			String sSql = "update so_saleexecute set ntranslossnum=coalesce(ntranslossnum,0) + ? "
					+ ", ntotalsignnumber=coalesce(ntotalsignnumber,0) + ? "
					+ "where csale_bid = ? and creceipttype = '30' ";
			SmartDMO sdmo = new SmartDMO();

			ArrayList alValue = new ArrayList();
			ArrayList alitem = null;

			for (int i = 0; i < ids.length; i++) {
				alitem = new ArrayList();
				alitem.add(divnums[i]);
				alitem.add(signnums[i]);
				alitem.add(ids[i]);
				alValue.add(alitem);
			}
			ArrayList alType = new ArrayList();

			alType.add(new Integer(SmartFieldMeta.JAVATYPE_UFDOUBLE));
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_UFDOUBLE));
			alType.add(new Integer(SmartFieldMeta.JAVATYPE_STRING));

			sdmo.executeUpdateBatch(sSql, alValue, alType);
			SaleOrderDMO ddmo = new SaleOrderDMO();
			SaleorderBVO[] bvos = (SaleorderBVO[]) ddmo
					.queryAllBodyDataByBIDs(ids);
			ddmo.processOutState(bvos);
		} catch (Exception e) {
			handleException(e);
		}

	}
	
	protected void handleException(Exception e) throws BusinessException {
		//e.printStackTrace();
		SCMEnv.out(e);

		if (e instanceof BusinessException)
			throw (BusinessException) e;
		else
			throw new BusinessException(e);
	}
	
}
