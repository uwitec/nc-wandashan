package nc.bs.wds.ic.other.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zpm
 *
 */

public class ChangeTo4I {
	
	private  String beanName = IGeneralBill.class.getName(); 
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private String s_billtype = "4I";
	private String corp = null;//当前登录公司pk
	private UFBoolean isReturn = UFBoolean.FALSE;
	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();;
	
	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	}
	/**
	 * @功能：取消签字动作
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject value,String coperator,String date) throws Exception {
		if(value == null ){
			return null;
		}
		GeneralBillVO[] billvo = null;
		//其它出库
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		String where  = " csourcebillhid = '"+outhvo.getPrimaryKey()+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4I", voCond);
		if(alListData!=null && alListData.size()>0){
			for(int i = 0 ;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);//增加当前操作员，业务员PK加锁
			}
			billvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
		}
		return billvo;
	}
	
	/**
	 * @功能：签字动作
	 */
	public AggregatedValueObject signQueryGenBillVO(
			AggregatedValueObject billVO,
			String coperator,
			String date) throws Exception {
		if(billVO==null){
			return null;
		}
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) billVO.getParentVO();
		TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) billVO.getChildrenVO();
		isReturn = PuPubVO.getUFBoolean_NullAs(outhvo.getIs_yundan(),UFBoolean.FALSE);
		corp =outhvo.getPk_corp();
		String pk_billtype = PuPubVO.getString_TrimZeroLenAsNull(outhvo.getVbilltype());
		if(pk_billtype == null){
			return null;
		}
		//1.设置货位信息
		setLocatorVO(bvos); 
		//2.交换生成ERP其他出库单
		GeneralBillVO vo = (GeneralBillVO)PfUtilTools.runChangeData(pk_billtype, s_billtype, billVO,null); //其它出库
		//		setSpcGenBillVO(vo,coperator,date);
		WdsWlIcPubDealTool.appFieldValueForIcNewBill(vo, l_map, corp,coperator, date, isReturn,getBaseDAO());
		if(!isReturn.booleanValue()){
			//如果不回传批次号 应该按照  来源订单id + 批次号  进行汇总处理------zhf		
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(vo, false);
		}
		return vo;
	}
	
	
	/**
	 * 
	 * @作者：zpm
	 * @说明：完达山物流项目 设置货位信息
	 * @时间：2011-4-20上午11:35:31
	 * @param value
	 */
	public void setLocatorVO(TbOutgeneralBVO[] bvos) {
		if(bvos == null || bvos.length == 0){
			return;
		}
		//		zhf   2011 12 27  调整    
		for(TbOutgeneralBVO bvo:bvos){
			String key = bvo.getGeneral_b_pk();
			LocatorVO lvo = new LocatorVO();
			lvo.setPk_corp(corp);
			lvo.setNoutspacenum(bvo.getNoutnum());
			lvo.setNoutspaceassistnum(bvo.getNoutassistnum());
			lvo.setCspaceid(bvo.getCspaceid());//货位
			lvo.setStatus(VOStatus.NEW);
			if(l_map.containsKey(key)){
				l_map.get(key).add(lvo);
			}else{
				ArrayList<LocatorVO> zList = new ArrayList<LocatorVO>();
				zList.add(lvo);
				l_map.put(key, zList);
			}
		}
	}
}
