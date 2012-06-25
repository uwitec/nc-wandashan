package nc.ui.wdsnew.pub;
import java.util.ArrayList;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
/**
 * 完达山物流 权限过滤工具类
 * 实现方法：
 * 根据操作员 查询 管理的存货管理主键
 * 根据操作员 生成 该操作员所管理存货的查询sql语句 
 * 根据操作员 过滤 出入库单表体数据 （过滤出操作员所管辖的存货的出入库单表体数据） 
 * @author mlr
 */
public class PowerGetTool {
	
	/**
	 * 获得某用户 所管理存货的 查询sql
	 * @param pk_user
	 *            用户
	 * @return
	 */
	public static String queryClassPowerSql(String pk_user) {
        //写的时候别忘了过滤公司
		return null;
	}
	/**
	 * 根据 操作员 过滤 存货
	 * @param tmpBodyVo  要过滤的vo数组
	 * @param pk_user
	 * @return
	 * @throws BusinessException
	 */
	    public static CircularlyAccessibleValueObject[] spiltByPower(CircularlyAccessibleValueObject[] tmpBodyVo,String pk_invmandocName,String pk_user) throws BusinessException {
  
	       return  null;
		}
	    /**
	     * 判断pk值是否在arrayList中存在
	     * @param pk
	     * @param arrayList2
	     * @return
	     */
		public static boolean exist(String pk, ArrayList arrayList) {
			if(pk==null || pk.length()==0)
				return false;
			boolean isf=false;
			for(int i=0;i<arrayList.size();i++){
				Object[] os=(Object[]) arrayList.get(i);
				if(pk.equals(os[0])){
					isf=true;
					return true;
				}
			}		
			return false;
		}

		/**
		 * 获得操作员管理权限下 的存货管理档案主键集合
		 * 
		 * @param tableName
		 * @param pk_corp
		 * @param pk_user
		 * @return
		 */
		public static ArrayList<String> getPowerByUser(String pk_user) {
//			String sql = queryClassPowerSql(tableName, pk_corp, pk_user);
//			ArrayListProcessor bc = new ArrayListProcessor();
//			ArrayList alGroup = (ArrayList) DBCacheFacade.runQuery(sql, bc);
			return null;
		}
	    
}
