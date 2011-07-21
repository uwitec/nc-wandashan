package nc.ui.hg.pu.check.oldmaterials;

import java.awt.Container;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;


public class ClientQueryDLG extends HYQueryDLG{
	
	private static final long serialVersionUID = 9153238819070518045L;
	
	public ClientQueryDLG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType, String nodeKey) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType, nodeKey);
	}
	
	UFDate billdate = ClientEnvironment.getInstance().getDate();
	@Override
	public void initData() {
//		setDefaultValue("hg_oldmaterials.dmakedate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
//		setDefaultValue("hg_oldmaterials.dmodifydate",null,PuPubVO.getString_TrimZeroLenAsNull(billdate.toString()));
		super.initData();
	}
	
	@Override
	public String getWhereSQL() {
		return super.getWhereSQL();
	}

}
