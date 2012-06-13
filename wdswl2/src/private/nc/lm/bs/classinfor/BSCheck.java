package nc.lm.bs.classinfor;
import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBDACTION;
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
		 //�༶�� ���ݿ�Ψһ ������ָ��˾����
		if(vo.getParentVO()==null){
			return;
		}				
		BsUniqueCheck.FieldUniqueChecks(new SuperVO[]{(SuperVO) vo.getParentVO()},new String[]{"pk_corp","cclasscode"},"","�༶��������ݿ����Ѿ�����");	
		//ѧ�� ���ݿ�Ψһ������ָ��˾����	
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}
		BsUniqueCheck.FieldUniqueChecks(vo,new String[]{"ccstucode"}," and pk_corp='"+vo.getParentVO().getAttributeValue("pk_corp")+"'","","ѧ����������ݿ����Ѿ�����");	
		}
	}
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
	}
}
