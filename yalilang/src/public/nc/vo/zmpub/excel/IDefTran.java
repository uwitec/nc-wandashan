package nc.vo.zmpub.excel;

import nc.vo.pub.BusinessException;

/**
 * zhf excel����ʱ�Զ���ı�����IDת������
 * @author Administrator
 *
 */
public interface IDefTran {

	public String transCodeToID(CodeToIDInfor infor) throws BusinessException;
}
