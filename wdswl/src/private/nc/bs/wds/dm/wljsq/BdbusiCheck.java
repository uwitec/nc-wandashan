package nc.bs.wds.dm.wljsq;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.wljsq.PeriodSettingVO;
/**
 * 结帐期 后台校验类
 * @author Administrator
 *xjx  add
 */
public class BdbusiCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		PeriodSettingVO ivo = (PeriodSettingVO) vo.getParentVO();  //pk_wds_corpseal
		String corp= ivo.getPk_corp();
		String primary= PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		//新增保存和 修改保存 区分校验
		if(primary == null){//新增校验
			StringBuffer sql = new StringBuffer();
			sql.append(" select datavale from wds_periodsetting_h ");
			sql.append(" where isnull(dr,0) =0 ");
			sql.append(" and pk_corp='"+corp+"'");
			Object value = getDao().executeQuery(sql.toString(), new ColumnProcessor());
		//	int datavale= PuPubVO.getInteger_NullAs(value, 0);
	        if( value!=null ){
	        	throw new BusinessException("该结帐期已经存在");
	        }
		}
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub

	}

}
