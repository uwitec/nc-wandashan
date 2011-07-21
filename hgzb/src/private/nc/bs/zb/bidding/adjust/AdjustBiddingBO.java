package nc.bs.zb.bidding.adjust;

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
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingSuppliersVO;
import nc.vo.zb.bidding.SmallBiddingBodyVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubTool;

public class AdjustBiddingBO {
	

	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private void checkBillStatue(String cbiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return;
		String sql = "select vbillstatus from zb_bidding_h where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"' ";
		int ii = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), -1);
		if(ii != IBillStatus.FREE)
			throw new BusinessException("发生并发操作请重新操作");
	}
	
	private void checkBiddingBodyTs(Map<String,String> tsInfor) throws BusinessException{
		if(tsInfor == null || tsInfor.size() == 0)
			throw new BusinessException("数据异常");
		String sql = "select cbiddingbid,ts from zb_bidding_b where cbiddingbid in "+ZbPubTool.getSubSql(tsInfor.keySet().toArray(new String[0]));
		List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.ARRAYLISTPROCESSOR);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("数据异常");
		Iterator it = ldata.iterator();
		Object[] os = null;
		String newts = null;
		while(it.hasNext()){
			os = (Object[])it.next();
			newts = ZbPubTool.getString_NullAsTrimZeroLen(os[1]);
			if(newts.equalsIgnoreCase(tsInfor.get(ZbPubTool.getString_NullAsTrimZeroLen(os[0]))))
				continue;
			throw new BusinessException("发生并发操作请重新操作");
		}
	}
	
//	private HYPubBO pubbo = new HYPubBO();
	
	private BiddingBodyVO[] getOldBiddingBodys(String cbiddingid) throws BusinessException{
		String wheresql = "cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0";
		Collection<BiddingBodyVO> c = getDao().retrieveByClause(BiddingBodyVO.class, wheresql);
		if(c == null||c.size() == 0 )
			return null;
		return c.toArray(new BiddingBodyVO[0]);			
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）标书调整
	 * 2011-6-11下午02:16:35
	 * @param sleft 调整后左侧标段数据
	 * @param sright 调整后右侧标段数据
	 * @param sLogUser 
	 * @param date
	 * @param leftbiddingid 左侧标段id
	 * @param rightbiddingid 右侧标段id
	 * @throws Exception
	 */
	public void dealBidding(SuperVO[] sleft,SuperVO[] sright,String sLogUser,UFDate date,String leftbiddingid,String rightbiddingid) throws Exception{	
//		if(lpara == null || lpara.size() == 0){
//			throw new BusinessException("传入数据为空");
//		}
//		---------------------------------进入数据校验---------------------------------------
		boolean isleftnull = sleft==null||sleft.length==0;
		boolean isrightnull = sright==null||sright.length==0;
		
		SmallBiddingBodyVO[] left = null;
		List<SmallBiddingBodyVO> lbody = new ArrayList<SmallBiddingBodyVO>();
		
//		int index = 0;
		if(!isleftnull){
//			left = new SmallBiddingBodyVO[sleft.length];
			for(SuperVO sl:sleft){
				if(sl==null)
					continue;
				lbody.add((SmallBiddingBodyVO)sl);
//				index ++;
			}
		}
		
		if(lbody.size()>0)
			left = lbody.toArray(new SmallBiddingBodyVO[0]);
		
		SmallBiddingBodyVO[] right = null;
		lbody.clear();
//		index = 0;
		if(!isrightnull){
			right = new SmallBiddingBodyVO[sleft.length];
			for(SuperVO sl:sright){
				if(sl == null)
					continue;
				lbody.add((SmallBiddingBodyVO)sl);
//				index ++;
			}
		}
		
		if(lbody.size()>0)
			right = lbody.toArray(new SmallBiddingBodyVO[0]);
//		String sLogUser = PuPubVO.getString_TrimZeroLenAsNull(lpara.get(2));
		
		isleftnull = left==null||left.length==0;
		isrightnull = right==null||right.length==0;
		
		if(isrightnull&&isleftnull){
    		throw new BusinessException("传入数据为空");
    	}	
		
//		String leftbiddingid = isleftnull?null:left[0].getCbiddingid();
//		String rightbiddingid = isrightnull?null:right[0].getCbiddingid();
//		UFDate date = PuPubVO.getUFDate(lpara.get(3));
		if(ZbPubTool.getString_NullAsTrimZeroLen(leftbiddingid).equalsIgnoreCase(ZbPubTool.getString_NullAsTrimZeroLen(rightbiddingid)))
			throw new BusinessException("同一标段不需要调整");
		
//		处理前的数据校验   标书状态校验    品种ts校验  两个标段存在相同的品种  ？？？
		checkBillStatue(leftbiddingid);
		checkBillStatue(rightbiddingid);
		Set<String> leftSet = new HashSet<String>();
		Set<String> rightSet = new HashSet<String>();
		Map<String,String> tsInfor = new HashMap<String, String>();
		if(!isleftnull){
			for(SmallBiddingBodyVO body:left){
				tsInfor.put(body.getPrimaryKey(), body.getTs());	
				leftSet.add(body.getPrimaryKey());
			}
//			= tsInfor.keySet();
		}
		
		if(!isrightnull){
			for(SmallBiddingBodyVO body:right){
				tsInfor.put(body.getPrimaryKey(), body.getTs());	
				rightSet.add(body.getPrimaryKey());
			}
//			rightSet = tsInfor.keySet();
		
		}
		checkBiddingBodyTs(tsInfor);
//---------------------------------数据校验结束---------------------------------------------
		BiddingBodyVO[] oldleft = getOldBiddingBodys(leftbiddingid);
		BiddingBodyVO[] oldright = getOldBiddingBodys(rightbiddingid);

		if((oldleft == null || oldleft.length ==0)&&(oldright==null || oldright.length==0))
			throw new BusinessException("数据异常");
		
		List<BiddingBodyVO> ldel = new ArrayList<BiddingBodyVO>();
		List<BiddingBodyVO> lnew = new ArrayList<BiddingBodyVO>();
		
		List<BiddingBodyVO> lleftnew = new ArrayList<BiddingBodyVO>();
		List<BiddingBodyVO> lrightnew = new ArrayList<BiddingBodyVO>();
//		BiddingBodyVO tmp = null;
		if(oldleft==null||oldleft.length==0||oldright==null||oldright.length==0){//这种情况不会存在  因为没有品种的标段 不存在 zhf 补充
//			if(isleftnull){
//				throw new BusinessException("数据没有变动");
//			}
//			/**
//			 * 原标段没有品种经过调整后由右侧标段调整到左侧标段品种的情况
//			 */
//			for(BiddingBodyVO body:oldright){
//				if(leftSet.contains(body.getPrimaryKey()))
//					lleftnew.add(getNewBody(body, leftbiddingid, sLogUser, date));
//			}
			throw new BusinessException("数据异常,存在品种信息为空的标段");
		}//else{
			if(isleftnull){//左侧原来不为空现在调空
				/**
				 * 左侧品种全部移到右侧标段情况
				 */
				ldel.addAll(Arrays.asList(oldleft));//左侧标段全部删除
				for(BiddingBodyVO body:oldleft){//右侧标段全部新增
					lrightnew.add(getNewBody(body, rightbiddingid, sLogUser, date));
				}
			}else{
				/**
				 * 两侧调整后均有品种信息  需要判断是原来的还是新增的
				 */
				for(BiddingBodyVO body:oldleft){
					if(leftSet.contains(body.getPrimaryKey())){//原来就含有保持不变
						continue;
					}else{				
						ldel.add(body);//不在左侧的品种删除
						if(rightSet.contains(body.getPrimaryKey())){//如果移入了右侧则右侧新增
							lrightnew.add(getNewBody(body, rightbiddingid, sLogUser, date));
						}				
					}				
				}
			}
		
		
//		if(oldright==null||oldright.length==0){//这种情况不会存在  因为没有品种的标段 不存在 zhf 补充
//
//			if(isrightnull){
//				throw new BusinessException("数据没有变动");
//			}
//			/**
//			 * 原标段没有品种经过调整后由左侧标段调整到右侧标段品种的情况   上面已经调整过这种情况
//			 */
////			for(BiddingBodyVO body:oldleft){
////				if(rightSet.contains(body.getPrimaryKey()))
////					lrightnew.add(getNewBody(body, rightbiddingid, sLogUser, date));
////			}
//		
//		}else{
			if(isrightnull){
				ldel.addAll(Arrays.asList(oldright));
				for(BiddingBodyVO body:oldright){
					lleftnew.add(getNewBody(body, leftbiddingid, sLogUser, date));
				}
			}else{
				for(BiddingBodyVO body:oldright){
					if(rightSet.contains(body.getPrimaryKey())){
						continue;
					}else{				
						ldel.add(body);
						if(leftSet.contains(body.getPrimaryKey())){
							lleftnew.add(getNewBody(body, leftbiddingid, sLogUser, date));
						}				
					}				
				}
			}
//		}
		
		
		int len = 0;
//		删除原标段品种
		len = ldel.size();
		
		if(len>0){
			getDao().deleteVOArray(ldel.toArray(new BiddingBodyVO[0]));
		}
		
		if(lleftnew.size()>0){
			lnew.addAll(lleftnew);
			adjustVendorInfor(rightbiddingid, leftbiddingid);
		}
		if(lrightnew.size()>0){
			lnew.addAll(lrightnew);
			adjustVendorInfor(leftbiddingid,rightbiddingid);
		}
		
//		插入标段新增品种
		len = lnew.size();
		if(len>0){
			String[] ids = getDao().insertVOArray(lnew.toArray(new BiddingBodyVO[0]));
			if(ids.length != len)
				throw new BusinessException("数据库操作失败，插入数据异常");
		}
		
//		删除左侧标段
		if(isleftnull){
			delBidding(leftbiddingid);
		}
//		删除右侧标段
		if(isrightnull){
			delBidding(rightbiddingid);
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）将来源标段的供应商信息转移到目的标段
	 * 2011-6-11下午02:36:44
	 * @param souBiddingid 来源标段
	 * @param tarBiddingid 目的标段
	 * @return
	 * @throws BusinessException
	 */
	private void adjustVendorInfor(String souBiddingid,String tarBiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(souBiddingid)==null || PuPubVO.getString_TrimZeroLenAsNull(tarBiddingid)==null)
			return;
		String whereSql = " isnull(dr,0) = 0 and cbiddingid = '"+souBiddingid+"' and coalesce(fisclose,'N')='N'";
		java.util.Collection c = getDao().retrieveByClause(BiddingSuppliersVO.class, whereSql);
		if(c == null || c.size() == 0)
			return;
		
//		如果目的标段已存在该供应商   不可重复录入   如果目的标段的该供应商处于关闭状态也认为已经存在 不再新增
		String sql = "select ccustmanid from zb_biddingsuppliers where isnull(dr,0)=0 and cbiddingid = '"+tarBiddingid+"'";
		List ldata = (List)getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNLISTPROCESSOR);
				
		BiddingSuppliersVO[] vendors = (BiddingSuppliersVO[])c.toArray(new BiddingSuppliersVO[0]);
		List<BiddingSuppliersVO> lnew = new ArrayList<BiddingSuppliersVO>();
		for(BiddingSuppliersVO vendor:vendors){
			if(ldata.contains(vendor.getCcustmanid()))
				continue;
			vendor.setPrimaryKey(null);
			vendor.setCbiddingid(tarBiddingid);
			vendor.setDr(0);
			vendor.setStatus(VOStatus.NEW);
			vendor.setCrowno(String.valueOf(Integer.parseInt(vendor.getCrowno())-5));
			vendor.setIdef1(1);//调整标识位
			lnew.add(vendor);
		}
		if(lnew.size()>0)
		getDao().insertVOArray(lnew.toArray(new BiddingSuppliersVO[0]));
//		if(ss.length != len)
//			throw new BusinessException();
	}
	
	private void delBidding(String cbiddingid) throws BusinessException{
		String sql = "update zb_bidding_b set dr = 1 where cbiddingid = '"+cbiddingid+"'";
		String sql1 = "update zb_biddingsuppliers set dr = 1 where cbiddingid = '"+cbiddingid+"'";
		String sql2 = "update zb_biddingtimes set dr = 1 where cbiddingid = '"+cbiddingid+"'";
		String sql3 = "update zb_bidding_h set dr = 1 where cbiddingid = '"+cbiddingid+"'";
		getDao().executeUpdate(sql);
		getDao().executeUpdate(sql1);
		getDao().executeUpdate(sql2);
		getDao().executeUpdate(sql3);
	}
	
	private BiddingBodyVO getNewBody(BiddingBodyVO body,String newParentKey,String sLogUser,UFDate date){
		BiddingBodyVO tmp = null;
		tmp = (BiddingBodyVO)body.clone();
		tmp.setPrimaryKey(null);
		tmp.setCbiddingid(newParentKey);
		tmp.setReserve1(sLogUser);
		tmp.setReserve14(UFBoolean.TRUE);
		tmp.setReserve11(date);
		tmp.setStatus(VOStatus.NEW);
		tmp.setCrowno(String.valueOf(Integer.parseInt(tmp.getCrowno())-5));
		return tmp;
	}
}
