package nc.ui.ncblk.yjcb.yjcbjs;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

/**
 * 按钮工具类
 * 
 * @author heyq
 * 
 */

/*
 * 
 * 卓竞劲修改
 * 2010-05-13
 * 
 */

public class PrivateButtonUtils {

	// 计算按钮
	public static ButtonVO getJsButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IPrivateButton.SYSTEM_BUTON_JS);
		btnVo.setBtnName("计算");
		btnVo.setBtnChinaName("计算");
		btnVo.setHintStr("计算");
		btnVo.setBtnCode("js");
//		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ALL });
		btnVo.setBusinessStatus(new int[] {});
		btnVo.setExtendStatus(new int[] {});
		return btnVo;
	}

	// 确认按钮
	public static ButtonVO getQrButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IPrivateButton.SYSTEM_BUTON_QR);
		btnVo.setBtnName("确认");
		btnVo.setBtnChinaName("确认");
		btnVo.setHintStr("确认");
		btnVo.setBtnCode("qr");
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ALL });
		btnVo.setBusinessStatus(new int[] {});
		btnVo.setExtendStatus(new int[] {});
		return btnVo;
	}
	
//修改内容
   public static ButtonVO getTzButton()
   {
	   ButtonVO btnVO = new ButtonVO();
	   btnVO.setBtnNo(IPrivateButton.SYSTEM_BUTTON_TZ);
	   btnVO.setBtnName("调整金额");
	   btnVO.setBtnChinaName("调整金额");
	   btnVO.setHintStr("调整销售成本金额");
	   btnVO.setBtnCode("tz");
	   btnVO.setOperateStatus(new int[] { IBillOperate.OP_ALL });
	   btnVO.setBusinessStatus(new int[] {});
	   btnVO.setExtendStatus(new int[] {});
	   return btnVO;
   }
}