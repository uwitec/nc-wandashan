package nc.ui.zb.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.zb.pub.IJoinTableQry;
import nc.vo.pub.BusinessException;
import nc.vo.zb.pub.JoinTableInfo;

/**
 * 
 */

public class ClientHelper {

	public ClientHelper() {
	}

	public static nc.vo.pub.SuperVO[] queryVOWithJoinTable(
			java.lang.Class supervoClass,
			JoinTableInfo[] joinTable, java.lang.String strWhere,
			java.lang.String strOrderBy) throws BusinessException {
		IJoinTableQry ijtq = (IJoinTableQry)NCLocator.getInstance().lookup(IJoinTableQry.class.getName());
		if(ijtq!=null){
		    return ijtq.queryVOWithJoinTable(supervoClass, joinTable, strWhere, strOrderBy);
		}else{
			return null;
		}
	}

	public static nc.vo.pub.SuperVO[] queryVOWithJoinTable(
			java.lang.Class supervoClass,
			JoinTableInfo[] joinTable, java.lang.String strWhere)
			throws BusinessException {
		IJoinTableQry ijtq = (IJoinTableQry)NCLocator.getInstance().lookup(IJoinTableQry.class.getName());
		if(ijtq!=null){
		return ijtq.queryVOWithJoinTable(supervoClass,
				joinTable, strWhere);
		}else{
			return null;
		}
	}

	public static nc.vo.pub.SuperVO[] queryVOWithJoinTable(
			java.lang.Class supervoClass,
			JoinTableInfo[] joinTable, java.lang.String strWhere,
			java.lang.String strOrderBy, java.lang.Integer maxRecNo,
			java.lang.String[] selectedFields) throws BusinessException {
		IJoinTableQry ijtq = (IJoinTableQry)NCLocator.getInstance().lookup(IJoinTableQry.class.getName());
		if(ijtq!=null){
		return ijtq.queryVOWithJoinTable(supervoClass,
				joinTable, strWhere, strOrderBy, maxRecNo, selectedFields);
		}else{
			return null;
		}
	}
}
