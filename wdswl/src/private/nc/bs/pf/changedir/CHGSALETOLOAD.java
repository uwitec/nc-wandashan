package nc.bs.pf.changedir;
/**
 * �������� �������ֱ�ҳǩ->װж�Ѻ��㵥
 * @author mlr
 *
 */
public class CHGSALETOLOAD extends nc.bs.pf.change.VOConversion{
	/**
	* ��ú������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return null;
	}
	/**
	* �����һ���������ȫ¼�����ơ�
	* @return java.lang.String[]
	*/
	public String getOtherClassName() {
		return null;
	}	
	@Override
	public String[] getField() {	
		return new String[]{				
		    "H_vmemo->H_vmemo",
		    "H_pk_wds_teamdoc_h->H_pk_wds_teamdoc_h",
		    "H_nloadprice->H_nloadprice"    
		};
	}

}
