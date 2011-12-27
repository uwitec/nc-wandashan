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
 * 其他入库
 * @author zpm
 * 同步库存库存其它入库单(4A)
 */
public class ChangeTo4A {
	
	private  String beanName = IGeneralBill.class.getName(); 
	
	private String s_billtype = "4A";
	private String corp = null;//当前登录公司pk
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
	 * @功能：取消签字动作
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject value,String coperator,String date) throws Exception {
		if(value == null ){
			return null;
		}
		GeneralBillVO[] billvo = null;
		//其它入库
		TbGeneralHVO outhvo = (TbGeneralHVO) value.getParentVO();
		String where  = " cfirstbillhid = '"+outhvo.getPrimaryKey()+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills(s_billtype, voCond);
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
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject billVO,String coperator,String date) throws Exception {
		if(billVO==null){
			return null;
		}
		String pk_billtype = (String)billVO.getParentVO().getAttributeValue("geh_billtype");
		if(pk_billtype == null || "".equals(pk_billtype)){
			return null;
		}
		setLocatorVO(billVO); //设置货位信息
		AggregatedValueObject vo = PfUtilTools.runChangeData(pk_billtype, s_billtype, billVO,null); //其它入库
		setSpcGenBillVO(vo,coperator,date);
		return vo;
	}
	
	public void setSpcGenBillVO(AggregatedValueObject billVO,String coperator,String date) throws BusinessException{
		if(billVO != null && billVO instanceof GeneralBillVO){
			GeneralBillVO bill = (GeneralBillVO)billVO;
			String para = getVbatchCode();
			bill.setGetPlanPriceAtBs(false);//不需要查询计划价
//			bill.getHeaderVO().setCoperatorid(coperator);//制单人
			bill.getHeaderVO().setCoperatoridnow(coperator);//当前操作人///业务加锁，锁定当前操作人员

			Integer dates= getDefaultDay();
	//		UFDate fs=NextMonth();
			Date dqdate= new Date();
			int dqday= dqdate.getDate();
			int jzday=dates.intValue();
			
			//如果当前期小于等于结账期，则传ERP的出入库单单据期为当前期；
			//如果当前期大于结账期，则传EPR单据为下一个月1号
			if(dqday<=jzday){
				bill.getHeaderVO().setDbilldate(new UFDate(date));//单据日期
			}else{
				bill.getHeaderVO().setDbilldate(NextMonth());//单据日期
			}
			bill.getHeaderVO().setStatus(VOStatus.NEW);//单据新增状态
			if(bill.getItemVOs()!=null && bill.getItemVOs().length>0){
				for(int i = 0 ;i<bill.getItemVOs().length;i++){
					bill.getItemVOs()[i].setCrowno(String.valueOf((i+1)*10));//行号
					bill.getItemVOs()[i].setStatus(VOStatus.NEW);//单据新增状态
					if(bill.getItemVOs()[i].getDbizdate() == null){
						bill.getItemVOs()[i].setDbizdate(new UFDate(date));//业务日期						
					}
					if(!isReturn){
						para =getVbatchCode();
						bill.getItemVOs()[i].setVbatchcode(para);
					}
					bill.getItemVOs()[i].setVbatchcode(para);
					//设置货位信息
					String key  = bill.getItemVOs()[i].getCfirstbillbid();
					ArrayList<LocatorVO> lvo = l_map.get(key);
					if(lvo!=null && lvo.size()>0){
						bill.getItemVOs()[i].setLocator(lvo.toArray(new LocatorVO[0]));
					}
				}
			}
		}
	}
	
	//当前月的下一个月一号   日期
	private UFDate NextMonth(){
        Calendar   calendar   =   new   GregorianCalendar(); 
        calendar.set(Calendar.DAY_OF_MONTH,  calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 1); //设置某个月第一天
		return new UFDate(calendar.getTime());		
	}
	
	/**
	 * 
	 * @作者：lyf 查询结账期默认值
	 * @说明：完达山物流项目 
	 * @时间：2011-12-20上午10:23:43
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 完达山物流回写供应链的批次号，默认是2009，可通过参数来配置
	 * @时间：2011-4-20上午11:57:57
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
			System.out.println("获取参数WDS00失败");
		}
		return para;
	}
	//货位信息
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
				lvo.setNinspacenum(tvo.getGebb_num());//主数量
				lvo.setNinspaceassistnum(tvo.getNinassistnum());//辅数量
				lvo.setCspaceid(tvo.getPk_cargdoc());//货位
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
