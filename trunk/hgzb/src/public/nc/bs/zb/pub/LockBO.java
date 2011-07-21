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
 * @date 2007-9-19 ����01:54:52
 * @version V5.0
 * @��Ҫ����ʹ�ã�
 *          <ul>
 *          <li><b>���ʹ�ø��ࣺ</b></li>
 *          ���ݱ�������ε�����Ϣ������������ͷ�ͱ������ݣ�
 *          <ul>
 *          <li>���뵥�ݱ���ʵ��IFdcLock�ӿڡ��μ�nc.vo.pp.pp0203.BillVO��</li>
 *          <li>��Ҫ�����Ķ����ű����ñ����lockBill������������¼���صı���pk��</li>
 *          //����˵��:���ݼ�ҵ����
 *          alLockedPK=runClass("nc.bs.fdc.pub.FdcLockBO","lockBill","nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
 * 
 * <li>�����ű�������try...finally�飻</li>
 * <li>finally���б�����ñ����unlockBill����������</li>
 * setParameter("ALLPK",(ArrayList)alLockedPK); if(alLockedPK!=null)
 * runClass("nc.bs.fdc.pub.FdcLockBO","unlockBill","nc.vo.pub.AggregatedValueObject:01,&ALLPK:ArrayList",vo,m_keyHas,m_methodReturnHas);
 * 
 * </ul>
 * <li><b>�Ƿ��̰߳�ȫ��</b></li>
 * <li><b>������Ҫ��</b></li>
 * <li><b>ʹ��Լ����</b></li>
 * <ul>
 * ����VO��Ҫʵ��IFdcLock�ӿڣ�����ֻ�õ�getSourceBillPKArray()������
 * </ul>
 * <li><b>������</b></li>
 * �ӹ�Ӧ����ֲ��
 * </ul>
 * </p>
 * <p>
 * @��֪��BUG��
 * <ul>
 * <li></li>
 * </ul>
 * </p>
 * <p>
 * @�޸���ʷ��
 */
public class LockBO {
	// ҵ������ʾ��Ϣ
	protected final String m_sLockHint = "���ڽ�����ز��������Ժ�����";

	public LockBO() {
		super();
	}

	/**
	 * ��ҵ����������Դ�ͱ���
	 * 
	 * @param voBill
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:26:42)<br>
	 */
	public ArrayList lockBill(AggregatedValueObject voBill)
			throws BusinessException {
		if (!(voBill instanceof ILock)) {
			// throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");
			System.out.println("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");
			return null;
		}

		return lockBills(new ILock[] { (ILock) voBill });

	}

	/**
	 * ��ҵ����������Դ�ͱ���
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:26:51)<br>
	 */
	protected ArrayList lockBills(ILock[] voBills) throws BusinessException {

		ArrayList alRet = null;

		alRet = lockSrcBills(voBills);

		alRet = lockThisBills(voBills);

		// �Ӷ�̬�����������������һ�������ݵĽ�����ɡ�
		alRet = new ArrayList(1);
		return alRet;

	}

	/**
	 * ��ҵ����������Դ�ͱ���
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:27:03)<br>
	 */
	public ArrayList lockBills(AggregatedValueObject[] voBills)
			throws BusinessException {

		if (!(voBills[0] instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");

		return lockBills((ILock[]) voBills);

	}

	/**
	 * ��ҵ����
	 * 
	 * @param voBill
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:27:15)<br>
	 */
	public ArrayList lockICBill(AggregatedValueObject voBill)
			throws BusinessException {
		if (!(voBill instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");

		return lockThisBills(new ILock[] { (ILock) voBill });
	}

	/**
	 * ���ܣ���ҵ����,���ŵ��� ����������VO ���أ�������PK�� ���⣺�׳�
	 */
	public ArrayList lockICBills(AggregatedValueObject[] voBills1)
			throws BusinessException {

		if (voBills1 == null || voBills1.length == 0 || voBills1[0] == null) {
			throw new BusinessException("ҵ�����쳣���������Ϊ�գ�");

		}

		if (!(voBills1[0] instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");

		ILock[] voBills = (ILock[]) voBills1;
		return lockThisBills(voBills);
	}

	/**
	 * ��ҵ����
	 * 
	 * @param alPK
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:43:25)<br>
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
				Logger.error("ҵ�����쳣", e);
				throw new BusinessException("ҵ�����쳣", e);
			}
		}
		return true;
	}

	/**
	 * ��ҵ����
	 * 
	 * @param sOperatorID
	 *            �ɿգ�
	 * @param alPK
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:30:26)<br>
	 */
	public ArrayList lockPKs(String sOperatorID, ArrayList alPK)
			throws BusinessException {
		try {
			// if (sOperatorID == null || sOperatorID.trim().length() == 0)
			// throw new BusinessException("�Ƿ�������!û�е�ǰ����Ա��");

			if (alPK == null || alPK.size() == 0)
				return null;

			// ��ǰ�Ĳ���ԱID

			boolean bOK = true; // �Ƿ�ɲ���

			// �����������ݣ���ִ�е��ݼ�����

			if (alPK != null && alPK.size() > 0) {
				String[] saPK = new String[alPK.size()];
				alPK.toArray(saPK);
				bOK = lockDynamicPks(saPK);
			}

			// �����������ݣ���ִ�е��ݼ�����

			// ������о��״�
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
				Logger.error("ҵ�����쳣", e);
				throw new BusinessException("ҵ�����쳣", e);
			}
		} finally {
		}

	}

	/**
	 * ��ҵ������Դ����
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:31:32)<br>
	 */
	public ArrayList lockSrcBill(AggregatedValueObject vo)
			throws BusinessException {

		if (vo == null || !(vo instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");
		ILock[] ilocks = new ILock[1];
		ilocks[0] = (ILock) vo;
		return lockSrcBills(ilocks);

	}

	/**
	 * ��ҵ������Դ����
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:47:40)<br>
	 */
	private ArrayList lockSrcBills(ILock[] voBills) throws BusinessException {
		try {
			if (voBills == null || voBills.length == 0 || voBills[0] == null) {
				throw new BusinessException("ҵ�����쳣:�������Ϊ�գ�");

			}

			String sOperatorID = voBills[0].getCurUserID();

			// ��ҵ����,�������е�PK,��ǰ����Ա�����ñ�����ʹ��set�������ظ�����
			Set set = new HashSet();
			ArrayList alTmp = null;
			for (int i = 0; i < voBills.length; i++) {
				if (voBills[i] != null) {
					alTmp = voBills[i].getSourceBillPKArray();
					if (alTmp != null && alTmp.size() > 0)
						set.addAll(alTmp);
				}
			}

			// �����������ݣ���ִ�е��ݼ�����

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
				Logger.error("ҵ�����쳣", e);
				throw new BusinessException("ҵ�����쳣", e);
			}
		}

	}

	/**
	 * ��ҵ�������ŵ���
	 * 
	 * @param voBills
	 * @return
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:33:06)<br>
	 */
	private ArrayList lockThisBills(ILock[] voBills)
			throws BusinessException {

		try {
			if (voBills == null || voBills.length == 0 || voBills[0] == null) {
				throw new BusinessException("ҵ�����쳣:�������Ϊ�գ�");

			}

			String sOperatorID = voBills[0].getCurUserID();

			// ��ҵ����,�������е�PK,��ǰ����Ա�����ñ�����
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

			// �����������ݣ���ִ�е��ݼ�����

			if (alRet != null && alRet.size() > 0) {
				lockPKs(sOperatorID, alRet);
				return alRet;
			}

			return null;
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("ҵ�����쳣", e);
				throw new BusinessException("ҵ�����쳣", e);
			}
		}

	}

	/**
	 * ��ҵ��������Ҫ����finally��ִ�С�
	 * 
	 * @param alLockedPK
	 * @param sOperatorID
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:33:50)<br>
	 */
	public void unlock(ArrayList alLockedPK, String sOperatorID)
			throws BusinessException {
		try {

			if (alLockedPK == null || alLockedPK.size() <= 0)
				return;
			// �õ���
			String[] saPK = new String[alLockedPK.size()];
			alLockedPK.toArray(saPK);
			// ////��ǰ�Ĳ���ԱID
			// if (sOperatorID == null || sOperatorID.trim().length() == 0)
			// throw new BusinessException("ҵ�����쳣:û�е�ǰ����Ա��");
			// �����������ݣ���ִ��Դ���ݽ���
			if (saPK != null && saPK.length > 0) {

				// �Ӷ�̬�����������
				// LockTool.releaseLockForPks(saPK, sOperatorID);
				// nc.vo.scm.pub.ctrl.GenMsgCtrl.printHint("@@:-)IC-UNLOCK:"+alLockedPK.get(0));
			}
		} catch (Exception e) {
			if (e instanceof BusinessException)
				throw (BusinessException) e;
			else {
				Logger.error("ҵ�����쳣", e);
				throw new BusinessException("ҵ�����쳣", e);
			}
		}

	}

	/**
	 * ��ҵ��������Ҫ����finally��ִ�С�
	 * 
	 * @param voBill
	 * @param alLockedPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:34:39)<br>
	 */
	public void unlockBill(AggregatedValueObject voBill, ArrayList alLockedPK)
			throws BusinessException {

		if (voBill == null) {
			throw new BusinessException("ҵ�����쳣:�������Ϊ�գ�");

		}

		if (!(voBill instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");

		unlockBills(new ILock[] { (ILock) voBill }, alLockedPK);

	}

	/**
	 * ��ҵ��������Ҫ����finally��ִ�С�
	 * 
	 * @param voBills
	 * @param alLockedPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:35:41)<br>
	 */
	protected void unlockBills(ILock[] voBills, ArrayList alLockedPK)
			throws BusinessException {

		if (voBills == null || voBills.length == 0 || voBills[0] == null) {
			throw new BusinessException("ҵ�����쳣���������Ϊ�գ�");

		}

		String sOperatorID = voBills[0].getCurUserID();

		unlock(alLockedPK, sOperatorID);

		return;

	}

	/**
	 * ��ҵ��������Ҫ����finally��ִ�С�
	 * 
	 * @param voBills1
	 * @param alPK
	 * @throws BusinessException
	 * @author: twh (2007-9-19 ����02:36:31)<br>
	 */
	public void unlockBills(AggregatedValueObject[] voBills1, ArrayList alPK)
			throws BusinessException {

		if (voBills1 == null || voBills1.length == 0 || voBills1[0] == null) {
			throw new BusinessException("ҵ�����쳣���������Ϊ�գ�");

		}

		if (!(voBills1[0] instanceof ILock))
			throw new BusinessException("ҵ�����쳣��û��ʵ��ҵ�����ӿ�!");

		ILock[] voBills = (ILock[]) voBills1;
		unlockBills(voBills, alPK);

		return;

	}

}
