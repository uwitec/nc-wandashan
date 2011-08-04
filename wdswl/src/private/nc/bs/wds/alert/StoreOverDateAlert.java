package nc.bs.wds.alert;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
/**
 * @author mlr
 * 对大日期库存的预警类
 */
public class StoreOverDateAlert implements IBusinessPlugin{
	public int getImplmentsType() {	
		return IBusinessPlugin.IMPLEMENT_RETURNFORMATMSG;
	}
	public Key[] getKeys() {		
		return null;
	}	
	public StoreOverDateAlert() {
		super();
	}
	public String getTypeDescription() {		
		return null;
	}
	public String getTypeName() {		
		return null;
	}
	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {	
		return new StoreAlertMessage(corpPK);
	}
	public String implementReturnMessage(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {		
		return null;
	}
	public Object implementReturnObject(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {		
		return null;
	}
	public boolean implementWriteFile(Key[] keys, String fileName,
			String corpPK, UFDate clientLoginDate) throws BusinessException {		
		return false;
	}
}
