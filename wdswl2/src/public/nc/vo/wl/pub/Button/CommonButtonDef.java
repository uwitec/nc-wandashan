package nc.vo.wl.pub.Button;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.wl.pub.ButtonCommon;

/**
 * �Զ��尴ťע��
 * @author zpm
 *
 */
public class CommonButtonDef {
	//�������鰴ť
	public ButtonVO getJoinUPButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ButtonCommon.joinup);
		btnVo.setBtnCode("joinup");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});	
		return btnVo;
	}
	//�������� ��ť
	
}
