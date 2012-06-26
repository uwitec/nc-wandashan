package nc.bs.wds.alert;
import nc.bs.pub.pa.IBusinessPlugin;
import nc.bs.pub.pa.IBusinessPlugin2;
import nc.bs.pub.pa.html.IAlertMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.pa.Key;
/**
 * @author mlr
 * �Դ����ڿ���Ԥ����
 */
public class StoreOverDateAlert implements IBusinessPlugin2{
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
	public String[] implementReturnMessage(Key[] keys, Object currEnvVO,
			UFDate clientLoginDate) throws BusinessException {
		return null;
	}
	public IAlertMessage[] implementReturnFormatMsg(Key[] keys,
			Object currEnvVO, UFDate clientLoginDate) throws BusinessException {
		nc.vo.pub.pa.CurrEnvVO vo = (nc.vo.pub.pa.CurrEnvVO) currEnvVO;
		String userid = vo.getPk_user();
		String pkcorp = vo.getPk_corp();
		return new IAlertMessage[]{new StoreAlertMessage(pkcorp,userid)};
	}
	public Object implementReturnObject(Key[] keys, Object currEnvVO,
			UFDate clientLoginDate) throws BusinessException {
		
		return null;
	}
	public boolean implementWriteFile(Key[] keys, String fileName,
			Object currEnvVO, UFDate clientLoginDate) throws BusinessException {
		
		return false;
	}
	public IAlertMessage implementReturnFormatMsg(Key[] keys, String corpPK,
			UFDate clientLoginDate) throws BusinessException {
		
		return null;
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