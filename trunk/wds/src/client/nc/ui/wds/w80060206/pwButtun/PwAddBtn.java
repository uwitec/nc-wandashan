package nc.ui.wds.w80060206.pwButtun;

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

public class PwAddBtn {

	public ButtonVO getButtonVO(){
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(116);
		btnVo.setBtnCode("PwAdd");
		btnVo.setBtnName("�����˵�");
		btnVo.setBtnChinaName("�����˵�");
			
				
		btnVo.setChildAry(new int[]{
		      		                   
		                });
		                
		return btnVo;
	}

}
