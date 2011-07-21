package nc.vo.zb.query.ZbDetail;

import nc.vo.zb.comments.BiEvaluationBodyVO;

public class ZbDetailVO extends BiEvaluationBodyVO{
	private Integer izbtype;//·½Ê½


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
	
	public static final String templet_data_ID1 = "0001A110000000013986";
	public static final String templet_query_ID = "0001A1100000000139DL";
	public static final String templet_modoulecode = "4004090702";

	public Integer getIzbtype() {
		return izbtype;
	}

	public void setIzbtype(Integer izbtype) {
		this.izbtype = izbtype;
	}

}
