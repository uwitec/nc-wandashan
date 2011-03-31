package nc.bo.other.out;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
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
		TbOutgeneralHVO generalh = (TbOutgeneralHVO)myBillVO.getParentVO();
		if(generalh.getStatus()== VOStatus.UPDATED){
			return ;
		}
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[]) myBillVO.getChildrenVO();		
		for (int i = 0; i < generalb.length; i++) {
			String sql =" update wds_sendorder_b set noutnum=coalesce(noutnum,0)+" +
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
