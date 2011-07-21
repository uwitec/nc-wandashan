package nc.bs.zb.pub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.bs.uap.lock.PKLock;
import nc.itf.zb.pub.ILock;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
 
 /* @author twh
 * @date 2007-9-19 下午01:54:52
 * @version V5.0
 * @主要的类使用：
 *          <ul>
 *          <li><b>如何使用该类：</b></li>
 *          根据表体的上游单据信息加锁（包括表头和表体数据）
 *          <ul>
 *          <li>传入单据必须实现IFdcLock接口。参见nc.vo.pp.pp0203.BillVO；</li>
 *          <li>需要加锁的动作脚本调用本类的lockBill方法加锁，记录返回的被锁pk：</li>
 *          //方法说明:单据加业务锁
 *          alLockedPK=runClass("nc.bs.fdc.pub.FdcLockBO","lockBill","nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
 * 
 * <li>动作脚本必须有try...finally块；</li>
 * <li>finally块中必须调用本类的unlockBill方法解锁：</li>
 * setParameter("ALLPK",(ArrayList)alLockedPK); if(alLockedPK!=null)
 * runClass("nc.bs.fdc.pub.FdcLockBO","unlockBill","nc.vo.pub.AggregatedValueObject:01,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
 * 
 * </ul>
 * <li><b>是否线程安全：</b></li>
 * <li><b>并发性要求：</b></li>
 * <li><b>使用约束：</b></li>
 * <ul>
 * 单据VO需要实现IFdcLock接口（这里只用到getSourceBillPKArray()方法）
 * </ul>
 * <li><b>其他：</b></li>
 * 从供应链移植。
 * </ul>
 * </p>
 * <p>
 * @已知的BUG：
 * <ul>
 * <li></li>
 * </ul>
 * </p>
 * <p>
 * @修改历史：
 */
public class LockBO {
	// 业务锁提示信息
	protected final String m_sLockHint = "正在进行相关操作，请稍后再试";

	public LockBO() {
		super();
	}

	/**
	 * 加业务锁，锁来源和本张
	 * 
	 * @param voBill
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:26:42)<br>
	 */
	public ArrayList lockBill(AggregatedValueObject voBill)
			throws BusinessException {
		if (!(voBill instanceof ILock)) {
			// throw new BusinessException("业务锁异常：没有实现业务锁接口!");
			System.out.println("业务锁异常：没有实现业务锁接口!");
			return null;
		}

		return lockBills(new ILock[] { (ILock) voBill });

	}

	/**
	 * 加业务锁，锁来源和本张
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:26:51)<br>
	 */
	protected ArrayList lockBills(ILock[] voBills) throws BusinessException {

		ArrayList alRet = null;

		alRet = lockSrcBills(voBills);

		alRet = lockThisBills(voBills);

		// 加动态锁后，无须解锁，返回一个无内容的结果即可。
		alRet = new ArrayList(1);
		return alRet;

	}

	/**
	 * 加业务锁，锁来源和本张
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:27:03)<br>
	 */
	public ArrayList lockBills(AggregatedValueObject[] voBills)
			throws BusinessException {

		if (!(voBills[0] instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");

		return lockBills((ILock[]) voBills);

	}

	/**
	 * 加业务锁
	 * 
	 * @param voBill
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:27:15)<br>
	 */
	public ArrayList lockICBill(AggregatedValueObject voBill)
			throws BusinessException {
		if (!(voBill instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");

		return lockThisBills(new ILock[] { (ILock) voBill });
	}

	/**
	 * 功能：加业务锁,本张单据 参数：单据VO 返回：加锁的PK们 例外：抛出
	 */
	public ArrayList lockICBills(AggregatedValueObject[] voBills1)
			throws BusinessException {

		if (voBills1 == null || voBills1.length == 0 || voBills1[0] == null) {
			throw new BusinessException("业务锁异常：传入参数为空！");

		}

		if (!(voBills1[0] instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");

		ILock[] voBills = (ILock[]) voBills1;
		return lockThisBills(voBills);
	}

	/**
	 * 加业务锁
	 * 
	 * @param alPK
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:43:25)<br>
	 */
	public boolean lockDynamicPks(String[] alPK) throws BusinessException {

		if (alPK == null || alPK.length <= 0)
			return true;

		try {
			PKLock lock = PKLock.getInstance();
			for (int i = 0, loop = alPK.length; i < loop; i++) {
				if (alPK[i]!=null && alPK[i].length()>0 && !lock.addDynamicLock(alPK[i]))
					throw new BusinessException(m_sLockHint);
			}
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			} else {
				Logger.error("业务锁异常", e);
				throw new BusinessException("业务锁异常", e);
			}
		}
		return true;
	}

	/**
	 * 加业务锁
	 * 
	 * @param sOperatorID
	 *            可空！
	 * @param alPK
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:30:26)<br>
	 */
	public ArrayList lockPKs(String sOperatorID, ArrayList alPK)
			throws BusinessException {
		try {
			// if (sOperatorID == null || sOperatorID.trim().length() == 0)
			// throw new BusinessException("非法的数据!没有当前操作员。");

			if (alPK == null || alPK.size() == 0)
				return null;

			// 当前的操作员ID

			boolean bOK = true; // 是否可操作

			// 如果需加锁单据，才执行单据加锁。

			if (alPK != null && alPK.size() > 0) {
				String[] saPK = new String[alPK.size()];
				alPK.toArray(saPK);
				bOK = lockDynamicPks(saPK);
			}

			// 如果需加锁单据，才执行单据加锁。

			// 如果不行就抛错
			if (!bOK)
				throw new BusinessException(m_sLockHint);

			// if(alPK!=null&&alPK.size()>0){
			// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("@@:-)IC-LOCK:" +
			// alPK.get(0));
			//
			// }
			return alPK;
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("业务锁异常", e);
				throw new BusinessException("业务锁异常", e);
			}
		} finally {
		}

	}

	/**
	 * 加业务锁来源单据
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:31:32)<br>
	 */
	public ArrayList lockSrcBill(AggregatedValueObject vo)
			throws BusinessException {

		if (vo == null || !(vo instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");
		ILock[] ilocks = new ILock[1];
		ilocks[0] = (ILock) vo;
		return lockSrcBills(ilocks);

	}

	/**
	 * 加业务锁来源单据
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:47:40)<br>
	 */
	private ArrayList lockSrcBills(ILock[] voBills) throws BusinessException {
		try {
			if (voBills == null || voBills.length == 0 || voBills[0] == null) {
				throw new BusinessException("业务锁异常:传入参数为空！");

			}

			String sOperatorID = voBills[0].getCurUserID();

			// 加业务锁,传入所有的PK,当前操作员，不用表名。使用set，避免重复加锁
			Set set = new HashSet();
			ArrayList alTmp = null;
			for (int i = 0; i < voBills.length; i++) {
				if (voBills[i] != null) {
					alTmp = voBills[i].getSourceBillPKArray();
					if (alTmp != null && alTmp.size() > 0)
						set.addAll(alTmp);
				}
			}

			// 如果需加锁单据，才执行单据加锁。

			if (set != null && set.size() > 0) {
				ArrayList alRet = new ArrayList();
				alRet.addAll(set);
				lockPKs(sOperatorID, alRet);
				return alRet;

			}

			return null;

		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("业务锁异常", e);
				throw new BusinessException("业务锁异常", e);
			}
		}

	}

	/**
	 * 加业务锁本张单据
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:33:06)<br>
	 */
	private ArrayList lockThisBills(ILock[] voBills)
			throws BusinessException {

		try {
			if (voBills == null || voBills.length == 0 || voBills[0] == null) {
				throw new BusinessException("业务锁异常:传入参数为空！");

			}

			String sOperatorID = voBills[0].getCurUserID();

			// 加业务锁,传入所有的PK,当前操作员，不用表名。
			ArrayList alRet = voBills[0].getThisBillPKArray();
			if (voBills.length > 1) {
				ArrayList alTmp = null;
				for (int i = 1; i < voBills.length; i++) {
					if (voBills[i] != null) {
						alTmp = voBills[i].getThisBillPKArray();
						if (alTmp != null)
							alRet.addAll(alTmp);
					}

				}
			}

			// 如果需加锁单据，才执行单据加锁。

			if (alRet != null && alRet.size() > 0) {
				lockPKs(sOperatorID, alRet);
				return alRet;
			}

			return null;
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("业务锁异常", e);
				throw new BusinessException("业务锁异常", e);
			}
		}

	}

	/**
	 * 解业务锁，需要放在finally里执行。
	 * 
	 * @param alLockedPK
	 * @param sOperatorID
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:33:50)<br>
	 */
	public void unlock(ArrayList alLockedPK, String sOperatorID)
			throws BusinessException {
		try {

			if (alLockedPK == null || alLockedPK.size() <= 0)
				return;
			// 得到锁
			String[] saPK = new String[alLockedPK.size()];
			alLockedPK.toArray(saPK);
			// ////当前的操作员ID
			// if (sOperatorID == null || sOperatorID.trim().length() == 0)
			// throw new BusinessException("业务锁异常:没有当前操作员。");
			// 如果需加锁单据，才执行源单据解锁
			if (saPK != null && saPK.length > 0) {

				// 加动态锁，无需解锁
				// LockTool.releaseLockForPks(saPK, sOperatorID);
				// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("@@:-)IC-UNLOCK:"+alLockedPK.get(0));
			}
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("业务锁异常", e);
				throw new BusinessException("业务锁异常", e);
			}
		}

	}

	/**
	 * 解业务锁，需要放在finally里执行。
	 * 
	 * @param voBill
	 * @param alLockedPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:34:39)<br>
	 */
	public void unlockBill(AggregatedValueObject voBill, ArrayList alLockedPK)
			throws BusinessException {

		if (voBill == null) {
			throw new BusinessException("业务锁异常:传入参数为空！");

		}

		if (!(voBill instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");

		unlockBills(new ILock[] { (ILock) voBill }, alLockedPK);

	}

	/**
	 * 解业务锁，需要放在finally里执行。
	 * 
	 * @param voBills
	 * @param alLockedPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:35:41)<br>
	 */
	protected void unlockBills(ILock[] voBills, ArrayList alLockedPK)
			throws BusinessException {

		if (voBills == null || voBills.length == 0 || voBills[0] == null) {
			throw new BusinessException("业务锁异常：传入参数为空！");

		}

		String sOperatorID = voBills[0].getCurUserID();

		unlock(alLockedPK, sOperatorID);

		return;

	}

	/**
	 * 解业务锁，需要放在finally里执行。
	 * 
	 * @param voBills1
	 * @param alPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 下午02:36:31)<br>
	 */
	public void unlockBills(AggregatedValueObject[] voBills1, ArrayList alPK)
			throws BusinessException {

		if (voBills1 == null || voBills1.length == 0 || voBills1[0] == null) {
			throw new BusinessException("业务锁异常：传入参数为空！");

		}

		if (!(voBills1[0] instanceof ILock))
			throw new BusinessException("业务锁异常：没有实现业务锁接口!");

		ILock[] voBills = (ILock[]) voBills1;
		unlockBills(voBills, alPK);

		return;

	}

}
