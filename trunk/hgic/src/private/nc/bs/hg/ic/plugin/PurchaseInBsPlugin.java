package nc.bs.hg.ic.plugin;

import java.util.HashMap;
import java.util.Map;

import nc.bs.hg.ic.pub.HgICPubBO;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.bill.GeneralBillDMO;
import nc.bs.scm.plugin.IScmBSPlugin;
import nc.bs.scm.plugin.SCMBsContext;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.PuPubVO;

public class PurchaseInBsPlugin implements IScmBSPlugin {
	private Map<String, GeneralBillItemVO[]> m_oldItemsInfor = null;
	HgScmPubBO bo = null;
	private HgScmPubBO getScmPubBO(){
		if(bo == null){
			bo = new HgScmPubBO();
		}
		return bo;
	}
	
	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		// TODO Auto-generated method stub
		if(billvos == null||billvos.length==0)
			return;
		GeneralBillVO[] gbillvos = (GeneralBillVO[])billvos;
//		HgICPubBO bo = new HgICPubBO();
		if(action==Action.AUDIT){
			//���ǩ��			
//			bo.checkAndUseFund((GeneralBillVO[])billvos, true,false);
		}else if(action == Action.UNAUDIT){
			//���ȡ��ǩ��
//			bo.checkAndUseFund((GeneralBillVO[])billvos, false,true);
		}else if(action == Action.SAVE){
			 //�����ݲ����   ֻ���Ͽ���
			for(GeneralBillVO gbillvo:gbillvos){
				if(!HgPubConst.SELF.equals(gbillvo.getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF))){
					getScmPubBO().checkAllowanceForICin(gbillvo);
				}	
			}
			if(PuPubVO.getString_TrimZeroLenAsNull(gbillvos[0].getHeaderVO().getPrimaryKey())==null)
				return;
			 GeneralBillDMO dmo = null; 
			try{
				dmo = new GeneralBillDMO();
			}catch(Exception e){
				if(e instanceof BusinessException)
					throw (BusinessException)e;
				throw new BusinessException(e);
			}
			
			String headpk = null;
			GeneralBillItemVO[] tmpitems = null;
			if(m_oldItemsInfor==null)
				m_oldItemsInfor = new HashMap<String, GeneralBillItemVO[]>();
				else 
					m_oldItemsInfor.clear();
		
			for(GeneralBillVO vo:gbillvos){
				headpk = PuPubVO.getString_TrimZeroLenAsNull(vo.getHeaderVO().getPrimaryKey());
				if(headpk == null)
					continue;
				tmpitems = (GeneralBillItemVO[])dmo.queryAllBodyData(headpk);
				if(tmpitems == null||tmpitems.length==0)
					continue;
				m_oldItemsInfor.put(headpk, tmpitems);	
			}
//			GeneralBillVO[] gbills = (GeneralBillVO[])billvos;
//			bo.rewriteBackArrNum(gbills[0].getItemVOs(), gbills[1]==null?null:gbills[1].getItemVOs(), true);
		}else if(action == Action.DELETE){
//			bo.rewriteBackArrNum(inCurVO.getItemVOs(), inPreVO==null?null:inPreVO.getItemVOs(), true);
		}

	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		if(billvos == null||billvos.length==0)
			return;
		HgICPubBO bo = new HgICPubBO();
        if(action == Action.SAVE){
			GeneralBillVO[] gbills = (GeneralBillVO[])billvos;
//			if(gbills.length==1){
//				bo.rewriteBackArrNum(gbills[0].getItemVOs(), null, true);
//			}else if(gbills.length==2){
//				bo.rewriteBackArrNum(gbills[0].getItemVOs(), gbills[1]==null?null:gbills[1].getItemVOs(), true);
//			}
			String self =HgPubTool.getString_NullAsTrimZeroLen(gbills[0].getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF));
			if(HgPubConst.SELF.equalsIgnoreCase(self)){
				if(HgPubTool.getPurchaseInBusiStatus(gbills[0])==HgPubConst.purchaseIn_arr){
					//Ӧ������ֻ����   ��һ�ε�ʱ������޸�
					bo.rewriteBackArrNum(gbills[0].getItemVOs(), null, true);
				}
			}else{
				// add by zhw 2011-01-13 22:30 ���ղɹ�����������ⵥ ɾ��ʱ��д�ۼ�����
					bo.rewriteBackNinNum(gbills[0].getItemVOs(), m_oldItemsInfor==null?null:m_oldItemsInfor.get(gbills[0].getHeaderVO().getPrimaryKey()),true);
			}
			
		}else if(action == Action.DELETE){
			GeneralBillVO[] gbills = (GeneralBillVO[])billvos;
			String self =HgPubTool.getString_NullAsTrimZeroLen(gbills[0].getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF));
			if(HgPubConst.SELF.equalsIgnoreCase(self)){
			    //Ӧ������ֻ����   ��һ�ε�ʱ������޸�
				bo.rewriteBackArrNum(gbills[0].getItemVOs(), null, false);
			}else{ 
				// add by zhw 2011-01-13 22:30 ���ղɹ�����������ⵥ ���������Ϊ�� ��дʵ������  
				// ���������Ϊ��  ��д������� ��ֹû������������Զ�β��ղɹ�����
				bo.rewriteBackNinNum(gbills[0].getItemVOs(),null,false);
			}
		}
	}

	public AggregatedValueObject[] checkOutVO(AggregatedValueObject[] avos)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}