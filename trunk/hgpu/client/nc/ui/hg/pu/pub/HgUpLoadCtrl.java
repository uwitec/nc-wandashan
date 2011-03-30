package nc.ui.hg.pu.pub;

/**
 * liuys
 * ���ܣ��ϴ��ļ�ѡ��
 * ���ڣ�(2010-12-23 20:53:13)
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
	 * UpLoadCtrl ������ע�⡣
	 */
	public HgUpLoadCtrl() {
		super();
	}

	public HgUpLoadCtrl(AbstractBillUI parentUI) {
		billUI = parentUI;
	}

	/**
	 * �����ļ�f���ļ�VO�� �������ڣ�(2010-12-23 20:53:13)
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
		vo.setSequence((new Integer(iSeq)).toString()); // ���к�
		vo.setSelect(new nc.vo.pub.lang.UFBoolean(false)); // �Ƿ�ѡ�У�Ĭ��Ϊ��ѡ
		vo.setFileName(fname); // �ļ�����
		vo.setBillCode(s[0]); // ���ݺţ����ļ���һ����¼
		vo
				.setFileDate((new nc.vo.pub.lang.UFDate(f.lastModified()))
						.toString());// �ļ�����
		vo.setFileStatus(s[1]); // �ļ�״̬
		return vo;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * 
	 * @version (2010-12-23 20:53:13)
	 * 
	 * @return java.lang.String
	 */
	public String[] getBillCode(String sFilePath) {
		// ���ļ���һ����¼�ĵ��ݺ�
		String sBillCode = null;
		// ���ļ�״̬
		String sStatus = null;
		// ����
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
	 * ���ܣ������ļ����������ƻ�ר�ã�
	 * ������
	 * ���أ�
	 * ���⣺
	 * ���ڣ�(2010-12-23 20:53:13)
	 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
	 *
	 * @return java.util.ArrayList
	 * @param pk_corp java.lang.String
	 * @param sFilePath java.lang.String[]
	 * @param sDir java.lang.String
	 * @param user java.lang.String
	 * @param logDate java.lang.String
	 * @exception java.lang.Exception �쳣˵����
	 */
	public void UpLoadFiles(String pk_corp, String[] sFilePath, String sDir, String user, String logDate) throws java.lang.Exception {
		UpLoadFiles(pk_corp, sFilePath, sDir, false, user,logDate, false);
	}
	/**
	 * liuys
	 * 
	 * ���ܣ��������ļ���ѯ�۵��ã� ������ ���أ� ���⣺
	 *  ���ڣ�(2010-12-23 20:53:13) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	 *                �쳣˵����
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
			// ÿ��ֻ�ϴ�һ����֤��ȷ���ļ����Բ��ܴ����ļ�Ӱ��
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
