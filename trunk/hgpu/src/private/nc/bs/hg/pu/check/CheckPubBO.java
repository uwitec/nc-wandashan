package nc.bs.hg.pu.check;

import nc.bs.dao.BaseDAO;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class CheckPubBO {
	private BaseDAO dao = null;
	private BaseDAO getBaseDAO() {
		if (dao == null) {
			 dao = new BaseDAO();
		}
		return dao;
	}
	
	public FUNDSETVO onSave(FUNDSETVO[] vos,boolean flag) throws Exception{
		if(vos==null || vos.length==0)
			throw new BusinessException("传入数据出错");
		FUNDSETVO setvo =null;
		if(flag){
			String key =vos[0].getPrimaryKey();
			onCheckSave(vos[0]);
			if(key==null){
				key =getBaseDAO().insertVO(vos[0]);
			}else{
				setvo =(FUNDSETVO)getBaseDAO().retrieveByPK(FUNDSETVO.class,key);
				//checkData(setvo.getNfund(), setvo.getNlockfund(), setvo.getNactfund());
				checkData(vos[0].getNfund(), setvo.getNlockfund(), setvo.getNlockfund());
				FUNDSETVO setvo1 = new FUNDSETVO();
				String[] strs = setvo1.getAttributeNames();
				String[] strl = new String[strs.length-3];
				int index =0;
				for(String str:strs){
					if("ts".equalsIgnoreCase(str) ||"nactfund".equalsIgnoreCase(str) ||"nlockfund".equalsIgnoreCase(str) )
						continue;
					strl[index]=str;
					index++;
				}
				//String[] str= new String[]{"pk_corp","vdef3","",""};
				getBaseDAO().updateVOArray(vos,strl);
			}
			 setvo =(FUNDSETVO)getBaseDAO().retrieveByPK(FUNDSETVO.class,key);
			 checkData(setvo.getNfund(),setvo.getNlockfund(),setvo.getNactfund());
			return setvo;
		}else{
			int len = vos.length;
			String[] strs = new String[len];
			int index =0;
			for(FUNDSETVO vo:vos){
				if(!PuPubVO.getUFDouble_NullAsZero(vo.getNactfund()).add(PuPubVO.getUFDouble_NullAsZero(vo.getNlockfund())).equals(UFDouble.ZERO_DBL))
					throw new BusinessException("存在着预扣或实扣不能删除");
					strs[index]=vo.getPrimaryKey();
				    index++;
			}
			String sql ="update "+vos[0].getTableName()+" set dr =1  where pk_fundset in "+HgPubTool.getSubSql(strs);
			getBaseDAO().executeUpdate(sql);
			return null;
		}
		
	}
	
	private void checkData(UFDouble nfund,UFDouble nlock,UFDouble nfact) throws BusinessException{
		nfund=PuPubVO.getUFDouble_NullAsZero(nfund);
		nlock = PuPubVO.getUFDouble_NullAsZero(nlock);
		nfact =PuPubVO.getUFDouble_NullAsZero(nfact);
		if((nfund.sub(nlock).sub(nfact)).compareTo(UFDouble.ZERO_DBL)<0)
			throw new BusinessException("资金不能小于预扣实扣之和");
	}
	
	private void onCheckSave(FUNDSETVO bvo) throws Exception {
		
		if (bvo == null) 
			return;
	
		String cdeptid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCdeptid());
		String pk_corp = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_corp());
		String imonth=PuPubVO.getString_TrimZeroLenAsNull(bvo.getImonth());
		String cyear =PuPubVO.getString_TrimZeroLenAsNull(bvo.getCyear());
		String ifundtype =PuPubVO.getString_TrimZeroLenAsNull(bvo.getIfundtype());
		String vdef1 = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVdef1());
		String vdef2 = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVdef2());
		String pk_fundset= PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_fundset());
		BaseDAO dao = new BaseDAO();
		StringBuffer  str= new StringBuffer();
		str.append("select count(*) from hg_fundset where ");
		str.append(" (imonth = " + imonth + " and cyear = '" + cyear
				+ "' and ifundtype = '" + ifundtype + "') and isnull(dr,0) = 0 " +
						"and vdef2 = '"+vdef2+"'");
		
		if(pk_fundset!=null)
			str.append(" and pk_fundset <> '" + pk_fundset + "' ");
		
		if(pk_corp!=null)
			str.append(" and pk_corp = '" + pk_corp + "' ");
		else
			str.append(" and (pk_corp = '' or pk_corp is null) ");
		
		if(cdeptid!=null)
			str.append(" and cdeptid = '" + cdeptid + "' ");
		else
			str.append(" and (cdeptid = '' or cdeptid is null) ");
		
		if(vdef1!=null)
			str.append(" and vdef1 = '" + vdef1 + "' ");
		else
			str.append(" and (vdef1 = '' or vdef1 is null) ");
		
		String len = PuPubVO.getString_TrimZeroLenAsNull(dao.executeQuery(str.toString(), HgBsPubTool.COLUMNPROCESSOR));
		
		if(Integer.parseInt(len)>0){
			if(vdef1!=null)
				throw new BusinessException("客户,年度和月份不能重复！");
			
			throw new BusinessException(" 公司,部门,年度和月份不能重复！");
		}
	}
}
