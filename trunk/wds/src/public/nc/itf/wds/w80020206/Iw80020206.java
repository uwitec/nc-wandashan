package nc.itf.wds.w80020206;

import java.util.List;

import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8004040204.MyClientUI;
import nc.ui.wds.w8004040204.MyEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.pf.PfUtilWorkFlowVO;
import nc.vo.wds.w8004040204.MyBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralTVO;
import nc.vo.wds.w8004040204.TbWarehousestockVO;

public interface Iw80020206 {

	public Object whs_processAction80020206(String actionName, String actionName2,
			String billType, String currentDate, AggregatedValueObject vo,
			Object outgeneralVo) throws Exception;
}
