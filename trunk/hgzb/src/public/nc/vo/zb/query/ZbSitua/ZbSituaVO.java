package nc.vo.zb.query.ZbSitua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.query.ZbNmny.ZbNmnyVO;

public class ZbSituaVO  extends SuperVO {
	
	private String cbiddingid;//标段
	private String ccustmanid;// 供应商管理ID
	private String ccustbasid;// 供应商基本ID
	private UFDouble ntotalgrad;//综合分
	private UFDouble nzbmny;//中标金额
	private UFDouble nprezbnmny;//分厂商中标额
	private String pk_deptdoc;//部门
	private Integer izbtype;//方式
	private UFDouble nwinpercent;//各厂商中标比例
	
	
	private String vdef1;
	private String vdef2;
	private String vdef3;
	private String vdef4;
	private String vdef5;
	private String vdef6;

	private UFDouble ndef1;
	private UFDouble ndef2;
	private UFDouble ndef3;
	private UFDouble ndef4;
	private UFDouble ndef5;
	private UFDouble ndef6;

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

	public String getCbiddingid() {
		return cbiddingid;
	}

	public void setCbiddingid(String cbiddingid) {
		this.cbiddingid = cbiddingid;
	}

	public String getCcustmanid() {
		return ccustmanid;
	}

	public void setCcustmanid(String ccustmanid) {
		this.ccustmanid = ccustmanid;
	}

	public String getCcustbasid() {
		return ccustbasid;
	}

	public void setCcustbasid(String ccustbasid) {
		this.ccustbasid = ccustbasid;
	}

	public UFDouble getNtotalgrad() {
		return ntotalgrad;
	}

	public void setNtotalgrad(UFDouble ntotalgrad) {
		this.ntotalgrad = ntotalgrad;
	}

	public UFDouble getNprezbnmny() {
		return nprezbnmny;
	}

	public void setNprezbnmny(UFDouble nprezbnmny) {
		this.nprezbnmny = nprezbnmny;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}


	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getVdef2() {
		return vdef2;
	}

	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}

	public String getVdef3() {
		return vdef3;
	}

	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}

	public String getVdef4() {
		return vdef4;
	}

	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}

	public String getVdef5() {
		return vdef5;
	}

	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}

	public String getVdef6() {
		return vdef6;
	}

	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}

	public UFDouble getNdef1() {
		return ndef1;
	}

	public void setNdef1(UFDouble ndef1) {
		this.ndef1 = ndef1;
	}

	public UFDouble getNdef2() {
		return ndef2;
	}

	public void setNdef2(UFDouble ndef2) {
		this.ndef2 = ndef2;
	}

	public UFDouble getNdef3() {
		return ndef3;
	}

	public void setNdef3(UFDouble ndef3) {
		this.ndef3 = ndef3;
	}

	public UFDouble getNdef4() {
		return ndef4;
	}

	public void setNdef4(UFDouble ndef4) {
		this.ndef4 = ndef4;
	}

	public UFDouble getNdef5() {
		return ndef5;
	}

	public void setNdef5(UFDouble ndef5) {
		this.ndef5 = ndef5;
	}

	public UFDouble getNdef6() {
		return ndef6;
	}

	public void setNdef6(UFDouble ndef6) {
		this.ndef6 = ndef6;
	}
	public static final String templet_data_ID1 = "0001A1100000000139FY";
	public static final String templet_query_ID = "0001A110000000013O9K";
	public static final String templet_modoulecode = "4004090703";

	public UFDouble getNzbmny() {
		return nzbmny;
	}

	public void setNzbmny(UFDouble nzbmny) {
		this.nzbmny = nzbmny;
	}

	public Integer getIzbtype() {
		return izbtype;
	}

	public void setIzbtype(Integer izbtype) {
		this.izbtype = izbtype;
	}
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）中标明细 2011-7-8下午06:04:54
	 */
	public static void sortZbSituaVO(ZbSituaVO[] vos) {
		if (vos == null || vos.length == 0) 
			return ;
		Set<String> ss = new HashSet<String>();
		
		Map<String,UFDouble> map =  new HashMap<String,UFDouble>();
		String key = null;
		for (ZbSituaVO vo : vos) {
			key = vo.getCbiddingid();
			if (map.containsKey(key)) {
				UFDouble nmny =PuPubVO.getUFDouble_NullAsZero(map.get(key)).add(PuPubVO.getUFDouble_NullAsZero(vo.getNprezbnmny()));
				map.remove(key);
				map.put(key,nmny);
			} else{
				map.put(key, PuPubVO.getUFDouble_NullAsZero(vo.getNprezbnmny()));
			}
		}
		
		for (ZbSituaVO vo : vos) {
			key = vo.getCbiddingid();
			UFDouble nzbnmny = map.get(key);
			if(!nzbnmny.equals(UFDouble.ZERO_DBL)){
				vo.setNwinpercent(PuPubVO.getUFDouble_NullAsZero(vo.getNprezbnmny()).div(nzbnmny).multiply(new UFDouble(100)));
			}
			if (ss.contains(key)) {
				String[] names = vo.getAttributeNames();
					for (String name : names) {
						if (name.equalsIgnoreCase("cbiddingid")){
							 vo.setAttributeValue(name, null);
						}  
					}
			} else{
				ss.add(key);
				vo.setNzbmny(nzbnmny);
			}
		}
	}

	public UFDouble getNwinpercent() {
		return nwinpercent;
	}

	public void setNwinpercent(UFDouble nwinpercent) {
		this.nwinpercent = nwinpercent;
	}

}
