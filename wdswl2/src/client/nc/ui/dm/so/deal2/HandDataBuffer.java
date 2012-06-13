package nc.ui.dm.so.deal2;

import java.util.List;

import nc.vo.dm.so.deal2.SoDealBillVO;
import nc.vo.dm.so.deal2.SoDealVO;
import nc.vo.dm.so.deal2.StoreInvNumVO;

public class HandDataBuffer {

	private List<SoDealBillVO> lcust = null;//δ���ŵĿͻ���Ϣ
	private List<StoreInvNumVO> lnum = null;//��ǰ������Ϣ
	
	private int custSelRow = -1;//�ͻ���Ϣ  ģ�� ��ͷѡ����
	private int numSelRow = -1;//�������Ž���  ��ͷ ѡ����
	
	
	public SoDealVO[] getCurrBodysForCust(){
		if(isCustEmpty())
			return null;
		if(getCustSelRow()<0)
			return null;
		return getLcust().get(getCustSelRow()).getBodyVos();
	}
	
	public SoDealVO[] getCurrBodysForDeal(){
		if(isNumEmpty())
			return null;
		if(getNumSelRow()<0)
			return null;
		return getLnum().get(getNumSelRow()).getLdeal().toArray(new SoDealVO[0]);
	}
	
	public int getCustSelRow() {
		return custSelRow;
	}

	public void setCustSelRow(int custSelRow) {
		this.custSelRow = custSelRow;
	}

	public int getNumSelRow() {
		return numSelRow;
	}

	public void setNumSelRow(int numSelRow) {
		this.numSelRow = numSelRow;
	}

	public void clear(){
		lcust = null;
		lnum = null;
	}
	
	public boolean isCustEmpty(){
		return lcust == null || lcust.size() == 0?true:false;
	}

	public boolean isNumEmpty(){
		return lnum == null || lnum.size() == 0?true:false;
	}
	
	public List<SoDealBillVO> getLcust() {
		return lcust;
	}

	public void setLcust(List<SoDealBillVO> lcust) {
		this.lcust = lcust;
	}

	public List<StoreInvNumVO> getLnum() {
		return lnum;
	}

	public void setLnum(List<StoreInvNumVO> lnum) {
		this.lnum = lnum;
	}	
	
}
