package nc.bs.wds.ic.so.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cB2VO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author lyf 
 * 销售出库回传单，同步到ERP销售出库单
 *  
 */

public class ChangeTo4C {
	
	HYPubBO pubbo = null;
	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}	
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	private String corp=null;
	private String coperator = null;
	private String date = null;
	private  String beanName = IGeneralBill.class.getName(); 
	private UFBoolean fisvbatchcontorl = UFBoolean.FALSE;
	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	}
	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();	
	/**
	 * @功能：签字动作:得到销售出库单
	 */
	public AggregatedValueObject signQueryGenBillVO(
			AggregatedValueObject bill,
			String coperator,
			String date) throws Exception {
		if(bill==null){
			return bill;
		}
		this.coperator = coperator;
		this.date = date;
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		fisvbatchcontorl= PuPubVO.getUFBoolean_NullAs(hvo.getFisvbatchcontorl(), UFBoolean.FALSE);
////		//装载非虚拟流程表体
//		List<Writeback4cB2VO>  listbvos = new ArrayList<Writeback4cB2VO>();
		//原销售出库回传单表体vo
		Writeback4cB2VO[] bvos = (Writeback4cB2VO[])billvo.getTableVO(billvo.getTableCodes()[1]);
		if(bvos == null || bvos.length ==0){
			return null;
		}
//		// liuys add 判断是否为虚拟安排  , 如果是,那么不回传erp销售出库
//		for(int i=0;i<bvos.length;i++){
//			UFBoolean isxnap=PuPubVO.getUFBoolean_NullAs(bvos[i].getIsxnap(), UFBoolean.FALSE);
//			if(!isxnap.booleanValue())
//				listbvos.add(bvos[i]);
//		}
//		//liuys add 如果整单都是虚拟安排,那么直接返回null,不处理
//		if(listbvos==null || listbvos.size()==0)
//			return null;
//		Writeback4cB2VO[] b2vos  = listbvos.toArray(new Writeback4cB2VO[0]);

		List<String> general_hs = new ArrayList<String>();
		for(Writeback4cB2VO b2vo:bvos){
			String general_h = b2vo.getCsourcebillhid();
			if(general_h == null || "".equalsIgnoreCase(general_h) 
					|| general_hs.contains(general_h)){
				continue;
			}
			general_hs.add(general_h);
		}
		//根据销售出库回传单，查找上有销售出库单（WDS8），分别对应交换成erp销售出库单(4C)
		GeneralBillVO vo = getGeneralBillVO(hvo,general_hs);
		
		return vo;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目:交换生成销售出库单 
	 * @时间：2011-11-2下午02:52:09
	 * @param general_hs
	 * @return
	 */
	public GeneralBillVO getGeneralBillVO(Writeback4cHVO hvo,List<String> general_hs) throws BusinessException{
		if(general_hs == null || general_hs.size() ==0){
			return null;
		}
		Object userObj = new String[]{HYBillVO.class.getName(),
				TbOutgeneralHVO.class.getName(),TbOutgeneralBVO.class.getName()};

		GeneralBillVO bill = new GeneralBillVO();
		GeneralBillHeaderVO head = null;
		List<GeneralBillItemVO> items = new ArrayList<GeneralBillItemVO>();

		for(int i=0;i<general_hs.size();i++){
			AggregatedValueObject billVO = getHypubBO().queryBillVOByPrimaryKey(userObj, general_hs.get(i));
			String pk_billtype = PuPubVO.getString_TrimZeroLenAsNull(billVO.getParentVO().getAttributeValue("vbilltype"));
			if(pk_billtype == null){
				continue;
			}
			//设置货位信息
			setLocatorVO(billVO);
			GeneralBillVO vo =(GeneralBillVO) PfUtilTools.runChangeData(pk_billtype, "4C", billVO,null); //销售出库
			if(i == 0){
				head = vo.getHeaderVO();
			}
			for(GeneralBillItemVO item:vo.getItemVOs()){
				item.setAttributeValue(WdsWlPubConst.csourcehid_wds, hvo.getPrimaryKey());
			}
			items.addAll(Arrays.asList(vo.getItemVOs()));
		}
		bill.setParentVO(head);
		items=filter(items,hvo);
		bill.setChildrenVO(items.toArray(new GeneralBillItemVO[0]));
		
		WdsWlIcPubDealTool.appFieldValueForIcNewBill(bill, l_map, corp,coperator, date, fisvbatchcontorl,getBaseDAO());

		if(!fisvbatchcontorl.booleanValue()){
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(bill, false);
		}
		return bill;
	}
	/**
	 * 过滤销售订单的订单号
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-8-6下午03:40:11
	 * @param items
	 * @param hvo 
	 */
	private List<GeneralBillItemVO> filter(List<GeneralBillItemVO> items, Writeback4cHVO hvo) {
		List<GeneralBillItemVO> list=new ArrayList<GeneralBillItemVO>();
		if(items==null || items.size()==0)
			return list;
		if(hvo==null){
			return list;
		}
		for(int i=0;i<items.size();i++){
			if(hvo.getVbillno().equals(items.get(i).getVsourcebillcode())){
				list.add(items.get(i));
			}		
		}	
		return list;
	}
	/**
	 * @功能：取消签字动作
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return null;
		}
		this.coperator = coperator;
		this.date = date;
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		String csaleid = hvo.getPrimaryKey()==null ?"":hvo.getPrimaryKey();
		String where  = "body."+WdsWlPubConst.csourcehid_wds+"='"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4C", voCond);
	    GeneralBillVO[] gbillvo = null;
		if(alListData!=null && alListData.size()>0){
			for(int i = 0;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);
				gvo.getHeaderVO().setDaccountdate(new UFDate(date));
				gvo.getHeaderVO().setClogdatenow(date);
			}
			gbillvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
		}
		return gbillvo;
	}

	
	/**
	 * 
	 * @作者：zpm
	 * @说明：完达山物流项目 查询 物流出库单的 货位信息
	 * @时间：2011-11-3上午11:30:04
	 * @param value
	 * @throws UifException
	 */
	public void setLocatorVO(AggregatedValueObject value) throws UifException {
		if(value==null || value.getParentVO()== null 
				|| value.getChildrenVO() == null || value.getChildrenVO().length == 0){
			return;
		}
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) value.getChildrenVO();
		corp =outhvo.getPk_corp();
		//		zhf  modify  on 2011 12 27
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
