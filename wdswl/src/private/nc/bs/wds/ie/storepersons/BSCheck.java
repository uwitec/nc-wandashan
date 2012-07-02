package nc.bs.wds.ie.storepersons;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.ie.storepersons.TbStockstaffVO;
/**
 * @author mlr
 * ��̨У����
 *  ����ִ��ǰ ִ��check����
 *  ������ AggregatedValueObject ������ͨ��
 *  getBillUI().getChangedVOFromUI()�����õ���
 *  �ۺ�vo
 */
public class BSCheck implements IBDBusiCheck{
	private BaseDAO dao=null;

	private BaseDAO getDao(){
		if(dao==null){
			dao=new BaseDAO();
		}
		return dao;	   
	}
	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
	throws Exception {
		if(intBdAction==IBDACTION.SAVE){
			if(vo==null){
				return;
			}
			TbStockstaffVO head=(TbStockstaffVO)vo.getParentVO();
			if(head ==null)
				return;
			BsUniqueCheck.FieldUniqueCheck((SuperVO)head, TbStockstaffVO.uniquecheckfields,"����Ա�Ѿ��󶨹���λ");//modify by yf 2012-06-26���� ����Ա+��˾�� ΨһУ��
		}
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {


	}

}
