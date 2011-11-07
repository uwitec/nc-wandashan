package nc.ui.wds.dm.corpseal;

import java.awt.Container;

import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.dm.corpseal.CorpsealVO;
/**
 * 客户公司图章 前台校验类
 * @author Administrator
 *xjx  add
 */
public class BeforeActionCheck extends BeforeActionCHK {

	public void runBatchClass(Container parent, String billType,
			String actionName, AggregatedValueObject[] vos, Object[] obj)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void runClass(Container parent, String billType, String actionName,
			AggregatedValueObject vo, Object obj) throws Exception {

		//校验表体发货数量按照前开后闭区间原则不能存在交叉
		if (vo == null) {
			return;
		}
		//表头数据校验
		CorpsealVO head=(CorpsealVO) vo.getParentVO();
		String corpseal=PuPubVO.getString_TrimZeroLenAsNull(head.getPk_cubasdoc());
		if(corpseal==null){
			throw new BusinessException("客户公司图章不能为空");
		}
	}
}
