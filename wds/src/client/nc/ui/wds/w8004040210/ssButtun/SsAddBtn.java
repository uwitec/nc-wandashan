package nc.ui.wds.w8004040210.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.wds.w8004040210.ssButtun.ISsButtun;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b><br>
 *
 * <p>
 *     �ڴ˴���Ӵ����������Ϣ
 * </p>
 *
 *
 * @author authorName
 * @version tempProject version
 */

public class SsAddBtn {

	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(117);
		btnVo.setBtnCode("SsAdd");
		btnVo.setBtnName("������ⵥ");
		btnVo.setBtnChinaName("������ⵥ");

		btnVo.setChildAry(new int[] { 
//				ISsButtun.sp, ISsButtun.pd

		});

		return btnVo;
	}

}
