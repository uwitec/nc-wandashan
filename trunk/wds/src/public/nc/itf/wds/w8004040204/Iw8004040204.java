package nc.itf.wds.w8004040204;

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

public interface Iw8004040204 {
	

	/**
	 * 销售出库对ERP中的单据动作处理
	 */
	public Object whs_processAction(String actionName,String actionName2, String billType, String currentDate,
			AggregatedValueObject vo,Object outgeneralVo) throws Exception;

	
	/**
	 * 自动取货
	 * 
	 * @param pk_user 当前登录者主键
	 * 
	 * @param pk_stordoc
	 *            仓库主键
	 * 
	 * @param pk
	 *            单品主键
	 *            
	 * @param queryVar 
	 * 				警戒天数的字段
	 * @throws Exception
	 */
	public String autoPickAction(String pk_user ,TbOutgeneralBVO[] generalb, String pk_stordoc,String queryVar)
			throws Exception;

	public void insertGeneralTVO(List<TbOutgeneralTVO> itemList,
			List<TbOutgeneralTVO> itemList2) throws Exception;

	public void deleteGeneralTVO(List<TbOutgeneralTVO> itemList)
			throws Exception;

	public void saveGeneralVO(MyBillVO myBillVO) throws Exception;

	public void insertWarehousestock(TbWarehousestockVO item) throws Exception;

	public void updateWarehousestock(TbWarehousestockVO item) throws Exception;

	public void queryWarehousestock(List itemList) throws Exception;

	public List queryGeneralTVO(String cinventoryid, String cfirstbillhid,
			String cfirstbillbid) throws Exception;

	public void updateBdcargdocTray(String trayPK) throws Exception;

	public AggregatedValueObject saveBD8004040204(AggregatedValueObject billVO,
			Object userObj) throws Exception;
	
	public AggregatedValueObject saveBD8004040602(AggregatedValueObject billVO,
			Object userObj) throws Exception;

	public AggregatedValueObject deleteBD8004040204(
			AggregatedValueObject billVO, Object userObj) throws Exception;

	/**
	 * 查询缓存表中的实出主数量和辅数量
	 * 
	 * @param pk_cfirst
	 *            来源单据子表主键
	 * @param pk_invbasdoc
	 *            单品主键
	 * @return Object数组 0主数量 1辅数量
	 * @throws Exception
	 */
	public Object[] getNoutNum(String pk_cfirst, String pk_invbasdoc)
			throws Exception;
}
