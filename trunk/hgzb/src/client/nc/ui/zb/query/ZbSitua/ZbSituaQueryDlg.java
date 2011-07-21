package nc.ui.zb.query.ZbSitua;

import java.util.ArrayList;

import nc.ui.pub.query.QueryConditionClient;
import nc.vo.pub.query.ConditionVO;

// zhw 鹤岗项目   
public class ZbSituaQueryDlg extends QueryConditionClient {

	public ZbSituaQueryDlg(){
		super();
	}
	
	public String checkCondition() {
		String strRet =null;
		ConditionVO[] cons = getConditionVO();
		 ArrayList<String> al = new ArrayList<String>();
			for(ConditionVO con:cons){
				if(con.getFieldCode().equalsIgnoreCase("a.ccustmanid")){
					al.add(con.getFieldCode());
				}
			}
			
			if(al.size()>1)
				strRet="只能选择一个供应商";
		String sResult = super.checkCondition();
		if (strRet == null || strRet.length() == 0)
			return sResult;
		else
			return strRet + (sResult == null ? "" : sResult);
	}
}
