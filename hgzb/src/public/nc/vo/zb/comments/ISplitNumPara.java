package nc.vo.zb.comments;

import nc.vo.trade.pub.HYBillVO;


/**
 * 
 * @author zhf  �ýӿڴ���ûʲôʵ������   611 ʱ�����㷨�����µĲ���  ������  ֧�������㷨
 * ��϶ȵ�һ����
 *
 */
public interface ISplitNumPara {
	public void clear();
	public void refresh(String cbiddingid,HYBillVO bill) throws Exception;
	public HYBillVO getBill();	
}
