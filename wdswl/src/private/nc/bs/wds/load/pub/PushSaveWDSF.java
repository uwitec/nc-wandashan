package nc.bs.wds.load.pub;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.billcodemanage.BillcodeGenerater;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wds.load.account.LoadAccountBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * �����ǩ�� ��ʽ�����γ�װж�Ѻ��㵥
 * @author mlr
 */
public class PushSaveWDSF {
	private LoadAccountBS  lbs=null; 
	private BillcodeGenerater billcode;
	private nc.bs.wl.pub.WdsWlPubBO bo;
	public nc.bs.wl.pub.WdsWlPubBO getBo(){
		if(bo==null){
		  bo=new nc.bs.wl.pub.WdsWlPubBO();
		}
		return bo;
	}
	public  LoadAccountBS getBs(){
		if(lbs==null){
			lbs=new LoadAccountBS();
		}
		return lbs;
	} 
	
	public BillcodeGenerater  getBillcode(){
		if(billcode==null){
			billcode=new BillcodeGenerater();
		}
		return billcode;
	}
	
	 /**
     * ͨ������ⵥvo���װж�Ѻ��㵥vo
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ 
     * @ʱ�䣺2011-9-22����07:55:38
     * @throws Exception
     */
	public AggregatedValueObject getPushSaveWDSF(AggregatedValueObject billVO,String coperator,String date,int lodytype)throws Exception{
		if(billVO==null){
			return null;
		}
		String pk_billtype = (String)billVO.getParentVO().getAttributeValue("vbilltype");
		if(pk_billtype == null || "".equals(pk_billtype)){
			pk_billtype=(String)billVO.getParentVO().getAttributeValue("geh_cbilltypecode");
			if(pk_billtype == null || "".equals(pk_billtype))
			return null;
		}
		AggregatedValueObject vo = PfUtilTools.runChangeData(pk_billtype, WdsWlPubConst.WDSF, billVO,null); //���۳���
		ExaggLoadPricVO epvo = (ExaggLoadPricVO)setInfor(vo,coperator,date,lodytype);
		return epvo;
	}
	/**
	 * ��ʽ�γ�����װж�Ѻ��㵥
	 * lodytype 0Ϊ����װж����
	 *          1Ϊ���װж����
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-9-23����09:53:23
	 * @param billVO
	 * @param coperator
	 * @param date
	 * @throws Exception
	 */
	public void  pushSaveWDSF(AggregatedValueObject billVO,String coperator,String date,int lodytype)throws Exception{
		AggregatedValueObject bill=getPushSaveWDSF(billVO,coperator,date,lodytype);	   
		IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator.getInstance().lookup(IPFBusiAction.class.getName());		
		bsBusiAction.processAction("SAVE",WdsWlPubConst.WDSF,date,null,bill, null,null);		
	}
	/**
	 * ������ʽ�����γɵĺ��㵥��vo��Ϣ
	 * ����vo��Ϣ ������װж����
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-9-22����08:05:12
	 * @param vo
	 * @throws Exception 
	 * @throws BusinessException 
	 */
	private AggregatedValueObject setInfor(AggregatedValueObject billVO,String coperator,String date,int lodytype) throws BusinessException, Exception {
		if(billVO != null && billVO instanceof ExaggLoadPricVO){
			ExaggLoadPricVO bill = (ExaggLoadPricVO)billVO;
			String pk_corp=PuPubVO.getString_TrimZeroLenAsNull(bill.getParentVO().getAttributeValue("pk_corp"));
			
			getBs().accoutLoadPrice(bill,pk_corp, getBo().getLogInfor(coperator,pk_corp).getWhid(),lodytype);	
			//�����������ݵĻ�����Ϣ
			bill.getParentVO().setAttributeValue("vbillstatus", IBillStatus.FREE);
			bill.getParentVO().setAttributeValue("pk_billtype", WdsWlPubConst.WDSF);
			bill.getParentVO().setAttributeValue("dbilldate", new UFDate(date.trim()));
			bill.getParentVO().setAttributeValue("voperatorid", coperator);
			bill.getParentVO().setAttributeValue("dmakedate", new UFDate(date.trim()));
			bill.getParentVO().setStatus(VOStatus.NEW);//����vo״̬	         
			if(bill.getTableVO("wds_loadprice_b1")!=null && bill.getTableVO("wds_loadprice_b1").length!=0){
		    	 LoadpriceB1VO[]  vss=(LoadpriceB1VO[]) bill.getTableVO("wds_loadprice_b1");		    	
		    	 for(int i=0;i<vss.length;i++){   		    		
		    		 vss[i].setStatus(VOStatus.NEW);
		    	 } 	 
		     }
		     if(bill.getTableVO("wds_loadprice_b2")!=null && bill.getTableVO("wds_loadprice_b2").length!=0){
		    	 LoadpriceB2VO[]  vss1=(LoadpriceB2VO[]) bill.getTableVO("wds_loadprice_b2");
		    	 for(int i=0;i<vss1.length;i++){   		    	
		    		 vss1[i].setStatus(VOStatus.NEW);
		    	 }	    	 
		     }
		     String billcode=getBillcode().getBillCode(WdsWlPubConst.WDSF, pk_corp, null, null);
		     bill.getParentVO().setAttributeValue("vbillno", billcode);
		     return bill;
		}  
		return null;
		 
	}

}
