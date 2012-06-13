package nc.bs.pf.changedir;
/**
 * 此类用于 出入库多字表页签->装卸费核算单
 * @author mlr
 *
 */
public class CHGSALETOLOAD extends nc.bs.pf.change.VOConversion{
	/**
	* 获得后续类的全录经名称。
	* @return java.lang.String[]
	*/
	public String getAfterClassName() {
		return null;
	}
	/**
	* 获得另一个后续类的全录径名称。
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
