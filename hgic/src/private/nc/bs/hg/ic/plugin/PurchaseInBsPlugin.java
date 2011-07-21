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
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
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
         if(action == Action.SAVE){
			 //出库容差控制   只向上控制
			for(GeneralBillVO gbillvo:gbillvos){
				if(!HgPubConst.SELF.equals(gbillvo.getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF))){
					getScmPubBO().checkAllowanceForICin(gbillvo);
				}	
				HgICPubBO bo  = new HgICPubBO();
				bo.checkDatas(gbillvo);
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
		}
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			Object otherparam, SCMBsContext conx) throws BusinessException {
		if(billvos == null||billvos.length==0)
			return;
		HgICPubBO bo = new HgICPubBO();
        if(action == Action.SAVE){
			GeneralBillVO[] gbills = (GeneralBillVO[])billvos;
			String self =HgPubTool.getString_NullAsTrimZeroLen(gbills[0].getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF));
			if(HgPubConst.SELF.equalsIgnoreCase(self)){
					//应收数量只有在   第一次的时候可以修改
				bo.rewriteBackArrNum(gbills[0].getItemVOs(), m_oldItemsInfor==null?null:m_oldItemsInfor.get(gbills[0].getHeaderVO().getPrimaryKey()), true);
			}else{
				// add by zhw 2011-01-13 22:30 参照采购订单生成入库单 删除时回写累计数量
				bo.rewriteBackNinNum(gbills[0].getItemVOs(), m_oldItemsInfor==null?null:m_oldItemsInfor.get(gbills[0].getHeaderVO().getPrimaryKey()),true);
			}
			GeneralBillHeaderVO  head =(GeneralBillHeaderVO)gbills[0].getHeaderVO();
			GeneralBillItemVO[] items =(GeneralBillItemVO[])gbills[0].getItemVOs();
			String cwarehouseid = head.getCwarehouseid();
			for(GeneralBillItemVO item : items){
				if(!PuPubVO.getUFDouble_NullAsZero(item.getNinnum()).equals(UFDouble.ZERO_DBL)&& bo.getCsflag(cwarehouseid)){
					if(PuPubVO.getString_TrimZeroLenAsNull(item.getCspaceid())==null
							||"_________N/A________".equals(item.getCspaceid()))
						throw  new BusinessException("行号："+item.getCrowno()+"货位不能为空");
					
				}
			}
		}else if(action == Action.DELETE){
			GeneralBillVO[] gbills = (GeneralBillVO[])billvos;
			String self =HgPubTool.getString_NullAsTrimZeroLen(gbills[0].getHeaderVO().getAttributeValue(HgPubConst.F_IS_SELF));
			if(HgPubConst.SELF.equalsIgnoreCase(self)){
			    //应收数量只有在   第一次的时候可以修改
				bo.rewriteBackArrNum(gbills[0].getItemVOs(), null, false);
			}else{ 
				// add by zhw 2011-01-13 22:30 参照采购订单生成入库单 当入库数量为空 回写实收数量  
				// 入库数量不为空  回写入库数量 防止没有入库数量可以多次参照采购订单
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
