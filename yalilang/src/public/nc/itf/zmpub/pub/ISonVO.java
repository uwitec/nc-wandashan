package nc.itf.zmpub.pub;

import java.util.ArrayList;
import java.util.List;
import nc.vo.pub.SuperVO;

/**
 * 实现孙表保存 子表vo必须实现该接口
 * 
 * @author mlr
 */
public interface ISonVO {
	/**
	 * 返回孙表信息
	 * 
	 * @return
	 */
	public abstract ArrayList<SuperVO> getSonVOS();

	/**
	 * 返回行号名字
	 * 
	 * @return
	 */
	public abstract String getRowNumName();

	/**
	 * 设置孙表信息
	 * 
	 * @param list
	 */
	public abstract void setSonVOS(List<SuperVO> list);
}
