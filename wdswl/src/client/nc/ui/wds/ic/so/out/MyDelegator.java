package nc.ui.wds.ic.so.out;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

import nc.ui.wl.pub.LongTimeTask;
import nc.vo.ic.other.out.TbOutgeneralB2VO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;


/**
  *
  *抽象业务代理类的缺省实现
  *@author author
  *@version tempProject version
  */
public class MyDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{

//	/**
//	 * queryBodyVOs 方法注解。
//	 */
//	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(Class voClass, String billType, String key, String strWhere)throws Exception {
//		if(PuPubVO.getString_TrimZeroLenAsNull(strWhere)==null)
//			strWhere = " general_pk = '"+key+"'";
//		else
//			strWhere = strWhere +=" and general_pk = '"+key+"'";
//		Class[] ParameterTypes = new Class[]{String.class};
//		Object[] ParameterValues = new Object[]{strWhere};
//		SuperVO[] os = (SuperVO[])LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME, "nc.bs.ic.pub.WdsIcPubBO", "queryIcOutBodyInfor", ParameterTypes, ParameterValues, 2);
//		return os;
//	}

	public MyDelegator() {
		super();
	}
	
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		// 根据主表主键，取得子表的数据
		TbOutgeneralBVO[] b1vos = (TbOutgeneralBVO[]) this.queryByCondition(TbOutgeneralBVO.class, "general_pk='" + key + "' and isnull(dr,0)=0");	
		for(TbOutgeneralBVO body:b1vos){
			TbOutgeneralTVO[] ctmps=(TbOutgeneralTVO[]) this.queryByCondition(TbOutgeneralTVO.class, " general_b_pk = '"+body.getPrimaryKey()+"'");
			if(ctmps == null||ctmps.length==0)
				continue;
			body.setTrayInfor(Arrays.asList(ctmps));
		}
		
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

//	protected class QueryInfo implements IMultiChildQueryInfo {
//		/**
//		 * 根据TableCode取得别名
//		 */
//		public String getAliasByTableCode(String tblCode) {
//			return tblCode;
//		}
//	}


//	protected class ChildVOInfo implements IMultiChildVOInfo {
//		/**
//		 * 
//		 */
//		public String[] getVONames() {
//			String[] VoNames = new String[] { 
//					BiddingBillVO.class.getName(),
//					BiddingHeaderVO.class.getName(),
//					BiddingBodyVO.class.getName(),
//					BiddingSuppliersVO.class.getName(),
//					BiddingTimesVO.class.getName()
//					};
//			return VoNames;
//		}
//
//		/**
//		 * 根据tablecode获得VO名称
//		 */
//		public String getVoClassNameByTableCode(String tblCode) {
//			for (int i = 0; i < getTableCodes().length; i++) {
//				if (getTableCodes()[i].equals(tblCode)) {
//					return getVONames()[i + 2];
//				}
//			}
//			return null;
//		}
//
//		/**
//		 * 获取所有的TableCode
//		 */
//		public String[] getTableCodes() {
//			String[] childTableCodes = new String[] { 
//					"zb_bidding_b","zb_biddingsuppliers","zb_biddingtimes"};
//			return childTableCodes;
//		}
//	}
//
//	@Override
//	public IMultiChildQueryInfo getMultiChildQueryInfo() {
//		if (queryInfo == null)
//			queryInfo = new QueryInfo();
//		return queryInfo;
//	}
//
//	@Override
//	public IMultiChildVOInfo getMultiChildVoInfo() {
//		if (childVOInfo == null)
//			childVOInfo = new ChildVOInfo();
//		return childVOInfo;
//	}
}