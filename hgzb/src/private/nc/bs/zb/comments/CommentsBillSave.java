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
			throw new ValidationException("��������Ϊ��");
		if(!(billVo instanceof HYBillVO))
			throw new ValidationException("�������ݷǷ�");
		
//		���ౣ���������  
		checkData(billVo);
		
//		�����ͷ
		String key = getDao().insertVO((SuperVO)billVo.getParentVO());
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null){
			throw new BusinessException("���������쳣");
		}
		
//		��װ�����ۺ�vo
		BiEvaluationBodyVO[] bodys = (BiEvaluationBodyVO[])billVo.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("�����ϢΪ��");
		
		HYBillVO[] bills = new HYBillVO[bodys.length];
		BidSlvendorVO[] tmpVendor = null;
        HYBillVO tmpBill = null;
        int index = 0;
		for(BiEvaluationBodyVO body:bodys){
			body.setCevaluationid(key);//��ͷ������ֵ
			
			tmpVendor = body.getBidSlvendorVOs();
			tmpBill =  new HYBillVO();
			tmpBill.setParentVO(body);
			tmpBill.setChildrenVO(tmpVendor);
			bills[index] = tmpBill;
			index ++;
		}
		
		if(bills == null || bills.length == 0)
			throw new BusinessException("�����ϢΪ��");
		
		for(HYBillVO bill:bills){
			super.saveBill(bill);
		}		
		return null;
	}

	protected void checkData(AggregatedValueObject billVo)
	throws BusinessException {
		HYBillVO billvo = (HYBillVO)billVo;
//		��ͷ����У��
		BidEvaluationHeaderVO head = (BidEvaluationHeaderVO)billvo.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
//			���ɵ��ݺ�
			BillcodeGenerater gen = new BillcodeGenerater();
			head.setVbillno(gen.getBillCode(head.getPk_billtype(), head.getPk_corp(), null, null));
		}
		head.validate();
//		��������У��
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
