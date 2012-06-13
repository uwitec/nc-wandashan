package nc.bs.wds.ic.other.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.zgjz.ZgjzBVO;
import nc.vo.wds.ic.zgjz.ZgjzHVO;

/**
 * lyf:
 * 虚拟欠发的其他出库单处理
 * @author Administrator
 *
 */
public class OtherOutBO_XN {
	HYPubBO pubbo = null;
	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @throws BusinessException 
	 * @作者：lyf
	 * @说明：完达山物流项目:更新转分仓暂估记账的累计安排数量
	 * @时间：2011-12-22下午09:50:16
	 */
	public void updateZgjzNum(AggregatedValueObject billvo ,boolean isAudit) throws BusinessException{
		if(billvo == null){
			return;
		}
		TbOutgeneralHVO head =(TbOutgeneralHVO) billvo.getParentVO();
		TbOutgeneralBVO[] xnvo =(TbOutgeneralBVO[]) billvo.getChildrenVO();;
		if(xnvo == null || xnvo.length ==0){
			return ;
		}
		String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pk());
		if(pk_outwhouse == null){
			throw new BusinessException("更新暂估记账数据：出库仓库不能为空");
		}
		String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pkr());
		if(pk_inwhouse == null){
			throw new BusinessException("更新暂估记账数据：入库仓库不能为空");
		}
		//按照存货汇总出库数量
		Map<String, UFDouble[]> map = new HashMap<String, UFDouble[]>();
		for(TbOutgeneralBVO bvo:xnvo){
			String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCinventoryid());
			if(pk_invmandoc == null){
				continue;
			}
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
			UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassistnum());
			if(map.containsKey(pk_invmandoc)){
				UFDouble noutnum_old = map.get(pk_invmandoc)[0].add(noutnum);
				UFDouble noutassistnum_old = map.get(pk_invmandoc)[1].add(noutassistnum);
				map.get(pk_invmandoc)[0] = noutnum_old;
				map.get(pk_invmandoc)[1] = noutassistnum_old;
			}else{
				UFDouble[]  nums= new UFDouble[2];
				nums[0]=noutnum;
				nums[1]=noutassistnum;
				map.put(pk_invmandoc,nums );
			}
		}
		if(map.size()==0){
			return;
		}
		//查询当期的暂估记账 
			String strWhere = " pk_outwhouse='"+pk_outwhouse+"' and pk_intwhouse='"+pk_inwhouse+"' and  isnull(dr,0)=0  and isnull(ilacktype,0)=1 and vbillstatus='"+IBillStatus.FREE+"'";
			SuperVO[] heads = getHypubBO().queryByCondition(ZgjzHVO.class, strWhere);
			if(heads == null || heads.length==0){
				throw new BusinessException("未查询到匹配的转分仓虚拟欠发数据");
			}else if(heads.length >1){
				throw new BusinessException("查询到该出库仓库多条自由态转分仓虚拟欠发数据");
			}
			ZgjzHVO hvo = (ZgjzHVO)heads[0]; 
			ZgjzBVO[] bodys = (ZgjzBVO[])getHypubBO().queryByCondition(ZgjzBVO.class, " isnull(dr,0)=0 and pk_wds_zgjz_h='"+hvo.getPrimaryKey()+"'");
			Set<String> keys = map.keySet();
			for(String key:keys){
				boolean falge = false;
				for(ZgjzBVO body:bodys){
					String pk_invmandoc =PuPubVO.getString_TrimZeroLenAsNull(body.getPk_invmandoc()) ;
					if(pk_invmandoc == null){
						continue;
					}
					if(key.equalsIgnoreCase(pk_invmandoc)){
						//更新出库数量
						UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());
						UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutassnum());
						UFDouble noutnum_add= map.get(key)[0];
						UFDouble noutassnum_add= map.get(key)[1];
						if(!isAudit){
							noutnum_add = noutnum_add.multiply(-1);
							noutassnum_add = noutassnum_add.multiply(-1);
						}
						body.setNoutnum(noutnum.add(noutnum_add));
						body.setNoutassnum(noutassnum.add(noutassnum_add));
						//校验 不能超量
						UFDouble nlastnum= PuPubVO.getUFDouble_NullAsZero(body.getNlastnum());//暂估欠发量
						UFDouble nreducnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNreducnum());//冲减量
						UFDouble noutnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());//出库量
						if(nlastnum.sub(nreducnum_new).sub(noutnum_new).doubleValue()<0){
							throw new BusinessException("存货:"+getInvCode(key)+"，超过暂估未出库数量");
						}
						falge = true;
					}
				}
				if(!falge){
					throw new BusinessException("存货:"+getInvCode(key)+"，无暂估");
				}
			}
			getHypubBO().updateAry(bodys);
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
