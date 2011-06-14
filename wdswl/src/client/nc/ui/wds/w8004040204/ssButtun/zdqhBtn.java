package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * 自动拣货
 * @author Administrator
 *
 */
public class zdqhBtn {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(122);
		btnVo.setBtnCode("zdqh");
		btnVo.setBtnName("自动拣货");
		btnVo.setBtnChinaName("自动拣货");
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ADD,
				IBillOperate.OP_REFADD, IBillOperate.OP_EDIT });
		return btnVo;
	}

}
