package nc.bs.wds.ic.so.out;

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
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
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
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cB2VO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;
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
	 * @功能：签字动作
	 */
	public AggregatedValueObject signQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return bill;
		}
		this.coperator = coperator;
		this.date = date;
		//根据销售出库回传单，查找上有销售出库单（WDS8），然后分别对应交换成erp销售出库单(4C)
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		fisvbatchcontorl= hvo.getFisvbatchcontorl();
		Writeback4cB2VO[] b2vos = (Writeback4cB2VO[])billvo.getTableVO(billvo.getTableCodes()[1]);
		if(b2vos == null || b2vos.length ==0){
			return null;
		}
		List<String> general_hs = new ArrayList<String>();
		for(Writeback4cB2VO b2vo:b2vos){
			String general_h = b2vo.getCsourcebillhid();
			if(general_h == null || "".equalsIgnoreCase(general_h) 
					|| general_hs.contains(general_h)){
				continue;
			}
			general_hs.add(general_h);
		}
		GeneralBillVO vo = getGeneralBillVO(general_hs);
		if(vo == null){
			return null;
		}
		//是否回传批次：如果不回传批次，则客户要求根据销售订单汇总，批次号统一为 参数对应的值
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
						UFDouble newsout = PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutnum());
						UFDouble newsoutass =  PuPubVO.getUFDouble_NullAsZero(item.getNshouldoutassistnum());
						UFDouble newout =  PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
						UFDouble newoutass =  PuPubVO.getUFDouble_NullAsZero(item.getNoutassistnum());
						oldItem.setNshouldoutnum(oldsout.add(newsout));
						oldItem.setNshouldoutassistnum(oldsoutass.add(newsoutass));
						oldItem.setNoutnum(oldout.add(newout));
						oldItem.setNoutassistnum(oldoutass.add(newoutass));
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
	 * @作者：lyf
	 * @说明：完达山物流项目:交换生成销售出库单 
	 * @时间：2011-11-2下午02:52:09
	 * @param general_hs
	 * @return
	 */
	public GeneralBillVO getGeneralBillVO(List<String> general_hs) throws BusinessException{
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
			String pk_billtype = (String)billVO.getParentVO().getAttributeValue("vbilltype");
			if(pk_billtype == null || "".equals(pk_billtype)){
				return null;
			}
			setLocatorVO(billVO); //设置货位信息
			GeneralBillVO vo =(GeneralBillVO) PfUtilTools.runChangeData(pk_billtype, "4C", billVO,null); //销售出库
			setSpcGenBillVO(vo,coperator,date);
			if(i == 0){
				head = vo.getHeaderVO();
			}
			items.addAll(Arrays.asList(vo.getItemVOs()));
		}
		bill.setParentVO(head);
		bill.setChildrenVO(items.toArray(new GeneralBillItemVO[0]));
		return bill;
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
		//根据销售出库回传单，查找上有销售出库单（WDS8），然后分别对应交换成erp销售出库单(4C)
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		String csaleid = hvo.getCsaleid()==null ?"":hvo.getCsaleid();
		String where  = " csourcebillhid = '"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4C", voCond);
	    GeneralBillVO[] gbillvo = null;
		if(alListData!=null && alListData.size()>0){
			for(int i = 0 ;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);
			}
			gbillvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
		}
		return gbillvo;
	}
	/**
	 * 
	 * @作者：zpm
	 * @说明：完达山物流项目 
	 * @时间：2011-11-3上午11:30:45
	 * @param bill
	 * @param coperator
	 * @param date
	 */
	public void setSpcGenBillVO(GeneralBillVO bill,String coperator,String date){
		String para = getVbatchCode();
		if(bill != null && bill instanceof GeneralBillVO){
			bill.setGetPlanPriceAtBs(false);//不需要查询计划价
			bill.getHeaderVO().setCoperatoridnow(coperator);//当前操作人///业务加锁，锁定当前操作人员
			bill.getHeaderVO().setDbilldate(new UFDate(date));//单据日期
			bill.getHeaderVO().setStatus(VOStatus.NEW);//单据新增状态
			if(bill.getItemVOs()!=null && bill.getItemVOs().length>0){
				for(int i = 0 ;i<bill.getItemVOs().length;i++){
					bill.getItemVOs()[i].setCrowno(String.valueOf((i+1)*10));//行号
					bill.getItemVOs()[i].setStatus(VOStatus.NEW);//单据新增状态
					if(bill.getItemVOs()[i].getDbizdate() == null){
						bill.getItemVOs()[i].setDbizdate(new UFDate(date));//业务日期
					}	
					if(fisvbatchcontorl == null || fisvbatchcontorl == UFBoolean.FALSE){
						bill.getItemVOs()[i].setVbatchcode(para);
					}
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
		//
		for(int i = 0 ;i<bvos.length;i++){
			String key = bvos[i].getGeneral_b_pk();
			String str = " general_b_pk ='"+key+"'";
			TbOutgeneralTVO[] tvos = (TbOutgeneralTVO[] )getHypubBO().queryByCondition(TbOutgeneralTVO.class, str	);
			bvos[i].setTrayInfor(Arrays.asList(tvos));
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
			if(vo != null){
				para = vo.getValue();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			System.out.println("获取参数WDS00失败");
		}
		return para;
	}
}
