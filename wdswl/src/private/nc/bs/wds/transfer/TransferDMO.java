package nc.bs.wds.transfer;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYSuperDMO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.field.IBillField;
import nc.vo.wds.transfer.BillField;

public class TransferDMO extends HYSuperDMO {
	private BaseDAO dao = null;
	BaseDAO getBaseDAO(){
		if(dao==null){
			dao = new BaseDAO();
		}
		return dao;
}
	@Override
	protected IBillField createBillField() throws Exception {
		return BillField.getInstance();
	}
	public void beforeUnApprove(String pk) throws BusinessException{
		String sql = " select count(0) from tb_outgeneral_b where csourcebillhid='"+pk+"' and isnull(dr,0)=0";
		Integer i =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR),0);
		if(i>0){
			throw new BusinessException("存在下游其他出库单,请先删除下游单据再操作");
		}
	}
}
