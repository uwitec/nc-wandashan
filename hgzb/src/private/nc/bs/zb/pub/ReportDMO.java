package nc.bs.zb.pub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import nc.bs.logging.Logger;
import nc.bs.mw.sqltrans.SqlTranslator;
import nc.bs.pub.DataManageObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.DynamicVO;

public class ReportDMO extends DataManageObject {

	public ReportDMO() throws NamingException {
		super();
	}

	public void setData(java.sql.ResultSet rs,
			CircularlyAccessibleValueObject vo,
			java.sql.ResultSetMetaData meta) throws SQLException {
		Object oValue = null;
		int iCount = meta.getColumnCount();
		for (int i = 1; i <= iCount; i++) {
			oValue = rs.getObject(i);
			vo.setAttributeValue(meta.getColumnName(i).toLowerCase(),oValue);
		}
	}

	public ArrayList<DynamicVO> queryReportData(String sql)
			throws BusinessException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DynamicVO> list = new ArrayList<DynamicVO>();
		DynamicVO voItem = null;
		try {
			int dbtype = getDatabaseType();
			SqlTranslator translate = new SqlTranslator(dbtype);
			sql = translate.getSql(sql);
			//翻译sql
			con = getConnection();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				// 置入读出的结果
				voItem = new DynamicVO();
				setData(rs, voItem, meta);
				list.add(voItem);
			}
		} catch (SQLException e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		} finally {
			try{
				if (rs != null)
					rs.close();
				if (stmt != null) 
					stmt.close();
				if (con != null) 
					con.close();
			}catch (SQLException ex) {
				throw new BusinessException(ex.getMessage());
			}
		}
		return list;
	}
	/**
	 * 根据sql查询数据vo
	 */
	public ArrayList<CircularlyAccessibleValueObject> queryCircuarlyVO(Class circuarClass,String sql)throws BusinessException{
		ArrayList<CircularlyAccessibleValueObject> list = new ArrayList<CircularlyAccessibleValueObject>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SuperVO voItem = null;
		try {
			int dbtype = getDatabaseType();
			SqlTranslator translate = new SqlTranslator(dbtype);

			sql = translate.getSql(sql);
			//翻译sql
			con = getConnection();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				// 置入读出的结果
				Object o = Class.forName(circuarClass.getName()).newInstance();
				if(o instanceof CircularlyAccessibleValueObject){
					setData(rs, (CircularlyAccessibleValueObject)o, meta);
					list.add(voItem);
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e.getMessage());
		} finally {
			try{
				if (rs != null)
					rs.close();
				if (stmt != null) 
					stmt.close();
				if (con != null) 
					con.close();
			}catch (SQLException ex) {
				throw new BusinessException(ex.getMessage());
			}
		}
		return list;
	}
}
