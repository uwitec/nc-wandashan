package nv.bs.wds2.inv;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.wds2.inv.StatusUpdateBillVO;
import nc.vo.wds2.inv.StatusUpdateBodyVO;
import nc.vo.wds2.inv.StatusUpdateHeadVO;

public class StatusUpdateBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}

	public void checkDataOnSave(StatusUpdateBillVO bill) throws BusinessException{
		if(bill == null)
			throw new BusinessException("数据为空");
		StatusUpdateHeadVO head = bill.getHeader();
		head.validation();
		StatusUpdateBodyVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("表体数据为空");
		
//		对表体数据校验：
	}
}
