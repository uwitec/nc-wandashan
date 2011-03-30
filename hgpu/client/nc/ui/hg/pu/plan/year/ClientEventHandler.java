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
	/** 文件选取器 */
	private UIFileChooser m_filechooser = null;
	private int state = 0;
	/** 当前选择的路径 */
	private String m_currentPath = null;
	// 当前文件
	private File m_fFilePath = null;
	/** 当前目录的XLS文件数组 */
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
		// 修改字段
		String sql = " h.pk_corp = '" + _getCorp().getPrimaryKey()
				+ "'  and  h.pk_billtype = '" + HgPubConst.PLAN_YEAR_BILLTYPE
				+ "' ";
		PlanApplyInforVO appInfor = ((PlanPubClientUI) getBillUI()).m_appInfor;
		if (appInfor != null) {
			String dept = ((PlanPubClientUI) getBillUI()).m_appInfor// 过滤部门
					// 只能查询出自己部门的计划
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
		if(intBtn == HgPuBtnConst.IMPROT){//导入
			onExcelBill();
		}else{
			super.onBoElse(intBtn);
		}
	}

	/**
	 * liuys Excel导入到单据 创建日期：(2010-12-23 9:51:50)
	 */
	public void onExcelBill() {
		// 保存选中的上传的文件
		String[] sFileNames = null;
		// 打开文件
		doShowDir();
		// 根据按钮做出判断
		if (state == javax.swing.JFileChooser.CANCEL_OPTION) {
			getBillUI().showWarningMessage(
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000074")/* @res "上传操作被取消！" */);
			return;
		} else {
			// 获取选中的上传文件
			sFileNames = getSelectedFiles();
			if (sFileNames == null || sFileNames.length <= 0) {
				getBillUI().showWarningMessage(
						nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
								"UPP40040701-000075")/*
														 * @res "请选择要上传的单据文件！"
														 */);
				if (!((BillManageUI) getBillUI()).isListPanelSelected()) {
					// 显示数据、处理按钮状态
					getBufferData().clear();
					getBufferData().updateView();
				}
				return;
			}
		}

		try {

			// 获取上传文件的路径
			String sPath = gettfDirectory().getText().trim();
			// 批量上传
			getUploadCtrl().UpLoadFiles(getCorpId(), sFileNames, sPath,
					getUserId(), getLogDate());
			String isSuccess = "success";

			isUpLoadFileSuccessNew(isSuccess);

		} catch (Exception e) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "警告" */, e
					.getMessage());
			return;
		}
	}

	/**
	 * @功能：判断单据是否上传成功
	 * @作者：liuys 创建日期：(2004-12-8 15:48:14)
	 * @param:alUploadFile--所有上传的文件 alUploadFailFile--所有上传失败的文件
	 *                              askBillVOs--所有上传成功的文件 sPath--上传文件的路径
	 *                              erc---EXCEL文件接口
	 * @return:
	 * @exception:
	 * @see;
	 * @since:
	 * 
	 * @param newM_strState
	 *            java.lang.String
	 */
	private void isUpLoadFileSuccessNew(String isSuccess) {
		// 判断文件是否上传成功
		if (isSuccess != null && isSuccess.length() > 0
				&& "success".equals(isSuccess)) {
			MessageDialog
					.showWarningDlg(getBillUI(), "提示", nc.ui.ml.NCLangRes
							.getInstance().getStrByID("40040701",
									"UPP40040701-000070")/* @res "所有文件上传成功！" */);

		} else if ((isSuccess != null && isSuccess.length() > 0 && "false"
				.equals(isSuccess))
				|| isSuccess == null) {
			MessageDialog.showErrorDlg(getBillUI(), nc.ui.ml.NCLangRes
					.getInstance().getStrByID("SCMCOMMON",
							"UPPSCMCommon-000132")/* @res "警告" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("40040701",
							"UPP40040701-000073")/* @res "所有文件上传失败!" */);
		}
	}

	/**
	 * 得到选中的文件
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
	 * @功能：获取公司ID
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
	 * @功能：获取登陆日期
	 */
	private String getLogDate() {
		UFDate logDate = nc.ui.pub.ClientEnvironment.getInstance().getDate();
		return logDate.toString();
	}

	/**
	 * @功能：获取ClientEnvironment
	 */
	private ClientEnvironment getcl(){
		return ClientEnvironment.getInstance();
	}
	/**
	 * @功能：获取登陆人ID
	 */
	public String getUserId() {
		String userid = nc.ui.pub.ClientEnvironment.getInstance().getUser()
				.getPrimaryKey();
		return userid;
	}

	/**
	 * liuys 功能： 参数： 返回： 例外： 日期：(2010-12-23 20:53:13) 修改日期，修改人，修改原因，注释标志：
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
	 * 显示选择文件目录
	 * 
	 */
	private void doShowDir() {
		state = getFileChooser().showOpenDialog(getBillUI());
		File f = getFileChooser().getSelectedFile();
		if (f != null && state == UIFileChooser.APPROVE_OPTION) {
			{
				// 记录当前文件的目录
				m_currentPath = f.toString();
				// 设置当前文件目录
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
				// 读出当前目录下所有的文件
				readAllFileList();
			}
		}
	}

	/**
	 * 读取目录下所有文件
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

		// 清空表格
		getBillCardPanelWrapper().getBillCardPanel().getBillModel()
				.clearBodyData();

		// 把读出的XLS文件加入到表格中
		UpLoadFileVO vo = null;
		int k = 0;
		// UpLoadFileVO[] vos = null;
		excelTOBill = new Vector();
		if (m_ht == null || m_ht.size() == 0) { // 非过滤时
			for (int i = 0; i < m_allcurrfiles.length; i++) {
				if (m_fFilePath.isDirectory() && !m_allcurrfiles[i].exists()) {
					continue;
				}
				vo = getUploadCtrl().createFileVOs(m_allcurrfiles[i], i + 1);
				excelTOBill.add(vo);
			}
		} else { // 处理过滤
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
				// 过滤文件类型
				if (sfiletype != null && sfiletype.length() > 0) {
					if (!sfiletype.equals(sType)) {
						continue;
					}
				}
				// 过滤文件名称
				if (sfilename != null && sfilename.length() > 0) {
					if (sName.indexOf(sfilename) == -1) {
						continue;
					}
				}
				// 过滤文件日期
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
	 * 过滤选中的文件类型
	 * 
	 * @return javax.swing.JFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (m_filechooser == null) {
			m_filechooser = new UIFileChooser();
			// 移去当前的文件过滤器
			m_filechooser.removeChoosableFileFilter(m_filechooser
					.getFileFilter());
			// 添加文件选择过滤器
			m_filechooser.addChoosableFileFilter(new HgExcelFileFilter("xls"));// Excel文件
			// 表示可选取的包括文件和目录
			m_filechooser
					.setFileSelectionMode(m_filechooser.FILES_AND_DIRECTORIES);
		}
		return m_filechooser;
	}

	/**
	 * liuys 功能： 参数： 返回： 例外： 日期：(2004-10-11 19:40:03) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return nc.ui.ic.pub.barcodeoffline.UpLoadCtrl
	 */
	public HgUpLoadCtrl getUploadCtrl() {
		if (m_HgUploadCtrl == null)
			m_HgUploadCtrl = new HgUpLoadCtrl(getBillUI());
		return m_HgUploadCtrl;
	}
}
