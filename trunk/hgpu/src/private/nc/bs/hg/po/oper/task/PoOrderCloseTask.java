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
 * 采购订单超过有效期截止日期时,自动关闭该订单
 * 采购订单有效期截止日期(vdef1)
 * 1. 调用原产品的查询方法
 * 2. 调用原产品的整单关闭方法
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
	 * 查询出符合条件的采购订单
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
			throw new BusinessException("查询关闭的采购订单出错");
		}
		return ordervos;
	}

	/**
	 * 调用采购订单关闭脚本
	 */
	private void closeOrder(OrderVO[] ordervos, BgWorkingContext bgwc)
			throws BusinessException {
		String action = "CLOSE";
		String PO_ORDER = "21";
		// 复制当前单据
		for (int x = 0; x < ordervos.length; x++) {
			OrderVO ordervo = (OrderVO) ordervos[x];
			OrderItemVO[] voaItem = ordervo.getBodyVO();
			int iBodyLen = voaItem.length;
			for (int i = 0; i < iBodyLen; i++) {
				// 关闭激活订单行
				if (voaItem[i].getIisactive().compareTo(
						OrderItemVO.IISACTIVE_ACTIVE) == 0) {
					// //状态字为正常关闭：到BO中设定
					// voaItem[i].setIisactive(OrderItemVO.IISACTIVE_CLOSE_NORMAL);
					// 记录关闭时间到修正日期，以备评估时查询
					voaItem[i].setDcorrectdate(new UFDate(System
							.currentTimeMillis()));

					// V501,支持记录关闭时间
					voaItem[i].setDCloseDate(new UFDate(System
							.currentTimeMillis()));
					// V501,支持记录关闭人
					voaItem[i].setCCloseUserId(bgwc.getPk_user());
				}
			}

			// 关闭
			OrderVO voRet = null;
			try {
				PfUtilBO pfbo = new PfUtilBO();
				voRet = (OrderVO) pfbo.processAction(action, PO_ORDER,
						new UFDate(System.currentTimeMillis()).toString()
								.trim(), null, ordervo, null);
			} catch (Exception e) {
				throw new BusinessException("后台任务:关闭有效截止日期的采购订单出错");
			}
		}
		return;
	}

	/**
	 * 环境信息
	 */
	// private ClientLink getClientLink(BgWorkingContext ctx) {
	// ClientLink cl = new ClientLink(ctx.getPk_corp(), NullStr.ID,
	// new UFDate(System.currentTimeMillis()), null, null, null, null,
	// null, null, false, "采购管理", "PU", "0001481000000000000G");
	// return cl;
	// }
}
