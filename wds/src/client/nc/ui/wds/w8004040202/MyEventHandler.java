package nc.ui.wds.w8004040202;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060406.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
  *
  * 
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{
	  
		MyClientUI myClientUI = null;

		private FydnewDlg fydnewdlg = null; // 查询方法的类

		boolean isAdd = false;

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		isAdd = true;
		// 实例查询类
		fydnewdlg = new FydnewDlg(myClientUI);
		// 调用方法 获取查询后的聚合VO
		AggregatedValueObject[] vos = fydnewdlg.getReturnVOs(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"4202", ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
				myClientUI);
		// 判断是否对查询模板有进行操作
		if (null == vos || vos.length == 0) {
			getBillUI().showWarningMessage("您没有进行操作!");
			return;
		}
		// 调用转换类 把模板中获取的对象转换成自己的当前显示的对象，调用方法
		//MyBillVO voForSave = changeReqSaleOrderYtoFyd(vos);
		// 进行数据情况 和按钮初始化
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		// 填充数据
		getBufferData().addVOToBuffer(vos[0]);
		// 更新数据
		updateBuffer();
		super.onBoEdit();
		// 执行公式表头公式
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailEditFormulas();
		// 设置制单人
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem("fyd_zdr")
				.setValue(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
		// 设置调拨员
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fyd_dby")
				.setValue(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
		// 设置制单时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("fyd_zdsj").setValue(
						dateFormat.format(new Date()));
	}
	
	/**
	 * 把模板中的选中的VO 进行转换成销售出库的VO
	 * 
	 * @param vos
	 *            页面选中的聚合VO
	 * @return 销售的聚合VO
	 */
//	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos) {
//		MyBillVO myBillVO = new MyBillVO();
//		int num = 0; // 为了计算数组的长度
//		SaleorderHVO salehVO = null;
//		TbFydnewVO fydnewVO = new TbFydnewVO();
//		// 子表信息数组集合
//		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
//		// 如果聚合VO的长度大于1 说明是合并订单操作
//		if (vos.length > 1) {
//			String vercode = null; // 订单号
//			String saleid = null; // 销售订单主键
//			// 获取第一次查询后的数据缓存
//			SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
//			// 循环聚合VO的长度
//			for (int i = 0; i < vos.length; i++) {
//				// 把第一个主表提取出来
//				salehVO = (SaleorderHVO) vos[i].getParentVO();
//				if (null != salehVO.getCsaleid() // 判断销售主键是否为空
//						&& !"".equals(salehVO.getCsaleid())) {
//					if (null == saleid || "".equals(saleid)) { // 判断主键是否为空，如果为空说明是第一次赋值
//						saleid = salehVO.getCsaleid();
//					} else { // 如果有值 进行累加，中间用逗号来区分，方便后面来拆分
//						saleid = saleid + "," + salehVO.getCsaleid();
//					}
//				}
//				// 判断订单号是否为空，同上
//				if (null != salehVO.getVreceiptcode()
//						&& !"".equals(salehVO.getVreceiptcode())) {
//					if (null == vercode || "".equals(vercode)) {
//						vercode = salehVO.getVreceiptcode();
//					} else {
//						vercode = vercode + "," + salehVO.getVreceiptcode();
//					}
//				}
//				// 获取当前主表中的销售主键(不是累加后的主键)
//				String csaleid = salehVO.getCsaleid();
//				// 循环缓存
//				for (int j = 0; j < salevo.length; j++) {
//					// 获取缓存中的销售主键
//					String salehid = salevo[j].getHeadVO().getCsaleid();
//					// 判断当前的销售主键和缓存中的销售主键是否相同，
//					if (csaleid.equals(salehid)) {
//						// 如果两者主键相同就把缓存中对应主键的子表信息数组提取出来放到 子表信息集合中
//						salevoList.add((SaleorderBVO[]) salevo[j]
//								.getChildrenVO());
//						// 获取当前子表信息的长度 如果有多个 进行累加 为了下面的数组初始化
//						num = num + salevo[j].getChildrenVO().length;
//						break;
//					}
//				}
//			}
//			// 把累加后的订单号和主键赋值给当前的对象
//			fydnewVO.setVbillno(vercode); // 单据号
//			fydnewVO.setCsaleid(saleid); // 销售主表主键
//			// 子表信息集合
//			List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
//			// 循环子表信息数组集合，把集合中的数组给循环出来，放到子表信息集合中，下面就能转换成数组了
//			for (int i = 0; i < salevoList.size(); i++) {
//				SaleorderBVO[] tmp = salevoList.get(i);
//				for (int j = 0; j < tmp.length; j++) {
//					salelist.add(tmp[j]);
//				}
//			}
//			SaleorderBVO[] saleb = new SaleorderBVO[num]; // 实例一个子表信息数组，其长度就是上面进行多次累加的
//			salelist.toArray(saleb); // 数组转换
//			// 把转换后的子表数组放到当前的聚合VO中
//			vos[0].setChildrenVO(saleb);
//		} else { // 如果不是合并订单的操作 就是正常赋值了
//			salehVO = (SaleorderHVO) vos[0].getParentVO();
//			if (null != salehVO.getVreceiptcode()
//					&& !"".equals(salehVO.getVreceiptcode())) {
//				fydnewVO.setVbillno(salehVO.getVreceiptcode()); // 单据号
//			}
//			if (null != salehVO.getCsaleid()
//					&& !"".equals(salehVO.getCsaleid())) {
//				fydnewVO.setCsaleid(salehVO.getCsaleid()); // 主键
//			}
//			// 循环缓存中的子表信息
//			for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
//				if (salehVO.getCsaleid().equals(
//						saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
//					vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i]
//							.getBodyVOs());
//					break;
//				}
//			}
//		}
//		// 这里为公用部分 因为在合并与正常的，下面的信息是不变的
//		if (null != salehVO.getCcustomerid()
//				&& !"".equals(salehVO.getCcustomerid())) {
//			fydnewVO.setPk_kh(salehVO.getCcustomerid()); // 客户主键
//		}
//		if (null != salehVO.getVreceiveaddress()
//				&& !"".equals(salehVO.getVreceiveaddress())) {
//			fydnewVO.setFyd_shdz(salehVO.getVreceiveaddress()); // 收货地址
//		}
//		if (null != salehVO.getCemployeeid()
//				&& !"".equals(salehVO.getCemployeeid())) {
//			fydnewVO.setPk_psndoc(salehVO.getCemployeeid()); // 业务员
//		}
//		if (null != salehVO.getCbiztype() && !"".equals(salehVO.getCbiztype())) {
//			fydnewVO.setPk_busitype(salehVO.getCbiztype()); // 业务类型
//		}
//		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
//			fydnewVO.setFyd_bz(salehVO.getVnote()); // 备注
//		}
//		if (null != salehVO.getDapprovedate()
//				&& !"".equals(salehVO.getDapprovedate())) {
//			fydnewVO.setDapprovedate(salehVO.getDapprovedate()); // 审批时间
//		}
//		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
//				.getPrimaryKey()); // 设置调度员
//		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
//				.getPrimaryKey()); // 设置制单人
//		fydnewVO.setBilltype(new Integer(1));
//		myBillVO.setParentVO(fydnewVO);
//
//		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
//			SaleorderBVO[] salebVO = (SaleorderBVO[]) vos[0].getChildrenVO();
//			TbFydmxnewVO[] fydmxnewVO = new TbFydmxnewVO[salebVO.length];
//			// 获取缓存 转换子表其他的信息
//			for (int i = 0; i < salebVO.length; i++) {
//				SaleorderBVO salebvo = salebVO[i];
//				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
//				if (null != salebvo.getCorder_bid()
//						&& !"".equals(salebvo.getCorder_bid())) {
//					fydmxnewvo.setCorder_bid(salebvo.getCorder_bid()); // 销售附表主键
//				}
//				if (null != salebvo.getNnumber()
//						&& !"".equals(salebvo.getNnumber())) {
//					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber()); // 应发数量
//				}
//				if (null != salebvo.getNpacknumber()
//						&& !"".equals(salebvo.getNpacknumber())) {
//					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber()); // 箱数
//				}
//
//				fydmxnewVO[i] = fydmxnewvo;
//
//			}
//			myBillVO.setChildrenVO(fydmxnewVO);
//		}
//
//		return myBillVO;
//	}
		
}