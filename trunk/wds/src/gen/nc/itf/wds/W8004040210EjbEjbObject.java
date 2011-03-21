package nc.itf.wds;

/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public interface W8004040210EjbEjbObject extends  javax.ejb.EJBLocalObject,nc.itf.wds.w80020202.Iw80020202,nc.itf.wds.w80020206.Iw80020206,nc.itf.wds.w8004040204.Iw8004040204,nc.itf.wds.w8004040210.Iw8004040210,nc.itf.wds.w80060202.Iw80060202,nc.itf.wds.w80060204.Iw80060204,nc.itf.wds.w80060208.Iw80060208,nc.itf.wds.w80060210.Iw80060210,nc.itf.wds.w80060401.Iw80060401,nc.itf.wds.w80060406.Iw80060406,nc.itf.wds.w80060408.Iw80060408,nc.itf.wds.w80060604.Iw80060604{
	public nc.vo.pub.AggregatedValueObject saveBD80020202(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public java.lang.Object whs_processAction80020206(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) throws java.lang.Exception;
	public void deleteGeneralTVO(java.util.List arg0) throws java.lang.Exception;
	public void insertGeneralTVO(java.util.List arg0 ,java.util.List arg1 ) throws java.lang.Exception;
	public void insertWarehousestock(nc.vo.wds.w8004040204.TbWarehousestockVO arg0) throws java.lang.Exception;
	public java.util.List queryGeneralTVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2 ) throws java.lang.Exception;
	public void queryWarehousestock(java.util.List arg0) throws java.lang.Exception;
	public void updateWarehousestock(nc.vo.wds.w8004040204.TbWarehousestockVO arg0) throws java.lang.Exception;
	public void updateBdcargdocTray(java.lang.String arg0) throws java.lang.Exception;
	public void saveGeneralVO(nc.vo.wds.w8004040204.MyBillVO arg0) throws java.lang.Exception;
	public java.lang.String autoPickAction(java.lang.String arg0 ,nc.vo.wds.w8004040204.TbOutgeneralBVO[] arg1  ,java.lang.String arg2  ,java.lang.String arg3 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject deleteBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public java.lang.Object[] getNoutNum(java.lang.String arg0 ,java.lang.String arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD8004040602(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public java.lang.Object whs_processAction(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public int delTbGeneralBBVO(java.util.List arg0) throws nc.vo.pub.BusinessException;
	public int insertTbGeneralBBVO(nc.vo.wds.w8004040210.TbGeneralBBVO[] arg0) throws nc.vo.pub.BusinessException;
	public int updateBdCargdocTray(java.util.List arg0) throws nc.vo.pub.BusinessException;
	public void delAndInsertTbGeneralBBVO(java.util.List arg0 ,nc.vo.wds.w8004040210.TbGeneralBBVO[] arg1 ) throws nc.vo.pub.BusinessException;
	public void canceldelete8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,nc.vo.wds.w8004040210.TbGeneralHVO arg5 ) throws java.lang.Exception;
	public void pushsavesign8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,nc.vo.pub.AggregatedValueObject arg3  ,nc.vo.wds.w8004040210.TbGeneralHVO arg4 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject deleteBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public void saveFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException;
	public void deleteFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException;
	public void saveFyd(nc.vo.pub.AggregatedValueObject arg0) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public int saveAndCommit80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject deleteBD80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject prinDB80060208(nc.vo.wds.w80060406.TbFydnewVO arg0 ,nc.vo.wds.w80060604.SoSaleVO arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveAndCommit80060210(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.wds.w80060401.TbShipentryBVO[] queryShipentryBVO(java.lang.String arg0) throws nc.vo.pub.BusinessException;
	public nc.vo.pub.AggregatedValueObject deleteBD80060401(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.pub.AggregatedValueObject saveBD80060401(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception;
	public nc.vo.wds.w80060406.TbFydmxnewVO[] queryFydmxnewVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,java.lang.String arg4 ) throws nc.vo.pub.BusinessException ,java.text.ParseException;
	public void updateFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException;
	public void insertFyd(java.util.List arg0 ,java.util.List arg1  ,java.util.List arg2 ) throws java.lang.Exception;
	public void updateSosale(java.util.List arg0) throws java.lang.Exception;
}