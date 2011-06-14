package nc.bs.wds.ie.storepersons;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.ie.storepersons.TbStockstaffVO;

public class BSCheck implements IBDBusiCheck{
	private BaseDAO dao=null;

	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;	   
	}
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
	throws Exception {
		if(intBdAction==IBDACTION.SAVE){
			if(vo==null){
				return;
			}
			TbStockstaffVO head=(TbStockstaffVO)vo.getParentVO();
			if(head ==null)
				return;

			//判断是新增后的保存，还是修改后得保存
			if(head.getPrimaryKey()==null || head.getPrimaryKey().equals("") || head.getPrimaryKey().trim().length()==0){
				String sql="select count(0) from tb_stockstaff where cuserid='"+head.getCuserid()+"' and  isnull(dr,0)=0";
				int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
				if(index>0)
					throw new BusinessException("该人员已经和仓库绑定");
			}else{
				//先把就的数据通过主键把cuserid取出来
				String sql="select cuserid from tb_stockstaff where st_pk='"+head.getPrimaryKey()+"' and isnull(dr,0)=0";
			    Object obj=getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR);
			    if(obj==null){
			    	return;
			    }
				//拿当前vo head的cuserid 比较
				if(obj.toString().equals(head.getCuserid())){
					return;
				}				
				String sql1="select count(0) from tb_stockstaff where cuserid='"+head.getCuserid()+"' and isnull(dr,0)=0";
				int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql1, WdsPubResulSetProcesser.COLUMNPROCESSOR), -1);
				if(index>0)
					throw new BusinessException("该人员已经和仓库绑定");				
			}
		}
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {


	}

}
