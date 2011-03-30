package nc.bs.hg.pu.pub;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.trade.comsave.BillSave;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;


public class HYBillSave extends BillSave {

	public static String DATA_ERROR_ORACAL = "ORA-01438";

	public static String DATA_ERROR_MSSQL = "�����������";

	public static String DATA_ERROR_DB2 = "";
	
	public static String DATA_OVERFLOW_MSSQL = "�� float ת��Ϊ�������� numeric ʱ���������������";

	public static String DATA_OVERFLOW_ORACLE = "ORA-01438: ֵ���ڴ���ָ����������";

	public static String DATA_OVERFLOW_DB2 = "";
	

	@Override
	public ArrayList saveBill(AggregatedValueObject billVo)
			throws BusinessException {
		ArrayList al = null;
		try {
			//���ݱ���ǰ����
			beforeSave(billVo);
			//����
			al = super.saveBill(billVo);
			if (al != null && al.size()>1)
				billVo = (AggregatedValueObject)al.get(1);
			else
				billVo = null;
			//���ݱ������
			afterSave(billVo);
		} catch (Exception e) {
//			if (e instanceof BusinessException) {
////				Logger.error("���Ե����" + e);
////				e.printStackTrace();
//				if (e.getCause() == null)
//					transError(e.getMessage());
//				else {
//					String errMsg = e.getCause().getMessage() == null ? "" : e
//							.getCause().getMessage();
////					Logger.debug("���Ե����errMsg==" + errMsg);
//					transError(e.getMessage());
//					
//				}
//			} else {
//				Logger.error("���ݱ����쳣", e);
//				throw new BusinessException("���ݱ����쳣", e);
//			}
			if (e instanceof BusinessException) {
				Logger.error("���Ե����" + e);
				e.printStackTrace();
				if (e.getCause() == null)
					throw (BusinessException) e;
				else if (e.getCause() != null && e.getCause().getMessage() != null
						&& e.getCause().getMessage().indexOf("�����ֵ�������ݿ�����ľ�ȷ��,���������롣") > 0) {
					throw new BusinessException("�����ֵ�������ݿ�����ľ�ȷ��,���������롣");
				} else {
					String errMsg = e.getCause().getMessage() == null ? "" : e.getCause().getMessage();
					Logger.debug("���Ե����errMsg==" + errMsg);
					if (isDataErrorMsg(errMsg)) {
						errMsg = "�����ֵ�������ݿ�����ľ�ȷ��,���������롣";
					}
					BusinessException ex1 = new BusinessException(errMsg);
					throw ex1;
				}
			} else {
				Logger.error("���ݱ����쳣", e);
				throw new BusinessException("���ݱ����쳣", e);
			}
		}
		return al;
	}

	/**
	 * �Ƿ����ݾ��ȴ���
	 * @param msg
	 * @return
	 * twh (2006-11-30 ����10:46:18)<br>
	 */
	private boolean isDataErrorMsg(String msg) {
		if ((DATA_ERROR_DB2.length() > 0 && msg.indexOf(DATA_ERROR_DB2) >= 0)
				|| (DATA_ERROR_MSSQL.length() > 0 && msg
						.indexOf(DATA_ERROR_MSSQL) >= 0)
				|| (DATA_ERROR_ORACAL.length() > 0 && msg
						.indexOf(DATA_ERROR_ORACAL) >= 0))
			return true;
		else
			return false;
	}

	/**
	 * ���ݱ���ǰ����
	 * @param billVo
	 * @throws BusinessException
	 * twh (2006-11-30 ����10:39:22)<br>
	 */
	protected void beforeSave(AggregatedValueObject billVo)
			throws BusinessException {
		return;
	}

	/**
	 * ���ݱ������
	 * @param billVo
	 * @throws BusinessException
	 * twh (2006-11-30 ����10:39:34)<br>
	 */
	protected void afterSave(AggregatedValueObject billVo)
			throws BusinessException {
		return;
	}
	
	/**
	 * ת��������ϢΪ�û��ɽ��ܵ���Ϣ
	 * @author zhucx1
	 * 20072007-10-27����03:25:34
	 * @param errMsg
	 * @throws BusinessException
	 */
	private void transError(String errMsg) throws BusinessException{
		if (isDataErrorMsg(errMsg)) {
			errMsg = "�����ֵ�������ݿ�����ľ�ȷ��,���������롣";
		}
		BusinessException ex1 = new BusinessException(errMsg);
		throw ex1;

	}

	/**
	 * ����VO���档 �������ڣ�(2004-2-27 11:15:29)
	 * 
	 * @return nc.vo.pub.AggregatedValueObject
	 * @param vo
	 *            nc.vo.pub.AggregatedValueObject
	 * @exception java.rmi.RemoteException
	 *                �쳣˵����
	 */
	public AggregatedValueObject saveBillCom(nc.vo.pub.AggregatedValueObject billVo) throws BusinessException {
		if (billVo == null)
			throw new BusinessException(nc.bs.ml.NCLangResOnserver.getInstance().getStrByID("uffactory_hyeaa",
					"UPPuffactory_hyeaa-000041")/*
												 * @res
												 * "���棺saveBill�õ��Ĳ���Ϊnull��δ�ܱ����κ�����"
												 */);

		if (billVo.getParentVO() == null) {
			System.out.println("���棺saveBill�õ��Ĳ����ı�ͷVOδnull��δ�ܱ����κ�����");
			return null;
		}
		HYSuperDMO dmo = null;
		AggregatedValueObject retVO = null;
		try {

			dmo = new HYSuperDMO();
			if (isNew(billVo)) {
				retVO = saveBillWhenAdd(billVo, dmo);
			} else {
				retVO = saveBillWhenEdit(billVo, dmo);
			}
			return retVO;
		}

		catch (Exception e) {
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
			Logger.error("���Ե����" + e);
			e.printStackTrace();
			if (e.getCause() == null)
				throw (BusinessException) e;
			else {
				String errMsg = e.getCause().getMessage() == null ? "" : e.getCause().getMessage();
				Logger.debug("���Ե����errMsg==" + errMsg);
				if (isDataErrorMsg(errMsg)) {
					errMsg = "�����ֵ�������ݿ�����ľ�ȷ��,���������롣";
				}
				BusinessException ex1 = new BusinessException(errMsg);
				throw ex1;
			}
		}
	}

	private AggregatedValueObject saveBillWhenAdd(AggregatedValueObject vo, HYSuperDMO dmo) throws RemoteException {

		try {
			// �����ͷ��
			SuperVO headVO = (SuperVO) vo.getParentVO();
			headVO.setAttributeValue("dr", new Integer(0));
			String key = dmo.insert(headVO);

			// �����ӱ�
			SuperVO[] items = (SuperVO[]) vo.getChildrenVO();
			if (vo instanceof IExAggVO) {
				items = (SuperVO[]) ((IExAggVO) vo).getAllChildrenVO();
			}
			if (items != null) {
				// ���������
				for (int i = 0; i < items.length; i++) {
					items[i].setAttributeValue("dr", new Integer(0));
					items[i].setStatus(nc.vo.pub.VOStatus.NEW);
				}
				saveItems(dmo, items, headVO.getPKFieldName(), key);
			}

			vo.setParentVO(dmo.queryByPrimaryKey(headVO.getClass(), key));
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("HYPubBO::saveBillWhenAdd(AggregateValueObject) Exception!", e);
		}

	}
	
	/**
	* �����ݿ��в���һ��VO����
	*
	* �������ڣ�(2003-9-18)
	* @param sendmny nc.vo.testforei.AggregatedValueObject
	* @return java.lang.String  ������VO����������ַ�����
	*
	*/
	private AggregatedValueObject saveBillWhenEdit(
		AggregatedValueObject billVO,
		HYSuperDMO dmo)
		throws RemoteException {

		try
		{
			SuperVO headvo = (SuperVO) billVO.getParentVO();
			String billPK = headvo.getPrimaryKey();
			String mainPKFiledName = headvo.getPKFieldName();
			headvo.setAttributeValue("dr",new Integer(0));
			dmo.update(headvo);
			boolean isMultiChild = false;
			//�����ӱ�
			SuperVO[] items = (SuperVO[]) billVO.getChildrenVO();
			if (billVO instanceof IExAggVO)
			{
				isMultiChild = true;
				items = (SuperVO[]) ((IExAggVO) billVO).getAllChildrenVO();
			}
			if (items != null)
				saveItems(dmo, items, mainPKFiledName, billPK);

			billVO.setParentVO(dmo.queryByPrimaryKey(headvo.getClass(), billPK));
			if (items != null && items.length > 0)
				setChildData(billVO,isMultiChild,dmo);
			return billVO;
		}
		catch (Exception e)
		{
			Logger.error(e);
			throw new RemoteException(
				"HYPubBO::saveBillWhenEdit(AggregatedValueObject) Exception!",
				e);
		}

	}
	
	/**
	 * �����ӱ����ݡ�
	 * �������ڣ�(2004-2-27 20:59:29)
	 * @param vo nc.vo.pub.AggregatedValueObject
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void setChildData(
		AggregatedValueObject vo,
		boolean isMultiChild,
		HYSuperDMO dmo)
		throws java.lang.Exception {
		//��ѯ�ӱ�
		if (isMultiChild)
		{
			IExAggVO exAggVO = (IExAggVO) vo;
			for (int i = 0; i < exAggVO.getTableCodes().length; i++)
			{
				String tableCode = exAggVO.getTableCodes()[i];
				SuperVO[] items = (SuperVO[]) exAggVO.getTableVO(tableCode);
				exAggVO.setTableVO(tableCode, dealChildVO(items));
			}
		}
		else
		{
			SuperVO[] itemVos = (SuperVO[]) vo.getChildrenVO();
			SuperVO headVo = (SuperVO) vo.getParentVO();
			vo.setChildrenVO(
				dmo.queryByWhereClause(
					itemVos[0].getClass(),
					headVo.getPKFieldName()
						+ "='"
						+ headVo.getPrimaryKey()
						+ "' and isnull(dr,0)=0"));
		}
	}

	/**
	 * �����ӱ����ݣ���ȥɾ����)��
	 * �������ڣ�(2004-2-27 21:27:21)
	 * @param items nc.vo.pub.SuperVO[]
	 * @exception java.lang.Exception �쳣˵����
	 */
	private SuperVO[] dealChildVO(SuperVO[] items) throws java.lang.Exception {
		if (items == null)
			return null;
		Vector v = new Vector();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getStatus() != VOStatus.DELETED)
				v.addElement(items[i]);
		}
		if (v.size() > 0)
		{
			SuperVO[] vos =
				(SuperVO[]) java.lang.reflect.Array.newInstance(
					items.getClass().getComponentType(),
					v.size());
			v.copyInto(vos);
			return vos;
		}
		return null;
	}
	 
	
	protected boolean isNew(AggregatedValueObject billVO) throws BusinessException {
		return billVO.getParentVO().getPrimaryKey() == null || billVO.getParentVO().getPrimaryKey().length() == 0;

	}
}
