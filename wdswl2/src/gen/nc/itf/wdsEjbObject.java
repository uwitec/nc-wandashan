package nc.itf;

/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public interface wdsEjbObject extends  javax.ejb.EJBLocalObject,nc.itf.wds.w8004040204.Iw8004040204,nc.itf.wds.w8004040210.Iw8004040210,nc.itf.wds.w80060210.Iw80060210,nc.itf.wds.w80060604.Iw80060604{
	public void deleteGeneralTVO(java.util.List arg0) throws java.lang.Exception;
	public void insertGeneralTVO(java.util.List arg0 ,java.util.List arg1 ) throws java.lang.Exception;
	public void insertWarehousestock(nc.vo.ic.pub.StockInvOnHandVO arg0) throws java.lang.Exception;
	public java.util.List queryGeneralTVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2 ) throws java.lang.Exception;
	public void updateWarehousestock(nc.vo.ic.pub.StockInvOnHandVO arg0) throws java.lang.Exception;
	public void updateBdcargdocTray(java.lang.String arg0) throws java.lang.Exception;
	public void saveGeneralVO(nc.vo.ic.other.out.MyBillVO arg0) throws java.lang.Exception;
	public java.util.Map autoPickAction(java.lang.String arg0 ,nc.vo.ic.other.out.TbOutgeneralBVO[] arg1  ,java.lang.String arg2 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject deleteBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public java.lang.Object[] getNoutNum(java.lang.String arg0 ,java.lang.String arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD8004040602(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public java.lang.Object whs_processAction(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) throws java.lang.Exception;
	public void queryWarehousestock(java.util.List arg0) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public int delTbGeneralBBVO(java.util.List arg0) throws nc.vo.pub.BusinessException;
	public int insertTbGeneralBBVO(nc.vo.ic.pub.TbGeneralBBVO[] arg0) throws nc.vo.pub.BusinessException;
	public int updateBdCargdocTray(java.util.List arg0) throws nc.vo.pub.BusinessException;
	public void delAndInsertTbGeneralBBVO(java.util.List arg0 ,nc.vo.ic.pub.TbGeneralBBVO[] arg1 ) throws nc.vo.pub.BusinessException;
	public void canceldelete8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,nc.vo.ic.pub.TbGeneralHVO arg5 ) throws java.lang.Exception;
	public void pushsavesign8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,nc.vo.pub.AggregatedValueObject arg3  ,nc.vo.ic.pub.TbGeneralHVO arg4 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject deleteBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveAndCommit80060210(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public void insertFyd(java.util.List arg0 ,java.util.List arg1  ,java.util.List arg2 ) throws java.lang.Exception;
	public void updateSosale(java.util.List arg0) throws java.lang.Exception;
}