package nc.ui.hg.pu.plan.year;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFileChooser;

import nc.ui.hg.pu.pub.HgExcelFileFilter;
import nc.ui.hg.pu.pub.HgUpLoadCtrl;
import nc.ui.hg.pu.pub.PlanPubClientUI;
import nc.ui.hg.pu.pub.PlanPubEventHandler;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UITextField;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.hg.pu.plan.year.PlanYearBVO;
import nc.vo.hg.pu.pub.HgPuBtnConst;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.PlanApplyInforVO;
import nc.vo.pfxx.pub.Filter;
import nc.vo.pp.ask.UpLoadFileVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;

public class ClientEventHandler extends PlanPubEventHandler {
	private UITextField ivjtfDirectory = null;
	public HgUpLoadCtrl m_HgUploadCtrl;
	/** �ļ�ѡȡ�� */
	private UIFileChooser m_filechooser = null;
	private int state = 0;
	/** ��ǰѡ���·�� */
	private String m_currentPath = null;
	// ��ǰ�ļ�
	private File m_fFilePath = null;
	/** ��ǰĿ¼��XLS�ļ����� */
	private File[] m_allcurrfiles = null;

	private Hashtable m_ht = null;

	private Vector excelTOBill = null;

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected UIDialog createQueryUI() {
		return new ClientUIQueryDlg(getBillUI(), null, _getCorp()
				.getPrimaryKey(), getBillUI().getModuleCode(), _getOperator(),
				null, null);
	}

	protected String getHeadCondition() {
		// �޸��ֶ�
		String sql = " h.pk_corp = '" + _getCorp().getPrimaryKey()
				+ "'  and  h.pk_billtype = '" + HgPubConst.PLAN_YEAR_BILLTYPE
				+ "' ";
		PlanApplyInforVO appInfor = ((PlanPubClientUI) getBillUI()).m_appInfor;
		if (appInfor != null) {
			String dept = ((PlanPubClientUI) getBillUI()).m_appInfor// ���˲���
					// ֻ�ܲ�ѯ���Լ����ŵļƻ�
					.getCapplydeptid();
			if (PuPubVO.getString_TrimZeroLenAsNull(dept) != null) {
				sql = sql + " and h.capplydeptid = '" + dept + "'";
			}

		}
		return sql;
	}

	protected void beforeSave() throws Exception {
		AggregatedValueObject checkVO = getBillUI().getVOFromUI();
		PlanYearBVO[] items = (PlanYearBVO[]) checkVO.getChildrenVO();

		for (PlanYearBVO item : items) {
			item.validataClient();
		}
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		if(intBtn == HgPuBtnConst.IMPROT){//����
			onExcelBill();
		}else{
			super.onBoElse(intBtn);
		}
	}

	/**
	 * liuys Excel���뵽���� �������ڣ�(2010-12-23 9:51:50)
	 */
	public void onExcelBill() {
		// ����ѡ�е��ϴ����ļ�
		String[] sFileNames = null;
		// ���ļ�
		doShowDir();
		// ���ݰ�ť�����ж�
		if (state == javax.swing.JFileChooser.CANCEL_OPTION) {
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000074")/* @res "�ϴ�������ȡ����" */);
			return;
		} else {
			// ��ȡѡ�е��ϴ��ļ�
			sFileNames = getSelectedFiles();
			if (sFileNames == null || sFileNames.length <= 0) {
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000075")/*
														 * @res "��ѡ��Ҫ�ϴ��ĵ����ļ���"
														 */);
				if (!((BillManageUI) getBillUI()).isListPanelSelected()) {
					// ��ʾ���ݡ�����ť״̬
					getBufferData().clear();
					getBufferData().updateView();
				}
				return;
			}
		}

		try {

			// ��ȡ�ϴ��ļ���·��
			String sPath = gettfDirectory().getText().trim();
			// �����ϴ�
			getUploadCtrl().UpLoadFiles(getCorpId(), sFileNames, sPath,
					getUserId(), getLogDate());
			String isSuccess = "success";

			isUpLoadFileSuccessNew(isSuccess);

		} catch (Exception e) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "����" */, e
					.getMessage());
			return;
		}
	}

	/**
	 * @���ܣ��жϵ����Ƿ��ϴ��ɹ�
	 * @���ߣ�liuys �������ڣ�(2004-12-8 15:48:14)
	 * @param:alUploadFile--�����ϴ����ļ� alUploadFailFile--�����ϴ�ʧ�ܵ��ļ�
	 *                              askBillVOs--�����ϴ��ɹ����ļ� sPath--�ϴ��ļ���·��
	 *                              erc---EXCEL�ļ��ӿ�
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
	private void isUpLoadFileSuccessNew(String isSuccess) {
		// �ж��ļ��Ƿ��ϴ��ɹ�
		if (isSuccess != null && isSuccess.length() > 0
				&& "success".equals(isSuccess)) {
			MessageDialog
					.showWarningDlg(getBillUI(), "��ʾ", nc.ui.ml.NCLangRes
							.getInstance().getStrByID("40040701",
									"UPP40040701-000070")/* @res "�����ļ��ϴ��ɹ���" */);

		} else if ((isSuccess != null && isSuccess.length() > 0 && "false"
				.equals(isSuccess))
				|| isSuccess == null) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000073")/* @res "�����ļ��ϴ�ʧ��!" */);
		}
	}

	/**
	 * �õ�ѡ�е��ļ�
	 * 
	 * @return java.lang.String[]
	 */
	private String[] getSelectedFiles() {
		String[] ss = null;
		java.util.Vector v = new java.util.Vector();
		// String sPath = null;
		String sFilePath = null;
		UpLoadFileVO temp = null;
		if (null != excelTOBill && excelTOBill.size() > 0) {
			for (int i = 0; i < excelTOBill.size(); i++) {
				temp = new UpLoadFileVO();
				temp = (UpLoadFileVO) excelTOBill.get(i);
				if (temp != null && temp.getFileName() != null
						&& temp.getFileName().trim().length() > 0) {
					sFilePath = temp.getFileName().trim();
					v.add(sFilePath);
				}
			}
		} else {
			return null;
		}
		ss = new String[v.size()];
		v.copyInto(ss);
		return ss;
	}

	/**
	 * @���ܣ���ȡ��˾ID
	 */
	private String getCorpId() {
		String corpid = null;
		if (corpid == null || "".equals(corpid.trim())) {
			corpid = nc.ui.pub.ClientEnvironment.getInstance().getCorporation()
					.getPrimaryKey();
		}
		return corpid;
	}

	/**
	 * @���ܣ���ȡ��½����
	 */
	private String getLogDate() {
		UFDate logDate = nc.ui.pub.ClientEnvironment.getInstance().getDate();
		return logDate.toString();
	}

	/**
	 * @���ܣ���ȡClientEnvironment
	 */
	private ClientEnvironment getcl(){
		return ClientEnvironment.getInstance();
	}
	/**
	 * @���ܣ���ȡ��½��ID
	 */
	public String getUserId() {
		String userid = nc.ui.pub.ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		return userid;
	}

	/**
	 * liuys ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2010-12-23 20:53:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	private nc.ui.pub.beans.UITextField gettfDirectory() {
		if (ivjtfDirectory == null) {
			try {
				ivjtfDirectory = new nc.ui.pub.beans.UITextField();
				ivjtfDirectory.setName("tfDirectory");
				ivjtfDirectory.setBounds(92, 9, 634, 22);
				ivjtfDirectory.setMaxLength(500);
			} catch (java.lang.Throwable ivjExc) {
			}
		}
		return ivjtfDirectory;
	}

	/**
	 * ��ʾѡ���ļ�Ŀ¼
	 * 
	 */
	private void doShowDir() {
		state = getFileChooser().showOpenDialog(getBillUI());
		File f = getFileChooser().getSelectedFile();
		if (f != null && state == UIFileChooser.APPROVE_OPTION) {
			{
				// ��¼��ǰ�ļ���Ŀ¼
				m_currentPath = f.toString();
				// ���õ�ǰ�ļ�Ŀ¼
				m_fFilePath = f;

				if (m_fFilePath.isFile()) {
					int pos = f.toString().lastIndexOf("\\");
					String sFilePath = null;
					// String[] s=null;
					if (pos > 0 && pos < f.toString().length() - 1)
						sFilePath = f.toString().substring(0, pos);
					gettfDirectory().setText(sFilePath);
				} else {
					gettfDirectory().setText(f.toString());
				}
				// ������ǰĿ¼�����е��ļ�
				readAllFileList();
			}
		}
	}

	/**
	 * ��ȡĿ¼�������ļ�
	 * 
	 * @return boolean
	 */
	private boolean readAllFileList() {
		// select directory or file
		if (m_fFilePath.isDirectory()) {
			m_allcurrfiles = m_fFilePath.listFiles(new Filter("xls"));
		} else if (m_fFilePath.isFile()) {
			String fileName = m_currentPath;
			File f = new File(fileName);

			m_allcurrfiles = new File[1];
			m_allcurrfiles[0] = f;
		}
		if (m_allcurrfiles == null)
			return false;

		// ��ձ��
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();

		// �Ѷ�����XLS�ļ����뵽�����
		UpLoadFileVO vo = null;
		int k = 0;
		// UpLoadFileVO[] vos = null;
		excelTOBill = new Vector();
		if (m_ht == null || m_ht.size() == 0) { // �ǹ���ʱ
			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if (m_fFilePath.isDirectory() && !m_allcurrfiles[i].exists()) {
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], i + 1);
				excelTOBill.add(vo);
			}
		} else { // �������
			String sfiletype = null;
			String sfilename = null;
			String sfiledate = null;

			if (m_ht.containsKey("filetype")) {
				sfiletype = m_ht.get("filetype").toString();
			}
			if (m_ht.containsKey("filename")) {
				sfilename = m_ht.get("filename").toString();
			}
			if (m_ht.containsKey("filedate")) {
				sfiledate = m_ht.get("filedate").toString();
			}

			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if (!m_allcurrfiles[i].exists()) {
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], k + 1);
				String sType = null;
				String sName = null;
				String sDate = null;
				sType = vo.getFileStatus();
				sName = vo.getFileName();
				sDate = vo.getFileDate();
				// �����ļ�����
				if (sfiletype != null && sfiletype.length() > 0) {
					if (!sfiletype.equals(sType)) {
						continue;
					}
				}
				// �����ļ�����
				if (sfilename != null && sfilename.length() > 0) {
					if (sName.indexOf(sfilename) == -1) {
						continue;
					}
				}
				// �����ļ�����
				if (sfiledate != null && sfiledate.length() > 0) {
					if (sDate.indexOf(sfiletype) == -1) {
						continue;
					}
				}
				excelTOBill.add(vo);
			}
		}
		return true;
	}

	/**
	 * ����ѡ�е��ļ�����
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (m_filechooser == null) {
			m_filechooser = new UIFileChooser();
			// ��ȥ��ǰ���ļ�������
			m_filechooser.removeChoosableFileFilter(m_filechooser
					.getFileFilter());
			// ����ļ�ѡ�������
			m_filechooser.addChoosableFileFilter(new HgExcelFileFilter("xls"));// Excel�ļ�
			// ��ʾ��ѡȡ�İ����ļ���Ŀ¼
			m_filechooser
					.setFileSelectionMode(m_filechooser.FILES_AND_DIRECTORIES);
		}
		return m_filechooser;
	}

	/**
	 * liuys ���ܣ� ������ ���أ� ���⣺ ���ڣ�(2004-10-11 19:40:03) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 * 
	 * @return nc.ui.ic.pub.barcodeoffline.UpLoadCtrl
	 */
	public HgUpLoadCtrl getUploadCtrl() {
		if (m_HgUploadCtrl == null)
			m_HgUploadCtrl = new HgUpLoadCtrl(getBillUI());
		return m_HgUploadCtrl;
	}
}
