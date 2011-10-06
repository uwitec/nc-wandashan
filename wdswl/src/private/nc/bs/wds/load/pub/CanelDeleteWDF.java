package nc.bs.wds.load.pub;
import java.util.ArrayList;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.BillQueryTool;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 出入库 取消签字 删除装卸费核算单
 * 如果要删除的装卸费核算单已经被审批
 * 则 不允许取消签字
 * @author mlr
 */
public class CanelDeleteWDF {
	private BaseDAO dao=null;
	private BaseDAO getDao(){
		if(dao==null){
		   dao=new BaseDAO();
		}
		return dao;
	}	
	public void canelDeleteWDF(AggregatedValueObject value,String coperator,String date) throws Exception {
		if(value == null ){
			return ;
		}
		String currbilltype = (String)value.getParentVO().getAttributeValue("vbilltype");
		if(currbilltype == null || "".equals(currbilltype)){
			currbilltype=(String)value.getParentVO().getAttributeValue("geh_billtype");
		    if(currbilltype == null || "".equals(currbilltype))
			return ;
		}
		SuperVO vo = (SuperVO) value.getParentVO();
		String currbillid=vo.getPrimaryKey();
		nc.vo.trade.billsource.LightBillVO[] lvos=BillQueryTool.getForwardBills(currbilltype, currbillid,WdsWlPubConst.WDSF);//只查装卸费核算单
		if(lvos==null || lvos.length==0){
			return;
		}
		//一张出入库单子  只能生成一张下游核算单
		String billid=lvos[0].getID();
		ExaggLoadPricVO[] billvos = null;//装卸费核算单聚合vo
		String where  = " pk_loadprice = '"+billid+"' ";
	    billvos=queryWDSF(where);
	    if(billvos!=null && billvos.length!=0){
	    	for(int i=0;i<billvos.length;i++){
	    	 Integer billstate=PuPubVO.getInteger_NullAs(billvos[i].getParentVO().getAttributeValue("vbillstatus"), new Integer(-1));
	    	 if(billstate==IBillStatus.CHECKPASS){
	    		 throw new Exception(" 下游装卸费核算单已经审批，不能取消签字");
	    	 }		 		
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());		
			bsBusiAction.processAction("DELETE",WdsWlPubConst.WDSF,date,null,billvos[i], null,null);			    		
	    	}
	    }	  
	}
 
	private ExaggLoadPricVO[] queryWDSF(String where) throws Exception {
		ExaggLoadPricVO[] billvos=null;
		List  list=(List) getDao().retrieveByClause(LoadpriceHVO.class, where);
		if(list ==null || list.size()==0)
			return null;
		SuperVO[] hvos=(SuperVO[]) list.toArray(new LoadpriceHVO[0]);
		billvos=new ExaggLoadPricVO[hvos.length];
		if(hvos==null || hvos.length==0){
			return null;
		}	
		 billvos=new ExaggLoadPricVO[hvos.length];
		for(int i=0;i<hvos.length;i++){
			billvos[i]=new ExaggLoadPricVO();
			List list1=(List) getDao().retrieveByClause(LoadpriceB1VO.class," pk_loadprice='"+hvos[i].getPrimaryKey()+"'");
			SuperVO[] bvos1=null;
			if(list1 !=null && list1.size()!=0)
			bvos1=(SuperVO[]) list1.toArray(new LoadpriceB1VO[0]);
			List list2=(List) getDao().retrieveByClause(LoadpriceB2VO.class," pk_loadprice='"+hvos[i].getPrimaryKey()+"'");
			SuperVO[] bvos2=null;
			if(list2 !=null && list2.size()!=0)
			bvos2=(SuperVO[]) list2.toArray(new LoadpriceB2VO[0]);	
			billvos[i].setParentVO(hvos[i]);
			billvos[i].setChildrenVO(bvos1);
			billvos[i].setTableVO(billvos[i].getTableCodes()[0],bvos1);
			billvos[i].setTableVO(billvos[i].getTableCodes()[1],bvos2);
		}
		return billvos;
	}
}
