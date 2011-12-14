package nc.bs.wds.report.fjcbh;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.report.ReportBaseVO;

public class ReportBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public ReportBaseVO[] doQuery(String whereSql) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" bd_invbasdoc.invcode invcode, ");
		sql.append(" bd_invbasdoc.invname invname, ");
		sql.append(" bd_invbasdoc.invspec invspec, ");
		sql.append(" sum(tb_warehousestock.whs_stocktonnage) whs_stocktonnage, ");//现存数量
		sql.append(" sum(tb_warehousestock.whs_stockpieces) whs_stockpieces, ");//现存辅数量
		sql.append(" sum(wds_soorder_b.narrangnmu) narrangnmu, ");//销售运单数量
		sql.append(" sum(wds_soorder_b.nassarrangnum) nassarrangnum ");//销售运单辅数量
		sql.append(" from tb_warehousestock ");
		sql.append(" join wds_soorder_b on wds_soorder_b.pk_invmandoc=tb_warehousestock.pk_invmandoc ");
		sql.append(" join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc=tb_warehousestock.pk_invbasdoc ");
		
		
		sql.append(" where ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){
			sql.append(whereSql);
		}
		sql.append(" and isnull(tb_warehousestock.dr,0)=0  and " + " isnull(wds_soorder_b.dr,0)=0 ");
			
		sql.append(" group by  tb_warehousestock.pk_invmandoc ,bd_invbasdoc.invcode,bd_invbasdoc.invname,bd_invbasdoc.invspec ");//分类汇总
		sql.append(" order by  tb_warehousestock.pk_invmandoc  ");  
		List ldata = (List)getDao().executeQuery(sql.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
		if(ldata == null || ldata.size() == 0){
			return null;
		}
		
		Map dataMap = null;
		int len = ldata.size();
		ReportBaseVO[] vos = new ReportBaseVO[len];
		ReportBaseVO vo = null;
		for(int i=0;i<len;i++){
			vo = new ReportBaseVO();
			dataMap = (Map)ldata.get(i);
			for(String key:keys){
				vo.setAttributeValue(key, dataMap.get(key));
			}
			vos[i] = vo;
		}
		return vos;
		
				
	}
	private String[] keys = new String[]{"invcode","invname","invspec","whs_stocktonnage","whs_stockpieces","narrangnmu","nassarrangnum"};

}
