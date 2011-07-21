package nc.bs.zb.bidding.make;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.zb.pub.HYBillSave;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubTool;

public class MakeBiddingSave extends HYBillSave {
	
	private BaseDAO dao = null;
	public BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}

	
	/**
	 * 单据保存前处理
	 * @param billVo
	 * @throws BusinessException
	 * twh (2006-11-30 上午10:39:22)<br>
	 */
	protected void beforeSave(AggregatedValueObject billVo)
			throws BusinessException {
/**
 * 		校验  单据号不能为空  单据名称不能为空  招标类型  
 *      品种数据完整性校验   
 *      供应商数据完整性校验  
 *      轮次安排校验
 */
		if(PuPubVO.getString_TrimZeroLenAsNull(billVo.getParentVO().getPrimaryKey())==null)
		      ((BiddingBillVO)billVo).validateOnSave(true);
		BiddingBillVO bill = (BiddingBillVO)billVo;
		
//		单据号唯一性校验
		String sql = "select count(0) from zb_bidding_h where isnull(dr,0) = 0 and vbillno = '"+bill.getHeader().getVbillno().trim()+"' and pk_corp = '"+SQLHelper.getCorpPk()+"'";
		
		String ID = PuPubVO.getString_TrimZeroLenAsNull(bill.getHeader().getPrimaryKey());
		if(ID != null){
			sql = sql + " and cbiddingid <> '"+ID+"'";
		}

		String s = ZbPubTool.getParam();
	    if(s!=null &&!"".equals(s))
	    	sql = sql+" and reserve1 = '"+s+"'";
		if(PuPubVO.getInteger_NullAs(getBaseDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0){
			throw new BusinessException("标书标号重复");
		}
		
//		修改删行保存 时  回写计划数量
		
		if(bill.getHeader().getFisself().booleanValue())
			return;
		
		BiddingBodyVO[] bodys = (BiddingBodyVO[])bill.getChildrenVO();
		List<String> lids = new ArrayList<String>();
		MakeBiddingBO bo = new MakeBiddingBO();
		for(BiddingBodyVO body:bodys){
			if(PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebilltype())==null||!PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebilltype()).equalsIgnoreCase(ScmConst.PO_Pray))
				continue;
			if(PuPubVO.getString_TrimZeroLenAsNull(body.getCupsourcebillrowid())==null)
				continue;
			if(body.getStatus() == VOStatus.DELETED){
				if(!lids.contains(body.getCsourcebillbid()))
					lids.add(body.getPrimaryKey());
			}
			// add by zhw  2011-06-11
			if(body.getStatus()==VOStatus.UPDATED){//修改 校验来源数量  并回写
				bo.updatePraBill(body);
			}    
		}
		if(lids.size()>0){
			bo.reWritePONumOnDel(lids.toArray(new String[0]),null,false);
		}
	}
	
//	protected void afterSave(AggregatedValueObject billVo)throws BusinessException {
//		
//		BiddingBillVO bill = (BiddingBillVO)billVo;
//		BiddingHeaderVO head = bill.getHeader();
//		String sql = "update zb_bidding_h set vbillstatus = "+IBillStatus.CHECKPASS+" where cbiddingid = '"+head.getCbiddingid()+"' and isnull(dr,0)=0";
//		getBaseDao().executeUpdate(sql);
//}

}
