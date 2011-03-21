package nc.ui.wds.w8004040216;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.w8004040216.MyClientUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w8004040216.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 *
 *该类是AbstractMyEventHandler抽象类的实现类，
 *主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private MyClientUI myClientUI;
	private DcSaleOrderDlg dcSaleOrderDlg = null; // 查询方法的类
	private boolean isAdd=false;
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	protected void ondc() throws Exception {
		// 实例查询类
		dcSaleOrderDlg = new DcSaleOrderDlg(myClientUI);

		// 调用方法 获取查询后的聚合VO
		AggregatedValueObject[] vos = dcSaleOrderDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0216",
						ConstVO.m_sBillDRSQ, "8004040216", "021601", myClientUI);

		// 判断是否对查询模板有进行操作
		if (null == vos || vos.length == 0) {
//			getBillUI().showWarningMessage("您没有进行操作!");
			return;
		}

		// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
		MyBillVO voForSave = changeReqFydtoOutgeneral(vos);
		// 进行数据情况 和按钮初始化
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		// 填充数据
		getBufferData().addVOToBuffer(voForSave);
//		 更新数据
		updateBuffer();
//		 更改增加按钮状态
		getButtonManager().getButton(nc.ui.wds.w8004040216.dcButtun.IDcButtun.dc)
				.setEnabled(false);
//		super.onBoEdit();
		// getBillUI().setBillOperate( // 因为点击自定义按钮后就会切到正常按钮模式 所以设置状态
		// nc.ui.trade.base.IBillOperate.OP_EDIT);
		// 执行公式表头公式
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
//		isAdd = true;
		super.onBoEdit();
		// 执行公式表头公式
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailEditFormulas();
		// 设置库管员
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"cwhsmanagerid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 设置制单人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 设置修改人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"clastmodiid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// 单据日期
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"dbilldate").setValue(_getDate().toString());
		
		isAdd=true;
	}

	/**
	 * 把模板中的选中的VO 进行转换成销售出库的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 销售的聚合VO
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		int num = 0; // 为了计算数组的长度
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// 子表信息数组集合
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SaleorderHVO salehVO = (SaleorderHVO) vos[0].getParentVO();
//		if (null != salehVO.getSrl_pk() && !"".equals(fydnew.getSrl_pk())) {
//			generalHVO.setSrl_pkr(fydnew.getSrl_pk()); // 出库仓库
//		}
		generalHVO.setSrl_pkr("1021A91000000004YZ0P"); // 出库仓库
		if (null != salehVO.getCbiztype()
				&& !"".equals(salehVO.getCbiztype())) {
			generalHVO.setCbiztype(salehVO.getCbiztype()); // 业务类型主键
		}
		if (null != salehVO.getCdeptid() && !"".equals(salehVO.getCdeptid())) {
			generalHVO.setCdptid(salehVO.getCdeptid()); // 部门
		}
		if (null != salehVO.getCemployeeid() && !"".equals(salehVO.getCemployeeid())) {
			generalHVO.setCbizid(salehVO.getCemployeeid()); // 业务员
		}
		if (null != salehVO.getCcustomerid() && !"".equals(salehVO.getCcustomerid())) {
			generalHVO.setCcustomerid(salehVO.getCcustomerid()); // 客户
		}
		if (null != salehVO.getVreceiveaddress() && !"".equals(salehVO.getVreceiveaddress())) {
			generalHVO.setVdiliveraddress(salehVO.getVreceiveaddress()); // 收货地址
		}
		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
			generalHVO.setVnote(salehVO.getVnote()); // 备注
		}

		myBillVO.setParentVO(generalHVO);
		// 循环缓存中的子表信息
		if (null != dcSaleOrderDlg.getSaleVO() && dcSaleOrderDlg.getSaleVO().length > 0) {
			// 循环缓存中的子表信息
			for (int i = 0; i < dcSaleOrderDlg.getSaleVO().length; i++) {
				if (salehVO.getCsaleid().equals(
						dcSaleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
					vos[0].setChildrenVO(dcSaleOrderDlg.getSaleVO()[i]
							.getBodyVOs());
					break;
				}
			}
			SaleorderBVO[] salebVOs = (SaleorderBVO[]) vos[0].getChildrenVO();
			// 获取缓存 转换子表其他的信息
			for (int i = 0; i < 2; i++) {
				String a="";
			}
			for (int i = 0; i <salebVOs.length; i++) {
				SaleorderBVO salebvo = salebVOs[i];
//				TbFydmxnewVO fydmxnewvo = fydnewdlg.getFydmxVO()[i];
//				if (fydnew.getFyd_pk().equals(fydmxnewvo.getFyd_pk())) {
					TbOutgeneralBVO generalBVO = new TbOutgeneralBVO();
					if (null != salehVO.getCsaleid()
							&& !"".equals(salehVO.getCsaleid())) {
						generalBVO.setCsourcebillhid(salehVO.getCsaleid()); // 来源单据表头主键
					}
					if (null != salehVO.getVreceiptcode()
							&& !"".equals(salehVO.getVreceiptcode())) {
						generalBVO.setVsourcebillcode(salehVO.getVreceiptcode()); // 来源单据号
					}
					if (null != salehVO.getCreceipttype()
							&& !"".equals(salehVO.getCreceipttype())) {
						generalBVO.setCsourcetype(salehVO.getCreceipttype()
								.toString()); // 来源单据类型
					}
					if (null != salebvo.getCorder_bid()
							&& !"".equals(salebvo.getCorder_bid())) {
						generalBVO.setCsourcebillbid(salebvo.getCorder_bid()); // 来源单据表体主键
					}
					if (null != salehVO.getCsaleid()
							&& !"".equals(salehVO.getCsaleid())) {
						generalBVO.setCfirstbillhid(salehVO.getCsaleid()); // 源头单据表头主键
					}
					if (null != salebvo.getCorder_bid()
							&& !"".equals(salebvo.getCorder_bid())) {
						generalBVO.setCfirstbillbid(salebvo.getCorder_bid()); // 源头单据表体主键
					}
					if (null != salebvo.getCrowno()
							&& !"".equals(salebvo.getCrowno())) {
						generalBVO.setCrowno(salebvo.getCrowno()); // 行号
					}
					if (null != salebvo.getNnumber()
							&& !"".equals(salebvo.getNnumber())) {
						generalBVO.setNshouldoutnum(salebvo.getNnumber()); // 应发数量
						generalBVO.setNoutnum(salebvo.getNnumber());//实发数量
					}
					if (null != salebvo.getNpacknumber()
							&& !"".equals(salebvo.getNpacknumber())) {
						generalBVO.setNshouldoutassistnum(salebvo.getNpacknumber()); // 应发辅数量
						generalBVO.setNoutassistnum(salebvo.getNpacknumber());//实发辅数量
					}
					if (null != salebvo.getCinvbasdocid()
							&& !"".equals(salebvo.getCinvbasdocid())) {
						generalBVO
								.setCinventoryid(salebvo.getCinvbasdocid()); // 存货主键
					}
					if (null != salebvo.getBlargessflag()
							&& !"".equals(salebvo.getBlargessflag())) {
						generalBVO.setFlargess(salebvo.getBlargessflag()); // 是否赠品
					}
					generalBList.add(generalBVO);

//				}
			}
			TbOutgeneralBVO[] generalBVO = new TbOutgeneralBVO[generalBList
					.size()];
			generalBVO = generalBList.toArray(generalBVO);
			myBillVO.setChildrenVO(generalBVO);
		}

		return myBillVO;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(nc.ui.wds.w8004040216.dcButtun.IDcButtun.dc)
		.setEnabled(true);
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// 进行数据晴空
		Object o = null;
		ISingleController sCtrl = null;
		if (getUIController() instanceof ISingleController) {
			sCtrl = (ISingleController) getUIController();
			if (sCtrl.isSingleDetail()) {
				o = billVO.getParentVO();
				billVO.setParentVO(null);
			} else {
				o = billVO.getChildrenVO();
				billVO.setChildrenVO(null);
			}
		}

		boolean isSave = true;

		// 判断是否有存盘数据
		if (billVO.getParentVO() == null
				&& (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0)) {
			isSave = false;
		} else {
			if (getBillUI().isSaveAndCommitTogether())
				billVO = getBusinessAction().saveAndCommit(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
			else

				// write to database
				billVO = getBusinessAction().save(billVO,
						getUIController().getBillType(), _getDate().toString(),
						getBillUI().getUserObject(), checkVO);
		}

		// 进行数据恢复处理
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow=-1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow=0;
					
				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow=getBufferData().getCurrentRow();
				}
			}
			// 新增后操作处理
			setAddNewOperate(isAdding(), billVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		if(nCurrentRow>=0){
			getBufferData().setCurrentRow(nCurrentRow);
		}
		
		showZeroLikeNull(false);
	}
	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(nc.ui.wds.w8004040216.dcButtun.IDcButtun.dc)
		.setEnabled(true);
		if (isAdd) {
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			isAdd = false;
		} else {
			super.onBoCancel();
		}
	}
	@Override
	protected void onBoDel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDel();
	}
	
	
}