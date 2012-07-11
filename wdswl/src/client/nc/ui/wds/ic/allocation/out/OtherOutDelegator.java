package nc.ui.wds.ic.allocation.out;
import java.util.Hashtable;

import nc.vo.ic.other.out.TbOutgeneralB2VO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.pub.SuperVO;
/**
  *抽象业务代理类的缺省实现
  *@author mlr
  */
public class OtherOutDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public Hashtable loadChildDataAry(String[] tableCodes,String key)throws Exception{	
		// 根据主表主键，取得子表的数据
		TbOutgeneralBVO[] b1vos = (TbOutgeneralBVO[]) this.queryByCondition(TbOutgeneralBVO.class, "general_pk='" + key + "' and isnull(dr,0)=0");		
		// 根据主表主键，取得子的数据
		TbOutgeneralB2VO[] b2vos = (TbOutgeneralB2VO[]) this.queryByCondition(TbOutgeneralB2VO.class, "general_pk='" + key + "' and isnull(dr,0)=0");
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