package nc.itf;

import nc.bs.framework.server._ServerForEJBNCLocator;
import nc.bs.pub.BusinessObject;

/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public class  wdsEjbBean extends BusinessObject{
	private Object thisService;
	
	public void ejbActivate(){}
	public void ejbPassivate(){}
	public void ejbRemove(){}
	
	public wdsEjbBean() {
		thisService=(Object)_ServerForEJBNCLocator.lookup("nc.itf.wds");
	}
	public void ejbCreate(){
		if(thisService == null)
			thisService=(Object)_ServerForEJBNCLocator.lookup("nc.itf.wds");
	}
	
	public  Object getService() {
		if(thisService == null)
			thisService=(Object)_ServerForEJBNCLocator.lookup("nc.itf.wds");			
		return thisService;
	}
	public void deleteGeneralTVO(java.util.List  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).deleteGeneralTVO(arg0);
		
	}
	public void insertGeneralTVO(java.util.List  arg0 ,java.util.List  arg1 ) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).insertGeneralTVO(arg0 ,arg1 );
		
	}
	public void insertWarehousestock(nc.vo.ic.pub.StockInvOnHandVO  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).insertWarehousestock(arg0);
		
	}
	public java.util.List queryGeneralTVO(java.lang.String  arg0 ,java.lang.String  arg1  ,java.lang.String  arg2 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).queryGeneralTVO(arg0 ,arg1  ,arg2 );		
	}
	public void updateWarehousestock(nc.vo.ic.pub.StockInvOnHandVO  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).updateWarehousestock(arg0);
		
	}
	public void updateBdcargdocTray(java.lang.String  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).updateBdcargdocTray(arg0);
		
	}
	public void saveGeneralVO(nc.vo.ic.other.out.MyBillVO  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).saveGeneralVO(arg0);
		
	}
	public java.util.Map autoPickAction(java.lang.String  arg0 ,nc.vo.ic.other.out.TbOutgeneralBVO[]  arg1  ,java.lang.String  arg2 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).autoPickAction(arg0 ,arg1  ,arg2 );		
	}
	public nc.vo.pub.AggregatedValueObject deleteBD8004040204(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).deleteBD8004040204(arg0 ,arg1 );		
	}
	public nc.vo.pub.AggregatedValueObject saveBD8004040204(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).saveBD8004040204(arg0 ,arg1 );		
	}
	public java.lang.Object[] getNoutNum(java.lang.String  arg0 ,java.lang.String  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).getNoutNum(arg0 ,arg1 );		
	}
	public nc.vo.pub.AggregatedValueObject saveBD8004040602(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).saveBD8004040602(arg0 ,arg1 );		
	}
	public java.lang.Object whs_processAction(java.lang.String  arg0 ,java.lang.String  arg1  ,java.lang.String  arg2  ,java.lang.String  arg3  ,nc.vo.pub.AggregatedValueObject  arg4  ,java.lang.Object  arg5 ) throws Exception{
		
		return ((nc.itf.wds.w8004040204.Iw8004040204 )getService()).whs_processAction(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );		
	}
	public void queryWarehousestock(java.util.List  arg0) throws Exception{
		
		((nc.itf.wds.w8004040204.Iw8004040204 )getService()).queryWarehousestock(arg0);
		
	}
	public nc.vo.pub.AggregatedValueObject saveBD(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040210.Iw8004040210 )getService()).saveBD(arg0 ,arg1 );		
	}
	public int delTbGeneralBBVO(java.util.List  arg0) throws Exception{
		
		return ((nc.itf.wds.w8004040210.Iw8004040210 )getService()).delTbGeneralBBVO(arg0);		
	}
	public int insertTbGeneralBBVO(nc.vo.ic.pub.TbGeneralBBVO[]  arg0) throws Exception{
		
		return ((nc.itf.wds.w8004040210.Iw8004040210 )getService()).insertTbGeneralBBVO(arg0);		
	}
	public int updateBdCargdocTray(java.util.List  arg0) throws Exception{
		
		return ((nc.itf.wds.w8004040210.Iw8004040210 )getService()).updateBdCargdocTray(arg0);		
	}
	public void delAndInsertTbGeneralBBVO(java.util.List  arg0 ,nc.vo.ic.pub.TbGeneralBBVO[]  arg1 ) throws Exception{
		
		((nc.itf.wds.w8004040210.Iw8004040210 )getService()).delAndInsertTbGeneralBBVO(arg0 ,arg1 );
		
	}
	public void canceldelete8004040210(java.lang.String  arg0 ,java.lang.String  arg1  ,java.lang.String  arg2  ,java.lang.String  arg3  ,nc.vo.pub.AggregatedValueObject  arg4  ,nc.vo.ic.pub.TbGeneralHVO  arg5 ) throws Exception{
		
		((nc.itf.wds.w8004040210.Iw8004040210 )getService()).canceldelete8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );
		
	}
	public void pushsavesign8004040210(java.lang.String  arg0 ,java.lang.String  arg1  ,java.lang.String  arg2  ,nc.vo.pub.AggregatedValueObject  arg3  ,nc.vo.ic.pub.TbGeneralHVO  arg4 ) throws Exception{
		
		((nc.itf.wds.w8004040210.Iw8004040210 )getService()).pushsavesign8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4 );
		
	}
	public nc.vo.pub.AggregatedValueObject deleteBD(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w8004040210.Iw8004040210 )getService()).deleteBD(arg0 ,arg1 );		
	}
	public nc.vo.pub.AggregatedValueObject saveAndCommit80060210(nc.vo.pub.AggregatedValueObject  arg0 ,java.lang.Object  arg1 ) throws Exception{
		
		return ((nc.itf.wds.w80060210.Iw80060210 )getService()).saveAndCommit80060210(arg0 ,arg1 );		
	}
	public void insertFyd(java.util.List  arg0 ,java.util.List  arg1  ,java.util.List  arg2 ) throws Exception{
		
		((nc.itf.wds.w80060604.Iw80060604 )getService()).insertFyd(arg0 ,arg1  ,arg2 );
		
	}
	public void updateSosale(java.util.List  arg0) throws Exception{
		
		((nc.itf.wds.w80060604.Iw80060604 )getService()).updateSosale(arg0);
		
	}
}
	