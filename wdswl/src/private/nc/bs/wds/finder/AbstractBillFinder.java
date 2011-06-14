package nc.bs.wds.finder;

import java.util.Hashtable;

import nc.bs.pf.pub.PfDataCache;
import nc.bs.trade.billsource.IBillDataFinder;
import nc.bs.trade.billsource.IBillFinder;
import nc.vo.trade.billsource.LightBillVO;


public abstract class AbstractBillFinder implements IBillFinder {
	private Hashtable m_finderHas = new Hashtable();

	/**
	 * AbstractBillFinder ������ע�⡣
	 */
	public AbstractBillFinder() {
		super();
	}

	/**
	 * ���ؾ���ĵ������ݲ������� �ô�����Ĭ�ϵ����ݲ������� �������ڣ�(2004-6-21 20:02:15)
	 * 
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public IBillDataFinder createBillDataFinder(String billType)
			throws Exception {
		return new DefaultDataFinder();
	}

	/**
	 * �ݹ麯��,Ϊ��ǰVO������Դ����VO
	 */
	protected void findForwardBillsForBill(LightBillVO bill) throws Exception {

		// ��õ��ݵ���������,���һ�õ��ݵĵ��ݺ�
		searchForwardBills(bill);
		// ��õ������͵�����
		if (bill.getTypeName() == null || bill.getTypeName().length() == 0) {
			bill.setTypeName(PfDataCache.getBillTypeInfo(bill.getType()).getBilltypename());
		}

		LightBillVO[] forwards = bill.getForwardBillVOs();

		for (int i = 0; forwards != null && i < forwards.length; i++)
			// �ݹ����
			findForwardBillsForBill(forwards[i]);
	}

	/**
	 * �ݹ麯��,Ϊ��ǰVO������Դ����VO
	 */
	protected void findSourceBillsForBill(LightBillVO bill) throws Exception {

		// ��õ��ݵ���Դ����,���һ�õ��ݵĵ��ݺ�
		searchSourceBills(bill);

		// ��õ������͵�����
		if (bill.getTypeName() == null || bill.getTypeName().length() == 0) {
			bill.setTypeName(PfDataCache.getBillTypeInfo(bill.getType()).getBilltypename());
		}

		// �ݹ����
		LightBillVO[] sources = bill.getSourceBillVOs();
		for (int i = 0; sources != null && i < sources.length; i++)
			findSourceBillsForBill(sources[i]);
	}

	/**
	 * ���ؾ���ĵ������ݲ������� �ô�����Ĭ�ϵ����ݲ������� �������ڣ�(2004-6-21 20:02:15)
	 * 
	 * @return nc.bs.trade.billsource.IBillDataFinder
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public IBillDataFinder getBillDataFinder(String billType) throws Exception {
		if (!this.m_finderHas.containsKey(billType))
			this.m_finderHas.put(billType, createBillDataFinder(billType));

		return (IBillDataFinder) this.m_finderHas.get(billType);
	}


	/**
	 * ���ص��ݹ�ϵ��VO�� �������ڣ�(2004-6-21 19:58:46)
	 * 
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
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
							"UPPuifactory-000000")/* @res "ȡ��Դ���ݴ�!" */);
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
	 * ȡ�ö�Ӧ���ݵ���Դ����
	 */
	public void searchForwardBills(LightBillVO vo) throws Exception {
		LightBillVO[] svos = null;
		setCurrentvo(vo);//zpm ����
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
	 * ȡ�ö�Ӧ���ݵ���Դ����
	 */
	public void searchSourceBills(LightBillVO vo) throws Exception {
		LightBillVO[] vos = getBillDataFinder(vo.getType()).getSourceBills(
				vo.getType(), vo.getID());
		for (int i = 0; vos != null && i < vos.length; i++)
			vo.addSourceBillVO(vos[i]);
	}
}