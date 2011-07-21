package nc.ui.hg.pu.plan.refmodel;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.hg.pu.pub.PlanPubHelper;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.vo.bd.b06.PsndocVO;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.scm.pu.PuPubVO;

//�ƻ���Ŀ��������

public class DeptVerRefModel extends AbstractRefModel {
	private String corpid;
	private String deptid;
	public DeptVerRefModel() {
		super();
		init();
	}
	public DeptVerRefModel(String corp,String deptid) {
		super();
		corpid = corp;
		this.deptid = deptid;
		init();
	}
	
	private void init(){
		if(corpid == null)
			corpid =  ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		if(deptid == null){
			PlanApplyInforVO vo = null;
			try{
				vo = PlanPubHelper.getAppInfor(corpid, ClientEnvironment.getInstance().getUser().getPrimaryKey());
			}catch(Exception e){}
			if(vo == null)
				return;
			deptid = PuPubVO.getString_TrimZeroLenAsNull(vo.getCapplydeptid());
		}
	}

	@Override
	public String getWherePart() {
		String sql =  " isnull(dr,0)= 0  and pk_corp = '"+corpid+"'";
		if(deptid!=null)
			sql = sql+" and pk_fathedept = '"+deptid+"'";
		return sql;
	}

	@Override
	public String[] getFieldCode() {
		return new String[] { "deptcode",
				"deptname",
				"depttype"
				};
	}

	@Override
	public String[] getFieldName() {
		return new String[] { "���ű���", "��������","��������"};
	}

	@Override
	public String[] getHiddenFieldCode() {
		return new String[] {"pk_deptdoc","depttype" };
	}

	@Override
	public int getDefaultFieldCount() {
		return 3;
	}

	@Override
	public String getPkFieldCode() {
		return "pk_deptdoc";
	}

	@Override
	public String getRefTitle() {
		return "���ŵ���";
	}

	@Override
	public String getTableName() {
		return "bd_deptdoc";
	}
	
	@Override
	protected String getRefCacheSqlKey() {
		//���ò�����
		setCacheEnabled(false);
		return "";
	}

}
