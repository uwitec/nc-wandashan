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
 * @author zhf   �ֿ�������vo
 *
 */

public class StoreInvNumVO extends SuperVO{
	private String pk_corp;//��˾
	private String ccalbodyid;//�����֯   ��ʱ����Ҫ
	private String cstoreid;//�ֿ�
	private String ccargdocid;//��λ //��ʱ����Ҫ
	private String cinvmanid;
	private String cinvbasid;
	
	private UFDouble nnum;//������
	private UFDouble nassnum;//������������
	
	private UFDouble nstocknum;//�ִ���
	private UFDouble nstockassnum;//�ִ���������
	private UFDouble ndealnum;//�Ѱ���δ������
	private UFDouble ndealassnum;
	
	private UFDouble nplannum;//���μƻ�������
	private UFDouble nplanassnum;
	
	private UFBoolean bisok;//�Ƿ�ɷ���  �����Ƿ�
	
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
