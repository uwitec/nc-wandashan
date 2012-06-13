package nc.ui.wds.w80020206.buttun0206;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 签字确认
 * @author Administrator
 *
 */
public class QzqrBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(199);
		btnVo.setBtnCode("Qzqr");
		btnVo.setBtnName("签字确认");
		btnVo.setBtnChinaName("签字确认");
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
		btnVo.setBusinessStatus(new int[]{ IBillStatus.FREE });
		return btnVo;
	}
}
