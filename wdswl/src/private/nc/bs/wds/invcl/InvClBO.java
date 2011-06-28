package nc.bs.wds.invcl;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.invcl.WdsInvClVO;

public class InvClBO implements IBDBusiCheck {

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		// TODO Auto-generated method stub
//  zhf  保存前校验  编码不能重复   名称不能重复  不能为空    编码规则   ##--##--##  编码规则一致性校验
		if(vo == null || vo.getParentVO() == null)
			return;
		if(intBdAction == IBDACTION.SAVE){
			WdsInvClVO head = (WdsInvClVO)vo.getParentVO();
			head.validate();
//			校验编码是否和父类保持一致性  编码位数  必须为  两位  两位
			String code = head.getVinvclcode();
			String fathercode = getInvclCodeByKey(head.getPk_father(), head.getPk_corp());
			if(fathercode == null)
				throw new BusinessException("数据异常，获取父类编码为空");
			checkCode(code, fathercode, head.getPrimaryKey(),head.getPk_corp());
		}else if(intBdAction == IBDACTION.DELETE){
//			校验如果存在下级节点 不能删除
			WdsInvClVO head = (WdsInvClVO)vo.getParentVO();
			String sql = "select count(0) from wds_invcl where isnull(dr,0) = 0 and pk_corp = '"+head.getPk_corp()+"' and pk_father = '"+head.getPrimaryKey()+"'";
			if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0)>0){
				throw new BusinessException("存在下级节点");
			}
		}
	}
	
	private void checkCode(String code,String fathercode,String key,String corp) throws Exception{
//		首先校验编码不能重复
		StringBuffer str = new StringBuffer();
		str.append("select count(0) from wds_invcl where isnull(dr,0)= 0 ");
		str.append(" and pk_corp = '"+corp+"' and vinvclcode = '"+code+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(key)!=null){
			str.append(" pk_invcl <> '"+key+"'");
		}
		double a = code.length()/2;
		int b = code.length()/2;
		if(Math.abs(a-b)>0)
			throw new BusinessException("分类编码不符合规则，XX--XX--XX");
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(str.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0)>0){
			throw new BusinessException("分类编码重复");
		}
		if(!code.startsWith(fathercode)){
			throw new BusinessException("和父类编码不一致");
		}
		if(code.length()-fathercode.length()!=2){
			throw new BusinessException("分类编码不符合规则，XX--XX--XX");
		}
	}
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	
	private String getInvclCodeByKey(String key,String logcorp) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null)
			return null;
		String sql = "select vinvclcode from wds_invcl where isnull(dr,0) = 0 and pk_corp = '"+logcorp+"' and pk_invcl = '"+key+"'";
		
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
// 保存后校验
	}

}
