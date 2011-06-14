package nc.ui.wds.tranprice.transcorp;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BDBusinessDelegator;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wds.tranprice.transcorp.TanscorpB1VO;
import nc.vo.wds.tranprice.transcorp.TanscorpB2VO;

public class ClientBusinessDelegator extends BDBusinessDelegator {
	
	public ClientBusinessDelegator() {
		super();
	}
	
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		// 根据主表主键，取得子表的数据
		TanscorpB1VO[] b1vos = (TanscorpB1VO[]) this.queryByCondition(TanscorpB1VO.class, "pk_wds_tanscorp_h='" + key + "' and isnull(dr,0)=0");
	
		// 根据主表主键，取得子的数据
		TanscorpB2VO[] b2vos = (TanscorpB2VO[]) this.queryByCondition(TanscorpB2VO.class, "pk_wds_tanscorp_h='" + key + "' and isnull(dr,0)=0");
	
		
	
		// 查询数据放Hashtable并返回
		Hashtable<String, SuperVO[]> dataHT = new Hashtable<String, SuperVO[]>();
		if (b1vos != null && b1vos.length > 0) {
			dataHT.put(tableCodes[0], b1vos);
		}
		if (b2vos != null && b2vos.length > 0) {
			dataHT.put(tableCodes[1], b2vos);
		}
		return dataHT;
	}
}
