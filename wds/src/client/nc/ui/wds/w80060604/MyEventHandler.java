package nc.ui.wds.w80060604;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060604.Iw80060604;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.ic.pub.bill.query.QueryConditionDlgForBill;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTableSelectionEvent;
import nc.ui.pub.print.IDataSource;
import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wds.w8000.CommonUnit;
import nc.ui.wds.w8004040204.ssButtun.ISsButtun;
import nc.ui.wds.w80060604.czlbButtun.czlbBtn;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.lang.UFTime;
import nc.vo.pub.query.ConditionVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.MyBillVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wds.w80060604.SoSaleorderBVO;

/**
 * 
 * ���۶�������
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;
	private boolean isControl = false; // �Ƿ���Ȩ�޲�����ǰ����
	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	// ���ýӿ� �������ݿ�
	Iw80060604 iw = (Iw80060604) NCLocator.getInstance().lookup(
			Iw80060604.class.getName());

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

	private String sWhere;

	@Override
	protected void onBoQuery() throws Exception {
		if (isControl) {
			MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
			SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
					.getInstance().getCorporation().getPrimaryKey(),
					"80060604", ClientEnvironment.getInstance().getUser()
							.getPrimaryKey(), "80060604");

			if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
				ConditionVO[] tmpvoCons = null;
				// ��ȡ��ѯ����
				ConditionVO[] voCons = query.getConditionVO();
				String tmpwhere = null; // ��ʱ��¼��ȡ�Ĳֿ�����
				if (null != voCons && voCons.length > 0) {
					String pk = CommonUnit.getStordocName(ClientEnvironment
							.getInstance().getUser().getPrimaryKey());
					List<ConditionVO> tmplist = new ArrayList<ConditionVO>();
					for (int i = 0; i < voCons.length; i++) {
						tmpwhere = null;
						// ��ȡ��ѯ�����е�fieldcode
						String fieldCode = voCons[i].getFieldCode();
						// �����so_sale.tmpstock��ȡֵ���ݲֿ���в�ѯ�������еĶ���
						if (fieldCode.equals("so_sale.tmpstock")) {
							tmpwhere = voCons[i].getValue();
						}
						if (fieldCode.equals("so_sale.fstatus")) {
							tmpwhere = voCons[i].getValue();
							// ״̬ 0 ȫ�� ��1δ������2��������3�Ѷ���,4������
							if (tmpwhere.equals("0")) {
								voCons[i].setValue("1,2,6"); // 0��״̬��δʹ�ã�1����
																// 2���� 3����
																// 4�رգ�δʹ�ã� 5����
																// 6���� 7��������
																// 8����δͨ��
								voCons[i].setOperaCode("in");
							} else if (tmpwhere.equals("3")) {
								voCons[i].setValue("6");
							} else if (tmpwhere.equals("4")) {
								voCons[i].setValue("7");
							}
						}
						tmpwhere = null;
						tmplist.add(voCons[i]);
					}
					if (null == tmpwhere)
						tmpwhere = pk;
					tmpvoCons = new ConditionVO[tmplist.size()];
					tmpvoCons = tmplist.toArray(tmpvoCons);
					StringBuffer strWhere = new StringBuffer(query
							.getWhereSQL(tmpvoCons));

					strWhere
							.append(" and so_sale.dr = 0 "
									+ " and so_sale.ccustomerid in ( select b.pk_cumandoc "
									+ " from bd_cumandoc b, tb_storcubasdoc t"
									+ " where t.pk_cubasdoc = b.pk_cubasdoc and t.pk_stordoc = '"
									+ tmpwhere
									+ "') and so_sale.vreceiptcode not like '%��%'"
									+ " order by so_sale.ccustomerid");
					this.setSWhere(strWhere.toString());
					SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
					List<SuperVO> list = Arrays.asList(queryVos);
					if (null != list && list.size() > 0) {
						queryVos = new SuperVO[list.size()];
						queryVos = (SuperVO[]) list.toArray(queryVos);

						for (int i = 0; i < queryVos.length; i++) {
							((SoSaleVO) queryVos[i]).setPk_defdoc15(null);
							((SoSaleVO) queryVos[i]).setPk_defdoc15(pk);
						}
						getBufferData().clear();
						// �������ݵ�Buffer
						addDataToBuffer(queryVos);

						updateBuffer();
						myClientUI.getBillListPanel().getHeadItem("binitflag")
								.setEnabled(true);

					} else {
						getBufferData().clear();
						updateBuffer();
					}

				}
				if (getBufferData().getVOBufferSize() > 0)
					getButtonManager().getButton(ISsButtun.cz).setEnabled(true);
				myClientUI.updateButtons();
			}
		} else {
			getBillUI().showErrorMessage("����ʧ��,��ǰ��¼��û�н�����Ա��");
			return;
		}
	}

	/**
	 * ��ֶ���
	 * 
	 * @throws Exception
	 */
	protected void oncfdd() throws Exception {
		alertMsg("2", "��ֶ���");
	}

	/**
	 * �ϲ�����
	 * 
	 * @throws Exception
	 */
	protected void onhbdd() throws Exception {
		alertMsg("3", "�ϲ�����");
	}

	/**
	 * �ֳ�ֱ��
	 */
	protected void onfczl() throws Exception {
		alertMsg("1", "�ֳ�ֱ��");

	}

	/*
	 * ��������(non-Javadoc)
	 * 
	 * @see nc.ui.wds.w80060604.AbstractMyEventHandler#onzcdd()
	 */
	protected void onzcdd() throws Exception {
		int results = myClientUI.showOkCancelMessage("ȷ��ִ��������������?");
		if (results != 1)
			return;
		BillListPanel billList = ((BillManageUI) getBillUI())
				.getBillListPanel();
		// ��ȡ��ǰ�ı�ͷ���ݶ�������
		CircularlyAccessibleValueObject[] headVO = billList.getHeadBillModel()
				.getBodyValueVOs(SoSaleVO.class.getName());
		// �洢ѡ�к�Ķ��� ��������д
		List<SoSaleVO> tempList = new ArrayList<SoSaleVO>();
		// �ж��Ƿ�Ϊ��
		if (null != headVO && headVO.length > 0) {
			// ѭ��
			for (int j = 0; j < headVO.length; j++) {
				// ��ȡ������ͷ����
				SoSaleVO sale = (SoSaleVO) headVO[j];
				// ��ȡ�����һ������ ���������еġ��ڳ���־���ֶΣ����ڱ��Ϊ ��ͷ�еġ�ѡ�񡱸�ѡ��
				if (null != sale.getBinitflag()
						&& sale.getBinitflag().booleanValue()) {
					int count = CommonUnit.getIprintCount(sale.getCsaleid());
					if (count > 0) {
						myClientUI.showErrorMessage("����ʧ��!��ǰ�����������Ѿ������˵�");
						return;
					}
					/*
					 * // �ж����� if (null != sale.getVdef5() &&
					 * !"".equals(sale.getVdef5())) { // �����ǲ�ֶ��������Ѿ����в�ֺ�
					 * �����Ǻϲ����������Ѿ���ֵĵ��� ������ɾ�� if (sale.getVdef5().equals("2")) {
					 * if (null != sale.getVdef6() &&
					 * !"".equals(sale.getVdef6()) &&
					 * !sale.getVdef6().equals("0")) { myClientUI
					 * .showErrorMessage("����ʧ��!��ǰ�����������Ѿ������˵�"); return; } } if
					 * (sale.getVdef5().equals("3")) { if (null !=
					 * sale.getVdef6() && !"".equals(sale.getVdef6()) &&
					 * sale.getVdef6().equals("5")) { myClientUI
					 * .showErrorMessage("����ʧ��!��ǰ�����������Ѿ������˵�"); return; } }
					 * sale.setVdef5(null); // ��� sale.setVdef6(null); // ״̬
					 * sale.setIprintcount(null); // ��ӡ���� sale.setVdef7(null); //
					 * ��ӡʱ�� sale.setBinitflag(new UFBoolean(false)); // ��ѡ�񡱸�ѡ��
					 * tempList.add(sale); }
					 */
					sale.setVdef5(null); // ���
					sale.setVdef6(null); // ״̬
					sale.setIprintcount(null); // ��ӡ����
					sale.setVdef7(null); // ��ӡʱ��
					sale.setBinitflag(new UFBoolean(false)); // ��ѡ�񡱸�ѡ��
					tempList.add(sale);
				}
			}
			if (tempList.size() < 1) {
				myClientUI.showWarningMessage("����ǰ�渴ѡ����ѡ��һ����Ϣ���в���");
				return;
			}
			iw.updateSosale(tempList);
			//this.onRefresh();
		}
	}

	/**
	 * ѯ��ִ�в�������ȡ�޸ķ���
	 * 
	 * @param status
	 *            ״̬
	 * @param msg
	 *            ��ʾ��Ϣ
	 * @throws Exception
	 */
	private void alertMsg(String status, String msg) throws Exception {
		int result = myClientUI.showYesNoMessage("ȷ��ִ��" + msg + "����?");
		if (result == 4) {
			if (getchangelist(status)) {
				myClientUI.showHintMessage("�����ɹ�");
				// this.onRefresh();
			} else {
				myClientUI.showHintMessage("����ʧ��");
			}
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
		List<SuperVO> list = Arrays.asList(queryVos);
		if (null != list && list.size() > 0) {
			queryVos = new SuperVO[list.size()];
			queryVos = (SuperVO[]) list.toArray(queryVos);
			String pk = CommonUnit.getStordocName(ClientEnvironment
					.getInstance().getUser().getPrimaryKey());
			for (int i = 0; i < queryVos.length; i++) {
				((SoSaleVO) queryVos[i]).setPk_defdoc15(null);
				((SoSaleVO) queryVos[i]).setPk_defdoc15(pk);
			}
			getBufferData().clear();
			// �������ݵ�Buffer
			addDataToBuffer(queryVos);

			updateBuffer();

			myClientUI.getBillListPanel().getHeadItem("binitflag").setEnabled(
					true);
		} else {
			updateBuffer();
		}
	}

	/**
	 * ��ȡ��ǰ��ͷ�����ж��Ƿ���ѡ��ģ����ݲ����޸�״̬���л�дERP�����۶�����
	 * 
	 * @param status
	 *            ״̬ 1 �ֳ�ֱ�� 2 ��ֶ��� 3 �ϲ����� ������������
	 * @return boolean
	 * @throws Exception
	 */
	private boolean getchangelist(String status) throws Exception {

		List list = null;
		// ��ȡ����������
		int count = myClientUI.getBillListPanel().getHeadTable().getRowCount();
		if (count > 0) {
			list = new ArrayList();
			// ���״̬���ںϲ�����
			if (status.equals("3")) {
				String cusPK = ""; // �ͻ�����
				String cusAdd = ""; // ���̵�ַ
				int temp = 0;
				for (int j = 0; j < count; j++) {
					// ��ȡ�Ƿ�ѡ��
					Object o = myClientUI.getBillListPanel().getHeadBillModel()
							.getValueAt(j, "binitflag");
					if (null != o && Boolean.parseBoolean(o.toString())) {
						temp += 1;
						// ��ȡ�����б��µ�VO (ֻ�б�ͷ���ݣ�û�б�ti)
						MyBillVO mybillvo = (MyBillVO) myClientUI
								.getBillListPanel().getBillListData()
								.getBillValueVO(j, MyBillVO.class.getName(),
										SoSaleVO.class.getName(),
										SoSaleorderBVO.class.getName());
						SoSaleVO salevo = (SoSaleVO) mybillvo.getParentVO();
						// �������PK Ϊ�� ��һ�θ�ֵ
						if ("".equals(cusPK)) {
							cusPK = salevo.getCcustomerid();
							cusAdd = salevo.getVreceiveaddress();
						} else {
							// �жϿ��������Ƿ����
							if (!cusPK.equals(salevo.getCcustomerid())) {
								myClientUI.showWarningMessage("������Ϣ��һ��,���ܺϲ�����");
								return false;
							} else if (!(cusAdd + "1").equals(salevo
									.getVreceiveaddress()
									+ "1")) {
								myClientUI.showWarningMessage("���̵�ַ��һ��,���ܺϲ�����");
								return false;
							}
						}
					}
				}
				// �ж���Ҫ�ϲ����������ݸ���
				// if (temp <= 1) {
				// myClientUI.showWarningMessage("��ѡ�������������ݺϲ�����");
				// return false;
				// }
			}
			for (int i = 0; i < count; i++) {
				// ��ȡ�Ƿ�ѡ��
				Object o = myClientUI.getBillListPanel().getHeadBillModel()
						.getValueAt(i, "binitflag");

				if (null != o && Boolean.parseBoolean(o.toString())) {
					// ��ȡ�����б��µ�VO (ֻ�б�ͷ���ݣ�û�б�ti)
					MyBillVO mybillvo = (MyBillVO) myClientUI
							.getBillListPanel().getBillListData()
							.getBillValueVO(i, MyBillVO.class.getName(),
									SoSaleVO.class.getName(),
									SoSaleorderBVO.class.getName());
					SoSaleVO salevo = (SoSaleVO) mybillvo.getParentVO();
					// ���״̬Ϊ2 ��ֶ��� ����ERPռ���Զ����ֶ�Vdef6
					// ��ֶ���״̬ ����һ�β�ֶ�����0��һ�β�ֶ���
					int results = CommonUnit
							.getIprintCount(salevo.getCsaleid());
					if (results > 0) {
						myClientUI.showErrorMessage("����ʧ��!��ǰ�����������Ѿ������˵�");
						return false;
					}
					if (status.equals("2")) {
						int index = salevo.getVreceiptcode().toLowerCase()
								.indexOf("m");
						if (index > -1) {
							myClientUI.showErrorMessage("�����а�����������,�޷����в��");
							return false;
						}
						salevo.setVdef6("0");
					}
					if (status.equals("3"))
						salevo.setVdef6(null);
					// ���״̬
					salevo.setVdef5(status);
					salevo.setBinitflag(new UFBoolean(false));
					list.add(salevo);
				}
			}
			// �ж��Ƿ�������
			if (null != list && list.size() > 0) {
				// ��д���ݿ�
				ivo.updateVOList(list);
				return true;
			} else {
				myClientUI.showWarningMessage("��ѡ��һ�����ݽ��в���");
				return false;
			}
		}

		// BillListPanel panelList = ((BillManageUI)
		// getBillUI()).getBillListPanel();
		// if(panelList.getBillListData().getBillSelectValueVO(billVOName,
		// headVOName, bodyVOName))
		// System.out.println(123);
		//
		return false;

	}

	@Override
	protected void onBoPrint() throws Exception {
		int result = myClientUI.showOkCancelMessage("�Ƿ��ӡ?");
		if (result == 1) {
			// ������б���棬ʹ��ListPanelPRTS����Դ
			if (myClientUI.isListPanelSelected()) {
				// �洢����Դ�ļ���
				List<IDataSource> dsList = new ArrayList<IDataSource>();
				BillListPanel billList = ((BillManageUI) getBillUI())
						.getBillListPanel();
				// ��ȡ��ǰ�ı�ͷ���ݶ�������
				CircularlyAccessibleValueObject[] headVO = billList
						.getHeadBillModel().getBodyValueVOs(
								SoSaleVO.class.getName());
				// �洢ѡ�к�Ķ��� ��������д
				List<SoSaleVO> tempList = new ArrayList<SoSaleVO>();
				List<SoSaleorderBVO[]> tempbList = new ArrayList<SoSaleorderBVO[]>();
				List<SoSaleVO> saletempList = new ArrayList<SoSaleVO>();
				// �ж�����
				boolean isType = false;
				// �ж��Ƿ�Ϊ��
				if (null != headVO && headVO.length > 0) {
					// ѭ��
					for (int j = 0; j < headVO.length; j++) {
						// ��ȡ������ͷ����
						SoSaleVO sale = (SoSaleVO) headVO[j];
						// ��ȡ�����һ������ ���������еġ��ڳ���־���ֶΣ����ڱ��Ϊ ��ͷ�еġ�ѡ�񡱸�ѡ��
						if (null != sale.getBinitflag()
								&& sale.getBinitflag().booleanValue()) {

							// �ж�����
							if (null != sale.getVdef5()
									&& !"".equals(sale.getVdef5()))
								isType = true;
							else {
								if (null == sale.getIprintcount()
										|| "".equals(sale.getIprintcount())) {
									// ����ʱ��ͷ���ϸ�ֵ
									tempList.add(sale);
									// ����ʱ���弯�ϸ�ֵ
									tempbList
											.add((SoSaleorderBVO[]) getBufferData()
													.getVOByRowNo(j)
													.getChildrenVO());
								}
								sale.setBinitflag(new UFBoolean(false));
								saletempList.add(sale);
							}

							nc.ui.pub.print.IDataSource dataSource = new MyPrint(
									getBillUI()._getModuleCode(), billList, j,
									getBufferData(), myClientUI);

							dsList.add(dataSource);
						}
					}
				}
				if (dsList.size() <= 0) {
					myClientUI.showWarningMessage("����ǰ�渴ѡ����ѡ��һ����Ϣ���д�ӡ");
					return;
				}
				if (isType) {
					int results = myClientUI
							.showYesNoMessage("��ѡ�����Ϣ�а��������������Ƿ������ӡ?");
					if (results != 4) {
						return;
					}
				}
				// ��̨���ɷ��˵�
				insertFyd(tempList, tempbList, saletempList);
				// ��ȡ��ӡ����
				nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(
						null, null);
				// ��ʼ������ӡ
				print.beginBatchPrint();
				for (int i = 0; i < dsList.size(); i++) {
					IDataSource ds = dsList.get(i);
					print.setDataSource(ds);
				}
				print.endBatchPrint();
				print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(),
						getBillUI()._getModuleCode(), getBillUI()
								._getOperator(), getBillUI().getBusinessType(),
						getBillUI().getNodeKey());
				if (print.selectTemplate() == 1)
					print.preview();
			}
			// ����ǿ�Ƭ���棬ʹ��CardPanelPRTS����Դ
			else {
				super.onBoPrint();
			}
			// this.onRefresh();
		}
	}

	/**
	 * ���ɷ��˵� ���������������
	 * 
	 * @param tempList
	 *            ���������ļ���
	 * @param tempbList
	 *            �����������Ӽ���
	 * @param saletempList
	 *            ���ж����ļ���
	 * @throws Exception
	 */
	private void insertFyd(List<SoSaleVO> tempList,
			List<SoSaleorderBVO[]> tempbList, List<SoSaleVO> saletempList)
			throws Exception {

		List<TbFydnewVO> fydList = new ArrayList<TbFydnewVO>();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();
		if (tempList.size() > 0) {
			// /////����VOת��
			// �ѵ�ǰ�����д洢�������������ת���ɷ��˵�����ļ���/////////////////////////////////////////////
			for (int i = 0; i < tempList.size(); i++) {
				SoSaleVO salevo = tempList.get(i);
				// ------------ת����ͷ����-----------------//
				TbFydnewVO fydvo = new TbFydnewVO();

				if (null != salevo.getCsaleid()
						&& !"".equals(salevo.getCsaleid())) {
					int num = CommonUnit.getIprintCount(salevo.getCsaleid());
					if (num == 0) {
						fydvo.setCsaleid(salevo.getCsaleid()); // ������������

						if (null != salevo.getVreceiptcode()
								&& !"".equals(salevo.getVreceiptcode())) {
							fydvo.setVbillno(salevo.getVreceiptcode()); // ������
						}
						if (null != salevo.getCbiztype()
								&& !"".equals(salevo.getCbiztype())) {
							fydvo.setPk_busitype(salevo.getCbiztype()); // ҵ������
						}
						if (null != salevo.getCcustomerid()
								&& !"".equals(salevo.getCcustomerid())) {
							fydvo.setPk_kh(salevo.getCcustomerid()); // �ͻ�����
							fydvo.setSrl_pkr(CommonUnit.getAreaclName(salevo
									.getCcustomerid())); // �ջ�վ
						}
						if (null != salevo.getVreceiveaddress()
								&& !"".equals(salevo.getVreceiveaddress())) {
							fydvo.setFyd_shdz(salevo.getVreceiveaddress()); // �ջ���ַ
						}
						if (null != salevo.getCemployeeid()
								&& !"".equals(salevo.getCemployeeid())) {
							fydvo.setPk_psndoc(salevo.getCemployeeid()); // ҵ��Ա
						}
						if (null != salevo.getVnote()
								&& !"".equals(salevo.getVnote())) {
							fydvo.setFyd_bz(salevo.getVnote()); // ��ע
						}
						if (null != salevo.getCdeptid()
								&& !"".equals(salevo.getCdeptid())) {
							fydvo.setCdeptid(salevo.getCdeptid()); // ����
						}
						if (null != salevo.getDaudittime()
								&& !"".equals(salevo.getDaudittime())) {
							fydvo
									.setFyd_spsj(salevo.getDaudittime()
											.toString()); // ����ʱ��
						}
						if (null != salevo.getDapprovedate()
								&& !"".equals(salevo.getDapprovedate())) {
							fydvo.setDapprovedate(salevo.getDapprovedate()); // ��������
						}
						// if (null != salevo.getVdef18()
						// && !"".equals(salevo.getVdef18())) {
						// UFDouble gls = null;
						// try {
						// gls = new UFDouble(salevo.getVdef18());
						// } catch (NumberFormatException e) {
						// // TODO Auto-generated catch block
						// gls = null;
						// }
						// fydvo.setFyd_yslc(gls); // ������
						// }
						if (null != salevo.getDbilldate()
								&& !"".equals(salevo.getDbilldate())) {
							fydvo.setFyd_xhsj(salevo.getDbilldate()); // ���ʱ��
							// �������ת���������������еĵ��������ֶ�
						}
						// �����˻���ʽ
						fydvo.setFyd_yhfs("����");
						fydvo.setBilltype(1); // �������� 0 �����Ƶ� 1 ���۶��� 2 �ֳ�ֱ�� 3
						// ��ֶ���
						// 4 �ϲ�����
						fydvo.setVbillstatus(1); // ����״̬ �Ƶ�δ��� 1
						// �Ƶ����
						fydvo.setFyd_fyzt(0); // ����״̬ 0 ������ 1 �ѷ���
						// �Ƶ�����
						fydvo.setDmakedate(_getDate());
						fydvo.setFyd_dby(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey()); // ���õ���Ա
						fydvo.setVoperatorid(ClientEnvironment.getInstance()
								.getUser().getPrimaryKey()); // �����Ƶ���
						// ���÷���վ
						fydvo.setSrl_pk(CommonUnit
								.getStordocName(ClientEnvironment.getInstance()
										.getUser().getPrimaryKey()));
						fydvo.setIprintdate(_getDate()); // ��ӡ����
						fydvo.setIprintcount(1); // ��ӡ����
						// fydvo.setDmaketime(new UFTime()); // �Ƶ�ʱ��
						fydList.add(fydvo);
						// --------------ת����ͷ����---------------//
						// --------------ת������----------------//
						SoSaleorderBVO[] salebvo = tempbList.get(i);
						List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
						if (null != salebvo && salebvo.length > 0) {
							for (int j = 0; j < salebvo.length; j++) {
								SoSaleorderBVO saleb = salebvo[j];
								TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
								if (null != salevo.getCsaleid()
										&& !"".equals(salevo.getCsaleid())) {
									fydmxnewvo.setCsaleid(salevo.getCsaleid()); // ������������
								}
								if (null != saleb.getCorder_bid()
										&& !"".equals(saleb.getCorder_bid())) {
									fydmxnewvo.setCorder_bid(saleb
											.getCorder_bid()); // ���۸�������
								}
								if (null != saleb.getCinvbasdocid()
										&& !"".equals(saleb.getCinvbasdocid())) {
									fydmxnewvo.setPk_invbasdoc(saleb
											.getCinvbasdocid()); // ��Ʒ����
								}
								if (null != saleb.getNnumber()
										&& !"".equals(saleb.getNnumber())) {
									fydmxnewvo.setCfd_yfsl(saleb.getNnumber()); // Ӧ������
								}
								if (null != saleb.getNpacknumber()
										&& !"".equals(saleb.getNpacknumber())) {
									fydmxnewvo
											.setCfd_xs(saleb.getNpacknumber()); // ����
								}
								if (null != saleb.getCrowno()
										&& !"".equals(saleb.getCrowno())) {
									fydmxnewvo.setCrowno(saleb.getCrowno()); // �к�
								}
								if (null != saleb.getBlargessflag()
										&& !"".equals(saleb.getBlargessflag())) {
									fydmxnewvo.setBlargessflag(saleb
											.getBlargessflag()); // �Ƿ���Ʒ
								}
								if (null != saleb.getCunitid()
										&& !"".equals(saleb.getCunitid())) {
									fydmxnewvo.setCfd_dw(saleb.getCunitid()); // ��λ
								}
								tbfydmxList.add(fydmxnewvo);
							}
						}
						if (tbfydmxList.size() > 0) {
							TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList
									.size()];
							tbfydmxList.toArray(fydmxVO);
							fydmxList.add(fydmxVO);
						} else {
							fydmxList.add(null);
						}
					}

				}
				// ----------------ת���������---------------------//
			}

		}

		iw.insertFyd(fydList, fydmxList, saletempList);
	}

	@Override
	protected void onBoCard() throws Exception {
		// TODO Auto-generated method stub
		getButtonManager().getButton(
				nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz).setEnabled(false);

		super.onBoCard();

	}

	@Override
	protected void onBoReturn() throws Exception {
		// TODO Auto-generated method stub
		if (getBufferData().getVOBufferSize() > 0)
			getButtonManager().getButton(
					nc.ui.wds.w8004040204.ssButtun.ISsButtun.cz).setEnabled(
					true);
		super.onBoReturn();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		// TODO Auto-generated method stub
		super.onBoRefresh();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailLoadFormulas();
	}

	public String getSWhere() {
		return sWhere;
	}

	public void setSWhere(String where) {
		sWhere = where;
	}

}