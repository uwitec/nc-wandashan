package nc.bs.zmpub.pub.tool.mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.SystemException;
import nc.bs.zmpub.pub.report.ReportDMO;
import nc.bs.zmpub.pub.tool.SingleVOChangeDataBsTool;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.zmpub.pub.report.ReportBaseVO;

/**
 * 现存量修复工具类
 * 
 * @author mlr 由于计划台账BO没有按规则实现 所以 设置变化量的算法 和 最小维度合并 以及 更新计划量的算法 在本类实现
 */
public abstract class AccountModBOTool {
	private ReportDMO dmo = null;

	public ReportDMO getDMO() throws SystemException, NamingException {
		if (dmo == null) {
			dmo = new ReportDMO();
		}
		return dmo;
	}

	private BaseDAO dao = null;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public AccountModBOTool() {
		init();
	}

	protected void init() {
	}

	/**
	 * 获得清空现存量的语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getClearSql() throws Exception;

	/**
	 * 获取 单据类型 到 单据状态的对应关系 目前支持两种状态 boolean[] 数组对一个两种状态 索引如下： 0 --->是否查询保存
	 * 1---->是否查询审批
	 * 
	 * @return Map<String,boolean[]>
	 * @throws Exception
	 */
	public abstract Map<String, boolean[]> getTypetostatus() throws Exception;

	/**
	 * 获得各个单据类型 对应的 现存量设置规则
	 * 
	 * @return Map<String,boolean[]>
	 * @throws Exception
	 */
	public abstract Map<String, UFBoolean[]> getTypetosetnum() throws Exception;

	/**
	 * 获得影响现存量的 需要调整设置的字段名字数组
	 * 
	 * @return Map<String,boolean[]>
	 * @throws Exception
	 */
	public abstract String[] getSetNumFields() throws Exception;

	/**
	 * 单据类型 与 数据交换类 对应map
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, String> getTypetoChangeClass() throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getQuerySql(String whereSql) throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getQuerySql1(String whereSql) throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getQuerySql() throws Exception;

	/**
	 * 获得查询语句
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getQuerySql1() throws Exception;

	/**
	 * 获得单据类型字段名字
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getBillTypeName() throws Exception;

	/**
	 * 获得单据状态字段名字
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getBillTypeStatusName() throws Exception;

	/**
	 * 获得现存量类名全路径
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String getNumClass() throws Exception;

	/**
	 * 获得现存量最小维度数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract String[] getUnpk() throws Exception;

	/**
	 * @author mlr
	 * @说明：（鹤岗矿业）将矿级单据数据转换为台账参量 2011-9-14下午03:24:36
	 * @param bill
	 *            矿级单据
	 * @return
	 * @throws Exception
	 */
	public void setAccountNum(SuperVO[] vos, UFBoolean[] isNumCirl)
			throws Exception {
		if (vos == null || vos.length == 0)
			return;
		for (int j = 0; j < vos.length; j++) {
			String[] fields = getSetNumFields();
			for (int i = 0; i < fields.length; i++) {
				if (isNumCirl[i] == null) {
					vos[j].setAttributeValue(fields[i], new UFDouble(0));
				} else if (isNumCirl[i].booleanValue() == true) {
					UFDouble num = PuPubVO.getUFDouble_NullAsZero(vos[j]
							.getAttributeValue(fields[i]));
					vos[j].setAttributeValue(fields[i], num.multiply(-1));
				}
			}
		}
	}

	/**
	 * 查询所有的需要修复的 现存量的vo
	 * 
	 * @return
	 */
	public ReportBaseVO[] queryModVO() throws Exception {
		String sql = getQuerySql();
		String[] sql11 = getQuerySql1();
		ReportBaseVO[] mvos = null;
		List<ReportBaseVO> lists = new ArrayList<ReportBaseVO>();
		if (sql != null && sql.length() != 0) {
			mvos = getDMO().queryVOBySql(sql);
		} else {
			List<ReportBaseVO[]> list = getDMO().queryVOBySql(sql11);
			if (list == null || list.size() == 0)
				return null;
			for (int i = 0; i < list.size(); i++) {
				mvos = list.get(i);
				if (mvos != null && mvos.length != 0)
					lists.addAll(Arrays.asList(mvos));
			}
		}
		if (lists != null && lists.size() != 0)
			return lists.toArray(new ReportBaseVO[0]);
		return mvos;
	}

	public Map<String, ReportBaseVO[]> getTypetomodvo(ReportBaseVO[] vos)
			throws Exception {
		String billtype = getBillTypeName();
		if (billtype == null || billtype.length() == 0)
			throw new Exception(" 没有注册单据类型");
		if (vos == null || vos.length == 0) {
			return null;
		}
		ReportBaseVO[][] voss = (ReportBaseVO[][]) SplitBillVOs.getSplitVOs(
				vos, new String[] { billtype });
		if (voss == null || voss.length == 0)
			return null;
		Map<String, ReportBaseVO[]> map = new HashMap<String, ReportBaseVO[]>();
		for (int i = 0; i < voss.length; i++) {
			ReportBaseVO[] vs = voss[i];
			String btype = PuPubVO.getString_TrimZeroLenAsNull(vs[0]
					.getAttributeValue(billtype));
			if (btype == null || btype == null) {
				continue;
			}
			setAccountModVOBYStatus(map, btype, vs);
		}
		return map;
	}

	/**
	 * 根据typetostatus 单据类型 状态对应map 过滤现存量修复vo
	 * 
	 * @param map
	 * @param btype
	 * @param vs
	 * @throws Exception
	 */
	public void setAccountModVOBYStatus(Map<String, ReportBaseVO[]> map,
			String btype, ReportBaseVO[] vs) throws Exception {
		Map<String, boolean[]> smap = getTypetostatus();
		if (smap == null || smap.size() == 0) {
			throw new Exception("没有注册单据类型到单据状态的映射");
		}
		boolean[] isQuerys = smap.get(btype);
		if (isQuerys == null || isQuerys.length == 0) {
			return;
		}
		if (isQuerys.length != 2) {
			throw new Exception("获取单据类型对应 的单据状态出错");
		}
		if (isQuerys[0] == true && isQuerys[1] == true) {
			if (vs != null && vs.length != 0)
				map.put(btype, vs);
		}
		if (isQuerys[0] == true && isQuerys[1] == false) {
			if (vs == null || vs.length == 0)
				return;
			ReportBaseVO[] nvs = getModvosByStatus(IBillStatus.FREE, vs);
			if (nvs != null && nvs.length != 0)
				map.put(btype, nvs);
		}
		if (isQuerys[0] == false && isQuerys[1] == true) {
			if (vs == null || vs.length == 0)
				return;
			ReportBaseVO[] nvs = getModvosByStatus(IBillStatus.CHECKPASS, vs);
			if (nvs != null && nvs.length != 0)
				map.put(btype, nvs);
		}
	}

	/**
	 * 根据单据状态过滤 vs
	 * 
	 * @param checkpass
	 * @param vs
	 * @throws Exception
	 */
	public ReportBaseVO[] getModvosByStatus(int billStatus, ReportBaseVO[] vs)
			throws Exception {
		String status = getBillTypeStatusName();
		if (status == null || status.length() == 0)
			throw new Exception(" 没有注册单据状态名字");
		if (vs == null || vs.length == 0)
			return null;
		List<ReportBaseVO> list = new ArrayList<ReportBaseVO>();
		for (int i = 0; i < vs.length; i++) {
			Integer stus = PuPubVO.getInteger_NullAs(vs[i]
					.getAttributeValue(status), -1);
			if (stus == billStatus) {
				list.add(vs[i]);
			}
		}
		if (list == null || list.size() == 0)
			return null;
		return list.toArray(new ReportBaseVO[0]);
	}

	// getTypetoChangeClass
	/**
	 * 获得 现存量vo
	 */
	public Map<String, SuperVO[]> getNumVO(Map<String, ReportBaseVO[]> map)
			throws Exception {
		if (map == null || map.size() == 0)
			return null;
		Map<String, SuperVO[]> nummap = new HashMap<String, SuperVO[]>();// 存放现存量vo
		Map<String, String> chmap = getTypetoChangeClass();
		if (chmap == null || chmap.size() == 0)
			throw new Exception("没有注册单据类型 到  现存量 的交换类");
		String className = getNumClass();
		Class cl = Class.forName(className);
		if (className == null || className.length() == 0)
			throw new Exception("没有注册现存量对应类全名");
		ReportBaseVO[] rvos = null;
		SuperVO[] svos = null;
		String changClass = null;// 交换类
		for (String key : map.keySet()) {
			rvos = map.get(key);
			if (rvos == null || rvos.length == 0)
				continue;
			changClass = chmap.get(key);
			if (changClass == null || changClass.length() == 0)
				throw new Exception(" 单据类型编码为 :" + key + "的没有注册交换类");
			svos = (SuperVO[]) SingleVOChangeDataBsTool.runChangeVOAry(rvos,
					cl, changClass);
			if (svos != null && svos.length != 0)
				nummap.put(key, svos);
		}
		return nummap;
	}

	/**
	 * 设置现存量数据变化量
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void setAccountNumChange(Map<String, SuperVO[]> map)
			throws Exception {
		if (map == null || map.size() == 0)
			return;
		Map<String, UFBoolean[]> nmap = getTypetosetnum();
		if (nmap == null || nmap.size() == 0)
			throw new Exception(" 没有注册单据类型 对应  现存量变化规则");
		String[] fields = getSetNumFields();
		if (fields == null || fields.length == 0)
			throw new Exception(" 没有注册现存量变化量字段");
		SuperVO[] vos = null;
		for (String key : map.keySet()) {
			UFBoolean[] ufs = nmap.get(key);
			if (ufs == null || ufs.length == 0 || ufs.length != fields.length)
				throw new Exception("单据类型为" + key
						+ " 注册的变化量规则为空 或  注册的规则数组和变化量字段数组长度不一致");
			vos = map.get(key);
			if (vos == null || vos.length == 0)
				continue;
			setAccountNum(vos, ufs);
		}
	}

	/**
	 * 获得数组形式的现存量vo
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public SuperVO[] getAccountNum(Map<String, SuperVO[]> map) throws Exception {
		if (map == null || map.size() == 0)
			return null;
		List<SuperVO> list = new ArrayList<SuperVO>();
		SuperVO[] svos = null;
		for (String key : map.keySet()) {
			svos = map.get(key);
			if (svos != null && svos.length != 0)
				list.addAll(Arrays.asList(svos));
		}
		if (list != null && list.size() != 0) {
			String className = getNumClass();
			Class cl = Class.forName(className);
			return list.toArray((SuperVO[]) java.lang.reflect.Array
					.newInstance(cl, 0));
		}
		return null;
	}

	/**
	 * 按最小维度进行数据合并
	 */
	/**
	 * @author mlr 按台账统计维度合并数据
	 * @param accounts
	 * @return
	 * @throws Exception
	 */
	public SuperVO[] combinAccounts(SuperVO[] accounts) throws Exception {
		if (accounts == null || accounts.length == 0)
			return null;
		String[] num_condition_fields = getUnpk();
		if (num_condition_fields == null || num_condition_fields.length == 0)
			throw new Exception(" 没有注册现存量最小维度字段");

		CircularlyAccessibleValueObject[][] os = SplitBillVOs.getSplitVOs(
				accounts, num_condition_fields);
		int len = os.length;
		String className = getNumClass();
		Class cl = Class.forName(className);
		SuperVO[] newAccouts = (SuperVO[]) java.lang.reflect.Array.newInstance(
				cl, len);
		SuperVO[] datas = null;
		SuperVO tmp = null;
		for (int i = 0; i < len; i++) {
			datas = (SuperVO[]) os[i];
			tmp = datas[0];
			for (int j = 0; j < datas.length; j++) {
				if (j == 0)
					continue;
				for (String num : getSetNumFields()) {
					tmp.setAttributeValue(num, PuPubVO.getUFDouble_NullAsZero(
							tmp.getAttributeValue(num)).add(
							PuPubVO.getUFDouble_NullAsZero(datas[j]
									.getAttributeValue(num))));
				}
			}
			newAccouts[i] = tmp;
		}
		return newAccouts;
	}

	/**
	 * 清空现存量
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public void clearNum(String sql) throws Exception {
		getDao().executeUpdate(sql);
	}

	/**
	 * 现存量修复入口类
	 * 
	 * @throws Exception
	 */
	public void accountMOD() throws Exception {
		clearNum(getClearSql());// 清空现存量
		ReportBaseVO[] rvos = queryModVO();// 查询所有 修复数据
		Map<String, ReportBaseVO[]> map = getTypetomodvo(rvos);// 得到单据类型
																// 映射的需要修复的 数据
		setNumAccountModSpecialDeal(map);
		Map<String, SuperVO[]> nmap = getNumVO(map);// 数据交换 获得现存量vo
		setNumAccoutSpecialDeal(nmap);
		setAccountNumChange(nmap);// 设置数据变化量
		SuperVO[] vos = getAccountNum(nmap);// 得到现存量vo数组
		SuperVO[] combinvos = combinAccounts(vos);// 按现存量最小维度进行数据合并
		SuperVO[] zvos = filterNUll(combinvos);// 过滤掉变化量为零的数据
		if (zvos == null || zvos.length == 0)
			return;
		getDao().insertVOArray(zvos);// 更新现存量
	}

	public void setNumAccoutSpecialDeal(Map<String, SuperVO[]> nmap)
			throws Exception {

	}

	public void setNumAccountModSpecialDeal(Map<String, ReportBaseVO[]> map)
			throws Exception {

	}

	/**
	 * 过滤变掉变化量为零的数据
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-19下午05:33:57
	 * @param vos
	 * @return
	 * @throws Exception
	 */
	public SuperVO[] filterNUll(SuperVO[] vos) throws Exception {
		List<SuperVO> list = new ArrayList<SuperVO>();
		if (vos == null || vos.length == 0) {
			return null;
		}
		String[] fnames = getSetNumFields();
		if (fnames == null || fnames.length == 0)
			throw new Exception(" 没有注册现存量变化字段");
		for (int i = 0; i < vos.length; i++) {
			boolean isNull = true;
			for (int j = 0; j < fnames.length; j++) {
				UFDouble num = PuPubVO.getUFDouble_NullAsZero(vos[i]
						.getAttributeValue(fnames[j]));
				if (!num.equals(new UFDouble(0))) {
					isNull = false;
				}
			}
			if (isNull == false)
				list.add(vos[i]);
		}
		if (list.size() == 0) {
			return null;
		}
		String className = getNumClass();
		Class cl = Class.forName(className);
		return list.toArray((SuperVO[]) java.lang.reflect.Array.newInstance(cl,
				0));
	}

	/**
	 * 带查询条件 查询所有的需要修复的 现存量的vo
	 * 
	 * @return
	 */
	public ReportBaseVO[] queryModVO(String whereSql) throws Exception {
		String sql = getQuerySql(whereSql);
		String[] sql11 = getQuerySql1(whereSql);
		ReportBaseVO[] mvos = null;
		List<ReportBaseVO> lists = new ArrayList<ReportBaseVO>();
		if (sql != null && sql.length() != 0) {
			mvos = getDMO().queryVOBySql(sql);
		} else {
			List<ReportBaseVO[]> list = getDMO().queryVOBySql(sql11);
			if (list == null || list.size() == 0)
				return null;
			for (int i = 0; i < list.size(); i++) {
				mvos = list.get(i);
				if (mvos != null && mvos.length != 0)
					lists.addAll(Arrays.asList(mvos));
			}
		}
		if (lists != null && lists.size() != 0)
			return lists.toArray(new ReportBaseVO[0]);
		return mvos;
	}

	/**
	 * 现存量修复入口类 带whereSql条件
	 * 
	 * @throws Exception
	 */
	public void accountMOD(String whereSql) throws Exception {
		clearNum(getClearSql(whereSql));// 清空现存量
		ReportBaseVO[] rvos = queryModVO(whereSql);// 查询所有 修复数据
		Map<String, ReportBaseVO[]> map = getTypetomodvo(rvos);// 得到单据类型
																// 映射的需要修复的 数据
		setNumAccountModSpecialDeal(map);
		Map<String, SuperVO[]> nmap = getNumVO(map);// 数据交换 获得现存量vo
		setAccountNumChange(nmap);// 设置数据变化量
		SuperVO[] vos = getAccountNum(nmap);// 得到现存量vo数组
		SuperVO[] combinvos = combinAccounts(vos);// 按现存量最小维度进行数据合并
		SuperVO[] zvos = filterNUll(combinvos);// 过滤掉变化量为零的数据
		if (zvos == null || zvos.length == 0)
			return;
		getDao().insertVOArray(zvos);// 更新现存量
	}

	public abstract String getClearSql(String whereSql) throws Exception;

	/**
	 *根据条件 查询所有的单据 获得转化后的的台账数据
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-10-25上午10:17:02
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public SuperVO[] getAccountNum(String whereSql) throws Exception {
		ReportBaseVO[] rvos = queryModVO(whereSql);// 查询所有 修复数据
		Map<String, ReportBaseVO[]> map = getTypetomodvo(rvos);// 得到单据类型
																// 映射的需要修复的 数据
		setNumAccountModSpecialDeal(map);
		Map<String, SuperVO[]> nmap = getNumVO(map);// 数据交换 获得现存量vo
		setAccountNumChange(nmap);// 设置数据变化量
		SuperVO[] vos = getAccountNum(nmap);// 得到现存量vo数组
		SuperVO[] combinvos = combinAccounts(vos);// 按现存量最小维度进行数据合并
		SuperVO[] zvos = filterNUll(combinvos);// 过滤掉变化量为零的数据
		return zvos;
	}

}
