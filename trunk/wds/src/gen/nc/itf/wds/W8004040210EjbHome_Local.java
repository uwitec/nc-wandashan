package nc.itf.wds;

/**
 * business service ejb wrapper 
 * Created by EJBGenerator
 * based on velocity technology
 */

public class W8004040210EjbHome_Local extends nc.bs.mw.naming.HomeBase
    implements W8004040210EjbHome{
   
	public W8004040210EjbHome_Local() {
		super();
	}
	
	protected Object _createNewObject(){        
        return createEjb(nc.itf.wds.W8004040210Ejb_Local.class,nc.itf.wds.W8004040210EjbEjbBean.class);
    }

	public nc.itf.wds.W8004040210EjbEjbObject create() throws javax.ejb.CreateException{
	    	
	    if (isStatelessSessBean()) {
			synchronized (this) {
				Object o =
					ftechStateLessEjbObject(
						nc.itf.wds.W8004040210Ejb_Local.class);
				if (o != null) {
					return (nc.itf.wds.W8004040210EjbEjbObject) o;
				}
			}
		}
		nc.itf.wds.W8004040210Ejb_Local local = (nc.itf.wds.W8004040210Ejb_Local) _createNewObject();
		nc.itf.wds.W8004040210EjbEjbBean bean = (nc.itf.wds.W8004040210EjbEjbBean) local.getEjb();
		
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