package nc.bs.zb.pub;

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
 * 招标单据Finder
 * @author Administrator
 */
public class BidDataFinder extends DefaultDataFinder{
	
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
//		if(SrmBillStatus.SRM1.equals(getCurrentBillType())){//当前单据为销售返还政策 <加这个判断是因为，销售返还政策>
		//下游单据为
//		if(HgPubConst.PLAN_MONTH_BILLTYPE.equals(billType)){//月计划
//			sql = "  select distinct h.pk_plan,h.pk_corp,h.vbillno from hg_plan h inner join hg_planother_b b " +
//					" on h.pk_plan = b.pk_plan where b.cnextbillid = ?  " +
//					" and h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"' and isnull(h.dr,0)  = 0  and isnull(b.dr,0)  = 0; ";
//			return sql;
//		}
//		else if(SrmBillStatus.SRM8.equals(billType)){//销售返点单
//			sql = "  select distinct pk_xsfhd_h,pk_corp,vbillno from srm_xsfhd_h where cfhzcid = ? and pk_billtype = '"+SrmBillStatus.SRM8+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}
//		else if(SrmBillStatus.SRMC.equals(billType)){//销售返利额度
//			sql = "  select distinct pk_xsfhd_h,pk_corp,vbillno from srm_xsfhd_h where cfhzcid = ? and pk_billtype = '"+SrmBillStatus.SRMC+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM9.equals(billType)){//返点台帐
//			sql = "  select  distinct h.pk_fhtz_h, h.pk_corp, h.vbillno from srm_fhtz_h h,srm_fhtz_b1 b  where h.pk_fhtz_h=b.pk_fhtz_h and b.pk_fhd_h= ? and h.pk_billtype = '"+SrmBillStatus.SRM9+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRMD.equals(billType)){//返利额度台帐
//			sql = "  select  distinct h.pk_fhtz_h, h.pk_corp, h.vbillno from srm_fhtz_h h,srm_fhtz_b1 b  where h.pk_fhtz_h=b.pk_fhtz_h and b.pk_fhd_h= ? and h.pk_billtype = '"+SrmBillStatus.SRMD+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM3.equals(billType)){//返还台帐
//			sql = "  select  distinct h.pk_fhtz_h, h.pk_corp, h.vbillno from srm_fhtz_h h,srm_fhtz_b1 b  where h.pk_fhtz_h=b.pk_fhtz_h and b.pk_fhd_h= ? and h.pk_billtype = '"+SrmBillStatus.SRM3+"' and isnull(h.dr,0)  = 0 and isnull(b.dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM4.equals(billType)){//转赠品
//			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from srm_sxfhzzp_h where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRM4+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM5.equals(billType)){//冲应收
//			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from srm_sxfhzzp_h where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRM5+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM6.equals(billType)){//转应付
//			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from srm_sxfhzzp_h where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRM6+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRM7.equals(billType)){//冲发票
//			sql = "  select distinct pk_sxfhzzp_h,pk_corp,vusedcode from srm_sxfhzzp_h where pk_sxfhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRM7+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRMB.equals(billType)){//返点使用申请
//			sql = "  select distinct pk_fdsysq_h,pk_corp,vbillno from srm_fdsysq_h where pk_fhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRMB+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}else if(SrmBillStatus.SRME.equals(billType)){//返利额度使用申请
//			sql = "  select distinct pk_fledsysq_h,pk_corp,vbillno from srm_fledsysq_h where pk_fhtz_h = ? and pk_billtype = '"+SrmBillStatus.SRME+"' and isnull(dr,0)  = 0 ; ";
//			return sql;
//		}
//		}
		return super.createSQL1(billType);
	}
	//来源单据注册类
	@Override
	protected String createSQL(String billType) {//这个billtype参数为当前单据类型
		//正常作法为：根据当前单据查询出 来源单据类型、来源单据ID即可
		String sql = null;
		
//		if(HgPubConst.PLAN_MONTH_BILLTYPE.equalsIgnoreCase(billType)){
//			sql = " select distinct '"+HgPubConst.PLAN_YEAR_BILLTYPE+"', b.cnextbillid from hg_plan h inner join hg_planother_b b " +
//					" on  h.pk_plan = b.pk_plan where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
//					" and h.pk_billtype = '"+HgPubConst.PLAN_MONTH_BILLTYPE+"' and h.pk_plan = ?";
//			return sql;
//		}else if(HgPubConst.PLAN_YEAR_BILLTYPE.equalsIgnoreCase(billType)){
//			sql = " select distinct '"+HgPubConst.PLAN_YEAR_BILLTYPE+"', b.pk_plan,h.pk_corp from hg_plan h inner join hg_planyear_b b " +
//			" on  h.pk_plan = b.pk_plan where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
//			" and h.pk_billtype = '"+HgPubConst.PLAN_YEAR_BILLTYPE+"' and b.cnextbillid = ?";
//			return sql;
//		}else if(HgPubConst.PLAN_TEMP_BILLTYPE.equalsIgnoreCase(billType)){
//			sql = " select distinct '"+HgPubConst.PLAN_TEMP_BILLTYPE+"', b.pk_plan,h.pk_corp from hg_plan h inner join hg_planother_b b " +
//			" on  h.pk_plan = b.pk_plan where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
//			" and h.pk_billtype = '"+HgPubConst.PLAN_TEMP_BILLTYPE+"' and b.cnextbillid = ?";
//			return sql;
//		}else if(HgPubConst.PLAN_MNY_BILLTYPE.equalsIgnoreCase(billType)){
//			sql = " select distinct '"+HgPubConst.PLAN_MNY_BILLTYPE+"', b.pk_plan,h.pk_corp from hg_plan h inner join hg_planother_b b " +
//			" on  h.pk_plan = b.pk_plan where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 " +
////			" and h.pk_billtype = '"+HgPubConst.PLAN_MNY_BILLTYPE+"' and b.cnextbillid = ?";
//			return sql;
//		}
		
		
		
//		if(SrmBillStatus.SRM2.equals(billType) ||//销售返还单 (来源为销售返还政策)
//				SrmBillStatus.SRM8.equals(billType) ||//销售返点单 (来源为销售返还政策)
//				SrmBillStatus.SRMC.equals(billType)){//销售返利额度 (来源为销售返还政策)
//			sql = " select distinct 'SRM1' ,cfhzcid from srm_xsfhd_h where isnull(dr,0)= 0  and pk_xsfhd_h = ? ";
//			return sql;
//		}else if(SrmBillStatus.SRM3.equals(billType)){//销售返还台账 (来源为返还单,返点使用申请，返利额度使用申请)
//				sql = " select pk_billtype , pk_fhd_h from srm_fhtz_b1 where isnull(dr,0)= 0  and pk_fhtz_h = ? ";
//			return sql;
//		}else if(SrmBillStatus.SRM9.equals(billType)){//销售返点台帐 (来源为返点单)
//			sql = " select distinct 'SRM8' , pk_fhd_h from srm_fhtz_b1 where isnull(dr,0)= 0  and pk_fhtz_h = ? ";
//			return sql;	
//		}else if(SrmBillStatus.SRMD.equals(billType)){//销售返利额度台帐 (来源为返利额度单)
//			sql = " select distinct 'SRMC' , pk_fhd_h from srm_fhtz_b1 where isnull(dr,0)= 0  and pk_fhtz_h = ? ";
//			return sql;	
//		}else if(SrmBillStatus.SRMB.equals(billType)){//返点使用申请  (来源为返点台账)
//			sql = " select distinct 'SRM9',pk_fhtz_h from srm_fdsysq_h where isnull(dr,0) = 0 and pk_fdsysq_h  =  ? ";
//			return sql;
//		}else if(SrmBillStatus.SRME.equals(billType)){//返利额度使用申请 (来源为返利额度台账)
//			sql = " select distinct 'SRMD',pk_fhtz_h from srm_fledsysq_h where isnull(dr,0) = 0 and pk_fledsysq_h = ? ";
//			return sql;
//		}else if(SrmBillStatus.SRM4.equals(billType) ||//销售返还转赠品 (来源为返还台账)
//				SrmBillStatus.SRM5.equals(billType) ||//销售返还冲应收 (来源为返还台账)
//				SrmBillStatus.SRM6.equals(billType) ||//销售返还冲发票 (来源为返还台账)
//				SrmBillStatus.SRM7.equals(billType)){//销售返还转应付 (来源为返还台账)
//			sql = " select distinct 'SRM3',pk_sxfhtz_h from srm_sxfhzzp_h where isnull(dr,0) = 0 and  pk_sxfhzzp_h = ? ";
//			return sql;
//		}
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
