package nc.ui.hg.pu.nmr;


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
	public void initData(){//向查询对话框中填加当前日期
		setDefaultValue("hg_new_materials.dbilldate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
	}
	
	public String getWhereSQL() {

		String strWhere = null;
		
		ConditionVO[] cons = getConditionVO();
		List<ConditionVO> lcon = new ArrayList<ConditionVO>();
		for(ConditionVO con:cons){
			if(con.getFieldCode().equalsIgnoreCase("h.vbillstatus")){
				if(HgPubTool.getIPlanBilltypeBySBilltype(con.getValue())==-1)
					continue;
				con.setValue(String.valueOf(HgPubTool.getIPlanBilltypeBySBilltype(con.getValue())));
			}
			lcon.add(con);
		}
				
		cons = lcon.toArray(new ConditionVO[0]);		

		strWhere = getWhereSQL(cons);
		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
			strWhere = " (1=1)";
		return strWhere;
	}
}