package nc.bs.dm.so.send;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;

public class SoSendBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 发运日报表
	 * @时间：2011-7-12下午02:35:25
	 * @param whereSql
	 * @return
	 * @throws BusinessException
	 */
	public ReportBaseVO[] doQuery(String whereSql,String cstorid) throws BusinessException{
	
		StringBuffer str = new StringBuffer();
		str.append("select h.pk_outwhouse,ca.pk_cargdoc ccargdocid,b.pk_invmandoc cinvmanid,b.pk_invbasdoc cinvbasid,b.uint unitid,b.assunit assunitid,sum(b.narrangnmu) nnum,sum(b.nassarrangnum) nassnum ");
		str.append(" from wds_soorder h inner join wds_soorder_b b on h.pk_soorder = b.pk_soorder ");
		str.append(" inner join tb_spacegoods cab on cab.pk_invbasdoc = b.pk_invbasdoc ");
		str.append(" inner join wds_cargdoc1 ca on ca.pk_wds_cargdoc = cab.pk_wds_cargdoc ");
		str.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and isnull(ca.dr,0)=0 and isnull(cab.dr,0)=0 ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null)
			str.append(" and "+whereSql);
		str.append(" and ca.pk_stordoc='"+cstorid+"'");
		str.append(" group by h.pk_outwhouse,ca.pk_cargdoc,b.pk_invmandoc,b.pk_invbasdoc,b.uint,b.assunit");
		str.append(" order by h.pk_outwhouse,ca.pk_cargdoc");
		
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
	
	private String[] keys = new String[]{"pk_outwhouse","ccargdocid","cinvmanid","cinvbasid","unitid","assunitid","nnum","nassnum"};
}
