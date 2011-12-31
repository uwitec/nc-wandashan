package nc.bs.wds.ic.allocation.in;

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
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
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
import nc.vo.wds.ic.write.back4y.MultiBillVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 调拨入库回传单 传 供应链调拨入库
 * @author liuys
 *
 */

public class ChangeTo4E {
	
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
	
	public  ArrayList<?> queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList<?> o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	}
	
	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();	
	
	
	/**
	 * @功能：签字动作:得到调拨入库单
	 */
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return bill;
		}
		this.coperator = coperator;
		this.date = date;
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4yHVO hvo = (Writeback4yHVO)billvo.getParentVO();
		fisvbatchcontorl= PuPubVO.getUFBoolean_NullAs(hvo.getFisvbatchcontorl(), UFBoolean.FALSE);//是否批次管理
		Writeback4yB2VO[] b2vos = (Writeback4yB2VO[])billvo.getTableVO(billvo.getTableCodes()[1]);
		if(b2vos == null || b2vos.length ==0){
			return null;
		}
		List<String> general_hs = new ArrayList<String>();
		for(Writeback4yB2VO b2vo:b2vos){
			String general_h = b2vo.getCsourcebillhid();
			if(general_h == null || "".equalsIgnoreCase(general_h) 
					|| general_hs.contains(general_h)){
				continue;
			}
			general_hs.add(general_h);
		}
		//根据调拨入库回传单，查找上有调拨入库（WDS9），分别对应交换成erp调拨入库单(4E)
		GeneralBillVO vo = getGeneralBillVO( hvo,general_hs);
		
		return vo;
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
		Writeback4yHVO hvo = (Writeback4yHVO)billvo.getParentVO();
		String csaleid = hvo.getCgeneralhid()==null ?"":hvo.getCgeneralhid();
		String where  = " csourcebillhid = '"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4E", voCond);
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
	 * @作者：liuys
	 * @说明：完达山物流项目:调拨入库回传单交换生成供应链调拨入库单 
	 * @时间：2011-11-26下午02:52:09
	 * @param general_hs
	 * @return
	 */
	public GeneralBillVO getGeneralBillVO(Writeback4yHVO hvo,List<String> general_hs) throws BusinessException{
		if(general_hs == null || general_hs.size() ==0){
			return null;
		}
		Object userObj = new String[]{HYBillVO.class.getName(),
				TbGeneralHVO.class.getName(),TbGeneralBVO.class.getName()};
		GeneralBillVO bill = new GeneralBillVO();
		GeneralBillHeaderVO head = null;
		List<GeneralBillItemVO> items = new ArrayList<GeneralBillItemVO>();
		for(int i=0;i<general_hs.size();i++){
			AggregatedValueObject billVO = getHypubBO().queryBillVOByPrimaryKey(userObj, general_hs.get(i));
			String pk_billtype = PuPubVO.getString_TrimZeroLenAsNull(billVO.getParentVO().getAttributeValue("geh_billtype"));
			if(pk_billtype == null){
				continue;
			}
			 //设置货位信息
			setLocatorVO(billVO);
			GeneralBillVO vo =(GeneralBillVO) PfUtilTools.runChangeData(pk_billtype, "4E", billVO,null); //调拨入库
//			setSpcGenBillVO(vo,coperator,date);
			if(i == 0){
				head = vo.getHeaderVO();
			}
			for(GeneralBillItemVO item:vo.getItemVOs()){
				item.setAttributeValue(WdsWlPubConst.csourcehid_wds, hvo.getPrimaryKey());
			}
			items.addAll(Arrays.asList(vo.getItemVOs()));
		}
		bill.setParentVO(head);
		bill.setChildrenVO(items.toArray(new GeneralBillItemVO[0]));
		
		WdsWlIcPubDealTool.appFieldValueForIcNewBill(bill, l_map, corp,coperator, date, fisvbatchcontorl,getBaseDAO());

		if(!fisvbatchcontorl.booleanValue()){
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(bill, true);
		}
		
		return bill;
	}

	/**
	 * 
	 * @作者：liuys
	 * @说明：完达山物流项目 查询 物流调拨入库单的 货位信息
	 * @时间：2011-11-26下午02:52:09
	 * @param value
	 * @throws UifException
	 */
	public void setLocatorVO(AggregatedValueObject value) throws UifException {
		if(value==null || value.getParentVO()== null 
				|| value.getChildrenVO() == null || value.getChildrenVO().length == 0){
			return;
		}
		TbGeneralHVO outhvo = (TbGeneralHVO) value.getParentVO();
		TbGeneralBVO[] bvos = (TbGeneralBVO[]) value.getChildrenVO();
		corp =outhvo.getPk_corp();
//		zhf   2011 12 27  调整    
		for(TbGeneralBVO bvo:bvos){
			String key = bvo.getPrimaryKey();
			LocatorVO lvo = new LocatorVO();
			lvo.setPk_corp(corp);
			lvo.setNinspacenum(bvo.getGeb_anum());
			lvo.setNinspaceassistnum(bvo.getGeb_banum());
			lvo.setCspaceid(bvo.getGeb_space());//货位
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
