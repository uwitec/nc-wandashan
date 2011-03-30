package nc.bs.ic.pub.lot;

import java.util.ArrayList;

import nc.bs.ic.pub.GenMethod;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.BusinessException;
/**
 * �˴���������˵����
 * �����ߣ�����
 * �������ڣ�(2001-5-16 15:25:14)
 * ���ܣ�
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public class LotNumbRefBO  {
	
/**
 * LotNumbRefBO ������ע�⡣
 */
public LotNumbRefBO() {
	super();
}

/**
 * ���ȣ��Ӳ������ж����û�������Ƿ���ٵ���ⵥ�ݺͳ������β��մ�����������
 ������봫�����Object�����к󣬵���DMO�е�queryAllLotNum������⣬�����ٵ���ⵥ��
 ���뵽���صĽ���У������ص��ͻ��ˡ�
 * �����ߣ�����
 * ���ܣ�
 * ������ Object[] params��ArrayList FreeItemValue
 * ���أ�ArrayList
 * ���⣺BusinessException
 * ���ڣ�(2001-5-16 15:30:37)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * 
 * @deprecated
 */
public ArrayList queryAllLot(String[] params) throws BusinessException {
	String InvID = null;

	if (params[0] != null)
		InvID = params[0];
	
	return queryAllLot(InvID, " and restnum >= 0 ");
}

/**
 * 
 * ����������������ѯĳ������ķ���ĳЩ���������κš�
 * <p>
 * <b>����˵��</b>
 * @param sInvID �����������ID
 * @param whereString ��������������������ͼic_keep_detail1�е��ֶ�
 * @return ���κŵļ���
 * @throws BusinessException
 * <p>
 * @author duy
 * @time 2007-11-28 ����09:57:25
 */
public ArrayList queryAllLot(String sInvID, String whereString) throws BusinessException {
	ArrayList alAllData = null;

	try {
		LotNumbRefDMO lmrdmo = new LotNumbRefDMO();
		alAllData = lmrdmo.queryAllLot(sInvID, whereString);
	} catch (Exception e) {
		GenMethod.throwBusiException(e);
	}
	return alAllData;
}

/**
 * ������ѯ���е������������ݵķ���
 * ��������:
 * �������:
 * ����ֵ:
 * �쳣����:
 * ����:
 */
public GeneralBillVO queryAllLotData(GeneralBillVO gvo)
	throws BusinessException {
	GeneralBillVO voRet = null;
	try {
		LotNumbRefDMO lmrdmo = new LotNumbRefDMO();

		voRet = lmrdmo.queryAllLotData(gvo);

	} catch (Exception e) {
		GenMethod.throwBusiException(e);
	}
	return voRet;
}
/**
 * ���ȣ��Ӳ������ж����û�������Ƿ���ٵ���ⵥ�ݺͳ������β��մ�����������
 ������봫�����Object�����к󣬵���DMO�е�queryAllLotNum������⣬�����ٵ���ⵥ��
 ���뵽���صĽ���У������ص��ͻ��ˡ�
 * �����ߣ�����
 * ���ܣ�
 * ������ Object[] params��ArrayList FreeItemValue
 * ���أ�ArrayList
 * ���⣺BusinessException
 * ���ڣ�(2001-5-16 15:30:37)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 */
public ArrayList queryAllLotNum(Object[] params, ArrayList FreeItemValue)
	throws BusinessException {
	ArrayList alAllData = null;
	try {
		LotNumbRefDMO lmrdmo = new LotNumbRefDMO();
		alAllData = lmrdmo.queryAllLotNum(params, FreeItemValue);
		
		
		//liuys add for �׸ڿ�ҵ  ����δ�жϲ�����ִ����Լ�������Ϊ�յ�����,���½���ڶ��β������Ϊ����ô�����ֵ�һ�β��յ�ֵ
		if(alAllData == null || alAllData.size() == 0)
			return null;
		LotNumbRefVO[] voaAllData = new LotNumbRefVO[alAllData.size()];
		alAllData.toArray(voaAllData);
		GenMethod.execFormulaBatchCode(voaAllData);
	} catch (Exception e) {		
		GenMethod.throwBusiException(e);
	}		/** ���û������Ƿ���ٵ���ⵥ��ת��Ϊ�������󣬲����뷵�ؽ����ArrayList�ĵ�һ��λ�� */
	return alAllData;
}
/**
 * �����ˣ�������
�������ڣ�2007-12-18����05:19:15
����ԭ�򣺰�����û�г��������κ�
 * @param sInvID
 * @param whereString
 * @return
 * @throws BusinessException
 */
public ArrayList queryAllLotNew(String sInvID, String whereString) throws BusinessException {
	ArrayList alAllData = null;

	try {
		LotNumbRefDMO lmrdmo = new LotNumbRefDMO();
		alAllData = lmrdmo.queryAllLotNew(sInvID, whereString);
	} catch (Exception e) {
		GenMethod.throwBusiException(e);
	}
	return alAllData;
}



}
