package nc.bs.wds.pub.report;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.pub.DataManageObject;
import nc.bs.pub.SystemException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.zmpub.pub.report.ReportBaseVO;
/**
 * 报表查询DMO类
 * @author Administrator
 *
 */
public class ReportDMO  extends DataManageObject{

    public ReportDMO() throws javax.naming.NamingException, SystemException {
        super();
    }

    public ReportDMO(String dbName) throws javax.naming.NamingException,
            SystemException {
        super(dbName);
    }
   /**
    * 
    * @作者：mlr
    * @说明：完达山物流项目 
    *      报表批量查询类
    * @时间：2011-7-12下午12:48:26
    * @param sqls
    * @return
    * @throws SQLException
    */
    public List<ReportBaseVO[]> queryVOBySql(String[] sqls)throws SQLException{
    	List<ReportBaseVO[]> list=new ArrayList<ReportBaseVO[]>();
    	if(sqls==null ||sqls.length==0){
    		throw new SQLException("查询语句不能为空");
    	}
    	int size=sqls.length;
    	for(int i=0;i<size;i++){
    		ReportBaseVO[] vos=queryVOBySql(sqls[i]);
    		list.add(vos);
    	}
		return list; 	
    }
    /*
     * 得到总计VO.
     */
    public ReportBaseVO[] queryVOBySql(String sql) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Vector vResult = new Vector();
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            ReportBaseVO voTmp = null;
            while (rs.next()) {
                voTmp = new ReportBaseVO();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    Object value = null;
                    if (rsmd.getColumnType(i) == Types.NUMERIC
                            || rsmd.getColumnType(i) == Types.DECIMAL
                            || rsmd.getColumnType(i) == Types.DOUBLE
                            || rsmd.getColumnType(i) == Types.FLOAT) {
                        Object o = rs.getObject(i);
                        value = (o == null ? new UFDouble("0") : new UFDouble(o.toString()));
                    } else if (rsmd.getColumnType(i) == Types.INTEGER
                            || rsmd.getColumnType(i) == Types.SMALLINT) {
                        value = new Integer(rs.getInt(i));
                    } else if (rsmd.getColumnType(i) == Types.DATE) {
                        value = new UFDate(rs.getDate(i));
                    } else
                        value = rs.getString(i);
                    voTmp.setAttributeValue(rsmd.getColumnName(i), value);
                }
                vResult.add(voTmp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (con != null)
                    con.close();
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs = null;
            } catch (SQLException ex) {
                throw ex;
            }
        }
        ReportBaseVO[] vos = new ReportBaseVO[vResult.size()];
        vResult.copyInto(vos);
        return vos;
    }
}
