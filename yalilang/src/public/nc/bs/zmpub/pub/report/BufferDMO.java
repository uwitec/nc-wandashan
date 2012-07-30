package nc.bs.zmpub.pub.report;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.zmpub.pub.report.ReportBufferVO;
import nc.vo.zmpub.pub.report2.ReportBuffer;

/**
 * 报表缓存配置查询类
 * 
 * @author mlr
 * 
 */
public class BufferDMO {
	private BaseDAO dao = null;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 根据功能节点号 查询配置缓存
	 * 
	 * @param nodeid
	 * @return
	 * @throws DAOException
	 */
	public ReportBuffer queryByNodeId(String nodeid) throws DAOException {
		List<ReportBufferVO> cl = (List<ReportBufferVO>) getDao()
				.retrieveByClause(ReportBufferVO.class,
						"  nodecode = '" + nodeid + "'");
		if (cl == null || cl.size() == 0) {
			return null;
		}
		ReportBufferVO vo = cl.get(0);
		return createBuffer(vo);
	}

	/**
	 * 创建报表配置缓存
	 * 
	 * @param vo
	 * @return
	 */
	private ReportBuffer createBuffer(ReportBufferVO vo) {
		String pk_config = vo.getPrimaryKey();// 主键
		String nodecode = vo.getNodecode();// 功能节点号
		// 交叉数据
		String strRows = vo.getStrRows();// 交叉行 用&作为分隔符
		String strCols = vo.getStrCols();// 交叉列 用&作为分隔符
		String strVals = vo.getStrVals();// 交叉值 用&作为分隔符
		UFBoolean istotal = vo.getIstotal();

		Integer lel = vo.getLel();// 汇总级次
		// 合计数据
		UFBoolean issub = vo.getIssub();// 是否小计
		UFBoolean issum = vo.getIssum();// 是否合计
		String totfields = vo.getTotfields();// 合计纬度 用&作为分隔符
		String totfieldsNames = vo.getTotfieldsNames();// 合计纬度名字 用&作为分隔符
		ReportBuffer buffer = new ReportBuffer();
		buffer.setIssub(issub);
		buffer.setIssum(issum);
		buffer.setNodecode(nodecode);
		buffer.setPk_config(pk_config);
		buffer.setLel(lel);
		buffer.setIstotal(istotal);
		if (strRows != null && strRows.length() != 0) {
			buffer.setStrRows(strRows.split("&"));
		}
		if (strCols != null && strCols.length() != 0) {
			buffer.setStrCols(strCols.split("&"));
		}

		if (strVals != null && strVals.length() != 0) {
			buffer.setStrVals(strVals.split("&"));
		}

		if (totfields != null && totfields.length() != 0) {
			buffer.setTotfields(totfields.split("&"));
		}

		if (totfieldsNames != null && totfieldsNames.length() != 0) {
			buffer.setTotfieldsNames(totfieldsNames.split("&"));
		}
		return buffer;
	}

	/**
	 * 更新缓存到数据库
	 * 
	 * @param vo
	 * @return
	 * @throws DAOException
	 */
	public void updateBuffer(ReportBuffer vo) throws DAOException {
		String pk_config = vo.getPk_config();// 主键
		String nodecode = vo.getNodecode();// 功能节点号
		// 交叉数据
		String[] strRows = vo.getStrRows();// 交叉行 用&作为分隔符
		String[] strCols = vo.getStrCols();// 交叉列 用&作为分隔符
		String[] strVals = vo.getStrVals();// 交叉值 用&作为分隔符
		Integer lel = vo.getLel();// 获得汇总级次
		UFBoolean istotal = vo.getIstotal();
		// 合计数据
		UFBoolean issub = vo.getIssub();// 是否小计
		UFBoolean issum = vo.getIssum();// 是否合计
		String totfields[] = vo.getTotfields();// 合计纬度 用&作为分隔符
		String totfieldsNames[] = vo.getTotfieldsNames();// 合计纬度名字 用&作为分隔符
		ReportBufferVO buffer = new ReportBufferVO();
		buffer.setIssub(issub);
		buffer.setIssum(issum);
		buffer.setNodecode(nodecode);
		buffer.setPk_config(pk_config);
		buffer.setStrRows(getStr(strRows));
		buffer.setStrCols(getStr(strCols));
		buffer.setStrVals(getStr(strVals));
		buffer.setLel(lel);
		buffer.setIstotal(istotal);
		buffer.setTotfields(getStr(totfields));
		buffer.setTotfieldsNames(getStr(totfieldsNames));
		if (pk_config == null || pk_config.length() == 0) {
			getDao().insertVO(buffer);
		} else {
			getDao().updateVO(buffer);
		}

	}

	/**
	 * 
	 * @param strRows
	 * @return
	 */
	private String getStr(String[] strRows) {
		if (strRows == null || strRows.length == 0)
			return null;
		String s = "";
		for (int i = 0; i < strRows.length; i++) {
			s = s.trim() + strRows[i] + "&";
		}

		return s;
	}

}
