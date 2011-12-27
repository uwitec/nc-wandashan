package nc.bs.wds.ic.other.out;

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
	private boolean isReturn = false;
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
	    ArrayList alListData = (ArrayList)queryBills("4C", voCond);
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
		String pk_billtype = (String)billVO.getParentVO().getAttributeValue("vbilltype");
		if(pk_billtype == null || "".equals(pk_billtype)){
			return null;
		}
		setLocatorVO(billVO); //设置货位信息
		AggregatedValueObject vo = PfUtilTools.runChangeData(pk_billtype, s_billtype, billVO,null); //其它出库
		setSpcGenBillVO(vo,coperator,date);
		return vo;
	}
	/**
	 * 
	 * @作者：zpm
	 * @说明：完达山物流项目   设置表体参数数据
	 * @时间：2011-4-20上午11:58:28
	 * @param billVO
	 * @param coperator
	 * @param date
	 * @throws BusinessException 
	 */
	public void setSpcGenBillVO(AggregatedValueObject billVO,String coperator,String date) throws BusinessException{
		String para = null;
		if(!isReturn){
			para =getVbatchCode();
		}
		if(billVO != null && billVO instanceof GeneralBillVO){
			GeneralBillVO bill = (GeneralBillVO)billVO;
			bill.setGetPlanPriceAtBs(false);//不需要查询计划价
			bill.getHeaderVO().setCoperatoridnow(coperator);//当前操作人///业务加锁，锁定当前操作人员
			
			
			Integer dates= getDefaultDay();
			UFDate fs=NextMonth();
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
						bill.getItemVOs()[i].setVbatchcode(para);
					}
					//设置货位信息csourcebillbid
					String key  = bill.getItemVOs()[i].getCsourcebillbid();
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
        calendar.add(Calendar.DAY_OF_MONTH,  calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, 1); //设置某个月第一天
      
        int year = calendar.get(Calendar.YEAR); 
        int month = calendar.get(Calendar.MONTH)+1;//这里月要加1 
        String de=  year + "-" + month + "-1";    
		return new UFDate(de);		
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
	/**
	 * 
	 * @作者：zpm
	 * @说明：完达山物流项目 设置货位信息
	 * @时间：2011-4-20上午11:35:31
	 * @param value
	 */
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
