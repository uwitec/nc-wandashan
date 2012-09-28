package nc.ui.wds.load.account;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.ui.wl.pub.LoginInforHelper;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.ButtonCommon;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report2.ReportBuffer;

public class ClientEventHandler extends WdsPubEnventHandler {

	public ClientUIQueryDlg queryDialog = null;

	public ClientEventHandler(ClientUI clientUI, IControllerBase control) {
		super(clientUI, control);
	}

	protected UIDialog createQueryUI() {
		if (queryDialog == null) {
			queryDialog = new ClientUIQueryDlg(getBillUI(), null, _getCorp()
					.getPrimaryKey(), getBillUI()._getModuleCode(),
					_getOperator(), getBillUI().getBusinessType(), getBillUI()
							.getNodeKey());
			// queryDialog = new ClientUIQueryDlg(getBillUI(), null, tempinfo);
		}
		return queryDialog;
	}

	@Override
	protected String getHeadCondition() {
		if (super.getHeadCondition() == null) {
			return " and pk_billtype = '" + getUIController().getBillType()
					+ "' ";
		}
		return super.getHeadCondition() + " and pk_billtype = '"
				+ getUIController().getBillType() + "' ";
	}

	/**
	 * @author yf 加行之前进行表体判定 如果是来源明细表体便签 = 0，不增行 int tab =
	 *         getBillCardPanelWrapper
	 *         ().getBillCardPanel().getBodyTabbedPane().getSelectedIndex();
	 */
	@Override
	protected void onBoLineAdd() throws Exception {
		int tab = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyTabbedPane().getSelectedIndex();
		if (0 == tab) {
			MessageDialog.showWarningDlg(getBillUI(), "录入错误", "不能在此新增来源明细");
			return;
		}
		super.onBoLineAdd();
	}

	@Override
	protected void onBoSave() throws Exception {
		// 对总费用 分配给班组的费用校验
		ExaggLoadPricVO billvo = (ExaggLoadPricVO) getBillUI().getVOFromUI();
		LoadpriceHVO h = (LoadpriceHVO) billvo.getParentVO();
		LoadpriceB2VO[] b = (LoadpriceB2VO[]) billvo
				.getTableVO("wds_loadprice_b2");
		/**
		 * @author yf 校验 录入时表体数据不能为空
		 */
		if (b == null || b.length <= 0) {
			MessageDialog.showWarningDlg(getBillUI(), "录入错误", "保存前请先分配班组");
			return;
		}
		UFDouble zfee = h.getVzfee();
		if (h != null) {
			UFDouble zbz = new UFDouble();
			for (int i = 0; i < b.length; i++) {
				/**
				 * @author yf 校验 班组编码不能为空 同时 避免空指针异常
				 */
				if (BeforeSaveValudate.isEmpty(b[i].getPk_wds_teamdoc_h())) {
					MessageDialog.showWarningDlg(getBillUI(), "录入错误",
							"班组编码不能为空");
					return;
				}
				if (BeforeSaveValudate.isEmpty(b[i].getNloadprice())) {
					MessageDialog.showWarningDlg(getBillUI(), "录入错误",
							"班组费用不能为空");
					return;
				}
				zbz = zbz.add(b[i].getNloadprice());
			}
			if (zbz != null) {
				if (zfee.sub(zbz).intValue() < 0) {
					throw new BusinessException(" 班组装卸费用不能大于总费用");
				}
			}

		}

		super.onBoSave();
	}
	@Override
	protected void onBoEdit() throws Exception {	
		valuteEnd();
		super.onBoEdit();
	}
   /**
    * 检验 调拨入库回传单 是否已经审批 如果已经审批 则装卸费核算单 不允许编辑
 * @throws Exception 
    * @作者：zhf
    * @说明：完达山物流项目 
    * @时间：2012-9-28下午12:21:53
    */
	private void valuteEnd() throws Exception {
     if (getBufferData().getCurrentVO() == null)
			return;
     ExaggLoadPricVO billvo=(ExaggLoadPricVO) getBufferData().getCurrentVO();
	 
	 if(billvo.getTableVO(billvo.getTableCodes()[0])==null ||billvo.getTableVO(billvo.getTableCodes()[0]).length==0)
		 return ;
	 SuperVO bodyvo= (SuperVO) billvo.getTableVO(billvo.getTableCodes()[0])[0];
	  String chid=PuPubVO.getString_TrimZeroLenAsNull(bodyvo.getAttributeValue("csourcebillhid"));
	 if(chid==null )
		return ;

		Class[] ParameterTypes = new Class[] { String.class };
		Object[] ParameterValues = new Object[] { chid };
	     LongTimeTask.calllongTimeService("wds", getBillUI(),
				"数据校验...", 1, "nc.bs.pub.action.N_WDSF_DELETE", null,
				"valuteEnd1", ParameterTypes, ParameterValues);	
		
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		super.onBoElse(intBtn);
		switch (intBtn) {
		case ButtonCommon.REFWDS6:
			((ClientUI) getBillUI())
					.setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_OUT);
			onBillRef();
			break;
		case ButtonCommon.REFWDS7:
			((ClientUI) getBillUI())
					.setRefBillType(WdsWlPubConst.BILLTYPE_OTHER_IN);
			onBillRef();
			break;
		case ButtonCommon.REFWDS8:
			((ClientUI) getBillUI())
					.setRefBillType(WdsWlPubConst.BILLTYPE_SALE_OUT);
			onBillRef();
			break;
		case ButtonCommon.UNLOCK:
			onUnLock();
			break;
		case ButtonCommon.LOCK:
			onLock();
			break;	
		}
		
	}
	@Override
	public void onBoAudit() throws Exception {
		if(getBufferData().getCurrentVO() ==null){
			getBillUI().showWarningMessage("请先选择一条数据");
			return;
		}
		LoadpriceHVO head = (LoadpriceHVO)getBufferData().getCurrentVO().getParentVO();
		UFBoolean fisended = PuPubVO.getUFBoolean_NullAs(head.getReserve14(), UFBoolean.FALSE);
		if(fisended == UFBoolean.FALSE ){
			getBillUI().showWarningMessage("单据尚未确认");
			return ;
		}	
		super.onBoAudit();
	}
	/**
	 * 取消冻结
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-23下午03:06:13
	 */
    private void onUnLock() throws Exception {
		LoadpriceHVO head=(LoadpriceHVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		head.setReserve14(new UFBoolean(false));
		HYPubBO_Client.update(head);
		onBoRefresh();
	}

	/**
     * 冻结
	 * @throws Exception 
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-7-23下午03:04:18
     */
	private void onLock() throws Exception {		
		LoadpriceHVO head=(LoadpriceHVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
		head.setReserve14(new UFBoolean(true));
		HYPubBO_Client.update(head);
		onBoRefresh();
	}

	@Override
	protected boolean isDataChange() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		// 设置单据状态

		// 项目主键 nloadprice 项目主键 nunloadprice 项目主键 ncodeprice 项目主键 ntagprice
		getBillUI().setCardUIState();
		AggregatedValueObject vo = refVOChange(vos);
		if (vo == null)
			throw new BusinessException("未选择参照单据");
		LoginInforHelper login = new LoginInforHelper();// zhf
		Class[] ParameterTypes = new Class[] { AggregatedValueObject[].class,
				String.class, String.class };
		Object[] ParameterValues = new Object[] { vos, _getCorp().getPk_corp(),
				login.getWhidByUser(_getOperator()) };
		Object o = LongTimeTask.callRemoteService(
				WdsWlPubConst.WDS_WL_MODULENAME,
				"nc.bs.wds.load.account.LoadAccountBS", "accoutLoadPrice",
				ParameterTypes, ParameterValues, 2);
		ExaggLoadPricVO billVo = (ExaggLoadPricVO) o;

		// 设置表头字段的总费用
		if (billVo.getTableVO("wds_loadprice_b1") != null) {
			LoadpriceB1VO[] vss = (LoadpriceB1VO[]) billVo
					.getTableVO("wds_loadprice_b1");
			UFDouble feess = new UFDouble();
			for (int i = 0; i < vss.length; i++) {
				UFDouble fees = new UFDouble();
				fees = PuPubVO.getUFDouble_NullAsZero(vss[i].getNloadprice())
						.add(
								PuPubVO.getUFDouble_NullAsZero(vss[i]
										.getNunloadprice())).add(
								PuPubVO.getUFDouble_NullAsZero(vss[i]
										.getNcodeprice())).add(
								PuPubVO.getUFDouble_NullAsZero(vss[i]
										.getNtagprice()));
				feess = feess.add(fees);
			}
			// if(billVo.getParentVO()!=null){
			// LoadpriceHVO l= (LoadpriceHVO)(billVo.getParentVO());
			// l.setVzfee(feess);
			// }
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vzfee",
					feess);
			// 设置为新增处理
			getBillUI().setBillOperate(IBillOperate.OP_REFADD);
			// 填充界面
			getBillCardPanelWrapper().setCardData(billVo);
			// 设置界面默认值
			((ClientUI) getBillUI()).setRefDefalutData();
		}

	}
}