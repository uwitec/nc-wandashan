package nc.ui.wds.w8004040214.buttun0214;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * 
 * @author Administrator
 *�Զ����
 */
public class ZdrkBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(142);
		btnVo.setBtnCode("Zdrk");
		btnVo.setBtnName("�Զ����");
		btnVo.setBtnChinaName("�Զ����");

		btnVo.setChildAry(new int[] { 
				
		});
//		zhf add  ���� ��ť״̬
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_REFADD,IBillOperate.OP_EDIT});

		return btnVo;
	}
}
