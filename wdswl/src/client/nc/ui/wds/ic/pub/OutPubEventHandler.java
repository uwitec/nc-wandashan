package nc.ui.wds.ic.pub;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.wds.ic.so.out.TrayDisposeDlg;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.WdsPubEnventHandler;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;

public class OutPubEventHandler extends WdsPubEnventHandler {

	public OutPubClientUI ui = null;

	public OutPubEventHandler(OutPubClientUI billUI, IControllerBase control) {
		super(billUI, control);
		ui = billUI;
	}

	/*
	 * �Զ�ȡ��(non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	protected void onzdqh() throws Exception {
		int results = ui.showOkCancelMessage("ȷ���Զ����?");
		if (results != 1) {
			return;
		}
		if (getBillUI().getVOFromUI() == null
				|| getBillUI().getVOFromUI().getChildrenVO() == null
				|| getBillUI().getVOFromUI().getChildrenVO().length == 0) {
			ui.showErrorMessage("��ȡ��ǰ�������ݳ���.");
			return;
		}
		AggregatedValueObject billvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] generalbVOs = (TbOutgeneralBVO[]) billvo
				.getChildrenVO();

		// ����У�� begin
		WdsWlPubTool.validationOnPickAction(getBillCardPanelWrapper()
				.getBillCardPanel(), generalbVOs);
		// ����У��end
		String pk_stordoc = PuPubVO
				.getString_TrimZeroLenAsNull(getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("srl_pk")
						.getValueObject());
		Map<String, List<TbOutgeneralTVO>> trayInfor = null;
		// ui.showProgressBar(true);
		try {
			Class[] ParameterTypes = new Class[] { String.class,
					AggregatedValueObject.class, String.class };
			Object[] ParameterValues = new Object[] { ui._getOperator(),
					billvo, pk_stordoc };
			Object o = LongTimeTask.callRemoteService(
					WdsWlPubConst.WDS_WL_MODULENAME,
					"nc.bs.wds.w8004040204.W8004040204Impl", "autoPickAction",
					ParameterTypes, ParameterValues, 2);
			if (o != null) {
				trayInfor = (Map<String, List<TbOutgeneralTVO>>) o;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			ui.showProgressBar(false);
		}
		if (null == trayInfor || trayInfor.size() == 0) {
			chaneColor();
			return;
		}
		ui.setTrayInfor(trayInfor);
		if (getBillUI().getBillOperate() == IBillOperate.OP_EDIT) {
			// ����Ϣͬ��������
			TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) getBufferData()
					.getCurrentVO().getChildrenVO();
			if (bodys != null && bodys.length != 0) {
				String key = null;
				for (TbOutgeneralBVO body : bodys) {
					key = body.getCrowno();
					body.setTrayInfor(trayInfor.get(key));
				}
			}
		}

		WdsWlPubTool.setDatasToPanelForOutBill(generalbVOs,
				getBillCardPanelWrapper().getBillCardPanel(), trayInfor);

		chaneColor();
		setBodyModelState();
		ontpzd();
	}



	/**
	 * tT
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ
	 * @ʱ�䣺2011-4-20����09:09:55
	 */
	protected void setBodyModelState() {
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.getRowCount();
		if (row < 0)
			return;
		for (int i = 0; i < row; i++) {
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setRowState(i, BillModel.MODIFICATION);
		}
	}

	// �������ݽ�����Ϻ� ����Ĭ��ֵ
	@Override
	protected void setRefData(AggregatedValueObject[] vos) throws Exception {
		super.setRefData(vos);
		getBillUI().setDefaultData();
		setBodySpace();
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		setBodySpace();
	}

	// ���帳��λ
	protected void setBodySpace() throws BusinessException {
		String pk_cargdoc = getPk_cargDoc();
		if (pk_cargdoc == null || "".equals(pk_cargdoc)) {
			// throw new BusinessException("��ָ����λ��Ϣ");
			return;
		}
		int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		for (int i = 0; i < row; i++) {
			getBillCardPanelWrapper().getBillCardPanel().getBillModel()
					.setValueAt(pk_cargdoc, i, "cspaceid");
			//liuys add �������� ����Ĭ��ҵ������
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(_getDate(), i, "dbizdate");
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().execLoadFormulasByKey("cspaceid");
		}
	}

	// ��ͷ��ȡ�����λ
	protected String getPk_cargDoc() {
		String pk_cargdoc = (String) getBillCardPanelWrapper()
				.getBillCardPanel().getHeadItem("pk_cargdoc").getValueObject();
		return pk_cargdoc;
	}

	// ��ͷ��ȡ����ֿ�
	protected String getCwhid() {
		String cwhid = (String) getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("srl_pk").getValueObject();
		return cwhid;
	}

	/*
	 * ��ѯ��ϸ
	 */
	protected void onckmx() throws Exception {
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(
				WdsWlPubConst.DLG_OUT_TRAY_APPOINT, ui._getOperator(), ui
						._getCorp().getPrimaryKey(), ui, false);
		tdpDlg.showModal();
	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-6-14����09:43:54
	 * @throws Exception
	 */
	public void chaneColor() throws Exception {
		TbOutgeneralBVO[] generalbVO = (TbOutgeneralBVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();

		// ��ȡ���жϱ����Ƿ���ֵ������ѭ��
		if (null != generalbVO && generalbVO.length > 0) {
			for (int i = 0; i < generalbVO.length; i++) {
				// ��ȡ��ǰ�����е�Ӧ����������ʵ�����������бȽϣ����ݱȽϽ��������ɫ��ʾ
				// ��ɫ��û��ʵ����������ɫ����ʵ����������������������ɫ��ʵ��������Ӧ���������
				UFDouble num = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanelWrapper()
								.getBillCardPanel().getBodyValueAt(i,
										"nshouldoutassistnum"));// Ӧ��������
				UFDouble tatonum = PuPubVO
						.getUFDouble_NullAsZero(getBillCardPanelWrapper()
								.getBillCardPanel().getBodyValueAt(i,
										"noutassistnum"));// ʵ��������
				if (tatonum.doubleValue() == 0) {
					getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
							.setCellBackGround(i, "ccunhuobianma", Color.red);
				} else {
					if (tatonum.sub(num, 8).doubleValue() == 0) {
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.white);
					} else if (tatonum.sub(num, 8).doubleValue() < 0) {
						getBillCardPanelWrapper().getBillCardPanel()
								.getBodyPanel().setCellBackGround(i,
										"ccunhuobianma", Color.blue);
					}
				}

			}

		}

	}

	protected void onBoCancel() throws Exception {
		super.onBoCancel();
		onBoRefresh();
	}

	@Override
	protected void onBoSave() throws Exception {
		CircularlyAccessibleValueObject[] bodys = getBillUI().getVOFromUI()
				.getChildrenVO();
		super.onBoSave();
		onBoRefresh();
	}

	/*
	 * ����ָ��(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w8004040208.AbstractMyEventHandler#ontpzd()
	 */
	protected void ontpzd() throws Exception {
		String pk_cargdoc = getPk_cargDoc();// ��λ
		if (pk_cargdoc == null || "".equals(pk_cargdoc))
			throw new BusinessException("��ָ����λ��Ϣ");
		AggregatedValueObject aggvo = getBillUI().getVOFromUI();
		TbOutgeneralBVO[] bvo = (TbOutgeneralBVO[]) aggvo.getChildrenVO();
		if (bvo == null || bvo.length == 0)
			throw new BusinessException("û�б������ݣ����������! ");
		for (TbOutgeneralBVO vo : bvo) {
			vo.validationOnZdck();
		}
		TrayDisposeDlg tdpDlg = new TrayDisposeDlg(
				WdsWlPubConst.DLG_OUT_TRAY_APPOINT, ui._getOperator(), ui
						._getCorp().getPrimaryKey(), ui, true);
		if (tdpDlg.showModal() == UIDialog.ID_OK) {
			Map<String, List<TbOutgeneralTVO>> map2 = tdpDlg.getBufferData();
			ui.setTrayInfor(map2);
			setBodyValueToft();
		}
		chaneColor();
		setBodyModelState();
	}

	public void setBodyValueToft() {
		int row = getBodyRowCount();
		Map<String, List<TbOutgeneralTVO>> map = ((OutPubClientUI) getBillUI())
				.getTrayInfor();
		if (row > 0) {
			for (int i = 0; i < row; i++) {
				String crowno = (String) getBillCardPanelWrapper()
						.getBillCardPanel().getBillModel().getValueAt(i,
								"crowno");// �к�
				UFDouble[] d = getDFromBuffer(crowno);
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[0], i, "noutnum");// ������
				getBillCardPanelWrapper().getBillCardPanel().getBillModel()
						.setValueAt(d[1], i, "noutassistnum");// ������
				List<TbOutgeneralTVO> list = map.get(crowno);
				if (list == null || list.size() <= 0) {
					return;
				}
				// ��ȡ���κ�
				// ���κ�
				String vbachcode = "";
				// ��Դ���κ�
				String svbachcode = "";
				// ��ȡ����
				List vbp = new ArrayList();
				// ��ȡ��Դ����
				List vbl = new ArrayList();
				for (int j = 0; j < list.size(); j++) {
					TbOutgeneralTVO vo = list.get(j);
					if (vo.getVbatchcode() != null
							&& !vo.getVbatchcode().equalsIgnoreCase("")) {
						if (!vbp.contains(vo.getVbatchcode())) {
							vbachcode = vbachcode + "," + vo.getVbatchcode();
							vbp.add(vo.getVbatchcode());
						}
					}
					if (vo.getLvbatchcode() != null
							&& !vo.getLvbatchcode().equalsIgnoreCase("")) {
						if (!vbl.contains(vo.getVbatchcode())) {
							svbachcode = svbachcode + "," + vo.getLvbatchcode();
							vbl.add(vo.getLvbatchcode());
						}
					}
				}
				if (vbachcode.length() > 1) {
					// ����������κŸ�ֵ
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									vbachcode.substring(1, vbachcode.length()),
									i, "vbatchcode");
				}

				if (svbachcode.length() > 1) {
					// ���������Դ���κŸ�ֵ
					getBillCardPanelWrapper().getBillCardPanel()
							.setBodyValueAt(
									svbachcode
											.substring(1, svbachcode.length()),
									i, "lvbatchcode");
				}
			}
		}
	}

	public int getBodyRowCount() {
		int rowcount = -1;
		if (ui.getBillListPanel().isShowing()) {
			rowcount = ui.getBillListPanel().getBodyBillModel().getRowCount();
		} else {
			rowcount = ui.getBillCardPanel().getBillModel().getRowCount();
		}
		return rowcount;
	}

	public UFDouble[] getDFromBuffer(String crowno) {
		UFDouble[] d = new UFDouble[2];
		d[0] = new UFDouble(0);// ʵ������
		d[1] = new UFDouble(0);// ʵ�븨����
		List<TbOutgeneralTVO> list = ui.getTrayInfor().get(crowno);
		if (list != null && list.size() > 0) {
			for (TbOutgeneralTVO l : list) {
				UFDouble b = l.getNoutnum();
				UFDouble b1 = l.getNoutassistnum();// ʵ�븨����
				d[0] = d[0].add(b);
				d[1] = d[1].add(b1);
			}
		}
		return d;
	}

}
