package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * �Զ����
 * @author Administrator
 *
 */
public class zdqhBtn {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(122);
		btnVo.setBtnCode("zdqh");
		btnVo.setBtnName("�Զ����");
		btnVo.setBtnChinaName("�Զ����");
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ADD,
				IBillOperate.OP_REFADD, IBillOperate.OP_EDIT });
		return btnVo;
	}

}
