package nc.bs.zmpub.pub.tool.stock;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import nc.bs.logging.Logger;
import nc.bs.pub.SystemException;
import nc.bs.zmpub.pub.report.ReportDMO;
import nc.ui.zmpub.pub.tool.LongTimeTask;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.tool.CombinVO;
/**
 * 可用量查询处理工具类
 * @author mlr
 */
public abstract class AvailNumBoTool extends BillStockBO {
	private static final long serialVersionUID = 5669705676536970653L;
	   private ReportDMO dmo=null;
	   public ReportDMO getDMO() throws SystemException, NamingException{
		   if(dmo==null ){
			   dmo=new ReportDMO();
		   }
		   return dmo;
	   }

	// 这两个方法用不到 start
	@Override
	public Map<String, String> getTypetoChangeClass() throws Exception {
		return null;
	}

	@Override
	public Map<String, UFBoolean[]> getTypetosetnum() throws Exception {
		return null;
	}

	// 这两个方法用不到 end

	/**
	 * 得到单据类型->现存量的对应字段 即 单据 中的字段 跟 现存量 中的字段对应关系
	 */
	public abstract Map<String, String[]> getTypetoAccountFields()
			throws Exception;

	/**
	 * 得到单据类型 ->订单查询语句
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-26上午10:18:34
	 * 
	 */
	public abstract Map<String, String> getTypetoQuerySql() throws Exception;

	/**
	 * 
	 * 得到可用量 跟据用现存量vo封装的查询条件 此方法客户端调用
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-26上午10:13:19
	 * 
	 */
	public SuperVO[] getAvailNumForClient(SuperVO[] vos) throws Exception {
		// 得到现存量
		SuperVO[] nvos = queryStockCombinForClient(vos);
		if (nvos == null || nvos.length == 0)
			return null;
		//得到订单占用量
		ReportBaseVO[] dvos = getAvailNumForClient1(vos);
		if(dvos==null || dvos.length==0)
			return nvos;
		//得到可用量
		SuperVO[] zvos=getNums(nvos,dvos);       
		return zvos;
	}
	/**
	 * 
	 * 得到可用量 跟据用现存量vo封装的查询条件 此方法后台调用
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-26上午10:13:19
	 * 
	 */
	public SuperVO[] getAvailNumDatas(SuperVO[] vos) throws Exception {
		// 得到现存量
		SuperVO[] nvos = super.queryStockCombin(vos);
		if (nvos == null || nvos.length == 0)
			return null;
		//得到订单占用量
		ReportBaseVO[] dvos = getAvailNum1(vos);
		if(dvos==null || dvos.length==0)
			return nvos;
		//得到可用量
		SuperVO[] zvos=getNums(nvos,dvos);       
		return zvos;
	}
	
	private ReportBaseVO[] getAvailNum1(SuperVO[] vos) throws Exception {
		//得到订单查询的过滤条件
		Map<String, List<String>> whereSqls=getWhereSqls(vos);
		//得到添加过滤条件后的订单查询sql
		Map<String, List<String>> sqls=getSqls(whereSqls);
		//得到订单占用量
		ReportBaseVO[] dvos=getOrderNums(vos, sqls);
		return dvos;
	}

	private ReportBaseVO[] getAvailNumForClient1(SuperVO[] vos) throws Exception {
		//得到订单查询的过滤条件
		Map<String, List<String>> whereSqls=getWhereSqls(vos);
		//得到添加过滤条件后的订单查询sql
		Map<String, List<String>> sqls=getSqls(whereSqls);
		//得到订单占用量
		ReportBaseVO[] dvos=getOrderNumsForClient(vos,sqls);
		return dvos;
	}

    /**
     * 得到可用量  可用量=现存量->预计发出量
     * @throws Exception 
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-7-26下午01:08:07
     *
     */
	private SuperVO[] getNums(SuperVO[] nvos, ReportBaseVO[] dvos) throws Exception {
		if(nvos==null || nvos.length==0)
			return null;
		if(dvos==null || dvos.length==0)
			return nvos;
		if(nvos.length!=dvos.length)
			return nvos;
		for(int i=0;i<nvos.length;i++){
			SuperVO nvo=nvos[i];
			ReportBaseVO dvo=dvos[i];
			if(nvo==null )
				continue;
			if(dvo==null )
				continue;
			String[] fields=getChangeNums();
			if(fields==null|| fields .length==0)
				throw new Exception("没有注册现存量变化字段");
			for(int j=0;j<fields.length;j++){
				//得到 现存量
				UFDouble nuf=PuPubVO.getUFDouble_NullAsZero(nvo.getAttributeValue(fields[j]));
				//得到 预计出的量
				UFDouble duf=PuPubVO.getUFDouble_NullAsZero(dvo.getAttributeValue(fields[j]));
				//设置可用量
				nvo.setAttributeValue(fields[j], nuf.sub(duf));
			}	
		}		
		return nvos;
	}

	/**
	 * 得到订单的 预计出的量  for后台查询
	 * @throws Exception 
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-7-26上午10:20:59
	 * 
	 */
	public ReportBaseVO[] getAvailNum(SuperVO[] vos) throws Exception {
		//得到订单查询的过滤条件
		Map<String,List<String>> whereSqls=getWhereSqls(vos);
		//得到添加过滤条件后的订单查询sql
		Map<String,List<String>> sqls=getSqls(whereSqls);
		//得到订单占用量
		ReportBaseVO[] dvos=getOrderNums(vos,sqls);
		return dvos;
	}
	
	
	public ReportBaseVO[] getOrderNumsForClient(SuperVO[] vos,
			Map<String, List<String>> sqls) throws Exception {

		ReportBaseVO[] nvos=null;
	    	try {
				Class[] ParameterTypes = new Class[] { SuperVO[].class ,Map.class};
			
				Object[] ParameterValues = new Object[] { vos,sqls };
				Object o = LongTimeTask.calllongTimeService(
						"zmpub", null, "正在查询...", 1,
						getThisClassName(), null, "getOrderNums",
						ParameterTypes, ParameterValues);
				if (o != null) {
					nvos = (ReportBaseVO[]) o;
				}
			} catch (Exception e) {
				Logger.error(e);
				throw new Exception(e.getMessage());
				
			}		
	      return nvos;			
	}

	/**
	 * 得到定单占用量
	 * @throws Exception 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-7-26上午10:29:40
	 *
	 */
   public ReportBaseVO[] getOrderNums(SuperVO[] vos, Map<String, List<String>> sqls) throws Exception {
	   //查询订单的可用量
	   ReportBaseVO[] nvos=querySqls(sqls);
	   //得到按 查询维度合并后 的可用量
	   ReportBaseVO[] zvos=getCombinVos(vos,nvos);	   
		return zvos;
	}

/**
 * 按查询维度合并订单可用量
 * @throws Exception 
 * @作者：mlr
 * @说明：完达山物流项目 
 * @时间：2012-7-26下午12:07:40
 *
 */
private ReportBaseVO[] getCombinVos(SuperVO[] vos, ReportBaseVO[] nvos) throws Exception {
	ArrayList<ReportBaseVO> nlist = new ArrayList<ReportBaseVO>();	
	if (vos == null || vos.length == 0)
		return null;
	if (nvos == null || nvos.length == 0)
		return null;
	for (int i = 0; i < vos.length; i++) {
			Map<String,String>conds = getConminFields1(vos[i]);
			String[] conds1=getConminFields(vos[i]);
			if (conds == null || conds.size() == 0) {
				nlist.add(null);
			} else {
				//获得该维度下的订单可用量
				ReportBaseVO[] vos1=getVos(conds,nvos);				
				ReportBaseVO[] coms = (ReportBaseVO[]) CombinVO.combinData(vos1,
						conds1, getChangeNums(), ReportBaseVO.class);
				if (coms == null || coms.length == 0) {
					nlist.add(null);
				} else {
					nlist.add(coms[0]);
				}

			}

		
	}
	if(nlist==null || nlist.size()==0)
		return null;
	return nlist.toArray((ReportBaseVO[]) java.lang.reflect.Array.newInstance(
			ReportBaseVO.class, 0));
	}
/**
 * 获得数据合并维度
 * @throws Exception 
 * @作者：mlr
 * @说明：完达山物流项目 
 * @时间：2012-7-2下午12:52:49
 *
 */
public  Map<String,String> getConminFields1(SuperVO vo) throws Exception {

	Map<String,String> map=new HashMap<String, String>();//存放 合并字段的键值对
	String[] fields =getDef_Fields();
	   if(fields==null || fields.length==0)
	   		throw new Exception("没有注册现存量维度");	
	if(vo==null )
		return null;
	for(int i=0;i<fields.length;i++){
		String value=PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(fields[i]));
		if(value!=null){
			map.put(fields[i], value);
		}
	}
	if(map==null || map.size()==0)
		return null;

  return map;
}

/**
 * 按传入的维度 过滤数据
 * @作者：mlr
 * @说明：完达山物流项目 
 * @时间：2012-7-26下午12:49:53
 *
 */
private ReportBaseVO[] getVos(Map<String, String> conds, ReportBaseVO[] nvos) {
	if(conds==null || conds.size()==0)
		return nvos;
	if(nvos==null || nvos.length==0)
		return null;
	List<ReportBaseVO>  list=new ArrayList<ReportBaseVO>();	
	for(int i=0;i<nvos.length;i++){
		boolean bo=true;
		for(String key:conds.keySet()){
		    String qvalue=PuPubVO.getString_TrimZeroLenAsNull(conds.get(key));
		    String nvalue=PuPubVO.getString_TrimZeroLenAsNull(nvos[i].getAttributeValue(key));
		    if(qvalue==null || nvalue==null ){
		    	bo=false;
		        break;
		    }
		    if(!qvalue.equals(nvalue)){
		    	bo=false;
		    	break;
		    }		   
		}
		if(bo==true){
			list.add(nvos[i]);
		}
	}
	return list.toArray(new ReportBaseVO[0]);
}

/**
 * 查询订单的可用量
 * @throws NamingException 
 * @throws SQLException 
 * @throws SystemException 
 * @作者：mlr
 * @说明：完达山物流项目 
 * @时间：2012-7-26下午12:07:28
 *
 */
private ReportBaseVO[] querySqls(Map<String, List<String>> sqls) throws SystemException, SQLException, NamingException {
	  List<String> list=new ArrayList<String>();
	  
	  if(sqls==null || sqls.size()==0)
		  return null;
	  for(String key:sqls.keySet()){
		  List<String> li=sqls.get(key);
		  list.addAll(li);
	  }
	  if(list==null || list.size()==0)
		  return null;
	  List<ReportBaseVO[]> vos=getDMO().queryVOBySql(list.toArray(new String[0]));
	  if(vos==null || vos.size()==0)
		  return null;
	  List<ReportBaseVO>  listvo=new ArrayList<ReportBaseVO>();
	  for(int i=0;i<vos.size();i++){
		  ReportBaseVO[] rvos=vos.get(i);
		  if(rvos==null || rvos.length==0)
			  continue;
		  for(int j=0;j<rvos.length;j++){
			  listvo.add(rvos[j]);
		  }
	  }	  
		return listvo.toArray(new ReportBaseVO[0]);
	}

/**
    * 得到添加过滤条件后的 订单查询sql
    * @作者：mlr
    * @说明：完达山物流项目 
    * @时间：2012-7-26上午10:25:59
    *
    */
	public Map<String, List<String>> getSqls(Map<String, List<String>> whereSqls) throws Exception{
		Map<String ,String > sqls=getTypetoQuerySql();
		if(sqls==null || sqls.size()==0)
			throw new Exception("没有注册单据类型->订单查询sql");
		Map<String ,List<String>> map=new HashMap<String, List<String>>();
		
		if(whereSqls==null || whereSqls.size()==0)
			return null;
		for(String type:whereSqls.keySet()){
			List<String> wsqls=whereSqls.get(type);
			List<String> sqlzs=new ArrayList<String>();
			String sql=sqls.get(type);
			if(sql==null || sql.length()==0)
				throw new Exception("单据类型["+type+"] 没有注册订单查询语句");
			if(wsqls==null|| wsqls.size()==0){
				List<String> list=new ArrayList<String>();
				list.add(sql);
				map.put(type,list);
				continue;
			}	
			for(int i=0;i<wsqls.size();i++){
				String zsql=sql+" and "+wsqls.get(i);	
				sqlzs.add(zsql);
			}
			map.put(type, sqlzs);
		}	
		return map;
	}
	
    /**
     * 得到订单查询的过滤条件
     * @throws Exception 
     * @作者：mlr
     * @说明：完达山物流项目 
     * @时间：2012-7-26上午10:25:32
     *
     */
	public  Map<String, List<String>> getWhereSqls(SuperVO[] vos) throws Exception {
		if(vos==null || vos.length==0)
			return null;
		//单据类型 --->现存量    对应字段
		Map<String,String[]> map= getTypetoAccountFields();
		//单据类型  --->过滤sql
		Map<String,List<String>> mapn=new HashMap<String, List<String>>();
		if(map==null || map.size()==0)
			throw new Exception("没有注册单据类型到现存量的对应字段");
		for(String type:map.keySet()){
			//存放 过滤条件
			List<String> list=new ArrayList<String>();
		    String[] fields=map.get(type);
		   //得到现存量维度字段
		   String[] acfs=getDef_Fields();
		   if(acfs==null|| acfs.length==0)
			   throw new Exception("没有注册现存量最小维度字段");
		   if(fields.length!=acfs.length)
			   throw new Exception("单据类型["+type+"] 与现存量对应字段 长度不一致");
		   for(int i=0;i<vos.length;i++){
			   String  whereSql=null;
			   SuperVO vo=vos[i];
			   for(int j=0;j<acfs.length;j++){
				   if(PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue(acfs[j]))==null){
					   continue;
				   }
				   if(fields[j]==null || fields[j].length()==0)
					   continue;
					if (whereSql == null) {
						whereSql=fields[j]+" = '"+vo.getAttributeValue(acfs[j])+"'";	
					} else {
						whereSql = whereSql + " and " + fields[j] + " = '" +vo.getAttributeValue(acfs[j])+ "'";
					}				  				   
			   }
			   list.add(whereSql);		   
		   }
		   mapn.put(type, list) ;		
		}	
		return mapn;
	}
}
