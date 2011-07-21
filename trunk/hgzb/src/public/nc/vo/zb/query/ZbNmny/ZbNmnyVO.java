package nc.vo.zb.query.ZbNmny;

import java.util.HashSet;
import java.util.Set;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public class ZbNmnyVO extends SuperVO {

	private String cunitid;// 计量档案
	private String vmemo;// 备注
	private String cinvbasid;// 存货基本档案ID
	private UFDouble nzbprice;// 中标价格
	private String cinvmanid;// 存货管理档案ID
	private String pk_deptdoc;// 招标部门
	private String ccustmanid;// 供应商管理ID
	private UFDouble nzbnum;// 中标数量
	private String ccustbasid;// 供应商基本ID
	private UFDouble nzbmny;// 中标金额
	private String invcode;//存货编码用于排序

	private UFDouble reserve10;
	private UFBoolean reserve15;
	private String reserve2;
	private UFDouble reserve8;
	private UFDouble reserve7;
	private UFDate reserve11;
	private UFDouble reserve9;
	private String reserve5;
	private UFDate reserve12;
	private UFDouble reserve6;
	private String reserve1;
	private UFBoolean reserve16;
	private UFBoolean reserve14;
	private UFDate reserve13;
	private String reserve3;
	private String reserve4;

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

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getVmemo() {
		return vmemo;
	}

	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public UFDouble getNzbprice() {
		return nzbprice;
	}

	public void setNzbprice(UFDouble nzbprice) {
		this.nzbprice = nzbprice;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	public void setPk_deptdoc(String pk_deptdoc) {
		this.pk_deptdoc = pk_deptdoc;
	}

	public String getCcustmanid() {
		return ccustmanid;
	}

	public void setCcustmanid(String ccustmanid) {
		this.ccustmanid = ccustmanid;
	}

	public UFDouble getNzbnum() {
		return nzbnum;
	}

	public void setNzbnum(UFDouble nzbnum) {
		this.nzbnum = nzbnum;
	}

	public String getCcustbasid() {
		return ccustbasid;
	}

	public void setCcustbasid(String ccustbasid) {
		this.ccustbasid = ccustbasid;
	}

	public UFDouble getNzbmny() {
		return nzbmny;
	}

	public void setNzbmny(UFDouble nzbmny) {
		this.nzbmny = nzbmny;
	}

	public UFDouble getReserve10() {
		return reserve10;
	}

	public void setReserve10(UFDouble reserve10) {
		this.reserve10 = reserve10;
	}

	public UFBoolean getReserve15() {
		return reserve15;
	}

	public void setReserve15(UFBoolean reserve15) {
		this.reserve15 = reserve15;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public UFDouble getReserve8() {
		return reserve8;
	}

	public void setReserve8(UFDouble reserve8) {
		this.reserve8 = reserve8;
	}

	public UFDouble getReserve7() {
		return reserve7;
	}

	public void setReserve7(UFDouble reserve7) {
		this.reserve7 = reserve7;
	}

	public UFDate getReserve11() {
		return reserve11;
	}

	public void setReserve11(UFDate reserve11) {
		this.reserve11 = reserve11;
	}

	public UFDouble getReserve9() {
		return reserve9;
	}

	public void setReserve9(UFDouble reserve9) {
		this.reserve9 = reserve9;
	}

	public String getReserve5() {
		return reserve5;
	}

	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}

	public UFDate getReserve12() {
		return reserve12;
	}

	public void setReserve12(UFDate reserve12) {
		this.reserve12 = reserve12;
	}

	public UFDouble getReserve6() {
		return reserve6;
	}

	public void setReserve6(UFDouble reserve6) {
		this.reserve6 = reserve6;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public UFBoolean getReserve16() {
		return reserve16;
	}

	public void setReserve16(UFBoolean reserve16) {
		this.reserve16 = reserve16;
	}

	public UFBoolean getReserve14() {
		return reserve14;
	}

	public void setReserve14(UFBoolean reserve14) {
		this.reserve14 = reserve14;
	}

	public UFDate getReserve13() {
		return reserve13;
	}

	public void setReserve13(UFDate reserve13) {
		this.reserve13 = reserve13;
	}

	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getReserve4() {
		return reserve4;
	}

	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}

	public static final String templet_data_ID1 = "0001A1100000000138U7";
	public static final String templet_data_ID2 = "0001A1100000000138ZU";
	public static final String templet_data_ID3 = "0001A11000000001390W";
	public static final String templet_query_ID = "0001A110000000013920";
	public static final String templet_modoulecode = "4004090701";

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）中标明细 2011-7-8下午06:04:54
	 */
	public static void sortZbNmnyVO(ZbNmnyVO[] vos) {
		if (vos == null || vos.length == 0) 
			return ;
	//	VOUtil.sort(vos, vendor_sort,vendor_sort_rule,true);
		Set<String> ss = new HashSet<String>();
		Set<String> sss = new HashSet<String>();
		String key = null;
		String keys = null;
		for (ZbNmnyVO vo : vos) {
			key = vo.getPk_deptdoc();
			keys = vo.getCcustmanid();
			if (ss.contains(key)) {
				String[] names = vo.getAttributeNames();
				if(sss.contains(keys)){
					for (String name : names) {
						if (name.equalsIgnoreCase("pk_deptdoc")||name.equalsIgnoreCase("ccustmanid")||name.equalsIgnoreCase("ccustbasid"))
							 vo.setAttributeValue(name, null);
						  
					}
				}else{
					for (String name : names) {
						if (name.equalsIgnoreCase("pk_deptdoc")){
							 vo.setAttributeValue(name, null);
						}  
					}
					sss.add(keys);
				}
				
			} else{
				ss.add(key);
				sss.clear();
				sss.add(keys);
			}
		}
		
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}
	
//	public static String[] vendor_sort = new String[]{"invcode",};
//	public static int[]  vendor_sort_rule = new int[]{VOUtil.ASC};
}
