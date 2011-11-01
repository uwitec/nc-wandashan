package nc.bs.wds.ic.so.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
/**
 * 
 * @author zpm
 *  ͬ�� ERP������۳��ⵥ��4C��
 */
public class ChangeTo4C {
	
	private  String beanName = IGeneralBill.class.getName(); 
	private boolean isReturn = false;
	private String corp=null;
	
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
		// �������۳���� ��ͷ
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		String where  = " cfirstbillhid = '"+outhvo.getPrimaryKey()+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4C", voCond);
		if(alListData!=null && alListData.size()>0){
			for(int i = 0 ;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);
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
		String pk_billtype = (String)billVO.getParentVO().getAttributeValue("vbilltype");
		if(pk_billtype == null || "".equals(pk_billtype)){
			return null;
		}
		setLocatorVO(billVO); //���û�λ��Ϣ
		AggregatedValueObject vo = PfUtilTools.runChangeData(pk_billtype, "4C", billVO,null); //���۳���
		setSpcGenBillVO(vo,coperator,date);
		return vo;
	}
	
	public void setSpcGenBillVO(AggregatedValueObject billVO,String coperator,String date){
		String para = null;
		if(!isReturn){
			para =getVbatchCode();						
		}
		if(billVO != null && billVO instanceof GeneralBillVO){
			GeneralBillVO bill = (GeneralBillVO)billVO;
			bill.setGetPlanPriceAtBs(false);//����Ҫ��ѯ�ƻ���
			bill.getHeaderVO().setCoperatoridnow(coperator);//��ǰ������///ҵ�������������ǰ������Ա
			bill.getHeaderVO().setDbilldate(new UFDate(date));//��������
			bill.getHeaderVO().setStatus(VOStatus.NEW);//��������״̬
			if(bill.getItemVOs()!=null && bill.getItemVOs().length>0){
				for(int i = 0 ;i<bill.getItemVOs().length;i++){
					bill.getItemVOs()[i].setCrowno(String.valueOf((i+1)*10));//�к�
					bill.getItemVOs()[i].setStatus(VOStatus.NEW);//��������״̬
					if(bill.getItemVOs()[i].getDbizdate() == null){
						bill.getItemVOs()[i].setDbizdate(new UFDate(date));//ҵ������
					}
					if(!isReturn){
						bill.getItemVOs()[i].setVbatchcode(para);				
					}					
					//���û�λ��Ϣ
					String key  = bill.getItemVOs()[i].getCfirstbillbid();
					ArrayList<LocatorVO> lvo = l_map.get(key);
					if(lvo!=null && lvo.size()>0){
						bill.getItemVOs()[i].setLocator(lvo.toArray(new LocatorVO[0]));
					}
				}
			}
		}
	}
	//��λ��Ϣ
	public void setLocatorVO(AggregatedValueObject value) {
		if(value==null || value.getParentVO()== null 
				|| value.getChildrenVO() == null || value.getChildrenVO().length == 0){
			return;
		}
		TbOutgeneralHVO outhvo = (TbOutgeneralHVO) value.getParentVO();
		TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) value.getChildrenVO();
		isReturn = PuPubVO.getUFBoolean_NullAs(outhvo.getIs_yundan(),UFBoolean.FALSE).booleanValue();
		corp =outhvo.getPk_corp();
		//
		for(int i = 0 ;i<bvos.length;i++){
			String key = bvos[i].getGeneral_b_pk();
			List<TbOutgeneralTVO> list = bvos[i].getTrayInfor();
			if(list == null || list.size() == 0)
				continue;
			for(int j =0 ;j < list.size();j++){
				TbOutgeneralTVO tvo = list.get(j);
				LocatorVO lvo = new LocatorVO();
				lvo.setPk_corp(outhvo.getPk_corp());
				lvo.setNoutspacenum(tvo.getNoutnum());
				lvo.setNoutspaceassistnum(tvo.getNoutassistnum());
				lvo.setCspaceid(tvo.getPk_cargdoc());//��λ
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
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * ���ɽ������д��Ӧ�������κţ�Ĭ����2009����ͨ������������
	 * @ʱ�䣺2011-4-20����11:57:57
	 * @return
	 */
	private String getVbatchCode(){
		ISysInitQry sysinitQry = (ISysInitQry) NCLocator.getInstance().lookup(ISysInitQry.class.getName());
		String para = "2009";
		try {
			SysInitVO vo =sysinitQry.queryByParaCode(corp, "WDS00");
			 para = vo.getValue();
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println("��ȡ����WDS00ʧ��");
		}
		return para;
	}
}
