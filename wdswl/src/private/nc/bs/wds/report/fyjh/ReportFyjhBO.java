package nc.bs.wds.report.fyjh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.report.ReportBaseVO;

public class ReportFyjhBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	

	/**
	 * @author yf
	 * 发运计划月统计报表-- 录入月计划和追加计划的合并
	 */
	public ReportBaseVO[] doQuery(String whereSql) throws BusinessException{
	
		StringBuffer str = new StringBuffer();
		
		str.append(" select ");
		str.append(" sp.pk_outwhouse pk_outwhouse, ");
		str.append(" sp.pk_inwhouse pk_inwhouse, ");
		str.append(" win.pk_invbasdoc pk_invbasdoc, ");
		str.append(" sum(sb.nplannum) nplannum, ");
		str.append(" sum(sb.nassplannum) nassplannum, ");
		str.append(" sum(sb.ndealnum) ndealnum, ");
		str.append(" sum(sb.ndealnum / bc.mainmeasrate) nassdealnum ");

		str.append(" from wds_sendplanin sp ");
		str.append(" inner join wds_sendplanin_b sb on sb.pk_sendplanin = sp.pk_sendplanin ");
		str.append(" inner join wds_invbasdoc win on sb.pk_invbasdoc = win.pk_invbasdoc ");
		str.append(" inner join bd_convert bc on bc.pk_invbasdoc = sb.pk_invbasdoc ");
		
		str.append(" where ");
		if(PuPubVO.getString_TrimZeroLenAsNull(whereSql)!=null){
			str.append(whereSql);
		}
		str.append(" and isnull(sp.dr, 0) = 0 and isnull(win.dr, 0) = 0 and isnull(sb.dr, 0) = 0");

		
		str.append(" group by sp.pk_outwhouse, sp.pk_inwhouse, win.pk_invbasdoc ");
		str.append(" order by sp.pk_outwhouse, sp.pk_inwhouse, win.pk_invbasdoc ");
		List ldata = (List)getDao().executeQuery(str.toString(), WdsPubResulSetProcesser.MAPLISTROCESSOR);
		if(ldata == null || ldata.size() == 0){
			return null;
		}
		Map dataMap = null;
		int len = ldata.size();
		
		List arr = new ArrayList();
		ReportBaseVO vo = null;
		for(int i=0;i<len;i++){
			vo = new ReportBaseVO();
			dataMap = (Map)ldata.get(i);
			//去掉 数量为空或0的结果集
			if(PuPubVO.getUFDouble_ZeroAsNull(dataMap.get("nplannum")) == null
					&&PuPubVO.getUFDouble_ZeroAsNull(dataMap.get("nassplannum")) == null
					&&PuPubVO.getUFDouble_ZeroAsNull(dataMap.get("ndealnum")) == null
					&&PuPubVO.getUFDouble_ZeroAsNull(dataMap.get("nassdealnum")) == null){
				continue;
			}
			for(String key:keys){
				vo.setAttributeValue(key, dataMap.get(key));
			}
			arr.add(vo);
			//vos[i] = vo;
		}
		if(null == arr || 0 == arr.size())
			return null;
		int length = arr.size();
		ReportBaseVO[] vos = new ReportBaseVO[length];
		for (int i = 0; i < length; i++) {
			vos[i] = (ReportBaseVO) arr.get(i);
		}
		return vos;
	}
	
	private String[] keys = new String[]{"pk_outwhouse","pk_inwhouse","pk_invbasdoc","nplannum","nassplannum","ndealnum","nassdealnum"};
}
