package nc.vo.zmpub.pub.bill;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

public class MyBillVO extends AggregatedValueObject {

	private static final long serialVersionUID = 1L;

	// 主表VO
	CircularlyAccessibleValueObject m_headVo = null;

	// 子表VO
	CircularlyAccessibleValueObject[] m_itemVos = null;
	
	
//	临时子表信息zhf
	List<CircularlyAccessibleValueObject> lbody = null;
	
	public List<CircularlyAccessibleValueObject> getLbody(){
		if(lbody == null)
			lbody = new ArrayList<CircularlyAccessibleValueObject>();
		return lbody;
	}

	public void setLbody(List<CircularlyAccessibleValueObject> lbody2){
		lbody = lbody2;
	}

	/**
	 * YcBillVO 构造子注解。
	 */
	public MyBillVO() {
		super();
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
		return m_itemVos;
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public CircularlyAccessibleValueObject getParentVO() {
		return m_headVo;
	}

	

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:36:56)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public void setChildrenVO(
			nc.vo.pub.CircularlyAccessibleValueObject[] children) {
		if (children == null) {
			m_itemVos = null;
		} else if (children.length == 0) {
			try {
				m_itemVos = (CircularlyAccessibleValueObject[]) children;
			} catch (ClassCastException e) {
				m_itemVos = null;
			}
		} else {
			List l = Arrays.asList(children);
			m_itemVos = (CircularlyAccessibleValueObject[]) l
			.toArray((Object[]) Array.newInstance(children[0]
			                                               .getClass(), 0));
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(01-3-20 17:32:28)
	 * 
	 * @return nc.vo.pub.ValueObject
	 */
	public void setParentVO(CircularlyAccessibleValueObject parent) {
		m_headVo = (CircularlyAccessibleValueObject) parent;
	}
}


