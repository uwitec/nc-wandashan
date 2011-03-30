package nc.ui.hg.ic.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.pub.ToftPanel;
import nc.ui.sm.login.ShowDialog;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;

public class HgICPubHealper {
	
	private final static String bo = "nc.bs.hg.ic.pub.HgICPubBO";
	
	public static PactVO[] queryPactsByVendor(ToftPanel tp,String cbiztypeid,String cvendormanid,String[] cinvbasids) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class,String[].class};
		Object[] ParameterValues = new Object[]{cbiztypeid,cvendormanid,cinvbasids};
		Object o = LongTimeTask.calllongTimeService("ic", tp, "正在检索合同...", 2, bo, null, "queryPactsByVendor", ParameterTypes, ParameterValues);
//		Object o = LongTimeTask.callRemoteService("ic", bo, "queryPactsByVendor", ParameterTypes, ParameterValues, 2);
		if(o == null)
			return null;
		return (PactVO[])o;
	}
	
	public static Object splitGItemsOnPactMatch(GeneralBillVO gbillvo) throws Exception{
		Class[] ParameterTypes = new Class[]{GeneralBillVO.class};
		Object[] ParameterValues = new Object[]{gbillvo};
		Object o = LongTimeTask.callRemoteService("ic", bo, "splitGItemsOnPactMatch", ParameterTypes, ParameterValues, 2);
		return o;
	
	}
	
	public static PactVO[] combinMatchPactVOs(PactVO[] matchs,PactVO[] pactvos){
		if(matchs==null||matchs.length==0)
			return null;

		if(pactvos==null||pactvos.length==0){
			for(PactVO pactvo:matchs){
				pactvo.setNusenum(pactvo.getNnum());
			}
			return matchs;
		}
		
		Map<String, PactVO> pactInfor = new HashMap<String, PactVO>();
		for(PactVO vo:pactvos){
			pactInfor.put(vo.getPrimaryKey(), vo);
		}
		PactVO tmp = null;
		for(PactVO vo:matchs){
			if(pactInfor.containsKey(vo.getPrimaryKey())){
				tmp = pactInfor.get(vo.getPrimaryKey());
				tmp.setNusenum(vo.getNnum().add(tmp.getNusenum()));
			}else{
				tmp = vo;
				tmp.setNusenum(vo.getNnum());
				vo.setNnum(UFDouble.ZERO_DBL);
			}
			pactInfor.put(vo.getPrimaryKey(), tmp);
		}
		return pactInfor.values().toArray(new PactVO[0]);
	}
	
	/**
	 * @author zhf  按匹配信息拆分入库行
	 * @param gitem
	 * @param pvos
	 * @return
	 */
	public static List<GeneralBillItemVO> splitGItemsOnPactMatch(GeneralBillItemVO gitem,PactVO[] pvos){
		if(gitem==null||pvos==null||pvos.length==0)
			return null;
		//根据匹配信息  对入库行进行拆行
		List<GeneralBillItemVO> litem = new ArrayList<GeneralBillItemVO>();
		GeneralBillItemVO tmpItem = null;
		LocatorVO[] tmpLocs = null;
		
		//对应入数量   和   验收数量  进行倒挤
		UFDouble ninsumnum = UFDouble.ZERO_DBL;
		UFDouble nkdsumnum = UFDouble.ZERO_DBL;
		UFDouble nsum = UFDouble.ZERO_DBL;
		for(PactVO pvo:pvos){
			tmpItem = (GeneralBillItemVO)gitem.clone();
			tmpItem.setPrimaryKey(null);
			tmpItem.setCgeneralhid(null);
			tmpItem.setCgeneralbb3(null);
			tmpItem.setStatus(VOStatus.NEW);
			tmpItem.setNshouldinnum(pvo.getNnum());
			tmpItem.setAttributeValue(HgPubConst.NUM_DEF_QUA, pvo.getNnum());
			if(tmpItem.getHsl()!=null)
			tmpItem.setNneedinassistnum(tmpItem.getNshouldinnum().div(tmpItem.getHsl(), HgPubConst.NUM_DIGIT));
			
			tmpItem.setVsourcebillcode(pvo.getVordercode());
			tmpItem.setVsourcerowno(pvo.getCrowno());
			tmpItem.setCsourcebillbid(pvo.getCorder_bid());
			tmpItem.setCsourcebillhid(pvo.getCorderid());
			tmpItem.setCsourcetype(ScmConst.PO_Order);
			tmpItem.setCfirstbillbid(pvo.getCorder_bid());
			tmpItem.setCfirstbillhid(pvo.getCorderid());
			tmpItem.setCfirsttype(ScmConst.PO_Order);
			
			tmpLocs = tmpItem.getLocator();
			if(tmpLocs!=null){
			tmpLocs[0].setNinspacenum(gitem.getNinnum());
			tmpLocs[0].setNinspaceassistnum(gitem.getNinassistnum());
			tmpLocs[0].setStatus(VOStatus.NEW);
			tmpLocs[0].setPrimaryKey(null);
			}
			ninsumnum = ninsumnum.add(tmpItem.getNshouldinnum());
			nkdsumnum = nkdsumnum.add(PuPubVO.getUFDouble_NullAsZero(tmpItem.getAttributeValue(HgPubConst.NUM_DEF_QUA)));
			nsum = nsum.add(pvo.getNnum());
			litem.add(tmpItem);
		}
//		int ss = tmpItem.getStatus();
		int len = litem.size();
		tmpItem = litem.get(len-1);
		//数量倒挤到最后一行
		tmpItem.setNshouldinnum(tmpItem.getNshouldinnum().add(ninsumnum.sub(nsum)));
		tmpItem.setAttributeValue(HgPubConst.NUM_DEF_QUA, PuPubVO.getUFDouble_NullAsZero(tmpItem.getAttributeValue(HgPubConst.NUM_DEF_QUA)).add(nkdsumnum.sub(nsum)));
		if(tmpItem.getHsl()!=null)
		tmpItem.setNneedinassistnum(tmpItem.getNshouldinnum().div(tmpItem.getHsl(), HgPubConst.NUM_DIGIT));
		tmpItem.setStatus(VOStatus.UPDATED);
		tmpItem.setPrimaryKey(gitem.getPrimaryKey());
		tmpItem.setCgeneralbb3(gitem.getCgeneralbb3());
		tmpItem.setCgeneralhid(gitem.getCgeneralhid());
		tmpLocs = tmpItem.getLocator();
		if(tmpLocs!=null){
		tmpLocs[0].setNinspacenum(gitem.getNinnum());
		tmpLocs[0].setNinspaceassistnum(gitem.getNinassistnum());
		tmpLocs[0].setStatus(VOStatus.UPDATED);
		tmpLocs[0].setPrimaryKey(gitem.getLocator()[0].getPrimaryKey());
		}
		litem.set(len-1, tmpItem);
		
		return litem;
	}
	
	//根据存货仓库  判断是否进行了货位管理
	public static boolean getCsflag(String cwarehouseid) throws BusinessException{
		String sql =" select csflag from bd_stordoc where isnull(dr,0)=0 and pk_stordoc = '"+cwarehouseid+"'";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object o=query.executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR );
		
		if(PuPubVO.getUFBoolean_NullAs(o,UFBoolean.FALSE).booleanValue()){
			return true;
		}
		return false;
	}
	/**
	 * 根据批次号和存货获取供应商
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-3-7上午09:34:56
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws Exception
	 */
	public static String getcvendor(String cinventoryid,String vbatchcode) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class};
		Object[] ParameterValues = new Object[]{cinventoryid,vbatchcode};
		Object o;
		o = LongTimeTask.callRemoteService("ic", bo, "getcvendor", ParameterTypes, ParameterValues, 2);
		
		return PuPubVO.getString_TrimZeroLenAsNull(o);
	}
	
	/**
	 * 根据批次号和存货获取供应商和合同价
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-3-7上午09:34:56
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws Exception
	 */
	public static Object getInfoByBatchCode(String cinventoryid ,String invcode, String pk_corp,String vbatch) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,String.class,String.class,String.class};
		Object[] ParameterValues = new Object[]{cinventoryid,invcode,pk_corp,vbatch};
		Object o;
		o = LongTimeTask.callRemoteService("ic", bo, "getInfoByBatchCode", ParameterTypes, ParameterValues, 2);
		
		return o;
	}
}
