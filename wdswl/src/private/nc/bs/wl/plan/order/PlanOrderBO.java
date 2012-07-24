package nc.bs.wl.plan.order;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.tool.WriteBackTool;
/**
 * 发运订单（WDS3）后台类
 * @author Administrator
 *
 */
public class PlanOrderBO {
	

	BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @作者：liuys
	 * @说明：完达山物流项目 
	 *   作废前校验 是否已经安排生成下游其他出库单
	 * @时间：2011-11-29 上午09:44:46
	 * @param 要作废的 发运计划单据
	 * @throws BusinessException 
	 */
	public void beforeUnDel(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SendorderVO parent =(SendorderVO) obj.getParentVO();
		String pk_sendorder = parent.getPk_sendorder();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from tb_outgeneral_h ");
		sql.append(" join tb_outgeneral_b ");
		sql.append(" on tb_outgeneral_h.general_pk= tb_outgeneral_b.general_pk");
		sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and isnull(tb_outgeneral_b.dr,0)=0 ");
		sql.append(" and tb_outgeneral_b.csourcebillhid ='"+pk_sendorder+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("已有下游其他出库单，请先删除其他出库单再做此操作");
		}
		
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-11-30下午06:19:59
	 * @param obj
	 * @throws Exception 
	 */
	public void writeBack(AggregatedValueObject obj,int ibdaction) throws Exception{
		if(obj == null ||obj.getChildrenVO()==null ||obj.getChildrenVO().length==0 ){
			return ;
		}
		SendorderBVO[] bodys= (SendorderBVO[])ObjectUtils.serializableClone(obj.getChildrenVO());
		String csourcetype=PuPubVO.getString_TrimZeroLenAsNull(bodys[0].getCsourcetype());
		//自制单据不需要回写
		if(csourcetype==null){
			return;
		}
		WriteBackTool.setVsourcebillid("csourcebillhid");
		WriteBackTool.setVsourcebillrowid("csourcebillbid");
		WriteBackTool.setVsourcebilltype("csourcetype");
		if(ibdaction==IBDACTION.SAVE){
		  if(csourcetype.equals(WdsWlPubConst.WDS1)){
			  WriteBackTool.writeBack(bodys, "wds_sendplanin_b", "pk_sendplanin_b",
					  new String[]{"ndealnum","nassdealnum"}, 
					  new String[]{"ndealnum","nassdealnum"},
					  new String[]{"ndealnum","nassdealnum"});
			
		  }
		  if(csourcetype.equals(ScmConst.m_sBillGSJDBDD)){
			  WriteBackTool.writeBack(bodys, "to_bill_b", "cbill_bid",
					  new String[]{"ndealnum","nassdealnum"}, 
					  new String[]{"ndealnum","ndealnumb"},
					  new String[]{"ndealnum","ndealnumb"});
			
		  }
		}else if(ibdaction==IBDACTION.DELETE){
		   for (int i = 0; i < bodys.length; i++) {
			 bodys[i].setStatus(VOStatus.DELETED);
		   }	
		  if(csourcetype.equals(WdsWlPubConst.WDS1)){
			  WriteBackTool.writeBack(bodys, "wds_sendplanin_b", "pk_sendplanin_b",
					  new String[]{"ndealnum","nassdealnum"}, 
					  new String[]{"ndealnum","nassdealnum"},
					  new String[]{"ndealnum","nassdealnum"});	
		  }	
		  if(csourcetype.equals(ScmConst.m_sBillGSJDBDD)){
			  WriteBackTool.writeBack(bodys, "to_bill_b", "cbill_bid",
					  new String[]{"ndealnum","nassdealnum"}, 
					  new String[]{"ndealnum","ndealnumb"},
					  new String[]{"ndealnum","ndealnumb"});	
		  }
		}
	}
}
