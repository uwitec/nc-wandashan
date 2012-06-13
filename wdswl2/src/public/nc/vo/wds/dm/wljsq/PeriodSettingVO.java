package nc.vo.wds.dm.wljsq;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class PeriodSettingVO extends SuperVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Integer datavale;//  ����ֵ
	public String pk_corp;//��˾����
    public UFDate dmakedate;//�Ƶ�����
    public String voperatorid;//�Ƶ���
    public Integer dr;
    public UFDateTime ts;
    public String pk_periodsetting;  //�������� 
    
	public Integer getDatavale() {
		return datavale;
	}

	public void setDatavale(Integer datavale) {
		this.datavale = datavale;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public UFDate getDmakedate() {
		return dmakedate;
	}

	public void setDmakedate(UFDate dmakedate) {
		this.dmakedate = dmakedate;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_periodsetting";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_periodsetting_h";
	}
	/**
	 * ������ֵ�������ʾ����.
	 * 
	 * ��������:2010-7-16
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "wds_periodsetting_h";

	}
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return pk_periodsetting;
	}
	
	@Override
	public void setPrimaryKey(String newpk_periodsetting) {
		// TODO Auto-generated method stub
		pk_periodsetting=newpk_periodsetting;
	}

}
