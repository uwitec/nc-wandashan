package nc.bs.dm.so.deal2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.dm.so.SoDealBO;
import nc.bs.logging.Logger;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.so.deal.SoDealVO;
import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealHeaderVo;
import nc.vo.dm.so.deal2.StoreInvNumVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

/**
 * 
 * @author zhf   销售计划安排算法类
 * 
 * 输入 待安排的数据  进行安排处理  返回需要手工安排的数据
 *
 */
public class SoDealCol {
	
	private SoDealBillVO[] bills = null;
	
	private List lpara = null;
	public SoDealCol(){
		super();
	}
	public SoDealCol(SoDealBillVO[] bills,List lpara){
		super();
		this.bills = bills;
		this.lpara = lpara;
	}
	
	public void setData(SoDealBillVO[] bills,List lpara){
		this.bills = bills;
		this.lpara = lpara;
	}
	
	private StockInvOnHandBO stockbo = null;
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	private StockInvOnHandBO getStockBO(){
		if(stockbo == null){
			stockbo = new StockInvOnHandBO(getDao());
		}
		return stockbo;
	}
	
	private Map<String,UFDouble> custMinNumInfor = null;
	public void clearCustMinNumInfor(){
		if(custMinNumInfor != null)
			custMinNumInfor.clear();
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 通过分仓客商绑定获取  分仓客商的最小发货量设置
	 * @时间：2011-7-7下午04:09:28
	 * @param ccustid
	 * @param pk_store
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getMinSendNumForCust(String ccustid,String pk_store) throws BusinessException{

		if(PuPubVO.getString_TrimZeroLenAsNull(ccustid)==null)
			return null;
		String key = WdsWlPubTool.getString_NullAsTrimZeroLen(pk_store)+WdsWlPubTool.getString_NullAsTrimZeroLen(ccustid);
		if(custMinNumInfor == null){
			custMinNumInfor = new HashMap<String, UFDouble>();
		}
		if(!custMinNumInfor.containsKey(key)){
			String sql = " select ndef1 from tb_storcubasdoc where isnull(dr,0)=0 and " +
			" pk_cumandoc = '"+ccustid+"' and pk_stordoc = '"+pk_store+"'";// (select pk_stordoc from bd_stordoc where " +
			//					"isnull(dr,0)=0 and )";
			custMinNumInfor.put(key,PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR)));
		}
		return custMinNumInfor.get(key);
	}
	
	private void initInvNumInfor(Map<String, StoreInvNumVO> invNumInfor) throws BusinessException {

		SoDealVO[] bodys = null;
		SoDealHeaderVo head = null;
		String key = null;

		//	库存量信息  key 仓库+存货    
		//	Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();

		StoreInvNumVO tmpNumVO = null;
		//	获取库存量
		UFDouble[] stocknums = null;
		String pk_corp = SQLHelper.getCorpPk();
		Set<String> invs = new HashSet<String>();
		Logger.info("获取库存当前存量...");
		for(SoDealBillVO bill:bills){
			head = bill.getHeader();
			bodys = bill.getBodyVos();

			if(bodys == null || bodys.length ==0)
				continue;

			for(SoDealVO body:bodys){
				key = //WdsWlPubTool.getString_NullAsTrimZeroLen(head.getCbodywarehouseid())+
				WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCinvbasdocid());
				invs.add(body.getCinvbasdocid());
				if(invNumInfor.containsKey(key)){
					tmpNumVO = invNumInfor.get(key);
				}else{
					tmpNumVO = new StoreInvNumVO();
					tmpNumVO.setPk_corp(pk_corp);
					tmpNumVO.setCstoreid(head.getCbodywarehouseid());
					tmpNumVO.setCinvbasid(body.getCinvbasdocid());
					tmpNumVO.setCinvmanid(body.getCinventoryid());

					stocknums = getStockBO()
					.getInvStockNum(pk_corp, tmpNumVO.getCstoreid(), null, tmpNumVO.getCinvbasid(), null, null);
					
					if(stocknums == null || stocknums.length == 0)
						continue;
					tmpNumVO.setNstocknum(stocknums[0]);
					tmpNumVO.setNstockassnum(stocknums[1]);

				}

				tmpNumVO.setNplannum(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNplannum())
						.add(PuPubVO.getUFDouble_NullAsZero(body.getNnum())));
				tmpNumVO.setNplanassnum(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNplanassnum())
						.add(PuPubVO.getUFDouble_NullAsZero(body.getNassnum())));				

				invNumInfor.put(key, tmpNumVO);				
			}			
		}


		if(invNumInfor.size()==0){
			Logger.info("本次待安排存货库存均为空，无法安排，推出");
			return;
		}
		


		//	获取占用量
		Logger.info("获取存货已安排未出库量...");
		Map<String,UFDouble[]> invNumInfor2 = getStockBO().getNdealNumInfor(pk_corp, head.getCbodywarehouseid(), invs.toArray(new String[0]), new TempTableUtil());
		if(invNumInfor2 == null || invNumInfor2.size() == 0){
			Logger.info("本次安排的存货不存在已安排未出库量");
			if(invNumInfor2 == null)
				invNumInfor2 = new HashMap<String, UFDouble[]>();
//			return;
		}
			

		Logger.info("本次待安排存货库存状况：");
		for(String key2:invNumInfor.keySet()){
			tmpNumVO = invNumInfor.get(key2);
			stocknums = invNumInfor2.get(key2);
			if(tmpNumVO == null)
				continue;
			tmpNumVO.setNdealnum(stocknums == null?WdsWlPubTool.DOUBLE_ZERO:stocknums[0]);
			tmpNumVO.setNdealassnum(stocknums == null?WdsWlPubTool.DOUBLE_ZERO:stocknums[1]);
			//		当前可用量
			tmpNumVO.setNnum(tmpNumVO.getNstocknum().sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(tmpNumVO.getNdealassnum()));
			
			if(tmpNumVO.getNassnum().doubleValue()>tmpNumVO.getNplanassnum().doubleValue())
				tmpNumVO.setBisok(UFBoolean.TRUE);
			else
				tmpNumVO.setBisok(UFBoolean.FALSE);
			Logger.info(" 存货"+WdsWlPubTool.getInvCodeByInvid(tmpNumVO.getCinvbasid())
					+" 当前存量："+tmpNumVO.getNstockassnum()
					+" 已安排未出库量："+ tmpNumVO.getNdealassnum()
					+" 本次可用量："+ tmpNumVO.getNassnum()
					+" 本次待安排总量："+ tmpNumVO.getNplanassnum());
		}	
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 算法描述：
	 * 1、按品种获取各个品种的库存存量
	 * 2、按品种汇总各个品种的需求量
	 * 3、将品种分为两类：1、存量满足类 2、存量不足类
	 * 4、将客户分为两类：1、可直接安排类 2、存在存量不足的品种的客户类
	 * 5、可直接安排的客户 直接安排生成运单
	 * 6、不足的客户根据客户最小发货量过滤库存存量，如果库存存量比客户的最小发货量还小 抛弃该客户
	 * 7、将剩余的客户数据返回输出 等待 用户手工安排
	 * @时间：2011-7-7下午04:58:32
	 * 返回不够安排的客户   和  不够安排的品种库存状态
	 */
	public Object col() throws Exception {
		if(bills==null || bills.length ==0)
			return null;
//		库存量信息  key 仓库+存货    -----------去掉仓库   发货站不能不同
		Map<String, StoreInvNumVO> invNumInfor = new HashMap<String, StoreInvNumVO>();
		initInvNumInfor(invNumInfor);
        if(invNumInfor.size() == 0)
        	throw new BusinessException("所有存货的存量均为空,无法安排，退出");
//      根据存量过滤客户待安排的数据
//        Logger.info("###########################################################");
        Logger.info("根据最小发货量过滤库存可用量偏低的存货");
        List<SoDealVO> ldeal = null;
        List<SoDealVO> lnodeal = null;
        SoDealVO[] bodys = null;
        List<SoDealBillVO> lcust = new ArrayList<SoDealBillVO>();//不能发货的客户信息
        StoreInvNumVO tmpNumVO = null;
        for(SoDealBillVO bill:bills){
        	bodys = bill.getBodyVos();
        	if(bodys == null || bodys.length ==0)
        		continue;
        	boolean isdeal = true;
        	boolean pass = false;
        	for(SoDealVO body:bodys){
//        		body.setDbilldate(dbilldate)
        		tmpNumVO = invNumInfor.get(body.getCinvbasdocid());
        		if(tmpNumVO == null || PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNnum()).equals(WdsWlPubTool.DOUBLE_ZERO)){
//        			存在存货的可用量为空的     涉及该存货的客户  均不可安排 本次  该客户直接丢弃

        			Logger.info("存货"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"可用量为空,客户["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]本次无法安排");
        			
        			pass = true;
        			break;
        		}
        		
//        		若果库存可用量 低于该客户的最小发货量  过滤  掉该客户

        		if(PuPubVO.getUFDouble_NullAsZero(tmpNumVO.getNassnum())
        				.compareTo(
        						getMinSendNumForCust(bill.getHeader().getCcustomerid(), 
        								bill.getHeader().getCbodywarehouseid()))<0){
        			Logger.info("存货"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"可用量低于客户的最小发货量,客户["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]本次无法安排");
        			pass = true;
        			break;
        		}
        		if(!PuPubVO.getUFBoolean_NullAs(invNumInfor.get(body.getCinvbasdocid()).getBisok(),UFBoolean.FALSE).booleanValue()){
        			isdeal = false;
//        			Logger.info("存货"+WdsWlPubTool.getInvCodeByInvid(body.getCinvbasdocid())+"可用量低于本次待安排总量,客户["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]需手工安排");
        			continue;
        		}
        	}
        	
        	
        	if(pass)//该客户的本次发货要求 由于  库存量 过低  而  放弃
        		continue;
        	if(isdeal){
        		if(ldeal == null){
        			ldeal = new ArrayList<SoDealVO>();
        		}
        		ldeal.addAll(Arrays.asList(bodys));
        		Logger.info("##客户["+WdsWlPubTool.getCustNameByid(bill.getHeader().getCcustomerid())+"]本次可直接安排");
        	}else{
        		if(lnodeal == null){
        			lnodeal = new ArrayList<SoDealVO>();
        		}
        		lnodeal.addAll(Arrays.asList(bodys));
        		lcust.add(bill);
        	}        	
        }
        
        if(ldeal == null || ldeal.size()==0){
        	Logger.info("本次安排未存在可直接安排的客户");
        }else{//直接安排
        	SoDealBO dealbo = new SoDealBO();
        	dealbo.doDeal(ldeal, lpara);
        	Logger.info("系统直接安排成功");
        }
        if(lnodeal!= null && lnodeal.size()>0){//过滤最小发货量后  手动安排
//        	不知道如何过滤      暂不过滤 最小发货量       将不足的直接进入手工安排
//        	return new Object[]{lnodeal,invNumInfor};
//        	将数据转换封装处理
        	Collection<StoreInvNumVO> c = invNumInfor.values();
        	Iterator<StoreInvNumVO> it = c.iterator();
        	StoreInvNumVO tmp = null;
        	List<StoreInvNumVO> ltmp = new ArrayList<StoreInvNumVO>();
        	
        	while(it.hasNext()){
        		tmp = it.next();
        		for(SoDealVO deal:lnodeal){
        			if(deal.getCinvbasdocid().equalsIgnoreCase(tmp.getCinvbasid())){
        				tmp.getLdeal().add(deal);
        			}
        		}
        		if(tmp.getLdeal().size()>0)
        			ltmp.add(tmp);
        	}
        	if(lcust.size()>0)
        	Logger.info("存在"+lcust.size()+"个客户由于库存不足需要手工进行安排");
//        	UFDateTime time2 = new UFDateTime(System.currentTimeMillis());
        	Logger.info("本次安排处理结束,返回界面手工安排");
        	Logger.info("#####################################################");
        	return new Object[]{lcust,ltmp};
        } else{
        	Logger.info("本次安排未存在需要用户手工安排的数据");
        	Logger.info("本次安排处理结束");
        	Logger.info("#####################################################");
        }
        return null;
	}

}
