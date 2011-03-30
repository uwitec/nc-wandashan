package nc.bs.ic.pub.lot;

import java.util.ArrayList;

import nc.bs.ic.pub.GenMethod;

import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.lot.LotNumbRefVO;
import nc.vo.pub.BusinessException;
/**
 * 此处插入类型说明。
 * 创建者：张欣
 * 创建日期：(2001-5-16 15:25:14)
 * 功能：
 * 修改日期，修改人，修改原因，注释标志：
 */
public class LotNumbRefBO  {
	
/**
 * LotNumbRefBO 构造子注解。
 */
public LotNumbRefBO() {
	super();
}

/**
 * 首先，从参数表中读出用户定义的是否跟踪到入库单据和出库批次参照次序两个参数
 将其加入传入参数Object数组中后，调用DMO中的queryAllLotNum方法查库，将跟踪到入库单据
 加入到返回的结果中，并返回到客户端。
 * 创建者：张欣
 * 功能：
 * 参数： Object[] params，ArrayList FreeItemValue
 * 返回：ArrayList
 * 例外：BusinessException
 * 日期：(2001-5-16 15:30:37)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 方法功能描述：查询某个存货的符合某些条件的批次号。
 * <p>
 * <b>参数说明</b>
 * @param sInvID 存货管理档案的ID
 * @param whereString 过滤条件，必须是在视图ic_keep_detail1中的字段
 * @return 批次号的集合
 * @throws BusinessException
 * <p>
 * @author duy
 * @time 2007-11-28 上午09:57:25
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
 * 批量查询所有单据上批次数据的方法
 * 功能描述:
 * 输入参数:
 * 返回值:
 * 异常处理:
 * 日期:
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
 * 首先，从参数表中读出用户定义的是否跟踪到入库单据和出库批次参照次序两个参数
 将其加入传入参数Object数组中后，调用DMO中的queryAllLotNum方法查库，将跟踪到入库单据
 加入到返回的结果中，并返回到客户端。
 * 创建者：张欣
 * 功能：
 * 参数： Object[] params，ArrayList FreeItemValue
 * 返回：ArrayList
 * 例外：BusinessException
 * 日期：(2001-5-16 15:30:37)
 * 修改日期，修改人，修改原因，注释标志：
 */
public ArrayList queryAllLotNum(Object[] params, ArrayList FreeItemValue)
	throws BusinessException {
	ArrayList alAllData = null;
	try {
		LotNumbRefDMO lmrdmo = new LotNumbRefDMO();
		alAllData = lmrdmo.queryAllLotNum(params, FreeItemValue);
		
		
		//liuys add for 鹤岗矿业  由于未判断查出的现存量以及可用量为空的现象,导致界面第二次参照如果为空那么将出现第一次参照的值
		if(alAllData == null || alAllData.size() == 0)
			return null;
		LotNumbRefVO[] voaAllData = new LotNumbRefVO[alAllData.size()];
		alAllData.toArray(voaAllData);
		GenMethod.execFormulaBatchCode(voaAllData);
	} catch (Exception e) {		
		GenMethod.throwBusiException(e);
	}		/** 将用户定义是否跟踪到入库单据转换为整数对象，并加入返回结果的ArrayList的第一个位置 */
	return alAllData;
}
/**
 * 创建人：刘家清
创建日期：2007-12-18下午05:19:15
创建原因：包括了没有出入库的批次号
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
