  /***************************************************************\
  *     The skeleton of this class is generated by an automatic *
  * code generator for NC product. It is based on Velocity.     *
  \***************************************************************/

package nc.vo.zb.bidding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;


/**
 * 
 * 该类是用于多子表的聚合VO
 *
 * 创建日期:2011-04-28 16:51:42
 * @author Administrator
 * @version NCPrj 1.0
 */
@SuppressWarnings("serial")
@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.zb.bidding.BiddingHeaderVO")
public class BidViewBillVO extends HYBillVO implements IExAggVO{
    
	private Object m_useObj = null;//zhf add 最初用意：标书数据转换为评审表时 需要 useobj
	//用于装载多子表数据的HashMap
	private HashMap hmChildVOs = new HashMap();
	
	public BiddingHeaderVO getHeader(){
		return (BiddingHeaderVO)getParentVO();
	}
	
	public Object getUserObj(){
		return m_useObj;
	}
	public void setUserObj(Object o){
		m_useObj = o;
	}
	
	public static String tablecode_body = "zb_bidding_b";

//	public static String tablecode_suppliers = "zb_biddingsuppliers";

	public static String tablecode_times = "zb_biddingtimes";
	
	/**
	 * 返回多个子表的编码
	 * 必须与单据模版的页签编码对应
	 * 创建日期：2011-04-28 16:51:42
	 * @return String[]
	 */
	public String[] getTableCodes(){
		          
		return new String[]{tablecode_body,tablecode_times};
		          
	}
	
	
	/**
	 * 返回多个子表的中文名称
	 * 创建日期：2011-04-28 16:51:42
	 * @return String[]
	 */
	public String[] getTableNames(){
		
		return new String[]{"标书信息","标书的轮次时间安排"};
	}
	
	
	/**
	 * 取得所有子表的所有VO对象
	 * 创建日期：2011-04-28 16:51:42
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getAllChildrenVO(){
		
		ArrayList<CircularlyAccessibleValueObject> al = new ArrayList();
		for(int i = 0; i < getTableCodes().length; i++){
			CircularlyAccessibleValueObject[] cvos
			        = getTableVO(getTableCodes()[i]);
			if(cvos != null)
				al.addAll(Arrays.asList(cvos));
		}
		
		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}
	
	
	/**
	 * 返回每个子表的VO数组
	 * 创建日期：2011-04-28 16:51:42
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode){
		
		return (CircularlyAccessibleValueObject[])
		            hmChildVOs.get(tableCode);
	}
	
	
	/**
	 * 
	 * 创建日期：2011-04-28 16:51:42
	 * @param SuperVO item
	 * @param String id
	 */
	public void setParentId(SuperVO item,String id){}
	
	/**
	 * 为特定子表设置VO数据
	 * 创建日期：2011-04-28 16:51:42
	 * @param String tableCode
	 * @para CircularlyAccessibleValueObject[] vos
	 */
	public void setTableVO(String tableCode,CircularlyAccessibleValueObject[] vos){
		
		hmChildVOs.put(tableCode,vos);
	}
	
	/**
	 * 缺省的页签编码
	 * 创建日期：2011-04-28 16:51:42
	 * @return String 
	 */
	public String getDefaultTableCode(){
		
		return getTableCodes()[0];
	}
	
	/**
	 * 
	 * 创建日期：2011-04-28 16:51:42
	 * @param String tableCode
	 * @param String parentId
	 * @return SuperVO[]
	 */
	public SuperVO[] getChildVOsByParentId(String tableCode,String parentId){
		
		return null;
	}
	
	public void setChildrenVO(
			nc.vo.pub.CircularlyAccessibleValueObject[] children) {
		setTableVO(tablecode_body, children);
	}
	
	public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
		return getTableVO(tablecode_body);
	}
	
	/**
	 * 
	 * 创建日期：2011-04-28 16:51:42
	 * @return HashMap
	 */
	public HashMap getHmEditingVOs() throws Exception{
		
		return null;
	}
	
	/**
	 * 
	 * 创建日期:2011-04-28 16:51:42
	 * @param SuperVO item
	 * @return String
	 */
	public String getParentId(SuperVO item){
		
		return null;
	}
}
