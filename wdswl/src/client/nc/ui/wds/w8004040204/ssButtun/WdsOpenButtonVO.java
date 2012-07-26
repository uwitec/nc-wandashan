package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wl.pub.ButtonCommon;

public class WdsOpenButtonVO {

	public WdsOpenButtonVO() {
		super();
	}

	public ButtonVO getButton() {
		ButtonVO btnvo = new ButtonVO();
		btnvo.setBtnNo(ButtonCommon.btnopen);
		btnvo.setBtnCode(null);
		btnvo.setBtnName("打开");
		btnvo.setBtnChinaName("打开");
		btnvo.setOperateStatus(new int[] { IBillOperate.OP_NO_ADDANDEDIT });
		btnvo.setBusinessStatus(new int[] { IBillStatus.CHECKPASS });
		return btnvo;
	}
}
