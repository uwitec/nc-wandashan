package nc.bs.zb.pub;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

public class ZbPubBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf   zhw 实现逻辑
	 * @说明：（鹤岗矿业）根据登陆用户关联供应商  优先关联系统供应商  如果关联失败  关联临时供应商	 * 
	 * 2011-5-27上午10:40:20
	 * @param sLogUser
	 * @return
	 * @throws Exception
	 */
	public String getCvendoridByLogUser(String sLogUser) throws BusinessException{
		String cvendorid = null;

		//优先关联系统供应商
		String sql =" select pk_cust from hg_userandcust where pk_user ='"+sLogUser+"' and  isnull(dr,0)=0 ";
		cvendorid =dealArrayList(sql);
		
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null){
			String sqltemp =" select ccubasdochgid from  bd_cubasdochg where cuerid = '"+sLogUser+"' and isnull(dr,0)=0 ";
			cvendorid=dealArrayList(sqltemp);
			
			//如果找不到对应的供应商  
			if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null)
				throw new BusinessException("当前用户关联供应商出错:当前用户未关联供应商");
		}
		return cvendorid;
	}
	
	private String dealArrayList(String sql) throws BusinessException{
		String cvendorid=null;
		ArrayList al =(ArrayList)getDao().executeQuery(sql,ZbBsPubTool.COLUMNLISTPROCESSOR);
		if(al!=null && al.size()==1)
			cvendorid = PuPubVO.getString_TrimZeroLenAsNull(al.get(0));
		if(al.size()>1)
			throw new BusinessException("当前用户关联供应商出错:当前用户关联多个供应商");
		return cvendorid;
	}
	
	//根据登录人员找出业务员 部门
	public Object getLogInfor(String sLogCorp,String sLogUser) throws BusinessException{
		//业务员  部门  
		String sql =" select psn.pk_psndoc capplypsnid,psn.pk_deptdoc from bd_psndoc psn inner join sm_UserAndClerk use " +
			" on use.pk_psndoc = psn.pk_psnbasdoc where use.userid = '"+sLogUser+"' and psn.pk_corp = '"+sLogCorp+"' and isnull(psn.dr,0)=0 and isnull(use.dr,0)=0";
		Object o = getDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if(o==null)
			return null;
		ArrayList al = (ArrayList)o;
		if(al==null || al.size()==0)
			return null;
		if(al.size()>1)
			throw new BusinessException("获取部门,业务员信息出错");
		if(al.size()==1)
			o=al.get(0);
		return o;
	}
}
