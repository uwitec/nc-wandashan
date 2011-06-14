package nc.bs.wl.plan;

import java.util.ArrayList;
import java.util.List;

import sun.net.www.http.Hurryable;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.dm.SendplaninBVO;
import nc.vo.dm.SendplaninVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.VOTool;

/**
 * @作者：lyf
 * @说明：完达山物流项目 
 * 发运计划录入（WDS1）后台类
 */
public class PlanCheckinBO {
	
	BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * 	保存前校验
	 * 当前登录人的管理的仓库在当前会计月是否已经有月计划
	 * @时间：2011-3-23下午09:14:56
	 * @param pk_inwhouse =调入仓库p主键，pk=主表主键
	 */
	public void beforeCheck(String  pk_inwhouse,String pk) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where wds_sendplanin.iplantype=0 and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"' ");
		sql.append(" and isnull(dr,0)=0");

		
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if(pk == null || "".equalsIgnoreCase(pk)){
			if( i>0){
				throw new BusinessException("该调入仓库，当前会计月已经有月计划,只可以做追加计划");
			}
		}else{
			if( i>1){
				throw new BusinessException("该调入仓库，当前会计月已经有月计划,只可以做追加计划");
			}
		}
		
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *   弃审前校验 是否已经安排生产下游发运订单
	 * @时间：2011-3-27上午09:44:46
	 * @param 要弃审的 发运计划单据
	 * @throws BusinessException 
	 */
	public void beforeUnApprove(AggregatedValueObject obj) throws BusinessException{
		if(obj ==null){
			return;
		}
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();
		String pk_sendplanin = parent.getPk_sendplanin();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select count(*) ");
		sql.append(" from wds_sendorder ");
		sql.append(" join wds_sendorder_b ");
		sql.append(" on wds_sendorder.pk_sendorder= wds_sendorder_b.pk_sendorder");
		sql.append(" where isnull(wds_sendorder.dr,0)=0 and isnull(wds_sendorder_b.dr,0)=0 ");
		sql.append(" and wds_sendorder_b.csourcebillhid ='"+pk_sendplanin+"'");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
		if( i>0){
			throw new BusinessException("已有下游发运订单，请先删除发运订单再做此操作");
		}
		
	}
	//追加计划校验
	public void beforeCheck1(String  pk_inwhouse,String pk) throws BusinessException{
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from wds_sendplanin ");
		sql.append(" where dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");
		sql.append(" and wds_sendplanin.iplantype=0 ");
		sql.append(" and isnull(dr,0)=0");
		int i = PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);		
			if( i<=0){
				throw new BusinessException("该调入仓库，当前会计月没有月计划");
			}			
	}
	//将追加计划合并到月计划
	public void planStats(AggregatedValueObject obj2) throws BusinessException{
		//将传来的对象克隆一份
		AggregatedValueObject obj=VOTool.aggregateVOClone(obj2);
	   
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();		
		String pk_inwhouse=parent.getPk_inwhouse();	
		SendplaninBVO[] childs=(SendplaninBVO[]) obj.getChildrenVO();
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();		
		StringBuffer sql=new StringBuffer();
		sql.append(" select pk_sendplanin  from");
		sql.append(" wds_sendplanin where wds_sendplanin.vbillstatus=1");
		sql.append(" and isnull(wds_sendplanin.dr,0)=0");
		sql.append(" and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");

		List<SendplaninBVO> adds=new ArrayList<SendplaninBVO>();
		List<SendplaninBVO> mods=new ArrayList<SendplaninBVO>();
		
	    Object o=getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
	    if( o!=null && o instanceof String){      
	        String cond=" pk_sendplanin='"+o+"' and isnull(dr,0)=0";
	    	List<SendplaninBVO> list=(List<SendplaninBVO>) getBaseDAO().retrieveByClause(SendplaninBVO.class, cond);    	
	    	boolean isExist=false;
		    for(int i=0;i<childs.length;i++){
		       if(!isExist&& i>0){		        
		    	 childs[i-1].setPk_sendplanin((String)o);  
		    	 childs[i-1].setPk_sendplanin_b(null);
		         adds.add(childs[i-1]);	       
		       }
		       isExist=false;
		       for(int j=0;j<list.size();j++){
		    	  if(childs[i].getPk_invmandoc().equalsIgnoreCase(list.get(j).getPk_invmandoc())){	    		  
		    		  list.get(j).setNplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNplannum()).add(PuPubVO.getUFDouble_NullAsZero(childs[i].getNplannum())));
		    		  list.get(j).setNassplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNassplannum()).add(PuPubVO.getUFDouble_NullAsZero(childs[i].getNassplannum())));		    		  
		    		  mods.add(list.get(j)); 
		    		  isExist=true;
		    		  break;
		        }
		    	  
		      } 
		    }
	    }
	  for(int i=0;i<adds.size();i++){
	    getBaseDAO().insertVOWithPK(adds.get(i));
	  }	  
		  getBaseDAO().updateVOList(mods); 
	}	
	
	//将 追加计划    从  月计划 拆分出来
	public void unplanStats(AggregatedValueObject obj2) throws BusinessException{
		//将传来的对象克隆一份
		AggregatedValueObject obj=VOTool.aggregateVOClone(obj2);
	   
		SendplaninVO parent =(SendplaninVO) obj.getParentVO();		
		String pk_inwhouse=parent.getPk_inwhouse();	
		SendplaninBVO[] childs=(SendplaninBVO[]) obj.getChildrenVO();
		AccountCalendar calendar = AccountCalendar.getInstance();
		UFDate beginDate = calendar.getMonthVO().getBegindate();
		UFDate endDate = calendar.getMonthVO().getEnddate();		
		StringBuffer sql=new StringBuffer();
		sql.append(" select pk_sendplanin  from");
		sql.append(" wds_sendplanin where wds_sendplanin.vbillstatus=1");
		sql.append(" and isnull(wds_sendplanin.dr,0)=0");
		sql.append(" and dmakedate between '");
		sql.append(beginDate+"' and '" + endDate);
		sql.append("' and pk_inwhouse ='"+pk_inwhouse+"'");

		
		List<SendplaninBVO> mods=new ArrayList<SendplaninBVO>();
		
	    Object o=getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
	    if( o!=null && o instanceof String){      
	        String cond=" pk_sendplanin='"+o+"' and isnull(dr,0)=0";
	    	List<SendplaninBVO> list=(List<SendplaninBVO>) getBaseDAO().retrieveByClause(SendplaninBVO.class, cond);    	
	    	boolean isExist=false;
		    for(int i=0;i<childs.length;i++){
		    
		       for(int j=0;j<list.size();j++){
		    	  if(childs[i].getPk_invmandoc().equalsIgnoreCase(list.get(j).getPk_invmandoc())){	    		  
		    		  list.get(j).setNplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNplannum()).sub(PuPubVO.getUFDouble_NullAsZero(childs[i].getNplannum())));
		    		  list.get(j).setNassplannum(PuPubVO.getUFDouble_NullAsZero(list.get(j).getNassplannum()).sub(PuPubVO.getUFDouble_NullAsZero(childs[i].getNassplannum())));		    		  
		    		 if(list.get(j).getNplannum().doubleValue()<0 || list.get(j).getNassplannum().doubleValue()<0){
		    			throw new BusinessException("不能弃审，月计划已经安排了该追加计划中的单品");
		    		  }
		    		  mods.add(list.get(j));    		  
		    		  break;
		        }
		    	  
		      } 
		    }
	    }
	  
		  getBaseDAO().updateVOList(mods); 
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
