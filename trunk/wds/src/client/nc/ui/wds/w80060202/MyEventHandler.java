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
 * ���˼ƻ�����
 * 
 * @author author
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
			int result = myClientUI.showOkCancelMessage("ȷ��ɾ���õ���?");
			if (result == 1) {
				AggregatedValueObject item = getBillUI().getBufferData()
						.getCurrentVO();
				TbFydnewVO fydvo = (TbFydnewVO) item.getParentVO();
				if (null != fydvo.getFyd_fyzt() && fydvo.getFyd_fyzt() > 0) {
					myClientUI.showErrorMessage("ɾ��ʧ��!�õ�����ȷ��,��ȡ��ȷ��");
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
					myClientUI.showErrorMessage("ɾ��ʧ�ܣ��õ��������ɳ��ⵥ,����ɾ�����ⵥ");
					return;
				}
				// ���ýӿ�ɾ������
				Iw80060202 iw = (Iw80060202) NCLocator.getInstance().lookup(
						Iw80060202.class.getName());
				iw.deleteFydVO(item);
				// �����������
				getBufferData().removeCurrentRow();
				if (getBufferData().getVOBufferSize() == 0)
					getBillUI().setBillOperate(IBillOperate.OP_INIT);
				else
					getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);

				updateBuffer();
			}
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub
		if (isControl) {
			int result = getBillUI().showYesNoMessage("�Ƿ��Ƶ�����ӡ?");

			if (result == 4) {
				// �жϵ�ǰ�Ƿ���ѡ������
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
							// ͨ��������ȡ��ӡ����
							Integer count = fydvo.getIprintcount();
							if (null != fydVO.getIprintcount() && null != count) {
								// �ѵ�ǰ�����еĴ�ӡ���������ݿ�������бȽϣ����������������ʾ
								if (fydVO.getIprintcount().intValue() != count
										.intValue()) {
									myClientUI
											.showErrorMessage("����ʧ��,�õ����ѱ�������ˢ��");
									return;
								}
							}
							if (null != count && count > 0) {
								fydvo.setIprintcount(count + 1);
								ivo.updateVO(fydvo);
							} else {
								fydVO.setFyd_fyzt(new Integer(0)); // ���÷���״̬
								fydVO.setIprintcount(new Integer(1)); // ���ô�ӡ����
								fydVO.setFyd_zdr(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey()); // ���÷��˼ƻ��Ƶ���
								fydVO.setFyd_dby(ClientEnvironment
										.getInstance().getUser()
										.getPrimaryKey()); // ���õ���Ա
								fydVO.setFyd_zdsj(_getDate()); // �����Ƶ�����
								fydVO.setFyd_yhfs("�ƻ� ת��"); // ���˷�ʽ
								fydVO.setIprintdate(_getDate());// ��ӡ����
								fydVO.setVbillstatus(new Integer(1)); // ����״̬
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
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	/***************************************************************************
	 * ���ݵ�ǰ�Ĳ�ѯ�����ٽ��в�ѯ����
	 * 
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private void onRefresh() throws ClassNotFoundException, Exception {
		SuperVO[] queryVos = queryHeadVOs(this.getSWhere());
		getBufferData().clear();
		// �������ݵ�Buffer
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
			// ��ȡ��ѯ����
			ConditionVO[] voCons = query.getConditionVO();
			StringBuffer strWhere = new StringBuffer(query.getWhereSQL(voCons));
			strWhere
					.append(" and tb_fydnew.dr = 0 and tb_fydnew.fyd_approstate = '1' and tb_fydnew.billtype = 0 ");
			this.setSWhere(strWhere.toString());
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
			getBufferData().clear();
			// �������ݵ�Buffer
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