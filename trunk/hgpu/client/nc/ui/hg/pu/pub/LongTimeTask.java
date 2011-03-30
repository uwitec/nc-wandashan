/*
 * 
 */
package nc.ui.hg.pu.pub;

import java.awt.Container;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.SwingUtilities;
import nc.ui.pub.beans.UIDialog;
import nc.ui.scm.service.LocalCallService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.service.ServcallVO;

/**
 * @author zhf
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class LongTimeTask {

	private static nc.ui.pub.tools.BannerDialog m_dlgBanner = null;

	public static class DoCloseDlg implements Runnable {
		UIDialog dlg = null;

		DoCloseDlg(UIDialog adlg) {
			dlg = adlg;
		};

		public void run() {
			if (dlg != null) {
				dlg.setVisible(false);
				dlg.closeOK();
				dlg.dispose();
			}
		}
	};

	private static class RunMsg implements Runnable {
		private nc.ui.pub.tools.BannerDialog dlg = null;

		private String msg = null;

		RunMsg(nc.ui.pub.tools.BannerDialog adlg, String aMsg) {
			dlg = adlg;
			msg = aMsg;
		};

		public void run() {
			if (dlg != null) {
				dlg.setStartText(msg);
			}
		}
	};

	/**
	 * 
	 */
	public LongTimeTask() {
		super();
		// TODO 自动生成构造函数存根
	}

	//
	public static class WorkThread extends Thread {
		public final static int Interrupted = -1;

		public final static int Run = 1;

		public final static int Stop = 0;

		private int runstate = Stop;

		private Exception ex = null;

		private Object resultobj = null;

		public Object getResult() {
			return resultobj;
		}

		public Exception getException() {
			return ex;
		}

		private String modulename;

		private String classname;

		private String methodname;

		private Class[] ParameterTypes;

		private Object[] ParameterValues;

		private UIDialog digui;

		private int iCallPubServerType = -1;

		private Object runobj;

		public WorkThread(UIDialog digui, int iCallPubServerType,
				String modulename, String classname, Object runobj,
				String methodname, Class[] ParameterTypes,
				Object[] ParameterValues) {
			this.modulename = modulename;
			this.digui = digui;
//			if (isCallEJBServer) {
//				this.iCallPubServerType = 1;
//			} else {
//				this.iCallPubServerType = 2;
//			}
			this.iCallPubServerType = iCallPubServerType;

			this.classname = classname;
			this.runobj = runobj;
			this.methodname = methodname;
			this.ParameterTypes = ParameterTypes;
			this.ParameterValues = ParameterValues;
		}
		
		

		public void run() {
			runstate = Run;
			ex = null;
			resultobj = null;
			try {

				if ("nc.ui.pub.pf.PfUtilClient".equals(classname)
						&& methodname.startsWith("process")
						&& ParameterValues != null) {
					for (int i = 0; i < ParameterValues.length; i++)
						if (ParameterValues[i] != null
								&& Container.class
										.isInstance(ParameterTypes[i])) {
							ParameterValues[i] = this.digui;
							break;
						}
				}
				resultobj = callService(modulename, iCallPubServerType,
						classname, runobj, methodname, ParameterTypes,
						ParameterValues);
				// Thread.sleep(5000);
				runstate = Stop;
			} catch (Exception e) {
				resultobj = null;
				ex = e;
				runstate = Interrupted;
				if (digui != null && digui.isVisible()) {
					try {
						SwingUtilities.invokeAndWait(new DoCloseDlg(digui));
					} catch (Exception ee) {
						nc.vo.scm.pub.SCMEnv.error(ee);
					}
				}
			} finally {
				if (digui != null && digui.isVisible()) {
					try {
						SwingUtilities.invokeAndWait(new DoCloseDlg(digui));
					} catch (Exception ee) {
						nc.vo.scm.pub.SCMEnv.error(ee);
					}
				}
			}
			return;
		}

		public int getRunState() {
			return runstate;
		}

		public synchronized void setRunState(int istate) {
			runstate = istate;
		}
	}
	/**
	 * 单据动作批处理
	 */
	public static Object[] processBatch(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject[] voAry, Object[] userObjAry) throws Exception {
		return (Object[])calllongTimeService("so",parent,null,-1,"nc.ui.pub.pf.PfUtilClient",null,
				"processBatch",
				new Class[]{Container.class,String.class,String.class,String.class,
						AggregatedValueObject[].class,Object[].class},
				new Object[]{parent,actionName,billType,currentDate,voAry,userObjAry});
	}

	/**
	 * 单据动作处理（非"APPROVE"/"UNAPPROVE"）
	 */
	public static Object processAction(Container parent, String actionName, String billType,
			String currentDate, AggregatedValueObject vo, Object userObj) throws Exception {
		return  calllongTimeService("so",parent,null,-1,"nc.ui.pub.pf.PfUtilClient",null,
				"processAction",
				new Class[]{Container.class,String.class,String.class,String.class,
						AggregatedValueObject.class,Object.class},
				new Object[]{parent,actionName,billType,currentDate,vo,userObj});
	}
	// /*
	// *
	// */
	// public static Object callLongTimeEJBService(String modulename,Container
	// containter, String msg,
	// String classname, String methodname, Class[] ParameterTypes,
	// Object[] ParameterValues) throws Exception {
	//
	// return procclongTime(modulename,containter, msg, 2, classname, null,
	// methodname,
	// ParameterTypes, ParameterValues);
	// }
	//
	// /*
	// *
	// */
	// public static Object callLongTimeService(String modulename,Container
	// containter, String msg,
	// String classname, String methodname, Class[] ParameterTypes,
	// Object[] ParameterValues) throws Exception {
	//
	// return procclongTime(modulename,containter, msg, 1, classname, null,
	// methodname,
	// ParameterTypes, ParameterValues);
	// }

	/**
	 * 
	 */
	public static Object calllongTimeService(String modulename,
			Container containter, String msg, int iCallPubServerType,
			String classname, Object runobj, String methodname,
			Class[] ParameterTypes, Object[] ParameterValues) throws Exception {

		return calllongTimeService(modulename, containter, msg, 1000,
				iCallPubServerType, classname, runobj, methodname, ParameterTypes,
				ParameterValues);
	}

	public static Object calllongTimeService(String modulename,
			Container containter, String msg, long idelay,
			int iCallPubServerType, String classname, Object runobj,
			String methodname, Class[] ParameterTypes, Object[] ParameterValues)
			throws Exception {

		Object oret = null;

		m_dlgBanner = new nc.ui.pub.tools.BannerDialog(containter);
		if (msg == null || msg.trim().length() <= 0)
			msg = "正在操作，请等待...";
		m_dlgBanner.setStartText(msg);

		WorkThread taskwork = new WorkThread(m_dlgBanner, iCallPubServerType,
				modulename, classname, runobj, methodname, ParameterTypes,
				ParameterValues);
		boolean isshow = false;

		try {
			long icur = System.currentTimeMillis();
			long istart = System.currentTimeMillis();
			taskwork.setRunState(WorkThread.Run);
			taskwork.start();

			while (taskwork.getRunState() == WorkThread.Run) {
				if ((icur - istart) > idelay && !isshow) {
					// dlgBanner.start();
					m_dlgBanner.showModal();
					isshow = true;
					taskwork.join();
					break;
				}
				if (taskwork.getRunState() != WorkThread.Run)
					break;
				Thread.sleep(200);
				icur = System.currentTimeMillis();
			}
			oret = taskwork.getResult();
		} catch (Exception e) {
			nc.vo.scm.pub.SCMEnv.error(e);
			throw e;
		} finally {
			if (m_dlgBanner != null && m_dlgBanner.isVisible()) {
				// dlgBanner.end();
				m_dlgBanner.closeOK();
			}
			m_dlgBanner = null;
		}
		if (taskwork.getException() != null)
			throw taskwork.getException();
		return oret;
	}

	public static Object callRemoteService(String modulename,
			String classname, String methodname, Class[] ParameterTypes,
			Object[] ParameterValues, int iCallPubServerType) throws Exception {
		ServcallVO[] scd = new ServcallVO[1];
		Object oret = null;
		scd[0] = new ServcallVO();
		scd[0].setBeanName(classname);
		scd[0].setMethodName(methodname);
		scd[0].setParameterTypes(ParameterTypes);
		scd[0].setParameter(ParameterValues);
		// modifeid by lirr 2008-10-14
		// Object[] otemps = LocalCallService.callEJBService(CTConst.MODULE_IC,
		// scd);
		Object[] otemps = null;
		if (iCallPubServerType == 1) {
			otemps = LocalCallService.callEJBService(modulename, scd);
		} else if (iCallPubServerType == 2) {
			otemps = LocalCallService.callService(modulename, scd);
		}
		// = LocalCallService.callEJBService(modulename, scd);
		if (otemps != null && otemps.length > 0)
			oret = otemps[0];

		return oret;
	}

	/**
	 * 调用方法。
	 * 
	 * 创建日期：(2003-6-9)
	 * 
	 * @param mainBill
	 *            nc.vo.ybstep2.bill.MainBillVO
	 * @return java.lang.String 所插入VO对象的主键字符串。
	 * @exception java.rmi.RemoteException
	 *                异常说明。
	 */
	public static Object callService(String modulename, int iCallPubServerType,
			String classname, Object runobj, String methodname,
			Class[] ParameterTypes, Object[] ParameterValues) throws Exception {
		Object oret = null;
		if (classname == null || methodname == null)
			return oret;

		try {

			if (iCallPubServerType == 1 || iCallPubServerType == 2)
				return callRemoteService(modulename, classname, methodname,
						ParameterTypes, ParameterValues, iCallPubServerType);

			Class cl = runobj == null ? Class.forName(classname) : runobj
					.getClass();
			if (cl == null)
				return oret;
			Method m = cl.getMethod(methodname, ParameterTypes);
			if (m == null)
				return oret;
			if (Modifier.isStatic(m.getModifiers())) {
				oret = m.invoke(null, ParameterValues);
			} else {
				oret = m.invoke(runobj == null ? cl.newInstance() : runobj,
						ParameterValues);
			}
		} catch (java.lang.reflect.InvocationTargetException e) {
			Throwable ex = e.getTargetException();
			if (ex instanceof Exception) {
				throw (Exception) ex;
			} else {
				throw new Exception(e.getMessage());
			}
		}
		return oret;
	}

	public static void showHintMsg(String msg) {
		if (LongTimeTask.m_dlgBanner == null)
			return;
		try {
			SwingUtilities
					.invokeLater(new RunMsg(LongTimeTask.m_dlgBanner, msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-3-1 13:23:21)
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(String[] args) {

	}

}
