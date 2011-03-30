package nc.ui.hg.pu.pub;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*******************************************************************************
 * 文件打开框的过滤类
 */
public class HgExcelFileFilter extends FileFilter {
	String ext;

	public HgExcelFileFilter(String ext) {
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