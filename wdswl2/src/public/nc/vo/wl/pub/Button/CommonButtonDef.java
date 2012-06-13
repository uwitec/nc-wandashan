package nc.vo.wl.pub.Button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.ButtonCommon;

/**
 * 自定义按钮注册
 * @author zpm
 *
 */
public class CommonButtonDef {
	//定义联查按钮
	public ButtonVO getJoinUPButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ButtonCommon.joinup);
		btnVo.setBtnCode("joinup");
		btnVo.setBtnName("联查");
		btnVo.setBtnChinaName("联查");
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
		return btnVo;
	}
	//定义其他 按钮
	
}
