package nc.bs.wds.finder;
import java.util.Hashtable;
import nc.bs.pf.pub.PfDataCache;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.IBillFinder2;
/**
 * ���ݲ�����Ĭ����
 * @author mlr
 */
public abstract class AbstractBillFinder2 implements IBillFinder2 {
	private Hashtable m_finderHas = new Hashtable();

	public AbstractBillFinder2() {
		super();
	}
	/**
	 * ���ؾ���ĵ������ݲ��������ô�����Ĭ�ϵ����ݲ�������
	 * �������д�ͷ���Ĭ�ϵĲ����� 
	 */
	public IBillDataFinder2 createBillDataFinder(String billType)
			throws Exception {
		return new DefaultDataFinder2();
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
	public IBillDataFinder2 getBillDataFinder(String billType) throws Exception {
		if (!this.m_finderHas.containsKey(billType))
			this.m_finderHas.put(billType, createBillDataFinder(billType));

		return (IBillDataFinder2) this.m_finderHas.get(billType);
	}


	/**
	 * ���ص��ݹ�ϵ��VO�� �������ڣ�(2004-6-21 19:58:46)
	 * 
	 * @return nc.vo.trade.billsource.LightBillVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	public LightBillVO queryBillGraph(String id,
			String type,String code) throws Exception {
		LightBillVO vo = null;
		try {
			/** *************** */
			vo = new LightBillVO();
			vo.setID(id);
			vo.setType(type);
			//add  by zhw ���ӵ��ݺ�
			vo.setCode(code);
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