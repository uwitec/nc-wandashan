package nc.vo.wl.pub;

/*
 * 类名：业务日志类 
 * 
 * 说明：一个简单的“秒表”类，精度为毫秒。 输出方式增加到供应链日志文件 批量处理提示信息功能。
 * 调用addExecutePhase方法缓存多步的信息，然后调用showAllExecutePhase，一次批量输出。
 * 好处是不用每次调用ScmTimeLog.start()
 */
import java.util.Vector;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.vo.pub.lang.UFDateTime;

@SuppressWarnings( { "unchecked", "serial", "deprecation", "unused",
		"static-access" })
public class MedTimeLog {
	// 开始时刻，结束时刻
	private long startTime, endTime;

	// nc log
	//
	private Vector vecPhaseName = new Vector();

	private Vector vecPhaseTime = new Vector();

	private boolean isDebug = false;

	/**
	 * Timer 构造子注解。
	 */
	public MedTimeLog() {
		super();
		start();
	}

	/**
	 * 功能：得到消耗的时间 
	 */
	public long getTime() {
		stop();
		return endTime - startTime;
	}

	public void showExecuteTime(String sTaskHint) {
		stop();
		showTime(sTaskHint);
		start();
	}

	/**
	 * 功能：显示动作消耗的时间 
	 */
	public void showTime(String sTaskHint) {
		if (!isDebug)
			return;
		long lTime = getTime();
		UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
		String sTmp = new String("Time=" + curTime.getTime() + ":->执行<"
				+ sTaskHint + ">消耗的时间为：" + (lTime / 60000) + "分"
				+ ((lTime / 1000) % 60) + "秒" + (lTime % 1000) + "毫秒");
		// output to med-log.log
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			Logger.info(sTmp);
		} else {
			MedLog.getFileStream().println(sTmp);
		}
	}

	/**
	 * 
	 * @param sTaskHint
	 * @param voLog
	 */
	public void showTime(MedLogVO voLog) {
		if (!isDebug)
			return;
		long lTime = getTime();
		StringBuffer sbTmp = new StringBuffer();
		if (voLog != null) {
			if (voLog.getPkcorp() != null)
				sbTmp.append("<公司：" + voLog.getPkcorp() + ">");
			if (voLog.getBizType() != null)
				sbTmp.append("<业务类型：" + voLog.getBizType() + ">");
			if (voLog.getBillType() != null)
				sbTmp.append("<单据类型：" + voLog.getBillType() + ">");
			if (voLog.getBillcode() != null)
				sbTmp.append("<单据号：" + voLog.getBillcode() + ">");
			if (voLog.getUser() != null)
				sbTmp.append("<业务员：" + voLog.getUser() + ">");
			if (voLog.getHint() != null)
				sbTmp.append("<" + voLog.getHint() + ">");
		}
		UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
		String sTmp = new String("Time=" + curTime.getTime() + ":->执行<" + sbTmp
				+ ">消耗的时间为：" + (lTime / 60000) + "分" + ((lTime / 1000) % 60)
				+ "秒" + (lTime % 1000) + "毫秒");
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			// onserver
			Logger.info(sTmp);
		} else {
			// output to med-log.log
			MedLog.getFileStream().println(sTmp);
		}
	}

	/**
	 * 功能：记录开始时间 
	 */
	public void start() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * 功能：显示动作消耗的时间 
	 */
	public void start(String sTaskHint) {
		start();
		Logger.info("开始执行" + sTaskHint);
	}

	/**
	 * 功能：记录结束时间
	 */
	public void stop() {
		endTime = System.currentTimeMillis();
	}

	/**
	 * 功能：显示动作消耗的时间 
	 */
	public void stopAndShow(String sTaskHint) {
		stop();
		showTime(sTaskHint);
	}

	public void addExecutePhase(String sTaskHint) {
		stop();
		vecPhaseName.add(sTaskHint);
		vecPhaseTime.add(new Long(getTime()));
		start();
	}

	public void addExecutePhase(MedLogVO voLog) {
		stop();
		//
		StringBuffer sTmp = new StringBuffer();
		if (voLog != null) {
			if (voLog.getPkcorp() != null)
				sTmp.append("<公司：" + voLog.getPkcorp() + ">");
			if (voLog.getBizType() != null)
				sTmp.append("<业务类型：" + voLog.getBizType() + ">");
			if (voLog.getBillType() != null)
				sTmp.append("<单据类型：" + voLog.getBillType() + ">");
			if (voLog.getBillcode() != null)
				sTmp.append("<单据号：" + voLog.getBillcode() + ">");
			if (voLog.getUser() != null)
				sTmp.append("<业务员：" + voLog.getUser() + ">");
			if (voLog.getHint() != null)
				sTmp.append("<" + voLog.getHint() + ">");
		}
		//
		vecPhaseName.add(sTmp.toString());
		vecPhaseTime.add(new Long(getTime()));
		start();
	}

	public void showAllExecutePhase(String sTaskHint) {
		stop();
		if (!isDebug)
			return;
		int iSize = vecPhaseTime.size();
		if (iSize == 0) {
			return;
		}
		long lAllTime = 0;
		for (int i = 0; i < iSize; i++) {
			lAllTime += ((Long) vecPhaseTime.get(i)).longValue();
		}
		boolean onserver = false;
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			onserver = true;
		} else {
			MedLog.getFileStream()
					.println("\n" + sTaskHint + "总时间：" + lAllTime);
		}
		for (int i = 0; i < iSize; i++) {
			if (onserver) {
				Logger.info(vecPhaseName.get(i)
						+ " 消耗时间："
						+ vecPhaseTime.get(i)
						+ " 占总时间："
						+ new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime
								.get(i)).longValue() * 100.)
								/ ((lAllTime) * 1.), 2) + "%");
			} else {
				MedLog.getFileStream().println(
						vecPhaseName.get(i)
								+ " 消耗时间："
								+ vecPhaseTime.get(i)
								+ " 占总时间："
								+ new nc.vo.pub.lang.UFDouble(
										(((Long) vecPhaseTime.get(i))
												.longValue() * 100.)
												/ ((lAllTime) * 1.), 2) + "%");
			}
		}
		vecPhaseName = new Vector();
		vecPhaseTime = new Vector();
	}
}
