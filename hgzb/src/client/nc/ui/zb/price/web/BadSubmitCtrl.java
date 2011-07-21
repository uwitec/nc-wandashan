package nc.ui.zb.price.web;

import java.util.ArrayList;
import java.util.List;

import nc.ui.zb.price.pub.SubmitPriceHelper;
import nc.vo.pub.BusinessException;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;

//�����б���ⱨ�ۿ�����

public class BadSubmitCtrl {
	private List<SubmitPriceVO> ldata = null;
	public void addBadSubmit(SubmitPriceVO price){
		if(ldata == null)
			ldata = new ArrayList<SubmitPriceVO>();
		SubmitPriceVO newprice = (SubmitPriceVO)price.clone();
		newprice.setIsubmittype(ZbPubConst.BAD_SUBMIT_PRICE);
		ldata.add(newprice);
	}
	public int getBadSubNum(){
		return ldata == null?0:ldata.size();
	}
	public void clear(){
		if(ldata!=null)
			ldata.clear();
	}
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��������ⱨ����ϸ��Ϣ   �رոñ�θù�Ӧ��
	 * 2011-5-20����06:19:19
	 * @throws BusinessException
	 */
	public void dealBadSubmit() throws Exception{
		if(ldata == null || ldata.size() == 0)
			return;
		SubmitPriceHelper.dealBadPrice(ldata.toArray(new SubmitPriceVO[0]));
	}
}
