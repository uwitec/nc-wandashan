package nc.ui.zb.avnum;

import nc.ui.zb.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.zb.avnum.AvNumBodyVO;
import nc.vo.zb.avnum.AvVendorVO;

public class AvNumHelper {
    private static String bo = "nc.bs.zb.avnum.AvNumBO";
	
	public static HYBillVO  loadAvNumBillVO(String cbiddingid,ClientLink cl) throws Exception{
		Class[] ParameterTypes = new Class[]{String.class,ClientLink.class};
		Object[] ParameterValues = new Object[]{cbiddingid,cl};
		Object o = LongTimeTask.callRemoteService("pu",bo, "loadAvNumBillVO", ParameterTypes, ParameterValues, 2);
		return o==null?null:(HYBillVO)o;
	}
	
	public static HYBillVO saveHyBillVO(HYBillVO billvo) throws Exception{
		checkZbNum(billvo);
		Class[] ParameterTypes = new Class[]{HYBillVO.class};
		Object[] ParameterValues = new Object[]{billvo};
        Object o =LongTimeTask.callRemoteService("pu",bo, "saveHyBillVO", ParameterTypes, ParameterValues, 2);
        return o==null?null:(HYBillVO)o;
	}
	
	private static void checkZbNum(HYBillVO billvo) throws BusinessException {
		if(billvo==null)
			return;
		AvNumBodyVO[] vos =(AvNumBodyVO[])billvo.getChildrenVO();
		if(vos==null || vos.length==0)
			return;
		
		for(AvNumBodyVO vo:vos){
			AvVendorVO[] bvos = vo.getAvVendorVO();
			if(bvos==null || bvos.length==0)
				return;
			UFDouble sum = UFDouble.ZERO_DBL;
			UFDouble zbnum = PuPubVO.getUFDouble_NullAsZero(vo.getNzbnum());
			for(AvVendorVO bvo:bvos){
				sum = sum.add(PuPubVO.getUFDouble_NullAsZero(bvo.getNzbnum()));
			}
//			if(!sum.equals(zbnum))
//				throw new BusinessException("第"+vo.getCrowno()+"招标数量分摊总和不等于总招标数量");
			
//			zhf modify   客户声明   中标数量可以小于招标数量  但不能大于
			if(sum.equals(UFDouble.ZERO_DBL))
				throw new BusinessException("未分量,品种信息行号为"+vo.getCrowno());
			if(sum.compareTo(zbnum)>0)
				throw new BusinessException("供应商中标数量之和超过了招标数量,品种信息行号为"+vo.getCrowno());
			
		}
		
	}
	
}
