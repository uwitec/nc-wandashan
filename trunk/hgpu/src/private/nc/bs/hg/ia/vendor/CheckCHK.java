package nc.bs.hg.ia.vendor;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.trade.comchkref.CheckRef;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.hg.ia.vendor.BalFreezeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBDACTION;

public class CheckCHK implements IBDBusiCheck, IBDACTION {

    CheckRef check = null;

    private BaseDAO dao = null;

    public BaseDAO getBaseDao() {
        if (dao == null) {
            dao = new BaseDAO();
        }
        return dao;
    }

    /**
     * CostProjectBsBackCHK 构造子注释
     */
    public CheckCHK() {
        super();

    }

    /**
     * 校验
     */
    public void check(int intBdAction, AggregatedValueObject vo, Object userObj) throws Exception {
        switch (intBdAction) {
            case DELETE:
                onCheckDelete((HYBillVO) vo);
                break;
            case SAVE:
                onCheckSave((HYBillVO) vo);
                break;
        }

    }

    public void dealAfter(int intBdAction, AggregatedValueObject billVo, Object userObj) throws Exception {

    }

    private void onCheckSave(HYBillVO vo) throws Exception {
        BalFreezeVO[] bvos = (BalFreezeVO[]) vo.getChildrenVO();

        if (bvos == null || bvos.length == 0)
            return;

        BalFreezeVO bvo = bvos[0];
        String custbasid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCcustbasid());
        String custmanid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCcustmanid());
        String szxmid = PuPubVO.getString_TrimZeroLenAsNull(bvo.getSzxmid());
        String pk_corp = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_corp());
        String vdef1 = PuPubVO.getString_TrimZeroLenAsNull(bvo.getVdef1());
        String pk_balfreeze = PuPubVO.getString_TrimZeroLenAsNull(bvo.getPk_balfreeze());

        checkeunique(custbasid, custmanid, szxmid, pk_corp, vdef1, pk_balfreeze);
        UFDouble temp = PuPubVO.getUFDouble_NullAsZero(bvo.getNfreamount()).sub(
            PuPubVO.getUFDouble_NullAsZero(getByString(custbasid, szxmid, pk_corp, vdef1)));

        if (temp.compareTo(UFDouble.ZERO_DBL) > 0)
            throw new BusinessException("本次冻结超供应商余额:超出" + temp + "元");
    }

    private void onCheckDelete(HYBillVO vo) throws Exception {

    }

    public UFDouble getByString(String ccustbasid, String szxmid, String pk_corp, String vdef1) throws Exception {
        if (ccustbasid == null && szxmid == null)
            return null;
        UFDouble v_seq1 = UFDouble.ZERO_DBL;// --应付款总额
        UFDouble v_seq2 = UFDouble.ZERO_DBL;// --付款总额
        UFDouble v_seq3 = UFDouble.ZERO_DBL;// --冻结总额

        v_seq1 = pro_qryMny("yf", PuPubVO.getString_TrimZeroLenAsNull(ccustbasid), PuPubVO
            .getString_TrimZeroLenAsNull(szxmid), PuPubVO.getString_TrimZeroLenAsNull(pk_corp),
            PuPubVO.getString_TrimZeroLenAsNull(vdef1));
        v_seq2 = pro_qryMny("fk", PuPubVO.getString_TrimZeroLenAsNull(ccustbasid), PuPubVO
            .getString_TrimZeroLenAsNull(szxmid), PuPubVO.getString_TrimZeroLenAsNull(pk_corp),
            PuPubVO.getString_TrimZeroLenAsNull(vdef1));
        // v_seq3 = pro_qryFreezeMny(ccustbasid, szxmid, pk_corp,vdef1);
        return PuPubVO.getUFDouble_NullAsZero(v_seq1.sub(v_seq2).sub(v_seq3));

    }

    private void checkeunique(String custbasid, String custmanid, String szxmid, String pk_corp,
                              String vdef1, String pk_balfreeze) throws BusinessException {
        StringBuffer str = new StringBuffer();
        str.append("select count(0) from hg_balfreeze where ");
        str.append(" ccustbasid = '" + custbasid + "' and ccustmanid = '" + custmanid + "'");
        if (szxmid == null)
            str.append(" and szxmid is null ");
        else
            str.append(" and szxmid = '" + szxmid + "'");
        str.append(" and isnull(dr,0) = 0  and pk_corp = '" + pk_corp + "' and vdef1 = '" + vdef1 + "'");

        if (pk_balfreeze != null)
            str.append(" and pk_balfreeze <> '" + pk_balfreeze + "' ");

        String len = PuPubVO.getString_TrimZeroLenAsNull(getBaseDao().executeQuery(str.toString(),
            HgBsPubTool.COLUMNPROCESSOR));

        if (Integer.parseInt(len) > 0) {
            throw new BusinessException(" 该供应商+部门已经存在");
        }
    }

    private UFDouble pro_qryMny(String djdl, String ksbm_cl, String szxmid, String pk_corp,String vdef1)
        throws DAOException {
        UFDouble mny = UFDouble.ZERO_DBL;
        String sql = null;
        if ("yf".equals(djdl)) {
            sql = "select sum(coalesce(b.dfybje,0)) ";
        } else if ("fk".equals(djdl)) {
            sql = "select sum(coalesce(b.jfybje,0)) ";
        }
        sql += " from arap_djzb h inner join arap_djfb b  on h.vouchid = b.vouchid where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 "
            + " and h.djdl = '" + djdl + "' and b.hbbm ='" + ksbm_cl + "' and h.dwbm ='" + pk_corp + "' and b.deptid = '"+vdef1+"'";
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
