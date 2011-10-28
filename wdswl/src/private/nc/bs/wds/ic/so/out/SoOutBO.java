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
 * 销售出库(WDS8)
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
		// 销售出库签字
		if(billvo != null && billvo instanceof GeneralBillVO){
			GeneralBillVO billVO = (GeneralBillVO)billvo;
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
			//pushsave ，存在按表头仓库进行分单，没有必要进行分单
			ArrayList retList = (ArrayList)bsBusiAction.processAction("PUSHWRITE", s_billtype,date,null,billVO, null,null);
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			billVO.setSmallBillVO(smbillvo);
			//签字检查 <->[签字日期和表体业务日期]
			//当前操作人<->[业务加锁，锁定当前操作人员]
			//空货位检查 bb1表
			bsBusiAction.processAction("SIGN", s_billtype,date,null,billVO, null,null);
			//推式保存形成装卸费核算单
		//	getPuf().pushSaveWDSF(smbillvo, coperator, date, LoadAccountBS.LOADFEE);
		}		
	}
	public void canelPushSign4C(String date, AggregatedValueObject[] billvo) throws Exception {
		//取消销售出库签字
		if(billvo != null && billvo[0]!= null && billvo[0] instanceof GeneralBillVO){
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());
		for(int i=0;i<billvo.length;i++){
		//	ArrayList retList = (ArrayList)bsBusiAction.processAction("CANCELSIGN", s_billtype,date,null,billvo[i], null,null);
		//	if(retList.get(0) !=null && (Boolean)retList.get(0)){//取消签字成功
				bsBusiAction.processBatch("CANELDELETE", s_billtype, date, billvo, null, null);//执行删除
		//	}
		   }
		}
	}
}