package nc.bs.wds.report.kclcx;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

public class ReportKclcxBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	public ReportBaseVO[] doQuery(String whereSql) throws BusinessException{
		StringBuffer str = new StringBuffer();
		str.append(" select bd_cargdoc.csname kcname,  ");//  货位
		str.append(" bd_stordoc.storname   ckname, ");//仓库
		str.append(" bd_invbasdoc.invcode invcode, ");  //存货编码
		str.append(" bd_invbasdoc.invname invname, ");//存货名称

		str.append(" tb_stockstate.ss_state ss_state , ");//存货状态

		str.append(" sum(h.whs_stocktonnage) whs_stocktonnage, ");//存货主数量汇总
		str.append(" sum(h.whs_stockpieces) whs_stockpieces, ");   //存货辅数量汇总
		str.append(" sum(h.whs_omnum) whs_omnum, ");   //原始入库数量汇总
		str.append(" sum(h.whs_oanum) whs_oanum ");   //原始入库辅数量汇总
		
		str.append(" from tb_warehousestock  h ");
		str.append(" join    tb_stockstate on tb_stockstate.ss_pk=h.ss_pk ");
		str.append(" join   bd_cargdoc on h.pk_cargdoc=bd_cargdoc.pk_cargdoc ");
		str.append(" join  bd_stordoc on bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc ");
		str.append(" join  bd_invbasdoc on  h.pk_invbasdoc=bd_invbasdoc.pk_invbasdoc ");
		str.append(" join  bd_invmandoc on h.pk_invmandoc=bd_invmandoc.pk_invmandoc ");
		str.append(" join   bd_cargdoc_tray  on  h.pplpt_pk=bd_cargdoc_tray.cdt_pk ");
		
		str.append(" where ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){
			str.append(whereSql);
		}
		str.append(" and isnull(h.dr,0)=0  and " + " isnull(tb_stockstate.dr,0)=0 ");
		str.append(" and isnull(bd_cargdoc.dr,0)=0  and " + " isnull(bd_stordoc.dr,0)=0 ");
		str.append(" and  isnull(bd_invmandoc.dr,0)=0  ");
	
		
		str.append(" group by  bd_cargdoc.csname,bd_stordoc.storname,bd_invbasdoc.invcode,bd_invbasdoc.invname,tb_stockstate.ss_state ");//分类汇总
		str.append(" order by  bd_cargdoc.csname,bd_stordoc.storname ");  
		List ldata = (List)getDao().executeQuery(str.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
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
	private String[] keys = new String[]{"kcname","ckname","invcode","invname",
			"ss_state","whs_stocktonnage","whs_stockpieces","whs_omnum","whs_oanum"};

}
