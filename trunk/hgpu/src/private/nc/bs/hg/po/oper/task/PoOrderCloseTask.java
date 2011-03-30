package nc.bs.hg.po.oper.task;

import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.vo.po.OrderItemVO;
import nc.vo.po.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
/**
 * 
 * @author liuys
 * @date 2010-12-22
 * �ɹ�����������Ч�ڽ�ֹ����ʱ,�Զ��رոö���
 * �ɹ�������Ч�ڽ�ֹ����(vdef1)
 * 1. ����ԭ��Ʒ�Ĳ�ѯ����
 * 2. ����ԭ��Ʒ�������رշ���
 */
public class PoOrderCloseTask implements IBackgroundWorkPlugin {

	public String executeTask(BgWorkingContext bgwc) throws BusinessException {
		// TODO Auto-generated method stub
		String sWhere = "  po_order.vdef1 <'"
				+ new UFDate(System.currentTimeMillis()) + "' ";
		OrderVO[] ordervos = queryOrderForClose(sWhere, bgwc.getPk_corp(), bgwc
				.getPk_user());
		closeOrder(ordervos, bgwc);

		return null;
	}

	/**
	 * ��ѯ�����������Ĳɹ�����
	 * 
	 */
	private OrderVO[] queryOrderForClose(String sWhere, String sLoginCorpId,
			String sLoginUserId) throws BusinessException {
		nc.bs.po.OrderImpl impl = new nc.bs.po.OrderImpl();
		OrderVO[] ordervos = null;
		try {
			ordervos = impl.queryOrderArrayByConds(sWhere, null, null);
		} catch (BusinessException e) {
			e.printStackTrace();
			throw new BusinessException("��ѯ�رյĲɹ���������");
		}
		return ordervos;
	}

	/**
	 * ���òɹ������رսű�
	 */
	private void closeOrder(OrderVO[] ordervos, BgWorkingContext bgwc)
			throws BusinessException {
		String action = "CLOSE";
		String PO_ORDER = "21";
		// ���Ƶ�ǰ����
		for (int x = 0; x < ordervos.length; x++) {
			OrderVO ordervo = (OrderVO) ordervos[x];
			OrderItemVO[] voaItem = ordervo.getBodyVO();
			int iBodyLen = voaItem.length;
			for (int i = 0; i < iBodyLen; i++) {
				// �رռ������
				if (voaItem[i].getIisactive().compareTo(
						OrderItemVO.IISACTIVE_ACTIVE) == 0) {
					// //״̬��Ϊ�����رգ���BO���趨
					// voaItem[i].setIisactive(OrderItemVO.IISACTIVE_CLOSE_NORMAL);
					// ��¼�ر�ʱ�䵽�������ڣ��Ա�����ʱ��ѯ
					voaItem[i].setDcorrectdate(new UFDate(System
							.currentTimeMillis()));

					// V501,֧�ּ�¼�ر�ʱ��
					voaItem[i].setDCloseDate(new UFDate(System
							.currentTimeMillis()));
					// V501,֧�ּ�¼�ر���
					voaItem[i].setCCloseUserId(bgwc.getPk_user());
				}
			}

			// �ر�
			OrderVO voRet = null;
			try {
				PfUtilBO pfbo = new PfUtilBO();
				voRet = (OrderVO) pfbo.processAction(action, PO_ORDER,
						new UFDate(System.currentTimeMillis()).toString()
								.trim(), null, ordervo, null);
			} catch (Exception e) {
				throw new BusinessException("��̨����:�ر���Ч��ֹ���ڵĲɹ���������");
			}
		}
		return;
	}

	/**
	 * ������Ϣ
	 */
	// private ClientLink getClientLink(BgWorkingContext ctx) {
	// ClientLink cl = new ClientLink(ctx.getPk_corp(), NullStr.ID,
	// new UFDate(System.currentTimeMillis()), null, null, null, null,
	// null, null, false, "�ɹ�����", "PU", "0001481000000000000G");
	// return cl;
	// }
}
