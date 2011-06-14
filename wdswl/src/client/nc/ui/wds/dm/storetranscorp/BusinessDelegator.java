//package nc.ui.wds.dm.storetranscorp;
//
//import java.util.Hashtable;
//
//import nc.ui.trade.bsdelegate.BDBusinessDelegator;
//import nc.vo.wds.dm.storetranscorp.StortranscorpBVO;
//
//public class BusinessDelegator extends BDBusinessDelegator {
//
//	public Hashtable loadChildDataAry(String[] tableCodes, String key)
//			throws Exception {
//
//		Hashtable dataHashTable = new Hashtable();
//
//		nc.vo.wds.dm.storebing.TbStorcubasdocVO[] bodyVOs1 = (nc.vo.wds.dm.storebing.TbStorcubasdocVO[]) queryByCondition(
//				StortranscorpBVO.class, getBodyCondition(
//						StortranscorpBVO.class, key));
//		if (bodyVOs1 != null && bodyVOs1.length > 0) {
//
//			dataHashTable.put("wds_stortranscorp_b", bodyVOs1);
//		}
//
//		return dataHashTable;
//
//	}
//
//	/**
//	 * 
//	 * 该方法用于获取查询条件，用户在缺省实现中可以对该方法进行修改。
//	 * 
//	 */
//	public String getBodyCondition(Class bodyClass, String key) {
//
//		if (bodyClass == StortranscorpBVO.class)
//			return "pk_stordoc = '" + key + "' and isnull(dr,0)=0 ";
//
//		return null;
//	}
//
//}
