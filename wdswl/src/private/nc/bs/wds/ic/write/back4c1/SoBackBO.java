package nc.bs.wds.ic.write.back4c1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cB1VO;
import nc.vo.wds.ic.write.back4c.Writeback4cB2VO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report2.CombinVO;
/**
 * 调拨出库回传单处理类
 * @author mlr
 */
public class SoBackBO{
	private  String beanName = IGeneralBill.class.getName(); 

	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();	
	BaseDAO dao = null;
	private String corp=null;
	private String coperator = null;
	private String date = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 处理 调拨出库回传 到供应链调拨出库的数据交换 即 保存生成供应链的调拨出库单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-12上午10:56:07
	 * @param billvo
	 * @throws Exception 
	 */
	public void  changeTo4Y(PfParameterVO vo) throws Exception{
		MultiBillVO billvo =(MultiBillVO) vo.m_preValueVo;
		if(billvo==null)
			throw new Exception("数据为空");
	    if(billvo.getHeadVO()==null)
	    	throw new Exception("表头为空");
		if(billvo.getTableVO(billvo.getTableCodes()[0])==null|| billvo.getTableVO(billvo.getTableCodes()[0]).length==0)
			throw new Exception("表体为空");
		if(billvo.getTableVO(billvo.getTableCodes()[1])==null|| billvo.getTableVO(billvo.getTableCodes()[1]).length==0)
			throw new Exception("表体为空");
		//获得调拨订单
		Writeback4cHVO hvo=billvo.getHeadVO();		
		String vcode =hvo.getCsaleid();//获得调拨订单id
	    BillVO bill=getSaleOrder(vcode);
	    //进行分担 按调出仓库  调入仓库分担 因为调拨出库单仓库信息在表头 而调拨订单仓库信息在表体
	    BillVO[] bills= (BillVO[]) SplitBillVOs.getSplitVO(
	    		BillVO.class.getName(), BillHeaderVO.class.getName(), 
	    		BillItemVO.class.getName(), bill, null, new String[]{"coutwhid","cinwhid"});
	    
		//进行数据交换  获得调拨出库
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator =vo.m_operator;
		paraVo.m_coId = vo.m_coId;		
		paraVo.m_currentDate = vo.m_currentDate;
		paraVo.m_preValueVos=bills;
		
	    corp=vo.m_operator;
		coperator = vo.m_coId;
		date = vo.m_currentDate;
		GeneralBillVO[] orderVos = (GeneralBillVO[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDSX, "4Y",
						bills, paraVo);
	   //进行数据过滤   处理
		deal(orderVos,billvo);
		for (int i = 0; i < orderVos.length; i++) {
			if(orderVos[i].getChildrenVO()==null || orderVos[i].getChildrenVO().length==0)
				continue;
			// 执行保存操作
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator
					.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList) bsBusiAction.processAction("SAVE",
					"4Y", date, null, orderVos[i], null, null);
			//执行签字操作
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			orderVos[i].setSmallBillVO(smbillvo);
			//签字检查 <->[签字日期和表体业务日期]
			//当前操作人<->[业务加锁，锁定当前操作人员]
			//空货位检查 bb1表
			bsBusiAction.processAction("SIGN", "4Y",date,null,orderVos[i], null,null); //签字后续放开
		}
	//	throw new Exception("css");


	}
	/**
	 * 将调拨订单 ->供应链的调拨出库单 交换后的单据vo  设置应发数量 和 实发数量 并设置货位信息
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-12下午12:12:28
	 * @param orderVos
	 * @param billvo
	 * @throws UifException 
	 */
	public void deal(GeneralBillVO[] orderVos, MultiBillVO billvo) throws UifException {
		if(orderVos==null  || orderVos.length==0){
			return;
		}
		String pk=billvo.getHeadVO().getPrimaryKey();
		for (int k = 0; k < orderVos.length; k++) {
			GeneralBillVO bill = orderVos[k];
			bill.getParentVO().setStatus(VOStatus.NEW);
			GeneralBillItemVO[] vos = bill.getItemVOs();
			if (vos == null || vos.length == 0) {
				return;
			}

			Writeback4cB1VO[] bvos = (Writeback4cB1VO[]) billvo
					.getTableVO(billvo.getTableCodes()[0]);


			for (int i = 0; i < vos.length; i++) {
				GeneralBillItemVO vo = vos[i];
				String orderbid = vo.getCsourcebillbid();// 取得调拨订单主键
				for (int j = 0; j < bvos.length; j++) {
					if (orderbid.equals(bvos[j].getCorder_bid())) {
						// 设置应付数量
						vo.setNshouldoutnum(bvos[j].getNoutnum());
						vo.setNshouldoutassistnum(bvos[j].getNpacknumber());
						vo.setVbatchcode(getDefaultCode());
						// 设置实发数量
						vo.setNoutnum(bvos[j].getNoutnum());
						vo.setNoutassistnum(bvos[j].getNpacknumber());
						vo.setStatus(VOStatus.NEW);
						vo.setAttributeValue(WdsWlPubConst.csourcehid_wds, pk);
					}
				}
			}
			GeneralBillItemVO[] voss = flterNull(vos);
			bill.setChildrenVO(voss);		
		}
		setLocatorVO(orderVos, billvo);
	}
	/**
	 * 过滤数量为空的行
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-13上午10:46:21
	 * @param vos
	 */
	private GeneralBillItemVO[] flterNull(GeneralBillItemVO[] vos) {
		if(vos==null || vos.length==0){
			return null;
		}
		ArrayList<GeneralBillItemVO> list=new ArrayList<GeneralBillItemVO>();
		for(int i=0;i<vos.length;i++){
			GeneralBillItemVO vo=vos[i];
			UFDouble uf=PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());
			if(uf.doubleValue()>0){
				list.add(vo);
			}
		}	
		return list.toArray(new GeneralBillItemVO[0]);
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 查询 物流出库单的 货位信息
	 * @时间：2011-11-3上午11:30:04
	 * @param value
	 * @throws UifException
	 */
	public void setLocatorVO(GeneralBillVO[] orderVos, MultiBillVO billvo) throws UifException {
		if(billvo==null || billvo.getParentVO()== null ){
			return;
		}
		if(orderVos==null || orderVos.length==0)
			return ;
		for (int i = 0; i < orderVos.length; i++) {
			GeneralBillVO gbillvo = orderVos[i];
			if (gbillvo == null || gbillvo.getHeaderVO() == null
					|| gbillvo.getChildrenVO() == null
					|| gbillvo.getChildrenVO().length == 0)
				return;

			Writeback4cB1VO[] bvos = (Writeback4cB1VO[]) billvo
					.getTableVO(billvo.getTableCodes()[0]);
			Writeback4cB2VO[] bvos1 = (Writeback4cB2VO[]) billvo
					.getTableVO(billvo.getTableCodes()[1]);
			// 按订单id 和 货位id 合并数据 因为供应链 出入库单不允许 表体同一条记录 不允许重复货位
			Writeback4cB2VO[] zbvos = (Writeback4cB2VO[]) CombinVO.combinData(
					bvos1, new String[] { "cfirstbillbid", "vdef4" },
					new String[] { "noutnum", "noutassistnum" },
					Writeback4cB2VO.class);
			// 得到货位信息
			for (Writeback4cB2VO bvo : zbvos) {
				String key = bvo.getCfirstbillbid();// 销售订单表体 id
				LocatorVO lvo = new LocatorVO();
				lvo.setPk_corp(corp);
				lvo.setNoutspacenum(bvo.getNoutnum());
				lvo.setNoutspaceassistnum(bvo.getNoutassistnum());
				lvo.setCspaceid(bvo.getVdef4());// 货位
				lvo.setStatus(VOStatus.NEW);
				if (l_map.containsKey(key)) {
					l_map.get(key).add(lvo);
				} else {
					ArrayList<LocatorVO> zList = new ArrayList<LocatorVO>();
					zList.add(lvo);
					l_map.put(key, zList);
				}
			}
			// 设置货位信息
			GeneralBillItemVO[] gbvos = gbillvo.getItemVOs();
			for (int j = 0; j < gbvos.length; j++) {
				GeneralBillItemVO vo = gbvos[j];
				ArrayList<LocatorVO> zList = l_map.get(vo.getCsourcebillbid());
				if (zList == null || zList.size() == 0)
					throw new UifException("货位信息为空");
				vo.setLocator(zList.toArray(new LocatorVO[0]));
			}
		}
		
	}
	/** 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-13上午09:33:44
	 * @return
	 */
	public String getDefaultCode() {
		return WdsWlIcPubDealTool.getDefaultVbatchCode(corp);
	}
	/**
	 * 根据调拨订单主键 查询调拨订单
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-12上午11:05:27
	 * @param primarykey
	 * @return
	 * @throws BusinessException
	 */
	public BillVO getSaleOrder(String primarykey) throws BusinessException{
		if(primarykey==null){
			primarykey ="";
		}
		BillVO bill = new BillVO();
		String headSql = " select * from to_bill where isnull(dr,0)=0 and cbillid='"+primarykey+"'";
		ArrayList<BillHeaderVO> headList= (ArrayList<BillHeaderVO>)getBaseDAO().executeQuery(headSql, new BeanListProcessor(BillHeaderVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("查询调拨订单失败 ");
		}
		String bodySql = " select * from to_bill_b where isnull(dr,0)=0 and cbillid='"+primarykey+"' and coutwhid is not null";
		ArrayList<BillItemVO> bodyList= (ArrayList<BillItemVO>)getBaseDAO().executeQuery(bodySql, new BeanListProcessor(BillItemVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("查询调拨订单表体数据失败 ");
		}
		bill.setParentVO(headList.get(0));
		bill.setChildrenVO(bodyList.toArray(new BillItemVO[0]));
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
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		String csaleid = hvo.getPrimaryKey()==null ?"":hvo.getPrimaryKey();
		String where  = "body."+WdsWlPubConst.csourcehid_wds+"='"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4Y", voCond);
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
	public void canelPushSign4Y(String date, AggregatedValueObject[] billvo)
			throws Exception {
		// 取消销售出库签字
		if (billvo != null && billvo[0] != null
				&& billvo[0] instanceof GeneralBillVO) {
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator
					.getInstance().lookup(IPFBusiAction.class.getName());
			for (int i = 0; i < billvo.length; i++) {
				//取消签字
				ArrayList retList = (ArrayList) bsBusiAction.processAction(
						"CANCELSIGN", "4Y", date, null, billvo[i], null,
						null);
				//单据删除
				if (retList.get(0) != null && (Boolean) retList.get(0)) {
					bsBusiAction.processAction("DELETE", "4Y", date,
							null, billvo[i], null, null);
				}
			}
		}
	}
	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	}
}
