package nc.bs.wds.ic.soin.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
		GeneralBillVO vo = (GeneralBillVO) PfUtilTools.runChangeData(
				pk_billtype, "4C", billVO, null); // ���۳���
		setSpcGenBillVO(vo, coperator, date);
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
		String cid = hvo.getPrimaryKey()==null ?"":hvo.getPrimaryKey();
		String where  = " body.cfirstbillhid = '"+cid+"'";
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
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-3����11:30:45
	 * @param bill
	 * @param coperator
	 * @param date
	 */
	public void setSpcGenBillVO(GeneralBillVO bill,String coperator,String date){
		if(bill == null)
			return;
		String para = getVbatchCode();

		WdsWlIcPubDealTool.appFieldValueForIcNewBill(bill, l_map, coperator, date, fisvbatchcontorl, para);

		if(fisvbatchcontorl == null || fisvbatchcontorl == UFBoolean.FALSE){
			//		������ش����κ�  Ӧ�ð���  ��Դ����id + ���κ�  ���л��ܴ���------zhf		
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(bill, false);
		}
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
//			String str = " geb_pk ='"+key+"'";
//			TbGeneralBBVO[] tvos = (TbGeneralBBVO[] )getHypubBO().queryByCondition(TbGeneralBBVO.class, str	);
//			bvos[i].setTrayInfor(Arrays.asList(tvos));
//			List<TbGeneralBBVO> list = bvos[i].getTrayInfor();
//			if(list == null || list.size() == 0)
//				continue;
//			for(int j =0 ;j < list.size();j++){
//				TbGeneralBBVO tvo = list.get(j);
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
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ���ɽ������д��Ӧ�������κţ�Ĭ����2009����ͨ������������
	 * @ʱ�䣺2011-4-20����11:57:57
	 * @return
	 */
	private String getVbatchCode(){
		return WdsWlIcPubDealTool.getDefaultVbatchCode(corp);
	}
}
