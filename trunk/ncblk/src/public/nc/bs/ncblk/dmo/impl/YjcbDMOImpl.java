package nc.bs.ncblk.dmo.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.ncblk.constants.Constants;
import nc.bs.ncblk.dmo.IyjcbDMO;
import nc.bs.ncblk.exception.MyBsBeanException;
import nc.bs.ncblk.exception.MyDMOException;
import nc.bs.pub.DataManageObject;
import nc.ui.bd.b21.CurrParamQuery;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.lang.UFDouble;

public class YjcbDMOImpl extends DataManageObject implements IyjcbDMO {

	public YjcbDMOImpl() throws NamingException {
		super();
	}

	// ɾ���½�ɱ������ӱ�����
	public boolean deleteNcReportYjcbBVO(String whereStr) throws MyDMOException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sql = " delete nc_report_yjcb_b where " + whereStr;
			stmt.execute(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣��" + e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	/**
	   * ���ߣ�liuys 
	   * ���ܣ������۱������Լ�С��λ 
	   * ������String pk_corp 
	   * ��˾ID int iflag 
	   * ���أ��� ���⣺��
	   * ���ڣ�(2010-12-30 11:39:21) �޸����ڣ��޸��ˣ��޸�ԭ��
	   */
	  private List<Number> getRowDigits_ExchangeRate(NcReportYjcbVO pvo,String sPk_corp) throws MyBsBeanException{
		  	// ȡ�ñ���
		String sCurrId = pvo.getCcurrencytypeid();
//		String sPk_corp = pvo.getPk_corp();
//		Integer iDigit = new Integer(2);
		List hlinfo = new ArrayList();
		try {
			BusinessCurrencyRateUtil curTool = new BusinessCurrencyRateUtil(
					sPk_corp);
//			CurrinfoVO currinfoVO = curTool.getCurrinfoVO(sCurrId,
//					CurrParamQuery.getInstance().getLocalCurrPK(sPk_corp));
			UFDouble hl = curTool.getAdjustRate(CurrParamQuery.getInstance()
					.getLocalCurrPK(sPk_corp), sCurrId, null, pvo.getDjnd(),
					pvo.getDjqj());
			// �������Ϊ0,��ôֱ��ȡ1
			if (hl == null || hl.isTrimZero() || hl.doubleValue() == 1)
				hlinfo.add(1);
			else
				hlinfo.add(hl);
			//ȡ���ʾ���
//			if (currinfoVO.getRatedigit() != null) {
//				iDigit = currinfoVO.getRatedigit();
//				hlinfo.add(iDigit);
//			}else
//				hlinfo.add(6);
		} catch (Exception e) {
			throw new MyBsBeanException(e.getMessage()+"ȡ�����쳣��");
		}
		return hlinfo;
	}
	  
	  
	/**
	 * liuys add 
	 * 
	 * for ���忨��Ŀ,�������ԪΪ��λ�ҵĹ�˾,��ô���л���ת��
	 * 
	 * @param pvo
	 * @param pk_corp
	 * @param rsdouble
	 * @return
	 */
	private Double HJChange(NcReportYjcbVO pvo , String pk_corp,double rsdouble){
		
//		if(pk_corp.equals("")){
			List list = null;
			try {
				list = getRowDigits_ExchangeRate(pvo,pk_corp);
			} catch (MyBsBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double hlzhje = Double.parseDouble(list.get(0).toString());
			return rsdouble*hlzhje;// ����ת�����,��������
//		}
//		else
//			return rsdouble;// ��������
	}
	
	// ��ȡ���й�˾����ǰ��ݡ���ǰ�·��£�ָ������������˹��ĵĺϼ����ݡ�
	public List queryIaBillVoList(NcReportYjcbVO pvo, String billType)
			throws MyDMOException {
		Connection conn = null;
		Statement stmt = null;
		List reList = new ArrayList();
		String sql = "select t1.cbilltypecode,"
				+ " t1.caccountyear,"
				+ " t1.caccountmonth,"
				+ " sum(t1.ninmny) as ninmny,"
				+ " sum(t1.ninnum) as ninnum,"
				+ " sum(t1.noutmny) as noutmny,"
				+ " sum(t1.noutnum) as noutnum,"
				//liuys add 
				+ " t2.pk_corp, "
				+ " t3.pk_invbasdoc,"
				+ " t3.invcode,"
				+ " t3.invname,"
				+ " t3.invspec,"
				+ " t5.invclasscode,"
				+ " t5.invclassname"
				+ " from ia_monthinout t1"
				+ " left join bd_invmandoc t2 on t1.CINVENTORYID = t2.pk_invmandoc"
				+ " left join bd_invbasdoc t3 on t2.pk_invbasdoc = t3.pk_invbasdoc"
				+ " left join bd_invcl t4 on t3.pk_invcl = t4.pk_invcl"
				+ " left join bd_invcl t5 on t5.invclasscode = substr(t4.invclasscode, 1, 3)"
				+ " left join bd_rdcl t6 on t6.pk_rdcl = t1.cdispatchid"
				+ " where t1.dr = 0 and t1.caccountyear='" + pvo.getDjnd()
				+ "' and t1.caccountmonth='" + pvo.getDjqj() + "'";
		String group_by = " group by t1.cbilltypecode,t1.caccountyear,t1.caccountmonth,"
				+ "t2.pk_corp,t3.pk_invbasdoc,t3.invcode,t3.invname,t3.invspec,t5.invclasscode,t5.invclassname";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			// �ɱ�������
			if (billType.equals("I9IA")) {
				sql = "select "
						+ " t1.caccountyear,"
						+ " t1.caccountmonth,"
						+ " sum(t1.ninmny) as ninmny,"
						+ " sum(t1.ninnum) as ninnum,"
						+ " sum(t1.noutmny) as noutmny,"
						+ " sum(t1.noutnum) as noutnum,"
						//liuys add 
						+ " t2.pk_corp, "
						+ " t3.pk_invbasdoc,"
						+ " t3.invcode,"
						+ " t3.invname,"
						+ " t3.invspec,"
						+ " t5.invclasscode,"
						+ " t5.invclassname"
						+ " from ia_monthinout t1"
						+ " left join bd_invmandoc t2 on t1.CINVENTORYID = t2.pk_invmandoc"
						+ " left join bd_invbasdoc t3 on t2.pk_invbasdoc = t3.pk_invbasdoc"
						+ " left join bd_invcl t4 on t3.pk_invcl = t4.pk_invcl"
						+ " left join bd_invcl t5 on t5.invclasscode = substr(t4.invclasscode, 1, 3)"
						+ " left join bd_rdcl t6 on t6.pk_rdcl = t1.cdispatchid"
						+ " where t1.dr = 0 and t1.caccountyear='"
						+ pvo.getDjnd() + "' and t1.caccountmonth='"
						+ pvo.getDjqj() + "'";
				group_by = " group by t1.caccountyear,t1.caccountmonth,"
						+ "t2.pk_corp ,t3.pk_invbasdoc,t3.invcode,t3.invname,t3.invspec,t5.invclasscode,t5.invclassname";

				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode in ('I9','IA')";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					double rsdouble = rs_1.getDouble("ninmny")
					- rs_1.getDouble("noutmny");
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rsdouble);
					bvo.setCbtzdje(new UFDouble(rsje, Constants.XS_JINE));// ��������
					reList.add(bvo);
				}
			}
			// �ɹ����
			if (billType.equals("I2")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='I2'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("ninmny"));
					bvo.setCgrkje(new UFDouble(rsje,
							Constants.XS_JINE));// �ɹ�����
					
					bvo.setCgrksl(new UFDouble(rs_1.getDouble("ninnum"),
							Constants.XS_SHUNLIANG));// �ɹ��������
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}
			// �������
			if (billType.equals("I3")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='I3'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("ninmny"));
					bvo.setScrkje(new UFDouble(rsje,
							Constants.XS_JINE));// ��������
					
					bvo.setScrksl(new UFDouble(rs_1.getDouble("ninnum"),
							Constants.XS_SHUNLIANG));// �����������
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}
			// ί�����
			if (billType.equals("ID")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='ID'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("ninmny"));
					bvo.setWwrkje(new UFDouble(rsje,
							Constants.XS_JINE));// ί������
					
					bvo.setWwrksl(new UFDouble(rs_1.getDouble("ninnum"),
							Constants.XS_SHUNLIANG));// ί���������
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}

			// �����������������
			if (billType.equals("xssr")) {
				String sql_1 = "select t4.pk_corp,t5.invclasscode,"
						+ " t5.invclassname,"
						+ " t3.pk_invbasdoc,"
						+ " t3.invcode,"
						+ " t3.invname,"
						+ " t3.invspec,"
						+ " sum(t2.nnumber) as nnumber,"
						+ " sum(t2.nsummny) as nsummny"
						+ " from so_saleinvoice t1"
						+ " left join so_saleinvoice_b t2 on t1.csaleid = t2.csaleid"
						+ " left join bd_invbasdoc t3 on t2.cinvbasdocid = t3.pk_invbasdoc"
						+ " left join bd_invcl t4 on t3.pk_invcl = t4.pk_invcl"
						+ " left join bd_invcl t5 on t5.invclasscode = substr(t4.invclasscode, 1, 3)"
						+ " where t2.blargessflag = 'N'"
						+ " and t1.dr = 0"
						+ " and substr(t1.dapprovedate,1,4)='"
						+ pvo.getDjnd()
						+ "'"
						+ " and substr(t1.dapprovedate, 6, 2)='"
						+ pvo.getDjqj()
						+ "' group by t4.pk_corp,t5.invclasscode,t5.invclassname,t3.pk_invbasdoc,t3.invcode,t3.invname,t3.invspec";
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("nsummny"));
					bvo.setXssrje(new UFDouble(rsje,
							Constants.XS_JINE));// ���
					
					bvo.setXssrsl(new UFDouble(rs_1.getDouble("nnumber"),
							Constants.XS_SHUNLIANG));// ����
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}
			// ���������
			if (billType.equals("I4")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql
						+ " and t1.cbilltypecode='I4' and t6.rdname != 'ת�����' ";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				Map dMap_1 = new HashMap();
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("ninmny"));
					bvo.setQtrkje(new UFDouble(rsje,
							Constants.XS_JINE));// ��������
					
					bvo.setQtrksl(new UFDouble(rs_1.getDouble("ninnum"),
							Constants.XS_SHUNLIANG));// �����������
					bvo.setDr(new Integer(0));
					dMap_1.put(bvo.getChbm().trim(), bvo);
				}
				String sql2 = "select t1.pk_corp,t2.cinvbasid, t4.invcode,t4.pk_invcl, sum(t2.nnumber) as nnumber "
						+ "from ia_bill t1 left join ia_bill_b t2 on t1.cbillid = t2.cbillid "
						+ "left join to_settlelist_bb t3 on t2.csourcebillitemid = t3.csettlelist_bbid "
						+ "left join bd_invbasdoc t4 on t2.cinvbasid = t4.pk_invbasdoc "
						+ "left join bd_stordoc t5 on t3.cupwarehouseid=t5.pk_stordoc "
						+ "where t2.dr = 0 and t2.cbilltypecode = 'II' "
						+ "and t5.iscalculatedinvcost = 'N' "
						+ "and substr(t1.dbilldate, 1, 4) = '"
						+ pvo.getDjnd()
						+ "' "
						+ "and substr(t1.dbilldate, 6, 2) = '"
						+ pvo.getDjqj()
						+ "' "
						+ "group by t1.pk_corp,t2.cinvbasid, t4.invcode,t4.pk_invcl";
				ResultSet rs_2 = stmt.executeQuery(sql2);
				while (rs_2.next()) {
					// String t = rs_2.getString("invcode");
					String t = rs_2.getString("cinvbasid");
					NcReportYjcbBVO bvo = (NcReportYjcbBVO) dMap_1.get(t);
					if (bvo != null) {
						bvo.setQtrksl(new UFDouble(bvo.getQtrksl()
								.doubleValue()
								+ rs_2.getDouble("nnumber"),
								Constants.XS_SHUNLIANG));
						bvo.setQtrkje(new UFDouble(bvo.getQtrkje()
								.doubleValue(), Constants.XS_JINE));
						dMap_1.remove(t);
						dMap_1.put(t, bvo);
					} else {
						bvo = new NcReportYjcbBVO();
						bvo.setPk_yjcb(pvo.getPk_yjcb());
						bvo.setChfl(rs_2.getString("pk_invcl"));
						bvo.setChbm(t);
						bvo.setQtrksl(new UFDouble(rs_2.getDouble("nnumber"),
								Constants.XS_SHUNLIANG));
						bvo.setQtrkje(new UFDouble(0));
						// bvo.setQtrkje(new
						// UFDouble(rs.getDouble("nmoney"),Constants.XS_JINE));
						dMap_1.put(t, bvo);
					}
				}
				Set keys = dMap_1.keySet();
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					reList.add(dMap_1.get(it.next()));
				}
			}
			// ���ϳ���I6
			if (billType.equals("I6")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='I6'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("noutmny"));
					bvo.setClckje(new UFDouble(rsje,
							Constants.XS_JINE));// ���ϳ����
					
					bvo.setClcksl(new UFDouble(rs_1.getDouble("noutnum"),
							Constants.XS_SHUNLIANG));// ���ϳ�����
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}
			// ί����� IC
			if (billType.equals("IC")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='IC'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("noutmny"));
					bvo.setWwckje(new UFDouble(rsje,
							Constants.XS_JINE));// ί�������
					
					bvo.setWwcksl(new UFDouble(rs_1.getDouble("noutnum"),
							Constants.XS_SHUNLIANG));// ί���������
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}

			// ���۳�����I5
			if (billType.equals("I5")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql + " and t1.cbilltypecode='I5'";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("noutmny"));
					bvo.setXsckje(new UFDouble(rsje,
							Constants.XS_JINE));// ���۳�����
					
					bvo.setXscksl(new UFDouble(rs_1.getDouble("noutnum"),
							Constants.XS_SHUNLIANG));// ���۳�������
					bvo.setDr(new Integer(0));
					reList.add(bvo);
				}
			}

			// ��������I7
			if (billType.equals("I7")) {
				// �������Ͳ�ѯ����
				String sql_1 = sql
						+ " and t1.cbilltypecode='I7' and t6.rdname != 'ת�����' ";
				// ����
				sql_1 = sql_1 + group_by;
				ResultSet rs_1 = stmt.executeQuery(sql_1);
				Map dMap_1 = new HashMap();
				while (rs_1.next()) {
					NcReportYjcbBVO bvo = new NcReportYjcbBVO();
					// bvo.setChbm(rs_1.getString("invcode"));// �������
					bvo.setChbm(rs_1.getString("pk_invbasdoc"));// �������
					bvo.setDef14(rs_1.getString("invname"));// �������
					bvo.setDef12(rs_1.getString("invcode"));// �������(��)
					bvo.setDef13(rs_1.getString("invclasscode"));// ����������
					bvo.setChfl(rs_1.getString("invclassname"));// �����������
					bvo.setDef15(rs_1.getString("invspec"));// ���
					//liuys add 
					String pk_corp = rs_1.getString("pk_corp");
					double rsje = HJChange(pvo,pk_corp,rs_1.getDouble("noutmny"));
					bvo.setQtckje(new UFDouble(rsje,
							Constants.XS_JINE));// ���������
					
					bvo.setQtcksl(new UFDouble(rs_1.getDouble("noutnum"),
							Constants.XS_SHUNLIANG));// ������������
					bvo.setDr(new Integer(0));
					dMap_1.put(bvo.getChbm().trim(), bvo);
				}

				String sql2 = " select a5.pk_invcl, a5.invclasscode, a5.invclassname,"
						+ " a3.pk_invbasdoc, a3.invcode, a3.invname, a3.invspec, a1.cbilltypecode, "
						+ "sum(a2.nnumber) as nnumber, sum(a2.nmoney) as nmoney from ia_bill a1 "
						+ "left join ia_bill_b a2 on a1.cbillid = a2.cbillid "
						+ "left join bd_invbasdoc a3 on a3.pk_invbasdoc = a2.cinvbasid "
						+ "left join bd_invcl a4 on a3.pk_invcl = a4.pk_invcl "
						+ "left join bd_invcl a5 on a5.invclasscode=substr(a4.invclasscode,1,3)"
						+ " left join bd_rdcl a6 on a6.pk_rdcl=a1.cdispatchid  "
						+ "where a1.dr = 0  and a1.bauditedflag = 'Y'  "
						+ "and substr(a1.dbilldate, 1, 4) = '"
						+ pvo.getDjnd()
						+ "' "
						+ "and substr(a1.dbilldate, 6, 2) = '"
						+ pvo.getDjqj()
						+ "' "
						+ "and  a1.cbilltypecode = 'I5' and  a2.blargessflag = 'Y' "
						+ "group by a5.pk_invcl, a5.invclasscode, a5.invclassname, "
						+ "a3.pk_invbasdoc, a3.invcode, a3.invname, a3.invspec, a1.cbilltypecode "
						+ "order by a5.invclasscode";
				ResultSet rs_2 = stmt.executeQuery(sql2);
				while (rs_2.next()) {
					// String t = rs_2.getString("invcode");
					String t = rs_2.getString("pk_invbasdoc");

					NcReportYjcbBVO bvo = (NcReportYjcbBVO) dMap_1.get(t);
					if (bvo != null) {
						bvo.setQtcksl(new UFDouble(bvo.getQtcksl()
								.doubleValue()
								+ rs_2.getDouble("nnumber"),
								Constants.XS_SHUNLIANG));
						dMap_1.remove(t);
						dMap_1.put(t, bvo);
					} else {
						bvo = new NcReportYjcbBVO();
						bvo.setPk_yjcb(pvo.getPk_yjcb());
						bvo.setChfl(rs_2.getString("pk_invcl"));
						bvo.setChbm(t);
						bvo.setQtcksl(new UFDouble(rs_2.getDouble("nnumber"),
								Constants.XS_SHUNLIANG));
						bvo.setQtckje(new UFDouble(0));
						bvo.setDr(new Integer(0));
						bvo.setDef12(rs_2.getString("invcode"));// �������(��)
						bvo.setDef13(rs_2.getString("invclasscode")); // ����������
						bvo.setDef14(rs_2.getString("invname")); // �������
						bvo.setDef15(rs_2.getString("invspec"));// ���
						// bvo.setQtrkje(new
						// UFDouble(rs.getDouble("nmoney"),Constants.XS_JINE));
						dMap_1.put(t, bvo);
					}
				}
				Set keys = dMap_1.keySet();
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					reList.add(dMap_1.get(it.next()));
				}
			}
			
			//liuys add for ���忨��Ŀ 
			Map map = new HashMap();
			for(int i=0;i<reList.size();i++){
				NcReportYjcbBVO bvo = (NcReportYjcbBVO)reList.get(i);
				if(map.containsKey(bvo.getChbm())){
					NcReportYjcbBVO newbvo = (NcReportYjcbBVO)map.get(bvo.getChbm());
					newbvo.setCbtzdje(newbvo.getCbtzdje().add(bvo.getCbtzdje()));
					newbvo.setCgrkje(newbvo.getCgrkje().add(bvo.getCgrkje()));
					newbvo.setCgrksl(newbvo.getCgrksl().add(bvo.getCgrksl()));
					newbvo.setScrkje(newbvo.getScrkje().add(bvo.getScrkje()));
					newbvo.setScrksl(newbvo.getScrksl().add(bvo.getScrksl()));
					newbvo.setWwrkje(newbvo.getWwrkje().add(bvo.getWwrkje()));
					newbvo.setWwrksl(newbvo.getWwrksl().add(bvo.getWwrksl()));
					newbvo.setXssrje(newbvo.getXssrje().add(bvo.getXssrje()));
					newbvo.setXssrsl(newbvo.getXssrsl().add(bvo.getXssrsl()));
					newbvo.setQtrkje(newbvo.getQtrkje().add(bvo.getQtrkje()));
					newbvo.setQtrksl(newbvo.getQtrksl().add(bvo.getQtrksl()));
					newbvo.setClckje(newbvo.getClckje().add(bvo.getClckje()));
					newbvo.setClcksl(newbvo.getClcksl().add(bvo.getClcksl()));
					newbvo.setWwckje(newbvo.getWwckje().add(bvo.getWwckje()));
					newbvo.setWwcksl(newbvo.getWwcksl().add(bvo.getWwcksl()));
					newbvo.setXsckje(newbvo.getXsckje().add(bvo.getXsckje()));
					newbvo.setXscksl(newbvo.getXscksl().add(bvo.getXscksl()));
					newbvo.setQtckje(newbvo.getQtckje().add(bvo.getQtckje()));
					newbvo.setQtcksl(newbvo.getQtcksl().add(bvo.getQtcksl()));
				}else
					map.put(bvo.getChbm(), bvo);
			}
			List l_st  = new ArrayList();
			if(map.size()>0){
				Object[] o =  map.values().toArray();
				for(int i = 0 ;i<o.length;i++){
					NcReportYjcbBVO bbbvo = (NcReportYjcbBVO)o[i];
					bbbvo.setPk_yjcb(pvo.getPk_yjcb());
					l_st.add(bbbvo);
				}
			}
			return l_st;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣��" + e.getMessage());
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	// �����ɱ������ӱ�
	public boolean saveAllNcReportYjcbBVOList(List yjcbVoList)
			throws MyDMOException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVOList(yjcbVoList);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣:" + e.getMessage());
		}

	}

	// ����������Ϣ
	public boolean updateNcReportYjcbVO(NcReportYjcbVO pvo)
			throws MyDMOException {
		BaseDAO dao = new BaseDAO();
		try {
			dao.updateVO(pvo);
			return true;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣:" + e.getMessage());
		}

	}

	// ���ݱ�ͷ��ѯ��������ȡһ���ۺ�VO
	public MyBillVO queryMyBillVO(String djnd, String djqj)
			throws MyDMOException {
		BaseDAO dao = new BaseDAO();
		String whereStr = " djnd='" + djnd + "' and djqj='" + djqj
				+ "'  and (dr=0 or dr is null) ";
		try {
			Collection coll = dao.retrieveByClause(NcReportYjcbVO.class,
					whereStr);
			if (coll == null || coll.size() == 0)
				throw new MyDMOException("��ǰ���:" + djnd + ",�ڼ�:" + djqj
						+ "û���½�ɱ��������ݣ�");
			NcReportYjcbVO[] yjcbVoArrays = new NcReportYjcbVO[coll.size()];
			coll.toArray(yjcbVoArrays);
			// ��ͷVO
			NcReportYjcbVO pvo = yjcbVoArrays[0];
			// ����VOs
			Collection bcolls = dao.retrieveByClause(NcReportYjcbBVO.class,
					"pk_yjcb='" + pvo.getPk_yjcb() + "' and (dr=0 or dr is null) ");
			if (bcolls == null || bcolls.size() == 0)
				throw new MyDMOException("��ǰ���:" + djnd + ",�ڼ�:" + djqj
						+ "û���½�ɱ��������ݣ���������Ϊ�գ�");
			NcReportYjcbBVO[] bvos = new NcReportYjcbBVO[bcolls.size()];
			bcolls.toArray(bvos);
			MyBillVO billVo = new MyBillVO();
			billVo.setParentVO(pvo);
			billVo.setChildrenVO(bvos);
			return billVo;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		}

	}

	// added by zjj 2010-05-19
	// ȡ�õ�ǰ����
	public List<NcReportYjcbBVO> getXsList(NcReportYjcbVO nj)
			throws MyDMOException {
		Connection conn = null;
		Statement stmt = null;
		List<NcReportYjcbBVO> reList = new ArrayList();
		String sql = "select * from nc_report_yjcb_b where pk_yjcb ='"
				+ nj.getPrimaryKey() + "'";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				NcReportYjcbBVO nb = new NcReportYjcbBVO();
				nb.setPk_yjcb_b(rs.getString("pk_yjcb_b"));
				nb.setChfl(rs.getString("chfl"));
				nb.setChbm(rs.getString("chbm"));
				nb.setPk_yjcb(nj.getPrimaryKey());
				nb.setBqrkje(new UFDouble(rs.getDouble("bqrkje"),
						Constants.XS_JINE));
				nb.setBqrksl(new UFDouble(rs.getDouble("bqrksl"),
						Constants.XS_SHUNLIANG));
				nb.setBqrkdj(new UFDouble(rs.getDouble("bqrkdj"),
						Constants.XS_DANJIA));
				nb.setCgrkje(new UFDouble(rs.getDouble("cgrkje"),
						Constants.XS_JINE));
				nb.setCbtzdje(new UFDouble(rs.getDouble("cbtzdje"),
						Constants.XS_JINE));
				nb.setCgrksl(new UFDouble(rs.getDouble("cgrksl"),
						Constants.XS_SHUNLIANG));
				nb.setCkhjje(new UFDouble(rs.getDouble("ckhjje"),
						Constants.XS_JINE));
				nb.setCkhjsl(new UFDouble(rs.getDouble("ckhjsl"),
						Constants.XS_SHUNLIANG));
				nb.setClcksl(new UFDouble(rs.getDouble("clcksl"),
						Constants.XS_SHUNLIANG));
				nb.setQcjcsl(new UFDouble(rs.getDouble("qcjcsl"),
						Constants.XS_SHUNLIANG));
				nb.setClckje(new UFDouble(rs.getDouble("clckje"),
						Constants.XS_JINE));
				nb.setQcjcje(new UFDouble(rs.getDouble("qcjcje"),
						Constants.XS_JINE));
				nb.setQtckje(new UFDouble(rs.getDouble("qtckje"),
						Constants.XS_JINE));
				nb.setQtcksl(new UFDouble(rs.getDouble("qtcksl"),
						Constants.XS_SHUNLIANG));
				nb.setQtrkje(new UFDouble(rs.getDouble("qtrkje"),
						Constants.XS_JINE));
				nb.setQtrksl(new UFDouble(rs.getDouble("qtrksl"),
						Constants.XS_SHUNLIANG));
				nb.setScrkje(new UFDouble(rs.getDouble("scrkje"),
						Constants.XS_JINE));
				nb.setScrksl(new UFDouble(rs.getDouble("scrksl"),
						Constants.XS_SHUNLIANG));
				nb.setWwckje(new UFDouble(rs.getDouble("wwckje"),
						Constants.XS_JINE));
				nb.setWwcksl(new UFDouble(rs.getDouble("wwcksl"),
						Constants.XS_SHUNLIANG));
				nb.setWwrkje(new UFDouble(rs.getDouble("wwrkje"),
						Constants.XS_JINE));
				nb.setWwrksl(new UFDouble(rs.getDouble("wwrksl"),
						Constants.XS_SHUNLIANG));
				nb.setXssrje(new UFDouble(rs.getDouble("xssrje"),
						Constants.XS_JINE));
				nb.setXssrsl(new UFDouble(rs.getDouble("xssrsl"),
						Constants.XS_SHUNLIANG));
				nb.setXsckje(new UFDouble(rs.getDouble("xscdje"),
						Constants.XS_JINE));
				nb.setXscksl(new UFDouble(rs.getDouble("xscksl"),
						Constants.XS_SHUNLIANG));
				nb.setXscdje(new UFDouble(rs.getDouble("xscdje"),
						Constants.XS_SHUNLIANG));
				nb.setXsckdj(new UFDouble(rs.getDouble("xsckdj"),
						Constants.XS_DANJIA));
				nb.setXscbjzje(new UFDouble(rs.getDouble("xscbjzje"),
						Constants.XS_TZJE));
				nb.setXscbtzhje(new UFDouble(rs.getDouble("xscbtzhje"),
						Constants.XS_TZJE));
				nb.setQmjcdj(new UFDouble(rs.getDouble("qmjcdj"),
						Constants.XS_DANJIA));
				nb.setQmjcje(new UFDouble(rs.getDouble("qmjcje"),
						Constants.XS_JINE));
				nb.setQmjcsl(new UFDouble(rs.getDouble("qmjcsl"),
						Constants.XS_SHUNLIANG));
				nb.setDef12(rs.getString("def12"));// �������(��)
				nb.setDef13(rs.getString("def13"));
				nb.setDef14(rs.getString("def14"));
				nb.setDef15(rs.getString("def15"));
				nb.setDr(new Integer(0));
				reList.add(nb);
			}
			return reList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean saveSfccb(NcReportYjcbVO nj, List<NcReportYjcbBVO> list)
			throws MyDMOException {
		Connection conn = null;
		Statement stmt = null;
		String sql1 = "INSERT INTO  BLK_SFCCB("
				+ "CACCOUNTYEAR,CACCOUNTMONTH,"
				+ "INVCODE, QCJCS, QCJCJ, CGRKS, CGRK, SCRKS, SCRK, QTRKS, QTRKJ, WWRKS, "
				+ "WWRK, NINNUM, CBTZ, RKDJ, NINMNY, XSCKS, CKDJ, XSCKJ, NNUMBER, NORIGINALCURSUMMNY, "
				+ "CLCKS, CLJE, WWCKS, WWCKJ, QTCKS, QTCKJ, QMJCS, QMJCDJ,"
				+ "QMJCJ, CBTZJE, TZHCB) VALUES(";
		String year = nj.getDjnd();
		String month = nj.getDjqj();
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			for (int i = 0; i < list.size(); i++) {
				NcReportYjcbBVO temp = list.get(i);
				String sql2 = "'" + year + "','" + month + "','"
						+ temp.getChbm() + "',"
						+ temp.getQcjcsl().doubleValue() + ","
						+ temp.getQcjcje().doubleValue() + ","
						+ temp.getCgrksl().doubleValue() + ","
						+ temp.getCgrkje().doubleValue() + ","
						+ temp.getScrksl().doubleValue() + ","
						+ temp.getScrkje().doubleValue() + ","
						+ temp.getQtrksl().doubleValue() + ","
						+ temp.getQtrkje().doubleValue() + ","
						+ temp.getWwrksl().doubleValue() + ","
						+ temp.getWwrkje().doubleValue() + ","
						+ temp.getBqrksl().doubleValue() + ","
						+ temp.getCbtzdje().doubleValue() + ","
						+ temp.getBqrkdj().doubleValue() + ","
						+ temp.getBqrkje().doubleValue() + ","
						+ temp.getXscksl().doubleValue() + ","
						+ temp.getXsckdj().doubleValue() + ","
						+ temp.getXsckje().doubleValue() + ","
						+ temp.getXssrsl().doubleValue() + ","
						+ temp.getXssrje().doubleValue() + ","
						+ temp.getClcksl().doubleValue() + ","
						+ temp.getClckje().doubleValue() + ","
						+ temp.getWwcksl().doubleValue() + ","
						+ temp.getWwckje().doubleValue() + ","
						+ temp.getQtcksl().doubleValue() + ","
						+ temp.getQtckje().doubleValue() + ","
						+ temp.getQmjcsl().doubleValue() + ","
						+ temp.getQmjcdj().doubleValue() + ","
						+ temp.getQmjcje().doubleValue() + ","
						+ temp.getXscbjzje().doubleValue() + ","
						+ temp.getXscbtzhje().doubleValue() + ")";
				stmt.addBatch(sql1 + sql2);
			}
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// added by zjj 2010-05-21

	public List<NcReportYjcbBVO> queryIaBillVoList2(NcReportYjcbVO pvo,
			String billType) throws MyDMOException {
		Connection con = null;
		Statement stat = null;
		try {
			con = this.getConnection();
			stat = con.createStatement();
			List<NcReportYjcbBVO> reList = new ArrayList<NcReportYjcbBVO>();
			if (billType.equals("I5")) {
				String sql = "select a5.pk_invcl, a5.invclasscode, a5.invclassname, a3.pk_invbasdoc, "
						+ "a3.invcode, a3.invname, a3.invspec, a1.cbilltypecode, "
						+ "sum(a2.nnumber) as nnumber, sum(a2.nmoney) as nmoney "
						+ "from ia_bill a1 left join ia_bill_b a2 "
						+ "on a1.cbillid = a2.cbillid left join bd_invbasdoc a3 "
						+ "on a3.pk_invbasdoc = a2.cinvbasid left join bd_invcl a4 "
						+ "on a3.pk_invcl = a4.pk_invcl left join bd_invcl a5 "
						+ "on a5.invclasscode=substr(a4.invclasscode,1,3) "
						+ " left join bd_cubasdoc a6 on a1.ccustomvendorbasid=a6.pk_cubasdoc "
						+ "left join bd_areacl a7 on a6.pk_areacl=a7.pk_areacl "
						+ "where a1.dr = 0  and a1.bauditedflag = 'Y'  "
						+ "and substr(a1.dbilldate, 1, 4) = '"
						+ pvo.getDjnd()
						+ "' "
						+ "and substr(a1.dbilldate, 6, 2) = '"
						+ pvo.getDjqj()
						+ "' "
						+ "and  a1.cbilltypecode = 'I5' and a7.areaclcode != '3' "
						+ "and a2.blargessflag = 'N'  "
						+ "group by a5.pk_invcl, a5.invclasscode, a5.invclassname,"
						+ " a3.pk_invbasdoc, a3.invcode, a3.invname, a3.invspec, "
						+ "a1.cbilltypecode " + "order by a5.invclasscode";
				ResultSet rs = stat.executeQuery(sql);
				while (rs.next()) {
					NcReportYjcbBVO nb = new NcReportYjcbBVO();
					nb.setPk_yjcb(pvo.getPk_yjcb());
					// nb.setChbm(rs.getString("invcode"));
					nb.setChbm(rs.getString("pk_invbasdoc"));
					nb.setChfl(rs.getString("pk_invcl"));
					nb.setXscksl(new UFDouble(rs.getDouble("nnumber"),
							Constants.XS_SHUNLIANG));
					nb.setXsckje(new UFDouble(0));
					nb.setXssrsl(nb.getXscksl());
					nb.setDr(new Integer(0));
					nb.setDef12(rs.getString("invcode"));// �������(��)
					nb.setDef13(rs.getString("invclasscode")); // ����������
					nb.setDef14(rs.getString("invname")); // �������
					nb.setDef15(rs.getString("invspec"));// ���
					reList.add(nb);
				}
			}
			return reList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean updateVolist(List<NcReportYjcbBVO> list)
			throws MyDMOException {
		Connection con = null;
		Statement sta = null;
		try {
			con = this.getConnection();
			sta = con.createStatement();
			for (int i = 0; i < list.size(); i++) {
				String sql = "update nc_report_yjcb_b set xscbjzje = "
						+ list.get(i).getXscbjzje().doubleValue()
						+ " , xscbtzhje = "
						+ list.get(i).getXscbtzhje().doubleValue()
						+ " where pk_yjcb_b = '" + list.get(i).getPk_yjcb_b()
						+ "'";
				sta.addBatch(sql);
			}
			sta.executeBatch();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (sta != null) {
				try {
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public boolean deleteBlk(String year, String month) throws MyDMOException {
		Connection con = null;
		Statement sta = null;
		try {
			con = this.getConnection();
			String deleteSql = "delete blk_sfccb where caccountyear = '" + year
					+ "' and caccountmonth = '" + month + "'";
			sta = con.createStatement();
			sta.executeUpdate(deleteSql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (sta != null) {
				try {
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public MyBillVO queryMyBillVO_2(String djnd, String djqj)
			throws MyDMOException {
		Connection conn = null;
		Statement stmt = null;
		BaseDAO dao = new BaseDAO();
		String whereStr = " djnd='" + djnd + "' and djqj='" + djqj
				+ "' and (dr=0 or dr is null)";
		try {
			Collection coll = dao.retrieveByClause(NcReportYjcbVO.class,
					whereStr);
			if (coll == null || coll.size() == 0)
				throw new MyDMOException("��ǰ���:" + djnd + ",�ڼ�:" + djqj
						+ "û���½�ɱ��������ݣ�");
			NcReportYjcbVO[] yjcbVoArrays = new NcReportYjcbVO[coll.size()];
			coll.toArray(yjcbVoArrays);
			// ��ͷVO
			NcReportYjcbVO pvo = yjcbVoArrays[0];
			String sql = "select t2.pk_invbasdoc,t2.invcode,t2.invname,t1.qmjcje,t1.qmjcsl,t1.qmjcdj, t4.invclasscode, t4.invclassname, t2.invspec"
					+ " from nc_report_yjcb_b t1";
			// ��һ�μ���
			// if (djnd.equals("2009") && djqj.equals("12"))
			// sql += " left join bd_invbasdoc t2 on t1.chbm = t2.invcode";
			// else
			sql += " left join bd_invbasdoc t2 on t1.chbm = t2.pk_invbasdoc";
			sql += " left join bd_invcl t3 on t2.pk_invcl = t3.pk_invcl"
					+ " left join bd_invcl t4 on t4.invclasscode = substr(t3.invclasscode, 1, 3)"
					+ " where t1.pk_yjcb = '" + pvo.getPk_yjcb()
					+ "' and (t1.dr=0 or t1.dr is null)";
			conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List rsList = new ArrayList();
			while (rs.next()) {
				NcReportYjcbBVO vo = new NcReportYjcbBVO();
				// vo.setChbm(rs.getString("chbm"));
				vo.setChbm(rs.getString("pk_invbasdoc"));
				vo.setQmjcje(new UFDouble(rs.getDouble("qmjcje"),
						Constants.XS_JINE));
				vo.setQmjcsl(new UFDouble(rs.getDouble("qmjcsl"),
						Constants.XS_SHUNLIANG));
				vo.setQmjcdj(new UFDouble(rs.getDouble("qmjcdj"),
						Constants.XS_DANJIA));
				vo.setChfl(rs.getString("invclassname"));
				vo.setDef12(rs.getString("invcode"));// �������(��)
				vo.setDef13(rs.getString("invclasscode"));
				vo.setDef14(rs.getString("invname"));
				vo.setDef15(rs.getString("invspec"));
				vo.setDr(new Integer(0));
				rsList.add(vo);
			}

			// ����VOs
			if (rsList == null || rsList.size() == 0)
				throw new MyDMOException("��ǰ���:" + djnd + ",�ڼ�:" + djqj
						+ "û���½�ɱ��������ݣ���������Ϊ�գ�");
			NcReportYjcbBVO[] bvos = new NcReportYjcbBVO[rsList.size()];
			rsList.toArray(bvos);
			MyBillVO billVo = new MyBillVO();
			billVo.setParentVO(pvo);
			billVo.setChildrenVO(bvos);
			return billVo;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MyDMOException("���ݿ��쳣�� " + e.getMessage());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
