package nc.vo.hg.ia.report;

import java.util.HashMap;

import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;

//查询  数量和金额  的公共 数据载体

public class InOutSumPubVO extends SuperVO {

	private String  pk_corp;
	private String  crdcenterid;// 库存组织标识    
	private String cwarehouseid;//仓库
	private String cinvbasid;//存货ID
	private String  cinventoryid;//  存货标识  
	private String  vbatch;//批次号
	
	private String cinvclid;
	
	
	private UFDouble nnum;//数量
	private UFDouble nmny;//金额
	
	public static UFBoolean isqrybatch = UFBoolean.FALSE;
	public static UFBoolean isware = UFBoolean.FALSE;
	public static UFBoolean isinvcl = UFBoolean.FALSE;//是否明细查询
	
	public static void setIsInvcl(UFBoolean flag){
		isinvcl = flag;
	}
	
	public static String MCJGCK = "1002A11000000000D8AR";//固定仓库  木材原料加工仓库
	public static String[] sort_fields = new String[]{"crdcenterid","cinvclid","cinvbasid"};
	public static String materialOut_lldw = "vuserdef5";//材料出库单表头领料单位 字段
	private static java.util.Map<String, InOutSumPubVO> dataInfor = null;
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）处理数据
	 * 2011-6-20上午09:38:19
	 * @param vos
	 * @return
	 */
    public static java.util.Map<String, InOutSumPubVO> dealData(InOutSumPubVO[] vos){
    	if(vos == null || vos.length == 0)
    		return null;
    	if(dataInfor == null)
    		dataInfor = new HashMap<String, InOutSumPubVO>();
    	else
    		dataInfor.clear();
    	StringBuffer key = null;
    	for(InOutSumPubVO vo:vos){
    		key = new StringBuffer();
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getPk_corp()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCrdcenterid()));
    		//    		if(isware.booleanValue())
//    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCwarehouseid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvclid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvbasid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinventoryid()));
    		//    		if(isqrybatch.booleanValue())
//    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getVbatch()));
    		dataInfor.put(key.toString(), vo);
    	}
    	return dataInfor;
    }
    
    public static String dealCons(ConditionVO[] cons){
    	if(cons == null || cons.length ==0)
			return null;
//		return new ConditionVO().getWhereSQL(cons);
		StringBuffer str = new StringBuffer();
		String value = null;
		for(ConditionVO con:cons){
			value = PuPubVO.getString_TrimZeroLenAsNull(con.getValue());
			if(value == null)
				continue;
			if(con.getFieldCode().equalsIgnoreCase("accountdate")){
				continue;
			}else if(con.getFieldCode().equalsIgnoreCase("invcl")){	
				if(str.length()>0)
					str.append("and");
				str.append(" substr(inv.invcode,0,2) = '"+value+"'");
			}else if(con.getFieldCode().equalsIgnoreCase("inv")){	
				if(str.length()>0)
					str.append("and");
				str.append("  inv.pk_invbasdoc = '"+value+"'");
			}else if(con.getFieldCode().equalsIgnoreCase("pk_calbody")){//--------该查询条件废弃 如启用 发票不支持
				if(str.length()>0)
					str.append("and");
				str.append(" h.pk_calbody = '"+value+"'");
			}
		}
		
		return str.toString();
    }
    public String getCinvclid() {
		return cinvclid;
	}

	public void setCinvclid(String cinvclid) {
		this.cinvclid = cinvclid;
	}
	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCrdcenterid() {
		return crdcenterid;
	}

	public void setCrdcenterid(String crdcenterid) {
		this.crdcenterid = crdcenterid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getVbatch() {
		return vbatch;
	}

	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
