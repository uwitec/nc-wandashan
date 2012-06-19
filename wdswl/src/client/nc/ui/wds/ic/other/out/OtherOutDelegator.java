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
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class OtherOutDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public Hashtable loadChildDataAry(String[] tableCodes,String key)throws Exception{	
		// ��������������ȡ���ӱ������
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
		
		// ��������������ȡ���ӵ�����
		TbOutgeneralB2VO[] b2vos = (TbOutgeneralB2VO[]) this.queryByCondition(TbOutgeneralB2VO.class, "general_pk='" + key + "' and isnull(dr,0)=0");
		// ��ѯ���ݷ�Hashtable������
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
//	 * ���������������ݲ�ѯ  zhf  �����ѯ����  ������ϸ���ӱ���Ϣ
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
//	 * queryBodyVOs ����ע�⡣
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
//         *�÷������ڻ�ȡ��ѯ�������û���ȱʡʵ���п��ԶԸ÷��������޸ġ�
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