package nc.ui.wds.w8004040210.ssButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;
import nc.ui.wds.w8004040210.ssButtun.ISsButtun;

/**
 * <b> 在此处简要描述此类的功能 </b><br>
 *
 * <p>
 *     在此处添加此类的描述信息
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
		btnVo.setBtnName("增加入库单");
		btnVo.setBtnChinaName("增加入库单");

		btnVo.setChildAry(new int[] { 
//				ISsButtun.sp, ISsButtun.pd

		});

		return btnVo;
	}

}
