package nc.ui.zb.pub;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import nc.bs.logging.Logger;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.zb.pub.JoinTableInfo;

/**
 * <p>
 * 多子表的业务委托类。
 * <p>
 * 实现功能：
 * <li>根据表头查询所有表体；</li>
 * <li>主子表联查；</li>
 * 
 * @author twh
 * @date 2007-1-19 下午04:10:20
 * @version V5.0
 * @主要的类使用：
 *          <ul>
 *          <li><b>如何使用该类：</b>通过实现本类的子类可以默认支持根据表头查询所有表体，以及多子表的主子表联查功能。</li>
 *          <li><b>是否线程安全：</b></li>
 *          <li><b>并发性要求：</b></li>
 *          <li><b>使用约束：</b> 本抽象类的实现类必须实现接口IMultiChildVOInfo和IMultiChildQueryInfo，指定有关的VO信息和查询模板相关信息。</li>
 *          <li><b>其他：</b></li>
 *          </ul>
 *          </p>
 *          <p>
 * @已知的BUG：
 *          <ul>
 *          <li></li>
 *          </ul>
 *          </p>
 *          <p>
 * @修改历史：
 */
public abstract class MultiChildBusinessDelegator extends BusinessDelegator {

	public MultiChildBusinessDelegator() {
		super();
	}

	/**
	 * 多子表界面VO信息
	 * 
	 * @return twh (2007-1-22 上午09:55:24)<br>
	 */
	public abstract IMultiChildVOInfo getMultiChildVoInfo();

	/**
	 * 多子表联查信息。
	 * 
	 * @return twh (2007-1-22 上午10:37:44)<br>
	 */
	public abstract IMultiChildQueryInfo getMultiChildQueryInfo();

	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		Hashtable<String, SuperVO[]> dataHas = new Hashtable<String, SuperVO[]>();
		try {
			for (int i = 0; i < tableCodes.length; i++) {
				String clsName = getMultiChildVoInfo().getVoClassNameByTableCode(tableCodes[i]);
				if (clsName != null) {
					Class voClass = Class.forName(clsName);
					SuperVO vo = (SuperVO) voClass.newInstance();
					String strWhere = vo.getParentPKFieldName() + "='" + key + "' and isnull(dr,0)=0 ";
					SuperVO[] vos = queryByCondition(voClass, strWhere);
					if (vos != null && vos.length > 0)
						dataHas.put(tableCodes[i], vos);
				}
			}
		} catch (Exception e) {
			Logger.error("加载表体信息出错！", e);
			throw new Exception("加载表体信息出错！", e);
		}
		return dataHas;
	}

	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType, String strWhere) throws Exception {
		// 增加单据类型管理中定义的固定查询条件。
		BilltypeVO billVo = PfUIDataCache.getBillType(strBillType);
		if (billVo.getWherestring() != null && billVo.getWherestring().length() != 0) {
			if (strWhere != null)
				strWhere = strWhere + " and (" + billVo.getWherestring() + ")";
			else
				strWhere = billVo.getWherestring();
		}

		// 处理主子表关联查询
		return ClientHelper.queryVOWithJoinTable(headClass, getJoinTableInfo(strWhere), strWhere);
	}

	/**
	 * 表关联信息。
	 * 
	 * @param strWhere
	 * @return
	 * @throws Exception
	 * @author: twh (2007-4-11 下午04:39:36)<br>
	 */
	protected JoinTableInfo[] getJoinTableInfo(String strWhere) throws Exception {
		JoinTableInfo[] jtis = null;
		List<JoinTableInfo> lstJTI = new ArrayList<JoinTableInfo>();
		for (int i = 0; i < getMultiChildVoInfo().getTableCodes().length; i++) {
			String tblCode = getMultiChildVoInfo().getTableCodes()[i];
			// 表编码对应的别名
			String alias = getMultiChildQueryInfo().getAliasByTableCode(tblCode);
			// 表编码对应的VO类名
			String voClsName = getMultiChildVoInfo().getVoClassNameByTableCode(tblCode);
			// 查询条件中有该表对应的别名吗？
			if (alias != null && strWhere.indexOf(alias) >= 0 && voClsName != null) {
				JoinTableInfo jti = new JoinTableInfo(Class.forName(voClsName));
				jti.setAlias(alias);
				jti.setInnerjoin(false);
				// 子表与主表关联的字段
				jti.setJoinFieldName(((SuperVO) Class.forName(voClsName).newInstance()).getParentPKFieldName());
				// add to list
				lstJTI.add(jti);
			}
		}
		if (lstJTI.size() > 0)
			jtis = (JoinTableInfo[]) (lstJTI.toArray(new JoinTableInfo[0]));
		return jtis;
	}

	/**
	 * 调用平台默认的queryHeadAllData方法。
	 * 
	 * @param headClass
	 * @param strBillType
	 * @param strWhere
	 * @return
	 * @throws Exception
	 *             twh (2007-1-22 上午10:33:46)<br>
	 */
	public SuperVO[] queryHeadAllDataOrig(Class headClass, String strBillType, String strWhere) throws Exception {
		return super.queryHeadAllData(headClass, strBillType, strWhere);
	}

}
