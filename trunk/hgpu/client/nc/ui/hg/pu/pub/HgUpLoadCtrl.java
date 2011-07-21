package nc.ui.hg.pu.pub;

/**
 * liuys
 * 功能：上传文件选择
 * 日期：(2010-12-23 20:53:13)
 */
import java.io.File;

import nc.ui.pp.pub.ExcelReadCtrl;
import nc.ui.pu.pub.PuTool;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.base.AbstractBillUI;
import nc.vo.pp.ask.UpLoadFileVO;
import nc.vo.scm.pub.SCMEnv;

public class HgUpLoadCtrl {

	AbstractBillUI billUI = null;

	/**
	 * UpLoadCtrl 构造子注解。
	 */
	public HgUpLoadCtrl() {
		super();
	}

	public HgUpLoadCtrl(AbstractBillUI parentUI) {
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
		s = getBillCode(sFilePath);
		vo.setSequence((new Integer(iSeq)).toString()); // 序列号
		vo.setSelect(new nc.vo.pub.lang.UFBoolean(false)); // 是否选中，默认为不选
		vo.setFileName(fname); // 文件名称
		vo.setBillCode(s[0]); // 单据号，读文件第一条记录
		vo
				.setFileDate((new nc.vo.pub.lang.UFDate(f.lastModified()))
						.toString());// 文件日期
		vo.setFileStatus(s[1]); // 文件状态
		return vo;
	}

	/**
	 * 子类实现该方法，返回业务界面的标题。
	 * 
	 * @version (2010-12-23 20:53:13)
	 * 
	 * @return java.lang.String
	 */
	public String[] getBillCode(String sFilePath) {
		// 读文件第一条记录的单据号
		String sBillCode = null;
		// 读文件状态
		String sStatus = null;
		// 返回
		String[] sReturn = new String[2];

		try {
			ExcelReadCtrl ExcelReadCtrl = new ExcelReadCtrl(sFilePath, true);
			nc.vo.pp.ask.ExcelFileVO vo = ExcelReadCtrl.getVOAtLine(1);
			sBillCode = vo.getBillCode();
			sStatus = ExcelReadCtrl.getExcelFileFlag();
			sReturn[0] = sBillCode;
			sReturn[1] = sStatus;
		} catch (Exception e) {
			SCMEnv.out(e.getMessage());
			PuTool.outException(e);
		}
		return sReturn;
	}

	/**
	 * liuys
	 * 功能：导入文件（年度需求计划专用）
	 * 参数：
	 * 返回：
	 * 例外：
	 * 日期：(2010-12-23 20:53:13)
	 * 修改日期，修改人，修改原因，注释标志：
	 *
	 * @return java.util.ArrayList
	 * @param pk_corp java.lang.String
	 * @param sFilePath java.lang.String[]
	 * @param sDir java.lang.String
	 * @param user java.lang.String
	 * @param logDate java.lang.String
	 * @exception java.lang.Exception 异常说明。
	 */
	public void UpLoadFiles(String pk_corp, String[] sFilePath, String sDir, String user, String logDate) throws java.lang.Exception {
		UpLoadFiles(pk_corp, sFilePath, sDir, false, user,logDate, false);
	}
	/**
	 * liuys
	 * 
	 * 功能：：导入文件（询价单用） 参数： 返回： 例外：
	 *  日期：(2010-12-23 20:53:13) 修改日期，修改人，修改原因，注释标志：
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
	public void UpLoadFiles(String pk_corp, String[] sFilePath,
			String sDir, boolean bOnlyFullBarcode, String m_user,
			String m_logDate, boolean bAutoSend) throws java.lang.Exception {

		nc.vo.hg.pu.pub.HgYearExcelFileVO[] vos = null;
		HgExcelReadCtrl erc = null;

		if (pk_corp == null || pk_corp.length() <= 0 || sFilePath == null
				|| sFilePath.length <= 0)
			return;

		String sFldPath = null;
		for (int i = 0; i < sFilePath.length; i++) {
			// 每次只上传一个保证正确的文件可以不受错误文件影响
			try {
				sFldPath = sDir + "\\" + sFilePath[i];
				erc = new HgExcelReadCtrl(sFldPath, true);

				vos = erc.getAskVOsFromExcel();
			} catch (Exception e) {
				SCMEnv.out(e);
				PuTool.outException(e);
			}
		}
		PlanPubHelper.vchangevo(vos,m_user);
	}
}
