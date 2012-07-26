package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;

public class WdsCloseButtonVO {

	public WdsCloseButtonVO() {
		super();
	}

	public ButtonVO getButton() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(ButtonCommon.btnclose);
		btnvo.setBtnCode(null);
		btnvo.setBtnName("¹Ø±Õ");
		btnvo.setBtnChinaName("¹Ø±Õ");
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NO_ADDANDEDIT });
		btnvo.setBusinessStatus(new int[] { IBillStatus.CHECKPASS });
		return btnvo;
	}
}
