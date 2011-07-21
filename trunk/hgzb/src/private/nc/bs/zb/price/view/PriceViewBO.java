package nc.bs.zb.price.view;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class PriceViewBO {
	
	private BaseDAO dao = null;
	public BaseDAO getBaseDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @author zhw
	 * @说明：（鹤岗矿业） 查询报价信息
	 * @param whereSql
	 * @param cl
	 * @return 
	 * @throws BusinessException
	 */
	public SubmitPriceVO[] queryDataForInfo(String whereSql, ClientLink cl) throws BusinessException {
		
		StringBuffer strb = new StringBuffer();
		
		StringBuffer strtemp = new StringBuffer();//临时供应商
		StringBuffer strven = new StringBuffer();//供应商
		StringBuffer str = new StringBuffer();//相关信息
		
		StringBuffer strall = new StringBuffer();//相关信息
		StringBuffer stralltemp = new StringBuffer();//相关信息
		
		
		strb.append(" select ");

		SubmitPriceVO price = new SubmitPriceVO();
		String[] barrnames = price.getAttributeNames();

		for (String name : barrnames) {
			if("naverageprice".equalsIgnoreCase(name) || "nmarketprice".equalsIgnoreCase(name) 
					|| "nplanprice".equalsIgnoreCase(name) || "nmarkprice".equalsIgnoreCase(name) )
				continue;
			     strb.append("sp."+ name+",");
		}
		
		str.append(" join zb_bidding_h h on h.cbiddingid =sp.cbiddingid ");
		str.append(" join bd_invbasdoc ibas on ibas.pk_invbasdoc =sp.cinvbasid");
		str.append(" join zb_biddingtimes s on s.cbiddingtimesid =sp.ccircalnoid");
		
		
		if (PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null &&( whereSql.contains("sp.temp") || whereSql.contains("sp.cvendorid"))){
			strb.append(" h.vbillno,ibas.invcode,s.vname from " + price.getTableName()+" sp");
			strall.append(strb).append(str);
			if(whereSql.contains("sp.temp") && whereSql.contains("sp.cvendorid")){
				whereSql=whereSql.replace("sp.cvendorid", "( sp.cvendorid");
				whereSql=whereSql.replace("AND sp.temp", "OR sp.cvendorid");
				whereSql=whereSql.replace(")", "))");
			}else{
				whereSql=whereSql.replace("sp.temp", "sp.cvendorid");
			}
			strall.append(" where isnull(sp.dr,0)=0 and isnull(h.dr,0)=0 and isnull(ibas.dr,0)=0 and isnull(s.dr,0)=0");
			strall.append(" and " + whereSql);
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
			   strall.append(" and zh.reserve1 = '" +s+"'");
			strall.append(" order by h.vbillno,sp.cvendorid,ibas.invcode,s.vname desc ");			
		}else {
			strtemp.append(" join bd_cubasdochg g on g.ccubasdochgid =sp.cvendorid ");
			strtemp.append(" where isnull(sp.dr,0)=0 and isnull(h.dr,0)=0 and isnull(g.dr,0)=0 and isnull(ibas.dr,0)=0 and isnull(s.dr,0)=0");
			strven.append(" join bd_cumandoc man on man.pk_cumandoc =sp.cvendorid ");
			strven.append(" join bd_cubasdoc bas on bas.pk_cubasdoc =man.pk_cubasdoc");
			strven.append(" where isnull(sp.dr,0)=0 and isnull(h.dr,0)=0 and isnull(man.dr,0)=0 and isnull(bas.dr,0)=0 and isnull(ibas.dr,0)=0 and isnull(s.dr,0)=0");
			stralltemp.append(strb).append("h.vbillno,g.vbillno,ibas.invcode,s.crowno from " + price.getTableName()+" sp").append(str).append(strtemp);
			if(PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null)
			  stralltemp.append(" and " + whereSql);
			String s =ZbPubTool.getParam();
			if(s!=null &&!"".equals(s))
				stralltemp.append(" and h.reserve1 = '" +s+"'");
			stralltemp.append(" order by h.vbillno,g.vbillno,ibas.invcode,s.crowno");	
			strall.append(strb).append("h.vbillno,bas.custcode,ibas.invcode,s.crowno from " + price.getTableName()+" sp").append(str).append(strven);
			if(PuPubVO.getString_TrimZeroLenAsNull(whereSql) != null)
			   strall.append(" and " + whereSql);
			if(s!=null &&!"".equals(s))
			   strall.append(" and h.reserve1 = '" +s+"'");
			strall.append(" order by h.vbillno,bas.custcode,ibas.invcode,s.vname desc ");
		}
		 List ldataall=null;
		if(PuPubVO.getString_TrimZeroLenAsNull(strall.toString())!=null)
		      ldataall = (List) getBaseDao().executeQuery(strall.toString(),new BeanListProcessor(SubmitPriceVO.class));
		List ldataalltemp = null;
		if(PuPubVO.getString_TrimZeroLenAsNull(stralltemp.toString())!=null)
		   ldataalltemp=(List) getBaseDao().executeQuery(stralltemp.toString(),new BeanListProcessor(SubmitPriceVO.class));

		if (ldataall == null || ldataall.size() == 0) {
			if(ldataalltemp ==null ||ldataalltemp.size() ==0)
				return null;
			ldataall=ldataalltemp;
		}else{
			if(ldataalltemp!=null && ldataalltemp.size()>0){
				int size =ldataalltemp.size();
				for(int i=0;i<size;i++){
					if(ldataalltemp.get(i) !=null)
					   ldataall.add(ldataalltemp.get(i));
				}
			}	
		}
		SubmitPriceVO[] datas = (SubmitPriceVO[]) ldataall.toArray(new SubmitPriceVO[0]);
	//	VOUtil.ascSort(datas,new String[]{"cbiddingid","cvendorid","cinvmanid"});
		SubmitPriceVO.sortSubmitPriceVO(datas);
		return datas;
	}
	
}
