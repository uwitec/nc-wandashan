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
import nc.bs.wds.ic.stock.StockInvOnHandBO;
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
	
	public SoDealCol(SoDealBillVO[] bills,List lpara){
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


		if(invNumInfor.size()==0)
			return;


		//	获取占用量
		Map<String,UFDouble[]> invNumInfor2 = getStockBO().getNdealNumInfor(pk_corp, head.getCbodywarehouseid(), invs.toArray(new String[0]), new TempTableUtil());
		if(invNumInfor2 == null || invNumInfor2.size() == 0)
			return;

		for(String key2:invNumInfor.keySet()){
			tmpNumVO = invNumInfor.get(key2);
			stocknums = invNumInfor2.get(key2);
			if(tmpNumVO == null || stocknums == null || stocknums.length == 0)
				continue;
			tmpNumVO.setNdealnum(stocknums[0]);
			tmpNumVO.setNdealassnum(stocknums[1]);
			//		当前可用量
			tmpNumVO.setNnum(tmpNumVO.getNstocknum().sub(tmpNumVO.getNdealnum()));
			tmpNumVO.setNassnum(tmpNumVO.getNstockassnum().sub(tmpNumVO.getNdealassnum()));
			
			if(tmpNumVO.getNassnum().doubleValue()>tmpNumVO.getNplanassnum().doubleValue())
				tmpNumVO.setBisok(UFBoolean.TRUE);
			else
				tmpNumVO.setBisok(UFBoolean.FALSE);
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
        	throw new BusinessException("所有存货的存量均为空");
//      根据存量过滤客户待安排的数据
        List<SoDealVO> ldeal = null;
        List<SoDealVO> lnodeal = null;
        SoDealVO[] bodys = null;
        List<SoDealBillVO> lcust = new ArrayList<SoDealBillVO>();//不能发货的客户信息
        for(SoDealBillVO bill:bills){
        	bodys = bill.getBodyVos();
        	if(bodys == null || bodys.length ==0)
        		continue;
        	boolean isdeal = true;
        	for(SoDealVO body:bodys){
        		if(!PuPubVO.getUFBoolean_NullAs(invNumInfor.get(body.getCinvbasdocid()).getBisok(),UFBoolean.FALSE).booleanValue()){
        			isdeal = false;
        			break;
        		}
        	}
        	if(isdeal){
        		if(ldeal == null){
        			ldeal = new ArrayList<SoDealVO>();
        		}
        		ldeal.addAll(Arrays.asList(bodys));
        	}else{
        		if(lnodeal == null){
        			lnodeal = new ArrayList<SoDealVO>();
        		}
        		lnodeal.addAll(Arrays.asList(bodys));
        		lcust.add(bill);
        	}        	
        }
        
        if(ldeal.size()>0){//直接安排
        	SoDealBO dealbo = new SoDealBO();
        	dealbo.doDeal(ldeal, lpara);
        }
        if(lnodeal.size()>0){//过滤最小发货量后  手动安排
//        	不知道如何过滤      暂不过滤 最小发货量       将不足的直接进入手工安排
//        	return new Object[]{lnodeal,invNumInfor};
//        	将数据转换封装处理
        	Collection<StoreInvNumVO> c = invNumInfor.values();
        	Iterator<StoreInvNumVO> it = c.iterator();
        	StoreInvNumVO tmp = null;
        	List<StoreInvNumVO> ltmp = null;
        	
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
        	return new Object[]{lcust,ltmp};
        }  
        return null;
	}

}
