package nc.vo.zmpub.excel;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * zhf excel����ʱ�Զ���ı�����IDת������
 * @author Administrator
 *
 */
public interface IDefTran {

	public String transCodeToID(CircularlyAccessibleValueObject vo,CodeToIDInfor infor) throws BusinessException;
}
