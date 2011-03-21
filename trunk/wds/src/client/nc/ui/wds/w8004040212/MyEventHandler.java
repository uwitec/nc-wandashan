package nc.ui.wds.w8004040212;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.ui.trade.query.INormalQuery;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wds.w80021008.TbStockstaffVO;
import nc.vo.wds.w80040402020.TbMovetrayVO;
import nc.vo.wds.w8004040212.TbWarehousestockVO;
import nc.vo.wds.w8004061002.BdCargdocTrayVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private String st_type = "";
	private String stordocName = "";
	private boolean sotckIsTotal = true;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);

		try {
			st_type = nc.ui.wds.w8000.CommonUnit.getUserType(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (null != st_type && !"".equals(st_type)) {
			if ("0".equals(st_type) || "3".equals(st_type)) {
				try {
					stordocName = nc.ui.wds.w8000.CommonUnit
							.getStordocName(ClientEnvironment.getInstance()
									.getUser().getPrimaryKey());
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (null != stordocName && !"".equals(stordocName)) {
					try {
						sotckIsTotal = nc.ui.wds.w8000.CommonUnit
								.getSotckIsTotal(stordocName);

					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub

		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// ��λ
		List invLisk = nc.ui.wds.w8000.CommonUnit
				.getInvbasdoc_Pk(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());

		String pk_cargdoc = "";
		try {
			pk_cargdoc = nc.ui.wds.w8000.CommonUnit
					.getCargDocName(ClientEnvironment.getInstance().getUser()
							.getPrimaryKey());
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		StringBuffer strWhere = new StringBuffer();
		// 20110301
		UIDialog querydialog = getQueryUI();
		if (querydialog.showModal() != UIDialog.ID_OK) {
			return;
		}
		// �õ���ѯ����
		String strWhere1 = ((HYQueryConditionDLG) querydialog).getWhereSql();
		if (strWhere1 == null)
			strWhere1 = " 1=1 ";

		// �õ����еĲ�ѯ��ǩ
		ConditionVO[] ttc = ((HYQueryConditionDLG) querydialog)
				.getQryCondEditor().getGeneralCondtionVOs();
		String sendsignState = "";
		for (ConditionVO vo : ttc) {
			if ("tb_warehousestock.traycode".equals(vo.getFieldCode())) {
				sendsignState = vo.getValue();
				if (null != sendsignState && !"".equals(sendsignState)) {
					String sql = " cdt_traycode='" + sendsignState
							+ "' and pk_cargdoc='" + pk_cargdoc + "' ";
					ArrayList ttcs = new ArrayList();
					try {
						ttcs = (ArrayList) query.retrieveByClause(
								BdCargdocTrayVO.class, sql);
					} catch (BusinessException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					BdCargdocTrayVO[] ttcVO = new BdCargdocTrayVO[ttcs.size()];
					ttcs.toArray(ttcVO);
					if (ttcVO.length > 0 && null != ttcVO[0]
							&& null != ttcVO[0].getCdt_pk()
							&& !"".equals(ttcVO[0].getCdt_pk())) {
						strWhere1 = StringUtil.replaceIgnoreCase(strWhere1,
								"tb_warehousestock.traycode = '"
										+ sendsignState + "'",
								"tb_warehousestock.pplpt_pk='"
										+ ttcVO[0].getCdt_pk() + "' ");
					} else {
						strWhere1 = StringUtil.replaceIgnoreCase(strWhere1,
								"tb_warehousestock.traycode = '"
										+ sendsignState + "'", " 1=2 ");
					}

				}
			}
		}
		strWhere.append(strWhere1.toString());
		// 20110301

		if (null != st_type && "0".equals(st_type)) {
			if (sotckIsTotal) {
				if (null != invLisk && invLisk.size() > 0) {
					strWhere.append(" and pk_invbasdoc in ('");
					strWhere.append(invLisk.get(0));
					for (int i = 1; i < invLisk.size(); i++) {
						strWhere.append("','");
						strWhere.append(invLisk.get(i));
					}
					strWhere
							.append("')  and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString()
									+ "' and cdt_traystatus=1 and dr=0) "
									+ " and whs_stockpieces !=0 ");
				} else {

					strWhere
							.append("  and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString()
									+ "' and cdt_traystatus=1 and dr=0) "
									+ " and whs_stockpieces !=0 ");
				}
			} else {

				if (null != pk_cargdoc && !"".equals(pk_cargdoc)) {
					strWhere
							.append(" and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString() + "' and dr=0) "
									+ " and whs_stockpieces !=0  ");
					/* cdt_traystatus=1 and */
				} else {
					strWhere.append("and 1=2 ");
				}
			}
		} else if (null != st_type && "3".equals(st_type)) {
			if (sotckIsTotal) {
				if (null != invLisk && invLisk.size() > 0) {
					strWhere.append(" and pk_invbasdoc in ('");
					strWhere.append(invLisk.get(0));
					for (int i = 1; i < invLisk.size(); i++) {
						strWhere.append("','");
						strWhere.append(invLisk.get(i));
					}
					strWhere
							.append("')  and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString()
									+ "' and cdt_traystatus=1 and dr=0) "
									+ " and whs_stockpieces !=0 ");
				} else {

					strWhere
							.append("  and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString()
									+ "' and cdt_traystatus=1 and dr=0) "
									+ " and whs_stockpieces !=0 ");
				}
			} else {

				if (null != pk_cargdoc && !"".equals(pk_cargdoc)) {
					strWhere
							.append(" and pk_cargdoc = '"
									+ pk_cargdoc.toString()
									+ "' and dr=0 "
									+ " and whs_status=0 "
									+ " and pplpt_pk in (select cdt_pk from bd_cargdoc_tray where pk_cargdoc='"
									+ pk_cargdoc.toString() + "' and dr=0) "
									+ " and whs_stockpieces !=0  ");
					/* cdt_traystatus=1 and */
				} else {
					strWhere.append("and 1=2 ");
				}
			}
		} else {
			strWhere.append(" and 1=2 ");
		}

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// �������ݵ�Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}

	/**
	 * ������ѯ�Ի������û�ѯ�ʲ�ѯ������ ����û��ڶԻ������ˡ�ȷ��������ô����true,���򷵻�false
	 * ��ѯ����ͨ�������StringBuffer���ظ�������
	 * 
	 * @param sqlWhereBuf
	 *            �����ѯ������StringBuffer
	 * @return �û�ѡȷ������true���򷵻�false
	 */
	protected boolean askForQueryCondition(StringBuffer sqlWhereBuf)
			throws Exception {
		if (sqlWhereBuf == null)
			throw new IllegalArgumentException(
					"askForQueryCondition().sqlWhereBuf cann't be null");
		UIDialog querydialog = getQueryUI();

		if (querydialog.showModal() != UIDialog.ID_OK)
			return false;
		INormalQuery query = (INormalQuery) querydialog;

		String strWhere = query.getWhereSql();
		if (strWhere == null)
			strWhere = "1=1";

		if (getButtonManager().getButton(IBillButton.Busitype) != null) {
			if (getBillIsUseBusiCode().booleanValue())
				// ҵ�����ͱ���
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_BusiCode() + "='"
						+ getBillUI().getBusicode() + "'";

			else
				// ҵ������
				strWhere = "(" + strWhere + ") and "
						+ getBillField().getField_Busitype() + "='"
						+ getBillUI().getBusinessType() + "'";

		}

		strWhere = "(" + strWhere + ") and (isnull(dr,0)=0)";

		// if (getHeadCondition() != null)
		// strWhere = strWhere + " and " + getHeadCondition();
		// ��������ֱ�Ӱ����ƴ�õĴ��ŵ�StringBuffer�ж���ȥ�Ż�ƴ���Ĺ���
		sqlWhereBuf.append(strWhere);
		return true;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		if (null == ((TbWarehousestockVO) billVO.getParentVO()).getPplpt_pk()) {
			getBillUI().showErrorMessage("���̲���Ϊ�գ�");
			return;
		}
		setTSFormBufferToVO(checkVO);
		// ���̼���
		ArrayList pplpts = new ArrayList();
		// δ�޸�ǰ����
		String whs_pk = ((TbWarehousestockVO) billVO.getParentVO()).getWhs_pk();
		String whsysql = " whs_pk='" + whs_pk + "' and dr=0 ";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		IVOPersistence perse = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		//
		ArrayList whss = new ArrayList();
		whss = (ArrayList) query.retrieveByClause(TbWarehousestockVO.class,
				whsysql);
		//
		String pplptysql = " cdt_pk='"
				+ ((TbWarehousestockVO) whss.get(0)).getPplpt_pk()
				+ "' and dr=0 ";
		ArrayList pplptys = new ArrayList();
		pplptys = (ArrayList) query.retrieveByClause(BdCargdocTrayVO.class,
				pplptysql);
		if (null != pplptys && pplptys.size() > 0) {
			((BdCargdocTrayVO) pplptys.get(0)).setCdt_traystatus(0);
			pplpts.add(pplptys.get(0));
		}
		// �޸ĺ�
		String pplptx_pk = ((TbWarehousestockVO) billVO.getParentVO())
				.getPplpt_pk();
		String pplptxsql = " cdt_pk='" + pplptx_pk + "' and dr=0 ";
		ArrayList pplptxs = new ArrayList();
		pplptxs = (ArrayList) query.retrieveByClause(BdCargdocTrayVO.class,
				pplptxsql);
		if (null != pplptxs && pplptxs.size() > 0) {
			((BdCargdocTrayVO) pplptxs.get(0)).setCdt_traystatus(1);
			pplpts.add(pplptxs.get(0));
		}
		//
		if (!((TbWarehousestockVO) whss.get(0)).getPplpt_pk().equals(pplptx_pk)) {

			perse.updateVOList(pplpts);
		}

		// �����ƶ����
		TbMovetrayVO tbMovetrayVO = new TbMovetrayVO();
		tbMovetrayVO.setDbilldate(new UFDate(new Date()));
		tbMovetrayVO.setMakeman_pk(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey());
		tbMovetrayVO.setOuttray_pk(((TbWarehousestockVO) whss.get(0))
				.getPplpt_pk());
		tbMovetrayVO.setIntray_pk(pplptx_pk);

		tbMovetrayVO
				.setPk_invbasdoc(((TbWarehousestockVO) billVO.getParentVO())
						.getPk_invbasdoc());
		tbMovetrayVO.setMty_stockpieces(((TbWarehousestockVO) billVO
				.getParentVO()).getWhs_stockpieces());
		tbMovetrayVO.setMty_stocktonnage(((TbWarehousestockVO) billVO
				.getParentVO()).getWhs_stocktonnage());
		tbMovetrayVO.setDbilltime(getBillUI()._getServerTime().toString());
		tbMovetrayVO.setDr(0);
		tbMovetrayVO
				.setPk_customize(((TbWarehousestockVO) billVO.getParentVO())
						.getPk_cargdoc());
		perse.insertVO(tbMovetrayVO);

		// �����������
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

		// �ж��Ƿ��д�������
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

		// �������ݻָ�����
		if (sCtrl != null) {
			if (sCtrl.isSingleDetail())
				billVO.setParentVO((CircularlyAccessibleValueObject) o);
		}
		int nCurrentRow = -1;
		if (isSave) {
			if (isEditing()) {
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
					nCurrentRow = 0;

				} else {
					getBufferData().setCurrentVO(billVO);
					nCurrentRow = getBufferData().getCurrentRow();
				}
			}
			// �������������
			setAddNewOperate(isAdding(), billVO);
		}
		// ���ñ����״̬
		setSaveOperateState();
		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
	}
}