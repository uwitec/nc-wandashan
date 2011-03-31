package nc.bo.other.out;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
/**
 * 
 * @author Administrator
 * 其他出库后台类
 */
public class OtherOutBO  implements IBDBusiCheck{
	
BaseDAO dao = null;
	
	private BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
	}
	public void check(int intBdAction, AggregatedValueObject myBillVO, Object userObj)
			throws Exception {
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO.getChildrenVO();		
		for (int i = 0; i < generalb.length; i++) {
			String sql =" update wds_sendorder_b set ndealnum=coalesce(ndealnum,0)+" +
			PuPubVO.getUFDouble_NullAsZero(generalb[i].getNoutnum())+
			 " where pk_sendorder_b='"+generalb[i].getCsourcebillbid()+"'" +
			 		" and pk_sendorder='"+generalb[i].getCsourcebillhid()+"'";
			getBaseDAO().executeUpdate(sql);		
		}
	}
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
