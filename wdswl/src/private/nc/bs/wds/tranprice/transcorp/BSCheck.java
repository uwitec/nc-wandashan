package nc.bs.wds.tranprice.transcorp;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
/**
 * 
 * �������� ��̨У���� 

 * author:mlr
 * */

public class BSCheck implements IBDBusiCheck {
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
		//У�� ���乫˾��������Ʋ�Ϊ��
		SuperVO voparent = (SuperVO)(vo.getParentVO());
		Object tcode = voparent.getAttributeValue("ctranscorpcode");
		Object tname =voparent.getAttributeValue("vtranscorpname");
		if(BsUniqueCheck.isEmpty(tcode) || BsUniqueCheck.isEmpty(tname)){            	  
			throw new BusinessException("���乫˾��������乫˾���Ʋ���Ϊ��"); 
	    }
		//У�� ���乫˾����Ψһ��
		BsUniqueCheck.FieldUniqueCheck(voparent, "ctranscorpcode", "[���乫˾����] �����ݿ����Ѿ�����");
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
