package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.zb.bidding.make.MakeBiddingBO;
import nc.bs.zb.bidfloor.BidFloorBO;
import nc.ui.zb.bidfloor.BidFloorBodyVO;
import nc.ui.zb.bidfloor.BidFloorHeadVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.zb.pub.ZbPubConst;

/**
 * 标底价维护审批
 * @author Administrator
 *
 */
public class N_ZB06_APPROVE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;

	public N_ZB06_APPROVE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			
			AggregatedValueObject billvo = getVo();
			if(billvo == null){
				throw new BusinessException("传入数据为空");
			}
			
			HYBillVO biddingvo = (HYBillVO)billvo;
			// ####该组件为单动作工作流处理开始...不能进行修改####
			Object m_sysflowObj = procActionFlow(vo);
			if (m_sysflowObj != null) {
				return m_sysflowObj;
			}
			// ####该组件为单动作工作流处理结束...不能进行修改####
			Object retObj = null;
			setParameter("currentVo", vo.m_preValueVo);
			retObj = runClass("nc.bs.zb.pub.HYBillApprove", "approveHYBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);
			HYBillVO newbill = (HYBillVO)retObj;
			BidFloorHeadVO head = (BidFloorHeadVO)newbill.getParentVO();
			if(head.getVbillstatus()==IBillStatus.CHECKPASS){
				//回写标书上的标底价 是否存在为审批的标底价
				BidFloorBO bo = new BidFloorBO();
				bo.reWriteBidding((BidFloorBodyVO[])biddingvo.getChildrenVO());
//				回写标书业务状态为  投标
				MakeBiddingBO biddbo = new MakeBiddingBO();
				biddbo.updateBiddingBusiState(head.getCbiddingid(), ZbPubConst.BIDDING_BUSINESS_STATUE_SUBMIT);
			}
			
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
	}

	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####该组件为单动作工作流处理开始...不能进行修改####\nprocActionFlow@@;\n//####该组件为单动作工作流处理结束...不能进行修改####\nObject  retObj  =null;\n setParameter(\"currentVo\",vo.m_preValueVo);           \nretObj =runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"approveHYBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n            ArrayList ls = (ArrayList)getUserObj();\n       \n        setParameter(\"userOpt\",ls.get(1));               \n            runClassCom@ \"nc.bs.pp.pp0201.ApproveAction\", \"afterApprove\", \"&userOpt:java.lang.Integer,nc.vo.pub.AggregatedValueObject:01\"@;               \nreturn retObj;\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
