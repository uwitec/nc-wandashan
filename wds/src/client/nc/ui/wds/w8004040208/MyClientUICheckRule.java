package nc.ui.wds.w8004040208;

import java.awt.Container;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;
import nc.vo.trade.checkrule.VOChecker;


/**
 * <b> 前台校验类 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
 * </p>
 *
 *
 * @author author
 * @version tempProject 1.0
 */

public class MyClientUICheckRule extends BeforeActionCHK  implements ICheckRules,IUniqueRules,ICheckRules2{

	/**
	 * 返回表头唯一规则，仅用于后台检查。
	 */
	public IUniqueRule[] getHeadUniqueRules() {
		return null;
	}

	/**
	 * 返回表体唯一规则，仅用于前台检查。
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {
		IUniqueRule[] unique = null;		
		
		
		return unique;			
	}
	
	/**
	 * 返回特殊检查类。 如果VOChecker不能满足要求，可以用此类来检查。
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}
	
	/**
	 * 是否允许表体为空
	 */
	public boolean isAllowEmptyBody(String tablecode) {
			return true;
	}
	
		public ICheckRule[] getHeadCheckRules() {
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
		return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		return null;
	}

	public String[] getHeadIntegerField() {
		return null;
	}

	public String[] getHeadUFDoubleField() {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return null;
	}
	
	public void runBatchClass(Container parent, String billType, String actionName, AggregatedValueObject[] vos, Object[] obj) throws Exception {
	}


	public void runClass(Container parent, String billType, String actionName, AggregatedValueObject vo, Object obj) throws Exception {
		if(actionName.equals(IPFACTION.SAVE))
		{	
			if(!VOChecker.check(vo,this ))
				throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		}
		
	}


}
