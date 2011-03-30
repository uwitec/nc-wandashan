package nc.bs.wds.w8004040210;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilBO;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w8004040210.Iw8004040210;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.wds.w8004040210.TbGeneralBBVO;
import nc.vo.wds.w8004040210.TbGeneralHVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;

public class W8004040210Impl implements Iw8004040210 {

	IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	public int delTbGeneralBBVO(List items) throws BusinessException {
		// TODO Auto-generated method stub

		perse.deleteVOList(items);
		return 0;
	}

	public int insertTbGeneralBBVO(TbGeneralBBVO[] items)
			throws BusinessException {
		// TODO Auto-generated method stub

		perse.insertVOArray(items);

		return 0;
	}

	public int updateBdCargdocTray(List bcts) throws BusinessException {
		perse.updateVOList(bcts);
		return 0;
	}

	public void delAndInsertTbGeneralBBVO(List items1, TbGeneralBBVO[] items2)
			throws BusinessException {
		this.delTbGeneralBBVO(items1);
		boolean b = false;
		if (null != items2) {
			for (int i = 0; i < items2.length; i++) {
				if (null != items2[i]) {
					b = true;
				}
			}
		}
		if (b) {
			this.insertTbGeneralBBVO(items2);
		}

	}

	public AggregatedValueObject saveBD(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		ArrayList params = (ArrayList) userObj;
		// AggregatedValueObject retVo = nc.ui.trade.business.HYPubBO_Client
		// .saveBD(billVO, params.get(0));
		nc.itf.uif.pub.IUifService service = (IUifService) NCLocator
				.getInstance().lookup(IUifService.class.getName());
		AggregatedValueObject retVo = service.saveBD(billVO, params.get(0));
		// 存入库存表
		// for(){}
		// MyBillVO myBillVO=new MyBillVO();
		// myBillVO.setParentVO((TbWarehousestockVO) params.get(2));
		// service.saveBD(myBillVO,params.get(0));

		perse.insertVOArray((TbWarehousestockVO[]) params.get(2));
		// 托盘状态
		if (null != params.get(3)) {
			this.updateBdCargdocTray((List) params.get(3));
		}

		// GeneralBillVO voTempBill=(GeneralBillVO) params.get(1);
		// nc.ui.pub.pf.PfUtilClient.processAction("PUSHSAVESIGN",
		// "4E"/*单据类型*/, null, voTempBill);
		return retVo;
	}

	public AggregatedValueObject deleteBD(AggregatedValueObject billVO,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void canceldelete8004040210(String actionName1, String actionName2,
			String billType, String currentDate, AggregatedValueObject vo,
			TbGeneralHVO tbGeneralHVO) throws Exception {
		// TODO Auto-generated method stub
		nc.bs.pub.pf.PfUtilBO pfutilbo = new PfUtilBO();
		Object o = pfutilbo.processAction(actionName1/* 回写脚本名称 */,
				billType/* 单据类型 */, currentDate, null, vo, null);
		Object o1 = pfutilbo.processAction(actionName2/* 回写脚本名称 */,
				billType/* 单据类型 */, currentDate, null, vo, null);
		List bcts = new ArrayList();
		bcts.add(tbGeneralHVO);
		perse.updateVOList(bcts);

	}

	public void pushsavesign8004040210(String actionName, String billType,
			String currentDate, AggregatedValueObject vo,
			TbGeneralHVO tbGeneralHVO) throws Exception {
		// TODO Auto-generated method stub
		nc.bs.pub.pf.PfUtilBO pfutilbo = new PfUtilBO();
		Object o = pfutilbo.processAction(actionName/* 回写脚本名称 */,
				billType/* 单据类型 */, currentDate, null, vo, null);
		List bcts = new ArrayList();
		bcts.add(tbGeneralHVO);
		perse.updateVOList(bcts);

	}

}
