package nc.ui.wds2.inv;

import java.awt.Container;

import nc.ui.pub.pf.IUIBeforeProcAction;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.wds2.inv.StatusUpdateBillVO;
import nc.vo.wds2.inv.StatusUpdateBodyVO;
import nc.vo.wds2.inv.StatusUpdateHeadVO;

public class StatusBeforeAction implements IUIBeforeProcAction {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {
		// TODO Auto-generated method stub

		if(actionName.equalsIgnoreCase(IPFACTION.COMMIT)){
//			保存前数据校验  表体：存货 批次 调整前状态  调整后状态  不能重复  调整数量不能为0
			if(vo == null)
				return;
			
			if(!(vo instanceof StatusUpdateBillVO))
				throw new BusinessException("传入数据非法");
			
			StatusUpdateBillVO bill = (StatusUpdateBillVO)vo;
			StatusUpdateHeadVO head = bill.getHeader();
			head.validation();
			
			StatusUpdateBodyVO[] bodys = bill.getBodys();
			if(bodys == null || bodys.length == 0)
				throw new BusinessException("表体数据为空");
			
			if(bodys.length == 1)
				return;
			
			CircularlyAccessibleValueObject[][] datas=
				SplitBillVOs.getSplitVOs(bodys, StatusUpdateBodyVO.split_keys);
			
			if(datas.length == bodys.length)
				return;
			
			StringBuffer error = new StringBuffer();
			
			StatusUpdateBodyVO[] tmps = null;
			for(int i = 0;i<datas.length;i++){
				tmps = (StatusUpdateBodyVO[])datas[i];
				for(StatusUpdateBodyVO tmp:tmps){
					error.append(tmp.getCrowno()+"、");
				}
				error.append("行，重复;");
			}
			throw new BusinessException(error.toString());
		}
	}

}
