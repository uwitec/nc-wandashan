package nc.ui.wds.w80060208;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w8000.Iw8000;
import nc.itf.wds.w80060208.Iw80060208;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.BillCardPanelWrapper;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.BusinessAction;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.businessaction.IBusinessController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.pub.CardPanelPRTS;
import nc.ui.trade.pub.ListPanelPRTS;
import nc.ui.wds.w80060208.MyClientUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;
import nc.vo.to.pub.ConstVO;
import nc.vo.wds.w80060208.MyBillVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

import nc.ui.scm.pub.query.SCMQueryConditionDlg;
import nc.vo.pub.bill.BillRendererVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/**
 * 
 * ������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	MyClientUI myClientUI = null;

	private SaleOrderDlg saleOrderDlg = null; // ��ѯ��������

	boolean isAdd = false;
	private boolean dbbool = true;

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		myClientUI = (MyClientUI) billUI;

	}

	@Override
	protected IBusinessController createBusinessAction() {
		// TODO Auto-generated method stub
		switch (getUIController().getBusinessActionType()) {
		case IBusinessActionType.PLATFORM:
			return new BusinessAction(getBillUI());
		case IBusinessActionType.BD:
			return new W80060208Action(getBillUI());
		default:
			return new BusinessAction(getBillUI());
		}
	}

	@Override
	protected void onscpz() throws Exception {
		// TODO Auto-generated method stub

		// ����������
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// ��ֵ�������
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// ��ѯ����
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// �޸Ķ���
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// �жϵ�ǰ�����Ƿ������һ�Ų�ֵ���
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {
					getBillUI().showErrorMessage("���ŵ��ݲ������һ�Ų�ֵ��ݣ���ѡ�����һ�ŵ��ݣ�");
					return;
				}
			}
		} else {
			getBillUI().showErrorMessage("û�е��ݣ�");
			return;
		}
		// �жϲ���Ƿ����
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("���ݲ���Ѿ��������벻Ҫ�ظ������");
					return;
				}
			}
		}
		// ���ݲ�ֽ���
		if (null != ob) {
			ob.setVdef6("2");
		}
		iVOPersistence.updateVO(ob);

		TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
				.getParentVO();
		tbFydnewVO.setFyd_splitstatus(2);
		iVOPersistence.updateVO(tbFydnewVO);

	}

	@Override
	protected void oncfzj() throws Exception {
		// TODO Auto-generated method stub
		// ʵ����ѯ��
		saleOrderDlg = new SaleOrderDlg(myClientUI);

		// ���÷��� ��ȡ��ѯ��ľۺ�VO
		AggregatedValueObject[] vos = saleOrderDlg.getReturnVOs(
				ClientEnvironment.getInstance().getCorporation()
						.getPrimaryKey(), ClientEnvironment.getInstance()
						.getUser().getPrimaryKey(), "0208",
				ConstVO.m_sBillDRSQ, "80060208", "8006020801", myClientUI);

		// �ж��Ƿ�Բ�ѯģ���н��в���
		if (null == vos || vos.length == 0) {
			// getBillUI().showWarningMessage("��û�н��в���!");
			return;
		}

		// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
		MyBillVO voForSave = changeReqSaleOrderYtoFyd(vos);
		// ����������� �Ͱ�ť��ʼ��
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		// �������
		getBufferData().addVOToBuffer(voForSave);
		// ��������
		updateBuffer();
		// �������Ӱ�ť״̬
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(false);
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(false);
		super.onBoEdit();
		// getBillUI().setBillOperate( // ��Ϊ����Զ��尴ť��ͻ��е�������ťģʽ ��������״̬
		// nc.ui.trade.base.IBillOperate.OP_EDIT);
		// ִ�й�ʽ��ͷ��ʽ
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		isAdd = true;
		showZeroLikeNull(false);
	}

	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos) {
		MyBillVO myBillVO = new MyBillVO();
		int num = 0; // Ϊ�˼�������ĳ���
		SaleorderHVO salehVO = null;
		TbFydnewVO fydnewVO = new TbFydnewVO();
		// �ӱ���Ϣ���鼯��
		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
		// ����ۺ�VO�ĳ��ȴ���1 ˵���Ǻϲ���������
		// if (vos.length > 1) {
		// String vercode = null; // ������
		// String saleid = null; // ���۶�������
		// // ��ȡ��һ�β�ѯ������ݻ���
		// SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
		// // ѭ���ۺ�VO�ĳ���
		// for (int i = 0; i < vos.length; i++) {
		// // �ѵ�һ��������ȡ����
		// salehVO = (SaleorderHVO) vos[i].getParentVO();
		// if (null != salehVO.getCsaleid() // �ж����������Ƿ�Ϊ��
		// && !"".equals(salehVO.getCsaleid())) {
		// if (null == saleid || "".equals(saleid)) { // �ж������Ƿ�Ϊ�գ����Ϊ��˵���ǵ�һ�θ�ֵ
		// saleid = salehVO.getCsaleid();
		// } else { // �����ֵ �����ۼӣ��м��ö��������֣�������������
		// saleid = saleid + "," + salehVO.getCsaleid();
		// }
		// }
		// // �ж϶������Ƿ�Ϊ�գ�ͬ��
		// if (null != salehVO.getVreceiptcode()
		// && !"".equals(salehVO.getVreceiptcode())) {
		// if (null == vercode || "".equals(vercode)) {
		// vercode = salehVO.getVreceiptcode();
		// } else {
		// vercode = vercode + "," + salehVO.getVreceiptcode();
		// }
		// }
		// // ��ȡ��ǰ�����е���������(�����ۼӺ������)
		// String csaleid = salehVO.getCsaleid();
		// // ѭ������
		// for (int j = 0; j < salevo.length; j++) {
		// // ��ȡ�����е���������
		// String salehid = salevo[j].getHeadVO().getCsaleid();
		// // �жϵ�ǰ�����������ͻ����е����������Ƿ���ͬ��
		// if (csaleid.equals(salehid)) {
		// // �������������ͬ�Ͱѻ����ж�Ӧ�������ӱ���Ϣ������ȡ�����ŵ� �ӱ���Ϣ������
		// salevoList.add((SaleorderBVO[]) salevo[j]
		// .getChildrenVO());
		// // ��ȡ��ǰ�ӱ���Ϣ�ĳ��� ����ж�� �����ۼ� Ϊ������������ʼ��
		// num = num + salevo[j].getChildrenVO().length;
		// break;
		// }
		// }
		// }
		// // ���ۼӺ�Ķ����ź�������ֵ����ǰ�Ķ���
		// fydnewVO.setVbillno(vercode); // ���ݺ�
		// fydnewVO.setCsaleid(saleid); // ������������
		// // �ӱ���Ϣ����
		// List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
		// // ѭ���ӱ���Ϣ���鼯�ϣ��Ѽ����е������ѭ���������ŵ��ӱ���Ϣ�����У��������ת����������
		// for (int i = 0; i < salevoList.size(); i++) {
		// SaleorderBVO[] tmp = salevoList.get(i);
		// for (int j = 0; j < tmp.length; j++) {
		// salelist.add(tmp[j]);
		// }
		// }
		// SaleorderBVO[] saleb = new SaleorderBVO[num]; //
		// ʵ��һ���ӱ���Ϣ���飬�䳤�Ⱦ���������ж���ۼӵ�
		// salelist.toArray(saleb); // ����ת��
		// // ��ת������ӱ�����ŵ���ǰ�ľۺ�VO��
		// vos[0].setChildrenVO(saleb);
		// } else { // ������Ǻϲ������Ĳ��� ����������ֵ��
		salehVO = (SaleorderHVO) vos[0].getParentVO();
		// ��ֵ��ݺ�
		StringBuffer sbsql = new StringBuffer("select h.splitvbillno from "
				+ "(select splitvbillno from tb_fydnew " + "where vbillno = '");
		if (null != salehVO.getVreceiptcode()
				&& !"".equals(salehVO.getVreceiptcode())) {
			fydnewVO.setVbillno(salehVO.getVreceiptcode()); // ���ݺ�
			fydnewVO.setFyd_ddh(salehVO.getVreceiptcode());
			sbsql.append(salehVO.getVreceiptcode());
		}
		sbsql.append("' and dr=0 order by splitvbillno desc) h where rownum=1");
		// ��ȡ�������ݿ����
		IUAPQueryBS IUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// ��ѯ���ݿ��еĽ�� �������ObjectΪ���������
		ArrayList list = null;
		try {
			list = (ArrayList) IUAPQueryBS.executeQuery(sbsql.toString(),
					new ArrayListProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// �жϽ���Ƿ�Ϊ��
		String spno = null;
		if (list != null && list.size() > 0) {
			Object a[] = (Object[]) list.get(0);
			if (a != null && a.length > 0 && a[0] != null) {
				// �����Ϊ�ս��и�ֵ
				spno = a[0].toString();
			}
		}
		// �������͵Ĳ�ֵ��ݺ�
		int numno = 0;
		// �жϻ�õĶ������Ƿ�Ϊ��
		if (spno != null && !"".equals(spno)) {
			// �����Ϊ�ս����ַ�����ȡ����ȡ��������"-"������ֵ
			String n = spno.substring(spno.lastIndexOf("-") + 2, spno.length())
					.toString();
			numno = Integer.parseInt(n.trim());
			numno += 1;
			String splitvbillno = numno + "";
			while (splitvbillno.length() < 2) {
				splitvbillno = "0" + splitvbillno;
			}
			fydnewVO.setSplitvbillno(salehVO.getVreceiptcode() + "-S"
					+ splitvbillno);
		} else {
			fydnewVO.setSplitvbillno(salehVO.getVreceiptcode() + "-S01");
		}

		if (null != salehVO.getCsaleid() && !"".equals(salehVO.getCsaleid())) {
			fydnewVO.setCsaleid(salehVO.getCsaleid()); // ��������
		}
		// ѭ�������е��ӱ���Ϣ
		for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
			if (salehVO.getCsaleid().equals(
					saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
				vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i].getBodyVOs());
				break;
			}
		}
		// }
		// ����Ϊ���ò��� ��Ϊ�ںϲ��������ģ��������Ϣ�ǲ����
		if (null != salehVO.getCcustomerid()
				&& !"".equals(salehVO.getCcustomerid())) {
			fydnewVO.setPk_kh(salehVO.getCcustomerid()); // �ͻ�����
		}
		if (null != salehVO.getVreceiveaddress()
				&& !"".equals(salehVO.getVreceiveaddress())) {
			fydnewVO.setFyd_shdz(salehVO.getVreceiveaddress()); // �ջ���ַ
		}
		if (null != salehVO.getCemployeeid()
				&& !"".equals(salehVO.getCemployeeid())) {
			fydnewVO.setPk_psndoc(salehVO.getCemployeeid()); // ҵ��Ա
		}
		if (null != salehVO.getCbiztype() && !"".equals(salehVO.getCbiztype())) {
			fydnewVO.setPk_busitype(salehVO.getCbiztype()); // ҵ������
		}
		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
			fydnewVO.setFyd_bz(salehVO.getVnote()); // ��ע
		}
		if (null != salehVO.getCdeptid() && !"".equals(salehVO.getCdeptid())) {
			fydnewVO.setCdeptid(salehVO.getCdeptid()); // ����
		}
		// ����վ
		fydnewVO.setSrl_pk("1021A91000000004YZ0P");
		if (null != salehVO.getDapprovedate()
				&& !"".equals(salehVO.getDapprovedate())) {
			fydnewVO.setDapprovedate(salehVO.getDapprovedate()); // ��������
		}
		if (null != salehVO.getAttributeValue("daudittime")) {
			fydnewVO.setFyd_spsj(salehVO.getAttributeValue("daudittime")// ����ʱ��
					.toString());
		}
		if (null != salehVO.getCsaleid() && !"".equals(salehVO.getCsaleid())) {
			fydnewVO.setCsaleid(salehVO.getCsaleid()); // ��������
		}
		if (null != salehVO.getVdef16() && !"".equals(salehVO.getVdef16())) {
			fydnewVO.setFyd_splitstatus(Integer.parseInt(salehVO.getVdef16()));// �������
		}
		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // ���õ���Ա
		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
				.getPrimaryKey()); // �����Ƶ���
		fydnewVO.setBilltype(new Integer(3));
		fydnewVO.setFyd_xhsj(salehVO.getDbilldate());// ���ʱ��
		fydnewVO.setDmakedate(new UFDate(new Date()));// ��������

		myBillVO.setParentVO(fydnewVO);

		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
			SaleorderBVO[] salebVO = (SaleorderBVO[]) vos[0].getChildrenVO();
			TbFydmxnewVO[] fydmxnewVO = new TbFydmxnewVO[salebVO.length];
			// ��ȡ���� ת���ӱ���������Ϣ
			for (int i = 0; i < salebVO.length; i++) {
				SaleorderBVO salebvo = salebVO[i];
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				if (null != salebvo.getCorder_bid()
						&& !"".equals(salebvo.getCorder_bid())) {
					fydmxnewvo.setCorder_bid(salebvo.getCorder_bid()); // ���۸�������
				}
				if (null != salebvo.getNnumber()
						&& !"".equals(salebvo.getNnumber())) {
					fydmxnewvo.setCfd_ysyfsl(salebvo.getNnumber()); // Ӧ������
					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber());
					fydmxnewvo.setCfd_sysl(new UFDouble(0));
				}
				if (null != salebvo.getNpacknumber()
						&& !"".equals(salebvo.getNpacknumber())) {
					fydmxnewvo.setCfd_ysxs(salebvo.getNpacknumber()); // ����
					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber());
					fydmxnewvo.setCfd_syfsl(new UFDouble(0));
				}
				if (null != salebvo.getCrowno()
						&& !"".equals(salebvo.getCrowno())) {
					fydmxnewvo.setCrowno(salebvo.getCrowno()); // �к�
				}
				if (null != salebvo.getBlargessflag()
						&& !"".equals(salebvo.getBlargessflag())) {
					fydmxnewvo.setBlargessflag(salebvo.getBlargessflag()); // �Ƿ���Ʒ
				}

				fydmxnewVO[i] = fydmxnewvo;

			}
			myBillVO.setChildrenVO(fydmxnewVO);
		}

		return myBillVO;
	}

	@Override
	protected void onBoSave() throws Exception {

		AggregatedValueObject mybillVO = getBillUI().getVOFromUI();
		boolean isnotnull = false;
		for (int i = 0; i < mybillVO.getChildrenVO().length; i++) {
			TbFydmxnewVO tbFydmxnewVO = ((TbFydmxnewVO[]) mybillVO
					.getChildrenVO())[i];
			if (null != tbFydmxnewVO) {
				if (null != tbFydmxnewVO.getCfd_xs()
						&& 0 != tbFydmxnewVO.getCfd_xs().doubleValue()) {
					isnotnull = true;
				}
			}
		}
		if (!isnotnull) {
			getBillUI().showErrorMessage("�����ȫ��Ϊ���ȫ��Ϊ�ղ��ܱ��棡");
			return;
		}
		// �޸İ�ť״̬
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(true);
		// ��ȡҳ��ۺ�VO
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		String[] saleid = null;
		// �������������
		saleid = ((TbFydnewVO) billVO.getParentVO()).getCsaleid().split(",");
		// //��������
		// int rowNum =
		// getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
		//		
		// //ʣ������
		// String
		// cfd_sysl=getBillCardPanelWrapper().getBillCardPanel().getBillModel()
		// //ʣ�ศ����

		// // ������鳤�ȴ���1˵���Ǻϲ������Ĳ���
		// if (saleid.length > 1) {
		// // ��ʾ
		// int result = getBillUI().showYesNoMessage("�Ƿ�����Ƶ�?");
		// // ���ѡ��Yes
		// if (result == 4) {
		// // ������Ϊ�� ���������۵�������״̬�����
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "billtype").setValue(new Integer(3));
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "vbillstatus").setValue(new Integer(1));
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "fyd_fyzt").setValue(new Integer(0));
		// // ���úϲ����淽��
		// onSplitSave();
		// }
		// } else {

		// �ж������������޸�
		if (isAdd) {
			// ��ʾ
			int result = getBillUI().showOkCancelMessage("�Ƿ񱣴沢��ӡ?");
			int iprintcount = 0;
			if (null != getBillCardPanelWrapper().getBillCardPanel()
					.getHeadTailItem("iprintcount")
					&& !"".equals(getBillCardPanelWrapper().getBillCardPanel()
							.getHeadTailItem("iprintcount").getValue())) {
				iprintcount = Integer.parseInt(getBillCardPanelWrapper()
						.getBillCardPanel().getHeadTailItem("iprintcount")
						.getValue());
			}
			iprintcount++;
			// Yes
			if (result == 1) {
				// ������Ϊ�� ���������۵�������״̬�����
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"billtype").setValue(new Integer(3));
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"vbillstatus").setValue(new Integer(1));
				getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
						"fyd_fyzt").setValue(new Integer(0));
				// getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem(
				// "iprintcount").setValue(iprintcount);
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem(
						"iprintdate", new UFDate(new Date()));
				onSave();
				onBoPrint();
			}
		} else {
			onSave();
		}

		// }

	}

	// �ϲ������ı��淽��
	private void onSplitSave() throws Exception {
		// �������Ӱ�ť״̬
		getButtonManager().getButton(nc.ui.wds.w80060204.cfButtun.ICfButtun.zj)
				.setEnabled(true);
		// ��ȡҳ��ۺ�VO
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		AggregatedValueObject checkVO = null;
		// ����TS
		setTSFormBufferToVO(billVO);
		String[] saleid = null; // ���۶�������
		String[] vercode = null; // ����������
		// ��ȡ�ۺ�VO�е�������Ϣ
		TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
		// ��ȡ�ۺ�VO���ӱ���Ϣ
		TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
		// ����һ���ӱ���Ϣ�ļ���
		List<TbFydmxnewVO> fydmxList = null;
		// ���ñ�ʶ��
		fydVO.setMergelogo(fydVO.getVbillno());
		saleid = fydVO.getCsaleid().split(","); // ���н�ȡ���۶�������
		vercode = fydVO.getVbillno().split(","); // ��ȡ����������
		// ѭ�����۶�������������
		for (int i = 0; i < saleid.length; i++) {
			// �ѽ�ȡ������۶��������ٸ�ֵ����ǰ������VO�У�����������VO�е����������Ͷ����ž͸���ֿ���
			fydVO.setCsaleid(saleid[i]);
			fydVO.setVbillno(vercode[i]); // ������ ͬ��
			// ���޸ĺ������VO��װ�ؾۺ�VO��
			billVO.setParentVO(fydVO);
			// ʵ���ӱ���Ϣ����
			fydmxList = new ArrayList<TbFydmxnewVO>();
			// ѭ���ӱ����飬��Ϊ�Ƕ���������ӱ���Ϣ��������һ������Ҫ��ֿ��������зֱ�洢
			for (int j = 0; j < fydmxVO.length; j++) {
				// �ж� ���ѭ��������������ĵ����������ӱ���Ҳ��������������������ж�������������Ƿ����
				if (saleid[i].equals(fydmxVO[j].getCsaleid())) {
					// �����ȾͰ���Ϣ��ȡ�����洢�� �ӱ���Ϣ��������ȥ
					fydmxList.add(fydmxVO[j]);
				}
			}
			// ����һ���ӱ���Ϣ�����飬���ĳ��Ⱥ���ȡ�������ӱ���Ϣ�ĳ�����һ���ģ���Ϊ����Ҫ��������ת��
			TbFydmxnewVO[] tmpVO = new TbFydmxnewVO[fydmxList.size()];
			// ����ת����ֱ�Ӵ洢���ۺ�VO��
			billVO.setChildrenVO(fydmxList.toArray(tmpVO));
			// ��Ϊû�л�ȡcheckVO ���ԾͰ�billVO ��ֵ�� checkVO
			checkVO = billVO;
			// //////////////��ȡ���淽��������������������������
			getBusinessAction()
					.save(billVO, getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
		}
		// ����������գ���ť��ʼ��
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		getBillUI().initUI();
	}

	// �ѱ��淽������������޸�getBillUI().getVOFromUI();
	private void onSave() throws Exception {
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(true);

		// �޸�ԭ�еı��淽�� ��checkVOFromUI �޸Ļ�ȡ���е� �����Ĳ���
		AggregatedValueObject billVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(billVO);
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		setTSFormBufferToVO(checkVO);
		// ����״̬
		((TbFydnewVO) billVO.getParentVO()).setVbillstatus(1);
		// ����״̬
		((TbFydnewVO) billVO.getParentVO()).setFyd_fyzt(0);
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

		ArrayList params = new ArrayList();
		// �ж��Ƿ��д�������
		try {
			if (billVO.getParentVO() == null
					&& (billVO.getChildrenVO() == null || billVO
							.getChildrenVO().length == 0)) {
				isSave = false;
			} else {
				// ��������

				String csaleid = getBillCardPanelWrapper().getBillCardPanel()
						.getHeadItem("csaleid").getValue();
				// ��ֽ�����ʣ�»����Զ�����һ�Ų�ֵ�
				String fyd_splitend = getBillCardPanelWrapper()
						.getBillCardPanel().getHeadItem("fyd_splitend")
						.getValue();
				// ��ͷVO
				TbFydnewVO tbFydnewVO = (TbFydnewVO) getBillUI().getVOFromUI()
						.getParentVO();
				// ����VO
				TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
						.getVOFromUI().getChildrenVO();
				//
				// Iw8000 iw = (Iw80060208) NCLocator.getInstance().lookup(
				// Iw80060208.class.getName());
				params.add(getBillUI().getUserObject());
				// params.add(iw);
				params.add(csaleid);
				params.add(fyd_splitend);
				params.add(tbFydnewVO);
				params.add(tbFydmxnewVO);
				// getBillUI().showErrorMessage("������ʾ1");
				// Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
				// Iw80060208.class.getName());
				// AggregatedValueObject retVo = iw.saveBD80060208(billVO,
				// params);
				if (getBillUI().isSaveAndCommitTogether())
					billVO = getBusinessAction().saveAndCommit(billVO,
							getUIController().getBillType(),
							_getDate().toString(), getBillUI().getUserObject(),
							checkVO);
				else

					// write to database
					billVO = getBusinessAction().save(billVO,
							getUIController().getBillType(),
							_getDate().toString(), params, checkVO);
				// getBillUI().showErrorMessage("������ʾ2");
			}

		} catch (Exception ex1) {
			ex1.printStackTrace();
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

		// String csaleid = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("csaleid").getValue();
		// IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
		// IUAPQueryBS.class.getName());
		// SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		// ob.setVdef6("1");
		// // �޸Ķ���
		// IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		// .getInstance().lookup(IVOPersistence.class.getName());
		// iVOPersistence.updateVO(ob);
		// // ��ֽ�����ʣ�»����Զ�����һ�Ų�ֵ�
		// String fyd_splitend = getBillCardPanelWrapper().getBillCardPanel()
		// .getHeadItem("fyd_splitend").getValue();
		// // �ж��Ƿ�ʣ�л���
		// boolean isSurplus = false;
		// if ("true".equals(fyd_splitend)) {
		// // ��ͷVO
		// TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
		// .getParentVO();
		// // ����VO
		// TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBufferData()
		// .getCurrentVO().getChildrenVO();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// isSurplus = true;
		// }
		// }
		// if (!isSurplus) {
		// tbFydnewVO.setFyd_splitstatus(2);
		// iVOPersistence.updateVO(tbFydnewVO);
		// }
		// tbFydnewVO.setFyd_pk(null);
		// tbFydnewVO.setFyd_splitstatus(2);
		// // ��ֵ��ݺ�
		// String spno = tbFydnewVO.getSplitvbillno();
		// // �жϻ�õĶ������Ƿ�Ϊ��
		// if (spno != null && !"".equals(spno)) {
		// // �����Ϊ�ս����ַ�����ȡ����ȡ��������"-"������ֵ
		// // �����ֲ���
		// String nonum = spno.substring(0, spno.lastIndexOf("-") + 2);
		// // ��������
		// String n = spno.substring(spno.lastIndexOf("-") + 2,
		// spno.length()).toString();
		// int numno = Integer.parseInt(n.trim());
		// numno += 1;
		// String splitvbillno = numno + "";
		// while (splitvbillno.length() < 2) {
		// splitvbillno = "0" + splitvbillno;
		// }
		// tbFydnewVO.setSplitvbillno(nonum + splitvbillno);
		// }
		// // ��������
		// tbFydnewVO.setDmakedate(new UFDate(new Date()));
		//
		// // �������
		// String fyd_pk = "";
		// if (isSurplus) {
		// fyd_pk = iVOPersistence.insertVO(tbFydnewVO);
		// }
		//
		// // ʣ��VO
		// ArrayList newTbFydmxnewVO = new ArrayList();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// tbFydmxnewVO[i]
		// .setCfd_ysyfsl(tbFydmxnewVO[i].getCfd_sysl());
		// tbFydmxnewVO[i].setCfd_yfsl(tbFydmxnewVO[i].getCfd_sysl());
		// tbFydmxnewVO[i].setCfd_ysxs(tbFydmxnewVO[i].getCfd_syfsl());
		// tbFydmxnewVO[i].setCfd_xs(tbFydmxnewVO[i].getCfd_syfsl());
		// tbFydmxnewVO[i].setCfd_sysl(new UFDouble(0));
		// tbFydmxnewVO[i].setCfd_syfsl(new UFDouble(0));
		// tbFydmxnewVO[i].setCfd_pk(null);
		// tbFydmxnewVO[i].setFyd_pk(fyd_pk);
		// newTbFydmxnewVO.add(tbFydmxnewVO[i]);
		// isSurplus = true;
		// }
		// }
		// if (isSurplus) {
		// iVOPersistence.insertVOList(newTbFydmxnewVO);
		// }
		//
		// ob.setVdef6("2");
		// iVOPersistence.updateVO(ob);
		// } else {
		// // ��ͷVO
		// TbFydnewVO tbFydnewVO = (TbFydnewVO) getBufferData().getCurrentVO()
		// .getParentVO();
		// // ����VO
		// TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBufferData()
		// .getCurrentVO().getChildrenVO();
		// for (int i = 0; i < tbFydmxnewVO.length; i++) {
		// double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
		// if (cfd_syfsl != 0) {
		// isSurplus = true;
		// }
		// }
		// if (!isSurplus) {
		// ob.setVdef6("2");
		// iVOPersistence.updateVO(ob);
		// tbFydnewVO.setFyd_splitstatus(2);
		// iVOPersistence.updateVO(tbFydnewVO);
		// }
		// }

	}

	@Override
	protected void onBoCancel() throws Exception {
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.cfzj).setEnabled(true);
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(true);
		// �ж��Ƿ�Ϊ������� ȡ��
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
	protected void onBoQuery() throws Exception {
		// TODO Auto-generated method stub
		// StringBuffer strWhere = new StringBuffer();
		//
		// strWhere.append(" billtype=3 and ");
		// if (askForQueryCondition(strWhere) == false)
		// return;// �û������˲�ѯ
		//
		// SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
		//
		// getBufferData().clear();
		// // �������ݵ�Buffer
		// addDataToBuffer(queryVos);
		//
		// updateBuffer();

		MyQueryTemplate myQuery = new MyQueryTemplate(myClientUI);
		SCMQueryConditionDlg query = myQuery.getQueryDlg(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(), "80060208",
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"80060208");

		if (query.showModal() == nc.ui.pub.beans.MessageDialog.ID_OK) {
			// ��ȡ��ѯ����
			ConditionVO[] voCons = query.getConditionVO();

			StringBuffer strWhere = new StringBuffer(query.getWhereSQL(voCons));

			strWhere
					.append(" and tb_fydnew.billtype=3 and dr=0 order by tb_fydnew.vbillno");
			SuperVO[] queryVos = queryHeadVOs(strWhere.toString());
			List<SuperVO> list = Arrays.asList(queryVos);

			if (null != list && list.size() > 0) {
				queryVos = new SuperVO[list.size()];
				queryVos = (SuperVO[]) list.toArray(queryVos);
				getBufferData().clear();
				// �������ݵ�Buffer
				addDataToBuffer(queryVos);

				updateBuffer();
				getBillCardPanelWrapper().getBillCardPanel()
						.execHeadTailLoadFormulas();

				// myClientUI.getBillListPanel().getHeadItem("binitflag")
				// .setEnabled(true);
			} else {
				getBufferData().clear();
				updateBuffer();
			}

		}
		showZeroLikeNull(false);

	}

	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();
		// �ж��Ƿ��Գ���
		// ����VO
		TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();
		if (null != tbFydmxnewVO && tbFydmxnewVO.length > 0) {
			// �����ӱ�
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				if (null != tbFydmxnewVO[i]) {
					// ���������
					if (null != tbFydmxnewVO[i].getCfd_sfsl()
							&& tbFydmxnewVO[i].getCfd_sfsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("�õ����Գ��ⲻ���޸ģ�");
						return;
					}
					// ��鸨����
					if (null != tbFydmxnewVO[i].getCfd_sffsl()
							&& tbFydmxnewVO[i].getCfd_sffsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("�õ����Գ��ⲻ���޸ģ�");
						return;
					}
				}
			}
		}

		// �޸İ�ť״̬
		getButtonManager().getButton(
				nc.ui.wds.w80060208.cfButtun.ICfButtun.scpz).setEnabled(false);
		// ����������
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// ��ֵ�������
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// ��ѯ����
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// �޸Ķ���
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// �жϵ�ǰ�����Ƿ������һ�Ų�ֵ���
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {
					getBillUI().showErrorMessage(
							"���ŵ��ݲ������һ�Ų�ֵ��ݣ�ʣ������ѱ���֣���ѡ�����һ�Ų�ֵ��ݣ�");
					return;
				}
			}
		}
		// �жϲ���Ƿ����
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("���ݲ���Ѿ����������ݲ������޸ģ�");
					return;
				}
			}
		}
		isAdd = false;
		super.onBoEdit();
	}

	@Override
	protected void onBoDelete() throws Exception {
		// TODO Auto-generated method stub
		super.onBoCard();

		// �ж��Ƿ��Գ���
		// ����VO
		TbFydmxnewVO[] tbFydmxnewVO = (TbFydmxnewVO[]) getBillUI()
				.getVOFromUI().getChildrenVO();
		if (null != tbFydmxnewVO && tbFydmxnewVO.length > 0) {
			// �����ӱ�
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				if (null != tbFydmxnewVO[i]) {
					// ���������
					if (null != tbFydmxnewVO[i].getCfd_sfsl()
							&& tbFydmxnewVO[i].getCfd_sfsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("�õ����Գ��ⲻ���޸ģ�");
						return;
					}
					// ��鸨����
					if (null != tbFydmxnewVO[i].getCfd_sffsl()
							&& tbFydmxnewVO[i].getCfd_sffsl().doubleValue() != 0) {
						getBillUI().showErrorMessage("�õ����Գ��ⲻ���޸ģ�");
						return;
					}
				}
			}
		}

		// ����������
		String csaleid = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("csaleid").getValue();
		// ��ֵ�������
		String fyd_pk = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("fyd_pk").getValue();
		// ��ѯ����
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		// �޸Ķ���
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		// �жϵ�ǰ�����Ƿ������һ�Ų�ֵ���
		StringBuffer sbsql = new StringBuffer(
				"select fyd_pk,fyd_splitstatus from  (select * from tb_fydnew where csaleid='");
		sbsql.append(csaleid);
		sbsql
				.append("' and dr=0 order by splitvbillno desc) where rownum<2 and dr=0 ");
		ArrayList tbfydvos = (ArrayList) query.executeQuery(sbsql.toString(),
				new ArrayListProcessor());
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (!fyd_pk.equals(tbfydvo[0].toString())) {

					getBillUI().showErrorMessage(
							"���ŵ��ݲ������һ�Ų�ֵ��ݣ�ʣ������ѱ���֣���ѡ�����һ�Ų�ֵ��ݣ�");
					return;
				}
			}
		}
		// �жϲ���Ƿ����
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if (null != ob) {
			if (null != ob.getVdef6() && !"".equals(ob.getVdef6())) {
				if ("2".equals(ob.getVdef6())) {
					getBillUI().showErrorMessage("���ݲ���Ѿ����������ݲ�����ɾ����");
					return;
				}
			}
		}

		// // ɾ����һ�β�ֵĵ���
		// if (null != tbfydvos && tbfydvos.size() > 0) {
		// Object[] tbfydvo = (Object[]) tbfydvos.get(0);
		// if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
		// if (Integer.parseInt(tbfydvo[1].toString()) == 0) {
		// ob.setVdef6("0");
		// iVOPersistence.updateVO(ob);
		// }
		// }
		// }
		// ��������

		ArrayList params = new ArrayList();
		// Iw8000 iw = (Iw80060208) NCLocator.getInstance().lookup(
		// Iw80060208.class.getName());
		params.add(getBillUI().getUserObject());
		// params.add(iw);
		params.add(tbfydvos);
		params.add(ob);
		// ����û�����ݻ��������ݵ���û��ѡ���κ���
		if (getBufferData().getCurrentVO() == null)
			return;

		if (MessageDialog.showOkCancelDlg(getBillUI(),
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000064")/* @res "����ɾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory",
						"UPPuifactory-000065")/* @res "�Ƿ�ȷ��ɾ���û�������?" */
				, MessageDialog.ID_CANCEL) != UIDialog.ID_OK)
			return;

		AggregatedValueObject modelVo = getBufferData().getCurrentVO();
		try {
			getBusinessAction().delete(modelVo,
					getUIController().getBillType(),
					getBillUI()._getDate().toString(), params);

			// Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
			// Iw80060208.class.getName());
			// AggregatedValueObject retVo = iw.deleteBD80060208(modelVo,
			// params);
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		if (PfUtilClient.isSuccess()) {
			// �����������
			getBillUI().removeListHeadData(getBufferData().getCurrentRow());
			if (getUIController() instanceof ISingleController) {
				ISingleController sctl = (ISingleController) getUIController();
				if (!sctl.isSingleDetail())
					getBufferData().removeCurrentRow();
			} else {
				getBufferData().removeCurrentRow();
			}

		}
		if (getBufferData().getVOBufferSize() == 0)
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		else
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		getBufferData().setCurrentRow(getBufferData().getCurrentRow());

		//
		super.onBoReturn();
	}

	public void showZeroLikeNull(boolean bShowZeroLikeNull) {
		BillRendererVO vo = getBillCardPanelWrapper().getBillCardPanel()
				.getBodyPanel().getRendererVO();
		vo.setShowZeroLikeNull(bShowZeroLikeNull);
		getBillCardPanelWrapper().getBillCardPanel().getBodyPanel()
				.setShowFlags(vo);
	}

	@Override
	protected void onBoPrint() throws Exception {
		// TODO Auto-generated method stub

		TbFydnewVO tbFydnewVO = new TbFydnewVO();
		// ��ӡ����
		TbFydmxnewVO[] tbFydmxnewVO = null;
		if (getBillManageUI().isListPanelSelected()) {
			tbFydmxnewVO = (TbFydmxnewVO[]) getBillManageUI()
					.getBillListWrapper().getVOFromUI().getChildrenVO();
			tbFydnewVO = (TbFydnewVO) getBillManageUI().getBillListWrapper()
					.getVOFromUI().getParentVO();
		} else {
			tbFydmxnewVO = (TbFydmxnewVO[]) getBillCardPanelWrapper()
					.getBillVOFromUI().getChildrenVO();
			tbFydnewVO = (TbFydnewVO) getBillCardPanelWrapper()
					.getBillVOFromUI().getParentVO();
		}
		// ��ӡ����
		int iprintcount = 0;
		if (null != tbFydnewVO.getIprintcount()) {
			iprintcount = tbFydnewVO.getIprintcount();
		}
		iprintcount++;
		tbFydnewVO.setIprintcount(iprintcount);
		// �����Ĵ�ӡ����
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, tbFydnewVO
				.getCsaleid());
		// getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
		// "csaleid").getValue()
		int saleIprintcount = 0;
		if (null != ob.getIprintcount()) {
			saleIprintcount = ob.getIprintcount();
		} else {
			saleIprintcount = 0;
		}
		saleIprintcount++;
		ob.setIprintcount(saleIprintcount);
		Iw80060208 iw = (Iw80060208) NCLocator.getInstance().lookup(
				Iw80060208.class.getName());
		AggregatedValueObject retVo = iw.prinDB80060208(tbFydnewVO, ob);

		// ʵ�ʴ�ӡ�ļ���
		ArrayList tbFydmxnewVOs = new ArrayList();
		for (int i = 0; i < tbFydmxnewVO.length; i++) {
			double cfd_xs = 0;
			if (null != tbFydmxnewVO[i].getCfd_xs()) {
				cfd_xs = tbFydmxnewVO[i].getCfd_xs().doubleValue();
			}
			if (0 != cfd_xs) {
				tbFydmxnewVOs.add(tbFydmxnewVO[i]);
			}
		}
		// ʵ�ʴ�ӡ��VO
		TbFydmxnewVO[] tbFydmxnewPrintVO = new TbFydmxnewVO[tbFydmxnewVOs
				.size()];
		for (int i = 0; i < tbFydmxnewPrintVO.length; i++) {
			if (null != tbFydmxnewVOs.get(i)) {
				tbFydmxnewPrintVO[i] = (TbFydmxnewVO) tbFydmxnewVOs.get(i);
			}
		}
		// Ҫ��ӡ������Դ����
		getBufferData().getCurrentVO().setChildrenVO(tbFydmxnewPrintVO);
		int nCurrentRow = getBufferData().getCurrentRow();
		updateBuffer();

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		// ��ӡ����
		super.onBoPrint();
		// �ָ�������ʾ����
		getBufferData().getCurrentVO().setChildrenVO(tbFydmxnewVO);
		updateBuffer();

		if (nCurrentRow >= 0) {
			getBufferData().setCurrentRow(nCurrentRow);
		}
		super.onBoRefresh();
	}

	// �б�UI
	private BillManageUI getBillManageUI() {
		return (BillManageUI) getBillUI();
	}

}