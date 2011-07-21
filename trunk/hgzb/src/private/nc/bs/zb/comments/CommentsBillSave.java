package nc.bs.zb.comments;

import java.util.ArrayList;
import nc.bs.dao.BaseDAO;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.zb.pub.HYBillSave;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.BiEvaluationBodyVO;
import nc.vo.zb.comments.BidEvaluationHeaderVO;
import nc.vo.zb.comments.BidSlvendorVO;
import nc.vo.zb.pub.ResultSetProcessorTool;

public class CommentsBillSave extends HYBillSave {
	
	public ArrayList saveBill(AggregatedValueObject billVo)
	throws BusinessException {
		if(billVo == null)
			throw new ValidationException("传入数据为空");
		if(!(billVo instanceof HYBillVO))
			throw new ValidationException("传入数据非法");
		
//		变相保存主子孙表  
		checkData(billVo);
		
//		保存表头
		String key = getDao().insertVO((SuperVO)billVo.getParentVO());
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null){
			throw new BusinessException("保存数据异常");
		}
		
//		组装子孙表聚合vo
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])billVo.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("存货信息为空");
		
		HYBillVO[] bills = new HYBillVO[bodys.length];
		BidSlvendorVO[] tmpVendor = null;
        HYBillVO tmpBill = null;
        int index = 0;
		for(BiEvaluationBodyVO body:bodys){
			body.setCevaluationid(key);//表头主键赋值
			
			tmpVendor = body.getBidSlvendorVOs();
			tmpBill =  new HYBillVO();
			tmpBill.setParentVO(body);
			tmpBill.setChildrenVO(tmpVendor);
			bills[index] = tmpBill;
			index ++;
		}
		
		if(bills == null || bills.length == 0)
			throw new BusinessException("存货信息为空");
		
		for(HYBillVO bill:bills){
			super.saveBill(bill);
		}		
		return null;
	}

	protected void checkData(AggregatedValueObject billVo)
	throws BusinessException {
		HYBillVO billvo = (HYBillVO)billVo;
//		表头数据校验
		BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)billvo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
//			生成单据号
			BillcodeGenerater gen = new BillcodeGenerater();
			head.setVbillno(gen.getBillCode(head.getPk_billtype(), head.getPk_corp(), null, null));
		}
		head.validate();
//		表体数据校验
		String  sql = "select fisinvcl from zb_bidding_h where cbiddingid = '"+head.getCbiddingid().trim()+"'";
		boolean isinvcl = PuPubVO.getUFBoolean_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), UFBoolean.FALSE).booleanValue();
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])billvo.getChildrenVO();
		BidSlvendorVO[] vendors = null;
		for(BiEvaluationBodyVO body:bodys){
			body.validate(!isinvcl);
			vendors = body.getBidSlvendorVOs();
			for(BidSlvendorVO vendor:vendors){
				vendor.validate();
			}
		}
	}
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
}
