package nc.ui.wds.w8004040210.ssButtun;

import nc.vo.trade.button.ButtonVO;

public class CtAddBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(151);
		btnVo.setBtnCode("CtAdd");
		btnVo.setBtnName("辅助功能");
		btnVo.setBtnChinaName("辅助功能");

		btnVo.setChildAry(new int[] { 
				ISsButtun.Td,ISsButtun.Ai,ISsButtun.Vd
		});

		return btnVo;
	}
}
