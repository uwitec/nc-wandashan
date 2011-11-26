package nc.bs.wds.ic.allocation.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.busibean.ISysInitQry;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.TbGeneralBBVO;
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
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.para.SysInitVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.ic.write.back4y.MultiBillVO;
import nc.vo.wds.ic.write.back4y.Writeback4yB2VO;
import nc.vo.wds.ic.write.back4y.Writeback4yHVO;
/**
 * ���۳���ش��� �� ��Ӧ���������
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
	 * @���ܣ�ǩ�ֶ���:�õ�������ⵥ
	 */
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return bill;
		}
		this.coperator = coperator;
		this.date = date;
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4yHVO hvo = (Writeback4yHVO)billvo.getParentVO();
//		fisvbatchcontorl= hvo.getFisvbatchcontorl();�Ƿ����ι���
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
		//���ݵ������ش������������е�����⣨WDS9�����ֱ��Ӧ������erp������ⵥ(4E)
		GeneralBillVO vo = getGeneralBillVO( hvo,general_hs);
		if(vo == null){
			return null;
		}
		//�Ƿ�ش����Σ�������ش����Σ���ͻ�Ҫ��������۶������ܣ����κ�ͳһΪ ������Ӧ��ֵ
		if(fisvbatchcontorl == null || fisvbatchcontorl == UFBoolean.FALSE){
			GeneralBillItemVO[]  items = vo.getItemVOs();
			Map<String, GeneralBillItemVO> map = new HashMap<String, GeneralBillItemVO>();
			if(items !=null){
				int i=10;
				for(GeneralBillItemVO item:items){
					String key = item.getCsourcebillbid();
					if(key == null || "".equalsIgnoreCase(key)){
						continue;
					}
					if(map.containsKey(key)){
						GeneralBillItemVO oldItem = map.get(key);
						UFDouble oldsout = PuPubVO.getUFDouble_NullAsZero(oldItem.getNshouldoutnum());
						UFDouble oldsoutass =  PuPubVO.getUFDouble_NullAsZero(oldItem.getNshouldoutassistnum());
						UFDouble oldout =  PuPubVO.getUFDouble_NullAsZero(oldItem.getNoutnum());
						UFDouble oldoutass =  PuPubVO.getUFDouble_NullAsZero(oldItem.getNoutassistnum());
						LocatorVO[]  oldLoctor = oldItem.getLocator();
						
						//�������� ������Ϣ�ͻ�λ��Ϣ
						UFDouble newsout = PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum());
						UFDouble newsoutass =  PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutassistnum());
						UFDouble newout =  PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
						UFDouble newoutass =  PuPubVO.getUFDouble_NullAsZero(item.getNoutassistnum());
						LocatorVO[]  newLoctor = item.getLocator();

						oldItem.setNshouldoutnum(oldsout.add(newsout));
						oldItem.setNshouldoutassistnum(oldsoutass.add(newsoutass));
						oldItem.setNoutnum(oldout.add(newout));
						oldItem.setNoutassistnum(oldoutass.add(newoutass));
						ArrayList<LocatorVO> list= new ArrayList<LocatorVO>();
						if(oldLoctor != null){
							list.addAll(Arrays.asList(oldLoctor));
						}
						if(newLoctor != null){
							list.addAll(Arrays.asList(newLoctor));
						}
						oldItem.setLocator(list.toArray(new LocatorVO[0]));
						
					}else{
						item.setCrowno(""+i);
						map.put(key, item);
						i=i+10;
					}
				}
			}
			vo.setChildrenVO(map.values().toArray(new GeneralBillItemVO[0]));
		}
		return vo;
	}
	/**
	 * 
	 * @���ߣ�liuys
	 * @˵�������ɽ������Ŀ:�������ش����������ɹ�Ӧ��������ⵥ 
	 * @ʱ�䣺2011-11-26����02:52:09
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
			String pk_billtype = (String)billVO.getParentVO().getAttributeValue("geh_billtype");
			if(pk_billtype == null || "".equals(pk_billtype)){
				return null;
			}
			 //���û�λ��Ϣ
			setLocatorVO(billVO);
			GeneralBillVO vo =(GeneralBillVO) PfUtilTools.runChangeData(pk_billtype, "4E", billVO,null); //�������
			setSpcGenBillVO(vo,coperator,date);
			if(i == 0){
				head = vo.getHeaderVO();
			}
			for(GeneralBillItemVO item:vo.getItemVOs()){
				item.setCfirstbillhid(hvo.getPrimaryKey());
			}
			items.addAll(Arrays.asList(vo.getItemVOs()));
		}
		bill.setParentVO(head);
		bill.setChildrenVO(items.toArray(new GeneralBillItemVO[0]));
		return bill;
	}
	
//	/**
//	 * @���ܣ�ȡ��ǩ�ֶ���
//	 */
//	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
//		if(bill==null){
//			return null;
//		}
//		this.coperator = coperator;
//		this.date = date;
//		MultiBillVO billvo = (MultiBillVO)bill;
//		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
//		String csaleid = hvo.getCsaleid()==null ?"":hvo.getCsaleid();
//		String where  = " csourcebillhid = '"+csaleid+"' ";
//		QryConditionVO voCond = new QryConditionVO(where);
//	    ArrayList alListData = (ArrayList)queryBills("4C", voCond);
//	    GeneralBillVO[] gbillvo = null;
//		if(alListData!=null && alListData.size()>0){
//			for(int i = 0 ;i<alListData.size();i++){
//				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
//				gvo.getHeaderVO().setCoperatoridnow(coperator);
//				gvo.getHeaderVO().setDaccountdate(new UFDate(date));
//				gvo.getHeaderVO().setClogdatenow(date);
//			}
//			gbillvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
//		}
//		return gbillvo;
//	}
	/**
	 * 
	 * @���ߣ�liuys
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-11-26����02:52:09
	 * @param bill
	 * @param coperator
	 * @param date
	 */
	public void setSpcGenBillVO(GeneralBillVO bill,String coperator,String date){
		String para = getVbatchCode();
		if(bill != null && bill instanceof GeneralBillVO){
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
					if(fisvbatchcontorl == null || fisvbatchcontorl == UFBoolean.FALSE){
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
	/**
	 * 
	 * @���ߣ�liuys
	 * @˵�������ɽ������Ŀ ��ѯ ����������ⵥ�� ��λ��Ϣ
	 * @ʱ�䣺2011-11-26����02:52:09
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
		for(int i = 0 ;i<bvos.length;i++){
			String key = bvos[i].getPrimaryKey();
			String str = " geb_pk ='"+key+"'";
			TbGeneralBBVO[] tvos = (TbGeneralBBVO[] )getHypubBO().queryByCondition(TbGeneralBBVO.class, str	);
			bvos[i].setTrayInfor(Arrays.asList(tvos));
			List<TbGeneralBBVO> list = bvos[i].getTrayInfor();
			if(list == null || list.size() == 0)
				continue;
			for(int j =0 ;j < list.size();j++){
				TbGeneralBBVO tvo = list.get(j);
				LocatorVO lvo = new LocatorVO();
				lvo.setPk_corp(outhvo.getPk_corp());
				lvo.setNinspacenum(tvo.getGebb_num());
				lvo.setNinspaceassistnum(tvo.getNinassistnum());
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
			if(vo != null){
				para = vo.getValue();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println("��ȡ����WDS00ʧ��");
		}
		return para;
	}
}
