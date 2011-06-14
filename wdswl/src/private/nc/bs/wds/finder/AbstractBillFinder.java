package nc.bs.wds.finder;

import java.util.Hashtable;

import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.billsource.IBillDataFinder;
import nc.bs.trade.billsource.IBillFinder;
import nc.vo.trade.billsource.LightBillVO;


public abstract class AbstractBillFinder implements IBillFinder {
	private Hashtable m_finderHas = new Hashtable();

	/**
	 * AbstractBillFinder 构造子注解。
	 */
	public AbstractBillFinder() {
		super();
	}

	/**
	 * 返回具体的单据数据查找器。 该处返回默认的数据查找器。 创建日期：(2004-6-21 20:02:15)
	 * 
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public IBillDataFinder createBillDataFinder(String billType)
			throws Exception {
		return new DefaultDataFinder();
	}

	/**
	 * 递归函数,为当前VO查找来源单据VO
	 */
	protected void findForwardBillsForBill(LightBillVO bill) throws Exception {

		// 获得单据的驱动单据,并且获得单据的单据号
		searchForwardBills(bill);
		// 获得单据类型的名称
		if (bill.getTypeName() == null || bill.getTypeName().length() == 0) {
			bill.setTypeName(PfDataCache.getBillTypeInfo(bill.getType()).getBilltypename());
		}

		LightBillVO[] forwards = bill.getForwardBillVOs();

		for (int i = 0; forwards != null && i < forwards.length; i++)
			// 递归调用
			findForwardBillsForBill(forwards[i]);
	}

	/**
	 * 递归函数,为当前VO查找来源单据VO
	 */
	protected void findSourceBillsForBill(LightBillVO bill) throws Exception {

		// 获得单据的来源单据,并且获得单据的单据号
		searchSourceBills(bill);

		// 获得单据类型的名称
		if (bill.getTypeName() == null || bill.getTypeName().length() == 0) {
			bill.setTypeName(PfDataCache.getBillTypeInfo(bill.getType()).getBilltypename());
		}

		// 递归调用
		LightBillVO[] sources = bill.getSourceBillVOs();
		for (int i = 0; sources != null && i < sources.length; i++)
			findSourceBillsForBill(sources[i]);
	}

	/**
	 * 返回具体的单据数据查找器。 该处返回默认的数据查找器。 创建日期：(2004-6-21 20:02:15)
	 * 
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public IBillDataFinder getBillDataFinder(String billType) throws Exception {
		if (!this.m_finderHas.containsKey(billType))
			this.m_finderHas.put(billType, createBillDataFinder(billType));

		return (IBillDataFinder) this.m_finderHas.get(billType);
	}


	/**
	 * 返回单据关系的VO。 创建日期：(2004-6-21 19:58:46)
	 * 
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public nc.vo.trade.billsource.LightBillVO queryBillGraph(String id,
			String type) throws Exception {
		LightBillVO vo = null;
		try {
			/** *************** */
			vo = new LightBillVO();
			vo.setID(id);
			vo.setType(type);

			findSourceBillsForBill(vo);
			findForwardBillsForBill(vo);

		} catch (Exception e) {
			throw new nc.vo.pub.BusinessException(nc.bs.ml.NCLangResOnserver
					.getInstance().getStrByID("uifactory",
							"UPPuifactory-000000")/* @res "取来源单据错!" */);
		}
		return vo;
	}

	public LightBillVO currentvo = null;
	
	public LightBillVO getCurrentvo() {
		return currentvo;
	}

	public void setCurrentvo(LightBillVO currentvo) {
		this.currentvo = currentvo;
	}

	/**
	 * 取得对应单据的来源单据
	 */
	public void searchForwardBills(LightBillVO vo) throws Exception {
		LightBillVO[] svos = null;
		setCurrentvo(vo);//zpm 增加
		String[] types = getAllBillType();
		if (types == null)
			return;
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(vo.getType()))
				continue;

			LightBillVO[] tempVOs = getBillDataFinder(vo.getType())
					.getForwardBills(vo.getType(), vo.getID(), types[i]);
			for (int j = 0; tempVOs != null && j < tempVOs.length; j++) {
				vo.addForwardBillVO(tempVOs[j]);
			}
		}

	}

	/**
	 * 取得对应单据的来源单据
	 */
	public void searchSourceBills(LightBillVO vo) throws Exception {
		LightBillVO[] vos = getBillDataFinder(vo.getType()).getSourceBills(
				vo.getType(), vo.getID());
		for (int i = 0; vos != null && i < vos.length; i++)
			vo.addSourceBillVO(vos[i]);
	}
}