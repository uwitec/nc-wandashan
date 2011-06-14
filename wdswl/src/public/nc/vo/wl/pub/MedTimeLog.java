package nc.vo.wl.pub;

/*
 * ������ҵ����־�� 
 * 
 * ˵����һ���򵥵ġ�����࣬����Ϊ���롣 �����ʽ���ӵ���Ӧ����־�ļ� ����������ʾ��Ϣ���ܡ�
 * ����addExecutePhase��������ಽ����Ϣ��Ȼ�����showAllExecutePhase��һ�����������
 * �ô��ǲ���ÿ�ε���ScmTimeLog.start()
 */
import java.util.Vector;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.vo.pub.lang.UFDateTime;

@SuppressWarnings( { "unchecked", "serial", "deprecation", "unused",
		"static-access" })
public class MedTimeLog {
	// ��ʼʱ�̣�����ʱ��
	private long startTime, endTime;

	// nc log
	//
	private Vector vecPhaseName = new Vector();

	private Vector vecPhaseTime = new Vector();

	private boolean isDebug = false;

	/**
	 * Timer ������ע�⡣
	 */
	public MedTimeLog() {
		super();
		start();
	}

	/**
	 * ���ܣ��õ����ĵ�ʱ�� 
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
	 * ���ܣ���ʾ�������ĵ�ʱ�� 
	 */
	public void showTime(String sTaskHint) {
		if (!isDebug)
			return;
		long lTime = getTime();
		UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
		String sTmp = new String("Time=" + curTime.getTime() + ":->ִ��<"
				+ sTaskHint + ">���ĵ�ʱ��Ϊ��" + (lTime / 60000) + "��"
				+ ((lTime / 1000) % 60) + "��" + (lTime % 1000) + "����");
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
				sbTmp.append("<��˾��" + voLog.getPkcorp() + ">");
			if (voLog.getBizType() != null)
				sbTmp.append("<ҵ�����ͣ�" + voLog.getBizType() + ">");
			if (voLog.getBillType() != null)
				sbTmp.append("<�������ͣ�" + voLog.getBillType() + ">");
			if (voLog.getBillcode() != null)
				sbTmp.append("<���ݺţ�" + voLog.getBillcode() + ">");
			if (voLog.getUser() != null)
				sbTmp.append("<ҵ��Ա��" + voLog.getUser() + ">");
			if (voLog.getHint() != null)
				sbTmp.append("<" + voLog.getHint() + ">");
		}
		UFDateTime curTime = new UFDateTime(System.currentTimeMillis());
		String sTmp = new String("Time=" + curTime.getTime() + ":->ִ��<" + sbTmp
				+ ">���ĵ�ʱ��Ϊ��" + (lTime / 60000) + "��" + ((lTime / 1000) % 60)
				+ "��" + (lTime % 1000) + "����");
		if (RuntimeEnv.getInstance().isRunningInServer()) {
			// onserver
			Logger.info(sTmp);
		} else {
			// output to med-log.log
			MedLog.getFileStream().println(sTmp);
		}
	}

	/**
	 * ���ܣ���¼��ʼʱ�� 
	 */
	public void start() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * ���ܣ���ʾ�������ĵ�ʱ�� 
	 */
	public void start(String sTaskHint) {
		start();
		Logger.info("��ʼִ��" + sTaskHint);
	}

	/**
	 * ���ܣ���¼����ʱ��
	 */
	public void stop() {
		endTime = System.currentTimeMillis();
	}

	/**
	 * ���ܣ���ʾ�������ĵ�ʱ�� 
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
				sTmp.append("<��˾��" + voLog.getPkcorp() + ">");
			if (voLog.getBizType() != null)
				sTmp.append("<ҵ�����ͣ�" + voLog.getBizType() + ">");
			if (voLog.getBillType() != null)
				sTmp.append("<�������ͣ�" + voLog.getBillType() + ">");
			if (voLog.getBillcode() != null)
				sTmp.append("<���ݺţ�" + voLog.getBillcode() + ">");
			if (voLog.getUser() != null)
				sTmp.append("<ҵ��Ա��" + voLog.getUser() + ">");
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
					.println("\n" + sTaskHint + "��ʱ�䣺" + lAllTime);
		}
		for (int i = 0; i < iSize; i++) {
			if (onserver) {
				Logger.info(vecPhaseName.get(i)
						+ " ����ʱ�䣺"
						+ vecPhaseTime.get(i)
						+ " ռ��ʱ�䣺"
						+ new nc.vo.pub.lang.UFDouble((((Long) vecPhaseTime
								.get(i)).longValue() * 100.)
								/ ((lAllTime) * 1.), 2) + "%");
			} else {
				MedLog.getFileStream().println(
						vecPhaseName.get(i)
								+ " ����ʱ�䣺"
								+ vecPhaseTime.get(i)
								+ " ռ��ʱ�䣺"
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
