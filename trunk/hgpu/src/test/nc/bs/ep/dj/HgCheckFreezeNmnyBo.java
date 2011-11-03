package nc.bs.ep.dj;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class HgCheckFreezeNmnyBo {

	private BaseDAO dao = null;

	private BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void checkNmny(DJZBVO djzbvo) throws BusinessException {
		UFDouble v_seq1 = UFDouble.ZERO_DBL;// --应付款总额
		UFDouble v_seq2 = UFDouble.ZERO_DBL;// --付款总额 = 已付款总额 + 预付款总额
		UFDouble v_seq3 = UFDouble.ZERO_DBL;// --冻结总额
		UFDouble v_seq4 = UFDouble.ZERO_DBL;// 当前金额

		if (djzbvo == null)
			throw new BusinessException("传入数据为空");
		DJZBHeaderVO head = (DJZBHeaderVO) djzbvo.getParentVO();
		if (head == null)
			throw new BusinessException("传入数据为空");
		
		if (PuPubVO.getUFBoolean_NullAs(head.getPrepay(), UFBoolean.FALSE).booleanValue())// 预付款不校验
			return;

		String djdl = head.getDjdl();
		String pk_corp = head.getDwbm();
		String hbbmb = head.getHbbm();
		String szxmid = head.getSzxmid();
		String deptid = head.getDeptid();
		UFDouble mny  = PuPubVO.getUFDouble_NullAsZero(head.getYbje());
		
		String str1=deptid+szxmid+hbbmb;
		if (!"fk".equalsIgnoreCase(djdl))
			return;

		if (head.getPrimaryKey() != null) {
			Object[] os =pro_qryDjInfo(head.getPrimaryKey());
			if(os !=null && os.length>0){
				UFDouble oldmny =PuPubVO.getUFDouble_NullAsZero(os[0]);
				String oldszxmid = PuPubVO.getString_TrimZeroLenAsNull(os[1]);
				String olddeptid = PuPubVO.getString_TrimZeroLenAsNull(os[2]);
				String oldhbbmb = PuPubVO.getString_TrimZeroLenAsNull(os[3]);
				String str2=olddeptid+oldszxmid+oldhbbmb;
				if(str1.equals(str2))
				    mny = mny.sub(oldmny);
			}
		}

		v_seq1 = pro_qryMny("yf", hbbmb, szxmid, pk_corp,deptid);
		v_seq2 = pro_qryMny("fk", hbbmb, szxmid, pk_corp,deptid);
		v_seq3 = pro_qryFreezeMny(hbbmb, szxmid, pk_corp,deptid);
		v_seq4 = mny;
		UFDouble v_seq5=v_seq1.sub(v_seq2).sub(v_seq3).sub(v_seq4);
		if (v_seq5.doubleValue() < 0)
			throw new BusinessException("超应付余额,超"+v_seq5);
	}

	private UFDouble pro_qryMny(String djdl, String ksbm_cl, String szxmid,
			String pk_corp,String deptid) throws DAOException {
		UFDouble mny = UFDouble.ZERO_DBL;
		String sql = null;
		if ("yf".equals(djdl)) {
			sql = "select sum(coalesce(b.dfybje,0)) ";
		} else if ("fk".equals(djdl)) {
			sql = "select sum(coalesce(b.jfybje,0)) ";
		}
		sql += " from arap_djzb h inner join arap_djfb b  on h.vouchid = b.vouchid where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 "
				+ " and h.djdl = '"+ djdl+ "' and b.hbbm ='"+ ksbm_cl+ "' and h.dwbm ='" + pk_corp + "' and  b.deptid = '"+deptid+"'";
		// and h.djzt = 2
		if ("yf".equals(djdl))
			sql = sql + " and h.zgyf =0";
		if (PuPubVO.getString_TrimZeroLenAsNull(szxmid) != null
				&& !"null".equalsIgnoreCase(PuPubVO.getString_TrimZeroLenAsNull(szxmid)))
			sql += " and b.szxmid= '" + szxmid + "'";
		else
			sql += " and (b.szxmid != '0001A11000000000YK2X' or b.szxmid is null)";// 不是磨账
		mny = PuPubVO.getUFDouble_NullAsZero(getBaseDAO().executeQuery(sql,new ColumnProcessor()));
		return mny;
	}

	private UFDouble pro_qryFreezeMny(String ksbm_cl, String szxmid,
			String pk_corp,String deptid) throws DAOException {
		UFDouble mny = UFDouble.ZERO_DBL;

		String sql = " select nfreamount from hg_balfreeze where isnull(dr,0)=0 and ccustbasid  ='"
				+ ksbm_cl+ "' and pk_corp ='"+ pk_corp+ "' and fisrozen ='Y' and vdef1 = '"+deptid+"'";
		if (PuPubVO.getString_TrimZeroLenAsNull(szxmid) != null
				&& !"null".equalsIgnoreCase(PuPubVO
						.getString_TrimZeroLenAsNull(szxmid)))
			sql += " and szxmid= '" + szxmid + "'";
		else
			sql += " and (szxmid != '0001A11000000000YK2X' or szxmid is null)";// 不是磨账
		mny = PuPubVO.getUFDouble_NullAsZero(getBaseDAO().executeQuery(sql,
				new ColumnProcessor()));
		return mny;
	}
	
	private Object[] pro_qryDjInfo(String primaryKey) throws DAOException {
		 Object[] os =null;
		String sql = " select z.ybje,b.szxmid,b.deptid,b.hbbm  from arap_djzb z join arap_djfb b on b.vouchid=z.vouchid where isnull(z.dr,0)=0 and  isnull(b.dr,0)=0 and  z.vouchid ='"+primaryKey+"'";
		ArrayList al = (ArrayList)getBaseDAO().executeQuery(sql,new ArrayListProcessor());
		 if(al!=null && al.size()>0){
			 Object o = al.get(0);
			 if(o!=null){
				 os=(Object[])o;
			 }
		 }
		 return os;
	}
}
