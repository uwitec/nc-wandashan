package nc.ui.ncblk.yjcb.yjcbjs;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;

/**
 * ��ť������
 * 
 * @author heyq
 * 
 */

/*
 * 
 * ׿�����޸�
 * 2010-05-13
 * 
 */

public class PrivateButtonUtils {

	// ���㰴ť
	public static ButtonVO getJsButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IPrivateButton.SYSTEM_BUTON_JS);
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");
		btnVo.setHintStr("����");
		btnVo.setBtnCode("js");
//		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ALL });
		btnVo.setBusinessStatus(new int[] {});
		btnVo.setExtendStatus(new int[] {});
		return btnVo;
	}

	// ȷ�ϰ�ť
	public static ButtonVO getQrButton() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(IPrivateButton.SYSTEM_BUTON_QR);
		btnVo.setBtnName("ȷ��");
		btnVo.setBtnChinaName("ȷ��");
		btnVo.setHintStr("ȷ��");
		btnVo.setBtnCode("qr");
		btnVo.setOperateStatus(new int[] { IBillOperate.OP_ALL });
		btnVo.setBusinessStatus(new int[] {});
		btnVo.setExtendStatus(new int[] {});
		return btnVo;
	}
	
//�޸�����
   public static ButtonVO getTzButton()
   {
	   ButtonVO btnVO = new ButtonVO();
	   btnVO.setBtnNo(IPrivateButton.SYSTEM_BUTTON_TZ);
	   btnVO.setBtnName("�������");
	   btnVO.setBtnChinaName("�������");
	   btnVO.setHintStr("�������۳ɱ����");
	   btnVO.setBtnCode("tz");
	   btnVO.setOperateStatus(new int[] { IBillOperate.OP_ALL });
	   btnVO.setBusinessStatus(new int[] {});
	   btnVO.setExtendStatus(new int[] {});
	   return btnVO;
   }
}