package nc.impl;

/**
 * business service proxy 
 * Created by EJBGenerator
 * based on velocity technology
 */

public class wds implements 
	nc.itf.wds.w80060604.Iw80060604,nc.itf.wds.w80060210.Iw80060210
,nc.itf.wds.w8004040210.Iw8004040210
,nc.itf.wds.w8004040204.Iw8004040204
{
	
	private nc.itf.wds.w80060604.Iw80060604 iw80060604 = null;
	private nc.itf.wds.w80060210.Iw80060210 iw80060210 = null;
	private nc.itf.wds.w8004040210.Iw8004040210 iw8004040210 = null;
	private nc.itf.wds.w8004040204.Iw8004040204 iw8004040204 = null;
	
	public nc.itf.wds.w80060604.Iw80060604 getIw80060604(){
		return iw80060604;
	}
	public void setIw80060604(nc.itf.wds.w80060604.Iw80060604 iw80060604){
		this.iw80060604 = iw80060604;
	}
	
	public nc.itf.wds.w80060210.Iw80060210 getIw80060210(){
		return iw80060210;
	}
	public void setIw80060210(nc.itf.wds.w80060210.Iw80060210 iw80060210){
		this.iw80060210 = iw80060210;
	}
	
	public nc.itf.wds.w8004040210.Iw8004040210 getIw8004040210(){
		return iw8004040210;
	}
	public void setIw8004040210(nc.itf.wds.w8004040210.Iw8004040210 iw8004040210){
		this.iw8004040210 = iw8004040210;
	}
	
	public nc.itf.wds.w8004040204.Iw8004040204 getIw8004040204(){
		return iw8004040204;
	}
	public void setIw8004040204(nc.itf.wds.w8004040204.Iw8004040204 iw8004040204){
		this.iw8004040204 = iw8004040204;
	}

	public void deleteGeneralTVO(java.util.List arg0) 
		throws java.lang.Exception	{
	getIw8004040204().deleteGeneralTVO(arg0);

	}
	public void insertGeneralTVO(java.util.List arg0 ,java.util.List arg1 ) 
		throws java.lang.Exception	{
	getIw8004040204().insertGeneralTVO(arg0 ,arg1 );

	}
	public void insertWarehousestock(nc.vo.ic.pub.StockInvOnHandVO arg0) 
		throws java.lang.Exception	{
	getIw8004040204().insertWarehousestock(arg0);

	}
	public java.util.List queryGeneralTVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().queryGeneralTVO(arg0 ,arg1  ,arg2 );

	}
	public void updateWarehousestock(nc.vo.ic.pub.StockInvOnHandVO arg0) 
		throws java.lang.Exception	{
	getIw8004040204().updateWarehousestock(arg0);

	}
	public void updateBdcargdocTray(java.lang.String arg0) 
		throws java.lang.Exception	{
	getIw8004040204().updateBdcargdocTray(arg0);

	}
	public void saveGeneralVO(nc.vo.ic.other.out.MyBillVO arg0) 
		throws java.lang.Exception	{
	getIw8004040204().saveGeneralVO(arg0);

	}
	public java.util.Map autoPickAction(java.lang.String arg0 ,nc.vo.ic.other.out.TbOutgeneralBVO[] arg1  ,java.lang.String arg2 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().autoPickAction(arg0 ,arg1  ,arg2 );

	}
	public nc.vo.pub.AggregatedValueObject deleteBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().deleteBD8004040204(arg0 ,arg1 );

	}
	public nc.vo.pub.AggregatedValueObject saveBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().saveBD8004040204(arg0 ,arg1 );

	}
	public java.lang.Object[] getNoutNum(java.lang.String arg0 ,java.lang.String arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().getNoutNum(arg0 ,arg1 );

	}
	public nc.vo.pub.AggregatedValueObject saveBD8004040602(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().saveBD8004040602(arg0 ,arg1 );

	}
	public java.lang.Object whs_processAction(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) 
		throws java.lang.Exception	{
		return 	getIw8004040204().whs_processAction(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );

	}
	public void queryWarehousestock(java.util.List arg0) 
		throws java.lang.Exception	{
	getIw8004040204().queryWarehousestock(arg0);

	}
	public nc.vo.pub.AggregatedValueObject saveBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040210().saveBD(arg0 ,arg1 );

	}
	public int delTbGeneralBBVO(java.util.List arg0) 
		throws nc.vo.pub.BusinessException	{
		return 	getIw8004040210().delTbGeneralBBVO(arg0);

	}
	public int insertTbGeneralBBVO(nc.vo.ic.pub.TbGeneralBBVO[] arg0) 
		throws nc.vo.pub.BusinessException	{
		return 	getIw8004040210().insertTbGeneralBBVO(arg0);

	}
	public int updateBdCargdocTray(java.util.List arg0) 
		throws nc.vo.pub.BusinessException	{
		return 	getIw8004040210().updateBdCargdocTray(arg0);

	}
	public void delAndInsertTbGeneralBBVO(java.util.List arg0 ,nc.vo.ic.pub.TbGeneralBBVO[] arg1 ) 
		throws nc.vo.pub.BusinessException	{
	getIw8004040210().delAndInsertTbGeneralBBVO(arg0 ,arg1 );

	}
	public void canceldelete8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,nc.vo.ic.pub.TbGeneralHVO arg5 ) 
		throws java.lang.Exception	{
	getIw8004040210().canceldelete8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );

	}
	public void pushsavesign8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,nc.vo.pub.AggregatedValueObject arg3  ,nc.vo.ic.pub.TbGeneralHVO arg4 ) 
		throws java.lang.Exception	{
	getIw8004040210().pushsavesign8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4 );

	}
	public nc.vo.pub.AggregatedValueObject deleteBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw8004040210().deleteBD(arg0 ,arg1 );

	}
	public nc.vo.pub.AggregatedValueObject saveAndCommit80060210(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) 
		throws java.lang.Exception	{
		return 	getIw80060210().saveAndCommit80060210(arg0 ,arg1 );

	}
	public void insertFyd(java.util.List arg0 ,java.util.List arg1  ,java.util.List arg2 ) 
		throws java.lang.Exception	{
	getIw80060604().insertFyd(arg0 ,arg1  ,arg2 );

	}
	public void updateSosale(java.util.List arg0) 
		throws java.lang.Exception	{
	getIw80060604().updateSosale(arg0);

	}

}