package nc.ui.zmpub.pub.excel;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UITextField;
import nc.ui.trade.base.AbstractBillUI;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.ui.zmpub.pub.tool.SingleVOChangeDataUiTool;
import nc.vo.pfxx.pub.Filter;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.excel.UpLoadFileVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * Excel文件上传工具类
 * 
 * @author mlr
 * 
 */
public class UpLoadCtrlTool {

	AbstractBillUI billUI = null;
	private UITextField ivjtfDirectory = null;
	public UpLoadCtrlTool m_HgUploadCtrl;
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

	/**
	 * UpLoadCtrl 构造子注解。
	 */
	public UpLoadCtrlTool() {
		super();
	}

	public UpLoadCtrlTool(AbstractBillUI parentUI) {
		billUI = parentUI;
	}

	/**
	 * 创建文件f的文件VO。 创建日期：(2010-12-23 20:53:13)
	 */
	public UpLoadFileVO createFileVOs(File f, int iSeq) {
		UpLoadFileVO vo = new UpLoadFileVO();
		int pos = f.toString().lastIndexOf("\\");
		String fname = null;
		String sFilePath = null;
		String[] s = null;
		if (pos > 0 && pos < f.toString().length() - 1)
			fname = f.toString().substring(pos + 1);

		sFilePath = f.toString();
//		s = getBillCode(sFilePath);
		vo.setSequence((new Integer(iSeq)).toString()); // 序列号
		vo.setSelect(new nc.vo.pub.lang.UFBoolean(false)); // 是否选中，默认为不选
		vo.setFileName(fname); // 文件名称
//		vo.setBillCode(s[0]); // 单据号，读文件第一条记录
		vo
		.setFileDate((new nc.vo.pub.lang.UFDate(f.lastModified()))
				.toString());// 文件日期
//		vo.setFileStatus(s[1]); // 文件状态
		return vo;
	}

//	/**
//	 * 子类实现该方法，返回业务界面的标题。
//	 * 
//	 * @version (2010-12-23 20:53:13)
//	 * 
//	 * @return java.lang.String
//	 */
//	public String[] getBillCode(String sFilePath) {
//		// 读文件第一条记录的单据号
//		String sBillCode = null;
//		// 读文件状态
//		String sStatus = null;
//		// 返回
//		String[] sReturn = new String[2];
//
//		try {
//			ExcelReadCtrl excelReadCtrl = getCa(sFilePath, true);
//			if (excelReadCtrl == null) {
//				throw new Exception("没有注册ExcelReadCtrl实现类");
//			}
//			nc.vo.pp.ask.ExcelFileVO vo = excelReadCtrl.getVOAtLine(1);
//			sBillCode = vo.getBillCode();
//			sStatus = excelReadCtrl.getExcelFileFlag();
//			sReturn[0] = sBillCode;
//			sReturn[1] = sStatus;
//		} catch (Exception e) {
//			SCMEnv.out(e.getMessage());
//			PuTool.outException(e);
//		}
//		return sReturn;
//	}

	/**
	 * liuys 功能：导入文件（年度需求计划专用） 参数： 返回： 例外： 日期：(2010-12-23 20:53:13)
	 * 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.ArrayList
	 * @param pk_corp
	 *            java.lang.String
	 * @param sFilePath
	 *            java.lang.String[]
	 * @param sDir
	 *            java.lang.String
	 * @param user
	 *            java.lang.String
	 * @param logDate
	 *            java.lang.String
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public ReportBaseVO[] UpLoadFiles(String pk_corp, String[] sFilePath,
			String sDir, String user, String logDate)
	throws java.lang.Exception {
		return UpLoadFiles(pk_corp, sFilePath, sDir, false, user, logDate,
				false);
	}

	/**
	 * liuys
	 * 
	 * 功能：：导入文件（询价单用） 参数： 返回： 例外： 日期：(2010-12-23 20:53:13) 修改日期，修改人，修改原因，注释标志：
	 * 
	 * @return java.util.ArrayList
	 * @param pk_corp
	 *            java.lang.String
	 * @param sFilePath
	 *            java.lang.String[]
	 * @param sDir
	 *            java.lang.String
	 * @param bOnlyFullBarcode
	 *            boolean
	 * @param m_user
	 *            java.lang.String
	 * @param m_logDate
	 *            java.lang.String
	 * @param bAutoSend
	 *            boolean
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public ReportBaseVO[] UpLoadFiles(String pk_corp, String[] sFilePath,
			String sDir, boolean bOnlyFullBarcode, String m_user,
			String m_logDate, boolean bAutoSend) throws java.lang.Exception {

		ReportBaseVO[] vos = null;
		ExcelReadCtrl erc = null;

		if (pk_corp == null || pk_corp.length() <= 0 || sFilePath == null
				|| sFilePath.length <= 0)
			return null;

		String sFldPath = null;
		for (int i = 0; i < sFilePath.length; i++) {
			// 每次只上传一个保证正确的文件可以不受错误文件影响
			try {
				sFldPath = sDir + "\\" + sFilePath[i];
				erc = getCa(sFldPath, true);

				vos = erc.getAskVOsFromExcel();
			} catch (Exception e) {
				SCMEnv.out(e);
			}
		}
		return vos;
	}

	
	private ExcelReadCtrl ca = null;

	public ExcelReadCtrl getCa(String spath, boolean flag) throws Exception {
		if (ca == null) {
			String cl = getExcelReadCtrlClass();
			if (cl == null || cl.length() == 0)
				throw new Exception("没有注册ExcelReadCtrl实现类全路径");
			Class cls = Class.forName(cl);
			Constructor ctor = cls.getConstructor(new Class[] { String.class,
					boolean.class });

			this.ca = (ExcelReadCtrl) ctor.newInstance(new Object[] { spath,
					flag });
		}

		return ca;
	}

	private String cls = null;

	public String getExcelReadCtrlClass() {
		return cls;
	}

	public void setExcelReadCtrlClass(String cls) {
		this.cls = cls;
	}

	/**
	 * liuys Excel导入到单据 创建日期：(2010-12-23 9:51:50)
	 */
	public ReportBaseVO[] onExcelBill(AbstractBillUI ui) {
		// 保存选中的上传的文件
		String[] sFileNames = null;
		// 打开文件
		doShowDir(ui);
		// 根据按钮做出判断
		ReportBaseVO[] rvos = null;
		if (state == javax.swing.JFileChooser.CANCEL_OPTION) {
			ui.showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"40040701", "UPP40040701-000074")/* @res "上传操作被取消！" */);
			return null;
		} else {
			// 获取选中的上传文件
			sFileNames = getSelectedFiles();
			if (sFileNames == null || sFileNames.length <= 0) {
				ui.showWarningMessage(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("40040701", "UPP40040701-000075")/*
						 * @res
						 * "请选择要上传的单据文件！"
						 */);
				//				if (!((BillManageUI) ui).isListPanelSelected()) {
				//					// // 显示数据、处理按钮状态
				//					// getBufferData().clear();
				//					// getBufferData().updateView();
				//					// }
				//					// return;
				//				}
			}

			try {

				// 获取上传文件的路径
				String sPath = gettfDirectory().getText().trim();
				// 批量上传
				rvos = this.UpLoadFiles(getCorpId(), sFileNames, sPath,
						getUserId(), getLogDate());
				String isSuccess = "success";				

				//				对数据进行处理
				dealData(rvos);
				
				isUpLoadFileSuccessNew(isSuccess, ui);

				return rvos;
				
				

			} catch (Exception e) {
				MessageDialog.showErrorDlg(ui, nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("SCMCOMMON", "UPPSCMCommon-000132")/*
						 * @res
						 * "警告"
						 */, e
						 .getMessage());

			}
		}
		return rvos;
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
	private void isUpLoadFileSuccessNew(String isSuccess, AbstractBillUI ui) {
		// 判断文件是否上传成功
		if (isSuccess != null && isSuccess.length() > 0
				&& "success".equals(isSuccess)) {
			MessageDialog
			.showWarningDlg(ui, "提示", nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("40040701", "UPP40040701-000070")/*
					 * @res
					 * "所有文件上传成功！"
					 */);

		} else if ((isSuccess != null && isSuccess.length() > 0 && "false"
				.equals(isSuccess))
				|| isSuccess == null) {
			MessageDialog.showErrorDlg(ui,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
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

//	/**
//	 * @功能：获取ClientEnvironment
//	 */
//	private ClientEnvironment getcl() {
//		return ClientEnvironment.getInstance();
//	}

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
	private void doShowDir(AbstractBillUI ui) {
		state = getFileChooser().showOpenDialog(ui);
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
				readAllFileList(ui);
			}
		}
	}

	/**
	 * 读取目录下所有文件
	 * 
	 * @return boolean
	 */
	private boolean readAllFileList(AbstractBillUI ui) {
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
				vo = this.createFileVOs(m_allcurrfiles[i], i + 1);
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
				vo = this.createFileVOs(m_allcurrfiles[i], k + 1);
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
			m_filechooser.addChoosableFileFilter(new ExcelFileFilter("xls"));// Excel文件
			// 表示可选取的包括文件和目录
			m_filechooser
			.setFileSelectionMode(m_filechooser.FILES_AND_DIRECTORIES);
		}
		return m_filechooser;
	}

	/*******************************************************************************
	 * 文件打开框的过滤类
	 */
	public class ExcelFileFilter extends FileFilter {
		String ext;

		public ExcelFileFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			String fileName = file.getName();
			int index = fileName.lastIndexOf(".");
			if (index > 0 && index < fileName.length() - 1) {
				String extension = fileName.substring(index + 1).toLowerCase();
				if (extension.equals(ext)) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			if ("xls".equals(ext)) {
				return "Excel类型（*.xls)";
			}
			return "";
		}
	}
	
	/**
	 * 调用后台插件处理数据
	 */
	public void dealData(ReportBaseVO[] rvos) throws Exception {
		if (rvos == null || rvos.length == 0)
			return;

		ExcelReadCtrl ctrl = getCa(null, true);
		// 转换成数据vo
		if (ctrl.isSingle()){// 单体数据处理
			CircularlyAccessibleValueObject[] vos = SingleVOChangeDataUiTool
			.runChangeVOAry(rvos, ctrl.getSingleVOClass(),
					ctrl.getSingleChangeClassName());
			// 转后台处理
			Class[] ParameterTypes = new Class[] { CircularlyAccessibleValueObject[].class };
			Object[] ParameterValues = new Object[] { vos };

			LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, billUI,
					"正在处理...", 2, ctrl.getDealBOClassName(), null, "dealSingleImportDatas", ParameterTypes,
					ParameterValues);
		} else {// 单据数据处理 或 具有表头表体 的档案 需要注册单据类型 vo对照
			// 直接转后台处理
			Class[] ParameterTypes = new Class[] { ReportBaseVO[].class,
					String.class, String.class };
			Object[] ParameterValues = new Object[] { rvos, ctrl.getBillType(),
					ctrl.getTmpBillType() };
			//			LongTimeTask.callRemoteService(WdsWlPubConst.WDS_WL_MODULENAME,
			//					getDealBOClassName(), "dealBillImportDatas",
			//					ParameterTypes, ParameterValues, 2);

			LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, billUI,
					"正在处理...", 2, ctrl.getDealBOClassName(), null, "dealSingleImportDatas", ParameterTypes,
					ParameterValues);
		}
	}
}
