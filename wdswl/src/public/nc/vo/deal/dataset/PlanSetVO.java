package nc.vo.deal.dataset;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFTime;

/**
 * 发运计划安排合格状态查询 期间段设置
 * @author mlr
 *
 */
public class PlanSetVO extends SuperVO{
	
	//create table(pk_name char(20), start1 int ,ends1 int ,start2 int,ends2 int)
	
	private static final long serialVersionUID = -3913070714902658292L;

	private String pk_name;//主键
	private String pk_corp;//
	
	private String name;//期间段名字
	private String namse2;//期间段名字2
	
	private Integer start1;//开始天数
	private Integer ends1;//结束天数
	
	private Integer start2;//开始天数
	
	private Integer ends2;//结束天数
	
	private UFTime ts;
	private Integer dr;

	
	
	
	public UFTime getTs() {
		return ts;
	}

	public void setTs(UFTime ts) {
		this.ts = ts;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_name() {
		return pk_name;
	}

	public void setPk_name(String pk_name) {
		this.pk_name = pk_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamse2() {
		return namse2;
	}

	public void setNamse2(String namse2) {
		this.namse2 = namse2;
	}

	public Integer getStart1() {
		return start1;
	}

	public void setStart1(Integer start1) {
		this.start1 = start1;
	}

	public Integer getEnds1() {
		return ends1;
	}

	public void setEnds1(Integer ends1) {
		this.ends1 = ends1;
	}

	public Integer getStart2() {
		return start2;
	}

	public void setStart2(Integer start2) {
		this.start2 = start2;
	}

	public Integer getEnds2() {
		return ends2;
	}

	public void setEnds2(Integer ends2) {
		this.ends2 = ends2;
	}

	@Override
	public String getPKFieldName() {
		return "pk_name";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "wds_setname";
	}

}
