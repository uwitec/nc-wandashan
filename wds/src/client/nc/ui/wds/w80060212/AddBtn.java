package nc.ui.wds.w80060212;

import nc.vo.trade.button.ButtonVO;

public class AddBtn {

	 public ButtonVO getButtonVO() {
			ButtonVO btnVo = new ButtonVO();
			btnVo.setBtnNo(179);
			btnVo.setBtnCode("Adbtn");
			btnVo.setBtnName("增加");
			btnVo.setBtnChinaName("增");

			btnVo.setChildAry(new int[] { 
					//有子按钮是将常量添加进来 ISsButtun.Add
			});

			return btnVo;
}
}
