package nc.bs.wds.ic.other.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * �������
 * @author zpm
 * ͬ�������������ⵥ(4A)
 */
public class ChangeTo4A {
	
	private  String beanName = IGeneralBill.class.getName(); 
	
	private String s_billtype = "4A";
	private String corp = null;//��ǰ��¼��˾pk
	private UFBoolean isReturn = UFBoolean.FALSE;
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();;
	
	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	} 
	/**
	 * @���ܣ�ȡ��ǩ�ֶ���
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject value,String coperator,String date) throws Exception {
		if(value == null ){
			return null;
		}
		GeneralBillVO[] billvo = null;
		//�������
		TbGeneralHVO outhvo = (TbGeneralHVO) value.getParentVO();
		String where  = "body."+WdsWlPubConst.csourcehid_wds+"='"+outhvo.getPrimaryKey()+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills(s_billtype, voCond);
		if(alListData!=null && alListData.size()>0){
			for(int i = 0 ;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);//���ӵ�ǰ����Ա��ҵ��ԱPK����
			}
			billvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
		}
		return billvo;
	}
	
	/**
	 * @���ܣ�ǩ�ֶ���
	 */
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject billVO,String coperator,String date) throws Exception {
		if(billVO==null){
			return null;
		}
		TbGeneralHVO outhvo = (TbGeneralHVO) billVO.getParentVO();
		TbGeneralBVO[] outbodys = (TbGeneralBVO[])billVO.getChildrenVO();
		isReturn = PuPubVO.getUFBoolean_NullAs(outhvo.getFisnewcode(),UFBoolean.FALSE);
		corp =outhvo.getPk_corp();
		String pk_billtype = PuPubVO.getString_TrimZeroLenAsNull(outhvo.getGeh_billtype());
		if(pk_billtype == null){
			return null;
		}
		setLocatorVO(outbodys); //���û�λ��Ϣ
		GeneralBillVO vo = (GeneralBillVO)PfUtilTools.runChangeData(pk_billtype, s_billtype, billVO,null); //�������
		
		WdsWlIcPubDealTool.appFieldValueForIcNewBill(vo, l_map, corp,coperator, date, isReturn,getBaseDAO());

		if(!isReturn.booleanValue()){
			WdsWlIcPubDealTool.combinItemsBySourceAndInv(vo, true);
		}
		
		return vo;
	}

	//��λ��Ϣ
	public void setLocatorVO(TbGeneralBVO[] bvos) {
		if(bvos == null || bvos.length == 0){
			return;
		}
//		zhf   2011 12 27  ����    
		for(TbGeneralBVO bvo:bvos){
			String key = bvo.getPrimaryKey();
			LocatorVO lvo = new LocatorVO();
			lvo.setPk_corp(corp);
			lvo.setNinspacenum(bvo.getGeb_anum());
			lvo.setNinspaceassistnum(bvo.getGeb_banum());
			lvo.setCspaceid(bvo.getGeb_space());//��λ
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
