package nc.bs.wds.ic.so.out;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.SuperDMO;
import nc.bs.wds.load.pub.pushSaveWDSF;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
/**
 * ���۳���(WDS8)
 * @author Administrator
 */
public class SoOutBO  {
	private SuperDMO dmo = new SuperDMO();	
	private String s_billtype = "4C";	
	private pushSaveWDSF puf=null;
	public pushSaveWDSF getPuf(){
		if(puf==null){
			puf=new pushSaveWDSF();
		}
		return puf;
	}
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
	public void pushSign4C(String date,String coperator,AggregatedValueObject billvo) throws Exception {
		// ���۳���ǩ��
		if(billvo != null && billvo instanceof GeneralBillVO){
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			//pushsave �����ڰ���ͷ�ֿ���зֵ���û�б�Ҫ���зֵ�
			ArrayList retList = (ArrayList)bsBusiAction.processAction("PUSHWRITE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//ǩ�ּ�� <->[ǩ�����ںͱ���ҵ������]
			//��ǰ������<->[ҵ�������������ǰ������Ա]
			//�ջ�λ��� bb1��
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null);
			//��ʽ�����γ�װж�Ѻ��㵥
		//	getPuf().pushSaveWDSF(smbillvo, coperator, date, LoadAccountBS.LOADFEE);
		}		
	}
	public void canelPushSign4C(String date, AggregatedValueObject[] billvo) throws Exception {
		//ȡ�����۳���ǩ��
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
		for(int i=0;i<billvo.length;i++){
		//	ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
		//	if(retList.get(0) !=null && (Boolean)retList.get(0)){//ȡ��ǩ�ֳɹ�
				bsBusiAction.processBatch("CANELDELETE", s_billtype, date, billvo, null, null);//ִ��ɾ��
		//	}
		   }
		}
	}
}