
import javax.naming.NamingException;
import nc.bs.logging.Logger;
import nc.bs.pub.DataManageObject;
import nc.uif.pub.exception.UifException;
import nc.vo.pub.BusinessException;
import nc.vo.scm.sourcebill.LightBillVO;
import nc.vo.wl.pub.IBillFinder2;
/**
 * 用于联查的DMO类
 * @author mlr
 */
public class LinkQueryDMO extends DataManageObject {
	public LinkQueryDMO() throws NamingException {
		super();
	}
	public LightBillVO queryBillGraph(
			String billFinderClassName,
			String id,
			String type)
			throws UifException {
			try
			{
				IBillFinder2 billFinder =
					(IBillFinder2) Class.forName(billFinderClassName).newInstance();
				return billFinder.queryBillGraph(id, type);
			}
			catch (BusinessException be)
			{
				Logger.error(be.getMessage(), be);
				throw new UifException(be.getMessage(), be);
			}

			catch (Exception ex)
			{
				Logger.error(ex.getMessage(), ex);
				throw new UifException(ex.getMessage(), ex);
			}
		}

}
