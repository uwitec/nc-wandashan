package nc.bo.other.out;

import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * 
 * @author Administrator
 * ���������̨��
 */
public class OtherOutBO {
	
BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * lyf
	 * @param map<��Դ���ݱ���id��ʵ�ʳ�������>
	 * @param assmap<��Դ���ݱ���id��ʵ�ʸ�����>
	 * @param vsourbillhid
	 * @throws DAOException 
	 */
	private void reWriteTOWDS5(	Map<String,UFDouble> map,Map<String,UFDouble> assmap,String vsourbillhid) throws DAOException{
		for(Entry<String, UFDouble> entry:map.entrySet()){
			String sql =" update wds_sendorder_b set ndealnum=coalesce(ndealnum,0)+" +
					PuPubVO.getUFDouble_NullAsZero(entry.getValue())+
					 " where pk_sendorder_b='"+entry.getKey();
			getBaseDAO().executeUpdate(sql);
		}
	
	}

}
