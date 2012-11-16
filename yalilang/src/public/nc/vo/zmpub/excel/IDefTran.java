package nc.vo.zmpub.excel;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * zhf excel导入时自定义的编码向ID转换规则
 * @author Administrator
 *
 */
public interface IDefTran {
	

	public String transCodeToID(BaseDAO dao,CircularlyAccessibleValueObject vo,CodeToIDInfor infor) throws BusinessException;
}
