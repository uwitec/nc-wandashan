package nc.bs.ic.tpyd;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.vo.ic.tpyd.TpydBVO;
import nc.vo.ic.tpyd.TpydHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class TpydBO {
	
	private BaseDAO dao = null;

	BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @throws Exception 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 托盘移动 审批回写：
	 * 检查 移出托盘是否已经变化（绑定存货没有改变,库存数量没有变化,移动数量没有超量）;
	 * 检查 目标托盘是可用（绑定存货没有改变，仍然为空,移动数量没有超量）;
	 * 检查通过：减去移出托盘库存数量，如果库存数量完全移走，则改变状态为空着;
	 *         移入托盘增加库存数量，改变状态为占用;
	 * @时间：2011-4-10下午06:59:34
	 */
	public void writeBack(AggregatedValueObject obj ) throws Exception{
		if(obj == null || obj.getParentVO()==null
				|| obj.getChildrenVO() == null
				|| obj.getChildrenVO().length ==0){
			return ;
		}
		TpydHVO hvo = (TpydHVO )obj.getParentVO();
		TpydBVO[] bvos = (TpydBVO[])obj.getChildrenVO();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(0) from tb_warehousestock where isnull(dr,0)=0 and pk_corp='"+hvo.getPk_corp()+"'");
		sql.append(" and pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? and whs_stocktonnage=? and whs_stockpieces=?");
		SQLParameter para = new SQLParameter() ;
		int i=1;
		for(TpydBVO bvo :bvos){
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayout());
			para.addParam(bvo.getPk_invmandoc());
			para.addParam(bvo.getNoutnum());
			para.addParam(bvo.getNoutassnum());
			Integer count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if(count == 0){
				throw new BusinessException("在表体第"+i+"行：移出托盘信息绑定存货或者托盘库存数量已经改变");
			}
			
			para.clearParams();
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			para.addParam(bvo.getPk_invmandoc());
			para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinnum()));
			para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinassnum()));
			count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
			if(count == 0){
				throw new BusinessException("在表体第"+i+"行：移入托盘信息绑定存货改变或者托盘已占用");
			}
			i++;
			para.clearParams();
		}
		
		String upStockInv = "update tb_warehousestock set whs_stocktonnage=coalesce(whs_stocktonnage,0)+?,whs_stockpieces=coalesce(whs_stockpieces,0)+?" +
				" where pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? ";
		String upCargeTary =" update bd_cargdoc_tray set cdt_traystatus=? " +
				" where pk_cargdoc=? and cdt_pk=?";
		for(TpydBVO bvo :bvos){
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
//			UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassnum());
			UFDouble nmovenum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmovenum());
			UFDouble nmoveassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmoveassnum());
			para.addParam(nmovenum.multiply(-1));
			para.addParam(nmoveassnum.multiply(-1));
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayout());
			para.addParam(bvo.getPk_invmandoc());
			getBaseDAO().executeUpdate(upStockInv,para);
			para.clearParams();
			if((nmovenum.sub(noutnum)).doubleValue()==0){//如果托盘移空，则改变托盘状态为空
				para.addParam(0);
				para.addParam(hvo.getPk_cargedoc());
				para.addParam(bvo.getPk_trayout());
				getBaseDAO().executeUpdate(upCargeTary,para);
				para.clearParams();
			}
			para.addParam(nmovenum);
			para.addParam(nmoveassnum);
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			para.addParam(bvo.getPk_invmandoc());
			getBaseDAO().executeUpdate(upStockInv,para);
			para.clearParams();
			para.addParam(1);
			para.addParam(hvo.getPk_cargedoc());
			para.addParam(bvo.getPk_trayin());
			getBaseDAO().executeUpdate(upCargeTary,para);
			para.clearParams();
			
		}
	}

}
