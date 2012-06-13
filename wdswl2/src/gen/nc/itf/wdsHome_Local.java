package nc.itf;

/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public class wdsHome_Local extends nc.bs.mw.naming.HomeBase
    implements wdsHome{
   
	public wdsHome_Local() {
		super();
	}
	
	protected Object _createNewObject(){        
        return createEjb(nc.itf.wds_Local.class,nc.itf.wdsEjbBean.class);
    }

	public nc.itf.wdsEjbObject create() throws javax.ejb.CreateException{
	    	
	    if (isStatelessSessBean()) {
			synchronized (this) {
				Object o =
					ftechStateLessEjbObject(
						nc.itf.wds_Local.class);
				if (o != null) {
					return (nc.itf.wdsEjbObject) o;
				}
			}
		}
		nc.itf.wds_Local local = (nc.itf.wds_Local) _createNewObject();
		nc.itf.wdsEjbBean bean = (nc.itf.wdsEjbBean) local.getEjb();
		
		try {
            bean.ejbCreate();
        } catch(RuntimeException e) {
        	throw e;
        } catch (Exception e) {
        	nc.bs.logging.Logger.error("ejb create error", e);
            throw new javax.ejb.CreateException(e.getMessage());
        }
		return local;
	}	
}