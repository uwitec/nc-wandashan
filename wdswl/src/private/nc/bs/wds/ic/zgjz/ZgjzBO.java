package nc.bs.wds.ic.zgjz;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.ic.zgjz.ZgjzBVO;
import nc.vo.wds.ic.zgjz.ZgjzHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 暂估记账 后台VO
 * @author Administrator
 *
 */
public class ZgjzBO {  
	
	private BaseDAO baseDAO = null;
	
	private BaseDAO  getBaseDAO(){
		if(baseDAO == null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2011-12-21下午06:54:22
	 * @param vo
	 * @throws BusinessException 
	 */
	public void beforeSaveChect(AggregatedValueObject vo) throws BusinessException{
		if(vo == null){
			return ;
		}
		ZgjzHVO head = (ZgjzHVO) vo.getParentVO();
		Integer ilacktype = PuPubVO.getInteger_NullAs(head.getIlacktype(), 0);//销售的不需要更新虚拟欠发量
		StringBuffer sql = new StringBuffer();
		if( ilacktype ==0){//销售
			String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getPk_outwhouse());
			if(pk_outwhouse == null){
				throw new BusinessException("出货仓库不能为空");
			}
			sql.append(" select * from wds_zgjz_h ");
			sql.append(" where  isnull(dr,0)=0 ");
			sql.append(" and ilacktype='"+ilacktype+"'");
			sql.append(" and pk_outwhouse='"+pk_outwhouse+"'");
			sql.append(" and( (dbegindate<='" +head.getDbegindate()
					+ "' and denddate>='" + head.getDenddate() + "')");
			sql.append(" or (dbegindate<='" + head.getDenddate() 
					+ "' and denddate>='" + head.getDenddate() + "')");
			sql.append(" or (dbegindate>='" + head.getDbegindate()
					+ "' and denddate<='" + head.getDenddate() + "'))");
		}else{//转分仓的
			String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getPk_outwhouse());
			if(pk_outwhouse == null){
				throw new BusinessException("发货仓库不能为空");
			}
			String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getPk_intwhouse());
			if(pk_inwhouse == null){
				throw new BusinessException("收货仓库不能为空");
			}
			sql.append(" select * from wds_zgjz_h ");
			sql.append(" where  isnull(dr,0)=0 ");
			sql.append(" and ilacktype='"+ilacktype+"'");
			sql.append(" and pk_outwhouse='"+head.getPk_outwhouse()+"'");
			sql.append(" and pk_intwhouse='"+head.getPk_intwhouse()+"'");
			sql.append(" and( (dbegindate<='" +head.getDbegindate()
					+ "' and denddate>='" + head.getDbegindate() + "')");
			sql.append(" or (dbegindate<='" + head.getDenddate() 
					+ "' and denddate>='" + head.getDenddate() + "')");
			sql.append(" or (dbegindate>='" + head.getDbegindate()
					+ "' and denddate<='" + head.getDenddate() + "'))");
		}
		List<ZgjzHVO> list = (ArrayList<ZgjzHVO>) getBaseDAO()
		.executeQuery(sql.toString(),
				new BeanListProcessor(ZgjzHVO.class));
		String primarykey = PuPubVO.getString_TrimZeroLenAsNull(head.getPrimaryKey());
		if( primarykey == null){
			if(list.size()>0){
				throw new BusinessException("存在日期交叉:已经存在暂估记账,开始日期="+list.get(0).getDbegindate()+"结束日期="+list.get(0).getDenddate());
			}	
		}else{
			if(list.size() > 1){
				for(ZgjzHVO hvo:list){
					if(hvo.getPrimaryKey() == null){
						continue;
					}
					if(hvo.getPrimaryKey().equalsIgnoreCase(primarykey)){
						continue;
					}
					throw new BusinessException("存在日期交叉:已经存在暂估记账,开始日期="+hvo.getDbegindate()+"结束日期="+hvo.getDenddate());

				}
			}
		
		}
	}
	
	/**
	 * 
	 * @作者：lyf:更新上月欠发数量 
	 * @说明：完达山物流项目 
	 * @时间：2011-12-15下午04:04:42
	 * @param vo
	 * @param infor:登录人主键，登录日期，当前公司主键，系统日期
	 * @return
	 */
	public AggregatedValueObject refeshDeducNum( AggregatedValueObject vo,ArrayList<String> infor) throws BusinessException{
		if(vo == null ){
			return vo;
		}
		ZgjzHVO head = (ZgjzHVO) vo.getParentVO();
		Integer ilacktype = PuPubVO.getInteger_NullAs(head.getIlacktype(), 0);//销售的不需要更新虚拟欠发量
		if( 0== ilacktype){
			return vo;
		}
		ZgjzBVO[] bodys = (ZgjzBVO[])vo.getChildrenVO();
		String pk_corp = infor.get(2);
		if(pk_corp == null || "".equalsIgnoreCase(pk_corp)){
			pk_corp = SQLHelper.getCorpPk();
		}
		UFDate  dbeginDate = head.getDbegindate();
		if(dbeginDate == null){
			throw new BusinessException("开始日期不能为空");
		}
//		UFDate dendDate = head.getDenddate();
		UFDate dendDate = null;
		if(dendDate == null ){
			dendDate = new UFDate(System.currentTimeMillis());
		}
		String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getPk_outwhouse());//出库仓库
		if(pk_outwhouse == null){
			throw new BusinessException("发货仓库不能为空");
		}
		String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getPk_intwhouse());//入库仓库
		if(pk_inwhouse == null){
			throw new BusinessException("收货仓库不能为空");
		}
		//1.查询ERP的本月红冲数量
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.cinventoryid, ");//存货管理id
		sql.append(" sum(b.noutnum) noutnum,  ");//实出数量
		sql.append(" sum(b.noutassistnum) noutassistnum ");//实出辅数量
		sql.append(" from ic_general_h h ");// --出入库单主表
		sql.append(" join ic_general_b b ");//--出入库单子表
		sql.append(" on h.cgeneralhid = b.cgeneralhid ");
		sql.append(" where isnull(h.dr,0)=0  and isnull(b.dr,0)=0 ");
		sql.append(" and h.pk_corp='"+infor.get(2)+"'");
		sql.append(" and h.cbilltypecode='4I'");//其他出库单
		sql.append(" and h."+WdsWlPubConst.WDS_IC_ZG_DEF+"='"+WdsWlPubConst.WDS_IC_FLAG_wu+"'");//虚拟出库
		sql.append(" and h.dbilldate between '"+dbeginDate+"' and '"+dendDate+"' ");
		sql.append(" and h.cwarehouseid='"+pk_outwhouse+"'");//出库仓库
		sql.append(" and h.cotherwhid='"+pk_inwhouse+"'");//入库仓库
		sql.append(" and b.noutnum<0 ");//冲减的单据：实发数量小于0
		sql.append(" group by b.cinventoryid ");
		Object result = getBaseDAO().executeQuery(sql.toString(),new ArrayListProcessor());
		if(result == null){
			return vo;
		}else{
		//2.按照存货来更新本月冲上月欠发量
			StringBuffer reasons = new StringBuffer();
			ArrayList<Object[]>  list = (ArrayList<Object[]>)result;
			for(Object[] obj:list){
				String cinventoryid =PuPubVO.getString_TrimZeroLenAsNull(obj[0]);
				UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(obj[1]);
				UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(obj[2]);
				if(cinventoryid == null)
					continue;
				boolean flag = false;
				for(ZgjzBVO body:bodys){
					String pk_invmandoc= PuPubVO.getString_TrimZeroLenAsNull(body.getPk_invmandoc());
					UFDouble nlastnum = PuPubVO.getUFDouble_NullAsZero(body.getNlastnum());//上月欠发量
					if(pk_invmandoc != null && pk_invmandoc.equalsIgnoreCase(cinventoryid)){
						if(nlastnum.add(noutnum).doubleValue() <0){//
							String reason = getInvCode(cinventoryid)+"上月欠发主数量="+nlastnum+",获得冲减量="+noutnum+"\n";
							reasons.append(reason);
						}else{//更新冲减上月数量
							body.setNreducnum(noutnum.multiply(-1));
							body.setNreducassnum(noutassistnum.multiply(-1));
							flag = true;
						}
						
					}
				}
				if(!flag){// 冲减量
					String reason = getInvCode(cinventoryid)+"上月无欠发量,获得冲减量="+noutnum+"\n";
					reasons.append(reason);
				}
			}
			if(reasons.length()>0){
				throw new BusinessException(reasons.toString());
			}
		}
		//2.更新
		HYPubBO pubo = new HYPubBO();
		pubo.update(head);
		pubo.updateAry(bodys);
		return vo;
	}
	/**
	 * 
	 * @作者：lyf
	 * @说明：完达山物流项目:查询存货编码
	 * @时间：2011-12-15下午07:20:55
	 * @param pk_ivnmandoc
	 * @return
	 */
	public String getInvCode(String pk_ivnmandoc) throws BusinessException{
		String sql =" select invcode from bd_invbasdoc where pk_invbasdoc = " +
				"( select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_ivnmandoc+"' and isnull(dr,0)=0 ) " +
						" and isnull(dr,0)=0 ";
		String value = (String)getBaseDAO().executeQuery(sql, new ColumnProcessor());
		if(value == null){
			value ="";
		}
		return value;
	}

}
