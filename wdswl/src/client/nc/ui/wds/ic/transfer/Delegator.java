package nc.ui.wds.ic.transfer;
import java.util.Hashtable;
import nc.vo.pub.SuperVO;
import nc.vo.wds.transfer.TransferBVO;
/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class Delegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public Hashtable loadChildDataAry(String[] tableCodes,String key)throws Exception{	
		// 根据主表主键，取得子表的数据
		TransferBVO[] b1vos = (TransferBVO[]) this.queryByCondition(TransferBVO.class, "general_pk='" + key + "' and isnull(dr,0)=0");	
		Hashtable<String, SuperVO[]> dataHT = new Hashtable<String, SuperVO[]>();
		if (b1vos != null && b1vos.length > 0) {
			dataHT.put(tableCodes[0], b1vos);
		}
	    return dataHT;
	}
}