package nc.vo.hg.pu.check.fund;

import java.util.ArrayList;
import nc.vo.trade.comcheckunique.IUniqueFieldCheck;
import nc.vo.trade.pub.IBDGetCheckClass;
import nc.vo.trade.pub.IRetCurrentDataAfterSave;

public class CritiCHK implements IUniqueFieldCheck, IBDGetCheckClass,IRetCurrentDataAfterSave{

	public ArrayList getFieldArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList getNameArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDetail() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSingleTable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getCheckClass() {
		return "nc.bs.hg.pu.check.fund.FundCheckCHK";
	}

}
