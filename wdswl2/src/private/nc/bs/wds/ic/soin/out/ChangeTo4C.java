package nc.bs.wds.ic.soin.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author lyf 
 * ͬ����ERP���۳��ⵥ
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
	 * @���ܣ�ǩ�ֶ���:�õ����۳��ⵥ
	 */
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws BusinessException {
		if(bill==null){
			return null;
		}
		this.coperator = coperator;
		this.date = date;
		OtherInBillVO billvo = (OtherInBillVO)bill;
		TbGeneralHVO hvo = (TbGeneralHVO)billvo.getParentVO();
		fisvbatchcontorl= PuPubVO.getUFBoolean_NullAs(hvo.getFisnewcode(),UFBoolean.FALSE);
	
		//ԭ���۳���ش�������vo
		TbGeneralBVO[] bvos = (TbGeneralBVO[])billvo.getTableVO(billvo.getTableCodes()[0]);
		if(bvos == null || bvos.length ==0){
			return null;
		}
	
		GeneralBillVO vo = getGeneralBillVO(billvo);
		if(vo == null){
			return null;
		}
		return vo;
	}	
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:�����������۳��ⵥ 
	 * @ʱ�䣺2011-11-2����02:52:09
	 * @param general_hs
	 * @return
	 */
	public GeneralBillVO getGeneralBillVO(OtherInBillVO billVO)
			throws BusinessException {
		if (billVO == null) {
			return null;
		}

		String pk_billtype = PuPubVO.getString_TrimZeroLenAsNull(billVO
				.getParentVO().getAttributeValue("geh_billtype"));
		if (pk_billtype == null) {
			return null;
		}
		
		// ���û�λ��Ϣ
		setLocatorVO(billVO);		
	
		if(!fisvbatchcontorl.booleanValue()){
			String debatchcode = WdsWlIcPubDealTool.getDefaultVbatchCode(corp);
			TbGeneralBVO[] bvos = (TbGeneralBVO[]) billVO.getChildrenVO();
			for(TbGeneralBVO bvo:bvos){
				bvo.setGeb_vbatchcode(debatchcode);
			}
		}
		
		GeneralBillVO vo = (GeneralBillVO) PfUtilTools.runChangeData(
				pk_billtype, "4C", billVO, null); // ���۳���
		
		WdsWlIcPubDealTool.appFieldValueForIcNewBill(vo, l_map, corp,coperator, date, fisvbatchcontorl,getBaseDAO());
		
		if(!fisvbatchcontorl.booleanValue()){
			//		������ش����κ�  Ӧ�ð���  ��Դ����id + ���κ�  ���л��ܴ���------zhf		
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(vo, false);
		}
		return vo;
	}
	
	/**
	 * @���ܣ�ȡ��ǩ�ֶ���
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return null;
		}
		this.coperator = coperator;
		this.date = date;
		OtherInBillVO billvo = (OtherInBillVO)bill;
		TbGeneralHVO hvo = (TbGeneralHVO)billvo.getParentVO();
		String csaleid = hvo.getPrimaryKey()==null ?"":hvo.getPrimaryKey();
		String where  = "body."+WdsWlPubConst.csourcehid_wds+"='"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
		ArrayList alListData = (ArrayList)queryBills("4C", voCond);		

		if(alListData == null || alListData.size()  == 0)
			return null;

		for(int i = 0 ;i<alListData.size();i++){
			GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
			gvo.getHeaderVO().setCoperatoridnow(coperator);
			gvo.getHeaderVO().setDaccountdate(new UFDate(date));
			gvo.getHeaderVO().setClogdatenow(date);
		}

		return (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
	}

	/**
	 * 
	 * @���ߣ�zpm
	 * @˵�������ɽ������Ŀ ��ѯ �������ⵥ�� ��λ��Ϣ
	 * @ʱ�䣺2011-11-3����11:30:04
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
		//
		for(TbGeneralBVO bvo:bvos){
			String key = bvo.getGeb_pk();
			LocatorVO lvo = new LocatorVO();
			lvo.setPk_corp(outhvo.getPk_corp());
			lvo.setNoutspacenum(bvo.getGeb_anum().multiply(-1));
			lvo.setNoutspaceassistnum(bvo.getGeb_banum().multiply(-1));
			lvo.setCspaceid(bvo.getGeb_space());//��λ
			lvo.setStatus(VOStatus.NEW);
			if(l_map.containsKey(key)){
				l_map.get(key).add(lvo);
			}else{
				ArrayList<LocatorVO> zList = new ArrayList<LocatorVO>();
				zList.add(lvo);
				l_map.put(key, zList);
			}
			//			}
		}
	}
}