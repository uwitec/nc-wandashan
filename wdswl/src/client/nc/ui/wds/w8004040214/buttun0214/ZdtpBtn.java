package nc.ui.wds.w8004040214.buttun0214;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
/**
 * 
 * @author Administrator
 *�ֶ����
 */
public class ZdtpBtn {
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(141);
		btnVo.setBtnCode("Zdtp");
		btnVo.setBtnName("�ֶ����");
		btnVo.setBtnChinaName("�ֶ����");

		btnVo.setChildAry(new int[] { 
				
		});
		
//		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT,IBillOperate.OP_REFADD});
//		zhf add  ���� ��ť״̬
		btnVo.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_REFADD,IBillOperate.OP_EDIT});

		return btnVo;
	}
}
