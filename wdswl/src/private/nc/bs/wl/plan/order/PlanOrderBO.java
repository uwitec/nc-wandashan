package nc.bs.wl.plan.order;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
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
	 * @throws BusinessException 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 发运订单作废的时候，需要回减 发运计划累计安排数量
	 * @时间：2011-3-28上午11:35:11
	 */
	public void reWriteSendPlan(AggregatedValueObject obj) throws BusinessException{
		if(obj == null ||obj.getChildrenVO()==null ||obj.getChildrenVO().length==0 ){
			return ;
		}
		// liuys add 作废前增加下游单据校验
		beforeUnDel(obj);
		SendorderBVO[] bodys= (SendorderBVO[])obj.getChildrenVO();
		StringBuffer sql = new StringBuffer();
		for(SendorderBVO body:bodys){			
			sql.append(" update wds_sendplanin_b set ndealnum=coalesce(ndealnum,0)-");
			sql.append(PuPubVO.getUFDouble_NullAsZero(body.getNdealnum()));
			sql.append(" where pk_sendplanin_b='"+body.getCsourcebillbid()+"'");
			sql.append(" and pk_sendplanin='"+body.getCsourcebillhid()+"'");
			if(getBaseDAO().executeUpdate(sql.toString())==0){
				throw new BusinessException("数据异常：该发运计划可能已作废");
			};
			sql.setLength(0);//清空buffer
		}
	}

}
