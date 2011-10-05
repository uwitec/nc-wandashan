package nc.ui.wds.ic.allocation.in;
import java.util.Arrays;
import java.util.Hashtable;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbgeneralB2VO;
import nc.vo.pub.SuperVO;
/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class AlloDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {
		// 根据主表主键，取得子表的数据
		TbGeneralBVO[] b1vos = (TbGeneralBVO[]) this.queryByCondition(TbGeneralBVO.class, "geh_pk='" + key + "' and isnull(dr,0)=0");	
		for(TbGeneralBVO body:b1vos){
			TbGeneralBBVO[] ctmps=(TbGeneralBBVO[]) this.queryByCondition(TbGeneralBBVO.class, " geb_pk = '"+body.getPrimaryKey()+"'");
			if(ctmps == null||ctmps.length==0)
				continue;
			body.setTrayInfor(Arrays.asList(ctmps));
		}		
		// 根据主表主键，取得子的数据
		TbgeneralB2VO[] b2vos = (TbgeneralB2VO[]) this.queryByCondition(TbgeneralB2VO.class, "geh_pk='" + key + "' and isnull(dr,0)=0");
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