package nc.ui.pf.changedir.after;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillHeaderVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
/**
 * 
 * @author Administrator
 *销售运单转换成运费核算单 后台处理类
 */
public class ChgWDS5TOWDSMAfter implements IchangeVO{

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		SendBillHeaderVO hvo = (SendBillHeaderVO)nowVo.getParentVO();
		SendBillBodyVO[] bodyvos = (SendBillBodyVO[])nowVo.getChildrenVO();
		if(bodyvos !=null && bodyvos.length>0){
			for(int i =0 ;i<bodyvos.length;i++){
				bodyvos[i].setCrowno(String.valueOf((i+1)*10));
			}
		}
		setIcolType(bodyvos);
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos == null)
			return nowVos;
		SendBillVO[] billvo = new SendBillVO[1];
		billvo[0]= new SendBillVO();
		SendBillHeaderVO headvo = new SendBillHeaderVO();
		headvo.setIcoltype(1);
		ArrayList<SendBillBodyVO> list = new ArrayList<SendBillBodyVO>();
		for( int j = 0 ;j< nowVos.length;j++){
			SendBillBodyVO[] bodyvos = (SendBillBodyVO[])nowVos[j].getChildrenVO();
			list.addAll(Arrays.asList(bodyvos));
		}
		SendBillBodyVO[] bodys = list.toArray(new SendBillBodyVO[0]);
		billvo[0].setParentVO(headvo);
		billvo[0].setChildrenVO(bodys);
		setIcolType(bodys);
		return billvo;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：按照同一个经销商主数量汇总，
	 *  如果数量大于 5吨，按照吨公里来计算，如果小于等于5吨按照箱数来计算
	 * @时间：2011-6-8下午02:09:05
	 */
	private void setIcolType(SendBillBodyVO[] bodyvos ){
		Map<String, UFDouble> map = new HashMap<String, UFDouble>();
		int i=0;
		for(SendBillBodyVO body: bodyvos){			
			String key = body.getPk_trader();
			UFDouble newNum = PuPubVO.getUFDouble_NullAsZero(body.getNnum());
			if(map.containsKey(key)){
				UFDouble oldNum = PuPubVO.getUFDouble_NullAsZero(map.get(key));
				newNum = newNum.add(oldNum);
				map.put(key, newNum);
			}else{
				map.put(key, newNum);
			}
		}
		for(SendBillBodyVO body: bodyvos){
			String key = body.getPk_trader();
			UFDouble newNum = PuPubVO.getUFDouble_NullAsZero(map.get(key));
			if(newNum.doubleValue()>5){
				body.setIcoltype(1);
			}else{
				body.setIcoltype(2);
			}
			body.setCrowno(String.valueOf((i+1)*10));
			i++;
		}
	}
}
