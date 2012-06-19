package nc.bs.zmpub.pub.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.SuperVO;

/**
 * 业务单据后台查询类
 * @author mlr
 *
 */
public class ZmPubBO {
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 支持查询对话框按表体查询
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2012-1-11下午03:28:22
	 * @param headClassName
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public SuperVO[] queryByHeadAndBodyVOs(String headVoName,String sql) throws Exception{
		Class hc=Class.forName(headVoName);
		List list=(List) getDao().executeQuery(sql, new BeanListProcessor(hc));	
		SuperVO vo=(SuperVO) Class.forName(headVoName).newInstance();
		String  pkName=vo.getPKFieldName();
		if(list==null || list.size()==0)			
			return null;
		Map<String,SuperVO> map=new HashMap<String,SuperVO>();
		for(int i=0;i<list.size();i++){
			SuperVO vo1=(SuperVO) list.get(i);
			map.put(vo1.getPrimaryKey(), vo1);
		}
		List li=new ArrayList();
		if(map!=null && map.size()>0){
			for(String key :map.keySet()){
				li.add(map.get(key));
			}
		}		
		return (SuperVO[]) li.toArray((SuperVO[])java.lang.reflect.Array.newInstance(hc, 0));
	}

}
