package nc.ui.ncblk.yjcb.yjcbjs;

import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.ncblk.yjcb.IYjcbSrv;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.ConnectionFactory;
import nc.ui.ncblk.yjcb.yjcbjs.dialog.InputJEDialog;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.bd.CorpVO;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

/**
 * 
 *������AbstractMyEventHandler�������ʵ���࣬ ��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
 * 
 * @author author
 *@version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().dataNotNullValidate();
		// �жϣ���������ݡ��ڼ������ظ�.
		// ���
		String djnd = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"djnd").getValue();
		// �ڼ�
		String djqj = getBillCardPanelWrapper().getBillCardPanel().getHeadItem(
				"djqj").getValue();
		// PKֵ
		String pk_yjcb = getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("pk_yjcb").getValue();
		// ��������޸�����
		if (pk_yjcb == null || pk_yjcb.equals("")) {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			Collection coll = iUAPQueryBS.retrieveByClause(
					NcReportYjcbVO.class, " djnd='" + djnd + "' and djqj='"
							+ djqj + "' and dr=0 ");
			if (coll != null && coll.size() > 0) {
				getBillUI().showErrorMessage(
						"��ǰ���" + djnd + ",�ڼ�" + djqj + "�������Ѿ����ڣ�����ʧ�ܣ�");
				return;
			}
		}
		super.onBoSave();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null) {
			getBillUI().showErrorMessage("��ѡ����Ҫ���������ݣ�");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		// �������ȷ��
		if (intBtn == IPrivateButton.SYSTEM_BUTON_QR) {
			// �Ƿ��Ѿ�ȷ��
			if (pvo.getIsstatus().booleanValue() == true) {
				getBillUI().showErrorMessage("��ǰ�����Ѿ�ȷ�ϣ�ȷ��ʧ�ܣ�");
				return;
			}
			// ���жϵ�ǰ�����Ƿ��Ѿ�����
			// ���û�м��㣬�򷵻�
			if (pvo.getDef4().booleanValue() == false) {
				getBillUI().showErrorMessage("��ǰ�·ݻ�û�н������½�ɱ���ȷ��ʧ�ܣ�");
				return;
			}
			// ����Ѿ�����
			int result = getBillUI().showOkCancelMessage(
					"��ȷ��Ҫȷ�ϴ˴μ�������ȷ�Ϻ�ǰ�·ݵ����ݲ�����ɾ�����ؼ���!");
			// ���ѡ������
			if (result == 1) {
				// ����ȷ�ϲ���
				pvo.setIsstatus(new UFBoolean(true));
				// ���и��º�̨����
				IVOPersistence voPer = (IVOPersistence) NCLocator.getInstance()
						.lookup(IVOPersistence.class.getName());
				voPer.updateVO(pvo);
				
				// added by zjj 2010-05-19
				IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
						IYjcbSrv.class.getName());
				jsSrv.yjcbQr(pvo);
				getBillUI().showWarningMessage("�ɹ�ȷ��");
				// added end
				onBoRefresh();
				getBillUI().updateUI();
			
			}
		}
		// ������˼���
		if (intBtn == IPrivateButton.SYSTEM_BUTON_JS) {
			// �û�VO
			UserVO userVo = getBillUI().getEnvironment().getUser();
			// ������˾VO
			CorpVO corpVo = getBillUI().getEnvironment().getCorporation();
			// ��������
			UFDate ufdate = getBillUI().getEnvironment().getDate();
			// �Ƿ��Ѿ�ȷ��
			if (pvo.getIsstatus().booleanValue() == true) {
				// �Ѿ�ȷ�Ϻ�����ݲ����ٴμ���
				getBillUI().showErrorMessage("��ǰ�����Ѿ�ȷ�ϣ������ٴμ��㣡");
				return;
			}
			// �������
			int sj = getBillUI().showOkCancelMessage("��ȷ��Ҫ�Ե�ǰ�·ݵ����ݽ��м�����");
			if (sj != 1)
				return;
			IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
					IYjcbSrv.class.getName());
			boolean sfyjjs = pvo.getDef4().booleanValue();
			// ����Ѿ�����
			if (sfyjjs == true) {
				// ��ʾ�Ƿ���Ҫ����
				int sffg = getBillUI().showOkCancelMessage(
						"��ǰ�����Ѿ�����ɹ�������Ҫ�����ϴεļ�����");
				// ��Ҫ����
				if (sffg == 1) {
					// ���Ǽ���
					// getBillUI().showWarningMessage("��ʼ���Ǽ���......");
					jsSrv.yjcbjsSrv(userVo, corpVo, ufdate, pvo, false);
					// �������
					onBoRefresh();
					getBillUI().showWarningMessage("����ɹ�!");
				} else
					return;
			}
			// ��û�м���
			else {
				// ֱ�Ӽ���
				// getBillUI().showWarningMessage("��ʼֱ�Ӽ���......");
				jsSrv.yjcbjsSrv(userVo, corpVo, ufdate, pvo, true);
				// �������
				onBoRefresh();
				getBillUI().showWarningMessage("����ɹ�!");
			}
		}
		/*
		 * 
		 * ׿�����޸�
		 * 2010-05-13
		 * 
		 */
		//�޸����� �����°�ť�¼�
		if(intBtn == IPrivateButton.SYSTEM_BUTTON_TZ)
		{  
			if (pvo.getIsstatus().booleanValue() == true) {
				// �Ѿ�ȷ�Ϻ�����ݲ����ٴμ���
				getBillUI().showErrorMessage("��ǰ�����Ѿ�ȷ�ϣ������ٴμ��㣡");
				return;
			}
			if(pvo.getDef4().booleanValue())
			{
			InputJEDialog input = new InputJEDialog();
			
			if(input.str != null && input.isTz == true)
			{		
			IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(
					IYjcbSrv.class.getName());
			jsSrv.yjcbTz(Double.parseDouble(input.str), pvo);
			onBoRefresh();
			getBillUI().showWarningMessage("�����ɹ�!");
			}
			//String t = JOptionPane.showInputDialog(input, "��������", "����",JOptionPane.OK_CANCEL_OPTION);
		   // System.out.println(t);
			}
			else
			{
				getBillUI().showWarningMessage("�ü�¼��δ���㣬���ȼ��㣡");
			}
		}
       
		super.onBoElse(intBtn);

	}

	@Override
	protected void onBoDelete() throws Exception {
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null ) {
			getBillUI().showErrorMessage("��ѡ����Ҫ���������ݣ�");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		IUAPQueryBS  query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		List<NcReportYjcbVO> list1 = (List<NcReportYjcbVO>)query.retrieveByClause(NcReportYjcbVO.class," djnd > '" + pvo.getDjnd() +
		  "' or (djnd ='" + pvo.getDjnd() + "' and djqj > '" + pvo.getDjqj() + "')"  );
		if(list1.size() == 0)
		{    
			//System.out.println(getBillUI().showOkCancelMessage("�Ƿ�ɾ���ü�¼��") == 2);
			 if(getBillUI().showOkCancelMessage("�Ƿ�ɾ���ü�¼��") == 1)
			 {
		     IVOPersistence delete = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			 IYjcbSrv jsSrv = (IYjcbSrv) NCLocator.getInstance().lookup(IYjcbSrv.class.getName());
			 delete.deleteByClause(NcReportYjcbBVO.class, "pk_yjcb = '" + pvo.getPk_yjcb() +"'");
			 delete.deleteByClause(NcReportYjcbVO.class, "pk_yjcb = '" + pvo.getPk_yjcb() + "'");
			 jsSrv.yjcbSynDel(pvo.getDjnd(), pvo.getDjqj());
			 this.onBoRefresh();
			 }
		}
		else
		{
			this.getBillUI().showErrorMessage("������һ���¼�¼��Ҫ������������ɾ����һ���µļ�¼��");
		}
//		if (pvo.getIsstatus().booleanValue() == true) {
//			throw new Exception("��ǰ�����Ѿ�ȷ�ϣ����ܱ�ɾ����");
//		} 
		
		
//		else  {
//			super.onBoDelete();
//			}
		/*
		 * 
		 *  added by zjj 
		 *  2010-05-18
		 *  
		 */
//		else  
//		{    
//			super.onBoDelete();
//			try
//			{  
//			  if(query == null)
//				  query = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//			  IVOPersistence delete = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
//			  List<NcReportYjcbVO> list = (List<NcReportYjcbVO>) query.retrieveByClause(NcReportYjcbVO.class," djnd > '" + pvo.getDjnd() 
//					  + "' or (djnd ='" + pvo.getDjnd() + "' and djqj > '" + pvo.getDjqj() + "')"  );
//			  for(int i = 0; i < list.size(); i++)
//			  {   
//				  NcReportYjcbVO tem = list.get(i);
//				  delete.deleteByPK(NcReportYjcbVO.class, tem.getPk_yjcb());
//				  //delete.deleteByClause(NcReportYjcbBVO.class, "pk_yjcb = '" + tem.getPk_yjcb() +"'");
//			     
//			  }
//			  onBoRefresh();
//			}
//			catch(BusinessException be)
//			{
//				be.printStackTrace();
//			}
//			catch(Exception e)
//			{  
//				e.printStackTrace();
//				System.out.println("IVOPersistence cannot find!");
//			}
//		}
		/*
		 * 
		 *   added end
		 * 
		 */
	}

	@Override
	protected void onBoEdit() throws Exception {
		BillUIBuffer billUIBuffer = getBillUI().getBufferData();
		MyBillVO myBillVo = (MyBillVO) billUIBuffer.getCurrentVO();
		if (myBillVo == null) {
			getBillUI().showErrorMessage("��ѡ����Ҫ���������ݣ�");
			return;
		}
		NcReportYjcbVO pvo = (NcReportYjcbVO) myBillVo.getParentVO();
		if (pvo.getIsstatus().booleanValue() == true) {
			throw new Exception("��ǰ�����Ѿ�ȷ�ϣ����ܱ��޸ģ�");
		} else
			super.onBoEdit();
	}

}