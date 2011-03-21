package nc.ui.wds.w80060202;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060202.Iw80060202;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds.w80060604.MyQueryTemplate;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

/**
 * 
 * 发运计划调度
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // 是否有权限操作当前单据

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {

		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if ((null != isType && isType.equals("2"))
					|| (null != isType && isType.equals("3"))) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			if (getBufferData().getCurrentVO() == null)
				return;
			int result = myClientUI.showOkCancelMessage("确定删除该单据?");
			if (result == 1) {
				AggregatedValueObject item = getBillUI().getBufferData()
						.getCurrentVO();
				TbFydnewVO fydvo = (TbFydnewVO) item.getParentVO();
				if (null != fydvo.getFyd_fyzt() && fydvo.getFyd_fyzt() > 0) {
					myClientUI.showErrorMessage("删除失败!该单据已确认,请取消确认");
					return;
				}
				String sql = "select count(general_b_pk) from tb_outgeneral_b where dr = 0 and  csourcebillhid = '"
						+ fydvo.getFyd_pk() + "'";
				IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance()
						.lookup(IUAPQueryBS.class.getName());
				ArrayList results = (ArrayList) iuap.executeQuery(sql,
						new ArrayListProcessor());
				Object[] a = (Object[]) results.get(0);
				if (null != a[0] && !"".equals(a[0])
						&& Integer.parseInt(a[0].toString()) > 0) {
					myClientUI.showErrorMessage("删除失败！该单据已生成出库单,请先删除出库单");
					return;
				}
				// 调用接口删除方法
				Iw80060202 iw = (Iw80060202) NCLocator.getInstance().lookup(
						Iw80060202.class.getName());
				iw.deleteFydVO(item);
				// 清除界面数据
				getBufferData().removeCurrentRow();
				if (getBufferData().getVOBufferSize() == 0)
					getBillUI().setBillOperate(IBillOperate.OP_INIT);
				else
					getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

				updateBuffer();
			}
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			int result = getBillUI().showYesNoMessage("是否制单并打印?");

			if (result == 4) {
				// 判断当前是否有选中数据
				if (getBufferData().getVOBufferSize() > 0
						&& getBufferData().getCurrentRow() >= 0) {
					TbFydnewVO fydVO = (TbFydnewVO) getBufferData()
							.getCurrentVO().getParentVO();

					if (null != fydVO) {
						IUAPQueryBS iuap = (IUAPQueryBS) NCLocator
								.getInstance().lookup(
										IUAPQueryBS.class.getName());
						IVOPersistence ivo = (IVOPersistence) NCLocator
								.getInstance().lookup(
										IVOPersistence.class.getName());
						TbFydnewVO fydvo = (TbFydnewVO) iuap.retrieveByPK(
								TbFydnewVO.class, fydVO.getFyd_pk());
						if (null != fydvo) {
							// 通过主键获取打印次数
							Integer count = fydvo.getIprintcount();
							if (null != fydVO.getIprintcount() && null != count) {
								// 把当前界面中的打印次数和数据库里面进行比较，如果有异样进行提示
								if (fydVO.getIprintcount().intValue() != count
										.intValue()) {
									myClientUI
											.showErrorMessage("操作失败,该单据已被操作请刷新");
									return;
								}
							}
							if (null != count && count > 0) {
								fydvo.setIprintcount(count + 1);
								ivo.updateVO(fydvo);
							} else {
								fydVO.setFyd_fyzt(new Integer(0)); // 设置发运状态
								fydVO.setIprintcount(new Integer(1)); // 设置打印次数
								fydVO.setFyd_zdr(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey()); // 设置发运计划制单人
								fydVO.setFyd_dby(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey()); // 设置调拨员
								fydVO.setFyd_zdsj(_getDate()); // 设置制单日期
								fydVO.setFyd_yhfs("计划 转库"); // 发运方式
								fydVO.setIprintdate(_getDate());// 打印日期
								fydVO.setVbillstatus(new Integer(1)); // 单据状态
								AggregatedValueObject billVO = getBufferData()
										.getCurrentVO();
								billVO.setParentVO(fydVO);
								Iw80060202 iw = (Iw80060202) NCLocator
										.getInstance().lookup(
												Iw80060202.class.getName());
								iw.saveFydVO(billVO);
								getBufferData().getCurrentVO().setParentVO(
										fydVO);
							}
							super.onBoPrint();
							super.onBoRefresh();
							// this.onRefresh();
						}
					}
				}
			}
		} else {
			getBillUI().showErrorMessage("操作失败,当前登录者没有进行人员绑定");
			return;
		}
	}

	/***************************************************************************
	 * 根据当前的查询条件再进行查询更新
	 * 
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void onRefresh() throws ClassNotFoundException, Exception {
		SuperVO[] queryVos = queryHeadVOs(this.getSWhere());
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
		SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "80060202",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"80060202");

		if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// 获取查询条件
			ConditionVO[] voCons = query.getConditionVO();
			StringBuffer strWhere = new StringBuffer(query.getWhereSQL(voCons));
			strWhere
					.append(" and tb_fydnew.dr = 0 and tb_fydnew.fyd_approstate = '1' and tb_fydnew.billtype = 0 ");
			this.setSWhere(strWhere.toString());
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
			getBufferData().clear();
			// 增加数据到Buffer
			addDataToBuffer(queryVos);
			updateBuffer();
		}
	}

	private String sWhere = null;

	public String getSWhere() {
		return sWhere;
	}

	public void setSWhere(String where) {
		sWhere = where;
	}

}