package nc.ui.wds.w8004040204.ssButtun;

import nc.vo.trade.button.ButtonVO;

/**
 * ��������
 * @author Administrator
 *
 */

public class fzgnBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ISsButtun.fzgn);
		btnVo.setBtnCode("fzgn");
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("��������");
		btnVo.setChildAry(new int[]{ISsButtun.tpzd,ISsButtun.zdqh, ISsButtun.ckmx});
		return btnVo;
	}

}
