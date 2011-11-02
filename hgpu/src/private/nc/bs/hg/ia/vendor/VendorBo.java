package nc.bs.hg.ia.vendor;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.hg.ia.vendor.BalFreezeVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class VendorBo {

    private BaseDAO dao = null;

    public BaseDAO getBaseDao() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }

    public BalFreezeVO[] getBySueprVO(Map<String, SuperVO> map) throws Exception {
        if (map == null || map.size() == 0)
            return null;
        int size = map.size();
        BalFreezeVO[] frees = new BalFreezeVO[size];
        UFDouble v_seq1 = UFDouble.ZERO_DBL;// --应付款总额
        UFDouble v_seq2 = UFDouble.ZERO_DBL;// --付款总额
        int index = 0;
        for (Map.Entry entry : map.entrySet()) {
            String str = PuPubVO.getString_TrimZeroLenAsNull(entry.getKey());
            String[] strs = str.split("&");
            v_seq1 = pro_qryMny("yf", PuPubVO.getString_TrimZeroLenAsNull(strs[0]), PuPubVO
                .getString_TrimZeroLenAsNull(strs[1]), PuPubVO.getString_TrimZeroLenAsNull(strs[2]));
            v_seq2 = pro_qryMny("fk", PuPubVO.getString_TrimZeroLenAsNull(strs[0]), PuPubVO
                .getString_TrimZeroLenAsNull(strs[1]), PuPubVO.getString_TrimZeroLenAsNull(strs[2]));
            Object o = map.get(str);
            BalFreezeVO vo = null;
            if (o != null) {
                vo = (BalFreezeVO) o;
                vo.setNcurbalance(v_seq1.sub(v_seq2));
            }

            frees[index] = vo;
            index++;
        };
        return frees;
    }

    public UFDouble getByString(String ccustbasid, String szxmid, String pk_corp) throws Exception {
        if (ccustbasid == null && szxmid == null)
            return null;
        UFDouble v_seq1 = UFDouble.ZERO_DBL;// --应付款总额
        UFDouble v_seq2 = UFDouble.ZERO_DBL;// --付款总额

        v_seq1 = pro_qryMny("yf", PuPubVO.getString_TrimZeroLenAsNull(ccustbasid), PuPubVO
            .getString_TrimZeroLenAsNull(szxmid), PuPubVO.getString_TrimZeroLenAsNull(pk_corp));
        v_seq2 = pro_qryMny("fk", PuPubVO.getString_TrimZeroLenAsNull(ccustbasid), PuPubVO
            .getString_TrimZeroLenAsNull(szxmid), PuPubVO.getString_TrimZeroLenAsNull(pk_corp));
        return PuPubVO.getUFDouble_NullAsZero(v_seq1.sub(v_seq2));

    }

    private UFDouble pro_qryMny(String djdl, String ksbm_cl, String szxmid, String pk_corp)
        throws DAOException {
        UFDouble mny = UFDouble.ZERO_DBL;
        String sql = null;
        if ("yf".equals(djdl)) {
            sql = "select sum(coalesce(b.dfybje,0)) ";
        } else if ("fk".equals(djdl)) {
            sql = "select sum(coalesce(b.jfybje,0)) ";
        }
        sql += " from arap_djzb h inner join arap_djfb b  on h.vouchid = b.vouchid where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 "
            + " and h.djdl = '" + djdl + "' and b.hbbm ='" + ksbm_cl + "' and h.dwbm ='" + pk_corp + "'";
        // and h.djzt = 2
        if ("yf".equals(djdl))
            sql = sql + " and h.zgyf =0";
        if (PuPubVO.getString_TrimZeroLenAsNull(szxmid) != null && !"null".equals(szxmid))
            sql += " and b.szxmid= '" + szxmid + "'";
        else
            sql += " and b.szxmid is null ";// 不是磨账
        mny = PuPubVO.getUFDouble_NullAsZero(getBaseDao().executeQuery(sql, new ColumnProcessor()));
        return mny;
    }
}
