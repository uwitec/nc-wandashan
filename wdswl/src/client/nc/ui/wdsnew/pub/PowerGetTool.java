package nc.ui.wdsnew.pub;
import java.util.ArrayList;
import java.util.List;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.pub.ClientEnvironment;
import nc.ui.wl.pub.LoginInforHelper;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
/**
 * 完达山物流 权限过滤工具类
 * 实现方法：
 * 根据操作员 查询 管理的存货管理主键
 * 根据操作员 生成 该操作员所管理存货的查询sql语句 
 * 根据操作员 过滤 出入库单表体数据 （过滤出操作员所管辖的存货的出入库单表体数据） 
 * @author mlr
 */
public class PowerGetTool {
	private static LoginInforHelper login;
	public static LoginInforHelper getLoginInfor(){
		if(login==null ){
			login=new LoginInforHelper();
		}
		return login;
	}	
	/**
	 * 获得某用户 所管理存货的 查询sql
	 * @param pk_user
	 *            用户
	 * @return
	 * @throws Exception 
	 */
	public static String queryClassPowerSql(String pk_user) throws Exception {
		if(pk_user==null || pk_user.length()==0)
			throw new Exception("操作员为空");
		//如果不是报关员 报错
		if(getLoginInfor().getLogInfor(pk_user)==null){
			throw new Exception("该操作员没有绑定货位");
		}
		
//		if(getLoginInfor().getLogInfor(pk_user).getType()!=0){
//			throw new Exception("该操作员不是报管员");
//		}
		String[] pk_cargdocs=getLoginInfor().getSpaceByLogUser(pk_user);
		if(pk_cargdocs==null ||pk_cargdocs.length==0)
			throw new Exception("该操作员没有绑定货位");
		String sql=" select b.pk_invmandoc " +
				"   from wds_cargdoc1 h  " +
				"join tb_spacegoods b" +
				" on h.pk_wds_cargdoc=b.pk_wds_cargdoc " +
				" where isnull(h.dr,0)=0  "+
				"  and  isnull(b.dr,0)=0 "+
				"  and  h.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'"+
				"  and  h.pk_cargdoc='"+pk_cargdocs[0]+"' ";     
		return sql;
	}
	/**
	 * 根据 操作员 过滤 存货
	 * @param tmpBodyVo  要过滤的vo数组
	 * @param pk_user
	 * @return
	 * @throws Exception 
	 */
	    public static CircularlyAccessibleValueObject[] spiltByPower(CircularlyAccessibleValueObject[] tmpBodyVo,String pk_invmandocName,String pk_user) throws Exception {
            //取得该操作员所管理的存货主键id
	    	ArrayList pks=getPowerByUser(pk_user);
	    	   //存放过滤后的表体数据
	        List<CircularlyAccessibleValueObject> list=new ArrayList<CircularlyAccessibleValueObject>();
	        for(int i=0;i<tmpBodyVo.length;i++){
	            String pk=PuPubVO.getString_TrimZeroLenAsNull(
	        			tmpBodyVo[i].getAttributeValue(pk_invmandocName));
	            if(exist(pk,pks))            	
	        	        list.add(tmpBodyVo[i]);           	
	        }
	       return  list.toArray((CircularlyAccessibleValueObject[])java.lang.reflect.Array.newInstance(tmpBodyVo[0].getClass(), list.size()));	     
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
		 * @throws Exception 
		 */
		public static ArrayList getPowerByUser(String pk_user) throws Exception {
			String sql = queryClassPowerSql(pk_user);
			ArrayListProcessor bc = new ArrayListProcessor();
			ArrayList alGroup = (ArrayList) DBCacheFacade.runQuery(sql, bc);
			return alGroup;
		}
	    
}
