package nc.bs.zb.comments;

import nc.vo.pub.BusinessException;
import nc.vo.zb.comments.ISplitNumPara;

/**
 * 
 * @author zhf  分量计算 降代码低耦合性 接口
 *
 */
public interface  AbstractSplitNumCol {
	public void col() throws BusinessException;
	public void setPara(ISplitNumPara para);
}
