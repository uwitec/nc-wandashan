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
		
//		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT,IBillOperate.OP_REFADD});
//		zhf add  ���� ��ť״̬
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_REFADD});

		return btnVo;
	}
}
