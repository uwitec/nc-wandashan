package nc.vo.wl.pub;
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
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uif.pub.exception.UifRuntimeException;
import nc.vo.trade.billsource.LightBillVO;
/**
 * �ù�����Ҫ���� 
 * ͨ���������� ����id����ѯ���ε��ݻ����ε���
 * @author mlr
 */
public class BillQueryTool {
	/**
	 * ���ز�ѯ��Դ���ݵ�SQL
	 * ԭ���� ���ݵ�ǰ�������� billType
	 * ���VoTable ͨ��VoTable ƴ��һ�����ӱ�������
	 * sql��� ����billid ��ѯ�� ��ǰ���ݵ� ���ݺ� 
	 * ��Դ���ݵĵ������� �͵���id
	 * ��Դ�������ֶα���Ϊ vlastbilltype
	 * ��Դ����id�ֶα���Ϊ  vlastbillid
	 * ���������ֶα���Ϊ  pk_billtype
	 * @return java.lang.String
	 * @param billType java.lang.String
	 */
	protected static String createSQL(String billType)
	{
		nc.vo.pf.changeui02.VotableVO headAttrVo = PfDataCache.getBillTypeToVO(billType, true);
		nc.vo.pf.changeui02.VotableVO itemAttrVo = PfDataCache.getBillTypeToVO(billType, false);
		if (headAttrVo == null || itemAttrVo == null)
		{
			System.out.println("��������:" + billType + "û��ע�ᵥ��VO���ձ�");
			return null;
		}
		SourceParaVO paraVo = new SourceParaVO(headAttrVo);
		String hTable = headAttrVo.getVotable();//����
		String hPkField = headAttrVo.getPkfield();//��������
		String hTableCodeField = headAttrVo.getBillno();//�������ݺ�
		String hTableBillTypeField = paraVo.getBillType();//��������
		String bTable = itemAttrVo.getVotable();//�ӱ�
		String bFkField = itemAttrVo.getPkfield();//�ӱ�����
		String bTableSourceTypeField = paraVo.getLastBillType();//��Դ��������
		String bTableSourceIDField = paraVo.getLastBillID();//��Դ����id
		StringBuffer sb = new StringBuffer("SELECT DISTINCT");
		sb.append(" ");
		sb.append(hTable + "." + hTableCodeField);
		sb.append(", ");
		//����ֻ�����������,�ñ�������
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
		//����ֻ�����������,�ñ�������
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
	 * ��������:������ѯ���;�ڸ����͵ĵ����в���ĳ�����͵��ݵĺ�������
	 * �������:String billType,��������. ��ǰ���ݵ� ���ε�������
	 * ����ֵ: SQL���
	 * �쳣����:
	 * ����:
	 * @return java.lang.String
	 * @param billType java.lang.String
	 */
	protected static  String  createSQL1(String billType)
	{	
		nc.vo.pf.changeui02.VotableVO headAttrVo =PfDataCache.getBillTypeToVO(billType, true);
		nc.vo.pf.changeui02.VotableVO itemAttrVo =PfDataCache.getBillTypeToVO(billType, false);
		if (headAttrVo == null || itemAttrVo == null)
		{
			System.out.println("��������:" + billType + "û��ע�ᵥ��VO���ձ�");
			return null;
		}
		SourceParaVO paraVo = new SourceParaVO(headAttrVo);
		String hTable = headAttrVo.getVotable();
		String hPkField = headAttrVo.getPkfield();
		String hTableCodeField = headAttrVo.getBillno();
		String hTableBillTypeField = paraVo.getBillType();//�������� --> ��Ӧvotable �� def1      Ĭ�� pk_billtype
		String bTable = itemAttrVo.getVotable();
		String bFkField = itemAttrVo.getPkfield();
		String bTableSourceTypeField = paraVo.getLastBillType();//�ϲ㵥������-->��Ӧvotable ��def2  Ĭ��vlastbilltype
		String bTableSourceIDField = paraVo.getLastBillID();//�ϲ㵥��id -->��Ӧvotable ��def3  
		//�������û����Դ���������ֶ�,�򷵻ؿ�.����������͵���û�б�ʶ��Դ����
		//����,���޷���λ���Ƿ��Ǻ󵥾�.
		//ͨ���������:����ĳ�̶ֹ����͵��ݵĺ�������.
		if (bTableSourceTypeField == null || bTableSourceIDField == null)
			return null;
		//�ڸ����͵ĵ����в���ĳ�����͵��ݵĺ������� 
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
	/*
	* ����:���ݵ�ǰ�ĵ���ID,��������,���ָ�����͵ĺ�������.
	* ����:LightBillVO[],��������VO����,����Ҫ��дLightBillVO��ID,TYPE,CODE��������.
	* ����TYPE���Ծ���forwardBillTYPE�Ĳ���ֵ
	* ����:
	* 1.String curBillType :��ǰ��������
	* 2.String curBillID:��ǰ����ID
	* 3.String forwardBillType:�������ݵ�����
	*
	*/
	public static nc.vo.trade.billsource.LightBillVO[] getForwardBills(
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
			para.addParam(curBillType);
			para.addParam(forwardBillType);
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
	 * ����:���ݵ�ǰ�ĵ���ID,��������,������е���Դ����
	 * ����:LightBillVO[],��Դ����VO����,����Ҫ��дLightBillVO��ID,TYPE,CODE��������.
	 * ����:
	 * 1.String curBillType :��ǰ��������
	 * 2.String curBillID:��ǰ����ID
	 * Ҫ�������е���Դ �����в��õݹ��㷨��ѯ
	 */
	public static nc.vo.trade.billsource.LightBillVO[] getSourceBills(
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
			return (nc.vo.trade.billsource.LightBillVO[]) result.toArray(new nc.vo.trade.billsource.LightBillVO[result.size()]);
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