package nc.ui.wds.w8006080602.cfButtun;

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

public class CfStopBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(107);
		btnVo.setBtnCode("CfStop");
		btnVo.setBtnName("��泵��");
		btnVo.setBtnChinaName("��泵��");
			
				
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
