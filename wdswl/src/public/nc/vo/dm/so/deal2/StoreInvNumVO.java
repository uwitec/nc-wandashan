package nc.vo.dm.so.deal2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * @author zhf   仓库存货存量vo
 *
 */

public class StoreInvNumVO extends SuperVO{
	private String pk_corp;//公司
	private String ccalbodyid;//库存组织   暂时不需要
	private String cstoreid;//仓库
	private String ccargdocid;//货位 //暂时不需要
	private String cinvmanid;
	private String cinvbasid;
	
	private UFDouble nnum;//可用量
	private UFDouble nassnum;//可用量辅数量
	
	private UFDouble nstocknum;//现存量
	private UFDouble nstockassnum;//现存量辅数量
	private UFDouble ndealnum;//已安排未出库量
	private UFDouble ndealassnum;
	
	private UFDouble nplannum;//本次计划按排量
	private UFDouble nplanassnum;
	
	private UFBoolean bisok;//是否可发货  存量是否够
	
	private Set<SoDealVO> ldeal = null;
	
	public Set<SoDealVO> getLdeal() {
		if(ldeal == null){
			ldeal = new TreeSet<SoDealVO>(new DateComparator());
		}
		return ldeal;
	}

	public void setLdeal(Set<SoDealVO> ldeal) {
		this.ldeal = ldeal;
	}

	public UFBoolean getBisok() {
		return bisok;
	}

	public void setBisok(UFBoolean bisok) {
		this.bisok = bisok;
	}

	public UFDouble getNstocknum() {
		return nstocknum;
	}

	public void setNstocknum(UFDouble nstocknum) {
		this.nstocknum = nstocknum;
	}

	public UFDouble getNstockassnum() {
		return nstockassnum;
	}

	public void setNstockassnum(UFDouble nstockassnum) {
		this.nstockassnum = nstockassnum;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCcalbodyid() {
		return ccalbodyid;
	}

	public void setCcalbodyid(String ccalbodyid) {
		this.ccalbodyid = ccalbodyid;
	}

	public String getCstoreid() {
		return cstoreid;
	}

	public void setCstoreid(String cstoreid) {
		this.cstoreid = cstoreid;
	}

	public String getCcargdocid() {
		return ccargdocid;
	}

	public void setCcargdocid(String ccargdocid) {
		this.ccargdocid = ccargdocid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNassnum() {
		return nassnum;
	}

	public void setNassnum(UFDouble nassnum) {
		this.nassnum = nassnum;
	}

	public UFDouble getNdealnum() {
		return ndealnum;
	}

	public void setNdealnum(UFDouble ndealnum) {
		this.ndealnum = ndealnum;
	}

	public UFDouble getNdealassnum() {
		return ndealassnum;
	}

	public void setNdealassnum(UFDouble ndealassnum) {
		this.ndealassnum = ndealassnum;
	}

	public UFDouble getNplannum() {
		return nplannum;
	}

	public void setNplannum(UFDouble nplannum) {
		this.nplannum = nplannum;
	}

	public UFDouble getNplanassnum() {
		return nplanassnum;
	}

	public void setNplanassnum(UFDouble nplanassnum) {
		this.nplanassnum = nplanassnum;
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
