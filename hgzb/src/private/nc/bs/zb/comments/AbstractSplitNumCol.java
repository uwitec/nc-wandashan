package nc.bs.zb.comments;

import nc.vo.pub.BusinessException;
import nc.vo.zb.comments.ISplitNumPara;

/**
 * 
 * @author zhf  �������� ������������ �ӿ�
 *
 */
public interface  AbstractSplitNumCol {
	public void col() throws BusinessException;
	public void setPara(ISplitNumPara para);
}
