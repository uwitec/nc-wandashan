package nc.bs.wl.so.order;

import java.util.HashMap;
import java.util.Map;

import nc.bs.trade.comsave.BillSave;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.dm.so.order.SoorderBVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class SoOrderBillSave extends BillSave {
	
	public java.util.ArrayList saveBill(nc.vo.pub.AggregatedValueObject billVo)
	throws BusinessException {
		beforeSave(billVo);
		java.util.ArrayList  ret = super.saveBill(billVo);
		afterSave(billVo);
		return ret;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 保存前数据完整性校验
	 * @时间：2011-7-11下午08:43:09
	 * @param billVo
	 * @throws BusinessException
	 */
	public void beforeSave(nc.vo.pub.AggregatedValueObject billVo) throws BusinessException{
//		保存前可用量校验
		if(billVo == null)
			return;
		SoorderVO head = (SoorderVO)billVo.getParentVO();
		head.validateOnPushSave();
		SoorderBVO[] bodys = (SoorderBVO[])billVo.getChildrenVO();
		for(SoorderBVO body:bodys){
			body.validateOnPushSave();			
		}
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 保存后校验可用量是否可用
	 * @时间：2011-7-11下午08:42:48
	 * @param billVo
	 * @throws BusinessException
	 */
	public void afterSave(nc.vo.pub.AggregatedValueObject billVo) throws BusinessException{
//		保存前可用量校验
		if(billVo == null)
			return;
		SoorderVO head = (SoorderVO)billVo.getParentVO();
		SoorderBVO[] bodys = (SoorderBVO[])billVo.getChildrenVO();
		if(head == null || bodys == null || bodys.length == 0)
			throw new ValidationException("数据异常");
		
		Map<String, UFDouble[]> invNumInfor = new HashMap<String, UFDouble[]>();
		UFDouble[] nums = null;
		String key;
		for(SoorderBVO body:bodys){
			key = body.getPk_invbasdoc();
			if(invNumInfor.containsKey(key))
				nums = invNumInfor.get(key);
			else{
				nums = new UFDouble[]{WdsWlPubTool.DOUBLE_ZERO,WdsWlPubTool.DOUBLE_ZERO};
			}
			
			nums[0] = nums[0].add(PuPubVO.getUFDouble_NullAsZero(body.getNarrangnmu()));
			nums[1] = nums[1].add(PuPubVO.getUFDouble_NullAsZero(body.getNassarrangnum()));
		}
		StockInvOnHandBO stock = new StockInvOnHandBO();
		stock.checkNumForOut(head.getPk_corp(), head.getPk_outwhouse(), invNumInfor, new TempTableUtil());
	}
}
