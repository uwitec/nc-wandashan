package nc.ui.wds.w80060408;

import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFMessage;
import nc.itf.wds.w80060408.Iw80060408;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.wds.button.IButton;
import nc.ui.wds.w8000.CommonUnit;
import nc.vo.fts.bill.BusiDataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.msg.CommonMessageVO;
import nc.vo.pub.msg.UserNameObject;
import nc.vo.wds.w80060401.TbShipentryBVO;
import nc.vo.wds.w80060401.TbShipentryVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;

/**
 * 
 * �������
 * 
 * @author author xzs
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // �Ƿ���Ȩ�޲�����ǰ����

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;
		String isType;
		try {
			isType = CommonUnit.getUserType(ClientEnvironment.getInstance()
					.getUser().getPrimaryKey());
			if (null != isType && isType.equals("2")) {
				isControl = true;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int state = -1;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	protected void onBoSave() throws Exception {
		// TODO Auto-generated method stub
		// ��ȡ����״̬
		Object temp = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("fyd_approstate").getValueObject();
		if (null != temp && !"".equals(temp)) {
			int state = Integer.parseInt(temp.toString());
			if (this.getState() == 1 && this.getState() != state) {
				Object type = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("vbillstatus").getValueObject();
				if (Integer.parseInt(type.toString()) > 0) {
					myClientUI.showErrorMessage("�޸�ʧ��!�õ������Ƴ��˵�,����ɾ���˵�");
					return;
				}
			}

			// ���������ͨ��
			if (state == 1) {
				// ���õ���״̬
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vbillstatus").setValue(new Integer(0));
			}
			AggregatedValueObject billVO = getBillUI().getVOFromUI();
			setTSFormBufferToVO(billVO);
			AggregatedValueObject checkVO = billVO;
			setTSFormBufferToVO(checkVO);
			if(state == 2){
				CommonMessageVO cmvos =new CommonMessageVO();
				cmvos.setTitle("������Ϣ");
				cmvos.setBilltype(null);
				cmvos.setMessageContent("���Ķ����ţ�"+((TbFydnewVO)billVO.getParentVO()).getVbillno()+"û��ͨ������" + "\n" +
						"�������: "+ ((TbFydnewVO)billVO.getParentVO()).getVapprovenote());
				
				cmvos.setSendDataTime(ClientEnvironment.getServerTime());
				cmvos.setSender(ClientEnvironment.getInstance().getUser().getPrimaryKey());
				UserNameObject ou = new UserNameObject(ClientEnvironment.getInstance().getUser().getUserName());
				ou.setUserPK(((TbFydnewVO)billVO.getParentVO()).getVoperatorid());
				cmvos.setReceiver(new UserNameObject[]{ou});
				IPFMessage im = (IPFMessage) NCLocator.getInstance().lookup(
						IPFMessage.class.getName());
				im.insertCommonMsgAry(new CommonMessageVO[]{cmvos});
			}
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
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				if (getBillUI().isSaveAndCommitTogether())
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				else

					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
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
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
		}

	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			super.onBoEdit();

			Object[] values = new Object[] { ClientEnvironment.getInstance()
					.getUser().getPrimaryKey() };
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"vapproveid").setValue(values[0]);
			getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
					"dapprovedate").setValue(_getDate());
			getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
			this.setState(Integer.parseInt(getBillCardPanelWrapper()
					.getBillCardPanel().getHeadTailItem("cshenpizhuangtai")
					.getValueObject().toString()));
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		super.onBoQuery();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
	}

	@Override
	protected void onBoCancel() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCancel();
		getBillCardPanelWrapper().getBillCardPanel().execTailLoadFormulas(); // ִ�б�β��ʽ
	}
}