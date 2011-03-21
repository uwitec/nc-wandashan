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
 *������AbstractMyEventHandler�������ʵ���࣬
 *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 *@author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	private MyClientUI myClientUI;
	private DcSaleOrderDlg dcSaleOrderDlg = null; // ��ѯ��������
	private boolean isAdd=false;
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
	}

	protected void ondc() throws Exception {
		// ʵ����ѯ��
		dcSaleOrderDlg = new DcSaleOrderDlg(myClientUI);

		// ���÷��� ��ȡ��ѯ��ľۺ�VO
		AggregatedValueObject[] vos = dcSaleOrderDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0216",
						ConstVO.m_sBillDRSQ, "8004040216", "021601", myClientUI);

		// �ж��Ƿ�Բ�ѯģ���н��в���
		if (null == vos || vos.length == 0) {
//			getBillUI().showWarningMessage("��û�н��в���!");
			return;
		}

		// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
		MyBillVO voForSave = changeReqFydtoOutgeneral(vos);
		// ����������� �Ͱ�ť��ʼ��
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		// �������
		getBufferData().addVOToBuffer(voForSave);
//		 ��������
		updateBuffer();
//		 �������Ӱ�ť״̬
		getButtonManager().getButton(nc.ui.wds.w8004040216.dcButtun.IDcButtun.dc)
				.setEnabled(false);
//		super.onBoEdit();
		// getBillUI().setBillOperate( // ��Ϊ����Զ��尴ť��ͻ��е�������ťģʽ ��������״̬
		// nc.ui.trade.base.IBillOperate.OP_EDIT);
		// ִ�й�ʽ��ͷ��ʽ
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
//		isAdd = true;
		super.onBoEdit();
		// ִ�й�ʽ��ͷ��ʽ
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailEditFormulas();
		// ���ÿ��Ա
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"cwhsmanagerid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// �����Ƶ���
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"coperatorid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// �����޸���
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"clastmodiid").setValue(
				ClientEnvironment.getInstance().getUser().getPrimaryKey());
		// ��������
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				"dbilldate").setValue(_getDate().toString());
		
		isAdd=true;
	}

	/**
	 * ��ģ���е�ѡ�е�VO ����ת�������۳����VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return ���۵ľۺ�VO
	 */
	private MyBillVO changeReqFydtoOutgeneral(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		int num = 0; // Ϊ�˼�������ĳ���
		TbOutgeneralHVO generalHVO = new TbOutgeneralHVO();
		// �ӱ���Ϣ���鼯��
		List<TbOutgeneralBVO> generalBList = new ArrayList<TbOutgeneralBVO>();

		SaleorderHVO salehVO = (SaleorderHVO) vos[0].getParentVO();
//		if (null != salehVO.getSrl_pk() && !"".equals(fydnew.getSrl_pk())) {
//			generalHVO.setSrl_pkr(fydnew.getSrl_pk()); // ����ֿ�
//		}
		generalHVO.setSrl_pkr("1021A91000000004YZ0P"); // ����ֿ�
		if (null != salehVO.getCbiztype()
				&& !"".equals(salehVO.getCbiztype())) {
			generalHVO.setCbiztype(salehVO.getCbiztype()); // ҵ����������
		}
		if (null != salehVO.getCdeptid() && !"".equals(salehVO.getCdeptid())) {
			generalHVO.setCdptid(salehVO.getCdeptid()); // ����
		}
		if (null != salehVO.getCemployeeid() && !"".equals(salehVO.getCemployeeid())) {
			generalHVO.setCbizid(salehVO.getCemployeeid()); // ҵ��Ա
		}
		if (null != salehVO.getCcustomerid() && !"".equals(salehVO.getCcustomerid())) {
			generalHVO.setCcustomerid(salehVO.getCcustomerid()); // �ͻ�
		}
		if (null != salehVO.getVreceiveaddress() && !"".equals(salehVO.getVreceiveaddress())) {
			generalHVO.setVdiliveraddress(salehVO.getVreceiveaddress()); // �ջ���ַ
		}
		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
			generalHVO.setVnote(salehVO.getVnote()); // ��ע
		}

		myBillVO.setParentVO(generalHVO);
		// ѭ�������е��ӱ���Ϣ
		if (null != dcSaleOrderDlg.getSaleVO() && dcSaleOrderDlg.getSaleVO().length > 0) {
			// ѭ�������е��ӱ���Ϣ
			for (int i = 0; i < dcSaleOrderDlg.getSaleVO().length; i++) {
				if (salehVO.getCsaleid().equals(
						dcSaleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
					vos[0].setChildrenVO(dcSaleOrderDlg.getSaleVO()[i]
							.getBodyVOs());
					break;
				}
			}
			SaleorderBVO[] salebVOs = (SaleorderBVO[]) vos[0].getChildrenVO();
			// ��ȡ���� ת���ӱ���������Ϣ
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
						generalBVO.setCsourcebillhid(salehVO.getCsaleid()); // ��Դ���ݱ�ͷ����
					}
					if (null != salehVO.getVreceiptcode()
							&& !"".equals(salehVO.getVreceiptcode())) {
						generalBVO.setVsourcebillcode(salehVO.getVreceiptcode()); // ��Դ���ݺ�
					}
					if (null != salehVO.getCreceipttype()
							&& !"".equals(salehVO.getCreceipttype())) {
						generalBVO.setCsourcetype(salehVO.getCreceipttype()
								.toString()); // ��Դ��������
					}
					if (null != salebvo.getCorder_bid()
							&& !"".equals(salebvo.getCorder_bid())) {
						generalBVO.setCsourcebillbid(salebvo.getCorder_bid()); // ��Դ���ݱ�������
					}
					if (null != salehVO.getCsaleid()
							&& !"".equals(salehVO.getCsaleid())) {
						generalBVO.setCfirstbillhid(salehVO.getCsaleid()); // Դͷ���ݱ�ͷ����
					}
					if (null != salebvo.getCorder_bid()
							&& !"".equals(salebvo.getCorder_bid())) {
						generalBVO.setCfirstbillbid(salebvo.getCorder_bid()); // Դͷ���ݱ�������
					}
					if (null != salebvo.getCrowno()
							&& !"".equals(salebvo.getCrowno())) {
						generalBVO.setCrowno(salebvo.getCrowno()); // �к�
					}
					if (null != salebvo.getNnumber()
							&& !"".equals(salebvo.getNnumber())) {
						generalBVO.setNshouldoutnum(salebvo.getNnumber()); // Ӧ������
						generalBVO.setNoutnum(salebvo.getNnumber());//ʵ������
					}
					if (null != salebvo.getNpacknumber()
							&& !"".equals(salebvo.getNpacknumber())) {
						generalBVO.setNshouldoutassistnum(salebvo.getNpacknumber()); // Ӧ��������
						generalBVO.setNoutassistnum(salebvo.getNpacknumber());//ʵ��������
					}
					if (null != salebvo.getCinvbasdocid()
							&& !"".equals(salebvo.getCinvbasdocid())) {
						generalBVO
								.setCinventoryid(salebvo.getCinvbasdocid()); // �������
					}
					if (null != salebvo.getBlargessflag()
							&& !"".equals(salebvo.getBlargessflag())) {
						generalBVO.setFlargess(salebvo.getBlargessflag()); // �Ƿ���Ʒ
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
			// �������������
			setAddNewOperate(isAdding(), billVO);
		}
		// ���ñ����״̬
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