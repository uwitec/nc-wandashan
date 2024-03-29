/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.hg.pu.pub;
	
import java.util.ArrayList;
import java.util.List;

import nc.ui.pu.pub.PuTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
	
/**
 * <b> 在此处简要描述此类的功能 </b>
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 * 创建日期:2011-11-01 10:37:18
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
public class PlanBVO extends SuperVO {
	private String cinventoryid;//存货管理
	private String pk_invbasdoc;//存货基本
	private String pk_measdoc;//主计量
	private String castunitid;//辅计量
	private UFDouble hsl;//换算率
	private String creqcalbodyid;//需求组织
	private String creqwarehouseid;//需求仓库
	private UFDate dreqdate;//到货日期
	private String csupplycalbodyid;//供货组织	
	private String csupplywarehouseid;//供货仓库
	private UFBoolean bisuse; //是否使用临时物资

	private String vbatchcode;	

	private UFDouble nprice;
	private UFDouble nmny;

	private String cnextbillbid;
	
	private UFDouble nsafestocknum;//安全库存
	private UFDouble nnum;//毛需求
	private UFDouble nassistnum;//毛需求辅数量
	private UFDouble nonhandnum;//截止N结账的库存量
	private UFDouble nallusenum;//上年度消耗
	private UFDouble nnonum;	//库存不可用
	private UFDouble nnetnum;   //净需求
	private UFDouble nreoutnum; //预月预耗
	private UFDouble nreinnum;  //剩余资源
	private UFDouble nusenum;  //1-N月消耗
	private Integer irowstatus;
	private UFBoolean fixedflag; //是否固定换算率 	
	

	private String vdef10;
	private String crowno;	
	private String pk_defdoc8;
	private String pk_defdoc5;	
	private String vreserve1;	//用作新物资申请id
	private String vdef7;
	private String pk_defdoc7;	
	private String pk_plan;
	private UFDouble nreserve8;	
	private String vdef2;
	private String vdef5;
	private String vmemo;
	private UFDateTime ts;
	private String vfree2;
	private String vdef9;
	private String pk_defdoc10;	
	private String pk_defdoc6;	
	private String vdef4;
	private String vreserve5;//平衡日期
	private UFDouble nreserve9;
	private String pk_defdoc2;
	private String vdef1;
	private String vdef8;
	private String vfree4;	
	private UFDouble nreserve7;
	private String pk_defdoc3;	
	private String vreserve4;//平衡人
	private String cnextbillid;
	private String vfree5;	
	private String vreserve2;//审批完成标志
	private String vfree3;	
	private String vreserve3;//平衡后  上个月不在领用
	private String vfree1;
	private String pk_defdoc9;	
	private UFDouble nreserve10;//平衡数量	
	private String pk_defdoc4;
	private String vdef3;
	private String vdef6;	
	private Integer dr;
	private UFDouble nreserve6;	
	private String pk_defdoc1;
	
	private List<String> lsourceid = null;//记录汇总行来源计划体id

	public List<String> getLsourceid(){
		if(lsourceid == null)
			lsourceid = new ArrayList<String>();
		return lsourceid;
	}
	public void setLsourceid(List<String> lsourceid) {
		this.lsourceid = lsourceid;
	}
	public String getCinventoryid() {
		return cinventoryid;
	}



	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}



	public String getPk_invbasdoc() {
		return pk_invbasdoc;
	}



	public void setPk_invbasdoc(String pk_invbasdoc) {
		this.pk_invbasdoc = pk_invbasdoc;
	}



	public String getPk_measdoc() {
		return pk_measdoc;
	}



	public void setPk_measdoc(String pk_measdoc) {
		this.pk_measdoc = pk_measdoc;
	}



	public String getCastunitid() {
		return castunitid;
	}



	public void setCastunitid(String castunitid) {
		this.castunitid = castunitid;
	}



	public UFDouble getHsl() {
		return hsl;
	}



	public void setHsl(UFDouble hsl) {
		this.hsl = hsl;
	}



	public String getCreqcalbodyid() {
		return creqcalbodyid;
	}



	public void setCreqcalbodyid(String creqcalbodyid) {
		this.creqcalbodyid = creqcalbodyid;
	}



	public String getCreqwarehouseid() {
		return creqwarehouseid;
	}



	public void setCreqwarehouseid(String creqwarehouseid) {
		this.creqwarehouseid = creqwarehouseid;
	}



	public UFDate getDreqdate() {
		return dreqdate;
	}



	public void setDreqdate(UFDate dreqdate) {
		this.dreqdate = dreqdate;
	}



	public String getCsupplycalbodyid() {
		return csupplycalbodyid;
	}



	public void setCsupplycalbodyid(String csupplycalbodyid) {
		this.csupplycalbodyid = csupplycalbodyid;
	}



	public String getCsupplywarehouseid() {
		return csupplywarehouseid;
	}



	public void setCsupplywarehouseid(String csupplywarehouseid) {
		this.csupplywarehouseid = csupplywarehouseid;
	}

	public String getVbatchcode() {
		return vbatchcode;
	}

	public void setVbatchcode(String vbatchcode) {
		this.vbatchcode = vbatchcode;
	}
	public UFDouble getNprice() {
		return nprice;
	}
	public void setNprice(UFDouble nprice) {
		this.nprice = nprice;
	}
	public UFDouble getNmny() {
		return nmny;
	}
	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}
	public String getCnextbillbid() {
		return cnextbillbid;
	}
	public void setCnextbillbid(String cnextbillbid) {
		this.cnextbillbid = cnextbillbid;
	}
	public UFDouble getNsafestocknum() {
		return nsafestocknum;
	}
	public void setNsafestocknum(UFDouble nsafestocknum) {
		this.nsafestocknum = nsafestocknum;
	}
	public UFDouble getNnum() {
		return nnum;
	}
	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}
	public UFDouble getNassistnum() {
		return nassistnum;
	}
	public void setNassistnum(UFDouble nassistnum) {
		this.nassistnum = nassistnum;
	}
	public UFDouble getNonhandnum() {
		return nonhandnum;
	}
	public void setNonhandnum(UFDouble nonhandnum) {
		this.nonhandnum = nonhandnum;
	}
	public UFDouble getNallusenum() {
		return nallusenum;
	}
	public void setNallusenum(UFDouble nallusenum) {
		this.nallusenum = nallusenum;
	}
	public UFDouble getNnonum() {
		return nnonum;
	}
	public void setNnonum(UFDouble nnonum) {
		this.nnonum = nnonum;
	}
	public UFDouble getNnetnum() {
		return nnetnum;
	}
	public void setNnetnum(UFDouble nnetnum) {
		this.nnetnum = nnetnum;
	}
	public UFDouble getNreoutnum() {
		return nreoutnum;
	}
	public void setNreoutnum(UFDouble nreoutnum) {
		this.nreoutnum = nreoutnum;
	}
	public UFDouble getNreinnum() {
		return nreinnum;
	}
	public void setNreinnum(UFDouble nreinnum) {
		this.nreinnum = nreinnum;
	}
	public UFDouble getNusenum() {
		return nusenum;
	}
	public void setNusenum(UFDouble nusenum) {
		this.nusenum = nusenum;
	}
	public Integer getIrowstatus() {
		return irowstatus;
	}
	public void setIrowstatus(Integer irowstatus) {
		this.irowstatus = irowstatus;
	}
	public UFBoolean getFixedflag() {
		return fixedflag;
	}
	public void setFixedflag(UFBoolean fixedflag) {
		this.fixedflag = fixedflag;
	}
	public String getVdef10() {
		return vdef10;
	}
	public void setVdef10(String vdef10) {
		this.vdef10 = vdef10;
	}
	public String getCrowno() {
		return crowno;
	}
	public void setCrowno(String crowno) {
		this.crowno = crowno;
	}
	public String getPk_defdoc8() {
		return pk_defdoc8;
	}
	public void setPk_defdoc8(String pk_defdoc8) {
		this.pk_defdoc8 = pk_defdoc8;
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
	public String getVdef7() {
		return vdef7;
	}
	public void setVdef7(String vdef7) {
		this.vdef7 = vdef7;
	}
	public String getPk_defdoc7() {
		return pk_defdoc7;
	}
	public void setPk_defdoc7(String pk_defdoc7) {
		this.pk_defdoc7 = pk_defdoc7;
	}
	public String getPk_plan() {
		return pk_plan;
	}
	public void setPk_plan(String pk_plan) {
		this.pk_plan = pk_plan;
	}
	public UFDouble getNreserve8() {
		return nreserve8;
	}
	public void setNreserve8(UFDouble nreserve8) {
		this.nreserve8 = nreserve8;
	}
	public String getVdef2() {
		return vdef2;
	}
	public void setVdef2(String vdef2) {
		this.vdef2 = vdef2;
	}
	public String getVdef5() {
		return vdef5;
	}
	public void setVdef5(String vdef5) {
		this.vdef5 = vdef5;
	}
	public String getVmemo() {
		return vmemo;
	}
	public void setVmemo(String vmemo) {
		this.vmemo = vmemo;
	}
	public UFDateTime getTs() {
		return ts;
	}
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}
	public String getVfree2() {
		return vfree2;
	}
	public void setVfree2(String vfree2) {
		this.vfree2 = vfree2;
	}
	public String getVdef9() {
		return vdef9;
	}
	public void setVdef9(String vdef9) {
		this.vdef9 = vdef9;
	}
	public String getPk_defdoc10() {
		return pk_defdoc10;
	}
	public void setPk_defdoc10(String pk_defdoc10) {
		this.pk_defdoc10 = pk_defdoc10;
	}
	public String getPk_defdoc6() {
		return pk_defdoc6;
	}
	public void setPk_defdoc6(String pk_defdoc6) {
		this.pk_defdoc6 = pk_defdoc6;
	}
	public String getVdef4() {
		return vdef4;
	}
	public void setVdef4(String vdef4) {
		this.vdef4 = vdef4;
	}
	public String getVreserve5() {
		return vreserve5;
	}
	public void setVreserve5(String vreserve5) {
		this.vreserve5 = vreserve5;
	}
	public UFDouble getNreserve9() {
		return nreserve9;
	}
	public void setNreserve9(UFDouble nreserve9) {
		this.nreserve9 = nreserve9;
	}
	public String getPk_defdoc2() {
		return pk_defdoc2;
	}
	public void setPk_defdoc2(String pk_defdoc2) {
		this.pk_defdoc2 = pk_defdoc2;
	}
	public String getVdef1() {
		return vdef1;
	}
	public void setVdef1(String vdef1) {
		this.vdef1 = vdef1;
	}
	public String getVdef8() {
		return vdef8;
	}
	public void setVdef8(String vdef8) {
		this.vdef8 = vdef8;
	}
	public String getVfree4() {
		return vfree4;
	}
	public void setVfree4(String vfree4) {
		this.vfree4 = vfree4;
	}
	public UFDouble getNreserve7() {
		return nreserve7;
	}
	public void setNreserve7(UFDouble nreserve7) {
		this.nreserve7 = nreserve7;
	}
	public String getPk_defdoc3() {
		return pk_defdoc3;
	}
	public void setPk_defdoc3(String pk_defdoc3) {
		this.pk_defdoc3 = pk_defdoc3;
	}
	public String getVreserve4() {
		return vreserve4;
	}
	public void setVreserve4(String vreserve4) {
		this.vreserve4 = vreserve4;
	}
	public String getCnextbillid() {
		return cnextbillid;
	}
	public void setCnextbillid(String cnextbillid) {
		this.cnextbillid = cnextbillid;
	}
	public String getVfree5() {
		return vfree5;
	}
	public void setVfree5(String vfree5) {
		this.vfree5 = vfree5;
	}
	public String getVreserve2() {
		return vreserve2;
	}
	public void setVreserve2(String vreserve2) {
		this.vreserve2 = vreserve2;
	}
	public String getVfree3() {
		return vfree3;
	}
	public void setVfree3(String vfree3) {
		this.vfree3 = vfree3;
	}
	public String getVreserve3() {
		return vreserve3;
	}
	public void setVreserve3(String vreserve3) {
		this.vreserve3 = vreserve3;
	}
	public String getVfree1() {
		return vfree1;
	}
	public void setVfree1(String vfree1) {
		this.vfree1 = vfree1;
	}



	public String getPk_defdoc9() {
		return pk_defdoc9;
	}



	public void setPk_defdoc9(String pk_defdoc9) {
		this.pk_defdoc9 = pk_defdoc9;
	}



	public UFDouble getNreserve10() {
		return nreserve10;
	}



	public void setNreserve10(UFDouble nreserve10) {
		this.nreserve10 = nreserve10;
	}



	public String getPk_defdoc4() {
		return pk_defdoc4;
	}



	public void setPk_defdoc4(String pk_defdoc4) {
		this.pk_defdoc4 = pk_defdoc4;
	}



	public String getVdef3() {
		return vdef3;
	}



	public void setVdef3(String vdef3) {
		this.vdef3 = vdef3;
	}



	public String getVdef6() {
		return vdef6;
	}



	public void setVdef6(String vdef6) {
		this.vdef6 = vdef6;
	}



	public Integer getDr() {
		return dr;
	}



	public void setDr(Integer dr) {
		this.dr = dr;
	}



	public UFDouble getNreserve6() {
		return nreserve6;
	}



	public void setNreserve6(UFDouble nreserve6) {
		this.nreserve6 = nreserve6;
	}



	public String getPk_defdoc1() {
		return pk_defdoc1;
	}



	public void setPk_defdoc1(String pk_defdoc1) {
		this.pk_defdoc1 = pk_defdoc1;
	}

    public void validataClient() throws ValidationException{
    	if(PuTool.isAssUnitManaged(getPk_invbasdoc())&&PuPubVO.getString_TrimZeroLenAsNull(getCastunitid())==null){
			throw new ValidationException("辅计量管理存货，辅计量不能为空");
		}
	}
	public void validataServer() throws BusinessException{
		String famular = "assistunit -> getColValue(bd_invbasdoc,assistunit,pk_invbasdoc,cinvbasid)";
		String[] names = new String[]{"cinvbasid"};
		String[] values = new String[]{getPk_invbasdoc()};
		UFBoolean isassist = PuPubVO.getUFBoolean_NullAs(HgPubTool.execFomular(famular, names, values),UFBoolean.FALSE);
		if(isassist.booleanValue()&&PuPubVO.getString_TrimZeroLenAsNull(getCastunitid())==null){
			throw new ValidationException("辅计量管理存货，辅计量不能为空");
		}		
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）年计划表体物资数据保存时校验
	 * 2011-1-26下午12:36:00
	 * @throws ValidationException
	 */
	public void validata() throws ValidationException{
		//净需求数量=12月份分量
		
		if(!PuPubVO.getUFBoolean_NullAs(getBisuse(), UFBoolean.FALSE).booleanValue()&&(PuPubVO.getString_TrimZeroLenAsNull(getPk_invbasdoc())==null||PuPubVO.getString_TrimZeroLenAsNull(getCinventoryid())==null)){
			throw new ValidationException("存货信息为空");
		}
		
		if(PuPubVO.getUFBoolean_NullAs(getBisuse(), UFBoolean.FALSE).booleanValue()&&PuPubVO.getString_TrimZeroLenAsNull(getVreserve1())==null){
			throw new ValidationException("临时物资信息为空");
		}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(getCsupplycalbodyid())==null){
			throw new ValidationException("行号"+getCrowno()+"供货库存组织为空");
		}
		
		String[] names = HgPubTool.PLAN_NUMKEYS;
		for(String name:names){
			if(PuPubVO.getUFDouble_NullAsZero(getAttributeValue(name)).doubleValue()<UFDouble.ZERO_DBL.doubleValue()){
				throw new ValidationException("存在小于零的数量字段");
			}
		}
		
		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(getNnetnum());
		if(nnum.doubleValue() == UFDouble.ZERO_DBL.doubleValue())
			throw new ValidationException("需求数量不能为空");
		UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getNprice());
		if(nprice.doubleValue() > UFDouble.ZERO_DBL.doubleValue()){
			UFDouble nmny = getNmny().add(UFDouble.ZERO_DBL,HgPubConst.MNY_DIGIT);
			UFDouble nmny2 = getNnum().multiply(nprice,HgPubConst.MNY_DIGIT);
			if(nmny.doubleValue() != nmny2.doubleValue()){
				throw new ValidationException("单价、数量、金额 三者数据不一致");
			}
		}		
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
	public UFBoolean getBisuse() {
		return bisuse;
	}
	public void setBisuse(UFBoolean bisuse) {
		this.bisuse = bisuse;
	}

} 
