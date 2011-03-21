package nc.itf.wds;


/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public class W8004040210Ejb_Local extends nc.bs.mw.naming.BeanBase
    implements nc.itf.wds.W8004040210EjbEjbObject {
   
  public W8004040210Ejb_Local() {
	super();
  }

  private nc.itf.wds.W8004040210EjbEjbBean _getBeanObject() throws java.rmi.RemoteException {
    return (nc.itf.wds.W8004040210EjbEjbBean) getEjb();
  }

  public nc.vo.pub.AggregatedValueObject saveBD80020202(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80020202$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD80020202(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80020202$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public java.lang.Object whs_processAction80020206(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_whs_processAction80020206$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$Object_arg5    );
    Exception er = null;
	java.lang.Object o = null;
	try{
				o = (java.lang.Object)_getBeanObject().whs_processAction80020206(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_whs_processAction80020206$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$Object_arg5, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public void deleteGeneralTVO(java.util.List arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteGeneralTVO$List_arg0    );
    Exception er = null;
	try{
				_getBeanObject().deleteGeneralTVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteGeneralTVO$List_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void insertGeneralTVO(java.util.List arg0 ,java.util.List arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertGeneralTVO$List_arg0$List_arg1    );
    Exception er = null;
	try{
				_getBeanObject().insertGeneralTVO(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertGeneralTVO$List_arg0$List_arg1, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void insertWarehousestock(nc.vo.wds.w8004040204.TbWarehousestockVO arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertWarehousestock$TbWarehousestockVO_arg0    );
    Exception er = null;
	try{
				_getBeanObject().insertWarehousestock(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertWarehousestock$TbWarehousestockVO_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public java.util.List queryGeneralTVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryGeneralTVO$String_arg0$String_arg1$String_arg2    );
    Exception er = null;
	java.util.List o = null;
	try{
				o = (java.util.List)_getBeanObject().queryGeneralTVO(arg0 ,arg1  ,arg2 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryGeneralTVO$String_arg0$String_arg1$String_arg2, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public void queryWarehousestock(java.util.List arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryWarehousestock$List_arg0    );
    Exception er = null;
	try{
				_getBeanObject().queryWarehousestock(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryWarehousestock$List_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void updateWarehousestock(nc.vo.wds.w8004040204.TbWarehousestockVO arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateWarehousestock$TbWarehousestockVO_arg0    );
    Exception er = null;
	try{
				_getBeanObject().updateWarehousestock(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateWarehousestock$TbWarehousestockVO_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void updateBdcargdocTray(java.lang.String arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateBdcargdocTray$String_arg0    );
    Exception er = null;
	try{
				_getBeanObject().updateBdcargdocTray(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateBdcargdocTray$String_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void saveGeneralVO(nc.vo.wds.w8004040204.MyBillVO arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveGeneralVO$MyBillVO_arg0    );
    Exception er = null;
	try{
				_getBeanObject().saveGeneralVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveGeneralVO$MyBillVO_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public java.lang.String autoPickAction(java.lang.String arg0 ,nc.vo.wds.w8004040204.TbOutgeneralBVO[] arg1  ,java.lang.String arg2  ,java.lang.String arg3 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_autoPickAction$String_arg0$TbOutgeneralBVOS_arg1$String_arg2$String_arg3    );
    Exception er = null;
	java.lang.String o = null;
	try{
				o = (java.lang.String)_getBeanObject().autoPickAction(arg0 ,arg1  ,arg2  ,arg3 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_autoPickAction$String_arg0$TbOutgeneralBVOS_arg1$String_arg2$String_arg3, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject deleteBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD8004040204$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().deleteBD8004040204(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD8004040204$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject saveBD8004040204(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD8004040204$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD8004040204(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD8004040204$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public java.lang.Object[] getNoutNum(java.lang.String arg0 ,java.lang.String arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_getNoutNum$String_arg0$String_arg1    );
    Exception er = null;
	java.lang.Object[] o = null;
	try{
				o = (java.lang.Object[])_getBeanObject().getNoutNum(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_getNoutNum$String_arg0$String_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject saveBD8004040602(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD8004040602$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD8004040602(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD8004040602$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public java.lang.Object whs_processAction(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,java.lang.Object arg5 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_whs_processAction$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$Object_arg5    );
    Exception er = null;
	java.lang.Object o = null;
	try{
				o = (java.lang.Object)_getBeanObject().whs_processAction(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_whs_processAction$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$Object_arg5, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject saveBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public int delTbGeneralBBVO(java.util.List arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_delTbGeneralBBVO$List_arg0    );
    Exception er = null;
		int o = 0;
	try{
				o = (int)_getBeanObject().delTbGeneralBBVO(arg0);			
	}
	catch(Exception e){
		er = e;
	}  catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_delTbGeneralBBVO$List_arg0, er);
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public int insertTbGeneralBBVO(nc.vo.wds.w8004040210.TbGeneralBBVO[] arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertTbGeneralBBVO$TbGeneralBBVOS_arg0    );
    Exception er = null;
		int o = 0;
	try{
				o = (int)_getBeanObject().insertTbGeneralBBVO(arg0);			
	}
	catch(Exception e){
		er = e;
	}  catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertTbGeneralBBVO$TbGeneralBBVOS_arg0, er);
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public int updateBdCargdocTray(java.util.List arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateBdCargdocTray$List_arg0    );
    Exception er = null;
		int o = 0;
	try{
				o = (int)_getBeanObject().updateBdCargdocTray(arg0);			
	}
	catch(Exception e){
		er = e;
	}  catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateBdCargdocTray$List_arg0, er);
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public void delAndInsertTbGeneralBBVO(java.util.List arg0 ,nc.vo.wds.w8004040210.TbGeneralBBVO[] arg1 ) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_delAndInsertTbGeneralBBVO$List_arg0$TbGeneralBBVOS_arg1    );
    Exception er = null;
	try{
				_getBeanObject().delAndInsertTbGeneralBBVO(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_delAndInsertTbGeneralBBVO$List_arg0$TbGeneralBBVOS_arg1, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void canceldelete8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,nc.vo.pub.AggregatedValueObject arg4  ,nc.vo.wds.w8004040210.TbGeneralHVO arg5 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_canceldelete8004040210$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$TbGeneralHVO_arg5    );
    Exception er = null;
	try{
				_getBeanObject().canceldelete8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4  ,arg5 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_canceldelete8004040210$String_arg0$String_arg1$String_arg2$String_arg3$AggregatedValueObject_arg4$TbGeneralHVO_arg5, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void pushsavesign8004040210(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,nc.vo.pub.AggregatedValueObject arg3  ,nc.vo.wds.w8004040210.TbGeneralHVO arg4 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_pushsavesign8004040210$String_arg0$String_arg1$String_arg2$AggregatedValueObject_arg3$TbGeneralHVO_arg4    );
    Exception er = null;
	try{
				_getBeanObject().pushsavesign8004040210(arg0 ,arg1  ,arg2  ,arg3  ,arg4 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_pushsavesign8004040210$String_arg0$String_arg1$String_arg2$AggregatedValueObject_arg3$TbGeneralHVO_arg4, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public nc.vo.pub.AggregatedValueObject deleteBD(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().deleteBD(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public void saveFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveFydVO$AggregatedValueObject_arg0    );
    Exception er = null;
	try{
				_getBeanObject().saveFydVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveFydVO$AggregatedValueObject_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void deleteFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteFydVO$AggregatedValueObject_arg0    );
    Exception er = null;
	try{
				_getBeanObject().deleteFydVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteFydVO$AggregatedValueObject_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void saveFyd(nc.vo.pub.AggregatedValueObject arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveFyd$AggregatedValueObject_arg0    );
    Exception er = null;
	try{
				_getBeanObject().saveFyd(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveFyd$AggregatedValueObject_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public nc.vo.pub.AggregatedValueObject saveBD80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80060208$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD80060208(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80060208$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public int saveAndCommit80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveAndCommit80060208$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
		int o = 0;
	try{
				o = (int)_getBeanObject().saveAndCommit80060208(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	}  catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveAndCommit80060208$AggregatedValueObject_arg0$Object_arg1, er);
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject deleteBD80060208(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD80060208$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().deleteBD80060208(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD80060208$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject prinDB80060208(nc.vo.wds.w80060406.TbFydnewVO arg0 ,nc.vo.wds.w80060604.SoSaleVO arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_prinDB80060208$TbFydnewVO_arg0$SoSaleVO_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().prinDB80060208(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_prinDB80060208$TbFydnewVO_arg0$SoSaleVO_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject saveAndCommit80060210(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveAndCommit80060210$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveAndCommit80060210(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveAndCommit80060210$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.wds.w80060401.TbShipentryBVO[] queryShipentryBVO(java.lang.String arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryShipentryBVO$String_arg0    );
    Exception er = null;
	nc.vo.wds.w80060401.TbShipentryBVO[] o = null;
	try{
				o = (nc.vo.wds.w80060401.TbShipentryBVO[])_getBeanObject().queryShipentryBVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryShipentryBVO$String_arg0, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject deleteBD80060401(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD80060401$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().deleteBD80060401(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_deleteBD80060401$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.pub.AggregatedValueObject saveBD80060401(nc.vo.pub.AggregatedValueObject arg0 ,java.lang.Object arg1 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80060401$AggregatedValueObject_arg0$Object_arg1    );
    Exception er = null;
	nc.vo.pub.AggregatedValueObject o = null;
	try{
				o = (nc.vo.pub.AggregatedValueObject)_getBeanObject().saveBD80060401(arg0 ,arg1 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_saveBD80060401$AggregatedValueObject_arg0$Object_arg1, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public nc.vo.wds.w80060406.TbFydmxnewVO[] queryFydmxnewVO(java.lang.String arg0 ,java.lang.String arg1  ,java.lang.String arg2  ,java.lang.String arg3  ,java.lang.String arg4 ) throws nc.vo.pub.BusinessException ,java.text.ParseException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryFydmxnewVO$String_arg0$String_arg1$String_arg2$String_arg3$String_arg4    );
    Exception er = null;
	nc.vo.wds.w80060406.TbFydmxnewVO[] o = null;
	try{
				o = (nc.vo.wds.w80060406.TbFydmxnewVO[])_getBeanObject().queryFydmxnewVO(arg0 ,arg1  ,arg2  ,arg3  ,arg4 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);	  
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_queryFydmxnewVO$String_arg0$String_arg1$String_arg2$String_arg3$String_arg4, er);
 	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    
    	if( er instanceof java.text.ParseException ){
    		throw (java.text.ParseException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
	return o;
 }
  public void updateFydVO(nc.vo.pub.AggregatedValueObject arg0) throws nc.vo.pub.BusinessException{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateFydVO$AggregatedValueObject_arg0    );
    Exception er = null;
	try{
				_getBeanObject().updateFydVO(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateFydVO$AggregatedValueObject_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof nc.vo.pub.BusinessException ){
    		throw (nc.vo.pub.BusinessException)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void insertFyd(java.util.List arg0 ,java.util.List arg1  ,java.util.List arg2 ) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertFyd$List_arg0$List_arg1$List_arg2    );
    Exception er = null;
	try{
				_getBeanObject().insertFyd(arg0 ,arg1  ,arg2 );			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_insertFyd$List_arg0$List_arg1$List_arg2, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
  public void updateSosale(java.util.List arg0) throws java.lang.Exception{
    beforeCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateSosale$List_arg0    );
    Exception er = null;
	try{
				_getBeanObject().updateSosale(arg0);			
	}
	catch(Exception e){
		er = e;
	} catch(Throwable thr) {
		  nc.bs.logging.Logger.error("HGY:Unexpected error, tx will rb", thr);
		  er = new nc.bs.framework.exception.FrameworkEJBException("Fatal unknown error", thr);
	}
	try{
		afterCallMethod(nc.itf.wds.W8004040210Ejb_Method_Const_Local.Method_updateSosale$List_arg0, er);    	
	}
	catch(java.rmi.RemoteException remoteException){
		nc.bs.logging.Logger.error("HGY: Unexpected error when call afterCallMethod",  remoteException);
	} finally {
			}
	if( null != er ){
    	if( er instanceof java.lang.Exception ){
    		throw (java.lang.Exception)er;
    	}    

	if(er instanceof RuntimeException)
		throw (RuntimeException)er;
	else	
		throw new nc.bs.framework.exception.FrameworkEJBException(er.getMessage(),er);
	}
 }
}