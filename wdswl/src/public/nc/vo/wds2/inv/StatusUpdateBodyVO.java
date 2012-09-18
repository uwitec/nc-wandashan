package nc.vo.wds2.inv;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class StatusUpdateBodyVO extends SuperVO {
	
	public String cbillid;
	public String cbill_bid;
	public String cinvbasid;
	public String cinvmanid;
	public String vbatchcode;
	public String cinvstatusid;//库存状态档案id
	public String cinvstatusid2;//库存状态档案id
	public UFDouble nnum = null;
	public UFDouble nassnum = null;
	public String cunitid;//主计量单位
	public String cassunitid;//辅计量单位
	
	
	public String crowno;//行号
	public Integer irowstatus;//行状态	
	public Integer idatasource;//0 为自制   1为上报 
	public Integer ieditcount;//修订次数
    public Integer getIdatasource() {
		return idatasource;
	}
	public void setIdatasource(Integer idatasource) {
		this.idatasource = idatasource;
	}
	public Integer getIeditcount() {
		return ieditcount;
	}
	public void setIeditcount(Integer ieditcount) {
		this.ieditcount = ieditcount;
	}
    //严重声明 ：这几个字段 不允许随便改
	//如果乱改  ui工厂不支持的
	//尤其在 参照修改  容易导致数据并发的情况下  ui工厂只对vlastbillid 加来源锁
	//如果记录上游的id的字段不是vlastbillid 
	//那么会导致 对上游数据的加锁失败 容易导致并发造成数据错误
	//记录来源 和 源头数据的字段  开始
	public String  vlastbilltype;//来源单据类型    记录流程 上游核心单据用的    
    public String  vlastbillid;//来源单据ID
    public String  vlastbillrowid;//来源单据RowID
    public String  vsourcebilltype;//源头单据类型      
    public String  vsourcebillid;//源头单据ID
    public String  vsourcebillrowid;//源头单据RowID	
	//记录来源 和 源头数据的字段  结束
    public String vmemo;//备注
	public String vdef1;//---------------生产日期
	public String vdef2;
	public String vdef3;
	public String vdef4;
	public String vdef5;//货架id
	public String vdef6;
	public String vdef7;
	public String vdef8;
	public String vdef9;
	public String vdef10;
	
	public String pk_defdoc1;
	public String pk_defdoc2;
	public String pk_defdoc3;
	public String pk_defdoc4;
	public String pk_defdoc5;
	
	public String vreserve1;
	public String vreserve2;
	public String vreserve3;
	
	
	public UFDouble nreserve1;
	public UFDouble nreserve2;
	public UFDouble nreserve3;
	public UFDouble nreserve4;
	public UFDouble nreserve5;
	
	public UFBoolean ureserve1;
	public UFBoolean ureserve2;
	public UFBoolean ureserve3;
	
	public UFDateTime ts;		
	
	public Integer dr;
	public static String[] split_keys = new String[]{"cinvmanid","vbatchcode","cinvstatusid","cinvstatusid2"};
	
   
	public String getVdef1() {
		return vdef1;
	}

	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}

	public String getCbillid() {
		return cbillid;
	}

	public void setCbillid(String cbillid) {
		this.cbillid = cbillid;
	}

	public String getCbill_bid() {
		return cbill_bid;
	}

	public void setCbill_bid(String cbill_bid) {
		this.cbill_bid = cbill_bid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinvmanid() {
		return cinvmanid;
	}

	public void setCinvmanid(String cinvmanid) {
		this.cinvmanid = cinvmanid;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}

	public String getCinvstatusid() {
		return cinvstatusid;
	}

	public void setCinvstatusid(String cinvstatusid) {
		this.cinvstatusid = cinvstatusid;
	}

	public String getCinvstatusid2() {
		return cinvstatusid2;
	}

	public void setCinvstatusid2(String cinvstatusid2) {
		this.cinvstatusid2 = cinvstatusid2;
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

	public String getCunitid() {
		return cunitid;
	}

	public void setCunitid(String cunitid) {
		this.cunitid = cunitid;
	}

	public String getCassunitid() {
		return cassunitid;
	}

	public void setCassunitid(String cassunitid) {
		this.cassunitid = cassunitid;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "cbill_bid";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "cbillid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds2_statusupdate_b";
	}
	public String getCrowno() {
		return crowno;
	}
	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}
	public Integer getIrowstatus() {
		return irowstatus;
	}
	public void setIrowstatus(Integer irowstatus) {
		this.irowstatus = irowstatus;
	}
	public String getVlastbilltype() {
		return vlastbilltype;
	}
	public void setVlastbilltype(String vlastbilltype) {
		this.vlastbilltype = vlastbilltype;
	}
	public String getVlastbillid() {
		return vlastbillid;
	}
	public void setVlastbillid(String vlastbillid) {
		this.vlastbillid = vlastbillid;
	}
	public String getVlastbillrowid() {
		return vlastbillrowid;
	}
	public void setVlastbillrowid(String vlastbillrowid) {
		this.vlastbillrowid = vlastbillrowid;
	}
	public String getVsourcebilltype() {
		return vsourcebilltype;
	}
	public void setVsourcebilltype(String vsourcebilltype) {
		this.vsourcebilltype = vsourcebilltype;
	}
	public String getVsourcebillid() {
		return vsourcebillid;
	}
	public void setVsourcebillid(String vsourcebillid) {
		this.vsourcebillid = vsourcebillid;
	}
	public String getVsourcebillrowid() {
		return vsourcebillrowid;
	}
	public void setVsourcebillrowid(String vsourcebillrowid) {
		this.vsourcebillrowid = vsourcebillrowid;
	}
	public String getVmemo() {
		return vmemo;
	}
	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
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
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getPk_defdoc1() {
		return pk_defdoc1;
	}
	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}
	public String getPk_defdoc2() {
		return pk_defdoc2;
	}
	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}
	public String getPk_defdoc3() {
		return pk_defdoc3;
	}
	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}
	public String getPk_defdoc4() {
		return pk_defdoc4;
	}
	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}
	public String getPk_defdoc5() {
		return pk_defdoc5;
	}
	public void setPk_defdoc5(String pk_defdoc5) {
		this.pk_defdoc5 = pk_defdoc5;
	}
	public String getVreserve1() {
		return vreserve1;
	}
	public void setVreserve1(String vreserve1) {
		this.vreserve1 = vreserve1;
	}
	public String getVreserve2() {
		return vreserve2;
	}
	public void setVreserve2(String vreserve2) {
		this.vreserve2 = vreserve2;
	}
	public String getVreserve3() {
		return vreserve3;
	}
	public void setVreserve3(String vreserve3) {
		this.vreserve3 = vreserve3;
	}
	public UFDouble getNreserve1() {
		return nreserve1;
	}
	public void setNreserve1(UFDouble nreserve1) {
		this.nreserve1 = nreserve1;
	}
	public UFDouble getNreserve2() {
		return nreserve2;
	}
	public void setNreserve2(UFDouble nreserve2) {
		this.nreserve2 = nreserve2;
	}
	public UFDouble getNreserve3() {
		return nreserve3;
	}
	public void setNreserve3(UFDouble nreserve3) {
		this.nreserve3 = nreserve3;
	}
	public UFDouble getNreserve4() {
		return nreserve4;
	}
	public void setNreserve4(UFDouble nreserve4) {
		this.nreserve4 = nreserve4;
	}
	public UFDouble getNreserve5() {
		return nreserve5;
	}
	public void setNreserve5(UFDouble nreserve5) {
		this.nreserve5 = nreserve5;
	}
	public UFBoolean getUreserve1() {
		return ureserve1;
	}
	public void setUreserve1(UFBoolean ureserve1) {
		this.ureserve1 = ureserve1;
	}
	public UFBoolean getUreserve2() {
		return ureserve2;
	}
	public void setUreserve2(UFBoolean ureserve2) {
		this.ureserve2 = ureserve2;
	}
	public UFBoolean getUreserve3() {
		return ureserve3;
	}
	public void setUreserve3(UFBoolean ureserve3) {
		this.ureserve3 = ureserve3;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
}
