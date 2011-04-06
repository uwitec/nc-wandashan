package nc.itf.hg.pu.pub;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

public interface IFundCheck {

	// 控制入方 资金 定额
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）预扣 2011-2-2下午04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid 内销时 领用物资的各个公司（矿）
	 * @param sdeptid 当公司为空时  代表客户管理id  用于外销
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  主键  billtype 类型  找出修改前的金额
	 */
	public void useFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * 预扣时扣除预留（专项计划）
	 * @author zhw
	 * @说明：（鹤岗矿业）
	 * 2011-3-21下午03:07:53
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @param pk
	 * @param billtype
	 * @param planotherbmny
	 * @throws BusinessException
	 */
	public void useSpeacialFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny,String pk,String billtype,Object[][] o)
			throws BusinessException;
	// public void reUseFund_Before(String pk_plan,int ifundtype,String scorpid,
	// String sdeptid, UFDate uDate,UFDouble nmny) throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）实扣 2011-2-2下午04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid内销时 领用物资的各个公司（矿）
	 * @param sdeptid当公司为空时  代表客户管理id  用于外销
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  主键  billtype 类型  找出修改前的金额
	 */
	public void useFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 2011-4-2上午11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 2011-4-2上午11:47:47
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  主键  billtype 类型  找出修改前的金额
	 */
	public void reUseFund_before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException;

	public void checkFund_Before(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）校验是否可以进行实扣 2011-2-3下午04:06:57
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void checkFund(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;
	
	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业）实扣 2011-2-2下午04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param dept   用于材料出库单
	 * @param cust
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void useFundMater(String pk_plan, int ifundtype, String dept,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype)
			throws BusinessException;

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 2011-4-2上午11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param dept 用于材料出库单
	 * @param cust
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFundMater(String pk_plan, int ifundtype, String dept,
			String cust, UFDate uDate, UFDouble nbeforemny, UFDouble nmny)
			throws BusinessException;

	/**
	 * 
	 * @author zhw  用于调拨出库
	 * @说明：（鹤岗矿业）实扣 2011-2-2下午04:44:00
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid内销时 领用物资的各个公司（矿）
	 * @param sdeptid当公司为空时  代表客户管理id  用于外销
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 * add by zhw   pk  主键  billtype 类型  找出修改前的金额
	 */
	public void useFund1(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,String pk,String billtype,UFDouble nmny1)
			throws BusinessException;

	/**
	 * 
	 * @author zhw  用于调拨出库
	 * @说明：（鹤岗矿业） 2011-4-2上午11:47:17
	 * @param pk_plan
	 * @param ifundtype
	 * @param scorpid
	 * @param sdeptid
	 * @param uDate
	 * @param nbeforemny
	 * @param nmny
	 * @throws BusinessException
	 */
	public void reUseFund1(String pk_plan, int ifundtype, String scorpid,
			String sdeptid, UFDate uDate, UFDouble nbeforemny, UFDouble nmny,UFDouble nmny1)
			throws BusinessException;

}
