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
public class BiddingBillVO extends HYBillVO implements IExAggVO{
    
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

	public static String tablecode_suppliers = "zb_biddingsuppliers";

	public static String tablecode_times = "zb_biddingtimes";
	
	/**
	 * 返回多个子表的编码
	 * 必须与单据模版的页签编码对应
	 * 创建日期：2011-04-28 16:51:42
	 * @return String[]
	 */
	public String[] getTableCodes(){
		          
		return new String[]{tablecode_body,tablecode_suppliers,tablecode_times};
		          
	}
	
	
	/**
	 * 返回多个子表的中文名称
	 * 创建日期：2011-04-28 16:51:42
	 * @return String[]
	 */
	public String[] getTableNames(){
		
		return new String[]{"标书信息","标书的供应商信息","标书的轮次时间安排"};
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
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）系统设置的资质分最高值
	 * 2011-5-26下午04:24:04
	 * @param nmaxquo
	 * @throws BusinessException
	 */
	public void validateOnCommit(UFDouble nmaxquo) throws BusinessException{

		BiddingHeaderVO head = (BiddingHeaderVO)getParentVO();
		if(head == null)
			throw new ValidationException("数据异常");
		boolean isself = PuPubVO.getUFBoolean_NullAs(head.getFisself(), UFBoolean.FALSE).booleanValue();
		head.validateOnCommit();
		
		BiddingBodyVO[] bodys = (BiddingBodyVO[])getTableVO(getTableCodes()[0]);
		if(bodys == null || bodys.length == 0)
			throw new ValidationException("数据异常，存在表体数据为空的单据");
		for(BiddingBodyVO body:bodys){
			body.validateOnSave(isself);
		}
		
		BiddingSuppliersVO[] vendors = (BiddingSuppliersVO[])getTableVO(getTableCodes()[1]);
		if(vendors == null || vendors.length == 0)
			throw new ValidationException("备选供应商信息不能为空");
		int num = PuPubVO.getInteger_NullAs(head.getNvendornum(), -1);
		if(num>vendors.length){
			throw new ValidationException("备选供应商数量不能少于入围供应商数量");
		}
		for(BiddingSuppliersVO vendor:vendors){
			vendor.validate(nmaxquo);
		}	
		if(PuPubVO.getInteger_NullAs(head.getIzbtype(),new Integer(-1))==0){
			BiddingTimesVO[] times = (BiddingTimesVO[])getTableVO(getTableCodes()[2]);
			if(times == null || times.length == 0)
				throw new ValidationException("轮次信息不可为空");
		}
	}
	
	public void validateOnSave(boolean ischeckno) throws ValidationException {
		BiddingHeaderVO head = (BiddingHeaderVO)getParentVO();
		if(head == null)
			throw new ValidationException("数据异常");
		head.validateOnPushSave(ischeckno);
		boolean isself = PuPubVO.getUFBoolean_NullAs(head.getFisself(), UFBoolean.FALSE).booleanValue();
		BiddingBodyVO[] bodys = (BiddingBodyVO[])getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new ValidationException("数据异常，存在表体数据为空的单据");
		for(BiddingBodyVO body:bodys){
			body.validateOnSave(isself);
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）计算历史品均价
	 * 2011-5-18下午06:06:51
	 * @param prices
	 * @return
	 * @throws BusinessException
	 */
	public static Map<String,UFDouble> colHistoryPrice(HistoryPriceVO[] prices) throws BusinessException{
		if(prices == null || prices.length == 0)
			return null;
		Map<String,List<UFDouble>> priceInfor = new HashMap<String, List<UFDouble>>();
		List<UFDouble> ltmp = null;
		String key = null;
		for(HistoryPriceVO price:prices){
			key = PuPubVO.getString_TrimZeroLenAsNull(price.getCbaseid());
			if(key == null){
				throw new BusinessException("数据异常,获取到得历史合同品种为空");
			}
			if(priceInfor.containsKey(key))
				ltmp = priceInfor.get(key);
			else
				ltmp = new ArrayList<UFDouble>();
			ltmp.add(PuPubVO.getUFDouble_NullAsZero(price.getNoriginalcurprice()));
			priceInfor.put(key, ltmp);
		}
		Map<String,UFDouble> retMap = new HashMap<String, UFDouble>();
		UFDouble nall = null;
		for(String key2:priceInfor.keySet()){
			ltmp = priceInfor.get(key2);
			nall = UFDouble.ZERO_DBL;
			for(UFDouble n:ltmp){
				nall = nall.add(n);
			}
			retMap.put(key2, nall.div(new UFDouble(ltmp.size()), 8));
		}
		
		return retMap;
	}
	
	private static String invfoumuler = "code->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinvid)";
	private static String[] invnames = new String[]{"cinvid"};
	
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业）是否属于同一大类
	 * 2011-6-11下午01:39:13
	 * @param invid
	 * @param invid2
	 * @return
	 * @throws BusinessException
	 */
	public static boolean isClass(String invid,String invid2) throws BusinessException{
		String code1 = PuPubVO.getString_TrimZeroLenAsNull(ZbPubTool.execFomular(invfoumuler, invnames, new String[]{invid}));
		String code2 = PuPubVO.getString_TrimZeroLenAsNull(ZbPubTool.execFomular(invfoumuler, invnames, new String[]{invid2}));
		if(code1 == null || code2 == null){
			throw new BusinessException("数据异常获取品种编码为空");
		}
		code1 = code1.substring(0, ZbPubConst.inv_class_coderule);
		code2 = code2.substring(0, ZbPubConst.inv_class_coderule);
		if(code1.equalsIgnoreCase(code2)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @author zhf
	 * @说明：（鹤岗矿业） 处理品种编码为大类编码
	 * 2011-6-11下午07:09:14
	 * @param bodys
	 */
	public static void dealBiddingInvBodys(BiddingBodyVO[] bodys){
		if(bodys == null || bodys.length == 0)
			return;
		for(BiddingBodyVO body:bodys){
			 if(PuPubVO.getString_TrimZeroLenAsNull(body.getInvclcode())==null)
				  continue;
//			  调整存货编码为大类编码
			  body.setInvclcode(body.getInvclcode().substring(0, ZbPubConst.inv_class_coderule));
		}
	}
}
