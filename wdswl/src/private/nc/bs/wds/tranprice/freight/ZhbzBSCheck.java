package nc.bs.wds.tranprice.freight;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wds.tranprice.freight.ZhbzHVO;

public class ZhbzBSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}

		// 判断是修改后的保存还是新增后的保存
		ZhbzHVO ivo = (ZhbzHVO) vo.getParentVO();   //得到主表VO
		
		ZhbzBVO[] bvo=(ZhbzBVO[])vo.getChildrenVO();  //得到子表
		
		String prmint=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		//判断是否新增保存
		if(prmint==null){
			//表头的 唯一校验
			String carriersid=PuPubVO.getString_TrimZeroLenAsNull(ivo.getCarriersid());
			String standardtune=PuPubVO.getString_TrimZeroLenAsNull(ivo.getStandardtune());
			String tuneunits=PuPubVO.getString_TrimZeroLenAsNull(ivo.getTuneunits());
			if(carriersid==null||standardtune==null||tuneunits==null){
				return;
			}
			StringBuffer sql=new StringBuffer();
			sql.append( " carriersid='" + carriersid+ "' and  isnull(dr,0)=0");
			sql.append("  and standardtune='"+standardtune+"' and tuneunits ='"+tuneunits+"'");
	        List list1 = (List) getDao().retrieveByClause(ZhbzHVO.class,sql.toString());
	        if(list1 != null &&  list1.size() >0){
	        	throw new BusinessException("表头的字段存在重复!");
	        }

			
			//BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"carriersid","standardtune","tuneunits"}, " 表头的字段存在重复！ ");
			//对表体存货的唯一性效验
			if(bvo==null||bvo.length==0){
				throw new BusinessException("表体不能为空！");
			}
			String sql1="select pk_invmandoc from wds_zhbz_b where isnull(dr,0)=0";
			ArrayListProcessor processor = new ArrayListProcessor();
			List list=(List) getDao().executeQuery(sql1,processor);
			for(int i=0;i<bvo.length;i++){
			  String invmandoc=PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getPk_invmandoc());
			  if(invmandoc==null){
					throw new BusinessException("存货信息不能为空！");
			  }
			  if(list!=null&&list.size()>0){
					for(int j=0;j<list.size();j++){
						Object[] lis= (Object[]) list.get(j);
						String old=lis[0].toString();
						if(invmandoc.equals(old)){
							throw new BusinessException("存货信息在库中已经存在，不能重复！");

						}
					}
				}
			}
		}else{
			//修改校验 
			//表头的 唯一校验
			String carriersid=PuPubVO.getString_TrimZeroLenAsNull(ivo.getCarriersid());
			String standardtune=PuPubVO.getString_TrimZeroLenAsNull(ivo.getStandardtune());
			String tuneunits=PuPubVO.getString_TrimZeroLenAsNull(ivo.getTuneunits());
			if(carriersid==null||standardtune==null||tuneunits==null){
				return;
			}
			StringBuffer buf=new StringBuffer();
			buf.append( " carriersid='" + carriersid+ "' and  isnull(dr,0)=0");
			buf.append("  and standardtune='"+standardtune+"' and tuneunits ='"+tuneunits+"'");
	        List list1 = (List) getDao().retrieveByClause(ZhbzHVO.class,buf.toString());
	        if(list1 != null &&  list1.size() >1){
	        	throw new BusinessException("表头的字段存在重复!");
	        }
	        if(list1 != null &&  list1.size() == 1){
	        	ZhbzHVO zhbz = (ZhbzHVO)list1.get(0);
	        	if(!prmint.equalsIgnoreCase(zhbz.getPrimaryKey())){
		        	throw new BusinessException("表头的字段存在重复");

	        	}
	        }
	        
	    	//对表体存货的唯一性效验
			if(bvo==null||bvo.length==0){
				throw new BusinessException("表体不能为空！");
			}
			String sql="select pk_invmandoc from wds_zhbz_b where isnull(dr,0)=0";
			ArrayListProcessor processor = new ArrayListProcessor();
			List list=(List) getDao().executeQuery(sql,processor);
			for(int i=0;i<bvo.length;i++){
			  String invmandoc=PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getPk_invmandoc());
			  if(invmandoc==null){
					throw new BusinessException("存货信息不能为空！");
			  }
			  if(list!=null&&list.size()>0){
					for(int j=0;j<list.size();j++){
						Object[] lis= (Object[]) list.get(j);
						String old=lis[0].toString();
						if(invmandoc.equals(old)){
							throw new BusinessException("存货信息在库中已经存在，不能重复！");

						}
					}
				}
			}
	        
			
		
		}
	
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
