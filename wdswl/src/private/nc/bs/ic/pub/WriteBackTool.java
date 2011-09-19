package nc.bs.ic.pub;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * 用于回写的工具类
 * @author mlr
 * @date 2011-9-13
 */
public class WriteBackTool{
	private static BaseDAO dao=null;
	private static BaseDAO getDao(){
	 if(dao==null){
		 dao=new BaseDAO();
	 }	
	 return dao;
	}
	private static Map<String,WriteBackVO> addmap= new HashMap<String,WriteBackVO>();//存放新增回写的数据
	private static Map<String,WriteBackVO> editmap= new HashMap<String,WriteBackVO>();//存放修改回写的数据
	private static Map<String,WriteBackVO> delemap= new HashMap<String,WriteBackVO>();//存放删除回写的数据	
    public static Map<String, WriteBackVO> getAddmap() {
		return addmap;
	}

	public  void setAddmap(Map<String, WriteBackVO> addmap) {
		this.addmap = addmap;
	}

	public Map<String, WriteBackVO> getEditmap() {
		return editmap;
	}

	public void setEditmap(Map<String, WriteBackVO> editmap) {
		this.editmap = editmap;
	}

	public Map<String, WriteBackVO> getDelemap() {
		return delemap;
	}

	public void setDelemap(Map<String, WriteBackVO> delemap) {
		this.delemap = delemap;
	}
/**
   * 回写处理的入口方法	
   * @throws Exception
   * @param vos
   */
  public static void writeBack(SuperVO[] vos,String soutablename,String soutableid,String[] fieldnames,String[] backfieldnames)throws Exception{
	 //区分回写的vo类型 
	 splitSetMap(vos,soutablename,soutableid,fieldnames,backfieldnames);
	 //进行数据的回写
	 writeBackSou();
  }
  /**
   * 进行数据的回写
 * @throws Exception 
   */
private static void writeBackSou() throws Exception{
	writeBackAdd();//回写新增数据
	writeBackEdit();//回写修改数据
	writeBackDete();//回写删除数据
}
/**
 * 对删除数据的回写
 * @throws Exception 
 */
private static void writeBackDete() throws Exception {
	for(String key:delemap.keySet()){
	  WriteBackVO vo=delemap.get(key);
	   UFDouble[] oldnums=getOldNums(vo);//旧数据    
	   for(int i=0;i<oldnums.length;i++){
		   oldnums[i]=new UFDouble(0.0).sub(oldnums[i]);
	   }
       vo.setNums(oldnums);
       writeBackToSource(delemap);
	} 
}
/**
 * 获得数据库中旧的查询数据
 * @param vo
 * @return
 * @throws Exception 
 */
private static UFDouble[] getOldNums(WriteBackVO vo) throws Exception {
	String sql=vo.getQueryOldSql();
    Object o=getDao().executeQuery(sql, new ArrayProcessor());
    if(o==null){
  	  throw new Exception(" 要回写的数据已经不存在 ,出现错误");
    }
    Object[] obs=(Object[])o;
    if(obs==null || obs.length==0)
      throw new Exception(" 要回写的数据已经不存在 ,出现错误");
    
    UFDouble[] oldnums=new UFDouble[obs.length];
    for(int i=0;i<obs.length;i++){
    	oldnums[i]=PuPubVO.getUFDouble_NullAsZero(obs[i]);
    }
   
	return oldnums;
}

/**
 * 对修改数据的回写
 * @throws DAOException 
 */
private static void writeBackEdit() throws Exception {
	 if(editmap.size()==0){
		   return;
	 }
	 for(String key:editmap.keySet()){
		  WriteBackVO vo=editmap.get(key);
	      UFDouble[] oldnums=getOldNums(vo);//旧数据      
	      UFDouble[] newnums=vo.getNums();//新数据
	      if(newnums==null || newnums.length==0){
	    	  throw new Exception(" 要回写的数据已经不存在 ,出现错误");
	      }
	      if(oldnums==null || oldnums.length==0){
	    	  throw new Exception(" 要回写的数据已经不存在 ,出现错误");
	      }
	      UFDouble[] editnums=new UFDouble[newnums.length];//调整后 要会写的数据
	      for(int i=0;i<oldnums.length;i++){
	    	  editnums[i]=newnums[i].sub(oldnums[i]);
	      }
	      vo.setNums(editnums);//将修正后的回写数据  传入回写vo	      
	      writeBackToSource(editmap);//将数据回写到来源,进行数据库更新	    	      
	 }	
}
/**
 * 进行回写操作,更新数据库
 * @param map
 * @throws Exception 
 */
private static void writeBackToSource(Map<String, WriteBackVO> map) throws Exception {
	  if(map.size()==0){
		  return;
	  }
	  for(String key:map.keySet()){
    	  WriteBackVO vo=map.get(key);
    	  String sql=vo.getWriteBackSql();
    	  getDao().executeUpdate(sql);  
      }	
	  map.clear();
}

/**
 * 对新增数据的回写
 * @throws Exception
 */
private static void writeBackAdd() throws Exception {
	writeBackToSource(addmap);
}

/**
 * 该方法用于 将新增 修改  删除 的回写区分出来，并封装成本工具类需要的数据类型
 * @param vos
 * @param soutablename
 * @param soutableid
 * @param backfieldnames
 * @param backnums
 * @throws Exception 
 */
private static void splitSetMap(SuperVO[] vos, String soutablename,String soutableidname,
		String[] fieldnames, String[] backfieldnames) throws Exception {
	if(vos==null || vos.length==0){
		return;
	}
	if(soutablename==null || soutablename.length()==0){
		return;
	}
	if(soutableidname==null || soutableidname.length()==0){
		return;
	}
	if(backfieldnames==null || backfieldnames.length==0){
		return;
	}
	if(backfieldnames.length!=fieldnames.length){
		throw new Exception("回写数据不合法");
	}
	for(SuperVO vo:vos){		
		//如果主键为空 则为新增数据
		if(vo.getStatus()==VOStatus.NEW || vo.getPrimaryKey()==null){				
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			addmap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("vsourcebillrowid")),backvo);
		}else if(vo.getStatus()==VOStatus.UPDATED){
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			editmap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("vsourcebillrowid")),backvo);
		}else{
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			editmap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("vsourcebillrowid")),backvo);			
		}	
	}
}
private static  WriteBackVO  backvo=null;
/**
 * 给回写的vo赋值
 * @param vo  
 * @param soutablename 来源表名
 * @param soutableidname 来源表id名字
 * @param fieldnames     要回写的表的字段数组
 * @param backfieldnames 要回写的来源表字段名数组
 */
private static void setValue(SuperVO vo, String soutablename,
		String soutableidname, String[] fieldnames, String[] backfieldnames) {
	backvo=new WriteBackVO();
	backvo.setIdname(soutableidname);
	backvo.setIdvalue(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("csourcebillbid")));
    UFDouble[] backnums=new UFDouble[fieldnames.length];
	for(int i=0;i<fieldnames.length;i++){
		backnums[i]=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(fieldnames[i]));
	}
	backvo.setNums(backnums);
	backvo.setNumsnames1(fieldnames);
	backvo.setSourcetablename(soutablename);
	backvo.setVsourcebillid(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("csourcebillhid")));
	backvo.setVsourcebillrowid(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("csourcebillbid")));
	backvo.setVsourcebilltype(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("csourcetype")));
	backvo.setNumsnames(backfieldnames);
	backvo.setTablename(PuPubVO.getString_TrimZeroLenAsNull(vo.getTableName()));
	backvo.setId(PuPubVO.getString_TrimZeroLenAsNull(vo.getPrimaryKey()));
	backvo.setIdname1(PuPubVO.getString_TrimZeroLenAsNull(vo.getPKFieldName()));	
} 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
}
