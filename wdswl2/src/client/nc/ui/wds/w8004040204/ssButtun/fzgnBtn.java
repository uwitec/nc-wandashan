package nc.ui.wds.w8004040204.ssButtun;

import nc.vo.trade.button.ButtonVO;

/**
 * 辅助功能
 * @author Administrator
 *
 */

public class fzgnBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ISsButtun.fzgn);
		btnVo.setBtnCode("fzgn");
		btnVo.setBtnName("辅助功能");
		btnVo.setBtnChinaName("辅助功能");
		btnVo.setChildAry(new int[]{ISsButtun.tpzd,ISsButtun.zdqh, ISsButtun.ckmx});
		return btnVo;
	}

}
