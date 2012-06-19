package nc.ui.wds.ic.other.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import nc.vo.ic.other.out.TbOutgeneralB2VO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.pub.SuperVO;
/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class OtherOutDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public Hashtable loadChildDataAry(String[] tableCodes,String key)throws Exception{	
		// 根据主表主键，取得子表的数据
		TbOutgeneralBVO[] b1vos = (TbOutgeneralBVO[]) this.queryByCondition(TbOutgeneralBVO.class, "general_pk='" + key + "' and isnull(dr,0)=0");	
//		for(TbOutgeneralBVO body:b1vos){
//			TbOutgeneralTVO[] ctmps=(TbOutgeneralTVO[]) this.queryByCondition(TbOutgeneralTVO.class, " general_b_pk = '"+body.getPrimaryKey()+"'");
//			if(ctmps == null||ctmps.length==0)
//				continue;
//			List<TbOutgeneralTVO> list=new ArrayList<TbOutgeneralTVO>();
//			list.addAll(Arrays.asList(ctmps));
//	
//			body.setTrayInfor(list);
//		}
		
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
	
//	/**
//	 * 根据条件进行数据查询  zhf  改造查询补充  托盘明细子子表信息
//	 */
//	public SuperVO[] queryByCondition(Class voClass, String strWhere)
//			throws Exception {		
//		
//		Class[] ParameterTypes = new Class[]{String.class};
//		Object[] ParameterValues = new Object[]{strWhere};
//		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
//		
//		return os;
//	}
//	
//	
//	/**
//	 * queryBodyVOs 方法注解。
//	 */
//	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
//			Class voClass, String billType, String key, String strWhere)
//			throws Exception {
//		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
//			strWhere = strWhere +=" general_pk = '"+key+"'";
//		else
//			strWhere = strWhere +=" and general_pk = '"+key+"'";
//		Class[] ParameterTypes = new Class[]{String.class};
//		Object[] ParameterValues = new Object[]{strWhere};
//		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
//		
//		return os;
//		
////		
////		if (billType == null || billType.trim().length() == 0)
////			return null;
////		else
////			return HYPubBO_Client.queryAllBodyData(billType, voClass, key,
////					strWhere);
//	}
//	
//	
//       /**
//         *
//         *该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
//         *
//         */	
//       public String getBodyCondition(Class bodyClass,String key){
//		
//       	 if(bodyClass == nc.vo.ic.other.out.TbOutgeneralBVO.class)
//	   return "general_pk = '" + key + "' and isnull(dr,0)=0 ";
//       		
//	 return null;
//       } 
	


}