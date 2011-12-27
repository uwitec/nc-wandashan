package nc.bs.wds.ic.other.in;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.pub.TbGeneralBBVO;
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
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
/**
 * �������
 * @author zpm
 * ͬ�������������ⵥ(4A)
 */
public class ChangeTo4A {
	
	private  String beanName = IGeneralBill.class.getName(); 
	
	private String s_billtype = "4A";
	private String corp = null;//��ǰ��¼��˾pk
	private boolean isReturn = false;
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
		String where  = " cfirstbillhid = '"+outhvo.getPrimaryKey()+"' ";
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
		String pk_billtype = (String)billVO.getParentVO().getAttributeValue("geh_billtype");
		if(pk_billtype == null || "".equals(pk_billtype)){
			return null;
		}
		setLocatorVO(billVO); //���û�λ��Ϣ
		AggregatedValueObject vo = PfUtilTools.runChangeData(pk_billtype, s_billtype, billVO,null); //�������
		setSpcGenBillVO(vo,coperator,date);
		return vo;
	}
	
	public void setSpcGenBillVO(AggregatedValueObject billVO,String coperator,String date) throws BusinessException{
		if(billVO != null && billVO instanceof GeneralBillVO){
			GeneralBillVO bill = (GeneralBillVO)billVO;
			String para = getVbatchCode();
			bill.setGetPlanPriceAtBs(false);//����Ҫ��ѯ�ƻ���
//			bill.getHeaderVO().setCoperatorid(coperator);//�Ƶ���
			bill.getHeaderVO().setCoperatoridnow(coperator);//��ǰ������///ҵ�������������ǰ������Ա

			Integer dates= getDefaultDay();
	//		UFDate fs=NextMonth();
			Date dqdate= new Date();
			int dqday= dqdate.getDate();
			int jzday=dates.intValue();
			
			//�����ǰ��С�ڵ��ڽ����ڣ���ERP�ĳ���ⵥ������Ϊ��ǰ�ڣ�
			//�����ǰ�ڴ��ڽ����ڣ���EPR����Ϊ��һ����1��
			if(dqday<=jzday){
				bill.getHeaderVO().setDbilldate(new UFDate(date));//��������
			}else{
				bill.getHeaderVO().setDbilldate(NextMonth());//��������
			}
			bill.getHeaderVO().setStatus(VOStatus.NEW);//��������״̬
			if(bill.getItemVOs()!=null && bill.getItemVOs().length>0){
				for(int i = 0 ;i<bill.getItemVOs().length;i++){
					bill.getItemVOs()[i].setCrowno(String.valueOf((i+1)*10));//�к�
					bill.getItemVOs()[i].setStatus(VOStatus.NEW);//��������״̬
					if(bill.getItemVOs()[i].getDbizdate() == null){
						bill.getItemVOs()[i].setDbizdate(new UFDate(date));//ҵ������						
					}
					if(!isReturn){
						para =getVbatchCode();
						bill.getItemVOs()[i].setVbatchcode(para);
					}
					bill.getItemVOs()[i].setVbatchcode(para);
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
	
	//��ǰ�µ���һ����һ��   ����
	private UFDate NextMonth(){
        Calendar   calendar   =   new   GregorianCalendar(); 
        calendar.set(Calendar.DAY_OF_MONTH,  calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 1); //����ĳ���µ�һ��
		return new UFDate(calendar.getTime());		
	}
	
	/**
	 * 
	 * @���ߣ�lyf ��ѯ������Ĭ��ֵ
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-12-20����10:23:43
	 * @throws BusinessException
	 */
	private Integer getDefaultDay() throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select datavale from wds_periodsetting_h ");
		sql.append(" where isnull(dr,0) =0 ");
		sql.append(" and pk_corp='"+corp+"'");
		Object value = getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
		return PuPubVO.getInteger_NullAs(value, 20);
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
	//��λ��Ϣ
	public void setLocatorVO(AggregatedValueObject value) {
		if(value==null || value.getParentVO()== null 
				|| value.getChildrenVO() == null || value.getChildrenVO().length == 0){
			return;
		}
		//nc.vo.ic.pub.TbGeneralBVO
		TbGeneralHVO outhvo = (TbGeneralHVO) value.getParentVO();
		TbGeneralBVO[] bvos = (TbGeneralBVO[]) value.getChildrenVO();
		isReturn = PuPubVO.getUFBoolean_NullAs(outhvo.getFisnewcode(),UFBoolean.FALSE).booleanValue();
		corp =outhvo.getPk_corp();
		for(int i = 0 ;i<bvos.length;i++){
			String key = bvos[i].getGeb_pk();
			List<TbGeneralBBVO> list = bvos[i].getTrayInfor();
			if(list == null || list.size() == 0)
				continue;			
			for(int j =0 ;j < list.size();j++){
				TbGeneralBBVO tvo = list.get(j);
				LocatorVO lvo = new LocatorVO();
				lvo.setPk_corp(outhvo.getPk_corp());
				lvo.setNinspacenum(tvo.getGebb_num());//������
				lvo.setNinspaceassistnum(tvo.getNinassistnum());//������
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
}
