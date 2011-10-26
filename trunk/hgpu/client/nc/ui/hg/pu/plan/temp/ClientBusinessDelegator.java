package nc.ui.hg.pu.plan.temp;

import java.awt.Container;
import java.util.ArrayList;

import nc.ui.hg.pu.pub.LongTimeTask;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.hg.pu.plan.temp.PlanInventoryVO;
import nc.vo.hg.pu.plan.temp.PlanOtherTempVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;

public class ClientBusinessDelegator extends BusinessDelegator {

	private Container parent = null;
	
	public ClientBusinessDelegator(Container parent ){
		this.parent = parent;
	}
	@Override
	public SuperVO[] queryHeadAllData(Class headClass, String strBillType,
			String strWhere) throws Exception {	
		String where = "";
		if(strWhere!=null && strWhere.length()>0){
			where =  " hg_plan.pk_plan in (select distinct h.pk_plan from hg_plan h join hg_planother_b b " +
			" on h.pk_plan = b.pk_plan join bd_invbasdoc c on c.pk_invbasdoc = b.pk_invbasdoc "+
	        " where isnull(h.dr,0)=0 and isnull(b.dr,0) = 0  and isnull(c.dr,0) = 0 "+
			" and  "+strWhere+") ";
		}
		SuperVO[] vo = HYPubBO_Client.queryByCondition(headClass, where);
		return vo;
	}
	
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		CircularlyAccessibleValueObject[] vos = null;
		if (billType == null || billType.trim().length() == 0)
			return null;
		else {
			vos = (CircularlyAccessibleValueObject[]) HYPubBO_Client.queryAllBodyData(
					billType, voClass, key, strWhere);
			if(vos!=null&&vos.length>0){
					ArrayList<String> linvman = new ArrayList<String>();
					int len =vos.length;
					for(int i=0;i<len;i++){
						linvman.add(PuPubVO.getString_TrimZeroLenAsNull(vos[i].getAttributeValue("cinventoryid")));
					}
					Class[] ParameterTypes = new Class[]{String.class,ArrayList.class};
					Object[] ParameterValues = new Object[]{key,linvman};
					Object o = LongTimeTask.callRemoteService("pu","nc.bs.hg.pu.plan.temp.StockNumBO", "getOnhandNum", ParameterTypes, ParameterValues, 2);
				    if(o!=null){
				    	PlanInventoryVO[] vo= (PlanInventoryVO[])o;
				    	 for(CircularlyAccessibleValueObject c:vos)
					    	((PlanOtherTempVO)c).setInvvos(vo);
				}
			    
			   
			}
		}
		return vos;

	}
}
