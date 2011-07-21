package nc.ui.zb.comments;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.zb.pub.ZbPubPrintDataSou;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.comments.EvaluationBodyPrintVO;

public class CommentsPrintDataSou extends ZbPubPrintDataSou{

	public CommentsPrintDataSou(String moduleName, BillCardPanel billcardpanel) {
		super(moduleName, billcardpanel);
		// TODO Auto-generated constructor stub
	}
	
	public void setPrintDatas(AggregatedValueObject bill){
		super.setPrintDatas(bill);
	}
	
	protected void dealData(){
		if(m_data == null)
			return;
		if(!(m_data instanceof HYBillVO))
			return;
//		BidEvalHeaderPrintVO head = (BidEvalHeaderPrintVO)m_data.getParentVO();
		EvaluationBodyPrintVO[] bodys = (EvaluationBodyPrintVO[])m_data.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			return;
//		分类处理  格式处理
	}
	
	



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	public String[] getItemValuesByExpress(String itemExpress) {
//		String key = itemExpress.substring(2);
//		if(m_data == null)
//			return null;
//		if(itemExpress.startsWith("v_")){//供应商信息
//			
//		}
//	}
	
}
