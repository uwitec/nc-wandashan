package nc.ui.wds.w8004040214.buttun0214;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * 
 * @author Administrator
 *自动拣货
 */
public class ZdrkBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(142);
		btnVo.setBtnCode("Zdrk");
		btnVo.setBtnName("自动拣货");
		btnVo.setBtnChinaName("自动拣货");

		btnVo.setChildAry(new int[] { 
				
		});
//		zhf add  调整 按钮状态
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_REFADD,IBillOperate.OP_EDIT});

		return btnVo;
	}
}
