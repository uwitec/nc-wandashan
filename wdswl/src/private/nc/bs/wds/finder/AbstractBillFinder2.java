package nc.bs.wds.finder;
import java.util.Hashtable;
import nc.bs.pf.pub.PfDataCache;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.IBillFinder2;
/**
 * 单据查找器默认类
 * @author mlr
 */
public abstract class AbstractBillFinder2 implements IBillFinder2 {
	private Hashtable m_finderHas = new Hashtable();

	public AbstractBillFinder2() {
		super();
	}
	/**
	 * 返回具体的单据数据查找器。该处返回默认的数据查找器。
	 * 如果不重写就返回默认的查找器 
	 */
	public IBillDataFinder2 createBillDataFinder(String billType)
			throws Exception {
		return new DefaultDataFinder2();
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
	public IBillDataFinder2 getBillDataFinder(String billType) throws Exception {
		if (!this.m_finderHas.containsKey(billType))
			this.m_finderHas.put(billType, createBillDataFinder(billType));

		return (IBillDataFinder2) this.m_finderHas.get(billType);
	}


	/**
	 * 返回单据关系的VO。 创建日期：(2004-6-21 19:58:46)
	 * 
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	public LightBillVO queryBillGraph(String id,
			String type,String code) throws Exception {
		LightBillVO vo = null;
		try {
			/** *************** */
			vo = new LightBillVO();
			vo.setID(id);
			vo.setType(type);
			//add  by zhw 增加单据号
			vo.setCode(code);
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