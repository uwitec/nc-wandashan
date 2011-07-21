package nc.vo.zb.pub;

import java.io.Serializable;

/**
 * <p>用于多表联查的表连接信息。
 * @author twh
 * @date 2007-1-19 下午03:09:38
 * @version V5.0
 * @since V3.1
 * @主要的类使用：
 *  <ul>
 * 		<li><b>如何使用该类：</b></li>
 *      <li><b>是否线程安全：</b></li>
 * 		<li><b>并发性要求：</b></li>
 * 		<li><b>使用约束：</b></li>
 * 		<li><b>其他：</b></li>
 * </ul>
 * </p>
 * <p>
 * @已知的BUG：
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 * <p>
 * @修改历史：
 */
public class JoinTableInfo implements Serializable {

	/* 要join的表class，不可空 */
	private Class joinSourceTableClass = null;

	/* join的主表，为空则为from 之后的表 */
	private Class joinTargetTableClass = null;

	/* join在主表中的字段名，为空则自动匹配名称一样的 */
	private String fkFieldName = null;

	/* join在目标表的字段名，为空则为主键 */
	private String joinFieldName = null;

	/* 放在on之后的where语句，dr已经默认加了 */
	private String joinWhere = null;

	/* inner join还是left outer join */
	private boolean innerjoin = true;

	/* join 目标表的别名，为空则为表名 */
	private String alias = null;

	/**
	 * @param joinSourceTableClass
	 */
	public JoinTableInfo(Class joinSourceTableClass) {
		super();
		this.joinSourceTableClass = joinSourceTableClass;
	}
	public String getJoinFieldName() {
		return joinFieldName;
	}

	public void setJoinFieldName(String joinFieldName) {
		this.joinFieldName = joinFieldName;
	}

	public String getJoinWhere() {
		return joinWhere;
	}

	public void setJoinWhere(String joinWhere) {
		this.joinWhere = joinWhere;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFkFieldName() {
		return fkFieldName;
	}

	public void setFkFieldName(String fkFieldName) {
		this.fkFieldName = fkFieldName;
	}

	public boolean isInnerjoin() {
		return innerjoin;
	}

	public void setInnerjoin(boolean innerjoin) {
		this.innerjoin = innerjoin;
	}

	/**
	 *  
	 */
	private JoinTableInfo() {
		super();
		// TODO 自动生成构造函数存根
	}

	public Class getJoinSourceTableClass() {
		return joinSourceTableClass;
	}

	
	public Class getJoinTargetTableClass() {
		return joinTargetTableClass;
	}

	public void setJoinTargetTableClass(Class joinTargetTableClass) {
		this.joinTargetTableClass = joinTargetTableClass;
	}
}