package nc.ui.ncblk.yjcb.yjcbjs;

import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.ncblk.yjcb.IYjcbSrv;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.ConnectionFactory;
import nc.ui.ncblk.yjcb.yjcbjs.dialog.InputJEDialog;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.CorpVO;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

/**
 * 
 *该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		// 判断，不允许年份、期间数据重复.
		// 年度
		String djnd = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"djnd").getValue();
		// 期间
		String djqj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"djqj").getValue();
		// PK值
		String pk_yjcb = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("pk_yjcb").getValue();
		// 如果不是修改数据
		if (pk_yjcb == null || pk_yjcb.equals("")) {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			Collection coll = iUAPQueryBS.retrieveByClause(
					NcReportYjcbVO.class, " djnd='" + djnd + "' and djqj='"
							+ djqj + "' and dr=0 ");
			if (coll != null && coll.size() > 0) {
				getBillUI().showErrorMessage(
						"当前年份" + djnd + ",期间" + djqj + "的数据已经存在，保存失败！");
				return;
			}
		}
		super.onBoSave();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null) {
			getBillUI().showErrorMessage("请选择需要操作的数据！");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		// 如果点了确认
		if (intBtn == IPrivateButton.SYSTEM_BUTON_QR) {
			// 是否已经确认
			if (pvo.getIsstatus().booleanValue() == true) {
				getBillUI().showErrorMessage("当前数据已经确认，确认失败！");
				return;
			}
			// 先判断当前数据是否已经计算
			// 如果没有计算，则返回
			if (pvo.getDef4().booleanValue() == false) {
				getBillUI().showErrorMessage("当前月份还没有进计算月结成本，确认失败！");
				return;
			}
			// 如果已经计算
			int result = getBillUI().showOkCancelMessage(
					"你确定要确认此次计算结果吗？确认后当前月份的数据不能再删除、重计算!");
			// 如果选择了是
			if (result == 1) {
				// 进行确认操作
				pvo.setIsstatus(new UFBoolean(true));
				// 进行更新后台数据
				IVOPersistence voPer = (IVOPersistence) NCLocator.getInstance()
						.lookup(IVOPersistence.class.getName());
				voPer.updateVO(pvo);
				
				// added by zjj 2010-05-19
				IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
						IYjcbSrv.class.getName());
				jsSrv.yjcbQr(pvo);
				getBillUI().showWarningMessage("成功确认");
				// added end
				onBoRefresh();
				getBillUI().updateUI();
			
			}
		}
		// 如果点了计算
		if (intBtn == IPrivateButton.SYSTEM_BUTON_JS) {
			// 用户VO
			UserVO userVo = getBillUI().getEnvironment().getUser();
			// 操作公司VO
			CorpVO corpVo = getBillUI().getEnvironment().getCorporation();
			// 操作日期
			UFDate ufdate = getBillUI().getEnvironment().getDate();
			// 是否已经确认
			if (pvo.getIsstatus().booleanValue() == true) {
				// 已经确认后的数据不能再次计算
				getBillUI().showErrorMessage("当前数据已经确认，不能再次计算！");
				return;
			}
			// 计算服务
			int sj = getBillUI().showOkCancelMessage("您确认要对当前月份的数据进行计算吗？");
			if (sj != 1)
				return;
			IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
					IYjcbSrv.class.getName());
			boolean sfyjjs = pvo.getDef4().booleanValue();
			// 如果已经计算
			if (sfyjjs == true) {
				// 提示是否需要覆盖
				int sffg = getBillUI().showOkCancelMessage(
						"当前数据已经计算成功，您需要覆盖上次的计算吗？");
				// 需要覆盖
				if (sffg == 1) {
					// 覆盖计算
					// getBillUI().showWarningMessage("开始覆盖计算......");
					jsSrv.yjcbjsSrv(userVo, corpVo, ufdate, pvo, false);
					// 计算完成
					onBoRefresh();
					getBillUI().showWarningMessage("计算成功!");
				} else
					return;
			}
			// 还没有计算
			else {
				// 直接计算
				// getBillUI().showWarningMessage("开始直接计算......");
				jsSrv.yjcbjsSrv(userVo, corpVo, ufdate, pvo, true);
				// 计算完成
				onBoRefresh();
				getBillUI().showWarningMessage("计算成功!");
			}
		}
		/*
		 * 
		 * 卓竞劲修改
		 * 2010-05-13
		 * 
		 */
		//修改内容 增加新按钮事件
		if(intBtn == IPrivateButton.SYSTEM_BUTTON_TZ)
		{  
			if (pvo.getIsstatus().booleanValue() == true) {
				// 已经确认后的数据不能再次计算
				getBillUI().showErrorMessage("当前数据已经确认，不能再次计算！");
				return;
			}
			if(pvo.getDef4().booleanValue())
			{
			InputJEDialog input = new InputJEDialog();
			
			if(input.str != null && input.isTz == true)
			{		
			IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
					IYjcbSrv.class.getName());
			jsSrv.yjcbTz(Double.parseDouble(input.str), pvo);
			onBoRefresh();
			getBillUI().showWarningMessage("调整成功!");
			}
			//String t = JOptionPane.showInputDialog(input, "请输入金额", "输入",JOptionPane.OK_CANCEL_OPTION);
		   // System.out.println(t);
			}
			else
			{
				getBillUI().showWarningMessage("该记录还未计算，请先计算！");
			}
		}
       
		super.onBoElse(intBtn);

	}

	@Override
	protected void onBoDelete() throws Exception {
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null ) {
			getBillUI().showErrorMessage("请选择需要操作的数据！");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		IUAPQueryBS  query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<NcReportYjcbVO> list1 = (List<NcReportYjcbVO>)query.retrieveByClause(NcReportYjcbVO.class," djnd > '" + pvo.getDjnd() +
		  "' or (djnd ='" + pvo.getDjnd() + "' and djqj > '" + pvo.getDjqj() + "')"  );
		if(list1.size() == 0)
		{    
			//System.out.println(getBillUI().showOkCancelMessage("是否删除该记录？") == 2);
			 if(getBillUI().showOkCancelMessage("是否删除该记录？") == 1)
			 {
		     IVOPersistence delete = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			 IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(IYjcbSrv.class.getName());
			 delete.deleteByClause(NcReportYjcbBVO.class, "pk_yjcb = '" + pvo.getPk_yjcb() +"'");
			 delete.deleteByClause(NcReportYjcbVO.class, "pk_yjcb = '" + pvo.getPk_yjcb() + "'");
			 jsSrv.yjcbSynDel(pvo.getDjnd(), pvo.getDjqj());
			 this.onBoRefresh();
			 }
		}
		else
		{
			this.getBillUI().showErrorMessage("存在下一个月记录，要继续操作请先删除下一个月的记录！");
		}
//		if (pvo.getIsstatus().booleanValue() == true) {
//			throw new Exception("当前数据已经确认，不能被删除！");
//		} 
		
		
//		else  {
//			super.onBoDelete();
//			}
		/*
		 * 
		 *  added by zjj 
		 *  2010-05-18
		 *  
		 */
//		else  
//		{    
//			super.onBoDelete();
//			try
//			{  
//			  if(query == null)
//				  query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//			  IVOPersistence delete = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//			  List<NcReportYjcbVO> list = (List<NcReportYjcbVO>) query.retrieveByClause(NcReportYjcbVO.class," djnd > '" + pvo.getDjnd() 
//					  + "' or (djnd ='" + pvo.getDjnd() + "' and djqj > '" + pvo.getDjqj() + "')"  );
//			  for(int i = 0; i < list.size(); i++)
//			  {   
//				  NcReportYjcbVO tem = list.get(i);
//				  delete.deleteByPK(NcReportYjcbVO.class, tem.getPk_yjcb());
//				  //delete.deleteByClause(NcReportYjcbBVO.class, "pk_yjcb = '" + tem.getPk_yjcb() +"'");
//			     
//			  }
//			  onBoRefresh();
//			}
//			catch(BusinessException be)
//			{
//				be.printStackTrace();
//			}
//			catch(Exception e)
//			{  
//				e.printStackTrace();
//				System.out.println("IVOPersistence cannot find!");
//			}
//		}
		/*
		 * 
		 *   added end
		 * 
		 */
	}

	@Override
	protected void onBoEdit() throws Exception {
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null) {
			getBillUI().showErrorMessage("请选择需要操作的数据！");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		if (pvo.getIsstatus().booleanValue() == true) {
			throw new Exception("当前数据已经确认，不能被修改！");
		} else
			super.onBoEdit();
	}

}