package nc.ui.wds2.set;

import java.util.HashMap;
import java.util.Map;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.pub.BusinessException;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds2.set.OutInSetVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;

public class OutInSetHelper {


	private static Map<String,String> typeInfor = null;

	static Map<String,String> getTypeInfor(){
		if(typeInfor == null)
			typeInfor = new HashMap<String, String>();
		return typeInfor;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 库存单据获取默认收发类别  根据收发类别默认设置
	 * @时间：2012-7-16下午12:18:02
	 * @param bm 单据界面模型类
	 * @param orderFieldName 订单类型字段名称
	 * @param isout 是否出库
	 * @return
	 * @throws BusinessException
	 */
	public static String getDefaultOutInTypeID(BillModel bm,String orderFieldName,boolean isout) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(orderFieldName)==null)
			return getDefaultOutInTypeID(Wds2WlPubConst.other, isout);
		if(bm.getItemByKey(orderFieldName) == null)
			return null;
		if(bm.getRowCount() == 0){
			return getDefaultOutInTypeID(Wds2WlPubConst.other, isout);
		}
		if(bm.getRowCount() < 0)
			return null;
		String ordertype = PuPubVO.getString_TrimZeroLenAsNull(bm.getValueAt(0, orderFieldName));
		
		if(ordertype == null)
			return null;
		
		return getDefaultOutInTypeID(ordertype, isout);
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 根据订单单据类型获取库存单据默认收发类别（基本是流程运单单据类型）
	 * @时间：2012-7-13下午05:13:23
	 * @param ordertype 运单类型
	 * @param isout 是否出库
	 * @return
	 * @throws BusinessException
	 */
	public static String getDefaultOutInTypeID(String ordertype,boolean isout) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(ordertype) == null)
			return null;
		String key = ordertype+isout;
		if(getTypeInfor().containsKey(key)){
			return getTypeInfor().get(key);
		}

		//		从数据库获取信息

		OutInSetVO[] vos = (OutInSetVO[])HYPubBO_Client.queryByCondition(OutInSetVO.class, 
				" pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");

		if(vos == null || vos.length == 0)
			return null;

		for(OutInSetVO vo:vos){
			getTypeInfor().put(getKey(vo.getIbiztype())+isout,isout?vo.getCouttypeid():vo.getCintypeid());
		}

		return getTypeInfor().get(key);
	}

	public final static int tw = 0;//转分仓
	public final static int tc = 1;//转货位
	public final static int so = 2;//销售出
	public final static int to = 3;//调拨出
	public final static int ti = 4;//调拨入
	public final static int virtual = 5;//虚拟
	public final static int special = 6;//特殊
	public final static int other = 7;//其他
	public final static int back = 8;//销售退货入库

	private static String getKey(int ibiztype){
		switch (ibiztype) {
		case tw:
			return WdsWlPubConst.WDS3;
		case tc:
			return WdsWlPubConst.HWTZ;
		case so:
			return WdsWlPubConst.WDS5;
		case to:
			return WdsWlPubConst.WDSH;
		case ti:
			return Wds2WlPubConst.billtype_alloinsendorder;
		case virtual:
			return Wds2WlPubConst.virtual;
		case special:
			return WdsWlPubConst.WDSS;
		case other:
			return Wds2WlPubConst.other;
		case back:
			return ScmConst.m_saleOut;
		default:
			return Wds2WlPubConst.other;
		}
	}

}
