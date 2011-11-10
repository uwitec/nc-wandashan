package nc.ui.wds.ic.write.back4y;

import java.util.Hashtable;

import nc.vo.pub.SuperVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB1VO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;

public class Writeback4yBusinessDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator {

	
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {
		String strWhere =" isnull(dr,0)=0 and pk_wds_writeback4Y_h='"+key+"'";
		SuperVO[] b1vos = this.queryByCondition(Writeback4yB1VO.class, strWhere);
		SuperVO[] b2vos = this.queryByCondition(Writeback4yB2VO.class, strWhere);
		Hashtable<String, SuperVO[]> dataH=new Hashtable<String, SuperVO[]>();
		
		if(b1vos!=null && b1vos.length>0){
			dataH.put(tableCodes[0], b1vos);
		}
		if(b2vos!=null && b2vos.length>0){
			dataH.put(tableCodes[1], b2vos);
		}
		return dataH;
	}
}
