package nc.vo.hg.pu.plan.temp;

import nc.vo.hg.pu.plan.month.PlanOtherBVO;

public class PlanOtherTempVO extends PlanOtherBVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  PlanInventoryVO[] invvos;

	public PlanInventoryVO[] getInvvos() {
		return invvos;
	}

	public void setInvvos(PlanInventoryVO[] invvos) {
		this.invvos = invvos;
	}

}
