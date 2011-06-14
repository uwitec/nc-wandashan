package nc.ui.wds.w80020206.buttun0206;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 取消签字
 * @author Administrator
 *
 */
public class QxqzBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(198);
		btnVo.setBtnCode("Qxqz");
		btnVo.setBtnName("取消签字");
		btnVo.setBtnChinaName("取消签字");
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
		btnVo.setBusinessStatus(new int[]{  IBillStatus.CHECKPASS });
		return btnVo;
	}
}
