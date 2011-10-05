package nc.bs.wl.pub;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import nc.bs.logging.Logger;
import nc.bs.pub.DataManageObject;
import nc.bs.trade.billsource.IBillFinder;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.trade.billsource.LightBillVO;

public class WdsWlPubDMO extends DataManageObject {

	public WdsWlPubDMO() throws NamingException {
		super();
	}
	public int executeUpdateSQL(String sql) throws SQLException {

		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			return session.executeUpdate(sql);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage(), e.getSQLState());
		} finally {
			if (sessionManager != null)
				sessionManager.release();
		}
	}

	public int executeUpdateSQL(String sql, SQLParameter[] paras)
			throws SQLException {

		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			for (SQLParameter para : paras) {
				session.executeUpdate(sql, para);
			}
			return session.executeBatch();
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage(), e.getSQLState());
		} finally {
			if (sessionManager != null)
				sessionManager.release();
		}
	}
	//ÅúÁ¿Ö´ÐÐSQL
	protected int[] executeBatch(String[] sqls) throws Exception {
		if (sqls == null || sqls.length == 0) {
			return null;
		}
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int[] resust = null;
		try {
			con = getConnection();
			st = con.createStatement();
			for (String sql : sqls) {
				st.addBatch(sql);
			}
			resust = st.executeBatch();
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new SQLException(ex.getMessage());
			}
		}
		return resust;
	}
	public ArrayList executeQuerySQL(String sql) throws SQLException {
		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			return (ArrayList) session.executeQuery(sql,new ArrayListProcessor());
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage(), e.getSQLState());
		} finally {
			if (sessionManager != null)
				sessionManager.release();
		}
	}
	public ArrayList executeQuerySQL(String sql, Class type) throws SQLException {
		//
		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			Object o  = session.executeQuery(sql, new BeanListProcessor(type));
			if (o == null)
				return null;
			ArrayList l = (ArrayList) o;
			return l;
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new SQLException(e.getMessage(), e.getSQLState());
		} finally {
			if (sessionManager != null)
				sessionManager.release();
		}
	}
	//nc.bs.wl.pub.WdsWlPubDMO
	//nc.vo.scm.sourcebill.LightBillVO
	public LightBillVO queryBillGraph(
			String billFinderClassName,
			String id,
			String type)
			throws UifException {
			try
			{
				IBillFinder billFinder =
					(IBillFinder) Class.forName(billFinderClassName).newInstance();
				return billFinder.queryBillGraph(id, type);
			}
			catch (BusinessException be)
			{
				Logger.error(be.getMessage(), be);
				throw new UifException(be.getMessage(), be);
			}

			catch (Exception ex)
			{
				Logger.error(ex.getMessage(), ex);
				throw new UifException(ex.getMessage(), ex);
			}
		}
}
