package nc.ui.wds.w8004040214.buttun0214;

import nc.vo.trade.button.ButtonVO;

public class ZjBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(144);
		btnVo.setBtnCode("Zj");
		btnVo.setBtnName("增加");
		btnVo.setBtnChinaName("增加");

		btnVo.setChildAry(new int[] { 
				ISsButtun.Zzdj,ISsButtun.Fydj,ISsButtun.Hwtz
		});

		return btnVo;
	}
}
