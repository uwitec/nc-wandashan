package nc.ui.wds.w80060804.tcButtun;

import nc.ui.trade.base.IBillOperate;
import nc.vo.trade.button.ButtonVO;
import nc.vo.trade.pub.IBillStatus;

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

public class TcStopBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(104);
		btnVo.setBtnCode("TcStop");
		btnVo.setBtnName("��湫˾");
		btnVo.setBtnChinaName("��湫˾");
			
				
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
