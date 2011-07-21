package nc.itf.zb.pub;


import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.zb.pub.JoinTableInfo;

public interface IJoinTableQry {
	
	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere, String strOrderBy)
			throws BusinessException;

	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere)
			throws BusinessException;

	public SuperVO[] queryVOWithJoinTable(Class supervoClass,
			JoinTableInfo[] joinTable, String strWhere, String strOrderBy,
			Integer maxRecNo, String[] selectedFields) throws BusinessException;
}
