package nc.ui.wds.w8004040214.buttun0214;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * 
 * @author Administrator
 *手动拣货
 */
public class ZdtpBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(141);
		btnVo.setBtnCode("Zdtp");
		btnVo.setBtnName("手动拣货");
		btnVo.setBtnChinaName("手动拣货");

		btnVo.setChildAry(new int[] { 
				
		});
		
//		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT,IBillOperate.OP_REFADD});
//		zhf add  调整 按钮状态
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_REFADD,IBillOperate.OP_EDIT});

		return btnVo;
	}
}
