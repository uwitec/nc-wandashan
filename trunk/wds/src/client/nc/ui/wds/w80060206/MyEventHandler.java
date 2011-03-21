package nc.ui.wds.w80060206;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.bs.ic.mm.pub.GenMethod;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.sfapp.IBillcodeRuleService;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.MessageEvent;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillTemplateWrapper;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.billcodemanage.BillCodeObjValueVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060206.MyBillVO;
import nc.vo.wds.w80060206.TbProdwaybillBVO;
import nc.vo.wds.w80060206.TbProdwaybillVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);

	}

	// �ж�����ӻ����޸�
	private boolean addoredit = true;
	private MyClientUI myClientUI;

	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		super.onBoAdd(bo);
	}
	
	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w80060206.pwButtun.IPwButtun.PwAdd).setEnabled(true);

		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
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
		showZeroLikeNull(false);
	}

	protected void onPwAdd() {
		// TODO ��ʵ�ִ˰�ť�¼����߼�

		ProdWaybillDlg pwbQuery = new ProdWaybillDlg(myClientUI);

		AggregatedValueObject[] vos = pwbQuery.getReturnVOs(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"0206", "4Y", "80060206", "8006020601", myClientUI);

		try {
			
			if (null == vos || vos.length == 0) {
				return;
			}

			// if (((BillManageUI)getBillUI()).isListPanelSelected())
			// ((BillManageUI)getBillUI()).setCurrentPanel(BillTemplateWrapper.CARDPANEL);

			MyBillVO voForSave = changeGentoTbpwb(vos);
			getBufferData().clear();
			getBufferData().addVOToBuffer(voForSave);
			updateBuffer();
			// getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			super.onBoEdit();
			getButtonManager().getButton(IBillButton.Edit).setEnabled(false);

			getButtonManager().getButton(IBillButton.Save).setEnabled(true);
			getButtonManager().getButton(IBillButton.Cancel).setEnabled(true);
			getButtonManager().getButton(
					nc.ui.wds.w80060206.pwButtun.IPwButtun.PwAdd).setEnabled(
					false);
			getButtonManager().getButton(IBillButton.Print).setEnabled(false);
			getButtonManager().getButton(IBillButton.Refresh).setEnabled(false);
			getButtonManager().getButton(IBillButton.Query).setEnabled(false);
			getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
			getBillUI().updateButtonUI();

		} catch (Exception e) {
			getBillUI().showErrorMessage(e.getMessage());
		}
		addoredit = true;
		showZeroLikeNull(false);
	}

	private MyBillVO changeGentoTbpwb(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		// ��ȡ����VO
		TbProdwaybillVO tbProdwaybillVO = new TbProdwaybillVO();

		GeneralBillHeaderVO firstVO = (GeneralBillHeaderVO) vos[0]
				.getParentVO();
		tbProdwaybillVO.setPwb_vbillcode(firstVO.getVbillcode());
		if (null != firstVO.getAttributeValue("cotherwhid")) {
			tbProdwaybillVO.setPwb_cwarehouseid(firstVO.getAttributeValue(
					"cotherwhid").toString());
		}
		tbProdwaybillVO.setPwb_cwhsmanagerid(firstVO.getCwhsmanagerid());
		if (null != firstVO.getAttributeValue("cothercorpid")) {
			tbProdwaybillVO.setPwb_corp(firstVO.getAttributeValue(
					"cothercorpid").toString());
		}
		tbProdwaybillVO.setPwb_cdptid(firstVO.getCdptid());

		if (null != firstVO.getAttributeValue("cothercalbodyid")) {
			tbProdwaybillVO.setPwb_calbody(firstVO.getAttributeValue(
					"cothercalbodyid").toString());
		}
		tbProdwaybillVO.setPwb_cotherwhid(firstVO.getCwarehouseid());
		tbProdwaybillVO.setPwb_cothercorpid(firstVO.getPk_corp());
		tbProdwaybillVO.setPwb_cothercalbodyid(firstVO.getPk_calbody());
		// tbProdwaybillVO.setPwb_cdilivertypeid(firstVO.getCdilivertypeid());
		// tbProdwaybillVO.setPwb_cinventoryid(firstVO.getCdilivertypeid());
		tbProdwaybillVO.setPwb_cbizid(firstVO.getCbizid());
		tbProdwaybillVO.setPwb_cdispatcherid(firstVO.getCdispatcherid());
		tbProdwaybillVO.setCoperatorid(ClientEnvironment.getInstance()
				.getUser().getPrimaryKey());
		//��ӵ�����ⵥ����
		tbProdwaybillVO.setPwb_cgeneralhid(firstVO.getCgeneralhid());
		tbProdwaybillVO.setCopetadate(new UFDate(new Date()));
		// ��������˵���
//		ArrayList list = new ArrayList();
//		
//		for (int i = 0; i < 10; i++) {
//			list.add(i);
//			
//		}
//
//		StringBuffer mathnum = new StringBuffer("TC");
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
//		String today=sdf.format(new Date());
//		String myToday=today.substring(2);
//		mathnum.append(myToday);
//		for (int i = 0; i < 4; i++) {
//			int mathInt;
//			mathInt = (int) (Math.random() * list.size());
//			mathnum.append(list.get(mathInt));
//			// ���ظ�������
//			// list.remove(mathInt);
//		}
		//��ӵ��ݺ�
		try {
			tbProdwaybillVO.setPwb_billcode(getBillCode("0210", "", "", ""));
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//���õ���״̬
		tbProdwaybillVO.setPwb_fbillflag(1);
		//���õ�������
		tbProdwaybillVO.setPwb_cbilltypecode("8");
		//��������
		tbProdwaybillVO.setPwb_fallocflag(firstVO.getFallocflag());
		//�������VO
		myBillVO.setParentVO(tbProdwaybillVO);

		// ��ȡ�ӱ�vo
		String pk = firstVO.getCgeneralhid().toString();
		String sql = "select cinvbasid ,cinventoryid,crowno,vbatchcode "
				+ ",dvalidate,noutnum,ntranoutnum ,nprice"
				+ ",cgeneralbid ,castunitid,noutassistnum,ntranoutastnum" 
				+ ",hsl from ic_general_b where cgeneralhid='"
				+ firstVO.getCgeneralhid().toString()
				+ "' and ((coalesce(noutnum, 0) > 0 " 
				+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) > 0)" 
				+ " or (coalesce(noutnum, 0) < 0" 
				+ " and coalesce(noutnum, 0) - coalesce(ntranoutnum, 0) - coalesce(naccumwastnum, 0) < 0))" 
				+ " and dr=0";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		Vector myTemp = new Vector();
		try {
			ArrayList ttcs = (ArrayList) query.executeQuery(sql.toString(),
					new ArrayListProcessor());
			for (int i = 0; i < ttcs.size(); i++) {
				Object[] gbvo = (Object[]) ttcs.get(i);
				TbProdwaybillBVO tbProdwaybillBVO = new TbProdwaybillBVO();
				
				tbProdwaybillBVO.setPk_customize8(gbvo[8].toString());
				if (null != gbvo[0]) {
					tbProdwaybillBVO.setPwbb_cinvbasid(gbvo[0].toString());
				}
				if (null != gbvo[1]) {
					tbProdwaybillBVO.setPwbb_cinventoryid(gbvo[1].toString());
				}

				if (null != gbvo[2]) {
					tbProdwaybillBVO.setPwbb_crowno(gbvo[2]
							.toString());
				}
				if (null != gbvo[3]) {
					tbProdwaybillBVO.setPwbb_vbatchcode(gbvo[3].toString());
				}
				//�������Σ���ѯ�������ڣ�ʧЧ����
				String vbcsql = "select dproducedate ,dvalidate  from scm_batchcode where pk_invbasdoc='"
						+ tbProdwaybillBVO.getPwbb_cinvbasid()
						+ "' and vbatchcode='"
						+ tbProdwaybillBVO.getPwbb_vbatchcode() + "' and dr=0";
				ArrayList vbc = (ArrayList) query.executeQuery(vbcsql,
						new ArrayListProcessor());
				tbProdwaybillBVO.setPwbb_proddate(new UFDate(((Object[]) vbc
						.get(0))[0].toString()));
				tbProdwaybillBVO.setPwbb_dvalidate(new UFDate(((Object[]) vbc
						.get(0))[1].toString()));
				// if(null!=gbvo[4]){
				// tbProdwaybillBVO.setPwbb_dvalidate(new
				// UFDate(gbvo[4].toString()));
				// }
				//���ʵ��������Ӧ������
				double noutnum = 0.00;
				double ntranoutnum = 0.00;
				if (null != gbvo[5]) {
					noutnum = Double.parseDouble(gbvo[5].toString());
				}
				if (null != gbvo[6]) {
					ntranoutnum = Double.parseDouble(gbvo[6].toString());
				}
				tbProdwaybillBVO.setPwbb_snum(new UFDouble(noutnum
						- ntranoutnum));
				
				tbProdwaybillBVO.setPwbb_anum(tbProdwaybillBVO.getPwbb_snum());
				//
				if (null != gbvo[7]) {
					tbProdwaybillBVO.setPwbb_nprice(new UFDouble(gbvo[7]
							.toString()));
					tbProdwaybillBVO.setPwbb_nmny(new UFDouble(Double
							.parseDouble(gbvo[7].toString())
							* (noutnum - ntranoutnum)));
				}
				//����ӱ�����
				if (null!=gbvo[8]) {
					tbProdwaybillBVO.setPwbb_cgeneralbid(gbvo[8].toString());
				}
				//��ø�������λID
				if (null != gbvo[9]) {
					tbProdwaybillBVO.setCastunitid(gbvo[9].toString());
				}
				//���Ӧ�˸�������ʵ�˸�����
				double noutassistnum = 0.00;
				double ntranoutastnum = 0.00;
				if (null != gbvo[10]) {
					noutassistnum=Double.parseDouble(gbvo[10].toString());
				}
				if (null != gbvo[11]) {
					ntranoutastnum=Double.parseDouble(gbvo[11].toString());
				}
				tbProdwaybillBVO.setPwbb_bsnum(new UFDouble(noutassistnum-ntranoutastnum));
				tbProdwaybillBVO.setPwbb_banum(tbProdwaybillBVO.getPwbb_bsnum());
				//��û�����
				if(null!=gbvo[12]){
					tbProdwaybillBVO.setPwbb_hsl(new UFDouble(gbvo[12].toString()));
				}
				
				//�ı�״̬
				tbProdwaybillBVO.setStatus(VOStatus.NEW);
				myTemp.add(tbProdwaybillBVO);
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TbProdwaybillBVO[] tbProdwaybillBVOs = new TbProdwaybillBVO[myTemp
				.size()];
		tbProdwaybillBVOs = (TbProdwaybillBVO[]) myTemp
				.toArray(tbProdwaybillBVOs);

		myBillVO.setChildrenVO(tbProdwaybillBVOs);

		return myBillVO;

	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoDelete();
		getButtonManager().getButton(
				nc.ui.wds.w80060206.pwButtun.IPwButtun.PwAdd).setEnabled(true);
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		if (addoredit) {
			getBufferData().clear();
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
			getBillUI().initUI();
			// super.onBoRefresh();
		}
		getButtonManager().getButton(
				nc.ui.wds.w80060206.pwButtun.IPwButtun.PwAdd).setEnabled(true);

		super.onBoCancel();

	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w80060206.pwButtun.IPwButtun.PwAdd).setEnabled(false);
		super.onBoEdit();
		addoredit = false;
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
	
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}
	// ��õ��ݺ�
	public String getBillCode(String billtype, String pkcorp, String gcbm,
			String operator) throws BusinessException {
		String scddh = null;
		try {
			BillCodeObjValueVO vo = new BillCodeObjValueVO();
			String[] names = { "�����֯", "����Ա" };
			String[] values = new String[] { gcbm, operator };
			vo.setAttributeValue(names, values);
			scddh = getBillCode(billtype, pkcorp, vo);
		} catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return scddh;
	}

	private String getBillCode(String billtype, String pkcorp,
			nc.vo.pub.billcodemanage.BillCodeObjValueVO billVO)
			throws BusinessException {
		String djh = null;
		try {
			IBillcodeRuleService bo = (IBillcodeRuleService) NCLocator
					.getInstance().lookup(IBillcodeRuleService.class.getName());
			djh = bo.getBillCode_RequiresNew(billtype, pkcorp, null, billVO);

		} catch (Exception e) {
			GenMethod.throwBusiException(e);
		}
		return djh;
	}

}