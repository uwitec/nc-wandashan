package nc.ui.wds.w80060212;

import nc.vo.trade.button.ButtonVO;

public class AddBtn {

	 public ButtonVO getButtonVO() {
			ButtonVO btnVo = new ButtonVO();
			btnVo.setBtnNo(179);
			btnVo.setBtnCode("Adbtn");
			btnVo.setBtnName("����");
			btnVo.setBtnChinaName("��");

			btnVo.setChildAry(new int[] { 
					//���Ӱ�ť�ǽ�������ӽ��� ISsButtun.Add
			});

			return btnVo;
}
}
