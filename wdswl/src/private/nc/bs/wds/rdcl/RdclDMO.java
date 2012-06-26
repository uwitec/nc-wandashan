package nc.bs.wds.rdcl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import nc.bs.pub.DataManageObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wds.rdcl.RdclVO;

/**
 * Rdcl的DMO类。
 * 
 * 创建日期：(2001-5-28)
 * 
 * @author：
 */
public class RdclDMO extends DataManageObject {
	/**
	 * RdclDMO 构造子注解。
	 * 
	 * @exception javax.naming.NamingException
	 *                父类构造子抛出的异常。
	 * @exception nc.bs.pub.SystemException
	 *                父类构造子抛出的异常。
	 */
	public RdclDMO() throws javax.naming.NamingException {
		super();
	}

	/**
	 * RdclDMO 构造子注解。
	 * 
	 * @param dbName
	 *            java.lang.String 在EJB Server中配置的数据库DataSource名称。
	 * @exception javax.naming.NamingException
	 *                父类构造子抛出的异常。
	 * @exception nc.bs.pub.SystemException
	 *                父类构造子抛出的异常。
	 */
	public RdclDMO(String dbName) throws javax.naming.NamingException {
		super(dbName);
	}

	/**
	 * 通过单位编码返回指定公司所有记录VO数组。如果单位编码为空返回所有记录。
	 * 
	 * 已知问题：请注意生成的sql语句：where子句中假设公司编码字段为pk_corp。 如果你要针对公司进行查询，那么应采用你的实际字段名来手工修改
	 * sql语句。 创建日期：(2001-5-28)
	 * 
	 * @return nc.vo.bd.b25.RdclVO[]
	 * @param unitCode
	 *            int
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public RdclVO[] queryAll(String unitCode) throws SQLException {

		/*************************************************************/
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryAll",
				new Object[] { unitCode });
		/*************************************************************/

		String sql = "";
		if (unitCode != null) {
			sql = "select pk_rdcl, pk_corp, rdflag, rdcode, rdname, pk_frdcl,sealflag,uisreturn from wds_rdcl where pk_corp = ? order by pk_frdcl,rdcode asc";
		} else {
			sql = "select pk_rdcl, pk_corp, rdflag, rdcode, rdname, pk_frdcl,sealflag,uisreturn from wds_rdcl order by pk_frdcl,rdcode";
		}

		RdclVO rdcls[] = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			if (unitCode != null) {
				stmt.setString(1, unitCode);
			}
			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {
				RdclVO rdcl = new RdclVO();
				// pk_rdcl :
				String pk_rdcl = rs.getString(1);
				rdcl.setPk_rdcl(pk_rdcl == null ? null : pk_rdcl.trim());
				// pk_corp :
				String pk_corp = rs.getString(2);
				rdcl.setPk_corp(pk_corp == null ? null : pk_corp.trim());
				// rdflag :
				Integer rdflag = (Integer) rs.getObject(3);
				rdcl.setRdflag(rdflag == null ? null : rdflag);
				// rdcode :
				String rdcode = rs.getString(4);
				rdcl.setRdcode(rdcode == null ? null : rdcode.trim());
				// rdname :
				String rdname = rs.getString(5);
				rdcl.setRdname(rdname == null ? null : rdname.trim());
				// pk_frdcl :
				String pk_frdcl = rs.getString(6);
				rdcl.setPk_frdcl(pk_frdcl == null ? null : pk_frdcl.trim());
				String sealflag = rs.getString(7);
				rdcl.setSealflag(new UFBoolean(
						"Y".equalsIgnoreCase(sealflag) ? true : false));
				String uisreturn = rs.getString(8);
				rdcl.setUisreturn(new UFBoolean(
						"Y".equalsIgnoreCase(uisreturn) ? true : false));
				v.addElement(rdcl);
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
		rdcls = new RdclVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(rdcls);
		}

		/*************************************************************/
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryAll",
				new Object[] { unitCode });
		/*************************************************************/

		return rdcls;
	}

	/**
	 * 根据VO中所设定的条件返回所有符合条件的VO数组
	 * 
	 * 创建日期：(2001-5-28)
	 * 
	 * @return nc.vo.bd.b25.RdclVO[]
	 * @param rdclVO
	 *            nc.vo.bd.b25.RdclVO
	 * @param isAnd
	 *            boolean 以与条件查询还是以或条件查询
	 * @exception java.sql.SQLException
	 *                异常说明。
	 */
	public RdclVO[] queryByVO(RdclVO condRdclVO, Boolean isAnd)
			throws SQLException {

		/*************************************************************/
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryByVO", new Object[] {
				condRdclVO, isAnd });
		/*************************************************************/

		String strSql = "select pk_rdcl, pk_corp, rdflag, rdcode, rdname, pk_frdcl,uisreturn from wds_rdcl";
		String strConditionNames = "";
		String strAndOr = "and ";
		if (!isAnd.booleanValue()) {
			strAndOr = "or  ";
		}
		if (condRdclVO.getPk_corp() != null) {
			strConditionNames += strAndOr + "pk_corp=? ";
		}
		if (condRdclVO.getRdflag() != null) {
			strConditionNames += strAndOr + "rdflag=? ";
		}
		if (condRdclVO.getRdcode() != null) {
			strConditionNames += strAndOr + "rdcode=? ";
		}
		if (condRdclVO.getRdname() != null) {
			strConditionNames += strAndOr + "rdname=? ";
		}
		if (condRdclVO.getPk_frdcl() != null) {
			strConditionNames += strAndOr + "pk_frdcl=? ";
		}
		if (condRdclVO.getSealflag() != null) {
			strConditionNames += strAndOr + "pk_frdcl=? ";
		}
		if (condRdclVO.getUisreturn() != null) {
			strConditionNames += strAndOr + "uisreturn=? ";
		}
		if (strConditionNames.length() > 0) {
			strConditionNames = strConditionNames.substring(3,
					strConditionNames.length() - 1);
		} else {
			return queryAll(null);
		}
		// 拼接后的SQL语句
		strSql = strSql + " where " + strConditionNames;
		//
		int index = 0;
		RdclVO rdcls[] = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			stmt = con.prepareStatement(strSql);
			// set query condition fields:
			// set non PK fields:
			if (condRdclVO.getPk_corp() != null) {
				stmt.setString(++index, condRdclVO.getPk_corp());
			}
			if (condRdclVO.getRdflag() != null) {
				stmt.setInt(++index, condRdclVO.getRdflag().intValue());
			}
			if (condRdclVO.getRdcode() != null) {
				stmt.setString(++index, condRdclVO.getRdcode());
			}
			if (condRdclVO.getRdname() != null) {
				stmt.setString(++index, condRdclVO.getRdname());
			}
			if (condRdclVO.getPk_frdcl() != null) {
				stmt.setString(++index, condRdclVO.getPk_frdcl());
			}
			if (condRdclVO.getSealflag() != null) {
				stmt.setString(++index,
						condRdclVO.getSealflag().booleanValue() ? "Y" : "N");
			}
			if (condRdclVO.getUisreturn() != null) {
				stmt.setString(++index, condRdclVO.getUisreturn()
						.booleanValue() ? "Y" : "N");
			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				RdclVO rdcl = new RdclVO();
				//
				String pk_rdcl = rs.getString(1);
				rdcl.setPk_rdcl(pk_rdcl == null ? null : pk_rdcl.trim());
				//
				String pk_corp = rs.getString(2);
				rdcl.setPk_corp(pk_corp == null ? null : pk_corp.trim());
				//
				Integer rdflag = (Integer) rs.getObject(3);
				rdcl.setRdflag(rdflag == null ? null : rdflag);
				//
				String rdcode = rs.getString(4);
				rdcl.setRdcode(rdcode == null ? null : rdcode.trim());
				//
				String rdname = rs.getString(5);
				rdcl.setRdname(rdname == null ? null : rdname.trim());
				//
				String pk_frdcl = rs.getString(6);
				rdcl.setPk_frdcl(pk_frdcl == null ? null : pk_frdcl.trim());
				String sealflag = rs.getString(7);
				rdcl.setSealflag(new UFBoolean(
						"Y".equalsIgnoreCase(sealflag) ? true : false));
				String uisreturn = rs.getString(8);
				rdcl.setUisreturn(new UFBoolean(
						"Y".equalsIgnoreCase(uisreturn) ? true : false));
				v.addElement(rdcl);
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
		rdcls = new RdclVO[v.size()];
		if (v.size() > 0) {
			v.copyInto(rdcls);
		}

		/*************************************************************/
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryByVO", new Object[] {
				condRdclVO, isAnd });
		/*************************************************************/

		return rdcls;
	}

	public RdclVO[] queryByVO(RdclVO condRdclVO)
			throws SQLException {
		return queryByVO(condRdclVO, true);
	}

	public boolean isCurExisted(String pk_rdcl) throws SQLException {

		/*************************************************************/
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.wds.rdcl.RdclDMO", "isCurExisted",
				new Object[] { pk_rdcl });
		/*************************************************************/

		String sql = "";

		sql = "select count(*) from wds_rdcl where pk_rdcl = ? ";

		RdclVO rdcls[] = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		int number = 0;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, pk_rdcl);

			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {
				number = rs.getInt(1);
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

		/*************************************************************/
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.wds.rdcl.RdclDMO", "isCurExisted",
				new Object[] { pk_rdcl });
		/*************************************************************/
		if (number > 0)
			return true;
		else
			return false;
	}

	public boolean isHaveChildren(String pk_rdcl) throws SQLException {

		/*************************************************************/
		// 保留的系统管理接口：
		beforeCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryAll",
				new Object[] { pk_rdcl });
		/*************************************************************/

		String sql = "";

		sql = "select count(*) from wds_rdcl where pk_frdcl = ? ";

		RdclVO rdcls[] = null;
		Vector v = new Vector();
		Connection con = null;
		PreparedStatement stmt = null;
		int number = 0;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setString(1, pk_rdcl);

			ResultSet rs = stmt.executeQuery();
			//
			while (rs.next()) {
				number = rs.getInt(1);
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

		/*************************************************************/
		// 保留的系统管理接口：
		afterCallMethod("nc.bs.wds.rdcl.RdclDMO", "queryAll",
				new Object[] { pk_rdcl });
		/*************************************************************/
		if (number > 0)
			return true;
		else
			return false;
	}
}