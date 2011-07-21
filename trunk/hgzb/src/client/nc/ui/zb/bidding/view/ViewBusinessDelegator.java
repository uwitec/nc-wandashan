package nc.ui.zb.bidding.view;

import java.util.Hashtable;

import nc.ui.zb.pub.IMultiChildQueryInfo;
import nc.ui.zb.pub.IMultiChildVOInfo;
import nc.ui.zb.pub.MultiChildBusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.zb.bidding.BidViewBillVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingHeaderVO;
import nc.vo.zb.bidding.BiddingTimesVO;

public class ViewBusinessDelegator extends MultiChildBusinessDelegator {
	
	private IMultiChildQueryInfo queryInfo = null;

	private IMultiChildVOInfo childVOInfo = null;

	public ViewBusinessDelegator() {
		super();
	}
	
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		// ��������������ȡ���ӱ������
		BiddingBodyVO[] b1vos = (BiddingBodyVO[]) this.queryByCondition(BiddingBodyVO.class, "cbiddingid='" + key + "' and isnull(dr,0)=0");
	
		// ��������������ȡ���ӱ������
		BiddingTimesVO[] b3vos = (BiddingTimesVO[]) this.queryByCondition(BiddingTimesVO.class, "cbiddingid='" + key + "' and isnull(dr,0)=0");

		// ��ѯ���ݷ�Hashtable������
		Hashtable<String, SuperVO[]> dataHT = new Hashtable<String, SuperVO[]>();
		if (b1vos != null && b1vos.length > 0) {
			dataHT.put(tableCodes[0], b1vos);
		}
		
		if (b3vos != null && b3vos.length > 0) {
			dataHT.put(tableCodes[2], b3vos);
		}
		
		return dataHT;
	}

	protected class QueryInfo implements IMultiChildQueryInfo {
		/**
		 * ����TableCodeȡ�ñ���
		 */
		public String getAliasByTableCode(String tblCode) {
			return tblCode;
		}
	}


	protected class ChildVOInfo implements IMultiChildVOInfo {
		/**
		 * 
		 */
		public String[] getVONames() {
			String[] VoNames = new String[] { 
					BidViewBillVO.class.getName(),
					BiddingHeaderVO.class.getName(),
					BiddingBodyVO.class.getName(),
					BiddingTimesVO.class.getName()
					};
			return VoNames;
		}

		/**
		 * ����tablecode���VO����
		 */
		public String getVoClassNameByTableCode(String tblCode) {
			for (int i = 0; i < getTableCodes().length; i++) {
				if (getTableCodes()[i].equals(tblCode)) {
					return getVONames()[i + 2];
				}
			}
			return null;
		}

		/**
		 * ��ȡ���е�TableCode
		 */
		public String[] getTableCodes() {
			String[] childTableCodes = new String[] { 
					"zb_bidding_b","zb_biddingtimes"};
			return childTableCodes;
		}
	}

	@Override
	public IMultiChildQueryInfo getMultiChildQueryInfo() {
		if (queryInfo == null)
			queryInfo = new QueryInfo();
		return queryInfo;
	}

	@Override
	public IMultiChildVOInfo getMultiChildVoInfo() {
		if (childVOInfo == null)
			childVOInfo = new ChildVOInfo();
		return childVOInfo;
	}

}
