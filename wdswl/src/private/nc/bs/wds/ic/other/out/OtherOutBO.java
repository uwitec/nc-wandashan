package nc.bs.wds.ic.other.out;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SuperDMO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scm.pu.PuPubVO;
/**
 * ��������(WDS8)
 * @author Administrator
 */
public class OtherOutBO  {
	
	private String s_billtype = "4I";
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	private SuperDMO dmo = new SuperDMO();
	
	public void updateHVO(TbOutgeneralHVO hvo) throws BusinessException{
		if(hvo == null){
			return;
		}
		if(hvo.getPrimaryKey() != null)
			hvo.setStatus(VOStatus.UPDATED);
		else
			hvo.setStatus(VOStatus.NEW);
		dmo.update(hvo);
	}

	
	public void pushSign4I(String date, AggregatedValueObject billvo) throws Exception {
		// ��������ǩ��
		if(billvo != null && billvo instanceof GeneralBillVO){
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList)bsBusiAction.processAction("SAVE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//ǩ�ּ�� <->[ǩ�����ںͱ���ҵ������]
			//��ǰ������<->[ҵ�������������ǰ������Ա]
			//�ջ�λ��� bb1��
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null);
		}
		
	}
	
	public void canelPushSign4I(String date, AggregatedValueObject[] billvo) throws Exception {
		//ȡ����������ǩ��
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			for(int i = 0 ;i < billvo.length;i++){
				ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
				if(retList.get(0) !=null && (Boolean)retList.get(0)){//ȡ��ǩ�ֳɹ�
					String sql = "select ts from ic_general_h where cgeneralhid = '"
						+ billvo[i].getParentVO().getPrimaryKey() + "'";
				String ts = PuPubVO.getString_TrimZeroLenAsNull(getBaseDAO()
						.executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
				billvo[i].getParentVO().setAttributeValue("ts", new UFDateTime(ts));
					bsBusiAction.processAction("DELETE", s_billtype,date,null,billvo[i], null,null);//ִ��ɾ��
				}
			}
		}
	}
}