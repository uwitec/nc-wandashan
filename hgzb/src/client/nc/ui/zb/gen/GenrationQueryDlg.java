package nc.ui.zb.gen;


import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.pub.ZbPubTool;

public class GenrationQueryDlg extends HYQueryDLG{
	
	private static final long serialVersionUID = 9153238819070518045L;
	public GenrationQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
	}
	UFDate billdate = ClientEnvironment.getInstance().getDate();
	public void initData(){//向查询对话框中填加当前日期
//		setDefaultValue("hg_new_materials.dbilldate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
	}
	
	public String getWhereSQL() {

		String strWhere = null;
		String str = null;
		ConditionVO[] cons = getConditionVO();
		List<ConditionVO> lcon = new ArrayList<ConditionVO>();
		for(ConditionVO con:cons){
			
			//modify by zhw 2011-01-24  根据存货分类查询  按大类查询
			if(con.getFieldCode().equalsIgnoreCase("h.cinvclid")){
					String pk_invcl = con.getValue();
					String invcode = PuPubVO.getString_TrimZeroLenAsNull(ZbPubTool.getInvclasscode(pk_invcl));
					str=" and inv.pk_invcl in(select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')";
					con.setFieldCode("1");
					con.setValue("1");
			}
			lcon.add(con);
		}
		cons = lcon.toArray(new ConditionVO[0]);		

		strWhere = getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		if(PuPubVO.getString_TrimZeroLenAsNull(str) != null)
			   strWhere = strWhere + str;
		return strWhere;
	}
	
	public String checkCondition() {
		String strRet =null;
		ConditionVO[] cons = getConditionVO();
		 ArrayList<String> al = new ArrayList<String>();
			for(ConditionVO con:cons){
				if(con.getFieldCode().equalsIgnoreCase("h.temp") ||con.getFieldCode().equalsIgnoreCase("h.ccustmanid")){
					al.add(con.getFieldCode());
				}
			}
			if(al==null || al.size()==0)
				strRet ="必须选择一个供应商";
			
			if(al.size()>1)
				strRet="只能选择一个供应商";
		String sResult = super.checkCondition();
		if (strRet == null || strRet.length() == 0)
			return sResult;
		else
			return strRet + (sResult == null ? "" : sResult);
	}
}