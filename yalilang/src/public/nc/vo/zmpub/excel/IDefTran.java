package nc.vo.zmpub.excel;

import nc.vo.pub.BusinessException;

/**
 * zhf excel导入时自定义的编码向ID转换规则
 * @author Administrator
 *
 */
public interface IDefTran {

	public String transCodeToID(CodeToIDInfor infor) throws BusinessException;
}
