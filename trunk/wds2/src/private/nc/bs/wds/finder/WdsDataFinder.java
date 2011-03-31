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
		//下游单据为
		if(SrmBillStatus.SDM2.equals(billType)){//销售提成单
			sql = "  select distinct pk_xsfhd_h,pk_corp,vbillno from SDM_TCD_H where cfhzcid = ?  and pk_billtype = '"+SrmBillStatus.SDM2+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM8.equals(billType)){//销售计提单
			sql = "  select distinct pk_xsfhd_h,pk_corp,vbillno from SDM_TCD_H where cfhzcid = ? and pk_billtype = '"+SrmBillStatus.SDM8+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM9.equals(billType)){//销售计提台帐
			sql = "  select  distinct h.pk_fhtz_h, h.pk_corp, h.vbillno from SDM_TCTZ_H h,sdm_tctz_b1 b  where h.pk_fhtz_h=b.pk_fhtz_h and b.pk_fhd_h= ? and h.pk_billtype = '"+SrmBillStatus.SDM9+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM3.equals(billType)){//提成台帐
			sql = "  select  distinct h.pk_fhtz_h, h.pk_corp, h.vbillno from SDM_TCTZ_H h,sdm_tctz_b1 b  where h.pk_fhtz_h=b.pk_fhtz_h and b.pk_fhd_h= ? and h.pk_billtype = '"+SrmBillStatus.SDM3+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM4.equals(billType)){//转赠品
			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from SDM_TCSYFS_H where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SDM4+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM5.equals(billType)){//冲应收
			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from SDM_TCSYFS_H where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SDM5+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM6.equals(billType)){//冲发票
			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from SDM_TCSYFS_H where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SDM6+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDM7.equals(billType)){//转应付
			sql = "  select distinct h.pk_sxfhzzp_h,h.pk_corp,h.vbillno from SDM_TCSYFS_H h,SDM_TCSYFS_B b where h.pk_sxfhzzp_h=b.pk_sxfhzzp_h and b.csourcebillhid = ? and  h.pk_billtype = '"+SrmBillStatus.SDM7+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
			return sql;
		}else if(SrmBillStatus.SDMA.equals(billType)){//计提使用申请
			sql = "  select distinct pk_fledsysq_h,pk_corp,vbillno from SDM_JTSYSQ_H where pk_fhtz_h = ? and pk_billtype = '"+SrmBillStatus.SDMA+"' and isnull(dr,0)  = 0 ; ";
			return sql;
		}
//		}
		return super.createSQL1(billType);
	}
	//来源单据注册类
	@Override
	protected String createSQL(String billType) {//这个billtype参数为当前单据类型
		//正常作法为：根据当前单据查询出 来源单据类型、来源单据ID即可
		String sql = null;
		if(SrmBillStatus.SDM2.equals(billType) ||//销售提成单 (来源为销售返还政策)
				SrmBillStatus.SDM8.equals(billType)){//销售计提单 (来源为销售返还政策)
			sql = " select distinct 'SDM1' ,cfhzcid from SDM_TCD_H where isnull(dr,0)= 0  and pk_xsfhd_h = ? ";
			return sql;
		}else if(SrmBillStatus.SDM3.equals(billType)){//销售提成台账 (来源为提成单,计提使用申请)
				sql = " select pk_billtype , pk_fhd_h from SDM_TCTZ_B1 where isnull(dr,0)= 0  and pk_fhtz_h = ? ";
			return sql;
		}else if(SrmBillStatus.SDM9.equals(billType)){//销售计提台帐 (来源为计提单)
			sql = " select distinct 'SDM8' , pk_fhd_h from SDM_TCTZ_B1 where isnull(dr,0)= 0  and pk_fhtz_h = ? ";
			return sql;	
		}else if(SrmBillStatus.SDMA.equals(billType)){//销售使用申请 (来源为计提台账)
			sql = " select distinct 'SDM9',pk_fhtz_h from SDM_JTSYSQ_H where isnull(dr,0) = 0 and pk_fledsysq_h = ? ";
			return sql;
		}else if(SrmBillStatus.SDM4.equals(billType) ||//销售提成转赠品 (来源为提成台账)
				SrmBillStatus.SDM5.equals(billType) ||//销售提成冲应收 (来源为提成台账)
				SrmBillStatus.SDM6.equals(billType)
				){//销售提成转应付 (来源为提成台账)
			sql = " select distinct 'SDM3',pk_sxfhtz_h from SDM_TCSYFS_H where isnull(dr,0) = 0 and  pk_sxfhzzp_h = ? ";
			return sql;
		}else if(SrmBillStatus.SDM7.equals(billType)){ //销售提成冲发票 (来源为提成台账)
			sql=" select distinct 'SDM3',csourcebillhid from SDM_TCSYFS_B where isnull(dr,0) = 0 and  pk_sxfhzzp_h = ?  ";
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
