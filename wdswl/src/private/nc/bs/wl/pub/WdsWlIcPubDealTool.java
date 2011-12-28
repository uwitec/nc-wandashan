package nc.bs.wl.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsWlIcPubDealTool {
	
	private static Map dataMap = new HashMap();
	
	
	/**
	 * 
	 * @作者：zhf 按照来源id 和  批次号  进行汇总合并  库存单据体数据
	 * @说明：完达山物流项目 
	 * @时间：2011-12-28下午03:26:41
	 * @param bill 待处理的库存单据
	 * @param isin 是否入库类型
	 */
	public static void combinItemsBySourceAndInv(GeneralBillVO bill,boolean isin){

		//		如果不回传批次号  应该按照  来源订单id + 批次号  进行汇总处理------zhf		
		String key = null;
		GeneralBillItemVO[] items = bill.getItemVOs();
		GeneralBillItemVO tmp = null;
		LocatorVO locavo = null;
		
		for(GeneralBillItemVO item:items){
			key = WdsWlPubTool.getString_NullAsTrimZeroLen(item.getCsourcebillbid())+WdsWlPubTool.getString_NullAsTrimZeroLen(item.getVbatchcode());
			locavo = item.getLocator()[0];
			if(dataMap.containsKey(key)){
				tmp = (GeneralBillItemVO)(dataMap.get(key));
				if(isin){
					item.setNshouldinnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldinnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum())));
					item.setNneedinassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNneedinassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNneedinassistnum())));
					item.setNinnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNinnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNinnum())));
					item.setNinassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNinassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNinassistnum())));
					locavo.setNinspacenum(item.getNinnum());
					locavo.setNinspaceassistnum(item.getNinassistnum());
				}else{
					item.setNshouldoutnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldoutnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum())));
					item.setNshouldoutassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNshouldoutassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutassistnum())));
					item.setNoutnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNoutnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNoutnum())));
					item.setNoutassistnum(PuPubVO.getUFDouble_NullAsZero(tmp.getNoutassistnum()).add(PuPubVO.getUFDouble_NullAsZero(item.getNoutassistnum())));
					locavo.setNoutspacenum(item.getNoutnum());
					locavo.setNoutspaceassistnum(item.getNoutassistnum());
				}
			}
			dataMap.put(key, item);
		}
		GeneralBillItemVO[] newItems = (GeneralBillItemVO[])(dataMap.values().toArray(new GeneralBillItemVO[0]));
		bill.setChildrenVO(newItems);
		dataMap.clear();
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2011-12-28下午03:41:43
	 * @param bill 待处理的新生成的库存单据
	 * @param l_map 已封装好的货位信息
	 * @param coperator 当前用户
	 * @param sdate 当前日期
	 * @param fisvbatchcontorl 是否回传批次到erp
	 * @param returnBatchcode 默认回传的批次号 ‘2009’
	 */
	public static void appFieldValueForIcNewBill(GeneralBillVO bill,
			Map<String,ArrayList<LocatorVO>> l_map,
			String coperator,
			String sdate,
			UFBoolean fisvbatchcontorl,
			String returnBatchcode){
		if(bill == null)
			return;
		bill.setGetPlanPriceAtBs(false);//不需要查询计划价
		bill.getHeaderVO().setCoperatoridnow(coperator);//当前操作人///业务加锁，锁定当前操作人员
		bill.getHeaderVO().setDbilldate(new UFDate(sdate));//单据日期
		bill.getHeaderVO().setStatus(VOStatus.NEW);//单据新增状态
		GeneralBillItemVO[] items = bill.getItemVOs();
		if(items == null || items.length == 0)
			return;
		int index =  1;
		for(GeneralBillItemVO item:items){
			item.setCrowno(String.valueOf((index)*10));//行号
			item.setStatus(VOStatus.NEW);//单据新增状态
			if(item.getDbizdate() == null){
				item.setDbizdate(new UFDate(sdate));//业务日期
			}	
			if(fisvbatchcontorl == null || !fisvbatchcontorl.booleanValue()){
				item.setVbatchcode(returnBatchcode);
			}
			//设置货位信息
			String key  = item.getCfirstbillbid();
			ArrayList<LocatorVO> lvo = l_map.get(key);
			if(lvo!=null && lvo.size()>0){
				item.setLocator(lvo.toArray(new LocatorVO[0]));
			}
			index++;
		}
	}
	
	private static String m_batchcode = "";
	
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 完达山物流回写供应链的批次号，默认是2009，可通过参数来配置
	 * @时间：2011-4-20上午11:57:57
	 * @return
	 */
	public static  String getDefaultVbatchCode(String corp){
		if(PuPubVO.getString_TrimZeroLenAsNull(m_batchcode)!=null)
			return m_batchcode;
		ISysInitQry sysinitQry = (ISysInitQry) NCLocator.getInstance().lookup(ISysInitQry.class.getName());
		try {
			SysInitVO vo =sysinitQry.queryByParaCode(corp, "WDS00");
			if(vo != null){
				m_batchcode = vo.getValue();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println("获取参数WDS00失败");
			return "2009";
		}
		return m_batchcode;
	}

}
