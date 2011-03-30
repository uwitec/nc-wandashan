package nc.ui.hg.pu.plan.month;


import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;

public class ClientUIQueryDlg extends HYQueryDLG{
	
	private static final long serialVersionUID = 9153238819070518045L;
	
	public ClientUIQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
	}
	UFDate billdate = ClientEnvironment.getInstance().getDate();
	@Override

	public void initData(){//向查询对话框中填加当前日期
		setDefaultValue("h.dbilldate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
	}
	
	public String getWhereSQL() {

		String strWhere = null;
		String str = null;
		ConditionVO[] cons = getConditionVO();
		List<ConditionVO> lcon = new ArrayList<ConditionVO>();
		for(ConditionVO con:cons){
			if(con.getFieldCode().equalsIgnoreCase("h.vbillstatus")){
				if(HgPubTool.getIPlanBilltypeBySBilltype(con.getValue())==-1)
					continue;
				con.setValue(String.valueOf(HgPubTool.getIPlanBilltypeBySBilltype(con.getValue())));
			}
			lcon.add(con);
			//modify by zhw 2011-01-24  根据存货分类查询  按大类查询
			if(con.getFieldCode().equalsIgnoreCase("h.cinvclassid")){
					String pk_invcl = con.getValue();
					String invcode = PuPubVO.getString_TrimZeroLenAsNull(HgPubTool.getInvclasscode(pk_invcl));
					str=" and c.pk_invcl in(select pk_invcl from bd_invcl where invclasscode like '"+ invcode + "%')";
					con.setFieldCode("1");
					con.setValue("1");
			}
		}
				
		cons = lcon.toArray(new ConditionVO[0]);		

		strWhere = getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		if(PuPubVO.getString_TrimZeroLenAsNull(str) != null)
			   strWhere = strWhere + str;
		return strWhere;
	}
}