package nc.ui.wds.w8004040214.buttun0214;

import nc.vo.trade.button.ButtonVO;

public class FzgnBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(140);
		btnVo.setBtnCode("Fzgn");
		btnVo.setBtnName("��������");
		btnVo.setBtnChinaName("��������");

		btnVo.setChildAry(new int[] { 
				ISsButtun.Zdtp,ISsButtun.Zdrk,ISsButtun.Ckmx
		});

		return btnVo;
	}
}
