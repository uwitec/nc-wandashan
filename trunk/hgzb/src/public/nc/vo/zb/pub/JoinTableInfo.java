package nc.vo.zb.pub;

import java.io.Serializable;

/**
 * <p>���ڶ������ı�������Ϣ��
 * @author twh
 * @date 2007-1-19 ����03:09:38
 * @version V5.0
 * @since V3.1
 * @��Ҫ����ʹ�ã�
 *  <ul>
 * 		<li><b>���ʹ�ø��ࣺ</b></li>
 *      <li><b>�Ƿ��̰߳�ȫ��</b></li>
 * 		<li><b>������Ҫ��</b></li>
 * 		<li><b>ʹ��Լ����</b></li>
 * 		<li><b>������</b></li>
 * </ul>
 * </p>
 * <p>
 * @��֪��BUG��
 * 	<ul>
 * 		<li></li>
 *  </ul>
 * </p>
 * <p>
 * @�޸���ʷ��
 */
public class JoinTableInfo implements Serializable {

	/* Ҫjoin�ı�class�����ɿ� */
	private Class joinSourceTableClass = null;

	/* join������Ϊ����Ϊfrom ֮��ı� */
	private Class joinTargetTableClass = null;

	/* join�������е��ֶ�����Ϊ�����Զ�ƥ������һ���� */
	private String fkFieldName = null;

	/* join��Ŀ�����ֶ�����Ϊ����Ϊ���� */
	private String joinFieldName = null;

	/* ����on֮���where��䣬dr�Ѿ�Ĭ�ϼ��� */
	private String joinWhere = null;

	/* inner join����left outer join */
	private boolean innerjoin = true;

	/* join Ŀ���ı�����Ϊ����Ϊ���� */
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
		// TODO �Զ����ɹ��캯�����
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