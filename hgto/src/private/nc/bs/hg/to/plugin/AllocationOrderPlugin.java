package nc.bs.hg.to.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.bs.to.pub.BillDMO;
import nc.ui.hg.pu.pub.LongTimeTask;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;

public class AllocationOrderPlugin implements IScmBSPlugin {

	private Map<String, BillItemVO[]> m_oldItemsInfor = null;
	
	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {

		if(billvos == null||billvos.length==0)
			return;
		BillVO[] inCurVOs = (BillVO[])billvos;
		HgScmPubBO bo = new HgScmPubBO();
	//  zhw end 2011-01-08
		if(action == Action.DELETE){
			//zhf add 如果来源于计划  回写 计划累计领用数量
			for (BillVO billvo:inCurVOs) {			       
				bo.reWriteToPlanFor5X(billvo.getItemVOs(), null, false);
			}
		}else if(action == Action.SAVE){
			for (BillVO billvo:inCurVOs) {	
				//liuys add 调拨订单与需求计划容差校验
				bo.reWriteToPlanFor5X(billvo.getItemVOs(), 
						m_oldItemsInfor==null?null:m_oldItemsInfor.get(billvo.getHeaderVO().getPrimaryKey()), 
								true);
			}
		}
	
	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {

		HgScmPubBO bo = new HgScmPubBO();
		String loginCorp = conx.getLoginCorp();// 登录公司
		
		if (billvos != null && billvos.length > 0) {
			BillVO[] vos = (BillVO[]) billvos;

			if (action == Action.SAVE) {
				for (BillVO vo : vos) {
					// modify by zhw 2011-01-08 调拨订单进行预扣 在保存的时候
					bo.checkAndUseFund_before(vo, false, loginCorp);
					// add by liuys 2011-01-10 调拨订单容差控制
					bo.checkAllowanceForTOOrder(vo);
				}
				// end zhw
				if (PuPubVO.getString_TrimZeroLenAsNull(vos[0].getHeaderVO().getPrimaryKey()) == null)
					return;
				
				BillDMO dmo = null;
				try {
					dmo = new BillDMO();
				} catch (Exception e) {
					if (e instanceof BusinessException)
						throw (BusinessException) e;
					throw new BusinessException(e);
				}

				String headpk = null;
				BillItemVO[] tmpitems = null;
				
				if (m_oldItemsInfor == null)
					m_oldItemsInfor = new HashMap<String, BillItemVO[]>();
				else
					m_oldItemsInfor.clear();

				for (BillVO vo : vos) {
					headpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getHeaderVO().getPrimaryKey());
					
					if (headpk == null)
						continue;
					
					tmpitems = (BillItemVO[]) dmo.queryAllBodyData(headpk);
					
					if (tmpitems == null || tmpitems.length == 0)
						continue;
					m_oldItemsInfor.put(headpk, tmpitems);
				}
			} else if (action == Action.DELETE) {
				
				for (BillVO billvo : vos) {
					// modify by zhw 2011-01-08
					bo.checkAndUseFund_before(billvo, true, loginCorp);
				}
			}
		}

		// modify by zhw 2011-04-27
		if (otherparam != null) {//调拨订单处理 回写预扣金额 计划数量
			if (action == Action.BILLSTATUS) {
				
				ArrayList al = (ArrayList) otherparam;
				int billStatus = Integer.parseInt(HgPubTool.getString_NullAsTrimZeroLen(al.get(2)));
				
				if (billStatus == 7) {// 行关闭
					BillVO[] vos = (BillVO[]) al.get(3);
					bo.reWriteToPlanFor5XEnd(vos, loginCorp);
				}
			}
		}

		// modify by zhw 2011-01-08 end
	}

	public AggregatedValueObject[] checkOutVO(AggregatedValueObject[] avos)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）销售订单询采购合同价销售 2012-2-21下午02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public static UFDouble calFormAllocationOrderOrder(String cinvmanid,String cbatchid) throws Exception {
		UFDouble npriceinfor = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(cinvmanid) == null && PuPubVO.getString_TrimZeroLenAsNull(cbatchid) == null)
			return null;

		Class[] ParameterTypes = new Class[] { String.class,String.class };
		Object[] ParameterValues = new Object[] { cinvmanid,cbatchid };
		Object o=LongTimeTask.callRemoteService("scmpub","nc.bs.hg.scm.pub.HgScmPubBO", "callPurchasePactPrice", ParameterTypes, ParameterValues, 1);

		if (o == null || !(o instanceof UFDouble))
			return null;

		npriceinfor = (UFDouble) o;
		return npriceinfor;
	}
}
