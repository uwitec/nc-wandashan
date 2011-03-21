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

		private FydnewDlg fydnewdlg = null; // ��ѯ��������

		boolean isAdd = false;

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	@Override
	public void onBoAdd(ButtonObject bo) throws Exception {
		// TODO Auto-generated method stub
		isAdd = true;
		// ʵ����ѯ��
		fydnewdlg = new FydnewDlg(myClientUI);
		// ���÷��� ��ȡ��ѯ��ľۺ�VO
		AggregatedValueObject[] vos = fydnewdlg.getReturnVOs(ClientEnvironment
				.getInstance().getCorporation().getPrimaryKey(),
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				"4202", ConstVO.m_sBillDRSQ, "8004040204", "8004040294",
				myClientUI);
		// �ж��Ƿ�Բ�ѯģ���н��в���
		if (null == vos || vos.length == 0) {
			getBillUI().showWarningMessage("��û�н��в���!");
			return;
		}
		// ����ת���� ��ģ���л�ȡ�Ķ���ת�����Լ��ĵ�ǰ��ʾ�Ķ��󣬵��÷���
		//MyBillVO voForSave = changeReqSaleOrderYtoFyd(vos);
		// ����������� �Ͱ�ť��ʼ��
		getBufferData().clear();
		getBillUI().setBillOperate(IBillOperate.OP_INIT);
		// �������
		getBufferData().addVOToBuffer(vos[0]);
		// ��������
		updateBuffer();
		super.onBoEdit();
		// ִ�й�ʽ��ͷ��ʽ
		getBillCardPanelWrapper().getBillCardPanel().execHeadEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadTailEditFormulas();
		// �����Ƶ���
		getBillCardPanelWrapper().getBillCardPanel().getHeadTailItem("fyd_zdr")
				.setValue(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
		// ���õ���Ա
		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fyd_dby")
				.setValue(
						ClientEnvironment.getInstance().getUser()
								.getPrimaryKey());
		// �����Ƶ�ʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		getBillCardPanelWrapper().getBillCardPanel()
				.getHeadTailItem("fyd_zdsj").setValue(
						dateFormat.format(new Date()));
	}
	
	/**
	 * ��ģ���е�ѡ�е�VO ����ת�������۳����VO
	 * 
	 * @param vos
	 *            ҳ��ѡ�еľۺ�VO
	 * @return ���۵ľۺ�VO
	 */
//	private MyBillVO changeReqSaleOrderYtoFyd(AggregatedValueObject[] vos) {
//		MyBillVO myBillVO = new MyBillVO();
//		int num = 0; // Ϊ�˼�������ĳ���
//		SaleorderHVO salehVO = null;
//		TbFydnewVO fydnewVO = new TbFydnewVO();
//		// �ӱ���Ϣ���鼯��
//		List<SaleorderBVO[]> salevoList = new ArrayList<SaleorderBVO[]>();
//		// ����ۺ�VO�ĳ��ȴ���1 ˵���Ǻϲ���������
//		if (vos.length > 1) {
//			String vercode = null; // ������
//			String saleid = null; // ���۶�������
//			// ��ȡ��һ�β�ѯ������ݻ���
//			SaleOrderVO[] salevo = saleOrderDlg.getSaleVO();
//			// ѭ���ۺ�VO�ĳ���
//			for (int i = 0; i < vos.length; i++) {
//				// �ѵ�һ��������ȡ����
//				salehVO = (SaleorderHVO) vos[i].getParentVO();
//				if (null != salehVO.getCsaleid() // �ж����������Ƿ�Ϊ��
//						&& !"".equals(salehVO.getCsaleid())) {
//					if (null == saleid || "".equals(saleid)) { // �ж������Ƿ�Ϊ�գ����Ϊ��˵���ǵ�һ�θ�ֵ
//						saleid = salehVO.getCsaleid();
//					} else { // �����ֵ �����ۼӣ��м��ö��������֣�������������
//						saleid = saleid + "," + salehVO.getCsaleid();
//					}
//				}
//				// �ж϶������Ƿ�Ϊ�գ�ͬ��
//				if (null != salehVO.getVreceiptcode()
//						&& !"".equals(salehVO.getVreceiptcode())) {
//					if (null == vercode || "".equals(vercode)) {
//						vercode = salehVO.getVreceiptcode();
//					} else {
//						vercode = vercode + "," + salehVO.getVreceiptcode();
//					}
//				}
//				// ��ȡ��ǰ�����е���������(�����ۼӺ������)
//				String csaleid = salehVO.getCsaleid();
//				// ѭ������
//				for (int j = 0; j < salevo.length; j++) {
//					// ��ȡ�����е���������
//					String salehid = salevo[j].getHeadVO().getCsaleid();
//					// �жϵ�ǰ�����������ͻ����е����������Ƿ���ͬ��
//					if (csaleid.equals(salehid)) {
//						// �������������ͬ�Ͱѻ����ж�Ӧ�������ӱ���Ϣ������ȡ�����ŵ� �ӱ���Ϣ������
//						salevoList.add((SaleorderBVO[]) salevo[j]
//								.getChildrenVO());
//						// ��ȡ��ǰ�ӱ���Ϣ�ĳ��� ����ж�� �����ۼ� Ϊ������������ʼ��
//						num = num + salevo[j].getChildrenVO().length;
//						break;
//					}
//				}
//			}
//			// ���ۼӺ�Ķ����ź�������ֵ����ǰ�Ķ���
//			fydnewVO.setVbillno(vercode); // ���ݺ�
//			fydnewVO.setCsaleid(saleid); // ������������
//			// �ӱ���Ϣ����
//			List<SaleorderBVO> salelist = new ArrayList<SaleorderBVO>();
//			// ѭ���ӱ���Ϣ���鼯�ϣ��Ѽ����е������ѭ���������ŵ��ӱ���Ϣ�����У��������ת����������
//			for (int i = 0; i < salevoList.size(); i++) {
//				SaleorderBVO[] tmp = salevoList.get(i);
//				for (int j = 0; j < tmp.length; j++) {
//					salelist.add(tmp[j]);
//				}
//			}
//			SaleorderBVO[] saleb = new SaleorderBVO[num]; // ʵ��һ���ӱ���Ϣ���飬�䳤�Ⱦ���������ж���ۼӵ�
//			salelist.toArray(saleb); // ����ת��
//			// ��ת������ӱ�����ŵ���ǰ�ľۺ�VO��
//			vos[0].setChildrenVO(saleb);
//		} else { // ������Ǻϲ������Ĳ��� ����������ֵ��
//			salehVO = (SaleorderHVO) vos[0].getParentVO();
//			if (null != salehVO.getVreceiptcode()
//					&& !"".equals(salehVO.getVreceiptcode())) {
//				fydnewVO.setVbillno(salehVO.getVreceiptcode()); // ���ݺ�
//			}
//			if (null != salehVO.getCsaleid()
//					&& !"".equals(salehVO.getCsaleid())) {
//				fydnewVO.setCsaleid(salehVO.getCsaleid()); // ����
//			}
//			// ѭ�������е��ӱ���Ϣ
//			for (int i = 0; i < saleOrderDlg.getSaleVO().length; i++) {
//				if (salehVO.getCsaleid().equals(
//						saleOrderDlg.getSaleVO()[i].getHeadVO().getCsaleid())) {
//					vos[0].setChildrenVO(saleOrderDlg.getSaleVO()[i]
//							.getBodyVOs());
//					break;
//				}
//			}
//		}
//		// ����Ϊ���ò��� ��Ϊ�ںϲ��������ģ��������Ϣ�ǲ����
//		if (null != salehVO.getCcustomerid()
//				&& !"".equals(salehVO.getCcustomerid())) {
//			fydnewVO.setPk_kh(salehVO.getCcustomerid()); // �ͻ�����
//		}
//		if (null != salehVO.getVreceiveaddress()
//				&& !"".equals(salehVO.getVreceiveaddress())) {
//			fydnewVO.setFyd_shdz(salehVO.getVreceiveaddress()); // �ջ���ַ
//		}
//		if (null != salehVO.getCemployeeid()
//				&& !"".equals(salehVO.getCemployeeid())) {
//			fydnewVO.setPk_psndoc(salehVO.getCemployeeid()); // ҵ��Ա
//		}
//		if (null != salehVO.getCbiztype() && !"".equals(salehVO.getCbiztype())) {
//			fydnewVO.setPk_busitype(salehVO.getCbiztype()); // ҵ������
//		}
//		if (null != salehVO.getVnote() && !"".equals(salehVO.getVnote())) {
//			fydnewVO.setFyd_bz(salehVO.getVnote()); // ��ע
//		}
//		if (null != salehVO.getDapprovedate()
//				&& !"".equals(salehVO.getDapprovedate())) {
//			fydnewVO.setDapprovedate(salehVO.getDapprovedate()); // ����ʱ��
//		}
//		fydnewVO.setFyd_dby(ClientEnvironment.getInstance().getUser()
//				.getPrimaryKey()); // ���õ���Ա
//		fydnewVO.setVoperatorid(ClientEnvironment.getInstance().getUser()
//				.getPrimaryKey()); // �����Ƶ���
//		fydnewVO.setBilltype(new Integer(1));
//		myBillVO.setParentVO(fydnewVO);
//
//		if (null != vos[0].getChildrenVO() && vos[0].getChildrenVO().length > 0) {
//			SaleorderBVO[] salebVO = (SaleorderBVO[]) vos[0].getChildrenVO();
//			TbFydmxnewVO[] fydmxnewVO = new TbFydmxnewVO[salebVO.length];
//			// ��ȡ���� ת���ӱ���������Ϣ
//			for (int i = 0; i < salebVO.length; i++) {
//				SaleorderBVO salebvo = salebVO[i];
//				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
//				if (null != salebvo.getCorder_bid()
//						&& !"".equals(salebvo.getCorder_bid())) {
//					fydmxnewvo.setCorder_bid(salebvo.getCorder_bid()); // ���۸�������
//				}
//				if (null != salebvo.getNnumber()
//						&& !"".equals(salebvo.getNnumber())) {
//					fydmxnewvo.setCfd_yfsl(salebvo.getNnumber()); // Ӧ������
//				}
//				if (null != salebvo.getNpacknumber()
//						&& !"".equals(salebvo.getNpacknumber())) {
//					fydmxnewvo.setCfd_xs(salebvo.getNpacknumber()); // ����
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