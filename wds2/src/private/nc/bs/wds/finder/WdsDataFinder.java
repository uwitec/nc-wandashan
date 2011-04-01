package nc.bs.wds.finder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.billsource.DefaultDataFinder;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uif.pub.exception.UifRuntimeException;
import nc.vo.bill.pub.MiscUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.trade.billsource.LightBillVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * DataFinder
 * @author zpm
 *
 */
public class WdsDataFinder extends DefaultDataFinder{
	
	private String currentBillType = null;//设定当前单据
	
	private String srcBillType = null;//设定来源单据

	@Override
	public String[] getForwardBillTypes(LightBillVO vo) throws BusinessException {
		BilltypeVO type = PfDataCache.getBillType(vo.getType());
		if (type == null)
			return null;
		if (type.getForwardbilltype() == null)
			return null;
		String[] forwordtypes = MiscUtil.getStringTokens(type.getForwardbilltype(),",");
		//
		ArrayList<String> types = new ArrayList<String>();
		for (int i = 0; i < forwordtypes.length; i++) {
			if (PfDataCache.getBillType(forwordtypes[i]) != null)
				types.add(forwordtypes[i]);
		}
		//
		forwordtypes = new String[types.size()];
		types.toArray(forwordtypes);
		return forwordtypes;
	}
	//后续单据注册类
	@Override
	protected String createSQL1(String billType) {//这个billtype参数为下游单据类型
		String sql = null;
		//根据上游单据ID，查询billtype所在单据的主键，公司，单据号
		if(WdsWlPubConst.WDS3.equals(billType)){//发运订单
			sql = " select distinct h1.PK_SENDORDER,h1.PK_CORP,h1.VBILLNO from wds_sendorder h1,wds_sendorder_b b1 where h1.PK_SENDORDER = b1.PK_SENDORDER and isnull(h1.dr,0) = 0 and isnull(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
			return sql;
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||//其它出库单
				WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//销售出库
			sql = "  select distinct h1.GENERAL_PK,h1.COMP,h1.VBILLCODE from tb_outgeneral_h h1,tb_outgeneral_b b1 where h1.GENERAL_PK = b1.GENERAL_PK and isnull(h1.dr,0) = 0 and isnull(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
			return sql;
		}else if(WdsWlPubConst.WDS5.equals(billType)){//销售运单
			sql = "  select distinct h1.PK_SOORDER,h1.PK_CORP,h1.VBILLNO from WDS_SOORDER h1 ,WDS_SOORDER_B b1 where h1.PK_SOORDER = b1.PK_SOORDER and isnull(h1.dr,0) = 0 and isnull(b1.dr,0) = 0 and b1.CSOURCEBILLHID = ? ";
			return sql;
		}
		return super.createSQL1(billType);
	}
	//来源单据注册类
	@Override
	protected String createSQL(String billType) {//这个billtype参数为当前单据类型
		//正常作法为：根据billtype所在单据ID，查询当前单据出来 来源单据类型、来源单据ID即可
		String sql = null;
		if(WdsWlPubConst.WDS3.equals(billType)){//发运订单
			sql = " select distinct ss.CSOURCETYPE,ss.CSOURCEBILLHID from wds_sendorder_b ss  where ss.PK_SENDORDER = ? and isnull(ss.dr,0) = 0 ";
			return sql;
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(billType)||//其它出库单
				WdsWlPubConst.BILLTYPE_SALE_OUT.equals(billType)){//销售出库
				sql = " select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID from tb_outgeneral_b  zz where zz.GENERAL_PK = ?  and isnull(zz.dr,0) = 0 ";
			return sql;
		}else if(WdsWlPubConst.WDS5.equals(billType)){ //销售运单
			sql="  select distinct zz.CSOURCETYPE,zz.CSOURCEBILLHID from WDS_SOORDER_b zz where zz.PK_SOORDER = ? and isnull(zz.dr,0) = 0 ";
			return sql;
		}
		return super.createSQL(billType);
	}
	//这个是取后续单据
	@Override
	public LightBillVO[] getForwardBills(
			String srcBillType, String srcBillID, final String curBillType) {
		//
		setCurrentBillType(srcBillType);
		return super.getForwardBills(srcBillType, srcBillID, curBillType);
	}
	
	/*
	 * 功能:根据当前的单据ID,单据类型,获得所有的来源单据
	 * 返回:LightBillVO[],来源单据VO数组,至少要填写LightBillVO的ID,TYPE,CODE三个属性. 参数: 1.String
	 * curBillType :当前单据类型 2.String curBillID:当前单据ID
	 * 
	 */
	@SuppressWarnings( { "serial", "unchecked" })
	public nc.vo.trade.billsource.LightBillVO[] getSourceBills(String curBillType, String curBillID) {
		String sql = createSQL(curBillType);
		if (sql == null)
			return null;
		PersistenceManager sessionManager = null;
		try {
			sessionManager = PersistenceManager.getInstance();
			JdbcSession session = sessionManager.getJdbcSession();
			SQLParameter para = new SQLParameter();
			//
//			int num = getNumByChar(sql);
//			for(int i=0; num>0 && i<num; i++){
				para.addParam(curBillID);
//			}
			//
			ResultSetProcessor p = new ResultSetProcessor() {
				@SuppressWarnings("unchecked")
				public Object handleResultSet(ResultSet rs) throws SQLException {
					ArrayList al = new ArrayList();
					while (rs.next()) {
						String type = rs.getString(1);
						String id = rs.getString(2);
						if (type != null && id != null
								&& type.trim().length() > 0
								&& id.trim().length() > 0) {
							LightBillVO svo = new LightBillVO();
							svo.setType(type);
							svo.setID(id);
							al.add(svo);
						}
					}
					return al;
				}
			};
			ArrayList<LightBillVO> result = (ArrayList<LightBillVO>) session
					.executeQuery(sql, para, p);
			if (result.size() == 0)
				return null;
			// 增补上游单据号
			for (LightBillVO vo : result) {
				List<String> list = getBillCodeAndCorp(vo.getType(), vo.getID());
				if(list!=null && list.size()>0){
					vo.setCode(list.get(0));
					vo.setCorp(list.get(1));
				}
			}
			return (nc.vo.trade.billsource.LightBillVO[]) result
					.toArray(new nc.vo.trade.billsource.LightBillVO[result
							.size()]);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new UifRuntimeException(e.getMessage());
		} finally {
			sessionManager.release();
		}
	}

//	public int getNumByChar(String sql){
//		int num =  0;
//		if(sql != null && sql.length() > 0){
//			for(int i = 0 ; i < sql.length(); i++){
//				if("?".equals(String.valueOf(sql.charAt(i)))){
//					num++;
//				}
//			}
//		}
//		return num;
//	}
	
	public String getCurrentBillType() {
		return currentBillType;
	}
	
	public void setCurrentBillType(String currentBillType) {
		this.currentBillType = currentBillType;
	}
	
	public String getSrcBillType() {
		return srcBillType;
	}
	
	public void setSrcBillType(String srcBillType) {
		this.srcBillType = srcBillType;
	}
}
