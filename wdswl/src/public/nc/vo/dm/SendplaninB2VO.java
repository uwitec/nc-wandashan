package nc.vo.dm;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class SendplaninB2VO extends SuperVO{

	 /**子表主键 */
    public String pk_sendplanin_b;
    /**父键 */
    public String pk_sendplanin;
    /**主键 */
    public String pk_sendplanin_b2;
    /**来源主表主键 */
    public String pk_sorce_sendplanin;
    /**来源子表主键 */
    public String pk_sorce_sendplanin_b;
    /**来源主数量 */
    public UFDouble sorce_ndealnum;
    /**来源辅数量 */
    
    public UFDouble sorce_nassdealnum;
	public String getPk_sendplanin_b() {
		return pk_sendplanin_b;
	}

	public void setPk_sendplanin_b(String pk_sendplanin_b) {
		this.pk_sendplanin_b = pk_sendplanin_b;
	}

	public String getPk_sendplanin() {
		return pk_sendplanin;
	}

	public void setPk_sendplanin(String pk_sendplanin) {
		this.pk_sendplanin = pk_sendplanin;
	}

	public String getPk_sendplanin_b2() {
		return pk_sendplanin_b2;
	}

	public void setPk_sendplanin_b2(String pk_sendplanin_b2) {
		this.pk_sendplanin_b2 = pk_sendplanin_b2;
	}

	public String getPk_sorce_sendplanin() {
		return pk_sorce_sendplanin;
	}

	public void setPk_sorce_sendplanin(String pk_sorce_sendplanin) {
		this.pk_sorce_sendplanin = pk_sorce_sendplanin;
	}

	public String getPk_sorce_sendplanin_b() {
		return pk_sorce_sendplanin_b;
	}

	public void setPk_sorce_sendplanin_b(String pk_sorce_sendplanin_b) {
		this.pk_sorce_sendplanin_b = pk_sorce_sendplanin_b;
	}

	public UFDouble getSorce_ndealnum() {
		return sorce_ndealnum;
	}

	public void setSorce_ndealnum(UFDouble sorce_ndealnum) {
		this.sorce_ndealnum = sorce_ndealnum;
	}

	public UFDouble getSorce_nassdealnum() {
		return sorce_nassdealnum;
	}

	public void setSorce_nassdealnum(UFDouble sorce_nassdealnum) {
		this.sorce_nassdealnum = sorce_nassdealnum;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sendplanin_b2";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_sendplanin";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "wds_sendplanin_b2";
	}

}
