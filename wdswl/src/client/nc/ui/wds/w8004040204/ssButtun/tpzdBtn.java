package nc.ui.wds.w8004040204.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * �ֶ����
 * @author Administrator
 *
 */
public class tpzdBtn {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(121);
		btnVo.setBtnCode("tpzd");
		btnVo.setBtnName("�ֶ����");
		btnVo.setBtnChinaName("�ֶ����");
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ADD,
				IBillOperate.OP_REFADD, IBillOperate.OP_EDIT });
		return btnVo;
	}

}
