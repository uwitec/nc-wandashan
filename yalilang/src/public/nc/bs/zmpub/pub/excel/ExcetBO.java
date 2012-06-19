package nc.bs.zmpub.pub.excel;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.tool.ResultSetProcessorTool;
/**
 * Excel数据处理
 * @author mlr
 */
public class ExcetBO {
	private BaseDAO dao=null;
	private BaseDAO getDAO(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;
	}
	/**
	 * 从excelt 报表vo到 期初数据vo
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-11-4下午03:21:24
	 * @param vos
	 * @param m_user
	 * @param logDate
	 * @return
	 * @throws Exception
	 */	
	public SuperVO[] excelChangeToVO(String returnClass,ReportBaseVO[] vos,String[] fieldsNames,String queryIds[],String queryTables[],String querySelectIDs[],String[] queryCodeNames,boolean[] isVlCorp,boolean[] isMutiTables,String[] setValueIds,String pk_corp,String m_user,String logDate) throws Exception{
		if(vos==null || vos.length==0)
			return null;
		List<SuperVO>	list=new ArrayList<SuperVO>();	
		String[] chafields=getNotQueryFields(queryIds,fieldsNames);	
		Class cl=Class.forName(returnClass);
	for(int i=0;i<vos.length;i++){		
		SuperVO vo=(SuperVO) Class.forName(returnClass).newInstance();
		  for(int j=0;j<queryIds.length;j++){
				String code=PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue(queryIds[j]));
				String id=(String) querypkall(querySelectIDs[j],queryTables[j],queryCodeNames[j],code,isVlCorp[j],pk_corp,isMutiTables[j]);
				vo.setAttributeValue(setValueIds[j], id);
		  }			  	 
		  for(int l=0;l<chafields.length;l++){
			  Object value=vos[i].getAttributeValue(chafields[l]);
			  vo.setAttributeValue(chafields[l], value);
		  }
		  vo.setAttributeValue("pk_corp", pk_corp);
		  list.add(vo);
		}	
	    if(list.size()!=0)
	    	return list.toArray((SuperVO[]) java.lang.reflect.Array.newInstance(cl, 0));
	    return null;
	}	
	private String[] getNotQueryFields(String[] queryIds,String[] fields)throws Exception{
		List<String>  list=new ArrayList<String>();
		for(int i=0;i<fields.length;i++){
			boolean  flag=false;
			for(int j=0;j<queryIds.length;j++){
			    if(fields[i].equalsIgnoreCase(queryIds[j])){
			    	flag=true;
			    	break;
			    }    	
			}
			if(flag==false){
				list.add(fields[i]);
			}			
		}
		if(list.size()!=0)
			return list.toArray(new String[0]);
		return null;	
	}
    /**
     * liuys 根据各种档案编码查询其pK值
     * @param tablename
     * @param selectname
     * @param passvalue
     * @param isMutitable 是否多张表联 如果是 就不能过滤dr了
     * @return
     * @throws DAOException
     */
    public Object querypkall(String selectname, String tablename, String codename, String passvalue,
                             boolean bag,String pk_corp,boolean isMutitable) throws DAOException {
        String sqlpkcorp = "select " + selectname + " from " + tablename + " where " + codename
            + " = '"+passvalue+"'";
        if(isMutitable==false){
        	sqlpkcorp=sqlpkcorp+"  and isnull(dr,0)=0 " ;
        }
        if (bag==true)
            sqlpkcorp += " and pk_corp = '" + pk_corp + "'";
        else{
        	 sqlpkcorp += " ;";
        }
            Object obj = getDAO().executeQuery(sqlpkcorp, ResultSetProcessorTool.COLUMNPROCESSOR);
        return obj;
    }

}
