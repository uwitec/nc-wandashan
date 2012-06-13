
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.billsource.SourceParaVO;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uif.pub.exception.UifRuntimeException;
import nc.vo.scm.sourcebill.LightBillVO;
	/**
	 * 默认的单据来源查找算法。
	 * 创建日期：(2004-6-14 13:10:40)
	 * @author：mlr
	 */
	public class DefaultDataFinder2 implements IBillDataFinder2{
	/**
	 * 返回查询来源单据的SQL
	 * 原理： 根据当前单据类型 billType
	 * 获得VoTable 通过VoTable 拼接一个主子表关联的
	 * sql语句 根据billid 查询出 当前单据的 单据号 
	 * 来源单据的单据类型 和单据id
	 * 来源单据类字段必须为 vlastbilltype
	 * 来源单据id字段必须为  vlastbillid
	 * 单据类型字段必须为  pk_billtype
	 */
	protected String createSQL(String billType)
	{
		nc.vo.pf.changeui02.VotableVO headAttrVo = PfDataCache.getBillTypeToVO(billType, true);
		nc.vo.pf.changeui02.VotableVO itemAttrVo = PfDataCache.getBillTypeToVO(billType, false);
		if (headAttrVo == null || itemAttrVo == null)
		{
			System.out.println("单据类型:" + billType + "没有注册单据VO对照表");
			return null;
		}
		SourceParaVO paraVo = new SourceParaVO(headAttrVo);
		String hTable = headAttrVo.getVotable();//主表
		String hPkField = headAttrVo.getPkfield();//主表主键
		String hTableCodeField = headAttrVo.getBillno();//主表单据号
		String hTableBillTypeField = paraVo.getBillType();//单据类型
		String bTable = itemAttrVo.getVotable();//子表
		String bFkField = itemAttrVo.getPkfield();//子表主键
		String bTableSourceTypeField = paraVo.getLastBillType();//来源单据类型
		String bTableSourceIDField = paraVo.getLastBillID();//来源单据id
		StringBuffer sb = new StringBuffer("SELECT DISTINCT");
		sb.append(" ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(", ");
		//处理只有主表的情况,用别名处理
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + "." + bTableSourceTypeField);
		}
		else
		{
		sb.append(bTable + "." + bTableSourceTypeField);
		}
		sb.append(", ");
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + "." + bTableSourceIDField);
		}
		else
		{
		sb.append(bTable + "." + bTableSourceIDField);
		}
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(", ");
		//处理只有主表的情况,用别名处理
		if (hTable.equalsIgnoreCase(bTable))
		{
			sb.append(bTable + " B");
		}
		else
		{
			sb.append(bTable);
		}
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=");
		if (hTable.equalsIgnoreCase(bTable))
		{
			sb.append("B" + "." + bFkField);
		}
		else
		{
			sb.append(bTable + "." + bFkField);
		}
		sb.append(" and ");
		sb.append(hTable + "." + hPkField);
		sb.append("=?");
		sb.append(" and ");
		sb.append(hTable + ".dr =0");
		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable))
		{
			sb.append("B" + ".dr =0");
		}
		else
		{
			sb.append(bTable + ".dr =0");
		}
		return sb.toString();
	}
	/**
	 * 功能描述:创建查询语句;在该类型的单据中查找某种类型单据的后续单据
	 * 输入参数:String billType,单据类型. 当前单据的 下游单据类型
	 * 返回值: SQL语句
	 * 异常处理:
	 * 日期:
	 * @return java.lang.String
	 * @param billType java.lang.String
	 */
	protected String createSQL1(String billType)
	{	
		nc.vo.pf.changeui02.VotableVO headAttrVo =PfDataCache.getBillTypeToVO(billType, true);
		nc.vo.pf.changeui02.VotableVO itemAttrVo =PfDataCache.getBillTypeToVO(billType, false);
		if (headAttrVo == null || itemAttrVo == null)
		{
			System.out.println("单据类型:" + billType + "没有注册单据VO对照表");
			return null;
		}
		SourceParaVO paraVo = new SourceParaVO(headAttrVo);
		String hTable = headAttrVo.getVotable();
		String hPkField = headAttrVo.getPkfield();
		String hTableCodeField = headAttrVo.getBillno();
		String hTableBillTypeField = paraVo.getBillType();
		String bTable = itemAttrVo.getVotable();
		String bFkField = itemAttrVo.getPkfield();
		String bTableSourceTypeField = paraVo.getLastBillType();
		String bTableSourceIDField = paraVo.getLastBillID();
		//如果单据没有来源单据类型字段,则返回空.即如果该类型单据没有标识来源单据
		//类型,就无法定位它是否是后单据.
		//通常该情况是:它是某种固定类型单据的后续单据.
		if (bTableSourceTypeField == null || bTableSourceIDField == null)
			return null;
		//在该类型的单据中查找某种类型单据的后续单据 
		StringBuffer sb = new StringBuffer("SELECT DISTINCT");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append(", ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(", ");
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append(bTable + " B");
		}
		else
		{
		sb.append(bTable);
		}
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=");
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + "." + bFkField);
		}
		else
		{
		sb.append(bTable + "." + bFkField);
		}
		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + "." + bTableSourceIDField);
		}
		else
		{
		sb.append(bTable + "." + bTableSourceIDField);
		}
		sb.append("=?");
		sb.append(" and ");
		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + "." + bTableSourceTypeField);
		}
		else
		{
		sb.append(bTable + "." + bTableSourceTypeField);
		}
		sb.append("=?");

		if (hTableBillTypeField != null)
		{
		sb.append(" and ");
		sb.append(hTable + "." + hTableBillTypeField);
		sb.append("=?");
		}
		sb.append(" and ");
		sb.append(hTable + ".dr =0");
		sb.append(" and ");

		if (hTable.equalsIgnoreCase(bTable))
		{
		sb.append("B" + ".dr =0");
		}
		else
		{
		sb.append(bTable + ".dr =0");
		}
		return sb.toString();
	}
	/**
	 * 此处插入方法说明。
	 * 功能描述:
	 * 输入参数:
	 * 返回值:
	 * 异常处理:
	 * 日期:
	 * @return java.lang.String
	 * @param billType java.lang.String
	 */
	protected String createSQL2(String billType)
	{
		nc.vo.pf.changeui02.VotableVO headAttrVo =
			PfDataCache.getBillTypeToVO(billType, true);
		if (headAttrVo == null)
		{
			System.out.println("单据类型:" + billType + "没有注册单据VO对照表");
			return null;
		}
		SourceParaVO paraVo = new SourceParaVO(headAttrVo);

		String hTable = headAttrVo.getVotable();
		String hPkField = headAttrVo.getPkfield();
		String hTableCodeField = headAttrVo.getBillno();

		StringBuffer sb = new StringBuffer("SELECT");
		sb.append(" ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(" ");
		sb.append("FROM");
		sb.append(" ");
		sb.append(hTable);
		sb.append(" ");
		sb.append("WHERE");
		sb.append(" ");
		sb.append(hTable + "." + hPkField);
		sb.append("=?");

		return sb.toString();
	}
	/**
	 * 进行查询单据编码。
	 * @return nc.vo.scm.sourcebill.LightBillVO[]
	 * @param curBillType java.lang.String
	 * @param curBillID java.lang.String
	 */
	protected String getBillCode(String curBillType, String curBillID)
	{
		String sql = createSQL2(curBillType);
		if (sql == null)
			return null;
		String code = null;
		PersistenceManager sessionManager = null;
		try
		{ 
			sessionManager = PersistenceManager.getInstance ();
			JdbcSession session = sessionManager. getJdbcSession ();
			SQLParameter para = new SQLParameter();
			para.addParam(curBillID);
			code = (String)session.executeQuery(sql,new ColumnProcessor());
			
		} 
		catch (DbException e)
		{
			Logger.error(e.getMessage(), e);
		}
		finally 
		{
			sessionManager.release ();
		}
		return code;
	}
	/*
	* 功能:根据当前的单据ID,单据类型,获得指定类型的后续单据.
	* 返回:LightBillVO[],后续单据VO数组,至少要填写LightBillVO的ID,TYPE,CODE三个属性.
	* 其中TYPE属性就是forwardBillTYPE的参数值
	* 参数:
	* 1.String curBillType :当前单据类型
	* 2.String curBillID:当前单据ID
	* 3.String forwardBillType:后续单据的类型
	*
	*/
	public  nc.vo.scm.sourcebill.LightBillVO[] getForwardBills(
		String curBillType,
		String curBillID,
		final String forwardBillType)
	{
		String sql = createSQL1(forwardBillType);
		if(sql == null){
			return null;
		}
		PersistenceManager sessionManager = null;
		try
		{ 
			sessionManager = PersistenceManager.getInstance ();
			JdbcSession session = sessionManager. getJdbcSession ();
			SQLParameter para = new SQLParameter();
			para.addParam(curBillID);		
			ResultSetProcessor p = new ResultSetProcessor() {
				public Object handleResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();
					while (rs.next())
					{
						String id = rs.getString(1);
						String code = rs.getString(2);

						if (id != null)
						{
							LightBillVO svo = new LightBillVO();
							svo.setID(id);
							svo.setCode(code);
							svo.setType(forwardBillType);
							al.add(svo);
						}
					}
					return al;
				}
			};
			ArrayList result = (ArrayList) session.executeQuery(sql,para,p);
			if(result.size()==0)
				return null;
			else
				return (LightBillVO[]) result.toArray(new LightBillVO[result.size()]);			
		} 
		catch (DbException e)
		{
			Logger.error(e.getMessage(), e);
			throw new UifRuntimeException("getForwardBills error");
		}
		finally 
		{
			sessionManager.release ();
		}
	}
	/*
	 * 功能:根据当前的单据ID,单据类型,获得所有的来源单据
	 * 返回:LightBillVO[],来源单据VO数组,至少要填写LightBillVO的ID,TYPE,CODE三个属性.
	 * 参数:
	 * 1.String curBillType :当前单据类型
	 * 2.String curBillID:当前单据ID
	 * 利用递归算法可以算出来源的来源
	 *
	 */
	public  nc.vo.scm.sourcebill.LightBillVO[] getSourceBills(
		String curBillType,
		String curBillID)
	{
		String sql = createSQL(curBillType);
		if (sql == null)
			return null;
		PersistenceManager sessionManager = null;
		try
		{ 
			sessionManager = PersistenceManager.getInstance ();
			JdbcSession session = sessionManager. getJdbcSession ();
			SQLParameter para = new SQLParameter();
			para.addParam(curBillID);		
			ResultSetProcessor p = new ResultSetProcessor() {
				public Object handleResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();
					while (rs.next())
					{
//						rs.getObject(1);
						String type = rs.getString(1);
						String id = rs.getString(2);
						String code = rs.getString(3);
						if (type != null
							&& id != null
							&& type.trim().length() > 0
							&& id.trim().length() > 0)
						{
							LightBillVO svo = new LightBillVO();
							svo.setType(type);
							svo.setID(id);
							svo.setCode(code);
							al.add(svo);
						}
					}
					return al;
				}
			};
			ArrayList result = (ArrayList) session.executeQuery(sql,para,p);
			if(result.size()==0)
				return null;
			return ( nc.vo.scm.sourcebill.LightBillVO[]) result.toArray(new  nc.vo.scm.sourcebill.LightBillVO[result.size()]);
		} 
		catch (DbException e)
		{
			Logger.error(e.getMessage(), e);
			throw new UifRuntimeException(e.getMessage());
		}
		finally 
		{
			sessionManager.release ();
		}

	}
}

