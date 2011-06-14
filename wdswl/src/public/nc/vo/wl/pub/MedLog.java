package nc.vo.wl.pub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MedLog {
	private static FileOutputStream fos = null;

	private static PrintStream psLog = null;

	/**
	 * 
	 */
	public MedLog() {
		super();
		// FIXME 自动生成构造函数存根
	}

	/**
	 * 
	 * @return
	 */
	public static PrintStream getFileStream() {
		if (psLog == null) {
			File file = new File("c:/scmlog");
			file.mkdir();
			try {
				fos = new FileOutputStream("c:\\scmlog\\scm-log"
						+ System.currentTimeMillis() + ".txt");
				psLog = new PrintStream(fos);
			} catch (FileNotFoundException ex) {
				nc.vo.scm.pub.SCMEnv.out(ex.getMessage());
			}
		}
		return psLog;
	}
}
